package com.c4me.server.core.highschoolSearch.specifications;

import com.c4me.server.entities.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static com.c4me.server.config.constant.Const.Status.ACCEPTED;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

public class AcceptanceSpecification implements Specification<StudentApplicationEntity> {

    UserEntity userEntity;
    public AcceptanceSpecification(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Predicate toPredicate(Root<StudentApplicationEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate isAcceptance = criteriaBuilder.equal(root.get(StudentApplicationEntity_.STATUS), ACCEPTED);
        Predicate correctUser = criteriaBuilder.equal(root.get(StudentApplicationEntity_.USER_BY_USERNAME), userEntity);
        Join<StudentApplicationEntity, CollegeEntity> join = root.join(StudentApplicationEntity_.collegeByCollegeId);
        Expression<Integer> ranking = join.get(CollegeEntity_.ranking);
        Predicate rankingExists = criteriaBuilder.isNotNull(ranking);
        criteriaQuery.orderBy(criteriaBuilder.asc(ranking));
        return criteriaBuilder.and(rankingExists, isAcceptance, correctUser);
    }
}
