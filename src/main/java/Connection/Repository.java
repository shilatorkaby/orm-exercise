package Connection;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
    }


    //insert object to database



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
        public static <T> List<T> ConnectionToSQL(Class<T> clz) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String tableName=String.format("%s_data", clz.getSimpleName().toLowerCase());
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+tableName, "root","");
            if (con == null) {
                throw new IOException("Database connection failed");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from "+tableName);
            List<T> results=resultSetToList(clz,rs);
            if (con != null)
                con.close();
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
        }


        public static <T> void addObjectToDB(Class<T> clz,T object) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                String tableName = String.format("%s_data", clz.getSimpleName().toLowerCase());
                java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + tableName, "root", "");
                //info - Connection is created successfully
                Statement stmt = con.createStatement();
                String query = "INSERT INTO "+tableName + "VALUES (";

                Field[] declaredFields = clz.getDeclaredFields(); //list of fields


                for (Field field : declaredFields) {
                    object.getClass().getDeclaredField(field.getName());
                    field.setAccessible(true);
                    query += field.get(object);
                    query+=",";
                }
                query = query.substring(0,query.length()-1)+')';
                System.out.println(query);
                //stmt.executeUpdate(query);
                //stmt.executeUpdate("INSERT INTO Users " + "VALUES (4, 'haitham@gmail.com','Haitham', 'haitham1234', 4000 )");
            }
            catch (ClassNotFoundException e)
            {
                try {
                    throw new ClassNotFoundException("Driver class loading was failed");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Table Connection was failed");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    public static void deleteUser() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_ex", "root", "1234");
            Statement stmt = con.createStatement();
            String query = "delete from  Users " +
                    "where id = 4";
            stmt.executeUpdate(query);

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void updateUser() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_ex", "root", "1234");
            Statement stmt = con.createStatement();
            String query = "update Users set Name ='Mohammad' " + "where id = 1";
            stmt.executeUpdate(query);

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }



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