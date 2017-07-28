/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/ClassFilter.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: ClassFilter.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:54p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-17   Time: 1:33p
 * Created in $/JavaProjects/src/ant/pool/packagepool
 *
 */
package com.antsoft.ant.pool.packagepool;

import java.io.FilenameFilter;
import java.io.File;

/**
 * .class file만을 filtering 하기 위한 class
 * @author Kim sang kyun
 */
class ClassFilter implements FilenameFilter {
  public boolean accept( File dir, String name ) {
    return name.endsWith(".class");
  }
}