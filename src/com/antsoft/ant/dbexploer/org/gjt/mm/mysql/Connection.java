/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: Connection.java,v 1.2 1998/08/25 00:53:46 mmatthew Exp $
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
 *
 * Some portions:
 *
 * Copyright (c) 1996 Bradley McLean / Jeffrey Medeiros
 * Modifications Copyright (c) 1996/1997 Martin Rode
 * Copyright (c) 1997 Peter T Mount
 */

/**
 * A Connection represents a session with a specific database.  Within the
 * context of a Connection, SQL statements are executed and results are
 * returned.
 *
 * <P>A Connection's database is able to provide information describing
 * its tables, its supported SQL grammar, its stored procedures, the
 * capabilities of this connection, etc.  This information is obtained
 * with the getMetaData method.
 *
 * <p><B>Note:</B> MySQL does not support transactions, so all queries
 *                 are committed as they are executed.
 *
 * @see java.sql.Connection
 */

package org.gjt.mm.mysql;

import java.sql.*;
import java.util.Properties;

public class Connection implements java.sql.Connection
{
  private MysqlIO IO;
  
  private String Host;
  private int port;
  private String User;
  private String Password;
  private String Database;

  private boolean autoCommit = true;
  private boolean readOnly = false;

  private org.gjt.mm.mysql.Driver MyDriver;
  private String MyURL;

  /**
   * Connect to a MySQL Server.
   *
   * <p><b>Important Notice</b>
   *
   * <br>Although this will connect to the database, user code should open
   * the connection via the DriverManager.getConnection() methods only.
   *
   * <br>This should only be called from the postgresql.Driver class.
   *
   * @param Host the hostname of the database server
   * @param port the port number the server is listening on
   * @param Info a Properties[] list holding the user and password
   * @param Database the database to connect to
   * @param Url the URL of the connection
   * @param D the Driver instantation of the connection
   * @return a valid connection profile
   * @exception java.sql.SQLException if a database access error occurs
   */

  public Connection(String Host, int port, Properties Info, String Database, 
                    String Url, Driver D) throws java.sql.SQLException
  {
    this.Host = new String(Host);
    this.port = port;
    this.Database = new String(Database);
    this.MyURL = new String(Url);
    this.MyDriver = D;

    String U = Info.getProperty("user");
    String P = Info.getProperty("password");

    if (U == null || U.equals(""))
      User = "nobody";
    else
      User = new String(U);

    if (P == null)
      Password = "";
    else
      Password = new String(P);

    if (Driver.debug)
      System.out.println("Connect: " + User + " to " + Database);
    try {
      IO = new MysqlIO(Host, port);
      IO.init(User, Password);
      IO.sendCommand(MysqlDefs.INIT_DB, Database);
    } 
    catch (java.sql.SQLException E) {
      throw E;
    }
    catch (Exception E) {
      throw new java.sql.SQLException("General Error: " + E.getMessage());
    }
  }
  
  /**
   * SQL statements without parameters are normally executed using
   * Statement objects.  If the same SQL statement is executed many
   * times, it is more efficient to use a PreparedStatement
   *
   * @return a new Statement object
   * @exception java.sql.SQLException passed through from the constructor
   */

  public java.sql.Statement createStatement() throws java.sql.SQLException
  {
    if (Driver.debug)
      System.out.println(this + " creating statement.");
    return new org.gjt.mm.mysql.Statement(this);
  }

  /**
   * A SQL statement with or without IN parameters can be pre-compiled
   * and stored in a PreparedStatement object.  This object can then
   * be used to efficiently execute this statement multiple times.
   *
   * <B>Note:</B> This method is optimized for handling parametric
   * SQL statements that benefit from precompilation if the driver
   * supports precompilation.  MySQL does not support precompilation.
   * In this case, the statement is not sent to the database until the
   * PreparedStatement is executed.  This has no direct effect on users;
   * however it does affect which method throws certain java.sql.SQLExceptions
   *
   * @param sql a SQL statement that may contain one or more '?' IN
   *    parameter placeholders
   * @return a new PreparedStatement object containing the pre-compiled
   *    statement.
   * @exception java.sql.SQLException if a database access error occurs.
   */

  public java.sql.PreparedStatement prepareStatement(String Sql) throws java.sql.SQLException
  {
    return new org.gjt.mm.mysql.PreparedStatement(this, Sql);

  }

