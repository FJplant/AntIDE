package com.antsoft.ant.property.defaultproperty;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

// by kahn
import com.antsoft.ant.main.MainFrame;

import com.antsoft.ant.property.*;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.util.WindowDisposer;

/**
 * default property dialog
 *
 * @author kim sang kyun
 */
public class PathPropertyDlg extends JDialog{
  private PathPanel pathTab;
  private JButton okBtn, cancelBtn, helpBtn;
  private JdkInfoContainer jdkInfoContainer;
  private LibInfoContainer libraryInfoContainer;
  private PathPropertyInfo pathPropertyInfo;
  private JFrame parent;
  private DefaultPathModel pathModel;
  private boolean isOK = false;

  public PathPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, "IDE Property", modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    pathModel = new DefaultPathModel();

    pathTab = new PathPanel(parent);
    pathTab.setModel(pathModel);

    //button panel
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");
    //MainFrame.helpBroker.enableHelp(helpBtn,"prop.path",null);	// by kahn

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    //center panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(pathTab, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(420, 450);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height)
      dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)
      dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,  (screenSize.height - dlgSize.height) / 2);
    setResizable(false);

    setInitProperty();
  }



  private void setInitProperty(){

    if(Main.property == null) return;
    if(Main.property.getPathModel() != null)  pathTab.setModel(Main.property.getPathModel());
  }

  /**
   * set DefaultPathModel
   *
   * @param newModel DefaultPathModel
   */
  public void setPathModel(DefaultPathModel newModel){
    this.pathModel = newModel;
    pathTab.setModel(newModel);
  }

  public DefaultPathModel getPathModel(){
    return this.pathModel;
  }

  public boolean isOK(){
    return this.isOK;
  }

  public void dispose(){
    int count = getComponentCount();
    for(int i=0; i<count; i++){
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  public void okPressed(){
    isOK = true;
    pathModel = pathTab.getModel();

    //path
    Main.property.setPathModel(pathModel);
    //save to file
    IdePropertyManager.saveProperty(Main.property);

    ///////////////////Open된 모든 프로젝트의 JDK정보를 바로 Update시킨다////////////
    Vector all_projects = ProjectManager.getProjects();
    if(all_projects != null){
      for(int i=0; i<all_projects.size(); i++){
        Project project = (Project)all_projects.elementAt(i);
        if(project.getProjectName().equals("Files")) {
          project.updateClassLoader();
          continue;
        }

        DefaultPathModel project_pathModel= project.getPathModel();
        JdkInfoContainer mainJdkInfos = Main.property.getPathModel().getJdkInfoContainer();

        //Jdk Info 조정
        project_pathModel.setJdkInfoContainer(mainJdkInfos);

        if(project_pathModel.getCurrentJdkInfo() != null){
          JdkInfo newInfo = mainJdkInfos.getJdkInfo(project_pathModel.getCurrentJdkInfo().getVersion());
          if(newInfo ==null){
            project_pathModel.setCurrentJdkInfo(null);
          }
          else{
            project_pathModel.setCurrentJdkInfo(newInfo);
          }
        }
        else{
          project_pathModel.setCurrentJdkInfo(Main.property.getPathModel().getCurrentJdkInfo());
        }
        //Library Info 조정
        LibInfoContainer mainLibInfos = Main.property.getPathModel().getAllLibInfoContainer();
        project_pathModel.setAllLibInfoContainer(mainLibInfos);

        LibInfoContainer oldSelLibInfos = project_pathModel.getSelectedLibInfoContainer();
        LibInfoContainer newSelLibinfos = new LibInfoContainer();

        for(Enumeration e=oldSelLibInfos.getLibraryInfos(); e.hasMoreElements(); ){
          LibInfo newInfo = (LibInfo)mainLibInfos.getLibraryInfo(((LibInfo)(e.nextElement())).getName());
          if(newInfo != null){
            newSelLibinfos.addLibraryInfo(newInfo);
          }
        }
        project_pathModel.setSelectedLibInfoContainer(newSelLibinfos);

        //Library Pool 조정
        Vector datas = project_pathModel.getLibraryPoolDatas();
//        System.out.println("update class loader start ");

        project_pathModel.updateClassPath();

        String currProjName = project.getProjectName();
        LibraryPool.removeAllProjectLibs(currProjName);

        for(int j = 0; j < datas.size(); j++){
          LibraryPool.addProjectLibraryInfo(currProjName, (LibraryInfo)datas.elementAt(j));
        }
        project.setPathModel(project_pathModel);
        project.updateClassLoader();
        //일관성을 위해서 save한다
        ProjectManager.saveProject(project);
      }
    }
  }

  class DelayedFocus implements Runnable {
    Component comp;
    public DelayedFocus(Component comp) {
      this.comp = comp;
    }
    public void run() {
      comp.requestFocus();
    }
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okPressed();
        dispose();
        Runnable r = new DelayedFocus(MainFrame.mainFrame);
        SwingUtilities.invokeLater(r);
      }
      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
        Runnable r = new DelayedFocus(MainFrame.mainFrame);
        SwingUtilities.invokeLater(r);
      }
    }
  }
}

