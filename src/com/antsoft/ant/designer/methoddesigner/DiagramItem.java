/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/DiagramItem.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: DiagramItem.java $
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
 * User: Flood        Date: 98-09-18   Time: 4:48p
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 * Diagram Item
 */


package ant.designer.methoddesigner;



/**
 *
 *  diagram item class (Method desiger)
 *
 */


abstract class DiagramItem
{
  // UNIT ITEM
  public static final int UNIT_WIDTH = 16;
  public static final int UNIT_HEIGHT = 16;

  // UNIT LINE
  public static final int UNIT_LWIDTH = 16;
  public static final int UNIT_LHEIGHT = 16;


	DiagramItem parent;

  protected DiagramBound bound;

  /**
   *
   *  constructor
   *
   */

  DiagramItem()
  {
    parent = null;
    bound = null;
  }

  void setParent(DiagramItem _parent)
  {
   parent = _parent;
  }

  /**
   *
   *  return bounding info.
   *
   */

	final public DiagramBound getBound()
  {
    if(bound == null) calcBound();
    return bound;
  }

  /**
   *
   *  caculate bound
   *
   */

  abstract public void calcBound();

}



