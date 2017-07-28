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
 * .jar, .zip ���Ϸ� ���� package ������ �̾Ƴ��� class
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
 * zip or jar file �� ���� package ������ �̾Ƴ��� class
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
   * fileName���� ���� package �������� ������ ������
   */
  public void extract() {
    try {
      zipFile = new ZipFile(file);
    }catch( IOException e ) {
      e.printStackTrace();
    }


    //ZipEntry���� �̸��� ��ü�� �ҷ����δ�
    for( Enumeration e=zipFile.entries(); e.hasMoreElements(); ) {

      ZipEntry entry = (ZipEntry)e.nextElement();
      String fullName = entry.getName();

      //.class �� ������ file�� ����Ѵ�
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

        //������ ���� PackageInfo ��ü�� ������ classInfo ��ü�� �߰��ϰ�
        //������ PackageInfo ��ü�� ���� ���� �߰��Ѵ�
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

















