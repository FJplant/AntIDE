/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Id: FileBrowsePanel.java,v 1.19 1999/08/25 11:03:47 multipia Exp $
 * $Revision: 1.19 $
 */
package com.antsoft.ant.browser.filebrowser;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.plaf.*;
import java.util.Vector;

import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.util.QuickSorter;
//import com.antsoft.ant.designer.codeeditor.*;

/**
 * @author 이철목
 * @version 1.0
 * @since 1999.06.15
 */
public class FileBrowsePanel extends JPanel implements ActionListener {

  //JFrame frame = null;
	FileBrowser browser = null;
  protected ProjectExplorer pe = null;
  protected Project project = null;

//	JLabel statusLbl;
	File dir;

	public FileBrowsePanel(ProjectExplorer pe, Project prj) {
    //this.frame = frame;
    this.pe = pe;
    this.project = prj;

		browser = new FileBrowser(this);
		String[] files;

		//Layout.
    setLayout(new BorderLayout());
    add(browser, BorderLayout.CENTER);

//    JPanel statusPanel = new JPanel(new GridLayout(1,1));
//    statusLbl = new JLabel("StatusBar");
//    statusPanel.add(statusLbl);
//    add(statusPanel, BorderLayout.SOUTH);

    ///////// END Layout...
    if (System.getProperty("os.name").toLowerCase().indexOf("win") == -1)
      checkDriver_others();
    else
      checkDriver();
	}

