package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class RegisterMessage extends ServerMessage {


    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private String username;
    private String password;

    public RegisterMessage(String username, String password) {
        this.setType(MESSAGE.REGISTER);
        this.setUsername(username);
        this.setPassword(password);
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
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_USERNAME, getUsername());
        jsonObject.put(FIELD_PASSWORD, getPassword());
        return jsonObject.toString();
    }

}
