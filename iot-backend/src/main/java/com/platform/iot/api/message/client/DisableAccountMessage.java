package com.platform.iot.api.message.client;


import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class DisableAccountMessage extends ClientMessage {
    private int weeksNumber;

    public DisableAccountMessage(JSONObject object) {
        setType(MESSAGE.DISABLE_ACCOUNT);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
        this.weeksNumber = object.optInt("weeksNumber");
    }

    public int getWeeksNumber() {
        return weeksNumber;
    }

    public void setWeeksNumber(int weeksNumber) {
        this.weeksNumber = weeksNumber;
    }

    @Override
    public String toString() {
        return "DisableAccountMessage{" +
                "weeksNumber=" + weeksNumber +
                "token=" + getToken() +
                '}';
    }
}
