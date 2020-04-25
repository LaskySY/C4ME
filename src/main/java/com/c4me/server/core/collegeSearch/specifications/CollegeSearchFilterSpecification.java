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
 * @Description: Specification for turning a {@link CollegeSearchFilter} into a JPQL query
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

public class CollegeSearchFilterSpecification implements Specification<CollegeEntity>{

    private final String homeState;
    private final CollegeSearchFilter filter;
    private final BeanWrapper filterBeanWrapper;
    private final Boolean strict;

    /**
     * Constructor for {@link CollegeSearchFilterSpecification}
     * @param filter {@link CollegeSearchFilter} the filter embodying the search parameters
     * @param homeState {@link String} the user's home state (to accurately compute costOfAttendance)
     */
    public CollegeSearchFilterSpecification (CollegeSearchFilter filter, String homeState) {
        this.homeState = homeState;
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

    private String generateLikeString(String substring) {
        return "%" + substring + "%";
    }

    /**
     * Generate a predicate for a 'match' property, e.g. match a substring of the college name.
     * @param filterName {@link String} the name of the entity property to generate a predicate for
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the Criteria predicate corresponding to the match of the current filter property (strict or lax).
     */
    private Predicate generateMatchPredicate(String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(filterName == null || filterName.equals("")) return criteriaBuilder.conjunction();
        Object filterVal = filterBeanWrapper.getPropertyValue(filterName);
        if(filterVal == null || !(filterVal instanceof String)) return criteriaBuilder.conjunction();

        Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(filterName));
        Predicate requireNull = criteriaBuilder.isNull(root.get(filterName));

        if(strict) return criteriaBuilder.and(requireNonNull, criteriaBuilder.like(root.get(filterName), generateLikeString((String) filterVal)));
        else       return criteriaBuilder.or(requireNull, criteriaBuilder.like(root.get(filterName), generateLikeString((String) filterVal)));
    }

    /**
     * Generate a predicate corresponding to an 'in' property, e.g. {@link CollegeEntity#getState} is in provided list of states to search for
     * @param paramName the {@link String} name of the entity property
     * @param filterName the {@link String} name of the filter property
     * @param root the {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the Criteria predicate corresponding to the in of the current filter (strict or lax).
     */
    private Predicate generateInPredicate(String paramName, String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(filterName == null || filterName.equals("") || paramName == null || paramName.equals("")) return criteriaBuilder.conjunction();
        Object filterVal = filterBeanWrapper.getPropertyValue(filterName);
        if(filterVal == null || !(filterVal instanceof Collection<?>) || ((Collection<?>) filterVal).size() == 0) return criteriaBuilder.conjunction();

        Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(paramName));
        Predicate requireNull = criteriaBuilder.isNull(root.get(paramName));

        if(strict) return criteriaBuilder.and(requireNonNull, root.get(paramName).in(filterVal));
        else       return criteriaBuilder.or(requireNull, root.get(paramName).in(filterVal));
    }

    /**
     * Special case: generate the predicate for the costOfAttendance property.
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the Criteria predicate corresponding to the costOfAttendance range property (using either {@link CollegeEntity#getInstateTuition} or {@link CollegeEntity#getOutstateTuition} according to the student and college home states)
     */
    private Predicate generateCostOfAttendancePredicate(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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

    /**
     * Special case: generate a predicate for whether a {@link CollegeEntity} offers a given major
     * @param major {@link String}
     * @param root {@link Root} for the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the Criteria predicate for whether {@link CollegeEntity} contains a {@link CollegeMajorAssociationEntity} of the current major
     */
    private Predicate generateOffersMajorPredicate(String major, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if(major == null) return criteriaBuilder.conjunction();
        Join<CollegeEntity, CollegeMajorAssociationEntity> join = root.join(CollegeEntity_.collegeMajorAssociationsById);
        Expression<String> collegeMajors = join.get(CollegeMajorAssociationEntity_.majorByMajorName).get(MajorEntity_.name);
        return criteriaBuilder.like(collegeMajors, generateLikeString(major));
    }

    /**
     * Generate the predicate for all the specially handled filter properties
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the Criteria predicate according to the disjunction of the all the specially handed filter properties
     */
    private Predicate generateSpecialPredicates(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate costOfAttendancePredicate = generateCostOfAttendancePredicate(root, criteriaQuery, criteriaBuilder);
//        Predicate major1Predicate = generateOffersMajorPredicate(filter.getMajor1(), root, criteriaQuery, criteriaBuilder);
//        Predicate major2Predicate = generateOffersMajorPredicate(filter.getMajor2(), root, criteriaQuery, criteriaBuilder);
        String[] majors = filter.getMajor();
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

    /**
     * Generate the expression to use for selecting/sorting by costOfAttendance (conditionally on either {@link CollegeEntity#getInstateTuition} or {@link CollegeEntity#getOutstateTuition})
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Expression} the cost of attendance expression (used by {@link #generateCostOfAttendancePredicate})
     */
    private Expression<Integer> getCostOfAttendanceExpression(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Expression<Integer> selectInOrOutState = criteriaBuilder.selectCase(root.get(CollegeEntity_.state)).when(homeState, root.get(CollegeEntity_.instateTuition)).otherwise(root.get(CollegeEntity_.outstateTuition)).as(Integer.class);
        return selectInOrOutState;
    }

    /**
     * Set the search order of the criteria query
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaQuery}
     */
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


    /**
     * Compute the {@link Predicate} corresponding to the passed {@link CollegeSearchFilter} object
     * @param root {@link Root} of the {@link CollegeEntity} table
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate} the aggregate Criteria predicate for the current query
     */
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
