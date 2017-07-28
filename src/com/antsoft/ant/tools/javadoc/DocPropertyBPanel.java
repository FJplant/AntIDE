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

public class DocPropertyBPanel extends JPanel {
  private JFrame parent;
  private JCheckBox classpathCbx, sourcepathCbx;
  private JButton classpathBtn, sourcepathBtn;
  private JTextField classpathFld, sourcepathFld, encodingFld, docencodingFld, jFld;
  public static IdeProperty property;

  public DocPropertyBPanel(JFrame parent) {
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

    ChangeListener cl = new ChangeEventHandler();
    ActionListener al = new ActionHandler();

    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);

    // title Panel
    JLabel titleLbl = new JLabel("JDK JavaDoc Common Option 1.1 and 1.2", JLabel.LEFT);
    titleLbl.setForeground(Color.black);

    JPanel titleP = new JPanel(new FlowLayout(FlowLayout.CENTER));
    titleP.add(titleLbl);
    titleP.setBorder(BorderList.raisedBorder);
    titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

    // classpath Panel
    JLabel classpathLbl = new JLabel(" -classpath <path>",JLabel.LEFT);
    classpathLbl.setForeground(Color.black);

    classpathCbx = new JCheckBox(" new", property.getNewclasspathDoc());
    classpathCbx.setForeground(Color.black);
    classpathCbx.addChangeListener(cl);

    JLabel notRec2Lbl = new JLabel("(Not Recommended)");
    notRec2Lbl.setForeground(Color.darkGray);

    JPanel classpathUpP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    classpathUpP.add(classpathLbl);
    classpathUpP.add(classpathCbx);
    classpathUpP.add(notRec2Lbl);

    classpathFld = new JTextField(20);
    classpathFld.setText(property.getClasspathDoc());
    classpathBtn = new JButton("Browse");
    classpathBtn.setActionCommand("CLASS");
    classpathBtn.addActionListener(al);

    JPanel classpathDownP = new JPanel(new FlowLayout());
    classpathDownP.add(classpathFld);
    classpathDownP.add(classpathBtn);

    // sourcepath Panel
    JLabel sourcepathLbl = new JLabel(" -sourcepath <path>",JLabel.LEFT);
    sourcepathLbl.setForeground(Color.black);

    sourcepathCbx = new JCheckBox(" new", property.getNewsourcepathDoc());
    sourcepathCbx.setForeground(Color.black);
    sourcepathCbx.addChangeListener(cl);

    JLabel notRecLbl = new JLabel("(Not Recommended)");
    notRecLbl.setForeground(Color.darkGray);

    JPanel sourcepathUpP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourcepathUpP.add(sourcepathLbl);
    sourcepathUpP.add(sourcepathCbx);
    sourcepathUpP.add(notRecLbl);

    sourcepathFld = new JTextField(20);
    sourcepathFld.setText(property.getSourcepathDoc());
    sourcepathBtn = new JButton("Browse");
    sourcepathBtn.setActionCommand("SOURCE");
    sourcepathBtn.addActionListener(al);

    JPanel sourcepathDownP = new JPanel(new FlowLayout());
    sourcepathDownP.add(sourcepathFld);
    sourcepathDownP.add(sourcepathBtn);


    // class path 와 source path를 묶어주는 panel
    JPanel pathP = new JPanel(new GridLayout(4,1));
    pathP.add(classpathUpP);
    pathP.add(classpathDownP);
    pathP.add(sourcepathUpP);
    pathP.add(sourcepathDownP);
    pathP.setBorder(BorderList.pathBorder);

    // encoding Panel
    JLabel encodingLbl = new JLabel(" -encoding <name>",JLabel.LEFT);
    encodingLbl.setForeground(Color.black);

    encodingFld = new JTextField(20);
    encodingFld.setText(property.getEncodingDoc());

    JPanel encodingP = new JPanel(new GridLayout(2,1));
    encodingP.add(encodingLbl);
    encodingP.add(encodingFld);

    // decencoding Panel
    JLabel docencodingLbl = new JLabel(" -docencoding <name>",JLabel.LEFT);
    docencodingLbl.setForeground(Color.black);
    docencodingFld = new JTextField(20);
    docencodingFld.setText(property.getDocencodingDoc());

    JPanel docencodingP = new JPanel(new GridLayout(2,1));
    docencodingP.add(docencodingLbl);
    docencodingP.add(docencodingFld);

    // j Panel
    JLabel jLbl = new JLabel(" -J <flag>",JLabel.LEFT);
    jLbl.setForeground(Color.black);
    jFld = new JTextField(20);
    jFld.setText(property.getJDoc());

    JPanel jP = new JPanel(new GridLayout(2,1));
    jP.add(jLbl);
    jP.add(jFld);

    JPanel otherP = new JPanel(new GridLayout(3,1));
    otherP.add(encodingP);
    otherP.add(docencodingP);
    otherP.add(jP);

    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderList.otherBorder);
    p.add(otherP,BorderLayout.CENTER);
    p.add(new JPanel(),BorderLayout.WEST);
    p.add(new JPanel(),BorderLayout.EAST);
    p.add(new JPanel(),BorderLayout.SOUTH);

    JPanel emptyP = new JPanel();
    emptyP.setPreferredSize(new Dimension(350, 200));


    Box box = Box.createVerticalBox();
    box.add(pathP);
    box.add(p);
    box.add(emptyP);

    setBorder(BorderList.etchedBorder5);
    add(box, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);

    setDisableClasspath();
    setDisableSourcepath();

  }

  // option의 값을 넘겨주는 함수들
  public boolean getNewclasspathDoc() {
    return classpathCbx.isSelected();
  }
  public boolean getNewsourcepathDoc() {
    return sourcepathCbx.isSelected();
  }
  public String getClasspathDoc() {
    return classpathFld.getText();
  }
  public String getSourcepathDoc() {
    return sourcepathFld.getText();
  }
  public String getEncodingDoc() {
    return encodingFld.getText();
  }
  public String getDocencodingDoc() {
    return docencodingFld.getText();
  }
  public String getJDoc() {
    return jFld.getText();
  }


  private void classPathBtnSelected() {

    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("jar");
    filter.addExtension("zip");

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);
    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setMultiSelectionEnabled(false);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Classpath");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("New classpath ");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      classpathFld.setText(f.getAbsolutePath());
    }
  }

  private void sourcePathBtnSelected() {

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Source path Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Source path Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      sourcepathFld.setText(f.getAbsolutePath());
    }
  }

  private void setDisableClasspath() {
    classpathFld.setEnabled(classpathCbx.isSelected());
    classpathFld.setEditable(classpathCbx.isSelected());
    classpathBtn.setEnabled(classpathCbx.isSelected());
  }

  private void setDisableSourcepath() {
    sourcepathFld.setEnabled(sourcepathCbx.isSelected());
    sourcepathFld.setEditable(sourcepathCbx.isSelected());
    sourcepathBtn.setEnabled(sourcepathCbx.isSelected());
  }


  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("CLASS")){
        classPathBtnSelected();
      }
      else if(cmd.equals("SOURCE")){
        sourcePathBtnSelected();
      }
    }
  }

  class ChangeEventHandler implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      if (e.getSource() == classpathCbx) {
        setDisableClasspath();
      }
      else if (e.getSource() == sourcepathCbx) {
        setDisableSourcepath();
      }
    }
  }


}
