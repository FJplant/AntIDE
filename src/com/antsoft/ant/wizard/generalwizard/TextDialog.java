/*
 *	package : com.antsoft.ant.wizard.generalwizard
 *	source  : TextDialog.java
 *	date    : 1999.8.19
 *
 *	TextDialog - text file생성을 위해 이름을 받는 다이얼로그
 */

package com.antsoft.ant.wizard.generalwizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

import com.antsoft.ant.util.*;

public class TextDialog extends JDialog implements ActionListener,KeyListener{ 

	private JTextField name;
	private JButton ok;
	private JButton cancel;
	private String currentpath;
	private File[] file;
	private RandomAccessFile rf;
	
	
	/**
	 *	TextDialog -  constructor 
	 */
	public TextDialog(Frame parent,String path) {
		super(parent,"Text File Wizard",true);
		this.currentpath = path;
		
		try{
			aInit();
			setSize(300, 120);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      //setResizable( false );
      this.setVisible(true); 
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
	}
	
	private void aInit(){
		//name
		JLabel nameLbl = new JLabel("File Name");
		name = new JTextField("text1.txt",15);
		name.addKeyListener(this); 
		nameLbl.setPreferredSize(new Dimension(70,20)); 
		
		//button
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		//name panel
		JPanel nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameP.add(nameLbl);
		nameP.add(name);
		
		//button panel
		JPanel buttonP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonP.add(ok);
		buttonP.add(cancel);
		
		//total panel
		JPanel totalP = new JPanel(new GridLayout(2,1));
		totalP.setBorder(new EtchedBorder()); 
		totalP.add(nameP);
		totalP.add(buttonP);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(totalP,BorderLayout.CENTER);
		this.getContentPane().add(new JPanel(),BorderLayout.NORTH);
		this.getContentPane().add(new JPanel(),BorderLayout.WEST);
		this.getContentPane().add(new JPanel(),BorderLayout.EAST);
		this.getContentPane().add(new JPanel(),BorderLayout.SOUTH);  
	}

	private void createFile(){
		try{
			file = new File[1]; 
			File parent = new File(currentpath);
  	    if ((parent != null) && (parent.exists() || parent.mkdirs())){
  	    	String filename = null;
  	    	if(name.getText().endsWith(".txt")) filename = name.getText();
  	    	else filename=name.getText()+".txt";
  	    	file[0] = new File( parent, filename );
//  	    	if(file[0].exists()) JOptionPane.showMessageDialog(this,"Exist file!");
  	    	rf = new RandomAccessFile (file[0],"rw");
  	    }
  	    rf.close();
  	    rf = null;
  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
	}
	
	public File[] getFiles(){
		return file;
	}
	
	//////////////// event 처리  /////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e){
		dispose();
		if(e.getSource()==ok){
			createFile();
		}
	}
	public void keyReleased(KeyEvent e){
		if(!StripString.isNull(name.getText())) ok.setEnabled(true);
		else ok.setEnabled(false); 
		e.consume();  
	}
	public void keyPressed(KeyEvent e){
	}
	public void keyTyped(KeyEvent e){
	}
}
