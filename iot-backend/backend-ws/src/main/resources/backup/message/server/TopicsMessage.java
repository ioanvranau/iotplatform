package backup.message.server;


import backup.bussiness.MemoryStorage;
import backup.bussiness.model.Topic;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class TopicsMessage extends ServerMessage {

    private static final String FIELD_TOPICS = "topics";
    private List<Topic> topics;

    public TopicsMessage() {
        this.setType(MESSAGE.TOPICS);
        this.setTopics(MemoryStorage.INSTANCE.getTopics());
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
            jsonTopic.put(Topic.FIELD_PRICE, topic.getPrice());
            jsonTopicsArray.add(jsonTopic);
        }
        jsonObject.put(FIELD_TOPICS, jsonTopicsArray);
        return jsonObject.toString();
    }
}
