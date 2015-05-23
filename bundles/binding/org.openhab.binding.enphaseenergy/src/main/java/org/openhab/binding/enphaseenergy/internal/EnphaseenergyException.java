/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal;

/**
 * @author Markus Fritze
 * @since 1.7.0
 */
public class EnphaseenergyException extends RuntimeException {

	public EnphaseenergyException(String message) {
		super(message);
	}
	
	public EnphaseenergyException(final Throwable cause) {
		super(cause);
	}

	public EnphaseenergyException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
