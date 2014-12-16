/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.internal;


/**
 * A specialised Exception in the context of the Binding.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class EHealthException extends Exception {

	/** generated serial version uid */
	private static final long serialVersionUID = -1242847455651393036L;

	public EHealthException(Exception e) {
		super(e);
	}

	public EHealthException(String message) {
		super(message);
	}
	
}
