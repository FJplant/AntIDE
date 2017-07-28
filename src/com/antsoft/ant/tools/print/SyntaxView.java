/*
 * $Header: /AntIDE/source/ant/tools/print/SyntaxView.java 2     99-05-16 11:59p Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 */
package com.antsoft.ant.tools.print;

import javax.swing.text.*;
import java.awt.*;
import java.util.*;

/**
 * A Swing view implementation that colorizes lines of a
 * <code>SyntaxDocument</code> using a <code>TokenMarker</code>.<p>
 *
 * This class should not be used directly; a <code>SyntaxEditorKit</code>
 * should be used instead.
 *
 * @author Slava Pestov
 * @version $Id: SyntaxView.java,v 1.16 1999/03/13 08:50:39 sp Exp $
 */
public class SyntaxView extends PlainView
{
	/**
	 * Creates a new <code>SyntaxView</code> for painting the specified
	 * element.
	 * @param elem The element
	 */
	public SyntaxView(Element elem)
	{
		super(elem);
		line = new Segment();
	}
	
	/**
	 * Paints the specified line.
	 * <p>
	 * This method performs the following:
	 * <ul>
	 * <li>Gets the token marker and color table from the current document,
	 * typecast to a <code>SyntaxDocument</code>.
	 * <li>Tokenizes the required line by calling the
	 * <code>markTokens()</code> method of the token marker.
	 * <li>Paints each token, obtaining the color by looking up the
	 * the <code>Token.id</code> value in the color table.
	 * </ul>
	 * If either the document doesn't implement
	 * <code>SyntaxDocument</code>, or if the returned token marker is
	 * null, the line will be painted with no colorization.
	 *
	 * @param lineIndex The line number
	 * @param g The graphics context
	 * @param x The x co-ordinate where the line should be painted
	 * @param y The y co-ordinate where the line should be painted
	 */
	public void drawLine(int lineIndex, Graphics g, int x, int y)
	{
		SyntaxDocument syntaxDocument;
		TokenMarker tokenMarker;

		Document document = getDocument();

		if(document instanceof SyntaxDocument)
		{
			syntaxDocument = (SyntaxDocument)document;
			tokenMarker = syntaxDocument.getTokenMarker();
		}
		else
		{
			syntaxDocument = null;
			tokenMarker = null;
		}

		FontMetrics metrics = g.getFontMetrics();
		Color def = getDefaultColor();

		try
		{
			Element lineElement = getElement()
				.getElement(lineIndex);
			int start = lineElement.getStartOffset();
			int end = lineElement.getEndOffset();

			document.getText(start,end - (start + 1),line);

			if(tokenMarker == null)
			{
				g.setColor(def);
				Utilities.drawTabbedText(line,x,y,g,this,0);
			}
			else
			{
				Dictionary colors = syntaxDocument.getColors();
				Token tokens = tokenMarker.markTokens(line,
					lineIndex);
				int offset = 0;
				for(; tokens != null; tokens = tokens.next)
				{
					int length = tokens.length;
					Color color;
					String id = tokens.id;
					if(id == null)
						color = def;
					else
						color = (Color)colors.get(id);
					g.setColor(color == null ?
						   def : color);
				   	line.count = length;
					x = Utilities.drawTabbedText(line,x,
						   y,g,this,offset);
					line.offset += length;
					offset += length;
					if(!tokens.nextValid)
						break;
				}
			}
		}
		catch(BadLocationException bl)
		{
			// shouldn't happen
			bl.printStackTrace();
		}
	}

	// protected members
	protected Color getDefaultColor()
	{
		return getContainer().getForeground();
	}

	// private members
	private Segment line;
}

