/*
 * MM JDBC Drivers for MySQL
 *
 * $Id: DatabaseMetaData.java,v 1.2 1998/08/25 00:53:46 mmatthew Exp $
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
 * JDBC Interface to Mysql functions
 *
 * <p>
 * This class provides information about the database as a whole.
 * <p>
 * Many of the methods here return lists of information in ResultSets.
 * You can use the normal ResultSet methods such as getString and getInt
 * to retrieve the data from these ResultSets.  If a given form of
 * metadata is not available, these methods show throw a java.sql.SQLException.
 * <p>
 * Some of these methods take arguments that are String patterns.  These
 * methods all have names such as fooPattern.  Within a pattern String "%"
 * means match any substring of 0 or more characters and "_" means match
 * any one character.
 */

package org.gjt.mm.mysql;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.Vector;

public class DatabaseMetaData implements java.sql.DatabaseMetaData
{
  Connection Conn;
  
  public DatabaseMetaData(Connection Conn) 
  {
    this.Conn = Conn;
  }
   

  /**
   * Can all the procedures returned by getProcedures be called by the
   * current user?
   *
   * @return true if so
   */
  
  public boolean allProceduresAreCallable() throws java.sql.SQLException 
  {
    return false;  // not likely we will ever check
  }

  /**
   * Can all the tables returned by getTable be SELECTed by the
   * current user?
   *
   * @return true if so
   */

  public boolean allTablesAreSelectable() throws java.sql.SQLException 
  {
    return false;  // not likely we will ever check
  }

  /**
   * What's the url for this database?
   *
   * @return the url or null if it can't be generated
   */

  public String getURL() throws java.sql.SQLException 
  {
    return new String(Conn.getURL());
  }

  /**
   * What's our user name as known to the database?
   *
   * @return our database user name
   */

  public String getUserName() throws java.sql.SQLException 
  {
    return new String(Conn.getUser());
  }

  /**
   * Is the database in read-only mode?
   *
   * @return true if so
   */

  public boolean isReadOnly() throws java.sql.SQLException 
  {
    return false; // will this ever be true?
  }

  /**
   * Are NULL values sorted high?
   *
   * @return true if so
   */

  public boolean nullsAreSortedHigh() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Are NULL values sorted low?
   *
   * @return true if so
   */

  public boolean nullsAreSortedLow() throws java.sql.SQLException 
  {
    return !nullsAreSortedHigh(); // a guess!?
  }

  /**
   * Are NULL values sorted at the start regardless of sort order?
   *
   * @return true if so
   */

  public boolean nullsAreSortedAtStart() throws java.sql.SQLException 
  {
    return false; // maybe true?
  }

  /**
   * Are NULL values sorted at the end regardless of sort order?
   *
   * @return true if so
   */

  public boolean nullsAreSortedAtEnd() throws java.sql.SQLException 
  {
    return false;
  }
  
  /**
   * What's the name of this database product?
   *
   * @return database product name
   */

  public String getDatabaseProductName() throws java.sql.SQLException 
  {
    return "Mysql";
  }
  
  /**
   * What's the version of this database product?
   *
   * @return database version
   */

  public String getDatabaseProductVersion() throws java.sql.SQLException 
  {
    return new String(Conn.getServerVersion());
  }

  /**
   * What's the name of this JDBC driver?
   *
   * @return JDBC driver name
   */

  public String getDriverName() throws java.sql.SQLException 
  {
    return "Mark Matthews' MYSQL Driver";
  }

  /**
   * What's the version of this JDBC driver?
   *
   * @return JDBC driver version
   */

  public String getDriverVersion() throws java.sql.SQLException 
  {
    return Driver.MAJORVERSION + "." + Driver.MINORVERSION;
  }

  /**
   * What's this JDBC driver's major version number?
   *
   * @return JDBC driver major version
   */

  public int getDriverMajorVersion() 
  {
    return Driver.MAJORVERSION;
  }

  /**
   * What's this JDBC driver's minor version number?
   *
   * @return JDBC driver minor version number
   */

  public int getDriverMinorVersion() 
  {
    return Driver.MINORVERSION;
  }

  /**
   * Does the database store tables in a local file?
   *
   * @return true if so
   */
  
  public boolean usesLocalFiles() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database use a file for each table?
   *
   * @return true if the database uses a local file for each table
   */

  public boolean usesLocalFilePerTable() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database support mixed case unquoted SQL identifiers?
   *
   * @return true if so
   */

  public boolean supportsMixedCaseIdentifiers() throws java.sql.SQLException 
  {
    return true; // I think
  }

  /**
   * Does the database store mixed case unquoted SQL identifiers in
   * upper case?
   *
   * @return true if so
   */

  public boolean storesUpperCaseIdentifiers() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database store mixed case unquoted SQL identifiers in
   * lower case?
   *
   * @return true if so
   */

  public boolean storesLowerCaseIdentifiers() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database store mixed case unquoted SQL identifiers in
   * mixed case?
   *
   * @return true if so
   */

  public boolean storesMixedCaseIdentifiers() throws java.sql.SQLException 
  {
    return true; // I think
  }

  /**
   * Does the database support mixed case quoted SQL identifiers?
   *
   * A JDBC compliant driver will always return true.
   *
   * @return true if so
   */

  public boolean supportsMixedCaseQuotedIdentifiers() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Does the database store mixed case quoted SQL identifiers in
   * upper case?
   *
   * A JDBC compliant driver will always return true.
   *
   * @return true if so
   */

  public boolean storesUpperCaseQuotedIdentifiers() throws java.sql.SQLException 
  {
    return true; // don't know
  }

  /**
   * Does the database store mixed case quoted SQL identifiers in
   * lower case?
   *
   * A JDBC compliant driver will always return false.
   *
   * @return true if so
   */

  public boolean storesLowerCaseQuotedIdentifiers() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database store mixed case quoted SQL identifiers in
   * mixed case?
   *
   * A JDBC compliant driver will always return false.
   *
   * @return true if so
   */

  public boolean storesMixedCaseQuotedIdentifiers() throws java.sql.SQLException 
  {
    return false; // don't really know.
  }

  /**
   * What's the string used to quote SQL identifiers?
   * This returns a space " " if identifier quoting isn't supported.
   *
   * A JDBC compliant driver always uses a double quote character.
   *
   * @return the quoting string
   */

  public String getIdentifierQuoteString() throws java.sql.SQLException 
  {
    return "\"";
  }

  /**
   * Get a comma separated list of all a database's SQL keywords
   * that are NOT also SQL92 keywords.
   *
   * @return the list
   */

  public String getSQLKeywords() throws java.sql.SQLException 
  {
    return "";
  }

  /**
   * Get a comma separated list of math functions.
   *
   * @return the list
   */

  public String getNumericFunctions() throws java.sql.SQLException 
  {
    return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
  }

  /**
   * Get a comma separated list of string functions.
   *
   * @return the list
   */

  public String getStringFunctions() throws java.sql.SQLException 
  {
    return "ACII,CHAR,CHAR_LENGTH,CHARACTER_LENGTH,CONCAT,ELT,FIELD,FIND_IN_SET,INSERT,INSTR,INTERVAL,LCASE,LEFT,LENGTH,LOCATE,LOWER,LTRIM,MID,POSITION,OCTET_LENGTH,REPEAT,REPLACE,REVERSE,RIGHT,RTRIM,SPACE,SOUNDEX,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
  }

  /**
   * Get a comma separated list of system functions.
   *
   * @return the list
   */

  public String getSystemFunctions() throws java.sql.SQLException 
  {
    return "";
  }

  /**
   * Get a comma separated list of time and date functions.
   *
   * @return the list
   */
        
  public String getTimeDateFunctions() throws java.sql.SQLException {
    return "";
  }

  /**
   * This is the string that can be used to escape '_' or '%' in
   * the string pattern style catalog search parameters.
   *
   * <P>The '_' character represents any single character.
   * <P>The '%' character represents any sequence of zero or
   * more characters.
   * @return the string used to escape wildcard characters
   */

  public String getSearchStringEscape() throws java.sql.SQLException 
  {
    return "\\";
  }

  /**
   * Get all the "extra" characters that can be used in unquoted
   * identifier names (those beyond a-z, 0-9 and _).
   *
   * @return the string containing the extra characters
   */

  public String getExtraNameCharacters() throws java.sql.SQLException 
  {
    return "";
  }

  /**
   * Is "ALTER TABLE" with add column supported?
   *
   * @return true if so
   */

  public boolean supportsAlterTableWithAddColumn() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is "ALTER TABLE" with drop column supported?
   *
   * @return true if so
   */

  public boolean supportsAlterTableWithDropColumn() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is column aliasing supported?
   *
   * <P>If so, the SQL AS clause can be used to provide names for
   * computed columns or to provide alias names for columns as
   * required.
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsColumnAliasing() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Are concatenations between NULL and non-NULL values NULL?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean nullPlusNonNullIsNull() throws java.sql.SQLException 
  {
    return true;  // not sure
  }

  /**
   * Is the CONVERT function between SQL types supported?
   *
   * @return true if so
   */

  public boolean supportsConvert() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is CONVERT between the given SQL types supported?
   *
   * @param fromType the type to convert from
   * @param toType the type to convert to
   * @return true if so
   * @see Types
   */

  public boolean supportsConvert(int fromType, int toType) 
    throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Are table correlation names supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsTableCorrelationNames() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * If table correlation names are supported, are they restricted
   * to be different from the names of the tables?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsDifferentTableCorrelationNames() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Are expressions in "ORDER BY" lists supported?
   *
   * @return true if so
   */

  public boolean supportsExpressionsInOrderBy() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Can an "ORDER BY" clause use columns not in the SELECT?
   *
   * @return true if so
   */

  public boolean supportsOrderByUnrelated() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is some form of "GROUP BY" clause supported?
   *
   * @return true if so
   */

  public boolean supportsGroupBy() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Can a "GROUP BY" clause use columns not in the SELECT?
   *
   * @return true if so
   */
  
  public boolean supportsGroupByUnrelated() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Can a "GROUP BY" clause add columns not in the SELECT
   * provided it specifies all the columns in the SELECT?
   *
   * @return true if so
   */
        
