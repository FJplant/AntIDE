package com.antsoft.ant.codecontext;

public class InsightThread implements Runnable {
	CodeContext codeContext = null;
	String line = null;
	int lineNum = 0;
	int count = 0;

	public InsightThread(CodeContext codeContext,String line,int lineNum,int count) {
		this.codeContext = codeContext;
		this.line = line;
		this.lineNum = lineNum;
		this.count = count;
	}

	public void run() {
		codeContext.activateIntellisense(line,lineNum,count);
	}
}
