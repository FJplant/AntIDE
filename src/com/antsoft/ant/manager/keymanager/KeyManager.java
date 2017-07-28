package com.antsoft.ant.manager.keymanager;

import java.net.*;

/**
 * Local network상에 같은 키를 사용하는 프로그램이
 * 수행중인지를 체크한다
 *
 * @author kim sang kyun
 */
public class KeyManager {
  private static String key;
  private static final String MULTICAST_ADDRESS = "228.1.2.3";
  private static final int PORT = 1234;
  private static String localIP;

  public static boolean start(){
    //network상에 존재하는지 체크
    if(!existOnNetwork()) return true;

    //키를 load한다
    try{
      key = loadKey();
    }catch(Exception e){
      doKeyLoadErrorAction();
      return false;
    }

    //Comeng Computer를 위해서 
    if(localIP.startsWith("155.230.28") || localIP.startsWith("155.230.29")) return true;

    //Listener Thread를 시작한다
    KeyThread thread = new KeyThread(key, localIP, MULTICAST_ADDRESS, PORT);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

    //packet을 보낸다
    try{
      InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
      DatagramSocket socket = new DatagramSocket();
      String packetData = "key" + "&" + localIP + "&" + key.substring(0, key.indexOf(" ")).trim() + "&" + localIP;
      DatagramPacket packet = new DatagramPacket(packetData.getBytes(), packetData.length(), group, PORT);
      socket.send(packet);
      socket.close();
    }catch(Exception e){
      doMultiCastErrorAction();
      return false;
    }

    return true;
  }

  /**
   * network상에 존재하는 computer인지 체크한다
   */
  private static boolean existOnNetwork(){
    try{
      localIP = InetAddress.getLocalHost().getHostAddress();
    }
    catch(UnknownHostException e){
      return false;
    }
    catch(Exception e2){
      return false;
    }
    return true;
  }

  /**
   * setup file로 부터 key data를 읽어들인다
   */
  private static String loadKey() throws Exception {
    return "12345 2";
  }

  private static void doKeyLoadErrorAction(){
  }

  private static void doMultiCastErrorAction(){
  }
}
