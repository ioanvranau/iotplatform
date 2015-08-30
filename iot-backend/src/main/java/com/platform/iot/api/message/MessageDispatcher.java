package com.platform.iot.api.message;

import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.message.server.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: magdalena.gherasim
 */
public class MessageDispatcher {
    private final static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    public static void sendMessageToLoggedUsers(ServerMessage serverMessage, Topic topic) {
//        MemoryStorage.INSTANCE.getLoggedUsers().write(serverMessage);
//        logger.info("send to logged users " + serverMessage.toJSON());
        for (User user : MemoryStorage.INSTANCE.getUsers().values()) {
            try {
                if (user.getTopicsChannel() != null && user.isSubscriebed(topic)) {
                    sendMessageToUser(user, serverMessage);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void sendMessageToUser(User user, ServerMessage serverMessage) {
        try {
            logger.info("send  to " + user.getId() + serverMessage.toJSON() + ", channelId : " + user.getTopicsChannel().id());
            user.getTopicsChannel().write(serverMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
