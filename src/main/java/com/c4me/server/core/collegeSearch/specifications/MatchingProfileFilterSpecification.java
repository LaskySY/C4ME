package com.c4me.server.core.collegeSearch.specifications;

import com.c4me.server.core.collegeSearch.domain.MatchingProfileFilter;
import com.c4me.server.entities.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-24-2020
 */
public class MatchingProfileFilterSpecification implements Specification<ProfileEntity>{

  private final MatchingProfileFilter filter;
  private final Boolean strict;

  public MatchingProfileFilterSpecification (MatchingProfileFilter filter) {
    this.filter = filter;
    this.strict = filter.getStrict();
  }


  private Predicate generateCollegeClassPredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    Integer minVal = filter.getMinCollegeClass();
    Integer maxVal = filter.getMaxCollegeClass();

    Predicate predicate = criteriaBuilder.conjunction();

    Expression<Integer> collegeClassExpression = root.get(ProfileEntity_.schoolYear);
    Predicate requireNonNull = criteriaBuilder.isNotNull(collegeClassExpression);
    Predicate requireNull = criteriaBuilder.isNull(collegeClassExpression);
    if(minVal == null && maxVal == null) return predicate;
    if (minVal == null) {
      predicate = criteriaBuilder.lessThanOrEqualTo(collegeClassExpression, maxVal);
    }
    else if (maxVal == null) {
      predicate = criteriaBuilder.greaterThanOrEqualTo(collegeClassExpression, minVal);
    }
    else {
      predicate = criteriaBuilder.between(collegeClassExpression, minVal, maxVal);
    }

    if(strict) return criteriaBuilder.and(requireNonNull, predicate);
    else       return criteriaBuilder.or(requireNull, predicate);
  }

  private Predicate generateApplicationProfilePredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    Root<UserEntity> rootuser = criteriaQuery.from(UserEntity.class);
    Join<UserEntity, StudentApplicationEntity> join = rootuser.join(UserEntity_.studentApplicationsByUsername);
    Join<ProfileEntity, UserEntity> join2 = root.join(ProfileEntity_.userByUsername);

    Expression<String> collegename = join.get(StudentApplicationEntity_.collegeByCollegeId).get(CollegeEntity_.name);
    Expression<String> username = join.get(StudentApplicationEntity_.userByUsername).get(UserEntity_.username);
    Expression <String> profileusername = join2.get(UserEntity_.profileByUsername).get(ProfileEntity_.username);


    Predicate pred = filter.getName() == null? criteriaBuilder.disjunction() : criteriaBuilder.equal(collegename, filter.getName());
    Predicate pred2 = criteriaBuilder.equal(username, profileusername);
    Predicate pred3 = filter.getApplicationInts() == null? criteriaBuilder.conjunction() : join.get(StudentApplicationEntity_.status).in(filter.getApplicationInts());
    return criteriaBuilder.and(pred, pred2, pred3);
  }

  private Predicate generateHighschoolNamePredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

    if(filter.getHighSchools() == null){
      return criteriaBuilder.conjunction();
    }

    Expression<String> HSname = root.get(ProfileEntity_.highschoolBySchoolId).get(HighschoolEntity_.name);

    Predicate requireNonNull = criteriaBuilder.isNotNull(HSname);
    Predicate requireNull = criteriaBuilder.isNull(HSname);

    if (strict) return criteriaBuilder.and(requireNonNull, HSname.in(filter.getHighSchools()));
    else        return criteriaBuilder.or(requireNull, HSname.in(filter.getHighSchools()));
  }

  @Override
  public Predicate toPredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();

    predicates.add(generateCollegeClassPredicate(root, criteriaQuery, criteriaBuilder));
    //predicates.add(generateApplicationProfilePredicate(root, criteriaQuery, criteriaBuilder));
    predicates.add(generateHighschoolNamePredicate(root, criteriaQuery, criteriaBuilder));

    Predicate finalPredicate =  criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    return finalPredicate;
  }
}
