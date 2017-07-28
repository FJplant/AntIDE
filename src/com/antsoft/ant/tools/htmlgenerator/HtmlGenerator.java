/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/tools/htmlgenerator/HtmlGenerator.java,v 1.3 1999/07/22 03:09:23 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */

/*
 *	Author : Andrei Cioroianu
 *  Author : Kim, Sung-Hoon
 */
package com.antsoft.ant.tools.htmlgenerator;

import java.io.*;
import java.util.*;

public class HtmlGenerator
{
	private static final String keywords[] = 
	{
		"abstract",	"default",	"if",			"private",		"throw",
		"boolean",	"do",		"implements",	"protected",	"throws",
		"break",	"double",	"import",		"public",		"transient",
		"byte",		"else",		"instanceof",	"return",		"try",
		"case",		"extends",	"int",			"short",		"void",
		"catch",	"final",	"interface",	"static",		"volatile",
		"char",		"finally",	"long",			"super",		"while",
		"class",	"float",	"native",		"switch",
		"const",	"for",		"new",			"synchronized",
		"continue",	"goto",		"package",		"this"
	};
	private static Vector keyw = new Vector(keywords.length);
	static
	{
		for (int i = 0; i < keywords.length; i++)
			keyw.addElement(keywords[i]);
	}
	private static final int tabsize = 4;

	private static String bgcolor = "C0C0C0";
	private static String txcolor = "000000";
	private static String kwcolor = "0000F0";
	private static String cmcolor = "A00000";
	private static String licolor = "FF0000";	// kahn

	private static void convert(String source,String dest) throws IOException
	{
		FileReader in = new FileReader(source);
		FileWriter out = new FileWriter(dest);

		out.write("<html>\r\n<head>\r\n<title>");
		out.write(source);
		out.write("</title>\r\n</head>\r\n<body ");
		out.write("bgcolor=\"" + bgcolor +"\" ");
		out.write("text=\"" + txcolor +"\">\r\n");
		out.write("<pre>\r\n");

		StringBuffer buf = new StringBuffer(2048);
		int c = 0, kwl = 0, bufl = 0;
		char ch = 0, lastch;
		int s_normal  = 0;
		int s_string  = 1;
		int s_char    = 2;
		int s_comline = 3;
		int s_comment = 4;
		int s_constant= 5;
		int state = s_normal;

		while (c != -1)
		{
			c = in.read();
			lastch = ch;
			ch = c >= 0 ? (char) c : 0;
			if (state == s_normal)
				if (kwl == 0 && Character.isJavaIdentifierStart(ch) 
							 && !Character.isJavaIdentifierPart(lastch)
					|| kwl > 0 && Character.isJavaIdentifierPart(ch))
				{
					buf.append(ch);
					bufl++;
					kwl++;
					continue;
				} else
					if (kwl > 0)
					{
						String kw = buf.toString().substring(buf.length() - kwl);
						if (keyw.contains(kw))
						{
							buf.insert(buf.length() - kwl, 
								"<font color=\"" + kwcolor + "\">");
							buf.append("</font>");
						}
						kwl = 0;
					}
			switch (ch)
			{
				case '&':
					buf.append("&amp;");
					bufl++;
					break;
				case '\"':			// constant(literal) ===> coloring...	kahn
					//buf.append("&quot;");
					//bufl++;
					//if (state == s_normal)
						//state = s_string;
					//else
						//if (state == s_string && lastch != '\\')
							//state = s_normal;

					// kahn
					if (state == s_normal) {
						state = s_string;
						buf.append("<font color=\""+licolor+"\">");
						buf.append("&quot;");
						bufl++;
					}
					else
						if (state == s_string && lastch != '\\') {
							state = s_normal;
							buf.append("&quot;");
							bufl++;
							buf.append("</font>");
						}
					// kahn
					break;
				case '\'':
					//buf.append("\'");
					//bufl++;
					//if (state == s_normal)
						//state = s_char;
					//else
						//if (state == s_char && lastch != '\\')
							//state = s_normal;

					if (state == s_normal) {
						state = s_char;
						buf.append("<font color=\""+licolor+"\">");
						buf.append("\'");
						bufl++;
					}
					else
						if (state == s_char && lastch != '\\') {
							state = s_normal;
							buf.append("\'");
							bufl++;
							buf.append("</font>");
						}
					break;
				case '\\':
					buf.append("\\");
					bufl++;
					if (lastch == '\\' && (state == s_string || state == s_char))
						lastch = 0;
					break;
				case '/':
					buf.append("/");
					bufl++;
					if (state == s_comment && lastch == '*')
					{
						buf.append("</font>");
						state = s_normal;
					}
					if (state == s_normal && lastch == '/')
					{
						buf.insert(buf.length() - 2,
							"<font color=\"" + cmcolor + "\">");
						state = s_comline;
					}
					break;
				case '*':
					buf.append("*");
					bufl++;
					if (state == s_normal && lastch == '/')
					{
						buf.insert(buf.length() - 2, 
							"<font color=\"" + cmcolor + "\">");
						state = s_comment;
					}
					break;
				case '<':
					buf.append("&lt;");
					bufl++;
					break;
				case '>':
					buf.append("&gt;");
					bufl++;
					break;
				case '\t':
					int n = bufl / tabsize * tabsize + tabsize;
					while (bufl < n)
					{
						buf.append(' ');
						bufl++;
					}
					break;
				case '\r':
				case '\n':
					if (state == s_comline)
					{
						buf.append("</font>");
						state = s_normal;
					}
					buf.append(ch);
					if (buf.length() >= 1024)
					{
						out.write(buf.toString());
						buf.setLength(0);
					}
					bufl = 0;
					if (kwl != 0)
						kwl = 0; // This should never execute
					if (state != s_normal && state != s_comment)
						state = s_normal; // Sintax Error
					break;
					/*
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					if (state == s_normal) {
						buf.append("<font color=\""+licolor+"\">");
						bufl++;
						buf.append(ch);
						state = s_constant;
					}
					else 
						if (state == s_constant) {
							bufl++;
							buf.append(ch);
						}
					break;
				case '.':
				*/

				case 0:
					if (c < 0)
					{
						if (state == s_comline)
						{
							buf.append("</font>");
							state = s_normal;
						}
						out.write(buf.toString());
						buf.setLength(0);
						bufl = 0;
						if (state == s_comment)
						{
							// Sintax Error
							buf.append("</font>");
							state = s_normal;
						}
						break;
					}
				default:
					if (state == s_constant) {
						buf.append("</font>");
					}
					bufl++;
					buf.append(ch);
			}
		}
		out.write("</pre>\r\n</body>\r\n</html>");
		in.close();
		out.close();
	}

	public static void generate(String src,String dest,String b,String k,String c,String l,String t)
	{
		if (src==null) return;

		File f = new File(src);
		if (f.isDirectory()) return;

		if (b!=null) bgcolor = "#"+b;
		if (t!=null) txcolor = "#"+t;
		if (k!=null) kwcolor = "#"+k;
		if (c!=null) cmcolor = "#"+c;
		if (l!=null) licolor = "#"+l;

		try {
			convert(src,dest);
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}
}
