/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PackageContainer.java,v 1.3 1999/07/22 03:05:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: PackageContainer.java $
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
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * com.sun.java -> javax
 *
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-07   Time: 10:04p
 * Updated in $/Ant/src/ant/browser/packagebrowser
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 98-07-21   Time: 11:45a
 * Updated in $/Ant/src
 * removeClassInfo() --> removePackageInfo(); 잘못된 이름 수정
 * removePackageInfoAll(); 함수 추가
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 98-07-14   Time: 3:40p
 * Updated in $/Ant/src
 * package 이름을 ant --> Ant로 변경
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 98-07-14   Time: 2:47p
 * Created in $/Ant/src
 * 초기 버전
 */

package com.antsoft.ant.pool.packagepool;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import com.antsoft.ant.util.QuickSorter;

/**
 * PackageInfo Item들을 담고 있는 Container class
 *
 * @author 권영모
 * @author Kim sang kyun
 */
public class PackageContainer {
    private Hashtable packageInfos = new Hashtable();
    public PackageContainer() {
    }

    /**
     * PackageInfo item을 추가한다
     *
     * @param packageInfo 추가할 PackageInfo Item
     */
    public void addPackageInfo( PackageInfo packageInfo ) {
        packageInfos.put( packageInfo.getName(), packageInfo );
    }

    /**
     * PackageInfo item을 제거한다
     *
     * @param packageName 제거할 Package Name
     */
    public void removePackageInfo( String packageName ) {
        packageInfos.remove( packageName );
    }

    /**
     * 모든 PackageInfo item들을 제거한다
     *
     */
    public void removePackageInfoAll( ) {
        packageInfos.clear();
    }

    /**
     * PackageInfo Item을 가져온다
     *
     * @param packageName 가져올 Package Name
     */
    public PackageInfo getPackageInfo( String packageName ) {
        return ( PackageInfo ) packageInfos.get( packageName );
    }

    /**
     * Package Name 들을 sorting해서 반환한다.
     *
     * @return Package Name Enumeration
     */
    public Enumeration getPackageNames( ) {
        Enumeration e = packageInfos.keys();
        Vector v = new Vector();

        //Enumeration -> Vector
        for( ;e.hasMoreElements(); ) {
          v.addElement( (String) e.nextElement() );
        }

        return QuickSorter.sort(v, QuickSorter.LESS_STRING).elements();
    }
}
