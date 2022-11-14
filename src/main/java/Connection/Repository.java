package Connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private Class<T> clz;
    private String tableName;

    Connect connect = Connect.getInstance();

    public Repository(Class<T> clz) {
        this.clz = clz;
        tableName=String.format("%s_data", clz.getSimpleName().toLowerCase());
    }


        private static <T> List<T> resultSetToList(Class<T> clz,ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        try{
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
        }
        catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {throw new SQLException("Result sets is empty");} catch (
                NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage()+"Error in class' constructor");
        }
    }

        //Database to list of objects
        public <T> List<T> ConnectionToSQL(Class<T> clz) {
        try {
            ResultSet rs = connect.getStmt().executeQuery("select * from "+tableName);
            List<T> results=resultSetToList(clz,rs);

            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
        }

        public <T> T getItemById(Class<T> clz,int id)
        {
            try {
                String query = "SELECT * FROM "+tableName +" WHERE id =" +id;
                return resultSetToList(clz,connect.getStmt().executeQuery(query)).get(0);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        public <T> List<T> getItemByProperty(Class<T> clz,String propertyName,Object property)
        {
            try {
                String query = "SELECT * FROM "+tableName +
                        " WHERE " +propertyName +" = "+property;
                return resultSetToList(clz,connect.getStmt().executeQuery(query));

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


    }





//Read

//read all item from the table
//get one item by id
//Get item(s) by any property

//Add

//insert one item to table
//insert multiple items to table

//update

//update one property for item by an id
//update entire item//

//Delete

//delete Single item deletion by any property (delete user with email x)
//delete Multiple item deletion by any property (delete all users called x)
//Delete entire table (truncate)


//Table Creation

//the rest