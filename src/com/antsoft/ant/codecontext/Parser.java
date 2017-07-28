package com.antsoft.ant.codecontext;

import java.util.*;
import java.io.*;
import javax.swing.text.Document;

import com.antsoft.ant.compiler.DepsData;

/*
 @author Kim, SungHoon.
 */
public class Parser {
  private static Hashtable table_symbol;
  private static Hashtable table_import;
  private static int token;

  private static int dummyCount = 0;
  private static int staticCount = 0;
  private static int depth = 0;
  private static String currentClassName = null;
  private static String packageName = null;

  public static boolean hasMain = false;
  private static boolean isConstructor = false;
  private static String mainClass = null;

  private static boolean depsFlag = false;
  private static String fname = null;

  private static Vector classes = null;

	public static void setDepsFlag(String f) {
  	depsFlag = true;
    fname = f;
    //System.out.println(" filename => "+f);
  }

  private static Lexer lexer = null;

  public Parser(File file) {
  	//Lexer.setData(file);
    lexer = new Lexer(file);
  	init();
  }

  private static void init() {
    table_symbol = new Hashtable(); // for symbol table.
    table_import = new Hashtable(); // for import list table.

    dummyCount = 0;
    depth = 0;
    staticCount = 0;
    packageName = "dummypack";
    hasMain = false;

    depsFlag = false;
    fname = null;
    classes = new Vector(1,1);
	}

  public Parser(InputStream in) {
		//Lexer.setData(in);
    lexer = new Lexer(in);
  	init();
  }

  public Parser(Document doc) {
		//Lexer.setData(doc);
    lexer = new Lexer(doc);
    init();
  }

  private static final String name() {
    StringBuffer buf = new StringBuffer("");
    boolean loop = false;

    do {
      if (token == Sym.ID) {
        buf.append(Lexer.getValue());
        token = Lexer.nextToken();
        if (token == Sym.DOT) {
          buf.append(".");
          token = Lexer.nextToken();
          loop = true;
        }
        else loop = false;
      }
      else loop = false;
    } while (loop);

//    System.out.println("name =---> "+buf.toString());
   String result = buf.toString();
   if (depsFlag) DepsData.deps.addTypeReference(result);
   buf = null;

   return result;
 }

  private static final String type() {
    StringBuffer buf = new StringBuffer("");
    switch (token) {
      case Sym.BOOLEAN :
      case Sym.INT :
      case Sym.CHAR :
      case Sym.BYTE :
      case Sym.SHORT :
      case Sym.LONG :
      case Sym.FLOAT :
      case Sym.DOUBLE :
      case Sym.VOID :
        buf.append(Lexer.getValue());
        token = Lexer.nextToken();
        break;
      case Sym.ID :
        buf.append(name());
        break;
    }
    while (token == Sym.LS) {
      buf.append("[");
      if ( (token = Lexer.nextToken()) == Sym.RS) {
        buf.append("]");
        token = Lexer.nextToken();
      }
      else break;
    }

    String result = buf.toString();
    buf = null;
    return result;
  }

  private static final String formalParameter(Hashtable table,StringBuffer params) {
//    System.out.println(" formal parameter");
		boolean perfect = true;
    int count=0;
    StringBuffer buf = new StringBuffer();
    while (token != Sym.RP) {
      SymbolTableEntry entry = new SymbolTableEntry();
      entry.setStartLine(Lexer.getLineCount());
      entry.setMemberSort(SymbolTableEntry.FIELD);
      entry.setDepth(depth + 1);

      if (token == Sym.MODIFIER2) token = Lexer.nextToken();

      String temp = type();
			if (temp != null) {
      	buf.append(temp);
      	params.append(temp);
      	entry.setType(temp);
			}
			else {
				perfect = false;
				break;
			}

      String key = null;
      if (token == Sym.ID) {
        key = Lexer.getValue();
        table.put(key,entry);
      }
			else {
				perfect = false;
				break;
			}

      token = Lexer.nextToken();
      while (token == Sym.LS) {
        token = Lexer.nextToken();
        buf.append("[]");
        params.append("[]");
        token = Lexer.nextToken();
      }

      params.append(" "+key);

      if (token == Sym.COMMA) {
        buf.append(",");
        params.append(",");
        token = Lexer.nextToken();
      }
    }

		if (perfect) {
    	token = Lexer.nextToken();
    	return "("+buf.toString()+")";
		}
		else {
			while (token != Sym.RS && token != Sym.EOF) token = Lexer.nextToken();
			params = null;
			return "()";
		}
  }

