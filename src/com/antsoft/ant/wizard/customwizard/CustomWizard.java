/*
 *  CustomWizard - 사용자 위저드를 등록하고 삭제하고 실행시키는 모든 일을 관리하는 클래스
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
  //현재 project path
  private String currentPath;
  //사용자 위저드를 위한 fileoutputstream
  private PrintWriter[] pw;
  //사용자 위저드 생성을 도와주는 위저드를 위한 PrintWriter
  private PrintWriter out;
  //사용자 위저드에서 생성된 source file
  private File[] files;
  //Wizard Dialog
  private Frame parent;

  public CustomWizard(Frame parent) {
    this.parent = parent;
  }

  /**
   *  registerWizard - 사용자 위저드 등록시 jarfile을 wizard directory로 카피한다
   */
  public void registerWizard(String jarFile,String wizardName){
    try{
      //jarFile의 경로를 받아 File객체를 만든다
      File targetFile = new File(jarFile);
      FileInputStream targetFs = new FileInputStream(targetFile);
      File goalFile = null;
      FileOutputStream goalFs = null;

      //wizard directory가 존재하면 iostream을 만든다
      File parent = new File(wizardPath);
      if ((parent != null) && (parent.exists())) {
	      goalFile = new File(parent,wizardName+".jar");
  	    goalFs = new FileOutputStream(goalFile);
      }else{
        System.out.println("wizard directory is not existed");
        return;
      }

      //wizard directory로 copy한다
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
   *  removeWizard - wizard dir에서 file을 삭제한다
   *
   *  @param  name - 삭제할 file 이름
   */
  public void removeWizard(String name){
    try{
      File file = new File(wizardPath);
      String[] list = file.list();
      //같은 이름의 file을 찾아 삭제한다
      for(int i=0; i<list.length; i++){
        if(list[i].equals(name+".jar")){
          File deleteFile = new File(wizardPath+File.separator+list[i]);
          //delete가 안되면 error message를 보여준다
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
      //file을 loading한다
      File jarfile = new File(wizardPath+File.separator+jarFileName);
      fs = new FileInputStream(jarfile);
      zs = new ZipInputStream(fs);

      //add jarfile path
      FileClassLoader loader = new FileClassLoader();
      loader.addPath(jarfile.getAbsolutePath());
      System.out.println("path-"+jarfile.getAbsolutePath());

      ZipEntry entry = zs.getNextEntry();
      byte[] buff = null;

      //file들을 loading시킨다.
      while(entry != null){
        String name = entry.getName();
        System.out.println(name);
        if(name.endsWith("class")){
        	System.out.println("class다");
          name = name.substring(0,name.length()-6);
          name = name.replace('/','.');
          System.out.println("name-"+name);
          Class cls = loader.loadClass(name);
          if(cls == null)System.out.println("cls is null");
          System.out.println("class4다");
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
   *  startWizard - 사용자 위저드를 시작시킨다
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
			// 새로운 sourcefile을 만든다.
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
   *  writeSourceFile - source file을 생성한다
   */
  public void writeSourceFile(Source s, int i){
    try{
      FileOutputStream fs;
      OutputStreamWriter osw;
      BufferedWriter bw;

      File parent = new File(currentPath);
      // 새로운 sourcefile을 만든다.
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
                
  ///////////// event 처리  ////////////////////////////////////
  public void wizardOK(CustomWizardEvent e){
    //custom wizard로 부터 soruce vector를 받아온다
    System.out.println("custom event generate");
    Vector source = wizard.getSource();

    //file을 생성한다
    if(files != null) files = null;
    files = new File[source.size()];
    pw = new PrintWriter[source.size()];
    
    for(int i=0; i<source.size(); i++){
      writeSourceFile((Source)(source.elementAt(i)), i);
    }
  }
}
