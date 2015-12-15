package com.platform.iot.api.producer;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.message.MessageDispatcher;
import com.platform.iot.api.message.server.PriceMessage;
import com.platform.iot.api.message.server.ServerMessage;
import com.platform.iot.api.monitoring.MonitoringServiceLocator;
import com.platform.iot.api.storage.TopicService;
import com.platform.iot.producer.EventLogger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
public class EventDispatcher implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(EventLogger.class);

    @Autowired
    private EventContainer eventContainer;

    @Override
    public void run() {
        Collection<Event> events = eventContainer.getEvents();
        if (events.isEmpty()) {
            return;
        }
        JSONObject jsonObject = null;
        if (events.size() == 1) {
            jsonObject = events.iterator().next().getJsonObject();
            try {
                handleTopicVariationFromProducer(jsonObject);
            } catch (Exception e) {
                logger.error("error sending event to listening users", e);
            }
        } else {
            EventList eventList = new EventList(events);
            jsonObject = eventList.getJsonObject();
            JSONArray jsonArray = (JSONArray) jsonObject.get("eventList");
            Iterator iterator = jsonArray.iterator();
            if (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                try {
                    handleTopicVariationFromProducer(next);
                } catch (Exception e) {
                    logger.error("error sending event to listening users", e);
                }
            }
        }
    }

    public void handleTopicVariationFromProducer(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        long producerId = jsonObject.getLong("producerId");
        Topic topic = MemoryStorage.INSTANCE.getTopicByCodeAndProducerId(code, producerId);
        if (topic == null) {
            TopicService topicService = TopicDistributionApplication.context.getBean(TopicService.class);
            topic = topicService.findByProducerIdAndCode(producerId, code);
            if (topic == null) {
                try {
                    topic = new Topic(code, jsonObject.getString("name"), jsonObject.getDouble("price"), true, producerId);
                    topic = topicService.create(topic);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        PriceMessage priceMessage = new PriceMessage();
        priceMessage.setCode(code);
        priceMessage.setPrice(jsonObject.getDouble("price"));
        priceMessage.setChanged(jsonObject.getDouble("changed"));
        ServerMessage message = (ServerMessage) priceMessage;
        logger.info("Received from producer with Id " + producerId + " : " + message.toString());
        MonitoringServiceLocator.getInstance().getServerMonitor().updateVariationsCount();
        if (producerId == TopicDistributionApplication.DEFAULT_PRODUCER_ID) {
            MessageDispatcher.sendMessageToLoggedUsers(priceMessage, topic);
        }
    }


}
