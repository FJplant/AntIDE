/*
 * $Id: PanelWizard.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 *  PanelWizard - panel을 자동생성해주는 위저드 클래스
 *  designed by Kim Yun Kyung
 *  date 1999.7.13
 */
public class PanelWizard {
  private Frame parent;
  private String currentpath;
  private PanelDialog panelDialog;
  private String panelName;
  private String packName;
  private JCheckBox useSwing;
  private JCheckBox createConst;
  private File[] file = new File[1];
  private FileOutputStream fs;

  public PanelWizard(Frame parent,String path) {
    currentpath = path;
    panelDialog = new PanelDialog(parent, "Panel Wizard", true);
    panelDialog.setVisible( true );
    if (panelDialog.isOK()) {
      initialData();
      generatePanelClassCode();
    }
  }

  /**
   *  initiate - 초기화
   */
  private void initialData(){
    panelName = panelDialog.panelName.getText();
    packName = panelDialog.packName.getText();
    useSwing = panelDialog.useSwing;
    createConst = panelDialog.createConst;
  }
  /**
   *  generatePanelClassCode - panel class code를 생성해준다
   */
  public void generatePanelClassCode(){
    try{
    	File parent = new File(currentpath + File.separator + packName.replace('.',File.separator.charAt(0)));
			// 새로운 sourcefile을 만든다.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
	      file[0] = new File( parent, panelName + ".java" );
	      fs = new FileOutputStream(file[0]);

	      writer( "/*\n" );
        if((packName != null) || (!packName.equals("")))
      		writer( " *\tpackage : " + packName + "\n" );
				writer( " *\tsource  : " + panelName + ".java\n" );
				writer( " *\tdate    : " + getDate() + "\n" );
				writer( " */\n\n" );

        if((packName != null) || (!packName.equals("")))
    	    writer( "package " + packName + ";\n\n");

	      //using Swing case
        if(useSwing.isSelected()){
    	    writer( "import javax.swing.*;\n\n" );
		  		writer( "public class " + panelName );
        	writer( " extends JPanel{ \n\n"  );
        }else{
          writer("import java.awt.*;\n\n");
          writer("public class "+panelName);
          writer(" extends Panel{ \n\n");
        }
        if(createConst.isSelected()){
	        writer( "\t/**\n" );
  	      writer( "\t *\t" + panelName + " -  constructor \n" );
    	    writer( "\t */\n" );
      	  writer( "\tpublic " + panelName + "() {\n" );
          writer( "\t}\n\n");
        }
        writer("}\n");
        fs.close();
      }else{
        System.out.println("parent is null");
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public File[] getFiles() {
  	return file;
  }

  void writer(String s) throws IOException{
    fs.write(s.getBytes());
  }

  public String getDate(){
    Calendar cal = Calendar.getInstance();
    int year = cal.get( Calendar.YEAR );
    int month = cal.get( Calendar.MONTH ) + 1;
    int day = cal.get( Calendar.DATE );
    String date = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(day);

    return date;
  }
}
