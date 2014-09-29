/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

/**
 * Base interface for all Netatmo API responses.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
public interface Response {

	/**
	 * Access the error details if present.
	 * 
	 * @return the error details if the response contained an error,
	 *         <code>null</code> otherwise
	 * 
	 * @see #isError()
	 */
	NetatmoError getError();

	/**
	 * Checks if this response contained an error.
	 * 
	 * @return <code>true</code> if the response contained an error instead of
	 *         actual data.
	 */
	boolean isError();

}
