/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.exception;

/**
 * This represents an exception parsing a LightwaveRf Message.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfMessageException extends Exception {

	private static final long serialVersionUID = -2131620053984993990L;

	public LightwaveRfMessageException(String message) {
		super(message);
	}

	public LightwaveRfMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
