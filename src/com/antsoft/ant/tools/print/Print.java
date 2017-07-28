/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/tools/print/Print.java,v 1.6 1999/08/19 05:49:51 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 */
package com.antsoft.ant.tools.print;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.*;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.util.DayInfo;


public class Print {

  private JavaDocument document;

  public Print(){
  }

	public void doPrint(SourceEntry se, ViewFactory vf){
    Frame frame = MainFrame.mainFrame;

    String header = se.getFullPathName();
    int doctype = se.getDocumentType();

		PrintJob job = frame.getToolkit().getPrintJob(frame, header, null);
		if(job == null)	return;

		int ppi = job.getPageResolution();

		Graphics gfx = null;

    Font f = Main.property.getSelectedFont();
    Font font = new Font(f.getName(), f.getStyle(), f.getSize()-2);

		Dimension pageDimension = job.getPageDimension();
    double pageMargin = ppi * .75; 
		int pageWidth = (int)(pageDimension.width - pageMargin);
		int pageHeight = (int)(pageDimension.height - pageMargin);

    int topMargin = (int)(PrintSetupDlg.topMargin*0.1* ppi);
		int leftMargin = (int)(PrintSetupDlg.leftMargin*0.1*ppi);
		int bottomMargin = (int)(PrintSetupDlg.bottomMargin*0.1*ppi);
		int rightMargin = (int)(PrintSetupDlg.rightMargin*0.1*ppi);

		int y = 0;
    FontMetrics fmm = Toolkit.getDefaultToolkit().getFontMetrics(font);
		int fontHeight = font.getSize()+1;
    int charWidth = fmm.charWidth('m');
		int tabSize = Main.property.getTabSpaceSize() * charWidth;

    AntView  view = (AntView)vf.create(se.getDocument().getDefaultRootElement());
    view.setPrintMode(true);
    view.setTabSize(tabSize);
    view.setLeftMargin(leftMargin);
    if(PrintSetupDlg.drawWrapped){
      view.setPageWidth(pageWidth);
      view.setFontMetrics(fmm);
      view.setTabBase((int)pageMargin/2);
      view.setFontHeight(fontHeight);
    }


    int elementCount = view.getDocument().getDefaultRootElement().getElementCount();

    if(PrintSetupDlg.drawLine) {
      String line = elementCount+"";
      view.setDrawLineNumber(true, line.length(), charWidth*(line.length()+1));
    }

    boolean oldFlg = view.isSyntaxColoring();
    if(!PrintSetupDlg.drawColored){
      view.setSyntaxColoring(false);
    }

    int pageNumber=0;
    for(int i = 0; i < elementCount; i++)
    {
      if(gfx == null)
      {
        gfx = job.getGraphics();
        ++pageNumber;

        //draw header
        if(PrintSetupDlg.drawHeader){
          gfx.setFont(font);
          FontMetrics fm = gfx.getFontMetrics();
          gfx.setColor(Color.black);
          gfx.draw3DRect(leftMargin, topMargin, pageWidth	- leftMargin - rightMargin,
                       fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading(), true);
          y = topMargin + fontHeight;
          gfx.drawString(header, leftMargin, y);
          y = topMargin + fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
          y += fontHeight;
        }

        //draw header end
        gfx.setFont(font);
        gfx.setColor(Color.black);
      }

      //print footer
      if( (y + 2*fontHeight) > (pageHeight - bottomMargin) || (i == (elementCount - 1)) ){
        y+=fontHeight;
        if(PrintSetupDlg.drawDate){
           gfx.drawString(DayInfo.getPrintFormatDayStr(), leftMargin, pageHeight);
        }

        if(PrintSetupDlg.drawPageNumber){
           int x = (int)(pageMargin + leftMargin + (pageWidth/2)*.8);
           gfx.drawString("[ "+pageNumber+" ]", x,  pageHeight);
        }

        if(i!=(elementCount-1)) --i;
      }
      else{
        int count = view.printLine(i, gfx, leftMargin, y += fontHeight);
        y += count*fontHeight;
      }  

      //page end or print end
      if( (y > (pageHeight - bottomMargin)) || (i == (elementCount - 1)) )
      {
        gfx.dispose();
        gfx = null;
      }
    }

    if(!PrintSetupDlg.drawColored){
      view.setSyntaxColoring(oldFlg);
    }

    job.end();
    view = null;
  }
}

