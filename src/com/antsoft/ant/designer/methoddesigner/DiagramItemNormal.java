/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/DiagramItemNormal.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: DiagramItemNormal.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:47p
 * Updated in $/AntIDE/source/ant/designer/methoddesigner
 * 
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-09-19   Time: 2:22p
 * Updated in $/JavaProjects/src/ant/designer/methoddesigner
 * 
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-09-19   Time: 1:22p
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 * diagram structure
 */


package ant.designer.methoddesigner;


/**
 *
 *  normal
 *
 */


class DiagramItemNormal extends DiagramItem
{
  /**
   *
   *  caculate bound
   *
   */

  public void calcBound()
  {
   if(bound == null)
   {
     bound = new DiagramBound();
     bound.setBound(UNIT_WIDTH,UNIT_HEIGHT,UNIT_WIDTH / 2);
   }
  }
}


