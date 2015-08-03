/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic;

/**
 * A simple Exception for the LCNParser.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNParserException extends Exception {

	/** Generated Serial. */
	private static final long serialVersionUID = 3855283130606720020L;
	
	/**
	 * Simple constructor for the LCNParserException.
	 * 
	 * @param msg A String containing an explanation about the Exception.
	 */
	public LCNParserException(String msg) {
		super(msg);
	}
	
}