package com.platform.iot.message.server;


import com.platform.iot.message.Message;

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
    }
    public abstract String toJSON();

}
