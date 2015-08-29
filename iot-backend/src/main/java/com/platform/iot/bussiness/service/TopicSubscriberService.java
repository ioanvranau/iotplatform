package com.platform.iot.bussiness.service;

import com.platform.iot.bussiness.MemoryStorage;
import com.platform.iot.bussiness.model.Topic;
import com.platform.iot.bussiness.model.User;
import com.platform.iot.message.MessageDispatcher;
import com.platform.iot.message.client.RegisterMessage;
import com.platform.iot.message.client.TopicSubscribeMessage;
import com.platform.iot.message.server.PriceMessage;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Magda Gherasim
 */
public class TopicSubscriberService {
    private static final Logger logger = LoggerFactory.getLogger(TopicSubscriberService.class);
//    private static EventLogger eventLogger = HttpStreamingApplication.context.getBean(EventLogger.class);

    public void assignUserToGroup(RegisterMessage registerMessage) {
    }

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
//                    eventLogger.sendToQueue(new Event(jsonObject));
                }
            }
        } else {
            logger.error("Null topic group");
        }
    }

    public void subscribeToTopic(TopicSubscribeMessage message) {
        User user = MemoryStorage.INSTANCE.getUserByToken(message.getToken());
        user.getTopics().add(message.getSubscribedTopic());
        MessageDispatcher.sendMessageToUser(user, new com.platform.iot.message.server.TopicSubscribeMessage());
    }
}
