/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 */
package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import java.util.Hashtable;
import javax.swing.text.*;
import javax.swing.event.*;

import com.antsoft.ant.util.Constants;

/**
 *  class HtmlDocument
 *
 *  @author Kim, Sung-Hoon.
 *  @author Junwoo Baek.
 */
public class HtmlDocument extends AntDocument {
  private HtmlContext styles = new HtmlContext();

	/**
	 *  Constructor
	 */
  public HtmlDocument() {
  	this( 1024 );
	}
	
	public HtmlDocument( int size ) {
    super(new MyGapContent(size));
    this.setModified(false);
  }

	/**
	 * Create a lexical analyzer for this document.
	 */
	public Object createScanner() {
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
		char[] data = (char[])content.getArray();

		int gapStart = content.getGapStart();
		int gapEnd = content.getGapEnd();

    //System.out.println("GapStart : " + gapStart + "   GapEnd : " + gapEnd);

/*
		if (gapStart > p) {
			int i=0;
			for (i=p;i>2;--i) {

				if (data[i-3] == '<' && data[i-2] == '!' &&
						data[i-1] == '-' && data[i] == '-') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}

				if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}
			}

			if (i>2)
			if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
				Element e = root.getElement(root.getElementIndex(i-1));
				return e.getStartOffset();
			}
		}

    else {
      int gapLength = gapEnd - gapStart + 1;
			int i=0;
			for (i=p+gapLength;i>gapEnd+3;--i) {

				if (data[i-3] == '<' && data[i-2] == '!' &&
						data[i-1] == '-' && data[i] == '-') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					return e.getStartOffset();
				}

				if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					return e.getStartOffset();
				}
			}

			if (i>2)
			if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
				Element e = root.getElement(root.getElementIndex(i-1-gapLength));
				return e.getStartOffset();
			}


			for (i=gapStart-1;i>2;--i) {

				if (data[i-3] == '<' && data[i-2] == '!' &&
						data[i-1] == '-' && data[i] == '-') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}

				if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}
			}

			if (i>2)
			if (data[i-2] == '-' && data[i-1] == '-' && data[i] == '>') {
				Element e = root.getElement(root.getElementIndex(i-1));
				return e.getStartOffset();
			}
		}
*/
		if (gapStart > p) {
			for (int i=p;i>0;--i) {

				if (data[i] == '<') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}

				if (data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}
			}
		}

