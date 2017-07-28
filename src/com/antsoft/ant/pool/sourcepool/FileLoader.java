/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/sourcepool/FileLoader.java,v 1.3 1999/07/22 03:06:59 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: FileLoader.java $
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 99-06-21   Time: 2:33p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-05-17   Time: 10:14a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * ������ ���� ���� �� ���ξ� ��� �߰��Ǵ� ���׸� ����
 * 
 * *****************  Version 6  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:22a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 5  *****************
 * User: Multipia     Date: 99-05-15   Time: 12:07a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-12   Time: 12:29p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * ���� �д� ����� ��ť��Ʈ�� �ִ� ��� ����
 * 
 * *****************  Version 3  *****************
 * User: Strife       Date: 99-04-27   Time: 11:39a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * �ε����� ���� ����
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-04-26   Time: 8:21p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * close �߰� -> ������ ©���� ���׸� �ذ��� ������ ����
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * �ҽ� ������
 *
 */
package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 *  class FileLoader
 *
 *  Thread to load a file into the text storage model
 *
 *  @author Jinwoo Baek
 */
public class FileLoader {// extends Thread {
  private Document doc;
  private File f;

  public FileLoader(File f, Document doc) {
    this.f = f;
    this.doc = doc;

		BufferedReader in = null;
    try {
      // try to start reading
      //FileReader in = new FileReader(f);

			in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      String line;
      while ( ( line = in.readLine() ) != null ) {
        // Document�� Line Break �� '\n'�Դϴ�.
        line += '\n';
        doc.insertString( doc.getLength(), line, null );
        line = null;
      }
	  // �� ���ξ� �߰��Ǵ� ���� ���� ���ؼ� ��������� ������ ���� ���캻��.
      String endline = doc.getText(doc.getLength()-1, 1);
	  if (endline.equals("\n")) {
	    doc.remove(doc.getLength()-1, 1);
	  }
    } catch (IOException e) {
      System.err.println( "Exception Occurred when file loading: " + e.toString());
    } catch (BadLocationException e) {
      System.err.println(e.getMessage());
    } finally {
			try {
				in.close();
			} catch ( IOException ioe ) {
				System.err.println( "Can't close the file." );
			}
		}
	}
}
