package com.platform.iot.controller;

/**
 * Created by ioan.vranau on 1/4/2016.
 */

import com.platform.iot.model.Device;
import com.platform.iot.model.Greeting;
import com.platform.iot.service.DeviceService;
import com.platform.iot.utils.DeviceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DeviceController {

    DeviceService deviceService = new DeviceService();

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/devices")
    public
    @ResponseBody
    List<Device> devices(@RequestParam(value = "name", defaultValue = "World") String name) {
        return deviceService.getAllDevices();
    }

    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<Device> addDevice(@RequestBody Device device) throws UnknownHostException {
        //
        // Code processing the input parameters
        //
        if (device != null) {
            System.out.println(device);
            Device addedDevice;
            try {
                addedDevice = deviceService.addDevice(device.getIp(), device.getName());
            } catch (UnknownHostException e) {
                throw  e;
            }
            return new ResponseEntity<Device>(addedDevice, HttpStatus.OK);
        } else {
            return new ResponseEntity<Device>(DeviceBuilder.build("no ip provided", "no name provided"), HttpStatus.BAD_REQUEST);
        }
    }
}