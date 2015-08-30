package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class TokenMessage extends ServerMessage {
    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_ID = "id";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_EMAIL = "email";

    private String token;
    private String email;
    private String username;
    private Long id;

    public TokenMessage(User user) {
        this.setType(MESSAGE.TOKEN);
        this.setToken(user.getToken());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setId(user.getId());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        jsonObject.put(FIELD_ID, getId());
        jsonObject.put(FIELD_EMAIL, getEmail());
        jsonObject.put(FIELD_USERNAME, getUsername());
        return jsonObject.toString();
    }
}
