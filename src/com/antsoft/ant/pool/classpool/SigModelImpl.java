/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/classpool/SigModelImpl.java,v 1.2 1999/07/12 06:55:23 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 * $History: SigModelImpl.java $
 * 
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:52p
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 99-05-04   Time: 1:02p
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 4  *****************
 * User: Kahn         Date: 99-05-04   Time: 11:57a
 * Updated in $/AntIDE/source/ant/pool/classpool
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-04   Time: 9:17a
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-02   Time: 1:35a
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-10-20   Time: 2:55a
 * Updated in $/JavaProjects/src/ant/pool/classpool
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-16   Time: 12:45a
 * Updated in $/JavaProjects/src/ant/pool/classpool
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-14   Time: 9:17p
 * Updated in $/JavaProjects/src/ant/pool/classpool
 * ant.classpool -> ant.pool.classpool
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-14   Time: 3:41a
 * Created in $/JavaProjects/src/ant/classpool
 *
 */
 package com.antsoft.ant.pool.classpool;


 /**
  * method와 constructor의 signature model implementation
  *
  * @author Kim sang kyun
  */
 public class SigModelImpl implements SigModel {
   private Class [] parameterTypes;
   private String name = "";
   private int type;
   private String fullClassName = "";
   private Class typeInstance;
   private String typeName;
   private boolean staticFlag;
   private boolean isMine;

   /** default constructor */
   public SigModelImpl(int type, String name, String fullClassName, Class typeInstance,
                       boolean isStatic, boolean isMine){
     this.type = type;
     this.name = name;
     this.fullClassName = fullClassName;
     this.typeInstance = typeInstance;
     this.staticFlag = isStatic;
     this.isMine = isMine;
   }

   public SigModelImpl(int type, String name, String fullClassName, Class typeInstance,
                       boolean isStatic, boolean isMine, Class[] parameterTypes){
     this(type, name, fullClassName, typeInstance, isStatic, isMine);
     this.parameterTypes = parameterTypes;
   }  

   public SigModelImpl(){
   }

   public boolean isMine(){
     return isMine;
   }

   public void setMine(boolean flag){
     this.isMine = flag;
   }

   public void setReturnType(Class type){
     this.typeInstance = type;
   }

   public void setTypeInstance(Class instance){
     this.typeInstance = instance;
   }

   public String getTypeFullName(){

     String longName = typeInstance.getName();
     if(longName.indexOf(".") != -1){
       return longName.substring(0,longName.lastIndexOf(".")) + "." + getMeaningFullName(typeInstance);
     }
     else{
       return getMeaningFullName(typeInstance);
     }
   }

   public String getTypeShortName(){
     return getMeaningFullName(typeInstance);
   }

   public int getType(){
     return type;
   }

   public void setType(int type){
     this.type = type;
   }

   public String getFullClassName(){
     return fullClassName;
   }

   public void setFullClassName(String fullClassName){
     this.fullClassName = fullClassName;
   }

   public boolean isStatic(){
     return this.staticFlag;
   }

   public void setStatic(boolean isstatic){
     this.staticFlag = isstatic;
   }

   /**
    * parameter type들의 list를 제공
    *
    * @return parameter type list
    */
   public Class[] getParameterTypes(){
     return parameterTypes;
   }

   /**
    * parameter type을 add한다
    *
    * @param parameterType parameter type
    */
   public void setParamterTypes(Class[] parameterTypes){
     this.parameterTypes = parameterTypes;
   }

   /**
    * Method 또는 Constructor의 name 제공
    *
    * @return name
    */
   public String getName(){
     return name;
   }

   /**
    * Method 또는 Constructor의 name 설정
    *
    * @param name
    */
   public void setName(String name){
     this.name = name;
   }

   /**
    * signature를 제공하는 외부 interface
    *
    * @return signature
    */
   public String toString(){
     String sig = "";

     switch(type){
       case GENERAL :
              sig = getName();
              break;

       case FIELD :
              sig = getName();
              break;

       case CONSTRUCTOR :
              sig = getMethodTypeSig();
              break;

       case METHOD :
              sig = getMethodTypeSig();
              break;

       default :
              break;

     }

     return sig;
   }

   private String getMeaningFullName(Class instance){

     StringBuffer buf = new StringBuffer();
     String param=instance.getName();

     //primitive type
	   if (param.lastIndexOf(".")==-1)
     {
       //array type
       if(instance.isArray())
       {
         String arrParam="";
         for(int k=0; k<param.length(); k++)
         {
           if(param.charAt(k) == '[')
           {
             arrParam += "[]";
           }
           else
           {
             switch(param.charAt(k)){
               case 'Z' : arrParam = "boolean" + arrParam; break;
               case 'B' : arrParam = "byte" + arrParam; break;
               case 'C' : arrParam = "char" + arrParam; break;
               case 'S' : arrParam = "short" + arrParam; break;
               case 'I' : arrParam = "int" + arrParam; break;
               case 'J' : arrParam = "long" + arrParam; break;
               case 'F' : arrParam = "float" + arrParam; break;
               case 'D' : arrParam = "double" + arrParam; break;
             }
             buf.append(arrParam);
           }
         }
       }//array type end
       else
       {
         buf.append(param);
       }
     }
	   else
     {
       String arrParam2 = "";
       if(param.startsWith("[")){
         for(int k=0; k<param.length(); k++)
         {
           if(param.charAt(k) == '['){
             arrParam2 += "[]";
           }
           else if(param.charAt(k) == 'L')
           {
             arrParam2 = param.substring( param.lastIndexOf(".")+1, param.length()-1 ) + arrParam2;
             break;
           }
         }
       }
       else { arrParam2 = param.substring(param.lastIndexOf(".")+1,param.length()); }
        buf.append(arrParam2);
     }

     return buf.toString();
   }


   private String getMethodTypeSig(){

     String sig="";

  	 for (int i=0;i<parameterTypes.length;++i) {
       sig += getMeaningFullName(parameterTypes[i]);
       if(i != parameterTypes.length-1) sig +=",";
     }

     return name+"("+sig+")";
   }
 }

