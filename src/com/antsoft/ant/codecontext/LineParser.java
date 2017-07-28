/*
 * $Header: /AntIDE/source/ant/codecontext/LineParser.java 9     99-05-17 12:15a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 9 $
 */
package com.antsoft.ant.codecontext;				    

import java.util.Stack;

/**
  @author Kim, Sung-Hoon.
  */
public class LineParser {

	public boolean inMethodDeclaration=false;
	private boolean stateMethodInnerFlag=false;
				    
	/* 
	  Fetch an action from the action table.  The table is broken up into
	  rows, one per state (rows are indexed directly by state number).  
	  Within each row, a list of index, value pairs are given (as sequential
	  entries in the table), and the list is terminated by a default entry 
	  (denoted with a Symbol index of -1).  To find the proper entry in a row 
	  we do a linear or binary search (depending on the size of the row).  
	
	  @param state the state index of the action being accessed.
	  @param sym   the Symbol index of the action being accessed.
	*/
	final short get_action(int state, int sym) { 
		short tag;
		int first, last, probe;
		short[] row = _action_table[state];

		// linear search if we are < 10 entries.
		if (row.length < 20)
			for (probe = 0; probe < row.length; probe++) {
				// is this entry labeled with our Symbol or the default?.
				tag = row[probe++];
				if (tag == sym || tag == -1) {
					return row[probe];		// return the next entry.
				}
			}
		else { 			// otherwise binary search.
			first = 0; 
			last = (row.length-1)/2 - 1;  // leave out trailing default entry.
			while (first <= last) {
				probe = (first+last)/2;

				if (sym == row[probe*2]) return row[probe*2+1];
				else if (sym > row[probe*2]) first = probe+1;
				else last = probe-1;
			}

			// not found, use the default at the end.
			return row[row.length-1];
		}

		// shouldn't happened,but if we run off the end we return the default
		return 0;		// error = 0.
	}

	/* 
	  Fetch a state from the reduce-goto table.  The table is broken up into
	  rows, one per state (rows are indexed directly by state number).  
	  Within each row, a list of index, value pairs are given (as sequential
	  entries in the table), and the list is terminated by a default entry 
	  (denoted with a Symbol index of -1).  To find the proper entry in a row 
	  we do a linear search.  
  
	  @param state the state index of the entry being accessed.
	  @param sym   the Symbol index of the entry being accessed.
	 */
	final short get_reduce(int state, int sym) {
		short tag;
		short[] row = _reduce_table[state];

		// if we have a null row we go with the default.
		if (row == null) return -1;

		for (int probe = 0; probe < row.length; probe++) {
			// is this entry labeled with our Symbol or the default?.
			tag = row[probe++];
			if (tag == sym || tag == -1) {
				return row[probe]; // return the next entry.
			}
		}
		// if we run off the end we return the default (error == -1).
		return -1;
	}

	/**
	  Constructor.

	  @param str string to be parsed.
	  */
	public LineParser(String str,boolean flag) { 
		//System.out.println("string = "+str);
		lt=new LineTokenizer(str);	// scanning with LineTokenizer()

		// maintained the symbol table of line currently being parsed.
		table=new SymbolTableEntry();
		table.accessTyp="";
		table.typ="";

		stateMethodInnerFlag=flag;
		inMethodDeclaration=false;
	}

	// scanner
	LineTokenizer lt;

	// Internal flag to indicate when parser should quit.
	boolean _done_parsing = false;

	// This method is called to indicate that the parser should quit. 
	void done_parsing() { _done_parsing = true; }

	// top of Stack.
	int tos;

	// Symbol Table.
	SymbolTableEntry table;

	// The current lookahead Symbol.
	Symbol cur_token;

	// The parse stack itself.
	Stack stack = new Stack();

	boolean isFunction=false;
	boolean isImport=false;

	/** 
	  This method provides the main parsing routine.  
	  It returns only when done_parsing() has been called
	  (typically because the parser has accepted, 
	  or a fatal error has been reported).
	  See the header documentation for the class regarding how shift/reduce 
	  parsers operate and how the various tables are used.

	  @return Symbol Table value as the SymbolTableEntry class.
	  */
	public SymbolTableEntry parse() {
		int act;

		// the Symbol/stack element returned by a reduce.
		Symbol lhs_sym = null;

		// information about production being reduced with.
		short handle_size, lhs_sym_num;

		cur_token = scan(); 

		// push dummy Symbol with start state to get us underway.
		stack.removeAllElements();
		stack.push(new Symbol(0, start_state()));
		tos = 0;

		// continue until we are told to stop.
		for (_done_parsing = false;!_done_parsing;) {
			int currentState=((Symbol)stack.peek()).parse_state;
			act = get_action(currentState, cur_token.sym);

			if (act > 0) { 		// if greater than zero, it encodes a shift action.
				// shift to the encoded state by pushing it on the stack.
				cur_token.parse_state = act-1;
				stack.push(cur_token);
				tos++;

				if (cur_token.sym==Sym.LP) isFunction=true;
				if (cur_token.sym==Sym.IMPORT||cur_token.sym==Sym.PACKAGE) isImport=true;

				cur_token = scan(); // advance to the next Symbol
			}
			else if (act < 0) {	// if less than zero, it encodes a reduce action.
				lhs_sym = do_action((-act)-1, tos);

				// look up information about the production.
				lhs_sym_num = _production_table[(-act)-1][0];
				handle_size = _production_table[(-act)-1][1];

				// pop the handle off the stack.
				for (int i = 0; i < handle_size; i++) {
					stack.pop();
					tos--;
				}
	      
				// look up the state to go to from the one popped back to.
				act = get_reduce(((Symbol)stack.peek()).parse_state, lhs_sym_num);

				// shift to that state.
				lhs_sym.parse_state = act;
				stack.push(lhs_sym);
				tos++;
			}
			else if (act == 0) { // if zero, we have an error.
				// syntax_error in the absolute grammar.
				// but, case by case, the problem is treated.
				
				inMethodDeclaration=false;
				if (exceptionProcessing(currentState)) return table;
				else {
          table.setMemberSort(SymbolTableEntry.OTHERS);
          return table;
        }
			}
		}
		key=((String)lhs_sym.value).toString();
		return table;
	}