  private static final String innerDeclareStatement(SymbolTableEntry entry) {
    String key = "";

    if (token == Sym.ID) {
      key = Lexer.getValue();
    //System.out.println(" inner class is "+key);
      entry.setType(key);
      token = Lexer.nextToken();
      entry.setDepth(depth);
    }

    if (token == Sym.EXTENDS) {
      token = Lexer.nextToken();
      entry.setSuperClass(name());
    }
    else entry.setSuperClass("java.lang.Object");

    if (token == Sym.IMPLEMENTS) {
      token = Lexer.nextToken();
      while (true) {
        entry.addImplementsInterface(name());
        if (token == Sym.COMMA) token = Lexer.nextToken();
        else break;
      }
    }

    while (token != Sym.LB && token != Sym.EOF) token = Lexer.nextToken();

    if (token == Sym.LB) {
      depth++;
      token = Lexer.nextToken();
      entry.table = new Hashtable();
      blockStatement(entry,true);
    }

    //token = Lexer.nextToken();
    return key;
  }

  private static final String constructorStatement(SymbolTableEntry entry) {
//    System.out.println("  constructor declare statement");
    entry.table = new Hashtable();
    StringBuffer buf = new StringBuffer("");
    String param = formalParameter(entry.table,buf);
    if (!buf.toString().equals("")) {
      entry.addParameter(buf.toString());
    }

    if (token == Sym.THROWS) {
      token = Lexer.nextToken();
      boolean loop = false;
      do {
        name();
        if (token == Sym.COMMA) {
          loop = true;
          token = Lexer.nextToken();
        }
        else loop = false;
      } while (loop);
    }

    if (token == Sym.LB) {
      depth++;
      token = Lexer.nextToken();
      blockStatement(entry,false);
    }

    return param;
  }

  private static final String nonConstructorStatement(SymbolTableEntry entry) {
    String param = "";
    if (token == Sym.LP) {
      entry.table = new Hashtable();
      entry.setMemberSort(SymbolTableEntry.METHOD);
      token = Lexer.nextToken();
      StringBuffer buf = new StringBuffer("");
      param = formalParameter(entry.table,buf);
      if (!buf.toString().equals("")) {
        entry.addParameter(buf.toString());
      }

      if (token == Sym.THROWS) {
        token = Lexer.nextToken();
        boolean loop = false;
        do {
          name();
          if (token == Sym.COMMA) {
            loop = true;
            token = Lexer.nextToken();
          }
          else loop = false;
        } while (loop);
      }

      if (token == Sym.LB) {
        depth++;
        token = Lexer.nextToken();
        blockStatement(entry,false);
      }
      else if (token == Sym.SEMIC) token = Lexer.nextToken();
    }
    else {
      entry.setMemberSort(SymbolTableEntry.FIELD);
      entry.setEndLine(Lexer.getLineCount());

      StringBuffer buf = new StringBuffer("");
      while (token != Sym.SEMIC && token != Sym.EOF) {
        while (token == Sym.LS) {
          token = Lexer.nextToken();
          if (token == Sym.RS) {
            token = Lexer.nextToken();
            buf.append("[]");
          }
        }
        if (token == Sym.COMMA) {
          token = Lexer.nextToken();
          if (token == Sym.ID) {
          	String id = Lexer.getValue();
            if (depsFlag) DepsData.deps.addTypeReference(id);
            buf.append(","+id);
            token = Lexer.nextToken();
          }
        }
				else if (token == Sym.LB) {
					entry.table = new Hashtable();
        	depth++;
        	token = Lexer.nextToken();
        	blockStatement(entry,false);
				}
        else token = Lexer.nextToken();
      }


      token = Lexer.nextToken();
      param = buf.toString();
    }

    return param;
  }

