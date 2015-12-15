package com.platform.iot.api.message.server;


import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.monitoring.MonitoringServiceLocator;
import net.sf.json.JSONObject;

public class ErrorMessage extends ServerMessage {
    private static final String FIELD_CODE = "code";
    private static final String FIELD_MESSAGE = "message";
    private int code;
    private String message;

    public ErrorMessage(ApplicationException exception) {
        this.setType(MESSAGE.ERROR);
        this.code = exception.getType().getCode();
        this.message = exception.getMessage();
        MonitoringServiceLocator.getInstance().getServerMonitor().monitorException(null, exception);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        jsonObject.put(FIELD_CODE, getCode());
        jsonObject.put(FIELD_MESSAGE, getMessage());
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
