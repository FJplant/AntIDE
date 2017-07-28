package com.antsoft.ant.property.projectproperty;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import com.antsoft.ant.manager.projectmanager.ProjectManager;
import com.antsoft.ant.manager.projectmanager.Project;
import com.antsoft.ant.util.BorderList;


/**
 * Project의 일반적 정보를 보여주는 panel
 *
 * @author kim sang kyun
 */
public class ProjectGeneralPanel extends JPanel{
  private JLabel nameLbl, pathLbl, generatedLbl, lastSavedLbl, sourceNumLbl;
  private JTextArea commentTa;

  public ProjectGeneralPanel() {    setLayout(new BorderLayout(0, 5));
    setBorder(BorderList.etchedBorder5);
    setForeground(Color.black);

    JLabel nameL = new JLabel("Project Name : ");
    nameL.setForeground(Color.black);
    nameLbl = new JLabel();
    JPanel nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    nameP.add(nameL);
    nameP.add(nameLbl);

    JLabel pathL = new JLabel("Project File Path : ");
    pathL.setForeground(Color.black);
    pathLbl = new JLabel();
    JPanel pathP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pathP.add(pathL);
    pathP.add(pathLbl);

    JLabel geneL = new JLabel("Created : ");
    geneL.setForeground(Color.black);
    generatedLbl = new JLabel();
    JPanel generatedP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    generatedP.add(geneL);
    generatedP.add(generatedLbl);

    JLabel lastSavedL = new JLabel("Last Saved : ");
    lastSavedL.setForeground(Color.black);
    lastSavedLbl = new JLabel();
    JPanel lastSavedP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    lastSavedP.add(lastSavedL);
    lastSavedP.add(lastSavedLbl);

    JLabel sourceL = new JLabel("Source Count : ");
    sourceL.setForeground(Color.black);
    sourceNumLbl = new JLabel();
    JPanel sourceP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceP.add(sourceL);
    sourceP.add(sourceNumLbl);

    JLabel commentLbl = new JLabel("Comment");
    commentLbl.setForeground(Color.black);
    JPanel commentLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    commentLblP.add(commentLbl);

    Box box = Box.createVerticalBox();
    box.add(nameP);
    box.add(pathP);
    box.add(generatedP);
    box.add(lastSavedP);
    box.add(sourceP);
    box.add(commentLblP);

    commentTa = new JTextArea();
    commentTa.setEditable(false);
    commentTa.setMargin(new Insets(6, 6, 6, 6));

    JPanel commentP = new JPanel(new BorderLayout());
    commentP.add(commentTa, BorderLayout.CENTER);
    commentP.setBorder(BorderList.lightLoweredBorder);

    JScrollPane pane = new JScrollPane(commentP);

    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(box, BorderLayout.NORTH);
    centerP.add(pane, BorderLayout.CENTER);

    add(centerP, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    setData();
  }

  private void setData(){
    //name setting
    Project currProj = ProjectManager.getCurrentProject();
    nameLbl.setText( currProj.getProjectName() );

    //path setting
    pathLbl.setText( currProj.getPath() );

    //created time setting
    generatedLbl.setText( currProj.getCreatedTime() );

    //last saved setting
    lastSavedLbl.setText( currProj.getLastSavedTime() );

    //source count setting
    Vector files = currProj.getFiles();
    if(files != null) sourceNumLbl.setText( files.size()+"" );
    else sourceNumLbl.setText("0");

    //comment setting
    if(currProj.getComment() != null) commentTa.setText( currProj.getComment() );
  }
}