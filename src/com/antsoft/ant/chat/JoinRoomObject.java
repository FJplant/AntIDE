package com.antsoft.ant.chat;


public class JoinRoomObject extends GeneralObject{
  public User user;
  public String roomName;
  public JoinRoomObject(int protocol) {
    super(protocol);
  }
}
