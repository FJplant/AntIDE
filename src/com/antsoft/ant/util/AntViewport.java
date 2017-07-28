/*
 * $Id: AntViewport.java,v 1.9 1999/08/25 10:53:14 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 */
package com.antsoft.ant.util;

import javax.swing.*;
import java.awt.*;

/**
 * Ant Viewport class. source editor에 맞게 고쳤음
 *
 * @author kim sang kyun
 */
public class AntViewport extends JViewport {
  int unitIncrement;
  int charWidth;

  public static final int GENERAL_MODE = 0;
  public static final int CTRL_UP_DOWN_MODE = 1;
  public static final int BLOCK_PASTE_MODE = 2;

  public static int mode = GENERAL_MODE;

  public AntViewport() {
    super();
    this.setBackingStoreEnabled(false);
  }

  public void setUnitIncrement(int newIncre){
    unitIncrement = newIncre;
  }

  private void validateView() {
    Component validateRoot = null;

   	/* Find the first JComponent ancestor of this component whose
	   * isValidateRoot() method returns true.
  	 */
      for(Component c = this; c != null; c = c.getParent()) {
	      if ((c instanceof CellRendererPane) || (c.getPeer() == null)) {
    		return;
	    }

	    if ((c instanceof JComponent) &&(((JComponent)c).isValidateRoot())) {
    		validateRoot = c;
		    break;
	    }
    }
	}

    private int positionAdjustment(int parentWidth, int childWidth, int childAt, boolean isVertical)    {

        //   +-----+
        //   | --- |     No Change
        //   +-----+
        if (childAt >= 0 && childWidth + childAt <= parentWidth)    {
            return 0;
        }

        //   +-----+
        //  ---------   No Change
        //   +-----+
        if (childAt <= 0 && childWidth + childAt >= parentWidth) {
            return 0;
        }

        //   +-----+          +-----+
        //   |   ----    ->   | ----|
        //   +-----+          +-----+
        // VK-PGDOWN
        if (childAt > 0 && childWidth <= parentWidth)    {

            if(isVertical){
              if(mode == CTRL_UP_DOWN_MODE ) return -unitIncrement;
              else if(mode == BLOCK_PASTE_MODE) {
                return -childAt;
              }
              else if(childWidth <= unitIncrement) return -unitIncrement;
              else return -(parentWidth - unitIncrement) + (parentWidth%unitIncrement);
            }
            else{
              return -childAt + parentWidth - childWidth;
            }
        }

        //   +-----+             +-----+
        //   |  --------  ->     |--------
        //   +-----+             +-----+
        if (childAt >= 0 && childWidth >= parentWidth)   {
            return -childAt;
        }

        //   +-----+          +-----+
        // ----    |     ->   |---- |
        //   +-----+          +-----+
        // VK_PGUP
        if (childAt <= 0 && childWidth <= parentWidth)   {
            if(isVertical){
              if(mode == CTRL_UP_DOWN_MODE) return unitIncrement;
              else if(childWidth <= unitIncrement) return unitIncrement;
              else return (parentWidth - unitIncrement) - (parentWidth%unitIncrement);
            }
            else{
              return -childAt;
            }
        }

        //   +-----+             +-----+
        //-------- |      ->   --------|
        //   +-----+             +-----+
        if (childAt < 0 && childWidth >= parentWidth)    {
            return -childAt + parentWidth - childWidth;
        }

        return 0;
    }

  /**
   * Overridden to scroll the view so that Rectangle within the
   * view becomes visible.
   *
   * @param contentRect the Rectangle to display
   */

  public void scrollRectToVisible(Rectangle contentRect) {
    Component view = getView();

    if (view == null) {
        return;
    } else {
      if (!view.isValid()) {
        validateView();
      }
      int dx = 0, dy = 0;
      Rectangle bounds = getBounds();

      dx = positionAdjustment(bounds.width, contentRect.width, contentRect.x, false);
      dy = positionAdjustment(bounds.height, contentRect.height, contentRect.y, true);

      if (dx != 0 || dy != 0) {
        Point viewPosition = getViewPosition();
        setViewPosition(new Point(viewPosition.x - dx, viewPosition.y - dy));
        scrollUnderway = false;
      }
    }
  }
}
