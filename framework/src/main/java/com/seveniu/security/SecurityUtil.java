package com.seveniu.security;


import com.seveniu.entity.AuthorityName;
import com.seveniu.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by dhlz on 12/21/16.
 * *
 */
public class SecurityUtil {

    public static void setCustomAuthentication(User user) {
        SecurityUserDetails userDetails = SecurityUserDetailFactory.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static SecurityUserDetails getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (SecurityUserDetails) auth.getPrincipal();
    }


    public static Long getCurrentUserId() {
        SecurityUserDetails userDetails = getUserDetails();
        return userDetails == null ? null : userDetails.getId();
    }

    public static String getCurrentUserName() {
        SecurityUserDetails userDetails = getUserDetails();
        return userDetails == null ? null : userDetails.getUsername();
    }


    public static boolean hasRole(String roleName) {
        return getUserDetails().getAuthorities().stream().anyMatch(v -> {
            return v.getAuthority().endsWith(roleName);
        });
    }

    public static boolean isAdmin() {
        boolean isSystemAdmin = hasRole(AuthorityName.ROLE_ADMIN.name());
        return isSystemAdmin;
    }


    public static boolean hasAdminAuthority() {
        boolean hasAuthority = true;
        if (!isAdmin()) {
            boolean isAdmin = hasRole(AuthorityName.ROLE_ADMIN.name());
            if (!isAdmin) {
                hasAuthority = false;
            }
        }
        return hasAuthority;
    }

    public static void checkAdminAuthority() {
        if (!hasAdminAuthority()) {
            throw new AccessDeniedException("您没有该对象的管理权限");
        }
    }

}

