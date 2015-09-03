package com.platform.iot.api;

import com.platform.iot.api.balancing.LoadBalancer;
import com.platform.iot.api.balancing.Server;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.bussiness.service.AccountService;
import com.platform.iot.api.console.Console;
import com.platform.iot.api.monitoring.ServerMonitor;
import com.platform.iot.api.storage.ProducerService;
import com.platform.iot.api.storage.TopicService;
import com.platform.iot.api.storage.UserService;
import com.platform.iot.api.web.HttpServerThread;
import com.platform.iot.producer.EventLogger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicDistributionApplication {


    public static final long DEFAULT_PRODUCER_ID = 1l;
    public static AnnotationConfigApplicationContext context = null;

    static {
        context = new AnnotationConfigApplicationContext();
        context.register(JPAConfig.class);
        context.refresh();
    }
    private static Server server;

    // websocket server instance
    private WebSocketServer webSocketServer = new WebSocketServer(
            Config.INSTANCE.getServerPort());

    private static TopicDistributionApplication runningInstance;



    public static TopicDistributionApplication getRunningInstance() {
        return runningInstance;
    }

    public void start() throws Exception {
        runningInstance = this;
        Thread consoleThread = new Thread(new Console());
        consoleThread.setName("Console");
        consoleThread.start();

        Thread.currentThread().setName("WebSocket Server main");
        initTopics();
        initUsers();
        loadUsers();
        loadProducers();
        EventLogger eventLogger = context.getBean(EventLogger.class);
        eventLogger.startEventDispatcherScheduler();

        // Start the load balancer
        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        loadBalancer.registerServer();
        loadBalancer.startAliveThreadScheduler();

        new HttpServerThread().start();

        initMonitors();
        webSocketServer.run();
    }

    public void initMonitors() {

        ServerMonitor serverMonitor = ServerMonitor.createAndRegister(server);
        serverMonitor.setServer(server);
        serverMonitor.initMonitor();
    }


    public void loadUsers(){
        UserService userService = context.getBean(UserService.class);
        List<User> users = userService.findAll();
        for(User user : users){
            user.setStatus(User.Status.INACTIVE);
            MemoryStorage.INSTANCE.getUsers().put(user.getToken(), user);
        }
    }

    public void loadProducers(){
        ProducerService producerService = context.getBean(ProducerService.class);
        MemoryStorage.INSTANCE.setProducers(producerService.findAll());
    }

    public void initUsers() {
        User u1 = new User("a", "b", AccountService.generateToken("john_smith"), "John Smith", "johnsmith@gmail.com", User.UserType.NORMAL, "England");
        User u2 = new User("anne_hill", "pass2", AccountService.generateToken("anne_hill"), "Anne Hill", "anne_hill@gmail.com", User.UserType.NORMAL, "Poland");
        User u3 = new User("elli_james", "pass3", AccountService.generateToken("elli_james"), "Elli James", "elli_james@gmail.com", User.UserType.NORMAL, "Poland");
        User u4 = new User("admin", "admin", AccountService.generateToken("admin"), "admin", "admin@gmail.com", User.UserType.ADMIN, "Romania");
        UserService userService = context.getBean(UserService.class);
        userService.create(u1);
        userService.create(u2);
        userService.create(u3);
        userService.create(u4);
    }

    public void initTopics(){
        Topic t1 = new Topic("TLV", "Banca Transilvania S.A.", 13.45, true, DEFAULT_PRODUCER_ID);
        Topic t2 = new Topic("SNP","OMV Petrom S.A.", 78.46, true, DEFAULT_PRODUCER_ID);
        Topic t3 = new Topic("FP", "SC Fondul Proprietatea SA", 100.89, true, DEFAULT_PRODUCER_ID);
        Topic t4 = new Topic("BRD", "BRD - Groupe Societe Generale S.A", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t5 = new Topic("TGN", "S.N.T.G.N. Transgaz S.A.", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t6 = new Topic("TEL", "C.N.T.E.E. Transelectrica", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t7 = new Topic("BIO", "Biofarm S.A.", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t8 = new Topic("BVB", "SC Bursa de Valori București SA", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t9 = new Topic("ELMA", "Electromagnetica S.A. București", 79.45, true, DEFAULT_PRODUCER_ID);
        Topic t10 = new Topic("BKR", "S.S.I.F. Broker S.A.", 79.45, true, DEFAULT_PRODUCER_ID);
        Map<String, Topic> topicHashMap = new HashMap<String, Topic>();
        TopicService service= context.getBean(TopicService.class);
        t1 = service.create(t1);
        t2 = service.create(t2);
        t3 = service.create(t3);
        t4 = service.create(t4);
        t5 = service.create(t5);
        t6 = service.create(t6);
        t7 = service.create(t7);
        t8 = service.create(t8);
        t9 = service.create(t9);
        t10 = service.create(t10);
        topicHashMap.put(t1.getCode(), t1);
        topicHashMap.put(t2.getCode(), t2);
        topicHashMap.put(t3.getCode(), t3);
        topicHashMap.put(t4.getCode(), t4);
        topicHashMap.put(t5.getCode(), t5);
        topicHashMap.put(t6.getCode(), t6);
        topicHashMap.put(t7.getCode(), t7);
        topicHashMap.put(t8.getCode(), t8);
        topicHashMap.put(t9.getCode(), t9);
        topicHashMap.put(t10.getCode(), t10);

        MemoryStorage.INSTANCE.setTopics(topicHashMap);
//        User user = new User();
//        user.setUsername("magda");
//        user.setChannelId(4);
//        user.setToken("23423423");
//        userService = TopicDistributionApplication.context.getBean(UserService.class);
//        userService.create(user);

    }

    public static Server getServer() {
        if (server == null) {
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                server = new Server(hostName, Config.INSTANCE.getServerPort(), Config.INSTANCE.getMinClientVersionSupported());
            } catch (UnknownHostException e) {
                return null;
            }
        }

        return server;
    }

    /**
     * Shutsdown the application
     * Websocket server is stopped
     *
     * @throws Exception on any error encountered
     */
    public void shutdown() throws Exception {
        if (runningInstance != this) {
            throw new RuntimeException("Different application can not shutdown current instance!");
        }
        webSocketServer.shutdown();

        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        loadBalancer.unregisterServer();

        webSocketServer.shutdown();
        System.exit(0);
    }

    public boolean isWebsocketServerRunning() {
        return webSocketServer.isRunning();
    }


    /**
     * Starts migration of all users to other servers
     *
     * @throws Exception
     */
    public List<User> migrate() throws Exception {
        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        return loadBalancer.migrate();
    }

    /**
     * Stop the server from accepting any new connections
     */
    public void freeze() {
        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        loadBalancer.freeze(getServer());
    }

    /**
     * Allow the server to accept any new connections
     */
    public void unfreeze() {
        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        loadBalancer.unfreeze(getServer());
    }

}

