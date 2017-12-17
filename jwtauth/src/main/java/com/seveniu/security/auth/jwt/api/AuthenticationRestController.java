package com.seveniu.security.auth.jwt.api;

import com.seveniu.entity.User;
import com.seveniu.entity.UserService;
import com.seveniu.security.SecurityUserDetails;
import com.seveniu.security.auth.jwt.jwt.JwtAuthenticationResponse;
import com.seveniu.security.auth.jwt.jwt.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationRestController {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.expiration}")
    private Integer expiration;
    @Value("${jwt.cookie.enable:false}")
    private Boolean cookieEnable;
    @Value("${jwt.cookie.domain:}")
    private String domain;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;


    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(String username, String password, HttpServletResponse response) throws AuthenticationException {
        if (username == null || password == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        // Perform the security
        final Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            logger.info("Loading user: username=" + username);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("User is " + userDetails.toString());
            final String token = jwtTokenUtil.generateToken(userDetails);
            // Return the token
            if (cookieEnable) {
                Cookie cookie = jwtTokenUtil.generateJwtCookie(token);
                if (!StringUtils.isEmpty(domain)) {
                    cookie.setDomain(domain);
                }
                response.addCookie(cookie);
            }
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            response.addHeader("loginFailureMessageKey", "EXCEPTION_LOGIN_FAILED");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String oldToken = jwtTokenUtil.getToken(request);
            if (oldToken != null && jwtTokenUtil.canTokenBeRefreshed(oldToken)) {
                String refreshedToken = jwtTokenUtil.refreshToken(oldToken);
                response.addCookie(jwtTokenUtil.generateJwtCookie(refreshedToken));
                return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
            } else {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }

    //    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getCur(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtTokenUtil.getToken(request);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                return ((SecurityUserDetails)userDetails).getUser();
            }
        }
        ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        return null;
    }
}