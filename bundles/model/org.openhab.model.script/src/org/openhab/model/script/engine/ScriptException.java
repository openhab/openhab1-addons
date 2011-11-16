/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.model.script.engine;


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
		
		sb.append(super.getMessage());
		if(scriptText!=null) {
			sb.append('\n');
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
