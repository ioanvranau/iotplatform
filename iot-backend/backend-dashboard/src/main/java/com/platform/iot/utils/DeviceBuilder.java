package com.platform.iot.utils;

import com.platform.iot.model.Device;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
public class DeviceBuilder {

    public static Device build(String ip, String name) {
        String avatar = "error";
        if (name == null) {
            name = "No name proviced!";
        }
        if (name.toLowerCase().contains("phone")) {
            avatar = "smartphone";
        } else if (name.toLowerCase().contains("android")) {
            avatar = "android";
        } else if (name.toLowerCase().contains("iphone")) {
            avatar = "iphone";
        }

        long id = 0;
        String content = "";
        return new Device(id, name, avatar, content, ip);
    }
}
