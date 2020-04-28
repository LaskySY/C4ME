package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String>, JpaSpecificationExecutor<ProfileEntity> {
    ProfileEntity findByUsername(String username);
}
