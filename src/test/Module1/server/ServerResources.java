
package test.Module1.server;

import java.text.MessageFormat;

/**
* Template File
*   ServerResources.java.template
* IDL Object
*   Module1
* Generation Date
*   1999�� 7�� 28�� ������ 04��24��54�� ����
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Contains server application strings for localization.
*/

public class ServerResources extends java.util.ListResourceBundle {
  static final Object[][] contents = {
  { "created",         "{0} created" },
  { "isReady",         "{0} is ready!" },
  { "numberObjects",   "Number of objects created" },
  { "numberRead",      "Number of rows read" },
  { "numberWritten",   "Number of rows written" },
  { "logTitle",        "{0} Log" }};

  public Object[][] getContents() {
    return contents;
  }

  public static String format(String pattern, Object p1) {
    return MessageFormat.format(pattern, new Object[] {p1});
  }

  public static String format(String pattern, Object p1, Object p2) {
    return MessageFormat.format(pattern, new Object[] {p1, p2});
  }

  public static String format(String pattern, Object p1, Object p2, Object p3) {
    return MessageFormat.format(pattern, new Object[] {p1, p2, p3});
  }

  public static String format(String pattern, Object[] objects) {
    return MessageFormat.format(pattern, objects);
  }
}
