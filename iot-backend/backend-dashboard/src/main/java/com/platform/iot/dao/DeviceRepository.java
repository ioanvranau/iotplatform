package com.platform.iot.dao;

import com.platform.iot.model.Device;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ioan.vranau on 4/27/2016.
 */
public interface DeviceRepository extends CrudRepository<Device, Long> {
}
