package com.platform.iot.api.message.client;

import com.platform.iot.api.bussiness.model.User;
import net.sf.json.JSONObject;

public class RegisterMessage extends ClientMessage {
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_EMAIL = "email";

    private String username;
    private String password;
    private String email;
    private User.UserType userType = User.UserType.NORMAL;

    public RegisterMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(ClientMessage.MESSAGE.REGISTER);
        username = object.optString(FIELD_USERNAME);
        password = object.optString(FIELD_PASSWORD);
        email = object.optString(FIELD_EMAIL);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "token='" + super.getToken() + '\'' +
                " ,username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }
}
