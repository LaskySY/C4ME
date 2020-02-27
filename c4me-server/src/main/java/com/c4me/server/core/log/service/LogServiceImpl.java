package com.c4me.server.core.log.service;

import com.c4me.server.core.log.repository.LogRepository;
import com.c4me.server.entities.LogEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@Service
public class LogServiceImpl {

    @Autowired
    LogRepository logRepository;

    public List<LogEntity> queryAll() {
        return logRepository.findAll();
    }
}
