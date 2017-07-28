package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class BeanWizard{

    String currentpath = null;
    BeanDialog beanDlg;

    File[] file = new File[4];

    FileOutputStream fs1;
    FileOutputStream fs2;
    FileOutputStream fs3;
    FileOutputStream fs4;

    BeanClassPanel classPanel;
    EventOptionPanel eventPanel;
    PropertyPanel proPanel;
    FireEventPanel firePanel;
    EtcPanel etcPanel;

    String date;
    String[] eventID;
    String[] eventFName;
    String eventName;
    String listenerName;
    String event;   //eventName에서 "Event"를 뺀 string

    //property variable
    String[] name;
    String[] mName;
    String[] type;
    String[] read;
    String[] write;
    String[] bound;
    String[] constrained;

    int count = 0; //fire하는 event 갯수

  public BeanWizard( Frame parent, String path ){
    currentpath = path;

    beanDlg = new BeanDialog( parent, "Bean Wizard", true );


    beanDlg.setVisible( true );
    if (beanDlg.isOk()) {
      initData();
      generateBeanClassCode();
      if( classPanel.createEvent.isSelected() ){
        generateEventObjectCode();
        generateEventListenerCode();
      }
      if( etcPanel.beanInfo.isSelected() )
        generateBeanInfoClassCode();
    }
  }

  private void initData(){
    //date
    Calendar cal = Calendar.getInstance();
    int year = cal.get( Calendar.YEAR );
    int month = cal.get( Calendar.MONTH ) + 1;
    int day = cal.get( Calendar.DATE );
    date = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(day);

    classPanel = beanDlg.classPanel;
    eventPanel = beanDlg.eventPanel;
    proPanel = beanDlg.propertyPanel;
    firePanel = beanDlg.fireEventPanel;
    etcPanel = beanDlg.etcPanel;

    eventName = classPanel.eventName.getText();
    listenerName = classPanel.listenerName.getText();
    eventID = new String[eventPanel.table.getRowCount()];
    eventFName = new String[eventPanel.table.getRowCount()];

    int num = proPanel.table.getRowCount();
    name = new String[num];
    mName = new String[num];
    type = new String[num];
    read = new String[num];
    write = new String[num];
    bound = new String[num];
    constrained = new String[num];

    event = eventName.substring(0,eventName.length()-5);

    //이벤트 아이디와 함수 이름 얻기
    for( int i=0; i< eventPanel.table.getRowCount(); i++){
      eventFName[i] = (String)eventPanel.model.getValueAt(i,0);
      eventID[i] = (String)eventPanel.model.getValueAt(i,1);
    }
    eventID[eventPanel.table.getRowCount()-1] = beanDlg.lastValue;
    System.out.println(beanDlg.lastValue);
  }

  void generateBeanClassCode(){
    try{
      File parent = new File( currentpath + File.separator + classPanel.packName.getText().replace('.', File.separator.charAt(0)) );
        if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	        file[0] = new File( parent, classPanel.className.getText() + ".java" );
  	      fs1 = new FileOutputStream( file[0] );
        }

      //add/removePropertyChangeListener method를 한번만 만들기 위한 플래그
      boolean flag1 = false;
      //add/removeVetoableChangeListener method를 한번만 만들기 위한 플래그
      boolean flag2 = false;
      //PropertyChangeSupport 객체를 한번만 만들기 위한 플래그
      boolean flag3 = false;
      //VetoableChangeSupport 객체를 한번만 만들기 위한 플래그
      boolean flag4 = false;

      writer1("/*\n");
  	  writer1(" *\tpackage : " + classPanel.packName.getText() + "\n");
  		writer1(" *\tsource  : " + classPanel.className.getText() + ".java\n");
			writer1(" *\tdate    : " + date + "\n");
			writer1(" */\n\n");

    	writer1("package " + classPanel.packName.getText() + ";\n\n");

			writer1("import java.awt.*;\n");
			writer1("import java.awt.event.*;\n");
      writer1("import java.util.*;\n");
 	    writer1("import javax.swing.*;\n");
      writer1("import java.beans.*;\n\n");

   	  writer1("public class " + classPanel.className.getText() + "\n");
      writer1("{\n");

      //property 출력
      writer1("\t//property\n");
      for( int i=0; i<proPanel.table.getRowCount(); i++ ){
        name[i] = (String)proPanel.model.getValueAt(i,0);
        type[i] = (String)proPanel.model.getValueAt(i,1);
        read[i] = (String)proPanel.model.getValueAt(i,2);
        write[i] = (String)proPanel.model.getValueAt(i,3);
        bound[i] = (String)proPanel.model.getValueAt(i,4);
        constrained[i] = (String)proPanel.model.getValueAt(i,5);
        writer1("\t" + type[i] + " " + name[i] + ";\n");
      }
      writer1("\t\n");

      //method출력
      writer1("\t//methods\n");
      for( int j=0; j<proPanel.table.getRowCount(); j++ ){
        mName[j] = name[j].substring(0, 1).toUpperCase() + name[j].substring(1);
        if( read[j].equals("V") ){
          writer1("\tpublic " + type[j] + " get" + mName[j] +"(){\n");
          writer1("\t\treturn " + name[j] + ";\n");
          writer1("\t}\n\n");
        }
        if( write[j].equals("V") ){
          writer1("\tpublic void set" + mName[j] + "(" + type[j] + " " + name[j] + "){\n");
          writer1("\t\tthis." + name[j] + " = " + name[j] + ";\n");
          writer1("\t}\n\n");
        }
      }

      for( int j=0; j<proPanel.table.getRowCount(); j++ ){
        if( !flag1 ){
          if( bound[j].equals("V") ){
            writer1("\tpublic void addPropertyChangeListener(PropertyChangeListener l){\n");
            writer1("\t\tif(changes == null)\n");
            writer1("\t\t\tchanges = new PropertyChangeSupport(this);\n");
            writer1("\t\tchanges.addPropertyChangeListener(l);\n");
            writer1("\t}\n\n");

            writer1("\tpublic void removePropertyChangeListener(PropertyChangeListener l){\n");
            writer1("\t\tif(changes != null)\n");
            writer1("\t\t\tchanges.removePropertyChangeListener(l);\n");
            writer1("\t}\n\n");

            flag1 = true;
            flag3 = true;

          }
        }else{
          flag3 = false;
        }
        if( !flag2 ){
          if( constrained[j].equals("V") ){
            writer1("\tpublic void addVetoableChangeListener(VetoableChangeListener l){\n");
            writer1("\t\tif (veto == null)\n");
            writer1("\t\t\tveto = new VetoableChangeSupport(this);\n");
            writer1("\t\tveto.addVetoableChangeListener(l);\n");
            writer1("\t}\n\n");

            writer1("\tpublic void removeVetoableChangeListener(VetoableChangeListener l){\n");
            writer1("\t\tif (veto != null)\n");
            writer1("\t\t\tveto.removeVetoableChangeListener(l);\n");
            writer1("\t}\n\n");

            flag2 = true;
            flag4 = true;
          }
        }else{
          flag4 = false;
        }
      }

      if( flag1 )
        writer1("\tprivate PropertyChangeSupport changes;\n");
      if( flag2 )
        writer1("\tprivate VetoableChangeSupport veto;\n");
      writer1("\n");

      String vectorName = listenerName.substring(0,1).toLowerCase() + listenerName.substring(1);

      //custome event를 위한 벡터
      if( classPanel.createEvent.isSelected() )
        writer1("\tprivate transient Vector " + vectorName + "s;\n");

      if( firePanel.action.isSelected() )
        writer1("\tprivate transient Vector actionListeners;\n");
      if( firePanel.adjustment.isSelected() )
        writer1("\tprivate transient Vector adjustmentListeners;\n");
      if( firePanel.component.isSelected() )
        writer1("\tprivate transient Vector componentListeners;\n");
      if( firePanel.container.isSelected() )
        writer1("\tprivate transient Vector containerListeners;\n");
      if( firePanel.focus.isSelected() )
        writer1("\tprivate transient Vector focusListeners;\n");
      if( firePanel.item.isSelected() )
        writer1("\tprivate transient Vector itemListeners;\n");
      if( firePanel.key.isSelected() )
        writer1("\tprivate transient Vector keyListeners;\n");
      if( firePanel.mouse.isSelected() )
        writer1("\tprivate transient Vector mouseListeners;\n");
      if( firePanel.mouseMotion.isSelected() )
        writer1("\tprivate transient Vector mouseMotionListeners;\n");
      if( firePanel.text.isSelected() )
        writer1("\tprivate transient Vector textListeners;\n");
      if( firePanel.window.isSelected() )
        writer1("\tprivate transient Vector windowListeners;\n");
      if( firePanel.ancestor.isSelected() )
        writer1("\tprivate transient Vector ancestorListeners;\n");
      if( firePanel.caret.isSelected() )
        writer1("\tprivate transient Vector caretListeners;\n");
      if( firePanel.cellEditor.isSelected() )
        writer1("\tprivate transient Vector cellEditorListeners;\n");
      if( firePanel.change.isSelected() )
        writer1("\tprivate transient Vector changeListeners;\n");
      if( firePanel.document.isSelected() )
        writer1("\tprivate transient Vector documentListeners;\n");
      if( firePanel.hyperlink.isSelected() )
        writer1("\tprivate transient Vector hyperlinkListeners;\n");
      if( firePanel.internalF.isSelected() )
        writer1("\tprivate transient Vector internalFrameListeners;\n");
      if( firePanel.listData.isSelected() )
        writer1("\tprivate transient Vector listDataListeners;\n");
      if( firePanel.listS.isSelected() )
        writer1("\tprivate transient Vector listSelectionListeners;\n");
      if( firePanel.menu.isSelected() )
        writer1("\tprivate transient Vector menuListeners;\n");
      if( firePanel.popupMenu.isSelected() )
        writer1("\tprivate transient Vector popupMenuListeners;\n");
      if( firePanel.tableCM.isSelected() )
        writer1("\tprivate transient Vector tableColumnModelListeners;\n");
      if( firePanel.tableM.isSelected() )
        writer1("\tprivate transient Vector tableModelListeners;\n");
      if( firePanel.treeE.isSelected() )
        writer1("\tprivate transient Vector treeExpansionListeners;\n");
      if( firePanel.treeM.isSelected() )
        writer1("\tprivate transient Vector treeModelListeners;\n");
      if( firePanel.treeS.isSelected() )
        writer1("\tprivate transient Vector treeSelectionListeners;\n");
      if( firePanel.undo.isSelected() )
        writer1("\tprivate transient Vector undoableEditListeners;\n");
      writer1("\n");

      //event method coding
      if( classPanel.createEvent.isSelected() ){
        String lEvent = event.toLowerCase().substring(0,1) + event.substring(1);

        /*writer1("\tpublic synchronized void add" + event + "Listener(" + event + "Listener l){\n");
        writer1("\t\tif(" + lEvent + "Listeners == null)\n");
        writer1("\t\t\t" + lEvent + "Listeners = new Vector();\n");
        writer1("\t\t" + lEvent + "Listeners.addElement(l);\n");
        writer1("\t}\n\n");

        writer1("\tpublic synchronized void remove" + event + "Listener(" + event + "Listener l){\n");
        writer1("\t\tsuper.remove" + lEvent + "Listener(l);\n");
        writer1("\t\tif(" + lEvent + "Listeners != null && " + lEvent + "Listeners.contains(l))\n");
        writer1("\t\t\t" + lEvent + "Listeners.removeElement(l);\n");
        writer1("\t}\n\n");*/
        addMethodForm(lEvent,event);
        removeMethodForm(lEvent,event);

        for(int p=0; p<eventFName.length; p++){
          String fName = eventFName[p].toUpperCase().substring(0,1) + eventFName[p].substring(1);

          writer1("\tpublic void fire" + fName + "(" + eventName + " e){\n");
          writer1("\t\tprocess" + listenerName + "(new " + eventName + "(this," + eventName + "." + eventID[p] + "));\n");
          writer1("\t}\n\n");
          //fireMethodForm(lEvent,event,eventFName[p],fName);
        }
        writer1("\tprotected void process" + listenerName + "(" + eventName + " e){\n");
        writer1("\t\tswitch( e.getID() ){\n");
        for( int i=0; i<eventPanel.model.getRowCount(); i++){
          writer1("\t\tcase " + eventName + "." + eventID[i] + ":\n");
          writer1("\t\t\tfor(int i=0; i<" + vectorName + "s.size(); i++)\n");
          writer1("\t\t\t\t((" + listenerName + ")" + vectorName + "s.elementAt(i))." + eventFName[i] + "(e);\n");
          writer1("\t\t\tbreak;\n");

        }
        writer1("\t\t}\n");
        writer1("\t}\n");
      }
      if( firePanel.action.isSelected() ){
        addMethodForm("action","Action");
        removeMethodForm("action","Action");
        fireMethodForm("action","Action","actionPerformed","ActionPerformed");
      }
      if( firePanel.adjustment.isSelected() ){
        addMethodForm("adjustment","Adjustment");
        removeMethodForm("adjustment","Adjustment");
        fireMethodForm("adjustment","Adjustment","adjustmentValueChanged","AdjustmentValueChanged");
      }
      if( firePanel.component.isSelected() ){
        addMethodForm("component","component");
        removeMethodForm("component","component");
        fireMethodForm("component","component","componentResized","ComponentResized");
        fireMethodForm("component","component","componentMoved","ComponentMoved");
        fireMethodForm("component","component","componentShown","ComponentShown");
        fireMethodForm("component","component","componentHidden","ComponentHidden");
      }
      if( firePanel.container.isSelected() ){
        addMethodForm("container","Container");
        removeMethodForm("container","Container");
        fireMethodForm("container","Container","added","Added");
        fireMethodForm("container","Container","removed","Removed");
      }
      if( firePanel.focus.isSelected() ){
        addMethodForm("focus","Focus");
        removeMethodForm("focus","Focus");
        fireMethodForm("focus","Focus","focusGrained","FocusGrained");
        fireMethodForm("focus","Focus","focusLost","FocusLost");
      }
      if( firePanel.item.isSelected() ){
        addMethodForm("item","Item");
        removeMethodForm("item","Item");
        fireMethodForm("item","Item","itemStateChanged","ItemStateChanged");
      }
      if( firePanel.key.isSelected() ){
        addMethodForm("key","Key");
        removeMethodForm("key","Key");
        fireMethodForm("key","Key","keyPressed","KeyPressed");
        fireMethodForm("key","Key","keyReleased","KeyReleased");
        fireMethodForm("key","Key","keyTyped","KeyTyped");
      }
      if( firePanel.mouse.isSelected() ){
        addMethodForm("mouse","Mouse");
        removeMethodForm("mouse","Mouse");
        fireMethodForm("mouse","Mouse","mouseClicked","MouseClicked");
        fireMethodForm("mouse","Mouse","mousePressed","MousePressed");
        fireMethodForm("mouse","Mouse","mouseReleased","MouseReleased");
        fireMethodForm("mouse","Mouse","mouseEntered","MouseEntered");
        fireMethodForm("mouse","Mouse","mouseExited","MouseExited");
      }
      if( firePanel.mouseMotion.isSelected() ){
        addMethodForm("mouseMotion","MouseMotion");
        removeMethodForm("mouseMotion","MouseMotion");
        fireMethodForm("mouseMotion","MouseMotion","mouseDragged","MouseDragged");
        fireMethodForm("mouseMotion","MouseMotion","mouseMoved","MouseMoved");
      }
      if( firePanel.text.isSelected() ){
        addMethodForm("text","Text");
        removeMethodForm("text","Text");
        fireMethodForm("text","Text","textValueChanged","TextValueChanged");
      }
      if( firePanel.window.isSelected() ){
        addMethodForm("ancestor","Ancestor");
        removeMethodForm("ancestor","Ancestor");
        fireMethodForm("ancestor","Ancestor","windowOpened","WindowOpened");
        fireMethodForm("ancestor","Ancestor","windowClosing","WindowClosing");
        fireMethodForm("ancestor","Ancestor","windowClosed","WindowClosed");
        fireMethodForm("ancestor","Ancestor","windowIconified","WindowIconified");
        fireMethodForm("ancestor","Ancestor","windowDeiconified","WindowDeiconified");
        fireMethodForm("ancestor","Ancestor","windowActivated","WindowActivated");
        fireMethodForm("ancestor","Ancestor","windowDeactivated","WindowDeactivated");
      }
      if( firePanel.ancestor.isSelected() ){
        addMethodForm("ancestor","Ancestor");
        removeMethodForm("ancestor","Ancestor");
        fireMethodForm("ancestor","Ancestor","ancestorAdded","AncestorAdded");
        fireMethodForm("ancestor","Ancestor","ancestorRemoved","AncestorRemoved");
        fireMethodForm("ancestor","Ancestor","ancestorMoved","AncestorMoved");
      }
      if( firePanel.caret.isSelected() ){
        addMethodForm("caret","Caret");
        removeMethodForm("caret","Caret");
        fireMethodForm("caret","Caret","caretUpdate","CaretUpdate");
      }
      if( firePanel.cellEditor.isSelected() ){
        addMethodForm("cellEditor","CellEditor");
        removeMethodForm("cellEditor","CellEditor");
        fireMethodForm("cellEditor","CellEditor","editingStopped","EditingStopped");
        fireMethodForm("cellEditor","CellEditor","editingCanceled","EditingCanceled");
      }
      if( firePanel.change.isSelected() ){
        addMethodForm("change","Change");
        removeMethodForm("change","Change");
        fireMethodForm("change","Change","stateChanged","StateChanged");
      }
      if( firePanel.document.isSelected() ){
        addMethodForm("document","Document");
        removeMethodForm("document","Document");
        fireMethodForm("document","Document","insertUpdate","InsertUpdate");
        fireMethodForm("document","Document","removeUpdate","RemoveUpdate");
        fireMethodForm("document","Document","changedUpdate","ChangedUpdate");
      }
      if( firePanel.hyperlink.isSelected() ){
        addMethodForm("hyperlink","Hyperlink");
        removeMethodForm("hyperlink","Hyperlink");
        fireMethodForm("hyperlink","Hyperlink","hyperlinkUpdate","HyperlinkUpdate");
      }
      if( firePanel.internalF.isSelected() ){
        addMethodForm("internalFrame","InternalFrame");
        removeMethodForm("internalFrame","InternalFrame");
        fireMethodForm("internalFrame","InternalFrame","internalFrameOpened","InternalFrameOpened");
        fireMethodForm("internalFrame","InternalFrame","internalFrameClosing","InternalFrameClosing");
        fireMethodForm("internalFrame","InternalFrame","internalFrameClosed","InternalFrameClosed");
        fireMethodForm("internalFrame","InternalFrame","internalFrameIconified","InternalFrameIconified");
        fireMethodForm("internalFrame","InternalFrame","internalFrameDeiconified","InternalFrameDeiconified");
        fireMethodForm("internalFrame","InternalFrame","internalFrameActivated","InternalFrameActivated");
        fireMethodForm("internalFrame","InternalFrame","internalFrameDeactivated","InternalFrameDeactivated");
      }
      if( firePanel.listData.isSelected() ){
        addMethodForm("listData","ListData");
        removeMethodForm("listData","ListData");
        fireMethodForm("listData","ListData","intervalAdded","IntervalAdded");
        fireMethodForm("listData","ListData","intervalRemoved","IntervalRemoved");
        fireMethodForm("listData","ListData","intervalContentsChanged","IntervalContentsChanged");
      }
      if( firePanel.listS.isSelected() ){
        addMethodForm("listSelection","ListSelection");
        removeMethodForm("listSelection","ListSelection");
        fireMethodForm("listSelection","ListSelection","valueChanged","ValueChanged");
      }
      if( firePanel.menu.isSelected() ){
        addMethodForm("menu","Menu");
        removeMethodForm("menu","Menu");
        fireMethodForm("menu","Menu","menuSelected","MenuSelected");
        fireMethodForm("menu","Menu","menuDeselected","MenuDeselected");
        fireMethodForm("menu","Menu","menuCanceled","MenuCanceled");
      }
      if( firePanel.popupMenu.isSelected() ){
        addMethodForm("popupMenu","PopupMenu");
        removeMethodForm("popupMenu","PopupMenu");
        fireMethodForm("popupMenu","PopupMenu","popupMenuWillBecomeVisible","PopupMenuWillBecomeVisible");
        fireMethodForm("popupMenu","PopupMenu","popupMenuCanceled","PopupMenuCanceled");
      }
      if( firePanel.tableCM.isSelected() ){
        addMethodForm("tableColumnModel","TableColumnModel");
        removeMethodForm("tableColumnModel","TableColumnModel");
        fireMethodForm("tableColumnModel","TableColumnModel","columnAdded","ColumnAdded");
        fireMethodForm("tableColumnModel","TableColumnModel","columnRemoved","ColumnRemoved");
        fireMethodForm("tableColumnModel","TableColumnModel","columnMoved","ColumnMoved");
        fireMethodForm("tableColumnModel","TableColumnModel","columnMarginChanged","ColumnMarginChanged");
        fireMethodForm("tableColumnModel","TableColumnModel","columnSelectionChanged","ColumnSelectionChanged");
      }
      if( firePanel.tableM.isSelected() ){
        addMethodForm("tableModel","TableModel");
        removeMethodForm("tableModel","TableModel");
        fireMethodForm("tableModel","TableModel","tableChanged","TableChanged");
      }
      if( firePanel.treeE.isSelected() ){
        addMethodForm("treeExpansion","TreeExpansion");
        removeMethodForm("treeExpansion","TreeExpansion");
        fireMethodForm("treeExpansion","TreeExpansion","treeExpanded","TreeExpanded");
        fireMethodForm("treeExpansion","TreeExpansion","treeCollapsed","TreeCollapsed");
      }
      if( firePanel.treeM.isSelected() ){
        addMethodForm("treeModel","TreeModel");
        removeMethodForm("treeModel","TreeModel");
        fireMethodForm("treeModel","TreeModel","treeNodesChanged","TreeNodesChanged");
        fireMethodForm("treeModel","TreeModel","treeNodesInserted","TreeNodesInserted");
        fireMethodForm("treeModel","TreeModel","treeNodesRemoved","TreeNodesRemoved");
        fireMethodForm("treeModel","TreeModel","treeStructureChanged","TreeStructureChanged");
      }
      if( firePanel.treeS.isSelected() ){
        addMethodForm("treeSelection","TreeSelection");
        removeMethodForm("treeSelection","TreeSelection");
        fireMethodForm("treeSelection","TreeSelection","valueChanged","ValueChanged");
      }
      if( firePanel.undo.isSelected() ){
        addMethodForm("undoableEdit","UndoableEdit");
        removeMethodForm("undoableEdit","UndoableEdit");
        fireMethodForm("undoableEdit","UndoableEdit","undoableEditHappened","UndoableEditHappened");
      }

      //overriden method
      if( etcPanel.toString.isSelected() ){
        writer1("\tpublic String toString(){\n\n");
        writer1("\t\tString s = null;\n");
        writer1("\t\treturn s;\n");
        writer1("\t}\n\n");
      }
      if( etcPanel.equals.isSelected() ){
        writer1("\tpublic boolean equals(Object obj){\n\n");
        writer1("\t\tboolean b = false;\n");
        writer1("\t\treturn b;\n");
        writer1("\t}\n\n");
      }
      if( etcPanel.paint.isSelected() ){
        writer1("\tpublic void paint(Graphics g){\n\n");
        writer1("\t}\n\n");
      }
      writer1("}\n");
    }catch( IOException e ){
    }
  }

  void addMethodForm(String lName, String uName)throws IOException{
    writer1("\tpublic synchronized void add" + uName + "Listener(" + uName + "Listener l){\n");
//    writer1("\t\tsuper.add" + uName + "Listener(l);\n");
    writer1("\t\tif(" + lName + "Listeners == null)\n");
    writer1("\t\t\t" + lName + "Listeners = new Vector();\n");
    writer1("\t\t" + lName + "Listeners.addElement(l);\n");
    writer1("\t}\n\n");
  }

  void removeMethodForm(String lName, String uName)throws IOException{
    writer1("\tpublic synchronized void remove" + uName + "Listener(" + uName + "Listener l){\n");
//    writer1("\t\tsuper.remove" + uName + "Listener(l);\n");
    writer1("\t\tif(" + lName + "Listeners != null && " + lName + "Listeners.contains(l))\n");
    writer1("\t\t\t" + lName + "Listeners.removeElement(l);\n");
    writer1("\t}\n\n");
  }

  void fireMethodForm(String lName, String uName, String lMethodName, String uMethodName)throws IOException{
    writer1("\tprotected void fire" + uMethodName + "(" + uName + "Event e){\n");
    writer1("\t\tif(" + lName + "Listeners != null){\n");
    writer1("\t\t\tint count = " + lName + "Listeners.size();\n");
    writer1("\t\t\tfor(int i = 0; i < count; i++)\n");
    writer1("\t\t\t\t((" + uName + "Listener)" + lName + "Listeners.elementAt(i))." + lMethodName + "(e);\n");
    writer1("\t\t}\n");
    writer1("\t}\n\n");
  }

  void generateEventObjectCode(){
    try{
      File parent = new File( currentpath + File.separator + classPanel.packName.getText().replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[1] = new File( parent, eventName + ".java" );
  	    fs2 = new FileOutputStream( file[1] );
      }

      writer2("/*\n");
      writer2(" *\tpackage : " + classPanel.packName.getText() + "\n");
      writer2(" *\tsource  : " + eventName + ".java\n");
      writer2(" *\tdate    : " + date + "\n");
      writer2(" */\n\n");

      writer2("package " + classPanel.packName.getText() + ";\n\n");

      writer2("import java.util.*;\n\n");

      writer2("public class " + eventName + " extends EventObject {\n\n");
      for( int i=0; i< eventPanel.table.getRowCount(); i++){
        writer2("\tstatic final int " + eventID[i] + " = " + (i+1) + ";\n");
      }
      writer2("\n");
      writer2("\tprivate int id = 0;\n\n");

      writer2("\tpublic int getID() {return id;};\n\n");

      writer2("\t" + eventName + "(Object source, int i){\n\n");
      writer2("\t\tsuper(source);\n");
      writer2("\t\tid = i;\n");
      writer2("\t}\n");
      writer2("}");

    }catch( IOException e ){
    }
  }

  void generateEventListenerCode(){
    try{
      File parent = new File( currentpath + File.separator + classPanel.packName.getText().replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[2] = new File( parent, listenerName + ".java" );
  	    fs3 = new FileOutputStream( file[2] );
      }

      writer3("/*\n");
      writer3(" *\tpackage : " + classPanel.packName.getText() + "\n");
      writer3(" *\tsource  : " + listenerName + ".java\n");
      writer3(" *\tdate    : " + date + "\n");
      writer3(" */\n\n");

      writer3("package " + classPanel.packName.getText() + ";\n\n");

      writer3("import java.util.*;\n\n");

      writer3("public interface " + listenerName + " extends EventListener {\n\n");
      for( int i=0; i < eventPanel.table.getRowCount(); i++)
        writer3("\tpublic void " + eventFName[i] + "(" + eventName + " e);\n");
      writer3("}");
    }catch( IOException e ){
    }
  }

  void generateBeanInfoClassCode(){
    try{
      String className = classPanel.className.getText();

      File parent = new File( currentpath + File.separator + classPanel.packName.getText().replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[3] = new File( parent, className + "Info.java" );
  	    fs4 = new FileOutputStream( file[3] );
      }
      writer4("/*\n");
      writer4(" *\tpackage : " + classPanel.packName.getText() + "\n");
      writer4(" *\tsource  : " + className + "Info.java\n");
      writer4(" *\tdate    : " + date + "\n");
      writer4(" */\n\n");

      writer4("package " + classPanel.packName.getText() + ";\n\n");

      writer4("import java.beans.*;\n");
      writer4("import java.awt.event.*;\n\n");

      writer4("public class " + className + "Info extends SimpleBeanInfo {\n\n");
      writer4("\tpublic " + className + "Info(){\n");
      writer4("\t}\n\n");

      //writer4("\tbeanClass = " + className + ".class;\n\n");
      writer4("\tpublic BeanDescriptor getBeanDescriptor(){\n");
      writer4("\t\tBeanDescriptor bd = new BeanDescriptor(" + className + ".class);\n\n");
      writer4("\t\treturn bd;\n");
      writer4("\t}\n");

      if(proPanel.table.getRowCount() != 0){
        //property descriptor
        writer4("\t//property descriptor\n");
        writer4("\tpublic PropertyDescriptor[] getPropertyDescriptors(){\n");
        writer4("\t\ttry {\n");
        for( int i=0; i<name.length; i++){
          writer4("\t\t\tPropertyDescriptor " + name[i] + " = new PropertyDescriptor(\"" + name[i] + "\", " + className + ".class);\n");
          if( bound[i].equals("V") )
            writer4("\t\t\t" + name[i] + ".setBound(true);\n");
          else
            writer4("\t\t\t" + name[i] + ".setBound(false);\n");
          if( constrained[i].equals("V") )
            writer4("\t\t\t" + name[i] + ".setConstrained(true);\n");
          else
            writer4("\t\t\t" + name[i] + ".setConstrained(false);\n");
          writer4("\n");
        }

        writer4("\t\t\tPropertyDescriptor[] pds = new PropertyDescriptor[]{");
//        writer4("\t\t\t\t");
        for( int j=0; j<name.length-1; j++) writer4(name[j] + ",");
        writer4(name[name.length-1] + "};\n");
        writer4("\t\t\treturn pds;\n");
        writer4("\t\t}catch(IntrospectionException e){\n");
        writer4("\t\t\treturn null;\n");
        writer4("\t\t}\n\n");
        writer4("\t}\n\n");
      }

      //method descriptor
      /*writer4("\t//method descriptors\n");
      writer4("\tpublic MethodDescriptor[] getMethodDescriptors(){\n");
      writer4("\t}\n\n");*/

      if( classPanel.createEvent.isSelected() ){
        //event descriptor
        writer4("\t//event set descriptors\n");
        writer4("\tpublic EventSetDescriptor[] getEventSetDescriptors(){\n");
        writer4("\t\ttry{\n");

        if( classPanel.createEvent.isSelected() ){
          int num = eventPanel.table.getRowCount();
          String names[] = new String[num];
          for( int k=0; k<num; k++){
            names[k] = eventFName[k];
            System.out.println("eventFName-" + eventFName[k]);
          }

          eventDsc( event, names );
        }
        if( firePanel.action.isSelected() ){
          String names[] = {"actionPerformed"};
          eventDsc("Action",names);
        }
        if( firePanel.adjustment.isSelected() ){
          String names[] = {"AdjustmentValueChanged"};
          eventDsc("Adjustment",names);
        }
        if( firePanel.component.isSelected() ){
          String names[] = {"ComponentResized","ComponentMoved","ComponentShown","ComponentHidden"};
          eventDsc("Component",names);
        }
        if( firePanel.container.isSelected() ){
          String names[] = {"Added","Removed"};
          eventDsc("Container",names);
        }
        if( firePanel.focus.isSelected() ){
          String names[] = {"FocusGrained","FocusLost"};
          eventDsc("Focus",names);
        }
        if( firePanel.item.isSelected() ){
          String names[] = {"ItemStateChanged"};
          eventDsc("Item",names);
        }
        if( firePanel.key.isSelected() ){
          String names[] = {"KeyReleased","KeyPressed","KeyTyped"};
          eventDsc("Key",names);
        }
        if( firePanel.mouse.isSelected() ){
          String names[] = {"MouseClicked","MousePressed","MouseEntered","MouseExited","MouseReleased"};
          eventDsc("Mouse",names);
        }
        if( firePanel.mouseMotion.isSelected() ){
          String names[] = {"MouseDragged","MouseMoved"};
          eventDsc("MouseMotion",names);
        }
        if( firePanel.text.isSelected() ){
          String names[] = {"TextValueChanged"};
          eventDsc("Text",names);
        }
        if( firePanel.window.isSelected() ){
          String names[] = {"WindowClosing","WindowClosed","WindowOpened","WindowActived","WindowDeactivated",
                            "WindowIconified","WindowDeiconified"};
          eventDsc("Window",names);
        }
        if( firePanel.ancestor.isSelected() ){
          String names[] = {"AncestorAdded","AncestorRemoved","AncestorMoved"};
          eventDsc("Ancestor",names);
        }
        if( firePanel.caret.isSelected() ){
          String names[] = {"CaretUpdate"};
          eventDsc("Caret",names);
        }
        if( firePanel.cellEditor.isSelected() ){
          String names[] = {"EditingStopped","EditingCanceled"};
          eventDsc("CellEditor",names);
        }
        if( firePanel.change.isSelected() ){
          String names[] = {"StateChanged"};
          eventDsc("Change",names);
        }
        if( firePanel.document.isSelected() ){
          String names[] = {"InsertUpdate","RemoveUpdate","ChangedUpdate"};
          eventDsc("Document",names);
        }
        if( firePanel.hyperlink.isSelected() ){
          String names[] = {"HyperlinkUpdate"};
          eventDsc("Hyperlink",names);
        }
        if( firePanel.internalF.isSelected() ){
          String names[] = {"InternalFrameOpened","InternalFrameClosing","InternalFrameClosed","InternalFrameIconified",
          "InternalFrameDeiconified","InternalFrameActivated","InternalFrameDeactivated"};
          eventDsc("InternalFrame",names);
        }
        if( firePanel.listData.isSelected() ){
          String names[] = {"IntervalAdded","IntervalRemoved","IntervalContentsChanged"};
          eventDsc("ListData",names);
        }
        if( firePanel.listS.isSelected() ){
          String names[] = {"ValueChanged"};
          eventDsc("ListSelection",names);
        }
        if( firePanel.menu.isSelected() ){
          String names[] = {"MenuSelected","MenuDeselected","MenuCanceled"};
          eventDsc("Menu",names);
        }
        if( firePanel.popupMenu.isSelected() ){
          String names[] = {"PopupMenuWillBecomeVisible","PopupMenuCanceled"};
          eventDsc("PopupMenu",names);
        }
        if( firePanel.tableCM.isSelected() ){
          String names[] = {"ColumnAdded","ColumnRemoved","ColumnMoved","ColumnMarginChanged","ColumnSelectionChanged"};
          eventDsc("TableColumnModel",names);
        }
        if( firePanel.tableM.isSelected() ){
          String names[] = {"TableChanged"};
          eventDsc("TableModel",names);
        }
        if( firePanel.treeE.isSelected() ){
          String names[] = {"TreeExpanded","TreeCollapsed"};
          eventDsc("TreeExpasion",names);
        }
        if( firePanel.treeM.isSelected() ){
          String names[] = {"TreeNodesChanged","TreeNodesInserted","TreeNodesRemoved","TreeStructureChanged"};
          eventDsc("TreeModel",names);
        }
        if( firePanel.treeS.isSelected() ){
          String names[] = {"ValueChanged"};
          eventDsc("TreeSelection",names);
        }
        if( firePanel.undo.isSelected() ){
          String names[] = {"UndoableEditHappened"};
          eventDsc("UndoableEdit",names);
        }
        writer4("\n");
        writer4("\t\t\tEventSetDescriptor[] eda = {");
        for( int q=1; q<count; q++)
          writer4("ed" + q + ",");
        writer4("ed" + count + "};\n\n");
        writer4("\t\t\treturn eda;\n");
        writer4("\t\t}catch(IntrospectionException e){\n");
        writer4("\t\t\treturn null;\n");
        writer4("\t\t}\n");
        writer4("\t}\n");

        writer4("}");
      }
    }catch( Exception e ){
    }
  }

  private void eventDsc(String event, String[] names) throws IOException{
    count++;
    writer4("\t\t\tString[] names" + count + " = {");
    for( int i=0; i<names.length-1; i++ )
      writer4("\"" + names[i] + "\",");
    writer4("\"" + names[names.length-1] + "\"};\n");
    writer4("\t\t\tEventSetDescriptor ed" + count + " = new EventSetDescriptor( " + classPanel.className.getText() + ".class,\n");
    writer4("\t\t\t\t\t\t\t\t\t\"" + event + "Event\",\n");
    writer4("\t\t\t\t\t\t\t\t\t" + event + "Listener.class,\n");
    writer4("\t\t\t\t\t\t\t\t\t");
    writer4("names" + count + ",\n");
    writer4("\t\t\t\t\t\t\t\t\t\"add" + event + "Listener\",\n");
    writer4("\t\t\t\t\t\t\t\t\t\"remove" + event + "Listener\");\n\n");
  }

  public File[] getFiles() {
  	return file;
  }

  private void writer1( String s )throws IOException{
    fs1.write( s.getBytes() );
  }

  private void writer2( String s )throws IOException{
    fs2.write( s.getBytes() );
  }

  private void writer3( String s )throws IOException{
    fs3.write( s.getBytes() );
  }

  private void writer4( String s )throws IOException{
    fs4.write( s.getBytes() );
  }
}
