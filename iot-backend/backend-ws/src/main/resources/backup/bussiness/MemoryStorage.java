package backup.bussiness;

import backup.bussiness.model.Topic;
import backup.bussiness.model.TopicGroup;
import backup.bussiness.model.User;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Magda Gherasim
 */
public enum MemoryStorage {
    INSTANCE, Config;
    //keeps all connected users which are not logged in
    private ChannelGroup notLoggedUsers = new DefaultChannelGroup("not-logged-users");
    //keeps all connected users which are logged in
    private ChannelGroup loggedUsers = new DefaultChannelGroup("logged-users");
    private Map<String, Topic> topics = new HashMap<String, Topic>();
    private Map<Topic, TopicGroup> topicGroups = new HashMap<Topic, TopicGroup>();
    private Map<Integer, User> users = new HashMap<Integer, User>();    // <channelId, user>

    public ChannelGroup getNotLoggedUsers() {
        return notLoggedUsers;
    }

    public ChannelGroup getLoggedUsers() {
        return loggedUsers;
    }

    public List<Topic> getTopics() {
        return new ArrayList<Topic>(topics.values());
    }

    public void setTopics(Map<String, Topic> topics) {
        this.topics = topics;
    }

    public Topic getTopicByCode(String code) {
        return topics.get(code);
    }

    public Map<Topic, TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public TopicGroup getTopicGroupByTopic(Topic topic) {
        return topicGroups.get(topic);
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public User getUserByChannelId(int channelId) {
        return users.get(channelId);
    }

    public User getUserByToken(String token) {
        for (User user : users.values()) {
            if (user.getToken().equals(token)) {
                return user;
            }
        }
        return null;
    }
}
