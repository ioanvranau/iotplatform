package backup.message.client;


import backup.bussiness.MemoryStorage;
import backup.bussiness.model.Topic;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TopicSubscribeMessage extends ClientMessage{
    private Topic subscribedTopic;

    public TopicSubscribeMessage(JSONObject object) {
        setType(MESSAGE.TOPIC_SUBSCRIBE);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
        String topicCode = object.optString("topicCode");
        setSubscribedTopic(MemoryStorage.INSTANCE.getTopicByCode(topicCode));
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
