package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.exception.ApplicationException;

import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface UserService {

    User create(User user);

    User delete(long id) throws ApplicationException;

    List<User> findAll();

    User update(User user);
    User disable(User user);

    User findById(long id);

    User findByToken(String token);

    User findByUsername(String username);
}
