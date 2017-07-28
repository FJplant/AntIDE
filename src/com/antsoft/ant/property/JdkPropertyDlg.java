/*

 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/JdkPropertyDlg.java,v 1.11 1999/08/31 09:13:21 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.11 $
 * $History: JdkPropertyDlg.java $
 *
 * *****************  Version 31  *****************
 * User: Kahn         Date: 99-05-24   Time: 11:01a
 * Updated in $/AntIDE/source/ant/property
 *
 * *****************  Version 30  *****************
 * User: Remember     Date: 99-06-15   Time: 2:29p
 * Updated in $/AntIDE/source/ant/property
 *
 * *****************  Version 29  *****************
 * User: Remember     Date: 99-06-04   Time: 2:50p
 * Updated in $/AntIDE/source/ant/property
 *
 */
package com.antsoft.ant.property;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.zip.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.IdePropertyManager;

/**
 * jdk property를 설정하는 dialog
 *
 * @author kim sang kyun
 */

public class JdkPropertyDlg extends JDialog{
  private JTextField javaTf, classPathTf, sourcePathTf, docPathTf;
  private JButton javaBtn, classPathBtn, sourcePathBtn, docPathBtn,
                  cancelBtn, addBtn, removeBtn, editBtn, okBtn;
  private JList jdkList;
  private JdkListModel jdkListModel;
  private DefaultListSelectionModel jdkListSelectModel;

  private JFrame parent;
  private JdkInfoContainer jdkInfoContainer;
  private JLabel javaLbl, classPathLbl, sourcePathLbl, docPathLbl;
  private JScrollPane jdkPane;

  private JdkInfo selectedJdkInfo = null;
  private JdkInfo currentEditingJdkInfo = null;

  private JdkInfoContainer jdkInfos;

  private boolean isOK = false;
  private Vector removedJdks;
  private Thread sbThread;
  private JFrame frame;

  public JdkPropertyDlg(JFrame f, String title, boolean modal, JdkInfoContainer jdkInfos) {
    super(f, title, modal);
    frame = f;
    this.jdkInfos = jdkInfos;
    this.parent = f;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();
    removedJdks = new Vector(3, 2);

    //java.exe label
    if(System.getProperty("os.name").indexOf("Win") != -1)
      javaLbl = new JLabel(" Java.EXE (Not in jre directory)", JLabel.LEFT);
    else
      javaLbl = new JLabel(" Java (Not in jre directory)", JLabel.LEFT);
    javaLbl.setPreferredSize(new Dimension(180,20));
    //javaLbl.setEnabled(false);

    //java.exe text field
    javaTf = new JTextField(19);
    javaTf.setPreferredSize(new Dimension(150,20));
    javaTf.setEnabled(false);
    javaBtn = new JButton("...");
    javaBtn.setPreferredSize(new Dimension(20,20));
    javaBtn.setEnabled(false);

    javaBtn.setActionCommand("JAVA.EXE");
    javaBtn.addActionListener(al);

    //jdk panel
    JPanel jdkP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    jdkP.add(javaTf);
    jdkP.add(javaBtn);

    //class path label
    classPathLbl = new JLabel(" Class Path", JLabel.LEFT);
    classPathLbl.setPreferredSize(new Dimension(180,20));
    //classPathLbl.setEnabled(false);

    //class path text field
    classPathTf = new JTextField(19);
    classPathTf.setPreferredSize(new Dimension(150,20));
    classPathTf.setEnabled(false);
    classPathBtn = new JButton("...");
    classPathBtn.setPreferredSize(new Dimension(20,20));
    classPathBtn.setEnabled(false);
    classPathBtn.setActionCommand("CLASSPATH");
    classPathBtn.addActionListener(al);

    //class path panel
    JPanel classP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    classP.add(classPathTf);
    classP.add(classPathBtn);

    //source path label
    sourcePathLbl = new JLabel(" Source Path", JLabel.LEFT);
    sourcePathLbl.setPreferredSize(new Dimension(180,20));
    //sourcePathLbl.setEnabled(false);

    //source path text field
    sourcePathTf = new JTextField(19);
    sourcePathTf.setPreferredSize(new Dimension(150,20));
    sourcePathTf.setEnabled(false);
    sourcePathBtn = new JButton("...");
    sourcePathBtn.setPreferredSize(new Dimension(20,20));
    sourcePathBtn.setEnabled(false);
    sourcePathBtn.setActionCommand("SOURCE");
    sourcePathBtn.addActionListener(al);

    //source path panel
    JPanel sourceP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceP.add(sourcePathTf);
    sourceP.add(sourcePathBtn);

    //doc path label
    docPathLbl = new JLabel(" Doc Path");
    docPathLbl.setPreferredSize(new Dimension(180,20));
    //docPathLbl.setEnabled(false);

    //source path text field
    docPathTf = new JTextField(19);
    docPathTf.setPreferredSize(new Dimension(150,20));
    docPathTf.setEnabled(false);
    docPathBtn = new JButton("...");
    docPathBtn.setPreferredSize(new Dimension(20,20));
    docPathBtn.setEnabled(false);
    docPathBtn.setActionCommand("DOC");
    docPathBtn.addActionListener(al);

    //doc panel
    JPanel docP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    docP.add(docPathTf);
    docP.add(docPathBtn);

    //button
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    addBtn = new JButton("Add");
    addBtn.setActionCommand("ADD");
    addBtn.addActionListener(al);

    removeBtn = new JButton("Remove");
    removeBtn.setActionCommand("REMOVE");
    removeBtn.addActionListener(al);

    editBtn = new JButton("Edit");
    editBtn.setActionCommand("EDIT");
    editBtn.addActionListener(al);

    JPanel buttonP = new JPanel();
    buttonP.add(addBtn);
    buttonP.add(removeBtn);
    buttonP.add(editBtn);
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);

