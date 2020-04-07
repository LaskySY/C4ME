package com.c4me.server.core.collegeSearch.specifications;

import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.CollegeEntity_;
import com.c4me.server.utils.CopyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.c4me.server.config.constant.Const.FilterOptions.SUPPORTED_RANGE_FILTERS;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

public class CollegeSearchFilterSpecification implements Specification<CollegeEntity>{

    private final CollegeSearchFilter filter;
    private final BeanWrapper filterBeanWrapper;

    public CollegeSearchFilterSpecification (CollegeSearchFilter filter) {
        this.filter = filter;
        this.filterBeanWrapper = new BeanWrapperImpl(filter);
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
    private Predicate generateRangePredicate(String filterName, Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, boolean strict) {
        String minFilter = getMinPropertyName(filterName);
        String maxFilter = getMaxPropertyName(filterName);
        if(minFilter == null || maxFilter == null) return criteriaBuilder.conjunction();
        Object minVal = filterBeanWrapper.getPropertyValue(minFilter);
        Object maxVal = filterBeanWrapper.getPropertyValue(maxFilter);
        if((minVal == null && maxVal == null) || (minVal != null && !(minVal instanceof Comparable)) || (maxVal != null && !(maxVal instanceof Comparable))) return criteriaBuilder.conjunction();

        Predicate requireNonNull = criteriaBuilder.isNotNull(root.get(filterName));
        Predicate requireNull = criteriaBuilder.isNull(root.get(filterName));

        if (minVal == null) {
            if(strict) return criteriaBuilder.and(requireNonNull, criteriaBuilder.lessThanOrEqualTo(root.get(filterName), (Comparable) maxVal));
            else       return criteriaBuilder.or(requireNull, criteriaBuilder.lessThanOrEqualTo(root.get(filterName), (Comparable) maxVal));
        }
        else if (maxVal == null) {
            if(strict) return criteriaBuilder.and(requireNonNull, criteriaBuilder.greaterThanOrEqualTo(root.get(filterName), (Comparable) minVal));
            else       return criteriaBuilder.or(requireNull, criteriaBuilder.greaterThanOrEqualTo(root.get(filterName), (Comparable) minVal));
        }
        else {
            if(strict) return criteriaBuilder.and(requireNonNull, criteriaBuilder.between(root.get(filterName), (Comparable) minVal, (Comparable) maxVal));
            else       return criteriaBuilder.or(requireNull, criteriaBuilder.between(root.get(filterName), (Comparable) minVal, (Comparable) maxVal));
        }
    }


    @Override
    public Predicate toPredicate(Root<CollegeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for(String rangeFilter : SUPPORTED_RANGE_FILTERS) {
            predicates.add(generateRangePredicate(rangeFilter, root, criteriaQuery, criteriaBuilder, filter.getStrict()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
