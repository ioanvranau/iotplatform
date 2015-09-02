package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.Device;
import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class GetDeviceMessage extends ServerMessage {
    private static final String FIELD_DEVICE = "device";

    private Device device;

    public GetDeviceMessage(Device device) {
        this.setType(MESSAGE.GET_DEVICE);
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        JSONObject deviceObj = new JSONObject();
        deviceObj.put(Device.FIELD_ID, device.getId());
        deviceObj.put(Device.FIELD_PARAM1, device.getParam1());
        deviceObj.put(Device.FIELD_PARAM2, device.getParam2());
        deviceObj.put(Device.FIELD_PARAM3, device.getParam3());
        deviceObj.put(Device.FIELD_PARAM4, device.getParam4());
        deviceObj.put(Device.FIELD_PARAM5, device.getParam5());
        jsonObject.put(FIELD_DEVICE, deviceObj);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "GetDeviceMessage{" +
                "device=" + device +
                '}';
    }
}
