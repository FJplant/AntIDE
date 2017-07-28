/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * HtmlContext.java
 *
 * @author Kim, Sung-Hoon.
 *
 */
package com.antsoft.ant.pool.sourcepool;

import javax.swing.text.*;
import com.antsoft.ant.tools.print.*;

/**
 *  class HtmlContext
 *
 *  @author Kim, Sung-Hoon.
 */
public class HtmlContext extends StyleContext implements ViewFactory {

  // --- ViewFactory methods -------------------------------------
  public View create(Element elem) {
    return new HtmlView(this, elem);
  }
}
