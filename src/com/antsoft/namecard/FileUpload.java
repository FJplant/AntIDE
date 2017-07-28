package com.antsoft.namecard;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 *  FileUpload
 */
public class FileUpload {
	private String fname = "";
	private String boundary;

	String param;
	DataInputStream in;

	/**
	 *  FileUpload  - constructor
	 *
	 *  @param instream  InputStream
	 */
	public FileUpload( InputStream instream  ) throws CommandException {
		try {
			this.in = new DataInputStream( instream );
			boundary = in.readLine();
		} catch( IOException e ) {
			in = null;
			throw new CommandException( " fail to create FileUpload " + e.toString() );
		}
	}


	/** 
	 *  getDelimeter 
	 */
	public String getDelimeter() {
		return boundary;
	}

	/**
	 *  getDelimeter
	 *
	 *  @param  instream  InputStream
	 */
	public String getDelimeter( InputStream instream ) throws CommandException{
		try {
			this.in = new DataInputStream( instream );
			boundary = in.readLine();
		} catch( IOException e ) {
			in = null;
			throw new CommandException( "fail to getDelimeter" + e.toString() );
		}

		return boundary;
	}

	/** 
	 *  getParameter - parameter parsing을 위한 method
	 *
	 *  @param  name  parameter name
	 *  @return  parameter name
	 */
	public String getParameter( String name ) throws CommandException{
		String str = null;
		try {
			while( ( str = in.readLine() ) != null ) {
				if( str.indexOf( "name=" ) != -1 ) {
					int nS = str.indexOf( "name=" );
					int nE = str.indexOf( "\"", nS + 6 );
					param = str.substring( nS + 6 , nE );
	
					if( param.equals( name ) ) {
						str = in.readLine();
						str = in.readLine();
						return str;
					}
				}
			}
		} catch( IOException e ) {
			throw new CommandException( "fail to getParameter" + e.toString() );
		}
	
		return null;
	}

	/**
	 *  getParameter
	 *
	 *  @param  out  OutputStream
	 *  @param  name  parameter name
	 */
	public boolean getParameter( OutputStream out, String name ) throws CommandException {
		String str = null;

		try {
			while( ( str = in.readLine() ) != null ) {
				if( str.indexOf( "name=" ) != -1 ) {
					int nS = str.indexOf( "name=" );
					int nE = str.indexOf( "\"", nS + 6 );
					param = str.substring( nS + 6, nE );
					if( param.equals( name ) ) {
						str = in.readLine();
						if( readParameter( out ) )
							return true;
					}
				}
			} 
		} catch( IOException e ) {
			throw new CommandException( "fail to getParameter" + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to getParmater" + e.toString() );
		}
		return false;
	}

	/**
	 *  getFileName  - filename 을 parsing하는 method
	 *
	 */
	public String getFileName() throws CommandException {
		String str;
		int nS;
		int nE;

		try {
			while( ( str = in.readLine() ) != null ) {
				if( str.indexOf( "filename=\"\"" ) != -1 ) {
					str = in.readLine();
					return fname;
				}
				if( str.indexOf( "filename=" ) != -1 ) {
					nS = str.indexOf( "filename=" );
					nE = str.indexOf( "\"", nS + 10 );
					fname = str.substring( nS + 10, nE );
					if( fname.lastIndexOf( "\\" ) != -1 ) {
						fname = fname.substring( fname.lastIndexOf( "\\" ) + 1 );
						return fname;
					}
				}
			}
		} catch( IOException e ) {
			throw new CommandException( "fail to getParmater" + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to getParmater" + e.toString() );
		}
		return null;
	}

	/**
	 *  upFile
	 *
	 *  @param  out  OutputStream
	 *  @return 
	 */
	public boolean upFile( OutputStream out ) throws CommandException {
		String str;
		try {
			while( ( str = in.readLine() ) != null ) {
				if( str.indexOf( "Content-Type" ) != -1 ) {
					str = in.readLine();
					if( readParameter( out ) ) return true;
				}
			}
		} catch( IOException e ) {
			throw new CommandException( "fail to getParmater" + e.toString() );
		} catch( Exception e ) {
			throw new CommandException( "fail to getParmater" + e.toString() );
		}	
		return false;
	}

	/**
	 *  readParameter
	 *
	 *  @param  out  OutputStream
	 */
	public boolean readParameter( OutputStream os ) throws IOException {
		byte[] buffer = new byte[1024];
		byte[] tbuffer = new byte[boundary.length() + 1 ];
		byte tm ;
		int x = 0;

		for( ; ; ) {
			buffer[x++] = tm = in.readByte();
			if( x == boundary.length() + 1 ) {
				int y = 0;
				String temp = new String( buffer, 0, x );
				if( ( y = temp.indexOf( boundary) ) != -1 ) {
					x = y;
					if( x != 0 ) os.write( buffer, 0, x-1 );
					return true;
				} else {} 
			} else {
				if( ( x == 1023 ) || ( tm == '\n' ) ) {
					os.write( buffer, 0, x );
					x = 0;
				}
			}
		}

	}

}
