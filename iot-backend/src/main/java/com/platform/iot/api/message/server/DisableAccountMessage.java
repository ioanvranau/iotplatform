package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class DisableAccountMessage extends ServerMessage {
    public static final String FIELD_STATUS = "status";
    private Status status;

    public enum Status {SUCCESS, FAILED}

    public DisableAccountMessage(Status status) {
        this.setType(MESSAGE.DISABLE_ACCOUNT);
        this.setStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_STATUS, getStatus());
        return jsonObject.toString();
    }
}
