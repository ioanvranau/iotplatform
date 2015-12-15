package backup.message.server;

import backup.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TokenMessage extends ServerMessage {
    private static final String FIELD_TOKEN = "token";

    private String token;

    public TokenMessage(User user) {
        this.setType(MESSAGE.TOKEN);
        this.setToken(user.getToken());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenMessage{" +
                "token='" + token + '\'' +
                '}';
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_TOKEN, getToken());
        return jsonObject.toString();
    }
}
