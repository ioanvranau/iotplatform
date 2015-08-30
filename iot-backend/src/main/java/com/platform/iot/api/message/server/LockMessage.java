package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class LockMessage extends ServerMessage {

    public LockMessage() {
        this.setType(MESSAGE.LOCK);
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        return jsonObject.toString();
    }
}
