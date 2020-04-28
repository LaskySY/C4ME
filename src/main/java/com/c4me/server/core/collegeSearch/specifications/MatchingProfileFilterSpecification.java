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
  private final BeanWrapper filterBeanWrapper;
  private final Boolean strict;

  public MatchingProfileFilterSpecification (MatchingProfileFilter filter) {
    this.filter = filter;
    this.filterBeanWrapper = new BeanWrapperImpl(filter);
    this.strict = filter.getStrict();
  }

  /**
   * @param propertyName {@link String} name of the current range property
   * @return {@link String} the name of the minProperty
   */
  private String getMinPropertyName(String propertyName) {
    if (propertyName.length() < 1) return null;
    String minName = "min" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
    return minName;
  }

  /**
   * @param propertyName {@link String} name of the current range property
   * @return {@link String} the name of the maxProperty
   */
  private String getMaxPropertyName(String propertyName) {
    if (propertyName.length() < 1) return null;
    String maxName = "max" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
    return maxName;
  }

  /**
   * Generate a predicate for a range property, e.g. min/max ranking
   * @param filterName {@link String} the name of the entity property to generate a predicate for
   * @param root {@link Root} of the {@link CollegeEntity} table
   * @param criteriaQuery {@link CriteriaQuery}
   * @param criteriaBuilder {@link CriteriaBuilder}
   * @return {@link Predicate}: the Criteria predicate corresponding to the range for the current filter property (either strict or lax). Will fail if filterName is invalid
   */
  private Predicate generateRangePredicate(String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    String minFilter = getMinPropertyName(filterName);
    String maxFilter = getMaxPropertyName(filterName);
    if(minFilter == null || maxFilter == null) return criteriaBuilder.conjunction();
    Object minVal = filterBeanWrapper.getPropertyValue(minFilter);
    Object maxVal = filterBeanWrapper.getPropertyValue(maxFilter);
    if((minVal == null && maxVal == null) || (minVal != null && !(minVal instanceof Comparable)) || (maxVal != null && !(maxVal instanceof Comparable))) return criteriaBuilder.conjunction();

    Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(filterName));
    Predicate requireNull = criteriaBuilder.isNull(root.get(filterName));

    Predicate predicate;
    if (minVal == null) {
      predicate = criteriaBuilder.lessThanOrEqualTo(root.get(filterName), (Comparable) maxVal);
    }
    else if (maxVal == null) {
      predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(filterName), (Comparable) minVal);
    }
    else {
      predicate = criteriaBuilder.between(root.get(filterName), (Comparable) minVal, (Comparable) maxVal);
    }

    if(strict) return criteriaBuilder.and(requireNonNull, predicate);
    else       return criteriaBuilder.or(requireNull, predicate);
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


  private String generateLikeString(String substring) {
    return "%" + substring + "%";
  }


  private Predicate generateApplicationProfilePredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    Root<UserEntity> rootuser = criteriaQuery.from(UserEntity.class);
    Join<UserEntity, StudentApplicationEntity> join = rootuser.join(UserEntity_.studentApplicationsByUsername);
    Join<ProfileEntity, UserEntity> join2 = root.join(ProfileEntity_.userByUsername);

    Expression<String> collegename = join.get(StudentApplicationEntity_.collegeByCollegeId).get(CollegeEntity_.name);
    Expression<String> username = join.get(StudentApplicationEntity_.userByUsername).get(UserEntity_.username);
    Expression <String> profileusername = join2.get(UserEntity_.profileByUsername).get(ProfileEntity_.username);


    Predicate pred;
    Predicate pred3;
    if(filter.getName() == null){
      pred = criteriaBuilder.conjunction();
    }
    else{
      pred = criteriaBuilder.equal(collegename, filter.getName());
    }

    Predicate pred2 = criteriaBuilder.equal(username, profileusername);

    if(filter.getApplicationInts() == null){
      pred3 = criteriaBuilder.conjunction();
    }
    else{
      pred3 = join.get(StudentApplicationEntity_.status).in(filter.getApplicationInts());
    }

    return criteriaBuilder.and(pred, pred2, pred3);
  }

  private Predicate generateHighschoolNamePredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

    if(filter.getHighSchools() == null){
      return criteriaBuilder.conjunction();
    }

    Join<ProfileEntity, HighschoolEntity> join = root.join(ProfileEntity_.highschoolBySchoolId);

    Expression<String>HSname = join.get(HighschoolEntity_.name);

    Predicate requireNonNull = criteriaBuilder.isNotNull(HSname);
    Predicate requireNull = criteriaBuilder.isNull(HSname);

    if (strict) return criteriaBuilder.and(requireNonNull, HSname.in(filter.getHighSchools()));
    else        return criteriaBuilder.or(requireNull, HSname.in(filter.getHighSchools()));
  }






  @Override
  public Predicate toPredicate(Root<ProfileEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();

    predicates.add(generateCollegeClassPredicate(root, criteriaQuery, criteriaBuilder));
    predicates.add(generateApplicationProfilePredicate(root, criteriaQuery, criteriaBuilder));
    predicates.add(generateHighschoolNamePredicate(root, criteriaQuery, criteriaBuilder));

    Predicate finalPredicate =  criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    return finalPredicate;
  }
}