  public boolean supportsGroupByBeyondSelect() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Is the escape character in "LIKE" clauses supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsLikeEscapeClause() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Are multiple ResultSets from a single execute supported?
   *
   * @return true if so
   */

  public boolean supportsMultipleResultSets() throws java.sql.SQLException 
  {
    return false; // not sure
  }
  
  /**
   * Can we have multiple transactions open at once (on different
   * connections)?
   *
   * @return true if so
   */

  public boolean supportsMultipleTransactions() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Can columns be defined as non-nullable?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsNonNullableColumns() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is the ODBC Minimum SQL grammar supported?
   *
   * All JDBC compliant drivers must return true.
   *
   * @return true if so
   */
        
  public boolean supportsMinimumSQLGrammar() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is the ODBC Core SQL grammar supported?
   *
   * @return true if so
   */

  public boolean supportsCoreSQLGrammar() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is the ODBC Extended SQL grammar supported?
   *
   * @return true if so
   */
        
  public boolean supportsExtendedSQLGrammar() throws java.sql.SQLException 
  {
    return false; // not sure at all
  }


  /**
   * Is the ANSI92 entry level SQL grammar supported?
   *
   * All JDBC compliant drivers must return true.
   *
   * @return true if so
   */

  public boolean supportsANSI92EntryLevelSQL() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * Is the ANSI92 intermediate SQL grammar supported?
   *
   * @return true if so
   */
        
  public boolean supportsANSI92IntermediateSQL() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is the ANSI92 full SQL grammar supported?
   *
   * @return true if so
   */
        
  public boolean supportsANSI92FullSQL() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is the SQL Integrity Enhancement Facility supported?
   *
   * @return true if so
   */

  public boolean supportsIntegrityEnhancementFacility() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is some form of outer join supported?
   *
   * @return true if so
   */

  public boolean supportsOuterJoins() throws java.sql.SQLException 
  {
    return false; // not sure
  }
  
  /**
   * Are full nested outer joins supported?
   *
   * @return true if so
   */

  public boolean supportsFullOuterJoins() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is there limited support for outer joins?  (This will be true
   * if supportFullOuterJoins is true.)
   *
   * @return true if so
   */

  public boolean supportsLimitedOuterJoins() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * What's the database vendor's preferred term for "schema"?
   *
   * @return the vendor term
   */

  public String getSchemaTerm() throws java.sql.SQLException 
  {
    return "schema";
  }

  /**
   * What's the database vendor's preferred term for "procedure"?
   *
   * @return the vendor term
   */

  public String getProcedureTerm() throws java.sql.SQLException 
  {
    return "procedure";
  }

  /**
   * What's the database vendor's preferred term for "catalog"?
   *
   * @return the vendor term
   */

  public String getCatalogTerm() throws java.sql.SQLException 
  {
    return "catalog";
  }

  /**
   * Does a catalog appear at the start of a qualified table name?
   * (Otherwise it appears at the end)
   *
   * @return true if it appears at the start
   */

  public boolean isCatalogAtStart() throws java.sql.SQLException 
  {
    return true; // doesn't appear,  don't think.
  }

  /**
   * What's the separator between catalog and table name?
   *
   * @return the separator string
   */

  public String getCatalogSeparator() throws java.sql.SQLException 
  {
    return ".";
  }

  /**
   * Can a schema name be used in a data manipulation statement?
   *
   * @return true if so
   */

  public boolean supportsSchemasInDataManipulation() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a schema name be used in a procedure call statement?
   *
   * @return true if so
   */

  public boolean supportsSchemasInProcedureCalls() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a schema name be used in a table definition statement?
   *
   * @return true if so
   */

  public boolean supportsSchemasInTableDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a schema name be used in an index definition statement?
   *
   * @return true if so
   */

  public boolean supportsSchemasInIndexDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a schema name be used in a privilege definition statement?
   *
   * @return true if so
   */

  public boolean supportsSchemasInPrivilegeDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a catalog name be used in a data manipulation statement?
   *
   * @return true if so
   */

  public boolean supportsCatalogsInDataManipulation() throws java.sql.SQLException 
  {
    return false;
  }
  
  /**
   * Can a catalog name be used in a procedure call statement?
   *
   * @return true if so
   */

  public boolean supportsCatalogsInProcedureCalls() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a catalog name be used in a table definition statement?
   *
   * @return true if so
   */

  public boolean supportsCatalogsInTableDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a catalog name be used in a index definition statement?
   *
   * @return true if so
   */

  public boolean supportsCatalogsInIndexDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can a catalog name be used in a privilege definition statement?
   *
   * @return true if so
   */

  public boolean supportsCatalogsInPrivilegeDefinitions() throws java.sql.SQLException 
  {
    return false;
  }

  
  /**
   * Is positioned DELETE supported?
   *
   * @return true if so
   */

  public boolean supportsPositionedDelete() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Is positioned UPDATE supported?
   *
   * @return true if so
   */

  public boolean supportsPositionedUpdate() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Is SELECT for UPDATE supported?
   *
   * @return true if so
   */

  public boolean supportsSelectForUpdate() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Are stored procedure calls using the stored procedure escape
   * syntax supported?
   *
   * @return true if so
   */
  
  public boolean supportsStoredProcedures() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Are subqueries in comparison expressions supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsSubqueriesInComparisons() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Are subqueries in exists expressions supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsSubqueriesInExists() throws java.sql.SQLException 
  {
    return true;  // not sure
  }

  /**
   * Are subqueries in "in" statements supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsSubqueriesInIns() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Are subqueries in quantified expressions supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsSubqueriesInQuantifieds() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Are correlated subqueries supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsCorrelatedSubqueries() throws java.sql.SQLException 
  {
    return true; // not sure
  }

  /**
   * Is SQL UNION supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsUnion() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Is SQL UNION ALL supported?
   *
   * A JDBC compliant driver always returns true.
   *
   * @return true if so
   */

  public boolean supportsUnionAll() throws java.sql.SQLException 
  {
    return false; // not sure
  }

  /**
   * Can cursors remain open across commits?
   *
   * @return true if so
   * @see Connection#disableAutoClose
   */

  public boolean supportsOpenCursorsAcrossCommit() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can cursors remain open across rollbacks?
   *
   * @return true if so
   * @see Connection#disableAutoClose
   */

  public boolean supportsOpenCursorsAcrossRollback() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can statements remain open across commits?
   *
   * @return true if so
   * @see Connection#disableAutoClose
   */
  public boolean supportsOpenStatementsAcrossCommit() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Can statements remain open across rollbacks?
   *
   * @return true if so
   * @see Connection#disableAutoClose
   */

  public boolean supportsOpenStatementsAcrossRollback() throws java.sql.SQLException 
  {
    return false;
  }

  //----------------------------------------------------------------------
  // The following group of methods exposes various limitations
  // based on the target database with the current driver.
  // Unless otherwise specified, a result of zero means there is no
  // limit, or the limit is not known.
  
  /**
   * How many hex characters can you have in an inline binary literal?
   *
   * @return max literal length
   */

  public int getMaxBinaryLiteralLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the max length for a character literal?
   *
   * @return max literal length
   */

  public int getMaxCharLiteralLength() throws java.sql.SQLException 
  {
    return 0;
  }
  
  /**
   * What's the limit on column name length?
   *
   * @return max literal length
   */

  public int getMaxColumnNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum number of columns in a "GROUP BY" clause?
   *
   * @return max number of columns
   */

  public int getMaxColumnsInGroupBy() throws java.sql.SQLException 
  {
    return 0;
  }
  
  /**
   * What's the maximum number of columns allowed in an index?
   *
   * @return max columns
   */

  public int getMaxColumnsInIndex() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum number of columns in an "ORDER BY" clause?
   *
   * @return max columns
   */

  public int getMaxColumnsInOrderBy() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum number of columns in a "SELECT" list?
   *
   * @return max columns
   */

  public int getMaxColumnsInSelect() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's maximum number of columns in a table?
   *
   * @return max columns
   */

  public int getMaxColumnsInTable() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * How many active connections can we have at a time to this database?
   *
   * @return max connections
   */

  public int getMaxConnections() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum cursor name length?
   *
   * @return max cursor name length in bytes
   */

  public int getMaxCursorNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of an index (in bytes)?
   *
   * @return max index length in bytes
   */

  public int getMaxIndexLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length allowed for a schema name?
   *
   * @return max name length in bytes
   */

  public int getMaxSchemaNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of a procedure name?
   *
   * @return max name length in bytes
   */

  public int getMaxProcedureNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of a catalog name?
   *
   * @return max name length in bytes
   */

  public int getMaxCatalogNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of a single row?
   *
   * @return max row size in bytes
   */

  public int getMaxRowSize() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * Did getMaxRowSize() include LONGVARCHAR and LONGVARBINARY
   * blobs?
   *
   * @return true if so
   */

  public boolean doesMaxRowSizeIncludeBlobs() throws java.sql.SQLException 
  {
    return true;
  }

  /**
   * What's the maximum length of a SQL statement?
   *
   * @return max length in bytes
   */

  public int getMaxStatementLength() throws java.sql.SQLException 
  {
    return MysqlIO.MAXBUF - 4; // Max buffer - header
  }

  /**
   * How many active statements can we have open at one time to this
   * database?
   *
   * @return the maximum
   */

  public int getMaxStatements() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of a table name?
   *
   * @return max name length in bytes
   */

  public int getMaxTableNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum number of tables in a SELECT?
   *
   * @return the maximum
   */

  public int getMaxTablesInSelect() throws java.sql.SQLException 
  {
    return 0;
  }

  /**
   * What's the maximum length of a user name?
   *
   * @return max name length  in bytes
   */

  public int getMaxUserNameLength() throws java.sql.SQLException 
  {
    return 0;
  }

  //----------------------------------------------------------------------

  /**
   * What's the database's default transaction isolation level?  The
   * values are defined in java.sql.Connection.
   *
   * @return the default isolation level
   * @see Connection
   */

