package com.platform.iot.api.producer;

import net.sf.json.JSONObject;

public class Event {

    private JSONObject jsonObject;

    public Event(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

}
