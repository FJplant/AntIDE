package com.antsoft.namecard;

import java.io.*;

class Asc2Uni {
	public static String convert ( String str )
	throws UnsupportedEncodingException
	{
		if ( str == null )
			return null;

		return new String ( str.getBytes("8859_1") , "KSC5601" );
	}
}
