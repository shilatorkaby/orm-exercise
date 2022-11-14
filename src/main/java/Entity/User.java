package Entity;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.github.javafaker.Faker;

public class User {
    int id;
    String name;
    String email;
    String password;



    public User() {
        id = ThreadLocalRandom.current().nextInt(100000000,999999999);
        name = new Faker().name().firstName();
        email = name+ ThreadLocalRandom.current().nextInt(100, 299 + 1)+"@gmail.com";
        password = name+ ThreadLocalRandom.current().nextInt(10000, 99999);

    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }




//    public static List<User> ConnectionToSQL() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_data", "root","");
//
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from user_data");
//            List<User> results = new ArrayList<>();
//            while (rs.next()) {
//                User user = new User();
//                user.setId(rs.getInt("id"));
//                user.setEmail(rs.getString("email"));
//                user.setName(rs.getString("name"));
//                user.setPassword(rs.getString("password"));
//
//                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//                results.add(user);
//
//                con.close();
//                return results;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }



}
