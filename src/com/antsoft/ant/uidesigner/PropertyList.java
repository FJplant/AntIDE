package com.antsoft.ant.uidesigner;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
//import sun.beans.editors.*;


public class PropertyList extends JFrame implements MouseListener/*,PropertyChangeListener*/
{
  private JTable table;
  private DefaultTableModel model;
  private Component[] views;
  private String[] names;
  private PropertyEditor[] editors;
  //private MyTableCellEditor cellEditor = new MyTableCellEditor();
  private JList list;
  private Class cls;
  private LabelCellRenderer renderer;
  private TableColumn col;
  private JScrollPane sp;
  private int before;
  private int count;
  private String[] colNames = {"Property","Value"};
  private static ComponentList comList;

  public PropertyList(ComponentList comList){
    super("Property List");
    this.comList = comList;

    setSize(270,510);
    jInit();
    try  {
      jInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jInit(){

    //ù��° Į���� ������ ���ϵ���...
    model = new DefaultTableModel(colNames,0){
      public boolean isCellEditable(int row, int col){
        if(col == 0) return false;
        else return true;
      }
    };

    table = new JTable(model);

    sp = new JScrollPane(table);
    JPanel p = new JPanel();
    table.setBackground(p.getBackground());
    col = table.getColumn("Property");
    table.setPreferredSize(new Dimension(250,500));
    getContentPane().add(sp);
  }

  /**
   * getProDsc - ���õ� list�� propertyDescriptor�� ��´�
   */
  public PropertyDescriptor[] getProDsc(String selection){
    String pack = null;
    PropertyDescriptor[] pd = null;
    try{
      //System.out.println(selection);
      //BeanInfo class�� ��´�
      if(list.getSelectedIndex()<23){
        pack = "java.awt.";
      }else{
        pack = "javax.swing.";
      }

      cls = Class.forName(pack+selection);
      BeanInfo beanInfo = Introspector.getBeanInfo(cls);
      if(beanInfo == null)System.out.println("beanInfo is null");
      pd = beanInfo.getPropertyDescriptors();
      if(pd == null)System.out.println("pd is null");
      System.out.println("pd lenght-"+pd.length);
      for(int i=0; i<pd.length; i++){
        if(pd[i].getPropertyType() == null){
          System.out.println("type is null");
          continue;
        }
        System.out.println("type - "+(pd[i].getPropertyType()).getName());
        System.out.println("name - "+pd[i].getDisplayName());
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return pd;
  }

  /**
   *  makeEditorComponent - �� ������Ʈ�� editor�� ��� editor component�� �����.
   */
  public void makeEditorComponent(PropertyDescriptor[] pd){
    try{
      //�ʱ�ȭ
      count = 0;
      editors = null;
      views = null;
      names = null;

      editors = new PropertyEditor[pd.length];
      Object[] values = new Object[pd.length];
      views = new Component[pd.length];
      names = new String[pd.length];

      //property ����ŭ ������ ������
      for(int i=0; i<pd.length; i++){
        Class type = pd[i].getPropertyType();
        //property�� type�� ������ continue
        if(type == null){
          editors[i] = null;
          continue;
        }

        //editor�� ��´�
        Class editor = pd[i].getPropertyEditorClass();
        if(editor != null){
          editors[i] = (PropertyEditor)editor.newInstance();
          count++;
          System.out.println("c - "+count);
          //System.out.println("editor ��� �ϳ��� �ִ�.");

        //�����Ǵ� �����Ͱ� ���� ��� sun tool���� �����ϴ� default editor�� ã�´�
        }else{
          editors[i] = PropertyEditorManager.findEditor(type);
          //default editor�� ���� ���� ���� �����
          if(editors[i] == null){
           //System.out.println("skip");
           System.out.println(pd[i].getDisplayName());
           editors[i] = createEditor(type.getName());
           if(editors[i] == null) continue;

          //default editor�� �ִ� ���
          }else{
            count++;
            System.out.println("c - "+count);

            System.out.println(pd[i].getDisplayName());
            System.out.println("editor found");
          }
          //System.out.println("editor clss not exist!");
        }

        //proeprty�� �ʱⰪ�� �ִ� ��� �� ���� �����Ϳ� setting�Ѵ�
        Method read = pd[i].getReadMethod();
        Method write = pd[i].getWriteMethod();

        if((read == null) || (write == null)){
          System.out.println("read,write method is null");
          if(!type.getName().equals("int")){
            editors[i] = null;
            //editor�� �־ read/write method�� ������ ������ ī��Ʈ�� ���� �ٽ� ����.
            //���߿� ���̺��� �����ִ� ������ ������...
            count--;
            continue;
          }
        }else{
          Object[] args = {};
          if(cls == null)System.out.println("cls is null");
          try{
            values[i] = read.invoke(cls.newInstance(),args);
          }catch(Exception ex){
            values[i] = null;
          }
          if(values[i] == null){
            System.out.println("value is null");
            //continue;
          }else{
            editors[i].setValue(values[i]);
          }
        }
        //editors[i].addPropertyChangeListener(this);

        //������ editor component�� �����
        //case of Color, Font
        if(editors[i].isPaintable() && editors[i].supportsCustomEditor()){
          System.out.println("canvas");
          PaintablePropertyManager proManager = new PaintablePropertyManager(editors[i]);
          if(type.getName().equals("java.awt.Color")){
            views[i] = (Component)proManager.getColorComponent();
          }else if(type.getName().equals("java.awt.Font")){
            views[i] = (Component)proManager.getFontComponent();
          }else{
            continue;
          }
          //proeperty name�� ��´�
          names[i] = pd[i].getDisplayName();
        //case of selection(JComboBox)
        }else if(editors[i].getTags() != null){
          System.out.println("selection");
          views[i] = new PropertySelection(editors[i]);
          names[i] = pd[i].getDisplayName();
        //case of Text(JTextField)
        }else if(editors[i].getAsText() != null){
          System.out.println("textfield");
          views[i] = new PropertyText(editors[i],type.getName());
          names[i] = pd[i].getDisplayName();
        }else if(type.getName().equals("java.awt.Dimension")){
          views[i] = new PropertyDimension(editors[i],"width");
          names[i] = "width";
          i++;
          views[i] = new PropertyDimension(editors[i],"height");
          names[i] = "height";
        }else{
          System.out.println("invalid editor!");
          continue;
        }
        views[i].setFont(new Font("Arial",Font.PLAIN,11));
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /////////////////////// event ó�� ////////////////////////////////////
  public void mouseClicked(MouseEvent e){
    try{
      list = comList.getList();
      String selected = (String)list.getSelectedValue();
      System.out.println(selected);
      //�� component�� propertyDescriptor�� ��´�.
      PropertyDescriptor[] proDsc = getProDsc(selected);
      //property editor component�� �����
      makeEditorComponent(proDsc);

      //editor�� ���� property�� ���ܽ�Ų��
      int j=0;
      Component[] coms = new Component[count];
      PropertyEditor[] ed = new PropertyEditor[count];
      String[] n = new String[count];
      System.out.println(editors.length);
      for(int i=0; i<names.length; i++){
        if(names[i] != null){
          coms[j] = views[i];
          ed[j] = editors[i];
          n[j] = names[i];
          System.out.println("names-"+n[j]);
          j++;
        }
      }

      //table�� renderer�� editor�� �����Ѵ�
      int c = model.getRowCount();
      //���� table row�� �����
      table.setModel(model);
      model.setNumRows(count);
      for(int p=0; p<names.length; p++){
        model.setValueAt(n[p],p,0);
      }
      renderer = null;
      renderer = new LabelCellRenderer();
      col.setCellRenderer(renderer);

      col = table.getColumn("Value");
      col.setCellEditor(new MyTableCellEditor(coms));
      col.setCellRenderer(new MyCellRenderer(coms));
      sp.printComponents(sp.getGraphics());
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  createEditor - �����ϴ� �����Ͱ� ���� ��� ���� ���� �����Ѵ�
   *  @type - property type name
   */
  public PropertyEditor createEditor(String type){
    if(type.equals("java.awt.Dimension")){
      return new DimensionEditor();
    }else{
      return null;
    }
  }

  public void mouseEntered(MouseEvent e){
  }
  public void mouseReleased(MouseEvent e){
  }
  public void mouseExited(MouseEvent e){
  }
  public void mousePressed(MouseEvent e){
  }
}
