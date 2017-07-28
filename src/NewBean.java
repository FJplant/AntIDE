// This snippet creates a new empty bean
//

//Title:
//Version:      
//Copyright:
//Author:       
//Company:      
//Description:


package  com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import borland.jbcl.layout.*;
import borland.jbcl.control.*;
import borland.jbcl.view.*;
import borland.jbcl.util.BlackBox;

public class NewBean extends BeanPanel implements BlackBox{
  BevelPanel bevelPanel1 = new BevelPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout1 = new XYLayout();

  public NewBean() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    bevelPanel1.setLayout(xYLayout1);
    this.setLayout(borderLayout1);
    this.add(bevelPanel1, BorderLayout.CENTER);
  }

  // Example properties
  private String example = "Example1";

  public void setExample(String s) {
    example=s;
  }
  public String getExample(){
    return example;
  }

  // Example event
  public static final String EXAMPLE_EVENT = "ExampleEvent";
  protected void fireExampleActionEvent() {
    //Args:  event source,event ID, event command
    processActionEvent(new ActionEvent(this,ActionEvent.ACTION_PERFORMED, EXAMPLE_EVENT));
  }
}
 
