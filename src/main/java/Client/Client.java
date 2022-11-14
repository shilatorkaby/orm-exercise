package Client;
import Connection.Repository;
import Entity.User;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        Repository<User> repository = new Repository<>(User.class);
        List<User> users = repository.ConnectionToSQL(User.class);
        users.forEach(user -> System.out.println(user.toString()));
        System.out.println("get user by id:\n"+repository.getItemById(User.class,2));
        System.out.println("get user by property:\n"+repository.getItemByProperty(User.class,"password",1234));


    }
}
