package com.platform.iot.api.message.client;


import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class UserProfileMessage extends ClientMessage{

    public UserProfileMessage(JSONObject object) {
        setType(MESSAGE.USER_PROFILE);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
    }


}
