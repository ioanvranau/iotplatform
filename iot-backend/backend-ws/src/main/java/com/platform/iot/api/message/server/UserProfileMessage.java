package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class UserProfileMessage extends ServerMessage {
    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_ID = "id";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_EMAIL = "email";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_COUNTRY = "country";

    private String token;
    private String email;
    private String username;
    private String password;
    private String country;
    private String name;
    private Long id;

    public UserProfileMessage(User user) {
        this.setType(MESSAGE.USER_PROFILE);
        this.setToken(user.getToken());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setEmail(user.getEmail());
        this.setName(user.getName());
        this.setCountry(user.getCountry());
        this.setId(user.getId());
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        jsonObject.put(FIELD_PASSWORD, getPassword());
        jsonObject.put(FIELD_NAME, getName());
        jsonObject.put(FIELD_COUNTRY, getCountry());
        return jsonObject.toString();
    }
}
