/*
 *	package : com.antsoft.ant.wizard.generalwizard
 *	source  : HtmlDialog.java
 *	date    : 1999.8.20
 */

package com.antsoft.ant.wizard.generalwizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import com.antsoft.ant.util.*;

public class HtmlDialog extends JDialog implements ActionListener,KeyListener{ 

	public JTextField name;
	private JButton ok;
	private JButton cancel;
	private boolean isOK = false;
	
	/**
	 *	TextDialog -  constructor 
	 */
	public HtmlDialog(Frame parent,String title,boolean modal) {
		super(parent,title,modal);
		
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
		name = new JTextField("html1.html",15);
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

	public boolean isOK(){
		return isOK;
	}
	
	//////////////// event Ã³¸®  /////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e){
		dispose();
		if(e.getSource()==ok){
			isOK = true;
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
