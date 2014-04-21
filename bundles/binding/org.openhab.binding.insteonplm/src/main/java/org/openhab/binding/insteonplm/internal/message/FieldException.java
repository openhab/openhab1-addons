/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.message;
/**
 * Exception to be thrown if there is an error processing a field, for
 * example type mismatch, out of bounds etc.
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
@SuppressWarnings("serial")
public class FieldException extends Exception {
	public FieldException() { super(); }
	public FieldException(String m) { super(m); }
	public FieldException(String m, Throwable cause) { super(m, cause); }
	public FieldException(Throwable cause) { super(cause); }
}
