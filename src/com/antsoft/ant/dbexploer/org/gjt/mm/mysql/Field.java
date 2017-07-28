/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: Field.java,v 1.1.1.1 1998/08/24 16:37:38 mmatthew Exp $
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


/**
 * Field is a class used to describe fields in a
 * ResultSet
 */

package org.gjt.mm.mysql;

import java.sql.*;
import java.util.*;

class Field
{
  int length;          // Internal length of the field;
  String Name;         // The Field name
  String TableName;    // The Name of the Table
  int sql_type = -1;   // the java.sql.Type
  int mysql_type = -1; // the MySQL type
  short colFlag;
  int colDecimals;

  Field(String Table, String Name, int length, int mysql_type, 
               short col_flag, int col_decimals)
  {
    this.TableName = new String(Table);
    this.Name = new String(Name);
    this.length = length;
    colFlag = col_flag;
    colDecimals = col_decimals;
    this.mysql_type = mysql_type;

    // Map MySqlTypes to java.sql Types

    switch (mysql_type) {
    case MysqlDefs.FIELD_TYPE_DECIMAL     :   sql_type = Types.DECIMAL; break;
    case MysqlDefs.FIELD_TYPE_CHAR        :   sql_type = Types.CHAR; break;
    case MysqlDefs.FIELD_TYPE_SHORT       :   sql_type = Types.SMALLINT; break;
    case MysqlDefs.FIELD_TYPE_LONG        :   sql_type = Types.INTEGER; break;
    case MysqlDefs.FIELD_TYPE_FLOAT       :   sql_type = Types.FLOAT; break;
    case MysqlDefs.FIELD_TYPE_DOUBLE      :   sql_type = Types.DOUBLE; break;
    case MysqlDefs.FIELD_TYPE_NULL        :   sql_type = Types.NULL; break;
    case MysqlDefs.FIELD_TYPE_TIMESTAMP   :   sql_type = Types.TIMESTAMP; break;
    case MysqlDefs.FIELD_TYPE_LONGLONG    :   sql_type = Types.BIGINT; break;
    case MysqlDefs.FIELD_TYPE_INT24       :   sql_type = Types.INTEGER; break;
    case MysqlDefs.FIELD_TYPE_DATE        :   sql_type = Types.DATE; break;
    case MysqlDefs.FIELD_TYPE_TIME        :   sql_type = Types.TIME; break;
    case MysqlDefs.FIELD_TYPE_DATETIME    :   sql_type = Types.TIMESTAMP; break;
    case MysqlDefs.FIELD_TYPE_TINY_BLOB   :   sql_type = Types.VARBINARY; break;
    case MysqlDefs.FIELD_TYPE_MEDIUM_BLOB :   sql_type = Types.LONGVARBINARY; break;
    case MysqlDefs.FIELD_TYPE_LONG_BLOB   :   sql_type = Types.LONGVARBINARY; break;
    case MysqlDefs.FIELD_TYPE_BLOB        :   sql_type = Types.LONGVARBINARY; break;
    case MysqlDefs.FIELD_TYPE_VAR_STRING  :   sql_type = Types.VARCHAR; break;
    case MysqlDefs.FIELD_TYPE_STRING      :   sql_type = Types.VARCHAR; break;
    
    default:   sql_type = Types.VARCHAR;
    } 
  }
  
  /**
   * Constructor used by DatabaseMetaData methods.
   */
   
  public Field(String Table, String Name, int length, int jdbc_type)
  { 
    this.TableName = new String(Table);
    this.Name = new String(Name);
    this.length = length;
    sql_type = jdbc_type;
    colFlag = 0;
    colDecimals = 0;
  }
      
  String getTable() 
  {
    if (TableName != null)
      return new String(TableName);
    else
      return null;
  }
  
  String getName() 
  {
    if (Name != null)
      return new String(Name);
    else
      return null;
  }      
 
  String getFullName() 
  {
    String FullName = TableName + "." + "Name";
    return FullName;
  }

  String getTableName()
  {
    return TableName;
  }
  
  int getLength() 
  {
    return length;
  }
  
  int getSQLType()
  {
    return sql_type;
  }

  int getMysqlType()
  {
    return mysql_type;
  }

  int getDecimals() 
  {
    return colDecimals;
  }
  
  boolean isNotNull() 
  {
    if ((colFlag & 1) > 0) 
      return true;
    else 
      return false;
  }

  boolean isPrimaryKey() 
  {
    if ((colFlag & 2) > 0) 
      return true;
    else 
      return false;
  }

  boolean isUniqueKey() 
  {
    if ((colFlag & 4) > 0) 
      return true;
    else 
      return false;
  }
  
  boolean isMultipleKey() 
  {
    if ((colFlag & 8) > 0) return true;
    else return false;
  }

  boolean isBlob() 
  {
    if (( colFlag & 16) > 0) 
      return true;
    else 
      return false;
  }

  boolean isUnsigned() 
  {
    if ((colFlag & 32) > 0) 
      return true;
    else 
      return false;
  }

  boolean isZeroFill() 
  {
    if ((colFlag & 64) > 0) 
      return true;
    else 
      return false;
  }
  
  public boolean isPartKey() 
  {
    if ((colFlag & 128) > 0) 
      return true;
    else 
      return false;
  }
}
