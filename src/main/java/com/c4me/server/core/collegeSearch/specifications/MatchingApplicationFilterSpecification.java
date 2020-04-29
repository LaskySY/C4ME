package com.c4me.server.core.collegeSearch.specifications;

import com.c4me.server.core.collegeSearch.domain.MatchingProfileFilter;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.CollegeEntity_;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.StudentApplicationEntity_;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-28-2020
 */

public class MatchingApplicationFilterSpecification implements Specification<StudentApplicationEntity> {

    private final MatchingProfileFilter filter;
    private final Boolean strict;

    public MatchingApplicationFilterSpecification (MatchingProfileFilter filter) {
        this.filter = filter;
        this.strict = filter.getStrict();
    }

    private Predicate generateStatusPredicate(Root<StudentApplicationEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)  {
        if(filter.getApplicationInts() == null) return criteriaBuilder.conjunction();
        Predicate requireNull = criteriaBuilder.isNull(root.get(StudentApplicationEntity_.status));
        Predicate requireNotNull = criteriaBuilder.isNotNull(root.get(StudentApplicationEntity_.status));

        Predicate matchingStatus = root.get(StudentApplicationEntity_.status).in(filter.getApplicationInts());
        if(strict) return criteriaBuilder.and(requireNotNull, matchingStatus);
        else       return criteriaBuilder.or(requireNull, matchingStatus);
    }

    private Predicate generateCollegeNamePredicate(Root<StudentApplicationEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(filter.getName() == null) return criteriaBuilder.disjunction();
        return criteriaBuilder.equal(root.get(StudentApplicationEntity_.collegeByCollegeId).get(CollegeEntity_.name), filter.getName());
    }


    @Override
    public Predicate toPredicate(Root<StudentApplicationEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate statusPredicate = generateStatusPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate collegeNamePredicate = generateCollegeNamePredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(StudentApplicationEntity_.status)));
        return criteriaBuilder.and(statusPredicate, collegeNamePredicate);
    }
}
