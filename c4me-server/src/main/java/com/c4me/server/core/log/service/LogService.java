package com.c4me.server.core.log.service;

import com.c4me.server.entities.LogEntity;

import java.util.List;
/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
public interface LogService {
    List<LogEntity> queryAll();
}
