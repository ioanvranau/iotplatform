package com.platform.iot.api.balancing;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.model.User;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
  */
public class MigrationThread implements Runnable {

    private CountDownLatch countDownLatch;

    public MigrationThread(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (!allUsersMigrated()) {
            return;
        }
        countDownLatch.countDown();
    }

    private boolean allUsersMigrated() {
        LoadBalancer loadBalancer = TopicDistributionApplication.context.getBean(LoadBalancer.class);
        return loadBalancer.migrationStatus(true, new ArrayList<User>());
    }

}
