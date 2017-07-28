
package test.Module1.client;

import java.text.MessageFormat;

/**
* Template File
*   ClientResources.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분49초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Contains client application strings for localization.
*/

public class ClientResources extends java.util.ListResourceBundle {
  static final Object[][] contents = {
  { "File",              "File" },
  { "FileShortcut",      "F" },
  { "Exit",              "Exit" },
  { "ExitShortcut",      "x" },
  { "Actions",           "Actions" },
  { "ActionsShortcut",   "A" },
  { "RemoveTab",         "Remove Tab" },
  { "RemoveTabShortcut", "R" },
  { "Help",              "Help" },
  { "HelpShortcut",      "H" },
  { "About",             "About" },
  { "AboutShortcut",     "A" },
  { "Version",           "Version 1.0" },
  { "Copyright",         "Copyright (c) 1999" },
  { "Comments",          "JBuilder Generated Application" },
  { "OK",                "OK" },
  { "Refresh",           "Refresh" },
  { "Save",              "Save" },
  { "SaveAll",           "Save All" },
  { "result",            "result" },
  { "InterfaceNotInitialized", "{0} interface not initialized" }
  };

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
