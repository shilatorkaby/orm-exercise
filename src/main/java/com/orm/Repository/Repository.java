package com.orm.Repository;

import com.orm.Connection.SqlManager;
import com.orm.Utils.ErrorHandling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Repository<T> {
    private static Logger logger = LogManager.getLogger(Repository.class);
    private final Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
        ErrorHandling.validate(clz, logger);
        SqlManager.createTable(clz);
    }

    public List<T> findAll() {
        ErrorHandling.validate(clz, logger);
        return SqlManager.findAll(this.clz);
    }

    public List<T> findAll(String propertyName, Object property) {
        ErrorHandling.validate(clz, propertyName, property, logger);
        return SqlManager.findAll(this.clz, propertyName, property);
    }

    public T getItemById(int id) {
        ErrorHandling.validate(clz, id, logger);
        return SqlManager.getItemById(this.clz, id);
    }

    public void save(T t) {
        ErrorHandling.validate(t, logger);
        SqlManager.addSingleItem(t);
    }

    public void save(T... t) {

        ErrorHandling.validate(t, logger);
        SqlManager.addMultipleItems(t);
    }

    public T getOneById(int id) {
        ErrorHandling.validate(clz, id, logger);
        return SqlManager.getItemById(this.clz, id);
    }

    public int updatePropertyById(String propertyName, Object property, int id) {
        ErrorHandling.validate(clz, propertyName, property, id, logger);
        return SqlManager.updatePropertyById(this.clz, propertyName, property, id);
    }

    public int updateItem(T object, int id) {
        ErrorHandling.validate(clz, object, id, logger);
        return SqlManager.updateEntireItem(this.clz, object, id);
    }

    public int deleteOneItemByProperty(String propertyName, Object property) {
        ErrorHandling.validate(clz, propertyName, property, logger);
        return SqlManager.deleteSingleItemByProperty(this.clz, propertyName, property);
    }

    public int deleteItemsByProperty(String propertyName, Object property) {
        ErrorHandling.validate(clz, propertyName, property, logger);
        return SqlManager.deleteItemsByProperty(this.clz, propertyName, property);
    }

    public int deleteTable() {
        ErrorHandling.validate(clz, logger);
        return SqlManager.deleteTable(this.clz);
    }
}
