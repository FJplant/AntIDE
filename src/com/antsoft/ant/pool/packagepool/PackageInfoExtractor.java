/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PackageInfoExtractor.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: PackageInfoExtractor.java $
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
 */
package com.antsoft.ant.pool.packagepool;

import java.util.*;
import java.io.File;

/**
 * path또는 zip, jar file로 부터 package 정보를 뽑아낸다
 *
 * @author Kim sang kyun
 */

 public class PackageInfoExtractor{
  private PackageContainer pContainer;
  private Enumeration libPaths;
  private Extractable extractor;

	/**
	 * constructor
	 *
	 * @param libraryPath library path
   * @param pContainer package container
	 */
  public PackageInfoExtractor(Enumeration libraryPaths, PackageContainer pContainer) {
    this.libPaths = libraryPaths;
    this.pContainer = pContainer;
  }

 /**
  *  do extract
  */
	public void extract() {

      for(Enumeration e=libPaths; e.hasMoreElements(); ){
        File libPath = (File)e.nextElement();

        if (libPath.isFile()) {
          extractor = new FileExtractor(libPath, pContainer);
         }
        else if(libPath.isDirectory()) {
          extractor = new PathExtractor(libPath, pContainer);
        }
	    extractor.extract();
      }
    }
}


