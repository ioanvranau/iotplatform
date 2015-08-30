package com.platform.iot.api.message.server;

import com.platform.iot.api.message.Message;

/**
 * Created by Magda on 17.05.2014.
 */
public abstract class ServerMessage extends Message {

    public static class MESSAGE {
        public static final String VERSION = "version";
        public static final String ERROR = "error";
        public static final String TOKEN = "token";
        public static final String PRICE = "price";
        public static final String START = "start";
        public static final String TOPICS = "topics";
        public static final String MIGRATION = "migration";
        public static final String TOPIC_SUBSCRIBE = "topicSubscribe";
        public static final String TOPIC_UNSUBSCRIBE = "topicUnsubscribe";
        public static final String LOGOUT = "logout";
        public static final String REGISTER = "register";
        public static final String LOCK = "lock";
        public static final String UPDATE_USER_PROFILE = "updateUserProfile";
        public static final String EMAIL_NOTIFICATION = "emailNotification";
        public static final String DISABLE_ACCOUNT = "disableAccount";
        public static final String USER_PROFILE = "userProfile";
    }
    public abstract String toJSON();
}
