/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling of packets i.e. array of bytes.
 * 
 * <P>
 * http://127001.me/post/java-logging-caller-class-and-method/
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class LogExtension {
	private final Logger logger = LoggerFactory.getLogger(LogExtension.class);

	/**
	 * Log message via the default logging mechanism along with the caller class and method name.
	 * @param msg message to log
	 */
	public final void log(String msg) {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		// Index of StackTraceElement in stacktrace, where our logger appears.
		int i = getSelfElementIndex(stackTrace);
		// Caller is the next item down the stack.
//		StackTraceElement caller = stackTrace[i + 1];
		i--;
		StackTraceElement caller;
		caller = stackTrace[i--];
		logger.debug("{}.{}:{}", caller.getClassName(), caller.getMethodName(), msg);
		caller = stackTrace[i--];
		logger.debug("{}.{}:{}", caller.getClassName(), caller.getMethodName(), msg);
		caller = stackTrace[i--];
		logger.debug("{}.{}:{}", caller.getClassName(), caller.getMethodName(), msg);
	}

	private int getSelfElementIndex(StackTraceElement[] stackTrace) {
		for (int i = 0; i < stackTrace.length; i++) {
			StackTraceElement el = stackTrace[i];
			// We don't need to check method name, our logger has only one method.
			if (el.getClassName().equals(this.getClass().getName())) {
				return i;
			}
		}
		throw new IllegalStateException("Logger doesn't appear in stacktrace");
	}
}
/**
 * end-of-velux/util/Packet.java
 */
