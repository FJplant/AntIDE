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
 *  ������ ������ Java Program�� Output ������ ����� �ִ� Frame�̴�.
 *  
 *  TODO: stdout, stderr, stdin�� �����ؼ� ����� �ִ� ����� �ʿ��� 
 *      ���̴�. stdin�� ��쿡�� �ٸ� ������ �̿��ؼ� �Է��� �� �� �ֵ���
 *      �ϸ� �� ���̴�.
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

    // DocumentEvent �� ó���ϱ� ���ؼ� ���
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
	 *  Output������ �����Ѵ�.
	 */
  public void clear() {
    if (outputStd != null) outputStd.getOutput().setText("");
    if (outputErr != null) outputErr.getOutput().setText("");
  }

	/**
	 *  �� ���̾�α׸� �ݴ´�.
	 */
  public void closeDlg() {
  	setVisible(false);
  }

	/**
	 *  ��� �г��� ��´�.
	 */
  public Output getStdOut() {
  	return outputStd;
  }
  
  /**
   *  ���� ��� �г��� ��´�.
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
	 *  ȭ���� ������ �ٲ� Ouput�� ���̰� ��ȯ�Ѵ�.
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
 * ����� �� �������� �ڵ����� ���̵��� ����
 *
 * Revision 1.7  1999/08/25 03:32:20  multipia
 * stdout�� stderr�� �����ؼ� ����ϵ��� ����
 *
 * Revision 1.6  1999/08/18 04:20:18  multipia
 * ���� ��½� ��½�Ÿ� ����.
 * ��� �� ���������� ������ ����.
 */
