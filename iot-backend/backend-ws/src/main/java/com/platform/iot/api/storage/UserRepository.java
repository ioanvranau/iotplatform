package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT p FROM User p WHERE p.username= :username")
    public User findByUsername(@Param("username") String username);

}
