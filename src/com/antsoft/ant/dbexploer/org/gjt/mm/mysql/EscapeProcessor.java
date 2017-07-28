/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: EscapeProcessor.java,v 1.2 1998/08/25 00:53:47 mmatthew Exp $
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

import java.util.StringTokenizer;

public class EscapeProcessor
{
    private boolean replaceEscapeSequence  = false;
    private String EscapeSequence = null;
    private StringBuffer NewSQL   = null;
        
    public String escapeSQL(String SQL) throws java.sql.SQLException
    {
	NewSQL = new StringBuffer();
	EscapeSequence = null;
	replaceEscapeSequence = false;
                
	boolean inBraces = false;
                
	StringTokenizer ST = new StringTokenizer(SQL, "{}", true);
                
	while (ST.hasMoreTokens()) {
	    String Token = ST.nextToken();
                        
	    if (Token.startsWith("{")) {
		inBraces = true;
	    }
	    else if (Token.startsWith("}")) {
		inBraces = false;
	    }
	    else {
		if (inBraces) {
		    NewSQL.append(processEscape(Token));
		}
		else {
		    NewSQL.append(Token);
		}
	    }
	}
	String EscapedSQL = NewSQL.toString();
                
	if (replaceEscapeSequence) {
	    String CurrentSQL = EscapedSQL;
	    while (CurrentSQL.indexOf(EscapeSequence) != -1) {
		int escapePos = CurrentSQL.indexOf(EscapeSequence);
		String LHS = CurrentSQL.substring(0, escapePos);
		String RHS = CurrentSQL.substring(escapePos + 1, CurrentSQL.length());
		CurrentSQL = LHS + "\\" + RHS;
	    }
	    EscapedSQL = CurrentSQL;      
	}
	return EscapedSQL;
    }
                
    public String processEscape(String EscapeString) throws java.sql.SQLException
    {
	if (EscapeString.startsWith("escape")) { 
	    try {
		StringTokenizer ST = new StringTokenizer(EscapeString, " '");
		ST.nextToken(); // eat the "escape" token
		EscapeSequence = ST.nextToken();
		if (EscapeSequence.length() < 3) {
		    throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000"); 
		}
		EscapeSequence = EscapeSequence.substring( 1, EscapeSequence.length() - 1);
                                
		replaceEscapeSequence = true;
	    }
	    catch (java.util.NoSuchElementException E) {
		throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000");
	    }
	    return "";                                                       
	}
	else if (EscapeString.startsWith("fn")) {
	    try {
                                // just pass functions right to the DB
		StringTokenizer ST = new StringTokenizer(EscapeString, "fn ");
		String Function = ST.nextToken();
		return Function;
	    }
	    catch (java.util.NoSuchElementException E) {
		throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000");
	    }
	}
	else if (EscapeString.startsWith("d")) {
	    try {
		StringTokenizer ST = new StringTokenizer(EscapeString, " '-");
		ST.nextToken(); // eat the d
		String YYYY = ST.nextToken();
		String MM   = ST.nextToken();
		String DD   = ST.nextToken();
                                
		String DateString = YYYY + "-" + MM + "-" + DD;
		return DateString;
	    }
	    catch (java.util.NoSuchElementException E) {
		throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000");
	    }
                        
	}
	else if (EscapeString.startsWith("ts")) {
   
	    try {
		StringTokenizer ST = new StringTokenizer(EscapeString, " '.-:");
		ST.nextToken(); // eat the TS
		String YYYY = ST.nextToken();                                
		String MM   = ST.nextToken();                                
		String DD   = ST.nextToken();                               
		String HH   = ST.nextToken();                                
		String Mm   = ST.nextToken();      
		String SS   = ST.nextToken();
                               
		String F = "";
		if (ST.hasMoreTokens()) {
		    F = ST.nextToken();
		}
                                
		String TimestampString = YYYY+MM+DD+HH+Mm+SS;
                                
		return TimestampString;
	    }
	    catch (java.util.NoSuchElementException E) {
		throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000");
	    }
	}
	else if (EscapeString.startsWith("t")) {
	    try {
		StringTokenizer ST = new StringTokenizer(EscapeString, " ':");
		ST.nextToken(); // eat the TS
                                                            
		String HH   = ST.nextToken();                                
		String MM   = ST.nextToken();      
		String SS   = ST.nextToken();
                                                                                              
		String TimeString = HH + ":" + MM + ":" +SS;
                                
		return TimeString;
	    }
	    catch (java.util.NoSuchElementException E) {
		throw new java.sql.SQLException("Syntax error for escape sequence '" + EscapeString + "'", "42000");
	    }
	}
	else if (EscapeString.startsWith("call") ||
		 EscapeString.startsWith("? = call")) {
	    throw new java.sql.SQLException("Stored procedures not supported: " + EscapeString, "S1C00");
	}
	else if (EscapeString.startsWith("oj")) {
	    // MySQL already handles this escape sequence
	    // because of ODBC. Cool.
	    return "{" + EscapeString + "}";
	}
	else {
	    throw new java.sql.SQLException("Syntax error for escape sequence: unknown escape sequence '" + EscapeString + "'", "42000");
	}   
    }
}
               
