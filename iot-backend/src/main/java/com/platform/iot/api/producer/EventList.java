package com.platform.iot.api.producer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Collection;

/**
 * Created by magdalena.gherasim on 6/6/2014.
 */
public class EventList {

    private Collection<Event> events;
    private JSONObject jsonObject;

    public EventList(Collection<Event> events) {
        this.events = events;

        jsonObject = new JSONObject();
        JSONArray jsonList = new JSONArray();
        for (Event event : events) {
            JSONObject jsonEvent = event.getJsonObject();
            jsonList.add(jsonEvent);
        }
        jsonObject.put("eventList", jsonList);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}