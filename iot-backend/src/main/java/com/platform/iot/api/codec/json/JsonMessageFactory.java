package com.platform.iot.api.codec.json;

import com.platform.iot.api.codec.MessageFactory;
import com.platform.iot.api.message.Message;
import com.platform.iot.api.message.client.*;
import com.platform.iot.api.message.server.ServerMessage;
import io.netty.handler.codec.DecoderException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Created by Magda Gherasim
 */
public class JsonMessageFactory implements MessageFactory {
    @Override
    public Message decode(String input) throws Exception {
        try {
            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(input);
            Message message = createClientMessageInstance(jsonObject);
            return message;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String encode(ServerMessage serverMessage) {
        return outputServerMessage(serverMessage);
    }

    protected Message createClientMessageInstance(JSONObject jsonObject) {
        String type = jsonObject.getString(Message.FIELD_TYPE);
        Message message = null;
        if (type.equals(ClientMessage.MESSAGE.REGISTER)) {
            message = new RegisterMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.LOGIN)) {
            message = new LoginMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.ALL_USERS)) {
            message = new AllUsersMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.LOGOUT)) {
            message = new LogoutMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.LOCK)) {
            message = new LockMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.START_LISTENING)) {
//            try {
//                Thread.sleep(4000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            message = new StartListeningMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.TOPICS)) {
            message = new TopicsMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.TOPIC_SUBSCRIBE)) {
            message = new TopicSubscribeMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.TOPIC_UNSUBSCRIBE)) {
            message = new TopicUnsubscribeMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.MIGRATION)) {
            message = new MigrationMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.UPDATE_USER_PROFILE)) {
            message = new UpdateUserProfileMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.EMAIL_NOTIFICATION)) {
            message = new EmailNotificationMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.DISABLE_ACCOUNT)) {
            message = new DisableAccountMessage(jsonObject);

        } else if (type.equals(ClientMessage.MESSAGE.USER_PROFILE)) {
            message = new UserProfileMessage(jsonObject);

        }else if (type.equals(ClientMessage.MESSAGE.ADD_DEVICE)) {
            message = new AddDeviceMessage(jsonObject);

        }  else if (type.equals(ClientMessage.MESSAGE.GET_DEVICE)) {
            message = new GetDeviceMessage(jsonObject);

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
