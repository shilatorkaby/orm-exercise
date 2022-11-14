package Connection;
import java.sql.*;

public class Connect {

    java.sql.Connection con;
    Statement stmt;

    private static Connect connect;

    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_data", "root", "");
            this.stmt = con.createStatement();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public static Connect getInstance()
        {
            if (connect==null)
                connect = new Connect();
            return connect;
        }

    Statement getStmt() {
        return stmt;
    }

}


