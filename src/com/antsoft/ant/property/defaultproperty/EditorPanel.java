package com.antsoft.ant.property.defaultproperty;
import java.awt.*;import javax.swing.*;
import javax.swing.border.*;
import com.antsoft.ant.main.*;
import com.antsoft.ant.util.*;

/** *  class EditorPanel
 *
 *  @author Jinwoo Baek
 */
public class EditorPanel extends JPanel {
	JLabel keyMapLbl = null;
	JComboBox keyMap = null;
  JCheckBox indentCbx = null;
  JCheckBox insertCbx = null;
  JCheckBox useTabCbx = null;
  JCheckBox groupUndoCbx = null;
  JCheckBox synClrCbx = null;

  JLabel blockLbl = null;  JTextField blockIndent = null;
  JLabel tabStopLbl = null;
  JTextField tabStop = null;

  /**   *  Constructor
   */
	public EditorPanel() {
  	keyMapLbl = new JLabel("Key Mapping");
  	keyMap = new JComboBox();
    indentCbx = new JCheckBox("Auto Indent Mode");
    insertCbx = new JCheckBox("Insert Mode");
    useTabCbx = new JCheckBox("Use Tab Character");
    groupUndoCbx = new JCheckBox("Group Undo");
    synClrCbx = new JCheckBox("Syntax Colouring Editor");
    blockLbl = new JLabel("Block Indent");
    blockIndent = new JTextField(5);
    tabStopLbl = new JLabel("Tab Stops");
    tabStop = new JTextField(5);

    //set size
    keyMapLbl.setPreferredSize(new Dimension(80, 20));    keyMap.setPreferredSize(new Dimension(250, 20));
    indentCbx.setPreferredSize(new Dimension(170, 20));
    insertCbx.setPreferredSize(new Dimension(170, 20));
    useTabCbx.setPreferredSize(new Dimension(170, 20));
    groupUndoCbx.setPreferredSize(new Dimension(170, 20));
    synClrCbx.setPreferredSize(new Dimension(170, 20));
    blockLbl.setPreferredSize(new Dimension(80, 20));
    blockIndent.setPreferredSize(new Dimension(100, 20));
    tabStopLbl.setPreferredSize(new Dimension(80, 20));
    tabStop.setPreferredSize(new Dimension(100, 20));

    //panel
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(keyMapLbl);
    p1.add(keyMap);

    JPanel p2 = new JPanel(new GridLayout(5,1));
    p2.add(indentCbx);
    p2.add(insertCbx);
    p2.add(useTabCbx);
    p2.add(groupUndoCbx);
    p2.add(synClrCbx);

    JPanel p3 = new JPanel();
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Editor Option");
    border.setTitleColor(Color.black);
    border.setTitleFont(FontList.regularFont);
    p3.setBorder(border);
    p3.add(p2);

    JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p4.add(blockLbl);
    p4.add(blockIndent);

    JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p5.add(tabStopLbl);
    p5.add(tabStop);

    JPanel p6 = new JPanel(new GridLayout(4,1));
    //p6.setBorder(new EtchedBorder());
    p6.add(p4);
    p6.add(p5);

    /*JPanel p7 = new JPanel(new BorderLayout());
    p7.setPreferredSize(new Dimension(150,110));
    p7.add(p6,BorderLayout.NORTH);
    p7.add(new JPanel(),BorderLayout.CENTER);*/

    JPanel p8 = new JPanel();
    p8.setBorder(new EtchedBorder());
    p8.add(p6);

    JPanel p9 = new JPanel();
    p9.add(p3);
    p9.add(p6);

    add(p1);
    add(p9);

    keyMapLbl.setEnabled(false);    keyMap.setEnabled(false);
    blockLbl.setEnabled(false);
    blockIndent.setEnabled(false);
    setBorder(new javax.swing.border.EtchedBorder());
  }

  public boolean isIndent() {
  	return indentCbx.isSelected();
  }

  public boolean isInsert() {
  	return insertCbx.isSelected();
  }

  public boolean isUseTab() {
  	return useTabCbx.isSelected();
  }

  public boolean isGroupUndo() {
  	return groupUndoCbx.isSelected();
  }

  public boolean isSyntaxColoring() {
  	return synClrCbx.isSelected();
  }

  public void setIndent(boolean b) {
  	indentCbx.setSelected(b);
  }

  public void setInsert(boolean b) {
  	insertCbx.setSelected(b);
  }

  public void setUseTab(boolean b) {
  	useTabCbx.setSelected(b);
  }

  public void setGroupUndo(boolean b) {
  	groupUndoCbx.setSelected(b);
  }

  public void setSyntaxColoring(boolean b) {
  	synClrCbx.setSelected(b);
  }

  public int getBlockIndent() {
  	int size = -1;
  	try {
    	size = Integer.parseInt(blockIndent.getText());
      if (size <= 0) size = -1;
    } catch (NumberFormatException e) {
    	size = -1;
    }
  	return size;
  }

  public void setBlockIndent(int size) {
  	if (size > 0) blockIndent.setText(String.valueOf(size));
  }

  public int getTabStop() {
  	int size = -1;
  	try {
    	size = Integer.parseInt(tabStop.getText());
      if (size <= 0) size = -1;
    } catch (NumberFormatException e) {
    	size = -1;
    }
  	return size;
  }

  public void setTabStop(int size) {
  	if (size > 0) tabStop.setText(String.valueOf(size));
  }
}
