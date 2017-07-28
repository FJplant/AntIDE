
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Jinwoo Baek
//Company:      Antsoft
//Description:  Your description

package com.antsoft.ant.debugger;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

/**
 *  OutputFrame
 *
 *  @author Jinwoo Baek
 */
public class OutputFrame extends JFrame implements WindowListener {
  // Components
  JTabbedPane tabbedPane = new JTabbedPane();
  JScrollPane scroller1 = new JScrollPane();
  JScrollPane scroller2 = new JScrollPane();
  JTextArea consoleOut = new JTextArea();
  JTextArea debugOut = new JTextArea();

  // Data Members
  PrintStream outStream;
  PrintStream consoleStream;

  public OutputFrame() {
    try  {
      jbInit();
      pack();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setTitle("Debug Output");
    debugOut.setFont(new java.awt.Font("DialogInput", 0, 12));
    consoleOut.setFont(new java.awt.Font("DialogInput", 0, 12));
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.add(scroller1, "Output");
    scroller1.getViewport().add(debugOut, null);
    tabbedPane.add(scroller2, "Console");
    scroller2.getViewport().add(consoleOut, null);

    outStream = new PrintStream(new OutStreamWriter(debugOut.getDocument()));
    consoleStream = new PrintStream(new OutStreamWriter(consoleOut.getDocument()));
  }

  public PrintStream getOutStream() {
    return outStream;
  }

  public PrintStream getConsoleStream() {
    return consoleStream;
  }

  /**
   *  output 처리를 위한 클래스
   *
   *  @author Jinwoo Baek
   */
  class OutStreamWriter extends OutputStream {
    Document doc;
    OutStreamWriter(Document doc) {
      this.doc = doc;
    }

    // OutputStream의 필수 구현 함수 (abstract)
    public void write(int b) throws IOException {
      if (doc != null) {
        try {
          doc.insertString(doc.getLength(), String.valueOf(b), null);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    }

    public void write(byte[] b) throws IOException {
      if (doc != null) {
        try {
          doc.insertString(doc.getLength(), new String(b), null);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    }

    public void write(byte[] b, int off, int len) {
      if (doc != null) {
        try {
          doc.insertString(doc.getLength(), new String(b, off, len), null);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void windowOpened(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowClosing(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowClosed(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
    setVisible(false);
  }

  public void windowIconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeiconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowActivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeactivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  /**
   *  Console Output 처리를 위한 클래스
   *
   *  @author Jinwoo Baek
   * /
  class ConsoleStreamWriter extends OutputStream {
    Document doc;

    ConsoleStreamWriter() {
      doc = consoleOut.getDocument();
    }

    // OutputStream의 필수 구현 함수 (abstract)
    public void write(int b) throws IOException {
      if (doc != null) {
        try {
          doc.insertString(doc.getLength(), new String(b), null);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    }

    public void write(byte[] b) throws IOException {
      if (doc != null) {
        try {
          doc.insertString(doc.getLength(), new String(b), null);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    }
  }
  */
}
