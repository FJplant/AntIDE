/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: MysqlIO.java,v 1.3 1998/08/25 00:53:47 mmatthew Exp $
 *
 * Copyright (C) 1998 Mark Matthews <mmatthew@worldserver.com>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 * See the COPYING file located in the top-level-directory of
 * the archive of this library for complete text of license.
 */

package org.gjt.mm.mysql;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

import java.sql.SQLException;

public class MysqlIO
{
  /**
   * This class is used by Connection for communicating with the
   * MySQL server.
   *
   * @see java.sql.Connection
   */

  private Socket Mysql_Conn = null;
  private DataInputStream Mysql_Input = null;
  private DataOutputStream Mysql_Output = null;
  private BufferedInputStream Mysql_Buf_Input = null;
  private BufferedOutputStream Mysql_Buf_Output = null;

  static int MAXBUF = 65535; 
  static final int HEADER_LENGTH = 4;

  private byte packetSequence = 0;
  private byte protocol_V;
  private String Server_V;

  // For SQL Warnings
  java.sql.SQLWarning Warning = null;
  
  /**
   * Constructor:  Connect to the MySQL server and setup
   * a stream connection.
   *
   * @param host the hostname to connect to
   * @param port the port number that the server is listening on
   * @exception IOException if an IOException occurs during connect.
   */

  MysqlIO(String host, int port) throws IOException, java.sql.SQLException
  {
    Mysql_Conn = new Socket(host, port);
    Mysql_Buf_Input  = new BufferedInputStream(Mysql_Conn.getInputStream());
    Mysql_Buf_Output = new BufferedOutputStream(Mysql_Conn.getOutputStream());

    Mysql_Input  = new DataInputStream(Mysql_Buf_Input);
    Mysql_Output = new DataOutputStream(Mysql_Buf_Output);
    
  }

  void init(String User, String Password) throws java.sql.SQLException
  {
    String Seed;
 
    try {
      // Read the first packet
      Buffer Buf = readPacket();

      // Get the protocol version
      protocol_V = Buf.readByte();
      Server_V = Buf.readString().toString();
      long threadId = Buf.readLong();
      Seed = Buf.readString().toString();
   
      if (Driver.debug) {
        System.out.println("Protocol Version: " + (int)protocol_V);
        System.out.println("Server Version: " + Server_V);
        System.out.println("Thread ID: " + threadId);
        System.out.println("Crypt Seed: " + Seed);
      }
    
      // Authenticate
      int clientParam = 0;
      if (protocol_V > 9)
        clientParam = 1; // for long passwords

      int password_length = 16;
      int user_length = 0;
      
      if (User != null) {
        user_length = User.length();
      }
      
      int pack_length = (user_length + password_length) + 2 + 4 + HEADER_LENGTH; // Passwords can be 16 chars long
     
      Buffer Packet = new Buffer(pack_length);
      Packet.writeInt(clientParam);
      Packet.writeLongInt(pack_length);

      // User/Password data
      Packet.writeString(User);
      if (protocol_V > 9) 
        Packet.writeString(Util.newCrypt(Password, Seed));
      else
        Packet.writeString(Util.oldCrypt(Password, Seed));
      send(Packet);

      // Check for errors
      Buffer B = readPacket();
      byte status = B.readByte();
      if (status == (byte) 0xff) {
        String Message = "";
        int errno = 2000;

        if (protocol_V > 9) {
          errno = B.readInt();
          Message = B.readString().toString();
          clearReceive();
          String XOpen = SQLError.mysqlToXOpen(errno);
          throw new java.sql.SQLException(SQLError.get(XOpen) + ": " + Message, 
                                 XOpen, errno);
        }
        else {
          Message = B.readString().toString();
          clearReceive();
          if (Message.indexOf("Access denied") != -1) {
            throw new java.sql.SQLException(SQLError.get("28000") + ": " + Message, 
                                   "28000", errno);
          }
          else {
            throw new java.sql.SQLException(SQLError.get("08001") + ": " + Message,
                                   "08001", errno);
          }
        }
      }
      else if (status == 0x00) {
        long affectedRows = B.readLength();
        long uniqueId = B.readLength();
      }
      else {
        throw new java.sql.SQLException("Unknown Status code from server: '" + status +"'");
      }
    }
    catch (IOException E) {
      throw new java.sql.SQLException(SQLError.get("08S01") + ": " + E.getMessage(), "08S01", 0);
    }
  }

