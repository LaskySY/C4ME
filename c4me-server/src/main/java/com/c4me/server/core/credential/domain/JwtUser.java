package com.c4me.server.core.credential.domain;

import com.c4me.server.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
@Getter
public class JwtUser implements UserDetails {
    private UUID id;
    private String name;
    private Integer role;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;


    public JwtUser(UserEntity user) {
        id = user.getId();
        name = user.getName();
        role = user.getRole();
        username = user.getUsername();
        password = user.getPassword();
        authorities = new ArrayList<GrantedAuthority>(){{
            add(new SimpleGrantedAuthority(user.getRole()==1?"ROLE_ADMIN":"ROLE_STUDENT"));
        }};
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
