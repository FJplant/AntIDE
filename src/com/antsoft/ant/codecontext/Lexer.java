/*
 * $Id: Lexer.java,v 1.10 1999/08/26 03:10:42 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.10 $
 * Part : Line Tokenizer in CodeContext.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 99.2.3.
 */
package com.antsoft.ant.codecontext;

import java.io.*;
import java.util.*;
import javax.swing.text.*;

import com.antsoft.ant.util.DocumentReader;

/**
 * @author : kahn(Kim, Sung-Hoon)
 */
public class Lexer {
	private static int ungetchar;
  private static String value;

	private static boolean ungetflag = false;
  private static boolean doubleUnget = false;

  private static int doubleUngetChar;
  private static int lineCount;

  public static boolean newLine = false;

  // input stream.
  //private static StringInputStream in = null;
  private static Reader reader = null;

	public Lexer(Reader reader) {
		this.reader = reader;
		lineCount = 0;
		newLine = false;
	}
	
	public Lexer(InputStream ins) {
    this( new BufferedReader( new InputStreamReader(ins) ) );
  }

	public Lexer(Document doc) {
    lineCount = 0;
    newLine = false;
    setReader(doc);
  }

  public Lexer(String str) {
  	setReader(str);
  }

  public Lexer(File file) {
    lineCount = 0;
    newLine = false;
    try {
      //in = new StringInputStream(new FileInputStream(file));
      BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      setReader(bufferReader);
      //in = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
    } catch (IOException e) {
      System.out.println(e.toString());
    }
  }

	// method getC()
	// It reads one character, and return it.
	private static int getC() {
		if (ungetflag) ungetflag = false;
		else {
			try {
	      ungetchar = reader.read();
	      if (ungetchar == '\n') lineCount++;
	    } catch (IOException e ) {
	    }
		}
		return ungetchar;
	}

	// method ungetC()
	// 마지막으로 읽었던 한 character를 읽지 않은것으로 만든다.
	private static void ungetC() {
    ungetflag=true;
  }

