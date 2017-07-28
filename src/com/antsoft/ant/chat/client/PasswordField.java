package com.antsoft.ant.chat.client;

import javax.swing.JTextField;
import javax.swing.text.*;

public class PasswordField extends JTextField {

  private String text="";
  public PasswordField(int cols) {
    super(cols);
  }

  public String getText(){
    return text;
  }

  public void clearText(){
    text = "";
  }  

  protected Document createDefaultModel() {
    return new MaskDocument('*');
  }

  class MaskDocument extends PlainDocument {

    char c;
    public MaskDocument(char maskChar){
      c = maskChar;
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
      if (str == null) {    
        return;
      }
      
      text += str;

      String tempStr = "";

      for(int i=0; i<str.length(); i++){
        tempStr += c;
      }
      super.insertString(offs, tempStr, a);
    }
  }
}

