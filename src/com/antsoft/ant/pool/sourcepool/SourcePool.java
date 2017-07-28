/*
 * $Id: SourcePool.java,v 1.5 1999/08/30 07:59:07 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.pool.sourcepool;

import java.awt.*;
import java.io.File;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.text.*;

import com.antsoft.ant.manager.projectmanager.ProjectFileEntry;

/**
 *  프로젝트별로 열려진 소스를 관리하는 클래스
 *  소스 이름 = 파일 이름
 *
 *  @author Jin-woo Baek
 *  @author kim sang kyun
 *  @author Kwon, Young Mo
 */
public class SourcePool {
  private Hashtable sources = new Hashtable(10);

  /**
   *  Constructor
   */
  public SourcePool() {
  }

  /**
   *  Get source entry named "source_name"
   *
   *  @param pathName 찾을 소스이름
   *  @return SourceEntry 찾은 소스 엔트리
   *          에러가 발생하면, null을 반환
   */
  public SourceEntry getSource(String fullPath) {
    SourceEntry se = (SourceEntry)sources.get(fullPath);
    if (se != null) 
    	return se;

    se = openSource(fullPath);
    return se;
  }

  public SourceEntry getSource(ProjectFileEntry pfe) {
    SourceEntry ret = (SourceEntry)sources.get(pfe.getFullPathName());
    if(ret != null){
      if (pfe.isExternallyChanged()) {
      	pfe.syncLastModifiedTimeWithRealFile();
      	sources.remove(pfe.getFullPathName());
        ret = openSource(pfe.getFullPathName());
      }
    }
    else {
      ret = openSource(pfe.getFullPathName());
    }

    return ret;
  }

  public Enumeration getOpenedSources(){
    return sources.elements();
  }

  /**
   * 외부에서 변경되었을대 호출된다
   *
   * @return 만약 오픈되었던게 변경되었으면 새로운 SourceEntry를 반환하고
   * 그렇지 않으면 null을 반환한다
   */
  public SourceEntry getSourceWhenExternallyUpdated(ProjectFileEntry pfe){
    SourceEntry se = (SourceEntry)sources.get(pfe.getFullPathName());
    if ( se == null)  return null;
    sources.remove(pfe.getFullPathName());
    return openSource(pfe.getFullPathName());
  }


	/**
	 *  파일이름과 내용이 새로 만들어진 소스를 생성한다.
	 *  Class Designer로부터 생성되는 소스들을 처리한다.
	 *
	 *  @param fileName 생성될 소스의 파일 이름 (클래스 이름이 된다.)
	 *  @param fileData 생성될 소스의 내용
	 */
	public SourceEntry newSource(String pathName, String fileName, String fileData) {
  	SourceEntry se = new SourceEntry(pathName, fileName, fileData);
  	if ( se.getDocument() == null ) 
  		return null;
  		
    sources.put(se.getFullPathName(), se);
    return se;
	}

  /**
   *  리스트에 해당 이름의 소스가 없을 경우 파일을 연다.
   *
   *  @param pathname 열 파일의 경로 이름
   *  @param filename 열 파일 이름
   *  @return SourceEntry 연 소스 반환
   */
  public SourceEntry openSource(String fullPath) {
    SourceEntry se = new SourceEntry(fullPath);
    if ( se.getDocument() == null )
    	return null;

    sources.put(se.getFullPathName(), se);
    return se;
  }

  /**
   *  더이상 어떤 View도 소스를 참조하고 있지 않으면 리스트에서 삭제한다.
   *
   *  @param source_name 삭제 대상 소스
   */
  public void deleteSource(String fullPathName) {
    sources.remove(fullPathName);
  }

  /**
   *  더이상 어떤 View도 소스를 참조하고 있지 않으면 리스트에서 삭제한다.
   *
   *  @param path 삭제 대상 소스 Path
   *  @param name 삭제 대상 소스
   */
  public void deleteSource(String path, String name) {
    File obj = new File(path, name);
    sources.remove(obj.getAbsolutePath());
  }

  public void deleteAll(){
    if(sources != null) {
      for(Enumeration e=sources.keys(); e.hasMoreElements(); ){
        String key = (String)e.nextElement();
        sources.remove(key);
      }
      sources.clear();
    }
  }

  public void addSource(SourceEntry se) {  	
    sources.put(se.getFullPathName(), se);
  }
}
