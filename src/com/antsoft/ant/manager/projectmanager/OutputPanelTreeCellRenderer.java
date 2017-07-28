package com.antsoft.ant.manager.projectmanager;

import java.awt.Component;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

/**
 *  class OutputPanelTreeCellRenderer
 *
 *  @author Jinwoo Baek
 */
public class OutputPanelTreeCellRenderer extends JLabel implements TreeCellRenderer {
  static protected ImageIcon file;
  static protected ImageIcon error;
  static protected ImageIcon warning;

  String imagePath;

  static private Color sbc = new Color(0, 0, 31);
  static private Color dbc = Color.white;
  Font f = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());

  private boolean selected;

  static {
    try {
      //projectRoot = new ImageIcon(ProjectTreeCellRenderer.class.getResource("image/projectRoot.gif"));
      file = new ImageIcon(OutputPanelTreeCellRenderer.class.getResource("image/file.gif"));
      error = new ImageIcon(OutputPanelTreeCellRenderer.class.getResource("image/error.jpg"));
      warning = new ImageIcon(OutputPanelTreeCellRenderer.class.getResource("image/warning.jpg"));
    } catch (Exception e) {
      System.out.println("Couldn't load images: " + e);
    }
  }

  public OutputPanelTreeCellRenderer(){
    this.setForeground(Color.black);
    this.setFont(f);
  }

  /**
   * This is messaged from JTree whenever it needs to get the size
   * of the component or it wants to draw it.
   * This attempts to set the font based on value, which will be
   * a TreeNode.
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
					  boolean selected, boolean expanded,
					  boolean leaf, int row,
						boolean hasFocus) {
    String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);


    /* Set the color and the font based on the SampleData userObject. */
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
		if (dmtn instanceof ErrorMsgTreeNode) {
    	ErrorMsgTreeNode emtn = (ErrorMsgTreeNode)dmtn;
      switch (emtn.getType()) {
      case ErrorMsgTreeNode.ERROR:
      	setIcon(error);
        break;
      case ErrorMsgTreeNode.WARNING:
      	setIcon(warning);
        break;
      case ErrorMsgTreeNode.CUSTOM:
      	setIcon(warning);
        break;
      }
    }
    else setIcon(file);

    /* Tooltips used by the tree. */
    setToolTipText(stringValue);
    setText(stringValue);
    if(selected) this.setForeground(Color.yellow);
    else this.setForeground(Color.black);

    this.selected = selected;
    return this;
  }

  /**
    * Paints the value.  The background is filled based on selected.
    */
  public void paint(Graphics g) {
    Color bColor;

    if(selected) {
      bColor = sbc;
    } else {
      bColor = dbc;
      if(bColor == null)
      bColor = getBackground();
    }

    int imageOffset = -1;
    if(bColor != null) {
      Icon currentI = getIcon();

      imageOffset = getLabelStart();
      g.setColor(bColor);
      g.fillRect(imageOffset, 0, getWidth() - 1 - imageOffset,  getHeight());
    }
    super.paint(g);
  }

  private int getLabelStart() {
    Icon currentI = getIcon();
    if(currentI != null && getText() != null) {
      return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
    }
    return 0;
  }
}
