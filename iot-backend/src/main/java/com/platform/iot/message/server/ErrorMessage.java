package com.platform.iot.message.server;


import com.platform.iot.message.Message;

public class ErrorMessage extends Message {
    private int code;
    private String message;

    private final int TECHNICAL_ERROR = 0;

    public ErrorMessage(Exception ex) {
        this.setType(ServerMessage.MESSAGE.ERROR);
        this.setCode(TECHNICAL_ERROR);
        this.setMessage(ex.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
