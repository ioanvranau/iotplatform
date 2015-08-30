package com.platform.iot.producer;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by magdalena.gherasim on 9/3/2014.
 */
public class TopicPublisher {

    private static final Logger logger = LoggerFactory.getLogger(TopicPublisher.class);

    private static boolean startedForDefaultProducer = false;
    public static void startSimulator(List<Topic> topics, long producerId) {
        if (topics != null) {
            if(producerId == TopicDistributionApplication.DEFAULT_PRODUCER_ID && !startedForDefaultProducer) {
                startedForDefaultProducer = true;
                for (Topic topic : topics) {
                    new TopicPublishingTimer(topic, producerId);

                }
            }
        } else {
            logger.error("Null topic group");
        }
    }
}
