package com.seveniu.security.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by seveniu on 7/9/17.
 * *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(User newUserConfig, char[] password) {
        if (newUserConfig == null) {
            throw new IllegalArgumentException("New user config cannot be null");
        }
        try {
            User user = new User();
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
            user.setUsername(newUserConfig.getUsername());
            user.setEnabled(true);
            user.setLastPasswordResetDate(new Date());
            user.setEmail("");
            user.setAuthorities(newUserConfig.getAuthorities());
            user = userRepository.save(user);
//            if (notifyUser) {
//                emailService.notify(new WelcomeEmail(EmailType.NEW_USER, user.getId(), password));
//            }
            return user;
        } finally {
            Arrays.fill(password, '*');
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
