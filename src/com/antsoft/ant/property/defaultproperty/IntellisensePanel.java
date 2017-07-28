/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/IntellisensePanel.java,v 1.2 1999/07/20 09:18:45 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.property.defaultproperty;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import com.antsoft.ant.util.*;

/** * Intellisense delay ¼³Á¤ panel
 *
 * @author kim sang kyun
 */
public class IntellisensePanel extends JPanel {

  private JSlider s;  private double delayValue;
  private JLabel messageLbl;
  private JCheckBox cbx;

  public IntellisensePanel() {    setBorder(BorderList.etchedBorder5);

    messageLbl = new JLabel();    messageLbl.setPreferredSize(new Dimension(200,20));    messageLbl.setFont(FontList.regularWeakFont);    messageLbl.setText("Slider Value: " + (double)15/10 + "sec");
    ChangeListener listener = new SliderListener();

	  s = new JSlider(1, 40, 20);
  	s.putClientProperty( "JSlider.isFilled", Boolean.TRUE );
	  s.setPaintTicks(true);
  	s.setMajorTickSpacing(5);
	  s.setMinorTickSpacing(1);
  	s.setPaintLabels( true );
  	s.setSnapToTicks( true );

	  s.getAccessibleContext().setAccessibleName("Minor Ticks");
  	s.getAccessibleContext().setAccessibleDescription("A slider showing major and minor tick marks, with slider action snapping to tick marks, with some ticks visibly labeled");
  	s.addChangeListener(listener);
    s.setValue(1);

    cbx = new JCheckBox("On/Off");
    cbx.setSelected(true);
    cbx.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e) {
        if(cbx.isSelected()) s.setEnabled(true);
        else s.setEnabled(false);
      }
    });

    JPanel topP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topP.add(messageLbl);
    topP.add(cbx);

    JPanel p = new JPanel();
	  p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Intellisense delay");
    border.setTitleColor(Color.black);
    border.setTitleFont(FontList.regularFont);
  	p.setBorder(border);
    p.add(topP);
  	p.add(Box.createRigidArea(DimensionList.vpad5));
	  p.add(s);
  	p.add(Box.createRigidArea(DimensionList.vpad5));

    setLayout(new BorderLayout());    add(new JPanel(), BorderLayout.WEST);    add(new JPanel(), BorderLayout.EAST);
    add(p, BorderLayout.NORTH);
    add(new JPanel(),BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.SOUTH);
  }

  public int getDelayValue(){

    return (int)(delayValue * 1000);
  }

  public void setDelayValue(int value){
    this.delayValue = (double)value/1000;
    s.setValue(value/100);
    messageLbl.setText("Slider Value: " + delayValue + "sec");
  }

  public boolean isOn(){
    return cbx.isSelected();
  }

  public void setOnOff(boolean onOff){
    cbx.setSelected(onOff);
    s.setEnabled(onOff);
  }

  class SliderListener implements ChangeListener {

	public void stateChanged(ChangeEvent e) {
	    JSlider s1 = (JSlider)e.getSource();
        delayValue = (double)s1.getValue()/10;
        messageLbl.setText("Slider Value: " + delayValue + "sec");
	}
  }
}



