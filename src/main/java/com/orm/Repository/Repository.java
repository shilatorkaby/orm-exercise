package com.orm.Repository;

import com.orm.Connection.SqlManager;

import java.util.List;

public class Repository<T> {
    private final Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
        SqlManager.createTable(clz);
    }

    public List<T> findAll() {
        return SqlManager.findAll(this.clz);
    }

    public List<T> findAll(String propertyName, Object property) {
        return SqlManager.findAll(this.clz,propertyName,property);
    }

    public T getItemById(int id) {
        return SqlManager.getItemById(this.clz, id);
    }

    public void save(T t) {
        SqlManager.addSingleItem(t);
    }

    public T getOneById(int id) {
        return SqlManager.getItemById(this.clz, id);
    }

    public T updatePropertyById(String propertyName, Object property, int id) {
        return SqlManager.updatePropertyById(this.clz,propertyName,property,id);
    }

    public T updateItem(T object,int id) {
        return SqlManager.updateEntireItem(this.clz,object,id);
    }

    public List<T> deleteOneItemByProperty(String propertyName, Object property) {
        return SqlManager.deleteSingleItemByProperty(this.clz,propertyName,property);
    }

    public List<T> deleteItemsByProperty(String propertyName, Object property) {
        return SqlManager.deleteItemsByProperty(this.clz,propertyName,property);
    }

    public void deleteTable() {
        SqlManager.deleteTable(this.clz);
    }
}
