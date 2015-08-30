package com.platform.iot.api.bussiness;

import com.platform.iot.api.bussiness.model.Producer;
import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.bussiness.model.TopicGroup;
import com.platform.iot.api.bussiness.model.User;
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
    private Map<String, User> users = new HashMap<String, User>();    // <token, user>
    private List<Producer> producers = new ArrayList<>(); // <producers>

    public ChannelGroup getNotLoggedUsers() {
        return notLoggedUsers;
    }

    public ChannelGroup getLoggedUsers() {
        return loggedUsers;
    }

    public List<Topic> getTopics() {
        return new ArrayList<Topic>(topics.values());
    }

    public List<Topic> getTopicsByProducerId(long producerId) {
        List<Topic> selectedTopics = new ArrayList<>();
        for (Topic topic : topics.values()) {
            if (topic.getProducerId() == producerId) {
                selectedTopics.add(topic);
            }
        }
        return selectedTopics;
    }

    public void setTopics(Map<String, Topic> topics) {
        this.topics = topics;
    }

    public Topic getTopicByCodeAndProducerId(String code, long producerId) {
        for (Topic topic : topics.values()) {
            if (topic.getCode().equals(code) && topic.getProducerId() == producerId) {
                return topic;
            }
        }
        return null;
    }

    public Map<Topic, TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public TopicGroup getTopicGroupByTopic(Topic topic) {
        return topicGroups.get(topic);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public List<User> getActiveUsers(){
        List<User> users = new ArrayList<>();
        for(User user : getUsers().values()){
            if (user.getStatus() == User.Status.ACTIVE){
                users.add(user);
            }
        }
        return users;
    }

    public List<User> getInactiveUsers(){
        List<User> users = new ArrayList<>();
        for(User user : getUsers().values()){
            if (user.getStatus() != User.Status.ACTIVE){
                users.add(user);
            }
        }
        return users;
    }

    public User getUserByChannelId(int channelId) {
        return users.get(channelId);
    }

    public User getUserByToken(String token) {
        return users.get(token);
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void setProducers(List<Producer> producers) {
        this.producers = producers;
    }
}
