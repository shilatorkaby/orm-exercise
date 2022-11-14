import com.orm.Entity.User;
import com.orm.Connection.SqlManager;

public class Client {
    public static void main(String[] args) {
        SqlManager.createTable(User.class);
    }
}
