package com.platform.iot.api.message.client;

import com.platform.iot.api.message.Message;

/**
 * Created by Magda Gherasim
 */
public class ClientMessage extends Message {

    public static class MESSAGE {
        public static final String VERSION = "version";
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
        public static final String LOGOUT = "logout";
        public static final String START_LISTENING = "startListening";
        public static final String TOPICS = "topics";
        public static final String TOPIC_SUBSCRIBE = "topicSubscribe";
        public static final String TOPIC_UNSUBSCRIBE = "topicUnsubscribe";
        public static final String MIGRATION = "migration";
        public static final String LOCK = "lock";
        public static final String UPDATE_USER_PROFILE = "updateUserProfile";
        public static final String ALL_USERS = "allUsers";
        public static final String EMAIL_NOTIFICATION = "emailNotification";
        public static final String DISABLE_ACCOUNT = "disableAccount";
        public static final String USER_PROFILE = "userProfile";
        public static final String ADD_DEVICE = "addDevice";
        public static final String GET_DEVICE = "getDevice";
    }

    public final static String FIELD_TOKEN = "token";

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "type='" + getType() + '\'' +
                ",token='" + token + '\'' +
                "}";
    }

}
