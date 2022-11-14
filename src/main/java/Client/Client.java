package Client;
import Connection.Repository;
import Entity.User;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        Repository<User> repository = new Repository<>(User.class);

        List<User> users = repository.ConnectionToSQL(User.class);
        users.forEach(user -> System.out.println(user.toString()));

        repository.addObjectToDB(User.class,new User());
 //       Repository.insertUser();
//        Repository.deleteUser();
//        Repository.updateUser();

//        List<User> users = Repository.ConnectionToSQL();
//        users.forEach(user -> System.out.println(users.toString()));


    }
}
