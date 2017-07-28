/*
 * $Id: OverridePanel.java,v 1.16 1999/08/31 05:00:13 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.16 $
 */
package com.antsoft.ant.manager.projectmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.lang.reflect.*;
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.pool.classpool.ClassPool;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.QuickSorter;
import com.antsoft.ant.util.ColorList;

public class OverridePanel extends JPanel {
  private JComboBox classCbx, superCbx, methodCbx;
  private DefaultComboBoxModel classCbxM, superCbxM;
  private MethodComboModel methodCbxM;
  private JButton doBtn;

  private CodeContext codeContext;
  private ProjectExplorer pe;
                                                   
  public static final int CLASS = 0;
  public static final int INNER = 1;
  public static final int SUPER = 2;
  public static final int METHOD = 3;
  public static final int USER = 4;

  public OverridePanel(CodeContext codeContext, ProjectExplorer pe) {
    this.setPreferredSize(new Dimension(0, 22));

    setLayout(new BorderLayout());
    this.codeContext = codeContext;
    this.pe = pe;

    ComboRenderer renderer = new ComboRenderer();
    ItemListener itemHandler = new ItemHandler();
    ActionListener actionHandler = new ActionHandler();

    JPanel cbxP = new JPanel(new GridLayout(1, 3, 5, 0));

    classCbx = new JComboBox(classCbxM = new DefaultComboBoxModel());
    superCbx = new JComboBox(superCbxM = new DefaultComboBoxModel());
    methodCbx = new JComboBox(methodCbxM = new MethodComboModel());

    classCbx.setMinimumSize(new Dimension(50, 20));
    superCbx.setMinimumSize(new Dimension(50, 20));
    methodCbx.setMinimumSize(new Dimension(50, 20));

    Font f = new Font(classCbx.getFont().getName(), Font.PLAIN, 13);

    classCbx.setFont(f);
    superCbx.setFont(f);
    methodCbx.setFont(f);

    classCbx.setRenderer(renderer);
    superCbx.setRenderer(renderer);
    methodCbx.setRenderer(renderer);

    classCbx.addItemListener(itemHandler);
    superCbx.addItemListener(itemHandler);

    cbxP.add(classCbx);
    cbxP.add(superCbx);
    cbxP.add(methodCbx);

    doBtn = new JButton("Override");
    doBtn.setToolTipText("override method of super class");
    doBtn.addActionListener(actionHandler);

    this.add(cbxP, BorderLayout.CENTER);
    this.add(doBtn, BorderLayout.EAST);
  }

  public void removeAll(int type){
    switch(type){
      case CLASS :
      case INNER :
        classCbxM.removeAllElements();
        break;

      case SUPER :
        superCbxM.removeAllElements();
        break;

      case METHOD :
        methodCbxM.removeAllElements();
        break;
    }
  }

  public void addClass(String data, int type){
    switch(type){
      case CLASS :
        classCbxM.addElement(new ItemType(data, type));
        break;

      case INNER :
        classCbxM.addElement(new ItemType(data, type));
        break;
    }
  }

  public boolean isClassCbxEmpty(){
    return (classCbxM.getSize() == 0);
  }

  public void removeClass(String data, int type){
    for(int i=0; i<classCbxM.getSize(); i++){
      ItemType item = (ItemType)classCbxM.getElementAt(i);
      if(item.toString().equals(data)) {
        classCbxM.removeElementAt(i);
        break;
      }
    }
  }

  public void removeAllClass(){
    classCbxM.removeAllElements();
  }

  public void removeAll(){
    classCbxM.removeAllElements();
    superCbxM.removeAllElements();
    methodCbxM.removeAllElements();
  }

  String prevSuperSel = "";
  private void superCbxItemSelected(ItemType item){

    if(prevSuperSel.equals(item.toString()) && methodCbxM.getSize() != 0) return;
    else prevSuperSel = item.toString();

    methodCbxM.removeAllElements();
    String fullClassName = (String) item.getData();

    if(item.getType() != USER){
      Class instance = ClassPool.getClassInstance(fullClassName);
      Method [] methods = instance.getDeclaredMethods();

      if(methods != null)
      for(int i=0;  i<methods.length; i++){
        if(Modifier.isPrivate( methods[i].getModifiers() )) continue;
        methodCbxM.addElement(new ItemType(methods[i], this.METHOD));
      }
      methodCbxM.sort();
			methods = null;
			instance = null;
    }else {
      Vector methods = codeContext.getMethodToOverride(fullClassName);
      if(methods != null)
      for(int i=0;  i<methods.size(); i++){
        //methodCbxM.addElement(new ItemType(methods[i], this.METHOD));
      }
//      methodCbxM.sort();

      System.out.println("It is user source");
    }
  }

