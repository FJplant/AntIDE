

package com.antsoft.ant.debugger;

import javax.swing.text.*;
import java.io.Serializable;

/**
 * @author Kim, Sung-Hoon.
 */
public class MyGapContent extends GapContent 
					implements AbstractDocument.Content, Serializable {
	public MyGapContent(int size) {
		super(size);
	}

	public final int getMyGapStart() {
		return super.getGapStart();
	}

	public final int getMyGapEnd() {
		return super.getGapEnd();
	}

	public final Object getMyArray() {
		return super.getArray();
	}
}
