package com.platform.iot.api.message.client;


import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TopicUnsubscribeMessage extends ClientMessage {
    private Topic unsubscribedTopic;

    public TopicUnsubscribeMessage(JSONObject object) {
        setType(MESSAGE.TOPIC_UNSUBSCRIBE);
        setToken(object.optString("token"));
        User user = MemoryStorage.INSTANCE.getUserByToken(getToken());
        String topicCode = object.optString("topicCode");
        setUnsubscribedTopic(MemoryStorage.INSTANCE.getTopicByCodeAndProducerId(topicCode, user.getProducerId()));
    }

    public Topic getUnsubscribedTopic() {
        return unsubscribedTopic;
    }

    public void setUnsubscribedTopic(Topic unsubscribedTopic) {
        this.unsubscribedTopic = unsubscribedTopic;
    }

    @Override
    public String toString() {
        return "TopicUnsubscribeMessage{" +
                "unsubscribedTopic=" + unsubscribedTopic +
                '}';
    }
}
