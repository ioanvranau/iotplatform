package com.platform;

import com.platform.iot.WebSocketServer;
import com.platform.iot.bussiness.MemoryStorage;
import com.platform.iot.bussiness.model.Topic;
import com.platform.iot.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpStreamingApplication {



    public static AnnotationConfigApplicationContext context = null;

    static {
        context = new AnnotationConfigApplicationContext();
        context.register(JPAConfig.class);
        context.refresh();
    }

    // websocket server instance
    private WebSocketServer webSocketServer = new WebSocketServer(
            WebSocketServer.DEFAULT_PORT);

    private static HttpStreamingApplication runningInstance;

    private UserService userService;

    public static HttpStreamingApplication getRunningInstance() {
        return runningInstance;
    }

    public void start() throws Exception {
        runningInstance = this;
       initTopics();
        webSocketServer.run();


    }

    public void initTopics(){
        Topic t1 = new Topic("Topic1", 13.45);
        Topic t2 = new Topic("Topic2", 78.46);
        Topic t3 = new Topic("Topic3", 100.89);
        Topic t4 = new Topic("Topic4", 79.45);
        Map<String, Topic> topicHashMap = new HashMap<String, Topic>();
        topicHashMap.put(t1.getCode(), t1);
        topicHashMap.put(t2.getCode(), t2);
        topicHashMap.put(t3.getCode(), t3);
        topicHashMap.put(t4.getCode(), t4);
        MemoryStorage.INSTANCE.setTopics(topicHashMap);
//        User user = new User();
//        user.setUsername("magda");
//        user.setChannelId(4);
//        user.setToken("23423423");
//        userService = HttpStreamingApplication.context.getBean(UserService.class);
//        userService.create(user);

    }
}

