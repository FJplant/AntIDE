/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/classpool/SigModel.java,v 1.2 1999/07/12 06:55:19 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 * $History: SigModel.java $
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:52p
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-04   Time: 12:59p
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-02   Time: 1:35a
 * Updated in $/AntIDE/source/ant/pool/classpool
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-20   Time: 2:55a
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
  * signature model
  *
  * @author Kim sang kyun
  */
 public interface SigModel {
   public static final int GENERAL = 0;
   public static final int FIELD = 1;
   public static final int CONSTRUCTOR = 2;
   public static final int METHOD = 3;

   public String getFullClassName();
   public Class[] getParameterTypes();
   public String getName();
   public int getType();
   /** if method then return type name
    *  if field then type name
    */
   public String getTypeShortName();
   public String getTypeFullName();
   public boolean isStatic();
   public String toString();
   public boolean isMine();
 }