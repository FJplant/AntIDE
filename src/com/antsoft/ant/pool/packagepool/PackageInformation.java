/*
 * $Header: /AntIDE/source/ant/pool/packagepool/PackageInformation.java 2     99-05-16 11:54p Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 * $History: PackageInformation.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:54p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-17   Time: 4:08a
 * Updated in $/JavaProjects/src/ant/pool/packagepool
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
 */

 package com.antsoft.ant.pool.packagepool;

 import java.io.File;
 import java.util.Enumeration;
 import com.antsoft.ant.pool.librarypool.*;

 /**
  * package pool¿« ø‹∫Œ interface
  *
  * @author Kim sang kyun
  */
 public interface PackageInformation {
   public PackageContainer getPackageInfos(LibraryInfo libInfo);
   public PackageInfo getPackageInfo(String libraryName, Enumeration libraryPath, String packageName);
 }
