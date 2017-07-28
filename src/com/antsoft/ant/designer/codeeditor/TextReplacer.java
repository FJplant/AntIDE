/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/TextReplacer.java,v 1.4 1999/08/02 09:43:17 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.main.MainFrame;

/**
 * text replace
 *
 * @author Jinwoo Baek
 * @author kim sang kyun
 */
public class TextReplacer {
     private Frame owner = MainFrame.mainFrame;
     private ReplaceTextDialog rtdlg;
     private JEditorPane editor;

     public TextReplacer(JEditorPane editor) {
       this.editor = editor;
       rtdlg = new ReplaceTextDialog(owner, MainFrame.findModel, MainFrame.replaceModel);
     }

    /**
     *  주어진 문자가 operator인지 확인?
     */
    private boolean checkOperatorChar(char ch) {
      boolean status = true;
      if ((ch >= 'A') && (ch <= 'Z')) status = false;
      else if ((ch >= 'a') && (ch <= 'z')) status = false;
      else if ((ch >= '0') && (ch <= '9')) status = false;
      else if ((ch == '$') || (ch == '_')) status = false;
      return status;
    }


     public void do_Action(SourceEntry se, JViewport vp, boolean isDlg){
      // 바꾸기 다이얼로그를 띄운다.
      int type = 0;
      if (isDlg) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = rtdlg.getSize();
        if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
        if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
        rtdlg.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);

      	type = rtdlg.showDialog();
      }
      else type = 1;
      switch(type) {
      case ReplaceTextDialog.FIND: // 찾기 수행
      {
        String toFind = rtdlg.getTextToFind();
        if ((toFind != null) && !toFind.trim().equals("")) {
          AntDocument doc = se.getDocument();
          int offset = -1;
          try {
            int caret = editor.getCaretPosition();
            if ((caret < 0) || (caret > doc.getLength())) caret = 0;
            String str = doc.getText(caret, doc.getLength() - caret + 1);
            // Case Sensitive 일 경우
            if (rtdlg.isCaseSensitive()) {
              // 단어 단위로 찾을 때
              if (rtdlg.isWholeWordOnly()) {
                boolean found = false;
                int i = -1;
                int po = caret;
                while(!found) {
                  i = str.indexOf(toFind);
                  if (i == -1) {
                    offset = -1; break;
                  }
                  boolean frontCheck = true;
                  boolean endCheck = true;
                  if (i > 0) frontCheck = checkOperatorChar(str.charAt(i - 1));
                  if (i + toFind.length() < str.length())
                    endCheck = checkOperatorChar(str.charAt(i + toFind.length()));

                  //  양끝이 적당한(?) 문자여야 whole word
                  if (frontCheck && endCheck) {
                    offset = i + po - caret;
                    found = true;
                  }
                  else {
                    po = po + i + 1;
                    str = doc.getText(po, doc.getLength() - po + 1);
                  }
                }
              }
              else offset = str.indexOf(toFind);
            }
            // Case Sensitive가 아닌 경우
            else {
              int i = -1;
              int po = caret;
              boolean found = false;
              while (!found) {
                int i0 = str.indexOf(toFind.toLowerCase().charAt(0));
                int i1 = str.indexOf(toFind.toUpperCase().charAt(0));
                if (i0 < 0) i0 = str.length();
                if (i1 < 0) i1 = str.length();
                i = Math.min(i0, i1);
                if (i + toFind.length() <= str.length()) {
                  String substr = str.substring(i, i + toFind.length());
                  if (toFind.equalsIgnoreCase(substr)) {
                    // 단어 단위로 찾을 때
                    if (rtdlg.isWholeWordOnly()) {
                      boolean frontCheck = true;
                      boolean endCheck = true;
                      if (i > 0) frontCheck = checkOperatorChar(str.charAt(i - 1));
                      if (i + toFind.length() < str.length())
                        endCheck = checkOperatorChar(str.charAt(i + toFind.length()));

                      //  양끝이 적당한(?) 문자여야 whole word
                      if (frontCheck && endCheck) {
                        offset = i + po - caret;
                        found = true;
                      }
                      else {
                        po = po + i + 1;
                        str = doc.getText(po, doc.getLength() - po + 1);
                      }
                    }
                    // 단어 단위로 찾지 않을 때
                    else {
                      offset = i + po - caret;
                      found = true;
                    }
                  }
                  else {
                    po = po + i + 1;
                    str = doc.getText(po, doc.getLength() - po + 1);
                  }
                }
                else {
                  offset = -1;
                  break;
                }
              }
            }
            if (offset != -1) {
              editor.select(offset + caret, offset + caret + toFind.length());
              // 찾은 위치로 Viewport 이동
              try {
                int po = se.getLineFromOffset(editor.getCaretPosition());
                po = (se.getElementAt(po)).getStartOffset();
                Rectangle rect = editor.modelToView(po);
                int coord_y = vp.getExtentSize().height / 2;
                coord_y = rect.y - coord_y;
                if (coord_y < 0) coord_y = 0;
                vp.setViewPosition(new Point(0, coord_y));
              } catch (BadLocationException e) {
                System.err.println("TextViewPanel : " + e);
              }
            }
            else {
              JOptionPane.showMessageDialog(owner, "Not Found", "Find Text", JOptionPane.ERROR_MESSAGE);
            }
          } catch (BadLocationException ble) {
            System.err.println("Exception occurred : " + ble);
          }
        }
      }
        break;
      case ReplaceTextDialog.REPLACE:
      {
        String toFind = rtdlg.getTextToFind();
        String toReplace = rtdlg.getTextToReplace();
        if ((toFind != null) && !toFind.trim().equals("") && (toReplace != null)) {
          AntDocument doc = se.getDocument();
          int count = 0;
          try {
            do {
              int caret = editor.getCaretPosition();
              if (caret == -1) caret = 0;
              String str = doc.getText(caret, doc.getLength() - caret);
              int offset = str.indexOf(toFind);
              if (offset != -1) {
                editor.setCaretPosition(offset + caret);// + toFind.length());
                // 먼저 지우기
                doc.remove(offset + caret, toFind.length());
                doc.insertString(offset + caret, toReplace);
                count++;

                try {
                  int po = se.getLineFromOffset(editor.getCaretPosition());
                  po = (se.getElementAt(po)).getStartOffset();
                  Rectangle rect = editor.modelToView(po);
                  int coord_y = vp.getExtentSize().height / 2;
                  coord_y = rect.y - coord_y;
                  if (coord_y < 0) coord_y = 0;
                  vp.setViewPosition(new Point(0, coord_y));
                } catch (BadLocationException e) {
                  System.err.println("TextViewPanel : " + e);
                }
              }
              else {
                JOptionPane.showMessageDialog(owner, "Replacement Completed\n" + count + " times replaced",
                                "Replace Text", JOptionPane.INFORMATION_MESSAGE);
                break;
              }
            } while(rtdlg.isReplaceAll());
          } catch (BadLocationException ble) {
            System.err.println("Exception occurred : " + ble);
          }
        }
      }
        break;
      case ReplaceTextDialog.CANCEL:
        break;
      }
      editor.requestFocus();
    }  
	}
