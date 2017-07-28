/*
 * $Id: ClassWizard.java,v 1.3 1999/08/19 09:01:11 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.wizard.generalwizard;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

import com.antsoft.ant.util.*;

/**
 *  ClassWizard - 빈 클래스를 만들어주기 위한 위저드 클래스
 *  designed by Kim Yun Kyung
 *  date 1999.7.13
 */
public class ClassWizard {
  private String currentpath;
  private ClassDialog classDialog;
  private String packName;
  private String className;
  private String baseClass;
  private JCheckBox createConst;
  private File[] file = new File[1];
  private FileOutputStream fs;

  public ClassWizard(Frame parent, String path) {
    this.currentpath = path;
    classDialog = new ClassDialog(parent, "Class Wizard", true);
    classDialog.setVisible( true );

    if (classDialog.isOK()) {
      initialData();
      generateClassCode();
    }
  }

  private void initialData(){
    packName = classDialog.packName.getText();
    className = classDialog.className.getText();
    baseClass = classDialog.baseClass.getText();
    createConst = classDialog.createConst;
  }

  private void generateClassCode(){
    try{
    	File parent = new File(currentpath + File.separator + packName.replace('.',File.separator.charAt(0)));
			// 새로운 sourcefile을 만든다.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
	      file[0] = new File( parent, className + ".java" );
	      fs = new FileOutputStream(file[0]);

        String extend = null;
        if(baseClass.lastIndexOf(".") == -1)extend = baseClass;
        extend = baseClass.substring(baseClass.lastIndexOf(".")+1);

	      writer( "/*\n" );
        if(!StripString.isNull(packName))
      		writer( " *\tpackage : " + packName + "\n" );
				writer( " *\tsource  : " + className + ".java\n" );
				writer( " *\tdate    : " + getDate() + "\n" );
				writer( " */\n\n" );

        if(!StripString.isNull(packName))
    	    writer( "package " + packName + ";\n\n");

        if(!StripString.isNull(baseClass) && !baseClass.equals("java.lang.Object"))
      	  writer( "import "+baseClass+";\n\n" );
      	

		  	writer( "public class " + className );
        if(!StripString.isNull(baseClass) && !baseClass.equals("java.lang.Object"))
          writer( " extends "+extend);
        writer("{ \n\n"  );

        if(createConst.isSelected()){
	        writer( "\t/**\n" );
  	      writer( "\t *\t" + className + " -  constructor \n" );
    	    writer( "\t */\n" );
      	  writer( "\tpublic " + className + "() {\n" );
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
