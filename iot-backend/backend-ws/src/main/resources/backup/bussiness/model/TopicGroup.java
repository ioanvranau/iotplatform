package backup.bussiness.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Magda on 17.05.2014.
 */
public class TopicGroup {
    private Long id;
    private Topic topic;
    private List<User> users;

    public TopicGroup() {
        initGroup();
    }

    private void initGroup() {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.users = new ArrayList<User>();
    }

    public TopicGroup(Topic topic) {
        initGroup();
        this.topic = topic;
    }


    public void addUser(User user) {
        users.add(user);
    }


    public void removeUser(User user) {
        users.remove(user);
    }


    public List<User> getUsers() {
        return users;
    }

    public Long getId() {
        return id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
