package com.orm.Connection;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlManager {
    private static final Logger logger = LogManager.getLogger(SqlManager.class);

    public static <T> void createTable(Class<T> clz) {
        logger.info("Start Creating Table");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            String query = SqlQueryFactory.createNewTableQuery(clz);
            logger.info("start executing query");
            stmt.execute(query);
            logger.info("Table is Created");
        } catch (SQLException e) {
            logger.error("Sql Exception in creating table");
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> findAll(Class<T> clz) {
        logger.info("start Find All");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            String query = SqlQueryFactory.createFindAllQuery(clz);
            logger.info("start executing query");
            ResultSet rs = stmt.executeQuery(query);
            List<T> resList = resultSetToList(clz, rs);
            logger.info("result is generated " + resList);
            return resList;
        } catch (SQLException e) {
            logger.error("Sql Exception in creating table");
            throw new RuntimeException(e);
        }
    }


    /**
     * Need to add checking if list is empty
     */
    public static <T> List<T> findAll(Class<T> clz, String propertyName, Object property) {
        logger.info("start Find All according to property name and value");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createItemByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            logger.info("start executing query");
            ResultSet rs = stmt.executeQuery(query);
            List<T> resList = resultSetToList(clz, rs);
            logger.info("result is generated " + resList);
            return resList;
        } catch (SQLException e) {
            logger.error("Sql Exception in findingAll");
            throw new RuntimeException(e);
        }
    }

    public static <T> T getItemById(Class<T> clz, int id) {
        logger.info("start getting item by it's id");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createGetItemByIdQuery(clz, id);
            Statement stmt = con.createStatement();
            logger.info("start executing query");
            ResultSet rs = stmt.executeQuery(query);
            List<T> resList = resultSetToList(clz, rs);
            logger.info("result is generated " + resList.get(0));
            return resList.get(0);
        } catch (SQLException e) {
            logger.error("Sql Exception in getItemById");
            throw new RuntimeException(e);
        }
    }


    private static <T> List<T> resultSetToList(Class<T> clz, ResultSet rs) throws SQLException {
        logger.info("Start resultSetToList function");
        List<T> results = new ArrayList<>();
        try {
            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields(); //list of fields
                for (Field field : declaredFields) {
                    field.setAccessible(true); //turn to public
                    if (rs.getObject(field.getName()) != null
                            && rs.getObject(field.getName()).toString().contains("{")) {
                        Gson g = new Gson();
                        field.set(item, g.fromJson(rs.getObject(field.getName()).toString(), field.getType()));
                    } else {
                        field.set(item, rs.getObject(field.getName()));
                    }
                }
                results.add(item);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Result sets is empty");
            throw new SQLException("Result sets is empty");
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage() + "Error in class' constructor");
            throw new RuntimeException(e.getMessage() + "Error in class' constructor");
        }
        return results;
    }


    /**
     * ADD Functionality
     * Add a single item to a table
     */
    public static <T> T addSingleItem(T item) {
        logger.info("start adding single item");
        addMultipleItems(item);
        return item;
    }

    //TODO: Add multiple items
    public static <T> T[] addMultipleItems(T... items) {
        logger.info("start adding multiple items");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            Statement stmt = con.createStatement();
            for (T item : items) {
                String query = SqlQueryFactory.createAddSingleItemToTableQuery(item);
                logger.info("executing adding item query");
                stmt.execute(query);
            }
        } catch (SQLException e) {
            logger.error("Sql Exception in addMultipleItems");
            throw new RuntimeException(e);
        }
        return items;
    }

    /**
     * Update a single property of a single item (update email for user with id x)
     */
    public static <T> int updatePropertyById(Class<T> clz, String propertyName, Object property, int id) {
        logger.info("start updating property by id");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createUpdateByIdQuery(clz, propertyName, property, id);
            Statement stmt = con.createStatement();
            logger.info("executing updating property by id query");
            int rs = stmt.executeUpdate(query);

            logger.info("updated in row" + rs);
            return rs;
        } catch (SQLException e) {
            logger.error("Sql Exception in updatePrpertyById");
            throw new RuntimeException(e);
        }
    }

    /**
     * Update an entire item
     */
    public static <T> int updateEntireItem(Class<T> clz, T object, int id) {
        logger.info("start update entire item");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createUpdateItemQuery(clz, object, id);
            Statement stmt = con.createStatement();
            logger.info("executing updating entire item query");
            int rs = stmt.executeUpdate(query);
            logger.info("updated in row" + rs);
            return rs;
        } catch (SQLException e) {
            logger.error("Sql Exception in updateEntireItem");
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete Functionality
     * Single item deletion by any property (delete user with email x)
     */
    public static <T> int deleteSingleItemByProperty(Class<T> clz, String propertyName, Object property) {
        logger.info("start deleting single item by property");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteSingleItemByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            logger.info("executing deleting single item by property query");
            int rs = stmt.executeUpdate(query);
            return rs;
        } catch (SQLException e) {
            logger.error("Sql Exception in deleteSingleItemByProperty");
            throw new RuntimeException(e);
        }
    }

    /**
     * Multiple item deletion by any property (delete all users called x)
     */
    public static <T> int deleteItemsByProperty(Class<T> clz, String propertyName, Object property) {
        logger.info("start deleting item by property");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteItemsByPropertyQuery(clz, propertyName, property);
            Statement stmt = con.createStatement();
            logger.info("executing deleting item by property query");
            int rs = stmt.executeUpdate(query);
            return rs;
        } catch (SQLException e) {
            logger.error("Sql Exception in deleteItemsByProperty");
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete entire table (truncate)
     */
    public static <T> int deleteTable(Class<T> clz) {
        //check if table is there if yes delete else do nothing.
        logger.info("start deleting table");
        try (java.sql.Connection con = ConnectionFacade.getConnection()) {
            String query = SqlQueryFactory.createDeleteTableQuery(clz);
            Statement stmt = con.createStatement();
            logger.info("executing deleting Table query");
            int rs = stmt.executeUpdate(query);
            return rs;
        } catch (SQLException e) {
            logger.error("Sql Exception in deleteTable");
            throw new RuntimeException(e);
        }
    }
}
