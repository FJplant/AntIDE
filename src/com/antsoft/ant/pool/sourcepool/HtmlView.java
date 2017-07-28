/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 */
package com.antsoft.ant.pool.sourcepool;

import java.awt.*;
import javax.swing.text.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.defaultproperty.*;

/**
 * View that uses the lexical information to determine the
 * style characteristics of the text that it renders.  This
 * simply colorizes the various tokens and assumes a constant
 * font family and size.
 * 
 * @author Kim, Sung-Hoon.
 */
public final class HtmlView extends AntView {
  private HtmlContext context;
  private Segment segment = new Segment();
  private HtmlDocument doc;
  private Color lastColor, fgColor;
  private static Color commentColor = Main.property.getColor(ColorPanel.COMMENT);

  /**
   * Construct a simple colorized view of java
   * text.
   */
  public HtmlView( HtmlContext context, Element elem) {
    super(elem);
    this.context = context;
    HtmlDocument oldDoc = doc;
    doc = (HtmlDocument)getDocument();
    lexer =(HtmlDocument.Scanner) doc.createScanner();
    lexerValid = false;

    // 현재는 무조건 SyntaxColoring 수행
    setSyntaxColoring(true);
  }

  public static void setCommentColor(Color c){
    commentColor = c;
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
    if ( isSyntaxColoring() ) {
      lastColor = null;
      int mark = p0;
      for (; p0 < p1; ) {
        updateScanner(p0);
        int p = Math.min(lexer.getEndOffset(), p1);
        p = (p <= p0) ? p1 : p;
        p0 = lexer.getStartOffset();

        switch (lexer.token) {
          case HtmlDocument.KEYWORD :
            fgColor = Main.property.getColor(ColorPanel.KEYWORD);
            break;
          case HtmlDocument.STRING :
            fgColor = Main.property.getColor(ColorPanel.CONSTANT);
            break;
          case HtmlDocument.COMMENT :
            fgColor = Main.property.getColor(ColorPanel.COMMENT);
            break;
          default :
            fgColor = Main.property.getColor(ColorPanel.STRING);
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

        //System.out.println("SS : " + p + "   " + ret);

        lexer.setRange(ret, doc.getLength());
        lexerValid = true;
      }
      while (lexer.getEndOffset() <= p) {
				//System.out.println(" endOffset : "+lexer.getEndOffset()+" pos : "+p);
        lexer.scan();
      }
    } catch (Throwable e) {
      // can't adjust scanner... calling logic
      // will simply render the remaining text.
      e.printStackTrace();
    }
  }

  HtmlDocument.Scanner lexer;
  boolean lexerValid;
}

