/*
 * $Header: /AntIDE/source/ant/tools/print/Token.java 2     99-05-16 11:59p Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 */
package com.antsoft.ant.tools.print;

/**
 * A linked list of tokens. Each token has three fields - a token
 * identifier, which is a string that can be looked up in the
 * dictionary returned by <code>SyntaxDocument.getColors()</code>
 * to get a color value, a length value which is the length of the
 * token in the text, and a pointer to the next token in the list,
 * which may or may not be valid, depending on the value of the
 * <code>nextValid</code> flag.
 *
 * @author Slava Pestov
 * @version $Id: Token.java,v 1.8 1999/04/01 04:13:00 sp Exp $
 */
public class Token
{
	/**
	 * Alternate text token id. This will be rendered in the
	 * same color as normal text. It is provided for internal
	 * use by token markers (eg, HTML mode uses it to mark
	 * JavaScripts)
	 */
	public static final String ALTTXT = "alt";

	/**
	 * Comment 1 token id. This can be used to mark a comment.
	 */
	public static final String COMMENT1 = "comment1";

	/**
	 * Comment 2 token id. This can be used to mark a comment.
	 */
	public static final String COMMENT2 = "comment2";

	
	/**
	 * Literal 1 token id. This can be used to mark a string
	 * literal (eg, C mode uses this to mark "..." literals)
	 */
	public static final String LITERAL1 = "literal1";

	/**
	 * Literal 2 token id. This can be used to mark a string
	 * literal (eg, C mode uses this to mark '...' literals)
	 */
	public static final String LITERAL2 = "literal2";

	/**
	 * Label token id. This can be used to mark labels
	 * (eg, C mode uses this to mark ...: sequences)
	 */
	public static final String LABEL = "label";

	/**
	 * Keyword 1 token id. This can be used to mark a
	 * keyword. This should be used for general language
	 * constructs.
	 */
	public static final String KEYWORD1 = "keyword1";

	/**
	 * Keyword 2 token id. This can be used to mark a
	 * keyword. This should be used for preprocessor
	 * commands, or variables.
	 */
	public static final String KEYWORD2 = "keyword2";

	/**
	 * Keyword 3 token id. This can be used to mark a
	 * keyword. This should be used for data types.
	 */
	public static final String KEYWORD3 = "keyword3";

	/**
	 * Operator token id. This can be used to mark an
	 * operator. (eg, SQL mode marks +, -, etc with this
	 * token type)
	 */
	public static final String OPERATOR = "operator";

	/**
	 * Invalid token id. This can be used to mark invalid
	 * or incomplete tokens, so the user can easily spot
	 * syntax errors.
	 */
	public static final String INVALID = "invalid";

	/**
	 * The length of this token.
	 */
	public int length;

	/**
	 * The id of this token.
	 */
	public String id;

	/**
	 * The next token in the linked list.
	 */
	public Token next;

	/**
	 * Set to true if the next token is valid, false otherwise.
	 */
	public boolean nextValid;

	/**
	 * Creates a new token.
	 * @param length The length of the token
	 * @param id The id of the token
	 */
	public Token(int length, String id)
	{
		this.length = length;
		this.id = id;
	}

	/**
	 * Returns a string representation of this token.
	 */
	public String toString()
	{
		return id + "[length=" + length + (nextValid ? ",nextValid]"
						   : "nextInvalid]");
	}
}

