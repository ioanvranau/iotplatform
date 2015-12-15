package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

public class AllDevicesMessage extends ClientMessage {


    public AllDevicesMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(MESSAGE.ALL_DEVICES);
        setToken(object.optString(FIELD_TOKEN));
    }

    @Override
    public String toString() {
        return "AllDevicesMessage{" +
                "token='" + super.getToken() + "\'}";
    }

}
