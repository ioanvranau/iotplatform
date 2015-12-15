package com.platform.iot.api.message.client;

import com.platform.iot.api.TopicDistributionApplication;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TopicsMessage extends ClientMessage {
    private long producerId;

    public TopicsMessage(JSONObject jsonObject) {
        super();
        setToken(jsonObject.getString("token"));
        setProducerId(TopicDistributionApplication.DEFAULT_PRODUCER_ID);
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }
}
