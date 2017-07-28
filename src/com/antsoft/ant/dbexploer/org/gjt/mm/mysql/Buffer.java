/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: Buffer.java,v 1.1.1.1 1998/08/24 16:37:38 mmatthew Exp $
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

class Buffer 
{
    byte[] buf;
    int length = 0;
    int pos = 0;
    int send_length = 0;
    
    Buffer(byte[] buf)
    {
      this.buf = buf;
    }

    Buffer(int size)
    {
      buf = new byte[size];
      pos = MysqlIO.HEADER_LENGTH;
    }

    void setBytes(byte[] buf)
    {
        send_length = buf.length;
        System.arraycopy(buf, 0, this.buf, 0, buf.length);
    }

    byte readByte()
    {
      return buf[pos++];
    }

    int readInt() 
    {
      return (int)(ub(buf[pos++])+(256*ub(buf[pos++])));
    }

    int readLongInt() 
    {
      return (int)(ub(buf[pos++])+(256*ub(buf[pos++]))+(256*256*ub(buf[pos++])));
    }

    long readLong() 
    {
      return (long)(ub(buf[pos++])+(256*ub(buf[pos++]))+(256*256*ub(buf[pos++]))+(256*256*256*ub(buf[pos++])));
    }

    // Read n bytes depending
    int readnBytes() {
      switch(ub(buf[pos++])) {
      case 1 : return ub(buf[pos++]);
      case 2 : return this.readInt();
      case 3 : return this.readLongInt();
      case 4 : return (int)this.readLong();
      default : return 255;
      }
    }
    
    long readLength() 
    {
      switch(ub(buf[pos])) {
      case 251 : {pos++; return (long) 0; }
      case 252 : {pos++; return (long) readInt();}
      case 253 : {pos++; return (long) readLongInt();}
      case 254 : {pos++; return (long) readLong();}
      default  : return (long) ub(buf[pos++]);
      }
    }

    // Read null-terminated string
    StringBuffer readString() 
    {
       StringBuffer B = new StringBuffer();
       int i = pos;
       while(buf[i] != 0 && pos < buf.length) 
         B.append((char)(buf[i++] & 0xff));
       pos= i + 1;
       return B;
    }

    // Read given-length string
    StringBuffer readLenString() 
    {
      long len = this.readLength();
      if (len==0) 
        return new StringBuffer("");
      StringBuffer B = new StringBuffer((int)len);

      for (int i=0; i<len; i++) {
        /* This breaks BLOBS. Let's
           see if taking it out breaks
           anything.
           
        if (buf[pos] == 0) 
          break;
        else 
        */
        B.append((char)(buf[pos++] & 0xff));
      }
      return B;
    }

    boolean isLastDataPacket() 
    {
      return ((buf.length == 1) && (ub(buf[0]) == 254));
    }

    void clear()
    {
      pos = MysqlIO.HEADER_LENGTH;
    }

    void writeByte(byte b) 
    {
      buf[pos++]=b;
    }

    void writeInt(int i) 
    {
      int r;
      r=i % 256;
      buf[pos++] = (byte)r;
      i = i / 256;
      buf[pos++] = (byte)i;
    }

    void writeLongInt(int i) 
    {
      int r;
      r=i % 256;
      buf[pos++] = (byte)r;
      i = i / 256;
      r = i % 256;
      buf[pos++] = (byte)r;
      i = i / 256;
      buf[pos++] = (byte)i;
    }

    void writeLong(long i) 
    {
      long r;
      r = i % 256;
      buf[pos++]=(byte)r;
      i = i / 256;
      r = i % 256;
      buf[pos++] = (byte)r;
      i = i / 256;
      r = i % 256;
      buf[pos++] = (byte)r;
      i = i / 256;
      buf[pos++] = (byte)i;
    }

    // Write null-terminated string
    void writeString(String S) 
    {
      int l = S.length();
      for(int i = 0; i < l; i++) 
        buf[pos++] = (byte)S.charAt(i);
      buf[pos++] = 0;
    }

    // Write string, with no termination
    void writeStringNoNull(String S) 
    {
      int l = S.length();
      for(int i = 0; i < l; i++) 
        buf[pos++] = (byte)S.charAt(i);
    }

    static int ub(byte b)
    {
      return b < 0 ? (int)(256 + b) : b;
    }
}
