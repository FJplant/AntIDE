/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/sourcepool/FileFlusher.java,v 1.7 1999/08/30 07:59:07 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 * $History: FileFlusher.java $
 * 
 * *****************  Version 11  *****************
 * User: Remember     Date: 99-06-21   Time: 3:00p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 10  *****************
 * User: Remember     Date: 99-06-18   Time: 12:27p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 9  *****************
 * User: Remember     Date: 99-06-16   Time: 2:08p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 8  *****************
 * User: Multipia     Date: 99-05-17   Time: 10:01a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 저장시에 한 줄씩 계속 추가되는 문제 해결
 * 
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:22a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 99-05-15   Time: 5:35a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 5  *****************
 * User: Multipia     Date: 99-05-12   Time: 2:10p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 파일 저장을 Text file에 기반하여 저장
 * 
 * *****************  Version 4  *****************
 * User: Strife       Date: 99-04-27   Time: 11:39a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 저장 관련 버그 수정
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-04-26   Time: 10:43p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-04-26   Time: 8:21p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 파일 close 추가 --> 파일이 짤리는 버그를 해결할 것으로 사료됨
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
 *  class FileFlusher
 *
 *  @author Jinwoo Baek
 *  @author Kwon, Young Mo
 *  @author kim sang kyun
 */
public class FileFlusher {

  public static boolean doAction(File f, Document doc) {

		PrintWriter out = null;
    try {
      File parent = new File(f.getParent());
      if(!parent.exists()) parent.mkdir();

      // try to start writing
      FileOutputStream fos = new FileOutputStream(f);
      OutputStreamWriter osw = new OutputStreamWriter(fos);
      BufferedWriter bw = new BufferedWriter(osw);
      out = new PrintWriter(bw, true);

      String line; // content of line
      int cLine;   // number of line
      int length;
      Element content;
      Element root = doc.getDefaultRootElement();

      cLine = root.getElementCount();
      for ( int i = 0; i < cLine; i++ ) {
        content = root.getElement(i);
        length = content.getEndOffset() - content.getStartOffset();
        line = doc.getText(content.getStartOffset(), length);
        if ( line.endsWith("\n") ) {
          line = line.substring( 0, line.length() - 1 );
     		  out.println(line);

 		    } else {
    		  out.print(line);
        }
      }

      bw = null;
      osw = null;
      fos = null;

      return true;

    } catch (IOException e) {
      System.err.println(e.toString());
      return false;

    } catch (BadLocationException e) {
      System.err.println(e.getMessage());
      return false;

    } finally {

      if(out != null) {
        out.close();
        out = null;
      }
	  }
  }
}
