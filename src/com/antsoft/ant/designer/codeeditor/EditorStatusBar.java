/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/EditorStatusBar.java,v 1.3 1999/08/23 08:25:51 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: EditorStatusBar.java $
 * 
 * *****************  Version 17  *****************
 * User: Multipia     Date: 99-06-02   Time: 2:45a
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 메모리 상태의 크기를 줄임
 * 
 * *****************  Version 16  *****************
 * User: Multipia     Date: 99-05-31   Time: 5:28p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 메모리 상태를 추가하였슴.
 * 
 * *****************  Version 15  *****************
 * User: Itree        Date: 99-05-31   Time: 2:14p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * status bar 이동
 * 
 * *****************  Version 14  *****************
 * User: Itree        Date: 99-05-28   Time: 4:09p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * modify, save 부분 수정.. openSourcePanel에 기능 추가
 * 
 * *****************  Version 13  *****************
 * User: Itree        Date: 99-05-27   Time: 4:45p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 상태바의 버튼 크기를 약간 키웠음.
 * 
 * *****************  Version 12  *****************
 * User: Remember     Date: 99-05-26   Time: 12:17p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 *
 * *****************  Version 11  *****************
 * User: Multipia     Date: 99-05-23   Time: 3:41a
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * StatusBar -> EditorStatusBar로 Class 이름 변경
 * *****************  Version 3  *****************
 * User: Strife       Date: 99-05-07   Time: 5:56p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 출력 위치 조정
 *
 * *****************  Version 2  *****************
 * User: Strife       Date: 99-05-06   Time: 2:00a
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 라인 출력 관련 수정
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:16p
 * Created in $/Ant/src/ant/designer/codeeditor
 * 자바 프로그래밍용 텍스트 편집기
 *
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import javax.swing.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.compiler.Compiler;

/**
 *  class EditorStatusBar
 *
 *  @author Jinwoo Baek
 *  @author kim sang kyun
 */
public class EditorStatusBar extends JPanel {

  //remember
  JLabel lblLineCount = new JLabel();
  JLabel lblColCount = new JLabel();
  JLabel lblTotalLine = new JLabel();
  JLabel lblReadOnly = new JLabel();
  JLabel lblIsModified = new JLabel();
  JLabel lblInsertMode = new JLabel();
  MemoryBean memoryBean = new MemoryBean();
  
  JPanel explainP;
  JPanel statusP;


  //itree
  public JLabel lblExplain = new JLabel();
  private Font f;

