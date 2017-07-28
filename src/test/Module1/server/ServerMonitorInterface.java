
package test.Module1.server;

/**
* Template File
*   ServerMonitorInterface.java.template
* IDL Object
*   Module1
* Generation Date
*   1999�� 7�� 28�� ������ 04��24��53�� ����
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Used by server interface implementations to define and update their instance of a server monitor page.
*/

public abstract interface ServerMonitorInterface  {
  public void showObjectCounter(boolean bVisible);
  public void showReadCounter(boolean bVisible);
  public void showWriteCounter(boolean bVisible);
  public void updateObjectCounter(int n);
  public void updateReadCounter(int n);
  public void updateWriteCounter(int n);
  public void refresh();
}
