/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.console;

/**
 * This interface must be implemented by consoles which want to use the 
 * {@link ConsoleInterpreter}.
 * It allows basic output commands.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public interface Console {

	public void print(String s);
	
	public void println(String s);
	
	/** usage output is treated differently from other output as it might
	 * differ between different kinds of consoles
	 * 
	 * @param s the main usage string (console independent)
	 */
	public void printUsage(String s);
}
