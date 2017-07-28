/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/FileExtractor.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: FileExtractor.java $
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-06-28   Time: 5:14p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 * 
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-07-16   Time: 5:39p
 * Created in $/Ant/src
 * .jar, .zip 파일로 부터 package 정보를 뽑아내는 class
 *
 */
package com.antsoft.ant.pool.packagepool;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.File;


/**
 * zip or jar file 로 부터 package 정보를 뽑아내는 class
 *
 * @author Kim sang kyun
 */
public class FileExtractor implements Extractable {

  private File file;
  private ZipFile zipFile;
  private PackageContainer pContainer;

  /**
   * default constructor
   */
  public FileExtractor(File file, PackageContainer pContainer) {
    this.file = file;
    this.pContainer = pContainer;
  }

  /**
   * fileName으로 부터 package 정보들을 실제로 빼낸다
   */
  public void extract() {
    try {
      zipFile = new ZipFile(file);
    }catch( IOException e ) {
      e.printStackTrace();
    }


    //ZipEntry들의 이름과 객체를 불러들인다
    for( Enumeration e=zipFile.entries(); e.hasMoreElements(); ) {

      ZipEntry entry = (ZipEntry)e.nextElement();
      String fullName = entry.getName();

      //.class 로 끝나는 file만 취급한다
      if( fullName.endsWith(".class") == true ) {

        int index = fullName.lastIndexOf( "/" );
        String pathname = fullName.substring( 0, index );
        String className = fullName.substring( index+1 );
        String packageName = pathname.replace('/', '.');
        String superPackageName = (packageName.indexOf(".") != -1) ? packageName.substring(0, packageName.lastIndexOf(".")) : null;

        if(superPackageName != null){
          PackageInfo superPackageInfo = pContainer.getPackageInfo(superPackageName);
          if(superPackageInfo == null){
            superPackageInfo = new PackageInfo();
            superPackageInfo.setName(superPackageName);
            pContainer.addPackageInfo(superPackageInfo);
          }

          superPackageInfo.addChildPackageName(packageName.substring(packageName.lastIndexOf(".")+1));
        }

        //기존에 같은 PackageInfo 객체가 있으면 classInfo 객체만 추가하고
        //없으면 PackageInfo 객체를 새로 만들어서 추가한다
        PackageInfo currPackageInfo = pContainer.getPackageInfo( packageName );

        if( currPackageInfo == null ) {
          currPackageInfo = new PackageInfo();
          currPackageInfo.setName( packageName );
          currPackageInfo.addClassName( className );
          pContainer.addPackageInfo( currPackageInfo );

        }else {
          currPackageInfo.addClassName( className );
        }
      }
    }
  }
}

















