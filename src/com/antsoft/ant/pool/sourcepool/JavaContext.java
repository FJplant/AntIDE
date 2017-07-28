/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/sourcepool/JavaContext.java,v 1.8 1999/08/19 06:27:59 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.8 $
 * $History: JavaContext.java $
 *
 * *****************  Version 8  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:22a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 *
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-04-29   Time: 12:33p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * JavaView 클래스를 밖으로 빼내었습니다
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * 소스 관리자
 *
 */
package com.antsoft.ant.pool.sourcepool;

import java.awt.*;
import java.util.Vector;

import javax.swing.text.*;
import com.antsoft.ant.property.defaultproperty.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.tools.print.*;

/**
 *  class JavaContext
 *
 *  @author Jinwoo Baek
 */
public class JavaContext extends StyleContext implements ViewFactory {
  // --- ViewFactory methods -------------------------------------

  public View create(Element elem) {
    return new JavaView(this, elem);
  }
}
