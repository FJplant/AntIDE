package com.antsoft.ant.chat;

import java.util.Vector;
import java.io.Serializable;

public class RoomInfo implements Serializable {
  public String name="";
  public String passwd="";
  public boolean isPrivate = false;
  public Vector members = new Vector(10, 5);
  public int limit = -1;

  public RoomInfo(String name, String passwd, Vector members, int limit) {
    this.name = name;
    this.passwd = passwd;
    this.members = members;
    this.limit = limit;

    if(passwd.trim().length() != 0) isPrivate = true;
  }

  public String getAccessMode(){
    if(passwd.equals("")) return "[ PUBLIC ]";
    else return "[ PRIVATE ]";
  }

  public boolean contains(User u){
    for(int i=0; i<members.size(); i++){
      if(u.equals((User)members.elementAt(i))) return true;
    }
    return false;
  }

  public String toString(){
    if(limit != -1) return getAccessMode() + " " + name + "   [ "+ limit + " ]";
    else return getAccessMode() + " " + name;
  }

  public boolean equals(Object comp){
    if(comp == null) return false;

    RoomInfo info = (RoomInfo)comp;
    if(info.toString().equals(this.toString())) return true;
    else return false;
  }
} 