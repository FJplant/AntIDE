/*
 *  bean�� �����ֱ� ���� ���������� �ϵ��� �ϴ� method�� ��Ƴ��� Ŭ����
 */

package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.lang.reflect.*;
import java.beans.*;
import javax.swing.*;

public class PropertyEditors implements PropertyChangeListener{
  File file1;
  File file2;
  //RandomAccessFile rf1;
  //RandomAccessFile rf2;
  //bean.txt, path.txt �н�
  String path;
  //Vector beanNames = new Vector();
  //Vector classPathes = new Vector();
  boolean flag = false;

  private BeanInfo beanInfo;
  public Object bean;
  private Class customizer;
  private PropertyDescriptor[] pd;
  private PropertyEditor[] editors;
  private Object[] values;
  private Component[] views;
  private JLabel[] labels;
  private BeanTester tester;
  private BeanList beanList;
  private PropertySheet proSheet;
  public int xGab = 5;
  public int yGab = 23;

  public PropertyEditors(BeanTester tester,String path){
    this.path = path;
    this.tester = tester;
    file1 = new File(path+"beans.txt");
    file2 = new File(path+"path.txt");
  }

  /**
   *  bean.txt���� bean list�� �о�ͼ� list�� �Ѱ��ش�.
   */
  public Vector readBeanFile(){
    Vector beanNames;
    try{
      beanNames = new Vector();
      RandomAccessFile rf1 = new RandomAccessFile(file1,"rw");

      String n= rf1.readLine();
      while(n!=null){
        n = n.substring(0,n.length());
        beanNames.addElement(n);
        n = rf1.readLine();
      }
      rf1.close();

    }catch(IOException ex){
      ex.printStackTrace();
      return null;
    }
    return beanNames;
  }

  /**
   *  path.txt���� path�� �о�ͼ� �Ѱ��ش�.
   */
  public Vector readPathFile(){
    Vector classPathes = new Vector();
    try{
      RandomAccessFile rf2 = new RandomAccessFile(file2,"rw");
      String n= rf2.readLine();
      while(n!=null){
        n = n.substring(0,n.length());
        classPathes.addElement(n);
        n = rf2.readLine();
      }
      rf2.close();
    }catch(IOException ex){
      ex.printStackTrace();
      return null;
    }
    return classPathes;
  }

  /**
   *  bean.txt�� �����߰��� bean �̸��� �߰��Ѵ�.
   */
  public void writeBeanFile(String beanName){
    try{
      //Vector beanNames = new Vector();
      RandomAccessFile rf1 = new RandomAccessFile(file1,"rw");

      rf1.seek(rf1.length());
      rf1.writeBytes(beanName+"\n");
      //beanNames.addElement((String)beanName);
      rf1.close();

    }catch(IOException ex){
      ex.printStackTrace();
    }
  }

  /**
   *  path.txt�� �����߰��� path�� �߰��Ѵ�.
   */
  public void writePathFile(String path){
    try{
      RandomAccessFile rf2 = new RandomAccessFile(file2,"rw");
      rf2.seek(rf2.length());
      rf2.writeBytes(path+"\n");
    }catch(IOException ex){
      ex.printStackTrace();
    }
  }

  /**
   *  bean.txt path.txt ���Ͽ��� data�� �����Ѵ�.
   */
  public void updateFiles(int index){
      //FileOutputStream fs1;
      //FileOutputStream fs2;
    try{
      //File file3 = new File(path+"beans.txt");
      //File file4 = new File(path+"path.txt");

      Vector beanNames = readBeanFile();
      Vector classPathes = readPathFile();
      beanNames.removeElementAt(index);
      classPathes.removeElementAt(index);
      System.out.println("size-"+beanNames.size());
      if(beanNames.size() == 0){
        file1.delete();
        file2.delete();
      }else{
        FileOutputStream fs1 = new FileOutputStream(file1);
        FileOutputStream fs2 = new FileOutputStream(file2);
        //RandomAccessFile rf1 = new RandomAccessFile(file1,"rw");
        //RandomAccessFile rf2 = new RandomAccessFile(file2,"rw");
        for(int i=0; i<beanNames.size(); i++){
          if(beanNames.elementAt(i) != null){
            String s = (String)beanNames.elementAt(i)+"\n";
            System.out.println("bean-"+s);
            fs1.write(s.getBytes());
          }
          if(classPathes.elementAt(i) != null){
            String s = (String)classPathes.elementAt(i)+"\n";
            System.out.println("path-"+s);
            fs2.write(s.getBytes());
          }
          fs1.close();
          fs2.close();
        }
      }
    }catch(IOException ex){
      ex.printStackTrace();
    }

  }

