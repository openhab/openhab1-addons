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
 * Exception that is thrown on errors during script execution.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class ScriptExecutionException extends ScriptException {

	private static final long serialVersionUID = 149490362444673405L;

	public ScriptExecutionException(final String message, final int line, final int column, final int length) {
		super(message, null, line, column, length);
	}

	public ScriptExecutionException(final String message, final Throwable cause, final int line, final int column, final int length) {
		super(cause, message, null, line, column, length);
	}

	public ScriptExecutionException(final String message) {
		super(message);
	}

	public ScriptExecutionException(String message, Throwable exception) {
		super(message, exception);
	}
}
