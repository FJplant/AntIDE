package com.antsoft.ant.codecontext;

/**
 * @author Kim, Sung-Hoon.
 */
public class MyThread implements Runnable {
	CodeContext codeContext = null;
	int count = 0;
	boolean flag = false;

	public MyThread(CodeContext codeContext, int count, boolean flag) {
		this.codeContext = codeContext;
		this.count = count;
		this.flag = flag;
	}

	public void run() {
		//System.out.println(" thread start ");
		//System.out.println(" my thread : "+flag);
		codeContext.modifyTableWhenSaveFile(flag, count);
		//System.out.println(" thread end ");
	}
}
