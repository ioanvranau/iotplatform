package backup.codec.json;

import backup.codec.MessageFactory;
import backup.message.Message;
import backup.message.client.*;
import backup.message.server.ServerMessage;
import io.netty.handler.codec.DecoderException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


/**
 * Created by Magda Gherasim
 */
public class JsonMessageFactory implements MessageFactory {
    public Message decode(String input) throws Exception {
        try {
            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(input);
            Message message = createClientMessageInstance(jsonObject);
            return message;
        } catch (Exception e) {
            throw e;
        }
    }

    public String encode(ServerMessage serverMessage) {
        return outputServerMessage(serverMessage);
    }

    protected Message createClientMessageInstance(JSONObject jsonObject) {
        String type = jsonObject.getString(Message.FIELD_TYPE);
        Message message = null;
        if (type.equals(ClientMessage.MESSAGE.REGISTER)) {
            message = new RegisterMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.LOGIN)) {
            message = new LoginMessage();

        } else if (type.equals(ClientMessage.MESSAGE.START_LISTENING)) {
            message = new StartListeningMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.TOPICS)) {
            message = new TopicsMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.TOPIC_SUBSCRIBE)) {
            message = new TopicSubscribeMessage(jsonObject);

        } else {
            throw new DecoderException(String.format("Could not identify client " +
                    "message type for input {0}!", jsonObject.toString()));
        }
        message.setType(type);
        return message;
    }

    private String outputServerMessage(ServerMessage serverMessage) {
        return serverMessage.toJSON();
    }
}
