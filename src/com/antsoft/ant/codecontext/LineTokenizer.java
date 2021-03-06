/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/LineTokenizer.java,v 1.4 1999/08/20 02:03:51 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 * Part : Line Tokenizer in CodeContext.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 99.2.3.
 */

package com.antsoft.ant.codecontext;

import java.io.*;
import java.util.*;
                      
/**
 * LineTokenizer(lexical analyzer).
 * input : one line string with newline character.
 * output : token stream of the string.
 *
 * @author : kahn(Kim, Sung-Hoon)
 */
public class LineTokenizer {
	private String tokenizingString=null;
	private int ungetchar;
	private boolean ungetflag=false;
	private int index=0;

  protected static boolean inCommentState=false;
  protected static boolean inStringFlag=false;

	/** token string of the current token */
	public String currentToken;

	/**
	  Constructor with String parameter.

	  @param str the String to be tokenized.
	  */
	public LineTokenizer(String str) { tokenizingString=str; }

	// method getC()
	// It reads one character, and return it.
	private int getC() {
		if (ungetflag) ungetflag=false;
		else {
			if (index<tokenizingString.length()) {
				ungetchar=tokenizingString.charAt(index);
				index++;
			}
			else ungetchar=-1;
		}
		return ungetchar;
	}

	// method ungetC()
	// 마지막으로 읽었던 한 character를 읽지 않은것으로 만든다.
	private void ungetC() {
    ungetflag=true;
  }

	/**
 	  read the next token.
 	  get next token, currenttoken is assigned and return token type.

 	  @return token type of the current token(using field of class TokenType)
 	  */
	protected int nextToken() {
		int ch;
		StringBuffer buf=new StringBuffer();

		ch=getC();

		// Skip the white space(blank,\n,\t,\b,\f,\r)
		while (Character.isWhitespace((char)ch)) ch=getC();

    if (inCommentState) {
        if (processComment(ch)==-1) return Sym.EOL;
        ch=getC();
    }

		if (ch==-1) return Sym.EOL;    // end of the file

		// comment or divide operator
		if (ch=='/') {
      ch=getC();
      switch (ch) {
        case '*' :
          ch=getC();
          if (processComment(ch)==-1) return Sym.EOL;
          return Sym.COMMENT;
				case '/' :
					do { ch=getC(); } while (ch!='\n');
					return Sym.COMMENT;
				case '=' : currentToken="="; return Sym.ADIV;
				default : ungetC(); currentToken="/"; return Sym.DIV;
			}
		}

		// identifier or keywords...( letter(letter|digit)* )
		if (Character.isJavaIdentifierStart((char)ch)) {
			return processWord(ch);
		}

		// number(literal)
		if (Character.isDigit((char)ch)||ch=='.') {
			return numberProcessing(ch);
		}

		// string literal
		if (ch=='\"') {
			ch=getC();
			while (ch!='\"') {
				if (ch==-1) {
					inStringFlag=true;
					return Sym.EOL;
				}
				buf.append((char)ch);
				ch=getC();
        if (ch=='\\') {
          ch = getC();
          buf.append('\\'+ch);
        }
			}
			currentToken=buf.toString();
			return Sym.SLITERAL;
		}

		// character literal
		if (ch=='\'') {
			ch=getC();
			buf.append((char)ch);
			currentToken=buf.toString();
			return Sym.CLITERAL;
		}

		switch (ch) {
			case '(' : currentToken="("; return Sym.LP;
			case ')' : currentToken=")"; return Sym.RP;
			case '[' : currentToken="["; return Sym.LS;
			case ']' : currentToken="]"; return Sym.RS;
			case '{' : currentToken="{"; return Sym.LB;
			case '}' : currentToken="}"; return Sym.RB;
			case ';' : currentToken=";"; return Sym.SEMIC;
			case ':' : currentToken=":"; return Sym.COLON;
			case ',' : currentToken=","; return Sym.COMMA;
			case '~' : currentToken="~"; return Sym.BWN;
			case '?' : currentToken="?"; return Sym.QM;
			case '+' :
				ch=getC();
				if (ch=='+') {currentToken="++"; return Sym.DP;}
				if (ch=='=') {currentToken="+="; return Sym.AADD;}
				ungetC();
				currentToken="+";
                return Sym.ADD;
			case '-' :
				ch=getC();
				if (ch=='-') {currentToken="--"; return Sym.DM;}
				if (ch=='=') {currentToken="-="; return Sym.ASUB;}
				ungetC();
				currentToken="-";
                return Sym.SUB;
			case '*' :
				ch=getC();
				if (ch=='=') {currentToken="*="; return Sym.AMUL;}
				ungetC();
				currentToken="*"; return Sym.MUL;
			case '%' :
				ch=getC();
				if (ch=='=') {currentToken="%="; return Sym.AMOD;}
				ungetC();
				currentToken="%"; return Sym.MOD;
			case '<' :
				ch=getC();
				if (ch=='=') {currentToken="<="; return Sym.LE;}
				if (ch=='<') {
					ch=getC();
					if (ch=='=') {currentToken="<<="; return Sym.ASL;}
					ungetC();
					currentToken="<<"; return Sym.SL;
				}
				ungetC();
				currentToken="<"; return Sym.LT;
			case '>' :
				ch=getC();
				if (ch=='=') {currentToken=">="; return Sym.GE;}
				if (ch=='>') {
					ch=getC();
					if (ch=='>') {
						ch=getC();
						if (ch=='=') {currentToken=">>="; return Sym.AUSR;}
						ungetC();
						currentToken=">>>";
                        return Sym.USR;
					}
					ungetC();
					currentToken=">>";
                    return Sym.SR;
				}
				ungetC();
				currentToken=">";
                return Sym.GT;
			case '&' :
				ch=getC();
				if (ch=='&') {currentToken="&&"; return Sym.LA;}
				if (ch=='=') {currentToken="&="; return Sym.ABWA;}
				ungetC();
				currentToken="&"; return Sym.BWA;
			case '|' :
				ch=getC();
				if (ch=='|') {currentToken="||"; return Sym.LO;}
				if (ch=='=') {currentToken="|="; return Sym.ABWO;}
				ungetC();
				currentToken="|"; return Sym.BWO;
			case '^' :
				ch=getC();
				if (ch=='=') {currentToken="^="; return Sym.ABWN;}
				ungetC();
				currentToken="^"; return Sym.BWEO;
			case '!' :
				ch=getC();
				if (ch=='=') {currentToken="!="; return Sym.NE;}
				ungetC();
				currentToken="!"; return Sym.LN;
			case '=' :
				ch=getC();
				if (ch=='=') {currentToken="=="; return Sym.EQ;}
				ungetC();
				currentToken="="; return Sym.ASSIGN;
		}

		return Sym.ERROR;
	}

