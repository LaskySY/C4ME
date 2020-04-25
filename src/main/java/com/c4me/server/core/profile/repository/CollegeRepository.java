package com.c4me.server.core.profile.repository;

import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.entities.CollegeEntity;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollegeRepository extends JpaRepository<CollegeEntity, Integer>, JpaSpecificationExecutor<CollegeEntity> {
    CollegeEntity findByName(String name);
    List<CollegeEntity> findAllByOrderByName();
    CollegeEntity findByNameLike(String name);
}
