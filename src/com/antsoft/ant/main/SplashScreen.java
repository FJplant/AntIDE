/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/main/SplashScreen.java,v 1.7 1999/08/13 08:13:09 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 */
package com.antsoft.ant.main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class SplashScreen extends Window{
	JLabel StatusBar;

	// SplashScreen's constructor
	public SplashScreen(Frame owner, ImageIcon CoolPicture){		
		super(owner);

		// Create a JPanel so we can use a BevelBorder
		JPanel PanelForBorder=new JPanel(new BorderLayout());
		PanelForBorder.setLayout(new BorderLayout());
		PanelForBorder.add(new JLabel(CoolPicture),BorderLayout.CENTER);
		PanelForBorder.add(StatusBar=new JLabel("...",SwingConstants.CENTER),BorderLayout.SOUTH);
		PanelForBorder.setBorder(new BevelBorder(BevelBorder.RAISED));

		add(PanelForBorder);
		pack();

		// Plonk it on center of screen
		Dimension WindowSize=getSize(),ScreenSize=Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((ScreenSize.width-WindowSize.width)/2,(ScreenSize.height-WindowSize.height)/2,WindowSize.width,WindowSize.height);
		setVisible(true);
	}

	public void showStatus(String CurrentStatus){
		try{
			// Update Splash-Screen's status bar in AWT thread
			SwingUtilities.invokeLater(new UpdateStatus(CurrentStatus));
		}catch(Exception e){e.printStackTrace();}
	}

	public void close(){
		try{
			// Close and dispose Window in AWT thread
			SwingUtilities.invokeLater(new CloseSplashScreen());
		}catch(Exception e){e.printStackTrace();}
	}

	class UpdateStatus implements Runnable{
		String NewStatus;
		public UpdateStatus(String Status){NewStatus=Status;}
		public void run(){StatusBar.setText(NewStatus);}
	}

	class CloseSplashScreen implements Runnable{
		public void run(){
			setVisible(false);
			dispose();
		}
	}
}
