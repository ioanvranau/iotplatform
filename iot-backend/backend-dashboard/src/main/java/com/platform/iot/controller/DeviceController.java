package com.platform.iot.controller;

/**
 * Created by ioan.vranau on 1/4/2016.
 */

import com.platform.iot.model.Device;
import com.platform.iot.service.DeviceService;
import com.platform.iot.utils.DeviceBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController
public class DeviceController {

    private Logger log = Logger.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

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
            log.info(device);
            Device addedDevice;
            try {
                addedDevice = deviceService.addDevice(device);
            } catch (UnknownHostException e) {
                throw e;
            }
            return new ResponseEntity<Device>(addedDevice, HttpStatus.OK);
        } else {
            return new ResponseEntity<Device>(DeviceBuilder.build("no ip provided", "no name provided"), HttpStatus.BAD_REQUEST);
        }
    }
}