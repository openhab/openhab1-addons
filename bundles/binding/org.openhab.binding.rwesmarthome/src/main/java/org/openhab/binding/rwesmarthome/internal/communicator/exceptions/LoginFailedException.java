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
 * Exception if login has failed.
 * @author ollie-dev
 *
 */
public class LoginFailedException extends SHFunctionalException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3148382279756806820L;

	/**
	 * Constructor
	 */
	public LoginFailedException() {
		super();
	}

	/**
	 * Constructor with message and throwable.
	 * 
	 * @param detailMessage
	 * @param throwable
	 */
	public LoginFailedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructor with message.
	 * 
	 * @param detailMessage
	 */
	public LoginFailedException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructor with throwable.
	 * 
	 * @param throwable
	 */
	public LoginFailedException(Throwable throwable) {
		super(throwable);
	}

}
