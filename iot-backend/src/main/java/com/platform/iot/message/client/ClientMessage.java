package com.platform.iot.message.client;


import com.platform.iot.message.Message;

/**
 * Created by Magda Gherasim
 */
public class ClientMessage extends Message {

    public static class MESSAGE {
        public static final String VERSION = "version";
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
        public static final String START_LISTENING = "startListening";
        public static final String TOPICS = "topics";
        public static final String TOPIC_SUBSCRIBE = "topicSubscribe";
        public static final String MIGRATION = "migration";
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