  String prevClassSel = "";
  private void classCbxItemSelected(ItemType item){

    if(prevClassSel.equals(item.toString()) && superCbxM.getSize() != 0) return;
    else prevClassSel = item.toString();

    superCbxM.removeAllElements();

    Vector supers = null;
    boolean isInner = (item.getType() == INNER);
    //outter class
    supers = codeContext.getSuperClass(item.toString(), isInner);
    if(supers == null) return;
    int size = supers.size();

    //jdk class
    if(size == 1){
      Vector ret = ClassPool.getSuperClasses((String)supers.elementAt(0));
      if(ret != null){
        int retSize = ret.size();
        for(int i=0; i<retSize; i++){
          superCbxM.addElement(new ItemType(ret.elementAt(i), SUPER));
        }
      }
    }
    else {
      //user class
      int i;
      for(i=supers.size()-1 ;i>0; i--){
        superCbxM.addElement(new ItemType((String)supers.elementAt(i), USER));
      }
      //library class
      Vector ret = ClassPool.getSuperClasses((String)supers.elementAt(0));
      if(ret != null){
        int retSize = ret.size();
        for(int j=0; j<retSize; j++){
          superCbxM.addElement(new ItemType(ret.elementAt(j), SUPER));
        }
      }
    }
  }

  private String getSource(){
    String gap = null;

    if(((ItemType)classCbxM.getSelectedItem()).getType() == CLASS){
      gap = "\t";
    }
    else gap = "\t\t";

    Method m = (Method)((ItemType)methodCbxM.getSelectedItem()).getData();
    String paramStr = "";
    Class [] params = m.getParameterTypes();

    for(int j=0; j<params.length; j++)
    {
       paramStr += params[j].getName().substring(params[j].getName().lastIndexOf(".")+1) + " p"+j;
       if(j != params.length-1) paramStr += ", ";
    }

    StringBuffer source = new StringBuffer();

    source.append("\n");
    source.append(gap);
    int mod = m.getModifiers();
    boolean access = false;

    if(Modifier.isProtected(mod)) {
      source.append("protected");
      access = true;
    }
    else {
      source.append("public");
      access = true;
    }

    String str = m.getReturnType().getName();
    String returnStr = str.substring(str.lastIndexOf(".")+1);
    if(access) source.append(" ");

    if(paramStr.equals("")){
      source.append( returnStr + " " + m.getName() + "()" );
    }
    else{
      source.append( returnStr + " " + m.getName() + "( " + paramStr + " )" );
    }

    source.append("\n");
    source.append(gap + "{\n");
    source.append(gap + gap + "//TO DO (implementation here) \n");

    if(!returnStr.equals("void")){
      String param = "";
      for(int k=0; k<params.length; k++){
        param += "p" + k;
        if(k != params.length-1) param += ", "; 
      }
      source.append(gap + gap + "return super."+ m.getName() + "(" + param +");" );
      source.append("\n");
    }

    source.append(gap + "}\n");

    return source.toString();
  }

  private String getMethodTypeSig(Method m){

     String sig="";
     Class [] parameterTypes = m.getParameterTypes();
  	 for (int i=0;i<parameterTypes.length;++i) {
       sig += getMeaningFullName(parameterTypes[i]);
       if(i != parameterTypes.length-1) sig +=",";
     }

     return m.getName()+"("+sig+")";
  }

  private String getMeaningFullName(Class instance){

     StringBuffer buf = new StringBuffer();
     String param=instance.getName();

     //primitive type
	   if (param.lastIndexOf(".")==-1)
     {
       //array type

       if(instance.isArray())
       {
         String arrParam="";
         for(int k=0; k<param.length(); k++)
         {
           if(param.charAt(k) == '[')
           {
             arrParam += "[]";
           }
           else
           {
             switch(param.charAt(k)){
               case 'Z' : arrParam = "boolean" + arrParam; break;
               case 'B' : arrParam = "byte" + arrParam; break;
               case 'C' : arrParam = "char" + arrParam; break;
               case 'S' : arrParam = "short" + arrParam; break;
               case 'I' : arrParam = "int" + arrParam; break;
               case 'J' : arrParam = "long" + arrParam; break;
               case 'F' : arrParam = "float" + arrParam; break;
               case 'D' : arrParam = "double" + arrParam; break;
             }
             buf.append(arrParam);
           }
         }
       }//array type end
       else
       {
         buf.append(param);
       }
     }
	   else
     {
       String arrParam2 = "";
       if(param.startsWith("[")){
         for(int k=0; k<param.length(); k++)
         {
           if(param.charAt(k) == '['){
             arrParam2 += "[]";
           }
           else if(param.charAt(k) == 'L')
           {
             arrParam2 = param.substring( param.lastIndexOf(".")+1, param.length()-1 ) + arrParam2;
             break;
           }
         }
       }
       else { arrParam2 = param.substring(param.lastIndexOf(".")+1,param.length()); }
        buf.append(arrParam2);
     }

     return buf.toString();
  }

