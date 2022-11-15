package com.orm.Entity;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.github.javafaker.Faker;
import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;
import com.orm.Annotation.Unique;
//import com.github.javafaker.Name;

public class User {


    @PrimaryKey
    @AutoIncrement
    int id;
    Name name;
    String email;
    String password;



    public User() {
        id = ThreadLocalRandom.current().nextInt(100000000,999999999);
        name = new Name("shilat","orkaby");//new Faker().name().firstName();
        email = name.firstName+ ThreadLocalRandom.current().nextInt(100, 299 + 1)+"@gmail.com";
        password = name.firstName+ ThreadLocalRandom.current().nextInt(10000, 99999);

    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setEmail(Name name) {
        this.name = new Name(name.getFirstName(),name.getLastName());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
