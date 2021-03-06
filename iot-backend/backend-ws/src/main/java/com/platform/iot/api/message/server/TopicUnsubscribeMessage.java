package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class TopicUnsubscribeMessage extends ServerMessage {

    private static final String FIELD_TOPIC_CODE = "topicCode";
    public String topicCode;

    public TopicUnsubscribeMessage(String topicCode) {
        this.setType(MESSAGE.TOPIC_UNSUBSCRIBE);
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
