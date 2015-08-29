package com.platform.iot.service;


import com.platform.iot.bussiness.model.User;

import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface UserService {

    User create(User user);

    User delete(long id) throws Exception;

    List<User> findAll();

    User update(User user);

    User findById(long id);

    User findByToken(String token);
}
