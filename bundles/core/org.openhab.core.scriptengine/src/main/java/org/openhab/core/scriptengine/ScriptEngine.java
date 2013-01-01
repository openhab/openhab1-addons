/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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

import org.eclipse.xtext.xbase.XExpression;


/**
 * The script engine is the main entrypoint for openHAB script use. It can build {@link Script} instances
 * from simple strings. These scripts can then be executed by the caller.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public interface ScriptEngine {

	/**
	 * Parses a string and returns a parsed script object.
	 * 
	 * @param scriptAsString script to parse
	 * @return Script object, which can be executed 
	 * @throws ScriptParsingException
	 */
	public Script newScriptFromString(final String scriptAsString) throws ScriptParsingException;

	/**
	 * Executes a script that is passed as a string
	 * 
	 * @param scriptAsString
	 * @return the return value of the script
	 * @throws ScriptParsingException
	 * @throws ScriptExecutionException
	 */
	public Object executeScript(final String scriptAsString) throws ScriptParsingException, ScriptExecutionException;
	
	/**
	 * Wraps an Xbase XExpression in a Script instance
	 * @param expression the XExpression
	 * @return the Script instance containing the expression
	 */
	public Script newScriptFromXExpression(final XExpression expression);
}
