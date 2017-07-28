/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/librarypool/LibraryInfoContainer.java,v 1.3 1999/07/22 03:03:44 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: LibraryInfoContainer.java $
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
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:04p
 * Updated in $/Ant/src/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 98-07-21   Time: 1:33p
 * Created in $/Ant/src
 * LibraryInfo Class를 담고 있는 Container Class
 */

package com.antsoft.ant.pool.librarypool;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * LibraryInfo Class들을 갖고 있기 위한 Container Class
 * @author 권영모
 * @author Kim sang kyun
 */
public class LibraryInfoContainer {
    /** LibraryInfo class들을 가지고 있는 hash table */
    private Hashtable libraryInfos = new Hashtable();

    /** Default Constructor */
    public LibraryInfoContainer() {
    }

    /**
     * LibraryInfo 를 추가한다.
     *
     * @param newLibraryInfo 추가할 LibraryInfo class
     */
    public void addLibraryInfo( LibraryInfo newLibraryInfo ) {
        libraryInfos.put( newLibraryInfo.getName(), newLibraryInfo );
    }

    /**
     * LibraryInfo를 제거한다.
     *
     * @param libraryName 제거할 library name
     */
    public void removeLibraryInfo( String libraryName ) {
        libraryInfos.remove( libraryName );
    }

    /**
     * 모든 LibraryInfo를 제거한다.
     */
    public void removeLibraryInfoAll( ) {
        libraryInfos.clear();
    }

    /**
     * 지정한 이름의 LibraryInfo Class를 가져온다.
     *
     * @param libraryName 가져올 library 이름
     */
    public LibraryInfo getLibraryInfo( String libraryName ) {
        return (LibraryInfo)libraryInfos.get( libraryName );
    }

    /**
     * 저장된 library info 객체들을 모두 반환한다
     *
     * @return library infos
     */
    public Enumeration getLiararyInfos(){
        return libraryInfos.elements();
    }

    /**
     * 저장된 library info 객체 이름들ㅇ르 모두 반환한다
     *
     * @return library names
     */
    public Enumeration getLibraryNames(){
        return libraryInfos.keys();
    }
}
