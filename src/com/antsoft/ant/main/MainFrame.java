/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/main/MainFrame.java,v 1.70 1999/08/31 16:15:41 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.70 $
 * Author:       Baek, Jin-woo
 *               Kwon, Young Mo
 * SubAuthor:    Kim, Sung-hoon
 */
package com.antsoft.ant.main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.*;

// By Kahn
import java.net.URL;
//import javax.help.*;

import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.designer.codeeditor.EditorStatusBar;
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.designer.classdesigner.ClassDesigner;
import com.antsoft.ant.designer.codeeditor.TextViewPanel;
import com.antsoft.ant.util.*;
import com.antsoft.ant.property.defaultproperty.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.tools.htmlgenerator.*;
import com.antsoft.ant.util.theme.*;
import com.antsoft.ant.tools.print.*;
import com.antsoft.ant.wizard.*;
import com.antsoft.ant.wizard.customwizard.*;
import com.antsoft.ant.runner.*;
import com.antsoft.ant.util.LineCountDialog;
import com.antsoft.ant.property.*;
import com.antsoft.ant.tools.javadoc.*;
import com.antsoft.ant.chat.client.*;
import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.plaf.AntLookAndFeel;
import com.antsoft.ant.util.plaf.AntButtonBorder;
import com.antsoft.ant.designer.codeeditor.JavaEditorKit;
import com.antsoft.ant.designer.codeeditor.HtmlEditorKit;
import com.antsoft.ant.designer.codeeditor.NormalEditorKit;

/**
 *  class MainFrame
 *  AntIDE 메인 화면 출력을 담당한다.
 *
 *  @author Jinwoo Baek
 *  @author Kwon, Young Mo
 *  @author kim sang kyun
 */
public class MainFrame extends JFrame {
  java.applet.Applet applet;
  // Default Locale은 English이다.
  private ResourceBundle res = ResourceBundle.getBundle("com.antsoft.ant.main.MainFrameRes_en");

  // By Kahn                                           
  //public static HelpSet helpSet = null;
  //public static HelpBroker helpBroker;
  public static JFrame mainFrame;
//  public static JLabel m_MainStatusBar = new JLabel();
  public static EditorStatusBar m_Status = new EditorStatusBar();

  private JMenuBar m_MainMenuBar;
  private JToolBar m_MainToolBar;
  private JDesktopPane desktop;

  // Top level Menus
  private FileMenu file;
  private EditMenu edit;
  private SearchMenu search;
  private ViewMenu view;
  private BuildMenu build;
  private RunMenu run;
  private OptionMenu option;
  private LanguageMenu language;
  private ToolsMenu tools;
  private ServletMenu servlet;
  private HelpMenu help;

  // Current ui
  public String currentUI = "Metal";

  // ---------------- Internal data ----------------------------------------
  static JTabbedPane mainPane = new JTabbedPane();
//  OutputDialog output = new OutputDialog(this, "Output", false);
  private OutputFrame output;
  private ChatClientFrame chatFrame = new ChatClientFrame();
  boolean once = true;
  public static DefaultComboBoxModel findModel = new DefaultComboBoxModel();
  public static DefaultComboBoxModel replaceModel = new DefaultComboBoxModel();

  //lila
  public static CustomWizard customWizard;

  private String currentSourcePath = null;
  private ProjectExplorer currentPE = null;
  static ProjectExplorer filesPE;
	private Project latestProject = null;

  private ClassDesigner classDesigner;
  static CodeContext codeContext;

  private JavaEditorKit javaKit;
  private HtmlEditorKit htmlKit;
  private NormalEditorKit normalKit;

  /**
   *  Constructor
   */
  public MainFrame(CodeContext codeContext, SplashScreen splash) {
    this.codeContext = codeContext;
    this.mainFrame = this;

    //window event를 다룬다 ( 건드리지 마 )
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);

    //main frame menu에서 action(copy, cut, paste)를 사용해야 하므로 javaeditorkit을 미리 만들어 놓는다
    javaKit = new JavaEditorKit();
    htmlKit = new HtmlEditorKit();
    normalKit = new NormalEditorKit();
    
    buildMainFrame();

    output = new OutputFrame("Run", this);

    //Files PE Create
    Project files_proj = new Project();
    files_proj.updateClassLoader();
    filesPE = new ProjectExplorer(files_proj, this);
    currentPE = filesPE;

		ProjectManager.setCurrentProject(currentPE.getProject());
    /* codecontext에게 default project의 참조를 넘긴다 */
    codeContext.setProjectExplorer(filesPE);
    codeContext.addSourceBrowserEventListener(filesPE);
		codeContext.setEditFunctionEventListener(filesPE);

    //Lastest Project open
    if(Main.property.getLatestProjects().size() > 0 ){
      File recentProjFile = new File((String)Main.property.getLatestProjects().lastElement());
      if (recentProjFile.exists()) {
        currentSourcePath = recentProjFile.getParent() + File.separator;

        splash.showStatus("Opening the last project : " + recentProjFile.getAbsolutePath());
        Project recentPrj = openprj(recentProjFile.getParent()+ File.separator, recentProjFile.getName(), false);
      }
    }

    splash = null;

