package com.antsoft.ant.codecontext;

import java.util.*;
import java.io.*;
import javax.swing.text.Document;

/*
 @author Kim, SungHoon.
 */
public class ClassExtractor {
  private static Lexer lexer;
  private static int token;

  private static String packageName = null;
  private static Vector classes = null;

  // Constructor.
  public ClassExtractor(File file) {
    lexer = new Lexer(file);
    //Lexer.setData(file);
    packageName = "dummypack";
    classes = null;
  }

  /*
  public ClassExtractor(InputStream in) {
    lexer = new Lexer(in);
    packageName = "dummypack";
  }

  public ClassExtractor(Document doc) {
    lexer = new Lexer(doc);
    packageName = "dummypack";
  }
  */

  private static final String name() {
    StringBuffer buf = new StringBuffer("");
    boolean loop = false;

    do {
      if (token == Sym.ID) {
        buf.append(Lexer.getValue());
        token = Lexer.nextToken();
        if (token == Sym.DOT) {
          buf.append(".");
          token = Lexer.nextToken();
          loop = true;
        }
        else loop = false;
      }
      else loop = false;
    } while (loop);

    return buf.toString();
  }

  private static final void normalStatement() {
    while (token != Sym.RB && token != Sym.EOF) {
      if (token == Sym.LB) {
        token = Lexer.nextToken();
        normalStatement();
      }
      else token = Lexer.nextToken();
    }
    token = Lexer.nextToken();
  }

  private static final void classBlock() {
    String key = null;

    while (token == Sym.MODIFIER || token == Sym.MODIFIER2) {
      token = Lexer.nextToken();
    }

    while (token != Sym.CLASS && token != Sym.INTERFACE && token != Sym.EOF) token = Lexer.nextToken();
    if (token == Sym.EOF) return;

    token = Lexer.nextToken();
    while (token != Sym.ID && token != Sym.EOF) token = Lexer.nextToken();
    if (token == Sym.EOF) return;

    if (classes == null) classes = new Vector(1,1);
    classes.addElement(Lexer.getValue());

    token = Lexer.nextToken();
    if (token == Sym.EXTENDS) {
      token = Lexer.nextToken();
      name();
    }

    if (token == Sym.IMPLEMENTS) {
      token = Lexer.nextToken();
      while (true) {
        name();
        if (token == Sym.COMMA) token = Lexer.nextToken();
        else break;
      }
    }

    while (token != Sym.LB && token != Sym.EOF) token = Lexer.nextToken();
    if (token == Sym.EOF) {
      return;
    }

    token = Lexer.nextToken();
    normalStatement();
    token = Lexer.nextToken();
  }

  private static final void importStatement() {
    if (token == Sym.IMPORT) {
      Lexer.newLine = true;
      token = Lexer.nextToken();
      while ( (token != Sym.EOL) && (token != Sym.EOF) ) {
        token = Lexer.nextToken();
      }
    }

    Lexer.newLine = false;
    token = Lexer.nextToken();
  }

  private static final void packageStatement() {
    if (token == Sym.PACKAGE) {
      Lexer.newLine = true;
      StringBuffer buf = new StringBuffer();
      token = Lexer.nextToken();
      while ( (token  != Sym.EOL) && (token != Sym.EOF) ) {
        if (token == Sym.ID) buf.append(Lexer.getValue());
        else if (token == Sym.DOT) buf.append(".");
        token = Lexer.nextToken();
      }

      packageName = buf.toString();
    }

    Lexer.newLine = false;
    token = Lexer.nextToken();
  }

  public static final void doParse() {
    token = Lexer.nextToken();
    if (token == Sym.EOF) return;

    if (token == Sym.PACKAGE) packageStatement();
    while (token == Sym.IMPORT) importStatement();

    while (token != Sym.EOF) classBlock();
  }

  public static final String getPackageName() {
    return packageName;
  }

  public static final Vector getClasses() {
    return classes;
  }
}
