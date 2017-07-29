package com.seveniu.security.auth.jwt.api;

import com.seveniu.entity.Authority;
import com.seveniu.entity.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by seveniu on 7/12/17.
 * *
 */
@RestController
@RequestMapping("authority")
public class AuthorityController {
    @Autowired
    AuthorityRepository authorityRepository;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Page<Authority> list(@PageableDefault(size = 20) Pageable pageable) {
        return authorityRepository.findAll(pageable);
    }
}
