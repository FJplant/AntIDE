package com.antsoft.ant.manager.keymanager;

import java.net.*;

/**
 * Local network�� ���� Ű�� ����ϴ� ���α׷���
 * ������������ üũ�Ѵ�
 *
 * @author kim sang kyun
 */
public class KeyManager {
  private static String key;
  private static final String MULTICAST_ADDRESS = "228.1.2.3";
  private static final int PORT = 1234;
  private static String localIP;

  public static boolean start(){
    //network�� �����ϴ��� üũ
    if(!existOnNetwork()) return true;

    //Ű�� load�Ѵ�
    try{
      key = loadKey();
    }catch(Exception e){
      doKeyLoadErrorAction();
      return false;
    }

    //Comeng Computer�� ���ؼ� 
    if(localIP.startsWith("155.230.28") || localIP.startsWith("155.230.29")) return true;

    //Listener Thread�� �����Ѵ�
    KeyThread thread = new KeyThread(key, localIP, MULTICAST_ADDRESS, PORT);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

    //packet�� ������
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
   * network�� �����ϴ� computer���� üũ�Ѵ�
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
   * setup file�� ���� key data�� �о���δ�
   */
  private static String loadKey() throws Exception {
    return "12345 2";
  }

  private static void doKeyLoadErrorAction(){
  }

  private static void doMultiCastErrorAction(){
  }
}
