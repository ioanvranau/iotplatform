package com.platform.iot.api.storage;

/**
 * Created by magdalena.gherasim on 6/23/2014.
 */

import com.platform.iot.api.Config;
import com.platform.iot.api.balancing.ClientVersion;
import com.platform.iot.api.balancing.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
  */
@Service
public class RedisService {

    private static String HOST;
    private static int PORT;
    private static final int TIMEOUT = 3000;

    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static Object lock = new Object();

    private static final String LOAD_BALANCER_SERVERS_KEY = "load-balancer-servers-key";
    private static final String FREEZE_SERVERS_KEY = "freeze-servers-key";

    public RedisService() {
        HOST = Config.INSTANCE.getRedisHost();
        PORT = Config.INSTANCE.getRedisPort();
    }


    public boolean clearAllRedisData() {
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            jedis.flushAll();
        } catch (Exception e) {
            logger.error("Could not clear user XP data from redis ", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
        return true;

    }

    private String getServerKey(Server server) {
        return "server:" + server.getAddress() + ":" + server.getPort();
    }

    private String getServerKeyClientVersion(String serverKey) {
        return serverKey + ":" + "client-version";
    }

    private String getServerValue(int nbUsers) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date()) + ":" + String.valueOf(nbUsers);
    }

    public boolean serverAlive(Server server) {
        logger.info("notify that " + server + " is alive");
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key)) {
                    throw new RuntimeException("key should be in load balances servers!");
                }
                if (!jedis.exists(key)) {
                    throw new RuntimeException("server key should be there!");
                } else {
                    jedis.expire(key, 60);
                }
                String keyClientVersion = getServerKeyClientVersion(key);
                jedis.expire(keyClientVersion, 60);
            }
        } catch (Exception e) {
            logger.error("could not notify storage that server alive", e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
        return true;
    }

    public void registerServer(Server server, int nbUsers) {
        logger.info("register " + server + " in storage");
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key)) {
                    jedis.sadd(LOAD_BALANCER_SERVERS_KEY, key);
                }
                String keyClientVersion = getServerKeyClientVersion(key);
                jedis.setex(keyClientVersion, 60, server.getMinClientVersionSupported().toString());
                jedis.setex(key, 60, String.valueOf(nbUsers));
            }
        } catch (Exception e) {
            logger.error("could not notify storage that server alive", e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
    }

    public void unregisterServer(Server server) {
        logger.info("unregister " + server + " in storage");
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key)) {
                    return;
                }
                jedis.srem(LOAD_BALANCER_SERVERS_KEY, key);
                if (!jedis.exists(key)) {
                    return;
                }
                jedis.del(key);
            }
        } catch (Exception e) {
            logger.error("could not notify storage that server alive", e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
    }

    public List<Server> getAvailableServers() {
        logger.info("get servers");
        List<Server> servers = new ArrayList<Server>();
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
            for (String serverKey : serverKeys) {
                String[] serverValues = StringUtils.split(serverKey, ":");
                String keyClientVersion = getServerKeyClientVersion(serverKey);
                String clientVersion = jedis.get(keyClientVersion);
                Server server = new Server(serverValues[1], Integer.valueOf(serverValues[2]),
                        ClientVersion.fromString(clientVersion));
                if (!jedis.exists(getServerKey(server))) {
                    continue;
                }
                server.setNbUsers(Integer.valueOf(jedis.get(getServerKey(server))));
                servers.add(server);
            }
        } catch (Exception e) {
            logger.error("could not notify storage that server alive", e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }

        return servers;
    }

    public void serverIncrementLoad(Server server) {
        logger.info("increment " + server + " load");
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key) || !jedis.exists(key)) {
                    return;
                }
//                Integer nbUsers = Integer.valueOf(jedis.get(key));
//                nbUsers++;
//                jedis.setex(key, 60, String.valueOf(nbUsers));
                jedis.incr(key);
//                jedis.flushDB();
            }
        } catch (Exception e) {
            logger.error("could not notify storage that server alive", e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
    }

    public void serverDecrementLoad(Server server) {
        logger.info("decrement " + server + " load");
        synchronized (server) {
            Jedis jedis = null;
            try {
                jedis = new Jedis(HOST, PORT, TIMEOUT);
                Set<String> serverKeys = jedis.smembers(LOAD_BALANCER_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key) || !jedis.exists(key)) {
                    return;
                }
//                Integer nbUsers = Integer.valueOf(jedis.get(key));
//                nbUsers--;
//                jedis.setex(key, 60, String.valueOf(nbUsers));
                jedis.decr(key);
//                jedis.flushAll();
            } catch (Exception e) {
                logger.error("could not notify storage that server alive", e);
            } finally {
                if (jedis != null) {
                    jedis.quit();
                }
            }
        }
    }

    public void freezeServer(Server server) {
        logger.info("freeze " + server);
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(FREEZE_SERVERS_KEY);
                String key = getServerKey(server);
                if (!serverKeys.contains(key)) {
                    jedis.sadd(FREEZE_SERVERS_KEY, key);
                }
            }
        } catch (Exception e) {
            logger.error("could not freeze server" + server, e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
    }

    public boolean isServerFreezed(Server server) {

        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(FREEZE_SERVERS_KEY);
                String key = getServerKey(server);
                if(serverKeys.contains(key)){
                    logger.info("is freezed " + server);
                    return true;
                }
                else {
                    logger.info("is NOT freezed " + server);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("could not check if freezed " + server, e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }

        return true;
    }

    public void unfreezeServer(Server server) {
        logger.info("unfreeze " + server);
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT, TIMEOUT);
            synchronized (server) {
                Set<String> serverKeys = jedis.smembers(FREEZE_SERVERS_KEY);
                String key = getServerKey(server);
                if (serverKeys.contains(key)) {
                    jedis.srem(FREEZE_SERVERS_KEY, key);
                }
            }
        } catch (Exception e) {
            logger.error("could not unfreeze server" + server, e);
        } finally {
            if (jedis != null) {
                jedis.quit();
            }
        }
    }

    public static void main(String[] args) {
        RedisService redisService = new RedisService();
        if (Config.INSTANCE.getRedisClear() == 1) {
            redisService.clearAllRedisData();
        }
   }
}
