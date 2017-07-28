package com.antsoft.ant.chat.server;

import java.net.*;
import java.io.*;
import java.util.*;

import com.antsoft.ant.chat.*;

/**
 * Chat Server class
 *
 * @author Kim sang-kyun
 */
class ChatServer implements Protocol{

  private static final int DEFAULT_PORT=5432;
  private ConnectionManager cm = null;
  private int portNum = 0;

  private Hashtable messageItems = new Hashtable(50, 0.9F);
  private Hashtable projectRooms = new Hashtable(20, 0.9F);

  private Hashtable users;

  public static final String USERS_FILEPATH = "c:\\temp\\userHash.ser";

  public ChatServer(){
    portNum = DEFAULT_PORT;
    users = loadUserHash();
  }

  /**
   * constructor
   *
   * @param port server port
   */
  public ChatServer(int port)
  {
    portNum = port;
    users = loadUserHash();

    for(Enumeration e = users.elements(); e.hasMoreElements(); ){
      System.out.println((User)e.nextElement());
    }
  }

  public void connect(){
    cm.start();
  }

  public void disConnect(){
    if(cm != null) {
      cm.disConnect();
    }
  }

  public boolean isConnected(){
    return cm.isConnected();
  }

  public void start(){
    cm = new ConnectionManager(portNum, this);
  	cm.start();
  }

  synchronized public void projectRoomDestroyed(ProjectRoom pr){
    if(projectRooms.remove(pr.getName()) != null){
      SerObject ret = new SerObject(Protocol.ROOMDESTORY);
      ret.datas = new String[1];
      ret.datas[0] = pr.getName();
      broadCast(ret);
    }
  }

  /**
   * treat client request
   *
   * @param message object
   * @param client client who have this request
   */
  synchronized public void treatClientRequest(GeneralObject obj, Connection client)
  {
    int protocol = obj.getProtocol();
    System.out.println("received : " + protocol);

    switch(protocol){
      case Protocol.REGISTER :
           treatRegister(obj, client);
           break;

      case Protocol.LOGIN :
           treatLogin(obj, client);
           break;

      case Protocol.MAKEROOM :
           try{
           treatMakeRoom(obj, client);
           }catch(Exception e){e.printStackTrace();}
           break;

      case Protocol.LEAVEROOM :
           treatLeaveRoom(obj, client);
           break;

      case Protocol.JOINROOM :
           try{
           treatJoinRoom(obj, client);
           }catch(Exception e){e.printStackTrace(); }
           break;

      case Protocol.CHAT :
           treatChat(obj, client);
           break;

      default :
         break;
    }
  }

  /**
   * id를 검사해서 결과를 돌려주고, connection을 끊어 버린다
   */
  synchronized public void treatRegister(GeneralObject obj, Connection c){
    RegisterObject rec = (RegisterObject)obj;

    String id = rec.user.getID();
    String passwd = rec.user.getPassword();

    if(users.get(id) != null) {
      SerObject ret = new SerObject(Protocol.REGISTER);
      ret.isError = true;
      ret.datas = new String[1];
      ret.datas[0] = "Already exist ID...";
      c.put(ret);
    }
    else {
      users.put(rec.user.getID(), rec.user);

      SerObject ret = new SerObject(Protocol.REGISTER);
      ret.datas = new String[1];
      ret.datas[0] = "You are registered as the " + users.size() + " 's user";
      c.put(ret);
      saveUserHash();
    }
  }

  synchronized public void treatLogin(GeneralObject obj, Connection c){

    SerObject rec = (SerObject)obj;
    String id = rec.datas[0];
    String passwd = rec.datas[1];

    User u = (User)users.get(id);

    if(u == null){
      SerObject ret = new SerObject(Protocol.LOGIN);
      ret.isError = true;
      ret.datas = new String[1];
      ret.datas[0] = "Your ID is not exist";
      c.put(ret);
    }
    else if(!u.getPassword().equals(passwd)){
      SerObject ret = new SerObject(Protocol.LOGIN);
      ret.isError = true;
      ret.datas = new String[1];
      ret.datas[0] = "Your PASSWORD is not valid";
      c.put(ret);
    }

    else if(cm.getClients().get(u.getID()) != null){
      SerObject ret = new SerObject(Protocol.LOGIN);    
      ret.isError = true;
      ret.datas = new String[1];
      ret.datas[0] = "Already Logined User";
      c.put(ret);
    }

    else {
      LoginObject ret = new LoginObject(Protocol.LOGIN);

      Vector prs = new Vector(10, 5);

      int size = projectRooms.size();
      for(Enumeration e = projectRooms.elements(); e.hasMoreElements(); ){
        ProjectRoom room = (ProjectRoom)e.nextElement();
        RoomInfo info = new RoomInfo(room.getName(), room.getPasswd(), room.getUserInfos(), room.getLimit());
        prs.addElement(info);
      }


      ret.user = u;
      ret.datas = prs;
      c.put(ret);

      c.setUserInfo(u);
      cm.addConnection(c);
    }
  }

