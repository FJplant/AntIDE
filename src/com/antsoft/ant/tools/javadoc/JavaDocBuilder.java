/*
 * JavaDocBuilder.java
 * Title: 개미
 * Part : Java Document Builder
 * Copyright (c) 1998,1999 Ant Company, all right reserved.
 */

package com.antsoft.ant.tools.javadoc;

import java.util.Vector;
import java.io.*;

import com.antsoft.ant.manager.projectmanager.*;

/**
  @author Kim, Sung-Hoon.
  @author Lee, Chul-Mok.
  */
public class JavaDocBuilder implements Runnable{
  private DocInfo info;
  private Project project = null;
  private OutputFrame output = null;
  private Process p = null;

  private FileOutputStream out;
  private String pathName, fileName;
  private Vector command;

  private boolean fileDoc = false;
  private int index = 0;
  public JavaDocBuilder(Project prj) {
    this.project = prj;
    info = new DocInfo(project);
  }

  public void buildDocProject(Vector fileList, OutputFrame output) {
    command = new Vector();

    this.output = output;
    output.clear();
    if (info!=null) {
      if (!info.getDocumentRoot().equals("")) {
        File f = new File(info.getDocumentRoot());
        if (!f.exists()) {
          if(f.mkdir()) {
            output.getStdOut().appendText("JavadocBuilder created Directory. " + info.getDocumentRoot() + "\n");
          }
          else {
            output.getStdOut().appendText("Javadoc Directory Creation Failed..\n");
            return;
          }
        }
        String documentPath = "\"" + info.getDocumentRoot() + "\"";

        command.addElement("-d");
        command.addElement(documentPath);
      } else {
        command.addElement("-d");
        String documentPath = "\"" + info.getSourceRoot() + "\"";
        command.addElement(documentPath);
      }

      switch (info.getScope()) {
        case 1 : command.addElement("-public"); break;
        case 2 : command.addElement("-protected"); break;
        case 3 : command.addElement("-package"); break;
        case 4 : command.addElement("-private"); break;
      }

      if (info.getVerbose()) command.addElement("-verbose");
      if (info.getAuthor()) command.addElement("-author");
      if (info.getVersion()) command.addElement("-version");
      if (info.getNoindex()) command.addElement("-noindex");
      if (info.getNotree()) command.addElement("-notree");
      if (info.getNodeprecate()) command.addElement("-nodeprecate");

      if (info.getNewclasspath() && (info.getClasspath().equals(""))) {
        command.addElement("-classpath");
        command.addElement(info.getClasspath().trim());
      }
      if (info.getNewsourcepath() && (info.getSourcepath().equals(""))) {
        command.addElement("-sourcepath");
        command.addElement(info.getSourcepath().trim());
      }

      if (!info.getEncoding().equals("")) {
        command.addElement("-encoding");
        command.addElement(info.getEncoding().trim());
      }

      if (!info.getDocencoding().equals("")) {
        command.addElement("-docencoding");
        command.addElement(info.getDocencoding().trim());
      }
      if (!info.getJ().equals("")) {
        command.addElement("-J");
        command.addElement(info.getJ().trim());
      }

        // jdk 1.2 version인 경우
      if (!info.getJavadocVersion()) {

        command.addElement("-classpath");
        command.addElement("\"" + info.getSourceRoot() + "\"");

        if (info.getUse()) command.addElement("-use");
        if (info.getNohelp()) command.addElement("-nohelp");
        if (info.getNonavbar()) command.addElement("-nonavbar");
        if (info.getNodeprecatedlist()) command.addElement("-nodeprecatedlist");
        if (info.getSplitindex()) command.addElement("-splitindex");
        if (!info.getDoclet().equals("")) {
          command.addElement("-doclet");
          command.addElement(info.getDoclet().trim());
        }
        if (!info.getDocletpath().equals("")) {
          command.addElement("-docletpath");
          command.addElement(info.getDocletpath().trim());
        }
        if (!info.getBootclasspath().equals("")) {
          command.addElement("-bootclasspath");
          command.addElement(info.getBootclasspath().trim());
        }
        if (!info.getExtdirs().equals("")) {
          command.addElement("-extdirs");
          command.addElement(info.getExtdirs().trim());
        }
        if (!info.getLocale().equals("")) {
          command.addElement("-locale");
          command.addElement(info.getLocale().trim());
        }
        if (!info.getLink().equals("")) {
          command.addElement("-link");
          command.addElement(info.getLink().trim());
        }
//        if (!info.getLinkoffline().equals("")) {
//          command.addElement("-linkoffline");
//          command.addElement(info.getLinkoffline().trim());
//        }
        if (!info.getGroup().equals("")) {
          command.addElement("-group");
          command.addElement(info.getGroup().trim());
        }
        if (!info.getHelpfile().equals("")) {
          command.addElement("-helpfile");
          command.addElement(info.getHelpfile().trim());
        }
        if (!info.getStylesheetfile().equals("")) {
          command.addElement("-stylesheetfile");
          command.addElement(info.getStylesheetfile().trim());
        }
        if (!info.getWindowtitle().equals("")) {
          command.addElement("-windowtitle");
          command.addElement("\"" + info.getWindowtitle().trim() + "\"");
        }
        if (!info.getDoctitle().equals("")) {
          command.addElement("-doctitle");
          command.addElement("\"" + info.getDoctitle().trim() + "\"");
        }
        if (!info.getHeader().equals("")) {
          command.addElement("-header");
          command.addElement("\"" + info.getHeader().trim() + "\"");
        }
        if (!info.getFooter().equals("")) {
          command.addElement("-footer");
          command.addElement("\"" + info.getFooter().trim() + "\"");
        }
        if (!info.getBottom().equals("")) {
          command.addElement("-bottom");
          command.addElement("\"" + info.getBottom().trim() + "\"");
        }
      }

      // 패키지에 담긴 모든 파일을 등록한다.

      index = command.size();
      if (fileList.size() == 0) {
        System.out.println("no argument");
        return;
      }

      for(int i=0; i<fileList.size(); i++) {
        ProjectFileEntry pfe = (ProjectFileEntry)fileList.elementAt(i);

        if (pfe.getName().toLowerCase().endsWith(".java")) {
          command.addElement("\"" + pfe.getPath() + File.separator + pfe.getName() + "\"");
        //command.addElement(  fileList.elementAt(i));

        }
      }

      Thread t = new Thread (this);
      t.setPriority(Thread.MAX_PRIORITY);
      t.start();
      //makeDoc();
    }
  }
  public void buildDocFile(String pathname, String filename, OutputFrame output) {
    command = new Vector();

    this.pathName = pathname;
    this.fileName = filename;
    this.output = output;

    output.clear();
    if (info!=null) {
      if (!info.getDocumentRoot().equals("")) {
        File f = new File(info.getDocumentRoot());

        if (!f.exists()) {
          if(f.mkdir()) {
            output.getStdOut().appendText("JavadocBuilder created Directory. " + info.getDocumentRoot() + "\n");
          }
          else {
            output.getStdOut().appendText("Javadoc Directory Creation Failed.. \n");
            return;
          }
        }

        String documentPath = "\"" + info.getDocumentRoot() + "\"";

        command.addElement("-d");
        command.addElement(documentPath);
      } else {
        command.addElement("-d");
        String documentPath = "\"" + info.getSourceRoot() + "\"";
        command.addElement(documentPath);
      }

      switch (info.getScope()) {
        case 1 : command.addElement("-public"); break;
        case 2 : command.addElement("-protected"); break;
        case 3 : command.addElement("-package"); break;
        case 4 : command.addElement("-private"); break;
      }

      if (info.getVerbose()) command.addElement("-verbose");
      if (info.getAuthor()) command.addElement("-author");
      if (info.getVersion()) command.addElement("-version");
      if (info.getNoindex()) command.addElement("-noindex");
      if (info.getNotree()) command.addElement("-notree");
      if (info.getNodeprecate()) command.addElement("-nodeprecate");

      if (info.getNewclasspath() && (info.getClasspath().equals(""))) {
        command.addElement("-classpath");
        command.addElement(info.getClasspath().trim());
      }
      if (info.getNewsourcepath() && (info.getSourcepath().equals(""))) {
        command.addElement("-sourcepath");
        command.addElement(info.getSourcepath().trim());
      }

      if (!info.getEncoding().equals("")) {
        command.addElement("-encoding");
        command.addElement(info.getEncoding().trim());
      }

      if (!info.getDocencoding().equals("")) {
        command.addElement("-docencoding");
        command.addElement(info.getDocencoding().trim());
      }
      if (!info.getJ().equals("")) {
        command.addElement("-J");
        command.addElement(info.getJ().trim());
      }

        // jdk 1.2 version인 경우
      if (!info.getJavadocVersion()) {

        command.addElement("-classpath");
        command.addElement("\"" + info.getJdkClassPath() + "\"");

        if (info.getUse()) command.addElement("-use");
        if (info.getNohelp()) command.addElement("-nohelp");
        if (info.getNonavbar()) command.addElement("-nonavbar");
        if (info.getNodeprecatedlist()) command.addElement("-nodeprecatedlist");
        if (info.getSplitindex()) command.addElement("-splitindex");
        if (!info.getDoclet().equals("")) {
          command.addElement("-doclet");
          command.addElement(info.getDoclet().trim());
        }
        if (!info.getDocletpath().equals("")) {
          command.addElement("-docletpath");
          command.addElement(info.getDocletpath().trim());
        }
        if (!info.getBootclasspath().equals("")) {
          command.addElement("-bootclasspath");
          command.addElement(info.getBootclasspath().trim());
        }
        if (!info.getExtdirs().equals("")) {
          command.addElement("-extdirs");
          command.addElement(info.getExtdirs().trim());
        }
        if (!info.getLocale().equals("")) {
          command.addElement("-locale");
          command.addElement(info.getLocale().trim());
        }
        if (!info.getLink().equals("")) {
          command.addElement("-link");
          command.addElement(info.getLink().trim());
        }
//        if (!info.getLinkoffline().equals("")) {
//          command.addElement("-linkoffline");
//          command.addElement(info.getLinkoffline().trim());
//        }
        if (!info.getGroup().equals("")) {
          command.addElement("-group");
          command.addElement(info.getGroup().trim());
        }
        if (!info.getHelpfile().equals("")) {
          command.addElement("-helpfile");
          command.addElement(info.getHelpfile().trim());
        }
        if (!info.getStylesheetfile().equals("")) {
          command.addElement("-stylesheetfile");
          command.addElement(info.getStylesheetfile().trim());
        }
        if (!info.getWindowtitle().equals("")) {
          command.addElement("-windowtitle");
          command.addElement("\"" + info.getWindowtitle().trim() + "\"");
        }
        if (!info.getDoctitle().equals("")) {
          command.addElement("-doctitle");
          command.addElement("\"" + info.getDoctitle().trim() + "\"");
        }
        if (!info.getHeader().equals("")) {
          command.addElement("-header");
          command.addElement("\"" + info.getHeader().trim() + "\"");
        }
        if (!info.getFooter().equals("")) {
          command.addElement("-footer");
          command.addElement("\"" + info.getFooter().trim() + "\"");
        }
        if (!info.getBottom().equals("")) {
          command.addElement("-bottom");
          command.addElement("\"" + info.getBottom().trim() + "\"");
        }
      }

      fileDoc = true;
      Thread t = new Thread (this);
      t.setPriority(Thread.MAX_PRIORITY);
      t.start();

    }

  }
  void write( String source )  {
		try{
			out.write( source.getBytes() );
		} catch( IOException e ) {
			System.out.println( "Exception occurred.." + e.toString() );
		}
  }


