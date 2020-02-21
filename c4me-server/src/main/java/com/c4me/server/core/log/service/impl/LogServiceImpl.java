package com.c4me.server.core.log.service.impl;

import com.c4me.server.core.log.repository.LogRepository;
import com.c4me.server.core.log.service.LogService;
import com.c4me.server.entities.LogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logRepository;

    @Override
    public List<LogEntity> queryAll() {
        return logRepository.findAll();
    }
}