  synchronized private void treatMakeRoom(GeneralObject obj, Connection c){
    MakeRoomObject rec = (MakeRoomObject)obj;
    c.addJoinedRoom(rec.roomInfo.name);

    RoomInfo rInfo = rec.roomInfo;
    ProjectRoom room = new ProjectRoom(this, rInfo.name, rInfo.passwd, rInfo.limit, c);
    projectRooms.put(rInfo.name, room);

    broadCast(rec);
  }

  /**
   * treate message
   */
  synchronized private void treatLeaveRoom(GeneralObject obj, Connection c){
    System.out.println("stat leave room ");
    SerObject rec = (SerObject)obj;

    String roomName = rec.datas[0];
    String id = rec.datas[1];

    c.removeJoinedRoom(roomName);

    User u = null;

    //project room데이타에서 사용자를 삭제한다
    ProjectRoom room = (ProjectRoom)projectRooms.get(roomName);
    u = room.removeMember(id);

    //삭제 했다고 모두에게 알린다
    LeaveRoomObject ret = new LeaveRoomObject(Protocol.LEAVEROOM);
    ret.roomName = roomName;
    ret.user = u;
    broadCast(ret);

    //만약 그 room에 사용자가 하나도 없다면 모두에게 room이 삭제 되었다고 알린다
    if(room.getMembers().size() == 0){
       projectRoomDestroyed(room);
    }
  }

  synchronized public void disConnected(Connection c){

    Vector joinrooms = c.getJoinedRooms();
    for(int i=0;i<joinrooms.size(); i++){

      String room = (String)joinrooms.elementAt(i);
      System.out.println("joined room : " + room);

      ProjectRoom pr = (ProjectRoom)projectRooms.get(room);
      User u = pr.removeMember(c.getUserInfo().getID());

      System.out.println("removed from room : " + u);      

      //삭제 했다고 모두에게 알린다
      LeaveRoomObject ret = new LeaveRoomObject(Protocol.LEAVEROOM);
      ret.roomName = pr.getName();
      ret.user = u;
      broadCast(ret);

      //만약 그 room에 사용자가 하나도 없다면 모두에게 room이 삭제 되었다고 알린다
      if(pr.getMembers().size() == 0){
         projectRoomDestroyed(pr);
      }
    }
  }

  synchronized public void treatJoinRoom(GeneralObject obj, Connection c){
    JoinRoomObject rec = (JoinRoomObject)obj;

    ProjectRoom room = (ProjectRoom)projectRooms.get(rec.roomName);
    c.addJoinedRoom(rec.roomName);
    room.addMember(c);

    broadCast(rec);
  }

  synchronized private void treatChat(GeneralObject obj, Connection c){
    ChatObject rec = (ChatObject)obj;
    String roomName = rec.room;
    String msg = rec.message;

    ProjectRoom room = (ProjectRoom)projectRooms.get(roomName);

    ChatObject ret = new ChatObject(Protocol.CHAT);
    ret.room = roomName;
    ret.message = msg;
    ret.fromID = rec.fromID;

    room.broadCast(ret);
  }

  /**
   * 현재 접속되어 있는 모든 Client들에게 보낸다
   */
  synchronized public void broadCast(Object data){

    if(cm != null){
      Hashtable clients = cm.getClients();
      if(clients != null)
      for(Enumeration e=clients.elements(); e.hasMoreElements(); ){
        Connection c = (Connection)e.nextElement();
        System.out.println("Put : " + c.put(data));
     }
    }
  }

  /**
   * load serialized user hashtable
   */
  private Hashtable loadUserHash(){
    Hashtable ret = null;

    File f = new File(USERS_FILEPATH);

    if(f.exists() && (f.length() > 0)){
      try{
        FileInputStream istream = new FileInputStream(f);
        ObjectInputStream p = new ObjectInputStream(istream);
        ret = (Hashtable)p.readObject();
        p.close();
   	    istream.close();
      }
      catch(Exception e){
        ret = new Hashtable();
        boolean success = f.delete();
      }
    }
    else {
      if(f.exists() && f.length() == 0)  f.delete();
      ret = new Hashtable();
    }
    return ret;
  }

  /**
   * save user hashtable
   */
  private void saveUserHash(){
    try{
      FileOutputStream ostream = new FileOutputStream(USERS_FILEPATH);
      ObjectOutputStream p = new ObjectOutputStream(ostream);
      p.writeObject(users);
      ostream.close();
      p.close();
    }
    catch(IOException e){}
    catch(Exception e1){}
  }

  public static void main(String [] args){
    int port;
    if(args.length > 0){
      try{
        port = Integer.parseInt(args[0]);
      }catch(Exception e){
        port = DEFAULT_PORT;
      }
    }
    else port = DEFAULT_PORT;

    ChatServer cs = new ChatServer(port);
    cs.start();
  }
}


