/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/revisionmanager/cvsclient/CVSEntry.java,v 1.3 1999/07/22 03:02:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: CVSEntry.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:51p
 * Updated in $/AntIDE/source/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 5  *****************
 * User: Flood        Date: 98-09-18   Time: 1:59p
 * Updated in $/JavaProjects/src/ant/manager/revisionmanager/cvsclient
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
 * User: Flood        Date: 98-08-04   Time: 12:49a
 * Created in $/Ant/src
 * CVSEntry
 */


package ant.manager.revisionmanager.cvsclient;


import java.io.InputStream;


/**
 * @author Ç¥ÅÂ¼ö
 *
 */

abstract class CVSEntry
{
  int type;
  String name;
  String version;
  String conflict;
  String options;
  String tag_or_date;
  String file_mode;
  string repository_path;
  String path;
  int file_size;

  abstract InputStream getContent();
}




