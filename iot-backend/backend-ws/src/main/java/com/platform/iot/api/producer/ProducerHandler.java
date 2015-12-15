package com.platform.iot.api.producer;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Producer;
import com.platform.iot.api.monitoring.MonitoringServiceLocator;
import com.platform.iot.api.storage.ProducerService;

/**
 * Created by magdalena.gherasim on 9/5/2014.
 */
public class ProducerHandler {

    public static Producer registerProducer(String producerName){
        ProducerService producerService = TopicDistributionApplication.context.getBean(ProducerService.class);
        Producer producer = producerService.findByName(producerName);
        if (producer == null){
            producer = new Producer(producerName);
            MemoryStorage.INSTANCE.getProducers().add(producer);
            MonitoringServiceLocator.getInstance().getServerMonitor().updateProducersNumber();
            producer = producerService.create(producer);
        }
        return producer;
    }

}
