//Title:        AntProgrammer
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Kim sang-kyun
//Company:      AntSoft. co.
//Description:  IDE 프로젝트

package com;

import java.util.*;
import java.beans.*;

public class DataEvent extends EventObject {
  public DataEvent(Object source) {
    super(source);
  }
  private String tel;
  private String address;
  private String pager;

  public String getTel() {
    return tel;
  }

  public void setTel(String newTel) {
    tel = newTel;
  }

  public void setAddress(String newAddress) {
    String  oldAddress = address;
    address = newAddress;
    propertyChangeListeners.firePropertyChange("address", oldAddress, newAddress);
  }

  public String getAddress() {
    return address;
  }

  public void setPager(String newPager) throws java.beans.PropertyVetoException {
    String  oldPager = pager;
    vetoableChangeListeners.fireVetoableChange("pager", oldPager, newPager);
    pager = newPager;

    propertyChangeListeners.firePropertyChange("pager", oldPager, newPager);
  }

  public String getPager() {
    return pager;
  }
}

