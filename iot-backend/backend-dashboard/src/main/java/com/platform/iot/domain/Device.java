package com.platform.iot.domain;

/**
 * Created by ioan.vranau on 12/15/2015.
 */

public class Device {

    private final long id;
    private String name;
    private String avatar;
    private String content;

    public Device(long id, String name, String avatar, String content) {
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
