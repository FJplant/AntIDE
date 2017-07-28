/*
 * JavaScanner.java
 * Title: 개미
 * Part : Java scanner(lexical analyzer in parser - batch version)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *   - nextToken() constructed.
 *
 * Version 1.1.0 (completed)
 *   - keywordsORindentifier(using Hashtable) added.
 */

package ant.codecontext.parser;

import java.io.*;
import java.util.*;
//import java_cup.runtime.*;

/**
 * JavaScanner(lexical analyzer).
 * when the file is opened and the parse tree of the input file yet does not
 * exist, java Parser will request token stream sequentially to java Scanner.
 * Then, java Scanner will return to the Parser token in turns.
 *
 * @author : kahn(kim, sung-hoon)
 */
public class JavaScanner {
	public static String filename=null;
	private static File f=null;
	private static Reader in=null;
	private static boolean ungetflag=false;
	private static int ungetchar;

	/** token string of the current token */
	public static String currenttoken;

	/** Constructor */
	public JavaScanner() {}

	public static void initialize() {
		try {
			init();
			f=new File(filename);
			in=new FileReader(f);
			currenttoken=new String();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	// method getC()
	// It reads one character, and return it.
	// 후에 ungetC()가 호출되면서 한 character뒤로 이동을 위해 mark()를 한다.
	private static int getC() {
		try {
			if (ungetflag) ungetflag=false;
			else ungetchar=in.read();
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			return ungetchar;
		}
	}

	// method ungetC()
	// 읽었던 한 character를 읽지 않은것으로 만든다.
	private static void ungetC() {
		ungetflag=true;
	}

	/**
 	 * read the next token.
 	 * get next token, currenttoken is assigned and return token type.
 	 * @return token type of the current token(using field of class TokenType)
 	 * @see ant.parser10.sym class
 	 */
	public static Symbol nextToken() {
		int ch;
		StringBuffer buf=new StringBuffer();

		ch=getC();

		// Skip the white space(blank,\n,\t,\b,\f,\r)
		while (Character.isWhitespace((char)ch)) ch=getC();

		if (ch==-1) return new Symbol(sym.EOF);    // end of the file

		// comment or divide operator
		if (ch=='/') {
  			ch=getC();
  			switch (ch) {
    			case '*' :
      				if (commentProcessing()==-1) return new Symbol(sym.EOF);
      				return nextToken();
				case '/' :
					do { ch=getC(); } while (ch!='\n');
					return nextToken();
				case '=' : return new Symbol(sym.ADIV);
				default : ungetC(); return new Symbol(sym.DIV);
			}
		}

		// identifier or keywords...( letter(letter|digit)* )
		if (Character.isJavaIdentifierStart((char)ch)) {
			return wordProcessing(ch);
		}

		// number(literal)
		if (Character.isDigit((char)ch)||ch=='.') {
			return numberProcessing(ch);
		}

		// string literal
		if (ch=='\"') {
			ch=getC();
			while (ch!='\"') {
				buf.append((char)ch);
				ch=getC();
			}
			currenttoken=buf.toString();
			return new Symbol(sym.SLITERAL,currenttoken);
		}

		// character literal
		if (ch=='\'') {
			ch=getC();
			buf.append((char)ch);
			currenttoken=buf.toString();
			return new Symbol(sym.CLITERAL,currenttoken);
		}
	
		switch (ch) {
			case '(' : return new Symbol(sym.LP);
			case ')' : return new Symbol(sym.RP);
			case '[' : return new Symbol(sym.LS);
			case ']' : return new Symbol(sym.RS);
			case '{' : return new Symbol(sym.LB);
			case '}' : return new Symbol(sym.RB);
			case ';' : return new Symbol(sym.SEMIC);
			case ':' : return new Symbol(sym.COLON);
			case ',' : return new Symbol(sym.COMMA);
			case '~' : return new Symbol(sym.BWN);
			case '?' : return new Symbol(sym.QM);
			case '+' :
				ch=getC();
				if (ch=='+') return new Symbol(sym.DP);
				if (ch=='=') return new Symbol(sym.AADD);
				ungetC();
				return new Symbol(sym.ADD);
			case '-' :
				ch=getC();
				if (ch=='-') return new Symbol(sym.DM);
				if (ch=='=') return new Symbol(sym.ASUB);
				ungetC();
				return new Symbol(sym.SUB);
			case '*' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.AMUL);
				ungetC();
				return new Symbol(sym.MUL);
			case '%' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.AMOD);
				ungetC();
				return new Symbol(sym.MOD);
			case '<' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.LE);
				if (ch=='<') {
					ch=getC();
					if (ch=='=') return new Symbol(sym.ASL);
					ungetC();
					return new Symbol(sym.SL);
				}
				ungetC();
				return new Symbol(sym.LT);
			case '>' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.GE);
				if (ch=='>') {
					ch=getC();
					if (ch=='>') {
						ch=getC();
						if (ch=='=') return new Symbol(sym.AUSR);
						ungetC();
						return new Symbol(sym.USR);
					}
					ungetC();
					return new Symbol(sym.SR);
				}
				ungetC();
				return new Symbol(sym.GT);
			case '&' :
				ch=getC();
				if (ch=='&') return new Symbol(sym.LA);
				if (ch=='=') return new Symbol(sym.ABWA);
				ungetC();
				return new Symbol(sym.BWA);
			case '|' :
				ch=getC();
				if (ch=='|') return new Symbol(sym.LO);
				if (ch=='=') return new Symbol(sym.ABWO);
				ungetC();
				return new Symbol(sym.BWO);
			case '^' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.ABWN);
				ungetC();
				return new Symbol(sym.BWEO);
			case '!' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.NE);
				ungetC();
				return new Symbol(sym.LN);
			case '=' :
				ch=getC();
				if (ch=='=') return new Symbol(sym.EQ);
				ungetC();
				return new Symbol(sym.ASSIGN);
		}
	
		return new Symbol(sym.error);
	}

	private static Symbol wordProcessing (int c) {
		StringBuffer buf=new StringBuffer();
		do {
			buf.append((char)c);           // token is divided.
			c=getC();
		} while (Character.isJavaIdentifierPart((char)c));
		ungetC();
		currenttoken=buf.toString();
	
		return keywordsORidentifier(currenttoken);
	}

	private static Symbol numberProcessing(int c) {
		StringBuffer buf=new StringBuffer();
		if (c==0) {
  			c=getC();
  			if (c=='x'||c=='X') buf.append('0');
  			do {
    			buf.append((char)c);
    			c=getC();
  			} while (Character.isDigit((char)c));
			if (c=='l'||c=='L') buf.append('L');
			else ungetC();
			currenttoken=buf.toString();
			return new Symbol(sym.ILITERAL,currenttoken);
		}
		while (Character.isDigit((char)c)) {
  			buf.append((char)c);
  			c=getC();
		}
		if (c=='.') {
			c=getC();
			if (c!='E'&&c!='e'&&!Character.isDigit((char)c)) {
				ungetC();
				return new Symbol(sym.DOT);
			}
			ungetC();
			c='.';
  			do {
    			buf.append((char)c);
    			c=getC();
  			} while (Character.isDigit((char)c));
			if (c=='E'||c=='e') {
				do {
					buf.append((char)c);
					c=getC();
				} while (Character.isDigit((char)c));
			}
			ungetC();
			currenttoken=buf.toString();
			return new Symbol(sym.RLITERAL,currenttoken);
		}
		else {
			ungetC();
			currenttoken=buf.toString();
			return new Symbol(sym.ILITERAL,currenttoken);
		}
	}

	private static int commentProcessing() {
		int ch;
	
		ch=getC();
		while (ch=='/') ch=getC();
		while (ch!='/') {
  			while (ch!='*') {
    			ch=getC();
    			if (ch==-1) return -1;
  			}
  			ch=getC();
  			if (ch==-1) return -1;
		}
		return 1;
	}

	static Hashtable ht=new Hashtable();

	private static void init() {
		try {
			ht.put("abstract",new Integer(sym.ABSTRACT));
			ht.put("boolean",new Integer(sym.BOOLEAN));
			ht.put("break",new Integer(sym.BREAK));
			ht.put("byte",new Integer(sym.BYTE));
			ht.put("case",new Integer(sym.CASE));
			ht.put("catch",new Integer(sym.CATCH));
			ht.put("char",new Integer(sym.CHAR));
			ht.put("class",new Integer(sym.CLASS));
			//ht.put("const",new Integer(sym.CONST));
			ht.put("continue",new Integer(sym.CONTINUE));
			ht.put("default",new Integer(sym.DEFAULT));
			ht.put("do",new Integer(sym.DO));
			ht.put("double",new Integer(sym.DOUBLE));
			ht.put("else",new Integer(sym.ELSE));
			ht.put("extends",new Integer(sym.EXTENDS));
			ht.put("false",new Integer(sym.FALSE));
			ht.put("final",new Integer(sym.FINAL));
			ht.put("finally",new Integer(sym.FINALLY));
			ht.put("float",new Integer(sym.FLOAT));
			ht.put("for",new Integer(sym.FOR));
			//ht.put("goto",new Integer(sym.GOTO));
			ht.put("if",new Integer(sym.IF));
			ht.put("implements",new Integer(sym.IMPLEMENTS));
			ht.put("import",new Integer(sym.IMPORT));
			ht.put("instanceof",new Integer(sym.INSTANCEOF));
			ht.put("int",new Integer(sym.INT));
			ht.put("interface",new Integer(sym.INTERFACE));
			ht.put("long",new Integer(sym.LONG));
			ht.put("native",new Integer(sym.NATIVE));
			ht.put("new",new Integer(sym.NEW));
			ht.put("null",new Integer(sym.NULL));
			ht.put("package",new Integer(sym.PACKAGE));
			ht.put("private",new Integer(sym.PRIVATE));
			ht.put("protected",new Integer(sym.PROTECTED));
			ht.put("public",new Integer(sym.PUBLIC));
			ht.put("return",new Integer(sym.RETURN));
			ht.put("short",new Integer(sym.SHORT));
			ht.put("static",new Integer(sym.STATIC));
			ht.put("super",new Integer(sym.SUPER));
			ht.put("synchronized",new Integer(sym.SYNCHRONIZED));
			ht.put("switch",new Integer(sym.SWITCH));
			ht.put("this",new Integer(sym.THIS));
			ht.put("throw",new Integer(sym.THROW));
			ht.put("throws",new Integer(sym.THROWS));
			ht.put("transient",new Integer(sym.TRANSIENT));
			ht.put("true",new Integer(sym.TRUE));
			ht.put("try",new Integer(sym.TRY));
			ht.put("void",new Integer(sym.VOID));
			ht.put("volatile",new Integer(sym.VOLATILE));
			ht.put("while",new Integer(sym.WHILE));
		} catch (NullPointerException e) {
			System.err.println(e.toString());
		}
	}

	private static Symbol keywordsORidentifier(String s) {
		Integer value=(Integer)ht.get(s);

		if (value==null) return new Symbol(sym.ID,s);
		else return new Symbol(value.intValue(),s);
	}

	protected void finalize() throws Throwable {
		in.close();
		in=null;
		f=null;
		super.finalize();
	}
}
