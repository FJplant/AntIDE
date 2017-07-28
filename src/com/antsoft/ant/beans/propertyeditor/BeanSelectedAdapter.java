package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BeanSelectedAdapter extends MouseAdapter{
  private Component com;
  private BeanTester tester;

  public BeanSelectedAdapter(BeanTester tester){
    super();
    this.tester = tester;
  }

  /**
   * ���콺�� ������Ʈ ��迡 �������� Ŀ���� ��ȭ��Ų��.
   */
  public void mouseEntered(MouseEvent e){

    Component component = (Component)e.getSource();
    //component�� bound�� ��� ���콺 �̺�Ʈ �߻������� ���Ѵ�.
    Rectangle rec = component.getBounds();
    int x = rec.x;
    //System.out.println("x-"+x);
    int y = rec.y;
    //System.out.println("y-"+y);
    int width = rec.width;
    //System.out.println("width-"+width);
    int height = rec.height;
    //System.out.println("height-"+height);
    int gab = 5;
    //�̺�Ʈ �߻� ����Ʈ
    int eX = e.getX();
    //System.out.println("eX-"+eX);
    int eY = e.getY();
    //System.out.println("eY-"+eY);
    if((eX>=0)&&(eX<=3)&&(eY>=0)&&(eY<=3)){
      component.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
    }else if((eX>=0)&&(eX<=3)&&(eY>=height-3)&&(eY<=height)){
      component.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
    }else if((eX>=width-3)&&(eX<=width)&&(eY>=height-3)&&(eY<=height)){
      component.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
    }else if((eX>=width-3)&&(eX<=width)&&(eY>=0)&&(eY<3)){
      component.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
    }else if((eX>3)&&(eX<width-3)&&(eY>=0)&&(eY<=3)){
      component.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
    }else if((eX>=0)&&(eX<=3)&&(eY>3)&&(eY<height-3)){
      component.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
    }else if((eX>3) && (eX<width-3) && (eY>=height-3) && (eY<=height)){
      component.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
    }else if((eX>=width-3)&&(eX<=width)&&(eY>3)&&(eY<height-3)){
      component.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
    }else if((eX>3)&&(eX<width-3)&&(eY>3)&&(eY<height-3)){
      component.setCursor(new Cursor(Cursor.MOVE_CURSOR));
    }
  }

  /**
   *  ���õ� compoent�� �ܰ����� �����ش�.
   */
  public void mouseClicked(MouseEvent e){
    JPanel panel = (JPanel)tester.getGlassPane();
    Component[] c = panel.getComponents();
    //glass pane�� ������Ʈ�� ������ getContentPane���� ������Ʈ�� �ű��.
    if(c.length != 0){
      
    }
    Component com = (Component)e.getSource();
    tester.getContentPane().remove(com);
    Rectangle r = com.getBounds();
    JPanel glass = (JPanel)tester.getGlassPane();
    glass.setVisible(true);
    Graphics g = glass.getGraphics();
    g.drawRect(r.x-1,r.y-1,r.width+2,r.height+2);
    glass.add(com);
  }
}
