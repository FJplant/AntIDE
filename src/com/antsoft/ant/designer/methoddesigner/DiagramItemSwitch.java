/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/DiagramItemSwitch.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: DiagramItemSwitch.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:47p
 * Updated in $/AntIDE/source/ant/designer/methoddesigner
 * 
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-09-19   Time: 2:21p
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 */


package ant.designer.methoddesigner;


import java.util.*;

/**
 *
 *  switch class ( Method Designer )
 *
 */


class DiagramItemSwitch extends DiagramItem
{

  Vector cases = new Vector();

  /**
   *
   *  insert case item at 'pos'.
   *
   */

  public void insertCase(DiagramItemCase item,int pos)
  {
   cases.insertElementAt(item,pos);
  }

  /**
   *
   *  caculate bound
   *
   */

  public void calcBound()
  {
    bound = new DiagramBound();

    DiagramItem item;
    for(int i = items.size()-1; i >= 0 ; i--)
    {
      item = (DiagramItem) items.elementAt(i);
      bound.boundRightBellow(item.getBound());
    }

  }

}