  String getServerVersion()
  {
    return Server_V;
  }
    
  Buffer sendCommand(int command, String ExtraData) throws java.sql.SQLException
  {
    Buffer Ret = null;

    try {
      int pack_length = HEADER_LENGTH + 1 + ExtraData.length() + 2;
      Buffer Packet = new Buffer(pack_length);
      packetSequence = -1;
      Packet.clear();
      Packet.writeByte((byte)command);
      if (command == MysqlDefs.INIT_DB || command == MysqlDefs.CREATE_DB ||
          command == MysqlDefs.DROP_DB || command == MysqlDefs.QUERY)
        Packet.writeStringNoNull(ExtraData);
      else if (command == MysqlDefs.PROCESS_KILL) {
        long id = new Long(ExtraData).longValue();
        Packet.writeLong(id);
      }
      else if (command == MysqlDefs.RELOAD && protocol_V > 9) {
        //System.out.println("Reload");
        // Packet.writeByte(reloadParam);
      }
      send(Packet);
    
    // Check return value
      Ret = readPacket();
      byte statusCode = Ret.readByte();

    // Error handling
      if (statusCode == (byte)0xff) {
        String ErrorMessage;
        int errno = 2000;

        if (protocol_V > 9) {
          errno = Ret.readInt();
          ErrorMessage = Ret.readString().toString();
          clearReceive();
          String XOpen = SQLError.mysqlToXOpen(errno);
          throw new java.sql.SQLException(SQLError.get(XOpen) + ": " + ErrorMessage, 
                                 XOpen, errno);
        }
        else {
          ErrorMessage = Ret.readString().toString();
          clearReceive();

          if (ErrorMessage.indexOf("Unknown column") != -1) {
            throw new java.sql.SQLException(SQLError.get("S0022") + ": " + ErrorMessage,
                                   "S0022", -1);
          }
          else {
            throw new java.sql.SQLException(SQLError.get("S1000") + ": " + ErrorMessage,
                                   "S1000", -1);
          }
        }
      }
      else if (statusCode == 0x00) {
        if (command == MysqlDefs.CREATE_DB || command == MysqlDefs.DROP_DB) {
          java.sql.SQLWarning NW = new java.sql.SQLWarning("Command=" + command + ": ");
          if (Warning != null)
            NW.setNextException(Warning);
          Warning = NW;
        }
      }
      else if (Ret.isLastDataPacket()) {
        java.sql.SQLWarning NW = new java.sql.SQLWarning("Command=" + command + ": ");
        if (Warning != null)
          NW.setNextException(Warning);
        Warning = NW;
      }
      return Ret;
    }
    catch (IOException E) {
      throw new java.sql.SQLException(SQLError.get("08S01") + ": " + E.getMessage(), "08S01", 0);
    }
  }

  
  ResultSet sqlQuery(String Query) throws Exception
  {
    int updateCount = -1;
    int updateID = -1;
  
    // Send query command and sql query string
    clearAllReceive();
    Buffer Packet = sendCommand(MysqlDefs.QUERY, Query); //, (byte)0);
    Packet.pos--;

    long columnCount = Packet.readLength();
    if (Driver.debug)
      System.out.println("Column count: " + columnCount);
    
    if (columnCount == 0) {
      try {
        updateCount = (int)Packet.readLength();
        updateID = (int)Packet.readLength();
      }
      catch (Exception E) {
        throw new java.sql.SQLException(SQLError.get("S1000") + ": " + E.getMessage(),
                               "S1000", -1);
      }
      
      if (Driver.debug)
        System.out.println("Update Count = " + updateCount);

      return new ResultSet(updateCount, updateID);
    }
    else {
      Field[] Fields = new Field[(int)columnCount];
      // Read in the column information
      for (int i = 0; i < columnCount; i++) {
        Packet = readPacket();
        String TableName = Packet.readLenString().toString();
        String ColName   = Packet.readLenString().toString();
        int colLength    = Packet.readnBytes();
        int colType      = Packet.readnBytes();
        Packet.readByte(); // We know it's currently 2
        short colFlag    = (short)Buffer.ub(Packet.readByte());
        int colDecimals  = Buffer.ub(Packet.readByte());
        
        Fields[i] = new Field(TableName, ColName, colLength, colType, colFlag, 
                              colDecimals);
      }
      Packet = readPacket();
    
      Vector Rows = new Vector();
      // Now read the data
      StringBuffer[] Row = nextRow((int)columnCount);

     
      Rows.addElement(Row);
      while (Row != null) {
        Row = nextRow((int)columnCount);
        if (Row != null)
          Rows.addElement(Row);
        else {
                if (Driver.debug) {
                        System.out.println("* NULL Row *");
                }
        }
      }
      if (Driver.debug) {
          System.out.println("* Fetched " + Rows.size() + " rows from server *");
      }
      return new ResultSet(Fields, Rows);
    }
  }
    
