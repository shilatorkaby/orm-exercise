package com.orm.Repository;

import com.orm.Entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTests {
    private static Repository repo;

    @BeforeEach
    void setup() {
        repo = new Repository<>(User.class);
    }

    @AfterEach
    void after() {
        repo.deleteTable();
    }

    @Test
    void Constructor_RightParam_Work() {
        assertDoesNotThrow((Executable) new Repository<>(User.class));
    }

    @Test
    void Constructor_NullClass_Exception() {
        assertThrows(Exception.class, () -> new Repository<>(null));
    }

    @Test
    void findAll_RightPropertyType_Work() {
        Repository<User> repo = new Repository<>(User.class);
        assertDoesNotThrow((Executable) repo.findAll());
    }

    @Test
    void findAll_WrongPropertyType_Exception() {
    }

    @Test
    void findAllWithParam_RightPropertyType_Work() {
        Repository<User> repo = new Repository<>(User.class);
//        User user = new User();
//        user.setEmail("khader@gmail.com");
//        user.setName("Khader");
//        user.setId(1);
//        repo.save(user);


        assertEquals(2, repo.findAll("id", 1).size());
    }

    @Test
    void findAllWithParam_WrongPropertyName_Exception() {
    }

    @Test
    void getItemById_RightId_Work() {
    }

    @Test
    void getItemById_WrongId_Exception() {
    }

    @Test
    void saveSingleItem_RightItem_Work() {
    }

    @Test
    void saveSingleItem_WrongItem_Exception() {
    }

    @Test
    void saveMultipleItem_RightItem_Work() {
    }

    @Test
    void saveMultipleItem_WrongItem_Exception() {
    }

    @Test
    void updatePropertyById_RightParams_Work() {
    }

    @Test
    void updatePropertyById_WrongProperty_Exception() {
    }

    @Test
    void updateItem_RightItemAndId_Work() {
    }

    @Test
    void updateItem_RightItemWrongId_Exception() {
    }

    @Test
    void deleteOneItemByProperty_RightParams_Work() {
    }

    @Test
    void deleteOneItemByProperty_WrongPropertyName_Exception() {
    }

    @Test
    void deleteItemsByProperty_RightParams_Work() {
    }

    @Test
    void deleteItemsByProperty_WrongPropertyName_Exception() {
    }

    @Test
    void deleteTable_RightClass_Work() {
        assertDoesNotThrow(() -> repo.deleteTable());
    }

    @Test
    void deleteTable_nullClass_Exception() {
        Repository<Object> repoTest = new Repository<>(null);
        assertThrows(Exception.class, () -> repoTest.deleteTable());

    }


}
