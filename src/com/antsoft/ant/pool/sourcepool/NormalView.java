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
 */
public final class NormalView extends AntView {
  private AntDocument doc;
	Segment segment = new Segment();

  /**
   * Construct a simple colorized view of java
   * text.
   */
  public NormalView(Element elem) {
    super(elem);
    AntDocument oldDoc = doc;
    doc = (AntDocument) getDocument();
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

    doc.getText( p0, p1 - p0, segment );
    g.setColor( Color.black );
    x = Utilities.drawTabbedText(segment, x, y, g, this, p0 );

    return x;
  }
}