	/**
 	  read the next token.
 	  get next token, currenttoken is assigned and return token type.

 	  @return token type of the current token(using field of class TokenType)
 	  */
	public static int nextToken() {
//    System.out.println(" next token .....");
		if ( reader == null )
			return Sym.EOF;

    value = null;
		int ch;
		StringBuffer buf=new StringBuffer();

		ch=getC();

		// Skip the white space(blank,\n,\t,\b,\f,\r)
		while (Character.isWhitespace((char)ch)) {
      if (ch == '\n') if (newLine) return Sym.EOL;
      ch=getC();
    }

		if (ch==-1) {
			try {
				reader.close();
			} catch ( IOException e ) {
			} finally {
				reader = null;
			}
			return Sym.EOF;    // end of the file
		}
		
		// comment or divide operator
		if (ch=='/') {
      ch=getC();
      switch (ch) {
        case '*' :
          ch=getC();
          if (processComment(ch)==-1) return Sym.EOF;
          //return Sym.COMMENT;
          return nextToken();
				case '/' :
					do { ch=getC(); } while (ch!='\n'&&ch!=-1);
					if (ch == -1) return Sym.EOF;
          else return nextToken();
				case '=' : value = "/="; return Sym.ADIV;
				default : ungetC(); value = "/"; return Sym.DIV;
			}
		}

		// identifier or keywords...( letter(letter|digit)* )
		if (Character.isJavaIdentifierStart((char)ch)) {
			return processWord(ch);
		}

    	// number(literal)
		if (Character.isDigit((char)ch)) {//||ch=='.') {
			return numberProcessing(ch);
		}

		// string literal
		if (ch=='\"') {
			ch=getC();
			while (ch!='\"') {
				if (ch==-1) {
					return Sym.SLITERAL;
				}
        if (ch=='\\') {
          ch = getC();
        }
				ch=getC();
			}
			return Sym.SLITERAL;
		}

		// character literal
		if (ch=='\'') {
			ch=getC();
      if (ch == '\\') {
				buf.append((char)ch);
      	ch = getC();
				buf.append((char)ch);
      }
      ch = getC();

			return Sym.CLITERAL;
		}

		switch (ch) {
      case '.' :  value = "."; return Sym.DOT;
			case '(' :  value = "("; return Sym.LP;
			case ')' :  value = ")"; return Sym.RP;
			case '[' :  value = "["; return Sym.LS;
			case ']' :  value = "]"; return Sym.RS;
			case '{' :  value = "{"; return Sym.LB;
			case '}' :  value = "}"; return Sym.RB;
			case ';' :  value = ";"; return Sym.SEMIC;
			case ':' :  value = ":"; return Sym.COLON;
			case ',' :  value = ","; return Sym.COMMA;
			case '~' :  value = "="; return Sym.BWN;
			case '?' :  value = "?"; return Sym.QM;
			case '+' :
				ch=getC();
				if (ch=='+') { value = "++"; return Sym.DP;}
				if (ch=='=') { value = "+="; return Sym.AADD;}
				ungetC();
        value = "+";
        return Sym.ADD;
			case '-' :
				ch=getC();
				if (ch=='-') { value = "--"; return Sym.DM;}
				if (ch=='=') { value = "-="; return Sym.ASUB;}
        value = "-";
				ungetC();
        return Sym.SUB;
			case '*' :
				ch=getC();
				if (ch=='=') { value = "*="; return Sym.AMUL;}
				ungetC();
        value = "*";
				return Sym.MUL;
			case '%' :
				ch=getC();
				if (ch=='=') { value = "%="; return Sym.AMOD;}
				ungetC();
        value = "%";
				return Sym.MOD;
			case '<' :
				ch=getC();
				if (ch=='=') { value = "<="; return Sym.LE;}
				if (ch=='<') {
					ch=getC();
					if (ch=='=') { value = "<<="; return Sym.ASL;}
					ungetC();
          value = "<<";
				  return Sym.SL;
				}
				ungetC();
        value = "<";
	 		  return Sym.LT;
			case '>' :
				ch=getC();
				if (ch=='=') { value = ">="; return Sym.GE;}
				if (ch=='>') {
					ch=getC();
					if (ch=='>') {
						ch=getC();
						if (ch=='=') { value = ">>>="; return Sym.AUSR;}
						ungetC();
            value = ">>>";
            return Sym.USR;
					}
					ungetC();
          value = ">>";
          return Sym.SR;
				}
				ungetC();
        value = ">";
        return Sym.GT;
			case '&' :
				ch=getC();
				if (ch=='&') { value = "&&"; return Sym.LA;}
				if (ch=='=') { value = "&="; return Sym.ABWA;}
				ungetC();
        value = "&";
				 return Sym.BWA;
			case '|' :
				ch=getC();
				if (ch=='|') { value = "||"; return Sym.LO;}
				if (ch=='=') { value = "|="; return Sym.ABWO;}
				ungetC();
        value = "|";
				 return Sym.BWO;
			case '^' :
				ch=getC();
				if (ch=='=') { value = "^="; return Sym.ABWN;}
				ungetC();
        value = "^";
				 return Sym.BWEO;
			case '!' :
				ch=getC();
				if (ch=='=') { value = "!="; return Sym.NE;}
				ungetC();
        value = "!";
				 return Sym.LN;
			case '=' :
				ch=getC();
				if (ch=='=') { value = "=="; return Sym.EQ;}
				ungetC();
        value = "=";
				 return Sym.ASSIGN;
		}

		return Sym.ERROR;
	}

	private static int processWord (int c) {
		StringBuffer buf=new StringBuffer();
		do {
			buf.append((char)c);           // token is divided.
			c=getC();
      //System.out.println(" inner loop ");
		} while (Character.isJavaIdentifierPart((char)c));
		ungetC();

    value = buf.toString();

		return keywordsORidentifier(value);
	}

