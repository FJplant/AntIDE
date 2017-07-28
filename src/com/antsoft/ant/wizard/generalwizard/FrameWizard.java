/*
 * $Id: FrameWizard.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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

import com.antsoft.ant.util.*;

/*
 *  FrameWizard - frame을 자동으로 만들어 주는 위저드
 *  designed by Kim Yun Kyung
 *  date 1999.7.12
 */
public class FrameWizard {
  private Frame parent;
  private String currentpath;
  private FrameDialog frameDialog;
  private String fName;
  private String packName;
  private JCheckBox useSwing;
  private JCheckBox createConst;
  private JCheckBox createMain;
  private File[] file = new File[1];
  private FileOutputStream fs;

  public FrameWizard(Frame parent,String path) {
    currentpath = path;
    frameDialog = new FrameDialog(parent, "Frame Wizard", true);
    frameDialog.setVisible( true );
    if (frameDialog.isOK()) {
      initiate();
      generateFrameClassCode();
    }
  }

  /**
   *  initiate - 초기화
   */
  private void initiate(){
    fName = frameDialog.frameName.getText();
    packName = frameDialog.packName.getText();
    useSwing = frameDialog.useSwing;
    createConst = frameDialog.createConst;
    createMain = frameDialog.createMain;
  }
  /**
   *  generateFrameClassCode - frame class code를 생성해준다
   */
  public void generateFrameClassCode(){
    try{
    	File parent = new File(currentpath + File.separator + packName.replace('.',File.separator.charAt(0)));
			// 새로운 sourcefile을 만든다.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
	      file[0] = new File( parent, fName + ".java" );
	      fs = new FileOutputStream(file[0]);

	      writer( "/*\n" );
        if(!StripString.isNull(packName))
      		writer( " *\tpackage : " + packName + "\n" );
				writer( " *\tsource  : " + fName + ".java\n" );
				writer( " *\tdate    : " + getDate() + "\n" );
				writer( " */\n\n" );

        if(!StripString.isNull(packName))
    	    writer( "package " + packName + ";\n\n");

	      //using Swing case
        if(useSwing.isSelected()){
    	    writer( "import javax.swing.*;\n\n" );
		  		writer( "public class " + fName );
        	writer( " extends JFrame{ \n\n"  );
        }else{
          writer("import java.awt.*;\n");
          writer("\npublic class "+fName);
          writer(" extends Frame{ \n\n");
        }
        if(createConst.isSelected()){
	        writer( "\t/**\n" );
  	      writer( "\t *\t" + fName + " -  constructor \n" );
    	    writer( "\t */\n" );
      	  writer( "\tpublic " + fName + "() {\n" );
        	writer( "\t\tsuper(\"" + fName + "\");\n" );
          writer( "\t}\n\n");
        }
        if(createMain.isSelected()){
          writer("\t/**\n");
          writer("\t *\tMain method\n");
          writer("\t */\n");
          writer("\tpublic static void main(String[] args) {\n\n");
          writer("\t}\n\n");
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
