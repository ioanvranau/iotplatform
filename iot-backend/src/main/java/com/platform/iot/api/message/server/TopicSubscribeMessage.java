package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class TopicSubscribeMessage extends ServerMessage {

    private static final String FIELD_TOPIC_CODE = "topicCode";
    public String topicCode;

    public TopicSubscribeMessage(String topicCode) {
        this.setType(MESSAGE.TOPIC_SUBSCRIBE);
        this.setTopicCode(topicCode);
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_TOPIC_CODE, getTopicCode());
        return jsonObject.toString();
    }
}
