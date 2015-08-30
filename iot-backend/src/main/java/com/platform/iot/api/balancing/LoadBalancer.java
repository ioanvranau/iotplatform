package com.platform.iot.api.balancing;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.message.MessageDispatcher;
import com.platform.iot.api.message.server.MigrationMessage;
import com.platform.iot.api.monitoring.MonitoringServiceLocator;
import com.platform.iot.api.storage.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Performs the load balancing operations
 * Keeps the information about each server loading
 *
  */
@Component
public class LoadBalancer {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancer.class);

    @Autowired
    AliveThread aliveThread;

    @Autowired
    RedisService redisService;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> aliveThreadHandle;

    /**
     * Starts a scheduler which will ping the load balancer to inform that server is alive
     */
    public void startAliveThreadScheduler() {
        aliveThreadHandle = scheduler
                .scheduleAtFixedRate(aliveThread, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * Registers the server in load balancer
     */
    public void registerServer() {
        Server server = TopicDistributionApplication.getServer();
        redisService.registerServer(server, MemoryStorage.INSTANCE.getUsers().size());
        logger.info("registered " + server + " in load balancer");
    }

    /**
     * Unregisters the server from load balancer
     */
    public void unregisterServer() {

        aliveThreadHandle.cancel(true);

        redisService.unregisterServer(TopicDistributionApplication.getServer());
        logger.info("unregistered " + TopicDistributionApplication.getServer() + " from load balancer");
    }

    /**
     * Starts migration process, then checks if all users are notified about migration
     *
     * @return the user which were not notified about migration
     */
    public List<User> migrate() {
        List<User> notNotifiedUsers = new ArrayList<User>();

        //create a countdown of migrated users, can shutdown once all have migrated
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ScheduledFuture<?> migrationThreadHandle = scheduler
                .scheduleAtFixedRate(new MigrationThread(countDownLatch), 10, 10, TimeUnit.SECONDS);

        Collection<User> users = MemoryStorage.INSTANCE.getUsers().values();
        for (User user : users) {
            Server server = getBestServer(user.getClientVersion(), TopicDistributionApplication.getServer());
            if (server == null) {
                logger.warn("No available server found for " + user);
                continue;
            }
            user.getMigration().setServer(server);
            user.getMigration().setStarted(true);
            MessageDispatcher.sendMessageToUser(user, MigrationMessage.create(user));
            user.setStatus(User.Status.MIGRATION_NOTIFIED);
            //inform load balancer that message was sent to client for user
        }

        boolean success = false;
        try {
            success = countDownLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }

        if (!success) {
            //return list of not migrated users
            migrationStatus(false, notNotifiedUsers);
        }

        return notNotifiedUsers;
    }

    /**
     * Gives a feedback about the migration status
     *
     * @param returnOnFirstNonNotified if is true the nonNotifiedusers list is not filled with all non notified users
     * @param nonNotifiedUsers         a list of users not notified
     * @return true if all users notified about migration, false otherwise
     */
    public boolean migrationStatus(boolean returnOnFirstNonNotified, List<User> nonNotifiedUsers) {
        Collection<User> users = MemoryStorage.INSTANCE.getUsers().values();
        for (User user : users) {

            if (user.getStatus() != User.Status.MIGRATION_NOTIFIED) {
                if (returnOnFirstNonNotified) {
                    return false;
                } else {
                    nonNotifiedUsers.add(user);
                }
            }
        }

        return nonNotifiedUsers.isEmpty();
    }

    /**
     * Make this server no longer available for incoming users
     */
    public void freeze(Server server) {
        redisService.freezeServer(server);
        Server currentServer = TopicDistributionApplication.getServer();
        if (server.getAddress().equals(currentServer.getAddress()) && server.getPort() == currentServer.getPort()){
            MonitoringServiceLocator.getInstance().getServerMonitor().updateFreezed("YES");
        }
    }

    /**
     * Make this server available for incoming users
     */
    public void unfreeze(Server server) {
        redisService.unfreezeServer(server);
        Server currentServer = TopicDistributionApplication.getServer();
        if (server.getAddress().equals(currentServer.getAddress()) && server.getPort() == currentServer.getPort()){
            MonitoringServiceLocator.getInstance().getServerMonitor().updateFreezed("YES");
        }
    }

    /**
     * Cheks if server accepts new connections from users
     *
     * @param server server to check if freezed
     * @return true if server does not accept new connections, false otherwise
     */
    public boolean isFreezed(Server server) {
        return redisService.isServerFreezed(server);
    }

    /**
     * Gets the best available server for the provided client version
     * Best server is the one capable of handling that client version, is not freezed and has the minimum load
     *
     * @param clientVersion the client version
     * @return thebest server at the moment of call
     */
    public Server getBestServer(ClientVersion clientVersion) {
        return getBestServer(clientVersion, new Server[0]);
    }

    /**
     * Finds the least loaded server
     * Exclude from search the servers provided in <code>exceptedServers</code> parameter
     *
     * @param clientVersion   the version of client trying to find a server
     * @param exceptedServers server to exclude from search
     * @return least loaded server
     */
    public Server getBestServer(ClientVersion clientVersion, Server... exceptedServers) {
        if (clientVersion == null) {
            return null;
        }
        List<Server> servers = redisService.getAvailableServers();
        Server bestServer = null;
        for (Server server : servers) {
            for (Server exceptedServer : exceptedServers) {
                if (server.equals(exceptedServer)) {
                    continue;
                }
            }
            if (clientVersion != null && server.getMinClientVersionSupported().compareTo(clientVersion) < 0) {
                logger.warn("server is supporting client versions from " + server.getMinClientVersionSupported() +
                        ", client trying to connect has version " + clientVersion);
                continue;
            }
            if (isFreezed(server)) {
                continue;
            }
            if (bestServer == null || server.getNbUsers() < bestServer.getNbUsers()) {
                bestServer = server;
            }
        }
        logger.info("return least loaded " + bestServer);

        return bestServer;
    }

    /**
     * Registers a new user connection to server
     */
    public void connect() {
        redisService.serverIncrementLoad(TopicDistributionApplication.getServer());
    }

    /**
     * Marks a user disconnection from server
     */
    public void disconnect() {
        redisService.serverDecrementLoad(TopicDistributionApplication.getServer());
    }


    public ProducerServer getTopicQueue() {
        return new ProducerServer("tcp://localhost:61616","platform.producer.event.queue");
    }
}
