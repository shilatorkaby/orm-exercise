package com.orm.Utils;

public class JavaToSqlTypeMapper {

    public static String mapJavaFieldToSql(String javaType) {
        switch (javaType) {
            case "String":
                return "VARCHAR(255)";
            case "Bignum":
                return "NUMERIC";
            case "short":
                return "SMALLINT";
            case "int":
            case "Integer":
                return
                        "INTEGER";
            case "float":
                return "REAL";
            case "double":
                return "DOUBLE";
            case "Date":
                return "DATE";
            default:
                return "VARCHAR(255)";
        }
    }
}

