package com.orm.Repository;

import com.orm.Entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTests {
    private static Repository<User> repo;

    private static void createUsers(Repository<User> repo, int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            repo.save(new User());

        }
    }

    @BeforeEach
    void setup() {
        repo = new Repository<>(User.class);
        createUsers(repo, 2);
    }

    @AfterEach
    void after() {

        repo.deleteTable();
    }

    @Test
    void Constructor_RightParam_Work() {
        assertDoesNotThrow(() -> {
            new Repository<>(User.class);
        });
    }

    @Test
    void Constructor_NullClass_Exception() {
        assertThrows(Exception.class, () -> new Repository<>(null));
    }

    @Test
    void findAll_RightPropertyType_Work() {
        repo.deleteTable();
        Repository<User> repoTest = new Repository<>(User.class);

        User user1 = new User();
        User user2 = new User();
        repoTest.save(user2);
        repoTest.save(user1);

        List<User> userList = new ArrayList<User>(Arrays.asList(user2, user1));


//        assertDoesNotThrow(() -> repo.findAll());
        assertEquals(userList.toString(), repoTest.findAll().toString());
        repoTest.deleteTable();

    }

    @Test
    void findAll_WrongPropertyType_Exception() {
//        assertThrows(Exception.class,()-> repo.findAll());


    }

    @Test
    void findAllWithParam_RightPropertyType_Work() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);

        assertEquals(user.toString(), repo.findAll("id", 1).get(0).toString());
    }

    @Test
    void findAllWithParam_WrongPropertyName_Exception() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);

        assertThrows(Exception.class, () -> repo.findAll("k", 1));
    }

    @Test
    void getItemById_RightId_Work() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);

        assertEquals(user.toString(), repo.getItemById(1).toString());
    }

    @Test
    void getItemById_WrongId_Exception() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);
        assertThrows(Exception.class, () -> repo.getItemById(2));
    }

    @Test
    void saveSingleItem_RightItem_Work() {
        User user = new User();
        user.setId(1);

        assertDoesNotThrow(() -> repo.save(user));
        assertEquals(user.toString(), repo.getItemById(1).toString());
    }

    @Test
    void saveSingleItem_WrongItem_Exception() {
//        Car car = new Car("car1", 1);
//        assertDoesNotThrow(() -> repo.save(car));
    }

    @Test
    void saveMultipleItem_RightItem_Work() {
        User user1 = new User();
        User user2 = new User();
        assertDoesNotThrow(() -> repo.save(user1, user2));
    }

    @Test
    void saveMultipleItem_WrongItem_Exception() {
//        Car car1 = new Car("car1", 1);
//        Car car2 = new Car("car2", 2);
//        assertDoesNotThrow(() -> repo.save(car1, car2));

    }

    @Test
    void updatePropertyById_RightParams_Work() {
        User user = new User();
        user.setEmail("khader@gmail.com");
        user.setId(10);
        repo.save(user);

        repo.updatePropertyById("email", "what@gmail.com", 10);

        User updatedUser = new User();
        updatedUser.setName(user.getName());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail("what@gmail.com");
        updatedUser.setId(10);

        assertEquals(updatedUser.toString(), repo.getItemById(10).toString());
    }

    @Test
    void updatePropertyById_WrongProperty_Exception() {
    }

    @Test
    void updateItem_RightItemAndId_Work() {
        User user = new User();
        user.setId(10);
        repo.save(user);

        User updatedUser = new User();
        updatedUser.setName("khader");
        updatedUser.setPassword("khaderPass");
        updatedUser.setEmail("what@gmail.com");
        updatedUser.setId(10);

        repo.updateItem(updatedUser, 10);

        assertEquals(updatedUser.toString(), repo.getItemById(10).toString());
    }

    @Test
    void updateItem_RightItemWrongId_Exception() {
    }

    @Test
    void deleteOneItemByProperty_RightParams_Work() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);

        assertDoesNotThrow(() -> repo.deleteOneItemByProperty("name", "Khader"));
    }

    @Test
    void deleteOneItemByProperty_WrongPropertyName_Exception() {
        User user = new User();
        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
        user.setId(1);
        repo.save(user);

        assertThrows(Exception.class, () -> repo.deleteOneItemByProperty("id", "Khader"));
    }

    @Test
    void deleteItemsByProperty_RightParams_Work() {
        User user1 = new User();
        user1.setEmail("khader@gmail.com");
//        user1.setName("Khader");
        user1.setId(1);
        repo.save(user1);

        User user2 = new User();
        user2.setEmail("khsader@gmail.com");
//        user2.setName("Khader");
        user2.setId(2);
        repo.save(user2);

        assertDoesNotThrow(() -> repo.deleteItemsByProperty("name", "Khader"));
    }

    @Test
    void deleteItemsByProperty_WrongPropertyName_Exception() {

        User user1 = new User();
        user1.setEmail("khader@gmail.com");
//        user1.setName("Khader");
        user1.setId(1);
        repo.save(user1);

        User user2 = new User();
        user2.setEmail("khsader@gmail.com");
//        user2.setName("Khader");
        user2.setId(2);
        repo.save(user2);

        assertThrows(Exception.class, () -> repo.deleteItemsByProperty("id", "Khader"));

    }

    @Test
    void deleteTable_RightClass_Work() {
        assertDoesNotThrow(() -> repo.deleteTable());
    }

    @Test
    void deleteTable_nullClass_Exception() {
//        Repository<User> repoTest = new Repository<>(User.class);
//        assertThrows(Exception.class, () -> repoTest.deleteTable());

    }


}