    //connect to AntLive (chat server)
    //chatFrame.connectToServer();
    customWizard = new CustomWizard(this);
  }

  public JavaEditorKit getJavaKit(){
    return javaKit;
  }

  public HtmlEditorKit getHtmlKit(){
    return htmlKit;
  }

  public NormalEditorKit getNormalKit(){
    return normalKit;
  }

  //window event를 다룬다 ( 건드리지 마 )
  protected void processWindowEvent(WindowEvent evt) {
  	if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
    	fileExit();
    }
  }

  /**
   * Build Main Frame
   */
  protected void buildMainFrame() {
    //Construct the frame
    //Theme 설정
    AntTheme antTheme = new AntTheme();
    AntLookAndFeel.setCurrentTheme(antTheme);

    try {
			setLookAndFeel();
    } catch (Exception ex) {
      System.out.println("Failed loading Metal");
      System.out.println(ex);
    }

    // Set Main Frame's icon
    setIconImage(ImageList.frameIcon.getImage());

    setSize(800, 600);

    // Set Main Frame's Title
    setTitle("Ant 1.0");
//    addWindowListener( new WindowHandler() );

    buildMenus();
    buildContents();           
    buildToolBar();
    buildStatusBar();

    UIManager.addPropertyChangeListener(new UISwitchListener((JComponent)getRootPane()));
  }

  /**
   * Ant의 Look and Feel을 설정하는 method
   */
	protected void setLookAndFeel() throws Exception {
    //AntLookAndFeel로 세팅
  	UIManager.setLookAndFeel("com.antsoft.ant.util.plaf.AntLookAndFeel");

    //각 UI Component들의 property를 조정한다
    UIManager.put("ScrollBar.width", new Integer(19));
    UIManager.put("ScrollBar.foreground", ColorList.scrollBarForegroundColor);
    UIManager.put("ScrollBar.background", ColorList.scrollBarBackgroundColor);
    UIManager.put("ScrollBar.thumb", ColorList.scrollBarThumbColor);
    UIManager.put("ScrollBar.thumbDarkShadow", ColorList.scrollBarThumbDarkShadowColor);
    UIManager.put("ScrollBar.thumbLightShadow", ColorList.scrollBarThumbLightShadowColor);
    UIManager.put("ScrollBar.thumbHighlight", Color.white);
    UIManager.put("ScrollBar.track", new Color(230, 230, 230));

	  UIManager.put("MenuBar.font", FontList.MenuBarFont);
    UIManager.put("MenuBar.selectionForeground", ColorList.menuBarSelectionForegroundColor);
    UIManager.put("MenuBar.selectionBackground", ColorList.menuBarSelectionBackgroundColor);

    UIManager.put("Menu.selectionForeground", ColorList.menuSelectionForegroundColor);
    UIManager.put("Menu.selectionBackground", ColorList.menuSelectionBackgroundColor);
    UIManager.put("Menu.font", FontList.MenuFont);

    UIManager.put("MenuItem.font", FontList.MenuItemFont);
    UIManager.put("MenuItem.selectionForeground", ColorList.menuItemSelectionForegroundColor );
    UIManager.put("MenuItem.selectionBackground", ColorList.menuItemSelectionBackgroundColor );
    UIManager.put("MenuItem.acceleratorFont", FontList.shotKeyFont);

    UIManager.put("CheckBoxMenuItem.font", FontList.MenuItemFont);
    UIManager.put("CheckBoxMenuItem.selectionForeground", ColorList.menuItemSelectionForegroundColor );
    UIManager.put("CheckBoxMenuItem.selectionBackground", ColorList.menuItemSelectionBackgroundColor );

    UIManager.put("Tree.selectionForeground", ColorList.treeSelectionForegroundColor );
    UIManager.put("Tree.selectionBackground", ColorList.treeSelectionBackgroundColor );

    UIManager.put("Table.selectionForeground", ColorList.listSelectionForegroundColor );
    UIManager.put("Table.selectionBackground", ColorList.listSelectionBackgroundColor );

    UIManager.put("List.selectionForeground", ColorList.listSelectionForegroundColor);
    UIManager.put("List.selectionBackground", ColorList.listSelectionBackgroundColor);

    UIManager.put("ComboBox.selectionForeground", ColorList.comboBoxSelectionForegroundColor);
    UIManager.put("ComboBox.selectionBackground", ColorList.comboBoxSelectionBackgroundColor);
    UIManager.put("ComboBox.background", Color.white);

    UIManager.put("ToolTip.background", ColorList.tooltipBackgroundColor);
    UIManager.put("ToolTip.foreground", Color.black);

    UIManager.put("EditorPane.selectionForeground", Color.white);
    UIManager.put("EditorPane.selectionBackground", ColorList.darkBlue);

//      UIManager.put("Button.border", new AntButtonBorder());


    UIManager.put("PopupMenu.font", FontList.MenuFont);
    UIManager.put("TabbedPane.tabShadow", Color.lightGray);
    UIManager.put("Label.foreground", ColorList.labelForegroundColor);

    UIManager.put("ProgressBar.selectionForeground", Color.white);
    UIManager.put("ProgressBar.selectionBackground", Color.black);      
	}
	
  protected void buildMenus() {
    m_MainMenuBar = new JMenuBar();
    m_MainMenuBar.setOpaque( true );

    file = new FileMenu();
    edit = new EditMenu();
    search = new SearchMenu();
    view = new ViewMenu();
    build = new BuildMenu();
    run = new RunMenu();
    option = new OptionMenu();
    tools = new ToolsMenu();
    help = new HelpMenu();

    m_MainMenuBar.add(file);
    m_MainMenuBar.add(edit);
    m_MainMenuBar.add(search);
    m_MainMenuBar.add(view);
    m_MainMenuBar.add(build);
    m_MainMenuBar.add(run);
    m_MainMenuBar.add(option);
    m_MainMenuBar.add(tools);
    m_MainMenuBar.add(help);

    setJMenuBar( m_MainMenuBar );
    validate();
  }

  protected JMenu buildOptionLnFMenu() {
    // Look and Feel is coded by Kwon, Young Mo
    JMenu menuLookAndFeel;
    // L&F radio buttons
    JRadioButtonMenuItem macMenuItem;
    JRadioButtonMenuItem motifMenuItem;
    JRadioButtonMenuItem metalMenuItem;
    JRadioButtonMenuItem windowsMenuItem;

    // LookAndFeel class names
    final String macClassName =
          "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    final String metalClassName =
          "javax.swing.plaf.metal.MetalLookAndFeel";
    final String motifClassName =
          "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    final String windowsClassName =
          "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    // install look and feel menu
  	// Options Menu

  	menuLookAndFeel = new JMenu("Look and Feel");
    menuLookAndFeel.setMnemonic('p');
   	menuLookAndFeel.getAccessibleContext().setAccessibleDescription("Look and Feel options: select one of several different Look and Feels for the SwingSet application");

    // Look and Feel Radio control
  	ButtonGroup group = new ButtonGroup();
	  ToggleUIListener toggleUIListener = new ToggleUIListener();

    metalMenuItem = (JRadioButtonMenuItem) menuLookAndFeel.add(new JRadioButtonMenuItem("Java Look and Feel"));
	  metalMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("Metal"));
	  metalMenuItem.setSelected(true);
  	metalMenuItem.setEnabled(isAvailableLookAndFeel(metalClassName));
	  group.add(metalMenuItem);
  	metalMenuItem.addItemListener(toggleUIListener);
	  metalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    motifMenuItem = (JRadioButtonMenuItem) menuLookAndFeel.add(new JRadioButtonMenuItem("Motif Look and Feel"));
  	motifMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("CDE/Motif"));
	  motifMenuItem.setEnabled(isAvailableLookAndFeel(motifClassName));
  	group.add(motifMenuItem);
	  motifMenuItem.addItemListener(toggleUIListener);
  	motifMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));

    windowsMenuItem = (JRadioButtonMenuItem) menuLookAndFeel.add(new JRadioButtonMenuItem("Windows Style Look and Feel"));
  	windowsMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("Windows"));
  	windowsMenuItem.setEnabled(isAvailableLookAndFeel(windowsClassName));
	  group.add(windowsMenuItem);
  	windowsMenuItem.addItemListener(toggleUIListener);
	  windowsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));

  	macMenuItem = (JRadioButtonMenuItem) menuLookAndFeel.add(new JRadioButtonMenuItem("Macintosh Look and Feel"));
	  macMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("Macintosh"));
  	macMenuItem.setEnabled(isAvailableLookAndFeel(macClassName));
	  group.add(macMenuItem);
  	macMenuItem.addItemListener(toggleUIListener);
	  macMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));

    return menuLookAndFeel;
  }

  protected JMenu buildOptionMetalThemeMenu() {
    // menu "Themes"
    JMenu menuMetalTheme = new JMenu();
    //Theme 설정
	  // load a theme from a text file
  	MetalTheme myTheme = null;
	  try {
	    myTheme =  new PropertiesMetalTheme(new FileInputStream("MyTheme.theme"));
  	} catch (IOException e) {
      System.out.println("MyTheme.theme does not exist." );
      myTheme = new DefaultMetalTheme();
    }

   	// build an array of themes
	  MetalTheme[] themes = { new DefaultMetalTheme(),
        new AntTheme(),
		 		new GreenMetalTheme(),
				new AquaMetalTheme(),
				new KhakiMetalTheme(),
				new DemoMetalTheme(),
  			new ContrastMetalTheme(),
				new BigContrastMetalTheme(),
	                        myTheme };

	  // put the themes in a menu
	  //menuMetalTheme = new MetalThemeMenu("Metal Theme", themes);

    return menuMetalTheme;
  }

  protected void buildToolBar() {
    m_MainToolBar = new MainToolBar();
    desktop.add(m_MainToolBar, BorderLayout.NORTH);
  }

  protected void buildStatusBar() {
    //m_MainStatusBar.setBorder(BorderList.raisedBorder);
    //m_MainStatusBar.setPreferredSize(new Dimension(0,16));
    //m_MainStatusBar.setFont(new Font("Dialog", Font.BOLD, 10));
    //m_MainStatusBar.setText(" ");
    //m_MainStatusBar.setForeground(Color.white);
    //m_MainStatusBar.setVerticalAlignment(SwingConstants.CENTER);
    //desktop.add(m_MainStatusBar, BorderLayout.SOUTH);
    desktop.add(m_Status, BorderLayout.SOUTH);
    m_Status.setPreferredSize(new Dimension(m_MainToolBar.getWidth(),22));
    m_Status.setVisible(true);
  }

  protected void buildContents() {
    BorderLayout borderLayout = new BorderLayout();
    desktop = new JDesktopPane();
    desktop.setLayout(borderLayout);
    desktop.putClientProperty("JDesktopPane.dragMode", "faster");
//    desktop.setBackground(new Color(220, 220, 220));

    desktop.setBackground(ColorList.metalColor);

    /*
    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);
    private final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);
    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);

    private final ColorUIResource secondary1 = new ColorUIResource(102, 102, 102);
    private final ColorUIResource secondary2 = new ColorUIResource(153, 153, 153);
    */

    getContentPane().add(desktop);
    desktop.add(mainPane, BorderLayout.CENTER);

    // ----------------  etc Event Handler -----------------------------
    mainPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        currentPE = (ProjectExplorer)mainPane.getSelectedComponent();
				if (currentPE != null) {
					ProjectManager.setCurrentProject(currentPE.getProject());
    			codeContext.setProjectExplorer(currentPE);
          codeContext.addSourceBrowserEventListener(currentPE);
          codeContext.setEditFunctionEventListener(currentPE);
          codeContext.movePrj(currentPE.getProject().getProjectName(), currentPE.getProject().getPath());

          view.setMessageSelection(currentPE.isShowingMessageBoard());
					currentPE.fileSelected(currentPE.getLastOpenedSource());
          if (currentPE.getLastOpenedSource() != null) {
	          setCaptionText(currentPE.getProject().getProjectName() + " - " +
          							currentPE.getLastOpenedSource().getFile());
          }
          else setCaptionText("");
				}

        if(currentPE != null && !currentPE.isFilesTab()) filesPE.clearLibraryFiles();
      }
    });

    // Open project when double click the main pane
    mainPane.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        if (evt.getModifiers() == InputEvent.BUTTON1_MASK) {
          if (evt.getClickCount() == 2) {
            openProject();
          }
        }
        else if (evt.getModifiers() == InputEvent.BUTTON3_MASK) {
          fileOpen();
        }
      }
    });
  }

  /**
   * 새로운 파일 생성을 처리하는 method
   * 현재는 위저드를 연다.
   */
  void fileNew() {
  	if ((currentPE == null) || (currentPE == filesPE)) {
			newProject();
    }
  	if ((currentPE != null) && (currentPE != filesPE)) {
	   	Wizard wiz = new Wizard(MainFrame.this, "New...", true, customWizard);
      if (currentPE.getProject().getPathModel().getSourceRoot() != null) {
	      wiz.setPath(currentPE.getProject().getPathModel().getSourceRoot());
        customWizard.setPath(currentPE.getProject().getPathModel().getSourceRoot());
      } else {
      	File f = new File(currentPE.getProject().getPath());
      	wiz.setPath(f.getParent());
        customWizard.setPath(f.getParent());
      }
 		  Point p = mainPane.getAccessibleContext().getAccessibleComponent().getLocationOnScreen();
   		wiz.setBounds(p.x, p.y, 430, 380);
		  wiz.setVisible(true);                                                       

      File[] files = wiz.getFiles();
      if(files == null) return;

      for (int i = 0; i < files.length; i++) {
      	if (files[i] != null){
        	ProjectFileEntry pfe = currentPE.addFileToProject(files[i].getParent(), files[i].getName(), true);
          if(pfe != null) pfe.syncLastModifiedTimeWithRealFile();
        }
      }
    }
  }

	/**
	 *  파일을 새로 생성할 때
	 */
  void fileNewFile() {
     if(mainPane.getTabCount() == 0){
       ClassDesigner dlg = new ClassDesigner(this, "ClassDesigner", true);
       dlg.setVisible(true);

       if(dlg.isOK()){
         Project default_proj = new Project("Default", IdePropertyManager.DEFAULT_PROJECT_ROOT_DIR);
         default_proj.setPathModel(Main.property.getPathModel());
         ProjectManager.setCurrentProject(default_proj);
         ProjectExplorer defaultPE = new ProjectExplorer(default_proj, this);

         mainPane.addTab(default_proj.getProjectName(), ImageList.tabIcon, defaultPE);
         mainPane.addTab(filesPE.getProject().getProjectName(), ImageList.defaulttabIcon, filesPE);
         mainPane.setSelectedComponent(defaultPE);

         defaultPE.newCodeGenerated(dlg.getGeneratedSource(), dlg.getPackageName(), dlg.getClassName());
       }
     }
     else if(mainPane.getTabCount() >= 2){
       if(currentPE != null && currentPE != filesPE) {
         String selPackage = currentPE.getCurrentSelectedPackage();
         ClassDesigner dlg = new ClassDesigner(this, "ClassDesigner", true);
         if(selPackage != null) dlg.setPackageName(selPackage);
         dlg.setVisible(true);

         if(dlg.isOK()){
           currentPE.newCodeGenerated(dlg.getGeneratedSource(), dlg.getPackageName(), dlg.getClassName());
         }
       }
     }
  }

	/**
	 *  파일을 열 때
	 */
  void fileOpen() {
    File lastOpen = Main.property.getLastOpenedDir();

    FileDialog filedlg = new FileDialog(this, "Open");
    if (currentSourcePath != null) filedlg.setDirectory(currentSourcePath);
    else if(lastOpen != null && lastOpen.exists()) filedlg.setDirectory(lastOpen.getAbsolutePath());

		filedlg.setFile("*.java");
    filedlg.show();
    String filename = filedlg.getFile();
    if ((filename != null) &&
    	(filename.toLowerCase().endsWith(".java") || filename.toLowerCase().endsWith(".html")
      || filename.toLowerCase().endsWith(".htm") || filename.toLowerCase().endsWith(".txt"))) {
  	  currentSourcePath = filedlg.getDirectory();

      open(filedlg.getDirectory(), filedlg.getFile());
    }
  }

  void open(String path, String filename) {
  	if ((filename != null) && (path != null) &&
    		(filename.toLowerCase().endsWith(".java") || filename.toLowerCase().endsWith(".html")
        || filename.toLowerCase().endsWith(".htm") || filename.toLowerCase().endsWith(".txt"))) {

			if (mainPane.indexOfComponent(filesPE) == -1) {
				mainPane.insertTab(filesPE.getProject().getProjectName(),
														null, filesPE, null, 0);
			}
      ProjectFileEntry pfe = filesPE.addFileToProject(path, filename, true);
      if(pfe == null) return;

      mainPane.setSelectedComponent(filesPE);
			ProjectManager.setCurrentProject(filesPE.getProject());
 	  	codeContext.setProjectExplorer(filesPE);
      codeContext.movePrj(currentPE.getProject().getProjectName(), currentPE.getProject().getPath());

      Main.property.setLastOpenDir(new File(path));
      Main.property.setLatestOpenedFile(pfe.getFullPathName());
      IdePropertyManager.saveProperty(Main.property);
      file.recentUpdated();
    }
  }

	/**
	 *  프로젝트를 새로 만들 때
	 */
	void newProject() {
		ProjectDialog dlg = new ProjectDialog(this, "New Project...", true);
		//dlg.setBounds(200, 200, 300, 300);
		dlg.setVisible(true);
		if (dlg.isOK()) {
			Project np = ProjectManager.newProject(dlg.getProjectFilePath(), dlg.getProjectName(), dlg.getComment());
      if (np != null) {
				latestProject = np;
        ProjectManager.saveProject(np);
				ProjectExplorer pe = new ProjectExplorer(np, this);

        int defaultIndex = getDefaultPEIndex();
        if(defaultIndex != -1)  mainPane.removeTabAt(defaultIndex);

        mainPane.addTab(pe.getProject().getProjectName(), ImageList.tabIcon, pe);
        mainPane.addTab(filesPE.getProject().getProjectName(), ImageList.defaulttabIcon, filesPE);
     	 	mainPane.setSelectedIndex(mainPane.getTabCount()-2);

	      codeContext.openPrj(currentPE.getProject().getProjectName(), currentPE.getProject().getPath());

  	    setCaptionText("");
     	  Main.property.setLatestOpenedProject(np.getPath());
       	file.recentUpdated();

  			Main.property.setLastProjectOpenDir(new File(np.getPath()));
 	  		IdePropertyManager.saveProperty(Main.property);
      }
		}
	}

	/**
	 *  프로젝트를 열 때
	 */
  void openProject() {
    File lastOpenDir = Main.property.getLastProjectOpenDir();

    FileDialog filedlg = new FileDialog(this, "Open Project");
    if (currentSourcePath != null) filedlg.setDirectory(currentSourcePath);
    else if(lastOpenDir != null && lastOpenDir.exists()) filedlg.setDirectory(lastOpenDir.getAbsolutePath());

		filedlg.setFile("*.apr");
    filedlg.setVisible(true);
    if (filedlg.getFile() != null) {
      // To Do : 프로젝트 파일인지 확인하고 읽어서
      //         프로젝트 패널에 적당히 나타내준다.
      if (filedlg.getFile().toLowerCase().endsWith(".apr")) {
        currentSourcePath = filedlg.getDirectory();
        openprj(filedlg.getDirectory(), filedlg.getFile(), false);
      }
    }
  }

  /**
   * filesPE의 index반환
   */
  private int getDefaultPEIndex(){
    return mainPane.indexOfComponent(filesPE);
  }

	/**
	 * Project를 여는 method
	 */	 
  private Project openprj(String path, String name, boolean isStartup) {
  	if ((name != null) && (path != null) && (name.toLowerCase().endsWith(".apr"))) {
      Project prj = ProjectManager.openProject(path, name);

			if (prj != null) {
				latestProject = prj;
       	int tabIndex = mainPane.getTabCount();
        int idx = -1;
        ProjectExplorer ex = null;
        for (int i = 0; i < tabIndex; i++) {
        	ex = (ProjectExplorer)mainPane.getComponentAt(i);
        	Project pr = ex.getProject();
          if (pr.equals(prj)) {
          	idx = i; break;
          }
        }
        if (idx == -1) {
	       	ProjectExplorer pe = new ProjectExplorer(prj, this);

          int defaultIndex = getDefaultPEIndex();
          if(defaultIndex != -1){
            mainPane.removeTabAt(defaultIndex);
          }

          mainPane.addTab(pe.getProject().getProjectName(), ImageList.tabIcon, pe);
          mainPane.addTab(filesPE.getProject().getProjectName(),ImageList.defaulttabIcon, filesPE);
          mainPane.setSelectedIndex(mainPane.getTabCount()-2);

          codeContext.openPrj(pe.getProject().getProjectName(),pe.getProject().getPath());
  	     	mainPane.repaint();
    	    setCaptionText("");
      	  Main.property.setLatestOpenedProject(prj.getPath());
        	file.recentUpdated();
				}
        else {
        	mainPane.setSelectedIndex(idx);
  	     	mainPane.repaint();
    	    setCaptionText("");
      	  Main.property.setLatestOpenedProject(prj.getPath());
        	file.recentUpdated();
        }

  			Main.property.setLastProjectOpenDir(new File(path));
	  		IdePropertyManager.saveProperty(Main.property);
        return prj;
			}
			else{
        if(!isStartup){
         	JOptionPane.showMessageDialog(this, "This is not valid Project file.",
           		"Open Project Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
      }
    }
    return null;
  }

	/**
	 *  프로젝트를 닫는다.
	 */
  public boolean closeProject() {
    if(mainPane.getSelectedComponent() == filesPE) return true;

    int index = mainPane.getSelectedIndex();
    if (index != -1) {
			ProjectExplorer pe = (ProjectExplorer)mainPane.getSelectedComponent();
			if (pe.canClose(this)) {
        pe.clearSourcePool();
      	ProjectManager.removeProject(pe.getProject().getProjectName());
  			mainPane.removeTabAt(index);
        setCaptionText("");
        codeContext.closePrj(pe.getProject().getProjectName(), pe.getProject().getPath());
        pe = null;

        if(mainPane.getTabCount() == 1){
          mainPane.remove(filesPE);
          currentPE = null;
        }
        else{
          for(int i=0; i<mainPane.getTabCount(); i++){
            String title = mainPane.getTitleAt(i);
            if(!title.equals("Files")){
              currentPE = (ProjectExplorer)mainPane.getComponentAt(i);
              mainPane.setSelectedIndex(i);
              ProjectManager.setCurrentProject(currentPE.getProject());
              codeContext.setProjectExplorer(currentPE);
		          codeContext.movePrj(currentPE.getProject().getProjectName(), currentPE.getProject().getPath());
              currentPE.fileSelected(currentPE.getLastOpenedSource());
              break;
            }
          }
        }
        //Garbage collecting...
        System.gc();
        return true;
	  	}
    }
    return false;
  }

	/**
	 *  프로젝트를 저장한다.
	 */
	public void saveProject() {
		if (currentPE != null) {
			Project prj = currentPE.getProject();
			if ((prj != null) && !currentPE.isFilesTab()) {
				ProjectManager.saveProject(prj);
        setTextToStatusBar(" Saved Project [ "+prj.getPath()+" ]");
			}
		}
	}

	/**
	 *  연 파일을 닫는다.
	 */
  public void fileClose() {
    if (currentPE != null) currentPE.closeFile();
  }

  public void closeOpenFile() {
    if (currentPE != null) currentPE.closeOpenFile();
  }

  public void closeAllOpenFile() {
    if (currentPE != null) currentPE.closeAllOpenFile();
  }

	/**
	 *  현 파일을 저장한다.
	 */
  public void saveCurrent() {
    if (currentPE != null) {
      String saved = currentPE.save();
      if(saved!=null) setTextToStatusBar(" Saved File [ " + saved + " ]");
      else setTextToStatusBar(" Cann't Save File ");
    }
  }

	/**
	 *  현 파일을 새로 저장한다.
	 */
  public void saveAsCurrent() {
    if (currentPE != null) {
      String saved = currentPE.saveAs();
      if(saved!=null) setTextToStatusBar(" Saved File [ " + saved + " ]");
    }
  }

	/**
	 *  모든 파일을 저장한다.
	 */
  public void saveAllCurrent() {
    if (currentPE != null) {
      int savedCount= currentPE.saveAll();

      //모두 save되었으므로 reset한다
      currentPE.resetOpenedProjectFileEntryListForSaveAll();
      setTextToStatusBar(" SaveAll : Opened ["+ currentPE.getTotalOpenedList().size()+"] Modified ["+savedCount+"] Saved ["+savedCount+"]");
		}
  }

  /**
   * File|Exit
   */
  protected void fileExit() {

    Main.property.setLastFrameBounds(this.getBounds());
    IdePropertyManager.saveProperty(Main.property);

    for (int i = 0; i < mainPane.getTabCount(); i++) {
			ProjectExplorer pe = (ProjectExplorer)mainPane.getComponentAt(i);
      if(pe.isFilesTab()) continue;
      boolean projectExit = pe.canClose(this);
      if(!projectExit) return;
    }

    if ( output.isVisible() )
    	output.setVisible(false);
    
    if ( Main.isDebugMode() && Main.getAntOutputFrame().isVisible() )
	   	Main.getAntOutputFrame().setVisible(false);
	   	
    dispose();
    codeContext.exitProgram();
    System.exit(0);
  }

	/**
	 *  indent action
	 */
	void indentCurrent() {
		if ((currentPE != null) && (codeContext != null)){
			SourceEntry se = currentPE.getLastOpenedSource();
			if (se != null) {
				//codeContext.indentingDocument(se.getDocument());
//				codeContext.indentDocument();
			}
		}
	}

  void sourceLineCount(){
    if(mainPane.getTabCount() > 0) {
      LineCountDialog dlg = new LineCountDialog(currentPE.getProject().getFiles());
      dlg.setVisible(true);
    }
  }

  /**
   * CodeContext 의 참조를 넘긴다
   *
   * @return CodeContext
   */
  public static CodeContext getCodeContext(){
    return codeContext;
  }

	/**
	 *  Status Bar 에 메시지를 출력한다.
	 */
	public static void displayMessageAtStatusBar(String msg) {
		setTextToStatusBar(msg);
	}

	public OutputFrame showOutputDialog() {
  	if (!output.isVisible()) {
	  	Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
  		if (once) {
      	output.setBounds(dm.width / 4, dm.height / 4 * 3, dm.width / 2, dm.height / 5);
        once = false;
      }
			output.setVisible(true);
      view.setOutputSelection(true);
    }
    return output;
	}

  public void setCaptionText(String text) {
  	if ((text != null) && !text.equals("")){
    	this.setTitle("Ant 1.0: " + text);
    }
    else this.setTitle("Ant 1.0");
  }

  public void setMessageState(boolean b) {
    view.setMessageSelection(b);
  }

  public void setExecutionViewState(boolean b) {
    view.setOutputSelection(b);
  }

  /**
   * A utility function that layers on top of the LookAndFeel's
   * isSupportedLookAndFeel() method. Returns true if the LookAndFeel
   * is supported. Returns false if the LookAndFeel is not supported
   * and/or if there is any kind of error checking if the LookAndFeel
   * is supported.
   * The L&F menu will use this method to detemine whether the various
   * L&F options should be active or inactive.
   *
   */
  protected static boolean isAvailableLookAndFeel(String classname) {
    try { // Try to create a L&F given a String
	     Class lnfClass = Class.forName(classname);
	     LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
	     return newLAF.isSupportedLookAndFeel();
    } catch(Exception e) { // If ANYTHING weird happens, return false
	     return false;
  	}
  }

	/**
	 *  라이브러리의 소스를 읽기 전용으로 연다.
	 */
	static public void showLibSource(JavaDocument doc, String name) {
		if (mainPane.indexOfComponent(filesPE) == -1) {
			mainPane.insertTab(filesPE.getProject().getProjectName(),
													null, filesPE, null, 0);
		}
    mainPane.setSelectedComponent(filesPE);
		ProjectManager.setCurrentProject(filesPE.getProject());
    filesPE.addLibfileToProject(name+".java", doc);
	}

  public boolean isApplet() {
    return (applet != null);
  }

  public Container getRootComponent() {
    if(isApplet())
      return applet;
    else
      return mainFrame;
  }

  private static void setTextToStatusBar(String text){
    m_Status.setExplainText(text);
  }

  private void commentLines(){
    if(mainPane.getComponentCount() > 0) currentPE.commentLines();
  }

  private void unCommentLines(){
    if(mainPane.getComponentCount() > 0) currentPE.unCommentLines();
  }

  private void moveLines(boolean isForward){
    if(mainPane.getComponentCount() > 0) currentPE.moveLines(isForward);
  }

  /**
   * Switch the between the Windows, Motif, Mac, and the Java Look and Feel
   */
  class ToggleUIListener implements ItemListener{
  	public void itemStateChanged(ItemEvent e) {
	    Component root = getRootComponent();
	    root.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();

      try {
        if(rb.isSelected() && rb.getText().equals("Windows Style Look and Feel")) {
	        currentUI = "Windows";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     	    SwingUtilities.updateComponentTreeUI(getRootComponent());
	      } else if(rb.isSelected() && rb.getText().equals("Macintosh Look and Feel")) {
		      currentUI = "Macintosh";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
    	    SwingUtilities.updateComponentTreeUI(getRootComponent());
        } else if(rb.isSelected() && rb.getText().equals("Motif Look and Feel")) {
		      currentUI = "Motif";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
          SwingUtilities.updateComponentTreeUI(getRootComponent());
        } else if(rb.isSelected() && rb.getText().equals("Java Look and Feel")) {
		      currentUI = "Metal";
          // javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(
   		    //        new javax.swing.plaf.metal.DefaultMetalTheme());
	    	  UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
          //tabPlacement.setEnabled(true);
	    	  SwingUtilities.updateComponentTreeUI(getRootComponent());
        }
      } catch (UnsupportedLookAndFeelException exc) {
     		// Error - unsupported L&F
     		rb.setEnabled(false);
        System.err.println("Unsupported LookAndFeel: " + rb.getText());

		    // Set L&F to JLF
/*        try {
          currentUI = "Metal";
          metalMenuItem.setSelected(true);
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
   	 	    SwingUtilities.updateComponentTreeUI(getRootComponent());
     		} catch (Exception exc2) {
     		  exc2.printStackTrace();
   		    System.err.println("Could not load LookAndFeel: " + exc2);
   		    exc2.printStackTrace();
     		}
*/        
      } catch (Exception exc) {
        rb.setEnabled(false);
        exc.printStackTrace();
        System.err.println("Could not load LookAndFeel: " + rb.getText());
     		exc.printStackTrace();
      }

      root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
   	}
  }

  /**
   * Switch the between metal themes
   */
/*
  class ToggleMetalThemeListener implements ItemListener {
  	public void itemStateChanged(ItemEvent e) {
	    Component root = getRootComponent();
	    root.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();

      try {
        if(rb.isSelected() && rb.getText().equals("Windows Style Look and Feel")) {
	        currentUI = "Windows";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     	    SwingUtilities.updateComponentTreeUI(getRootComponent());
	      } else if(rb.isSelected() && rb.getText().equals("Macintosh Look and Feel")) {
		      currentUI = "Macintosh";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
    	    SwingUtilities.updateComponentTreeUI(getRootComponent());
        } else if(rb.isSelected() && rb.getText().equals("Motif Look and Feel")) {
		      currentUI = "Motif";
	    	  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
          SwingUtilities.updateComponentTreeUI(getRootComponent());
        } else if(rb.isSelected() && rb.getText().equals("Java Look and Feel")) {
		      currentUI = "Metal";
          // javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(
   		    //        new javax.swing.plaf.metal.DefaultMetalTheme());
	    	  UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
          //tabPlacement.setEnabled(true);
	    	  SwingUtilities.updateComponentTreeUI(getRootComponent());
        }
      } catch (UnsupportedLookAndFeelException exc) {
     		// Error - unsupported L&F
     		rb.setEnabled(false);
        System.err.println("Unsupported LookAndFeel: " + rb.getText());

		    // Set L&F to JLF
        try {
          currentUI = "Metal";
          metalMenuItem.setSelected(true);
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
   	 	    SwingUtilities.updateComponentTreeUI(getRootComponent());
     		} catch (Exception exc2) {
     		  exc2.printStackTrace();
   		    System.err.println("Could not load LookAndFeel: " + exc2);
   		    exc2.printStackTrace();
     		}
      } catch (Exception exc) {
        rb.setEnabled(false);
        exc.printStackTrace();
        System.err.println("Could not load LookAndFeel: " + rb.getText());
     		exc.printStackTrace();
      }

      root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
   	}
  }
*/
	/**
	 *  WindowEvent handler class
   */
  class WindowHandler extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      fileExit();
    }
  }

  /**
   *  FileMenu class
   */
  class FileMenu extends JMenu
                 implements ActionListener {
    // File Menu Items
    JMenuItem newWiz, newFile, open, newProject, openProject, closeProject, save, saveProject,
              saveAs, saveAll, close, projectProperty, print, printerSetup, exit;
  	JMenu recent;

    /**
     *  Build File Menu
     */
    public FileMenu() {
      setText( res.getString("File") );
      JMenu file = this;

      file.setMnemonic( 'F' );
      file.setActionCommand("hello");
      newWiz = new JMenuItem( res.getString("New"), 'N' );
      newWiz.setIcon(ImageList.newFile);
      newFile = new JMenuItem( res.getString("NewFile"), 'W' );

      open = new JMenuItem( res.getString("Open"), 'O' );
      open.setIcon(ImageList.open);

      close = new JMenuItem( res.getString("Close"), 'C' );
      newProject = new JMenuItem( res.getString("NewProject"), 'E' );
      openProject = new JMenuItem( res.getString("OpenProject"), 'J' );
      closeProject = new JMenuItem( res.getString("CloseProject"), 'L' );
      closeProject.setIcon(ImageList.closePrjBtnIcon);
      save = new JMenuItem( res.getString("Save"), 'S' );
      save.setIcon(ImageList.save);

      saveProject = new JMenuItem( res.getString("SaveProject"), 'V' );
      saveProject.setIcon(ImageList.savePrjBtnIcon);
      saveAs = new JMenuItem( res.getString("SaveAs"), 'A' );
      saveAll = new JMenuItem( res.getString("SaveAll") );
      projectProperty = new JMenuItem( res.getString("ProjectProperties"), 'T' );
      projectProperty.setIcon(ImageList.propertyBtnIcon);
      print = new JMenuItem( res.getString("Print"), 'P' );
      print.setIcon(ImageList.print);
      printerSetup = new JMenuItem( res.getString("PrinterSetup"), 'I' );
      recent = new RecentMenu();
      exit = new JMenuItem( res.getString("Exit"), 'X' );

      // set accelerator
      newWiz.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N, Event.CTRL_MASK ) );
      open.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, Event.CTRL_MASK ) );
      save.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, Event.CTRL_MASK ) );
      saveAll.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, Event.CTRL_MASK ) );

      newWiz.setHorizontalAlignment(AbstractButton.LEFT);
      newFile.setHorizontalAlignment(AbstractButton.LEFT);
      open.setHorizontalAlignment(AbstractButton.LEFT);
      save.setHorizontalAlignment(AbstractButton.LEFT);
      close.setHorizontalAlignment(AbstractButton.LEFT);

      newWiz.setHorizontalTextPosition(JButton.RIGHT);
      newFile.setHorizontalTextPosition(JButton.RIGHT);
      open.setHorizontalTextPosition(JButton.RIGHT);
      save.setHorizontalTextPosition(JButton.RIGHT);
      close.setHorizontalTextPosition(JButton.RIGHT);

      // menu "File" event handler
      newWiz.addActionListener( this );
      newFile.addActionListener( this );
      open.addActionListener( this );
      close.addActionListener( this );
      newProject.addActionListener( this );
      openProject.addActionListener( this );
      closeProject.addActionListener( this );
      save.addActionListener( this );
      saveProject.addActionListener( this );
      saveAs.addActionListener( this );
      saveAll.addActionListener( this );
      projectProperty.addActionListener( this );
      print.addActionListener( this );
      printerSetup.addActionListener( this );
      exit.addActionListener( this );

      // Adds file menu items to File Menu
      file.add(newWiz);
      file.add(newFile);
      file.add(open);
      file.add(close);
      file.addSeparator();
      file.add(newProject);
      file.add(openProject);
      file.add(closeProject);
      file.addSeparator();
      file.add(save);
      file.add(saveProject);
      file.add(saveAs);
      file.add(saveAll);
      file.addSeparator();
      file.add(projectProperty);
      file.addSeparator();
      file.add(print);
      file.add(printerSetup);
      file.addSeparator();
      file.add(recent);
      file.addSeparator();
      file.add(exit);
    }

    void recentUpdated() {
      this.remove(recent);
      recent = new RecentMenu();
      this.insert(recent, this.getItemCount() - 2);
    }

    public void actionPerformed( ActionEvent e ) {
      // menu "File" event handler
      if ( e.getSource() == newWiz ) {
      	fileNew();
      }
      else if ( e.getSource() == newFile ) {
        fileNewFile();
      }
      else if ( e.getSource() == open ) {
        fileOpen();
      }
      else if ( e.getSource() == newProject ) {
        newProject();
      }
      else if ( e.getSource() == openProject ) {
        openProject();
      }
      else if ( e.getSource() == closeProject ) {
        closeProject();
      }
      else if ( e.getSource() == save ) {
        saveCurrent();
      }
      else if ( e.getSource() == saveProject ) {
        saveProject();
      }
      else if ( e.getSource() == saveAs ) {
        saveAsCurrent();
      }
      else if ( e.getSource() == close ) {
        fileClose();
      }
      else if ( e.getSource() == saveAll ) {
        saveAllCurrent();
      }
      else if ( e.getSource() == projectProperty ) {
      	if (currentPE != null) currentPE.showProperty();
      }
      else if ( e.getSource() == printerSetup ) {
         PrintSetupDlg dlg = new PrintSetupDlg(MainFrame.this, "Print Setup", true);
        dlg.setVisible(true);
      }
      else if ( e.getSource() == print ) {
        if (currentPE != null) {
          SourceEntry se = currentPE.getLastOpenedSource();
          if (se != null) {
            /*
            JavaDocument doc = (JavaDocument)se.getDocument();
            Print printJob = new Print();
            printJob.doPrint(doc);
            */
            Print printJob = new Print();
            switch(se.getDocumentType()){
              case SourceEntry.JAVA :
                   printJob.doPrint(se, (ViewFactory)javaKit.getViewFactory());
                   break;

              case SourceEntry.HTML :
                   printJob.doPrint(se, (ViewFactory)htmlKit.getViewFactory());
                   break;

              case SourceEntry.TEXT :
                   printJob.doPrint(se, (ViewFactory)normalKit.getViewFactory());
                   break;

             }
          }
        }
      }
      else if ( e.getSource() == exit ) {
        fileExit( );
      }
    }

    public boolean isTopLevelMenu() {
      return true;
    }  
  }

  class RecentMenu extends JMenu implements ActionListener {
    public RecentMenu() {
    	// 최근 프로젝트
      setText(res.getString("Recent"));
    	Vector prjs = Main.property.getLatestProjects();
      if (prjs.size() <= 0) {
      	JMenuItem item = new JMenuItem("No Recent Project");
        item.setEnabled(false);
        this.add(item);
      } else {
	      for (int i = 0; i < prjs.size(); i++) {
        	String cmd = (String)prjs.elementAt(i);
        	JMenuItem item = new JMenuItem(cmd);
          item.setActionCommand(cmd);
          item.addActionListener(this);
          this.add(item);
  	    }
      }
      this.addSeparator();
      Vector files = Main.property.getLatestFiles();
      if (files.size() <= 0) {
      	JMenuItem item = new JMenuItem("No Recent File");
        item.setEnabled(false);
        this.add(item);
      } else {
	      for (int i = 0; i < files.size(); i++) {
        	String cmd = (String)files.elementAt(i);
        	JMenuItem item = new JMenuItem(cmd);
          item.setActionCommand(cmd);
          item.addActionListener(this);
          this.add(item);
  	    }
      }
    }

    public void actionPerformed(ActionEvent evt) {
    	String filename = evt.getActionCommand();
      if ((filename != null) && !filename.trim().equals("")) {
      	if (filename.endsWith(".apr")) {
        	File f = new File(filename);
          if (f.exists()) openprj(f.getParent(), f.getName(), false);
        }
        else {
        	File f = new File(filename);
          if (f.exists()) open(f.getParent(), f.getName());
        }
      }
    }
  }

  class EditMenu extends JMenu
                 implements ActionListener {
    // Edit Menu Items
    JMenuItem cut, copy, paste, undo, redo, comment, uncomment, forwardLines, backwardLines;

    // menu "Edit"
    public EditMenu() {
      setText( res.getString("Edit") );
      JMenu edit = this;
      edit.setMnemonic( 'E' );

      cut = edit.add(JavaEditorKit.getActionForKeymap(DefaultEditorKit.cutAction));
      cut.setMnemonic('T');

      copy = edit.add(JavaEditorKit.getActionForKeymap(DefaultEditorKit.copyAction));
      copy.setMnemonic('C');

      paste = edit.add(JavaEditorKit.getActionForKeymap(DefaultEditorKit.pasteAction));
      copy.setMnemonic('P');

      edit.addSeparator();

      undo = edit.add(JavaEditorKit.getActionForKeymap(JavaEditorKit.antUndo));
      undo.setMnemonic('U');

      redo = edit.add(JavaEditorKit.getActionForKeymap(JavaEditorKit.antRedo));
      redo.setMnemonic('R');

      comment = new JMenuItem( res.getString("Comment"), 'M' );
      comment.setIcon(ImageList.comment);
      uncomment = new JMenuItem( res.getString("Uncomment"), 'O' );
      uncomment.setIcon(ImageList.uncomment);
      forwardLines = new JMenuItem( res.getString("Forward"), 'F' );
      forwardLines.setIcon(ImageList.indent);
      backwardLines = new JMenuItem( res.getString("Backward"), 'B' );
      backwardLines.setIcon(ImageList.unindent);

      // set accelerators
      cut.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X, Event.CTRL_MASK ) );
      copy.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, Event.CTRL_MASK ) );
      paste.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V, Event.CTRL_MASK ) );
      undo.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Z, Event.CTRL_MASK ) );
      redo.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Z, Event.CTRL_MASK | Event.SHIFT_MASK ) );

      comment.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_K , Event.CTRL_MASK));
      uncomment.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L , Event.CTRL_MASK));

      forwardLines.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_G , Event.CTRL_MASK));
      backwardLines.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H , Event.CTRL_MASK));

      comment.setHorizontalAlignment(AbstractButton.LEFT);
      uncomment.setHorizontalAlignment(AbstractButton.LEFT);
      forwardLines.setHorizontalAlignment(AbstractButton.LEFT);
      backwardLines.setHorizontalAlignment(AbstractButton.LEFT);

      comment.addActionListener( this );
      uncomment.addActionListener( this );
      forwardLines.addActionListener( this );
      backwardLines.addActionListener( this );

      // adds edit menu items to edit menu
      edit.addSeparator();
      edit.add(comment);
      edit.add(uncomment);
      edit.addSeparator();
      edit.add(forwardLines);
      edit.add(backwardLines);
    }

    public boolean isTopLevelMenu() {
      return true;
    }  

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == comment ) {
        commentLines();
      }
      else if ( e.getSource() == uncomment ) {
        unCommentLines();
      }
      else if ( e.getSource() == forwardLines ) {
        moveLines(true);
      }
      else if ( e.getSource() == backwardLines ) {
        moveLines(false);
      }
    }
  }

  class SearchMenu extends JMenu
                   implements ActionListener {
    // Search Menu Items
    JMenuItem find, findNext, replace, gotoLineNum, browse;

    /**
     * Builds Search Menu
     */
    public SearchMenu() {
      setText( res.getString("Search") );
      JMenu search = this;
      search.setMnemonic( 'S' );
      find = new JMenuItem( res.getString("Find"), 'F' );
      find.setIcon(ImageList.find);
      findNext = new JMenuItem( res.getString("SearchAgain"), 'S' );
      findNext.setIcon(ImageList.findagain);
      replace = new JMenuItem( res.getString("Replace"), 'R');
      replace.setIcon(ImageList.replace);
      gotoLineNum = new JMenuItem( res.getString("GotoLineNumber"), 'G' );
      gotoLineNum.setIcon(ImageList.goline);
      browse = new JMenuItem( res.getString("BrowseSymbol"), 'Y' );

      // set accelerators
      find.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F, Event.CTRL_MASK ) );
      findNext.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F3, 0 ) );
      replace.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_R, Event.CTRL_MASK ) );
      gotoLineNum.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_J, Event.CTRL_MASK ) );
      browse.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0 ) );

      // set alignments
      find.setHorizontalAlignment(AbstractButton.LEFT);

      // add handler
      find.addActionListener( this );
      findNext.addActionListener( this );
      replace.addActionListener( this );
      gotoLineNum.addActionListener( this );

      search.add(find);
      search.add(findNext);
      search.add(replace);
      search.addSeparator();
      search.add(gotoLineNum);
      // 1999년 5월 12일 버전에서는 제외
      //search.addSeparator();
      //search.add(browse);
    }

    public boolean isTopLevelMenu() {
      return true;
    }

    public void actionPerformed( ActionEvent e ) {
      if(mainPane.getComponentCount() == 0) return;

      TextViewPanel tvp = (currentPE == null) ? null : currentPE.getCurrentTvp();

      if ( e.getSource() == find ) {
        if (currentPE != null &&  tvp != null ) tvp.findString(true);
      }
      else if ( e.getSource() == findNext ) {
        if (currentPE != null && tvp != null ) tvp.findString(false);
      }
      else if ( e.getSource() == replace ) {
        if (currentPE != null && tvp != null ) tvp.replaceString(true);
      }
      else if ( e.getSource() == gotoLineNum ) {
        if (currentPE != null) currentPE.gotoLine();
      }
    }
  }

  class ViewMenu extends JMenu
                  implements ActionListener {
    // View Menu Items
    JCheckBoxMenuItem msgView;
    JCheckBoxMenuItem executionView;
    JCheckBoxMenuItem chatView;
    JCheckBoxMenuItem antDebugView;

    // menu "View"
    public ViewMenu() {
      setText( res.getString("View") );
      JMenu view = this;
      view.setMnemonic( 'V' );
      msgView = new JCheckBoxMenuItem( res.getString("MessageView") );
      executionView = new JCheckBoxMenuItem( res.getString("ExecutionView") );
      chatView = new JCheckBoxMenuItem( res.getString("ChatView") );

      msgView.addActionListener(this);
      executionView.addActionListener(this);
      chatView.addActionListener(this);

      msgView.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_1, Event.ALT_MASK ) );
      msgView.setMnemonic('V');

      executionView.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_2, Event.ALT_MASK ) );
      executionView.setMnemonic('V');

      view.add(msgView);
      view.add(executionView);
      //view.add(chatView);
      
      // Debug 모드이면 Ant Debug View를 보여주는 메뉴를 추가 한다.
      if ( Main.isDebugMode() ) {
	      antDebugView = new JCheckBoxMenuItem( "Ant Debug View" );
	      antDebugView.addActionListener(this);
      	view.add(antDebugView);
      }
    }

    public boolean isTopLevelMenu() {
      return true;
    }
    

    public void setOutputSelection(boolean state){
      executionView.setState(state);
    }

    public void setMessageSelection(boolean state){
      msgView.setState(state);
    }

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == msgView ) {
        if(chatView.getState()){
          chatView.setState(false);
        }
      	if (currentPE != null) currentPE.showMessageBoard(msgView.getState());
      }
      else if ( e.getSource() == executionView ) {
      	if (output != null) output.setVisible(executionView.getState());
      }
      else if ( e.getSource() == antDebugView ) {
      	Main.getAntOutputFrame().setVisible(antDebugView.getState());
      }
      else if ( e.getSource() == chatView ) {
        if(!chatFrame.isConnected()) chatFrame.connectToServer();
        if(msgView.getState()) {
          msgView.setState(false);
      	  if (currentPE != null) currentPE.showMessageBoard(msgView.getState());
        }

        chatFrame.setVisible(true);
      }
    }
  }

  // itree 추가... OpenSourcePanel에서 shortcut으로 이용
  public void make() {
    if (currentPE != null) {
      SourceEntry se = currentPE.getLastOpenedSource();
      if (se != null) currentPE.compileFile(se.getPath(), se.getName());
    }
  }

  class BuildMenu extends JMenu
                  implements ActionListener {
    // Build Menu Items
    JMenuItem /*compile,*/ make, rebuild, javadocMake, javadocMakeDir, javadocMakePrj;

    // menu "Build"
    public BuildMenu() {
      setText( res.getString("Build") );
      JMenu build = this;
      build.setMnemonic( 'B' );
      make = new JMenuItem( res.getString("Make"), 'M' );
      make.setIcon(ImageList.make);
      rebuild = new JMenuItem( res.getString("Rebuild"), 'B' );
      rebuild.setIcon(ImageList.rebuild);
      javadocMakePrj = new JMenuItem( res.getString("JavadocMakePrj"), 'P');
      javadocMakeDir = new JMenuItem( res.getString("JavadocMakeDir"), 'D');
      javadocMake = new JMenuItem( res.getString("JavadocMake"), 'F');

      make.setHorizontalAlignment(AbstractButton.LEFT);
      rebuild.setHorizontalAlignment(AbstractButton.LEFT);
      javadocMakePrj.setHorizontalAlignment(AbstractButton.LEFT);
      javadocMakeDir.setHorizontalAlignment(AbstractButton.LEFT);
      javadocMake.setHorizontalAlignment(AbstractButton.LEFT);

      // accelerators
      make.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F9, Event.CTRL_MASK ) );

      make.addActionListener( this );
      rebuild.addActionListener( this );
      javadocMakePrj.addActionListener( this );
      javadocMakeDir.addActionListener( this );
      javadocMake.addActionListener( this );

      build.add(make);
      build.add(rebuild);
      build.addSeparator();
      build.add(javadocMakePrj);
      build.add(javadocMakeDir);
      build.add(javadocMake);
    }

    public boolean isTopLevelMenu() {
      return true;
    }  

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == make ) {
        //if (currentPE != null) {
          //SourceEntry se = currentPE.getLastOpenedSource();
          //if (se != null) currentPE.compileFile(se.getPath(), se.getName());
        //}
        if (currentPE != null) currentPE.makeProject();
      }
      else if ( e.getSource() == rebuild ) {
        if (currentPE != null) currentPE.buildProject();
      }
      else if ( e.getSource() == javadocMakePrj ) {
        if (currentPE != null) currentPE.makeJavadocPrj();

      }
      else if ( e.getSource() == javadocMakeDir ) {
        if (currentPE != null) currentPE.makeJavadocDir();
      }
      else if ( e.getSource() == javadocMake ) {
        if (currentPE != null) {
          SourceEntry se = currentPE.getLastOpenedSource();
          String fileName, pathName;
          if (se != null) {
            fileName = se.getName();
            pathName = se.getPath();

            currentPE.makeJavadocFile(pathName, fileName);
          }
        }
      }
    }
  }

  class RunMenu extends JMenu
                implements ActionListener {
    // Run Menu Items
    JMenuItem runApp, stop, parameters, stepover, traceinto, stepup, cont, reset,
              addwatch, addBreakPoint, inspect;

    public RunMenu() {
      setText( res.getString("Run") );
      JMenu run = this;
      run.setMnemonic( 'R' );
      runApp = new JMenuItem( res.getString("RunApp"), 'R' );
      runApp.setIcon(ImageList.run);
      stop = new JMenuItem( res.getString("Stop"), 'S' );
      stop.setIcon(ImageList.stop);
      parameters = new JMenuItem( res.getString("Parameters"), 'P' );
      stepover = new JMenuItem( res.getString("StepOver") );
      traceinto = new JMenuItem( res.getString("TraceInto") );
      stepup = new JMenuItem( res.getString("StepUp") );
      cont = new JMenuItem( res.getString("Continue") );
      reset = new JMenuItem( res.getString("ProgramReset") );

      addwatch = new JMenuItem( res.getString("AddWatch") );
      addBreakPoint = new JMenuItem( res.getString("AddBreakpoint") );
      inspect = new JMenuItem( res.getString("Inspect") );

      // set accelerators
      runApp.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ) );
      stepover.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ) );
      traceinto.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F7, 0 ) );
      addwatch.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F5, Event.CTRL_MASK ) );

      // alignments
      runApp.setHorizontalAlignment(AbstractButton.LEFT);
      stop.setHorizontalAlignment(AbstractButton.LEFT);

      // add handler
      runApp.addActionListener( this );
      stop.addActionListener( this );
      parameters.addActionListener( this );
      stepover.addActionListener( this );
      traceinto.addActionListener( this );
      stepup.addActionListener( this );
      cont.addActionListener( this );
      reset.addActionListener( this );
      addwatch.addActionListener( this );
      addBreakPoint.addActionListener( this );
      inspect.addActionListener( this );

      run.add( runApp );
      run.add( stop );
      run.add( parameters );
      // 1999년 5월 12일 버전에서는 디버그 기능 빼둠
      /*
      run.addSeparator();
      run.add( stepover );
      run.add( traceinto );
      run.add( stepup );
      run.add( cont );
      run.add( reset );
      run.addSeparator();
      run.add( addwatch );
      run.add( addBreakPoint );
      run.add( inspect );
      */
    }

    public boolean isTopLevelMenu() {
      return true;
    }

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == runApp ) {
        if (currentPE != null) {
//          System.out.println("current PE is not null in MF");
          SourceEntry se = currentPE.getLastOpenedSource();
          String fileName, pathName;
          // 1999.05.20 itree 추가
          // run source가 .html file인지 .java file인지 check!
          if (se != null) {
            fileName = se.getName();
            pathName = se.getPath();

            if (fileName.endsWith(".html") || fileName.endsWith(".htm"))
               currentPE.runApplet(pathName, fileName);
            else {
//             System.out.println("se is not null in MF");
               //codeContext.notifyRun();
              currentPE.runClass(se.getRunnableClassName());
            }
          }
        }
      }
      else if ( e.getSource() == stop ) {
        if (currentPE != null) {
//          System.out.println("current PE is not null in MF");
          currentPE.killProcess();
        }
      }
      else if ( e.getSource() == parameters ) {
      	if (currentPE != null)
        	currentPE.showParameters();
      }
    }
  }

  class OptionMenu extends JMenu
                   implements ActionListener {
    // Option Menu Items
    JMenuItem IDEOption, pathProperty, javaDocProperty, javaDocViewer;

    // menu "Option"
    public OptionMenu() {
      setText( res.getString("Option") );
      JMenu option = this;
      option.setMnemonic( 'O' );
      IDEOption = new JMenuItem( res.getString("IDEOptions"), 'I' );
      IDEOption.setIcon(ImageList.optionIDE);
      pathProperty = new JMenuItem( res.getString("DefaultPathProperies"), 'D' );
      pathProperty.setIcon(ImageList.defaultPath);
      javaDocProperty = new JMenuItem( res.getString("JavaDocProperty"), 'J' );
      javaDocProperty.setIcon(ImageList.docProperty);
      javaDocViewer = new JMenuItem( res.getString("SettingJavadoc"), 'S' );
      javaDocViewer.setIcon(ImageList.docViewer);

      IDEOption.setHorizontalTextPosition(JButton.RIGHT);
      IDEOption.setHorizontalAlignment(AbstractButton.LEFT);

      // add handler
      IDEOption.addActionListener( this );
      pathProperty.addActionListener( this );
      javaDocProperty.addActionListener( this );
      javaDocViewer.addActionListener( this );

      option.add(IDEOption);
      option.add(pathProperty);
      option.add(javaDocProperty);
      option.add(javaDocViewer);
      // 정상 작동을 하지 않아서, 우선에는 빼둠
      //option.add(buildOptionLnFMenu());
      //option.add(buildOptionMetalThemeMenu());
      option.add(new LanguageMenu());
    }

    public boolean isTopLevelMenu() {
      return true;
    }

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == IDEOption ) {
        OptionPropertyDlg dlg = new OptionPropertyDlg(MainFrame.this, "Option...", true);
        //dlg.setLocation(300, 300);
        dlg.setVisible(true);

        if(dlg.isOK()){
              //Main.property.setPathModel(dlg.getPathModel());
              //IdePropertyManager.saveProperty(Main.property);
          for (int i = 0; i < mainPane.getTabCount(); i++) {
            ProjectExplorer pe = (ProjectExplorer)mainPane.getComponentAt(i);
            if (pe != null) {
              pe.updateEnvironment();
            }
          }
        }
      }
      else if ( e.getSource() == pathProperty ) {
        PathPropertyDlg dlg = new PathPropertyDlg(MainFrame.this, "Option...", true);
        dlg.setVisible(true);
      }
      else if ( e.getSource() == javaDocProperty) {
        DocPropertyDlg dlg = new DocPropertyDlg(MainFrame.this, "JavaDoc Option Property", true);
        dlg.setVisible(true);
      }
      else if ( e.getSource() == javaDocViewer ) {
        HelpPropertyDlg dlg = new HelpPropertyDlg(MainFrame.this, "JavaDoc Viewer Property", true);
        dlg.setVisible(true);
      }

      MainFrame.this.requestFocus();
    }
  }

  class LanguageMenu extends JMenu
                     implements ActionListener {
    // Language Menu Items
    JMenuItem english, korean, japanese;

    public LanguageMenu() {
      setText( res.getString("Language") );
      JMenu language = this;
      language.setMnemonic( 'O' );
      english = new JMenuItem( res.getString("English"), 'E' );
      korean = new JMenuItem( res.getString("Korean"), 'K' );
      japanese = new JMenuItem( res.getString("Japanese"), 'J' );

      // add handler
      english.addActionListener( this );
      korean.addActionListener( this );
      japanese.addActionListener( this );

      language.add( english );
      language.add( korean );
      language.add( japanese );
    }

    public void actionPerformed( ActionEvent e ) {

      if ( e.getSource() == english ) {
        res = ResourceBundle.getBundle("com.antsoft.ant.main.MainFrameRes", Locale.ENGLISH );
      }
      else if ( e.getSource() == japanese ) {
        res = ResourceBundle.getBundle("com.antsoft.ant.main.MainFrameRes", Locale.JAPANESE );
      }
      else if ( e.getSource() == korean ) {
        res = ResourceBundle.getBundle("com.antsoft.ant.main.MainFrameRes", Locale.KOREAN );
      }
      buildMenus();
    }
  }

  class ToolsMenu extends JMenu {

    // Tools Menu Items
    JMenuItem java2html, beautify, lineCount, customW, addCustomW, removeCustomW;

    // menu "Tools"
    public ToolsMenu() {
      setText( res.getString("Tools") );
      JMenu tools = this;
      tools.setMnemonic( 'T' );
      java2html = new JMenuItem( res.getString("JavatoHTML"), 'H' );
      java2html.setIcon(ImageList.javatohtml);
      beautify = new JMenuItem( res.getString("BeautifyJavaSource"), 'B' );
      beautify.setIcon(ImageList.beautify);
      lineCount = new JMenuItem( res.getString("SourceLineCount"), 'C' );
      lineCount.setIcon(ImageList.linecount);
      customW = new JMenu( res.getString("CustomWizard"));
      customW.setIcon(ImageList.customW);
      customW.setMnemonic('W');
      addCustomW = new JMenuItem( res.getString("AddCustomWizard"), 'A' );
      removeCustomW = new JMenuItem( res.getString("RemoveCustomWizard"), 'R' );

      java2html.addActionListener(
        new ActionListener() {
          public void actionPerformed( ActionEvent e ) {
            String currFile = null;
            TextViewPanel tvp = currentPE.getCurrentTvp();
            if(tvp != null) {
              SourceEntry se = tvp.getSourceEntry();
              if(se != null){
                currFile = se.getFullPathName();
              }
            }

            HtmlGeneratorDlg dlg = new HtmlGeneratorDlg((JFrame)MainFrame.this, "Java -> Html Generator", true);
            if(currFile != null) dlg.setCurrentFile(currFile);
            dlg.setVisible(true);
          }
        } );
      beautify.addActionListener(
        new ActionListener() {
          public void actionPerformed( ActionEvent e ) {
            indentCurrent();
          }
        } );

      lineCount.addActionListener(
        new ActionListener() {
          public void actionPerformed( ActionEvent e ) {
            sourceLineCount();
          }
        } );

      //lila
      addCustomW.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e){
            AddCustomWizardDialog addCustom = new AddCustomWizardDialog(MainFrame.this,customWizard);
          }
        } );

      removeCustomW.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e){
            RemoveCustomWizardDialog removeCustom = new RemoveCustomWizardDialog(MainFrame.this,customWizard);
          }
        } );

      tools.add(new ServletMenu()); //itree
      tools.add(java2html);
      tools.add(beautify);
      tools.addSeparator();
      tools.add(lineCount);
      //lila
      customW.add(addCustomW);
      customW.add(removeCustomW);
      tools.add(customW);
    }

    public boolean isTopLevelMenu() {
      return true;
    }
  }
  class ServletMenu extends JMenu
                 implements ActionListener {
    // Servlet Menu Items
    JMenuItem servletPathSet, servletRun;
    public ServletMenu() {
      setText(res.getString("Servlet"));
      JMenu servlet = this;
      servlet.setMnemonic( 'S' );
      servlet.setIcon(ImageList.jsdk);
      servletPathSet = new JMenuItem( res.getString("ServletPathSet"), 'P');
      servletPathSet.setIcon(ImageList.servletPath);
      servletRun = new JMenuItem( res.getString("ServletRun"), 'R');
      servletRun.setIcon(ImageList.servletRun);

      //add handler
      servletPathSet.addActionListener(this);
      servletRun.addActionListener(this);

      servlet.add(servletPathSet);
      servlet.add(servletRun);
    }

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == servletPathSet) {
        // Code
        //ServletPropertyDlg dlg = new ServletPropertyDlg(MainFrame.this, "Servlet Option Property", true);

        //dlg.setLocation(300, 300);
        //dlg.setVisible(true);
        DirChooser dlg = new DirChooser(MainFrame.this, "Choose Directory", "D:\\java\\project\\filebrowser", true,true);
        dlg.setVisible(true);
        
        if(dlg.isOK()){
          System.out.println(dlg.getPath());
        }
      }
      else if (e.getSource() == servletRun) {
        // Code
      }
    }
  }


  class HelpMenu extends JMenu
                 implements ActionListener {
    // Help Menu Items
    JMenuItem about, usingHelp, example;

    // menu "Help"
    public HelpMenu() {
      setText( res.getString("Help") );
      JMenu help = this;
      help.setMnemonic( 'H' );
      about = new JMenuItem( res.getString("AboutAnt") ); // By Kahn
      about.setIcon(ImageList.frameIcon);
      //usingHelp = new JMenuItem( res.getString("UsingAnt") ); // By Kahn
      example = new JMenuItem( res.getString("ExampleProject") );

      about.setHorizontalAlignment(AbstractButton.LEFT);
      about.setHorizontalTextPosition(JButton.RIGHT);

      example.setHorizontalAlignment(AbstractButton.LEFT);
      example.setHorizontalTextPosition(JButton.RIGHT);

      // By Kahn
      //menuUsingHelp.addActionListener(helpBroker.getHelpActionListener());	// javahelp 1.0B
      //usingHelp.addActionListener(new CSH.DisplayHelpFromSource(helpBroker));// javahelp rc1.0
      about.addActionListener( this );
      example.addActionListener( this );

      help.add(about);
      //help.add(usingHelp);// By kahn
      //help.add(example);

      //usingHelp.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F1 , 0) );
    }

    public boolean isTopLevelMenu() {
      return true;
    }  

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == about ) {
        MainFrame_AboutBox dlg = new MainFrame_AboutBox(MainFrame.this, "About Ant...", true);
        dlg.setVisible(true);
      }

      else if( e.getSource() == example ) {
        File exampleProj = new File(IdePropertyManager.EXAMPLE_PROJECT_PATH);
        if (exampleProj.exists()) {
          currentSourcePath = exampleProj.getParent();
          Project prj = openprj(exampleProj.getParent(), exampleProj.getName(), false);
        }
        else{
         	JOptionPane.showMessageDialog(MainFrame.this, "Example Project File not exist ...",
           		"Open Example Project Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  class ToolbarButton extends JButton{
    Color oldColor = null;
    public ToolbarButton(ImageIcon icon){
      super(icon);
      oldColor = getBackground();
      addMouseListener(new MouseAdapter(){
        public void mouseEntered(MouseEvent e){
         //setBackground(Color.darkGray);
        }

        public void mouseExited(MouseEvent e){
          //setBackground(oldColor);
        }
      });
    }
  }

  class MainToolBar extends JToolBar implements ActionListener, MouseListener {
    ToolbarButton newFile, open, save,
            copy, cut, paste,
            undo, redo, find,
            make, rebuild, run, stop,
            optionIDE, about;

    public MainToolBar() {
//      putClientProperty("JToolBar.isRollover", Boolean.TRUE);

      // -------------------- ToolBar Buttons ----------------------------------
      newFile = new ToolbarButton(ImageList.newFile);
      open = new ToolbarButton(ImageList.open);
      save = new ToolbarButton(ImageList.save);
      copy = new ToolbarButton(ImageList.copy);
      cut = new ToolbarButton(ImageList.cut);
      paste = new ToolbarButton(ImageList.paste);
      undo = new ToolbarButton(ImageList.undo);
      redo = new ToolbarButton(ImageList.redo);
      find = new ToolbarButton(ImageList.find);
      make = new ToolbarButton(ImageList.make);
      rebuild = new ToolbarButton(ImageList.rebuild);
      run = new ToolbarButton(ImageList.run);
      stop = new ToolbarButton(ImageList.stop);
      optionIDE = new ToolbarButton(ImageList.optionIDE);
      about = new ToolbarButton(ImageList.about);

      // Set Tool Tips
      newFile.setToolTipText("New File");
      open.setToolTipText("Open File");
      save.setToolTipText("Save File");
      copy.setToolTipText("Copy");
      cut.setToolTipText("Cut");
      paste.setToolTipText("Paste");
      undo.setToolTipText("Undo");
      redo.setToolTipText("Redo");
      find.setToolTipText("Find Text & Replace");
      make.setToolTipText("Make Project");
      rebuild.setToolTipText("Rebuild Project");
      stop.setToolTipText("Stop Running Project");
      run.setToolTipText("Run Project");
      optionIDE.setToolTipText("IDE Option");
      about.setToolTipText("Using Help");

      // Add ActionEvent handlers
      newFile.addActionListener( this );
      newFile.addMouseListener(this);
      open.addActionListener( this );
      open.addMouseListener(this);
      save.addActionListener( this );
      save.addMouseListener(this);
      copy.addActionListener( JavaEditorKit.getActionForKeymap(DefaultEditorKit.copyAction) );
      copy.addMouseListener(this);
      cut.addActionListener( JavaEditorKit.getActionForKeymap(DefaultEditorKit.cutAction) );
      cut.addMouseListener(this);
      paste.addActionListener( JavaEditorKit.getActionForKeymap(DefaultEditorKit.pasteAction) );
      paste.addMouseListener(this);
      undo.addActionListener( JavaEditorKit.getActionForKeymap(JavaEditorKit.antUndo) );
      undo.addMouseListener(this);
      redo.addActionListener( JavaEditorKit.getActionForKeymap(JavaEditorKit.antRedo) );
      redo.addMouseListener(this);
      find.addActionListener( this );
      find.addMouseListener(this);
      make.addActionListener( this );
      make.addMouseListener(this);
      rebuild.addActionListener( this );
      rebuild.addMouseListener(this);
      run.addActionListener( this );
      run.addMouseListener(this);
      stop.addActionListener( this );
      stop.addMouseListener(this);
      optionIDE.addActionListener( this );
      optionIDE.addMouseListener(this);
      //about.addActionListener(helpBroker.getHelpActionListener());
      //about.addActionListener(new CSH.DisplayHelpFromSource(helpBroker));// javahelp rc1.0
      // ---------------------- end of setup toolbar -------------------------

      // install main toolbar
      // TODO: New Project, Open Project, Save Project, Close Project 추가
      add(newFile);
      add(open);
      add(save);
      addSeparator();
      add(cut);
      add(copy);
      add(paste);
      addSeparator();
      add(undo);
      add(redo);
      addSeparator();
      add(find);
      addSeparator();
      add(make);
      add(rebuild);
      addSeparator();
      add(run);
      add(stop);
      addSeparator();
      add(optionIDE);
      addSeparator();
      add(about);
    }

    public void mouseEntered(MouseEvent e){

      if ( e.getSource() == newFile ) {
        m_Status.setExplainText("New File");
      } else if ( e.getSource() == open ) {
        m_Status.setExplainText("Open File");
      } else if ( e.getSource() == save ) {
        m_Status.setExplainText("Save File");
      } else if ( e.getSource() == copy ) {
        m_Status.setExplainText("Copy");
      } else if ( e.getSource() == cut ) {
        m_Status.setExplainText("Cut");
      } else if ( e.getSource() == paste ) {
        m_Status.setExplainText("Paste");
      } else if ( e.getSource() == undo ) {
        m_Status.setExplainText("Undo");
      } else if ( e.getSource() == redo ) {
        m_Status.setExplainText("Redo");
      } else if ( e.getSource() == find ) {
        m_Status.setExplainText("Find");
      } else if ( e.getSource() == make ) {
        m_Status.setExplainText("Make");
      } else if ( e.getSource() == rebuild ) {
        m_Status.setExplainText("Rebuild");
      } else if ( e.getSource() == run ) {
        m_Status.setExplainText("Run");
      } else if ( e.getSource() == stop ) {
        m_Status.setExplainText("Stop");
      } else if ( e.getSource() == optionIDE ) {
        m_Status.setExplainText("IDE Option setting..");
      }

    }

    public void mouseExited(MouseEvent e){
      m_Status.setExplainText("Press F1, for Help ");
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    
    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == newFile ) {
        fileNew();
      } else if ( e.getSource() == open ) {
        fileOpen();
      } else if ( e.getSource() == save ) {
        saveCurrent();
      } else if ( e.getSource() == find ) {
        if (currentPE != null)
        	if (currentPE.getCurrentTvp() != null)
          	 currentPE.getCurrentTvp().findString(true);
      } else if ( e.getSource() == make ) {
        if (currentPE != null) {
          //SourceEntry se = currentPE.getLastOpenedSource();
          //if (se != null) currentPE.compileFile(se.getPath(), se.getName());
          currentPE.makeProject();
        }
      } else if ( e.getSource() == rebuild ) {
        if (currentPE != null) currentPE.buildProject();
      } else if ( e.getSource() == run ) {
        if (currentPE != null) {
//          System.out.println("current PE is not null in MF");
          SourceEntry se = currentPE.getLastOpenedSource();
          if (se != null) {
//            System.out.println("se is not null in MF");
            //codeContext.notifyRun();
            currentPE.runClass(se.getRunnableClassName());
          }
        }
      } else if ( e.getSource() == stop ) {
        if (currentPE != null) {
//          System.out.println("current PE is not null in MF");
          currentPE.killProcess();
        }
      } else if ( e.getSource() == optionIDE ) {
        OptionPropertyDlg dlg = new OptionPropertyDlg(MainFrame.this, "Default Property", true);
        //dlg.setPathModel(Main.property.getPathModel());
        //dlg.setBounds(100, 100, 500, 500);
        dlg.setVisible(true);

        if(dlg.isOK()){
        //Main.property.setPathModel(dlg.getPathModel());
        //IdePropertyManager.saveProperty(Main.property);
          for (int i = 0; i < mainPane.getTabCount(); i++) {
            ProjectExplorer pe = (ProjectExplorer)mainPane.getComponentAt(i);
            if (pe != null) {
              pe.updateEnvironment();
            }
          }
        }
      }
    }
  }
}
