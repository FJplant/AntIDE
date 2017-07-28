/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: MysqlDefs.java,v 1.1.1.1 1998/08/24 16:37:38 mmatthew Exp $
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

final class MysqlDefs
{

  //
  // Constants defined from mysql
  //

  // DB Operations

  static final int SLEEP        =  0;
  static final int QUIT         =  1;
  static final int INIT_DB      =  2;
  static final int QUERY        =  3;
  static final int FIELD_LIST   =  4;
  static final int CREATE_DB    =  5;
  static final int DROP_DB      =  6;
  static final int RELOAD       =  7;
  static final int SHUTDOWN     =  8;
  static final int STATISTICS   =  9;
  static final int PROCESS_INFO = 10;
  static final int CONNECT      = 11;
  static final int PROCESS_KILL = 12;

  // Data Types

  static final int FIELD_TYPE_DECIMAL     =   0;
  static final int FIELD_TYPE_CHAR        =   1;
  static final int FIELD_TYPE_SHORT       =   2;
  static final int FIELD_TYPE_LONG        =   3;
  static final int FIELD_TYPE_FLOAT       =   4;
  static final int FIELD_TYPE_DOUBLE      =   5;
  static final int FIELD_TYPE_NULL        =   6;
  static final int FIELD_TYPE_TIMESTAMP   =   7;
  static final int FIELD_TYPE_LONGLONG    =   8;
  static final int FIELD_TYPE_INT24       =   9;
  static final int FIELD_TYPE_DATE        =  10;
  static final int FIELD_TYPE_TIME        =  11;
  static final int FIELD_TYPE_DATETIME    =  12;
  static final int FIELD_TYPE_TINY_BLOB   = 249;
  static final int FIELD_TYPE_MEDIUM_BLOB = 250;
  static final int FIELD_TYPE_LONG_BLOB   = 251;
  static final int FIELD_TYPE_BLOB        = 252;
  static final int FIELD_TYPE_VAR_STRING  = 253;
  static final int FIELD_TYPE_STRING      = 254;

  // Limitations
  static final int MAX_ROWS = 50000000; // From the MySQL FAQ
}
