package backup.service;

import backup.bussiness.model.User;
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

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User findById(long id) {
        return userRepository.findOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public User delete(long id) throws Exception {
        User deletedUser = userRepository.findOne(id);
        if (deletedUser == null)
            throw new Exception();
        userRepository.delete(deletedUser);
        return deletedUser;
    }

    @Transactional
    public List<User> findAll() {
        if (userRepository != null) {
            return userRepository.findAll();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        User updatedUser = userRepository.findOne(user.getId());

        if (updatedUser == null)
            throw new RuntimeException("No user found for id " + user.getId());

        updatedUser.setToken(user.getToken());
        updatedUser.setUsername(user.getUsername());

        return updatedUser;
    }

    public User findByToken(String token) {
//        return userRepository.findByToken(token);
        return null;
    }

}
