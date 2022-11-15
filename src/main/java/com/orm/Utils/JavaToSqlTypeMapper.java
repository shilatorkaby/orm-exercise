package com.orm.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaToSqlTypeMapper {

    public static String mapJavaFieldToSql(String javaType) {
        return map().getOrDefault(javaType, "VARCHAR(255)");
    }

    public static boolean nonPrimitiveType(String javaType) {
        return (!map().containsKey(javaType));
    }

    static HashMap<String, String> map() {
        HashMap<String, String> typesMap = new HashMap<>();
        typesMap.put("String", "VARCHAR(255)");
        typesMap.put("int", "INTEGER");
        typesMap.put("Integer", "INTEGER");
        typesMap.put("float", "REAL");
        typesMap.put("double", "DOUBLE");
        typesMap.put("Date", "DATE");
        typesMap.put("Bignum", "NUMERIC");
        typesMap.put("short", "SMALLINT");
        return typesMap;
    }
}


