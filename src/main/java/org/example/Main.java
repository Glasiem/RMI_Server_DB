package org.example;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.example.remote.RemoteDBImpl;

public class Main {

  public static final String UNIQUE_BINDING_NAME = "server.db";

  public static void main(String[] args)
      throws RemoteException, AlreadyBoundException, InterruptedException {

    DatabaseManager dbManager = DatabaseManager.getInstance();
    dbManager.createDB("DB");
    final RemoteDBImpl server = new RemoteDBImpl();
    final Registry registry = LocateRegistry.createRegistry(8080);
    Remote stub = UnicastRemoteObject.exportObject(server,0);
    registry.bind(UNIQUE_BINDING_NAME,stub);

    Thread.sleep(Integer.MAX_VALUE);
  }
}