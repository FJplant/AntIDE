/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: PreparedStatement.java,v 1.2 1998/08/25 00:53:47 mmatthew Exp $
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
 * A SQL Statement is pre-compiled and stored in a PreparedStatement object.
 * This object can then be used to efficiently execute this statement multiple
 * times.
 *
 * <p><B>Note:</B> The setXXX methods for setting IN parameter values must
 * specify types that are compatible with the defined SQL type of the input
 * parameter.  For instance, if the IN parameter has SQL type Integer, then
 * setInt should be used.
 *
 * <p>If arbitrary parameter type conversions are required, then the setObject 
 * method should be used with a target SQL type.
 *
 * @see ResultSet
 * @see java.sql.PreparedStatement
 */

package org.gjt.mm.mysql;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class PreparedStatement extends org.gjt.mm.mysql.Statement implements java.sql.PreparedStatement  
{

        String        Sql              = null;
        String[]      TemplateStrings  = null;
        String[]      ParameterStrings = null;
        InputStream[] ParameterStreams = null;
        boolean[]     IsStream         = null;
        Connection    Conn             = null;

        /**
         * Constructor for the PreparedStatement class.
         * Split the SQL statement into segments - separated by the arguments.
         * When we rebuild the thing with the arguments, we can substitute the
         * args and join the whole thing together.
         *
         * @param conn the instanatiating connection
         * @param sql the SQL statement with ? for IN markers
         * @exception java.sql.SQLException if something bad occurs
         */

        public PreparedStatement(Connection Conn, String Sql) throws java.sql.SQLException
        {
                super(Conn);

                Vector V = new Vector();
                boolean inQuotes = false;
                int lastParmEnd = 0, i;

                this.Sql = Sql;
                this.Conn = Conn;

                for (i = 0; i < Sql.length(); ++i)
                {
                        int c = Sql.charAt(i);

                        if (c == '\'')
                                inQuotes = !inQuotes;
                        if (c == '?' && !inQuotes)
                        {
                                V.addElement(Sql.substring (lastParmEnd, i));
                                lastParmEnd = i + 1;
                        }
                }
                V.addElement(Sql.substring (lastParmEnd, Sql.length()));

                TemplateStrings = new String[V.size()];
                ParameterStrings = new String[V.size() - 1];
                ParameterStreams = new InputStream[V.size() - 1];
                IsStream         = new boolean[V.size() - 1];
                clearParameters();

                for (i = 0 ; i < TemplateStrings.length; ++i) {
                        TemplateStrings[i] = (String)V.elementAt(i);
                }

                for (int j = 0; j < ParameterStrings.length; j++) {
                        IsStream[j] = false;
                }
        }

        /**
         * A Prepared SQL query is executed and its ResultSet is returned
         *
         * @return a ResultSet that contains the data produced by the
         *      query - never null
         * @exception java.sql.SQLException if a database access error occurs
         */
        public java.sql.ResultSet executeQuery() throws java.sql.SQLException
        {
                StringBuffer S = new StringBuffer();

                for (int i = 0 ; i < ParameterStrings.length ; ++i)
                {
                        if (ParameterStrings[i] == null && (IsStream[i] && ParameterStreams[i] == null))
                                throw new java.sql.SQLException("No value specified for parameter " + (i + 1));
                        S.append (TemplateStrings[i]);
                        if (IsStream[i]) {
                                S.append(streamToString(ParameterStreams[i]));
                        }
                        else {
                                S.append (ParameterStrings[i]);
                        }
                }
                S.append(TemplateStrings[ParameterStrings.length]);

                return super.executeQuery(S.toString());        // in Statement class
        }

        /**
         * Execute a SQL INSERT, UPDATE or DELETE statement.  In addition,
         * SQL statements that return nothing such as SQL DDL statements can
         * be executed.
         *
         * @return either the row count for INSERT, UPDATE or DELETE; or
         *      0 for SQL statements that return nothing.
         * @exception java.sql.SQLException if a database access error occurs
         */
        public int executeUpdate() throws java.sql.SQLException
        {
                
                StringBuffer S = new StringBuffer();

                for (int i = 0 ; i < ParameterStrings.length ; ++i)
                {
                        if (ParameterStrings[i] == null && (IsStream[i] && ParameterStreams[i] == null))
                                throw new java.sql.SQLException("No value specified for parameter " + (i + 1));
                        S.append (TemplateStrings[i]);
                        if (IsStream[i]) {
                                S.append(streamToString(ParameterStreams[i]));
                        }
                        else {
                                S.append (ParameterStrings[i]);
                        }
                }
                S.append(TemplateStrings[ParameterStrings.length]);
        
                return super.executeUpdate(S.toString());       // in Statement class
        }       

        /**
         * Set a parameter to SQL NULL
         *
         * <p><B>Note:</B> You must specify the parameters SQL type (although
         * PostgreSQL ignores it)
         *
         * @param parameterIndex the first parameter is 1, etc...
         * @param sqlType the SQL type code defined in java.sql.Types
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setNull(int parameterIndex, int sqlType) throws java.sql.SQLException
        {
                set(parameterIndex, "null");
        }

        /**
         * Set a parameter to a Java boolean value.  The driver converts this
         * to a SQL BIT value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setBoolean(int parameterIndex, boolean x) throws java.sql.SQLException
        {
                set(parameterIndex, x ? "'t'" : "'f'");
        }

        /**
         * Set a parameter to a Java byte value.  The driver converts this to
         * a SQL TINYINT value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setByte(int parameterIndex, byte x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Integer(x)).toString());
        }

        /**
         * Set a parameter to a Java short value.  The driver converts this
         * to a SQL SMALLINT value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setShort(int parameterIndex, short x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Integer(x)).toString());
        }

        /**
         * Set a parameter to a Java int value.  The driver converts this to
         * a SQL INTEGER value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setInt(int parameterIndex, int x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Integer(x)).toString());
        }

        /**
         * Set a parameter to a Java long value.  The driver converts this to
         * a SQL BIGINT value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setLong(int parameterIndex, long x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Long(x)).toString());
        }

        /**
         * Set a parameter to a Java float value.  The driver converts this
         * to a SQL FLOAT value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setFloat(int parameterIndex, float x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Float(x)).toString());
        }

        /**
         * Set a parameter to a Java double value.  The driver converts this
         * to a SQL DOUBLE value when it sends it to the database
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setDouble(int parameterIndex, double x) throws java.sql.SQLException
        {
                set(parameterIndex, (new Double(x)).toString());
        }

        /**
         * Set a parameter to a java.lang.BigDecimal value.  The driver
         * converts this to a SQL NUMERIC value when it sends it to the
         * database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setBigDecimal(int parameterIndex, BigDecimal X) throws java.sql.SQLException
        {
                set(parameterIndex, X.toString());
        }

        /**
         * Set a parameter to a Java String value.  The driver converts this
         * to a SQL VARCHAR or LONGVARCHAR value (depending on the arguments
         * size relative to the driver's limits on VARCHARs) when it sends it
         * to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setString(int parameterIndex, String X) throws java.sql.SQLException
        {
          // if the passed string is null, then set this column to null
          if(X == null)
            set(parameterIndex, "null");
          else {
            StringBuffer B = new StringBuffer();
            int i;
            
            B.append('\'');

            for (i = 0 ; i < X.length() ; ++i) {
                        char c = X.charAt(i);
                        if (c == '\\' || c == '\'') {
                                B.append((char)'\\');
                        }
                        B.append(c);
            }
            B.append('\'');
            set(parameterIndex, B.toString());
          }
        }

  /**
   * Set a parameter to a Java array of bytes.  The driver converts this
   * to a SQL VARBINARY or LONGVARBINARY (depending on the argument's
   * size relative to the driver's limits on VARBINARYs) when it sends
   * it to the database.
   *
   *
   * @param parameterIndex the first parameter is 1...
   * @param x the parameter value
   * @exception java.sql.SQLException if a database access error occurs
   */
  public void setBytes(int parameterIndex, byte x[]) throws java.sql.SQLException
  {
    // To Be Implemented
  }

        /**
         * Set a parameter to a java.sql.Date value.  The driver converts this
         * to a SQL DATE value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setDate(int parameterIndex, java.sql.Date X) throws java.sql.SQLException
        {
           SimpleDateFormat DF = new SimpleDateFormat("''yyyy-MM-dd''");
        
           set(parameterIndex, DF.format(X));
        }

        /**
         * Set a parameter to a java.sql.Time value.  The driver converts
         * this to a SQL TIME value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...));
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setTime(int parameterIndex, Time X) throws java.sql.SQLException
        {
                set(parameterIndex, "'" + X.toString() + "'");
        }

        /**
         * Set a parameter to a java.sql.Timestamp value.  The driver converts
         * this to a SQL TIMESTAMP value when it sends it to the database.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setTimestamp(int parameterIndex, Timestamp X) throws java.sql.SQLException
        {
                EscapeProcessor EP = new EscapeProcessor();
                String TimestampString = EP.escapeSQL("{ts '" + X.toString() + "'}");
                set(parameterIndex, "'" + TimestampString + "'");
        }

        /**
         * When a very large ASCII value is input to a LONGVARCHAR parameter,
         * it may be more practical to send it via a java.io.InputStream.
         * JDBC will read the data from the stream as needed, until it reaches
         * end-of-file.  The JDBC driver will do any necessary conversion from
         * ASCII to the database char format.
         *
         * <P><B>Note:</B> This stream object can either be a standard Java
         * stream object or your own subclass that implements the standard
         * interface.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @param length the number of bytes in the stream
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setAsciiStream(int parameterIndex, InputStream X, int length) throws java.sql.SQLException
        {
                setBinaryStream(parameterIndex, X, length);
        }

        /**
         * When a very large Unicode value is input to a LONGVARCHAR parameter,
         * it may be more practical to send it via a java.io.InputStream.
         * JDBC will read the data from the stream as needed, until it reaches
         * end-of-file.  The JDBC driver will do any necessary conversion from
         * UNICODE to the database char format.
         *
         * <P><B>Note:</B> This stream object can either be a standard Java
         * stream object or your own subclass that implements the standard
         * interface.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setUnicodeStream(int parameterIndex, InputStream X, int length) throws java.sql.SQLException
        {
                setBinaryStream(parameterIndex, X, length);
        }

        /**
         * When a very large binary value is input to a LONGVARBINARY parameter,
         * it may be more practical to send it via a java.io.InputStream.
         * JDBC will read the data from the stream as needed, until it reaches
         * end-of-file.  
         *
         * <P><B>Note:</B> This stream object can either be a standard Java
         * stream object or your own subclass that implements the standard
         * interface.
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the parameter value
         * @exception java.sql.SQLException if a database access error occurs
         */

        public void setBinaryStream(int parameterIndex, InputStream X, int length) throws java.sql.SQLException
        {
                if (parameterIndex < 1 || parameterIndex > TemplateStrings.length) {
                        throw new java.sql.SQLException("Parameter index out of range");
                }
                ParameterStreams[parameterIndex - 1] = X;
                IsStream[parameterIndex - 1] = true;
                
        }

        /**
         * In general, parameter values remain in force for repeated used of a
         * Statement.  Setting a parameter value automatically clears its
         * previous value.  However, in coms cases, it is useful to immediately
         * release the resources used by the current parameter values; this
         * can be done by calling clearParameters
         *
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void clearParameters() throws java.sql.SQLException
        {
                for (int i = 0 ; i < ParameterStrings.length ; i++) {
                        ParameterStrings[i] = null;
                        ParameterStreams[i] = null;
                        IsStream[i] = false;
                }

        }

        /**
         * Set the value of a parameter using an object; use the java.lang
         * equivalent objects for integral values.
         *
         * <P>The given Java object will be converted to the targetSqlType before
         * being sent to the database.
         *
         * <P>note that this method may be used to pass database-specific
         * abstract data types.  This is done by using a Driver-specific
         * Java type and using a targetSqlType of java.sql.Types.OTHER
         *
         * @param parameterIndex the first parameter is 1...
         * @param x the object containing the input parameter value
         * @param targetSqlType The SQL type to be send to the database
         * @param scale For java.sql.Types.DECIMAL or java.sql.Types.NUMERIC
         *      types this is the number of digits after the decimal.  For 
         *      all other types this value will be ignored.
         * @exception java.sql.SQLException if a database access error occurs
         */
        public void setObject(int parameterIndex, Object X, int targetSqlType, int scale) throws java.sql.SQLException
        {
                switch (targetSqlType)
                {
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.INTEGER:
                        case Types.BIGINT:
                        case Types.REAL:
                        case Types.FLOAT:
                        case Types.DOUBLE:
                        case Types.DECIMAL:
                        case Types.NUMERIC:
                                if (X instanceof Boolean)
                                        set(parameterIndex, ((Boolean)X).booleanValue() ? "1" : "0");
                                else
                                        set(parameterIndex, X.toString());
                                break;
                        case Types.CHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                                setString(parameterIndex, X.toString());
                                break;
                        case Types.DATE:
                                setDate(parameterIndex, (java.sql.Date)X);
                                break;
                        case Types.TIME:
                                setTime(parameterIndex, (Time)X);
                                break;
                        case Types.TIMESTAMP:
                                setTimestamp(parameterIndex, (Timestamp)X);
                                break;
                        default:
                                throw new java.sql.SQLException("Unknown Types value");
                }
        }

        public void setObject(int parameterIndex, Object X, int targetSqlType) throws java.sql.SQLException
        {
                setObject(parameterIndex, X, targetSqlType, 0);
        }

        public void setObject(int parameterIndex, Object X) throws java.sql.SQLException
        {
                if (X instanceof String)
                        setString(parameterIndex, (String)X);
                else if (X instanceof BigDecimal)
                        setBigDecimal(parameterIndex, (BigDecimal)X);
                else if (X instanceof Integer)
                        setInt(parameterIndex, ((Integer)X).intValue());
                else if (X instanceof Long)
                        setLong(parameterIndex, ((Long)X).longValue());
                else if (X instanceof Float)
                        setFloat(parameterIndex, ((Float)X).floatValue());
                else if (X instanceof Double)
                        setDouble(parameterIndex, ((Double)X).doubleValue());
                else if (X instanceof byte[])
                        setBytes(parameterIndex, (byte[])X);
                else if (X instanceof java.sql.Date)
                        setDate(parameterIndex, (java.sql.Date)X);
                else if (X instanceof Time)
                        setTime(parameterIndex, (Time)X);
                else if (X instanceof Timestamp)
                        setTimestamp(parameterIndex, (Timestamp)X);
                else if (X instanceof Boolean)
                        setBoolean(parameterIndex, ((Boolean)X).booleanValue());
                else
                        throw new java.sql.SQLException("Unknown object type");
        }

        /**
         * Some prepared statements return multiple results; the execute method
         * handles these complex statements as well as the simpler form of 
         * statements handled by executeQuery and executeUpdate
         *
         * @return true if the next result is a ResultSet; false if it is an
         *      update count or there are no more results
         * @exception java.sql.SQLException if a database access error occurs
         */
        public boolean execute() throws java.sql.SQLException
        {
                StringBuffer S = new StringBuffer();
                
                for (int i = 0 ; i < ParameterStrings.length ; ++i)
                {
                        if (ParameterStrings[i] == null && (IsStream[i] && ParameterStreams[i] == null))
                                throw new java.sql.SQLException("No value specified for parameter " + (i + 1));
                        S.append (TemplateStrings[i]);
                        if (IsStream[i]) {
                                S.append(streamToString(ParameterStreams[i]));
                        }
                        else {
                                S.append (ParameterStrings[i]);
                        }
                }
                S.append(TemplateStrings[ParameterStrings.length]);
                
                return super.execute(S.toString());     
        }
        
        /**
         * There are a lot of setXXX classes which all basically do
         * the same thing.  We need a method which actually does the
         * set for us.
         *
         * @param paramIndex the index into the inString
         * @param s a string to be stored
         * @exception java.sql.SQLException if something goes wrong
         */
        private void set(int paramIndex, String S) throws java.sql.SQLException
        {
                if (paramIndex < 1 || paramIndex > TemplateStrings.length) {
                        throw new java.sql.SQLException("Parameter index out of range");
                }
                ParameterStrings[paramIndex - 1] = S;
        }

        /**
         * For the setXXXStream() methods. Basically converts an
         * InputStream into a String. Not very efficient, but it
         * works. 
         */
         
        private String streamToString(InputStream In) throws java.sql.SQLException
        {
                ByteArrayOutputStream BytesOut = new ByteArrayOutputStream();

                int readStatus = 0;

                try {
                        while (readStatus != -1) {
                                readStatus = In.read();
                        
                                byte b = (byte)readStatus;
        
                                if (readStatus != -1) {
                                        /*
                                         * Escape evil characters
                                         */

                                        if (b == '\0') {
                                                BytesOut.write('\\');
                                                BytesOut.write('0');
                                        }
                                        else {
                                                if (b == '\\' || b == '\'' || b == '"') {
                                                        BytesOut.write('\\');
                                                }
                                                BytesOut.write(b);
                                        }
                                }
                        }
                }
                catch (Exception E) {
                        throw new java.sql.SQLException("Error reading from InputStream " + E.getMessage());
                }
                return "'" + BytesOut.toString() + "'";
        }




}


