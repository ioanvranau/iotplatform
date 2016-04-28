package com.platform.iot.utils;

/**
 * Created by ioan.vranau on 4/28/2016.
 */

import com.platform.iot.dao.DeviceRepository;
import com.platform.iot.model.Device;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DeviceRepository deviceRepository;

    private Logger log = Logger.getLogger(DeviceLoader.class);

    public void onApplicationEvent(ContextRefreshedEvent event) {

        Device device1 = DeviceBuilder.build("localhost", "My super phone");
        deviceRepository.save(device1);

        log.info("Saved device - name: " + device1.getName());

        Device device2 = DeviceBuilder.build("127.0.0.1", "My super phone");
        deviceRepository.save(device2);

        log.info("Saved device - name: " + device2.getName());
    }
}