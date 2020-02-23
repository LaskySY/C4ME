package com.c4me.server.core.credential.service;

import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.core.credential.domain.JwtUser;
import com.c4me.server.core.credential.repository.UserRepository;
import com.c4me.server.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-20-2020
 */
@Service
public class userDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity register(Map<String, String> user) throws DuplicateUsernameException {
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .fistName(user.get("firstName"))
                .lastName(user.get("lastName"))
                .role(0)
                .username(user.get("username"))
                .password(bCryptPasswordEncoder.encode(user.get("password")))
                .build();
        if(userRepository.findByUsername(user.get("username"))!=null)
            throw new DuplicateUsernameException();
        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(s);
        if(userEntity == null)
            throw new BadCredentialsException("Wrong password or Username not found");
        return new JwtUser(userEntity);
    }
}
