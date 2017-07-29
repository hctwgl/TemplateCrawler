package com.seveniu.security.auth.jwt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dhlz on 12/18/16.
 * *
 */
@Component
public class AuthenticationFailHandler implements AuthenticationFailureHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("authentication failure, uri: {}, params : {}", request.getRequestURI(), request.getParameterMap());
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) { // ajax 请求
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "login error ....");
        } else {
            response.sendRedirect(WebSecurityConfig.LOGIN_URL);
        }
    }
}
