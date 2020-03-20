package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<CollegeEntity, Integer> {
    public CollegeEntity findByName(String name);
}
