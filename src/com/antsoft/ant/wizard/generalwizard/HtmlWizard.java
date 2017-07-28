/*
 *	package : com.antsoft.ant.wizard.generalwizard
 *	source  : HtmlWizard.java
 *	date    : 1999.8.20
 *
 *	HtmlWizard - html을 생성하는 위저드
 */

package com.antsoft.ant.wizard.generalwizard;

import java.awt.*;
import java.io.*;

public class HtmlWizard{ 

	private String currentpath;
	private HtmlDialog htmlDlg;
	private String name;
	
	private File[] file;
	private FileOutputStream fs;
	
	/**
	 *	HtmlWizard -  constructor 
	 */
	public HtmlWizard(Frame parent,String path) {
		this.currentpath = path;
		htmlDlg = new HtmlDialog(parent,"Html Wizard",true);
		if(htmlDlg.isOK()){
			initData();
			generateHtmlCode();
		}
	}
	
	private void initData(){
		name = htmlDlg.name.getText();
	}
	
	private void generateHtmlCode(){
		try{
			createFile();
			writer("<html>\n");
			writer("\t<head><title></title></head>\n");
			writer("\t<body>\n");
			writer("\t</body>\n");
			writer("</html>\n");
			
			fs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void createFile() throws IOException{
		file = new File[1];
			
		File parent = new File(currentpath);
		// 새로운 sourcefile을 만든다.
  	 if ((parent != null)&&(parent.exists()||parent.mkdirs())){
  	 	String filename = null;
  	 	if(name.endsWith(".html") || name.endsWith(".htm")) filename = name;
  	 	else filename = name+".html";
  	    	  
  		file[0] = new File( parent, filename );
  		fs = new FileOutputStream(file[0]);
  	}else{
  		System.out.println("parent dir is null");
  	 	return; 	
  	}
	}
	
	private void writer(String s) throws IOException{
		fs.write(s.getBytes());
	}
	
	public File[] getFiles(){
		return file;
	}
}
