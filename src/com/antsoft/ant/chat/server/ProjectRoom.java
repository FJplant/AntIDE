package com.antsoft.ant.chat.server;

import java.util.Vector;
import com.antsoft.ant.chat.*;

/**
 * ProjectRoom Class
 *
 * @author kim sang kyun
 */
public class ProjectRoom {
  private String name;
  private String passwd;
  private boolean isPrivate;
  private Vector members = new Vector(10, 5);
  private Vector userInfos = new Vector(10, 5);
  private ChatServer server;
  private int limit = -1;

  public ProjectRoom(ChatServer server, String name, String passwd, int limit, Connection c){
    this.server = server;
    this.name = name;
    if(passwd != null) {
      isPrivate = true;
    }
    this.passwd = passwd;
    this.limit = limit;


    addMember(c);
  }

  public String getName(){
    return name;
  }

  public String getPasswd(){
    return passwd;
  }

  public boolean isPrivate(){
    return isPrivate;
  }

  public Vector getMembers(){
    return members;
  }

  public Vector getUserInfos(){
    return userInfos;
  }

  public int getLimit(){
    return limit;
  }            

  public boolean addMember(Connection newM){

    if(limit == -1 || members.size() < limit){
      members.addElement(newM);
      userInfos.addElement(newM.getUserInfo());
      return true;
    }
    else
      return false;
  }

  public User removeMember(String id){
    User ret = null;

    for(int i=0; i<members.size(); i++){
      Connection c = (Connection)members.elementAt(i);
      if(c.getUserInfo().getID().equals(id)) {
        members.removeElementAt(i);
        ret = c.getUserInfo();
        userInfos.removeElement(c.getUserInfo());
        break;
      }
    }
    return ret;
  }

  public void broadCast(Object data){
    int size = members.size();
    for(int i=0; i<size; i++){
      Connection c = (Connection)members.elementAt(i);
      c.put(data);
    }
  }

  public String toString(){
    return "[ "+ name + " ] " + "[ " + passwd + " ]" + " [ " + limit + " ]" + " [ " + members.size() + " ]";
  }

  public boolean equals(Object comp){
    ProjectRoom pr = (ProjectRoom)comp;
    if(name.equals(pr.name)) return true;
    else return false;
  }

}
