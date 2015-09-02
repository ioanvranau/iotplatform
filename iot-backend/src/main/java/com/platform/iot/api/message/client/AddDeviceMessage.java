package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class AddDeviceMessage extends ClientMessage {
    public static final String PARAM1 = "param1";
    public static final String PARAM2 = "param2";
    public static final String PARAM3 = "param3";
    public static final String PARAM4 = "param4";
    public static final String PARAM5 = "param5";

    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;

    public AddDeviceMessage(JSONObject object) {
        setType(MESSAGE.ADD_DEVICE);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }
        this.param1 = object.optString(PARAM1);
        this.param2 = object.optString(PARAM2);
        this.param3 = object.optString(PARAM3);
        this.param4 = object.optString(PARAM4);
        this.param5 = object.optString(PARAM5);
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Override
    public String toString() {
        return "AddDeviceMessage{" +
                "token='" + super.getToken() + "'" +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param3='" + param3 + '\'' +
                ", param4='" + param4 + '\'' +
                ", param5='" + param5 + '\'' +
                '}';
    }
}
