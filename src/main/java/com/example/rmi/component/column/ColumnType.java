package com.example.rmi.component.column;

public enum ColumnType {
    INT("INT"),
    CHAR("CHAR(1)"),
    REAL("DECIMAL"),
    STRING("VARCHAR(256)"),

    MONEY("DECIMAL"),
    MONEY_INVL("VARCHAR(256)");
    private final String sqlTypeName;

    ColumnType(String sqlTypeName) {
        this.sqlTypeName = sqlTypeName;
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

}