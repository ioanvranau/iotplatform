package com.platform.iot.dao.temp;

import com.platform.iot.model.Device;
import com.platform.iot.utils.DeviceBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
public class Storage {

    public static class Devices {
        private static List<Device> devices = new ArrayList<Device>();
        static {
            devices.add(DeviceBuilder.build("localhost", "My super phone"));
            devices.add(DeviceBuilder.build("localhost", "My super iphone"));
        }

        public static List<Device> getDevices() {
            return devices;
        }

        public static Device addDevice(String ip, String name) {
            Device device = DeviceBuilder.build(ip, name);
            devices.add(device);

            return device;
        }
    }
}
