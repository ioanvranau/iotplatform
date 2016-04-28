package com.platform.iot.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ioan.vranau on 4/28/2016.
 */
public class IpValidator {

    public static InetAddress validate(String ip) throws UnknownHostException {
        return InetAddress.getByName(ip);
    }
}
