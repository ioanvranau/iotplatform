package com.platform.iot.bussiness.model;

import com.platform.iot.bussiness.MemoryStorage;
import io.netty.channel.Channel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magda on 17.05.2014.
 *
 */
@Entity
@javax.persistence.Table(name = "user") //, uniqueConstraints = {@UniqueConstraint(
//columnNames = {"username", "token"})}
public class User implements Serializable{
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_USERNAME = "username";

    public enum Status { ACTIVE, MIGRATION_NOTIFIED}

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "token", length = 64, nullable = false)
    private String token;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Column(name = "clientVersion", length = 25, nullable = true)
    @Access(value = AccessType.FIELD)
    private String clientVersion;

    @Transient
    private Channel channel;

    @Transient
    private List<Topic> topics;

    @Transient
    private List<TopicGroup> topicGroups;


    @Transient
    private Status status;

    public User() {
        topicGroups = new ArrayList<TopicGroup>();
        topics = new ArrayList<Topic>();
    }

    public List<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(List<Topic> topicGroups) {
        for(Topic topic: topicGroups){
            TopicGroup topicGroup = MemoryStorage.INSTANCE.getTopicGroupByTopic(topic);
            if(topicGroup == null){
                topicGroup = new TopicGroup(topic);
                topicGroup.addUser(this);
            }
            this.topicGroups.add(topicGroup);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                '}';
    }
}
