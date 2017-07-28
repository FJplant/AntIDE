/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/tools/htmlgenerator/HtmlViewer.java,v 1.4 1999/08/19 04:31:48 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 */
package com.antsoft.ant.tools.htmlgenerator;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.WindowDisposer;

/**
 * Html Viewer
 *
 * @author kim sang kyun
 */
public class HtmlViewer extends JDialog
{
  private JEditorPane pane;
  private JButton okBtn;

  public HtmlViewer(String fileName){
    super(MainFrame.mainFrame, fileName, false);

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    //button panel
    okBtn = new JButton("OK");
    okBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        dispose();
      }});

    pane = new JEditorPane();
    JScrollPane scrPane = new JScrollPane(pane);
    getContentPane().add(scrPane, BorderLayout.CENTER);
    getContentPane().add(okBtn, BorderLayout.SOUTH);

    try{
      pane.setPage(makeURL(fileName));
      pane.setEditable(false);
    }catch(IOException e){}  

    setSize(800, 800);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,  (screenSize.height - dlgSize.height) / 2);

  }

  class SafeLoader extends Thread{
    private JEditorPane pane;
    private URL url;
    public SafeLoader(JEditorPane pane, URL url){
      this.pane = pane;
      this.url = url;
    }

    public void run(){
      try{
        pane.setPage(url);
      }catch(IOException e){e.printStackTrace();}
    }
  }


  private URL makeURL(String fileName){
    File html = new File(fileName);
    URL ret = null;

    try{
      ret = new URL("file://///"+html.getAbsolutePath());
    }catch(MalformedURLException e){
      e.printStackTrace();
    }
    return ret;
  }
}