  private static final String memberDeclareStatement(SymbolTableEntry entry) {
    String key = "";
    entry.setType(type());
    entry.setDepth(depth);

    if (token == Sym.LP) {
      key = entry.getType();
      entry.setType(null);
      entry.setMemberSort(SymbolTableEntry.CONSTRUCTOR);
      token = Lexer.nextToken();
      key += constructorStatement(entry);
      //entry.setEndLine(Lexer.getLineCount());
    }
    else if (token == Sym.ID) {
      key = Lexer.getValue();
      token = Lexer.nextToken();
      key += nonConstructorStatement(entry);
      //entry.setEndLine(Lexer.getLineCount());
      if (entry.getMemberSort() == SymbolTableEntry.METHOD
          && key.equals("main(String[])") && entry.getAccessType().equals("public")
          && entry.getType().equals("void")) {
            hasMain = true;
            mainClass = currentClassName;
      }
    }
    else token = Lexer.nextToken();

    return key;
  }

  private static final void declareStatement(SymbolTableEntry origin) {
		Hashtable table = origin.table;
    while (token != Sym.RB && token != Sym.EOF) {
      SymbolTableEntry entry = new SymbolTableEntry();
      entry.setStartLine(Lexer.getLineCount());
      String key = null;

      while (token == Sym.MODIFIER || token == Sym.MODIFIER2) {
        if (token == Sym.MODIFIER) entry.setAccessType(Lexer.getValue());
        else if (Lexer.getValue().equals("static")) entry.setStatic(true);
        token = Lexer.nextToken();
      }

      if (token == Sym.INTERFACE || token == Sym.CLASS) {
        if (token == Sym.CLASS) entry.setMemberSort(SymbolTableEntry.CLASS);
        else entry.setMemberSort(SymbolTableEntry.INTERFACE);
        token = Lexer.nextToken();
        key = innerDeclareStatement(entry);
        //if (entry.table == null) System.out.println(" oh shit....." );
				if (token == Sym.RB) entry.setReallyEnd(true);
        entry.setEndLine(Lexer.getLineCount());
        //System.out.println(" end lien is "+entry.getType());
        token = Lexer.nextToken();
      }
      else if (token == Sym.LB) {
        entry.setDepth(depth);
        depth++;
        key = "static"+staticCount++;
        entry.table = new Hashtable();
        token = Lexer.nextToken();
        blockStatement(entry,false);
        //entry.setEndLine(Lexer.getLineCount());
        entry.setMemberSort(SymbolTableEntry.OTHERS);
      }
      else {
        key = memberDeclareStatement(entry);
        if (entry.getMemberSort() == SymbolTableEntry.FIELD) {
          StringTokenizer tokenizer = new StringTokenizer(key,",");
          while (tokenizer!=null&&tokenizer.hasMoreElements()) {
            String one = tokenizer.nextToken();
            entry.table = null;
            SymbolTableEntry e1 = new SymbolTableEntry(entry);
            int p = one.indexOf("[]");
            if (p == -1) table.put(one,e1);
            else {
              e1.setType(e1.getType()+one.substring(p,one.length()));
              table.put(one.substring(0,p),e1);
            }
          }
          continue;
        }
      }

      if (key !=null) {
        if (entry.getMemberSort() != SymbolTableEntry.CONSTRUCTOR) {
        	//if (entry.getMemberSort() == SymbolTableEntry.CLASS)
          	//System.out.println(" testing => "+entry);
          table.put(key,entry);
        }
        else {
          if (key.indexOf(currentClassName) != -1) {
            table.put(key,entry);
          }
        }
      }
    }
    depth--;
  }

