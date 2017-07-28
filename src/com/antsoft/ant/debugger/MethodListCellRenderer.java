
//Title:        AntDebugger
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       배재형
//Company:      개미소프트
//Description:  안뇽하세요.

/* $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/MethodListCellRenderer.java,v 1.6 1999/08/24 06:44:32 itree Exp $
 * $Revision: 1.6 $
 * $History: MethodListCellRenderer.java $
 */
package com.antsoft.ant.debugger;

import java.awt.*;
//import javax.swing.JList;
//import javax.swing.ListCellRenderer;
import javax.swing.*;
import java.util.*;
import sun.tools.debug.*;

public class MethodListCellRenderer extends JPanel implements ListCellRenderer {
  JList list;
  JLabel modifierLabel;
  JLabel cellText;
  JLabel breakpointLabel;
  private ImageIcon publicIcon, privateIcon, protectedIcon;
  private Font conFont, genFont, modifierFont;
  private Vector breakpointList;
  private String settingLabel = "[BREAKPOINT]";

  public MethodListCellRenderer(JList methodPanelList) {
    // Font 설정을 위해서.
    JButton b = new JButton();
    genFont = new Font(b.getFont().getName(), Font.PLAIN, 13);
    conFont = new Font(b.getFont().getName(), Font.BOLD,  13);
    
    publicIcon = new ImageIcon(MethodListCellRenderer.class.getResource("image/public.gif"));
    privateIcon = new ImageIcon(MethodListCellRenderer.class.getResource("image/private.gif"));
    protectedIcon = new ImageIcon(MethodListCellRenderer.class.getResource("image/protected.gif")); 
    
    list = methodPanelList;
    setLayout(new BorderLayout());

    modifierLabel = new JLabel();
    cellText = new JLabel();
    breakpointLabel = new JLabel();

    add(modifierLabel, BorderLayout.WEST);
    add(cellText, BorderLayout.CENTER);
    add(breakpointLabel,BorderLayout.EAST);
  }

  void setBreakpointVector(Vector breakpointList){
    if( breakpointList != null){
      this.breakpointList = breakpointList;
    }
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    //TODO: implement this javax.swing.ListCellRenderer method;
    boolean foundit = false;
    MethodEntry entry = (MethodEntry)value;
    
    if(value != null){
      String text = entry.getTotalname();
      cellText.setText(text);
      text = entry.getModifiers();
      modifierLabel.setText(text);
      if(entry.getModifierType() == MethodEntry.PUBLIC) 
      	modifierLabel.setIcon(publicIcon);
      else if(entry.getModifierType() == MethodEntry.PRIVATE)
      	modifierLabel.setIcon(privateIcon);
      else
      	modifierLabel.setIcon(protectedIcon);
      if(entry.getIsConstructor()) {
      	modifierLabel.setFont(conFont);
      	cellText.setFont(conFont);
      } else {
      	modifierLabel.setFont(genFont);
      	cellText.setFont(genFont);
      }	      	   
    }
    //현재 선택된 것이 breakpoint에 등록된건지 알아봅니다.
    // breakpointList에 있는 BreakpointEntity에 있는 method 이름과 value가
    // 같은지..
    if( breakpointList != null){
      int len = breakpointList.size();
      //System.out.println("[M_L_C_Renderer] breakpointlist의 크기 : " + len);
      BreakpointEvent ele;
       for(int i = 0; i < len && !foundit; i++){
  //      ele = breakpointList.capacity
          ele = (BreakpointEvent)breakpointList.elementAt(i);
          //System.out.println("[M_L_C_Renderer] breakpoint method name : " + ((RemoteField)value).getName());
          if( !(ele.isStopAt()) && ele.getMethod().equals(entry.getName())){
          //이 라인은 Breakpoint가 걸려 있는 라인 입니다.
            //System.out.println("[M_L_C_Renderer] Breakpoint가 걸려있는 함수 " + ele.getMethod());
            //System.out.println("[M_L_C_Renderer] Value.getname : " + ((RemoteField)value).getName());
            breakpointLabel.setText(settingLabel);
            foundit = true;
          }
      }
      if( foundit == false ){
        breakpointLabel.setText("");
      }
    }
    if( isSelected ){
      if( foundit == true){
        setBackground( Color.red.darker() );
        cellText.setForeground( Color.yellow );
      }else{
        setBackground( list.getSelectionBackground() );
        cellText.setForeground( list.getSelectionForeground() );
	      modifierLabel.setForeground( new Color(0,0,170) );	      
      }
      
    }else{
      if( foundit == true ) {
        setBackground( Color.red.brighter() );
        cellText.setForeground( Color.yellow );
      }else{
        setBackground( list.getBackground() );
        if (entry.getIsStatic()) {
	        cellText.setForeground(Color.gray);
	        modifierLabel.setForeground(Color.gray);
	      } else {
	        cellText.setForeground( list.getSelectionForeground() );
	        modifierLabel.setForeground( new Color(0,0,170) );
	      }	                
      }
    }

    return this;
  }
}