  StringBuffer[] nextRow(int columnCount) throws Exception
  {
    // Get the next incoming packet
    Buffer Packet = readPacket();
    
    // check for errors.
    
    if (Packet.readByte() == (byte)0xff) {
      String ErrorMessage;
      int errno = 2000;
      
      if (protocol_V > 9) {
        errno = Packet.readInt();
        ErrorMessage = Packet.readString().toString();
        String XOpen = SQLError.mysqlToXOpen(errno);
        clearReceive();
        throw new java.sql.SQLException(SQLError.get(SQLError.get(XOpen)) + ": " 
                               + ErrorMessage, XOpen, errno);
      }
      else {
        ErrorMessage = Packet.readString().toString();
        clearReceive();
        throw new java.sql.SQLException(ErrorMessage, "", errno);
      }
    }
    
    // Away we go....
    
    Packet.pos--;
    
    int[] dataStart = new int[columnCount];
    StringBuffer[] Row = new StringBuffer[columnCount];

    if (!Packet.isLastDataPacket()) {
      try {
        for (int i = 0; i < columnCount; i++) {
          int p = Packet.pos;
          dataStart[i] = p;
          Packet.pos = (int)Packet.readLength() + Packet.pos;
        }
        for (int i = 0; i < columnCount; i++) {
          Packet.pos = dataStart[i];
          Row[i] = Packet.readLenString();

          if (Driver.debug) {
                System.out.println("Field value: " + Row[i].toString());
          }
        }
        return Row;
      }
      catch (ArrayIndexOutOfBoundsException E) {
        // Horrible hack
        if (Driver.debug) {
                System.out.println("* NULL Packet *");
        }
        return null;
      }
    }
    return null;
  }
 

  void quit() throws IOException
  {
    Buffer Packet = new Buffer(6);
    packetSequence = -1;
    Packet.writeByte((byte)MysqlDefs.QUIT);
    send(Packet);
  }
     
  private Buffer readPacket() throws IOException
  {
    byte b0, b1, b2;

    b0 = Mysql_Input.readByte();
    b1 = Mysql_Input.readByte();
    b2 = Mysql_Input.readByte();
    
    int packetLength = (int)(Buffer.ub(b0) + (256*Buffer.ub(b1)) + (256*256*Buffer.ub(b2)));
    byte packetSeq = Mysql_Input.readByte();

    // Read data
    byte[] buffer = new byte[packetLength + 1];
    Mysql_Input.readFully(buffer, 0, packetLength);
    buffer[packetLength] = 0;
    return new Buffer(buffer);
  }

  private void send(Buffer Packet) throws IOException
  {
    int l = Packet.pos;
    packetSequence++;
    Packet.pos = 0;
    Packet.writeLongInt(l - HEADER_LENGTH);
    Packet.writeByte(packetSequence);
    Mysql_Output.write(Packet.buf, 0, l);
    Mysql_Output.flush();
    Mysql_Buf_Output.flush();
  }

  private void clearReceive() throws IOException
  {
      int len = Mysql_Input.available();
      if (len > 0) {
        Mysql_Input.skipBytes(len);
      }  
  }

  void clearAllReceive() // throws java.sql.SQLException
  {
    try
    {
      int len = Mysql_Input.available();
      if (len > 0)
      {
        Buffer Packet = readPacket();
        if (Packet.buf[0] == (byte)0xff)
        {
          clearReceive();
          return;
        }
        while (!Packet.isLastDataPacket()) {            
          // larger than the socket buffer.
          Packet = readPacket();
          if (Packet.buf[0] == (byte)0xff)
            break;
        }
      }
      clearReceive();
    }
    catch (IOException E) {
      System.out.println(E.getMessage());
    }
  }
  
  static int getMaxBuf()
  {
        return MAXBUF;
  }
}
