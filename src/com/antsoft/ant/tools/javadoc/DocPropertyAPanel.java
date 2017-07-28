/**
 * Javadoc Option 설정 Tabbed Panel;
 *
 * @author Lee Chul-Mok.
 */

package com.antsoft.ant.tools.javadoc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.ExtensionFileFilter;
import com.antsoft.ant.property.*;

public class DocPropertyAPanel extends JPanel {
  private JFrame parent;
  private Font f;
  private JRadioButton jdk1_1Rb, jdk1_2Rb, publicRb, protectedRb, packageRb, privateRb;
  private ButtonGroup jdkBg, scopeBg;
  private JCheckBox verboseCbx, authorCbx, versionCbx, noindexCbx, notreeCbx, nodeprecateCbx;
  private DocPropertyDlg docDlg;
  public static IdeProperty property;

  public DocPropertyAPanel(JFrame parent, DocPropertyDlg dlg) {
    this.parent = parent;
    docDlg = dlg;
    property = IdePropertyManager.loadProperty();

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  void jbInit() throws Exception {

    ChangeListener cl = new ChangeEventHandler();

    /*JButton b = new JButton();
    f = new Font(b.getFont().getName(), Font.BOLD, 12);
    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);

    JPanel emptyP = new JPanel();
    emptyP.setPreferredSize(new Dimension(350, 20));

    // title Label  Panel
    JLabel titleLbl = new JLabel("JDK Ja+vadoc Standard Option", JLabel.LEFT);
    titleLbl.setForeground(Color.black);
    JPanel titleP = new JPanel(new FlowLayout(FlowLayout.CENTER));

    titleP.add(titleLbl);
    titleP.setBorder(BorderList.loweredBorder);
    titleLbl.setHorizontalAlignment(SwingConstants.CENTER);*/


    // jdk version Panel
    boolean aa, bb;
    if (property.getJavadocVersion()) {
      aa = true;
      bb = false;
    } else {
      aa = false;
      bb = true;
    }
    jdk1_1Rb = new JRadioButton("JDK 1.1 ver", aa);
    jdk1_2Rb = new JRadioButton("JDK 1.2 ver", bb);
    //jdk1_1Rb.setFont(f);
    //jdk1_2Rb.setFont(f);

    jdkBg = new ButtonGroup();
    jdkBg.add(jdk1_1Rb);
    jdkBg.add(jdk1_2Rb);

    JPanel jdkP = new JPanel(new BorderLayout());
    JPanel jdkOP = new JPanel(new GridLayout(1,2));
    jdkOP.add(jdk1_1Rb);
    jdkOP.add(jdk1_2Rb);
    jdkOP.setPreferredSize(new Dimension(350,20));

    jdkP.add(jdkOP);
    jdkP.setBorder(BorderList.jdkBorder);
    jdk1_1Rb.addChangeListener(cl);
    jdk1_2Rb.addChangeListener(cl);


    // scope panel
    boolean publicB, protectedB, packageB, privateB;
    publicB = protectedB = packageB = privateB = false;
    if(property.getScopeDoc() == 1){
      publicB = true;
      protectedB = packageB = privateB = false;
    }
    else if (property.getScopeDoc() == 2){
      protectedB = true;
      publicB = packageB = privateB = false;
    }
    else if (property.getScopeDoc() == 3){
      packageB = true;
      publicB = protectedB = privateB = true;
    }
    else if (property.getScopeDoc() == 4){
      privateB = true;
      publicB = protectedB = packageB = false;
    }

    publicRb = new JRadioButton("-public ", publicB);
    protectedRb = new JRadioButton("-protected ", protectedB);
    packageRb = new JRadioButton("-package ", packageB);
    privateRb = new JRadioButton("-private ", privateB);

    /*publicRb.setFont(f);
    protectedRb.setFont(f);
    packageRb.setFont(f);
    privateRb.setFont(f);*/

    scopeBg = new ButtonGroup();
    scopeBg.add(publicRb);
    scopeBg.add(protectedRb);
    scopeBg.add(packageRb);
    scopeBg.add(privateRb);

    JPanel scopeP = new JPanel(new BorderLayout());
    JPanel scopeOP = new JPanel(new GridLayout(1,4));
    scopeOP.add(publicRb);
    scopeOP.add(protectedRb);
    scopeOP.add(packageRb);
    scopeOP.add(privateRb);
    scopeOP.setPreferredSize(new Dimension(350,20));

    scopeP.add(scopeOP);
    scopeP.setBorder(BorderList.scopeBorder);

    JPanel checkP = new JPanel(new GridLayout(2,3));

     // verbose panel
    verboseCbx = new JCheckBox("-verbose ", property.getVerboseDoc());
    //verboseCbx.setFont(f);

    // author panel
    authorCbx = new JCheckBox("-author ", property.getAuthorDoc());
    //authorCbx.setFont(f);

    // version panel
    versionCbx = new JCheckBox("-version ", property.getVersionDoc());
    //versionCbx.setFont(f);

    // noindex panel
    noindexCbx = new JCheckBox("-noindex ", property.getNoindexDoc());
    //noindexCbx.setFont(f);

    // notree panel
    notreeCbx = new JCheckBox("-notree ", property.getNotreeDoc());
    //notreeCbx.setFont(f);

    // nodeprecate panel
    nodeprecateCbx = new JCheckBox("-nodeprecate ", property.getNodeprecateDoc());
    //nodeprecateCbx.setFont(f);

    checkP.setBorder(BorderList.checkBorder);
    checkP.add(verboseCbx);
    checkP.add(versionCbx);
    checkP.add(authorCbx);
    checkP.add(noindexCbx);
    checkP.add(notreeCbx);
    checkP.add(nodeprecateCbx);


    /*JPanel empty2P = new JPanel();
    empty2P.setPreferredSize(new Dimension(350, 200));*/


    // Box
    Box box = Box.createVerticalBox();
    //box.add(titleP);
    //box.add(emptyP);
    box.add(jdkP);
    box.add(scopeP);
    box.add(checkP);
    //box.add(empty2P);

    setBorder(BorderList.etchedBorder5);
    add(box, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);

  }

  // Option 의 값을 return해 주는 함수
  public boolean getJdk1_1Doc() {
    return jdk1_1Rb.isSelected();
  }
  public boolean getVerboseDoc() {
    return verboseCbx.isSelected();
  }
  public boolean getPublicDoc() {
    return publicRb.isSelected();
  }
  public boolean getProtectedDoc() {
    return protectedRb.isSelected();
  }
  public boolean getPackageDoc() {
    return packageRb.isSelected();
  }
  public boolean getPrivateDoc() {
    return privateRb.isSelected();
  }
  public boolean getAuthorDoc() {
    return authorCbx.isSelected();
  }
  public boolean getVersionDoc() {
    return versionCbx.isSelected();
  }
  public boolean getNoindexDoc() {
    return noindexCbx.isSelected();
  }
  public boolean getNotreeDoc() {
    return notreeCbx.isSelected();
  }
  public boolean getNodeprecateDoc() {
    return nodeprecateCbx.isSelected();
  }


  class ChangeEventHandler implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      if(e.getSource() == jdk1_1Rb) {
        docDlg.setDisable1_2Component(true);
      }
      else if(e.getSource() == jdk1_2Rb) {
        docDlg.setDisable1_2Component(false);
      }
    }
  }

}
