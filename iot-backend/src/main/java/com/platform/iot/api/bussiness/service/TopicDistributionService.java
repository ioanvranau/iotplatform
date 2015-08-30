package com.platform.iot.api.bussiness.service;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.message.MessageDispatcher;
import com.platform.iot.api.message.client.TopicSubscribeMessage;
import com.platform.iot.api.message.client.TopicUnsubscribeMessage;
import com.platform.iot.api.message.server.PriceMessage;
import com.platform.iot.api.producer.Event;
import com.platform.iot.producer.EventLogger;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Magda Gherasim
 */
public class TopicDistributionService {
    private static final Logger logger = LoggerFactory.getLogger(TopicDistributionService.class);
    private static EventLogger eventLogger = TopicDistributionApplication.context.getBean(EventLogger.class);

    public void handleMessage(User user) {
        if (user.getTopics() != null) {
            for (Topic topic : user.getTopics()) {

                for (int i = 0; i < 3; i++) {
                    double variance = Math.random();
                    PriceMessage priceMessage = new PriceMessage();
                    priceMessage.setCode(topic.getCode());
                    double newPrice = topic.getPrice() * (variance + 1);
                    priceMessage.setChanged(newPrice - topic.getPrice());
                    priceMessage.setPrice(newPrice);
//                    priceMessage.setPrice(1001);

                    JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(priceMessage.toJSON());
                    eventLogger.sendToQueue(new Event(jsonObject));
                }
            }
        } else {
            logger.error("Null topic group");
        }
    }

    public void subscribeToTopic(TopicSubscribeMessage message) {
        User user = MemoryStorage.INSTANCE.getUserByToken(message.getToken());
        if (user != null) {
            Topic subscribedTopic = message.getSubscribedTopic();
            subscribedTopic.setDefaultSubscribed(true);
            user.getTopics().add(subscribedTopic);
            MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.TopicSubscribeMessage(subscribedTopic.getCode()));
        }
    }

    public void unsubscribeFromTopic(TopicUnsubscribeMessage message) {
        User user = MemoryStorage.INSTANCE.getUserByToken(message.getToken());
        if (user != null) {
            Topic unsubscribedTopic = message.getUnsubscribedTopic();
            unsubscribedTopic.setDefaultSubscribed(false);
            user.getTopics().remove(unsubscribedTopic);
            MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.TopicUnsubscribeMessage(unsubscribedTopic.getCode()));
        }
    }

    public void subscribeDefaultTopics(User user){
        logger.info("Subscribe default topics start");
        for(Topic topic : MemoryStorage.INSTANCE.getTopicsByProducerId(user.getProducerId())){
            if (topic.isDefaultSubscribed()){
                user.getTopics().add(topic);
            }
        }
        logger.info("Subscribe default topics ended");
    }
}
