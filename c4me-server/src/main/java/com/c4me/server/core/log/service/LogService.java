package com.c4me.server.core.log.service;

import com.c4me.server.entities.LogEntity;

import java.util.List;

public interface LogService {
    List<LogEntity> queryAll();
}