	private int processWord (int c) {
		StringBuffer buf=new StringBuffer();
		do {
			buf.append((char)c);           // token is divided.
			c=getC();
		} while (Character.isJavaIdentifierPart((char)c));
		ungetC();
		currentToken=buf.toString();

		return keywordsORidentifier(currentToken);
	}

	private int numberProcessing(int c) {
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
			currentToken=buf.toString();
			return Sym.ILITERAL;
		}
		while (Character.isDigit((char)c)) {
      buf.append((char)c);
      c=getC();
		}
		if (c=='.') {
			c=getC();
			if (c!='E'&&c!='e'&&!Character.isDigit((char)c)) {
				ungetC();
				currentToken=".";
                return Sym.DOT;
			}
			ungetC();
			c='.';
  			do {
    			buf.append((char)c);
    			c=getC();
  			} while (Character.isDigit((char)c));
			if (c=='E'||c=='e') {
				c=getC();
				if (!Character.isDigit((char)c)) {
					index-=2;		// two ungetC()의 효과...
					if (buf.length()==1) {
						currentToken=".";
						return Sym.DOT;
					}
					else {
						currentToken=buf.toString();
						return Sym.RLITERAL;
					}
				}
				do {
					buf.append((char)c);
					c=getC();
				} while (Character.isDigit((char)c));
			}
			ungetC();
			currentToken=buf.toString();
			return Sym.RLITERAL;
		}
		else {
			ungetC();
			currentToken=buf.toString();
			return Sym.ILITERAL;
		}
	}

	private int processComment(int c) {
		int ch=c;

    inCommentState=true;

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

    inCommentState=false;
		return 1;
	}

	static Hashtable ht=new Hashtable();

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

	private int keywordsORidentifier(String s) {
		Integer value=(Integer)ht.get(s);

		if (value==null) return Sym.ID;
		else return value.intValue();
	}

	/**
	  getting the current real token string.

	  @return the token as the String value.
	  */
	public String getTokenString() {
		return currentToken;
	}

	/**
	  getting the current character position.

	  @return the current position as int.
	  */
	public int getCurrentPosition() {
		return index;
	}
}
