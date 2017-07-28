package com.antsoft.ant.chat.client;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;

import com.antsoft.ant.chat.*;

import com.antsoft.ant.main.Main;

/**
 * Chat Client
 *
 * @author Kim sang-kyun
 */
public class ChatClient implements Runnable, Protocol{

  private static final int SERVER_PORT=5432;
  private Socket soc = null;
  private Thread reader = null;
  private boolean isConnected = false;
  private int port;
  private InetAddress host;
  private ObjectInputStream input;
  private ObjectOutputStream output;
  private ChatClientFrame parent;

  /**
   * constructor
   */
  public ChatClient(ChatClientFrame parent) {
    this.parent = parent;
  }

  /**
   * connect to server
   *
   * @param host host address
   * @param port port number
   */
  public void connect(InetAddress host, int port) throws Exception {

    this.port = port;
    this.host = host;

    try{
      soc = new Socket(host, port);
      input = new ObjectInputStream(soc.getInputStream());
	    output = new ObjectOutputStream(soc.getOutputStream());
      isConnected = true;

      reader = new Thread(this);
      reader.start();
    }catch(Exception e){
      isConnected = false;

      try{
        if(soc != null) soc.close();
      }
   	  catch(Exception e1){
        throw e1;
      }
    }
  }

  /**
   * disconnect socket connection
   */
  public void disconnect(){

    try{
      soc.close();
      isConnected = false;
    }catch(IOException e){}
    
  }

  /**
   * treat server response
   *
   * @param msg message string
   */
  synchronized public void treatServerResponse(GeneralObject obj){
    int protocol = obj.getProtocol();

    System.out.println("received : " + protocol);

    switch(protocol){
      case Protocol.REGISTER :
           parent.treatRegister(obj);
           break;

      case Protocol.LOGIN :
           parent.treatLogin(obj);
           break;

      case Protocol.MAKEROOM :
           try{
           parent.treatMakeRoom(obj);
           }catch(Exception e){e.printStackTrace();}
           break;

      case Protocol.LEAVEROOM :
           parent.treatLeaveRoom(obj);
           break;

      case Protocol.ROOMDESTORY :
           parent.treatRoomDestory(obj);
           break;

      case Protocol.JOINROOM :
           try{
           parent.treatJoinRoom(obj);
           }catch(Exception e){e.printStackTrace();}
           break;

      case Protocol.CHAT :
           parent.treatChat(obj);
           break;
           
      default :
    }
  }

  private void doClientWork(){
    while(isConnected){
      try
      {
         GeneralObject result = (GeneralObject)input.readObject();
         treatServerResponse(result);
      }
  	  catch(Exception e)
	    {
         if(isConnected) isConnected = false;
  	  }
    }
  }

  /**
   * socket¿¡ ¾´´Ù
   *
   * @param message message String
   */
  public void put(Object obj) throws Exception {
    try{
      if(obj != null) {
        output.writeObject(obj);
        output.flush();
      }
    }
    catch(IOException e){
      if(isConnected) isConnected = false;
      throw new Exception("Connection Failed...");
    }
  }

  public boolean isConnected(){
    return isConnected;
  }


  /**
   * run method
   */
  public void run(){

    doClientWork();

    try{
      input.close();
      output.close();
    }
    catch(IOException e){}
  }

  /*
  public static void main(String [] args){
    int port;
    if(args.length > 0){
      try{
        port = Integer.parseInt(args[0]);
      }catch(Exception e){
        port = SERVER_PORT;
      }
    }
    else port = SERVER_PORT;

    ChatClient cc = new ChatClient(this);

    try{
      cc.connect(InetAddress.getLocalHost(), port);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  */
}
