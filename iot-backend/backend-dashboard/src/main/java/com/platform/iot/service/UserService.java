package com.platform.iot.service;

import com.platform.iot.domain.User;

/**
 * Created by ioan.vranau on 12/15/2015.
 */
public class UserService {

    public User getDefaultUser() {
        User user = new User();
        user.setFirstName("JonFromREST");
        user.setLastName("DoeFromREST");
        return user;
    }
}
