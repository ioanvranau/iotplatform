package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserRepository userRepository;

//    private EntityManagerFactory emf;
//
//    @PersistenceUnit
//    public void setEntityManagerFactory(EntityManagerFactory emf) {
//        this.emf = emf;
//    }

    @Override
    @Transactional
    public User create(User user) {

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = ApplicationException.class)
    public User delete(long id) throws ApplicationException {
        User deletedUser = userRepository.findOne(id);
        if (deletedUser == null)
            throw new ApplicationException(ExceptionType.USER_NOT_FOUND);
        userRepository.delete(deletedUser);
        return deletedUser;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        if (userRepository != null) {
            return userRepository.findAll();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        User updatedUser = userRepository.findOne(user.getId());

        if (updatedUser == null)
            throw new RuntimeException("No user found for id " + user.getId());

        updatedUser.setToken(user.getToken());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setName(user.getName());
        updatedUser.setCountry(user.getCountry());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        return updatedUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User disable(User user) {
        User updatedUser = userRepository.findOne(user.getId());

        if (updatedUser == null)
            throw new RuntimeException("No user found for id " + user.getId());

        updatedUser.setDisableddate(user.getDisableddate());
        return updatedUser;
    }

    @Override
    public User findByToken(String token) {
//        return userRepository.findByToken(token);
        return null;
    }

}
