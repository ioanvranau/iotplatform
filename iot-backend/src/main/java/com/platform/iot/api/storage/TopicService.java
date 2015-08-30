package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.exception.ApplicationException;

import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface TopicService {

    Topic create(Topic topic);

    Topic delete(long id) throws ApplicationException;

    List<Topic> findAll();

    Topic update(Topic topic);

    Topic findById(long id);

    Topic findByProducerIdAndCode(long producerId, String code);
}
