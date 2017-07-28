package com.antsoft.ant.chat;


public class LeaveRoomObject extends GeneralObject{
  public User user;
  public String roomName;
  public LeaveRoomObject(int protocol) {
    super(protocol);
  }
}
