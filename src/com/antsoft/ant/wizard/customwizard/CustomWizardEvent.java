/*
 *  CustomWizardEvent - 사용자 위저드를 위한 이벤트 클래스
 *  designed by Kim yunKyung
 *  date 1999.8.3
 */
 
package com.antsoft.ant.wizard.customwizard;

import java.util.*;

public class CustomWizardEvent extends EventObject {

  public static final int OK=1;

  private int id=0;

  public int getID() {return id;};

  public CustomWizardEvent(Object source,int i) {
    super(source);
    id=i;
  }
}
