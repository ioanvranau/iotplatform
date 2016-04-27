package com.platform.iot.dao;

import com.platform.iot.dao.temp.Storage;
import com.platform.iot.model.Device;
import com.platform.iot.utils.DeviceBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
public class DeviceDao {
    public List<Device> getAllDevices() {
        return Storage.Devices.getDevices();
    }

    public Device addDevice(String ip, String name) {
        return Storage.Devices.addDevice(ip, name);
    }
}
