
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class EventWizard {

    private String currentpath;
    private EventDialog eventDlg;

    File[] file = new File[2];
    FileOutputStream fs1;
    FileOutputStream fs2;

    //date
    Calendar cal = Calendar.getInstance();
    int year = cal.get( Calendar.YEAR );
    int month = cal.get( Calendar.MONTH ) + 1;
    int day = cal.get( Calendar.DATE );
    String date = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(day);

  public EventWizard( Frame parent, String path ) {
    currentpath = path;

    eventDlg = new EventDialog( parent, "Event Wizard", true );


    eventDlg.setVisible( true );
    if (eventDlg.isOk()) {
      generateEventObjectCode();
      generateEventListenerCode();
    }
  }

  void generateEventObjectCode(){
    try{
      File parent = new File( currentpath + File.separator + eventDlg.packName.getText().replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[0] = new File( parent, eventDlg.eventName.getText() + ".java" );
  	    fs1 = new FileOutputStream( file[0] );
      }

      writer1("/*\n");
      writer1(" *\tpackage : " + eventDlg.packName.getText() + "\n");
      writer1(" *\tsource  : " + eventDlg.eventName.getText() + ".java\n");
      writer1(" *\tdate    : " + date + "\n");
      writer1(" */\n\n");

      writer1("package " + eventDlg.packName.getText() + ";\n\n");

      writer1("import java.util.*;\n\n");

      writer1("public class " + eventDlg.eventName.getText() + " extends EventObject {\n\n");
      for( int i=0; i< eventDlg.table.getRowCount()-1; i++){
        writer1("\tstatic final int " + (String)eventDlg.table.getValueAt(i,1) + " = " + (i+1) + ";\n");
      }
      writer1("\tstatic final int " + eventDlg.lastValue + " = " + (eventDlg.table.getRowCount()) + ";\n");
      writer1("\n");
      writer1("\tprivate int id = 0;\n\n");

      writer1("\tpublic int getID() {return id;};\n\n");

      writer1("\t" + eventDlg.eventName.getText() + "(Object source, int i){\n\n");
      writer1("\t\tsuper(source);\n");
      writer1("\t\tid = i;\n");
      writer1("\t}\n");
      writer1("}");

    }catch( IOException e ){
    }
  }

  void generateEventListenerCode(){
    try{
      File parent = new File( currentpath + File.separator + eventDlg.packName.getText().replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[1] = new File( parent, eventDlg.listenerName.getText() + ".java" );
  	    fs2 = new FileOutputStream( file[1] );
      }

      writer2("/*\n");
      writer2(" *\tpackage : " + eventDlg.packName.getText() + "\n");
      writer2(" *\tsource  : " + eventDlg.listenerName.getText() + ".java\n");
      writer2(" *\tdate    : " + date + "\n");
      writer2(" */\n\n");

      writer2("package " + eventDlg.packName.getText() + ";\n\n");

      writer2("import java.util.*;\n\n");

      writer2("public interface " + eventDlg.listenerName.getText() + " extends EventListener {\n\n");
      for( int i=0; i < eventDlg.table.getRowCount(); i++)
        writer2("\tpublic void " + eventDlg.table.getValueAt(i,0) + "(" + eventDlg.eventName.getText() + " e);\n");
      writer2("}");
    }catch( IOException e ){
    }
  }

  public File[] getFiles() {
  	return file;
  }

  private void writer1( String s )throws IOException{
    fs1.write( s.getBytes() );
  }

  private void writer2( String s )throws IOException{
    fs2.write( s.getBytes() );
  }
} 