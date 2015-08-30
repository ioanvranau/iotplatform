package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class LoginMessage extends ClientMessage {
    public final static String FIELD_USERNAME = "username";
    public final static String FIELD_PASSWORD = "password";

    private String username;
    private String password;

    public LoginMessage(JSONObject jsonObject) {
        this.username = jsonObject.getString(FIELD_USERNAME);
        this.password = jsonObject.getString(FIELD_PASSWORD);
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

    @Override
    public String toString() {
        return "LoginMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
