/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.scriptengine;


/**
 * A detailed error information for a script
 * 
 * @see ScriptException
 * @see ScriptExecutionException
 * @see ScriptParsingException
 *
 * @author Kai Kreuzer
 * @since 0.9.0
 * 
 */
public final class ScriptError {
	
	private final int column;
	private final int length;
	private final int line;
	
	// TODO Internationalize!  Not an Error string, but a key...
	private final String message;

	/**
	 * Creates new ScriptError.
	 * 
	 * @param message Error Message
	 * @param line Line number, or -1 if unknown
	 * @param column Column number, or -1 if unknown 
	 * @param length Length, or -1 if unknown
	 */
	public ScriptError(final String message, final int line, final int column, final int length) {
		this.message = message;
		this.line = line;
		this.column = column;
		this.length = length;
	}

	
	/**
	 * Returns a message containing the String passed to a constructor as well as line and column numbers if any of these are known.
	 * @return The error message.
	 */
	public String getMessage() {
		StringBuilder sb = new StringBuilder(message);
		if (line != -1) {
			sb.append("; line ");
			sb.append(line);
		}
		if (column != -1) {
			sb.append(", column ");
			sb.append(column);
		}
		if (length != -1) {
			sb.append(", length ");
			sb.append(length);
		}
		return sb.toString();
	}

	/**
	 * Get the line number on which an error occurred.
	 * @return The line number. Returns -1 if a line number is unavailable.
	 */
	public int getLineNumber() {
		return line;
	}

	/**
	 * Get the column number on which an error occurred.
	 * @return The column number. Returns -1 if a column number is unavailable.
	 */
	public int getColumnNumber() {
		return column;
	}
    
    /**
     * Get the number of columns affected by the error.
     * @return The number of columns. Returns -1 if unavailable.
     */
	public int getLength() {
		return length;
	}
}