  /**
   *  jar file�� Ǯ� bean class�� instantiate.
   */
  public void jarLoader(String jarPath,String jarFileName){
    FileInputStream fs;
    ZipInputStream zs;

    try{
      //jar file�� ���� �� file ������
      System.out.println("jar file name-"+jarPath+jarFileName);
      File file = new File(jarPath+jarFileName+".jar");
      fs = new FileInputStream(file);
      zs = new ZipInputStream(fs);

      //class loading
      FileClassLoader loader = new FileClassLoader();
      loader.addPath(file.getAbsolutePath());

      ZipEntry entry = null;
      Vector entries = new Vector();
      Vector names = new Vector();

      entry = zs.getNextEntry();
      //file���� loading��Ų��.
      while(entry != null){
        String name = entry.getName();
        name = name.substring(0,name.length()-6);
        name = name.replace('/','.');
        System.out.println("name-"+name);
        Class cls = loader.loadClass(name);
        if(cls != null){
          entries.addElement(cls);
          //names.addElement(name);
        }else{
          System.out.println("not loading");
        }
        entry = zs.getNextEntry();
      }

      //loading�� class�� �� �� Ŭ������ ã�� instatiate�Ѵ�.
      for(int i=0; i<entries.size(); i++){
        Class cls = (Class)entries.elementAt(i);
        String n = cls.getName();
        //n = n.substring(0,n.length()-6);
        //System.out.println("bean name-"+n);
        n = n.substring(n.lastIndexOf(".")+1);
        n = n.toLowerCase();
        System.out.println(n);
        //bean class�̸� ���� ����...
        String filename = jarFileName.substring(0,1).toUpperCase()+jarFileName.substring(1);
        filename = filename.toLowerCase();
        if(n.equals(filename)){
          System.out.println("bean class��.");
          bean = Beans.instantiate(loader,cls.getName());
          System.out.println("instatiate�ߴ�.");
          beanInfo = Introspector.getBeanInfo(cls);
          if(beanInfo == null){
            System.out.println("beanInfo class is null!");
            return;
          }
          BeanDescriptor beanDsc = beanInfo.getBeanDescriptor();
          Class custom = beanDsc.getCustomizerClass();
          pd = beanInfo.getPropertyDescriptors();
          if((pd == null) && (custom == null)){
            System.out.println("PropertyDescriptor,Customizer is null!");
            return;
          }
          for(int j=0; j<pd.length; j++){
            Class editor = pd[j].getPropertyEditorClass();
            if(editor != null) flag = true;
          }

          if(custom != null){
            //customizer�� ������
            customizer = custom;
          }else if(flag)
            //propertyEditor�� �����ϸ�
            makeEditor();
        }
        continue;
      }
      fs.close();
      zs.close();
    }catch(Exception ex){
      ex.printStackTrace();
    }

  }

