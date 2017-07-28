/**
 * Javadoc Option ���� Tabbed Panel;
 *
 * @author Lee Chul-Mok.
 */

package com.antsoft.ant.tools.javadoc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.IdePropertyManager;
import com.antsoft.ant.util.WindowDisposer;

public class DocPropertyDlg extends JDialog {

  private DocPropertyAPanel standardTab;
  private DocPropertyBPanel commonTab;
  private DocPropertyCPanel supplementTab;
  private DocPropertyDPanel htmlTab;

  private JTabbedPane tabbedPane;
  private JFrame parent;
  private JButton okBtn, cancelBtn, helpBtn;
  private boolean isOK = false;
  private boolean isHelp = false;

  public DocPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, "JavaDoc Option Property", modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    tabbedPane = new JTabbedPane();

    standardTab = new DocPropertyAPanel(parent, this);
    tabbedPane.addTab("Standard", standardTab);
 //   tabbedPane.setSelectedIndex(0);

    commonTab = new DocPropertyBPanel(parent);
    tabbedPane.addTab("Common", commonTab);
 //   tabbedPane.setSelectedIndex(1);

    supplementTab = new DocPropertyCPanel(parent);
    tabbedPane.addTab("Supplement", supplementTab);
 //   tabbedPane.setSelectedIndex(2);

    htmlTab = new DocPropertyDPanel(parent);
    tabbedPane.addTab("Html", htmlTab);
//    tabbedPane.setSelectedIndex(3);

    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");
    helpBtn.setActionCommand("Help");
    // Kahn���� ����� �����ؾ��� ����.
    //MainFrame.helpBroker.enableHelp(helpBtn, "servlet.option", null);

    //button Panel
    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    //center Panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(tabbedPane, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    // Size  �κ�..�����ϰ� �̰��� ��ġ��.
    setSize(400,570);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(false);


    setDisable1_2Component(standardTab.getJdk1_1Doc());

    // �̸� �����Ǿ� �ִ��� ���⼭ Ȯ���� ����..
    // ������ �ִ� init property�� �����Ѵ�.
    // setInitProperty();
  }

  public boolean isOK() {
    return this.isOK;
  }

  public void okPressed() {
    int scope = 0;
    isOK = true;

    // ���� Property�� �ٲپ� �ش�.

    Main.property.setJavadocVersion(standardTab.getJdk1_1Doc());
    Main.property.setVerboseDoc(standardTab.getVerboseDoc());

    if(standardTab.getPublicDoc())
      scope = 1;
    else if(standardTab.getProtectedDoc())
      scope = 2;
    else if(standardTab.getPackageDoc())
      scope = 3;
    else if(standardTab.getPrivateDoc())
      scope = 4;

    Main.property.setScopeDoc(scope);
    Main.property.setAuthorDoc(standardTab.getAuthorDoc());
    Main.property.setVersionDoc(standardTab.getVersionDoc());
    Main.property.setNoindexDoc(standardTab.getNoindexDoc());
    Main.property.setNotreeDoc(standardTab.getNotreeDoc());
    Main.property.setNodeprecateDoc(standardTab.getNodeprecateDoc());

    Main.property.setNewclasspathDoc(commonTab.getNewclasspathDoc());
    Main.property.setNewsourcepathDoc(commonTab.getNewsourcepathDoc());
    Main.property.setClasspathDoc(commonTab.getClasspathDoc());
    Main.property.setSourcepathDoc(commonTab.getSourcepathDoc());
    Main.property.setEncodingDoc(commonTab.getEncodingDoc());
    Main.property.setDocencodingDoc(commonTab.getDocencodingDoc());
    Main.property.setJDoc(commonTab.getJDoc());

    Main.property.setUseDoc(supplementTab.getUseDoc());
    Main.property.setNohelpDoc(supplementTab.getNohelpDoc());
    Main.property.setNonavbarDoc(supplementTab.getNonavbarDoc());
    Main.property.setNodeprecatedlistDoc(supplementTab.getNodeprecatedlistDoc());
    Main.property.setSplitindexDoc(supplementTab.getSplitindexDoc());
    Main.property.setDocletDoc(supplementTab.getDocletDoc());
    Main.property.setDocletpathDoc(supplementTab.getDocletpathDoc());
    Main.property.setBootclasspathDoc(supplementTab.getBootclasspathDoc());
    Main.property.setExtdirsDoc(supplementTab.getExtdirsDoc());
    Main.property.setLocaleDoc(supplementTab.getLocaleDoc());
    Main.property.setLinkDoc(supplementTab.getLinkDoc());
    Main.property.setLinkofflineDoc(supplementTab.getLinkofflineDoc());
    Main.property.setGroupDoc(supplementTab.getGroupDoc());
    Main.property.setHelpfileDoc(supplementTab.getHelpfileDoc());
    Main.property.setStylesheetfileDoc(supplementTab.getStylesheetfileDoc());

    Main.property.setWindowtitleDoc(htmlTab.getWindowtitleDoc());

    Main.property.setDoctitleDoc(convertString(htmlTab.getDoctitleDoc()));
    Main.property.setHeaderDoc(convertString(htmlTab.getHeaderDoc()));
    Main.property.setFooterDoc(convertString(htmlTab.getFooterDoc()));
    Main.property.setBottomDoc(convertString(htmlTab.getBottomDoc()));


    String merong = convertString(htmlTab.getHeaderDoc());
    
    IdePropertyManager.saveProperty(Main.property);

    DocInfo docInfo = new DocInfo();

    dispose();
  }
  public void dispose() {
    int count = getComponentCount();
    for(int i=0; i<count; i++) {
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  private String convertString(String str) {
    StringBuffer ret = new StringBuffer();
    char t;

    for(int i=0; i<str.length(); i++) {
      t = str.charAt(i);
      if(t == '\n')
        ret = ret.append("***");
      else
        ret.append(t);
    }

    return ret.toString();
  }

  public void setDisable1_2Component(boolean disable) {

    if(disable) {
      supplementTab.setDisableAllComponent(true);
      htmlTab.setDisableAllComponent(true);
    }
    else {
      supplementTab.setDisableAllComponent(false);
      htmlTab.setDisableAllComponent(false);
    }
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")) {
        okPressed();
      }
      else if(cmd.equals("CANCEL")) {
        isOK = false;
        dispose();
      }
    }
  }
}

