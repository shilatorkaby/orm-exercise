import com.orm.Connection.SqlQueryFactory;
import com.orm.Entity.User;
import com.orm.Connection.SqlManager;
import com.orm.Repository.Repository;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        SqlManager.deleteTable(User.class);
        Repository<User> users = new Repository(User.class);
        User user1 = new User();
        users.save(user1);
        List<User> userList = users.findAll();
        System.out.println(userList);
        System.out.println(SqlQueryFactory.createUpdateItemQuery(User.class,user1,user1.getId()));
    }
}
