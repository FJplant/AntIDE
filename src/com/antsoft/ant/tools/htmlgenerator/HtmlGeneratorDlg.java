/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/tools/htmlgenerator/HtmlGeneratorDlg.java,v 1.7 1999/08/19 06:51:01 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 */
package com.antsoft.ant.tools.htmlgenerator;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.BlackTitledBorder;
import com.antsoft.ant.util.ExtensionFileFilter;
import com.antsoft.ant.util.OSVerifier;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.IdePropertyManager;
import com.antsoft.ant.util.WindowDisposer;

/**
 * Java -> Html Genrator 를 설정하는 dialog
 *
 * @author kim sang kyun
 */

public class HtmlGeneratorDlg extends JDialog{

  private boolean isOK;
  private JCheckBox saveCbx;
  private JTextField sourceTf, destTf;
  private JButton sourceBtn, destBtn, okBtn, cancelBtn, previewBtn;;
  private ColoredButton  backgroundBtn, keywordBtn, commentBtn, literalBtn, otherBtn;
  private File tempFile;

  public static final String KEYWORD = "Keyword";
  public static final String COMMENT = "Comment";
  public static final String LITERAL = "Literal";
  public static final String OTHER = "Other";
  public static final String BACKGROUND = "Background";


  public HtmlGeneratorDlg(JFrame frame, String title, boolean isModal){
    super(frame, "Java -> Html Converter", isModal);

    //install 시
    //tempFile = new File(System.getProperty("lax.root.install.dir")+File.separator+"files"+File.separator+"generated.html");


    if( OSVerifier.getOS().equals(OSVerifier.MS_WINDOW) ){
      tempFile = new File("C:\\temp\\generated.html");
    }
    else{
      tempFile = new File( System.getProperty("user.home") + File.separator +  "generated.html");
    }

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    JLabel sourceLbl = new JLabel("Source");
    sourceLbl.setPreferredSize(new Dimension(330,20));

    sourceTf = new JTextField(24);
    sourceTf.setPreferredSize(new Dimension(330,20));
    sourceBtn = new JButton("..");
    sourceBtn.setPreferredSize(new Dimension(20,20));
    sourceBtn.setActionCommand("SOURCE");
    sourceBtn.addActionListener(al);

    JPanel sourceBtnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceBtnP.add(sourceTf);
    sourceBtnP.add(sourceBtn);

    JPanel sourceP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceP.add(sourceLbl);
    sourceP.add(sourceBtnP);

    JLabel destLbl = new JLabel("Destination");
    destLbl.setPreferredSize(new Dimension(330,20));

    destTf = new JTextField(24);
    destTf.setPreferredSize(new Dimension(330,20));
    destBtn = new JButton("..");
    destBtn.setPreferredSize(new Dimension(20,20));
    destBtn.setActionCommand("DEST");
    destBtn.addActionListener(al);

    JPanel destBtnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    destBtnP.add(destTf);
    destBtnP.add(destBtn);

    JPanel destP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    destP.add(destLbl);
    destP.add(destBtnP);

    BlackTitledBorder pathBorder = new BlackTitledBorder("Path setup");
    JPanel pathP = new JPanel(new GridLayout(2,1));
    pathP.setBorder(pathBorder);
    pathP.add(sourceP);
    pathP.add(destP);

    /////////////////////////////////////////////////////////////////////////////

    JLabel backLbl = new JLabel("background");
    backLbl.setPreferredSize(new Dimension(100,20));
    backgroundBtn = new ColoredButton();
    backgroundBtn.setPreferredSize(new Dimension(180,20));

    JPanel backP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    backP.add(backLbl);
    backP.add(backgroundBtn);

    JLabel keyLbl = new JLabel("keyword");
    keyLbl.setPreferredSize(new Dimension(100,20));
    keywordBtn = new ColoredButton();
    keywordBtn.setPreferredSize(new Dimension(180,20));

    JPanel keyP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    keyP.add(keyLbl);
    keyP.add(keywordBtn);

    JLabel commLbl = new JLabel("comment");
    commLbl.setPreferredSize(new Dimension(100,20));
    commentBtn = new ColoredButton();
    commentBtn.setPreferredSize(new Dimension(180,20));

    JPanel commentP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    commentP.add(commLbl);
    commentP.add(commentBtn);

    JLabel literalLbl = new JLabel("string literal");
    literalLbl.setPreferredSize(new Dimension(100,20));
    literalBtn = new ColoredButton();
    literalBtn.setPreferredSize(new Dimension(180,20));

    JPanel literalP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    literalP.add(literalLbl);
    literalP.add(literalBtn);

    JLabel otherLbl = new JLabel("other");
    otherLbl.setPreferredSize(new Dimension(100,20));
    otherBtn = new ColoredButton();
    otherBtn.setPreferredSize(new Dimension(180,20));

    JPanel otherP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    otherP.add(otherLbl);
    otherP.add(otherBtn);


    BlackTitledBorder colorBorder = new BlackTitledBorder("Syntax hilighting");

    JPanel colorP = new JPanel(new GridLayout(5,1));
    colorP.setBorder(colorBorder);
    colorP.add(backP);
    colorP.add(keyP);
    colorP.add(commentP);
    colorP.add(literalP);
    colorP.add(otherP);

    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    previewBtn = new JButton("Preview");
    previewBtn.setActionCommand("PREVIEW");
    previewBtn.addActionListener(al);

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(previewBtn);

    Box box = Box.createVerticalBox();

    box.add(pathP);
    box.add(colorP);

    this.getContentPane().add(box, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(buttonP, BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

   // pack();

    setSize(350, 440);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,  (screenSize.height - dlgSize.height) / 2);
    setResizable(false);

    sourceTf.requestFocus();
    setInitBtnColor();
  }

  public void setCurrentFile(String fileName){
    File f = new File(fileName);
    sourceTf.setText(f.getAbsolutePath());
    destTf.setText(f.getParent() + File.separator + f.getName().substring(0, f.getName().lastIndexOf("."))+".html");
  }

  private void setInitBtnColor(){
    if(Main.property.getHtmlColor(KEYWORD) != null){
      //saved color
      backgroundBtn.setBackground(Main.property.getHtmlColor(BACKGROUND));
      keywordBtn.setBackground(Main.property.getHtmlColor(KEYWORD));
      commentBtn.setBackground(Main.property.getHtmlColor(COMMENT));
      literalBtn.setBackground(Main.property.getHtmlColor(LITERAL));
      otherBtn.setBackground(Main.property.getHtmlColor(OTHER));
    }
    else{
      //default color
      backgroundBtn.setBackground(Color.white);
      keywordBtn.setBackground(Color.blue);
      commentBtn.setBackground(Color.green);
      literalBtn.setBackground(Color.magenta);
      otherBtn.setBackground(Color.black);
    }
  }

  public boolean isOk(){
    return this.isOK;
  }

  private void sourceBtnSelected(){
    JFileChooser filechooser = new JFileChooser();
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setFileFilter(new ExtensionFileFilter("java"));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JavaFile");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Select JavaFile");

    if(filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      sourceTf.setText(f.getAbsolutePath());
      destTf.setText(f.getParent() + File.separator + f.getName().substring(0, f.getName().lastIndexOf("."))+".html");
    }
  }

