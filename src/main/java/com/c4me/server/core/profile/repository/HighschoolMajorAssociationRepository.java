package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.HighschoolMajorAssociationEntity;
import com.c4me.server.entities.HighschoolMajorAssociationEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighschoolMajorAssociationRepository extends JpaRepository<HighschoolMajorAssociationEntity, HighschoolMajorAssociationEntityPK> {
}
