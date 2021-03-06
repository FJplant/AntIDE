/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectPanelTreeNode.java,v 1.3 1999/08/30 08:08:05 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * Project Panel에 폴더를 추가할 수 있도록 하기 위해서 트리의 노드를 구성하는
 * 새로운 오브젝트의 필요성이 있다.
 * 일단 폴더와 파일을 구분해 줄 필요가 있다.
 */

package com.antsoft.ant.manager.projectmanager;

import java.io.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *  class ProjectPanelTreeEntry
 *
 *  @author Jinwoo Baek
 */
public class ProjectPanelTreeNode extends DefaultMutableTreeNode implements Serializable {
	/** 노드의 타입을 나타내는 정수들 */
	static final int FOLDER = 0;
	static final int FILE   = 1;               
  private boolean isOpened = false;

	/** 노드가 포함할 객체(ProjectFileEntry 또는 String) */
	private Object obj = null;

	/** 노드의 타입 */
	private int type = 0;
  private String id = "0";
  private String parent = "0";

	/**
	 *  Constructor
	 *
	 *  @param obj ProjectFileEntry 또는 폴더 이름
	 *  @param type 노드 타입
	 */
	public ProjectPanelTreeNode(Object obj) {
		super(obj);
		if (obj instanceof String) {
			this.obj = obj;
			this.type = FOLDER;
		}
		else if (obj instanceof ProjectFileEntry) {
			this.obj = obj;
			this.type = FILE;
		}
	}

  public ProjectPanelTreeNode(Object obj, String id, String pid){
    this(obj);
    this.id = id;
    this.parent = pid;
  }

  public void setOpened(boolean flag){
    isOpened = flag;
  }

  public boolean isOpened(){
    return isOpened;
  }

	/**
	 *  현 노드의 타입을 얻는다.
	 *
	 *  @return int 현 노드의 타입
	 */
	int getType() {
		return type;
	}

	/**
	 *  현 노드가 폴더인지 확인한다.
	 *
	 *  @return boolean 현노드가 폴더이면 true 아니면 false
	 */
	boolean isFolder() {
		if (type == FOLDER) return true;
		else return false;
	}

	/**
	 *  현 노드가 파일인지 확인한다.
	 *
	 *  @return boolean 현 노드가 파일이면 true 아니면 false
	 */
	boolean isFile() {
		if (type == FILE) return true;
		else return false;
	}

  public String getID() {
  	return id;
  }

  public void setID(String id) {
  	this.id = id;
  }

  public String getParentID() {
  	return parent;
  }

  public void setParentID(String parent) {
  	this.parent = parent;
  }

	/**
	 *  현 노드가 포함하고 있는 객체를 반환한다.
	 *
	 *  @return Object 현 노드가 포함하고 있는 객체
	 */
	Object getObject() {
		return obj;
	}

	/**
	 *  포함하는 객체를 설정한다.
	 *
	 *  @param Object 설정할 객체
	 */
	void setObject(Object obj) {
		if (obj != null) {
			if (obj instanceof String) {
				this.type = FOLDER;
				this.obj = obj;
			}
			else if (obj instanceof ProjectFileEntry) {
				this.type = FILE;
				this.obj = obj;
			}
		}
	}

  public Object getUseObject() {
  	return obj;
  }

  public boolean equals(Object object) {
  	if (object != null) {
    	if (object instanceof ProjectPanelTreeNode) {
      	ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)object;
      	if ((pptn.getType() == type) && pptn.getUserObject().equals(obj))
        	return true;
      }
    }
    return false;
  }

	/**
	 *  현 노드를 나타내는 String을 반환한다.
	 *
	 *  @return String 현 노드를 나타내는 String
	 */
	public String toString() {
		if (obj != null) {
			return obj.toString();
			//if (obj instanceof ProjectFileEntry) return ((ProjectFileEntry)obj).toString();
			//else return obj.toString();
		}
		else return null;
	}
}
