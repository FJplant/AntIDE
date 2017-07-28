/*
 * $Id: DialogWizard.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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

public class DialogWizard {

  private Frame parent;
  private String currentpath;
  private DialogforDialog dDialog;
  private String dialogName;
  private String packName;
  private JCheckBox useSwing;
  private JCheckBox createConst;
  private File[] file = new File[1];
  private FileOutputStream fs;

  public DialogWizard(Frame parent,String path) {
    currentpath = path;
    dDialog = new DialogforDialog(parent, "Dialog Wizard", true);
    dDialog.setVisible( true );
    if (dDialog.isOK()) {
      initialData();
      generateDialogClassCode();
    }
  }

  /**
   *  initiate - 초기화
   */
  private void initialData(){
    dialogName = dDialog.dialogName.getText();
    packName = dDialog.packName.getText();
    useSwing = dDialog.useSwing;
    createConst = dDialog.createConst;
  }
  /**
   *  generateFrameClassCode - frame class code를 생성해준다
   */
  public void generateDialogClassCode(){
    try{
    	File parent = new File(currentpath + File.separator + packName.replace('.',File.separator.charAt(0)));
			// 새로운 sourcefile을 만든다.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
	      file[0] = new File( parent, dialogName + ".java" );
	      fs = new FileOutputStream(file[0]);

	      writer( "/*\n" );
        if(!StripString.isNull(packName))
      		writer( " *\tpackage : " + packName + "\n" );
				writer( " *\tsource  : " + dialogName + ".java\n" );
				writer( " *\tdate    : " + getDate() + "\n" );
				writer( " */\n\n" );

        if(!StripString.isNull(packName))
    	    writer( "package " + packName + ";\n\n");

	      //using Swing case
        if(useSwing.isSelected()){
    	    writer( "import javax.swing.*;\n\n" );
		  		writer( "public class " + dialogName );
        	writer( " extends JDialog{ \n\n"  );
        }else{
          writer("import java.awt.*;\n\n");
          writer("public class "+dialogName);
          writer(" extends Dialog{ \n\n");
        }
        if(createConst.isSelected()){
	        writer( "\t/**\n" );
  	      writer( "\t *\t" + dialogName + " -  constructor \n" );
    	    writer( "\t */\n" );
      	  writer( "\tpublic " + dialogName + "(Frame parent, String title, boolean modal) {\n" );
        	writer( "\t\tsuper(parent,title,modal);\n" );
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
