package com.orm.Connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;
import com.orm.Annotation.Unique;
import com.orm.Utils.EntityAnnotationsValidator;
import com.orm.Utils.JavaToSqlTypeMapper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SqlQueryFactory {

    public static <T> String createNewTableQuery(Class<T> clz) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableQuery.append(clz.getSimpleName().toLowerCase());
        createTableQuery.append(" (\n");
        createTableQuery.append(mapClassToSqlColumn(clz));
        createTableQuery.append(");");
        return createTableQuery.toString();
    }

    public static <T> String mapClassToSqlColumn(Class<T> clz) {
        StringBuilder sqlColumns = new StringBuilder();

        Field[] declaredFields = clz.getDeclaredFields();
        LinkedHashMap<Field, List<String>> fieldListHashMap =
                EntityAnnotationsValidator.validateAnnotations(declaredFields);
        String primaryKey = null, unique = null;
        int i = 0;
        for (Map.Entry<Field, List<String>> entry : fieldListHashMap.entrySet()) {
            String name = entry.getKey().getName();
            String type = entry.getKey().getType().getSimpleName();
            String sqlType = JavaToSqlTypeMapper.mapJavaFieldToSql(type);

            List<String> values = entry.getValue();
            sqlColumns.append(name + " " + sqlType);

            if (values.size() != 0) {
                if (values.contains(AutoIncrement.class.getName())) {
                    sqlColumns.append(" NOT NULL AUTO_INCREMENT");
                    primaryKey = String.format(",\nPRIMARY KEY (%s)", name);
                }
                if (values.contains(PrimaryKey.class.getName())
                        && !values.contains(AutoIncrement.class.getName())) {
                    sqlColumns.append(" NOT NULL");
                    primaryKey = String.format(",\nPRIMARY KEY (%s)", name);
                }
                if (values.contains(Unique.class.getName())) {
                    sqlColumns.append(" NOT NULL");
                    unique = String.format(",\nUNIQUE (%s)", name);
                }
            }
            if (i++ != declaredFields.length - 1) {
                sqlColumns.append(",\n");
            }
        }
        if (primaryKey != null) {
            sqlColumns.append(primaryKey);
        }
        if (unique != null) {
            sqlColumns.append(unique);
        }
        return sqlColumns.toString();
    }

    public static <T> String createFindAllQuery(Class<T> clz) {
        return "SELECT * FROM " + clz.getSimpleName().toLowerCase() + ";";
    }

    public static <T> String createGetItemByIdQuery(Class<T> clz, int id) {
        return createItemByPropertyQuery(clz, "id", id);
    }

    public static <T> String createItemByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "SELECT * FROM " + tableName + " WHERE " + propertyName + " = " + stringProperty;
        return query;
    }

    public static <T> String createAddSingleItemToTableQuery(T t) {
        String tableName = t.getClass().getSimpleName().toLowerCase();
        String query = "INSERT INTO " + tableName + getValues(t);
        return query;
    }

    public static <T> String getValues(T t) {
        StringBuilder values = new StringBuilder(" VALUES (");
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            declaredFields[i].setAccessible(true);
            try {
                Object o = declaredFields[i].get(t);
                if (o instanceof String) {
                    values.append(String.format("\'%s\'", o));
                } else if (o != null && JavaToSqlTypeMapper
                        .nonPrimitiveType(o.getClass().getSimpleName())) {
                    values.append("'" + new Gson().toJson(o) + "'");
                } else {
                    values.append(o);
                }
                if (i != declaredFields.length - 1) {
                    values.append(", ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        values.append(");");
        return values.toString();
    }

    public static <T> String createUpdateItemQuery(Class<T> clz, T object, int id) {
        String tableName = clz.getSimpleName().toLowerCase();
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        Field[] declaredFields = clz.getDeclaredFields(); //list of fields
        for (Field field : declaredFields) {
            field.setAccessible(true); //turn to public
            query.append(field.getName() + " = ");
            try {
                Object value = field.get(object);
                if (value instanceof String) {
                    query.append("'" + value + "', ");
                } else if (value != null && JavaToSqlTypeMapper
                        .nonPrimitiveType(value.getClass().getSimpleName())) {
                    query.append("'" + new Gson().toJson(value) + "', ");
                } else {
                    query.append(value + ", ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
       // String subQuery = query.substring(0, query.length() - 1);
        String res = query.substring(0,query.length()-2) +" WHERE id = " + id + ";";
        return res;
    }

    public static <T> String createUpdateByIdQuery(Class<T> clz, String propertyName, Object property, int id) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "UPDATE " + tableName + " SET " + propertyName + " = " + stringProperty +
                "WHERE id = " + id;
        return query;
    }

    public static <T> String createDeleteSingleItemByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "DELETE FROM " + tableName + " WHERE " + propertyName + " = " + stringProperty + " LIMIT 1";
        return query;
    }

    public static <T> String createDeleteItemsByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        if (JavaToSqlTypeMapper.nonPrimitiveType(property.getClass().getSimpleName())) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            stringProperty = gson.toJson(property, property.getClass());
        }
        return "DELETE FROM " + tableName + " WHERE " + propertyName + " = " + stringProperty;
    }

    public static <T> String createDeleteTableQuery(Class<T> clz) {
        String tableName = clz.getSimpleName().toLowerCase();
        String query = "DROP TABLE IF EXISTS " + tableName;
        return query;
    }

    private static String convertIfString(Object obj) {
        if (obj instanceof String) {
            return "'" + obj + "'";
        } else {
            return obj.toString();
        }
    }
}
