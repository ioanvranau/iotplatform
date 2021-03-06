package com.platform.iot.api.codec;

/**
 * Created by Magda Gherasim
 */
public enum MessageFactoryBuilder {

    INSTANCE;

    public MessageFactory createFactory() {
        Class<MessageFactory> messageFactoryClass = null;
        try {
            messageFactoryClass = (Class<MessageFactory>) Class.forName("com.platform.iot.api.codec.json.JsonMessageFactory");
            return messageFactoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate message factory", e);
        }
    }
}