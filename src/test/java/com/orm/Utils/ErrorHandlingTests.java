package com.orm.Utils;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErrorHandlingTests<T> {
    Class<T> clz;

    @BeforeAll
    static  void errorHandling_Setup(){

    }

    @Test
    void validate_ifPropertyIsNull_nullProperty()
    {
       // assertThrows(Exception.class,() -> ErrorHandling.validate(clz,"isExist",true,Logger logger);
    }




}
