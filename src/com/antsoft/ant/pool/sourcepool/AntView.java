package com.antsoft.ant.pool.sourcepool;

import java.awt.*;
import javax.swing.text.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.tools.print.PrintSetupDlg;

public class AntView extends PlainView {

  private boolean syntaxColoring = true;

  public AntView(Element elem) {
    super(elem);
  }

  public void setSyntaxColoring( boolean b ) {
    syntaxColoring = b;
  }

  public boolean isSyntaxColoring( ) {
    return syntaxColoring;
  }

  public int getTabSize() {
    int tabSize = Main.property.getTabSpaceSize();
    if (tabSize > 0) return tabSize;
    else return 4;
  }

  protected int calculateBreakPosition(int p0, int p1) {
    int p;
    Segment lineBuffer = new Segment();
    try {
        Document doc = getDocument();
        doc.getText(p0, p1 - p0, lineBuffer);
    } catch (BadLocationException bl) {
    }

    p = p0 + Utilities.getBreakLocation(lineBuffer, fm, tabBase, tabBase + pageWidth,	this, p0);
    return p;
  }

  /** for print */
  public int printLine(int lineIndex, Graphics g, int x, int y) {
    if(this.isDrawLineNumber()) {
      Color old = g.getColor();
      g.setColor(Color.black);
      g.drawString(getRightAllignedNumber((lineIndex+1)+""), x, y);
      g.setColor(old);
      x += gap;
    }

    if(PrintSetupDlg.drawWrapped){
      Element line = getElement().getElement(lineIndex);
      int p1 = line.getEndOffset();
      int lineCount = -1;
      for (int p0 = line.getStartOffset(); p0 < p1; ) {
     		int p = calculateBreakPosition(p0, p1);
        try{
          drawUnselectedText(g, x, y, p0, p);
          lineCount++;
        }catch(BadLocationException e){}

        p0 = (p == p0) ? p1 : p;
        y += fontHeight;
      }

      return lineCount;
    }

    else{
      super.drawLine(lineIndex, g, x, y);
      return 0;
    }
  }

  private String getRightAllignedNumber(String line){
    int gap = lineWidth - line.length();
    for(int i=0; i<gap; i++){
      line = " "+line;
    }
    return line;
  }

  public float nextTabStop(float x, int tabOffset) {
    if(isPrintMode)	return ((((int)x - leftMargin) / tabSize + 1) * tabSize) + leftMargin;
    else return super.nextTabStop(x, tabOffset);
  }

  public void setLeftMargin(int margin){
    leftMargin = margin;
  }

  public void setTabSize(int size){
    tabSize = size;
  }

  public void setPrintMode(boolean flag){
    this.isPrintMode = flag;
  }

  public boolean isPrintMode(){
    return isPrintMode;
  }

  public void setDrawLineNumber(boolean flag, int lineWidth, int gap){
    this.drawLineNumber = flag;
    this.lineWidth = lineWidth;
    this.gap = gap;
  }

  public boolean isDrawLineNumber(){
    return drawLineNumber;
  }

  public void setFontMetrics(FontMetrics fm){
    this.fm = fm;
  }

  public void setPageWidth(int width){
    this.pageWidth = width;
  }

  public void setTabBase(int base){
    this.tabBase = base;
  }

  public void setFontHeight(int height){
    this.fontHeight = height;
  }  

  // for print
  private int leftMargin;
	private int tabSize;
  private boolean isPrintMode = false;
  private boolean drawLineNumber = false;
  private int lineWidth = -1;
  private int gap = -1;
  private FontMetrics fm;
  private int pageWidth;
  private int tabBase;
  private int fontHeight;
}

