package com.platform.iot.api.bussiness.model;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.balancing.ClientVersion;
import com.platform.iot.api.balancing.Migration;
import com.platform.iot.api.bussiness.MemoryStorage;
import io.netty.channel.Channel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Magda on 17.05.2014.
 */
@Entity
@javax.persistence.Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "token"})})
public class User implements Serializable {
    public static final String FIELD_ID = "id";
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_USER_TYPE = "usertype";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_NAME = "name";

    public boolean isSubscriebed(Topic topic) {
        for (Topic topic1 : getTopics()) {
            if (topic1.getId().equals(topic.getId())) {
                return true;
            }
        }
        return false;
    }

    public enum Status {ACTIVE, MIGRATION_NOTIFIED, INACTIVE}

    public enum UserType {NORMAL, ADMIN}

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "usertype", length = 64, nullable = false)
    private UserType usertype;

    @Column(name = "token", length = 64, nullable = false)
    private String token;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "country", length = 64)
    private String country;

    @Column(name = "disableddate", length = 64)
    private Date disableddate;

    @Column(name = "clientVersion", length = 25, nullable = true)
    @Access(value = AccessType.FIELD)
    private String clientVersion;

    @Column(name = "producerId")
    private Long producerId;

    @Transient
    private Channel topicsChannel;

    @Transient
    private List<Topic> topics = new ArrayList<>();

    @Transient
    private List<TopicGroup> topicGroups = new ArrayList<>();

    @Transient
    private Migration migration = new Migration();

    @Transient
    private Status status;


    public User() {
    }

    public User(String username, String password, String token, String name, String email, UserType usertype, String country) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.name = name;
        this.email = email;
        this.producerId = TopicDistributionApplication.DEFAULT_PRODUCER_ID;
        this.usertype = usertype;
        this.country = country;
    }

    public User(Long id, String username, String password, String token, String email, UserType usertype) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        this.email = email;
        this.producerId = TopicDistributionApplication.DEFAULT_PRODUCER_ID;
        this.usertype = usertype;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setProducerId(Long producerId) {
        this.producerId = producerId;
    }

    public List<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(List<Topic> topicGroups) {
        for (Topic topic : topicGroups) {
            TopicGroup topicGroup = MemoryStorage.INSTANCE.getTopicGroupByTopic(topic);
            if (topicGroup == null) {
                topicGroup = new TopicGroup(topic);
                topicGroup.addUser(this);
            }
            this.topicGroups.add(topicGroup);
        }
    }


    public Migration getMigration() {
        return migration;
    }

    public void setMigration(Migration migration) {
        this.migration = migration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Channel getTopicsChannel() {
        return topicsChannel;
    }

    public void setTopicsChannel(Channel topicsChannel) {
        this.topicsChannel = topicsChannel;
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

    public ClientVersion getClientVersion() {
        return ClientVersion.fromString(clientVersion);
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion.toString();
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public Date getDisableddate() {
        return disableddate;
    }

    public void setDisableddate(Date disableddate) {
        this.disableddate = disableddate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", usertype=" + usertype +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
