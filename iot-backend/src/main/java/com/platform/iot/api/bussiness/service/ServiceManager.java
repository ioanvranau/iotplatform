package com.platform.iot.api.bussiness.service;

/**
 * Created by Magda Gherasim
 */
public enum ServiceManager {

    INSTANCE;

    private AccountService accountService = new AccountService();
    private TopicDistributionService topicDistributionService = new TopicDistributionService();

    public TopicDistributionService getTopicDistributionService() {
        return topicDistributionService;
    }

    public AccountService getAccountService() {
        return accountService;
    }


}