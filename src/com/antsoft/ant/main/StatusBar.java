/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/main/StatusBar.java,v 1.3 1999/07/22 03:00:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * Author:       Baek, Jin-woo
 * $History: StatusBar.java $
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-25   Time: 2:06a
 * Updated in $/AntIDE/source/ant/main
 * �޸� ������ ���� �� �ִ� ���� �߰���.
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:49p
 * Updated in $/AntIDE/source/ant/main
 * 
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:16p
 * Created in $/Ant/src/ant/designer/codeeditor
 * �ڹ� ���α׷��ֿ� �ؽ�Ʈ ������
 *
 */
package com.antsoft.ant.main;

import java.awt.*;
import javax.swing.*;

public class StatusBar extends JPanel {
  JLabel lblMessage = new JLabel();
  JLabel memoryUsage = new JLabel();
  
  GridLayout gridLayout1 = new GridLayout();

  public StatusBar() {
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void uiInit() throws Exception {
    lblMessage.setText("");

    lblMessage.setHorizontalAlignment(JLabel.LEFT);

    gridLayout1.setHgap(5);
    this.setLayout(gridLayout1);
//    this.setPreferredSize(new Dimension(0, 0));
    this.add(lblMessage, null);
    this.add(memoryUsage, null);
  }

	public void setText(String msg) {
		lblMessage.setText(msg);
	}

	public String getText() {
		return lblMessage.getText();
	}
	
	public void setMemoryUsage( long freeMemory, long totalMemory ) {
		String text = "Memory Usage: " + freeMemory + "/ " + totalMemory + "Kbytes";
		memoryUsage.setText(text);
	}
}
