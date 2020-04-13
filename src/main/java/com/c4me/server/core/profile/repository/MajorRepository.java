package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-27-2020
 */

public interface MajorRepository extends JpaRepository<MajorEntity, String> {

}
