/*
 * $Header: /AntIDE/source/ant/codecontext/TableCreator.java 26    99-06-07 12:25p Kahn $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 26 $
 * Part : Symbol Table Creator Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 6.
 *                   2. 9. (전면 수정)
 */

package com.antsoft.ant.codecontext;

import java.util.*;
import javax.swing.text.*;

/**
  @author Kim, Sung-Hoon.
  */
public class TableCreator {
	Hashtable symbolTable;
	Hashtable importTable;

	boolean hasMain=false;
	String mainClass=null;

  int staticCount=0;

	// text to be parsed.
	String text=null;

  String packagename = "dummypack";

    // the start offset and end offset to the member...
  int offsetStart=0;
  int offsetEnd=0;

	boolean inBodyFlag=false;
	int lineCount=0;
	boolean innerClassFlag=false;

  // 최초 특정 document가 activate되면, 그것을 분석하여
  // symbol table과 import list table을 구성한다.
  public TableCreator(Document doc) {
		symbolTable=new Hashtable();
		importTable=new Hashtable();

		Hashtable currentTable=symbolTable;
		Hashtable parentTable=null;

		// the stack of the hash table.
		Stack tableStack=new Stack();
		tableStack.removeAllElements();

		// the stack of the element(SymbolTableEntry Object)
		Stack elementStack=new Stack();
		elementStack.removeAllElements();

    SymbolTableEntry methodEntry = null;

		// get the text.
		getDocumentText(doc);

		// first one line.
		String string=nextOneLine();
		String paramString=null;
		int savedSOffsetForParam=0,savedEOffsetForParam=0;

    int depth=0;
    int dummyFlag=0;

    boolean methodFlag=false;
    boolean putParamInTableFlag=false;
    boolean normal = false;

    SymbolTableEntry currentElement;
    SymbolTableEntry backupElement;
    String key="";

    while (string.length()>0) {
     	putParamInTableFlag=false;
     	//System.out.println("string = "+string+" the flag "+inBodyFlag);
     	//System.out.println("the comment = "+LineTokenizer.inCommentState);
			LineParser parser=new LineParser(string,inBodyFlag);
			currentElement=parser.parse();

			if (currentElement!=null) {	// declaration : the class, interface, field, method
				currentElement.setStartOffset(offsetStart);	// set start offset
				currentElement.setEndOffset(offsetEnd);		// set end offset
				currentElement.setDepth(depth);
				//currentElement.setLineCount(lineCount);

        int sort = currentElement.getMemberSort();
				//System.out.println("the key = "+parser.getKey());
				//switch (currentElement.getMemberSort()) {
				switch (sort) {
					case SymbolTableEntry.FIELD:
						if (normal) {
   						string=nextOneLine();
 	   					continue;
            }

						key=parser.getKey();	// variable
						String tempType=currentElement.getType();
						int x1=key.indexOf("[");
						int y1=key.lastIndexOf("]");

						//System.out.println("the x="+x1+" the y="+y1);

						if (x1!=-1) {
							String indexstr=key.substring(x1,key.length());
							key=key.substring(0,x1);
							if (y1!=-1&&y1>x1) {
								StringBuffer buf=new StringBuffer();
								buf.append(currentElement.getType());

								for (int k=0;k<indexstr.length();++k) {
									if (indexstr.charAt(k)=='[')
										buf.append("[");
									if (indexstr.charAt(k)==']')
										buf.append("]");
								}
								currentElement.setType(buf.toString());
							}
						}

						//currentTable.put(parser.getKey(),currentElement);
						//System.out.println("the field key = "+key);
            currentElement.table=null;
            //System.out.println(" the key = "+key);
						currentTable.put(key,currentElement);

						//System.out.println("rest string = "+parser.getRestString());
						if (parser.endsWithComma) {
							StringBuffer temp=new StringBuffer();
							temp.append(currentElement.getAccessType()+" ");
							temp.append(tempType+" ");
							temp.append(parser.getRestString());
							parser.endsWithComma=false;
							string=temp.toString();
							continue;
						}
						else {
							String other=parser.getRestString();
							if (other!=null&&other.indexOf("{")!=-1) {
								int count=0;
								StringBuffer buf=new StringBuffer();
								while (true) {
									for (int i=0;i<other.length();++i) {
										if (other.charAt(i)=='{') count++;
										if (count==0) buf.append(other.charAt(i));
										if (other.charAt(i)=='}') count--;
									}

									if (count==0) break;

									//System.out.println("the count = "+count);
									other=nextOneLine();
									//System.out.println("get next one line "+other);
									if (other.length()>0) continue;
									else break;
								}
								//System.out.println("the buffer = "+buf.toString());
								other=buf.toString();
							}
							if (other!=null&&other.indexOf(",")!=-1) {
								StringBuffer temp=new StringBuffer();
								temp.append(currentElement.getAccessType()+" ");
								temp.append(tempType+" ");
								temp.append(other.substring(other.indexOf(",")+1,other.length()));
								string=temp.toString();
								continue;
							}
						}
						string=nextOneLine();
						continue;

					case SymbolTableEntry.CLASS:
					case SymbolTableEntry.INTERFACE:
						if (depth==1) innerClassFlag=true;
						if (!hasMain&&depth==0) {
							mainClass=new String(parser.getKey());
                        	//System.out.println("the main class is "+mainClass);
						}

					case SymbolTableEntry.METHOD:
					case SymbolTableEntry.CONSTRUCTOR:
						methodFlag=parser.inMethodDeclaration;
						if (string.indexOf("{")==-1&&string.indexOf(";")==-1) methodFlag=true;
						//System.out.println("key outside"+parser.getKey());
						if (!methodFlag) {
							if (parser.getKey().equals("main(String[])")&&
								currentElement.getAccessType().equals("public")&&
								currentElement.getType().equals("void")&&!hasMain) {
								hasMain=true;
							}

              currentTable.put(parser.getKey(),currentElement);

              methodEntry=currentElement;

							// processing parameter.
							putParamInTableFlag=true;
							paramString=string.toString();
							savedSOffsetForParam=offsetStart;
							savedEOffsetForParam=offsetEnd;
						}

						else {	// method declaration incompletely ends.
							int tempstart=currentElement.getStartOffset();

							String temp=string.replace('\n',' ');
							temp=temp.replace('\r',' ');
							string=nextOneLine();
							if (string.length()>0) string=temp.concat(string);
							else break;

							methodFlag=false;
							offsetStart=tempstart;
							continue;
						}

						break;

					case SymbolTableEntry.IMPORT:
						importTable.put(parser.getKey(),currentElement);
						break;
					case SymbolTableEntry.PACKAGE:
						importTable.put(parser.getKey(),currentElement);
            packagename = parser.getKey();
						break;

            /*
          case SymbolTableEntry.OTHERS:
						currentElement.setStartOffset(offsetStart);
						currentElement.setEndOffset(offsetEnd);
            currentElement.setDepth(depth);
						if (depth==1) {
  						currentElement.setMemberSort(SymbolTableEntry.CLASS);
              currentTable.put("staic init"+(dummyFlag++)+"{}",currentElement);
              innerClassFlag=true;
            }
            else {
              System.out.println("the string ==="+string);
              currentTable.put("dumumyblock"+dummyFlag++,currentElement);
            }
            break;
            */
				}
			}

			for (int i=0;i<string.length();++i) {
				char c=string.charAt(i);
				if (c=='{') {
          if (currentElement.getMemberSort()==SymbolTableEntry.OTHERS) {
						currentElement=new SymbolTableEntry();
						currentElement.setStartOffset(offsetStart);
						currentElement.setEndOffset(offsetEnd);
            currentElement.setDepth(depth);

						currentElement.setMemberSort(SymbolTableEntry.OTHERS);
						if (depth==1) {
  						currentElement.setMemberSort(SymbolTableEntry.CLASS);
              currentTable.put("staic init"+(dummyFlag++)+"{}",currentElement);
              innerClassFlag=true;
            }
            else {
              //System.out.println("the string ==="+string);
              currentTable.put("dumumyblock"+dummyFlag++,currentElement);
            }
					}

					//inBodyFlag=false;   // 1999. 2. 18. comment 처리
					// table references stored.
					if (parentTable!=null) tableStack.push(parentTable);
					parentTable=currentTable;

					currentElement.table=new Hashtable();
					currentTable=currentElement.table;

					elementStack.push(currentElement);

          depth++;

					if ((depth==2&&!innerClassFlag)||(depth==3&&innerClassFlag)) {
						inBodyFlag=true;	// for method invocation.

						if (putParamInTableFlag&&!innerClassFlag) {
							// processing parameter.....
							int x=paramString.indexOf("(");
							int y=paramString.indexOf(")");

							// make substring with type param,type param ....
              //System.out.println("param string ==> "+paramString);
							paramString=paramString.substring(x+1,y);
							while (paramString.startsWith(" "))
								paramString=paramString.substring(1,paramString.length());

							if (paramString.length()>0) {
								paramString=paramString.concat("\n");

								while (true) {
									LineParser p=new LineParser(paramString,true);
									currentElement=p.parse();

									if (currentElement.getMemberSort()==SymbolTableEntry.FIELD) {
										currentElement.setStartOffset(savedSOffsetForParam);
										currentElement.setEndOffset(savedEOffsetForParam);
										currentElement.setDepth(2);
                    currentElement.table=null;
										//currentElement.setLineCount(lineCount);

										key=p.getKey();	// variable
										int x1=key.indexOf("[");
										int y1=key.lastIndexOf("]");

										if (x1!=-1) {
											String indexstr=key.substring(x1,key.length());
											key=key.substring(0,x1);
											if (y1!=-1&&y1>x1) {
												StringBuffer buf=new StringBuffer();
												buf.append(currentElement.getType());

												for (int k=0;k<indexstr.length();++k) {
													if (indexstr.charAt(k)=='[')
														buf.append("[");
													if (indexstr.charAt(k)==']')
														buf.append("]");
												}
												currentElement.setType(buf.toString());
											}
										}
										currentTable.put(key,currentElement);
                    methodEntry.addParameter(currentElement.getType()+" "+key);
									}

									String rstring=p.getRestString();
									if (rstring==null) break;
									paramString=rstring.toString();
								}
							}

							putParamInTableFlag=false;
							paramString=null;
						}	// end if (putParamInTableFlag)
					}
				}// end of if(c=='{')

				if (c=='}') {
					if (!elementStack.empty()) {
						currentElement=(SymbolTableEntry)elementStack.pop();
            currentElement.setRealEnd(true);

            if (currentTable.size()==0) currentElement.table=null;

						currentTable=parentTable;
						if (!tableStack.empty()) parentTable=(Hashtable)tableStack.pop();
						else parentTable=null;

						currentElement.setEndOffset(offsetEnd);

						depth--;

						if (depth==1) {
							innerClassFlag=false;
							inBodyFlag=false;
						}

            if (depth == 0) normal = false;
					}
				}// end of if(c=='}')
			}

			// next one line.
			string=nextOneLine();
    }

		// the rest of the stack is processed.
		while (!elementStack.empty()) {
			currentElement=(SymbolTableEntry)elementStack.pop();
			currentElement.setEndOffset(offsetEnd);
      currentElement.setRealEnd(false);
		}
  }

