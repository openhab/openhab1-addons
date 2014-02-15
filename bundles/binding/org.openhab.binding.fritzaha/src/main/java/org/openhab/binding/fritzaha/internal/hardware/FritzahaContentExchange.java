/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware;

import java.io.IOException;

import org.eclipse.jetty.client.ContentExchange;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaCallback;

/**
 * Implementation of Jetty ContextExchange to handle callbacks
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaContentExchange extends ContentExchange {
	/**
	 * Callback to execute on complete response
	 */
	private FritzahaCallback Callback;

	/**
	 * Response handler to execute callback
	 */
	protected void onResponseComplete() throws IOException {
		int status = getResponseStatus();
		String response = getResponseContent();
		Callback.execute(status, response);
	}

	/**
	 * Constructor to assign callback with. Does not cache headers.
	 * 
	 * @param Callback
	 *            Callback to assign
	 */
	public FritzahaContentExchange(FritzahaCallback Callback) {
		this.Callback = Callback;
	}
}