  public void run() {
    try {

      Runtime rt = Runtime.getRuntime();

      int size;
      if (fileDoc) {
        size = command.size() + 2;
      } else {
        size = command.size() + 1;
      }

      String[] cmd = new String[size];

      cmd[0] = "\"" + info.getJavadocEXEPath()+ "\"" + " ";
      output.getStdOut().appendText(cmd[0]);

      if (fileDoc) {
        for(int i=0; i<command.size(); ++i) {
          cmd[i+1] = (String)command.elementAt(i)+ " ";
          output.getStdOut().appendText(cmd[i+1]);
        }
        cmd[size-1] = "\"" + pathName + File.separator + fileName + "\"";
        output.getStdOut().appendText(cmd[size-1]);
      } else {
        for(int i=0; i<command.size(); ++i) {
          cmd[i+1] = (String)command.elementAt(i)+ " ";
          if (i>=index-1)
            output.getStdOut().appendText(cmd[i+1] + "\n");
          else
            output.getStdOut().appendText(cmd[i+1]);
        }
      }

      output.getStdOut().appendText("\n");

      for (int a=0; a<cmd.length; a++) {
        System.out.println(cmd[a]);
      }


      p = rt.exec(cmd);
      BufferedInputStream out = (BufferedInputStream)p.getInputStream();

			LineNumberReader lnr = new LineNumberReader(
																		new InputStreamReader(p.getErrorStream()));
			String outStr = null;
			while((outStr = lnr.readLine()) != null){
				output.getStdOut().appendText(outStr + "\n");
			}
			out.close();
			output.getStdOut().appendText("\nMaking javadoc done..." + "\n");

      if (p != null) p.destroy();

    } catch(IOException e) {
      System.out.println("Exception in PE : " + e);
    }

  }
}
