/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/methoddesigner/MethodDesigner.java,v 1.3 1999/07/22 02:58:38 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: MethodDesigner.java $
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
 * User: Flood        Date: 98-09-18   Time: 4:53p
 * Created in $/JavaProjects/src/ant/designer/methoddesigner
 */


package ant.designer.methoddesigner;


import com.sun.java.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 *  Method Designer class
 *
 */


class MethodDesigner extends JFrame
{

	Diagram diagram;
  DiagramView view;

  public MethodDesigner()
  {
    // init

    diagram = new Diagram();

    // create menu

    JMenuBar mb = new JMenuBar();
    JMenu filemenu = new JMenu("file");
    setJMenuBar(mb);
    mb.add(filemenu);
    filemenu.add(new JMenuItem("open"));
    filemenu.add(new JMenuItem("close"));

    // layer
    view = new DiagramView(diagram);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(view);
  }

}


