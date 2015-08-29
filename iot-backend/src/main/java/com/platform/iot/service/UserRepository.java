package com.platform.iot.service;


import com.platform.iot.bussiness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT p FROM User p WHERE p.token = :token")
//    public User findByToken(@Param("token") String token);

}