  public int getDefaultTransactionIsolation() throws java.sql.SQLException 
  {
    return java.sql.Connection.TRANSACTION_NONE;
  }

  /**
   * Are transactions supported? If not, commit is a noop and the
   * isolation level is TRANSACTION_NONE.
   *
   * @return true if transactions are supported
   */

  public boolean supportsTransactions() throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does the database support the given transaction isolation level?
   *
   * @param level the values are defined in java.sql.Connection
   * @return true if so
   * @see Connection
   */

  public boolean supportsTransactionIsolationLevel(int level)
    throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Are both data definition and data manipulation statements
   * within a transaction supported?
   *
   * @return true if so
   */

  public boolean supportsDataDefinitionAndDataManipulationTransactions()
    throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Are only data manipulation statements within a transaction
   * supported?
   *
   * @return true if so
   */

  public boolean supportsDataManipulationTransactionsOnly()
    throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Does a data definition statement within a transaction force the
   * transaction to commit?
   *
   * @return true if so
   */

  public boolean dataDefinitionCausesTransactionCommit()
    throws java.sql.SQLException 
  {
    return false;
  }

  /**
   * Is a data definition statement within a transaction ignored?
   *
   * @return true if so
   */

  public boolean dataDefinitionIgnoredInTransactions()
    throws java.sql.SQLException 
  {
    return false;
  }


  /**
   * Get a description of stored procedures available in a
   * catalog.
   *
   * <P>Only procedure descriptions matching the schema and
   * procedure name criteria are returned.  They are ordered by
   * PROCEDURE_SCHEM, and PROCEDURE_NAME.
   *
   * <P>Each procedure description has the the following columns:
   *  <OL>
   *    <LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be null)
   *    <LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be null)
   *    <LI><B>PROCEDURE_NAME</B> String => procedure name
   *  <LI> reserved for future use
   *  <LI> reserved for future use
   *  <LI> reserved for future use
   *    <LI><B>REMARKS</B> String => explanatory comment on the procedure
   *    <LI><B>PROCEDURE_TYPE</B> short => kind of procedure:
   *      <UL>
   *      <LI> procedureResultUnknown - May return a result
   *      <LI> procedureNoResult - Does not return a result
   *      <LI> procedureReturnsResult - Returns a result
   *      </UL>
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schemaPattern a schema name pattern; "" retrieves those
   * without a schema
   * @param procedureNamePattern a procedure name pattern
   * @return ResultSet each row is a procedure description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getProcedures(String catalog, String schemaPattern,
                        String procedureNamePattern) throws java.sql.SQLException 
  {
    return null;
  }


  int procedureReturnsResult    = 2;

  /**
   * Get a description of a catalog's stored procedure parameters
   * and result columns.
   *
   * <P>Only descriptions matching the schema, procedure and
   * parameter name criteria are returned.  They are ordered by
   * PROCEDURE_SCHEM and PROCEDURE_NAME. Within this, the return value,
   * if any, is first. Next are the parameter descriptions in call
   * order. The column descriptions follow in column number order.
   *
   * <P>Each row in the ResultSet is a parameter desription or
   * column description with the following fields:
   *  <OL>
   *    <LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be null)
   *    <LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be null)
   *    <LI><B>PROCEDURE_NAME</B> String => procedure name
   *    <LI><B>COLUMN_NAME</B> String => column/parameter name
   *    <LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
   *      <UL>
   *      <LI> procedureColumnUnknown - nobody knows
   *      <LI> procedureColumnIn - IN parameter
   *      <LI> procedureColumnInOut - INOUT parameter
   *      <LI> procedureColumnOut - OUT parameter
   *      <LI> procedureColumnReturn - procedure return value
   *      <LI> procedureColumnResult - result column in ResultSet
   *      </UL>
   *  <LI><B>DATA_TYPE</B> short => SQL type from java.sql.Types
   *    <LI><B>TYPE_NAME</B> String => SQL type name
   *    <LI><B>PRECISION</B> int => precision
   *    <LI><B>LENGTH</B> int => length in bytes of data
   *    <LI><B>SCALE</B> short => scale
   *    <LI><B>RADIX</B> short => radix
   *    <LI><B>NULLABLE</B> short => can it contain NULL?
   *      <UL>
   *      <LI> procedureNoNulls - does not allow NULL values
   *      <LI> procedureNullable - allows NULL values
   *      <LI> procedureNullableUnknown - nullability unknown
   *      </UL>
   *    <LI><B>REMARKS</B> String => comment describing parameter/column
   *  </OL>
   *
   * <P><B>Note:</B> Some databases may not return the column
   * descriptions for a procedure. Additional columns beyond
   * REMARKS can be defined by the database.
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schemaPattern a schema name pattern; "" retrieves those
   * without a schema
   * @param procedureNamePattern a procedure name pattern
   * @param columnNamePattern a column name pattern
   * @return ResultSet each row is a stored procedure parameter or
   *      column description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getProcedureColumns(String Catalog,
                        String SchemaPattern,
                        String ProcedureNamePattern,
                        String ColumnNamePattern) throws java.sql.SQLException 
  {
    Field[] Fields = new Field[14];
    
    Fields[0]  = new Field("", "TABLE_CAT",       Types.CHAR,     1);
    Fields[1]  = new Field("", "PROCEDURE_CAT",   Types.CHAR,     1);
    Fields[2]  = new Field("", "PROCEDURE_SCHEM", Types.CHAR,     1);
    Fields[3]  = new Field("", "PROCEDURE_NAME",  Types.CHAR,     1);
    Fields[4]  = new Field("", "COLUMN_NAME",     Types.CHAR,     1);
    Fields[5]  = new Field("", "COLUMN_TYPE",     Types.CHAR,     1);
    Fields[6]  = new Field("", "DATA_TYPE",       Types.SMALLINT, 1);
    Fields[7]  = new Field("", "TYPE_NAME",       Types.CHAR,     1);
    Fields[8]  = new Field("", "PRECISION",       Types.INTEGER,  1);
    Fields[9]  = new Field("", "LENGTH",          Types.INTEGER,  1);
    Fields[10] = new Field("", "SCALE",           Types.SMALLINT, 1);
    Fields[11] = new Field("", "RADIX",           Types.SMALLINT, 1);
    Fields[12] = new Field("", "NULLABLE",        Types.SMALLINT, 1);
    Fields[13] = new Field("", "REMARKS",         Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector()); //  No procedures in MySQL
  }

  /**
   * Get a description of tables available in a catalog.
   *
   * <P>Only table descriptions matching the catalog, schema, table
   * name and type criteria are returned.  They are ordered by
   * TABLE_TYPE, TABLE_SCHEM and TABLE_NAME.
   *
   * <P>Each table description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
   *                    "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
   *                    "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
   *    <LI><B>REMARKS</B> String => explanatory comment on the table
   *  </OL>
   *
   * <P><B>Note:</B> Some databases may not return information for
   * all tables.
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schemaPattern a schema name pattern; "" retrieves those
   * without a schema
   * @param tableNamePattern a table name pattern
   * @param types a list of table types to include; null returns all types
   * @return ResultSet each row is a table description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getTables(String Catalog, String SchemaPattern, 
                                      String TableNamePattern, 
                                      String Types[])
    throws java.sql.SQLException
  {

    String DB_Sub = "";

    if (Catalog != null) {
      if (!Catalog.equals("")) {
        DB_Sub = " FROM " + Catalog;
      }
    }

    java.sql.ResultSet RS = Conn.createStatement().executeQuery(
                            "show tables " + DB_Sub + " like '" 
                            + TableNamePattern + "'");

    java.sql.ResultSetMetaData RsMd = RS.getMetaData();
    
    Field[] Fields = new Field[5];

    Fields[0] = new  Field("", "TABLE_CAT", 0, 1, (short)0, 0);
    Fields[1] = new  Field("", "TABLE_SCHEM", 0, 1, (short)0  , 0);
    Fields[2] = new  Field("", "TABLE_NAME", 0, 1, (short)0  , 0); 
    Fields[3] = new  Field("", "TABLE_TYPE", 0, 1, (short)0  , 0);
    Fields[4] = new  Field("", "REMARKS", 0, 1, (short)0  , 0);

    Vector Tuples = new Vector();

    StringBuffer[] Row = null;
    
    while (RS.next()) {
    
      String Name = RS.getString(1);
     
      Row = new StringBuffer[5];
      Row[0] = new StringBuffer("");
      Row[1] = new StringBuffer("");
      Row[2] = new StringBuffer(Name);
      Row[3] = new StringBuffer("TABLE");
      Row[4] = new StringBuffer("");
      Tuples.addElement(Row);
    } 

    ResultSet Results = new ResultSet(Fields, Tuples);
      
    return Results;
  }
  
  /**
   * Get the schema names available in this database.  The results
   * are ordered by schema name.
   *
   * <P>The schema column is:
   *  <OL>
   *    <LI><B>TABLE_SCHEM</B> String => schema name
   *  </OL>
   *
   * @return ResultSet each row has a single String column that is a
   * schema name
   */

  public java.sql.ResultSet getSchemas() throws java.sql.SQLException
  {  
    Field[] Fields = new Field[1];
    Fields[0] = new Field("", "TABLE_SCHEM", java.sql.Types.CHAR, 1);
    
    Vector Tuples = new Vector();
    ResultSet RS = new ResultSet(Fields, Tuples);
    return RS;
  }
  
  /**
   * Get the catalog names available in this database.  The results
   * are ordered by catalog name.
   *
   * <P>The catalog column is:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => catalog name
   *  </OL>
   *
   * @return ResultSet each row has a single String column that is a
   * catalog name
   */

  public java.sql.ResultSet getCatalogs() throws java.sql.SQLException
  {
    java.sql.ResultSet RS = Conn.createStatement().executeQuery(
                            "SHOW DATABASES");
    java.sql.ResultSetMetaData RSMD = RS.getMetaData();

    Field[] Fields = new Field[1];
    Fields[0]  = new Field("", "TABLE_CAT", RSMD.getColumnDisplaySize(1), 1, (short)0, 0);

    Vector Tuples = new Vector();

    while (RS.next()) {
      StringBuffer[] RowVal = new StringBuffer[1];
      RowVal[0] = new StringBuffer(RS.getString(1));
      Tuples.addElement(RowVal);
    }
      
    return new ResultSet(Fields, Tuples);
  }
  
