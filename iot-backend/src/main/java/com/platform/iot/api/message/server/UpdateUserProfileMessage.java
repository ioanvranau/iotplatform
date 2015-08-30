package com.platform.iot.api.message.server;


import net.sf.json.JSONObject;

public class UpdateUserProfileMessage extends ServerMessage {
    public static final String FIELD_STATUS = "status";
    private Status status;

    public enum Status {SUCCESS, FAILED}

    public UpdateUserProfileMessage(Status status) {
        this.setType(MESSAGE.UPDATE_USER_PROFILE);
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
