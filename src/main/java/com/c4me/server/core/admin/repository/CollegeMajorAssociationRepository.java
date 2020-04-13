package com.c4me.server.core.admin.repository;

import com.c4me.server.entities.CollegeMajorAssociationEntity;
import com.c4me.server.entities.CollegeMajorAssociationEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@Repository
public interface CollegeMajorAssociationRepository extends JpaRepository<CollegeMajorAssociationEntity, CollegeMajorAssociationEntityPK> {
}
