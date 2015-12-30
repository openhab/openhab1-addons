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
 * Functional exception for RWE Smarthome.
 * 
 * @author ollie-dev
 *
 */
public class SHFunctionalException extends SHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889832539819664531L;

	/**
	 * Constructor.
	 */
	public SHFunctionalException() {
		super();
	}

	/**
	 * Constructor with message and throwable.
	 * 
	 * @param detailMessage
	 * @param throwable
	 */
	public SHFunctionalException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructor message.
	 * 
	 * @param detailMessage
	 */
	public SHFunctionalException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructor throwable.
	 * 
	 * @param throwable
	 */
	public SHFunctionalException(Throwable throwable) {
		super(throwable);
	}

}