	String restString=null;
	public boolean endsWithComma=false;

	/**
	  getting the rest part of string not to parse.

	  @return the rest string.
	  */
	public String getRestString() {
		return restString;
	}

	// Nonreducible condition is processed here.
	boolean exceptionProcessing(int state) {
		Symbol s;

        StringBuffer buf;
		//System.out.println("the state = "+state);
		//System.out.println("the key = "+(String)((Symbol)stack.peek()).value);
		switch (state) {
			case 91:	// public class xxx extends
      case 76:	// class xxx extends
				s=(Symbol)stack.pop();
				key=(String)s.value;
				table.setMemberSort(SymbolTableEntry.CLASS);
        table.setType(key);
        if (cur_token.sym==Sym.EXTENDS) {
          cur_token=scan();
          table.setSuperClass(lt.currentToken);
          cur_token=scan();
        }
        else table.setSuperClass("java.lang.Object");
        if (cur_token.sym==Sym.IMPLEMENTS) {
          do {
            cur_token=scan();
            if (cur_token.sym==Sym.ID) table.addImplementsInterface(lt.currentToken);
          } while (cur_token.sym!=Sym.EOL);
        }

				return true;

			case 90:	// public interface xxx extends
            case 73:	// interface xxx extends
				s=(Symbol)stack.pop();
				key=(String)s.value;
				table.setMemberSort(SymbolTableEntry.INTERFACE);
        table.setType(key);
        if (cur_token.sym==Sym.EXTENDS) {
          cur_token=scan();
          table.setSuperClass(lt.currentToken);
        }
        else table.setSuperClass("java.lang.Object");

				return true;

			case 66:
			case 67:
				if (isFunction) {
					if (stateMethodInnerFlag) return false;

					inMethodDeclaration=true;

					buf=new StringBuffer();
					do {
						s=(Symbol)stack.pop();
						buf.insert(0,((String)s.value).toString());
					} while (s.parse_state!=29&&s.parse_state!=77);
							//&&s.parse_state!=92&&s.parse_state!=78);
					key=buf.toString();

					if (s.parse_state==29)
						table.setMemberSort(SymbolTableEntry.CONSTRUCTOR);
					else {
						s=(Symbol)stack.pop();
						table.setType(((String)s.value).toString());
						table.setMemberSort(SymbolTableEntry.METHOD);
					}
					return true;
				}

				// not function
				buf=new StringBuffer();
				do {
					s=(Symbol)stack.pop();
					buf.insert(0,((String)s.value).toString());
				} while (s.parse_state!=79&&s.parse_state!=93);
				key=buf.toString();

				if (state==66) {
					cur_token=scan();
					while (cur_token.sym!=Sym.EOL&&cur_token.sym!=Sym.ASSIGN
								&&cur_token.sym!=Sym.COMMA) {
						if (cur_token.sym==Sym.LS) key=key+"[";
						if (cur_token.sym==Sym.RS) key=key+"]";
						cur_token=scan();
					}
				}

				s=(Symbol)stack.pop();
				table.setType((String)s.value);
				table.setMemberSort(SymbolTableEntry.FIELD);

				if (cur_token.sym==Sym.ASSIGN) {
					StringBuffer restBuffer=new StringBuffer();
					cur_token=scan();
					while (cur_token.sym!=Sym.EOL) {
						restBuffer.append(lt.currentToken+" ");
						cur_token=scan();
					}
					restString=restBuffer.toString();
				}
				return true;

			case 77:
			case 79:
			case 93:
				s=(Symbol)stack.pop();
				key=(String)s.value;
				s=(Symbol)stack.pop();
				table.setType((String)s.value);
				table.setMemberSort(SymbolTableEntry.FIELD);

				if (cur_token.sym==Sym.COMMA) endsWithComma=true;
				if (cur_token.sym==Sym.COMMA||cur_token.sym==Sym.ASSIGN) {
					StringBuffer restBuffer=new StringBuffer();
					cur_token=scan();
					while (cur_token.sym!=Sym.EOL) {
						restBuffer.append(lt.currentToken+" ");
						cur_token=scan();
					}
					restString=restBuffer.toString();
				}

				return true;

//			case 25:
			case 52:
			case 53:
			case 96:
			case 97:
				if (isImport) {
					buf=new StringBuffer();
  				s=(Symbol)stack.pop();
					do {
						buf.insert(0,((String)s.value).toString());
    				s=(Symbol)stack.pop();
					} while (s.parse_state!=2&&s.parse_state!=42);
					key=buf.toString();

					if (s.parse_state==2) table.setMemberSort(SymbolTableEntry.IMPORT);
					else table.setMemberSort(SymbolTableEntry.PACKAGE);

					return true;
				}

			case 65:
				stack.pop();
			case 25:	// class or interface type.
 				if (isImport) {
					buf=new StringBuffer();
					s=(Symbol)stack.pop();
					do {
						buf.insert(0,((String)s.value).toString());
						s=(Symbol)stack.pop();
					} while (s.parse_state!=2&&s.parse_state!=42);
  				key=buf.toString();

					if (s.parse_state==2) table.setMemberSort(SymbolTableEntry.IMPORT);
					else table.setMemberSort(SymbolTableEntry.PACKAGE);

					return true;
				}
			case 19:	// int
			case 37:	// short
			case 20:	// long
			case 23:	// byte
			case 7:		// char
			case 43:	// float
			case 26:	// double
			case 18:	// boolean

			case 61:	// comma

			case 55:	// Constructor LP
			case 82:	// method left parenthesis
				//if (isFunction) inMethodDeclaration=true;

			case 59:	// Constructor RP
			case 83:	// method right parenthesis
			case 85:	// method right parenthesis()
			case 62:	// constructor right parenthesis()

			case 75:	// PRIMITIVE[]
			case 74:	// PRIMITIVE[
			case 72:	// CLASS[]
			case 71:	// CLASS[
			case 80:	// Method[
			case 81:	// Method[]
				if (isFunction) {
					if (stateMethodInnerFlag) return false;

					if (state==59||state==83||state==85||state==62
							||state==80||state==81) inMethodDeclaration=false;
					else inMethodDeclaration=true;

					buf=new StringBuffer();
					do {
						s=(Symbol)stack.pop();
						buf.insert(0,((String)s.value).toString());
					} while (s.parse_state!=29&&s.parse_state!=77
							&&s.parse_state!=92&&s.parse_state!=78);
					key=buf.toString();

					//System.out.println("key is ="+key);

					if (s.parse_state==29)
						table.setMemberSort(SymbolTableEntry.CONSTRUCTOR);
					else {
						s=(Symbol)stack.pop();
						table.setType(((String)s.value).toString());
						table.setMemberSort(SymbolTableEntry.METHOD);
					}
					return true;
				}

			default: /*table=null;*/ return false;
		}
	}

