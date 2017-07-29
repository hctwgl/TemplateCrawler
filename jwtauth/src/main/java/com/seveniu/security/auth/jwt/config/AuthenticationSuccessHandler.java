package com.seveniu.security.auth.jwt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) { // ajax 请求
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("ok");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("OK");
            response.flushBuffer();
        } else {
            response.sendRedirect("/");
        }
    }
}