  /**
   * A SQL stored procedure call statement is handled by creating a
   * CallableStatement for it.  The CallableStatement provides methods
   * for setting up its IN and OUT parameters and methods for executing
   * it.
   *
   * <B>Note:</B> This method is optimised for handling stored procedure
   * call statements.  Some drivers may send the call statement to the
   * database when the prepareCall is done; others may wait until the
   * CallableStatement is executed.  This has no direct effect on users;
   * however, it does affect which method throws certain java.sql.SQLExceptions
   *
   * @param sql a SQL statement that may contain one or more '?' parameter
   *    placeholders.  Typically this statement is a JDBC function call
   *    escape string.
   * @return a new CallableStatement object containing the pre-compiled
   *    SQL statement
   * @exception java.sql.SQLException if a database access error occurs
   */

  public java.sql.CallableStatement prepareCall(String sql) throws java.sql.SQLException
  {
    throw new java.sql.SQLException("Callable statments not suppoted.", "S1C00"); 
  }

  /**
   * A driver may convert the JDBC sql grammar into its system's
   * native SQL grammar prior to sending it; nativeSQL returns the
   * native form of the statement that the driver would have sent.
   *
   * @param sql a SQL statement that may contain one or more '?'
   *    parameter placeholders
   * @return the native form of this statement
   * @exception java.sql.SQLException if a database access error occurs
   */

  public String nativeSQL(String Sql) throws java.sql.SQLException
  {
    return Sql;
  }

  /**
   * If a connection is in auto-commit mode, than all its SQL
   * statements will be executed and committed as individual
   * transactions.  Otherwise, its SQL statements are grouped
   * into transactions that are terminated by either commit()
   * or rollback().  By default, new connections are in auto-
   * commit mode.  The commit occurs when the statement completes
   * or the next execute occurs, whichever comes first.  In the
   * case of statements returning a ResultSet, the statement
   * completes when the last row of the ResultSet has been retrieved
   * or the ResultSet has been closed.  In advanced cases, a single
   * statement may return multiple results as well as output parameter
   * values.  Here the commit occurs when all results and output param
   * values have been retrieved.
   *
   * <p><b>Note:</b> MySQL does not support transactions, so this
   *                 method is a no-op.
   *
   * @param autoCommit - true enables auto-commit; false disables it
   * @exception java.sql.SQLException if a database access error occurs
   */

  public void setAutoCommit(boolean autoCommit) throws java.sql.SQLException
  {
    return;
  }

  /**
   * gets the current auto-commit state
   *
   * @return Current state of the auto-commit mode
   * @exception java.sql.SQLException (why?)
   * @see setAutoCommit
   */

  public boolean getAutoCommit() throws java.sql.SQLException
  {
    return this.autoCommit;
  }

  /**
   * The method commit() makes all changes made since the previous
   * commit/rollback permanent and releases any database locks currently
   * held by the Connection.  This method should only be used when
   * auto-commit has been disabled.  (If autoCommit == true, then we
   * just return anyhow)
   * 
   * <p><b>Note:</b> MySQL does not support transactions, so this
   *                 method is a no-op.
   *
   * @exception java.sql.SQLException if a database access error occurs
   * @see setAutoCommit
   */

  public void commit() throws java.sql.SQLException
  {
    return;
  }

  /**
   * The method rollback() drops all changes made since the previous
   * commit/rollback and releases any database locks currently held by
   * the Connection.
   *
   * <p><b>Note:</b> MySQL does not support transactions, so this
   *                 method is a no-op.
   *
   * @exception java.sql.SQLException if a database access error occurs
   * @see commit
   */

  public void rollback() throws java.sql.SQLException
  {
    throw new java.sql.SQLException("Transactions not suppoted.", "S1C00");
  }
  
  /**
   * In some cases, it is desirable to immediately release a Connection's
   * database and JDBC resources instead of waiting for them to be
   * automatically released (cant think why off the top of my head)
   *
   * <B>Note:</B> A Connection is automatically closed when it is
   * garbage collected.  Certain fatal errors also result in a closed
   * connection.
   *
   * @exception java.sql.SQLException if a database access error occurs
   */

  public void close() throws java.sql.SQLException
  {
    if (IO != null)
      {
        try {
            IO.quit();
          } 
        catch (Exception e) {}
        IO = null;
      }
  }
  
  /**
   * Tests to see if a Connection is closed
   *
   * @return the status of the connection
   * @exception java.sql.SQLException (why?)
   */

  public boolean isClosed() throws java.sql.SQLException
  {
    return (IO == null);
  }

