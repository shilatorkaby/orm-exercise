package com.orm.Connection;

import com.google.gson.Gson;
import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;
import com.orm.Annotation.Unique;
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

    public static <T> String checkIfTableExistsQuery(Class<T> clz) {
        StringBuilder checkTableQuery = new StringBuilder(
                "SELECT EXISTS( " +
                        "SELECT * FROM information_schema.tables " +
                        "WHERE table_schema = ' ");
        checkTableQuery.append(ConnectionFacade.getDataBase());
        checkTableQuery.append("' AND table_name = '");
        checkTableQuery.append(clz.getSimpleName().toLowerCase());
        checkTableQuery.append(" ');");
        return checkTableQuery.toString();
    }

    public static <T> String mapClassToSqlColumn(Class<T> clz) {
        StringBuilder sqlColumns = new StringBuilder();

        Field[] declaredFields = clz.getDeclaredFields();
        LinkedHashMap<Field, List<String>> fieldListHashMap = validateAnnotations(declaredFields);
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

    private static LinkedHashMap<Field, List<String>> validateAnnotations(Field[] declaredFields) {
        // <PrimaryKey, [id, name]>
        HashMap<String, List<Field>> annotationListHashMap = new HashMap<>();

        //<id, [PrimaryKey, AutoIncrement]>
        LinkedHashMap<Field, List<String>> fieldListHashMap = new LinkedHashMap<>();

        for (Field field : declaredFields) {
            List<String> annotations = Arrays.stream(field.getAnnotations())
                    .map(annotation -> annotation.annotationType().getName())
                    .collect(Collectors.toList());
            fieldListHashMap.put(field, annotations);
            for (String annotation : annotations) {
                if (!annotationListHashMap.containsKey(annotation)) {
                    annotationListHashMap.put(annotation, List.of(field));
                } else {
                    List<Field> fields = annotationListHashMap.get(annotation);
                    List<Field> copy = new ArrayList<>(fields);
                    copy.add(field);
                    annotationListHashMap.put(annotation, copy);
                }
            }
        }

        /**
         * Check if annotation @AutoIncrement appears more than one
         */
        if (annotationListHashMap.containsKey(AutoIncrement.class.getName())) {
            if (annotationListHashMap.get(AutoIncrement.class.getName()).size() > 1) {
                throw new IllegalArgumentException("AutoIncrement annotation not allowed more than one");
            }
        }

        /**
         * Check if annotation @PrimaryKey appears more than one
         */
        if (annotationListHashMap.containsKey(PrimaryKey.class.getName())) {
            if (annotationListHashMap.get(PrimaryKey.class.getName()).size() > 1) {
                throw new IllegalArgumentException("PrimaryKey annotation not allowed more than one");
            }
        }

        /**
         * Check if annotation @Unique appears more than one
         */
        if (annotationListHashMap.containsKey(Unique.class.getName())) {
            if (annotationListHashMap.get(Unique.class.getName()).size() > 1) {
                throw new IllegalArgumentException("Unique annotation not allowed more than one");
            }
        }

        /**
         * Check if PrimaryKey AutoIncrement fields are different
         */
        if (annotationListHashMap.containsKey(AutoIncrement.class.getName())) {
            Field autoIncrementedField = annotationListHashMap.get(AutoIncrement.class.getName()).get(0);
            if (annotationListHashMap.containsKey(PrimaryKey.class.getName())) {
                Field primaryKeyField = annotationListHashMap.get(PrimaryKey.class.getName()).get(0);
                if (!autoIncrementedField.getName().equals(primaryKeyField.getName())) {
                    throw new IllegalArgumentException("AutoIncremented field should be PrimaryKey field");
                }
            }
        }
        return fieldListHashMap;
    }

    /**
     * Read Functionality
     */
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

    /**
     * ADD Functionality
     */
    // TODO: Add a single item to a table
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
                } else if (JavaToSqlTypeMapper.nonPrimitiveType(o.getClass().getSimpleName())) {
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

    // TODO: Add multiple items


    /**
     * UPDATE Functionality
     */
    // TODO: Update an entire item
    public static <T> String createUpdateItemQuery(Class<T> clz, T object, int id) {
        String tableName = clz.getSimpleName().toLowerCase() + "_data";
        String query = "UPDATE " + tableName + " SET ";
        Field[] declaredFields = clz.getDeclaredFields(); //list of fields
        for (Field field : declaredFields) {
            field.setAccessible(true); //turn to public
            query += (field.getName() + " = ");
            try {
                Object value = field.get(object);
                if (value instanceof String)
                    query += ("'" + value + "'" + ",");
                else
                    query += (value + ",");

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Field value was empty");
            }
        }
        query = query.substring(0, query.length() - 1);
        query += (" WHERE id = " + id);
        return query + ";";
    }

    // TODO: Update a single property of a single item (update email for user with id x)
    public static <T> String createUpdateByIdQuery(Class<T> clz, String propertyName, Object property, int id) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "UPDATE " + tableName + " SET " + propertyName + " = " + stringProperty +
                "WHERE id = " + id;
        return query;
    }

    /**
     * Delete Functionality
     */
    // TODO: Single item deletion by any property (delete user with email x)
    public static <T> String createDeleteSingleItemByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "DELETE FROM " + tableName + " WHERE " + propertyName + " = " + stringProperty + " LIMIT 1";
        return query;
    }

    // TODO Multiple item deletion by any property (delete all users called x)
    public static <T> String createDeleteItemsByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        return "DELETE FROM " + tableName + "WHERE " + propertyName + " = " + stringProperty;
    }


    // TODO Delete entire table (truncate)
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
