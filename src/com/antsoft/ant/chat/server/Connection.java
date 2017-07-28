package com.antsoft.ant.chat.server;

import java.io.*;
import java.net.*;
import java.util.Vector;
import com.antsoft.ant.chat.*;


class Connection extends Thread
{
  private Socket mySocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ChatServer server;
  private boolean isConnected = false;
  private boolean isStopped = false;
  private User userInfo;
  private ConnectionManager cm;
  private Vector joinedRooms = new Vector(5,2);

  public Connection(Socket s, ConnectionManager cm, ChatServer server)
  {
  	mySocket = s;
    this.cm = cm;
    this.server = server;
  }

  private void doServerWork()
  {

    while(isConnected){
      try
      {
        if(!isStopped){
          server.treatClientRequest((GeneralObject)in.readObject(), this);
        }
      }
   	  catch(IOException e)
      {
        if(isConnected){
          isConnected = false;
          cm.notifyDisconnection(this);
        }
   	  }
      catch(ClassNotFoundException e1)
      {
        if(isConnected){
          isConnected = false;
          cm.notifyDisconnection(this);
        }
      }
    }
  }

  /**
   * put result object to client
   *
   * @param result result object
   * @return success or fail tag
   */

  public boolean put(Object result)
  {
    try{
      out.writeObject(result);
      out.flush();

    }catch(IOException e){

       e.printStackTrace();
       return false;
    }
    return true;
  }

  public void run()
  {
  	try
  	{
      out = new ObjectOutputStream(mySocket.getOutputStream());
  	  in = new ObjectInputStream(mySocket.getInputStream());

      isConnected = true;

  	  doServerWork();
      mySocket.close();

	  }catch ( Exception e ){
  	}
  }

  /**
   * disconnect client connection
   */
  public void disconnect(){
    isConnected = false;
    try{
      mySocket.close();
    }catch(IOException e){}
  }

  public User getUserInfo(){
    return userInfo;
  }

  public void setUserInfo(User newUserInfo){
    this.userInfo = newUserInfo;
  }

  public void addJoinedRoom(String roomName){
    joinedRooms.addElement(roomName);
  }

  public void removeJoinedRoom(String roomName){
    joinedRooms.removeElement(roomName);
  }

  public Vector getJoinedRooms(){
    return joinedRooms;
  }
}