  /**
   * Get the table types available in this database.  The results
   * are ordered by table type.
   *
   * <P>The table type is:
   *  <OL>
   *    <LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
   *                    "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
   *                    "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
   *  </OL>
   *
   * @return ResultSet each row has a single String column that is a
   * table type
   */

  public java.sql.ResultSet getTableTypes() throws java.sql.SQLException
  {
    Vector Tuples = new Vector();
    Field[] Fields = new Field[1];
    Fields[0] = new Field("", "TABLE_TYPE", 0, 1, (short)0, 0);
    StringBuffer[] TType = new StringBuffer[1];
    TType[0] = new StringBuffer("TABLE");
    Tuples.addElement(TType);

    return new ResultSet(Fields, Tuples);
  }
  
  /**
   * Get a description of table columns available in a catalog.
   *
   * <P>Only column descriptions matching the catalog, schema, table
   * and column name criteria are returned.  They are ordered by
   * TABLE_SCHEM, TABLE_NAME and ORDINAL_POSITION.
   *
   * <P>Each column description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>DATA_TYPE</B> short => SQL type from java.sql.Types
   *    <LI><B>TYPE_NAME</B> String => Data source dependent type name
   *    <LI><B>COLUMN_SIZE</B> int => column size.  For char or date
   *        types this is the maximum number of characters, for numeric or
   *        decimal types this is precision.
   *    <LI><B>BUFFER_LENGTH</B> is not used.
   *    <LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits
   *    <LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
   *    <LI><B>NULLABLE</B> int => is NULL allowed?
   *      <UL>
   *      <LI> columnNoNulls - might not allow NULL values
   *      <LI> columnNullable - definitely allows NULL values
   *      <LI> columnNullableUnknown - nullability unknown
   *      </UL>
   *    <LI><B>REMARKS</B> String => comment describing column (may be null)
   *    <LI><B>COLUMN_DEF</B> String => default value (may be null)
   *    <LI><B>SQL_DATA_TYPE</B> int => unused
   *    <LI><B>SQL_DATETIME_SUB</B> int => unused
   *    <LI><B>CHAR_OCTET_LENGTH</B> int => for char types the
   *       maximum number of bytes in the column
   *    <LI><B>ORDINAL_POSITION</B> int => index of column in table
   *      (starting at 1)
   *    <LI><B>IS_NULLABLE</B> String => "NO" means column definitely
   *      does not allow NULL values; "YES" means the column might
   *      allow NULL values.  An empty string means nobody knows.
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schemaPattern a schema name pattern; "" retrieves those
   * without a schema
   * @param tableNamePattern a table name pattern
   * @param columnNamePattern a column name pattern
   * @return ResultSet each row is a column description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getColumns(String Catalog, String SchemaPattern,
                              String TableName, 
                              String ColumnNamePattern)
    throws java.sql.SQLException
  {
    String DB_Sub = "";

    if (Catalog != null) {
      if (!Catalog.equals("")) {
        DB_Sub = " FROM " + Catalog;
      }
    }
 
    java.sql.ResultSet RS = Conn.execSQL(
                            "show columns from " + 
                            TableName + DB_Sub + " like '" 
                            + ColumnNamePattern + "'");

    java.sql.ResultSetMetaData RSMD = RS.getMetaData();

    Field[] Fields = new Field[18];
    
    int catalog_length = 0;
    int tablename_length = 0;
    
    if (Catalog != null) {
        catalog_length = Catalog.length();
    }
    
    if (TableName != null) {
        tablename_length = TableName.length();
    }
    
    Fields[0]  = new Field("", "TABLE_CAT"        , catalog_length, 1, (short)0, 0);
    Fields[1]  = new Field("", "TABLE_SCHEM"      ,  0, 1, (short)0, 0);
    Fields[2]  = new Field("", "TABLE_NAME"       , tablename_length, 1, (short)0, 0);
    Fields[3]  = new Field("", "COLUMN_NAME"      ,  0, 1, (short)0, 0);
    Fields[4]  = new Field("", "DATA_TYPE"        ,  0, 9, (short)0, 0);
    Fields[5]  = new Field("", "TYPE_NAME"        ,  0, 1, (short)0, 0);
    Fields[6]  = new Field("", "COLUMN_SIZE"      ,  0, 9, (short)0, 0);
    Fields[7]  = new Field("", "BUFFER_LENGTH"    ,  0, 9, (short)0, 0);
    Fields[8]  = new Field("", "DECIMAL_DIGITS"   ,  0, 9, (short)0, 0);
    Fields[9]  = new Field("", "NUM_PREC_RADIX"   ,  0, 9, (short)0, 0);
    Fields[10] = new Field("", "NULLABLE"         ,  0, 9, (short)0, 0);
    Fields[11] = new Field("", "REMARK"           ,  0, 1, (short)0, 0);
    Fields[12] = new Field("", "COLUMN_DEF"       ,  0, 1, (short)0, 0);
    Fields[13] = new Field("", "SQL_DATA_TYPE"    ,  0, 9, (short)0, 0);
    Fields[14] = new Field("", "SQL_DATE_TIME_SUB",  0, 1, (short)0, 0);
    Fields[15] = new Field("", "CHAR_OCTET_LENGTH",  0, 9, (short)0, 0);
    Fields[16] = new Field("", "ORDINAL_POSITION" ,  0, 9, (short)0, 0);
    Fields[17] = new Field("", "IS_NULLABLE"      ,  3, 1, (short)0, 0);

    Vector Tuples = new Vector();
    int ord_pos = 1;

    /*
     * Fix up Catalog and TableName
     */
     
    if (Catalog == null) {
        Catalog = "";
    }
    
    if (TableName == null) {
        TableName = "";
    }
    
    while (RS.next()) {
      StringBuffer[] RowVal = new StringBuffer[18];

      
      RowVal[0] = new StringBuffer(Catalog);   // TABLE_CAT
      RowVal[1] = new StringBuffer("");        // TABLE_SCHEM (No schemas in MySQL)
      
      RowVal[2] = new StringBuffer(TableName); // TABLE_NAME
      RowVal[3] = new StringBuffer(RS.getString("Field"));
      
      String TypeInfo = RS.getString("Type");
      if (Driver.debug) {
        System.out.println("Type: " + TypeInfo);
      }
      String MysqlType = "";
      
      if (TypeInfo.indexOf("(") != -1) {
          MysqlType = TypeInfo.substring(0, TypeInfo.indexOf("("));
      }
      else {
          MysqlType = TypeInfo;
      }
      RowVal[4] = new StringBuffer(MysqlType); // DATA_TYPE (jdbc)

      RowVal[5] = new StringBuffer(MysqlType); // TYPE_NAME (native)

      // Figure Out the Size

      if (TypeInfo != null) {
        if (TypeInfo.indexOf(",") != -1) {
          // Numeric with decimals
          String Size = TypeInfo.substring((TypeInfo.indexOf("(") + 1),
                                           (TypeInfo.indexOf(",")));
          String Decimals = TypeInfo.substring((TypeInfo.indexOf(",") + 1),
                                               (TypeInfo.indexOf(")")));
          RowVal[6] = new StringBuffer(Size);
          RowVal[8] = new StringBuffer(Decimals);
        }
        else {
          String Size = "";
          if (TypeInfo.indexOf("(") != -1) {
              Size = TypeInfo.substring((TypeInfo.indexOf("(") + 1),
                                           (TypeInfo.indexOf(")")));
          }
          RowVal[6] = new StringBuffer(Size);
          RowVal[8] = new StringBuffer("0");
        }
      }
      else {                    
        RowVal[8] = new StringBuffer("0");
        RowVal[6] = new StringBuffer("0");
      }

      RowVal[7] = new StringBuffer(Integer.toString(MysqlIO.MAXBUF)); // BUFFER_LENGTH
      RowVal[9] = new StringBuffer("10"); // NUM_PREC_RADIX (is this right for char?)

      String Nullable = RS.getString("Null");

      // Nullable?

      if (Nullable != null) {
        if (Nullable.equals("YES")) {
          RowVal[10] = new StringBuffer(Integer.toString(columnNullable));
          RowVal[17] = new StringBuffer("YES"); // IS_NULLABLE
        }
        else {
          RowVal[10] = new StringBuffer(Integer.toString(columnNoNulls));
          RowVal[17] = new StringBuffer("NO");
        }
      }
      else {
        RowVal[10] = new StringBuffer(Integer.toString(columnNoNulls));
        RowVal[17] = new StringBuffer("NO");
      }

      RowVal[11] = new StringBuffer(""); // REMARK
      
      
      // COLUMN_DEF
      String Default = RS.getString("Default");

      RowVal[12] = new StringBuffer("");
      if (Default != null)
        RowVal[12] = new StringBuffer(Default);

      RowVal[13] = new StringBuffer(MysqlType);  // SQL_DATA_TYPE
      RowVal[14] = new StringBuffer("");       // SQL_DATE_TIME_SUB
      RowVal[15] = new StringBuffer("");       // CHAR_OCTET_LENGTH
      RowVal[16] = new StringBuffer(Integer.toString(ord_pos++));  // ORDINAL_POSITION
     
      Tuples.addElement(RowVal);
    }
    
