package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.StudentApplicationEntityPK;
import com.c4me.server.entities.UserEntity;
//import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentApplicationRepository extends JpaRepository<StudentApplicationEntity, StudentApplicationEntityPK>, JpaSpecificationExecutor<StudentApplicationEntity> {
    //public StudentApplicationEntity findByCollegeByCollegeId(CollegeEntity collegeEntity);
    List<StudentApplicationEntity> findAllByUserByUsername(UserEntity userEntity);

    StudentApplicationEntity findByUserByUsername(UserEntity userEntity);

    void deleteByStudentApplicationEntityPK(StudentApplicationEntityPK studentApplicationEntityPK);

    Long countByCollegeByCollegeId(CollegeEntity collegeEntity);

    List<StudentApplicationEntity> findAllByQuestionable(Byte questionable);
}
