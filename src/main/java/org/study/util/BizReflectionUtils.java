package org.study.util;

import java.lang.reflect.Field;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class BizReflectionUtils {

    /**
     * set object field by reflect
     * @param instance object to set field
     * @param clazz object type
     * @param fieldName field name of the object to set
     * @param value value to set
     * @param <T> target object type
     */
    public static <T> void reflectSet(T instance, Class<T> clazz, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = clazz.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(instance, value);
    }

    /**
     * get object field by reflect
     * @param instance target object
     * @param clazz target object class
     * @param fieldName target field name
     * @param <T> instance type
     * @param <U> filed type
     * @return file value
     */
    @SuppressWarnings("unchecked")
    public static <T, U> U reflectGet(T instance, Class<? extends T> clazz, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {
        Field declaredField = clazz.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return (U) declaredField.get(instance);
    }
}
