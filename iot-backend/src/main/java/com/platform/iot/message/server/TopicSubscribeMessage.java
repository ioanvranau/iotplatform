package com.platform.iot.message.server;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class TopicSubscribeMessage extends ServerMessage {

    public TopicSubscribeMessage() {
        this.setType(MESSAGE.TOPIC_SUBSCRIBE);
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        return jsonObject.toString();
    }
}
