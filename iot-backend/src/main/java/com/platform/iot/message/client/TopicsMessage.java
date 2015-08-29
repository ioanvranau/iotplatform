package com.platform.iot.message.client;

import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TopicsMessage extends ClientMessage{

    public TopicsMessage(JSONObject jsonObject) {
        super();
        setToken(jsonObject.getString("token"));
    }

}
