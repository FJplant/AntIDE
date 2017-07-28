/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/packagepool/PackagePool.java,v 1.11 1999/07/29 02:28:47 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.11 $
 * $History: PackagePool.java $
 *
 * *****************  Version 12  *****************
 * User: Remember     Date: 99-06-29   Time: 11:50a
 * Updated in $/AntIDE/source/ant/pool/packagepool
 *
 * *****************  Version 11  *****************
 * User: Remember     Date: 99-06-28   Time: 5:25p
 * Updated in $/AntIDE/source/ant/pool/packagepool
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-14   Time: 3:43a
 * Created in $/JavaProjects/src/ant/packagepool
 *
 */

 package com.antsoft.ant.pool.packagepool;

 import java.io.File;
 import java.util.Hashtable;
 import java.util.Enumeration;
 import java.util.Vector;
 import com.antsoft.ant.pool.librarypool.*;
 import com.antsoft.ant.manager.projectmanager.ProjectManager;
 import com.antsoft.ant.manager.projectmanager.Project;
 import com.antsoft.ant.property.DefaultPathModel;
 import com.antsoft.ant.codecontext.codeeditor.InsightEvent;

 /**
  * package pool main class
  *
  * @author Kim sang kyun
  */
 public class PackagePool {

   private static Hashtable packageContainers;
   private static Vector emptyImportDatas;

   static {
     packageContainers = new Hashtable();
     emptyImportDatas = new Vector(3);
     emptyImportDatas.addElement(new InsightEvent(InsightEvent.PACKAGE, "java"));
     emptyImportDatas.addElement(new InsightEvent(InsightEvent.PACKAGE, "javax"));
     emptyImportDatas.addElement(new InsightEvent(InsightEvent.PACKAGE, "com"));
   }

   public static final PackageContainer getPackageInfos(LibraryInfo libInfo){
     PackageContainer pContainer = null;
     if(!libInfo.isNew()) {
       pContainer =  (PackageContainer)packageContainers.get(libInfo.getName());
     }
     else{
       libInfo.setNew(false);
       pContainer = new PackageContainer();
       PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
       extractor.extract();
       packageContainers.put(libInfo.getName(), pContainer);
     }

     return pContainer;
   }

   public static final PackageInfo getPackageInfo(String libraryName, Enumeration libraryPaths, String packageName){
     PackageInfo ret = null;
     PackageContainer pContainer = (PackageContainer)packageContainers.get(libraryName);
     ret = pContainer.getPackageInfo(packageName);
     return ret;
   }


   public static final Vector getInterfaceNamesFromPackage(String packageName) {
     Vector libs = ProjectManager.getCurrentProject().getPathModel().getLibraryPoolDatas();
     int size = libs.size();

     for(int i=0; i<size; i++){
       LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
       PackageContainer pContainer = (PackageContainer)packageContainers.get(libInfo.getName());

       if(pContainer == null){
         libInfo.setNew(false);
         pContainer = new PackageContainer();
         PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
         extractor.extract();
         packageContainers.put(libInfo.getName(), pContainer);
       }

       PackageInfo pInfo = pContainer.getPackageInfo(packageName);
       if(pInfo != null) {
         Vector ret = pInfo.getInterfaceNames();
         if(ret.size() > 0){
            return ret;
         }
         else return null;
       }
       else continue;
     }

     return null;
   }

   /**
    * $이 들어가는 class  는 모두 빼서 보낸다
    */
   public static final  Vector getClassesFromPackageForNew(String packageName) {
     Vector v = getClassesFromPackage(packageName);
     if(v == null) return null;

     Vector ret = new Vector(20, 10);

     for(int i=0; i<v.size(); i++){
       String str = (String)v.elementAt(i);
       if(str.indexOf("$")==-1) ret.addElement(str);
     }

     if(ret.size() > 0) return ret;
     else return null;
   }

   /**
    * package name을 받아서 그 package에 속한 class name들을 반환
    */
   public static final  Vector getClassesFromPackage(String packageName) {

     Vector libs = ProjectManager.getCurrentProject().getPathModel().getLibraryPoolDatas();
     int size = libs.size();

     for(int i=0; i<size; i++){
       LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
       PackageContainer pContainer = (PackageContainer)packageContainers.get(libInfo.getName());

       if(pContainer == null){
         libInfo.setNew(false);
         pContainer = new PackageContainer();
         PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
         extractor.extract();
         packageContainers.put(libInfo.getName(), pContainer);
       }

       PackageInfo pInfo = pContainer.getPackageInfo(packageName);
       if(pInfo != null) {
         Vector ret = pInfo.makeImportDataOfClass();
         if(ret.size() > 0){
            return ret;
         }
         else return null;
       }
       else continue;
     }

     return null;
   }

   public static final Vector getEventClassesFromPackage(String packageName){
     Vector libs = ProjectManager.getCurrentProject().getPathModel().getLibraryPoolDatas();
     int size = libs.size();

     for(int i=0; i<size; i++){
       LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
       PackageContainer pContainer = (PackageContainer)packageContainers.get(libInfo.getName());

       if(pContainer == null){
         libInfo.setNew(false);           
         pContainer = new PackageContainer();
         PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
         extractor.extract();
         packageContainers.put(libInfo.getName(), pContainer);
       }

       PackageInfo pInfo = pContainer.getPackageInfo(packageName);

       if(pInfo != null) {
         Vector ret = pInfo.getEventClassNames();
         if(ret.size() > 0) return ret;
         else return null;
       }
       else continue;
     }

     return null;
   }

   public static final Vector getExceptionClassesFromPackage(String packageName){

     Vector libs = ProjectManager.getCurrentProject().getPathModel().getLibraryPoolDatas();
     int size = libs.size();

     for(int i=0; i<size; i++){
       LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
       PackageContainer pContainer = (PackageContainer)packageContainers.get(libInfo.getName());

       if(pContainer == null){
         libInfo.setNew(false);
         pContainer = new PackageContainer();
         PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
         extractor.extract();
         packageContainers.put(libInfo.getName(), pContainer);
       }

       PackageInfo pInfo = pContainer.getPackageInfo(packageName);

       if(pInfo != null) {
         Vector ret = pInfo.getShortExceptionClassNames();
         if(ret.size() > 0) return ret;
         else return null;
       }
       else continue;
     }

     return null;
   }

   /**
    * 특정 사용자 package 정보를 반환
    */
   public  static final Vector getUserPackage(String packageName){

     Vector ret = new Vector(20, 5);
     File dir = new File(ProjectManager.getCurrentProject().getPathModel().getOutputRoot() + File.separator +
                         packageName.replace('.', File.separatorChar));

     String [] files = dir.list();
     if(files != null)
     for(int i=0; i<files.length; i++)
     {
       File f = new File(dir, files[i]);
       if(f.isFile() && files[i].endsWith(".class"))
       {
         ret.addElement(files[i].substring(0, files[i].lastIndexOf(".")));
       }
     }

     if(dir.exists()) return ret;
     else return null;
   }

   /**
    * 모든 사용자 package 정보를 반환
    */
   public  static final Vector getUserPackages(){
     Vector userPackages = new Vector(20, 5);

     Project currProj = ProjectManager.getCurrentProject();
     if(currProj == null) return null;

     DefaultPathModel m = currProj.getPathModel();
     if(m == null) return null;

     String outputRoot = m.getOutputRoot();
     if(outputRoot == null) return null;

     File root = new File(outputRoot);
     String [] lists = root.list();

     if(lists != null)
     for(int i=0; i<lists.length; i++)
     {
        File sub = new File(root, lists[i]);
        if(sub.isDirectory())
        {
          StringBuffer packageName = new StringBuffer();
          userPackages.addElement(packageName);
          makePackage(userPackages, sub, packageName);
        }
     }

     return userPackages;
   }

   private  static final void makePackage(Vector userPackages, File item, StringBuffer packageName){
     if(item.isDirectory())
     {
       if(packageName.length() > 0) packageName.append("."+item.getName());
       else packageName.append(item.getName());

       String [] lists = item.list();
       if(lists == null) return;

       for(int i=0; i<lists.length; i++)
       {
         StringBuffer buf = new StringBuffer(packageName.toString());
         File sub = new File(item, lists[i]);

         if(sub.isDirectory())
         {
           userPackages.addElement(buf);
           makePackage(userPackages, sub, buf);
         }
       }
     }
   }

   /**
    * import 구문에서 하위에 나올수 있는 데이타들을 반환한다
    */
   public  static final Vector getImportDatas(String packageName){
     if(packageName.equals("")) return emptyImportDatas;

     Vector ret = new Vector(20, 10);
     Vector packageInfos = new Vector(5, 2);

     Vector libs = ProjectManager.getCurrentProject().getPathModel().getLibraryPoolDatas();
     int size = libs.size();

     for(int i=0; i<size; i++){
       LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
       PackageContainer pContainer = (PackageContainer)packageContainers.get(libInfo.getName());

       //새로운 library이다
       if(pContainer == null){
         libInfo.setNew(false);
         pContainer = new PackageContainer();
         PackageInfoExtractor extractor = new PackageInfoExtractor(libInfo.getFiles(), pContainer);
         extractor.extract();
         packageContainers.put(libInfo.getName(), pContainer);
       }

       PackageInfo pInfo = pContainer.getPackageInfo(packageName);
       if(pInfo != null) {
         packageInfos.addElement(pInfo);
       }
       else continue;
     }

     int pSize = packageInfos.size();
     if(pSize > 0){
       for(int i=0; i<pSize; i++){
         PackageInfo pInfo = (PackageInfo)packageInfos.elementAt(i);
         Vector pV = pInfo.makeImportDataOfPackage();
         int pvSize = pV.size();
         for(int j=0;j<pvSize; j++){
           ret.addElement(new InsightEvent(InsightEvent.PACKAGE, (String)pV.elementAt(j)));
         }

         Vector cV = pInfo.makeImportDataOfClass();
         int cvSize = cV.size();
         if(cvSize != 0){
           ret.addElement(new InsightEvent(InsightEvent.ALL, "*;"));
           for(int k=0; k<cvSize;k++){
             String cName = (String)cV.elementAt(k);
             if(cName.indexOf("$") == -1)  ret.addElement(new InsightEvent(InsightEvent.CLASS, (String)cV.elementAt(k) + ";" ));
           }
         }
       }
       return ret;
     }
     else{
       return null;
     }
   }
 }

