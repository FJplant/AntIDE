/*
 * Defines the Symbol class, which is used to represent all terminals
 * and nonterminals while parsing.  The lexer should pass CUP Symbols
 * and CUP returns a Symol
 *
 * @version last updated: 7/3/96
 * @author  Frank Flannery
 */

package com.antsoft.ant.codecontext;

/**
  @author Kim, Sung-Hoon.
  */
public class SortingData {
  String data = null;

  public SortingData(String data) {
    this.data = data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

  public String toString() {
    return data.toLowerCase();
  }
}






