package com.orm.Entity;

import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;

public class Car {

    @AutoIncrement
    private int id;
    @PrimaryKey
    private String name;
}
