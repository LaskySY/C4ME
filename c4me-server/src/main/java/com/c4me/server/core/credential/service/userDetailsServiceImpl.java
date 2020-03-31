package com.c4me.server.core.credential.service;

import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.core.credential.domain.JwtUser;
import com.c4me.server.core.credential.domain.RegisterUser;
import com.c4me.server.core.credential.repository.UserRepository;

import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    ProfileRepository profileRepository;

    public UserEntity register(RegisterUser user) throws DuplicateUsernameException {
        UserEntity userEntity = UserEntity.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .role(0)
                .build();
        if(userRepository.findByUsername(user.getUsername())!=null){
            throw(new DuplicateUsernameException("Username has been used"));
        }
        userRepository.save(userEntity);
        ProfileEntity profileEntity = ProfileEntity.builder().username(user.getUsername()).userByUsername(userEntity).build();
        profileRepository.save(profileEntity);

        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws BadCredentialsException {
        UserEntity userEntity = userRepository.findByUsername(s);
        if(userEntity == null)
            throw new BadCredentialsException("Username does not exist");
        return new JwtUser(userEntity);
    }
}
