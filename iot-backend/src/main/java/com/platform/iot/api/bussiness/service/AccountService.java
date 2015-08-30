package com.platform.iot.api.bussiness.service;

import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.balancing.ClientVersion;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.bussiness.model.User;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import com.platform.iot.api.message.MessageDispatcher;
import com.platform.iot.api.message.client.*;
import com.platform.iot.api.message.server.ErrorMessage;
import com.platform.iot.api.message.server.TokenMessage;
import com.platform.iot.api.monitoring.MonitoringServiceLocator;
import com.platform.iot.api.monitoring.ServerMonitor;
import com.platform.iot.api.storage.UserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Magda Gherasim
 */
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private List<User> migratedUsers = new ArrayList<>();
    ServerMonitor serverMonitor = MonitoringServiceLocator.getInstance().getServerMonitor();

    public void register(Channel channel, RegisterMessage registerMessage) {
        UserService userService = TopicDistributionApplication.context.getBean(UserService.class);
        if(userService.findByUsername(registerMessage.getUsername()) != null) {
            ErrorMessage userAlreadyExists = new ErrorMessage(new ApplicationException(ExceptionType.USER_ALREADY_EXISTS));
            channel.write(userAlreadyExists);
            return;
        }
        User user = new User();
        user.setStatus(User.Status.ACTIVE);
        user.setClientVersion(ClientVersion.fromString("1.0.0"));
        user.setUsername(registerMessage.getUsername());
        user.setPassword(registerMessage.getPassword());
        user.setEmail(registerMessage.getEmail());
        user.setToken(generateToken(registerMessage.getUsername()));
        MemoryStorage.INSTANCE.getUsers().put(user.getToken(), user);
        user.setTopicsChannel(channel);
        user.setProducerId(TopicDistributionApplication.DEFAULT_PRODUCER_ID);
        MemoryStorage.INSTANCE.getLoggedUsers().add(channel);
        MemoryStorage.INSTANCE.getNotLoggedUsers().remove(channel);
        userService.create(user);
        ServiceManager.INSTANCE.getTopicDistributionService().subscribeDefaultTopics(user);
        MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.RegisterMessage(user.getUsername(), user.getPassword()));
    }

    public void login(Channel channel, LoginMessage loginMessage) {
        //TODO: cauta in DB
        String username = loginMessage.getUsername(), password = loginMessage.getPassword();
        User user = null;
        Collection<User> users = MemoryStorage.INSTANCE.getUsers().values();
        for (User aUser : users){
            if (aUser.getUsername().equals(username) && aUser.getPassword().equals(password)){
                user = aUser;
            }
        }
        if (user == null) {
            UserService userService = TopicDistributionApplication.context.getBean(UserService.class);
            user = userService.findByUsername(loginMessage.getUsername());
            if (user!= null) {
                if (!user.getPassword().equals(loginMessage.getPassword())) {
                    user = null;
                }
                else {
                    MemoryStorage.INSTANCE.getUsers().put(user.getToken(), user);
                }
            }
        }

        if (user != null) {

            final boolean enabledUser = isUserEnabled(user);
            if(enabledUser) {

                user.setTopicsChannel(channel);
                user.setStatus(User.Status.ACTIVE);
                if(user.getTopics() == null ||  user.getTopics().size() == 0 ) {
                    ServiceManager.INSTANCE.getTopicDistributionService().subscribeDefaultTopics(user);
                }
                MessageDispatcher.sendMessageToUser(user, new TokenMessage(user));
                serverMonitor.updateMonitor();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = sdf.format(user.getDisableddate());  // dt i
                channel.write(new ErrorMessage(ApplicationException.create(ExceptionType.DISABLED_USER, "Your account is disabled! Please wait until " +formatedDate )));
                logger.error("User Disabled!");
            }
        } else {
            channel.write(new ErrorMessage(new ApplicationException(ExceptionType.WRONG_USERNAME_OR_PASSWORD)));
            logger.error("Trying to login unregistered user");
        }
    }

    private boolean isUserEnabled(User user) {
        final Date disableddate = user.getDisableddate();
        final Date today = new Date();
        return disableddate == null || disableddate.toString().equals("") || today.after(disableddate);
    }

    public void logout(Channel channel, LogoutMessage logoutMessage) {
        User user = MemoryStorage.INSTANCE.getUserByToken(logoutMessage.getToken());
        user.setStatus(User.Status.INACTIVE);
//        MemoryStorage.INSTANCE.getUsers().remove(logoutMessage.getToken());
        channel.write(new com.platform.iot.api.message.server.LogoutMessage());
        serverMonitor.updateMonitor();
    }

    public String generateToken(String seed) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(seed.getBytes(), 0, seed.length());
            return String.valueOf(new BigInteger(1, m.digest()).toString(16));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doMigration(Channel channel, MigrationMessage migrationMessage) {
        register(channel, migrationMessage);
        Runnable migrationThread = new Runnable() {
            @Override
            public void run() {
            }
        };
        ScheduledFuture<?> migrationThreadHandler = Executors.newSingleThreadScheduledExecutor()
                .schedule(migrationThread, 5, TimeUnit.SECONDS);
    }


    public void lock(Channel channel, LockMessage lockMessage) {
        User user = MemoryStorage.INSTANCE.getUserByToken(lockMessage.getToken());
        user.setStatus(User.Status.INACTIVE);
        MemoryStorage.INSTANCE.getUsers().remove(lockMessage.getToken());
        channel.write(new com.platform.iot.api.message.server.LockMessage());
        serverMonitor.updateMonitor();
    }

    public void updateUserProfileMessage(Channel channel, UpdateUserProfileMessage updateUserProfileMessage) {
        UserService userService = TopicDistributionApplication.context.getBean(UserService.class);
        User user = MemoryStorage.INSTANCE.getUserByToken(updateUserProfileMessage.getToken());

        if(user != null) {
            user.setName(updateUserProfileMessage.getName());
            user.setCountry(updateUserProfileMessage.getCountry());
            user.setEmail(updateUserProfileMessage.getEmail());
            user.setPassword(updateUserProfileMessage.getPassword());

            userService.update(user);
            MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.UpdateUserProfileMessage(com.platform.iot.api.message.server.UpdateUserProfileMessage.Status.SUCCESS));
        } else {
            channel.write(new ErrorMessage(new ApplicationException(ExceptionType.CANNOT_UPDATE_USER)));
            logger.error("Cannot find user in memory");
        }
    }

    public void disableAccount(Channel channel, DisableAccountMessage disableAccountMessage) {
        UserService userService = TopicDistributionApplication.context.getBean(UserService.class);
        User user = MemoryStorage.INSTANCE.getUserByToken(disableAccountMessage.getToken());

        if(user != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 7 * disableAccountMessage.getWeeksNumber());  // number of days to add
            final Date time = c.getTime();
            user.setDisableddate(time);

            userService.disable(user);
            MessageDispatcher.sendMessageToUser(user, new com.platform.iot.api.message.server.UpdateUserProfileMessage(com.platform.iot.api.message.server.UpdateUserProfileMessage.Status.SUCCESS));
        } else {
            channel.write(new ErrorMessage(new ApplicationException(ExceptionType.CANNOT_DISABLE_USER_ACCOUNT)));
            logger.error("Cannot find user in memory");
        }
    }
}