  public EditorStatusBar() {
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void uiInit() throws Exception {

    setLayout(new BorderLayout());

    f = new Font("Dialog", Font.PLAIN, 12);

    explainP = new JPanel();
    explainP.setLayout(new BorderLayout());

    lblExplain.setVerticalTextPosition(Label.CENTER);
    //lblExplain.setPreferredSize(new Dimension(100,21));
    lblExplain.setHorizontalAlignment(JLabel.LEFT);
    lblExplain.setForeground(Color.black);
    lblExplain.setFont(f);

    explainP.add(lblExplain,BorderLayout.CENTER);

    statusP = new JPanel();
    statusP.setLayout(new FlowLayout(FlowLayout.RIGHT, 0,0));

    lblLineCount.setBorder(BorderList.lightLoweredBorder);
    lblLineCount.setPreferredSize(new Dimension(60, 21));
    lblLineCount.setHorizontalAlignment(JLabel.CENTER);
    lblLineCount.setForeground(Color.black);
    lblLineCount.setFont(f);

    lblColCount.setBorder(BorderList.lightLoweredBorder);
    lblColCount.setPreferredSize(new Dimension(45, 21));
    lblColCount.setHorizontalAlignment(JLabel.CENTER);
    lblColCount.setForeground(Color.black);
    lblColCount.setFont(f);

    lblTotalLine.setBorder(BorderList.lightLoweredBorder);
    lblTotalLine.setPreferredSize(new Dimension(35, 21));
    lblTotalLine.setHorizontalAlignment(JLabel.CENTER);
    lblTotalLine.setForeground(Color.black);
    lblTotalLine.setFont(f);

    lblReadOnly.setBorder(BorderList.lightLoweredBorder);
    lblReadOnly.setPreferredSize(new Dimension(65, 21));
    lblReadOnly.setHorizontalAlignment(JLabel.CENTER);
    lblReadOnly.setForeground(Color.black);
    lblReadOnly.setFont(f);

    lblIsModified.setBorder(BorderList.lightLoweredBorder);
    lblIsModified.setPreferredSize(new Dimension(60, 21));
    lblIsModified.setHorizontalAlignment(JLabel.CENTER);
    lblIsModified.setForeground(Color.black);
    lblIsModified.setFont(f);

    lblInsertMode.setBorder(BorderList.lightLoweredBorder);
    lblInsertMode.setPreferredSize(new Dimension(60, 21));
    lblInsertMode.setVerticalAlignment(JLabel.CENTER);
    lblInsertMode.setHorizontalAlignment(JLabel.CENTER);
    lblInsertMode.setForeground(Color.black);
    lblInsertMode.setFont(f);

    memoryBean.setBorder(BorderList.lightLoweredBorder);
    memoryBean.setPreferredSize(new Dimension(40, 21));
    memoryBean.setForeground(Color.black);
    memoryBean.setFont(f);

    statusP.add(lblLineCount);
    statusP.add(lblColCount);
    statusP.add(lblTotalLine);
    statusP.add(lblReadOnly);
    statusP.add(lblIsModified);
    statusP.add(lblInsertMode);
    statusP.add(memoryBean);

    lblLineCount.setText("ln 1");
    lblColCount.setText("col 1");
    lblTotalLine.setText("1");

    lblExplain.setText("Press F1, for HELP");
    lblReadOnly.setText("Read Only");
    lblReadOnly.setForeground(Color.gray);
    lblIsModified.setText("Modified");
    lblIsModified.setForeground(Color.gray);


    add(explainP, BorderLayout.WEST);
    add(statusP, BorderLayout.EAST);

    //setPreferredSize(new Dimension(100, 18));
  }

  public boolean isModified() {

    if(lblIsModified.getForeground() == Color.black) return true;
    else return false;
  }

  public boolean isInsertMode() {
    if(lblInsertMode.getText().equals("Insert")) return true;
    else return false;
  }

  public boolean isReadOnlyMode() {
    if(lblReadOnly.getForeground() == Color.black) return true;
    else return false;
  }

  public void hideRightStatus(boolean b) {
    if (b) {
      lblColCount.setVisible(false);
      lblReadOnly.setVisible(false);
      lblInsertMode.setVisible(false);
      lblLineCount.setVisible(false);
      lblTotalLine.setVisible(false);
      lblIsModified.setVisible(false);
    }
    else {
      lblColCount.setVisible(true);
      lblReadOnly.setVisible(true);
      lblInsertMode.setVisible(true);
      lblLineCount.setVisible(true);
      lblTotalLine.setVisible(true);
      lblIsModified.setVisible(true);

    }
  }


  public void setModified(boolean modified) {
    if (modified) {
      lblIsModified.setForeground(Color.black);


    }
    else {
      lblIsModified.setForeground(Color.gray);
    }
  }

  public void setInsertMode(boolean mode) {
    if (mode) {
      lblInsertMode.setText("Insert");
    }
    else {
      lblInsertMode.setText("Overwrite");
    }
  }

  public void setReadOnlyMode(boolean mode) {
    if (mode) {
      lblReadOnly.setForeground(Color.black);
    }
    else {
      lblReadOnly.setForeground(Color.gray);
    }
  }

  public void setExplainText(String exp) {
    if(!Compiler.isCompiling){
      lblExplain.setText(exp);
    }  
  }

  public void setLineCount(int row, int col, int lineCount) {
    if ((row > 0) && (col >= 0)) {
      lblLineCount.setText("ln " + row);
      lblColCount.setText("col " + col);
      lblTotalLine.setText(lineCount+"");
    }
    else {
      lblLineCount.setText("ln 1");
      lblColCount.setText("col 1");
      lblTotalLine.setText("1");
    }
  }
 }
