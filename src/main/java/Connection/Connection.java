package Connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;

public class Connection<T> {
    private Class<T> clz;

    public Connection(Class<T> clz) {
        this.clz = clz;
    }

    public static List<User> ConnectionToSQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "root", "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user");
            List<User> results = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                results.add(user);
            }
            con.close();
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static <T> List<T> ConnectionToSQLGen(Class<T> clz) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "root", "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("select * from %s", clz.getSimpleName().toLowerCase()));
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }

                results.add(item);
            }
            con.close();
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}