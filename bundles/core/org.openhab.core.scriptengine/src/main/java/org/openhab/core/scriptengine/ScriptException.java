/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.scriptengine;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for exceptions thrown by the script engine.
 *
 * @author Kai Kreuzer
 * @since 0.9.0
 * 
 */
public abstract class ScriptException extends Exception {
	private static final long serialVersionUID = -4155948282895039148L;
	
	private String scriptText;
	private final List<ScriptError> errors; 

	protected ScriptException(String message) {
		super(message);
		this.errors = new ArrayList<ScriptError>(1);
		errors.add(new ScriptError(message, 0, 0, -1));
	}

	/**
	 * @param message
	 * @param cause
	 */
	protected ScriptException(final String message, final String scriptText, final Throwable cause) {
		super(message, cause);
		this.scriptText = scriptText;
		this.errors = new LinkedList<ScriptError>();
	}

	protected ScriptException(final String message, final String scriptText) {
		super(message);
		this.scriptText = scriptText;
		this.errors = new LinkedList<ScriptError>();
	}

	public ScriptException(final String message, final String scriptText, final int line, final int column, final int length) {
		this(scriptText, new ScriptError(message, line, column, length));
	}
	
	public ScriptException(final Throwable cause, final String message, final String scriptText, final int line, final int column, final int length) {
		this(cause, scriptText, new ScriptError(message, line, column, length));
	}

	private ScriptException(final Throwable cause, final String scriptText, final ScriptError error) {
		super(error.getMessage(), cause); // ?
		this.scriptText = scriptText;
		this.errors = new ArrayList<ScriptError>(1);
		errors.add(error);
	}
	
	/**
	 * Creates a ScriptException with one Error.
	 * @param errors
	 */
	private ScriptException(final String scriptText, final ScriptError error) {
		super(error.getMessage()); // ?
		this.scriptText = scriptText;
		this.errors = new ArrayList<ScriptError>(1);
		errors.add(error);
	}
	
	public ScriptException(String message, Throwable cause) {
		super(message, cause);
		this.errors = new ArrayList<ScriptError>(0);
	}

	/**
	 * All Errors that lead to this Exception.
	 * @return List of Error.  Size >= 1, there is at last one ScriptError.
	 */
	public List<ScriptError> getErrors() {
		return errors;
	}
	
	public void setScriptText(final String scriptText) {
		this.scriptText = scriptText;
	}
	
	/**
	 * Returns a concatenation of all errors in contained ScriptError instances.
	 * Separated by newline, except for last error; no \n if only one error.
	 * 
	 * @return The Message.
	 * 
	 * @see ScriptError#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		
		if(scriptText==null) {
			sb.append(super.getMessage());
		} else {
			int l = 1;
			int c = 0;
			for (int x = 0; x < scriptText.length(); x++) {
				if (hasMatchingError(l, c)) {
					sb.append(" ___ ");
				}
				sb.append(scriptText.charAt(x));
				if (scriptText.charAt(x) == '\n') {
					++l;
					c = 0;
				} else {
					++c;
				}
			}
		
			int i = 1;
			for (ScriptError e : getErrors()) {
				if (sb.length() > 0) {
					sb.append('\n');
				}
				sb.append("   ");
				if (getErrors().size() > 1) {
					sb.append(i++);
					sb.append(". ");
				}
				sb.append(e.getMessage());
			}
		}
		return sb.toString();
	}

	private boolean hasMatchingError(int l, int c) {
		for (ScriptError e : getErrors()) {
			if (e.getLineNumber() == l && e.getColumnNumber() == c) {
				return true;
			}
		}
		return false;
	}
}
