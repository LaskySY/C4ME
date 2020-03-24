package com.c4me.server.core.credential.repository;

import com.c4me.server.entities.HighschoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighschoolRepository extends JpaRepository<HighschoolEntity, Integer> {
    HighschoolEntity findByName(String name);
    Boolean existsByName(String name);
}
