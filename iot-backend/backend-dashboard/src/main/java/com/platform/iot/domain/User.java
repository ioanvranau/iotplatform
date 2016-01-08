package com.platform.iot.domain;

/**
 * Created by ioan.vranau on 12/15/2015.
 */

import javax.xml.bind.annotation.XmlRootElement;

public class User {

    private String name;
    private String avatar;
    private String content;
    private final long id;

    public User(long id, String name, String avatar, String content) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