  private void destBtnSelected(){
    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select destination directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Select destination directory");

    if(filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      destTf.setText(f.getAbsolutePath() + File.separator);
    }
  }

  private void previewBtnSelected(){
   	HtmlGenerator.generate(getSourceFile(), tempFile.getAbsolutePath(), getBackgroundStr(),
                           getKeywordStr(), getCommentStr(), getLiteralStr(), getOtherStr());

    HtmlViewer viewer = new HtmlViewer(tempFile.getAbsolutePath());
    viewer.setVisible(true);
  }

  private String getHexColorStr(String r, String g, String b){

    if(r.length() ==1) r = "0"+r;
    if(g.length() ==1) g = "0"+g;
    if(b.length() ==1) b = "0"+b;

    return r+g+b;
  }

  /** 6자리의 hex string으로 만들어준다 */
  private String getKeywordStr(){
    Color b = keywordBtn.getBackground();
    return getHexColorStr( Integer.toHexString( b.getRed()),
                           Integer.toHexString( b.getGreen()),
                           Integer.toHexString( b.getBlue()));
  }

  private String getCommentStr(){
    Color b = commentBtn.getBackground();
    return getHexColorStr( Integer.toHexString( b.getRed()),
                           Integer.toHexString( b.getGreen()),
                           Integer.toHexString( b.getBlue()));
  }

  private String getBackgroundStr(){
    Color b = backgroundBtn.getBackground();
    return getHexColorStr( Integer.toHexString( b.getRed()),
                           Integer.toHexString( b.getGreen()),
                           Integer.toHexString( b.getBlue()));
  }

  private String getLiteralStr(){
    Color b = literalBtn.getBackground();
    return getHexColorStr( Integer.toHexString( b.getRed()),
                           Integer.toHexString( b.getGreen()),
                           Integer.toHexString( b.getBlue()));
  }

  private String getOtherStr(){
    Color b = otherBtn.getBackground();
    return getHexColorStr( Integer.toHexString( b.getRed()),
                           Integer.toHexString( b.getGreen()),
                           Integer.toHexString( b.getBlue()));

  }

  /** action event handler */
  class ActionHandler implements ActionListener{

    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){

        if(getSourceFile() != null && getDestFile() != null){
        	HtmlGenerator.generate(getSourceFile(), getDestFile(), getBackgroundStr(), getKeywordStr(),
                                 getCommentStr(), getLiteralStr(), getOtherStr());

        }

        //save property
        Main.property.setHtmlColor(KEYWORD, keywordBtn.getBackground());
        Main.property.setHtmlColor(COMMENT, commentBtn.getBackground());
        Main.property.setHtmlColor(LITERAL, literalBtn.getBackground());
        Main.property.setHtmlColor(OTHER, otherBtn.getBackground());
        Main.property.setHtmlColor(BACKGROUND, backgroundBtn.getBackground());

        IdePropertyManager.saveProperty(Main.property);
        dispose();
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }

      else if(cmd.equals("PREVIEW")){

        if(getDestFile()!=null) previewBtnSelected();
      }

      else if(cmd.equals("SOURCE")){
        sourceBtnSelected();
      }

      else if(cmd.equals("DEST")){
        destBtnSelected();
      }
    }
  }

  public String getSourceFile(){
    File src = new File(sourceTf.getText());
    if(src.exists()) return src.getAbsolutePath();
    else return null;
  }

  public String getDestFile(){
    File dest = new File(destTf.getText());
    if(destTf.getText().length() ==0) return null;
    if(!destTf.getText().endsWith(".html")) return null;

    try{
      File parent = new File(dest.getParent());
      if(parent.exists()) return dest.getAbsolutePath();
      else return null;
    }catch(Exception e){ return null; }
  }
}

