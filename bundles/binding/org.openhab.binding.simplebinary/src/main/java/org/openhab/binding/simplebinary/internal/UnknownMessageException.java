/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

/**
 * Exception class for received unknown message (unknown message ID)
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */

public class UnknownMessageException extends Exception {
	private static final long serialVersionUID = 1367099920816957870L;

	public UnknownMessageException() {
		super();
	}

	public UnknownMessageException(String msg) {
		super(msg);
	}
}