
//Title:        AntDebugger
//Version:
//Copyright:    Copyright (c) 1998
//Author:       배재형
//Company:      개미소프트
//Description:  안뇽하세요.


package com.antsoft.ant.debugger;

class BreakpointEntity {
  private boolean isBPInMethod = false;
  private String methodName = null;
  private int line = 0;

  public BreakpointEntity(String methodName) {
    this.methodName = methodName;
    isBPInMethod = true;
  }
  public BreakpointEntity(int line){
    this.line = line;
    isBPInMethod = false;
  }
  String getMethodName(){
    return methodName;
  }
  int getLine(){
    return line;
  }
  boolean isBreakpointInMethod(){
    return isBPInMethod;
  }

  public String toString(){
    return "BreakpointEntity : " + methodName + " : " + line + " : " + isBPInMethod;
  }

  public boolean equals(BreakpointEntity e ) {
    System.out.println("[B_P_Entity] equals : this : " + this + ": e : " + e);
    if( e.isBreakpointInMethod() == this.isBPInMethod ){
      if( isBPInMethod ){
        if( methodName.equals( e.getMethodName() ) ){
          return true;
        }else{
          return false;
        }
      }else{
        if( line == e.getLine() ) {
          return true;
        }else{
          return false;
        }
      }
    }else{
      return false;
    }
  }
}