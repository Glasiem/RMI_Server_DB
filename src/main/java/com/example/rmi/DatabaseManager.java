package com.example.rmi;

import java.sql.SQLException;

import com.example.rmi.component.Table;
import com.example.rmi.component.Column;
import com.example.rmi.component.Database;
import com.example.rmi.component.Row;
import com.example.rmi.component.column.*;
import com.example.rmi.component.io.SQLDatabaseExporter;
import com.example.rmi.component.io.SQLDatabaseImporter;

public class DatabaseManager {
    private static DatabaseManager instance;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
//    public static DBMS instanceCSW;

    private DatabaseManager(){
    }

    public static DatabaseManager getInstance(){
        if (instance == null){
            instance = new DatabaseManager();
//            instanceCSW = DBMS.getInstance();
        }
        return instance;
    }

    public static Database database;

    public void populateTable() {
        Table table = new Table("newTestTable" + database.tables.size());
        table.addColumn(new IntegerColumn("column1"));
        table.addColumn(new RealColumn("column2"));
        table.addColumn(new StringColumn("column3"));
        table.addColumn(new CharColumn("column4"));
        table.addColumn(new MoneyColumn("column5"));
        table.addColumn(new MoneyInvlColumn("column6","0","1000"));
        Row row1 = new Row();
        row1.values.add("10");
        row1.values.add("10.0");
        row1.values.add("10");
        row1.values.add("1");
        row1.values.add("10.00");
        row1.values.add("10.00");
        table.addRow(row1);
        Row row2 = new Row();
        row2.values.add("15");
        row2.values.add("15.0");
        row2.values.add("15");
        row2.values.add("3");
        row2.values.add("15.00");
        row2.values.add("15.00");
        table.addRow(row2);
        database.addTable(table);
        SQLDatabaseExporter.exportDatabase(database,JDBC_URL,USERNAME,PASSWORD);
    }

//    public void openDB(String path){
//        DatabaseImporter.importDatabase(path);
//    }

    public void openRelationalDB(String name) throws SQLException {
        SQLDatabaseImporter.importDatabase(
                JDBC_URL +name, USERNAME, PASSWORD);
    }
    public void saveToRelationalDB(String name)  {
        SQLDatabaseExporter.exportDatabase(database,
                JDBC_URL +name, USERNAME, PASSWORD);
    }
    public String renameDB(String name){
        if (name != null && !name.isEmpty()) {
            database.setName(name);
//            instanceCSW.databaseLabel.setText(database.name);
            return name;
        }
        else return null;
    }

//    public void saveDB(String path) {
//        DatabaseExporter.exportDatabase(database, path);
//    }

//    public void deleteDB() {
//        database = null;
//        while (instanceCSW.tabbedPane.getTabCount() > 0) {
//            instanceCSW.tabbedPane.removeTabAt(0);
//        }
//    }

    public void createDB(String name) {
        database = new Database(name);
//        instanceCSW.databaseLabel.setText(database.name);
    }

//    public boolean existDB(){
//        return database != null;
//    }

