/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/librarypool/LibraryInfo.java,v 1.3 1999/07/22 03:03:44 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: LibraryInfo.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:53p
 * Updated in $/AntIDE/source/ant/pool/librarypool
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
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:04p
 * Updated in $/Ant/src/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 98-07-21   Time: 2:28p
 * Created in $/Ant/src
 * Library �������� ��� �ִ� Class �Դϴ�
 */

package com.antsoft.ant.pool.librarypool;

import java.io.File;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Library ������ ���� �ֱ� ���� Class
 * @author �ǿ���
 */
public class LibraryInfo {
    /** Library�� �ִ� Path */
    private Vector paths = new Vector(5,2);

    /** Library�� �̸� */
    private String name;

    private Vector files = new Vector(5,2);

    private boolean isNew = false;

    /**
     * Default Constructor
     */
    public LibraryInfo() {
    }

    /**
     * Constructor
     *
     * @param path Library�� path
     * @param name Library�� �̸�
     */
    public LibraryInfo( String name ) {
        this.name = name;
    }

    public void addLibraryPath( String path ){
      paths.addElement(path);
//      System.out.println("LibraryInfo : AddLibraryPath : " + path.trim());
      File file = new File(path.trim());
      files.addElement(file);
    }  

    /**
     * Library�� ��ġ�� path�� ��ȯ�Ѵ�.
     *
     * @return Library�� ��ġ�� path
     */
    public Enumeration getPath() {
        return paths.elements();
    }

    /**
     * Library�� �̸��� �����Ѵ�.
     *
     * @param newName ���� ������ �̸�
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Library�� �̸��� ��ȯ�Ѵ�.
     *
     * @return library�� �̸�
     */
    public String getName() {
        return name;
    }

    public void setNew(boolean isNew){
      this.isNew = isNew;
    }

    public boolean isNew(){
      return this.isNew;
    }    

    /**
     * Library Path File��ü�� ��ȯ�Ѵ�
     *
     * @return library path file
     */
    public Enumeration getFiles(){
        return files.elements();
    }
}
