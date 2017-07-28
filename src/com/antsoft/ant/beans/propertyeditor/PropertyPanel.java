package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import java.beans.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.reflect.*;

public class PropertyPanel extends JPanel{

  private Class customizer;
  private Component[] components;
  private JLabel[] labels;
  private String flag;
  //private BeanTester tester;

  public PropertyPanel(Class customizer){
    //this.tester = tester;
    this.customizer = customizer;
    flag = "c";
    setSize(300,360);
    setVisible(true);
    jInit();
  }

  public PropertyPanel(Component[] components,JLabel[] labels){
    //this.tester = tester;
    this.components = components;
    this.labels = labels;
    flag = "e";
    setSize(300,360);
    setVisible(true);
    jInit();
  }

  private void jInit(){
    removeAll();
    setLayout(new FlowLayout(FlowLayout.LEFT));

    if(flag.equals("e")){
      for(int i=0; i<components.length; i++){
        if((labels[i] != null)&&(components[i] != null)){
          labels[i].setPreferredSize(new Dimension(70,20));
          components[i].setSize(new Dimension(170,25));
          add(labels[i]);
          add(components[i]);
        }
      }
    }else if(flag.equals("c")){
      try{
        add((Component)customizer.newInstance());
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }
}
