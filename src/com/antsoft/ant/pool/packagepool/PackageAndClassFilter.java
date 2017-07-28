/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PackageAndClassFilter.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: PackageAndClassFilter.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:54p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-14   Time: 9:19p
 * Updated in $/JavaProjects/src/ant/pool/packagepool
 * ant.packagepool -> ant.pool.packagepool
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-14   Time: 3:43a
 * Created in $/JavaProjects/src/ant/packagepool
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-12   Time: 9:38p
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:04p
 * Updated in $/Ant/src/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-07-20   Time: 2:03a
 * Created in $/Ant/src
 * File.list( FilenameFilter filter ) 에 들어가는 filter class
 *
 */

package com.antsoft.ant.pool.packagepool;

import java.io.FilenameFilter;
import java.io.File;

/**
 * .class file 과 directory만을 filtering 하기 위한 class
 * @author 김 상균
 */
class PackageAndClassFilter implements FilenameFilter {
  public boolean accept( File dir, String name ) {
    return ( name.endsWith(".class") ||
             (new File( dir, name )).isDirectory());
  }
}
