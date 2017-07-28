/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/GetTokenForEvent.java,v 1.5 1999/08/24 09:19:38 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 * Part : Get Token For Event Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999.2.3.
 */

package com.antsoft.ant.codecontext;

import java.util.Vector;

/**
  Keying Dot or LeftParenthesis, gets the previous token and prepares to
  making the event.

  @author Kim, Sung-Hoon.
  */
public class GetTokenForEvent {
	private Vector type=new Vector();
	private Vector token=new Vector();
  private Lexer tokenizer = null;

  private boolean isCasting = false;

	/**
	  Constructor.

	  @param s string to be used to make the EditFuntionEvent.
	  */
	public GetTokenForEvent(String s) {
    tokenizer = new Lexer(s);
	}

  public boolean isCasting() {
  	return isCasting;
  }

	/**
	  tokenizing the specified string using LineTokenizer and storing
	  the tokentype and real token string in Vector list.
	  */
	public void tokenize() {
		int typ=tokenizer.nextToken();

		//while (typ!=Sym.EOL) {
    while (typ != Sym.EOF) {
      // comment���� �����ϰ� ��� token�� �� type�� list�� ����Ѵ�.
			//if (typ!=Sym.COMMENT) {
				type.addElement(new Integer(typ));
				//String tok=tokenizer.getTokenString();
        String tok = tokenizer.getValue();
				if (tok == null) tok= "";
       	switch (typ) {
       		case Sym.CLITERAL : tok="char"; break;
       		case Sym.ILITERAL : tok="int"; break;
       		case Sym.RLITERAL : tok="double"; break;
       		case Sym.SLITERAL : tok="String"; break;
      	}
  			token.addElement(tok);
			//}
      typ=tokenizer.nextToken();
		}
	}

	/**
	  get the just previous token and token type of the dot.

	  @return the token String.
	  */
	public String getPrevTokenOfDot() {
    tokenize();

		if (LineTokenizer.inStringFlag) {
			LineTokenizer.inStringFlag=false;
			return null;
		}
		if (LineTokenizer.inCommentState) {
			LineTokenizer.inCommentState=false;
			return null;
		}

    StringBuffer prevToken=new StringBuffer();
    if (type.size()==0) return null;
    int i=type.size()-1;

    Integer typ=(Integer)type.elementAt(i);
    while (true) {
      // ���� .�ٷξ��� )�� ó���Ѵ�. ��, Method Invocation�� ó��.
      if (typ.intValue()==Sym.RP||typ.intValue()==Sym.RS) {
        i--;
        if (i<0) return null;
        typ=(Integer)type.elementAt(i);
        int pdepth=0;
        int sdepth=0;
        // �����ϴ� '(' �Ǵ� '['�� ����������, ���� token���� �����Ѵ�.
        while ((typ.intValue()!=Sym.LP||pdepth!=0)&&(typ.intValue()!=Sym.LS||sdepth!=0)) {
          if (typ.intValue()==Sym.LP) pdepth++;
          if (typ.intValue()==Sym.RP) pdepth--;
          if (typ.intValue()==Sym.LS) sdepth++;
          if (typ.intValue()==Sym.RS) sdepth--;
          i--;
          if (i<0) return null;
          typ=(Integer)type.elementAt(i);
        }
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
        if (typ.intValue()==Sym.RP) continue;   // �Ǵٸ� )�� ������ ���ǰ��� �ٽ��Ѵ�.
        if (typ.intValue()==Sym.RS) continue;   // �Ǵٸ� ]�� ������ ���ǰ��� �ٽ��Ѵ�.
      }
      if (typ.intValue()==Sym.ID) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.THIS) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.SUPER) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.CLASS) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      //else return null;  // [](......)�� �����Ѵ�.
      else {		// for casting....
        i++;
        if (i >= type.size()) return null;

        typ = (Integer)type.elementAt(i);
        if (typ.intValue() == Sym.LP) {
          for (;i<type.size();++i) {
            typ=(Integer)type.elementAt(i);
            if (typ.intValue()==Sym.ID) {
              isCasting = true;
            	return (String)token.elementAt(i);
            }
          }
        }
        return null;
      }

      if (i<1) break;
      if (typ.intValue()==Sym.DOT) {
        i--;
        typ=(Integer)type.elementAt(i);
        continue;
      }
      else break;
    }
    int index=i+1;

    // �ʿ��� operand�� ã�Ҵ�. String���� �����Ͽ� return.
    for (i=index;i<type.size();++i) prevToken.append(token.elementAt(i));

    return prevToken.toString();
	}

	/**
	  get the just previous token and token type of the left parenthesis.

	  @return the token String.
	  */
	public String getPrevTokenOfLeftParen() {
    tokenize();
    StringBuffer prevToken=new StringBuffer();
    if (type.size()==0) return null;
    int i=type.size()-1;

    Integer typ=(Integer)type.elementAt(i);
    while (true) {
      // ���� id���� ���� ���� �� �ֳ� �˻�.
      if (typ.intValue()==Sym.ID) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.THIS) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.SUPER) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else if (typ.intValue()==Sym.CLASS) {
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
      }
      else return null;  // [](......)�� �����Ѵ�.

      if (i<0) break;
      if (typ.intValue()==Sym.DOT) {
        i--;
        typ=(Integer)type.elementAt(i);
      }
      else break;

      if (typ.intValue()==Sym.RP||typ.intValue()==Sym.RS) {
        i--;
        if (i<0) return null;
        typ=(Integer)type.elementAt(i);
        int pdepth=0;
        int sdepth=0;
        // �����ϴ� '(' �Ǵ� '['�� ����������, ���� token���� �����Ѵ�.
        while ((typ.intValue()!=Sym.LP||pdepth!=0)&&(typ.intValue()!=Sym.LS||sdepth!=0)) {
          if (typ.intValue()==Sym.LP) pdepth++;
          if (typ.intValue()==Sym.RP) pdepth--;
          if (typ.intValue()==Sym.LS) sdepth++;
          if (typ.intValue()==Sym.RS) sdepth--;
          i--;
          if (i<0) return null;
          typ=(Integer)type.elementAt(i);
        }
        i--;
        if (i>=0) typ=(Integer)type.elementAt(i);
        if (typ.intValue()==Sym.RP) continue;   // �Ǵٸ� )�� ������ ���ǰ��� �ٽ��Ѵ�.
        if (typ.intValue()==Sym.RS) continue;   // �Ǵٸ� ]�� ������ ���ǰ��� �ٽ��Ѵ�.
      }
    }
    int index=i+1;

    // �ʿ��� operand�� ã�Ҵ�. String���� �����Ͽ� return.
    for (i=index;i<type.size();++i) prevToken.append(token.elementAt(i));

    return prevToken.toString();
	}

  /**
    get the token string list as Vector.

    @return token string list.
    */
  public Vector getTokenList() {
    return token;
  }

  /**
    get the token type list as Vector.

    @return token type list.
    */
  public Vector getTokenTypeList() {
    return type;
  }
}
