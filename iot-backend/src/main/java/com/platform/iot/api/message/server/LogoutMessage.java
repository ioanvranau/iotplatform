package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class LogoutMessage extends ServerMessage {

    public LogoutMessage() {
        this.setType(MESSAGE.LOGOUT);
    }



    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        return jsonObject.toString();
    }
}
