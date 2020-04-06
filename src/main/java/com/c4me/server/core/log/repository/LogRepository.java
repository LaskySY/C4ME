package com.c4me.server.core.log.repository;

import com.c4me.server.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@Repository
public interface LogRepository extends JpaRepository<LogEntity, Integer> {
}