  class ItemHandler implements ItemListener{
    public void itemStateChanged(ItemEvent evt){
      if(evt.getSource() == classCbx){
        classCbxItemSelected((ItemType)evt.getItem());
      }
      else {
        superCbxItemSelected((ItemType)evt.getItem());
      }
    }
  }

  class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent e){
      if(methodCbxM.getSelectedItem() == null) return;

      int pos = codeContext.getInsertPosition(classCbxM.getSelectedItem().toString());
      String source = getSource();
      pe.addOverriedMethod(source, pos);
    }
  }

  class ItemType {
    private int type;
    private Object data;

    public ItemType(Object data, int type){
      this.data = data;
      this.type = type;
    }

    public int getType(){
      return type;
    }

    public Object getData(){
      return data;
    }

    public String toString(){
      if(data instanceof Class){
        Class obj = (Class)data;
        return obj.getName();
      }
      else if(data instanceof Method){
        Method obj = (Method)data;
        return getMethodTypeSig(obj);
      }
      else return data.toString();
    }

    public boolean equals(Object o){
      if(o == null) return false;
      else{
        if(o.toString().equals(toString())) return true;
        else return false;
      }
    }
  }

  class ComboRenderer extends DefaultListCellRenderer {
    private JList list;
    public ComboRenderer(){
      setOpaque(true);
      setFont(FontList.treeFont);
    }

    public Component getListCellRendererComponent(
        JList list,
      	Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
      if(list == null) this.list = list;

      if (isSelected) {
          setBackground(ColorList.darkBlue);
          setForeground(Color.white);
      }
      else {
          setBackground(Color.white);
          setForeground(Color.black);
      }

      if (value instanceof Icon) {
          setIcon((Icon)value);
      }
      else {
          setText((value == null) ? "" : value.toString());
      }

      setEnabled(list.isEnabled());
      setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

      if(list.getSelectedValue() != null)
      list.setToolTipText(list.getSelectedValue().toString());

      return this;
    }
  }

  class MethodComboModel extends DefaultComboBoxModel{
    private Vector data = null;
    private Object sel = null;

    public MethodComboModel(){
      data = new Vector(10, 5);
    }

    public void sort(){
      data = QuickSorter.sort(data, QuickSorter.LESS_STRING, true);
    }

    public Object getSelectedItem(){
      return sel;
    }

    public void setSelectedItem(Object anObject) {
        if ( (sel != null && !sel.equals( anObject )) ||
             sel == null && anObject != null ) {
            sel = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * Adds an item to the end of the model.
     */
    public void addElement( Object obj ){
      data.addElement(obj);
      fireIntervalAdded(this,data.size()-1, data.size()-1);
      super.addElement(obj);

      if ( data.size() == 1 && sel == null && obj != null ) {
          this.setSelectedItem(obj);
      }
    }

    /**
     * Adds an item to the end of the model.
     */
    public void removeElement( Object obj ){
      data.removeElement(obj);
    }

    /**
     * Adds an item at a specific index
     */
    public void insertElementAt( Object obj, int index ){
      data.insertElementAt(obj, index);
    }

    /**
     * Removes an item at a specific index
     */
    public void removeElementAt( int index ){
      data.removeElementAt(index);
    }


    public void removeAllElements(){
      if(data != null && data.size() > 0){
        int firstIndex = 0;
        int lastIndex = data.size() - 1;
        data.removeAllElements();
        sel = null;
        fireIntervalRemoved(this, firstIndex, lastIndex);
      }
    }

    /**
     * Returns the length of the list.
     */
    public int getSize(){
      return data.size();
    }

    /**
     * Returns the value at the specified index.
     */
    public Object getElementAt(int index){
      if ( index >= 0 && index < data.size() )
          return data.elementAt(index);
      else
          return null;

    }
  }
}
/*
 * $Log: OverridePanel.java,v $
 * Revision 1.16  1999/08/31 05:00:13  multipia
 * no message
 *
 */
