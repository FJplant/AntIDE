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
import com.antsoft.ant.property.*;

public class DocPropertyCPanel extends JPanel {
  private JFrame parent;
  private JCheckBox useCbx, nohelpCbx, nonavbarCbx, nodepreCbx, splitCbx;
  private JTextField docletFld, docletpathFld, bootclasspathFld, extdirsFld,
          localeFld,linkFld, linkofflineFld, groupFld, helpfileFld,stylesheetfileFld;
  private JLabel docletLbl, docletpathLbl, bootclasspathLbl, extdirsLbl,
          localeLbl,linkLbl, linkofflineLbl, groupLbl, helpfileLbl, stylesheetfileLbl;
  public static IdeProperty property;

  public DocPropertyCPanel(JFrame parent) {
    this.parent = parent;
    property = IdePropertyManager.loadProperty();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  void jbInit() throws Exception {

    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);

    // checkbox group panel
    JPanel checkP = new JPanel(new GridLayout(3,2));
    useCbx = new JCheckBox("-use", property.getUseDoc());
    //useCbx.setForeground(Color.black);
    nohelpCbx = new JCheckBox("-nohelp", property.getNohelpDoc());
    //nohelpCbx.setForeground(Color.black);
    nonavbarCbx = new JCheckBox("-nonavbar", property.getNonavbarDoc());
    //nonavbarCbx.setForeground(Color.black);

    checkP.add(useCbx);
    checkP.add(nohelpCbx);
    checkP.add(nonavbarCbx);

    //JPanel checkDownP = new JPanel(new GridLayout(1,2));
    nodepreCbx = new JCheckBox("-nodeprecatedlist", property.getNodeprecatedlistDoc());
    //nodepreCbx.setForeground(Color.black);
    splitCbx = new JCheckBox("-splitindex", property.getSplitindexDoc());
    //splitCbx.setForeground(Color.black);

    checkP.add(nodepreCbx);
    checkP.add(splitCbx);
    checkP.setBorder(BorderList.checkBorder);

    /*JPanel checkP = new JPanel(new GridLayout(2,1));
    checkP.add(checkUpP);
    checkP.add(checkDownP);
    checkP.setBorder(BorderList.checkBorder);*/

    // text Group Panel
    // doclet panel
    docletLbl = new JLabel("-doclet");
    docletLbl.setPreferredSize(new Dimension(100,20));
    docletFld = new JTextField(18);
    docletFld.setText(property.getDocletDoc());
    JPanel docletP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    docletP.add(docletLbl);
    docletP.add(docletFld);

    // docletpath panel
    docletpathLbl = new JLabel("-docletpath");
    docletpathLbl.setPreferredSize(new Dimension(100,20));
    docletpathFld = new JTextField(18);
    docletpathFld.setText(property.getDocletpathDoc());
    JPanel docletpathP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    docletpathP.add(docletpathLbl);
    docletpathP.add(docletpathFld);

    // bootclasspath panel
    bootclasspathLbl = new JLabel("-bootclasspath");
    bootclasspathLbl.setPreferredSize(new Dimension(100,20));
    bootclasspathFld = new JTextField(18);
    bootclasspathFld.setText(property.getBootclasspathDoc());
    JPanel bootclasspathP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    bootclasspathP.add(bootclasspathLbl);
    bootclasspathP.add(bootclasspathFld);

    // extdirs panel
    extdirsLbl = new JLabel("-extdirs");
    extdirsLbl.setPreferredSize(new Dimension(100,20));
    extdirsFld = new JTextField(18);
    JPanel extdirsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    extdirsP.add(extdirsLbl);
    extdirsP.add(extdirsFld);

    // locale panel
    localeLbl = new JLabel("-locale");
    localeLbl.setPreferredSize(new Dimension(100,20));
    localeFld = new JTextField(18);
    localeFld.setText(property.getLocaleDoc());
    JPanel localeP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    localeP.add(localeLbl);
    localeP.add(localeFld);

    // link panel
    linkLbl = new JLabel("-link");
    linkLbl.setPreferredSize(new Dimension(100,20));
    linkFld = new JTextField(18);
    linkFld.setText(property.getLinkDoc());
    JPanel linkP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    linkP.add(linkLbl);
    linkP.add(linkFld);

    // linkoffline panel
    linkofflineLbl = new JLabel("-linkoffline");
    linkofflineLbl.setPreferredSize(new Dimension(100,20));
    linkofflineFld = new JTextField(18);
    linkofflineFld.setText(property.getLinkofflineDoc());
    JPanel linkofflineP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    linkofflineP.add(linkofflineLbl);
    linkofflineP.add(linkofflineFld);

    // group panel
    groupLbl = new JLabel("-group");
    groupLbl.setPreferredSize(new Dimension(100,20));
    groupFld = new JTextField(18);
    groupFld.setText(property.getGroupDoc());
    JPanel groupP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    groupP.add(groupLbl);
    groupP.add(groupFld);

    // helpfile panel
    helpfileLbl = new JLabel("-helpfile");
    helpfileLbl.setPreferredSize(new Dimension(100,20));
    helpfileFld = new JTextField(18);
    helpfileFld.setText(property.getHelpfileDoc());
    JPanel helpfileP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    helpfileP.add(helpfileLbl);
    helpfileP.add(helpfileFld);

    // stylesheetfile panel
    stylesheetfileLbl = new JLabel("-stylesheetfile");
    stylesheetfileLbl.setPreferredSize(new Dimension(100,20));
    stylesheetfileFld = new JTextField(18);
    stylesheetfileFld.setText(property.getStylesheetfileDoc());
    JPanel stylesheetfileP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    stylesheetfileP.add(stylesheetfileLbl);
    stylesheetfileP.add(stylesheetfileFld);

    JPanel totalP = new JPanel(new GridLayout(10,1));
    totalP.add(docletP);
    totalP.add(docletpathP);
    totalP.add(bootclasspathP);
    totalP.add(extdirsP);
    totalP.add(localeP);
    totalP.add(linkP);
    totalP.add(linkofflineP);
    totalP.add(groupP);
    totalP.add(helpfileP);
    totalP.add(stylesheetfileP);
    totalP.setBorder(BorderList.pathBorder);

    JPanel emptyP = new JPanel(new BorderLayout());
    emptyP.setPreferredSize(new Dimension(350,200));

    // Box
    Box box = Box.createVerticalBox();
    box.add(checkP);
    box.add(totalP);
    //box.add(emptyP);

    setBorder(BorderList.etchedBorder5);
    add(box, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);

  }

