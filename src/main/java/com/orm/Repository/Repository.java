package com.orm.Repository;

import com.orm.Connection.SqlManager;

import java.util.List;

public class Repository<T> {
    private Class<T> clz;
    private String tableName;


    public Repository(Class<T> clz) {
        this.clz = clz;
        this.tableName = String.format("%s_data", clz.getSimpleName().toLowerCase());
        SqlManager.createTable(clz);
    }

    public List<T> findAll() {
        return SqlManager.findAll(this.clz);
    }

    public T getOneById(int id) {
        return SqlManager.getItemById(this.clz, id);
    }
}
