package com.c4me.server.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-21-2020
 */

public class CopyUtils {

    //this method is from https://stackoverflow.com/questions/19737626/how-to-ignore-null-values-using-springframework-beanutils-copyproperties
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            //System.out.println("name = " + pd.getName());
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static java.beans.PropertyDescriptor findDescriptor(Object source, String name) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        for(java.beans.PropertyDescriptor pd : pds) {
            if(pd.getName().equals(name)) {
                return pd;
            }
        }
        return null;
    }

    public static String[] invertProperties(Object source, String[] properties) {
        List<String> propertiesList = Arrays.asList(properties);

        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if(!(propertiesList.contains(propertyName))) emptyNames.add(propertyName);
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
