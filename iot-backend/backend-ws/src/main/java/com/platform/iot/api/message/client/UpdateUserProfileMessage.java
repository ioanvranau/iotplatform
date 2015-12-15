package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

public class UpdateUserProfileMessage extends ClientMessage {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_TOKEN = "token";

    private String name;
    private String country;
    private String password;
    private String email;


    public UpdateUserProfileMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(MESSAGE.UPDATE_USER_PROFILE);
        setToken(object.optString(FIELD_TOKEN));
        this.name = object.optString(FIELD_NAME);
        this.password = object.optString(FIELD_PASSWORD);
        this.email = object.optString(FIELD_EMAIL);
        this.country = object.optString(FIELD_COUNTRY);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "UpdateUserProfileMessage{" +
                "token='" + super.getToken() + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", token='" + getToken() + '\'' +
                '}';
    }
}
