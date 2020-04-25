package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.MajorAliasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorAliasRepository extends JpaRepository<MajorAliasEntity, String> {
}
