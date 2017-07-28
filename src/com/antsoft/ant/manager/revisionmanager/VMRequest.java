/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/revisionmanager/VMRequest.java,v 1.3 1999/07/22 03:02:50 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: VMRequest.java $
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:50p
 * Updated in $/AntIDE/source/ant/manager/revisionmanager
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-04-20   Time: 12:02a
 * Updated in $/AntIDE/source/ant/manager/revisionmanager
 * Package Name ����
 * ant.manager.revisionmanager 
 * --> com.antsoft.ant.manager.revisionmanager
 * 
 * *****************  Version 5  *****************
 * User: Flood        Date: 98-09-18   Time: 1:56p
 * Updated in $/JavaProjects/src/ant/manager/revisionmanager
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 98-09-09   Time: 2:33p
 * Updated in $/Ant/src/ant/revisionmanager
 * package name changed to
 * package ant.revisionmanager;
 * 
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-08-11   Time: 11:03a
 * Updated in $/Ant/src
 * 
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-08-04   Time: 12:52a
 * Created in $/Ant/src
 */


package com.antsoft.ant.manager.revisionmanager;


/**
 * @author ǥ�¼�
 *
 */


public class VMRequest
{
  public static final int CHECKOUT      = 10 ,
                          UNDOCHECKOUT  = 11 ,
                          COPY          = 12 ,
                          CHECKIN       = 20 ,
                          REMOVE        = 31 ,
                          HISTORY       = 41 ,
                          STATUS        = 42 ;

  int type;
  String[] arguments;
  String tempdir;
  VMEntry[] entries;

	public int getType();
	public void setType(int);
	public void addArgument(String);
	public String[] getArguments();
	public String getTempdir();
	public void setTempdir(String);
	public void addEntry(VMEntry);
	public VMEntry[] getEntries();
}



