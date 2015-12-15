package backup;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read configuration properties from config.properties file
 *
 */
public enum Config {

    INSTANCE, Config;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private boolean initialized = false;
    private Properties properties = new Properties();

    private void init() {
        String configFile = null;
        InputStream configStream = null;
        if (StringUtils.isBlank(configFile)) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            configStream = loader.getResourceAsStream("config.properties");
        } else {
            try {
                configStream = new FileInputStream(configFile);
            } catch (FileNotFoundException e) {
                logger.error("could not read config file '" + configFile + "'!", e);
                throw new RuntimeException("could not read config file '" + configFile + "'!", e);
            }
        }
        try {
            properties.load(configStream);
        } catch (IOException ioException) {
            throw new RuntimeException("Could not load configuration file!", ioException);
        }
        initialized = true;
    }

    /**
     * Gets the server name
     *
     * @return the server name
     */
    public String getServerName() {
        if (!initialized) {
            init();
        }
        String serverNameString = properties.getProperty("server-name");
        return serverNameString;
    }

    /**
     * Gets the port the server is running on
     *
     * @return the port server
     */
    public int getServerPort() {
        if (!initialized) {
            init();
        }
        String serverPortString = properties.getProperty("server-port");
        return Integer.valueOf(serverPortString);
    }

    /**
     * Gets the redis server hostname
     *
     * @return redis hostname
     */
    public String getRedisHost() {
        if (!initialized) {
            init();
        }
        return properties.getProperty("redisHost");
    }

    /**
     * gets the redis server port number
     *
     * @return the redis port number
     */
    public int getRedisPort() {
        if (!initialized) {
            init();
        }
        return Integer.valueOf(properties.getProperty("redisPort"));
    }

    /**
     * Tells if the redis data should be cleared or not on start
     *
     * @return 1 if redis data should be erased
     */
    public int getRedisClear() {
        if (!initialized) {
            init();
        }
        if (properties.getProperty("redisClear") != null) {
            return Integer.valueOf(properties.getProperty("redisClear"));
        } else {
            return 0;
        }
    }

    /**
     * Gets the port of the public API
     *
     * @return the public API web server port
     */
    public int getWebAPIPort() {
        if (!initialized) {
            init();
        }
        String webAPIPortString = properties.getProperty("web-api-port");
        return Integer.valueOf(webAPIPortString);
    }

    /**
     * Gets the port the server console is running on
     *
     * @return the console port
     */
    public int getConsolePort() {
        if (!initialized) {
            init();
        }
        String consolePortString = properties.getProperty("console-port");
        return Integer.valueOf(consolePortString);
    }


}
