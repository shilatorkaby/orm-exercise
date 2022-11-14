import com.orm.Connection.SqlQueryFactory;
import com.orm.Entity.User;
import com.orm.Connection.SqlManager;
import com.orm.Repository.Repository;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        Repository<User> users = new Repository(User.class);
        User user1 = new User();
        User user2 = new User();
//        users.save(user1);
//        users.save(user2);
        SqlManager.addMultipleItems(user1, user2);
        List<User> userList = users.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
        //SqlQueryFactory.getValues(user);
    }
}
