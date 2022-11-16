package com.orm.Entity;

import java.util.concurrent.ThreadLocalRandom;
import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;

public class User {


//    @PrimaryKey
//    @AutoIncrement
    int id;
    Name name;
    String email;
    String password;


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

    public void setName(Name name) {
        this.name = new Name(name.getFirstName(),name.getLastName());
    }

    public void setName(String name) {
        this.name = new Name(name, name);
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
