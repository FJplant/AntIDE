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
 * Library 정보들을 담고 있는 Class 입니다
 */

package com.antsoft.ant.pool.librarypool;

import java.io.File;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Library 정보를 갖고 있기 위한 Class
 * @author 권영모
 */
public class LibraryInfo {
    /** Library가 있는 Path */
    private Vector paths = new Vector(5,2);

    /** Library의 이름 */
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
     * @param path Library의 path
     * @param name Library의 이름
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
     * Library가 위치한 path를 반환한다.
     *
     * @return Library가 위치한 path
     */
    public Enumeration getPath() {
        return paths.elements();
    }

    /**
     * Library의 이름을 지정한다.
     *
     * @param newName 새로 지정할 이름
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Library의 이름을 반환한다.
     *
     * @return library의 이름
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
     * Library Path File객체를 반환한다
     *
     * @return library path file
     */
    public Enumeration getFiles(){
        return files.elements();
    }
}
