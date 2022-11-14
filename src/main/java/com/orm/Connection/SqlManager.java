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
            System.out.println(e);
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


    // TODO: Need to add checking if list is empty
    public static <T> List<T> findAll(Class<T> clz, String propertyName, Object property) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createItemByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
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
    public static <T> T addSingleItem(T item) {
        addMultipleItems(item);
        return item;
    }

    //TODO: Add multiple items
    public static <T> T[] addMultipleItems(T... items) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            for (T item : items) {
                String query = SqlQueryFactory.createAddSingleItemToTableQuery(item);
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    //TODO: Update a single property of a single item (update email for user with id x)

    public static <T> T updatePropertyById(Class<T> clz, String propertyName, Object property,int id) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createUpdateByIdQuery(clz, propertyName, property,id);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return resultSetToList(clz, rs).get(0);
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
    public static <T> void deleteTable(Class<T> clz) {
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteTableQuery(clz);
            Statement stmt = con.createStatement();
            stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
