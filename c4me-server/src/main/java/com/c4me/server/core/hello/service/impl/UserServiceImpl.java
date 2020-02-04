package com.c4me.server.core.hello.service.impl;

import com.c4me.server.core.hello.repository.UserRepository;
import com.c4me.server.core.hello.service.UserService;
import com.c4me.server.entities.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<TestEntity> queryAll() {
        return userRepository.findAll();
    }

}
