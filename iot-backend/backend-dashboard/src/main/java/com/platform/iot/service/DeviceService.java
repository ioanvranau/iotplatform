package com.platform.iot.service;

import com.google.common.collect.Lists;
import com.platform.iot.dao.DeviceRepository;
import com.platform.iot.model.Device;
import com.platform.iot.utils.IotException;
import com.platform.iot.utils.IpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        if (device != null) {
            String deviceIp = device.getIp();
            if (deviceIp == null || StringUtils.isEmpty(deviceIp.trim())) {
                throw new IotException("No device ip provided!");
            }

            IpValidator.validate(deviceIp);

            String deviceName = device.getName();
            if (device.getName() != null && (deviceName.toLowerCase().contains("phone"))) {
                device.setAvatar("smartphone");
            } else {
                device.setAvatar("error");
            }
            return deviceRepository.save(device);
        } else {
            throw new IotException("No device provided!");
        }
    }
}
