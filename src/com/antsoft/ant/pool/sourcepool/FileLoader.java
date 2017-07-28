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
 * 문서를 읽을 때에 한 라인씩 계속 추가되는 버그를 잡음
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
 * 파일 읽는 방법과 도큐먼트에 넣는 방법 변경
 * 
 * *****************  Version 3  *****************
 * User: Strife       Date: 99-04-27   Time: 11:39a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 로딩관련 버그 수정
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-04-26   Time: 8:21p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * close 추가 -> 파일이 짤리는 버그를 해결할 것으로 사료됨
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * 소스 관리자
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
        // Document의 Line Break 는 '\n'입니다.
        line += '\n';
        doc.insertString( doc.getLength(), line, null );
        line = null;
      }
	  // 한 라인씩 추가되는 것을 막기 위해서 명시적으로 마지막 줄을 살펴본다.
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
