package com.platform.iot.api.message.server;

import com.platform.iot.api.bussiness.model.Device;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Magda Gherasim
 */
public class AllDevicesMessage extends ServerMessage {
    private static final String FIELD_DEVICES = "devices";

    private List<Device> devices;

    public AllDevicesMessage(List<Device> devices) {
        this.setType(MESSAGE.ALL_DEVICES);
        this.devices = devices;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, getType());
        JSONArray devicesArray = new JSONArray();
        for (Device device : devices) {
            JSONObject deviceObj = new JSONObject();
            deviceObj.put(Device.FIELD_ID, device.getId());
            deviceObj.put(Device.FIELD_PARAM1, device.getParam1());
            deviceObj.put(Device.FIELD_PARAM2, device.getParam2());
            deviceObj.put(Device.FIELD_PARAM3, device.getParam3());
            deviceObj.put(Device.FIELD_PARAM4, device.getParam4());
            deviceObj.put(Device.FIELD_PARAM5, device.getParam5());
            devicesArray.add(deviceObj);
        }
        jsonObject.put(FIELD_DEVICES, devicesArray);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "AllDevicesMessage{" +
                "devices=" + devices +
                '}';
    }
}
