package com.seveniu.security.api;

import com.seveniu.security.entity.Authority;
import com.seveniu.security.entity.AuthorityRepository;
import com.seveniu.security.entity.User;
import com.seveniu.security.entity.UserService;
import com.seveniu.security.jwt.JwtTokenUtil;
import com.seveniu.security.jwt.JwtUserDetails;
import com.seveniu.security.jwt.JwtUserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private static Logger logger = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    UserService userService;
    @Autowired
    AuthorityRepository authorityRepository;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public JwtUserDetails getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUserDetails user = (JwtUserDetails) userDetailsService.loadUserByUsername(username);
        return user;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public JwtUserDetails createUser(HttpServletRequest request, String username, String password, @RequestParam(value = "authorities[]") Long[] authorities) {

        String token = request.getHeader(tokenHeader);
        String un = jwtTokenUtil.getUsernameFromToken(token);
        JwtUserDetails createUser = (JwtUserDetails) userDetailsService.loadUserByUsername(un);
        User user = new User();
        user.setUsername(username);
        user.setCreateBy(createUser.getId());
        for (Long authorityId : authorities) {
            Authority authority = authorityRepository.findOne(authorityId);
            if (authority != null) {
                user.getAuthorities().add(authority);
            }
        }
        user = userService.createUser(user, password.toCharArray());
        return JwtUserFactory.create(user);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public Page<User> list(@PageableDefault(size = 20) Pageable pageable) {
        return userService.findAll(pageable);
    }
}