  private static final void normalStatement(SymbolTableEntry origin) {
  	//System.out.println(" block start ");
		Hashtable table = origin.table;
    while (token != Sym.RB && token != Sym.EOF) {
      SymbolTableEntry entry;
      String type = type();
      if (type !=null && token == Sym.ID) {
        entry = new SymbolTableEntry();
        entry.setStartLine(Lexer.getLineCount());
        entry.setEndLine(Lexer.getLineCount());
        entry.setDepth(depth);
        entry.setType(type);
        entry.setMemberSort(SymbolTableEntry.FIELD);
        String key = Lexer.getValue();

        table.put(key,entry);
      }
      //else if (type != null && token != Sym.ID) {
      	//if (depsFlag) DepsData.deps.addMemberReference(type);
        //token = Lexer.nextToken();
      //}
      else if (token == Sym.ID) {
      	if (depsFlag) DepsData.deps.addTypeReference(Lexer.getValue());
        token = Lexer.nextToken();
      }
      else if (token == Sym.LB) {
      	if (depsFlag && type != null) {
			    if (type.indexOf('.') != -1) DepsData.deps.addTypeReference(type);
        }

        depth++;
        String key = "dummy" + dummyCount++;
        entry = new SymbolTableEntry();
        entry.setStartLine(Lexer.getLineCount());
        entry.setDepth(depth);
        entry.table = new Hashtable();

        token = Lexer.nextToken();
        //System.out.println(" value => "+Lexer.getValue());
        normalStatement(entry);

        entry.setMemberSort(SymbolTableEntry.OTHERS);
        //entry.setEndLine(Lexer.getLineCount());
        table.put(key,entry);
      }
      else if (token != Sym.RB) {
      	if (depsFlag && type != null) {
			    if (type.indexOf('.') != -1) DepsData.deps.addTypeReference(type);
        }
      	token = Lexer.nextToken();
      }
    }
		origin.setEndLine(Lexer.getLineCount());
    //System.out.println(" origin type ==> "+origin.getType());
    depth--;
    token = Lexer.nextToken();
  	//System.out.println(" block end ");
  }

  private static final void blockStatement(SymbolTableEntry entry,boolean isInClass) {
    if (isInClass) declareStatement(entry);
    else normalStatement(entry);
  }

  private static final void classBlock() {
    SymbolTableEntry entry = new SymbolTableEntry();
    String key = null;
    entry.setStartLine(Lexer.getLineCount());
    entry.setDepth(depth);

    while (token == Sym.MODIFIER || token == Sym.MODIFIER2) {
      if (token == Sym.MODIFIER) entry.setAccessType(Lexer.getValue());
      token = Lexer.nextToken();
    }

    while (token != Sym.CLASS && token != Sym.INTERFACE && token != Sym.EOF) token = Lexer.nextToken();
    if (token == Sym.EOF) return;
    if (token == Sym.CLASS) entry.setMemberSort(SymbolTableEntry.CLASS);
    else entry.setMemberSort(SymbolTableEntry.INTERFACE);

    token = Lexer.nextToken();
    while (token != Sym.ID && token != Sym.EOF) token = Lexer.nextToken();
    if (token == Sym.EOF) return;
    key = Lexer.getValue();
    currentClassName = key;
    entry.setType(key);

    classes.addElement(key);
    if (depsFlag) DepsData.deps.startDefinition(key);

    token = Lexer.nextToken();
    if (token == Sym.EXTENDS) {
      token = Lexer.nextToken();
      entry.setSuperClass(name());
    }
    else {
    	if (packageName.equals("java.lang") && key.equals("Object"))
      	entry.setSuperClass(null);
    	else entry.setSuperClass("java.lang.Object");
    }

    if (token == Sym.IMPLEMENTS) {
      token = Lexer.nextToken();
      while (true) {
        entry.addImplementsInterface(name());
        if (token == Sym.COMMA) token = Lexer.nextToken();
        else break;
      }
    }

    while (token != Sym.LB && token != Sym.EOF) token = Lexer.nextToken();

    token = Lexer.nextToken();
    entry.table = new Hashtable();
    depth++;
    blockStatement(entry,true);

    entry.setEndLine(Lexer.getLineCount());
		if (token == Sym.RB) entry.setReallyEnd(true);
    table_symbol.put(key,entry);
    token = Lexer.nextToken();
  }

