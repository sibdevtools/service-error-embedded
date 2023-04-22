package com.github.sibmaks.error.local.feature;

/**
 * @author sibmaks
 * @since 2023-04-22
 */
public class WhiteBox {

    /**
     * Get field value from object by name
     *
     * @param object object instance
     * @param fieldName field name
     * @return field value
     * @param <T> field type to cast
     * @throws NoSuchFieldException no field with passed name
     * @throws IllegalAccessException no access to field
     */
    public static<T> T getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        var type = object.getClass();
        var field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    /**
     *  Set field value by name
     *
     * @param object object instance
     * @param fieldName field name
     * @param value field value
     * @throws NoSuchFieldException no field with passed name
     * @throws IllegalAccessException no access to field
     */
    public static void setField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        var type = object.getClass();
        var field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

}