  // option의 값을 넘겨준다.

  public boolean getUseDoc() {
    return useCbx.isSelected();
  }
  public boolean getNohelpDoc() {
    return nohelpCbx.isSelected();
  }
  public boolean getNonavbarDoc() {
    return nonavbarCbx.isSelected();
  }
  public boolean getNodeprecatedlistDoc() {
    return nodepreCbx.isSelected();
  }
  public boolean getSplitindexDoc() {
    return splitCbx.isSelected();
  }

  public String getDocletDoc() {
    return docletFld.getText();
  }
  public String getDocletpathDoc() {
    return docletpathFld.getText();
  }
  public String getBootclasspathDoc() {
    return bootclasspathFld.getText();
  }
  public String getExtdirsDoc() {
    return extdirsFld.getText();
  }
  public String getLocaleDoc() {
    return localeFld.getText();
  }
  public String getLinkDoc() {
    return linkFld.getText();
  }
  public String getLinkofflineDoc() {
    return linkofflineFld.getText();
  }
  public String getGroupDoc() {
    return groupFld.getText();
  }
  public String getHelpfileDoc() {
    return helpfileFld.getText();
  }
  public String getStylesheetfileDoc() {
    return stylesheetfileFld.getText();
  }

  public void setDisableAllComponent(boolean disable) {

    if (disable) {
      useCbx.setEnabled(false);
      nohelpCbx.setEnabled(false);
      nonavbarCbx.setEnabled(false);
      nodepreCbx.setEnabled(false);
      splitCbx.setEnabled(false);

      docletLbl.setForeground(Color.gray);
      docletpathLbl.setForeground(Color.gray);
      bootclasspathLbl.setForeground(Color.gray);
      extdirsLbl.setForeground(Color.gray);
      localeLbl.setForeground(Color.gray);
      linkLbl.setForeground(Color.gray);
      linkofflineLbl.setForeground(Color.gray);
      groupLbl.setForeground(Color.gray);
      helpfileLbl.setForeground(Color.gray);
      stylesheetfileLbl.setForeground(Color.gray);

      docletFld.setEnabled(false);
      docletpathFld.setEnabled(false);
      bootclasspathFld.setEnabled(false);
      extdirsFld.setEnabled(false);
      localeFld.setEnabled(false);
      linkFld.setEnabled(false);
      linkofflineFld.setEnabled(false);
      groupFld.setEnabled(false);
      helpfileFld.setEnabled(false);
      stylesheetfileFld.setEnabled(false);
    }
    else {
      useCbx.setEnabled(true);
      nohelpCbx.setEnabled(true);
      nonavbarCbx.setEnabled(true);
      nodepreCbx.setEnabled(true);
      splitCbx.setEnabled(true);

      docletLbl.setForeground(Color.black);
      docletpathLbl.setForeground(Color.black);
      bootclasspathLbl.setForeground(Color.black);
      extdirsLbl.setForeground(Color.black);
      localeLbl.setForeground(Color.black);
      linkLbl.setForeground(Color.black);
      linkofflineLbl.setForeground(Color.black);
      groupLbl.setForeground(Color.black);
      helpfileLbl.setForeground(Color.black);
      stylesheetfileLbl.setForeground(Color.black);

      docletFld.setEnabled(true);
      docletpathFld.setEnabled(true);
      bootclasspathFld.setEnabled(true);
      extdirsFld.setEnabled(true);
      localeFld.setEnabled(true);
      linkFld.setEnabled(true);
      linkofflineFld.setEnabled(true);
      groupFld.setEnabled(true);
      helpfileFld.setEnabled(true);
      stylesheetfileFld.setEnabled(true);
    }
  }
}