    else {
      int gapLength = gapEnd - gapStart + 1;
			for (int i=p+gapLength;i>gapEnd;--i) {

				if (data[i] == '<') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					return e.getStartOffset();
				}

				if (data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1-gapLength));
					return e.getStartOffset();
				}
			}

			for (int i=gapStart-1;i>0;--i) {

				if (data[i] == '<') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}

				if (data[i] == '>') {
					Element e = root.getElement(root.getElementIndex(i-1));
					return e.getStartOffset();
				}
			}
		}

		return p;
	}

  ///////////////////////////////////////////////////////////////////

  public static final int KEYWORD = 0;
  public static final int STRING = 1;
  public static final int COMMENT = 2;
  public static final int TAG = 2;
  public static final int OTHER = 4;
  public static final int EOF = -1;

  private static long MAXFILESIZE = (1L << 32) -1;

	private static boolean inTag = false;
	private static boolean isTag = false;
	private static boolean isConstant = false;


  /**
   * @author : kahn(Kim, Sung-Hoon)
   */
  public class Scanner {
    private DocumentReader reader = null;

    private int p0;
    private int pos;

    public int token ;
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
			//int ss = reader.read();
			//System.out.println("char "+ss);
			//return ss;
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

			while (Character.isWhitespace((char)ch)||ch==0x0000) ch = read();

      if (ch == -1) { token = EOF; return; }

      switch (ch) {
        case '<' :
					inTag = true;
					ch = read();
					if (!Character.isWhitespace((char)ch)) isTag = true;
					else isTag = false;
					if (ch == '/') {
						ch = read();
						if (ch == ' ') isTag = false;
					}
					token = KEYWORD;
					isConstant = false;
          break;

        case '>' :
					inTag = false;
					ch = read();
					token = KEYWORD;
					isConstant = false;
					return;
          //break;

        case '"' :
          skipString();
          token = STRING;
					isConstant = false;
          break;

        case '\'' :
          skipCharacter();
          token = STRING;
					isConstant = false;
          break;

        case '=' :
					ch = read();
					if (inTag) isConstant = true;
					token = OTHER;
          break;

				case '!' :
					if (isTag) {
						token = checkTag();
						isTag = false;
					}
					else {
						token = OTHER;
						ch = read();
					}
					isConstant = false;
					break;

        default : 
					if (isTag) {
						token = checkTag();
						isTag = false;
					}
					else {
						if (inTag && isConstant) {
							token = STRING;

							while (ch != '>' && !Character.isWhitespace((char)ch) && ch != -1)
								ch = read();
						}
						else if (!inTag) {
							while (ch != '<' && ch != '\n' && ch != -1) ch = read();
						}
						else token = checkKeyword();
					}
					isConstant = false;
          break;
      } // end of switch(ch)

			if (!inTag) token = OTHER;
    }

    private int checkKeyword () throws IOException {
      StringBuffer buf = new StringBuffer();
      do {
        buf.append((char)ch);           // token is divided.
        ch = read();
      } while (Character.isLetterOrDigit((char)ch)||ch=='-');

      if ( keywordTable.get(buf.toString().toLowerCase()) != null ) 
				return KEYWORD;
      else return OTHER;
    }

		private int checkTag() throws IOException {
			StringBuffer buf = new StringBuffer();
			do {
				buf.append((char)ch);
				ch = read();
			} while (Character.isLetterOrDigit((char)ch) || ch == '-');

			if ( buf.toString().startsWith("!--")) {
				skipComment();
				return COMMENT;
			}

			if ( tagTable.get(buf.toString().toLowerCase()) != null)
				return TAG;
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
      while (true) {
        switch (ch) {
          case '.' :
            skipRealNumber();
            return;

          case '8' : case '9' :
          case '0' : case '1' : case '2' : case '3' :
          case '4' : case '5' : case '6' : case '7' :
            break;

					case '%' : 
						ch = read();
						return;

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

					case '%' :
						ch = read();
						return;

          default : return;
        }

        lastChar = ch;
        ch = read();
      }
    }

    private void skipComment() throws IOException{
      while (ch != '>') {
				while (ch != '-') {
          while (ch != '-') {
            ch = read();
            if (ch == -1) return;
          }
          ch = read();
          if (ch == -1) return;
				}
				ch = read();
				if (ch == -1) return;
      }
			//ch = read();
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

	private static Hashtable tagTable = new Hashtable();

	static {
		try {
			tagTable.put("!doctype","");
			tagTable.put("a","");
			tagTable.put("abbr","");
			tagTable.put("acronym","");
			tagTable.put("address","");
			tagTable.put("applet","");
			tagTable.put("area","");
			tagTable.put("b","");
			tagTable.put("base","");
			tagTable.put("basefont","");
			tagTable.put("bdo","");
			tagTable.put("bgsound","");
			tagTable.put("big","");
			tagTable.put("blink","");
			tagTable.put("blockquote","");
			tagTable.put("body","");
			tagTable.put("br","");
			tagTable.put("button","");
			tagTable.put("caption","");
			tagTable.put("center","");
			tagTable.put("cite","");
			tagTable.put("col","");
			tagTable.put("colgroup","");
			tagTable.put("comment","");
			tagTable.put("dd","");
			tagTable.put("del","");
			tagTable.put("dfn","");
			tagTable.put("dir","");
			tagTable.put("div","");
			tagTable.put("dl","");
			tagTable.put("dt","");
			tagTable.put("em","");
			tagTable.put("embed","");
			tagTable.put("fieldset","");
			tagTable.put("font","");
			tagTable.put("form","");
			tagTable.put("frame","");
			tagTable.put("frameset","");
			tagTable.put("h1","");
			tagTable.put("h2","");
			tagTable.put("h3","");
			tagTable.put("h4","");
			tagTable.put("h5","");
			tagTable.put("h6","");
			tagTable.put("head","");
			tagTable.put("hr","");
			tagTable.put("html","");
			tagTable.put("i","");
			tagTable.put("iframe","");
			tagTable.put("ilayer","");
			tagTable.put("img","");
			tagTable.put("input","");
			tagTable.put("ins","");
			tagTable.put("isindex","");
			tagTable.put("kbd","");
			tagTable.put("keygen","");
			tagTable.put("label","");
			tagTable.put("layer","");
			tagTable.put("legend","");
			tagTable.put("li","");
			tagTable.put("link","");
			tagTable.put("listing","");
			tagTable.put("marquee","");
			tagTable.put("menu","");
			tagTable.put("meta","");
			tagTable.put("multicol","");
			tagTable.put("nobr","");
			tagTable.put("noembed","");
			tagTable.put("noframes","");
			tagTable.put("noscript","");
			tagTable.put("object","");
			tagTable.put("ol","");
			tagTable.put("optgroup","");
			tagTable.put("option","");
			tagTable.put("p","");
			tagTable.put("param","");
			tagTable.put("plaintext","");
			tagTable.put("pre","");
			tagTable.put("q","");
			tagTable.put("s","");
			tagTable.put("samp","");
			tagTable.put("script","");
			tagTable.put("select","");
			tagTable.put("server","");
			tagTable.put("small","");
			tagTable.put("spacer","");
			tagTable.put("span","");
			tagTable.put("strike","");
			tagTable.put("strong","");
			tagTable.put("style","");
			tagTable.put("sub","");
			tagTable.put("sup","");
			tagTable.put("table","");
			tagTable.put("tbody","");
			tagTable.put("td","");
			tagTable.put("textarea","");
			tagTable.put("tfoot","");
			tagTable.put("th","");
			tagTable.put("thead","");
			tagTable.put("title","");
			tagTable.put("tr","");
			tagTable.put("tt","");
			tagTable.put("u","");
			tagTable.put("ul","");
			tagTable.put("var","");
			tagTable.put("wbr","");
			tagTable.put("xmp","");
		} catch (NullPointerException e) {
		}
	}


  private static Hashtable keywordTable = new Hashtable();

  static {
    try {
      keywordTable.put("abbr","");
      keywordTable.put("above","");
      keywordTable.put("accept-charset","");
      keywordTable.put("accesskey","");
      keywordTable.put("action",	"");
      keywordTable.put("align",	"");
      keywordTable.put("alt",	"");
      keywordTable.put("alink",			"");
      keywordTable.put("archive",	"");
      keywordTable.put("axis",		"");

      keywordTable.put("background",			"");
      keywordTable.put("behavior",			"");
      keywordTable.put("below",			"");
      keywordTable.put("bgcolor",			"");
      keywordTable.put("bgproperties",			"");
      keywordTable.put("border",			"");
      keywordTable.put("bordercolor",			"");
      keywordTable.put("bordercolordark",			"");
      keywordTable.put("bordercolorlight",			"");

      keywordTable.put("cellpadding",			"");
      keywordTable.put("cellspacing",			"");
      keywordTable.put("challenge",			"");
      keywordTable.put("char",			"");
      keywordTable.put("charoff",			"");
      keywordTable.put("charset",			"");
      keywordTable.put("checked",			"");
      keywordTable.put("cite",			"");
      keywordTable.put("class",			"");
      keywordTable.put("classid",			"");
      keywordTable.put("clear",			"");
      keywordTable.put("clip",			"");
      keywordTable.put("code",			"");
      keywordTable.put("codebase",			"");
      keywordTable.put("codetype",			"");
      keywordTable.put("color",			"");
      keywordTable.put("cols",			"");
      keywordTable.put("colspan",			"");
      keywordTable.put("compact",			"");
      keywordTable.put("controls",			"");
      keywordTable.put("content",			"");
      keywordTable.put("coords",			"");


      keywordTable.put("data",			"");
      keywordTable.put("datetime",			"");
      keywordTable.put("declare",			"");
      keywordTable.put("defer",			"");
      keywordTable.put("dir",			"");
      keywordTable.put("direction",			"");
      keywordTable.put("disable",			"");
      keywordTable.put("dynsrc",			"");

      keywordTable.put("enctype",			"");

      keywordTable.put("face",			"");
      keywordTable.put("for",			"");
      keywordTable.put("frame",			"");
      keywordTable.put("frameborder",			"");
      keywordTable.put("framespacing",			"");

      keywordTable.put("gutter",			"");

      keywordTable.put("headers",			"");
      keywordTable.put("height",			"");
      keywordTable.put("hidden",			"");
      keywordTable.put("href",			"");
      keywordTable.put("hreflang",			"");
      keywordTable.put("hspace",			"");
      keywordTable.put("http-equiv",			"");

      keywordTable.put("id",			"");
      keywordTable.put("ismap",			"");

      keywordTable.put("label",			"");
      keywordTable.put("lang",			"");
      keywordTable.put("language",			"");
      keywordTable.put("left",			"");
      keywordTable.put("leftmargin",			"");
      keywordTable.put("link",			"");
      keywordTable.put("loop",			"");
      keywordTable.put("longdesc",			"");
      keywordTable.put("lowsrc",			"");

      keywordTable.put("marginheight",			"");
      keywordTable.put("marginwidth",			"");
      keywordTable.put("mayscript",			"");
      keywordTable.put("maxlength",			"");
      keywordTable.put("media",			"");
      keywordTable.put("method",			"");
      keywordTable.put("multiple",			"");

      keywordTable.put("name",			"");
      keywordTable.put("nohref",			"");
      keywordTable.put("noresize",			"");
      keywordTable.put("noshape",			"");
      keywordTable.put("notab",			"");
      keywordTable.put("nowrap",			"");

      keywordTable.put("object",			"");
      keywordTable.put("onabort",			"");
      keywordTable.put("onblur",			"");
      keywordTable.put("onclick",			"");
      keywordTable.put("onchange",			"");
      keywordTable.put("ondblclick",			"");
      keywordTable.put("onerror",			"");
      keywordTable.put("onfocus",			"");
      keywordTable.put("onkeydown",			"");
      keywordTable.put("onkeypress",			"");
      keywordTable.put("onkeyup",			"");
      keywordTable.put("onmousedown",			"");
      keywordTable.put("onmousemove",			"");
      keywordTable.put("onmouseout",			"");
      keywordTable.put("onmouseover",			"");
      keywordTable.put("onmouseup",			"");
      keywordTable.put("onload",			"");
      keywordTable.put("onreset",			"");
      keywordTable.put("onsubmit",			"");
      keywordTable.put("onselect",			"");
      keywordTable.put("onunload",			"");

      keywordTable.put("palette",			"");
      keywordTable.put("pluginspage",			"");
      keywordTable.put("profile",			"");
      keywordTable.put("prompt",			"");

      keywordTable.put("readonly",			"");
      keywordTable.put("rel",			"");
      keywordTable.put("rev",			"");
      keywordTable.put("rows",			"");
      keywordTable.put("rowspan",			"");
      keywordTable.put("rules",			"");

      keywordTable.put("scheme",			"");
      keywordTable.put("scope",			"");
      keywordTable.put("scrolling",			"");
      keywordTable.put("scrollmount",			"");
      keywordTable.put("scrolldelay",			"");
      keywordTable.put("selected",			"");
      keywordTable.put("shape",			"");
      keywordTable.put("shapes",			"");
      keywordTable.put("size",			"");
      keywordTable.put("span",			"");
      keywordTable.put("src",			"");
      keywordTable.put("start",			"");
      keywordTable.put("style",			"");
      keywordTable.put("standby",			"");
      keywordTable.put("summary",			"");

      keywordTable.put("tabindex",			"");
      keywordTable.put("taborder",			"");
      keywordTable.put("target",			"");
      keywordTable.put("text",			"");
      keywordTable.put("title",			"");
      keywordTable.put("topmargin",			"");
      keywordTable.put("top",			"");
      keywordTable.put("type",			"");

      keywordTable.put("units",			"");
      keywordTable.put("usemap",			"");

      keywordTable.put("valign",			"");
      keywordTable.put("value",			"");
      keywordTable.put("valuetype",			"");
      keywordTable.put("version",			"");
      keywordTable.put("visibility",			"");
      keywordTable.put("vlink",			"");
      keywordTable.put("vspace",			"");

      keywordTable.put("width",			"");
      keywordTable.put("wrap",			"");

      keywordTable.put("z-index",			"");
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

