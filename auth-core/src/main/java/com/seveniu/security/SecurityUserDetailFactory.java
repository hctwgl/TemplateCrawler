package com.seveniu.security;

import com.seveniu.entity.Authority;
import com.seveniu.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUserDetailFactory {

    private SecurityUserDetailFactory() {
    }

    public static SecurityUserDetails create(User user) {
        return new SecurityUserDetails(
                user,
                mapToGrantedAuthorities(user.getAuthorities())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        if (authorities == null) {
            return Collections.emptyList();
        }
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }
}
