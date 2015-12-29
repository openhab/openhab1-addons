/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

/**
 * Thrown if the internals of the PanStampBinding has a problem with data conversion.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
public class ValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValueException(String message) {
		super(message);
	}

	public ValueException(String message, Throwable cause) {
		super(message, cause);
	}

}
