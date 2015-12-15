package backup;

import backup.bussiness.MemoryStorage;
import backup.bussiness.model.Topic;
import backup.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class HttpStreamingApplication {


    public static AnnotationConfigApplicationContext context = null;

    static {
        context = new AnnotationConfigApplicationContext();
        context.register(JPAConfig.class);
        context.refresh();
    }

    // websocket server instance
    private static WebSocketServer webSocketServer = new WebSocketServer(
            Config.INSTANCE.getServerPort());


    private UserService userService;

    public static void main(String[] args) {
        try {
            initTopics();
            webSocketServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initTopics() {
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

