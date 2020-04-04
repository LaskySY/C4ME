package com.c4me.server.core.profile.repository;

import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.StudentApplicationEntityPK;
import com.c4me.server.entities.UserEntity;
//import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentApplicationRepository extends JpaRepository<StudentApplicationEntity, StudentApplicationEntityPK> {
    //TODO: find applications for a college and for a student
    //public StudentApplicationEntity findByCollegeByCollegeId(CollegeEntity collegeEntity);
    public List<StudentApplicationEntity> findAllByUserByUsername(UserEntity userEntity);

    public StudentApplicationEntity findByUserByUsername(UserEntity userEntity);

    public void deleteByStudentApplicationEntityPK(StudentApplicationEntityPK studentApplicationEntityPK);

    public Long countByCollegeByCollegeId(CollegeEntity collegeEntity);

    public List<StudentApplicationEntity> findAllByQuestionable(Byte questionable);
}
