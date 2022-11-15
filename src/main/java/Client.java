import com.orm.Connection.SqlQueryFactory;
import com.orm.Entity.User;
import com.orm.Connection.SqlManager;
import com.orm.Repository.Repository;

import java.util.List;

public class Client {
    public static void main(String[] args) {

        Repository<User> users = new Repository(User.class);
        User user1 = new User();
        users.save(user1);
        //System.out.println(SqlQueryFactory.createDeleteTableQuery(User.class));
       // System.out.println(SqlQueryFactory.createAddSingleItemToTableQuery(user1));
        List<User> userList = users.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
//        System.out.println(userList);
        //SqlQueryFactory.getValues(user);
    }
}
