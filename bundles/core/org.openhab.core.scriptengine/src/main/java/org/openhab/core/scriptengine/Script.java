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

import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;

/**
 * This interface is implemented by openHAB scripts.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public interface Script {
		
	public static final String SCRIPT_FILEEXT = "script";

	
	/**
	 * Executes the script instance and returns the execution result
	 * 
	 * @return the execution result or <code>null</code>, if the script does not have a return value
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public Object execute() throws ScriptExecutionException;

	/**
	 * Executes the script instance with a given evaluation context and returns the execution result
	 * 
	 * @param evaluationContext the evaluation context is a map of variables (name, object)
	 *        that should be available during the script execution
	 * @return the execution result or <code>null</code>, if the script does not have a return value
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public Object execute(IEvaluationContext evaluationContext) throws ScriptExecutionException;
}
