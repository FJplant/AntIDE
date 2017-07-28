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

    //첫번째 칼럼은 에디팅 못하도록...
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
   * getProDsc - 선택된 list의 propertyDescriptor를 얻는다
   */
  public PropertyDescriptor[] getProDsc(String selection){
    String pack = null;
    PropertyDescriptor[] pd = null;
    try{
      //System.out.println(selection);
      //BeanInfo class를 얻는다
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
   *  makeEditorComponent - 각 콤포넌트의 editor를 얻어 editor component를 만든다.
   */
  public void makeEditorComponent(PropertyDescriptor[] pd){
    try{
      //초기화
      count = 0;
      editors = null;
      views = null;
      names = null;

      editors = new PropertyEditor[pd.length];
      Object[] values = new Object[pd.length];
      views = new Component[pd.length];
      names = new String[pd.length];

      //property 수만큼 루프를 돌린다
      for(int i=0; i<pd.length; i++){
        Class type = pd[i].getPropertyType();
        //property의 type이 없으면 continue
        if(type == null){
          editors[i] = null;
          continue;
        }

        //editor를 얻는다
        Class editor = pd[i].getPropertyEditorClass();
        if(editor != null){
          editors[i] = (PropertyEditor)editor.newInstance();
          count++;
          System.out.println("c - "+count);
          //System.out.println("editor 적어도 하나는 있다.");

        //제공되는 에디터가 없는 경우 sun tool에서 제공하는 default editor를 찾는다
        }else{
          editors[i] = PropertyEditorManager.findEditor(type);
          //default editor도 없는 경우는 내가 만든다
          if(editors[i] == null){
           //System.out.println("skip");
           System.out.println(pd[i].getDisplayName());
           editors[i] = createEditor(type.getName());
           if(editors[i] == null) continue;

          //default editor가 있는 경우
          }else{
            count++;
            System.out.println("c - "+count);

            System.out.println(pd[i].getDisplayName());
            System.out.println("editor found");
          }
          //System.out.println("editor clss not exist!");
        }

        //proeprty의 초기값이 있는 경우 그 값을 에디터에 setting한다
        Method read = pd[i].getReadMethod();
        Method write = pd[i].getWriteMethod();

        if((read == null) || (write == null)){
          System.out.println("read,write method is null");
          if(!type.getName().equals("int")){
            editors[i] = null;
            //editor가 있어도 read/write method가 없으면 위에서 카운트한 것을 다시 뺀다.
            //나중에 테이블에서 보여주는 곳에서 뺄려고...
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

        //적당한 editor component를 만든다
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
          //proeperty name을 얻는다
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

  /////////////////////// event 처리 ////////////////////////////////////
  public void mouseClicked(MouseEvent e){
    try{
      list = comList.getList();
      String selected = (String)list.getSelectedValue();
      System.out.println(selected);
      //각 component의 propertyDescriptor를 얻는다.
      PropertyDescriptor[] proDsc = getProDsc(selected);
      //property editor component를 만든다
      makeEditorComponent(proDsc);

      //editor가 없는 property는 제외시킨다
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

      //table의 renderer와 editor를 지정한다
      int c = model.getRowCount();
      //이전 table row를 지운다
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
   *  createEditor - 제공하는 에디터가 없을 경우 내가 만들어서 리턴한다
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
