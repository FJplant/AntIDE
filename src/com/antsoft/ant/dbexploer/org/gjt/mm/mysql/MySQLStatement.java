/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: MySQLStatement.java,v 1.2 1998/08/25 00:53:47 mmatthew Exp $
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
 */

package org.gjt.mm.mysql;

public class MySQLStatement extends org.gjt.mm.mysql.Statement
{

    public MySQLStatement(Connection C)
    {
       super(C);
    }
				    
    /**
     * Execute a SQL INSERT, UPDATE or DELETE statement.  In addition
     * SQL statements that return nothing such as SQL DDL statements
     * can be executed
     *
     * <p>
     * MySQL functionality in this method allows you to retrieve IDs 
     * generated for AUTO_INCREMENT by looking through the java.sql.SQLWarning 
     * chain of this statement for warnings of the form 
     * "LAST_INSERTED_ID = 'some number', COMMAND = 'your sql'".
     *
     * @param Sql a SQL statement
     * @return either a row count, or 0 for SQL commands
     * @exception java.sql.SQLException if a database access error occurs
     */

    public int executeUpdate(String Sql) throws java.sql.SQLException
    {
	if (Driver.debug)
	    System.out.println(this + " executing update '" + Sql + "'");
	
	if (escapeProcessing) {
	    Sql = Escaper.escapeSQL(Sql);
	}
	
	ResultSet RS = Conn.execSQL(Sql);
	
	if (RS.reallyResult()) {
	    throw new java.sql.SQLException("results returned");
	}
	else {
	    
	    // Add the last id inserted for AUTO_INCREMENT fields to the
	    // warning chain
	    
	    int updateID = RS.getUpdateID();
	    
	    if (updateID != -1) {
		java.sql.SQLWarning NewWarning = new java.sql.SQLWarning("LAST_INSERT_ID = " + updateID + ", COMMAND = " + Sql);
		
		if (Warnings == null) {
		    Warnings = NewWarning;
		}
		else {
		    Warnings.setNextWarning(NewWarning);
		}
	    }    
	    return RS.getUpdateCount();
	}
    }
}
