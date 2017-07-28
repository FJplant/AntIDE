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
 *  새로이 실행한 Java Program의 Output 내용을 출력해 주는 Panel이다.
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
    
    // DocumentEvent를 Listen해서 scroll을 하니깐 프로그램이 locked되었다.
    // 이유를 찾은 다음에 처리하도록 하자. by 영모
    // output.getDocument().addDocumentListener( this );
	
    outputScroller = new JScrollPane(output);
    add( outputScroller, BorderLayout.CENTER );
  }

	/**
	 *  output내용을 넣는다.
	 */
  public void appendText(String text) {
    if (output != null) {
    	output.append(text);
    	scrollToLast();
    }
  }
  
	/**
	 *  Output내용을 삭제한다.
	 */
  public void clear() {
    if (output != null) output.setText("");
  }

	/**
	 *  출력 패널을 얻는다.
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
	 * output의 가장 아래로 스크롤 한다.
	 */
	private void scrollToLast( ) {
   	// 텍스트가 추가 되었으면, 가장 아래 위치로 스크롤 한다.
   	JScrollBar vBar = outputScroller.getVerticalScrollBar();
   	vBar.setValue(vBar.getMaximum());
	}
}
/*
 * $Log: Output.java,v $
 * Revision 1.5  1999/08/31 04:53:05  multipia
 * 프로그램이 lock되는 것 방지
 *
 * Revision 1.4  1999/08/30 08:07:24  remember
 * no message
 *
 * Revision 1.3  1999/08/30 02:02:18  multipia
 * scroll이 중복으로 되는 것을 방지
 *
 * Revision 1.2  1999/08/27 08:12:39  multipia
 * 출력 창의 가장 아래로 스크롤 되도록 수정
 *
 * Revision 1.1  1999/08/25 07:18:17  multipia
 * no message
 *
 */
