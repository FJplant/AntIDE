/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/JavaView.java,v 1.2 1999/08/05 05:09:35 strife Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 * $History: JavaView.java $
 * 
 * *****************  Version 11  *****************
 * User: Remember     Date: 99-06-21   Time: 3:55p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 99-06-10   Time: 6:05p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 *
 */
package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.text.*;

/**
 * View that uses the lexical information to determine the
 * style characteristics of the text that it renders.  This
 * simply colorizes the various tokens and assumes a constant
 * font family and size.
 */
public final class JavaView extends PlainView {
  private boolean syntaxColoring = true;
  private JavaContext context;
  private Segment segment = new Segment();
  private JavaDocument doc;
  private Color lastColor, fgColor;
  private static Color commentColor = new Color(0, 153, 0);
	private Color string = new Color(0, 0, 0);
	private Color background = new Color(255, 255, 255);
	private Color keyword = new Color(0, 0, 255);
	private Color comment = new Color(0, 153, 0);
	private Color constant = new Color(255, 0, 0);

  /**
   * Construct a simple colorized view of java
   * text.
   */
  public JavaView( JavaContext context, Element elem) {
    super(elem);
    this.context = context;
    JavaDocument oldDoc = doc;
    doc = (JavaDocument) getDocument();
    lexer = doc.createScanner();
    lexerValid = false;

    // 현재는 무조건 SyntaxColoring 수행
    setSyntaxColoring(true);
  }

  public static void setCommentColor(Color c){
    commentColor = c;
  }

  private void setSyntaxColoring( boolean b ) {
    syntaxColoring = b;
  }

  private boolean isSyntaxColoring( ) {
    return syntaxColoring;
  }

  protected int getTabSize() {
    return 2;
  }

  /**
   * Renders using the given rendering surface and area
   * on that surface.  This is implemented to invalidate
   * the lexical scanner after rendering so that the next
   * request to drawUnselectedText will set a new range
   * for the scanner.
   *
   * @param g the rendering surface to use
   * @param a the allocated region to render into
   *
   * @see View#paint
   */
  public void paint(Graphics g, Shape a) {
    super.paint(g, a);
    lexerValid = false;
  }


  /**
   * Renders the given range in the model as normal unselected
   * text.  This is implemented to paint colors based upon the
   * token-to-color translations.  To reduce the number of calls
   * to the Graphics object, text is batched up until a color
   * change is detected or the entire requested range has been
   * reached.
   *
   * @param g the graphics context
   * @param x the starting X coordinate
   * @param y the starting Y coordinate
   * @param p0 the beginning position in the model
   * @param p1 the ending position in the model
   * @returns the location of the end of the range
   * @exception BadLocationException if the range is invalid
   */
  protected int drawUnselectedText(Graphics g, int x, int y,
         int p0, int p1) throws BadLocationException {

    Element rootelem = doc.getDefaultRootElement();
    if ( syntaxColoring ) {
      lastColor = null;
      int mark = p0;
      for (; p0 < p1; ) {
        updateScanner(p0);
        int p = Math.min(lexer.getEndOffset(), p1);
        p = (p <= p0) ? p1 : p;
        p0 = lexer.getStartOffset();

        switch (lexer.token) {
          case JavaDocument.KEYWORD :
            if (context.getKeywordColor() != null) fgColor = context.getKeywordColor();
            else fgColor = keyword;
            break;
          case JavaDocument.STRING :
            if (context.getConstantColor() != null) fgColor = context.getConstantColor();
            else fgColor = constant;
            break;
          case JavaDocument.COMMENT :
            if (context.getCommentColor() != null) fgColor = context.getCommentColor();
            else fgColor = comment;
            break;
          default :
            if (context.getStringColor() != null) fgColor = context.getStringColor();
            else fgColor = string;
        }


        // 주석문 내인지를 Check 한다.
        // elem의 Attribute 중에서 Comment Attribute가 있으면,
        if (fgColor != lastColor && lastColor != null) {
          // color change, flush what we have
          g.setColor(lastColor);
          doc.getText( mark, p0 - mark, segment );
          x = Utilities.drawTabbedText(segment, x, y, g, this, mark );
          mark = p0;
        }

        if(lastColor != null) lastColor = null;

        lastColor = fgColor;
        p0 = p;
      }
      // flush remaining
      g.setColor(lastColor);

      if ( p0 > mark ) {
        doc.getText( mark, p0 - mark, segment );
        x = Utilities.drawTabbedText(segment, x, y, g, this, mark );
      }
    } else {
      // no syntax coloring
      doc.getText( p0, p1 - p0, segment );
      g.setColor( Color.black );
      x = Utilities.drawTabbedText(segment, x, y, g, this, p0 );
    }

    rootelem = null;
    return x;
  }

  /**
   * Update the scanner (if necessary) to point to the appropriate
   * token for the given start position needed for rendering.
   */
  private void updateScanner(int p) {
    try {
      if (! lexerValid) {
        int ret = doc.getScannerStart(p);

        //stem.out.println("SS : " + p + "   " + ret);

        lexer.setRange(ret, doc.getLength());
        lexerValid = true;
      }
      while (lexer.getEndOffset() <= p) {
        lexer.scan();
      }
    } catch (Throwable e) {
      // can't adjust scanner... calling logic
      // will simply render the remaining text.
      e.printStackTrace();
    }
  }

  JavaDocument.Scanner lexer;
  boolean lexerValid;
}

