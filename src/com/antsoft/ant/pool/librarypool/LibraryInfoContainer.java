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
 * LibraryInfo Class�� ��� �ִ� Container Class
 */

package com.antsoft.ant.pool.librarypool;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * LibraryInfo Class���� ���� �ֱ� ���� Container Class
 * @author �ǿ���
 * @author Kim sang kyun
 */
public class LibraryInfoContainer {
    /** LibraryInfo class���� ������ �ִ� hash table */
    private Hashtable libraryInfos = new Hashtable();

    /** Default Constructor */
    public LibraryInfoContainer() {
    }

    /**
     * LibraryInfo �� �߰��Ѵ�.
     *
     * @param newLibraryInfo �߰��� LibraryInfo class
     */
    public void addLibraryInfo( LibraryInfo newLibraryInfo ) {
        libraryInfos.put( newLibraryInfo.getName(), newLibraryInfo );
    }

    /**
     * LibraryInfo�� �����Ѵ�.
     *
     * @param libraryName ������ library name
     */
    public void removeLibraryInfo( String libraryName ) {
        libraryInfos.remove( libraryName );
    }

    /**
     * ��� LibraryInfo�� �����Ѵ�.
     */
    public void removeLibraryInfoAll( ) {
        libraryInfos.clear();
    }

    /**
     * ������ �̸��� LibraryInfo Class�� �����´�.
     *
     * @param libraryName ������ library �̸�
     */
    public LibraryInfo getLibraryInfo( String libraryName ) {
        return (LibraryInfo)libraryInfos.get( libraryName );
    }

    /**
     * ����� library info ��ü���� ��� ��ȯ�Ѵ�
     *
     * @return library infos
     */
    public Enumeration getLiararyInfos(){
        return libraryInfos.elements();
    }

    /**
     * ����� library info ��ü �̸��餷�� ��� ��ȯ�Ѵ�
     *
     * @return library names
     */
    public Enumeration getLibraryNames(){
        return libraryInfos.keys();
    }
}
