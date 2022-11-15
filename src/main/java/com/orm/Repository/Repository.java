package com.orm.Repository;

import com.orm.Connection.SqlManager;
import com.orm.Utils.ErrorHandlling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Repository<T> {
    private static Logger logger = LogManager.getLogger(Repository.class);
    private final Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
        ErrorHandlling.validate(clz, logger);
        SqlManager.createTable(clz);
    }

    public List<T> findAll() {
        ErrorHandlling.validate(clz, logger);
        return SqlManager.findAll(this.clz);
    }

    public List<T> findAll(String propertyName, Object property) {
        ErrorHandlling.validate(clz, propertyName, property, logger);
        return SqlManager.findAll(this.clz, propertyName, property);
    }

    public T getItemById(int id) {
        ErrorHandlling.validate(clz, id, logger);
        return SqlManager.getItemById(this.clz, id);
    }

    public void save(T t) {

        ErrorHandlling.validate(t, logger);
        SqlManager.addSingleItem(t);
    }

    public void save(T... t) {

        ErrorHandlling.validate(t, logger);
        SqlManager.addMultipleItems(t);
    }

    public T getOneById(int id) {
        ErrorHandlling.validate(clz, id, logger);
        return SqlManager.getItemById(this.clz, id);
    }

    public T updatePropertyById(String propertyName, Object property, int id) {
        ErrorHandlling.validate(clz, propertyName, property, id, logger);
        return SqlManager.updatePropertyById(this.clz, propertyName, property, id);
    }

    public T updateItem(T object, int id) {
        ErrorHandlling.validate(clz, object, id, logger);
        return SqlManager.updateEntireItem(this.clz, object, id);
    }

    public List<T> deleteOneItemByProperty(String propertyName, Object property) {
        ErrorHandlling.validate(clz, propertyName, property, logger);
        return SqlManager.deleteSingleItemByProperty(this.clz, propertyName, property);
    }

    public List<T> deleteItemsByProperty(String propertyName, Object property) {
        ErrorHandlling.validate(clz, propertyName, property, logger);
        return SqlManager.deleteItemsByProperty(this.clz, propertyName, property);
    }

    public void deleteTable() {
        ErrorHandlling.validate(clz, logger);
        SqlManager.deleteTable(this.clz);
    }
}