  /**
   * A connection's database is able to provide information describing
   * its tables, its supported SQL grammar, its stored procedures, the
   * capabilities of this connection, etc.  This information is made
   * available through a DatabaseMetaData object.
   *
   * @return a DatabaseMetaData object for this connection
   * @exception java.sql.SQLException if a database access error occurs
   */

  public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException
  {
    return new org.gjt.mm.mysql.DatabaseMetaData(this);
  }

  /**
   * You can put a connection in read-only mode as a hunt to enable
   * database optimizations
   *
   * <B>Note:</B> setReadOnly cannot be called while in the middle
   * of a transaction
   *
   * @param readOnly - true enables read-only mode; false disables it
   * @exception java.sql.SQLException if a database access error occurs
   */

  public void setReadOnly (boolean readOnly) throws java.sql.SQLException
  {
    this.readOnly = readOnly;
  } 

  /**
   * Tests to see if the connection is in Read Only Mode.  Note that
   * we cannot really put the database in read only mode, but we pretend
   * we can by returning the value of the readOnly flag
   *
   * @return true if the connection is read only
   * @exception java.sql.SQLException if a database access error occurs
   */

  public boolean isReadOnly() throws java.sql.SQLException
  {
    return readOnly;
  }

  /**
   * A sub-space of this Connection's database may be selected by
   * setting a catalog name.  If the driver does not support catalogs,
   * it will silently ignore this request
   *
   * <p><b>Note:</b> MySQL does not support Catalogs
   *
   * @exception java.sql.SQLException if a database access error occurs
   */

  public void setCatalog(String catalog) throws java.sql.SQLException
  {
    // No-op
  }
  
  /**
   * Return the connections current catalog name, or null if no
   * catalog name is set, or we dont support catalogs.
   *
   * <p><b>Note:</b> MySQL does not support Catalogs
   * @return the current catalog name or null
   * @exception java.sql.SQLException if a database access error occurs
   */

  public String getCatalog() throws java.sql.SQLException
  {
    return null;
  }

  /**
   * You can call this method to try to change the transaction
   * isolation level using one of the TRANSACTION_* values.
   *
   * <B>Note:</B> setTransactionIsolation cannot be called while
   * in the middle of a transaction
   *
   * @param level one of the TRANSACTION_* isolation values with
   *    the exception of TRANSACTION_NONE; some databases may
   *    not support other values
   * @exception java.sql.SQLException if a database access error occurs
   * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel
   */

  public void setTransactionIsolation(int level) throws java.sql.SQLException
  {
    throw new java.sql.SQLException("Transaction Isolation Levels are not implemented");
  }
  
  /**
   * Get this Connection's current transaction isolation mode.
   *
   * @return the current TRANSACTION_* mode value
   * @exception java.sql.SQLException if a database access error occurs
   */

  public int getTransactionIsolation() throws java.sql.SQLException
  {
    return java.sql.Connection.TRANSACTION_SERIALIZABLE;
  }
  
  /**
   * The first warning reported by calls on this Connection is
   * returned.
   *
   * <B>Note:</B> Sebsequent warnings will be changed to this
   * java.sql.SQLWarning
   *
   * @return the first java.sql.SQLWarning or null
   * @exception java.sql.SQLException if a database access error occurs
   */

  public java.sql.SQLWarning getWarnings() throws java.sql.SQLException
  {
    return null;
  }

  /**
   * After this call, getWarnings returns null until a new warning
   * is reported for this connection.
   *
   * @exception java.sql.SQLException if a database access error occurs
   */
  
  public void clearWarnings() throws java.sql.SQLException
  {
    // firstWarning = null;
  }

  // *********************************************************************
  //
  //                END OF PUBLIC INTERFACE
  //
  // *********************************************************************

  /**
   * Send a query to the server.  Returns one of the ResultSet
   * objects.
   *
   * This is synchronized, so Statement's queries
   * will be serialized.
   *
   * @param sql the SQL statement to be executed
   * @return a ResultSet holding the results
   * @exception java.sql.SQLException if a database error occurs
   */

  synchronized ResultSet execSQL(String Sql) throws java.sql.SQLException
  {
    try {
      return IO.sqlQuery(Sql);
    }
    catch (java.sql.SQLException SQE) {
      throw SQE;
    }
    catch (Exception E) {
      E.printStackTrace();
      throw new java.sql.SQLException("Error during query: " + E.getMessage());
    
    }
  }

  String getURL()
  {
    return MyURL;
  }

  String getUser()
  {
    return User;
  }

  String getServerVersion()
  {
    return IO.getServerVersion();
  }
}

    