	// getting start state.
	int start_state() { return 0; }

	// scan to get the next Symbol.
	Symbol scan() {
		return new Symbol(lt.nextToken(),lt.getTokenString());
	}

	String key=null;

	/**
	  get the key value.

	  @return key value as the String value.
	  */
	public String getKey() {
		return key;
	}

	/* 
	  Method with the actual generated action code. 

	  @param act_num action number to be taken.
	  @return the result of the taken action as the Symbol type.
	 */
	Symbol do_action(int act_num,int top) {
		Symbol result;

      /* select the action based on the action number */
      switch (act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 67: // arrayType ::= arrayType LS RS 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"[]";
	
              	result = new Symbol(26/*arrayType*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 66: // arrayType ::= name LS RS 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"[]";
	
              	result = new Symbol(26/*arrayType*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 65: // arrayType ::= primitiveType LS RS 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"[]";
	
              	result = new Symbol(26/*arrayType*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 64: // classOrInterfaceType ::= name 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(25/*classOrInterfaceType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 63: // referenceType ::= arrayType 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(24/*referenceType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 62: // referenceType ::= classOrInterfaceType 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				RESULT=e1.toString();
	
              	result = new Symbol(24/*referenceType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 61: // primitiveType ::= DOUBLE 
            {
              	String RESULT = null;
		
				RESULT="double";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 60: // primitiveType ::= FLOAT 
            {
              	String RESULT = null;
		
				RESULT="float";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 59: // primitiveType ::= BOOLEAN 
            {
              	String RESULT = null;
		
				RESULT="boolean";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 58: // primitiveType ::= BYTE 
            {
              	String RESULT = null;
		
				RESULT="byte";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 57: // primitiveType ::= CHAR 
            {
              	String RESULT = null;
		
				RESULT="char";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 56: // primitiveType ::= SHORT 
            {
              	String RESULT = null;
		
				RESULT="short";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 55: // primitiveType ::= LONG
            {
              	String RESULT = null;
		
				RESULT="long";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 54: // primitiveType ::= INT 
            {
              	String RESULT = null;
		
				RESULT="int";
	
              	result = new Symbol(23/*primitiveType*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 53: // type ::= VOID 
            {
              	String RESULT = null;
		
				RESULT="void";
	
              	result = new Symbol(22/*type*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 52: // type ::= referenceType 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
				//table.setType(e1.toString());
	
              	result = new Symbol(22/*type*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 51: // type ::= primitiveType 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
				//table.setType(e1.toString());
	
              	result = new Symbol(22/*type*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 50: // interfaceDeclaration ::= INTERFACE ID
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
				table.setMemberSort(SymbolTableEntry.INTERFACE);
    			table.setType(e1.toString());
				key=e1.toString();

              	result = new Symbol(21/*interfaceDeclaration*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 49: // interfaceDeclaration ::= modifiers INTERFACE ID
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				RESULT=e1.toString();
				table.setMemberSort(SymbolTableEntry.INTERFACE);
    			table.setType(e1.toString());
				key=e1.toString();

              	result = new Symbol(21/*interfaceDeclaration*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 48: // constructorDeclarator ::= simpleName LP RP
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"()";
	
              	result = new Symbol(20/*constructorDeclarator*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 47: // constructorDeclarator ::= simpleName LP formalParameterList RP
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-3)).left;
				int e1right = ((Symbol)stack.elementAt(top-3)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-3)).value;
				int e2left = ((Symbol)stack.elementAt(top-1)).left;
				int e2right = ((Symbol)stack.elementAt(top-1)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-1)).value;
		
				RESULT=e1.toString()+"("+e2.toString()+")";
	
              	result = new Symbol(20/*constructorDeclarator*/, 
              						((Symbol)stack.elementAt(top-3)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 46: // constructorDeclaration ::= constructorDeclarator 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setMemberSort(SymbolTableEntry.CONSTRUCTOR);
				key=e1.toString();
		
				RESULT=e1.toString();
	
              	result = new Symbol(19/*constructorDeclaration*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 45: // constructorDeclaration ::= modifiers constructorDeclarator 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setMemberSort(SymbolTableEntry.CONSTRUCTOR);
				key=e1.toString();
		
				RESULT=e1.toString();
	
              	result = new Symbol(19/*constructorDeclaration*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 44: // formalParameter ::= modifiers type variableDeclaratorId 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;

				String temp=e2.toString();
				//System.out.println("e2 is = "+temp);
				StringBuffer buf=new StringBuffer();

				if (temp.indexOf("[")!=-1) {
					temp=temp.substring(temp.indexOf("["),temp.length());
					for (int i=0;i<temp.length();++i) {
						if (temp.charAt(i)=='[') buf.append("[");
						if (temp.charAt(i)==']') buf.append("]");
					}
				}
		
				RESULT=e1.toString()+buf.toString();
	
              	result = new Symbol(18/*formalParameter*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 43: // formalParameter ::= type variableDeclaratorId 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;

				String temp=e2.toString();
				//System.out.println("e2 is = "+temp);
				StringBuffer buf=new StringBuffer();

				if (temp.indexOf("[")!=-1) {
					temp=temp.substring(temp.indexOf("["),temp.length());
					for (int i=0;i<temp.length();++i) {
						if (temp.charAt(i)=='[') buf.append("[");
						if (temp.charAt(i)==']') buf.append("]");
					}
				}
		
				RESULT=e1.toString()+buf.toString();
	
              	result = new Symbol(18/*formalParameter*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 42: // formalParameterList ::= formalParameter 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(17/*formalParameterList*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 41: // formalParameterList ::= formalParameterList COMMA formalParameter 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString()+","+e2.toString();
	
              	result = new Symbol(17/*formalParameterList*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 40: // methodDeclarator ::= methodDeclarator LS RS 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"[]";
	
              	result = new Symbol(16/*methodDeclarator*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 39: // methodDeclarator ::= ID LP RP 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"()";
	
              	result = new Symbol(16/*methodDeclarator*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 38: // methodDeclarator ::= ID LP formalParameterList RP 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-3)).left;
				int e1right = ((Symbol)stack.elementAt(top-3)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-3)).value;
				int e2left = ((Symbol)stack.elementAt(top-1)).left;
				int e2right = ((Symbol)stack.elementAt(top-1)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-1)).value;
			
				RESULT=e1.toString()+"("+e2.toString()+")";
	
              	result = new Symbol(16/*methodDeclarator*/, 
              						((Symbol)stack.elementAt(top-3)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 37: // methodHeader ::= type methodDeclarator 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setType(e1.toString());
				table.setMemberSort(SymbolTableEntry.METHOD);
		
				RESULT=e2.toString();
	
              	result = new Symbol(15/*methodHeader*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 36: // methodHeader ::= modifiers type methodDeclarator 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				table.setType(e1.toString());
				table.setMemberSort(SymbolTableEntry.METHOD);
	
				RESULT=e2.toString();
	
              	result = new Symbol(15/*methodHeader*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 35: // methodDeclaration ::= methodHeader 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				key=e1.toString();

				RESULT=e1.toString();
	
              	result = new Symbol(14/*methodDeclaration*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 34: // variableDeclaratorId ::= variableDeclaratorId LS RS 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
		
				RESULT=e1.toString()+"[]";
	
              	result = new Symbol(13/*variableDeclaratorId*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 33: // variableDeclaratorId ::= ID 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(13/*variableDeclaratorId*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 32: // fieldDeclaration ::= type variableDeclaratorId 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;

				key=e2.toString();
				//table.setType(e1.toString());
				table.setMemberSort(SymbolTableEntry.FIELD);
				String type=e1.toString();

				if (key.indexOf("[")!=-1) {
					String temp=key.substring(key.indexOf("["),key.length());
					key=key.substring(0,key.indexOf("["));
					for (int i=0;i<temp.length();++i) {
						if (temp.charAt(i)=='[') type=type+"[";
						if (temp.charAt(i)==']') type=type+"]";
					}
				}

				table.setType(type);
				//System.out.println(type+"  "+key);
		
				RESULT=e2.toString();
	
              	result = new Symbol(12/*fieldDeclaration*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 31: // fieldDeclaration ::= modifiers type variableDeclaratorId 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;

				//table.setType(e1.toString());
				table.setMemberSort(SymbolTableEntry.FIELD);
				key=e2.toString();
				String type=e1.toString();

				if (key.indexOf("[")!=-1) {
					String temp=key.substring(key.indexOf("["),key.length());
					key=key.substring(0,key.indexOf("["));
					for (int i=0;i<temp.length();++i) {
						if (temp.charAt(i)=='[') type=type+"[";
						if (temp.charAt(i)==']') type=type+"]";
					}
				}

				table.setType(type);
				//System.out.println(e1.toString()+"  "+key);
		
				RESULT=e2.toString();
	
              	result = new Symbol(12/*fieldDeclaration*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 30: // modifier ::= SYNCHRONIZED 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 29: // modifier ::= NATIVE 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 28: // modifier ::= ABSTRACT 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 27: // modifier ::= VOLATILE 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // modifier ::= TRANSIENT 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // modifier ::= FINAL 
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // modifier ::= STATIC
            {
              	String RESULT = null;

              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // modifier ::= PRIVATE 
            {
              	String RESULT = null;
		
				table.setAccessType("private");
	
              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // modifier ::= PROTECTED 
            {
              	String RESULT = null;
		
				table.setAccessType("protected");
	
              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // modifier ::= PUBLIC 
            {
              	String RESULT = null;
		
				table.setAccessType("public");
	
              	result = new Symbol(11/*modifier*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // modifiers ::= modifiers modifier 
            {
              	String RESULT = null;

              	result = new Symbol(10/*modifiers*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // modifiers ::= modifier 
            {
              	String RESULT = null;

              	result = new Symbol(10/*modifiers*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // classDeclaration ::= CLASS ID 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setMemberSort(SymbolTableEntry.CLASS);
        		table.setType(e1.toString());
				key=e1.toString();

				RESULT=e1.toString();

              	result = new Symbol(9/*classDeclaration*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // classDeclaration ::= modifiers CLASS ID
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setMemberSort(SymbolTableEntry.CLASS);
        		table.setType(e1.toString());
				key=e1.toString();

				RESULT=e1.toString();

              	result = new Symbol(9/*classDeclaration*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // typeImportOnDemandDeclaration ::= IMPORT name DOT MUL SEMIC
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-3)).left;
				int e1right = ((Symbol)stack.elementAt(top-3)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-3)).value;

				RESULT=e1.toString()+".*";

              	result = new Symbol(8/*typeImportOnDemandDeclaration*/, 
              						((Symbol)stack.elementAt(top-4)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // singleTypeImportDeclaration ::= IMPORT name SEMIC
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
		
				RESULT=e1.toString();
		
              	result = new Symbol(7/*singleTypeImportDeclaration*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // importDeclaration ::= typeImportOnDemandDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				table.setMemberSort(SymbolTableEntry.IMPORT);
				key=e1.toString();
		
				RESULT=e1.toString();
	
              	result = new Symbol(6/*importDeclaration*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // importDeclaration ::= singleTypeImportDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				table.setMemberSort(SymbolTableEntry.IMPORT);
				key=e1.toString();
		
				RESULT=e1.toString();
	
              	result = new Symbol(6/*importDeclaration*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // qualifiedName ::= name DOT ID 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-2)).left;
				int e1right = ((Symbol)stack.elementAt(top-2)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-2)).value;
				int e2left = ((Symbol)stack.elementAt(top-0)).left;
				int e2right = ((Symbol)stack.elementAt(top-0)).right;
				String e2 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString()+"."+e2.toString();

              	result = new Symbol(5/*qualifiedName*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // simpleName ::= ID
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				RESULT=e1.toString();

              	result = new Symbol(4/*simpleName*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // name ::= qualifiedName
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;

				RESULT=e1.toString();

              	result = new Symbol(3/*name*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // name ::= simpleName 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(3/*name*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // packageDeclaration ::= PACKAGE name SEMIC 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-1)).left;
				int e1right = ((Symbol)stack.elementAt(top-1)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-1)).value;
		
				table.setMemberSort(SymbolTableEntry.PACKAGE);
				key=e1.toString();
		
				RESULT=e1.toString();
	
              	result = new Symbol(2/*packageDeclaration*/, 
              						((Symbol)stack.elementAt(top-2)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // compilationUnit ::= methodDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // compilationUnit ::= fieldDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // compilationUnit ::= constructorDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // compilationUnit ::= interfaceDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // compilationUnit ::= classDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // compilationUnit ::= importDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= compilationUnit EOF 
            {
              	Object RESULT = null;
				int start_valleft = ((Symbol)stack.elementAt(top-1)).left;
				int start_valright = ((Symbol)stack.elementAt(top-1)).right;
				String start_val = (String)((Symbol) stack.elementAt(top-1)).value;
				RESULT = start_val;
              	result = new Symbol(0/*$START*/, 
              						((Symbol)stack.elementAt(top-1)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }

          	done_parsing();
          return result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // compilationUnit ::= packageDeclaration 
            {
              	String RESULT = null;
				int e1left = ((Symbol)stack.elementAt(top-0)).left;
				int e1right = ((Symbol)stack.elementAt(top-0)).right;
				String e1 = (String)((Symbol) stack.elementAt(top-0)).value;
		
				RESULT=e1.toString();
	
              	result = new Symbol(1/*compilationUnit*/, 
              						((Symbol)stack.elementAt(top-0)).left, 
              						((Symbol)stack.elementAt(top-0)).right, RESULT);
            }
          return result;

          /* . . . . . .*/
          default: return null;
		}
	}

  	/** production table */
  	protected static final short _production_table[][] = {
        {1, 1},     {0, 2},     {1, 1},     {1, 1},     {1, 1}, 
        {1, 1},     {1, 1},     {1, 1},     {2, 3},     {3, 1}, 
        {3, 1},     {4, 1},     {5, 3},     {6, 1},     {6, 1}, 
        {7, 3},     {8, 5},     {9, 3},     {9, 2},     {10, 1}, 
        {10, 2},     {11, 1},     {11, 1},     {11, 1},     {11, 1}, 
        {11, 1},     {11, 1},     {11, 1},     {11, 1},     {11, 1}, 
        {11, 1},     {12, 3},     {12, 2},     {13, 1},     {13, 3}, 
        {14, 1},     {15, 3},     {15, 2},     {16, 4},     {16, 3}, 
        {16, 3},     {17, 3},     {17, 1},     {18, 2},     {18, 3}, 
        {19, 2},     {19, 1},     {20, 4},     {20, 3},     {21, 3}, 
        {21, 2},     {22, 1},     {22, 1},     {22, 1},     {23, 1}, 
        {23, 1},     {23, 1},     {23, 1},     {23, 1},     {23, 1}, 
        {23, 1},     {23, 1},     {24, 1},     {24, 1},     {25, 1}, 
        {26, 3},     {26, 3},     {26, 3}  };

  	/** parse action table */
  	protected static final short[][] _action_table = {
    /*0*/{2,26,53,17,54,19,56,24,59,8,60,6,64,27,68,25,70,44,74,3,76,20,77,12,78,21,79,29,82,43,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*1*/{2,-20,53,-20,54,-20,56,-20,59,-20,60,-20,64,-20,68,-20,70,-20,76,-20,77,-20,78,-20,79,-20,83,-20,84,-20,85,-20,87,-20,88,-20,90,-20,95,-20,98,-20,99,-20,-1,0},
    /*2*/{2,26,-1,0},
    /*3*/{2,26,53,17,54,19,56,24,59,8,60,88,64,27,68,25,70,44,76,20,77,89,78,21,79,29,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*4*/{2,78,-1,0},
    /*5*/{2,77,-1,0},
    /*6*/{2,-11,48,-11,50,-11,52,-11,-1,0},
    /*7*/{2,-58,48,-58,-1,0},
    /*8*/{2,-28,53,-28,54,-28,56,-28,59,-28,60,-28,64,-28,68,-28,70,-28,76,-28,77,-28,78,-28,79,-28,83,-28,84,-28,85,-28,87,-28,88,-28,90,-28,95,-28,98,-28,99,-28,-1,0},
    /*9*/{2,-52,48,75,-1,0},
    /*10*/{0,-3,-1,0},
    /*11*/{2,74,-1,0},
    /*12*/{2,-63,-1,0},
    /*13*/{2,-25,53,-25,54,-25,56,-25,59,-25,60,-25,64,-25,68,-25,70,-25,76,-25,77,-25,78,-25,79,-25,83,-25,84,-25,85,-25,87,-25,88,-25,90,-25,95,-25,98,-25,99,-25,-1,0},
    /*14*/{0,-15,-1,0},
    /*15*/{0,-47,-1,0},
    /*16*/{2,-29,53,-29,54,-29,56,-29,59,-29,60,-29,64,-29,68,-29,70,-29,76,-29,77,-29,78,-29,79,-29,83,-29,84,-29,85,-29,87,-29,88,-29,90,-29,95,-29,98,-29,99,-29,-1,0},
    /*17*/{2,-22,53,-22,54,-22,56,-22,59,-22,60,-22,64,-22,68,-22,70,-22,76,-22,77,-22,78,-22,79,-22,83,-22,84,-22,85,-22,87,-22,88,-22,90,-22,95,-22,98,-22,99,-22,-1,0},
    /*18*/{2,-60,48,-60,-1,0},
    /*19*/{2,-55,48,-55,-1,0},
    /*20*/{2,-56,48,-56,-1,0},
    /*21*/{2,-65,48,72,52,53,-1,0},
    /*22*/{2,-27,53,-27,54,-27,56,-27,59,-27,60,-27,64,-27,68,-27,70,-27,76,-27,77,-27,78,-27,79,-27,83,-27,84,-27,85,-27,87,-27,88,-27,90,-27,95,-27,98,-27,99,-27,-1,0},
    /*23*/{2,-59,48,-59,-1,0},
    /*24*/{2,-26,53,-26,54,-26,56,-26,59,-26,60,-26,64,-26,68,-26,70,-26,76,-26,77,-26,78,-26,79,-26,83,-26,84,-26,85,-26,87,-26,88,-26,90,-26,95,-26,98,-26,99,-26,-1,0},
    /*25*/{2,-12,44,-12,48,-12,50,-12,52,-12,-1,0},
    /*26*/{2,-62,48,-62,-1,0},
    /*27*/{0,-7,-1,0},
    /*28*/{2,-30,53,-30,54,-30,56,-30,59,-30,60,-30,64,-30,68,-30,70,-30,76,-30,77,-30,78,-30,79,-30,83,-30,84,-30,85,-30,87,-30,88,-30,90,-30,95,-30,98,-30,99,-30,-1,0},
    /*29*/{2,-10,44,56,48,-10,52,-10,-1,0},
    /*30*/{0,-14,-1,0},
    /*31*/{0,-4,-1,0},
    /*32*/{0,-36,-1,0},
    /*33*/{2,-23,53,-23,54,-23,56,-23,59,-23,60,-23,64,-23,68,-23,70,-23,76,-23,77,-23,78,-23,79,-23,83,-23,84,-23,85,-23,87,-23,88,-23,90,-23,95,-23,98,-23,99,-23,-1,0},
    /*34*/{2,-31,53,-31,54,-31,56,-31,59,-31,60,-31,64,-31,68,-31,70,-31,76,-31,77,-31,78,-31,79,-31,83,-31,84,-31,85,-31,87,-31,88,-31,90,-31,95,-31,98,-31,99,-31,-1,0},
    /*35*/{0,-1,-1,0},
    /*36*/{2,-53,-1,0},
    /*37*/{2,-57,48,-57,-1,0},
    /*38*/{0,-5,-1,0},
    /*39*/{0,-8,-1,0},
    /*40*/{2,-24,53,-24,54,-24,56,-24,59,-24,60,-24,64,-24,68,-24,70,-24,76,-24,77,-24,78,-24,79,-24,83,-24,84,-24,85,-24,87,-24,88,-24,90,-24,95,-24,98,-24,99,-24,-1,0},
    /*41*/{0,55,-1,0},
    /*42*/{2,26,-1,0},
    /*43*/{2,-61,48,-61,-1,0},
    /*44*/{2,-54,-1,0},
    /*45*/{2,-64,48,48,-1,0},
    /*46*/{0,-6,-1,0},
    /*47*/{49,49,-1,0},
    /*48*/{2,-68,48,-68,-1,0},
    /*49*/{50,52,52,53,-1,0},
    /*50*/{2,-10,48,-10,50,-10,52,-10,-1,0},
    /*51*/{0,-9,-1,0},
    /*52*/{2,54,-1,0},
    /*53*/{2,-13,48,-13,50,-13,52,-13,-1,0},
    /*54*/{0,-2,-1,0},
    /*55*/{2,26,45,60,53,17,54,19,56,24,59,8,64,27,68,25,70,44,76,20,78,21,79,29,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*56*/{2,26,53,17,54,19,56,24,59,8,64,27,68,25,70,44,76,20,78,21,79,29,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*57*/{2,66,-1,0},
    /*58*/{45,-43,51,-43,-1,0},
    /*59*/{0,-49,-1,0},
    /*60*/{45,63,51,62,-1,0},
    /*61*/{2,26,53,17,54,19,56,24,59,8,64,27,68,25,70,44,76,20,78,21,79,29,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*62*/{0,-48,-1,0},
    /*63*/{45,-42,51,-42,-1,0},
    /*64*/{45,-44,48,67,51,-44,-1,0},
    /*65*/{45,-34,48,-34,51,-34,-1,0},
    /*66*/{49,68,-1,0},
    /*67*/{0,-35,45,-35,48,-35,51,-35,-1,0},
    /*68*/{2,-21,53,-21,54,-21,56,-21,59,-21,60,-21,64,-21,68,-21,70,-21,76,-21,77,-21,78,-21,79,-21,83,-21,84,-21,85,-21,87,-21,88,-21,90,-21,95,-21,98,-21,99,-21,-1,0},
    /*69*/{2,66,-1,0},
    /*70*/{45,-45,48,67,51,-45,-1,0},
    /*71*/{49,73,-1,0},
    /*72*/{2,-67,48,-67,-1,0},
    /*73*/{0,-51,-1,0},
    /*74*/{49,76,-1,0},
    /*75*/{2,-66,48,-66,-1,0},
    /*76*/{0,-19,-1,0},
    /*77*/{0,-34,44,83,48,-34,-1,0},
    /*78*/{0,-38,48,81,-1,0},
    /*79*/{0,-33,48,67,-1,0},
    /*80*/{49,82,-1,0},
    /*81*/{0,-41,48,-41,-1,0},
    /*82*/{2,26,45,84,53,17,54,19,56,24,59,8,64,27,68,25,70,44,76,20,78,21,79,29,83,41,84,34,85,18,87,38,88,14,90,35,95,23,98,45,99,9,-1,0},
    /*83*/{0,-40,48,-40,-1,0},
    /*84*/{45,86,51,62,-1,0},
    /*85*/{0,-39,48,-39,-1,0},
    /*86*/{2,78,-1,0},
    /*87*/{2,92,-1,0},
    /*88*/{2,91,-1,0},
    /*89*/{0,-46,-1,0},
    /*90*/{0,-50,-1,0},
    /*91*/{0,-18,-1,0},
    /*92*/{0,-37,48,81,-1,0},
    /*93*/{0,-32,48,67,-1,0},
    /*94*/{50,96,52,97,-1,0},
    /*95*/{0,-16,-1,0},
    /*96*/{2,54,9,98,-1,0},
    /*97*/{50,99,-1,0},
    /*98*/{0,-17,-1,0},
  	};

  	/** reduce_goto table */
  	protected static final short[][] _reduce_table = {
    /*0*/{1,41,2,35,3,21,4,29,5,6,6,10,7,30,8,14,9,31,10,3,11,1,12,27,14,39,15,32,19,46,20,15,21,38,22,4,23,9,24,36,25,12,26,45,-1,-1},
    /*1*/{-1,-1},
    /*2*/{3,94,4,50,5,6,-1,-1},
    /*3*/{3,21,4,29,5,6,11,68,20,89,22,86,23,9,24,36,25,12,26,45,-1,-1},
    /*4*/{13,79,16,78,-1,-1},
    /*5*/{-1,-1},
    /*6*/{-1,-1},
    /*7*/{-1,-1},
    /*8*/{-1,-1},
    /*9*/{-1,-1},
    /*10*/{-1,-1},
    /*11*/{-1,-1},
    /*12*/{-1,-1},
    /*13*/{-1,-1},
    /*14*/{-1,-1},
    /*15*/{-1,-1},
    /*16*/{-1,-1},
    /*17*/{-1,-1},
    /*18*/{-1,-1},
    /*19*/{-1,-1},
    /*20*/{-1,-1},
    /*21*/{-1,-1},
    /*22*/{-1,-1},
    /*23*/{-1,-1},
    /*24*/{-1,-1},
    /*25*/{-1,-1},
    /*26*/{-1,-1},
    /*27*/{-1,-1},
    /*28*/{-1,-1},
    /*29*/{-1,-1},
    /*30*/{-1,-1},
    /*31*/{-1,-1},
    /*32*/{-1,-1},
    /*33*/{-1,-1},
    /*34*/{-1,-1},
    /*35*/{-1,-1},
    /*36*/{-1,-1},
    /*37*/{-1,-1},
    /*38*/{-1,-1},
    /*39*/{-1,-1},
    /*40*/{-1,-1},
    /*41*/{-1,-1},
    /*42*/{3,49,4,50,5,6,-1,-1},
    /*43*/{-1,-1},
    /*44*/{-1,-1},
    /*45*/{-1,-1},
    /*46*/{-1,-1},
    /*47*/{-1,-1},
    /*48*/{-1,-1},
    /*49*/{-1,-1},
    /*50*/{-1,-1},
    /*51*/{-1,-1},
    /*52*/{-1,-1},
    /*53*/{-1,-1},
    /*54*/{-1,-1},
    /*55*/{3,21,4,50,5,6,10,56,11,1,17,60,18,58,22,57,23,9,24,36,25,12,26,45,-1,-1},
    /*56*/{3,21,4,50,5,6,11,68,22,69,23,9,24,36,25,12,26,45,-1,-1},
    /*57*/{13,64,-1,-1},
    /*58*/{-1,-1},
    /*59*/{-1,-1},
    /*60*/{-1,-1},
    /*61*/{3,21,4,50,5,6,10,56,11,1,18,63,22,57,23,9,24,36,25,12,26,45,-1,-1},
    /*62*/{-1,-1},
    /*63*/{-1,-1},
    /*64*/{-1,-1},
    /*65*/{-1,-1},
    /*66*/{-1,-1},
    /*67*/{-1,-1},
    /*68*/{-1,-1},
    /*69*/{13,70,-1,-1},
    /*70*/{-1,-1},
    /*71*/{-1,-1},
    /*72*/{-1,-1},
    /*73*/{-1,-1},
    /*74*/{-1,-1},
    /*75*/{-1,-1},
    /*76*/{-1,-1},
    /*77*/{-1,-1},
    /*78*/{-1,-1},
    /*79*/{-1,-1},
    /*80*/{-1,-1},
    /*81*/{-1,-1},
    /*82*/{3,21,4,50,5,6,10,56,11,1,17,84,18,58,22,57,23,9,24,36,25,12,26,45,-1,-1},
    /*83*/{-1,-1},
    /*84*/{-1,-1},
    /*85*/{-1,-1},
    /*86*/{13,93,16,92,-1,-1},
    /*87*/{-1,-1},
    /*88*/{-1,-1},
    /*89*/{-1,-1},
    /*90*/{-1,-1},
    /*91*/{-1,-1},
    /*92*/{-1,-1},
    /*93*/{-1,-1},
    /*94*/{-1,-1},
    /*95*/{-1,-1},
    /*96*/{-1,-1},
    /*97*/{-1,-1},
    /*98*/{-1,-1},
  	};
}