    public Boolean addTable(String name){
        if (name != null && !name.isEmpty()) {
//            JPanel tablePanel = instanceCSW.createTablePanel();

//            DBMS.getInstance().tabbedPane.addTab(name, tablePanel);
            Table table = new Table(name);
            database.addTable(table);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean renameTable(int tableIndex, String name){
        if (name != null && !name.isEmpty() && tableIndex != -1) {
//            instanceCSW.tabbedPane.setTitleAt(tableIndex,name);
            database.tables.get(tableIndex).setName(name);
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean deleteTable(int tableIndex){

        if (tableIndex != -1) {
//            instanceCSW.tabbedPane.removeTabAt(tableIndex);

            database.deleteTable(tableIndex);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean addColumn(int tableIndex, String columnName, ColumnType columnType) {
        return addColumn(tableIndex, columnName, columnType, "", ""); // Default min and max
    }

    public Boolean addColumn(int tableIndex, String columnName, ColumnType columnType, String min, String max) {
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1) {
//                JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
//                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
//                JTable table = (JTable) scrollPane.getViewport().getView();
//                CustomTableModel tableModel = (CustomTableModel) table.getModel();

//                columnName +=  ;
//                if(!min.equals("") && !max.equals("")){
//                    tableModel.addColumn(columnName + " (" + columnType.name() + ")" + "(" + min + ":" + max + ")");
//                } else {
//                    tableModel.addColumn(columnName + " (" + columnType.name() + ")");
//                }

                switch (columnType) {
                    case INT -> {
                        Column columnInt = new IntegerColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnInt);
                    }
                    case REAL -> {
                        Column columnReal = new RealColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnReal);
                    }
                    case STRING -> {
                        Column columnStr = new StringColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnStr);
                    }
                    case CHAR -> {
                        Column columnChar = new CharColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnChar);
                    }
                    case MONEY -> {
                        Column columnMoney = new MoneyColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnMoney);
                    }
                    case MONEYINVL -> {
                        Column columnMoneyInvl = new MoneyInvlColumn(columnName, min, max);
                        database.tables.get(tableIndex).addColumn(columnMoneyInvl);
                    }
                }
                for (Row row : database.tables.get(tableIndex).rows) {
                    row.values.add("");
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }


    public Boolean renameColumn(int tableIndex, int columnIndex/*, String oldColumnName*/, String newColumnName/*, JTable table*/){
        if (newColumnName != null && !newColumnName.isEmpty()) {
            if (tableIndex != -1 && columnIndex != -1) {
//                TableColumn column = table.getColumnModel().getColumn(columnIndex);
//                column.setHeaderValue(column.getHeaderValue().toString().replace(oldColumnName, newColumnName));
//                table.getTableHeader().repaint();

                database.tables.get(tableIndex).columns.get(columnIndex).setName(newColumnName);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public Boolean changeColumnType(int tableIndex, int columnIndex, ColumnType columnType/*, JTable table*/){
        return changeColumnType(tableIndex, columnIndex, columnType,/*table,*/ "", "");
    }

    public Boolean changeColumnType(int tableIndex, int columnIndex, ColumnType columnType/*, JTable table*/, String min, String max){
        if (tableIndex != -1 && columnIndex != -1) {

//            String name = database.tables.get(tableIndex).columns.get(columnIndex).name;
//            TableColumn column1 = table.getColumnModel().getColumn(columnIndex);
//            if(!min.equals("") && !max.equals("")){
//                column1.setHeaderValue(name + " (" + columnType.name() + ")" + "(" + min + ":" + max + ")");
//            } else {
//                column1.setHeaderValue(name + " (" + columnType.name() + ")");
//            }
//
//            table.getTableHeader().repaint();


            switch (columnType) {
                case INT -> {
                    Column columnInt = new IntegerColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnInt);
                }
                case REAL -> {
                    Column columnReal = new RealColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnReal);
                }
                case STRING -> {
                    Column columnStr = new StringColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnStr);
                }
                case CHAR -> {
                    Column columnChar = new CharColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnChar);
                }
                case MONEY -> {
                    Column columnMoney = new MoneyColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnMoney);
                }
                case MONEY_INVL -> {
                    Column column = database.tables.get(tableIndex).columns.get(columnIndex);
//                    String name = column.name + "(" + min + ":" + max + ")";
                    Column columnMoneyInvl = new MoneyInvlColumn(column.name, min, max);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnMoneyInvl);
                }
            }
            for (Row row: database.tables.get(tableIndex).rows) {
                row.values.set(columnIndex,"");
            }
            return true;
//            for (int i = 0; i < database.tables.get(tableIndex).rows.size(); i++) {
//                table.setValueAt("", i, columnIndex);
//            }
        }
        else {
            return false;
        }
    }

    public Boolean deleteColumn(int tableIndex, int columnIndex/*, CustomTableModel tableModel*/){
        if (columnIndex != -1) {
//            tableModel.removeColumn(columnIndex);
            database.tables.get(tableIndex).deleteColumn(columnIndex);
            return true;
        } else {
            return false;
        }
    }

    public Boolean addRow(int tableIndex, Row row){
        if (tableIndex != -1) {
            for (int i = row.values.size(); i < database.tables.get(tableIndex).columns.size(); i++) {
                row.values.add("");
            }
//            JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
//            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
//            JTable table = (JTable) scrollPane.getViewport().getView();
//            CustomTableModel tableModel = (CustomTableModel) table.getModel();
//            tableModel.addRow(new Object[tableModel.getColumnCount()]);
            database.tables.get(tableIndex).addRow(row);
            System.out.println(row.values);
            System.out.println(database.tables.get(tableIndex).rows.size());
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean deleteRow(int tableIndex, int rowIndex/*, CustomTableModel tableModel*/){
        if (rowIndex != -1) {
//            tableModel.removeRow(rowIndex);
            database.tables.get(tableIndex).deleteRow(rowIndex);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean updateCellValue(String value, int tableIndex, int columnIndex, int rowIndex/*, CustomTable table*/){
        if (database.tables.get(tableIndex).columns.get(columnIndex).validate(value)){
            database.tables.get(tableIndex).rows.get(rowIndex).setAt(columnIndex,value.trim());
            return true;
        }
        return false;
//        else {
//            String data = database.tables.get(tableIndex).rows.get(rowIndex).getAt(columnIndex);
//            if (data != null){
//                table.setValueAt(data, rowIndex, columnIndex);
//            }
//            else {
////                table.setValueAt("", rowIndex, columnIndex);
//            }
//
//            JFrame frame = new JFrame("Помилка!!!");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            JOptionPane.showMessageDialog(
//                    frame,
//                    "Введено некоректне значення",
//                    "Помилка!!!",
//                    JOptionPane.INFORMATION_MESSAGE
//            );
//        }
    }

    private boolean evaluateCondition(String columnValue, String operator, String inputValue, Column column) {
        ColumnType columnType = ColumnType.valueOf(column.getType());

        if(columnValue == null || columnValue.equals("")) return false;

        // Handle different data types
        switch (columnType) {
            case INT: {
                int columnIntValue = Integer.parseInt(columnValue);
                int inputIntValue = Integer.parseInt(inputValue);
                return compareIntegers(columnIntValue, inputIntValue, operator);
            }
            case REAL: {
                double columnDoubleValue = Double.parseDouble(columnValue);
                double inputDoubleValue = Double.parseDouble(inputValue);
                return compareDoubles(columnDoubleValue, inputDoubleValue, operator);
            }
            case STRING:
                return compareStrings(columnValue, inputValue, operator);
            case CHAR:
                return compareChars(columnValue, inputValue, operator);
            case MONEY: {
                double columnMoneyValue = MoneyColumn.toDouble(columnValue);
                double inputMoneyValue = MoneyColumn.toDouble(inputValue);
                return compareDoubles(columnMoneyValue, inputMoneyValue, operator);
            }
            case MONEY_INVL: {
                double columnMoneyValue = MoneyColumn.toDouble(columnValue);
                double inputMoneyValue = MoneyColumn.toDouble(inputValue);
                return compareDoubles(columnMoneyValue, inputMoneyValue, operator);
            }
            default:
                // Handle unknown data type or invalid operator
                return false;
        }
    }

    private boolean compareIntegers(int columnValue, int inputValue, String operator) {
        switch (operator) {
            case ">":
                return columnValue > inputValue;
            case "<":
                return columnValue < inputValue;
            case ">=":
                return columnValue >= inputValue;
            case "<=":
                return columnValue <= inputValue;
            case "==":
                return columnValue == inputValue;
            default:
                return false;
        }
    }

    private boolean compareDoubles(double columnValue, double inputValue, String operator) {
        switch (operator) {
            case ">":
                return columnValue > inputValue;
            case "<":
                return columnValue < inputValue;
            case ">=":
                return columnValue >= inputValue;
            case "<=":
                return columnValue <= inputValue;
            case "==":
                return Double.compare(columnValue, inputValue) == 0;
            default:
                // Handle an invalid operator
                return false;
        }
    }

    private boolean compareStrings(String columnValue, String inputValue, String operator) {
        switch (operator) {
            case "==":
                return columnValue.equals(inputValue);
            case "!=":
                return !columnValue.equals(inputValue);
            case ">":
                return columnValue.compareTo(inputValue) > 0;
            case "<":
                return columnValue.compareTo(inputValue) < 0;
            case ">=":
                return columnValue.compareTo(inputValue) >= 0;
            case "<=":
                return columnValue.compareTo(inputValue) <= 0;
            default:
                // Handle an invalid operator
                return false;
        }
    }
    private boolean compareChars(String columnValue, String inputValue, String operator) {
        if (columnValue.length() != 1 || inputValue.length() != 1) {
            // Handle invalid input (not single characters)
            return false;
        }

        char columnChar = columnValue.charAt(0);
        char inputChar = inputValue.charAt(0);

        switch (operator) {
            case "==":
                return columnChar == inputChar;
            case "!=":
                return columnChar != inputChar;
            case ">":
                return columnChar > inputChar;
            case "<":
                return columnChar < inputChar;
            case ">=":
                return columnChar >= inputChar;
            case "<=":
                return columnChar <= inputChar;
            default:
                // Handle an invalid operator
                return false;
        }
    }



    public Boolean deleteDuplicateRows(int tableIndex) {
        int i = 0;
        boolean flag = true;
        while(i<database.tables.get(tableIndex).rows.size()){
            flag = true;
            for (int j = i+1; j < database.tables.get(tableIndex).rows.size(); j++) {
                if (database.tables.get(tableIndex).rows.get(i).values.equals(database.tables.get(tableIndex).rows.get(j).values)) {
                    deleteRow(tableIndex, i);
                    flag = false;
                    break;
                }
            }
            if (flag){
                i++;
            }
        }
        return true;
    }
}
