/*
 *  CustomWizard - ����� �����带 ����ϰ� �����ϰ� �����Ű�� ��� ���� �����ϴ� Ŭ����
 *  designed by Kim YunKyung
 *  date 1999.7.29
 */

package com.antsoft.ant.wizard.customwizard;

import javax.swing.*;
import java.io.*;
import java.util.zip.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

import com.antsoft.ant.property.*;
import com.antsoft.ant.util.*;

public class CustomWizard implements CustomWizardListener{

  private String wizardPath = IdePropertyManager.CUSTOM_WIZARD_PATH;

  //custom wizard class
  private Class wizardClass;
  //custom wizard object
  private AbstractCustomWizard wizard;
  //���� project path
  private String currentPath;
  //����� �����带 ���� fileoutputstream
  private PrintWriter[] pw;
  //����� ������ ������ �����ִ� �����带 ���� PrintWriter
  private PrintWriter out;
  //����� �����忡�� ������ source file
  private File[] files;
  //Wizard Dialog
  private Frame parent;

  public CustomWizard(Frame parent) {
    this.parent = parent;
  }

  /**
   *  registerWizard - ����� ������ ��Ͻ� jarfile�� wizard directory�� ī���Ѵ�
   */
  public void registerWizard(String jarFile,String wizardName){
    try{
      //jarFile�� ��θ� �޾� File��ü�� �����
      File targetFile = new File(jarFile);
      FileInputStream targetFs = new FileInputStream(targetFile);
      File goalFile = null;
      FileOutputStream goalFs = null;

      //wizard directory�� �����ϸ� iostream�� �����
      File parent = new File(wizardPath);
      if ((parent != null) && (parent.exists())) {
	      goalFile = new File(parent,wizardName+".jar");
  	    goalFs = new FileOutputStream(goalFile);
      }else{
        System.out.println("wizard directory is not existed");
        return;
      }

      //wizard directory�� copy�Ѵ�
      byte[] buff = new byte[1024];
      while(targetFs.read(buff) != -1){
        goalFs.write(buff);
      }

      targetFs.close();
      goalFs.close();

    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  removeWizard - wizard dir���� file�� �����Ѵ�
   *
   *  @param  name - ������ file �̸�
   */
  public void removeWizard(String name){
    try{
      File file = new File(wizardPath);
      String[] list = file.list();
      //���� �̸��� file�� ã�� �����Ѵ�
      for(int i=0; i<list.length; i++){
        if(list[i].equals(name+".jar")){
          File deleteFile = new File(wizardPath+File.separator+list[i]);
          //delete�� �ȵǸ� error message�� �����ش�
          if(!deleteFile.delete()){
            JOptionPane.showMessageDialog(null,"This file cannot delete!");
            return;
          }
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  jarLoader - jar file loader
   */
  public void jarLoader(String jarFileName){
    FileInputStream fs;
    ZipInputStream zs;

    try{
      //file�� loading�Ѵ�
      File jarfile = new File(wizardPath+File.separator+jarFileName);
      fs = new FileInputStream(jarfile);
      zs = new ZipInputStream(fs);

      //add jarfile path
      FileClassLoader loader = new FileClassLoader();
      loader.addPath(jarfile.getAbsolutePath());
      System.out.println("path-"+jarfile.getAbsolutePath());

      ZipEntry entry = zs.getNextEntry();
      byte[] buff = null;

      //file���� loading��Ų��.
      while(entry != null){
        String name = entry.getName();
        System.out.println(name);
        if(name.endsWith("class")){
        	System.out.println("class��");
          name = name.substring(0,name.length()-6);
          name = name.replace('/','.');
          System.out.println("name-"+name);
          Class cls = loader.loadClass(name);
          if(cls == null)System.out.println("cls is null");
          System.out.println("class4��");
          if((AbstractCustomWizard)cls.newInstance() instanceof AbstractCustomWizard){
          //if(name.toLowerCase().endsWith("wizard")){ 
          	System.out.println("abstractcustomwizard");
            wizardClass = cls;
          }
        }
        entry = zs.getNextEntry();
      }
      fs.close();
      zs.close();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  startWizard - ����� �����带 ���۽�Ų��
   *  @param  jarFileName   jar file name
   */
  public void startWizard(String jarFileName){
    try{
      jarLoader(jarFileName);
      if(wizardClass == null){
        JOptionPane.showMessageDialog(parent,"Invalid Wizard.\nCannot Execute.");
        return;
      }
      wizard = (AbstractCustomWizard)wizardClass.newInstance();
      wizard.addCustomWizardListener(this);
      wizard.setVisible(true);

    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  generateCustomWizardSource
   */
  public void generateCustomWizardSource(String packName,String className){
    try{
      File parent = new File(currentPath + File.separator + packName.replace('.',File.separator.charAt(0)));
			// ���ο� sourcefile�� �����.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
        if(files != null) files = null;
        files = new File[1];
	      files[0] = new File( parent, className + ".java" );
	      FileOutputStream myFs = new FileOutputStream(files[0]);
        OutputStreamWriter osw = new OutputStreamWriter(myFs);
        BufferedWriter bw = new BufferedWriter(osw);
        out = new PrintWriter(bw, true);

	      writer( "/*\n" );
        if(!StripString.isNull(packName))
      		writer( " *\tpackage : " + packName + "\n" );
				writer( " *\tsource  : " + className + ".java\n" );
				writer( " *\tdate    : " + DayInfo.getYear()+"."+DayInfo.getMonth()+"."+DayInfo.getDay() + "\n" );
				writer( " */\n\n" );

        if(!StripString.isNull(packName))
    	    writer( "package " + packName + ";\n\n");
        writer("import java.awt.*;\n");
        writer("import javax.swing.*;\n");
        writer("import java.io.*;\n\n");
        writer("import com.antsoft.ant.wizard.customwizard.AbstractCustomWizard;\n\n");

        writer("public class "+className+" extends AbstractCustomWizard {\n\n");

        writer("\t/**\n");
        writer("\t *\tconstructor\n");
        writer("\t */\n");
        writer("\tpublic "+className+"() {\n");
        writer("\t\tsuper(title);\n");
        writer("\t\taInit();\n");
        writer("\t\tpack();\n");
        writer("\t}\n\n");

        writer("\tprivate void aInit() {\n\n");
        writer("\t}\n\n");

        writer("}\n");

        //stream close
        out.close();
        bw.close();
        osw.close();
        myFs.close();
      }else{
        System.out.println("parent is null");
        return;
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  writeSourceFile - source file�� �����Ѵ�
   */
  public void writeSourceFile(Source s, int i){
    try{
      FileOutputStream fs;
      OutputStreamWriter osw;
      BufferedWriter bw;

      File parent = new File(currentPath);
      // ���ο� sourcefile�� �����.
      if ((parent != null)&&(parent.exists()||parent.mkdirs())){
  	    files[i] = new File( parent, s.getName() );
	      fs = new FileOutputStream(files[i]);
        osw = new OutputStreamWriter(fs);
        bw = new BufferedWriter(osw);
        pw[i] = new PrintWriter(bw, true);
        write(s.getSource(),i);

        //stream close
        pw[i].close();
        bw.close();
        osw.close();
        fs.close();

      }else{
        System.out.println("parent is null");
        return;
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public void setPath(String currentPath){
    this.currentPath = currentPath;
  }

  public File[] getFiles(){
    return files;
  }

  private void write(String s, int i) throws IOException{
    pw[i].write(s);
  }

  private void writer(String s) throws IOException{
    out.write(s);
  }
                
  ///////////// event ó��  ////////////////////////////////////
  public void wizardOK(CustomWizardEvent e){
    //custom wizard�� ���� soruce vector�� �޾ƿ´�
    System.out.println("custom event generate");
    Vector source = wizard.getSource();

    //file�� �����Ѵ�
    if(files != null) files = null;
    files = new File[source.size()];
    pw = new PrintWriter[source.size()];
    
    for(int i=0; i<source.size(); i++){
      writeSourceFile((Source)(source.elementAt(i)), i);
    }
  }
}
