/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

/**
 * Base interface for all Ecobee API responses.
 * 
 * @author John Cocula
 * @author Andreas Brenk
 */
public interface Response {

	/**
	 * Return the response message, or <code>null</code> if there is none.
	 * 
	 * @return the response message
	 * 
	 * @see #isError()
	 */
	String getResponseMessage();

	/**
	 * Checks if this response contained an error.
	 * 
	 * @return <code>true</code> if the response contained an error instead of
	 *         actual data.
	 */
	boolean isError();
}
