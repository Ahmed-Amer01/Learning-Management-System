package com.example.demo;

import org.hibernate.dialect.Dialect;

import java.sql.Types;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        super();
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.BIT, "integer");
        // Add other customizations here if needed
    }
}