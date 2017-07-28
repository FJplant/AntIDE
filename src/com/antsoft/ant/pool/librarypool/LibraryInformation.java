/*
 * $Header: /AntIDE/source/ant/pool/librarypool/LibraryInformation.java 2     99-05-16 11:53p Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 * $History: LibraryInformation.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:53p
 * Updated in $/AntIDE/source/ant/pool/librarypool
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-14   Time: 9:18p
 * Updated in $/JavaProjects/src/ant/pool/librarypool
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-14   Time: 9:08p
 * Created in $/JavaProjects/src/ant/librarypool
 *
 */
 package com.antsoft.ant.pool.librarypool;

 import java.util.Enumeration;

 /**
  * library pool¿« ø‹∫Œ interface
  *
  * @author Kim sang kyun
  */
 public interface LibraryInformation {
   public Enumeration getProjectLibrarys(String projectName);
   public Enumeration getProjectLibraryNames(String projectName);
   public LibraryInfo getProjectLibraryInfo(String projectName, String libraryName);
 }

