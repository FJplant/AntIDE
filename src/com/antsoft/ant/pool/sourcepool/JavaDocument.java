/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/sourcepool/JavaDocument.java,v 1.20 1999/08/30 07:59:07 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.20 $
 * $History: JavaDocument.java $
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * 소스 관리자
 *
 */
package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import java.util.Hashtable;
import javax.swing.text.*;
import javax.swing.event.*;

import com.antsoft.ant.util.Constants;

/**
 *  class JavaDocument
 *
 *  @author Jinwoo Baek
 */
public class JavaDocument extends AntDocument {
  private JavaContext styles = new JavaContext();

	/**
	 *  Constructor
	 */
  public JavaDocument() {
  	this( 1024 );
  }

  public JavaDocument( int size ) {
    super(new MyGapContent(size));
    this.setModified(false);
  }

  	/**
	 * Create a lexical analyzer for this document.
	 */
	public Object createScanner() {
    //System.out.println("create Scanner");
    return new Scanner();
	}

	/**
	 * Fetch a reasonable location to start scanning
	 * given the desired start location.  This allows
	 * for adjustments needed to accomodate multiline
	 * comments.
	 */
	public int getScannerStart(int p) {

		Element root = getDefaultRootElement();

		MyGapContent content = (MyGapContent)getContent();
		char[] data = (char []) content.getArray();

		int gapStart = content.getGapStart();
		int gapEnd = content.getGapEnd();

    //stem.out.println("GapStart : " + gapStart + "   GapEnd : " + gapEnd);

		int resultPos = 0;

		try {

		if (gapStart > p) {

			for (int i=p;i>0;--i) {

				if (data[i-1] == '/' && data[i] == '*') {
					Element e = root.getElement(root.getElementIndex(i-1));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return so;
					if ( str.indexOf("//") > i-1-so ) return so;
				}

				if (data[i-1] == '*' && data[i] == '/') {
					Element e = root.getElement(root.getElementIndex(i-1));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return p;
					if ( str.indexOf("//") > i-1-so ) return p;
				}
			}
		}

		//else if (gapEnd < p) {
    else {
      int gapLength = gapEnd - gapStart + 1;
			for (int i=p+gapLength;i>gapEnd;--i) {

				if (data[i-1] == '/' && data[i] == '*') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return so;
					if ( str.indexOf("//") > i-1-so ) return so;
				}

				if (data[i-1] == '*' && data[i] == '/') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return p;
					if ( str.indexOf("//") > i-1-so ) return p;
				}
			}

			for (int i=gapStart-1;i>0;--i) {

				if (data[i-1] == '/' && data[i] == '*') {
					Element e = root.getElement(root.getElementIndex(i-1));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return so;
					if ( str.indexOf("//") > i-1-so ) return so;
				}

				if (data[i-1] == '*' && data[i] == '/') {
					Element e = root.getElement(root.getElementIndex(i-1));
					int so = e.getStartOffset();
					String str = getText(so, e.getEndOffset() - so + 1);

          if (str.indexOf("//") == -1) return p;
					if ( str.indexOf("//") > i-1-so ) return p;
				}
			}
		}

		} catch (BadLocationException e) {
			System.out.println(" GetScannerStart() : "+e.toString());
		}

		return p;
	}

  ////////////////////////////////////////////////////////////////////////////////////

  public static final int KEYWORD = 0;
  public static final int STRING = 1;
  public static final int COMMENT = 2;
  public static final int OTHER = 3;
  public static final int EOF = -1;

  private static long MAXFILESIZE = (1L << 32) -1;

  /**
   * @author : kahn(Kim, Sung-Hoon)
   */
  public class Scanner {
    private DocumentReader reader = null;

    private int p0;
    private int pos;

    public int token ;
    private int _token = -2;
    public long firstPos = 0;

    protected int ch;

		public Scanner() {
		}

    /*
     * Sets the range of the scanner.  This should be called
     * to reinitialize the scanner to the desired range of
     * coverage.
     */
    public void setRange(int p0, int p1) throws IOException {
      this.reader = new DocumentReader(p0, p1);
      this.p0 = p0;
      ch = read();
      pos = 0;
    }

    private int read() throws IOException {
      pos++;
      return reader.read();
    }

    /**
     * This fetches the starting location of the current
     * token in the document.
     */
    public final int getStartOffset() {
      int begOffs = (int) ((firstPos) & MAXFILESIZE);
      return p0 + begOffs;
    }

    /**
     * This fetches the ending location of the current
     * token in the document.
     */
    public final int getEndOffset() {
      int endOffs = (int) (pos & MAXFILESIZE);
      return p0 + endOffs;
    }

    /*
     * read the next token.
     * get next token, currenttoken is assigned and return token type.
     *
     * @return token type of the current token(using field of class TokenType)
     */
    public void scan() throws IOException {

      firstPos = pos;

      if (ch == -1) { token = EOF; return; }

      switch (ch) {
        case '/' :
          ch = read();
          switch (ch) {
            case '*' :
              ch = read();
              skipComment();
              _token = COMMENT;
              break;

            case '/' :
							do {
								ch = read();
              } while ( ch != -1 && ch != '\n');
              _token = COMMENT;
               break;

            case '=' :
              ch = read();
              _token = OTHER;
              break;

            default :
              _token = OTHER;
          }
          break;

        case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
        case '6' : case '7' : case '8' : case '9' :
          skipNumber();
          _token = STRING;
          break;

        case '"' :
          skipString();
          _token = STRING;
          break;

        case '\'' :
          skipCharacter();
          _token = STRING;
          break;

        case '.' :
          ch = read();
          switch (ch) {
            case '0' : case '1' : case '2' : case '3' : case '4' : case '5' :
            case '6' : case '7' : case '8' : case '9' :
              skipRealNumber();
              _token = STRING;
              break;

            default :
              _token = OTHER;
          }
          break;

        case '(' : case ')' : case '[' : case ']' : case ',' :
        case '{' : case '}' : case ';' : case ':' : case '~' :
        case '?' :
          ch = read();
          _token = OTHER;
          break;

        case '+' :
          ch = read();
          switch (ch) {
            case '+' : case '=' :
              ch = read();
              break;
          }
          _token = OTHER;
          break;

        case '-' :
          ch = read();
          switch (ch) {
            case '-' : case '=' :
              ch = read();
              break;
          }
          _token = OTHER;
          break;

        case '*' :
          if ( (ch = read()) == '=' ) ch = read();
          _token = OTHER;
          break;

        case '%' :
          if ( (ch = read()) == '=' ) ch = read();
          _token = OTHER;
          break;

        case '<' :
          ch = read();
          switch (ch) {
            case '=' :
              ch = read();
              break;
            case '<' :
              if ( (ch = read()) == '=' ) ch = read();
              break;
          }
          _token = OTHER;
          break;

        case '>' :
          ch = read();
          switch (ch) {
            case '=' :
              ch = read();
              break;
            case '>' :
              ch = read();
              switch (ch) {
                case '>' :
                  if ( (ch = read()) == '=' ) ch = read();
              }
              break;
          }
          _token = OTHER;
          break;

        case '&' :
          ch = read();
          switch (ch) {
            case '&' : case '=' :
              ch = read();
              break;
          }
          _token = OTHER;
          break;

        case '|' :
          ch = read();
          switch (ch) {
            case '|' : case '=' :
              ch = read();
              break;
          }
          _token = OTHER;
          break;

        case '^' :
          if ( (ch = read()) == '=' ) ch = read();
          _token = OTHER;
          break;

        case '!' :
          if ( (ch = read()) == '=' ) ch = read();
          _token = OTHER;
          break;

        case '=' :
          if ( (ch = read()) == '=' ) ch = read();
          _token = OTHER;
          break;

        case 'a' : case 'b' : case 'c' : case 'd' : case 'e' : case 'f' :
        case 'g' : case 'h' : case 'i' : case 'j' : case 'k' : case 'l' :
        case 'm' : case 'n' : case 'o' : case 'p' : case 'q' : case 'r' :
        case 's' : case 't' : case 'u' : case 'v' : case 'w' : case 'x' :
        case 'y' : case 'z' : case 'A' : case 'B' : case 'C' : case 'D' :
        case 'E' : case 'F' : case 'G' : case 'H' : case 'I' : case 'J' :
        case 'K' : case 'L' : case 'M' : case 'N' : case 'O' : case 'P' :
        case 'Q' : case 'R' : case 'S' : case 'T' : case 'U' : case 'V' :
        case 'W' : case 'X' : case 'Y' : case 'Z' : case '$' : case '_' :
          _token = checkKeyword();
          break;

        default : 
					ch = read(); 
					_token = OTHER;

      } // end of switch(ch)


			token = _token;
    }

    private int checkKeyword () throws IOException {
      StringBuffer buf = new StringBuffer();
      do {
        buf.append((char)ch);           // token is divided.
        ch = read();
      } while (Character.isJavaIdentifierPart((char)ch));

      if ( keywordTable.get(buf.toString()) != null ) return KEYWORD;
      else return OTHER;
    }

    private void skipString() throws IOException {
      if (ch == '\"') {
        ch = read();
        while (ch != '\"') {
          if (ch == -1) return;
					if (ch == '\n') return;
          if (ch == '\\') read();
          ch = read();
        }

        ch = read();
      }
    }

    private void skipNumber() throws IOException {
      boolean isOctal = false;
      boolean isHex = false;

      isOctal = (ch == '0') ? true : false;

      if ( (ch = read()) == 'x' || ch == 'x' ) {
        isHex = true;
        ch = read();
      }

      while (true) {
        switch (ch) {
          case '.' :
            if (isHex) return;
            skipRealNumber();
            return;

          case '8' : case '9' :
            isOctal = false;
          case '0' : case '1' : case '2' : case '3' :
          case '4' : case '5' : case '6' : case '7' :
            break;

          case 'd' : case 'D' : case 'e' : case 'E' : case 'f' : case 'F' :
            if (!isHex) {
              skipRealNumber();
              return;
            }
            break;

          case 'a' : case 'A' : case 'b' : case 'B' : case 'c' : case 'C' :
            if (!isHex) return;
            break;

          case 'l' : case 'L' : ch = read(); return;

          default : return;
        }
        ch = read();
      }
    }

    private void skipRealNumber() throws IOException {
      boolean isExponent = false;
      int lastChar = 0;

      if (ch == '.') ch = read();

      while (true) {
        switch (ch) {
          case 'e' : case 'E' :
            if (isExponent) return;

            isExponent = true;
            break;

          case '+' : case '-' :
            if (lastChar != 'e' && lastChar != 'E') return;

          case '0' : case '1' : case '2' : case '3' : case '4' :
          case '5' : case '6' : case '7' : case '8' : case '9' :
            break;

          case 'd' : case 'D' : case 'f' : case 'F' :
            ch = read();
            return;

          default : return;
        }

        lastChar = ch;
        ch = read();
      }
    }

    private void skipComment() throws IOException{
      while (ch == '/') ch = read();
      while (ch != '/') {
          while (ch!='*') {
            ch = read();
            if (ch == -1) return;
          }
          ch = read();
          if (ch == -1) return;
      }
			ch = read();
    }

    private void skipCharacter() throws IOException {
      switch (ch = read()) {
        case '\'' :
          ch = read();
          break;

        case '\\' :
          switch (ch = read()) {
            case 'r' : case 'n'  : case 't'  : case 'b' :
            case 'f' : case '\\' : case '\"' : case '\'' :
              ch = read();
							if (ch == '\'') ch = read();
              break;

            case '0' : case '1' : case '2' : case '3' :
            case '4' : case '5' : case '6' : case '7' :
              for (int i=2; i > 0; --i) {
                switch (ch = read()) {
                  case '0' : case '1' : case '2' : case '3' :
                  case '4' : case '5' : case '6' : case '7' :
                    break;
                  default : return;
                }
              }
							if (ch == '\'') ch = read();
          }
          break;

        case '\r' :
        case '\n' :
          ch = read();
          break;

        default :
          ch = read();
          if (ch == '\'') ch = read();
          break;
      }
    }
  }


  private static Hashtable keywordTable = new Hashtable();

  static {
    try {
      keywordTable.put("abstract",		new Integer(KEYWORD));
      keywordTable.put("boolean",			new Integer(KEYWORD));
      keywordTable.put("break",				new Integer(KEYWORD));
      keywordTable.put("byte",				new Integer(KEYWORD));
      keywordTable.put("case",				new Integer(KEYWORD));
      keywordTable.put("catch",				new Integer(KEYWORD));
      keywordTable.put("char",				new Integer(KEYWORD));
      keywordTable.put("class",				new Integer(KEYWORD));
      keywordTable.put("const",				new Integer(KEYWORD));
      keywordTable.put("continue",		new Integer(KEYWORD));
      keywordTable.put("default",			new Integer(KEYWORD));
      keywordTable.put("do",					new Integer(KEYWORD));
      keywordTable.put("double",			new Integer(KEYWORD));
      keywordTable.put("else",				new Integer(KEYWORD));
      keywordTable.put("extends",			new Integer(KEYWORD));
      keywordTable.put("false",				new Integer(KEYWORD));
      keywordTable.put("final",				new Integer(KEYWORD));
      keywordTable.put("finally",			new Integer(KEYWORD));
      keywordTable.put("float",				new Integer(KEYWORD));
      keywordTable.put("for",					new Integer(KEYWORD));
      keywordTable.put("goto",				new Integer(KEYWORD));
      keywordTable.put("if",					new Integer(KEYWORD));
      keywordTable.put("implements",	new Integer(KEYWORD));
      keywordTable.put("import",			new Integer(KEYWORD));
      keywordTable.put("instanceof",	new Integer(KEYWORD));
      keywordTable.put("int",					new Integer(KEYWORD));
      keywordTable.put("interface",		new Integer(KEYWORD));
      keywordTable.put("long",				new Integer(KEYWORD));
      keywordTable.put("native",			new Integer(KEYWORD));
      keywordTable.put("new",					new Integer(KEYWORD));
      keywordTable.put("null",				new Integer(KEYWORD));
      keywordTable.put("package",			new Integer(KEYWORD));
      keywordTable.put("private",			new Integer(KEYWORD));
      keywordTable.put("protected",		new Integer(KEYWORD));
      keywordTable.put("public",			new Integer(KEYWORD));
      keywordTable.put("return",			new Integer(KEYWORD));
      keywordTable.put("short",				new Integer(KEYWORD));
      keywordTable.put("static",			new Integer(KEYWORD));
      keywordTable.put("super",				new Integer(KEYWORD));
      keywordTable.put("synchronized",new Integer(KEYWORD));
      keywordTable.put("switch",			new Integer(KEYWORD));
      keywordTable.put("this",				new Integer(KEYWORD));
      keywordTable.put("throw",				new Integer(KEYWORD));
      keywordTable.put("throws",			new Integer(KEYWORD));
      keywordTable.put("transient",		new Integer(KEYWORD));
      keywordTable.put("true",				new Integer(KEYWORD));
      keywordTable.put("try",					new Integer(KEYWORD));
      keywordTable.put("void",				new Integer(KEYWORD));
      keywordTable.put("volatile",		new Integer(KEYWORD));
      keywordTable.put("while",				new Integer(KEYWORD));
    } catch (NullPointerException e) {
      System.err.println(e.toString());
    }
  }


	/**
	 * Class to provide InputStream functionality from a portion of a
	 * Document.  This really should be a Reader, but not enough
	 * things use it yet.
	 */
	//class DocumentInputStream extends InputStream {
	class DocumentReader extends Reader {
		public DocumentReader(int p0, int p1) {
			this.segment = new Segment();
			this.p0 = p0;
			this.p1 = Math.min(getLength(), p1);
			pos = p0;
			try {
				loadSegment();
			} catch (IOException ioe) {
				throw new Error("unexpected: " + ioe);
			}
		}

		/**
		 * Reads the next byte of data from this input stream. The value
		 * byte is returned as an <code>int</code> in the range
		 * <code>0</code> to <code>255</code>. If no byte is available
		 * because the end of the stream has been reached, the value
		 * <code>-1</code> is returned. This method blocks until input data
		 * is available, the end of the stream is detected, or an exception
		 * is thrown.
		 * <p>
		 * A subclass must provide an implementation of this method.
		 *
		 * @return the next byte of data, or <code>-1</code> if the end of the
		 * stream is reached.
		 * @exception  IOException  if an I/O error occurs.
		 * @since  JDK1.0
		 */
		public int read() throws IOException {
			if (index >= segment.offset + segment.count) {
				if (pos >= p1) {
					// no more data
					return -1;
				}
				loadSegment();
			}

			return segment.array[index++];
		}

    /**
     *
     */
		void loadSegment() throws IOException {
			try {
				int n = Math.min(1024, p1 - pos);
				getText(pos, n, segment);
				pos += n;
				index = segment.offset;
			} catch (BadLocationException e) {
				throw new IOException("Bad location");
			}
		}

		Segment segment;
		int p0;// start position
		int p1;// end position
		int pos;   // pos in document
		int index; // index into array of the segment

		public int read(char[] buf, int i, int j) {return -1;}
		public void close() { }
	}
}

