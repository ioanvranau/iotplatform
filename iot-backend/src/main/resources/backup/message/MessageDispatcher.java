package backup.message;

import backup.bussiness.MemoryStorage;
import backup.bussiness.model.User;
import backup.message.server.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: magdalena.gherasim
 */
public class MessageDispatcher {
    private final static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    public static void sendMessageToLoggedUsers(ServerMessage serverMessage) {
        MemoryStorage.INSTANCE.getLoggedUsers().write(serverMessage);
        logger.info("send to logged users " + serverMessage.toJSON());
    }

    public static void sendMessageToUser(User user, ServerMessage serverMessage) {
        logger.info("send  to " + user.getId() + serverMessage.toJSON() + ", channelId : " + user.getChannel().id());
        user.getChannel().write(serverMessage);
    }

}
