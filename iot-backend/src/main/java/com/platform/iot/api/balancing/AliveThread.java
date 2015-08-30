package com.platform.iot.api.balancing;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.storage.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Thread that runs at fixed rate and informs the load balancer storage that the server is up and running
 *
 */
@Component
public class AliveThread implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(AliveThread.class);

    @Autowired
    private RedisService redisService;

    @Override
    public void run() {
        redisService.serverAlive(TopicDistributionApplication.getServer());
    }

}
