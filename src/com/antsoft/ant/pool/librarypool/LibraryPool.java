/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/librarypool/LibraryPool.java,v 1.4 1999/07/23 01:21:42 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 * $History: LibraryPool.java $
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:53p
 * Updated in $/AntIDE/source/ant/pool/librarypool
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-08   Time: 11:21a
 * Updated in $/AntIDE/source/ant/pool/librarypool
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-10-17   Time: 4:08a
 * Updated in $/JavaProjects/src/ant/pool/librarypool
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-16   Time: 12:46a
 * Updated in $/JavaProjects/src/ant/pool/librarypool
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

 import java.util.Vector;
 import java.util.Enumeration;
 import java.util.Hashtable;
 import java.io.File;

 /**
  * library pool의 외부 interface implementation
  *
  * @author Kim sang kyun
  */
 public class LibraryPool{

   private static LibraryInfoContainer defaultLibrarys;
   private static Hashtable projectLibrarys;

   static {
     String pathSeparator = File.pathSeparator;
     String separator = File.separator;

     projectLibrarys = new Hashtable();
   }

   public static final void removeAllProjectLibs(String projectName){
     LibraryInfoContainer libs = (LibraryInfoContainer)projectLibrarys.get(projectName);
     if(libs != null) libs.removeLibraryInfoAll();
   }

   /**
    * 특정 project의 library info들을 제공
    *
    * @param projectName project name
    * @return library infos
    */
   public static final  Enumeration getProjectLibrarys(String projectName){
     LibraryInfoContainer lic = getProjectLibrary(projectName);
     if(lic == null) return null;
     else return lic.getLiararyInfos();
   }

   /**
    * 특정 project의 library name들을 제공
    *
    * @param projectName project name
    * @return library names
    */
   public static final  Enumeration getProjectLibraryNames(String projectName){
     return getProjectLibrary(projectName).getLibraryNames();
   }


   /**
    * 특정 project의 library name에 해당하는 library info 제공
    *
    * @param projectName project name
    * @param libraryName library name
    * @return default library infos
    */
   public static final  LibraryInfo getProjectLibraryInfo(String projectName, String libraryName){
     LibraryInfoContainer c =  getProjectLibrary(projectName);
     if(c == null) return null;
     else return c.getLibraryInfo(libraryName);
   }


   /**
    * 특정 project library info를 add한다
    *
    * @param projectName project name
    * @param libInfo new library info
    */
   public static final  void addProjectLibraryInfo(String projectName, LibraryInfo libInfo){
     LibraryInfoContainer libInfos = getProjectLibrary(projectName);
     if(libInfos == null){
       libInfos = new LibraryInfoContainer();
       setProjectLibrary(projectName, libInfos);
     }
     libInfos.addLibraryInfo(libInfo);
   }

   /**
    * 특정 project library info를 remove한다
    *
    * @param projectName project name
    * @param libName new library name
    */
   public static final  void removeProjectLibraryInfo(String projectName, String libName){
     getProjectLibrary(projectName).removeLibraryInfo(libName);
   }

   /**
    * 특정 project의 library container를 제공한다
    *
    * @param projectName project name
    * @return library info container
    */
   public static final  LibraryInfoContainer getProjectLibrary(String projectName){
     return (LibraryInfoContainer)projectLibrarys.get(projectName);
   }

   /**
    * 특정 project의 library lnfo container를 setting한다
    *
    * @param projectName project name
    * @param libContainer new library container
    */
   private static final  void setProjectLibrary(String projectName, LibraryInfoContainer libContainer){
     projectLibrarys.put(projectName, libContainer);
   }

 }
