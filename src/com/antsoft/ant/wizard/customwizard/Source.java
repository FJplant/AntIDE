/*
 *  SourceVector - source를 전달하기 위한 vector
 *  designed by Kim yunKyung
 *  date 1999.8.3
 */
 
package com.antsoft.ant.wizard.customwizard;

public class Source {

  private String name;
  private String source;

  public Source(String name,String source) {
    this.name = name;
    this.source = source;
  }

  public String getName(){
    return name;
  }

  public String getSource(){
    return source;
  }
}
