/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

/**
 * This exception is used if the XMPP/XMPP connection has not
 * been initialized.
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class NotInitializedException extends Exception {

	private static final long serialVersionUID = 4706382830782417755L;

	public NotInitializedException() {
		super();
	}
	
	public NotInitializedException(String message) {
		super(message);
	}
}
