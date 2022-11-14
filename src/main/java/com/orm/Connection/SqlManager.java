package com.orm.Connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlManager {

    public static <T> void createTable(Class<T> clz) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            String query = SqlQueryFactory.createNewTableQuery(clz);
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> findAll(Class<T> clz) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            String query = SqlQueryFactory.createFindAllQuery(clz);
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getItemById(Class<T> clz, int id) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createGetItemByIdQuery(clz, id);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Need to add checking if list is empty
    public static <T> List<T> getItemByProperty(Class<T> clz, String propertyName, Object property) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createItemByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> resultSetToList(Class<T> clz, ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        try {
            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields(); //list of fields
                for (Field field : declaredFields) {
                    field.setAccessible(true); //turn to public
                    field.set(item, rs.getObject(field.getName()));
                }
                results.add(item);
            }
            return results;
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SQLException("Result sets is empty");
        } catch (
                NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage() + "Error in class' constructor");
        }
    }

    /**
     * ADD Functionality
     */
    //TODO: Add a single item to a table
    public static <T> String createAddSingleItemToTableQuery() {
        return null;
    }
    //TODO: Add multiple items

    public static <T> List<T> updatePropertyById(Class<T> clz, String propertyName, Object property,int id) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createUpdateByIdQuery(clz, propertyName, property,id);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO: Update an entire item
    public static <T> T updateEntireItem(Class<T> clz, T object,int id) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createUpdateItemQuery(clz,object,id);
            Statement stmt = con.createStatement();
            stmt.execute(query);
            return object;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //        public <T> void addObjectToDB(Class<T> clz,T object) {
//            try{
//
//                //info - Connection is created successfully
//                String query = "INSERT INTO "+tableName + " VALUES (";
//
//                Field[] declaredFields = clz.getDeclaredFields(); //list of fields
//
//
//                for (Field field : declaredFields) {
//                    object.getClass().getDeclaredField(field.getName());
//                    field.setAccessible(true);
//                    query += field.get(object);
//                    query+=",";
//                }
//
//                query = query.substring(0,query.length()-1)+')';
//                System.out.println(query);
//                connect.getStmt().executeUpdate(query);
//                //stmt.executeUpdate("INSERT INTO Users " + "VALUES (4, 'haitham@gmail.com','Haitham', 'haitham1234', 4000 )");
//            } catch (SQLException e) {
//                throw new RuntimeException("Entity insertion was failed");
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }


    /**
     * Delete Functionality
     */
    //TODO: Single item deletion by any property (delete user with email x)
    public static <T> List<T> deleteSingleItemByProperty(Class<T> clz, String propertyName, Object property) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteSingleItemByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO Multiple item deletion by any property (delete all users called x)
    public static <T> List<T> deleteItemsByProperty(Class<T> clz, String propertyName, Object property) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteItemsByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO Delete entire table (truncate)
    public static <T> List<T> deleteTable(Class<T> clz) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteTableQuery(clz);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
