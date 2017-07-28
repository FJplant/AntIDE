/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/util/BorderList.java,v 1.8 1999/08/25 10:47:36 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.8 $
 */

package com.antsoft.ant.util;

import javax.swing.border.*;
import java.awt.*;

/**
 * BorderList util class
 *
 * @author kim sang kyun
 */
public class BorderList {
    public final static Border emptyBorder0 = new EmptyBorder(0,0,0,0);
    public final static Border emptyBorder1 = new EmptyBorder(1,1,1,1);
    public final static Border emptyBorder5 = new EmptyBorder(5,5,5,5);
    public final static Border emptyBorder10 = new EmptyBorder(10,10,10,10);
    public final static Border emptyBorder15 = new EmptyBorder(15,15,15,15);
    public final static Border emptyBorder20 = new EmptyBorder(20,20,20,20);

    public final static Border etchedBorder10 = new CompoundBorder(new EtchedBorder(),emptyBorder10);
    public final static Border etchedBorder5 = new CompoundBorder(new EtchedBorder(),emptyBorder5);
    public final static Border etchedBorder1 = new CompoundBorder(new EtchedBorder(),emptyBorder1);

    public final static Border raisedBorder = new BevelBorder(BevelBorder.RAISED);
    public final static Border lightLoweredBorder = new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray);
    public final static Border loweredBorder = new SoftBevelBorder(BevelBorder.LOWERED);
    public final static Border titledBorder = new TitledBorder(etchedBorder5, "Project Setup");

    // itree√ﬂ∞°
    public final static BlackTitledBorder jdkBorder = new BlackTitledBorder("JDK Choose");
    public final static BlackTitledBorder scopeBorder = new BlackTitledBorder("scope Choose");
    public final static BlackTitledBorder checkBorder = new BlackTitledBorder("Check Option");
    public final static BlackTitledBorder pathBorder = new BlackTitledBorder("Path Setting");
    public final static BlackTitledBorder otherBorder = new BlackTitledBorder("Others");
    public final static BlackTitledBorder textBorder = new BlackTitledBorder("Text");
    public final static BlackTitledBorder htmlBorder = new BlackTitledBorder("Html");
    public final static BlackTitledBorder optionBorder = new BlackTitledBorder("Option"); 
    public final static BlackTitledBorder descBorder = new BlackTitledBorder("Description");
    public final static BlackTitledBorder dirBorder = new BlackTitledBorder("Root Directories");
    public final static BlackTitledBorder libBorder = new BlackTitledBorder("Libraries");
    public final static BlackTitledBorder previewBorder = new BlackTitledBorder("Preview");

    public final static BevelBorder bebelBorder = new BevelBorder(BevelBorder.LOWERED);
    public final static Border selLineBorder = new LineBorder(Color.yellow, 1);
    public final static Border unselLineBorder = new LineBorder(new Color(0,0,31), 1);
}

