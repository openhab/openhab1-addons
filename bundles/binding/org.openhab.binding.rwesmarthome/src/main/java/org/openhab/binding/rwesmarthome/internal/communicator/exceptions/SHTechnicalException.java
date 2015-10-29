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
 * @author ollie-dev
 *
 */
public class SHTechnicalException extends SHException {

	/**
	 * 
	 */
	public SHTechnicalException() {
		super();
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public SHTechnicalException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * @param detailMessage
	 */
	public SHTechnicalException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public SHTechnicalException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7670415727186606365L;

}

