package com.antsoft.ant.chat.server;

import java.net.*;
import java.util.*;
import java.io.*;

import com.antsoft.ant.chat.User;

/**
 * Connection들을 관리하는 Class
 *
 * @author kim sang kyun
 */
class ConnectionManager extends Thread
{
  private static int port;
  private static Hashtable clients = new Hashtable(50, 0.9F);
  private ServerSocket serverSocket;
  private ChatServer server;
  private boolean isConnected = false;
  private int numOfClientLimit = 500;

  public static int clientNum = 0;
  private static int index = 0;

  private Vector tempConnections = new Vector(10, 5);

  public ConnectionManager(int port, ChatServer server)
  {
  	this.port = port;
    this.server = server;
    isConnected = true;
  }

  public void run()
  {
  	serveRequests();
    try{
      serverSocket.close();
    }catch(IOException e){
    }

    System.exit(-1);
  }

  public Hashtable getClients(){
    return clients;
  }

  synchronized public void addConnection(Connection c){
    clients.put(c.getUserInfo().getID(), c);
  }

  public void disConnect(){
    isConnected = false;

    for(Enumeration e = clients.elements(); e.hasMoreElements(); ){
      Connection conn = (Connection)e.nextElement();
      conn.disconnect();
      clients.remove(conn.getUserInfo().getID());
    }
  }

  public boolean isConnected(){
    return isConnected;
  }

  public void notifyDisconnection(Connection c){
    clients.remove(c.getUserInfo().getID());
    server.disConnected(c);
    System.out.println("removed " + c.getUserInfo().getID());

  }

  public void removeFromTemp(Connection c){
    tempConnections.removeElement(c);
  }

  private void serveRequests(){
  	try {
      serverSocket = new ServerSocket(port);
    }
  	catch(Exception e) {
      System.exit(1);
    }

  	Connection tempSC = null;
  	while (isConnected)
  	{
      try
  	  {
        System.out.println("Listening Client Connection...");
        Socket thisConnection = serverSocket.accept();
        Connection conn = new Connection(thisConnection, this, server);
        tempConnections.addElement(conn);
        conn.start();

        System.out.println("Current Client Number : " + clients.size());
      }catch(Exception e){
      }
    }
  }
}