	private static int numberProcessing(int c) {
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
			return Sym.ILITERAL;
		}
		while (Character.isDigit((char)c)) {
      buf.append((char)c);
      c=getC();
		}
    /*
		if (c=='.') {
			c=getC();
			if (c!='E'&&c!='e'&&!Character.isDigit((char)c)) {
				ungetC();
        return Sym.DOT;
			}
			ungetC();
			c='.';
      do {
        buf.append((char)c);
        c=getC();
      } while (Character.isDigit((char)c));
			if (c=='E'||c=='e') {
        doubleUngetChar = c;
				c=getC();
				if (!Character.isDigit((char)c)) {
          ungetC();
					if (buf.length()==1) {
            doubleUnget = true;
						return Sym.DOT;
					}
					else {
						return Sym.RLITERAL;
					}
				}
				do {
					buf.append((char)c);
					c=getC();
				} while (Character.isDigit((char)c));
			}
			ungetC();
			return Sym.RLITERAL;
		}
		else {
    */
			ungetC();
			return Sym.ILITERAL;
		//}
	}

	private static int processComment(int c) {
		int ch=c;

		//ch=getC();
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

	private static Hashtable ht=new Hashtable();

	static {
		try {
			//ht.put("abstract",new Integer(Sym.ABSTRACT));
			ht.put("abstract",new Integer(Sym.MODIFIER2));
			ht.put("boolean",new Integer(Sym.BOOLEAN));
			ht.put("break",new Integer(Sym.BREAK));
			ht.put("byte",new Integer(Sym.BYTE));
			ht.put("case",new Integer(Sym.CASE));
			ht.put("catch",new Integer(Sym.CATCH));
			ht.put("char",new Integer(Sym.CHAR));
			ht.put("class",new Integer(Sym.CLASS));
			//ht.put("const",new Integer(Sym.CONST));
			ht.put("continue",new Integer(Sym.CONTINUE));
			ht.put("default",new Integer(Sym.DEFAULT));
			ht.put("do",new Integer(Sym.DO));
			ht.put("double",new Integer(Sym.DOUBLE));
			ht.put("else",new Integer(Sym.ELSE));
			ht.put("extends",new Integer(Sym.EXTENDS));
			ht.put("false",new Integer(Sym.FALSE));
			//ht.put("final",new Integer(Sym.FINAL));
			ht.put("final",new Integer(Sym.MODIFIER2));
			ht.put("finally",new Integer(Sym.FINALLY));
			ht.put("float",new Integer(Sym.FLOAT));
			ht.put("for",new Integer(Sym.FOR));
			//ht.put("goto",new Integer(Sym.GOTO));
			ht.put("if",new Integer(Sym.IF));
			ht.put("implements",new Integer(Sym.IMPLEMENTS));
			ht.put("import",new Integer(Sym.IMPORT));
			ht.put("instanceof",new Integer(Sym.INSTANCEOF));
			ht.put("int",new Integer(Sym.INT));
			ht.put("interface",new Integer(Sym.INTERFACE));
			ht.put("long",new Integer(Sym.LONG));
			//ht.put("native",new Integer(Sym.NATIVE));
			ht.put("native",new Integer(Sym.MODIFIER2));
			ht.put("new",new Integer(Sym.NEW));
			ht.put("null",new Integer(Sym.NULL));
			ht.put("package",new Integer(Sym.PACKAGE));
			//ht.put("private",new Integer(Sym.PRIVATE));
			//ht.put("protected",new Integer(Sym.PROTECTED));
			//ht.put("public",new Integer(Sym.PUBLIC));
			ht.put("private",new Integer(Sym.MODIFIER));
			ht.put("protected",new Integer(Sym.MODIFIER));
			ht.put("public",new Integer(Sym.MODIFIER));
			ht.put("return",new Integer(Sym.RETURN));
			ht.put("short",new Integer(Sym.SHORT));
			//ht.put("static",new Integer(Sym.STATIC));
			ht.put("static",new Integer(Sym.MODIFIER2));
			ht.put("super",new Integer(Sym.SUPER));
			//ht.put("synchronized",new Integer(Sym.SYNCHRONIZED));
			ht.put("synchronized",new Integer(Sym.MODIFIER2));
			ht.put("switch",new Integer(Sym.SWITCH));
			ht.put("this",new Integer(Sym.THIS));
			ht.put("throw",new Integer(Sym.THROW));
			ht.put("throws",new Integer(Sym.THROWS));
			//ht.put("transient",new Integer(Sym.TRANSIENT));
			ht.put("transient",new Integer(Sym.MODIFIER2));
			ht.put("true",new Integer(Sym.TRUE));
			ht.put("try",new Integer(Sym.TRY));
			ht.put("void",new Integer(Sym.VOID));
			ht.put("volatile",new Integer(Sym.MODIFIER2));
			//ht.put("volatile",new Integer(Sym.VOLATILE));
			ht.put("while",new Integer(Sym.WHILE));
		} catch (NullPointerException e) {
			System.err.println(e.toString());
		}
	}

	private static int keywordsORidentifier(String s) {
		Integer value=(Integer)ht.get(s);

		if (value==null) return Sym.ID;
		else return value.intValue();
	}

	/**
	  getting the current real token string.

	  @return the token as the String value.
	  */
	public static String getValue() {
		return value;
	}

  public static int getLineCount() {
    return lineCount;
  }

  /**
   */
  public  void setReader(Reader reader) {
  	this.reader = reader;
  }
  
  public  void setReader(Document doc) {
    try {
//    	StringReader reader = new StringReader(doc.getText(0,doc.getLength()));
			DocumentReader reader = new DocumentReader(doc); 
    	setReader(reader);
//    } catch (BadLocationException e) {
    } catch (OutOfMemoryError e ) {
    	System.err.println("Out of memory error:");
    	setReader((Reader)null);
    }
  }
  public void setReader(String str) {
		StringReader reader = new StringReader(str);
		setReader(reader);
	}
}