  private static final void importStatement() {
    if (token == Sym.IMPORT) {
      Lexer.newLine = true;
      SymbolTableEntry entry = new SymbolTableEntry();
      entry.setMemberSort(SymbolTableEntry.IMPORT);
      entry.setStartLine(Lexer.getLineCount());

      StringBuffer buf = new StringBuffer();
      token = Lexer.nextToken();
      while ( (token != Sym.EOL) && (token != Sym.EOF) ) {
        if (token == Sym.ID) buf.append(Lexer.getValue());
        else if (token == Sym.DOT) buf.append(".");
        else if (token == Sym.MUL) buf.append("*");
        token = Lexer.nextToken();
      }

      String iname = buf.toString();
      if (depsFlag) DepsData.deps.addImport(iname);
      table_import.put(iname,entry);
      entry = null;
    }

    Lexer.newLine = false;
    token = Lexer.nextToken();
  }

  private static final void packageStatement() {
    if (token == Sym.PACKAGE) {
      Lexer.newLine = true;
      SymbolTableEntry entry = new SymbolTableEntry();
      entry.setMemberSort(SymbolTableEntry.PACKAGE);
      entry.setStartLine(Lexer.getLineCount());

      StringBuffer buf = new StringBuffer();
      token = Lexer.nextToken();
      while ( (token  != Sym.EOL) && (token != Sym.EOF) ) {
        if (token == Sym.ID) buf.append(Lexer.getValue());
        else if (token == Sym.DOT) buf.append(".");
        token = Lexer.nextToken();
      }

      packageName = buf.toString();
    	if (depsFlag) DepsData.deps.startPackage(packageName);
      table_import.put(packageName,entry);
      entry = null;
    }

    Lexer.newLine = false;
    token = Lexer.nextToken();
  }

  public static final void doParse() {
  	if (depsFlag) {
    	//System.out.println(" f name => "+fname);
    	DepsData.deps.startFile(fname);
    }

    token = Lexer.nextToken();
    if (token == Sym.EOF) return;

    if (depsFlag) DepsData.deps.startPackage("dummypack");
    if (token == Sym.PACKAGE) packageStatement();
    while (token == Sym.IMPORT) importStatement();

    while (token != Sym.EOF) classBlock();

    //show(table_symbol);
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  // result of parsing is retrieved by other object.

  public static final Hashtable getTableImport() {
    return table_import;
  }

  public static final Hashtable getTableSymbol() {
    return table_symbol;
  }

  public static final String getPackageName() {
    return packageName;
  }

  public static final String getMainClass() {
    return mainClass;
  }

  public static final Vector getClasses() {
  	return classes;
  }

/*
	public static void main(String[] argv) {
		File f = new File("Lexer.java");
		Parser.setData(f);
		Parser.doParse();
		System.out.println("result ");
		Hashtable table = Parser.getTableSymbol();
		//Show show = new Show();
		//show.show(table);
		show(table);
	}

	static int count = 0;
	public static void show(Hashtable t) {
		//System.out.println("count is "+count++);
		Enumeration key = t.keys();
		while (key != null && key.hasMoreElements()) {
			String ttt = (String)key.nextElement();
			SymbolTableEntry e = (SymbolTableEntry)t.get(ttt);
			System.out.println("key => "+ttt);
			if (e!=null) {
				System.out.println("entry => "+e.toString());

				if (e.table != null) show(e.table);
			}
		}
	}
*/
}
