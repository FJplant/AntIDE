/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /AntIDE/source/ant/debugger/LoadedClassesTreeModel.java 3     99-06-18 1:56a Bezant $
 * $Revision: 3 $
 * $History: LoadedClassesTreeModel.java $
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 * 
 * *****************  Version 2  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
 */
package com.antsoft.ant.debugger;

import java.util.StringTokenizer;
import javax.swing.tree.*;

/**
 *
 */
class LoadedClassesTreeModel extends DefaultTreeModel {
  public LoadedClassesTreeModel() {
    super(new DefaultMutableTreeNode("loaded classes"));
  }
  public final void reset(){
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
    rootNode.removeAllChildren();
    reload();
  }

	/**
	 * Class 이름을 트리 모델에 추가한다.
	 * @param fullClassName Package 이름을 포함한 full class 이름을 지정하여야 함.
	 *
	 */
	public void addClass( String fullClassName ) {
		//TODO: 1. package name과 class name을 추출.
		//      2. package name을 가지는 MutableTreeNode reference를 가져옴.
		//      3. 가져온 MutableTreeNode reference를 parent로 하여, reference의
		//        마지막 인덱스에 Class Name을 추가
		String packageName = null;
		String className = "";
		StringTokenizer t = new StringTokenizer(fullClassName, ".");
		className = t.nextToken();
		while ( t.hasMoreTokens() ) {
			if ( packageName == null )
				packageName = className;
			else
			  packageName = packageName + "." + className;

			className = t.nextToken();
		}

    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
		int childCount = rootNode.getChildCount();
		boolean packageFound = false;
		for ( int i = 0; i < childCount && packageFound != true; i++ ) {
			DefaultMutableTreeNode packageNode = (DefaultMutableTreeNode)rootNode.getChildAt(i);
			if ( packageNode.getUserObject().equals(packageName) ) {
			  // check the class is already loaded.
			  boolean classFound = false;
			  for ( int j = 0; j < packageNode.getChildCount(); j++ ) {
			    DefaultMutableTreeNode classNode = (DefaultMutableTreeNode)packageNode.getChildAt(j);
			  	if ( ((String)classNode.getUserObject()).equals(className) )
			  		classFound = true;
			  }

			  if ( !classFound )
			    insertNodeInto( new DefaultMutableTreeNode( className, false ),
                          packageNode, packageNode.getChildCount());
                          
				packageFound = true;
			}
		}		
		
		if ( packageFound == false ) {
			// 새로운 Package Node를 생성한다.
			DefaultMutableTreeNode newPackageNode = new DefaultMutableTreeNode( packageName, true );
			// 새로운 Class Node를 생성한다.
			DefaultMutableTreeNode newClassNode = new DefaultMutableTreeNode( className, false );
			// 만들어진 새로운 package를 root에 추가
			insertNodeInto( newPackageNode, rootNode, rootNode.getChildCount() );
			insertNodeInto( newClassNode, newPackageNode, newPackageNode.getChildCount() );
		}		
	}
}

