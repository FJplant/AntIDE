package com.antsoft.ant.manager.keymanager;

import java.net.*;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

import com.antsoft.ant.main.MainFrame;

/**
 * Listening Thread
 *
 * @author kim sang kyun
 */
public class KeyThread extends Thread{
  private MulticastSocket msocket;
  private InetAddress group;
  private String key;
  private String localIP;
  private int port;
  private int my_count= 0;
  private int limit;

  public KeyThread(String key, String localIP, String gaddress, int port) {
    super();

    try{
      this.key = key.substring(0, key.indexOf(" ")).trim();
      this.limit = Integer.parseInt( key.substring(key.indexOf(" ")).trim() );
      this.localIP = localIP;
      this.port = port;
      msocket = new MulticastSocket(port);
      group = InetAddress.getByName(gaddress);
      msocket.joinGroup(group);
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public void run(){
    while(true){

      try{
        byte [] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        msocket.receive(packet);

        String r_data = new String(packet.getData(), 0, packet.getLength());
        StringTokenizer st = new StringTokenizer(r_data, "&", false);

        //wrong packet
        if(st.countTokens() != 4) continue;

        String r_type = st.nextToken();
        String r_to_ip = st.nextToken();
        String r_key = st.nextToken();
        String r_from_ip = st.nextToken();

        //max packet인가?
        if(r_type.equals("max") && r_from_ip.equals(localIP) && r_to_ip.equals(localIP)){
          int max = Integer.parseInt(r_key);
          if(max - my_count >= limit){
            doCountLimitAction(r_from_ip);
            break;
          }
          else continue;
        }

        //decrease packet인가?
        else if(r_type.equals("decrease") && r_from_ip.equals(localIP) && r_to_ip.equals(localIP)){
          --my_count;
          continue;
        }

        //key packet인가?
        else if(r_type.equals("key")){

          if(localIP.equals(r_from_ip)) {
            ++my_count;
            try{
              String r_packet_data = "max" + "&" + localIP + "&" + my_count + "&" + localIP;
              DatagramPacket ret_packet = new DatagramPacket(r_packet_data.getBytes(), r_packet_data.length(), group, port);
              msocket.send(ret_packet);
            }catch(Exception e){
            }
            continue;
          }

          else if(key.equals(r_key)){
            if(localIP.equals(r_to_ip)){
              doKeyEqualAction(r_from_ip);
              break;
            }
            else{
              try{
                String r_packet_data = "key" + "&" + r_to_ip + "&" + r_key + "&" + localIP;
                DatagramPacket ret_packet = new DatagramPacket(r_packet_data.getBytes(), r_packet_data.length(), group, port);
                msocket.send(ret_packet);

              }catch(Exception e){
              }
            }
          }
        }
      }catch(IOException e){
        System.out.println(e);
      }
    }
  }

  private synchronized void doKeyEqualAction(String targetIP){
    try{
      InetAddress t_machine = InetAddress.getByName(targetIP);
      String hostName = t_machine.getHostName();
      String address = t_machine.getHostAddress();

      String msg = "다음의 컴퓨터에서 같은 키를 사용합니다\n" +
                   "조용히 죽겠습니까?\n" +
                   "HostName : " + hostName + ",  IP : " + address;
     	int answer = JOptionPane.showConfirmDialog(MainFrame.mainFrame, msg ,	"Key Collision ERROR", JOptionPane.YES_NO_OPTION);
      if (answer == JOptionPane.YES_OPTION) {
        MainFrame.mainFrame.dispose();
        System.exit(0);
      }
    }catch(UnknownHostException e){}
  }

  private synchronized void doCountLimitAction(String targetIP){

    try{
      String r_packet_data = "decrease" + "&" + localIP + "&" + "aaa" + "&" + localIP;
      DatagramPacket ret_packet = new DatagramPacket(r_packet_data.getBytes(), r_packet_data.length(), group, port);
      msocket.send(ret_packet);
    }catch(Exception e){
    }

    try{
      InetAddress t_machine = InetAddress.getByName(targetIP);
      String hostName = t_machine.getHostName();
      String address = t_machine.getHostAddress();

      String msg = "다음의 컴퓨터에서 허가받은 사용자수를 넘었습니다\n" +
                   "조용히 죽겠습니까?\n" +
                   "허가받은 수 : " + limit + "\n" +
                   "현재 사용자수 : " + limit + "\n" +
                   "HostName : " + hostName + ",  IP : " + address;

     	int answer = JOptionPane.showConfirmDialog(MainFrame.mainFrame, msg ,	"User Limit ERROR", JOptionPane.YES_NO_OPTION);
      if (answer == JOptionPane.YES_OPTION) {
        MainFrame.mainFrame.dispose();
        System.exit(0);
      }
    }catch(UnknownHostException e){}
  }
}