package com.example.rmi.remote;

import com.example.rmi.component.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.example.rmi.DatabaseManager;
import com.example.rmi.component.column.ColumnType;

public class RemoteDBImpl implements RemoteDB{

  @Override
  public List<Row> getRows(int tableIndex) throws RemoteException {
    return DatabaseManager.database.tables.get(tableIndex).rows;
  }

  @Override
  public List<Column> getColumns(int tableIndex) throws RemoteException {
    return DatabaseManager.database.tables.get(tableIndex).columns;
  }

  @Override
  public List<TableData> getTablesData() throws RemoteException {
    List<Table> tables = DatabaseManager.database.tables;
    List<TableData> names = new ArrayList<>();
    for (int i = 0; i < tables.size(); i++) {
      names.add(new TableData(tables.get(i).name,i));
    }
    return names;
  }

  @Override
  public Boolean createTable(String name) throws RemoteException {
    return DatabaseManager.getInstance().addTable(name);
  }

  @Override
  public Boolean addRow(int tableIndex) throws RemoteException {
    return DatabaseManager.getInstance().addRow(tableIndex,new Row());
  }

  @Override
  public Boolean addColumn(int tableIndex, String name, ColumnType columnType, String min, String max) throws RemoteException {
    if(columnType == ColumnType.MONEYINVL) {
      return DatabaseManager.getInstance().addColumn(tableIndex,name,columnType, min, max);
    }

    return DatabaseManager.getInstance().addColumn(tableIndex,name,columnType);
  }

  @Override
  public Boolean deleteTable(int tableIndex) throws RemoteException {
    return DatabaseManager.getInstance().deleteTable(tableIndex);
  }

  @Override
  public Boolean deleteColumn(int tableIndex, int columnIndex) throws RemoteException {
    return DatabaseManager.getInstance().deleteColumn(tableIndex,columnIndex);
  }

  @Override
  public Boolean deleteRow(int tableIndex, int rowIndex) throws RemoteException {
    return DatabaseManager.getInstance().deleteRow(tableIndex,rowIndex);
  }
  
  @Override
  public Boolean renameTable(int tableIndex, String name) throws RemoteException {
    return DatabaseManager.getInstance().renameTable(tableIndex,name);
  }

  @Override
  public Boolean renameColumn(int tableIndex, int columnIndex, String name) throws RemoteException {
    return DatabaseManager.getInstance().renameColumn(tableIndex,columnIndex,name);
  }

  @Override
  public Boolean changeColumnType(int tableIndex, int columnIndex, ColumnType columnType)
      throws RemoteException {
    return DatabaseManager.getInstance().changeColumnType(tableIndex,columnIndex,columnType);
  }

  @Override
  public Boolean editCell(int tableIndex, int rowIndex, int columnIndex, String value) throws RemoteException {
    return DatabaseManager.getInstance().updateCellValue(value, tableIndex, columnIndex, rowIndex);
  }

  @Override
  public void createTestTable() throws RemoteException {
    try {
      DatabaseManager dbManager = DatabaseManager.getInstance();
      dbManager.populateTable();
    }
    catch (Exception e){
      System.out.println(e);
    }
    System.out.println("Table created");
  }

  @Override
  public Boolean deleteDuplicateRows(int tableIndex) throws RemoteException {
    return DatabaseManager.getInstance().deleteDuplicateRows(tableIndex);
  }
}
