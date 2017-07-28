/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/OptionPropertyDlg.java,v 1.5 1999/08/19 04:22:50 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.property.defaultproperty;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

// by kahn
//import com.antsoft.ant.main.MainFrame;
// by kahn

import com.antsoft.ant.property.*;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.pool.sourcepool.JavaView;
import com.antsoft.ant.util.MyCaret;
import com.antsoft.ant.util.WindowDisposer;

/**
 * option property dialog
 *
 * @author kim sang kyun
 * @author kim sung hoon
 */
public class OptionPropertyDlg extends JDialog {
	private EditorPanel editorTab;
  private FontPanel fontTab;
  private ColorPanel colorTab;
//  private CodeBeautifyPanel codeTab;
  private IntellisensePanel intelliTab;
  private JTabbedPane tabbedPane;
  private JButton okBtn, cancelBtn, helpBtn;
  private JFrame parent;
  private boolean isOK = false;
  private boolean isHelp = false;

  public OptionPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, "IDE Option Property", modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    tabbedPane = new JTabbedPane();

    editorTab = new EditorPanel();
    tabbedPane.addTab("Editor", editorTab);
    
    fontTab = new FontPanel();
    tabbedPane.addTab("Font", fontTab);
    tabbedPane.setSelectedIndex(0);

    colorTab = new ColorPanel();
    tabbedPane.addTab("Color", colorTab);

    /*
    codeTab = new CodeBeautifyPanel();
    tabbedPane.addTab("Code Beautify", codeTab);
    */

    intelliTab = new IntellisensePanel();
    tabbedPane.addTab("Intellisense", intelliTab);

    //button panel
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");
    // by kahn
    helpBtn.setActionCommand("Help");
    //MainFrame.helpBroker.enableHelp(helpBtn,"prop.option",null);
    // by kahn

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    //center panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(tabbedPane, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(420, 450);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(false);

    setInitProperty();
  }

  private void setInitProperty(){
    if(Main.property == null) return;
    if(Main.property.getSelectedFont() != null) fontTab.setSelectedFont(Main.property.getSelectedFont());
    if(Main.property.getColor(ColorPanel.KEYWORD) != null){
      colorTab.setColor(ColorPanel.KEYWORD, Main.property.getColor(ColorPanel.KEYWORD));
      colorTab.setColor(ColorPanel.COMMENT, Main.property.getColor(ColorPanel.COMMENT));
      colorTab.setColor(ColorPanel.CONSTANT, Main.property.getColor(ColorPanel.CONSTANT));
      colorTab.setColor(ColorPanel.STRING, Main.property.getColor(ColorPanel.STRING));
      colorTab.setColor(ColorPanel.BACKGROUND, Main.property.getColor(ColorPanel.BACKGROUND));
    }

    editorTab.setIndent(Main.property.isAutoIndentMode());
    editorTab.setInsert(Main.property.isInsertMode());
    editorTab.setUseTab(Main.property.isUseTabChar());
    editorTab.setGroupUndo(Main.property.isGroupUndo());
    editorTab.setSyntaxColoring(Main.property.isSynClring());
    editorTab.setTabStop(Main.property.getTabSpaceSize());

/*
    codeTab.setTab(Main.property.isTab());
    codeTab.setMSize(Main.property.getTabSpaceSize());
    codeTab.setSwitchIndent(Main.property.isSwitchIndent());
    codeTab.setCloseIndent(Main.property.isCloseIndent());
*/
    intelliTab.setOnOff(Main.property.isIntelliOn());
    intelliTab.setDelayValue(Main.property.getIntellisenseDelay());
  }

  public boolean isOK(){
    return this.isOK;

  }

  public Color getColor(String type){
    return colorTab.getColor(type);
  }

  public void setColor(String type, Color newColor){
    colorTab.setColor(type, newColor);
  }

  public Font getSelectedFont(){
    return fontTab.getSelectedFont();
  }

  public void okPressed(){
    isOK = true;

    // editor
    Main.property.setAutoIndentMode(editorTab.isIndent());
    Main.property.setInsertMode(editorTab.isInsert());
    Main.property.setUseTabChar(editorTab.isUseTab());
    Main.property.setGroupUndo(editorTab.isGroupUndo());
    Main.property.setSynClring(editorTab.isSyntaxColoring());
    if (editorTab.getTabStop() != -1)
	    Main.property.setTabSpaceSize(editorTab.getTabStop());

    //font
    Main.property.setSelectedFont(fontTab.getSelectedFont());

    //color
    Main.property.setColor(ColorPanel.KEYWORD, colorTab.getColor(ColorPanel.KEYWORD));
    Main.property.setColor(ColorPanel.COMMENT, colorTab.getColor(ColorPanel.COMMENT));
    Main.property.setColor(ColorPanel.CONSTANT, colorTab.getColor(ColorPanel.CONSTANT));
    Main.property.setColor(ColorPanel.STRING, colorTab.getColor(ColorPanel.STRING));
    Main.property.setColor(ColorPanel.BACKGROUND, colorTab.getColor(ColorPanel.BACKGROUND));

    //tab, space
    /*
    Main.property.setTab(codeTab.isTab());
    Main.property.setTabSpaceSize(codeTab.getMSize());
    Main.property.setSwitchIndent(codeTab.isSwitchIndent());
    Main.property.setCloseIndent(codeTab.isCloseIndent());
    */

    //intellisense delay
    Main.property.setIntelliOnOff(intelliTab.isOn());
    Main.property.setIntellisenseDelay(intelliTab.getDelayValue());
    CodeContext.timeOut = intelliTab.getDelayValue();

    //save to file
    IdePropertyManager.saveProperty(Main.property);

    //JavaView의 comment color를 설정한다
    JavaView.setCommentColor(colorTab.getColor(ColorPanel.COMMENT));

    //Editor caret 색깔을 설정한다
    MyCaret.setCaretColor(MyCaret.getBiasedColor(colorTab.getColor(ColorPanel.BACKGROUND)));


    dispose();
  }

  public void dispose(){

    int count = getComponentCount();
    for(int i=0; i<count; i++){
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okPressed();
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }
    }
  }
}
