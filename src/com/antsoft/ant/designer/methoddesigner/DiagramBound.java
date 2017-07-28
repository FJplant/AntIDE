/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/DiagramBound.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: DiagramBound.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:47p
 * Updated in $/AntIDE/source/ant/designer/methoddesigner
 * 
 * *****************  Version 3  *****************
 * User: Flood        Date: 98-09-19   Time: 2:22p
 * Updated in $/JavaProjects/src/ant/designer/methoddesigner
 *
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-09-19   Time: 1:23p
 * Updated in $/JavaProjects/src/ant/designer/methoddesigner
 *
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-09-19   Time: 12:36p
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 * Diagram Bound
 */


package ant.designer.methoddesigner;


/**
 *
 *  diagram bound class (Method desiger)
 *
 */


class DiagramBound implements Cloneable
{

  int width,height;
  int centerline;

  public int getWidth() { return width;}
  public int getHeight() { return height;}
  public int getLeftWidth() { return centerline;};
  public int getRightWidth() { return width - centerline;};
  public int getCenterline() { return centerline;}

  public DiagramBound()
  {
    width = height = centerline = 0;
  }

  public DiagramBound(int _w,int _h,int _c)
  {
    width = _w;
    height = _h;
    centerline = _c;
  }


  package void setBound(int _w,int _h,int _c)
  {
    width = _w;
    height = _h;
    centerline = _c;
  }

  package void setHeight(int c)
  {
    height = c;
  }

  package void setLWidth(int c)
  {
    width += c - centerline;
    centerline = c;
  }
  package void setRWidth(int c)
  {
    width = centerline + c;
  }

  package void incWidth(int c)
  {
    width += c;
    centerline += c / 2;
  }

  package void incLWidth(int c)
  {
    centerline += c;
    width += c;
  }

  package void incRWidth(int c)
  {
    width += c;
  }

  package void incHeight(int c)
  {
    height += c;
  }


  package void boundBellow(DiagramBound bound)
  {
    incHeight(DiagramItem.UNIT_LHEIGHT + bound.getHeight());

    if(getLWidth() < bound.getLWidth())
      setLWidth(bound.getLWidth());

    if(getRWidth() < bound.getRWidth())
      setRWidth(bound.getRWidth());
  }

  package void boundRight(DiagramBound bound)
  {
    if(getHeight() < bound.getHeight())
      setHeight(bound.getHeight());

    incRWidth(DiagramItem.UNIT_LWIDTH + bound.getWidth());
  }

  package void boundRightBellow(DiagramBound bound)
  {
    incHeight(DiagramItem.UNIT_LHEIGHT + bound.getHeight());

    if(getRWidth() < DiagramItem.UNIT_LWIDTH + bound.getWidth())
      setRWidth(DiagramItem.UNIT_LWIDTH + bound.getWidth());
  }

  public Object clone()
  {
     return new DiagramBound(width,height,centerline);
  }


}




