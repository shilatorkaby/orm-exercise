package com.orm.Connection;

import com.orm.Utils.JavaToSqlTypeMapper;

import java.lang.reflect.Field;

public class SqlQueryFactory {

    public static <T> String createNewTableQuery(Class<T> clz) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE ");
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

        for (int i = 0; i < declaredFields.length; i++) {
            String column = declaredFields[i].getName();
            String type = declaredFields[i].getType().getSimpleName();
            String sqlType = JavaToSqlTypeMapper.mapJavaFieldToSql(type);
            sqlColumns.append(column + " " + sqlType);
            if (i != declaredFields.length - 1) {
                sqlColumns.append(",\n");
            }
        }
        return sqlColumns.toString();
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
                    query += ("\'" + value + "\'" + ",");
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
        String query = "DELETE FROM " + tableName + "WHERE " + propertyName + " = " + stringProperty + " LIMIT 1";
        return query;
    }

    // TODO Multiple item deletion by any property (delete all users called x)
    public static <T> String createDeleteItemsByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String stringProperty = convertIfString(property);
        String query = "DELETE FROM " + tableName + "WHERE " + propertyName + " = " + stringProperty;
        return query;
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
