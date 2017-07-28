package com.antsoft.ant.chat;

public interface Protocol {
  // id passwd
  public static final int REGISTER = 1;

  // id passwd
  public static final int LOGIN = 2;

  // name passwd limit 
  public static final int MAKEROOM = 3;

  // roomname id
  public static final int LEAVEROOM = 4;

  // roomname
  public static final int ROOMDESTORY = 5;

  // roomname id
  public static final int JOINROOM = 6;

  public static final int CHAT = 7;
  public static final int SENDMESSAGE = 8;
}
