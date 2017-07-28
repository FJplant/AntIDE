
package test.Module1.client;

/**
* Template File
*   OpExecutedEvent.java.template
* IDL Object
*   Module1.Interface1.operation1()
* Generation Date
*   1999년 7월 28일 수요일 04시24분52초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Defines the event which is fired upon completion of a particular IDL method.
*/

public class Interface1_operation1_OpExecutedEvent extends java.util.EventObject {
  public String result;
  public int parameterParam1;
  public float parameterParam2;

  public Interface1_operation1_OpExecutedEvent(Object source) {
    super(source);
  }
}
