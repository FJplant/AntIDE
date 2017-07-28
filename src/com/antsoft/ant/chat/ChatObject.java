package com.antsoft.ant.chat;

import java.awt.event.*;
import java.util.*;


/**
 * message item
 *
 * @author kim sang kyun
 */
public class ChatObject extends GeneralObject{

  public String fromID;
  public String message;
  public String room;

  public ChatObject(int protocol){
    super(protocol);
  }
}
