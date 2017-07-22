package com.seveniu.security.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by seveniu on 7/9/17.
 * *
 */
public interface UserService {
    User createUser(User newUserConfig, char[] password);

    Page<User> findAll(Pageable pageable);
}
