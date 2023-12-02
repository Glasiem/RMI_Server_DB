package com.example.rmi.component.io;


import com.example.rmi.component.Column;
import com.example.rmi.component.Database;
import com.example.rmi.component.Row;
import com.example.rmi.component.Table;
import com.example.rmi.component.column.ColumnType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLDatabaseExporter {

    public static void exportDatabase(Database localDatabase, String jdbcUrl, String username, String password) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try {
                connection.setAutoCommit(false);
                for (Table table : localDatabase.tables) {
                    exportTable(connection, table);
                }
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                ex.printStackTrace();
            } finally {
                connection.setAutoCommit(false);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void exportTable(Connection connection, Table table) throws SQLException {
        // Create table
        String createTableSQL = generateCreateTableSQL(table);
        try (PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL)) {
            createTableStatement.execute();
        }

        // Insert data
        String insertDataSQL = generateInsertDataSQL(table);
        try (PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL)) {
            for (Row row : table.rows) {
                setPreparedStatementParameters(insertDataStatement, table, row);
                insertDataStatement.addBatch();
            }
            insertDataStatement.executeBatch();
        }
    }

    private static String generateCreateTableSQL(Table table) {
        StringBuilder sqlBuilder = new StringBuilder("DROP TABLE IF EXISTS ").append(table.name).append("; ")
                .append("CREATE TABLE ")
                .append(table.name)
                .append(" (");

        for (Column column : table.columns) {
            sqlBuilder.append(column.name)
                    .append(" ")
                    .append(ColumnType.valueOf(column.type).getSqlTypeName())
                    .append(", ");
        }

        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }

    private static String generateInsertDataSQL(Table table) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ")
                .append(table.name)
                .append(" (");

        for (Column column : table.columns) {
            sqlBuilder.append(column.name).append(", ");
        }

        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());

        sqlBuilder.append(") VALUES (");

        table.columns.forEach(c -> sqlBuilder.append("?, "));

        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());

        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }

    private static void setPreparedStatementParameters(PreparedStatement statement, Table table, Row row) throws SQLException {
        for (int i = 0; i < table.columns.size(); i++) {
            if (table.columns.get(i).type.equals("INT"))
                statement.setInt(i + 1, Integer.parseInt(row.getAt(i)));
            else if (table.columns.get(i).type.equals("REAL")) {
                statement.setBigDecimal(i + 1, new BigDecimal(row.getAt(i)));
            } else
                statement.setString(i + 1, row.getAt(i));
        }
    }
}