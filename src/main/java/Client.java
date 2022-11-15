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
//        Gson g = new Gson();
//        Name name = g.fromJson("{\"firstName\":\"shilat\",\"lastName\":\"orkaby\"}",Name.class);
//        System.out.println(name);
        users.save(user1);
        //System.out.println(SqlQueryFactory.createDeleteTableQuery(User.class));
       // System.out.println(SqlQueryFactory.createAddSingleItemToTableQuery(user1));
        List<User> userList = users.findAll();
//        System.out.println(userList);
        //SqlQueryFactory.getValues(user);
    }
}
