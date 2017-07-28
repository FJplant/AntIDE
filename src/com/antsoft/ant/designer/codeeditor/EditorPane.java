/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/EditorPane.java,v 1.2 1999/07/15 07:19:29 itree Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 * $History: EditorPane.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-28   Time: 2:28p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 */

package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.pool.sourcepool.*;

/**
 *  class EditorPane
 *  TODO: ProjectExplorer�� �ִ� Editor ���� �ʵ� �� �޼ҵ���� �̰����� �ű⵵��
 *        �Ѵ�.
 *  @author Kwon, Young Mo
 */
public class EditorPane extends JPanel {
  // Opened File list
  private Vector openedList = new Vector(15, 5);
  private Vector openedListForSaveAll = new Vector(15, 5);

  // ���� �ֱٿ� ���µǾ��� source
  private SourceEntry lastOpenedSource;

  private TextViewPanel upperTvp, bottomTvp;
  private Point upperTvpPosition, bottomTvpPosition;

  // for GUI
  private JSplitPane vertSP;

  private EditorStatusBar statusBar = new EditorStatusBar();
  private ProjectExplorer projectExplorer;

  public EditorPane( ProjectExplorer explorer ) {
  }
}
