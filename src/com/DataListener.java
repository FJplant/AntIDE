//Title:        AntProgrammer
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Kim sang-kyun
//Company:      AntSoft. co.
//Description:  IDE 프로젝트

package com;

import java.util.*;

public interface DataListener extends EventListener {
  public void dataChanged(DataEvent e);
  public void dataRemoved(DataEvent e);
}
