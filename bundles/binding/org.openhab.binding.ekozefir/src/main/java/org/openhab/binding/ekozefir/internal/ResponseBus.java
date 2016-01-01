/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import org.openhab.binding.ekozefir.response.Response;
import org.openhab.binding.ekozefir.response.ResponseListener;

/**
 * Interface for posting commands to event bus from ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public interface ResponseBus {
	/**
	 * Post response to openhab.
	 * 
	 * @param event response from ahu
	 */
	void post(Response event);

	/**
	 * Register listener of response.
	 * 
	 * @param listener of response
	 */
	void register(ResponseListener listener);

	/**
	 * Unregister listener of response
	 * 
	 * @param listener response listener
	 */
	void unregister(ResponseListener listener);
}
