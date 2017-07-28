/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/classpool/ClassPool.java,v 1.7 1999/08/19 09:36:27 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 * $History: ClassPool.java $
 * 
 * *****************  Version 20  *****************
 * User: Remember     Date: 99-06-10   Time: 6:12p
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 */

 package com.antsoft.ant.pool.classpool;

 import java.lang.reflect.*;
 import java.util.Hashtable;
 import java.util.Vector;
 import javax.swing.table.TableModel;

 import com.antsoft.ant.util.*;
 import com.antsoft.ant.pool.librarypool.*;
 import com.antsoft.ant.manager.projectmanager.*;

 /**
  * class의 reflection 정보를 제공한다
  *
  * @author Kim sang kyun
  */

 public final class ClassPool {

   //exist를 호출 했을때 load해놓은 class instance
   private static Class cachedInstance;


   /**
    * interface인지 아닌지 정보를 제공. package browser에서 처음에
    * class list를 보여줄때 사용한다
    *
    * @param fullClassName classname
    * @return interface인지 class인지 정보
    */
   public static final boolean isInterface(String fullClassName) throws java.lang.ClassNotFoundException{
     Class instance = getClassInstance(fullClassName);

     if(instance != null){
       return instance.isInterface();
     }
     else return false;
   }

   /**
    * class instance를 반환한다
    *
    * @param fullClassName key
    * @return class instance
    */
   public static Class getClassInstance(String fullClassName){

     if( (cachedInstance != null) && cachedInstance.getName().equals(fullClassName)) {
       return  cachedInstance;
     }

     Class instance = null;
     try{
       instance = createClassInstance(fullClassName);
       cachedInstance = instance;

     }catch(ClassNotFoundException e){ instance = null; }

     return instance;
   }

   /**
    * class instance가 존재하는지 알아본다
    *
    * @param fullClassName fullClassName to Search
    * @return exist or not flag
    */
   public static boolean exist(String fullClassName){
     if( (cachedInstance != null) && cachedInstance.getName().equals(fullClassName)) return true;

     FileClassLoader loader = ProjectManager.getCurrentProject().getClassLoader();
     if(loader == null) return false;

     Class instance = null;
     try{
       instance = loader.loadClass(fullClassName, true);
     }catch(Exception e){
       return false;
     }

     if(instance == null) return false;
     else {
       cachedInstance = instance;
       return true;
     }
   }

   /**
    * class instance를 얻는다
    *
    * @param fullClassName classname
    * @return Class instance
    */
   private static Class createClassInstance(String fullClassName) throws java.lang.ClassNotFoundException{

     Class loaded = null;
     FileClassLoader classLoader = ProjectManager.getCurrentProject().getClassLoader();

     if(classLoader == null) {
       System.out.println("Class Loader is NULL");
       throw new ClassNotFoundException("CurrentProject's ClassLoader is NULL");
     }

     try{
       loaded = classLoader.loadClass(fullClassName, true);
     }catch(Exception e){ e.printStackTrace(); return null; }

     return loaded;
   }

   /**
    * get super class vector
    */
   private static Vector superClasses = new Vector(4, 2);
   public static final Vector getSuperClasses(String fullClassName){
     if(superClasses.size() > 0) superClasses.removeAllElements();

     Class instance = getClassInstance(fullClassName);
     if(instance == null) return null;

     superClasses.addElement(fullClassName);
     instance = instance.getSuperclass();
     while(instance != null){
       superClasses.addElement(instance.getName());
       instance = instance.getSuperclass();
     }

     return superClasses;
   }

   /**
    * 특정 class의 static field name list를 얻는다
    *
    * @param fullClassName classname
    * @return static field name list
    */
   public static final Vector getStaticFieldSignatures(String fullClassName){
     Vector ret = new Vector(15, 5);
     Vector signatures = getFieldSignatures(fullClassName);

     if(signatures != null){
       for(int i=0; i<signatures.size(); i++){
         SigModelImpl sig = (SigModelImpl)signatures.elementAt(i);
         if(sig.isStatic()) ret.addElement(sig);
       }
       return ret;
     }
     else {
       // it must be not happened..
       return null;
     }
   }


   /**
    * 특정 class의 field name list를 얻는다. public 인것만 리턴
    *
    * @param fullClassName classname
    * @return field name list
    */
   public static final Vector getFieldSignatures(String fullClassName){

     Class classInstance = getClassInstance(fullClassName);
     if(classInstance == null) return null;
     Hashtable tempHash = new Hashtable(20, 0.9F);

     //mine
     Field [] mine_fields = classInstance.getDeclaredFields();

     Vector signatures = new Vector();
     for(int i=0; i<mine_fields.length; i++){
       if(!Modifier.isPublic( mine_fields[i].getModifiers() )) continue;

       SigModelImpl model = new SigModelImpl(SigModelImpl.FIELD, mine_fields[i].getName(), fullClassName,
                                mine_fields[i].getType(), Modifier.isStatic(mine_fields[i].getModifiers()), true);
       signatures.addElement(model);
       tempHash.put(mine_fields[i].getName(), "");
     }


     //inherit
     Field [] inherit_fields = classInstance.getFields();

     for(int i=0; i<inherit_fields.length; i++){
       if(tempHash.get(inherit_fields[i].getName()) != null) continue;
       if(!(Modifier.isPublic( inherit_fields[i].getModifiers() ) || Modifier.isProtected( inherit_fields[i].getModifiers() ))) continue;

       SigModelImpl model = new SigModelImpl(SigModelImpl.FIELD, inherit_fields[i].getName(),
                                             fullClassName, inherit_fields[i].getType(),
                                             Modifier.isStatic(inherit_fields[i].getModifiers()), false);

       signatures.addElement(model);
     }

     signatures = getSortedNames(signatures);
     return signatures;
   }

   /**
    * 특정 class의 constructor name list를 얻는다
    *
    * @param fullClassName classname
    * @return constructor name list
    */
   public static final Vector getConstructorSignatures(String fullClassName){
     Class classInstance = getClassInstance(fullClassName);
     if(classInstance == null) return null;

     Constructor [] constructors = classInstance.getConstructors();

     Vector signatures = new Vector();
     for(int i=0; i<constructors.length; i++){
       SigModelImpl sigModel = new SigModelImpl(SigModelImpl.CONSTRUCTOR, constructors[i].getName(),
                                                fullClassName, null, false, true, constructors[i].getParameterTypes());
       signatures.addElement(sigModel);
     }

     signatures = getSortedNames(signatures);
     return signatures;
   }

   /**
    * 특정 class의 static method name list를 얻는다
    *
    * @param fullClassName classname
    * @return static method name list
    */
   public static final Vector getStaticMethodSignatures(String fullClassName){

     Vector ret = new Vector(15, 5);
     Vector signatures = getMethodSignatures(fullClassName);
     if(signatures != null)
     {
       for(int i=0; i<signatures.size(); i++){
         SigModelImpl sig = (SigModelImpl)signatures.elementAt(i);
         if(sig.isStatic()) ret.addElement(sig);
       }
       return ret;
     }
     else {
       // it must be not happened..
       return null;
     }
   }


   /**
    * 특정 class의 method name list를 얻는다
    *
    * @param fullClassName classname
    * @return method name list
    */
   public static final Vector getMethodSignatures(String fullClassName){

     Class classInstance = getClassInstance(fullClassName);
     if(classInstance == null) return null;
     Hashtable tempHash = new Hashtable(20, 0.9F);

     //mine
     Method [] mine_methods = classInstance.getDeclaredMethods();

     Vector signatures = new Vector();
     for(int i=0; i<mine_methods.length; i++){
       if(!Modifier.isPublic( mine_methods[i].getModifiers() )) continue;

       SigModelImpl model = new SigModelImpl(SigModelImpl.METHOD, mine_methods[i].getName(), fullClassName,
                            mine_methods[i].getReturnType(), Modifier.isStatic(mine_methods[i].getModifiers()),
                            true, mine_methods[i].getParameterTypes() );

       signatures.addElement(model);
       tempHash.put(mine_methods[i].getName(), "");
     }



     //inherit
     Method [] inherit_methods = classInstance.getMethods();

     for(int i=0; i<inherit_methods.length; i++){
       if(tempHash.get(inherit_methods[i].getName()) != null ) continue;

       if(!(Modifier.isPublic( inherit_methods[i].getModifiers() ) || Modifier.isProtected( inherit_methods[i].getModifiers() ))) continue;

       SigModelImpl model = new SigModelImpl(SigModelImpl.METHOD, inherit_methods[i].getName(), fullClassName,
                            inherit_methods[i].getReturnType(), Modifier.isStatic(inherit_methods[i].getModifiers()),
                            false, inherit_methods[i].getParameterTypes() );

       signatures.addElement(model);
     }

     return signatures;
   }

   /**
    * sorting 시켜준다
    *
    * @param unSorted unsorted vector
    * @return sorted vector
    */
   public static final Vector getSortedNames(Vector unSorted) {
     Hashtable dataH = new Hashtable();
     Vector dataV = new Vector();
     Vector returnV = new Vector();

     for(int i=0; i<unSorted.size(); i++){
       SigModelImpl sigModel = (SigModelImpl)unSorted.elementAt(i);
       dataH.put(sigModel.toString(), sigModel);
       dataV.addElement(sigModel.toString());
     }

     dataV = QuickSorter.sort(dataV, QuickSorter.LESS_STRING);

     for(int i=0; i<dataV.size(); i++){
       returnV.addElement((SigModelImpl)dataH.get((String)dataV.elementAt(i)));
     }
     return returnV;
   }

   /**
    * return super class name
    */
   public static final String getSuperClass(String fullClassName){
     Class original = getClassInstance(fullClassName);

     if(original == null) return null;
     if(original.getSuperclass() == null) return null;
     else  return original.getSuperclass().getName();
   }
 }


