package com.orm.Utils;
import com.orm.Entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingTests<T> {
    private static Logger logger;

    @BeforeAll
    static  void errorHandling_Setup(){
        logger= LogManager.getLogger(ErrorHandlingTests.class);
    }

    @Test
    void validate_ifPropertyNameIsNull_nullProperty()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validate(User.class,null,null,logger));
    }
    @Test
    void validate_ifPropertyNameIsNull_notNullProperty()
    {
        assertDoesNotThrow(() -> ErrorHandling.validate(User.class,"isExist",true,logger));
    }
    @Test
    void validateId_ifIdIsNegative_idIsPositive()
    {
        assertDoesNotThrow(() -> ErrorHandling.validateId(5,logger));
    }
    @Test
    void validateId_ifIdIsNegative_idIsNegative()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validateId(-5,logger));
    }

    @Test
    void validateClz_ifClzIsNull_notNullClz()
    {
        assertDoesNotThrow(() -> ErrorHandling.validateClz(User.class,logger));
    }
    @Test
    void validateClz_ifClzIsNull_NullClz()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validateClz(null,logger));
    }
    @Test
    void validateItem_ifItemIsNull_NullItem()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validateItem(null,logger));
    }
    @Test
    void validateItem_ifItemIsNull_notNullItem()
    {
        assertDoesNotThrow(() -> ErrorHandling.validateItem(new User(),logger));
    }
    @Test
    void validatePropertyName_ifPropertyNameIsEmpty_legalName()
    {
        assertDoesNotThrow(() -> ErrorHandling.validateItem("id",logger));
    }
    @Test
    void validatePropertyName_ifPropertyNameIsEmpty_nullName()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validatePropertyName(null,logger));
    }
    @Test
    void validatePropertyName_ifPropertyNameIsEmpty_emptyName()
    {
        assertThrows(Exception.class,() -> ErrorHandling.validatePropertyName("",logger));
    }




}
