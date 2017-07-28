/*
 * $Id: OutputFrame.java,v 1.9 1999/08/30 08:07:24 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.ImageList;             
import com.antsoft.ant.main.*;
import com.antsoft.ant.util.plaf.*;

/**
 *  새로이 실행한 Java Program의 Output 내용을 출력해 주는 Frame이다.
 *  
 *  TODO: stdout, stderr, stdin을 구분해서 출력해 주는 기능이 필요할 
 *      것이다. stdin의 경우에는 다른 색상을 이용해서 입력을 할 수 있도록
 *      하면 될 것이다.
 *
 *  @author Jinwoo Baek
 *  @author Kwon, Young Mo
 */
public class OutputFrame extends JFrame implements DocumentListener {
  private Output outputStd = new Output();
  private Output outputErr = new Output();

  private MainFrame mainFrm = null;
	private JTabbedPane tabPane;
	
  WindowAdapter windowHandler = new WindowAdapter() {
  	public void windowClosing(WindowEvent evt) {
    	if (mainFrm != null) mainFrm.setExecutionViewState(false);
    }
  };

	/**
	 *  Constructor
	 */
  public OutputFrame(String title, MainFrame frame) {
    super(title);
    this.mainFrm = frame;
    try  {
      uiInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void uiInit() throws Exception {
    getContentPane().setLayout(new BorderLayout());
		tabPane = new JTabbedPane(JTabbedPane.TOP);
		
    tabPane.addTab("stdout", outputStd);
    tabPane.addTab("stderr", outputErr);

    getContentPane().add(tabPane, BorderLayout.CENTER);
    setIconImage(ImageList.frameIcon.getImage());
    addWindowListener(windowHandler);

    // DocumentEvent 를 처리하기 위해서 등록
    outputStd.getOutput().getDocument().addDocumentListener(this);
    outputErr.getOutput().getDocument().addDocumentListener(this);
    setSize(500, 500);
    setLocation(200, 200);
  }

  public void finalize() {
    removeWindowListener(windowHandler);
    System.gc();
  }

	/**
	 *  Output내용을 삭제한다.
	 */
  public void clear() {
    if (outputStd != null) outputStd.getOutput().setText("");
    if (outputErr != null) outputErr.getOutput().setText("");
  }

	/**
	 *  현 다이얼로그를 닫는다.
	 */
  public void closeDlg() {
  	setVisible(false);
  }

	/**
	 *  출력 패널을 얻는다.
	 */
  public Output getStdOut() {
  	return outputStd;
  }
  
  /**
   *  에러 출력 패널을 얻는다.
   */
  public Output getStdErr() {
  	return outputErr;
  }

	public void changedUpdate( DocumentEvent e )	{
		//TO DO 
		changeTab( e );
	}

	public void insertUpdate( DocumentEvent e )	{
		//TO DO 
		changeTab( e );
	}

	public void removeUpdate( DocumentEvent e )	{
		//TO DO 
		changeTab( e );
	}
	
	/**
	 *  화면의 내용이 바뀐 Ouput을 보이게 전환한다.
	 */
	private void changeTab( DocumentEvent e ) {
		if ( e.getDocument() == outputStd.getOutput().getDocument() ) {
			if ( tabPane.getSelectedComponent() != outputStd )
				tabPane.setSelectedComponent(outputStd);
		} else if ( e.getDocument() == outputErr.getOutput().getDocument() ) {
			if ( tabPane.getSelectedComponent() != outputErr )
				tabPane.setSelectedComponent(outputErr);
		}
	}
}
/*
 * $Log: OutputFrame.java,v $
 * Revision 1.9  1999/08/30 08:07:24  remember
 * no message
 *
 * Revision 1.8  1999/08/27 07:08:40  multipia
 * 출력이 된 페이지가 자동으로 보이도록 수정
 *
 * Revision 1.7  1999/08/25 03:32:20  multipia
 * stdout과 stderr를 구분해서 출력하도록 수정
 *
 * Revision 1.6  1999/08/18 04:20:18  multipia
 * 내용 출력시 번쩍거림 없앰.
 * 출력 후 마지막으로 가도록 수정.
 */
