/*
 * $Id: Output.java,v 1.5 1999/08/31 04:53:05 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;               

/**
 *  ������ ������ Java Program�� Output ������ ����� �ִ� Panel�̴�.
 *
 *  @author Kwon, Young Mo
 */
public class Output extends JPanel implements DocumentListener {
  private JTextArea output = new JTextArea();

  private JScrollPane outputScroller;
	
	/**
	 *  Constructor
	 */
  public Output() {
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void uiInit() throws Exception {
    setLayout(new BorderLayout());
    
    // DocumentEvent�� Listen�ؼ� scroll�� �ϴϱ� ���α׷��� locked�Ǿ���.
    // ������ ã�� ������ ó���ϵ��� ����. by ����
    // output.getDocument().addDocumentListener( this );
	
    outputScroller = new JScrollPane(output);
    add( outputScroller, BorderLayout.CENTER );
  }

	/**
	 *  output������ �ִ´�.
	 */
  public void appendText(String text) {
    if (output != null) {
    	output.append(text);
    	scrollToLast();
    }
  }
  
	/**
	 *  Output������ �����Ѵ�.
	 */
  public void clear() {
    if (output != null) output.setText("");
  }

	/**
	 *  ��� �г��� ��´�.
	 */
  public JTextArea getOutput() {
  	return output;
  }

	public void changedUpdate( DocumentEvent e )	{
		//TO DO 
	}

	public void insertUpdate( DocumentEvent e )	{
		scrollToLast();
	}

	public void removeUpdate( DocumentEvent e )	{
		scrollToLast();
	}

	/**
	 * output�� ���� �Ʒ��� ��ũ�� �Ѵ�.
	 */
	private void scrollToLast( ) {
   	// �ؽ�Ʈ�� �߰� �Ǿ�����, ���� �Ʒ� ��ġ�� ��ũ�� �Ѵ�.
   	JScrollBar vBar = outputScroller.getVerticalScrollBar();
   	vBar.setValue(vBar.getMaximum());
	}
}
/*
 * $Log: Output.java,v $
 * Revision 1.5  1999/08/31 04:53:05  multipia
 * ���α׷��� lock�Ǵ� �� ����
 *
 * Revision 1.4  1999/08/30 08:07:24  remember
 * no message
 *
 * Revision 1.3  1999/08/30 02:02:18  multipia
 * scroll�� �ߺ����� �Ǵ� ���� ����
 *
 * Revision 1.2  1999/08/27 08:12:39  multipia
 * ��� â�� ���� �Ʒ��� ��ũ�� �ǵ��� ����
 *
 * Revision 1.1  1999/08/25 07:18:17  multipia
 * no message
 *
 */
