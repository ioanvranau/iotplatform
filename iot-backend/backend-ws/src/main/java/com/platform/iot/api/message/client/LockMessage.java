package com.platform.iot.api.message.client;


import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class LockMessage extends ClientMessage {

    public LockMessage(JSONObject object) {
        setType(MESSAGE.LOCK);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
    }


}
