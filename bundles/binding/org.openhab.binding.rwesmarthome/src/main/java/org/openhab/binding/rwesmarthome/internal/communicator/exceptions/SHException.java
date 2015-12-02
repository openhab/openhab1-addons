/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.exceptions;

/**
 * Base exception for RWE Smarthome.
 * 
 * @author ollie-dev
 *
 */
public class SHException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4861308355876842323L;

	/**
	 * Constructor.
	 */
	public SHException() {
		super();
	}

	/**
	 * Constructor with message and throwable.
	 * 
	 * @param detailMessage
	 * @param throwable
	 */
	public SHException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructor with message.
	 * 
	 * @param detailMessage
	 */
	public SHException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructor throwable.
	 * 
	 * @param throwable
	 */
	public SHException(Throwable throwable) {
		super(throwable);
	}

}