  /**
   *  BeanInfo Ŭ�����κ��� PropertyDescriptor�� �̾� �ش� editor�� ��� component�� �����.
   */
  public void makeEditor(){
    editors = new PropertyEditor[pd.length];
    values = new Object[pd.length];
    views = new Component[pd.length];
    labels = new JLabel[pd.length];

    for(int i=0; i<pd.length; i++){
      String name = pd[i].getDisplayName();
      labels[i] = new JLabel(name);
      Class type = pd[i].getPropertyType();
      Class editor = pd[i].getPropertyEditorClass();
      try{
        if(editor != null){
          editors[i] = (PropertyEditor)editor.newInstance();
          System.out.println("editor ��� �ϳ��� �ִ�.");
        }else{
          System.out.println("editor not exist!");
          continue;
        }
      }catch(IllegalAccessException ex){
        ex.printStackTrace();
      }catch(InstantiationException ex){
        ex.printStackTrace();
      }
      if(editors[i] == null)
        editors[i] = PropertyEditorManager.findEditor(type);

      Method read = pd[i].getReadMethod();
      Method write = pd[i].getWriteMethod();
      if((read == null) || (write == null)) continue;

      try{
        Object[] args = {};
        values[i] = read.invoke(bean,args);
        editors[i].setValue(values[i]);
        editors[i].addPropertyChangeListener(this);

        if(editors[i].isPaintable() && editors[i].supportsCustomEditor()){
          views[i] = new PropertyCanvas(tester,editors[i]);
        }else if(editors[i].getTags() != null){
          views[i] = new PropertySelection(editors[i]);
        }else if(editors[i].getAsText() != null){
          views[i] = new PropertyText(editors[i]);
        }else{
          System.out.println("invalid editor!");
          continue;
        }
      }catch(InvocationTargetException ex){
        ex.printStackTrace();
      }catch(IllegalAccessException ex){
        ex.printStackTrace();
      }
    }
  }

  public void showBean(PropertySheet proSheet,int selectedIndex,String selectedValue,Point p){
    try{
      PropertyPanel propertyPanel;
      proSheet.p0.removeAll();
      //path�� �о�´�.
      //int index = beanList.list.getSelectedIndex();
      //���õ� �� load��Ű��
      System.out.println("selected index-"+selectedIndex);
      Vector classPathes = readPathFile();
      System.out.println("classPathes length-"+classPathes.size());
      String jarPath = (String)classPathes.elementAt(selectedIndex);
      System.out.println("!!!");
      String beanName = selectedValue;
      //jar fileǮ� bean instantiate
      System.out.println("jar-"+jarPath);
      jarLoader(jarPath,beanName);

      //property Sheet�� editor �ѷ��ֱ�
      if(customizer == null){
        propertyPanel = new PropertyPanel(views,labels);
      }else{
        propertyPanel = new PropertyPanel(customizer);
      }

      //property sheet��  editor �߰�
      proSheet.p0.add(propertyPanel);
      proSheet.p0.revalidate();

      //tester�� visual bean �����ֱ�
      Component com = (Component)bean;
      if(com != null){
        com.addMouseListener(new BeanSelectedAdapter(tester));
        tester.getContentPane().setLayout(null);
        //component size,��ġ�� �����Ѵ�.
        int w = 0;
        int h = 0;
        if(com.getSize().width == 0) w = 50;
        else w = com.getSize().width;
        if(com.getSize().height == 0) h = 50;
        else h = com.getSize().height;
        com.setBounds(p.x-xGab,p.y-yGab, w, h);
        tester.getContentPane().add(com);
        tester.x = p.x-xGab;
        tester.y = p.y-yGab;
        tester.width = w;
        tester.height = h;
        JPanel glass = (JPanel)tester.getGlassPane();
        glass.setLayout(null);
        glass.setVisible(true);
        glass.add(com);
        tester.getContentPane().repaint();
        //tester.getGlassPane().repaint();
        //glass.setLayout(null);
        //glass.setVisible(true);

      }else{
        System.out.println("Error! Bean object is null");
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  ////////////////////////   event ó��   ////////////////////////////////////
  public void propertyChange(PropertyChangeEvent e){
    System.out.println("event �߻�");
    PropertyEditor editor = (PropertyEditor)e.getSource();
    for(int i=0; i<editors.length; i++){
      try{
        if(editors[i] == editor){
          values[i] = editor.getValue();
          Method write = pd[i].getWriteMethod();
          Object[] args = {values[i]};
          write.invoke(bean,args);
          views[i].repaint();
        }
      }catch(InvocationTargetException ex){
        ex.printStackTrace();
      }catch(IllegalAccessException ex){
        ex.printStackTrace();
      }
    }
  }
}
