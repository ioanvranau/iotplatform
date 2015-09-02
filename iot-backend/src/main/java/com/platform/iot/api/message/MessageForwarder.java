package com.platform.iot.api.message;

import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.bussiness.service.ServiceManager;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import com.platform.iot.api.message.client.*;
import com.platform.iot.api.message.server.EmailNotificationMessage;
import com.platform.iot.api.message.server.ErrorMessage;
import com.platform.iot.api.message.server.TopicsMessage;
import com.platform.iot.api.message.server.UserProfileMessage;
import io.netty.channel.Channel;


/**
 * Created by Magda Gherasim
 */
public class MessageForwarder {
    public static void forward(Channel channel, Message message) throws ApplicationException {
        String messageType = message.getType();

        User user = MemoryStorage.INSTANCE.getUserByToken(((ClientMessage) message).getToken());
        if(user!= null && user.getTopicsChannel() != channel){
            user.setTopicsChannel(channel);
        }
        switch (messageType) {
            case ClientMessage.MESSAGE.REGISTER: {
                RegisterMessage registerMessage = (RegisterMessage) message;
                ServiceManager.INSTANCE.getAccountService().register(channel, registerMessage);
            }
            break;
            case ClientMessage.MESSAGE.LOGIN: {
                LoginMessage loginMessage = (LoginMessage) message;
                ServiceManager.INSTANCE.getAccountService().login(channel, loginMessage);
            }
            break;

            case ClientMessage.MESSAGE.LOGOUT: {
                LogoutMessage logoutMessage = (LogoutMessage) message;
                ServiceManager.INSTANCE.getAccountService().logout(channel, logoutMessage);
            }
            break;
            case ClientMessage.MESSAGE.LOCK: {
                LockMessage lockMessage = (LockMessage) message;
                ServiceManager.INSTANCE.getAccountService().lock(channel, lockMessage);
            }
            break;
            case ClientMessage.MESSAGE.ALL_USERS: {
                AllUsersMessage allUsersMessage = (AllUsersMessage) message;
                ServiceManager.INSTANCE.getAccountService().getAllUsers(channel, allUsersMessage);
            }
            break;
            case ClientMessage.MESSAGE.START_LISTENING: {
                if (user == null) {
                    channel.write(new ErrorMessage(new ApplicationException(ExceptionType.USER_NOT_AUTHENTICATED)));
                    return;
                } else {
                    user.setTopicsChannel(channel);
                }
//                TopicPublisher.startSimulator(MemoryStorage.INSTANCE.getTopics(), TopicDistributionApplication.DEFAULT_PRODUCER_ID);
//                ServiceManager.INSTANCE.getTopicSubscriberService().startSimulator(user);
            }
            break;
            case ClientMessage.MESSAGE.TOPICS: {
                if (user == null) {
                    channel.write(new ErrorMessage(new ApplicationException(ExceptionType.USER_NOT_AUTHENTICATED)));
                    return;
                } else {
                    user.setTopicsChannel(channel);
                }
                MessageDispatcher.sendMessageToUser(user, new TopicsMessage(user.getProducerId()));
            }
            break;
            case ClientMessage.MESSAGE.TOPIC_SUBSCRIBE: {
                if (user == null) {
                    channel.write(new ErrorMessage(new ApplicationException(ExceptionType.USER_NOT_AUTHENTICATED)));
                    return;
                } else {
                    user.setTopicsChannel(channel);
                }
                com.platform.iot.api.message.client.TopicSubscribeMessage topicSubscribeMessage = (com.platform.iot.api.message.client.TopicSubscribeMessage) message;
                ServiceManager.INSTANCE.getTopicDistributionService().subscribeToTopic(topicSubscribeMessage);
            }
            break;
            case ClientMessage.MESSAGE.TOPIC_UNSUBSCRIBE: {
                if (user == null) {
                    channel.write(new ErrorMessage(new ApplicationException(ExceptionType.USER_NOT_AUTHENTICATED)));
                    return;
                } else {
                    user.setTopicsChannel(channel);
                }
                TopicUnsubscribeMessage unsubscribeMessage = (TopicUnsubscribeMessage) message;
                ServiceManager.INSTANCE.getTopicDistributionService().unsubscribeFromTopic(unsubscribeMessage);
            }
            break;

            case ClientMessage.MESSAGE.MIGRATION: {
                if (user == null) {
                    channel.write(new ErrorMessage(new ApplicationException(ExceptionType.USER_NOT_AUTHENTICATED)));
                    return;
                } else {
                    user.setTopicsChannel(channel);
                }
                MigrationMessage migrationMessage = (MigrationMessage) message;
                ServiceManager.INSTANCE.getAccountService().doMigration(channel, migrationMessage);
            }
            break;

            case ClientMessage.MESSAGE.UPDATE_USER_PROFILE: {
                com.platform.iot.api.message.client.UpdateUserProfileMessage updateUserProfileMessage = (com.platform.iot.api.message.client.UpdateUserProfileMessage) message;
                ServiceManager.INSTANCE.getAccountService().updateUserProfileMessage(channel, updateUserProfileMessage);
            }
            break;
            case ClientMessage.MESSAGE.EMAIL_NOTIFICATION: {
                //TODO : logic to update email notification
                MessageDispatcher.sendMessageToUser(user, new EmailNotificationMessage(com.platform.iot.api.message.server.EmailNotificationMessage.Status.SUCCESS));
            }
            break;
            case ClientMessage.MESSAGE.DISABLE_ACCOUNT: {
                com.platform.iot.api.message.client.DisableAccountMessage disableAccountMessage = (com.platform.iot.api.message.client.DisableAccountMessage) message;
                ServiceManager.INSTANCE.getAccountService().disableAccount(channel, disableAccountMessage);
                MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.DisableAccountMessage(com.platform.iot.api.message.server.DisableAccountMessage.Status.SUCCESS));
            }
            break;
            case ClientMessage.MESSAGE.USER_PROFILE:
                MessageDispatcher.sendMessageToUser(user, new UserProfileMessage(user));
                break;
            case ClientMessage.MESSAGE.ADD_DEVICE: {
                AddDeviceMessage addDeviceMessage = (AddDeviceMessage) message;
                ServiceManager.INSTANCE.getAccountService().addDevice(channel, addDeviceMessage);
            }
            break;
            case ClientMessage.MESSAGE.GET_DEVICE: {
                GetDeviceMessage getDeviceMessage = (GetDeviceMessage) message;
                ServiceManager.INSTANCE.getAccountService().getDevice(channel, getDeviceMessage);
            }
            break;

        }
    }
}