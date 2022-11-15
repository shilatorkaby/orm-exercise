package com.orm.Utils;

import org.apache.logging.log4j.Logger;

public class ErrorHandlling {

    public static <T> void validate(Class<T> clz, String propertyName, Object property, Logger logger) {
        validateClz(clz, logger);
        validateItem(property, logger);
        validatePropertyName(propertyName, logger);

    }

    public static <T> void validate(Class<T> clz, String propertyName, Object property, int id, Logger logger) {
        validateClz(clz, logger);
        if (property == null) {
            logger.error("property value should not be null");
            throw new IllegalArgumentException("property value should not be null");
        }
        validatePropertyName(propertyName, logger);
        validateId(id, logger);

    }

    public static <T> void validate(Class<T> clz, T object, int id, Logger logger) {
        validateClz(clz, logger);
        validateItem(object, logger);
        validateId(id, logger);
    }

    public static <T> void validate(Class<T> clz, Logger logger) {
        validateClz(clz, logger);
    }

    public static <T> void validate(Class<T> clz, int id, Logger logger) {
        validateClz(clz, logger);
        validateId(id, logger);
    }

    public static <T> void validate(T item, Logger logger) {
        validateItem(item, logger);
    }

    private static void validateId(int id, Logger logger) {
        if (id <= 0) {
            logger.error("id should not be 0 or minus");
            throw new IllegalArgumentException("id should not be 0 or minus");
        }
    }

    private static <T> void validateClz(Class<T> clz, Logger logger) {
        if (clz == null) {
            logger.error("class should not be null");
            throw new IllegalArgumentException("class should not be null");
        }

    }

    private static <T> void validateItem(T item, Logger logger) {
        if (item == null) {
            logger.error("item should not be null");
            throw new IllegalArgumentException("item should not be null");
        }
    }

    private static void validatePropertyName(String propertyName, Logger logger) {
        if (propertyName == null || propertyName.equals("")) {
            logger.error("property name should not be null or empty");
            throw new IllegalArgumentException("property name should not be null or empty");
        }
    }

}
