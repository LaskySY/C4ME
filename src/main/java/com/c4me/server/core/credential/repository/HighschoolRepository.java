package com.c4me.server.core.credential.repository;

import com.c4me.server.entities.HighschoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HighschoolRepository extends JpaRepository<HighschoolEntity, Integer> {
    HighschoolEntity findByName(String name);
    Boolean existsByName(String name);
//    HighschoolEntity findByNameLike(String name);

//    @Query("Select h from HighschoolEntity h where :name like %h.name%")
//    HighschoolEntity findByNameSubstringOf(@Param("name") String name);

}
