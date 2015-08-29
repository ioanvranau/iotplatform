package com.platform.iot.bussiness.service;

import com.platform.HttpStreamingApplication;
import com.platform.iot.bussiness.MemoryStorage;
import com.platform.iot.bussiness.model.User;
import com.platform.iot.message.MessageDispatcher;
import com.platform.iot.message.client.LoginMessage;
import com.platform.iot.message.client.RegisterMessage;
import com.platform.iot.message.server.TokenMessage;
import com.platform.iot.service.UserService;
import io.netty.channel.Channel;
import org.omg.PortableInterceptor.ACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Magda Gherasim
 */
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public void register(Channel channel, RegisterMessage registerMessage) {
        User user = new User();
        user.setStatus(User.Status.ACTIVE);
        user.setUsername(registerMessage.getUsername());
        user.setPassword(registerMessage.getPassword());
        user.setEmail(registerMessage.getEmail());
        user.setToken(generateToken(registerMessage.getUsername()));
        MemoryStorage.INSTANCE.getUsers().put(channel.id(), user);
        user.setChannel(channel);
        MemoryStorage.INSTANCE.getLoggedUsers().add(channel);
        MemoryStorage.INSTANCE.getNotLoggedUsers().remove(channel);
        MessageDispatcher.sendMessageToUser(user, new TokenMessage(user));
        UserService userService = HttpStreamingApplication.context.getBean(UserService.class);
        userService.create(user);
    }

    public void login(LoginMessage loginMessage) {
        User user = MemoryStorage.INSTANCE.getUserByToken(loginMessage.getToken());
        if (user != null) {
            MessageDispatcher.sendMessageToUser(user, new TokenMessage(user));
        } else {
            logger.error("Trying to login unregistered user");
        }
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

}
