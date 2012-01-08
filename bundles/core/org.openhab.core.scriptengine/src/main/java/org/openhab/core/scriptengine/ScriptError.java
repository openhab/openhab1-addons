/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
