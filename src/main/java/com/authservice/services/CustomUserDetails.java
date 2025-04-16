package com.authservice.services;

import com.authservice.entities.UserInfo;
import com.authservice.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomUserDetails extends UserInfo implements UserDetails {


    private String username;

    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUserName) {
        this.username = byUserName.getUsername();
        this.password = byUserName.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();
        for (UserRole role : byUserName.getRoles()) {/** -------------->>>>>>>                                          ek ek role uthayege or auth mai add karege then authorities mai add karege**/
            auths.add(new SimpleGrantedAuthority(role.getRoleName().toUpperCase()));
        }

        this.authorities = auths;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
