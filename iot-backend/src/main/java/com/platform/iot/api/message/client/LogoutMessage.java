package com.platform.iot.api.message.client;


import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class LogoutMessage extends ClientMessage {
    public LogoutMessage(JSONObject object) {
        setType(MESSAGE.LOGOUT);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
    }
}
