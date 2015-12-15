package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class GetDeviceMessage extends ClientMessage {
    public static final String DEVICE_ID = "deviceId";

    private Long deviceId;

    public GetDeviceMessage(JSONObject object) {
        setType(MESSAGE.GET_DEVICE);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
        this.deviceId = object.optLong(DEVICE_ID);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "GetDeviceMessage{" +
                "token='" + super.getToken() + "'" +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
