/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PathExtractor.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: PathExtractor.java $
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-06-28   Time: 5:14p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-20   Time: 8:20p
 * Updated in $/AntIDE/source/ant/pool/packagepool
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
 * *****************  Version 10  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * com.sun.java -> javax
 *
 * *****************  Version 9  *****************
 * User: Remember     Date: 98-10-07   Time: 5:49a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 *
 * *****************  Version 8  *****************
 * User: Multipia     Date: 98-09-20   Time: 1:42a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 *  implemented Extractable interface.
 *
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-12   Time: 2:24a
 * Updated in $/Ant/src/ant/browser/packagebrowser
 * 데이타를 직접 가지지 않고 넘겨 받아서 채우는 식으로 변경
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-10   Time: 3:19a
 * Updated in $/Ant/src/ant/browser/packagebrowser
 * 버그 수정
 *
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-07   Time: 10:04p
 * Updated in $/Ant/src/ant/browser/packagebrowser
 *
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-07-20   Time: 3:59p
 * Updated in $/Ant/src
 * comment 부분 수정
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-07-20   Time: 4:49a
 * Updated in $/Ant/src
 * JGL을 사용했음(sorting)
 * recursive 부분의 부하를 줄이기 위해 Queue를 사용했음
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-07-20   Time: 2:09a
 * Updated in $/Ant/src
 * import 문에서 필요한 것만 명시했음
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-07-20   Time: 2:04a
 * Created in $/Ant/src
 * path로 부터 package 정보를 뽑아내는 class
 *
 */

package com.antsoft.ant.pool.packagepool;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

/**
 * path로 부터 package 정보를 뽑아낸다
 *
 * @author Kim sang kyun
 */

public class PathExtractor implements Extractable {
  private File libPath;
  private PackageContainer pContainer;
  private Vector myQueue = new Vector(5, 2);

  /**
   * constractor
   *
   * @param pathName
   */
  public PathExtractor(File libPath, PackageContainer pContainer) {
     this.libPath = libPath;
     this.pContainer = pContainer;
  }

  /**
   * path로 부터 package 정보들을 실제로 빼낸다
   *
   */
  public void extract() {
    String [] dirs = libPath.list( new PackageFilter() );

    String trimpath = libPath.getPath();
    int trimpathLength = trimpath.length();

    for( int i=0; i<dirs.length; i++ ) {
      ExtractFromDir2( new File( libPath, dirs[i] ) );
    }

    for( Enumeration e = myQueue.elements(); e.hasMoreElements(); ) {
      String fullpath = (String)e.nextElement();

      //만약 class file이라면
      if(fullpath.endsWith(".class")) {
        String relativepath = fullpath.substring( trimpathLength );

        int index = relativepath.lastIndexOf( "\\" );
        String pathname = relativepath.substring( 0, index );
        String className = relativepath.substring( index+1 );
        String packageName = pathname.replace('\\', '.');
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

        //PackageInfo 를 얻어서 삽입한다
        PackageInfo currPackageInfo = pContainer.getPackageInfo( packageName );
        currPackageInfo.addClassName(className);
      }

      //만약 directory 라면
      else {
        String relativepath = fullpath.substring( trimpathLength );
        String packageName = relativepath.replace('\\', '.');

        PackageInfo currPackageInfo = pContainer.getPackageInfo( packageName );
        if( currPackageInfo == null ) {
          currPackageInfo = new PackageInfo();
          currPackageInfo.setName( packageName );
          pContainer.addPackageInfo( currPackageInfo );
        }
      }
    }
  }

  /**
   * reculsive하게 directory로 부터 package정보를 뽑아낸다
   *
   * @param dir Directory file 객체
   */
  private void ExtractFromDir( File dir ) {
    File currdir = dir;
    String currPackageName;
    String trimpath = this.libPath.getPath();
    int trimpathLength = trimpath.length();

    String [] dirandclasses = currdir.list( new PackageAndClassFilter() );
    for( int i=0; i<dirandclasses.length; i++ ) {
      File subfile = new File( currdir, dirandclasses[i] );

      if( subfile.isDirectory() == true ) {
        ExtractFromDir( subfile );
      }

      if( subfile.isFile() == true ) {
        String fullpath = subfile.getPath();
        String relativepath = fullpath.substring( trimpathLength );

        int index = relativepath.lastIndexOf( "\\" );
        String pathname = relativepath.substring( 0, index );
        String className = relativepath.substring( index+1 );
        String packageName = pathname.replace('\\', '.');


        //기존에 같은 PackageInfo 객체가 있으면 classInfo 객체만 추가하고
        //없으면 PackageInfo 객체를 새로 만들어서 추가한다
        PackageInfo currPackageInfo = pContainer.getPackageInfo( packageName );
        if( currPackageInfo == null ) {
          currPackageInfo = new PackageInfo();
          currPackageInfo.setName( packageName );
          currPackageInfo.addClassName(className);
          pContainer.addPackageInfo( currPackageInfo );
        }else {
          currPackageInfo.addClassName(className);
        }
      }
    }
  }

  /**
   * reculsive하게 directory로 부터 package정보를 뽑아낸다
   * Queue 를 사용해서 reculsion의 부하를 좀 줄였다
   *
   * @param dir Directory file 객체
   */
  private void ExtractFromDir2( File dir ) {
    File currdir = dir;

    if((currdir.list(new ClassFilter())).length > 0){
      myQueue.addElement( currdir.getPath() );
    }  

    String currPackageName;
    String trimpath = this.libPath.getPath();
    int trimpathLength = trimpath.length();

    String [] dirandclasses = currdir.list( new PackageAndClassFilter() );
    for( int i=0; i<dirandclasses.length; i++ ) {
      File subfile = new File( currdir, dirandclasses[i] );

      if( subfile.isDirectory() == true ) {
        ExtractFromDir2( subfile );
      }
      if( subfile.isFile() == true ) {
        myQueue.addElement( subfile.getPath() );
      }
    }
  }
}
