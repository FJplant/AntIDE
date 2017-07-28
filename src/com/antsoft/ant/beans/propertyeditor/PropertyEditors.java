/*
 *  bean을 보여주기 위한 자질구레한 일들을 하는 method를 모아놓은 클래스
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
  //bean.txt, path.txt 패스
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
   *  bean.txt에서 bean list를 읽어와서 list를 넘겨준다.
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
   *  path.txt에서 path를 읽어와서 넘겨준다.
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
   *  bean.txt에 새로추가된 bean 이름을 추가한다.
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
   *  path.txt에 새로추가된 path를 추가한다.
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
   *  bean.txt path.txt 파일에서 data를 삭제한다.
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
   *  jar file을 풀어서 bean class를 instantiate.
   */
  public void jarLoader(String jarPath,String jarFileName){
    FileInputStream fs;
    ZipInputStream zs;

    try{
      //jar file로 부터 빈 file 얻어오기
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
      //file들을 loading시킨다.
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

      //loading된 class들 중 빈 클래스를 찾아 instatiate한다.
      for(int i=0; i<entries.size(); i++){
        Class cls = (Class)entries.elementAt(i);
        String n = cls.getName();
        //n = n.substring(0,n.length()-6);
        //System.out.println("bean name-"+n);
        n = n.substring(n.lastIndexOf(".")+1);
        n = n.toLowerCase();
        System.out.println(n);
        //bean class이면 일을 하지...
        String filename = jarFileName.substring(0,1).toUpperCase()+jarFileName.substring(1);
        filename = filename.toLowerCase();
        if(n.equals(filename)){
          System.out.println("bean class다.");
          bean = Beans.instantiate(loader,cls.getName());
          System.out.println("instatiate했다.");
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
            //customizer가 있으면
            customizer = custom;
          }else if(flag)
            //propertyEditor가 존재하면
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
   *  BeanInfo 클래스로부터 PropertyDescriptor를 뽑아 해당 editor를 얻어 component를 만든다.
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
          System.out.println("editor 적어도 하나는 있다.");
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
      //path를 읽어온다.
      //int index = beanList.list.getSelectedIndex();
      //선택된 빈 load시키기
      System.out.println("selected index-"+selectedIndex);
      Vector classPathes = readPathFile();
      System.out.println("classPathes length-"+classPathes.size());
      String jarPath = (String)classPathes.elementAt(selectedIndex);
      System.out.println("!!!");
      String beanName = selectedValue;
      //jar file풀어서 bean instantiate
      System.out.println("jar-"+jarPath);
      jarLoader(jarPath,beanName);

      //property Sheet에 editor 뿌려주기
      if(customizer == null){
        propertyPanel = new PropertyPanel(views,labels);
      }else{
        propertyPanel = new PropertyPanel(customizer);
      }

      //property sheet에  editor 추가
      proSheet.p0.add(propertyPanel);
      proSheet.p0.revalidate();

      //tester에 visual bean 보여주기
      Component com = (Component)bean;
      if(com != null){
        com.addMouseListener(new BeanSelectedAdapter(tester));
        tester.getContentPane().setLayout(null);
        //component size,위치를 결정한다.
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

  ////////////////////////   event 처리   ////////////////////////////////////
  public void propertyChange(PropertyChangeEvent e){
    System.out.println("event 발생");
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
