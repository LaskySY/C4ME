package com.c4me.server.core.collegeSearch.specifications;

import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.entities.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

import static com.c4me.server.config.constant.Const.FilterOptions.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

public class CollegeSearchFilterSpecification implements Specification<CollegeEntity>{

    private final String homeState;
    private final CollegeSearchFilter filter;
    private final BeanWrapper filterBeanWrapper;
    private final Boolean strict;

    public CollegeSearchFilterSpecification (CollegeSearchFilter filter, String homeState) {
        this.homeState = homeState;
        this.filter = filter;
        this.filterBeanWrapper = new BeanWrapperImpl(filter);
        this.strict = filter.getStrict();
    }

    private String getMinPropertyName(String propertyName) {
        if (propertyName.length() < 1) return null;
        String minName = "min" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
        return minName;
    }
    private String getMaxPropertyName(String propertyName) {
        if (propertyName.length() < 1) return null;
        String maxName = "max" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
        return maxName;
    }
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

    private String generateLikeString(String substring) {
        return "%" + substring + "%";
    }
    private Predicate generateMatchPredicate(String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(filterName == null || filterName.equals("")) return criteriaBuilder.conjunction();
        Object filterVal = filterBeanWrapper.getPropertyValue(filterName);
        if(filterVal == null || !(filterVal instanceof String)) return criteriaBuilder.conjunction();

        Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(filterName));
        Predicate requireNull = criteriaBuilder.isNull(root.get(filterName));

        if(strict) return criteriaBuilder.and(requireNonNull, criteriaBuilder.like(root.get(filterName), generateLikeString((String) filterVal)));
        else       return criteriaBuilder.or(requireNull, criteriaBuilder.like(root.get(filterName), generateLikeString((String) filterVal)));
    }

    private Predicate generateInPredicate(String paramName, String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(filterName == null || filterName.equals("") || paramName == null || paramName.equals("")) return criteriaBuilder.conjunction();
        Object filterVal = filterBeanWrapper.getPropertyValue(filterName);
        if(filterVal == null || !(filterVal instanceof List<?>)) return criteriaBuilder.conjunction();

        Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(paramName));
        Predicate requireNull = criteriaBuilder.isNull(root.get(paramName));

        if(strict) return criteriaBuilder.and(requireNonNull, root.get(paramName).in(filterVal));
        else       return criteriaBuilder.or(requireNull, root.get(paramName).in(filterVal));
    }

    // for cost of attendance, we need to select based on either the instate column if college state matches student home state, or outstate column otherwise
    private Predicate generateCostOfAttendancePredicate(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//        Predicate isHomeState = criteriaBuilder.equal(root.get(CollegeEntity_.state), homeState);

        Integer minVal = filter.getMinCostOfAttendance();
        Integer maxVal = filter.getMaxCostOfAttendance();

        Predicate predicate = criteriaBuilder.conjunction();

        Expression<Integer> costOfAttendanceExpression = getCostOfAttendanceExpression(root, criteriaQuery, criteriaBuilder);
        Predicate requireNonNull = criteriaBuilder.isNotNull(costOfAttendanceExpression);
        Predicate requireNull = criteriaBuilder.isNull(costOfAttendanceExpression);
        if(minVal == null && maxVal == null) return predicate;
        if (minVal == null) {
            predicate = criteriaBuilder.lessThanOrEqualTo(costOfAttendanceExpression, maxVal);
        }
        else if (maxVal == null) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(costOfAttendanceExpression, minVal);
        }
        else {
            predicate = criteriaBuilder.between(costOfAttendanceExpression, minVal, maxVal);
        }

        if(strict) return criteriaBuilder.and(requireNonNull, predicate);
        else       return criteriaBuilder.or(requireNull, predicate);
    }

    private Predicate generateOffersMajorPredicate(String major, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(major == null) return criteriaBuilder.conjunction();
        Join<CollegeEntity, CollegeMajorAssociationEntity> join = root.join(CollegeEntity_.collegeMajorAssociationsById);
        Expression<String> collegeMajors = join.get(CollegeMajorAssociationEntity_.majorByMajorName).get(MajorEntity_.name);
        return criteriaBuilder.like(collegeMajors, generateLikeString(major));
    }

    private Predicate generateSpecialPredicates(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate costOfAttendancePredicate = generateCostOfAttendancePredicate(root, criteriaQuery, criteriaBuilder);
//        Predicate major1Predicate = generateOffersMajorPredicate(filter.getMajor1(), root, criteriaQuery, criteriaBuilder);
//        Predicate major2Predicate = generateOffersMajorPredicate(filter.getMajor2(), root, criteriaQuery, criteriaBuilder);
        String[] majors = filter.getMajors();
        Predicate allMajorsPredicate;
        if(majors != null && majors.length > 0) {
            Predicate[] majorPredicates = new Predicate[majors.length];
            for (int i = 0; i < majorPredicates.length; i++) {
                majorPredicates[i] = generateOffersMajorPredicate(majors[i], root, criteriaQuery, criteriaBuilder);
            }
            allMajorsPredicate = criteriaBuilder.and(majorPredicates);
        }
        else {
            allMajorsPredicate = criteriaBuilder.conjunction();
        }

        Predicate[] specialPredicates = {costOfAttendancePredicate, allMajorsPredicate};
        return criteriaBuilder.and(specialPredicates);
    }

    private Expression<Integer> getCostOfAttendanceExpression(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Expression<Integer> selectInOrOutState = criteriaBuilder.selectCase(root.get(CollegeEntity_.state)).when(homeState, root.get(CollegeEntity_.instateTuition)).otherwise(root.get(CollegeEntity_.outstateTuition)).as(Integer.class);
        return selectInOrOutState;
    }

    private void setSearchOrder(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Expression<?> sortExpression;
        if(Arrays.asList(SUPPORTED_SORT_FILTERS).contains(filter.getSortBy())) {
            sortExpression = root.get(filter.getSortBy());
        }
        else {
            //may need to handle more cases in the future, if we add new special sort types
            //costOfAttendance needs to be sorted more carefully, since it's sorted on two columns, conditionally
            sortExpression = getCostOfAttendanceExpression(root, criteriaQuery, criteriaBuilder);
        }

        if(filter.getAscending()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortExpression));
        }
        else {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortExpression));
        }
    }


    @Override
    public Predicate toPredicate(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for(String rangeFilter : SUPPORTED_RANGE_FILTERS) {
            predicates.add(generateRangePredicate(rangeFilter, root, criteriaQuery, criteriaBuilder));
        }
        for(String matchFilter : SUPPORTED_MATCH_FILTERS) {
            predicates.add(generateMatchPredicate(matchFilter, root, criteriaQuery, criteriaBuilder));
        }
        for(Map.Entry<String, String> entry : SUPPORTED_IN_FILTERS.entrySet()) {
            String paramName = entry.getKey();
            String filterName = entry.getValue();
            predicates.add(generateInPredicate(paramName, filterName, root, criteriaQuery, criteriaBuilder));
        }
        predicates.add(generateSpecialPredicates(root, criteriaQuery, criteriaBuilder));

        Predicate finalPredicate =  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        setSearchOrder(root, criteriaQuery, criteriaBuilder);
        return finalPredicate;
    }
}
