/*
 * $Id: MainFrame_AboutBox.java,v 1.11 1999/08/31 16:16:48 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.11 $
 * Author:       Baek, Jin-woo
 *               Kwon, Young Mo
 * $History: MainFrame_AboutBox.java $
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-08   Time: 6:43p
 * Updated in $/AntIDE/source/ant/main
 * Image를 static으로 loading하도록 변경
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-06   Time: 3:06a
 * Updated in $/AntIDE/source/ant/main
 * 출력 메시지 변경
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-04-24   Time: 12:54a
 * Updated in $/AntIDE/source/ant/main
 * About Box의 내용 변경
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:14p
 * Created in $/Ant/src/ant
 * Main class.
 * 실행시 이 클래스로 실행한다.
 *
 */
package com.antsoft.ant.main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.antsoft.ant.util.MemoryInfoPanel;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.util.WindowDisposer;

/**
 *  class MainFrame_AboutBox
 *
 *  @author Jinwoo Baek
 */
public class MainFrame_AboutBox extends JDialog implements ActionListener {
  JPanel topPanel = null;
  JPanel contentPanel = new JPanel();
  JPanel bottomPanel = new JPanel();
  JPanel logoPanel = new JPanel();
  JPanel descPanel = new JPanel();
	JPanel memoryInfoPanel = new MemoryInfoPanel();
	
  JButton okBtn = new JButton();
	
  JLabel imageControl1;
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();

  String oops = "Hi! I'm Oops.";
  String product = "Ant";
  String version = "1.0 pre Beta";
  String copyright = "Copyright (c) 1998-1999 Antsoft Co.";
  String comments = "All rights reserved.";
  String website = "http://www.antsoft.co.kr";
  String email = "E-Mail: antsoft@antsoft.co.kr";

	/**
	 *  Constructor
	 */
  public MainFrame_AboutBox(Frame parent, String title, boolean isModal) {
    super(parent, title, isModal);
    try  {
      uiInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    pack();
  }

  private void uiInit() throws Exception {
  	// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    imageControl1 = new JLabel(ImageList.aboutLogo);

    this.setTitle("About");
    topPanel = (JPanel)getContentPane();
    
    topPanel.setLayout(borderLayout1);
    contentPanel.setLayout(borderLayout2);
    logoPanel.setLayout(new BorderLayout());
    logoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    logoPanel.add(imageControl1, BorderLayout.CENTER);
    logoPanel.add(new JLabel(oops, JLabel.CENTER), BorderLayout.SOUTH);

    descPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    descPanel.setLayout( new GridLayout(0, 1) );

    descPanel.add(new JLabel(product+version), null);
    descPanel.add(new JLabel(copyright), null);
    descPanel.add(new JLabel(comments), null);
    descPanel.add(new JLabel(website), null);
    descPanel.add(new JLabel(email), null);
    
    contentPanel.add( descPanel, BorderLayout.CENTER );
    contentPanel.add( memoryInfoPanel, BorderLayout.SOUTH );

    okBtn.setText("Ok");
    okBtn.addActionListener(this);

    bottomPanel.setLayout(flowLayout1);
    bottomPanel.add( okBtn );
    topPanel.add(bottomPanel, BorderLayout.SOUTH);
    topPanel.add(logoPanel, BorderLayout.WEST);
    topPanel.add(contentPanel, BorderLayout.CENTER);

    setSize(350, 230);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
  }

	/**
	 *  Event Handler
	 */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == okBtn) {
      ok();
    }
  }

  void ok() {
    WindowDisposer.disposeWindow(this);
  }
}
/*
 * $Log: MainFrame_AboutBox.java,v $
 * Revision 1.11  1999/08/31 16:16:48  multipia
 * Garbage Collection 버튼 추가
 * Memory 관련 정보를 Panel로 분리
 *
 */

