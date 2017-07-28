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
 *  ������Ʈ���� ������ �ҽ��� �����ϴ� Ŭ����
 *  �ҽ� �̸� = ���� �̸�
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
   *  @param pathName ã�� �ҽ��̸�
   *  @return SourceEntry ã�� �ҽ� ��Ʈ��
   *          ������ �߻��ϸ�, null�� ��ȯ
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
   * �ܺο��� ����Ǿ����� ȣ��ȴ�
   *
   * @return ���� ���µǾ����� ����Ǿ����� ���ο� SourceEntry�� ��ȯ�ϰ�
   * �׷��� ������ null�� ��ȯ�Ѵ�
   */
  public SourceEntry getSourceWhenExternallyUpdated(ProjectFileEntry pfe){
    SourceEntry se = (SourceEntry)sources.get(pfe.getFullPathName());
    if ( se == null)  return null;
    sources.remove(pfe.getFullPathName());
    return openSource(pfe.getFullPathName());
  }


	/**
	 *  �����̸��� ������ ���� ������� �ҽ��� �����Ѵ�.
	 *  Class Designer�κ��� �����Ǵ� �ҽ����� ó���Ѵ�.
	 *
	 *  @param fileName ������ �ҽ��� ���� �̸� (Ŭ���� �̸��� �ȴ�.)
	 *  @param fileData ������ �ҽ��� ����
	 */
	public SourceEntry newSource(String pathName, String fileName, String fileData) {
  	SourceEntry se = new SourceEntry(pathName, fileName, fileData);
  	if ( se.getDocument() == null ) 
  		return null;
  		
    sources.put(se.getFullPathName(), se);
    return se;
	}

  /**
   *  ����Ʈ�� �ش� �̸��� �ҽ��� ���� ��� ������ ����.
   *
   *  @param pathname �� ������ ��� �̸�
   *  @param filename �� ���� �̸�
   *  @return SourceEntry �� �ҽ� ��ȯ
   */
  public SourceEntry openSource(String fullPath) {
    SourceEntry se = new SourceEntry(fullPath);
    if ( se.getDocument() == null )
    	return null;

    sources.put(se.getFullPathName(), se);
    return se;
  }

  /**
   *  ���̻� � View�� �ҽ��� �����ϰ� ���� ������ ����Ʈ���� �����Ѵ�.
   *
   *  @param source_name ���� ��� �ҽ�
   */
  public void deleteSource(String fullPathName) {
    sources.remove(fullPathName);
  }

  /**
   *  ���̻� � View�� �ҽ��� �����ϰ� ���� ������ ����Ʈ���� �����Ѵ�.
   *
   *  @param path ���� ��� �ҽ� Path
   *  @param name ���� ��� �ҽ�
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
