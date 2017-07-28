package com.antsoft.ant.chat;

import java.io.Serializable;

public class GeneralObject implements Serializable {
  public boolean isError = false;
  private int protocol;

  public GeneralObject(int protocol){
    this.protocol = protocol;
  }

  public int getProtocol(){
    return protocol;
  }
}
