/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PackageInfo.java,v 1.7 1999/07/29 02:28:47 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 * $History: PackageInfo.java $
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 98-07-14   Time: 2:47p
 * Created in $/Ant/src
 * 초기 버전
 */

package com.antsoft.ant.pool.packagepool;

import  java.util.Vector;
import  java.util.Enumeration;
import  java.util.Hashtable;
import  com.antsoft.ant.util.QuickSorter;
import  com.antsoft.ant.pool.classpool.ClassPool;
import  com.antsoft.ant.main.MainFrame;

/**
 * Package 리스트를 가지고 있기 위한 Class
 *
 * @author 권영모
 * @author Kim sang kyun
 */
public class PackageInfo {
    private String name;
    private Vector classNames;
    private Vector shortClassNames;
    private Vector childPackageNames;
    private Vector interfaceNames;

    /**
     * Default Constructor
     */
    public PackageInfo() {
      classNames = new Vector();
      shortClassNames = new Vector();
      childPackageNames = new Vector(5, 2);
    }

    public Vector getInterfaceNames(){

      if(interfaceNames != null) return interfaceNames;
      else {
        interfaceNames = new Vector(5, 2);

        int size = shortClassNames.size();
        for(int i=0; i<size; i++){
          String className = (String)shortClassNames.elementAt(i);
          try{
            if(ClassPool.isInterface(name+"."+className)){
              interfaceNames.addElement(className);
            }
          }catch(ClassNotFoundException e){
          }
        }  
      }

      return interfaceNames;
    }

    public int getSize(){
      return classNames.size();
    }

    public Vector makeImportDataOfPackage(){
      return childPackageNames;
    }

    public Vector makeImportDataOfClass(){
      return shortClassNames;
    }

    public void addChildPackageName(String packageName){
      childPackageNames.addElement(packageName);
    }

    public Vector getChildPackageNames(){
      return childPackageNames;
    }

    /**
     * Package의 이름을 지정한다
     *
     * @param name  Package name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Package의 이름을 가져온다
     *
     * @return Package Name
     */
    public String getName( ) {
        return name;
    }

    /**
     * Class name을 추가한다
     *
     * @param className 추가할 class name
     */
    public void addClassName( String className ) {
      classNames.addElement(className);
      shortClassNames.addElement(getShortName(className));
    }

    /**
     * Class Name 들을 반환한다.
     *
     * @return Class Name Enumeration
     */
    public Enumeration getClassNames(){
        return QuickSorter.sort(classNames, QuickSorter.LESS_STRING).elements();
    }

    public Vector getShortExceptionClassNames(){
      Vector ret = new Vector(10, 5);

      int size = shortClassNames.size();
      for(int i=0; i<size; i++){
        String name = (String)shortClassNames.elementAt(i);
        if(name.endsWith("Exception")) {
          ret.addElement(name);
        }
      }

      return ret;
    }

    public Vector getEventClassNames(){
      Vector ret = new Vector(10, 5);

      int size = shortClassNames.size();
      for(int i=0; i<size; i++){
        String name = (String)shortClassNames.elementAt(i);
        if(name.endsWith("Event") || name.endsWith("Listener") || name.endsWith("Adapter")) {
          ret.addElement(name);
        }
      }

      return ret;
    }

    /**
     * 확장자 없는 Class Name 들을 반환한다.
     *
     * @return Class Name Enumeration without extension ( .class )
     */
    public Enumeration getClassNamesWithoutExtension(){
        return QuickSorter.sort(shortClassNames, QuickSorter.LESS_STRING).elements();
    }

    /**
     * example.class -> example
     *
     * @param fullClassName example.class
     * @return example
     */
    private String getShortName(String fullClassName){
      return fullClassName.substring(0, fullClassName.lastIndexOf("."));
    }
}


