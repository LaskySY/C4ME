package com.c4me.server.core.collegeSearch.domain;

import com.c4me.server.entities.CollegeEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

public class CollegeSearchFilterSpecification implements Specification<CollegeEntity>{

    private CollegeSearchFilter filter;

    public CollegeSearchFilterSpecification (CollegeSearchFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
