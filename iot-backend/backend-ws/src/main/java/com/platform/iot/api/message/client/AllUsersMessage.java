package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

public class AllUsersMessage extends ClientMessage {


    public AllUsersMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(MESSAGE.ALL_USERS);
        setToken(object.optString(FIELD_TOKEN));
    }

    @Override
    public String toString() {
        return "AllUsersMessage{" +
                "token='" + super.getToken() + "\'}";
    }

}
