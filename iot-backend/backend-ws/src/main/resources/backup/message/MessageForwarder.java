package backup.message;

import backup.bussiness.MemoryStorage;
import backup.bussiness.model.User;
import backup.bussiness.service.ServiceManager;
import backup.exception.ApplicationException;
import backup.message.client.*;
import io.netty.channel.Channel;


/**
 * Created by Magda Gherasim
 */
public class MessageForwarder {
    public static void forward(Channel channel, Message message) throws ApplicationException {
        String messageType = message.getType();

        if (messageType.equals(ClientMessage.MESSAGE.REGISTER)) {
            RegisterMessage registerMessage = (RegisterMessage) message;
            ServiceManager.INSTANCE.getAccountService().register(channel, registerMessage);

        } else if (messageType.equals(ClientMessage.MESSAGE.LOGIN)) {
            LoginMessage loginMessage = (LoginMessage) message;
            ServiceManager.INSTANCE.getAccountService().login(loginMessage);

        } else if (messageType.equals(ClientMessage.MESSAGE.START_LISTENING)) {
            User user = MemoryStorage.INSTANCE.getUserByToken(((StartListeningMessage) message).getToken());
            ServiceManager.INSTANCE.getTopicSubscriberService().handleMessage(user);
        } else if (messageType.equals(ClientMessage.MESSAGE.TOPICS)) {
            User user = MemoryStorage.INSTANCE.getUserByToken(((TopicsMessage) message).getToken());
            MessageDispatcher.sendMessageToUser(user, new backup.message.server.TopicsMessage());
        } else if (messageType.equals(ClientMessage.MESSAGE.TOPIC_SUBSCRIBE)) {
            TopicSubscribeMessage topicSubscribeMessage = (TopicSubscribeMessage) message;
            ServiceManager.INSTANCE.getTopicSubscriberService().subscribeToTopic(topicSubscribeMessage);
        }
    }
}