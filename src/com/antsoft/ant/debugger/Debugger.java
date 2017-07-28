/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/Debugger.java,v 1.4 1999/07/29 01:27:08 itree Exp $
 * $Revision: 1.4 $
 * $History: Debugger.java $
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-11   Time: 6:48p
 * Created in $/AntIDE/source/ant/debugger
 * Initial Version.
 */

package com.antsoft.ant.debugger;

import java.util.StringTokenizer;

public abstract interface Debugger {
  public void step( StringTokenizer tn ) throws Exception;
  public void cont() throws Exception;
  public void next() throws Exception;
  public void classes() throws Exception;
  public void locals() throws Exception;
  public void threads() throws Exception;
  public void where(boolean showPC) throws Exception;
}
