package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeHighschoolAssociationEntity;
import com.c4me.server.entities.CollegeHighschoolAssociationEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeHighSchoolAssociationRepository extends JpaRepository<CollegeHighschoolAssociationEntity, CollegeHighschoolAssociationEntityPK> {
}