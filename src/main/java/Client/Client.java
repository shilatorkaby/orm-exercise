package Client;

import Connection.Connection;

public class Client {
    public static void main(String[] args) {
        List<User> users = Connection.ConnectionToSQL();
        users.forEach(user -> System.out.println(users.toString()));
        List<User> users = Connection.ConnectionToSQLGen(User.class);

        Connection<User> userRepo = new Connection<>(User.class);
        List<User> users = userRepo.
    }
}
