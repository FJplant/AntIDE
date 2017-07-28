/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/DiagramView.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: DiagramView.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:47p
 * Updated in $/AntIDE/source/ant/designer/methoddesigner
 * 
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-09-19   Time: 1:23p
 * Updated in $/JavaProjects/src/ant/designer/methoddesigner
 *
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-09-19   Time: 11:56a
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 * Diagram View
 */


package ant.designer.methoddesigner;

import com.sun.java.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 *  Diagram View class
 *
 */


class DiagramView extends JPanel
{
  Diagram diagram;

  public DiagramView(Diagram _diagram)
  {
    diagram = _diagram;
  }

}