    ResultSet Results = new ResultSet(Fields, Tuples);
    return Results;
  }

  /**
   * Get a description of the access rights for a table's columns.
   *
   * <P>Only privileges matching the column name criteria are
   * returned.  They are ordered by COLUMN_NAME and PRIVILEGE.
   *
   * <P>Each privilige description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>GRANTOR</B> => grantor of access (may be null)
   *    <LI><B>GRANTEE</B> String => grantee of access
   *    <LI><B>PRIVILEGE</B> String => name of access (SELECT,
   *      INSERT, UPDATE, REFRENCES, ...)
   *    <LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted
   *      to grant to others; "NO" if not; null if unknown
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name; "" retrieves those without a schema
   * @param table a table name
   * @param columnNamePattern a column name pattern
   * @return ResultSet each row is a column privilege description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getColumnPrivileges(String catalog, String schema,
                String table, String columnNamePattern) throws java.sql.SQLException 
  {
    Field[] Fields = new Field[8];
    
    Fields[0]  = new Field("", "TABLE_CAT",    Types.CHAR,     1);
    Fields[1]  = new Field("", "TABLE_SCHEM",  Types.CHAR,     1);
    Fields[2]  = new Field("", "TABLE_NAME",   Types.CHAR,     1);
    Fields[3]  = new Field("", "COLUMN_NAME",  Types.CHAR,     1);
    Fields[4]  = new Field("", "GRANTOR",      Types.CHAR,     1);
    Fields[5]  = new Field("", "GRANTEE",      Types.CHAR,     1);
    Fields[6]  = new Field("", "PRIVILEGE",    Types.CHAR,     1); 
    Fields[7]  = new Field("", "IS_GRANTABLE", Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector());
  }

  /**
   * Get a description of the access rights for each table available
   * in a catalog.
   *
   * <P>Only privileges matching the schema and table name
   * criteria are returned.  They are ordered by TABLE_SCHEM,
   * TABLE_NAME, and PRIVILEGE.
   *
   * <P>Each privilige description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>GRANTOR</B> => grantor of access (may be null)
   *    <LI><B>GRANTEE</B> String => grantee of access
   *    <LI><B>PRIVILEGE</B> String => name of access (SELECT,
   *      INSERT, UPDATE, REFRENCES, ...)
   *    <LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted
   *      to grant to others; "NO" if not; null if unknown
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schemaPattern a schema name pattern; "" retrieves those
   * without a schema
   * @param tableNamePattern a table name pattern
   * @return ResultSet each row is a table privilege description
   * @see #getSearchStringEscape
   */

  public java.sql.ResultSet getTablePrivileges(String catalog, String schemaPattern,
                                String tableNamePattern) throws java.sql.SQLException 
  {
    Field[] Fields = new Field[8];
    
    Fields[0]  = new Field("", "TABLE_CAT",    Types.CHAR,     1);
    Fields[1]  = new Field("", "TABLE_SCHEM",  Types.CHAR,     1);
    Fields[2]  = new Field("", "TABLE_NAME",   Types.CHAR,     1);
    Fields[3]  = new Field("", "COLUMN_NAME",  Types.CHAR,     1);
    Fields[4]  = new Field("", "GRANTOR",      Types.CHAR,     1);
    Fields[5]  = new Field("", "GRANTEE",      Types.CHAR,     1);
    Fields[6]  = new Field("", "PRIVILEGE",    Types.CHAR,     1); 
    Fields[7]  = new Field("", "IS_GRANTABLE", Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector());
  }

  /**
   * Get a description of a table's optimal set of columns that
   * uniquely identifies a row. They are ordered by SCOPE.
   *
   * <P>Each column description has the following columns:
   *  <OL>
   *    <LI><B>SCOPE</B> short => actual scope of result
   *      <UL>
   *      <LI> bestRowTemporary - very temporary, while using row
   *      <LI> bestRowTransaction - valid for remainder of current transaction
   *      <LI> bestRowSession - valid for remainder of current session
   *      </UL>
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
   *    <LI><B>TYPE_NAME</B> String => Data source dependent type name
   *    <LI><B>COLUMN_SIZE</B> int => precision
   *    <LI><B>BUFFER_LENGTH</B> int => not used
   *    <LI><B>DECIMAL_DIGITS</B> short  => scale
   *    <LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column
   *      like an Oracle ROWID
   *      <UL>
   *      <LI> bestRowUnknown - may or may not be pseudo column
   *      <LI> bestRowNotPseudo - is NOT a pseudo column
   *      <LI> bestRowPseudo - is a pseudo column
   *      </UL>
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name; "" retrieves those without a schema
   * @param table a table name
   * @param scope the scope of interest; use same values as SCOPE
   * @param nullable include columns that are nullable?
   * @return ResultSet each row is a column description
   */

  public java.sql.ResultSet getBestRowIdentifier(String Catalog, String Schema,
                String Table, int scope, boolean nullable) 
    throws java.sql.SQLException 
  {
    Field[] Fields = new Field[8];
    
    Fields[0]  = new Field("", "SCOPE",          Types.SMALLINT, 1);
    Fields[1]  = new Field("", "COLUMN_NAME",    Types.CHAR,     1);
    Fields[2]  = new Field("", "DATA_TYPE",      Types.SMALLINT, 1);
    Fields[3]  = new Field("", "TYPE_NAME",      Types.CHAR,     1);
    Fields[4]  = new Field("", "COLUMN_SIZE",    Types.INTEGER,  1);
    Fields[5]  = new Field("", "BUFFER_LENGTH",  Types.INTEGER,  1);
    Fields[6]  = new Field("", "DECIMAL_DIGITS", Types.INTEGER,  1);
    Fields[7]  = new Field("", "PSEUDO_COLUMN",  Types.SMALLINT, 1);  
    
    String DB_Sub = "";

    if (Catalog != null) {
      if (!Catalog.equals("")) {
        DB_Sub = " FROM " + Catalog;
      }
    }
    
    if (Table == null) {
        throw new java.sql.SQLException("Table not specified.", "S1009"); 
    }
 
    java.sql.ResultSet RS = Conn.execSQL(
                            "show columns from " + 
                            Table + DB_Sub);
    
    Vector Tuples = new Vector();
                            
    while (RS.next()) {
        String KeyType = RS.getString("Key");
        if (KeyType != null) {
                if (KeyType.equals("PRI")) {
                        StringBuffer[] Tuple = new StringBuffer[8];
                        
                        Tuple[0] = new StringBuffer(Integer.toString(java.sql.DatabaseMetaData.bestRowSession ));
                        Tuple[1] = new StringBuffer(RS.getString("Field")); 
                        
                        String Type = RS.getString("Type");
                        int size = MysqlIO.getMaxBuf();
                        int decimals = 0;
                        
                        /*
                         * Parse the Type column from MySQL
                         */
                         
                        if (Type.indexOf("(") != -1) {
                                if (Type.indexOf(",") != -1) {
                                        size = Integer.parseInt(Type.substring(Type.indexOf("(") + 1, Type.indexOf(",")));
                                        decimals = Integer.parseInt(Type.substring(Type.indexOf(",") + 1, Type.indexOf(")")));
                                }
                                else {
                                        size = Integer.parseInt(Type.substring(Type.indexOf("(") + 1, Type.indexOf(")")));
                                }
                                Type = Type.substring(Type.indexOf("("));
                        }
                        
                        Tuple[2] = new StringBuffer(""); // FIXME!
                        Tuple[3] = new StringBuffer(Type);
                        Tuple[4] = new StringBuffer(Integer.toString(size + decimals));
                        Tuple[5] = new StringBuffer(Integer.toString(size + decimals));
                        Tuple[6] = new StringBuffer(Integer.toString(decimals));
                        Tuple[7] = new StringBuffer(Integer.toString(java.sql.DatabaseMetaData.bestRowNotPseudo));
                        
                        Tuples.addElement(Tuple);           
                }
        }
    }
    
    return new ResultSet(Fields, Tuples);
  }

  /**
   * Get a description of a table's columns that are automatically
   * updated when any value in a row is updated.  They are
   * unordered.
   *
   * <P>Each column description has the following columns:
   *  <OL>
   *    <LI><B>SCOPE</B> short => is not used
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
   *    <LI><B>TYPE_NAME</B> String => Data source dependent type name
   *    <LI><B>COLUMN_SIZE</B> int => precision
   *    <LI><B>BUFFER_LENGTH</B> int => length of column value in bytes
   *    <LI><B>DECIMAL_DIGITS</B> short  => scale
   *    <LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column
   *      like an Oracle ROWID
   *      <UL>
   *      <LI> versionColumnUnknown - may or may not be pseudo column
   *      <LI> versionColumnNotPseudo - is NOT a pseudo column
   *      <LI> versionColumnPseudo - is a pseudo column
   *      </UL>
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name; "" retrieves those without a schema
   * @param table a table name
   * @return ResultSet each row is a column description
   */

  public java.sql.ResultSet getVersionColumns(String catalog, String schema,
                                String table) throws java.sql.SQLException 
  {  
    Field[] Fields = new Field[8];
    
    Fields[0]  = new Field("", "SCOPE",          Types.SMALLINT, 1);
    Fields[1]  = new Field("", "COLUMN_NAME",    Types.CHAR,     1);
    Fields[2]  = new Field("", "DATA_TYPE",      Types.SMALLINT, 1);
    Fields[3]  = new Field("", "TYPE_NAME",      Types.CHAR,     1);
    Fields[4]  = new Field("", "COLUMN_SIZE",    Types.CHAR,     1);
    Fields[5]  = new Field("", "BUFFER_LENGTH",  Types.CHAR,     1);
    Fields[6]  = new Field("", "DECIMAL_DIGITS", Types.CHAR,     1);
    Fields[7]  = new Field("", "PSEUDO_COLUMN",  Types.SMALLINT, 1);
    
    return new ResultSet(Fields, new Vector()); // do TIMESTAMP columns count?
  }

  /**
   * Get a description of a table's primary key columns.  They
   * are ordered by COLUMN_NAME.
   *
   * <P>Each column description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>COLUMN_NAME</B> String => column name
   *    <LI><B>KEY_SEQ</B> short => sequence number within primary key
   *    <LI><B>PK_NAME</B> String => primary key name (may be null)
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name pattern; "" retrieves those
   * without a schema
   * @param table a table name
   * @return ResultSet each row is a primary key column description
   */

  public java.sql.ResultSet getPrimaryKeys(String Catalog, String Schema, String Table) 
    throws java.sql.SQLException
  {
  
    Field[] Fields = new Field[6];
    
    Fields[0]  = new Field("", "TABLE_CAT",   Types.CHAR,     1);
    Fields[1]  = new Field("", "TABLE_SCHEM", Types.CHAR,     1);
    Fields[2]  = new Field("", "TABLE_NAME",  Types.CHAR,     1);
    Fields[3]  = new Field("", "COLUMN_NAME", Types.CHAR,     1);
    Fields[4]  = new Field("", "KEY_SEQ",     Types.CHAR,     1);
    Fields[5]  = new Field("", "PK_NAME",     Types.CHAR,     1);
    
    String DB_Sub = "";

    if (Catalog != null) {
      if (!Catalog.equals("")) {
        DB_Sub = " FROM " + Catalog;
      }
    }
    
    if (Table == null) {
        throw new java.sql.SQLException("Table not specified.", "S1009"); 
    }
 
    java.sql.ResultSet RS = Conn.execSQL(
                            "show columns from " + 
                            Table + DB_Sub);
    
    Vector Tuples = new Vector();
                            
    while (RS.next()) {
        String KeyType = RS.getString("Key");
        if (KeyType != null) {
                if (KeyType.equals("PRI")) {
                        StringBuffer[] Tuple = new StringBuffer[6];
                        Tuple[0] = new StringBuffer((Catalog == null ? "" : Catalog));
                        Tuple[1] = new StringBuffer("");
                        Tuple[2] = new StringBuffer(Table);
                        Tuple[3] = new StringBuffer(RS.getString("Field"));
                        Tuple[4] = new StringBuffer("0");
                        Tuple[5] = new StringBuffer(RS.getString("Field"));
                        Tuples.addElement(Tuple);
                }
         }
    }
                        
                        
    
    
    return new ResultSet(Fields, Tuples);
  }
  
  /**
   * Get a description of the primary key columns that are
   * referenced by a table's foreign key columns (the primary keys
   * imported by a table).  They are ordered by PKTABLE_CAT,
   * PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ.
   *
   * <P>Each primary key column description has the following columns:
   *  <OL>
   *    <LI><B>PKTABLE_CAT</B> String => primary key table catalog
   *      being imported (may be null)
   *    <LI><B>PKTABLE_SCHEM</B> String => primary key table schema
   *      being imported (may be null)
   *    <LI><B>PKTABLE_NAME</B> String => primary key table name
   *      being imported
   *    <LI><B>PKCOLUMN_NAME</B> String => primary key column name
   *      being imported
   *    <LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
   *    <LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
   *    <LI><B>FKTABLE_NAME</B> String => foreign key table name
   *    <LI><B>FKCOLUMN_NAME</B> String => foreign key column name
   *    <LI><B>KEY_SEQ</B> short => sequence number within foreign key
   *    <LI><B>UPDATE_RULE</B> short => What happens to
   *       foreign key when primary is updated:
   *      <UL>
   *      <LI> importedKeyCascade - change imported key to agree
   *               with primary key update
   *      <LI> importedKeyRestrict - do not allow update of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been updated
   *      </UL>
   *    <LI><B>DELETE_RULE</B> short => What happens to
   *      the foreign key when primary is deleted.
   *      <UL>
   *      <LI> importedKeyCascade - delete rows that import a deleted key
   *      <LI> importedKeyRestrict - do not allow delete of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been deleted
   *      </UL>
   *    <LI><B>FK_NAME</B> String => foreign key name (may be null)
   *    <LI><B>PK_NAME</B> String => primary key name (may be null)
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name pattern; "" retrieves those
   * without a schema
   * @param table a table name
   * @return ResultSet each row is a primary key column description
   * @see #getExportedKeys
   */

  public java.sql.ResultSet getImportedKeys(String catalog, String schema,
                                String table) throws java.sql.SQLException 
  {  
    Field[] Fields = new Field[13];
    
    Fields[0]  = new Field("", "PKTABLE_CAT",   Types.CHAR,     1);
    Fields[1]  = new Field("", "PKTABLE_SCHEM", Types.CHAR,     1);
    Fields[2]  = new Field("", "PKTABLE_NAME",  Types.CHAR,     1);
    Fields[3]  = new Field("", "PKCOLUMN_NAME", Types.CHAR,     1);
    Fields[4]  = new Field("", "FKTABLE_CAT",   Types.CHAR,     1);
    Fields[5]  = new Field("", "FKTABLE_SCHEM", Types.CHAR,     1);
    Fields[6]  = new Field("", "FKTABLE_NAME",  Types.CHAR,     1);
    Fields[7]  = new Field("", "FKCOLUMN_NAME", Types.CHAR,     1);
    Fields[8]  = new Field("", "KEY_SEQ",       Types.SMALLINT, 1);
    Fields[9]  = new Field("", "UPDATE_RULE",   Types.SMALLINT, 1); 
    Fields[10] = new Field("", "DELETE_RULE",   Types.SMALLINT, 1);
    Fields[11] = new Field("", "FK_NAME",       Types.CHAR,     1);
    Fields[12] = new Field("", "PK_NAME",       Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector());
  }

  /**
   * Get a description of a foreign key columns that reference a
   * table's primary key columns (the foreign keys exported by a
   * table).  They are ordered by FKTABLE_CAT, FKTABLE_SCHEM,
   * FKTABLE_NAME, and KEY_SEQ.
   *
   * <P>Each foreign key column description has the following columns:
   *  <OL>
   *    <LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
   *    <LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
   *    <LI><B>PKTABLE_NAME</B> String => primary key table name
   *    <LI><B>PKCOLUMN_NAME</B> String => primary key column name
   *    <LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
   *      being exported (may be null)
   *    <LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
   *      being exported (may be null)
   *    <LI><B>FKTABLE_NAME</B> String => foreign key table name
   *      being exported
   *    <LI><B>FKCOLUMN_NAME</B> String => foreign key column name
   *      being exported
   *    <LI><B>KEY_SEQ</B> short => sequence number within foreign key
   *    <LI><B>UPDATE_RULE</B> short => What happens to
   *       foreign key when primary is updated:
   *      <UL>
   *      <LI> importedKeyCascade - change imported key to agree
   *               with primary key update
   *      <LI> importedKeyRestrict - do not allow update of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been updated
   *      </UL>
   *    <LI><B>DELETE_RULE</B> short => What happens to
   *      the foreign key when primary is deleted.
   *      <UL>
   *      <LI> importedKeyCascade - delete rows that import a deleted key
   *      <LI> importedKeyRestrict - do not allow delete of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been deleted
   *      </UL>
   *    <LI><B>FK_NAME</B> String => foreign key identifier (may be null)
   *    <LI><B>PK_NAME</B> String => primary key identifier (may be null)
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name pattern; "" retrieves those
   * without a schema
   * @param table a table name
   * @return ResultSet each row is a foreign key column description
   * @see #getImportedKeys
   */

  public java.sql.ResultSet getExportedKeys(String catalog, String schema,
                                String table) throws java.sql.SQLException 
  {
    Field[] Fields = new Field[13];
    
    Fields[0]  = new Field("", "PKTABLE_CAT",   Types.CHAR,     1);
    Fields[1]  = new Field("", "PKTABLE_SCHEM", Types.CHAR,     1);
    Fields[2]  = new Field("", "PKTABLE_NAME",  Types.CHAR,     1);
    Fields[3]  = new Field("", "PKCOLUMN_NAME", Types.CHAR,     1);
    Fields[4]  = new Field("", "FKTABLE_CAT",   Types.CHAR,     1);
    Fields[5]  = new Field("", "FKTABLE_SCHEM", Types.CHAR,     1);
    Fields[6]  = new Field("", "FKTABLE_NAME",  Types.CHAR,     1);
    Fields[7]  = new Field("", "FKCOLUMN_NAME", Types.CHAR,     1);
    Fields[8]  = new Field("", "KEY_SEQ",       Types.SMALLINT, 1);
    Fields[9]  = new Field("", "UPDATE_RULE",   Types.SMALLINT, 1); 
    Fields[10] = new Field("", "DELETE_RULE",   Types.SMALLINT, 1);
    Fields[11] = new Field("", "FK_NAME",       Types.CHAR,     1);
    Fields[12] = new Field("", "PK_NAME",       Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector()); // No foreign keys in MySQL
  }

  /**
   * Get a description of the foreign key columns in the foreign key
   * table that reference the primary key columns of the primary key
   * table (describe how one table imports another's key.) This
   * should normally return a single foreign key/primary key pair
   * (most tables only import a foreign key from a table once.)  They
   * are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and
   * KEY_SEQ.
   *
   * <P>Each foreign key column description has the following columns:
   *  <OL>
   *    <LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
   *    <LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
   *    <LI><B>PKTABLE_NAME</B> String => primary key table name
   *    <LI><B>PKCOLUMN_NAME</B> String => primary key column name
   *    <LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
   *      being exported (may be null)
   *    <LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
   *      being exported (may be null)
   *    <LI><B>FKTABLE_NAME</B> String => foreign key table name
   *      being exported
   *    <LI><B>FKCOLUMN_NAME</B> String => foreign key column name
   *      being exported
   *    <LI><B>KEY_SEQ</B> short => sequence number within foreign key
   *    <LI><B>UPDATE_RULE</B> short => What happens to
   *       foreign key when primary is updated:
   *      <UL>
   *      <LI> importedKeyCascade - change imported key to agree
   *               with primary key update
   *      <LI> importedKeyRestrict - do not allow update of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been updated
   *      </UL>
   *    <LI><B>DELETE_RULE</B> short => What happens to
   *      the foreign key when primary is deleted.
   *      <UL>
   *      <LI> importedKeyCascade - delete rows that import a deleted key
   *      <LI> importedKeyRestrict - do not allow delete of primary
   *               key if it has been imported
   *      <LI> importedKeySetNull - change imported key to NULL if
   *               its primary key has been deleted
   *      </UL>
   *    <LI><B>FK_NAME</B> String => foreign key identifier (may be null)
   *    <LI><B>PK_NAME</B> String => primary key identifier (may be null)
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name pattern; "" retrieves those
   * without a schema
   * @param table a table name
   * @return ResultSet each row is a foreign key column description
   * @see #getImportedKeys
   */

  public java.sql.ResultSet getCrossReference(String PrimaryCatalog, 
                                     String PrimarySchema, 
                                     String PrimaryTable,
                                     String ForeignCatalog, 
                                     String ForeignSchema, 
                                     String ForeignTable
                                     ) throws java.sql.SQLException 
  {
    Field[] Fields = new Field[13];
    
    Fields[0]  = new Field("", "PKTABLE_CAT",   Types.CHAR,     1);
    Fields[1]  = new Field("", "PKTABLE_SCHEM", Types.CHAR,     1);
    Fields[2]  = new Field("", "PKTABLE_NAME",  Types.CHAR,     1);
    Fields[3]  = new Field("", "PKCOLUMN_NAME", Types.CHAR,     1);
    Fields[4]  = new Field("", "FKTABLE_CAT",   Types.CHAR,     1);
    Fields[5]  = new Field("", "FKTABLE_SCHEM", Types.CHAR,     1);
    Fields[6]  = new Field("", "FKTABLE_NAME",  Types.CHAR,     1);
    Fields[7]  = new Field("", "FKCOLUMN_NAME", Types.CHAR,     1);
    Fields[8]  = new Field("", "KEY_SEQ",       Types.SMALLINT, 1);
    Fields[9]  = new Field("", "UPDATE_RULE",   Types.SMALLINT, 1);
    Fields[10] = new Field("", "DELETE_RULE",   Types.SMALLINT, 1);
    Fields[11] = new Field("", "FK_NAME",       Types.CHAR,     1);
    Fields[12] = new Field("", "PK_NAME",       Types.CHAR,     1);
    
    return new ResultSet(Fields, new Vector()); // no foreign keys in MySQL
  }
  
  /**
   * Get a description of all the standard SQL types supported by
   * this database. They are ordered by DATA_TYPE and then by how
   * closely the data type maps to the corresponding JDBC SQL type.
   *
   * <P>Each type description has the following columns:
   *  <OL>
   *    <LI><B>TYPE_NAME</B> String => Type name
   *    <LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
   *    <LI><B>PRECISION</B> int => maximum precision
   *    <LI><B>LITERAL_PREFIX</B> String => prefix used to quote a literal
   *      (may be null)
   *    <LI><B>LITERAL_SUFFIX</B> String => suffix used to quote a literal
   (may be null)
   *    <LI><B>CREATE_PARAMS</B> String => parameters used in creating
   *      the type (may be null)
   *    <LI><B>NULLABLE</B> short => can you use NULL for this type?
   *      <UL>
   *      <LI> typeNoNulls - does not allow NULL values
   *      <LI> typeNullable - allows NULL values
   *      <LI> typeNullableUnknown - nullability unknown
   *      </UL>
   *    <LI><B>CASE_SENSITIVE</B> boolean=> is it case sensitive?
   *    <LI><B>SEARCHABLE</B> short => can you use "WHERE" based on this type:
   *      <UL>
   *      <LI> typePredNone - No support
   *      <LI> typePredChar - Only supported with WHERE .. LIKE
   *      <LI> typePredBasic - Supported except for WHERE .. LIKE
   *      <LI> typeSearchable - Supported for all WHERE ..
   *      </UL>
   *    <LI><B>UNSIGNED_ATTRIBUTE</B> boolean => is it unsigned?
   *    <LI><B>FIXED_PREC_SCALE</B> boolean => can it be a money value?
   *    <LI><B>AUTO_INCREMENT</B> boolean => can it be used for an
   *      auto-increment value?
   *    <LI><B>LOCAL_TYPE_NAME</B> String => localized version of type name
   *      (may be null)
   *    <LI><B>MINIMUM_SCALE</B> short => minimum scale supported
   *    <LI><B>MAXIMUM_SCALE</B> short => maximum scale supported
   *    <LI><B>SQL_DATA_TYPE</B> int => unused
   *    <LI><B>SQL_DATETIME_SUB</B> int => unused
   *    <LI><B>NUM_PREC_RADIX</B> int => usually 2 or 10
   *  </OL>
   *
   * @return ResultSet each row is a SQL type description
   */

  public java.sql.ResultSet getTypeInfo() throws java.sql.SQLException 
  { 
    Field[] Fields = new Field[18];
    
    Fields[0]  = new Field("", "TYPE_NAME",          Types.CHAR,     1);
    Fields[1]  = new Field("", "DATA_TYPE",          Types.SMALLINT, 1);
    Fields[2]  = new Field("", "PRECISION",          Types.INTEGER,  1);
    Fields[3]  = new Field("", "LITERAL_PREFIX",     Types.CHAR,     1);
    Fields[4]  = new Field("", "LITERAL_SUFFIX",     Types.CHAR,     1);
    Fields[5]  = new Field("", "CREATE_PARAMS",      Types.CHAR,     1);
    Fields[6]  = new Field("", "NULLABLE",           Types.SMALLINT, 1);
    Fields[7]  = new Field("", "CASE_SENSITIVE",     Types.CHAR,     1);
    Fields[8]  = new Field("", "SEARCHABLE",         Types.SMALLINT, 1);
    Fields[9]  = new Field("", "UNSIGNED_ATTRIBUTE", Types.CHAR,     1);
    Fields[10] = new Field("", "FIXED_PREC_SCALE",   Types.CHAR,     1);
    Fields[11] = new Field("", "AUTO_INCREMENT",     Types.CHAR,     1);
    Fields[12] = new Field("", "LOCAL_TYPE_NAME",    Types.CHAR,     1);
    Fields[13] = new Field("", "MINIMUM_SCALE",      Types.SMALLINT, 1);
    Fields[14] = new Field("", "MAXIMUM_SCALE",      Types.SMALLINT, 1);
    Fields[15] = new Field("", "SQL_DATA_TYPE",      Types.INTEGER,  1);
    Fields[16] = new Field("", "SQL_DATETIME_SUB",   Types.INTEGER,  1);
    Fields[16] = new Field("", "NUM_PREC_RADIX",     Types.INTEGER,  1);
    
    StringBuffer[] RowVal = new StringBuffer[18];
    Vector Tuples = new Vector();
  
   /*
    * The following are ordered by java.sql.Types, and
    * then by how closely the MySQL type matches the 
    * JDBC Type (per spec)
    */
    
    /*
     * MySQL Type: TINYINT
     * JDBC  Type: TINYINT
     */
    
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("TINYINT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.TINYINT); // JDBC Data type
    RowVal[2]  = new StringBuffer("3"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) UNSIGNED ZEROFILL"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("true"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("TINYINT"); // Locale Type Name
    RowVal[13] = new StringBuffer("0"); // Minimum Scale
    RowVal[14] = new StringBuffer("0"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: BIGINT
     * JDBC  Type: BIGINT
     */
    
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("BIGINT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.BIGINT); // JDBC Data type
    RowVal[2]  = new StringBuffer("19"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) UNSIGNED ZEROFILL"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("true"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("BIGINT"); // Locale Type Name
    RowVal[13] = new StringBuffer("0"); // Minimum Scale
    RowVal[14] = new StringBuffer("0"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
    
    Tuples.addElement(RowVal);
       
    /*
     * MySQL Type: MEDIUMBLOB
     * JDBC  Type: LONGVARBINARY
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("MEDIUMBLOB"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.LONGVARBINARY); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("true"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("MEDIUMBLOB"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
   
    Tuples.addElement(RowVal); 
     
    /*
     * MySQL Type: LONGBLOB
     * JDBC  Type: LONGVARBINARY
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("LONGBLOB"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.LONGVARBINARY); // JDBC Data type
    RowVal[2]  = new StringBuffer(""); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("true"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("LONGBLOB"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
     
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: BLOB
     * JDBC  Type: LONGVARBINARY
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("BLOB"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.LONGVARBINARY); // JDBC Data type
    RowVal[2]  = new StringBuffer(""); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("true"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("BLOB"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
    
    Tuples.addElement(RowVal);
                                  
    /*
     * MySQL Type: TINYBLOB
     * JDBC  Type: VARBINARY
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("TINYBLOB"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.VARBINARY); // JDBC Data type
    RowVal[2]  = new StringBuffer(""); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("true"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("TINYBLOB"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
    
    Tuples.addElement(RowVal);
       
    /*
     * MySQL Type: CHAR
     * JDBC  Type: CHAR
     */
    
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("CHAR"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.CHAR); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) [BINARY]"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("CHAR"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
   
    Tuples.addElement(RowVal); 
    
    /*
     * MySQL Type: NUMERIC
     * JDBC  Type: NUMERIC
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("NUMERIC"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.NUMERIC); // JDBC Data type
    RowVal[2]  = new StringBuffer("17"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("NUMERIC"); // Locale Type Name
    RowVal[13] = new StringBuffer("-308"); // Minimum Scale
    RowVal[14] = new StringBuffer("308"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
   
    Tuples.addElement(RowVal); 
    
    /*
     * MySQL Type: DECIMAL
     * JDBC  Type: DECIMAL
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("DECIMAL"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DECIMAL); // JDBC Data type
    RowVal[2]  = new StringBuffer("17"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("DECIMAL"); // Locale Type Name
    RowVal[13] = new StringBuffer("-308"); // Minimum Scale
    RowVal[14] = new StringBuffer("308"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
   
    Tuples.addElement(RowVal); 
       
    /*
     * MySQL Type: INT
     * JDBC  Type: INTEGER
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("INT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.INTEGER); // JDBC Data type
    RowVal[2]  = new StringBuffer("10"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) UNSIGNED ZEROFILL"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("true"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("INT"); // Locale Type Name
    RowVal[13] = new StringBuffer("0"); // Minimum Scale
    RowVal[14] = new StringBuffer("0"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10)
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: MEDIUMINT
     * JDBC  Type: INTEGER
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("MEDIUMINT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.INTEGER); // JDBC Data type
    RowVal[2]  = new StringBuffer("7"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) UNSIGNED ZEROFILL"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("true"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("MEDIUMINT"); // Locale Type Name
    RowVal[13] = new StringBuffer("0"); // Minimum Scale
    RowVal[14] = new StringBuffer("0"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: SMALLINT
     * JDBC  Type: SMALLINT
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("SMALLINT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.SMALLINT); // JDBC Data type
    RowVal[2]  = new StringBuffer("5"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M) UNSIGNED ZEROFILL"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("true"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("SMALLINT"); // Locale Type Name
    RowVal[13] = new StringBuffer("0"); // Minimum Scale
    RowVal[14] = new StringBuffer("0"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: FLOAT
     * JDBC  Type: FLOAT
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("FLOAT"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.FLOAT); // JDBC Data type
    RowVal[2]  = new StringBuffer("10"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("FLOAT"); // Locale Type Name
    RowVal[13] = new StringBuffer("-38"); // Minimum Scale
    RowVal[14] = new StringBuffer("38"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
       
    /*
     * MySQL Type: DOUBLE
     * JDBC  Type: DOUBLE
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("DOUBLE"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DOUBLE); // JDBC Data type
    RowVal[2]  = new StringBuffer("17"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("DOUBLE"); // Locale Type Name
    RowVal[13] = new StringBuffer("-308"); // Minimum Scale
    RowVal[14] = new StringBuffer("308"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: DOUBLE PRECISION
     * JDBC  Type: DOUBLE
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("DOUBLE PRECISION"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DOUBLE); // JDBC Data type
    RowVal[2]  = new StringBuffer("17"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("DOUBLE PRECISION"); // Locale Type Name
    RowVal[13] = new StringBuffer("-308"); // Minimum Scale
    RowVal[14] = new StringBuffer("308"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: REAL (does not map to Types.REAL)
     * JDBC  Type: DOUBLE
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("REAL"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DOUBLE); // JDBC Data type
    RowVal[2]  = new StringBuffer("17"); // Precision
    RowVal[3]  = new StringBuffer(""); // Literal Prefix
    RowVal[4]  = new StringBuffer(""); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M,D)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("true"); // Auto Increment
    RowVal[12] = new StringBuffer("REAL"); // Locale Type Name
    RowVal[13] = new StringBuffer("-308"); // Minimum Scale
    RowVal[14] = new StringBuffer("308"); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
     
    /*
     * MySQL Type: DATE
     * JDBC  Type: DATE
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("VARCHAR"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DATE); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("VARCHAR"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    
    //    DATE            =  91;
    
    /*
     * MySQL Type: DATE
     * JDBC  Type: DATE
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("DATE"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DATE); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("DATE"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
      
    /*
     * MySQL Type: TIME
     * JDBC  Type: TIME
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("TIME"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.DATE); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer(""); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("TIME"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
     
    /*
     * MySQL Type: DATETIME
     * JDBC  Type: TIMESTAMP
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("DATETIME"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.TIMESTAMP); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("DATETIME"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);
    
    /*
     * MySQL Type: TIMESTAMP
     * JDBC  Type: TIMESTAMP
     */
     
    RowVal = new StringBuffer[18];
    RowVal[0]  = new StringBuffer("TIMESTAMP"); 
    RowVal[1]  = new StringBuffer(java.sql.Types.TIMESTAMP); // JDBC Data type
    RowVal[2]  = new StringBuffer("0"); // Precision
    RowVal[3]  = new StringBuffer("'"); // Literal Prefix
    RowVal[4]  = new StringBuffer("'"); // Literal Suffix
    RowVal[5]  = new StringBuffer("(M)"); // Create Params
    RowVal[6]  = new StringBuffer(java.sql.DatabaseMetaData.typeNullable); // Nullable
    RowVal[7]  = new StringBuffer("false"); // Case Sensitive
    RowVal[8]  = new StringBuffer(java.sql.DatabaseMetaData.typeSearchable); // Searchable
    RowVal[9]  = new StringBuffer("false"); // Unsignable
    RowVal[10] = new StringBuffer("false"); // Fixed Prec Scale
    RowVal[11] = new StringBuffer("false"); // Auto Increment
    RowVal[12] = new StringBuffer("TIMESTAMP"); // Locale Type Name
    RowVal[13] = new StringBuffer(""); // Minimum Scale
    RowVal[14] = new StringBuffer(""); // Maximum Scale
    RowVal[15] = new StringBuffer(""); // SQL Data Type (not used)
    RowVal[16] = new StringBuffer(""); // SQL DATETIME SUB (not used)
    RowVal[17] = new StringBuffer("10"); //  NUM_PREC_RADIX (2 or 10) 
    
    Tuples.addElement(RowVal);

       
    ResultSet RS = new ResultSet(Fields, Tuples);
    return RS;  
  }

  /**
   * Get a description of a table's indices and statistics. They are
   * ordered by NON_UNIQUE, TYPE, INDEX_NAME, and ORDINAL_POSITION.
   *
   * <P>Each index column description has the following columns:
   *  <OL>
   *    <LI><B>TABLE_CAT</B> String => table catalog (may be null)
   *    <LI><B>TABLE_SCHEM</B> String => table schema (may be null)
   *    <LI><B>TABLE_NAME</B> String => table name
   *    <LI><B>NON_UNIQUE</B> boolean => Can index values be non-unique?
   *      false when TYPE is tableIndexStatistic
   *    <LI><B>INDEX_QUALIFIER</B> String => index catalog (may be null);
   *      null when TYPE is tableIndexStatistic
   *    <LI><B>INDEX_NAME</B> String => index name; null when TYPE is
   *      tableIndexStatistic
   *    <LI><B>TYPE</B> short => index type:
   *      <UL>
   *      <LI> tableIndexStatistic - this identifies table statistics that are
   *           returned in conjuction with a table's index descriptions
   *      <LI> tableIndexClustered - this is a clustered index
   *      <LI> tableIndexHashed - this is a hashed index
   *      <LI> tableIndexOther - this is some other style of index
   *      </UL>
   *    <LI><B>ORDINAL_POSITION</B> short => column sequence number
   *      within index; zero when TYPE is tableIndexStatistic
   *    <LI><B>COLUMN_NAME</B> String => column name; null when TYPE is
   *      tableIndexStatistic
   *    <LI><B>ASC_OR_DESC</B> String => column sort sequence, "A" => ascending,
   *      "D" => descending, may be null if sort sequence is not supported;
   *      null when TYPE is tableIndexStatistic
   *    <LI><B>CARDINALITY</B> int => When TYPE is tableIndexStatisic then
   *      this is the number of rows in the table; otherwise it is the
   *      number of unique values in the index.
   *    <LI><B>PAGES</B> int => When TYPE is  tableIndexStatisic then
   *      this is the number of pages used for the table, otherwise it
   *      is the number of pages used for the current index.
   *    <LI><B>FILTER_CONDITION</B> String => Filter condition, if any.
   *      (may be null)
   *  </OL>
   *
   * @param catalog a catalog name; "" retrieves those without a catalog
   * @param schema a schema name pattern; "" retrieves those without a schema
   * @param table a table name
   * @param unique when true, return only indices for unique values;
   *     when false, return indices regardless of whether unique or not
   * @param approximate when true, result is allowed to reflect approximate
   *     or out of data values; when false, results are requested to be
   *     accurate
   * @return ResultSet each row is an index column description
   */

  public java.sql.ResultSet getIndexInfo(String Catalog, String Schema, String Table,
                        boolean unique, boolean approximate)
    throws java.sql.SQLException 
  {
    /*
     * MySQL stores index information in the following fields:
     *
     * Table
     * Non_unique 
     * Key_name
     * Seq_in_index
     * Column_name
     * Collation
     * Cardinality
     * Sub_part
     */
     
    String DB_Sub = "";

    if (Catalog != null) {
      if (!Catalog.equals("")) {
        DB_Sub = " FROM " + Catalog;
      }
    }
 
    java.sql.ResultSet RS = Conn.execSQL(
                            "SHOW INDEX FROM " + 
                            Table + DB_Sub);
    
    Field[] Fields = new Field[13];
    
    Fields[0]  = new Field("", "TABLE_CAT",           Types.CHAR,     1);
    Fields[1]  = new Field("", "TABLE_SCHEM",         Types.CHAR,     1);
    Fields[2]  = new Field("", "TABLE_NAME",          Types.CHAR,     1);
    Fields[3]  = new Field("", "NON_UNIQUE",          Types.CHAR,     1);
    Fields[4]  = new Field("", "INDEX_QUALIFIER",     Types.CHAR,     1);
    Fields[5]  = new Field("", "INDEX_NAME",          Types.CHAR,     1);
    Fields[6]  = new Field("", "TYPE",                Types.CHAR,     1); 
    Fields[7]  = new Field("", "ORDINAL_POSITION",    Types.SMALLINT, 1);
    Fields[8]  = new Field("", "COLUMN_NAME",         Types.CHAR,     1);
    Fields[9]  = new Field("", "ASC_OR_DESC",         Types.CHAR,     1);
    Fields[10] = new Field("", "CARDINALITY",         Types.CHAR,     1);
    Fields[11] = new Field("", "PAGES",               Types.INTEGER,  1);
    Fields[12] = new Field("", "FILTER_CONDITION",    Types.CHAR,     1);
    
    Vector Tuples = new Vector();
      
    while (RS.next()) {
        StringBuffer[] Tuple = new StringBuffer[14];
       
        Tuple[0]  = new StringBuffer((Catalog == null ? "" : Catalog));
        Tuple[1]  = new StringBuffer("");
        Tuple[2]  = new StringBuffer(RS.getString("Table"));
        Tuple[3]  = new StringBuffer((RS.getInt("Non_unique") != 0 ? "true" : "false")); 
        Tuple[4]  = new StringBuffer("");
        Tuple[5]  = new StringBuffer(RS.getString("Key_name"));
        Tuple[6]  = new StringBuffer( Integer.toString(java.sql.DatabaseMetaData.tableIndexOther) );
        Tuple[7]  = new StringBuffer(RS.getString("Seq_in_index"));
        Tuple[8]  = new StringBuffer(RS.getString("Column_name"));
        Tuple[9]  = new StringBuffer(RS.getString("Collation"));
        Tuple[10] = new StringBuffer(RS.getString("Cardinality"));
        Tuple[11] = new StringBuffer("");
        Tuple[12] = new StringBuffer("");
        
        Tuples.addElement(Tuple);
    }
    
    ResultSet Results = new ResultSet(Fields, Tuples);
        
    return Results;
  }
}