  /**
    getting the symbol table.

    @return reference of the symbol table.
    */
  public Hashtable getSymbolTable() {
    //SymbolTableIterator iter = new SymbolTableIterator(symbolTable);
    //iter.getDependentClasses();
    return symbolTable;
  }

  /**
    getting the import list table.

    @return reference of the import list table.
    */
  public Hashtable getImportListTable() {
      return importTable;
  }

	// getting the text to be parsed.
	void getDocumentText(Document doc) {
    // document는 element들로 구성되어있으므로, 이들을 iterate한다.
    ElementIterator it=new ElementIterator(doc);
    Element e=it.first();  // 이 element는 file 전체를 가진다.

    // element의 start and end offset, length.
    int start=e.getStartOffset();
    int end=e.getEndOffset();
    length=end-start;

    try {
      text=doc.getText(start,length);
    } catch (Exception ex) {
      System.out.println("ant.codecontext.TableCreator.getText() . Exception");
    }
	}

	// the length of the text.
	int length;
	// current character counter.
  int charCount=0;
  boolean inComment=false;

	String nextOneLine() {
    offsetStart=charCount;		// start offset.
    StringBuffer strbuf=new StringBuffer("");

    lineCount++;

    //char c=text.charAt(charCount);
    char c;
		do {
			if (charCount<length) c=text.charAt(charCount);
			else break;
			strbuf.append(c);
			charCount++;
		} while (c!='\n');

    offsetEnd=charCount-1;		// end offset.
		String line=strbuf.toString();
		if (line.length()<=0) return "";

    //System.out.println("before : "+line);
    line = getNoStringLine(line);
    //System.out.println("after  : "+line);

		int p=0;

		if (inComment) {
			p=line.indexOf("*/");
			if (p==-1) return nextOneLine();
			else {
				inComment=false;
				p+=2;
				if (p==line.length()) return nextOneLine();
				else return line.substring(p,line.length());
			}
		}
		else {
			p=line.indexOf("/*");
			if (p!=-1) {
				int q=line.indexOf("*/");
				if (q==-1) {
					inComment=true;
					//System.out.println("in Comment");
					return nextOneLine();
				}
				else {
					StringBuffer temp=new StringBuffer();
					boolean fflag=false;
					if (p!=0) {
						fflag=true;
						temp.append(line.substring(0,p));
					}
					if (line.length()!=q+2) {
						if (fflag) temp.append(" ");
						fflag=true;
						temp.append(line.substring(q+2,line.length()));
					}

					if (fflag) return temp.toString();
					else return nextOneLine();
				}
			}
		}

		p=line.indexOf("//");
		if (p!=-1) {
			if (p==0) return nextOneLine();
			else return line.substring(0,p);
		}

		return line;
	}

	public boolean hasMain() {
		return hasMain;
	}

	public String getMainClass() {
		return mainClass;
	}

  public String getPackageName() {
    return packagename;
  }

  boolean isString = false;

  private String getNoStringLine(String str) {
    int index = 0;
    int length = str.length();
    StringBuffer buf = new StringBuffer("");

    while (index<length) {
      char ch = str.charAt(index);
      if (ch == '\\') {
        buf.append(ch);
        index++;
        ch = str.charAt(index);
      }
      else if (ch == '\"') {
        if (isString) isString = false;
        else isString = true;
      }
      else {
        if (isString) ch = ' ';
      }
      buf.append(ch);
      index++;
    }

    return buf.toString();
  }
}
