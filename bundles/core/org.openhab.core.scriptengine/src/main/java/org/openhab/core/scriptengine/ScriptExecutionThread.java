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

import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be used to execute scripts in a separate thread, so that the execution
 * of the caller thread is not blocked.
 * 
 * @author Kai Kreuzer
 * @since 0.9.1
 *
 */
@SuppressWarnings("restriction")
public class ScriptExecutionThread extends Thread {

	static private final Logger logger = LoggerFactory.getLogger(ScriptExecutionThread.class);

	private Script script;
	private IEvaluationContext context;

	// the script evaluation result
	private Object result = null;
	
	public ScriptExecutionThread(String name, Script script, IEvaluationContext context) {
		setName(name);
		this.script = script;
		this.context = context;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			result = script.execute(context);
		} catch (ScriptExecutionException e) {
			String msg = e.getCause().getMessage();
			if (msg==null) {
				logger.error("Error during the execution of rule '{}'", getName(), e.getCause());
			} else {
				logger.error("Error during the execution of rule '{}': {}", new String[] { getName(), msg });
			}
		}
	}

	/**
	 * Returns the script evaluation result (or null, if thread is still active)
	 * 
	 * @return the script evaluation result
	 */
	public Object getResult() {
		return result;
	}
}
