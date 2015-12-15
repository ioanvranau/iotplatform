package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Producer;
import com.platform.iot.api.exception.ApplicationException;

import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface ProducerService {

    Producer create(Producer producer);

    Producer delete(long id) throws ApplicationException;

    List<Producer> findAll();

    Producer update(Producer user);

    Producer findById(long id);

    Producer findByName(String username);
}
