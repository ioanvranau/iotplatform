package com.platform.iot.api;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Main class starting Http Streaming server
 * <p/>
 * Application configuration is done in the config.properties file
 */

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public static final String SYSTEM_PARAMETER_CONFIG_FILE = "configFile";
    public static final String SYSTEM_PARAMETER_DB_CONFIG_FILE = "dbConfigFile";

    public static void main(String[] args) {
        String configFileName = System.getProperty(SYSTEM_PARAMETER_CONFIG_FILE);
        String dbConfigFileName = System.getProperty(SYSTEM_PARAMETER_DB_CONFIG_FILE);
        if (!StringUtils.isBlank(configFileName) && !new File(configFileName).exists()) {
            System.out.println("Configuration file '" + configFileName + "' could not be found!");
            return;
        }
        if (!StringUtils.isBlank(configFileName) && !new File(dbConfigFileName).exists()) {
            System.out.println("Database configuration file '" + dbConfigFileName + "' could not be found!");
            return;
        }
        if (StringUtils.isBlank(dbConfigFileName)) {
            System.setProperty(SYSTEM_PARAMETER_DB_CONFIG_FILE, "resources/jdbc.properties");
        }
        TopicDistributionApplication application = new TopicDistributionApplication();
        try {
            application.start();
        } catch (Exception e) {
            logger.error("Severe exception encountered server can not recover, stopping ...", e);
        }
    }


}
