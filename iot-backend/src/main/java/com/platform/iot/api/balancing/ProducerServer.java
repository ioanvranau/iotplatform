package com.platform.iot.api.balancing;

/**
 * Created by Magda Gherasim
 */
public class ProducerServer {

    private String brokerURL;
    private String queueName;

    public ProducerServer(String brokerURL, String queueName) {
        this.brokerURL = brokerURL;
        this.queueName = queueName;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
