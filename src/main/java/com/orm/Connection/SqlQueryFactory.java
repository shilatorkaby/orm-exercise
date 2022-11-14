package com.orm.Connection;

import com.orm.Utils.JavaToSqlTypeMapper;

import java.lang.reflect.Field;

public class SqlQueryFactory {

        public static<T>String tableName(Class<T> clz) { return(clz.getSimpleName().toLowerCase());}

        public static <T> String createNewTableQuery(Class<T> clz) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE ");
        createTableQuery.append(clz.getSimpleName().toLowerCase());
        createTableQuery.append(" (\n");
        createTableQuery.append(mapClassToSqlColumn(clz));
        createTableQuery.append(");");
        return createTableQuery.toString();
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
     *  Read Functionality
     */
    public static <T> String createFindAllQuery(Class<T> clz) {
        return "SELECT * FROM " + clz.getSimpleName().toLowerCase() + ";";
    }

    public static <T> String createGetItemByIdQuery(Class<T> clz, int id) {
        return createItemByPropertyQuery(clz, "id", id);
    }

    public static <T> String createItemByPropertyQuery(Class<T> clz, String propertyName, Object property) {
        String tableName = clz.getSimpleName().toLowerCase();
        String query = "SELECT * FROM " + tableName + " WHERE " + propertyName + " = " + property;
        return query;
    }

    public static <T> String createUpdateByIdQuery(Class<T> clz, String propertyName, Object property,int id) {
        String tableName = clz.getSimpleName().toLowerCase();
        String query = "UPDATE " + tableName + " SET " + propertyName + " = " + property +
                "WHERE id = " +id;
        return query;
    }

    /**
     *  ADD Functionality
     */
    // TODO: Add a single item to a table
    public static <T> String createAddSingleItemToTableQuery() {
        return null;
    }

    public static <T> String createUpdateItemQuery(Class<T> clz, T object,int id) {
        String tableName = clz.getSimpleName().toLowerCase() +"_data";
        String query = "UPDATE " + tableName + " SET ";
            Field[] declaredFields = clz.getDeclaredFields(); //list of fields
            for (Field field : declaredFields) {
                field.setAccessible(true); //turn to public
                query += (field.getName() + " = ");
                try {
                    Object value = field.get(object);
                    if (value instanceof String)
                        query += ("\'"+value+"\'" + ",");
                    else
                        query += (value + ",");

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Field value was empty");
                }
            }
            query = query.substring(0,query.length()-1);
            query += (" WHERE id = " +id);
            return query+";";
    }


    // TODO: Add multiple items
    // TODO: Update a single property of a single item (update email for user with id x)
    // TODO: Update an entire item

    /**
     *  Delete Functionality
      */
    // TODO: Single item deletion by any property (delete user with email x)

    // TODO Multiple item deletion by any property (delete all users called x)
    // TODO Delete entire table (truncate)

}
