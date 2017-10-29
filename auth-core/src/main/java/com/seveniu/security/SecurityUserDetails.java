package com.seveniu.security;

import com.seveniu.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class SecurityUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    private final Date lastPasswordResetDate;
    private User user;

    public SecurityUserDetails(
            User user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
        this.enabled = user.getEnabled();
        this.lastPasswordResetDate = user.getLastPasswordResetDate();
        this.user = user;
    }

    //    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    //    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    //    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public User getUser() {
        return user;
    }

}
