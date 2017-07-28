/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: Util.java,v 1.1.1.1 1998/08/24 16:37:38 mmatthew Exp $
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

class Util
{
  // Right from Monty's code

  public static String newCrypt(String Passwd, String Seed)
  {
    byte b;
    double d;

    if (Passwd == null || Passwd.length() == 0)
      return Passwd;

    long[] pw = newHash(Seed);
    long[] msg = newHash(Passwd);
    
    long max = 0x3fffffffL;
    long seed1 = (pw[0] ^ msg[0]) % max;
    long seed2 = (pw[1] ^ msg[1]) % max;

    char[] chars = new char[Passwd.length()];
  
    for (int i = 0; i < Passwd.length(); i++)
    {
      seed1 = (seed1 * 3 + seed2) % max;
      seed2 = (seed1 + seed2 + 33) % max;
      d = (double)seed1 / (double)max;
      b = (byte)java.lang.Math.floor((d * 31) + 64);
      chars[i] = (char)b;
    }

    seed1 = (seed1 * 3 + seed2) % max;
    seed2 = (seed1 + seed2 + 33) % max;
    d = (double)seed1 / (double)max;
    b = (byte)java.lang.Math.floor(d * 31);

    for (int i = 0; i < Passwd.length(); i++)
      chars[i] ^= (char)b;

    return new String(chars);
  }

  public static String oldCrypt(String Passwd, String Seed)
  {
    long hp, hm, s1, s2;
    long max = 0x01FFFFFF;
    double d;
    byte b;

    if (Passwd == null || Passwd.length() == 0 )
      return Passwd;

    hp = oldHash(Seed);
    hm = oldHash(Passwd);
    long nr = hp ^ hm;
    nr %= max;
    s1 = nr;
    s2 = nr/2;
    
    char[] chars = new char[Passwd.length()];

    for (int i = 0; i< Passwd.length(); i++)
    {
      s1 = (s1 * 3 + s2) % max;
      s2 = (s1 + s2 + 33) % max;
      d = (double)s1 / max;
      b = (byte)java.lang.Math.floor((d * 31) + 64);
      chars[i] = (char)b;
    }
    return new String(chars);
  }

  public static long[] newHash(String P)
  {
    long nr=1345345333L;
    long add=7;
    long nr2=0x12345671L;
    long tmp;
    for (int i = 0; i < P.length(); ++i)
    {
      if (P.charAt(i) == ' ' || P.charAt(i) == '\t')
        continue;       // skip spaces
      tmp = (long)(0xff & P.charAt(i));
      nr ^= (((nr & 63) + add) * tmp) + (nr << 8);
      nr2 += (nr2 << 8) ^ nr;
      add += tmp;
    }
    long[] result = new long[2];
    result[0] = nr & 0x7fffffffL;
    result[1] = nr2 & 0x7fffffffL;
    return result;
  }

  public static long oldHash(String P)
  {
    long  nr=1345345333;
    long  nr2=7;
    long tmp;

    for (int i = 0; i < P.length(); i++)
    {
      if ((P.charAt(i) == ' ') || (P.charAt(i) == '\t'))
        continue;
      tmp = (long)P.charAt(i);
      nr ^= (((nr & 63) + nr2) * tmp) + (nr << 8);
      nr2 += tmp;
    }
    return nr & (((long) 1L << 31) -1L);
  }
}
