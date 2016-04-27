package com.platform.iot.service;

import com.platform.iot.dao.DeviceDao;
import com.platform.iot.model.Device;
import com.platform.iot.utils.DeviceBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
public class DeviceService {

    DeviceDao deviceDao = new DeviceDao();

    public List<Device> getAllDevices() {
        return deviceDao.getAllDevices();
    }

    public Device addDevice(String ip, String name) {
        return deviceDao.addDevice(ip, name);
    }
}
