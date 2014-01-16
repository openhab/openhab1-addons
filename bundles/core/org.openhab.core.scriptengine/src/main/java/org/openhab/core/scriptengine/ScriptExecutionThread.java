/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
