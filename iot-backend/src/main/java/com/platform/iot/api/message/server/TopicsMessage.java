package com.platform.iot.api.message.server;


import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class TopicsMessage extends ServerMessage {

    private static final String FIELD_TOPICS = "topics";
    private List<Topic> topics;

    public TopicsMessage(long producerId) {
        this.setType(MESSAGE.TOPICS);
        this.setTopics(MemoryStorage.INSTANCE.getTopicsByProducerId(producerId));
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        JSONArray jsonTopicsArray = new JSONArray();
        for (Topic topic : getTopics()) {
            JSONObject jsonTopic = new JSONObject();
            jsonTopic.put(Topic.FIELD_CODE, topic.getCode());
            jsonTopic.put(Topic.FIELD_NAME, topic.getName());
            jsonTopic.put(Topic.FIELD_PRICE, topic.getPrice());
            jsonTopic.put(Topic.FIELD_DEFAULT_SUBSCRIBED, topic.isDefaultSubscribed());
            jsonTopicsArray.add(jsonTopic);
        }
        jsonObject.put(FIELD_TOPICS, jsonTopicsArray);
        return jsonObject.toString();
    }
}
