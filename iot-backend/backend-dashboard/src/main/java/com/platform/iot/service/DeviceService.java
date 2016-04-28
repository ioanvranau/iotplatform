package com.platform.iot.service;

import com.google.common.collect.Lists;
import com.platform.iot.dao.DeviceRepository;
import com.platform.iot.model.Device;
import com.platform.iot.utils.DeviceBuilder;
import com.platform.iot.utils.IpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
@Component
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return Lists.newArrayList(deviceRepository.findAll());
    }

    public Device addDevice(Device device) throws UnknownHostException {
        if(device != null) {
            IpValidator.validate(device.getIp());

            String deviceName = device.getName();
            if(device.getName() != null && (deviceName.toLowerCase().contains("phone"))) {
                device.setAvatar("smartphone");
            } else {
                device.setAvatar("error");
            }
            return deviceRepository.save(device);
        } else {
            throw new RuntimeException("No device provided!");
        }
    }
}
