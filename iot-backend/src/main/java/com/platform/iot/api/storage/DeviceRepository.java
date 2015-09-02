package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

}
