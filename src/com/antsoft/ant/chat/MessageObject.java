package com.antsoft.ant.chat;

/**
 * message item
 *
 * @author kim sang kyun
 */
public class MessageObject extends GeneralObject{

  public String fromID;
  public String toID;
  public String message;
  public String date;

  public MessageObject(int protocol){
    super(protocol);
  }
}
