package com.example.rmi.component.io;


import com.example.rmi.DatabaseManager;
import com.example.rmi.component.Row;
import com.example.rmi.component.column.ColumnType;

import java.sql.*;

public class SQLDatabaseImporter {
    private SQLDatabaseImporter(){}
    public static void importDatabase(String jdbcUrl, String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            DatabaseManager.getInstance().createDB("ImportedDB"); // Change as needed

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema='public';");

            int tableIndex = 0;
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                DatabaseManager.getInstance().addTable(tableName);

                importTable(connection, tableName, tableIndex++);
            }

//            DBMS.getInstance().renderCells();

        }
    }

    private static void importTable(Connection connection, String tableName, int tableIndex) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet columnResultSet = statement.executeQuery("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "';");

        while (columnResultSet.next()) {
            String columnName = columnResultSet.getString("column_name");
            String columnType = columnResultSet.getString("data_type");
            ColumnType columnTypeEnum = convertPostgresTypeToColumnType(columnType);

            DatabaseManager.getInstance().addColumn(tableIndex, columnName, columnTypeEnum);
        }

        ResultSet dataResultSet = statement.executeQuery("SELECT * FROM " + tableName + ";");

        while (dataResultSet.next()) {
            Row row = new Row();

            for (int i = 1; i <= dataResultSet.getMetaData().getColumnCount(); i++) {
                String data = dataResultSet.getString(i);

                if (!data.isEmpty() && !data.isBlank())
                    row.values.add(data);
            }

            DatabaseManager.getInstance().addRow(tableIndex, row);
        }
    }

    private static ColumnType convertPostgresTypeToColumnType(String postgresType) {
        if (postgresType.equalsIgnoreCase("integer")) {
            return ColumnType.INT;
        } else if (postgresType.equalsIgnoreCase("character")) {
            return ColumnType.CHAR;
        } else if (postgresType.equalsIgnoreCase("numeric") || postgresType.equalsIgnoreCase("decimal")) {
            return ColumnType.REAL;
        } else {
            System.out.println(postgresType);
            return ColumnType.STRING;
        }
    }
}
