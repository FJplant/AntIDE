package com.antsoft.namecard;

import java.io.*;

class Uni2Asc {
	public static String convert ( String str )
	throws UnsupportedEncodingException
	{
		if ( str == null )
			return null;

		return new String ( str.getBytes("KSC5601") , "8859_1" );
	}
}
