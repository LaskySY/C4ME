package com.c4me.server.core.hello.service;

import com.c4me.server.entities.TestEntity;

import java.util.List;

public interface UserService {
    List<TestEntity> queryAll();
}
