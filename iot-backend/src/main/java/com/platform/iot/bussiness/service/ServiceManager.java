package com.platform.iot.bussiness.service;

/**
 * Created by Magda Gherasim
 */
public enum ServiceManager {

    INSTANCE;

    private AccountService accountService = new AccountService();
    private TopicSubscriberService topicSubscriberService = new TopicSubscriberService();

    public TopicSubscriberService getTopicSubscriberService() {
        return topicSubscriberService;
    }

    public AccountService getAccountService() {
        return accountService;
    }


}