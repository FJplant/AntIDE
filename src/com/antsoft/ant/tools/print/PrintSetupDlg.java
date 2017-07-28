package com.antsoft.ant.tools.print;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.IdePropertyManager;

/**
 * source print setup dialog
 * @author kim sang kyun
 */

public class PrintSetupDlg extends JDialog implements ActionListener {


  private JCheckBox lineCbx, headerCbx, pageNumCbx, dateCbx,
                    wrapCbx, colorCbx;
  private JTextField left, right, bottom, top;
  private JButton okBtn, cancelBtn;//, printBtn;

  public static boolean drawLine = false;
  public static boolean drawHeader = true;
  public static boolean drawPageNumber = true;
  public static boolean drawDate = true;
  public static boolean drawWrapped = true;
  public static boolean drawColored = true;

  public static int leftMargin = 0;
  public static int rightMargin = 0;
  public static int bottomMargin = 0;
  public static int topMargin = 0;

  public static final int LINE = 0;
  public static final int HEADER = 1;
  public static final int PAGENUMBER = 2;
  public static final int DATE = 3;
  public static final int WRAP = 4;
  public static final int COLOR = 5;

  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int BOTTOM = 2;
  public static final int TOP = 3;

  public PrintSetupDlg(Frame parent, String title, boolean isModal){
    super(parent, title, isModal);

		// register window closing event handler
		addKeyListener(WindowDisposer.getDisposer());
		addWindowListener(WindowDisposer.getDisposer());

    drawLine = Main.property.getPrintOption(this.LINE);
    drawHeader = Main.property.getPrintOption(this.HEADER);
    drawPageNumber = Main.property.getPrintOption(this.PAGENUMBER);
    drawDate = Main.property.getPrintOption(this.DATE);
    drawWrapped = Main.property.getPrintOption(this.WRAP);
    drawColored = Main.property.getPrintOption(this.COLOR);

    leftMargin = Main.property.getPrintMargin(this.LEFT);
    rightMargin = Main.property.getPrintMargin(this.RIGHT);
    bottomMargin = Main.property.getPrintMargin(this.BOTTOM);
    topMargin = Main.property.getPrintMargin(this.TOP);


    lineCbx = new JCheckBox("Line Number", drawLine);
    headerCbx = new JCheckBox("Header", drawHeader);
    pageNumCbx = new JCheckBox("Page Number", drawPageNumber);
    dateCbx = new JCheckBox("Date", drawDate);
    wrapCbx = new JCheckBox("Wrap Line", drawWrapped);
    colorCbx = new JCheckBox("Color", drawColored);

    Box leftBox = Box.createVerticalBox();
    leftBox.add(lineCbx);
    leftBox.add(headerCbx);
    leftBox.add(pageNumCbx);
    leftBox.add(dateCbx);
    leftBox.add(wrapCbx);
    leftBox.add(colorCbx);

    left = new JTextField(leftMargin+"", 2);
    right = new JTextField(rightMargin+"", 2);
    bottom = new JTextField(bottomMargin+"", 2);
    top = new JTextField(topMargin+"", 2);

    JLabel leftLbl = new JLabel("Left");
    JLabel rightLbl = new JLabel("Right");
    JLabel bottomLbl = new JLabel("Bottom");
    JLabel topLbl = new JLabel("Top");

    JPanel leftP =  new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel rightP =  new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel bottomP =  new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel topP =  new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    leftP.add(leftLbl);
    leftP.add(left);
    rightP.add(rightLbl);
    rightP.add(right);
    bottomP.add(bottomLbl);
    bottomP.add(bottom);
    topP.add(topLbl);
    topP.add(top);
    
    Box rightBox = Box.createVerticalBox();
    rightBox.add(new JLabel("Margin"));
    rightBox.add(leftP);
    rightBox.add(rightP);
    rightBox.add(bottomP);
    rightBox.add(topP);            
    
    JPanel rightPP = new JPanel(new BorderLayout());
    rightPP.add(rightBox, BorderLayout.NORTH);
    
    Box wholeBox = Box.createHorizontalBox();
    wholeBox.add(leftBox);
    wholeBox.add(rightPP);
    
    JPanel p = new JPanel(new BorderLayout());
    p.add(wholeBox);
    p.setBorder(new TitledBorder(BorderList.etchedBorder5, "Options"));
    getContentPane().add(p);
    
    
    okBtn = new JButton("OK");
    cancelBtn = new JButton("Cancel");
//    printBtn = new JButton("Print");
    
    okBtn.addActionListener(this) ;
    cancelBtn.addActionListener(this) ;
//    printBtn.addActionListener(this); 
    
    JPanel btnP = new JPanel();
    btnP.add(okBtn);
    btnP.add(cancelBtn);
//    btnP.add(printBtn);
    
    getContentPane().add(btnP, BorderLayout.SOUTH);   

    setSize(244, 232);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,  (screenSize.height - dlgSize.height) / 2);
    setResizable(false);
  }
  
	public void actionPerformed( ActionEvent e )	{
		//TO DO .

		if(e.getSource() == okBtn){
		  drawLine = lineCbx.isSelected();
		  drawHeader = headerCbx.isSelected();
		  drawPageNumber = pageNumCbx.isSelected();
		  drawDate = dateCbx.isSelected();
		  drawWrapped = wrapCbx.isSelected();
		  drawColored = colorCbx.isSelected();

		  try{
	      int l = Integer.parseInt( left.getText() );
	      int r = Integer.parseInt( right.getText() );
	      int b = Integer.parseInt( bottom.getText() );
	      int t = Integer.parseInt( top.getText() );

  		  leftMargin =  l;
  		  rightMargin = r;
  		  bottomMargin = b;
  		  topMargin = t;


        Main.property.setPrintOption(this.LINE, drawLine);
        Main.property.setPrintOption(this.HEADER, drawHeader);
        Main.property.setPrintOption(this.PAGENUMBER, drawPageNumber);
        Main.property.setPrintOption(this.DATE, drawDate);
        Main.property.setPrintOption(this.WRAP, drawWrapped);
        Main.property.setPrintOption(this.COLOR, drawColored);

        Main.property.setPrintMargin(this.LEFT, leftMargin);
        Main.property.setPrintMargin(this.RIGHT, rightMargin);
        Main.property.setPrintMargin(this.BOTTOM, bottomMargin);
        Main.property.setPrintMargin(this.TOP, topMargin);

        IdePropertyManager.saveProperty(Main.property);

				dispose();
  		}catch(NumberFormatException e2){
  		}  
		}
		
		else if(e.getSource() == cancelBtn){
  		dispose();
		}
	}
	
	public void finalize(){
	 okBtn.removeActionListener(this);
	 cancelBtn.removeActionListener(this);
//	 printBtn.removeActionListener(this);
	}
 
}

