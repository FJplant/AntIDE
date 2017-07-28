/**
 * 오늘의 날짜, 시간등에 대해서 제공하는  Class
 *
 * @author Kim, Sung-Hoon.
 */

package com.antsoft.ant.tools.javadoc;

import java.util.Vector;

import com.antsoft.ant.property.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.manager.projectmanager.*;

public class DocInfo {
  // jdk

  private String javadocEXEpath;
  private boolean javaVersion1_1; // true -> 1.1, false -> 1.2

  // option
  IdeProperty property = null;
  Project project = null;
  JdkInfo info = null;

  public DocInfo () {}

  public DocInfo (Project prj) {
    this.project = prj;
    property = Main.property;
    info = project.getPathModel().getCurrentJdkInfo();

  }

  public String getJdkClassPath() {
    return project.getPathModel().getClassPath();
  }
  
  public String getDocumentRoot() {
    return project.getPathModel().getDocumentRoot();
  }

  public String getSourceRoot() {
    return project.getPathModel().getSourceRoot();
  }
  
  public String getJavadocEXEPath() {
    return info.getJavadocEXEPath();
  }

  public String getJdkVersion() {
    return info.getVersion();
  }

  public boolean getJavadocVersion() {
    return Main.property.getJavadocVersion();
  }
  public int getScope() {
    return Main.property.getScopeDoc();
  }
  public boolean getVerbose() {
    return Main.property.getVerboseDoc();
  }
  public boolean getAuthor() {
    return Main.property.getAuthorDoc();
  }
  public boolean getVersion() {
    return Main.property.getVersionDoc();
  }
  public boolean getNoindex() {
    return Main.property.getNoindexDoc();
  }
  public boolean getNotree() {
    return Main.property.getNotreeDoc();
  }
  public boolean getNodeprecate() {
    return Main.property.getNodeprecateDoc();
  }
  public boolean getNewclasspath() {
    return Main.property.getNewclasspathDoc();
  }
  public boolean getNewsourcepath() {
    return Main.property.getNewsourcepathDoc();
  }
  public String getClasspath() {
    return Main.property.getClasspathDoc();
  }
  public String getSourcepath() {
    return Main.property.getSourcepathDoc();
  }
  public String getEncoding() {
    return Main.property.getEncodingDoc();
  }
  public String getDocencoding() {
    return Main.property.getDocencodingDoc();
  }
  public String getJ() {
    return Main.property.getJDoc();
  }
  public boolean getUse() {
    return Main.property.getUseDoc();
  }
  public boolean getNohelp() {
    return Main.property.getNohelpDoc();
  }
  public boolean getNonavbar() {
    return Main.property.getNonavbarDoc();
  }
  public boolean getNodeprecatedlist() {
    return Main.property.getNodeprecatedlistDoc();
  }
  public boolean getSplitindex() {
    return Main.property.getSplitindexDoc();
  }
  public String getDoclet() {
    return Main.property.getDocletDoc();
  }
  public String getDocletpath() {
    return Main.property.getDocletpathDoc();
  }
  public String getBootclasspath() {
    return Main.property.getBootclasspathDoc();
  }
  public String getExtdirs() {
    return Main.property.getExtdirsDoc();
  }
  public String getLocale() {
    return Main.property.getLocaleDoc();
  }
  public String getLink() {
    return Main.property.getLinkDoc();
  }
  public String getLinkoffline() {
    return Main.property.getLinkofflineDoc();
  }
  public String getGroup() {
    return Main.property.getGroupDoc();
  }
  public String getHelpfile() {
    return Main.property.getHelpfileDoc();
  }
  public String getStylesheetfile() {
    return Main.property.getStylesheetfileDoc();
  }
  public String getWindowtitle() {
    return Main.property.getWindowtitleDoc();
  }
  public String getDoctitle() {
    return convertString(Main.property.getDoctitleDoc());
  }
  public String getHeader() {
    return convertString(Main.property.getHeaderDoc());
  }
  public String getFooter() {
    return convertString(Main.property.getFooterDoc());
  }
  public String getBottom() {
    return convertString(Main.property.getBottomDoc());
  }

  private String convertString(String str) {
    StringBuffer buf = new StringBuffer();
    for(int i=0; i<str.length(); i++) {
      if (str.charAt(i) == '*') {
        i++;
        if (str.charAt(i) == '*') {
          i++;
          if(str.charAt(i) == '*') {
            buf.append('\n');
          }
          else {
            buf.append("**" + str.charAt(i));
          }
        }
        else {
          buf.append('*');
          buf.append(str.charAt(i));
        }
      }
      else {
         buf.append(str.charAt(i));
      }
    }

    return buf.toString();
  }

}

