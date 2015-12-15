package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Device;
import com.platform.iot.api.exception.ApplicationException;

import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface DeviceService {

    Device create(Device device);

    List<Device> findAll();

    public Device delete(long id) throws ApplicationException;

    public Device findById(long id);

}
