package backup.message.server;


import net.sf.json.JSONObject;

public class TopicSubscribeMessage extends ServerMessage {

    public TopicSubscribeMessage() {
        this.setType(MESSAGE.TOPIC_SUBSCRIBE);
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        return jsonObject.toString();
    }
}