    //jdk list
    jdkList = new JList();
    jdkList.setFont(FontList.myTextFieldFont);
    jdkListModel = new JdkListModel(jdkInfos);
    jdkList.setModel(jdkListModel);

    ListSelectionListener lsl = new ListSelectionHandler();
    jdkListSelectModel = new DefaultListSelectionModel();
    jdkListSelectModel.addListSelectionListener(lsl);
    jdkList.setSelectionModel(jdkListSelectModel);

    JScrollPane listPane = new JScrollPane(jdkList);
    listPane.setPreferredSize(new Dimension(230,190));

    //jdk list panel
    JPanel listP = new JPanel();
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"JDK List");
    border1.setTitleColor(Color.black);
    border1.setTitleFont(FontList.regularFont);
    listP.setBorder(border1);
    listP.add(listPane);

    //right panel
    JPanel rightP = new JPanel(new GridLayout(8,1));
    rightP.setBorder(BorderList.pathBorder);
    rightP.add(javaLbl);
    rightP.add(jdkP);
    rightP.add(classPathLbl);
    rightP.add(classP);
    rightP.add(sourcePathLbl);
    rightP.add(sourceP);
    rightP.add(docPathLbl);
    rightP.add(docP);

    //total panel
    JPanel totalP = new JPanel(new GridLayout(1,2));
    totalP.add(listP);
    totalP.add(rightP);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(totalP,BorderLayout.CENTER);
    getContentPane().add(buttonP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
    
    //focus setting
    javaTf.setNextFocusableComponent(classPathTf);
    classPathTf.setNextFocusableComponent(sourcePathTf);
    sourcePathTf.setNextFocusableComponent(docPathTf);

    //this.setSize(440,330);
    this.setResizable(false);
  }

  public boolean isOk(){
    return isOK;
  }

  public JdkInfoContainer getJdkInfos(){
    return this.jdkInfos;
  }

  public JdkInfo getSelectedJdkInfo(){
    return selectedJdkInfo;
  }

  public void classPathBtnSelected(){

    /*
    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("jar");
    filter.addExtension("zip");

    JFileChooser filechooser = new JFileChooser();

    filechooser.setFileFilter(filter);
    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setMultiSelectionEnabled(false);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("ClassPath");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("ClassPath");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();

      if(!classPathTf.getText().equals("")){
        classPathTf.setText(classPathTf.getText() + ", " + f.getAbsolutePath());
      }
      else{
        classPathTf.setText(f.getAbsolutePath());
      }
    }

    */
    DirChooser dirchooser = new DirChooser(frame, "Choose Class Path", classPathTf.getText() , true,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      if(!classPathTf.getText().equals("")){
        classPathTf.setText(classPathTf.getText() + ", " + f.getAbsolutePath());
      }
      else{
        classPathTf.setText(f.getAbsolutePath());
      }
    }
  }

  public void sourceBtnSelected(){
    /*
    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("jar");
    filter.addExtension("zip");

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);

    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JDK Source Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      sourcePathTf.setText(f.getAbsolutePath());
    }
    */
    DirChooser dirchooser = new DirChooser(frame, "Select JDK Source Directory", sourcePathTf.getText() , true,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      sourcePathTf.setText(f.getAbsolutePath());
    }
  }

  public void docBtnSelected(){
    /*
    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JDK Doc Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      docPathTf.setText(f.getAbsolutePath());
    }
    */
    DirChooser dirchooser = new DirChooser(frame, "Choose Class Path", docPathTf.getText() ,false,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      docPathTf.setText(f.getAbsolutePath());
    }
  }

  public void javaBtnSelected(){
    NameFileFilter filter = null;

    if(System.getProperty("os.name").indexOf("Win") != -1)
      filter = new NameFileFilter("java.exe", "java.exe");
    else
      filter = new NameFileFilter("java", "java");


    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JDK Doc Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      javaTf.setText(f.getAbsolutePath());

      try{
        String [] command = {f.getAbsolutePath(), "-version"};
  			process = Runtime.getRuntime().exec(command);

        try{ process.waitFor(); }
        catch(InterruptedException e){}

        process.getOutputStream().close();
        process.getInputStream().close();
  		}
	  	catch(IOException io){ return; }
      stderr = new StderrThread();
    }
  }

  private Process process;
  private StderrThread stderr;
  private String currJdkVersion = null;
  class StderrThread extends Thread
	{
		StderrThread(){ start();	}

		public void run(){

			try
			{
				BufferedReader err = new BufferedReader(	new InputStreamReader(process.getErrorStream()));
        SwingUtilities.invokeLater(new SafeAppend(err.readLine()));
				err.close();
			}
			catch(IOException io){}
		}
	}

  class SafeAppend implements Runnable
	{
		private String jdkVersion;

		SafeAppend(String msg){	jdkVersion = msg;	}

		public void run(){
      currJdkVersion = jdkVersion;
      String javaexePath = javaTf.getText();
      String javaexeLowerpath =  javaTf.getText().toLowerCase();
      File javaexe = new File(javaexePath);
      File javaHome = null;
      File classPath = null;
      File sourcePath = null;
      File docPath = null;

      File parent1 = new File(javaexe.getParent());
      File parent2 = new File(parent1.getParent());

      javaHome = parent2;

      //jdk1.0 or jdk1.1
      if(currJdkVersion.indexOf("1.1") != -1 || currJdkVersion.indexOf("1.0") != -1){
        //set classpath
        classPath = new File(javaHome.getAbsolutePath() + File.separator +"lib"+File.separator+"classes.zip");
        if(classPath.exists()) classPathTf.setText(classPath.getAbsolutePath());

        //set source path
        sourcePath = new File(javaHome.getAbsolutePath() + File.separator +"src");
        if(sourcePath.exists()) sourcePathTf.setText(sourcePath.getAbsolutePath());

        //set doc path
        docPath = new File(javaHome.getAbsolutePath() + File.separator +"docs"+File.separator+"api");
        if(docPath.exists()) docPathTf.setText(docPath.getAbsolutePath());

      }
      //jdk1.2
      else if(currJdkVersion.indexOf("1.2") != -1){
        classPath = new File(javaHome.getAbsolutePath() + File.separator +"jre"+File.separator+"lib"+File.separator+"rt.jar");
        if(classPath.exists()) classPathTf.setText(classPath.getAbsolutePath());

        //set source path
        sourcePath = new File(javaHome.getAbsolutePath() + File.separator +"src.jar");
        if(sourcePath.exists()) sourcePathTf.setText(sourcePath.getAbsolutePath());

        //set doc path
        docPath = new File(javaHome.getAbsolutePath() + File.separator +"docs"+File.separator+"api");
        if(docPath.exists()) docPathTf.setText(docPath.getAbsolutePath());
      }
		}
	}

  public void editBtnSelected(){
    JdkInfo jdkInfo = (JdkInfo)jdkListModel.getElementAt(jdkList.getSelectedIndex());
    this.currentEditingJdkInfo = jdkInfo;
    this.currJdkVersion = jdkInfo.toString();

    javaTf.setText(jdkInfo.getJavaEXEPath());
    classPathTf.setText(jdkInfo.getClassPathString());
    sourcePathTf.setText(jdkInfo.getSourcePath());
    docPathTf.setText(jdkInfo.getDocPath());
  }

  public void removeBtnSelected(){
    if(jdkListModel.getSize() > 0 && !jdkListSelectModel.isSelectionEmpty())
    {
      removedJdks.addElement(jdkListModel.getElementAt(jdkList.getSelectedIndex()));
      jdkListModel.removeElementAt(jdkList.getSelectedIndex());
    }

    setEnableFlag(false);
    setEmptyFieldEnabled();
  }

  public boolean doneBtnSelected(){
    File javac = new File(javaTf.getText());
    File classPath = new File(classPathTf.getText());
    File source = new File(sourcePathTf.getText());
    File doc = new File(docPathTf.getText());
    boolean isfalse = true;
    boolean isMSWindow = OSVerifier.getOS().equals(OSVerifier.MS_WINDOW);


    if(javaTf.getText().equals("")){
      if(isMSWindow){
        JOptionPane.showMessageDialog(null, "JAVA.EXE not specified!!", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
      }
      else{
        JOptionPane.showMessageDialog(null, "JAVA not specified!!", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
      }
    }

    else if(classPathTf.getText().equals(""))
    JOptionPane.showMessageDialog(null, "CLASSPATH not specified!!", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );

    else if(isMSWindow && ( !javac.exists() || !javaTf.getText().endsWith("java.exe"))){
      JOptionPane.showMessageDialog(null, "JAVA.EXE Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
    }

    else if(!isMSWindow && ( !javac.exists() || !javaTf.getText().endsWith("java"))){
      JOptionPane.showMessageDialog(null, "JAVA Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
    }

    else if(classPathTf.getText().length() > 0 && !classPath.exists())
    JOptionPane.showMessageDialog(null, "Class Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );

    else if(sourcePathTf.getText().length() > 0 && !source.exists())
    JOptionPane.showMessageDialog(null, "Source Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );

    else if(docPathTf.getText().length() > 0 && !doc.exists())
    JOptionPane.showMessageDialog(null, "Doc Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );

    else
    {
      isfalse = false;
      addJdkInfo();

    }

    if(!source.exists()) return isfalse;
   
    String name = source.getName().toLowerCase();
    if(source.isDirectory() || name.endsWith(".jar") || name.endsWith(".zip")){
       SourceBrowser sb = new SourceBrowser(source, "");
       sbThread = new SourceBrowser(source, "");
       sbThread.start();
    }

    return isfalse;
  }


  static int count=0;

  class SourceBrowser extends Thread{

    private File root;
    private String prefix;
    private ProgressMonitor monitor;
    private int index=0;
    private boolean isCanceled = false;
    private boolean inProcessing = false;

    public SourceBrowser(File root, String prefix){
      super("SourceBrowser");
      this.root = root;
      this.prefix = prefix;
    }

    public void setCanceled(boolean isCanceled){
      this.isCanceled = isCanceled;
    }

    public void run(){
      inProcessing = true;

      monitor = new ProgressMonitor(JdkPropertyDlg.this, "Extracting param from source!!", "Please wait...", 0, 100);
      monitor.setMillisToDecideToPopup(0);
      monitor.setMillisToPopup(0);
      SwingUtilities.invokeLater(new Runnable() {
        public void run(){
          for(index=0;index<10; index++) {
            if(isCanceled || monitor.isCanceled()) break;
            monitor.setNote("preparing param extracting ....");
          }
        }
      });

      checkFileCount(root);
      monitor.setMaximum(count);

      //Main의 ParamLoader Thread가 일을 끝낼때 까지 기다린다
      while(!Main.isParamLoadEnd){
        try{ sleep(500);
             if(isCanceled || monitor.isCanceled())  {
               monitor.close();
               return;
             }
        }
        catch(InterruptedException e){
          monitor.close();
          return;
        }
      }

      browseSource(root, prefix);
      monitor.setProgress(count);
      monitor.close();

      count = 0;
      index = 0;

      if(!isCanceled)  IdePropertyManager.saveParamHash(Main.paramHash);

      inProcessing = false;
      //Garbage collecting...
      for(int i=0; i<2; i++) System.gc();
    }

    public boolean inProcessing(){
      return inProcessing;
    }

    private void checkFileCount(File root){
   
      if(root.isDirectory()){
        String [] dirs = root.list();
        for(int i=0; i<dirs.length; i++){
          File ele = new File(root, dirs[i]);

          if(ele.isDirectory()) checkFileCount(ele);
          else if(dirs[i].endsWith(".java")) ++count;
        }
      }
      else{
        try{
          ZipFile zip = new ZipFile(root);
          for(Enumeration e=zip.entries(); e.hasMoreElements(); ) {
             ZipEntry ze = (ZipEntry)e.nextElement();
             if(ze.getName().toLowerCase().trim().endsWith("java")) ++count;
          }
        }catch(IOException e){}
      }
    }

    private void browseSource(File root, String prefix){

      if(root.isDirectory()){
        String [] dirs = root.list();
        for(int i=0; i<dirs.length; i++){
          if(monitor.isCanceled() || isCanceled)  break;

          File ele = new File(root, dirs[i]);

          if(ele.isDirectory()) browseSource(ele, prefix + dirs[i]+".");
          else if(dirs[i].toLowerCase().endsWith(".java")){

            final String msg = dirs[i];
            SwingUtilities.invokeLater(new Runnable() {
              public void run(){
                monitor.setProgress( index++ );
                monitor.setNote("processing : "+ msg);
              }
            });

            try{
              FileInputStream fis = new FileInputStream(ele);
              ParamExtractor.setData(prefix+dirs[i].substring(0, dirs[i].toLowerCase().indexOf(".java")), fis, Main.paramHash);
            }catch(IOException e){}
          }
          else continue;
        }
      }
      else{
        try{
          ZipFile zip = new ZipFile(root);
          int i=0;
          for(Enumeration e=zip.entries(); e.hasMoreElements(); ){
            ZipEntry entry = (ZipEntry)e.nextElement();
            if(!entry.getName().toLowerCase().trim().endsWith(".java")) continue;

            final String msg = entry.getName().substring(entry.getName().indexOf("/")+1);
            SwingUtilities.invokeLater(new Runnable() {
              public void run(){
		            monitor.setProgress( index++ );
		            monitor.setNote("processing : "+ msg);
              }
            });

            InputStream in = zip.getInputStream(entry);
            if(entry.getName().indexOf(".") != -1)
              ParamExtractor.setData(entry.getName().substring(entry.getName().indexOf("/")+1,entry.getName().lastIndexOf(".")), in, Main.paramHash);
            else
              ParamExtractor.setData(entry.getName().substring(entry.getName().indexOf(",")+1), in, Main.paramHash);
          }
        }catch(IOException e){}
      }
    }
  }

  private void addJdkInfo(){

    Vector classPath = new Vector();
    StringTokenizer st = new StringTokenizer(classPathTf.getText(), "," , false);
    if(st.countTokens() == 0)
      classPath.addElement(classPathTf.getText());

    else
      while(st.hasMoreTokens()) classPath.addElement(st.nextToken());

    if(this.currentEditingJdkInfo == null)
    {
      JdkInfo jdkInfo = null;
      if(currJdkVersion != null)
        jdkInfo = new JdkInfo(javaTf.getText(), classPath, sourcePathTf.getText(),
                              docPathTf.getText(), currJdkVersion);
      else
        jdkInfo = new JdkInfo(javaTf.getText(), classPath, sourcePathTf.getText(),
                              docPathTf.getText(), javaTf.getText());

      if(!jdkListModel.exist(jdkInfo)) jdkListModel.addElement(jdkInfo);
    }

    else
    {
      currentEditingJdkInfo.setJavaEXEPath(javaTf.getText());
      currentEditingJdkInfo.setClassPath(classPath);
      currentEditingJdkInfo.setSourcePath(sourcePathTf.getText());
      currentEditingJdkInfo.setDocPath(docPathTf.getText());

      //Runtime.exec가 실패했는지를 체크한다
      if(currJdkVersion != null) currentEditingJdkInfo.setVersion(currJdkVersion);
      else currentEditingJdkInfo.setVersion(javaTf.getText());

      currJdkVersion = null;
      jdkListModel.fireUpdate(currentEditingJdkInfo, jdkList.getSelectedIndex());
      currentEditingJdkInfo = null;
    }

    currJdkVersion = null;
    setEnableFlag(false);
    setEmptyFieldEnabled();
  }

  public void okBtnSelected(){

    if(javaTf.isEnabled()) doneBtnSelected();
    else
    {
      if(!jdkList.isSelectionEmpty())
      this.selectedJdkInfo = (JdkInfo)jdkListModel.getElementAt(jdkList.getSelectedIndex());

      this.isOK = true;
      dispose();
    }
  }

  public Vector getRemovedJdks(){
    return this.removedJdks;
  }

  /** jdklist selection event handler */
  class ListSelectionHandler implements ListSelectionListener{

    public void valueChanged(ListSelectionEvent e){
      JdkInfo selectedJdk = (JdkInfo)jdkListModel.getElementAt(jdkList.getSelectedIndex());
      setEnableFlag(false);

      javaTf.setText(selectedJdk.getJavaEXEPath());
      classPathTf.setText(selectedJdk.getClassPathString());
      sourcePathTf.setText(selectedJdk.getSourcePath());
      docPathTf.setText(selectedJdk.getDocPath());
    }
  }

  private void setEnableFlag(boolean flag){
    javaTf.setEnabled(flag);
    classPathTf.setEnabled(flag);
    sourcePathTf.setEnabled(flag);
    docPathTf.setEnabled(flag);

    javaBtn.setEnabled(flag);
    classPathBtn.setEnabled(flag);
    sourcePathBtn.setEnabled(flag);
    docPathBtn.setEnabled(flag);

    //javaLbl.setEnabled(flag);
    //classPathLbl.setEnabled(flag);
    //sourcePathLbl.setEnabled(flag);
    //docPathLbl.setEnabled(flag);
  }

  private void setEmptyFieldEnabled(){
    javaTf.setText("");
    classPathTf.setText("");
    sourcePathTf.setText("");
    docPathTf.setText("");
  }

  /** action event handler */
  class ActionHandler implements ActionListener{

    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("JAVA.EXE")) javaBtnSelected();
      else if(cmd.equals("CLASSPATH")) classPathBtnSelected();
      else if(cmd.equals("SOURCE")) sourceBtnSelected();
      else if(cmd.equals("DOC")) docBtnSelected();
      else if(cmd.equals("ADD"))
      {
        setEnableFlag(true);
        setEmptyFieldEnabled();
        javaTf.requestFocus();
      }

      else if(cmd.equals("EDIT"))
      {
        if(!jdkListSelectModel.isSelectionEmpty())
        {
          setEnableFlag(true);
          javaTf.requestFocus();
          editBtnSelected();
        }
      }

      else if(cmd.equals("OK")) okBtnSelected();
      else if(cmd.equals("REMOVE")){
        if(!jdkListSelectModel.isSelectionEmpty()) removeBtnSelected();
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }
    }
  }
}