	public String[] searchChildDir(String dirName) {
		String [] dirs = null ;

		File temp = new File(dirName);
		dirs = temp.list( new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				if ((new File(dir, fileName)).isDirectory() )
					return true;
				else return false;
			}
		});

		return dirs;
	}

  public String[] searchChildFiles(String dirName) {
    String [] files = null;

    File temp = new File(dirName);
    files = temp.list( new FilenameFilter() {
      public boolean accept(File dir, String fileName) {
        if ((new File(dir,fileName)).isFile() )
          return true;
        else return false;
      }
    });

    return files;
  }

  public void checkDriver_others() {
    File temp;
		//String allDriver = new String("CDEFGHIJKLMNOPQRSTUVWXYZ");

		String driver = "/";
		String name = "root[/]";
		FileBrowserEntry entry = new FileBrowserEntry(driver, FileBrowserEntry.DRIVER, null, false);
		DefaultMutableTreeNode treeNode = browser.addObject(entry);
    FileBrowserEntry empty = new FileBrowserEntry(null,FileBrowserEntry.DRIVER,null,false);
    //browser.addObject(treeNode,empty);

    Vector sortedList = new Vector();
    if (treeNode == null) return;
    FileBrowserEntry nodeinfo = (FileBrowserEntry)treeNode.getUserObject();
    if (nodeinfo.getCheck()) {
      return;
    }
    nodeinfo.setCheck(true);

    String[] childNode = searchChildDir(driver);
    System.out.println(driver);

    if (childNode == null)
        return;
    String[] childFiles = searchChildFiles(driver);


    for(int a=0; a<childNode.length; a++){
      entry = new FileBrowserEntry(childNode[a],FileBrowserEntry.DIR,null,false);
      //showDirectory(node,entry);
      sortedList.addElement(entry);
    }
    sortedList = QuickSorter.sort(sortedList,QuickSorter.LESS_STRING,true);
    for( int b=0; b<sortedList.size(); b++) {
      FileBrowserEntry em = (FileBrowserEntry)sortedList.elementAt(b);
      browser.showDirectory(treeNode,em);
    }

    for( int b=0; b<sortedList.size(); b++) {
      DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode)treeNode.getChildAt(b);
      FileBrowserEntry em = (FileBrowserEntry)sortedList.elementAt(b);

      String[] childOfChild  = searchChildDir(driver+em.getContent());

      String[] filesOfChild = null;

      if ( childOfChild == null)
        continue;
      if (childOfChild.length  == 0) {
        filesOfChild = searchChildFiles(driver+ em.getContent());
        if (filesOfChild == null)
          continue;
      }

      if(childOfChild.length >0 || filesOfChild.length >0) {
        empty = new FileBrowserEntry(null,FileBrowserEntry.DIR,null,false);
        browser.addObject(childTreeNode, empty);
      }
    }

    sortedList.removeAllElements();


    for (int a=0; a<childFiles.length; a++) {
      if (childFiles[a].toLowerCase().endsWith(".java")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JAVA,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".js")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JAVASCRIPT,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".pl")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.PERL,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".txt")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.TEXT,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".html") || childFiles[a].toLowerCase().endsWith(".htm")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.HTML,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".gif")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.GIF,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } else if (childFiles[a].toLowerCase().endsWith(".jpg")) {
        entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JPG,null,false);
        //showDirectory(node,entry);
        sortedList.addElement(entry);
      } //else if (childFiles[a].toLowerCase().endsWith(".bmp")) {
        //FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.BMP,null,false);
        //showDirectory(node,entry);
      //}
    }

    sortedList = QuickSorter.sort(sortedList,QuickSorter.LESS_STRING,true);


    for( int b=0; b<sortedList.size(); b++) {
      FileBrowserEntry em = (FileBrowserEntry)sortedList.elementAt(b);
      browser.showDirectory(treeNode,em);
    }
	}

	public void checkDriver() {
		//File temp;
		String allDriver = new String("CDEFGHIJKLMNOPQRSTUVWXYZ");
    /*
    String driver = null;
    String name = null;
    FileBrowserEntry entry = null;
    DefaultMutableTreeNode treeNode = null;
    FileBrowserEntry empty = null;
    */

		String driver = "A:";
		String name = "  A Driver ( A: )";
		FileBrowserEntry entry = new FileBrowserEntry(driver, FileBrowserEntry.DRIVER, null, false);
		DefaultMutableTreeNode treeNode = browser.addObject(entry);
    FileBrowserEntry empty = new FileBrowserEntry(null,FileBrowserEntry.DRIVER,null,false);
    browser.addObject(treeNode,empty);

		for(int i=0; i<24; i++) {
			driver = allDriver.charAt(i) + ":";
			File temp = new File(driver);
			if (temp.exists()) {
				name = "  " + allDriver.charAt(i) + " Driver ( " + allDriver.charAt(i) + ": )";
				//String [] childDirs = searchChildDir(driver + File.separator);
				entry = new FileBrowserEntry(driver, FileBrowserEntry.DRIVER, null, false);
				treeNode = browser.addObject(entry);
				//viewDownNode(treeNode, entry);
				//if (childDirs.length>0) {
				empty = new FileBrowserEntry(null,FileBrowserEntry.DRIVER,null,false);
				browser.addObject(treeNode,empty);
				//}
			}
      //} catch (NullPointerException e) {
      //  System.out.println(e.toString());
      //}
		}
	}

	public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();
	}

  /**
   * 현재 열린 source를 닫는 정리 작업을 한다
   */
  public void clear(){
    if(browser!=null) browser.closeDocument();
  }
}
/*
 * $Log: FileBrowsePanel.java,v $
 * Revision 1.19  1999/08/25 11:03:47  multipia
 * 소스 정리
 *
 * Revision 1.18  1999/08/24 07:44:51  remember
 * no message
 *
 * Revision 1.17  1999/08/07 02:58:57  itree
 * 버전정보 없애기(하나만)
 *
 * Revision 1.16  1999/07/29 08:31:57  itree
 * system.out.prinln제거
 *
 * Revision 1.15  1999/07/29 01:28:52  itree
 * linux 버전 수정
 *
 * Revision 1.14  1999/07/27 09:11:10  itree
 * no message
 *
 * Revision 1.13  1999/07/27 04:53:53  multipia
 * Java Script, Perl 추가
 *
 * Revision 1.12  1999/07/27 04:45:52  multipia
 * Header 부분 추가
 *
 */


