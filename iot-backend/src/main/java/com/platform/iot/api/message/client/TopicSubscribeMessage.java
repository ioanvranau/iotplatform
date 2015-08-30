package com.platform.iot.api.message.client;


import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TopicSubscribeMessage extends ClientMessage {
    private Topic subscribedTopic;

    public TopicSubscribeMessage(JSONObject object) {
        setType(MESSAGE.TOPIC_SUBSCRIBE);
        if (object.optString("token") == null) {
            return;
        }
        setToken(object.optString("token"));
        User user = MemoryStorage.INSTANCE.getUserByToken(getToken());
        String topicCode = object.optString("topicCode");
        setSubscribedTopic(MemoryStorage.INSTANCE.getTopicByCodeAndProducerId(topicCode, user.getProducerId()));
    }

    public Topic getSubscribedTopic() {
        return subscribedTopic;
    }

    public void setSubscribedTopic(Topic subscribedTopic) {
        this.subscribedTopic = subscribedTopic;
    }

    @Override
    public String toString() {
        return "TopicSubscribeMessage{" +
                "subscribedTopic=" + subscribedTopic +
                '}';
    }
}
