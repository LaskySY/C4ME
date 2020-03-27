package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeRepository extends JpaRepository<CollegeEntity, Integer> {
    public CollegeEntity findByName(String name);
    public List<CollegeEntity> findAllByOrderByName();
}
