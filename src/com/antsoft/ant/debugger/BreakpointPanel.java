/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/BreakpointPanel.java,v 1.8 1999/08/31 05:22:57 itree Exp $
 * $Revision: 1.8 $
 * $History: BreakpointPanel.java $
 * 
 * *****************  Version 10  *****************
 * User: Bezant       Date: 99-06-20   Time: 10:06p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ���α׷��� ����Ǿ����� reset�����ִ�
 * ��ƾ�� �־����ϴ�.
 *
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-12   Time: 12:01p
 * Updated in $/AntIDE/source/ant/debugger
 * ���� Breakpoint�� breakpoint �߻��� SourcePanel
 * �� �׺κ��� ���̵��� �ϴ� ���� ����.. ��.
 *
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-12   Time: 2:45a
 * Updated in $/AntIDE/source/ant/debugger
 * BreakpointPanel�� �������� �պ�
 *
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:37p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage�� ���� ���ɰ����� �� �־����ϴ�.
 * ��..
 *
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * �̷��� �� ������ ���� �����ָ� ���ڱ���.
 * �׸��� ��ü���� ������ �� �߶Ծ����� ����
 * �ɳ����� �� ���� �ϴ� ���� ������ ����
 * �߰� ���� ������..
 * ������ �Ϸ����� ����ġ��~~~
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-11   Time: 6:48p
 * Created in $/AntIDE/source/ant/debugger
 * Initial Version.
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class BreakpointPanel extends JPanel implements ActionListener{
  private JFrame myWrapperFrame;
  private JLabel breakpointLabel;
  private BreakpointEventListener breakpointEventListener;
  private SourcePanel sourcePanel;
  private BorderLayout borderLayout1 = new BorderLayout();
  private JList breakpointList = new JList();
  private DefaultListModel breakpointListModel = new DefaultListModel();
  private BreakpointListCellRenderer renderer;
  private int count;


  private JButton clearButton = null;
  private String clearActionCommand = "Clear Breakpoint";


  public BreakpointPanel(BreakpointEventListener breakpointEventListener, SourcePanel sourcePanel) {
    myWrapperFrame = new JFrame("Breakpoints");
    this.breakpointEventListener = breakpointEventListener;
    this.sourcePanel = sourcePanel;
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    clearButton.setEnabled(false);
  }

  public final void reset(BreakpointEventListener breakpointEventListener, SourcePanel sourcePanel){
    this.breakpointEventListener = breakpointEventListener;
    this.sourcePanel = sourcePanel;

    breakpointListModel.removeAllElements();
    //repaint();
    //breakpointList.removeAll();
  }

  void uiInit() throws Exception {
    setLayout(borderLayout1);
    setPreferredSize( new Dimension( 300, 200 ) );
    renderer = new BreakpointListCellRenderer(this);
    breakpointList.setModel( breakpointListModel );
    breakpointList.setCellRenderer(renderer);

    breakpointLabel = new JLabel( "Breakpoint : 0 ");

    clearButton = new JButton("Clear");
    clearButton.setMargin( new Insets(0, 0, 0, 0));
    clearButton.setActionCommand(clearActionCommand);
    clearButton.setToolTipText(clearActionCommand);
    clearButton.addActionListener(this);

    JPanel pane = new JPanel(new BorderLayout());
    pane.add(breakpointLabel, BorderLayout.WEST);
    pane.add(clearButton, BorderLayout.EAST);

    JPanel upperPane = new JPanel();
    upperPane.setLayout(new BorderLayout() );
    upperPane.add( pane, BorderLayout.NORTH );

    JPanel colPane = new JPanel();
    colPane.setLayout(new GridLayout(1,3) );

    JLabel classLabel = new JLabel(" ClassName");
    classLabel.setBorder( LineBorder.createGrayLineBorder() );
    colPane.add( classLabel );

    JLabel methodLabel = new JLabel(" MethodName");
    methodLabel.setBorder( LineBorder.createGrayLineBorder() );
    colPane.add( methodLabel );

    JLabel lineLabel = new JLabel(" LineNumber");
    lineLabel.setBorder( LineBorder.createGrayLineBorder() );
    colPane.add( lineLabel );
    JPanel pl = new JPanel(new BorderLayout());
    pl.add(colPane, BorderLayout.NORTH);
    pl.add(breakpointList, BorderLayout.CENTER);
    JScrollPane scroller = new JScrollPane(pl);
    add(scroller, BorderLayout.CENTER);
//    add(new JScrollPane(breakpointList), BorderLayout.CENTER);
//    upperPane.add(colPane, BorderLayout.SOUTH);
    add(upperPane, BorderLayout.NORTH);
    MouseListener mouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          BreakpointEvent event = (BreakpointEvent)(breakpointList.getSelectedValue());
          if (event != null) {
            String className = event.getClazz();
            if (className != null) {
              sourcePanel.setClass(className);
              int line = event.getLine();
              if(line != -1) {
                sourcePanel.showLine(line, false);
              }
            }
          }
        }
      }
    };
    breakpointList.addMouseListener(mouseListener);


    myWrapperFrame.getContentPane().add(this, BorderLayout.CENTER);
    myWrapperFrame.pack();
    //ó������ �Ⱥ������� �ɷ�.
    //myWrapperFrame.setVisible(true);
  }
  public void update() throws Exception{
    //���⼭�� breakpointManager���� Breakpointlist�� �����;� �մϴ�.
    Vector list = breakpointEventListener.getAllElement();
    System.out.println("[breakpointPanel] list.size : " + list.size());
    count = list.size();
    breakpointLabel.setText("Beakpoints :" + count );
    if( count > 0 ){
      clearButton.setEnabled( true );
    }else{
      clearButton.setEnabled( false);
    }
    setBreakpoints(list);
    //System.out.println("[breakpointPanel] setBP Ok");
  }
  public void setBreakpoints( Vector list ) {
    breakpointList.setListData( list );
  }

  public void showBreakpointFrame(){
    //System.out.println("[Breakpoint] visible : " + myWrapperFrame.isShowing());
    if(myWrapperFrame.isShowing()){
      myWrapperFrame.setVisible(false);
    }else{
      myWrapperFrame.setVisible(true);
    }
  }

  public void actionPerformed(ActionEvent e){
    if( e.getActionCommand().equals(clearActionCommand)){
      //int index = breakpointList.locationToIndex(e.getPoint());
      int index = breakpointList.getSelectedIndex();
      if( index != -1 ){
        breakpointList.setSelectedIndex(index);
        BreakpointEvent event = (BreakpointEvent)breakpointList.getSelectedValue();

        if ( event != null ) {
          breakpointEventListener.breakpointRemoved( event );
          count--;
          if(count <= 0) {
            count = 0;
            clearButton.setEnabled(false);
          }
        }
      }
    }
  }
}

