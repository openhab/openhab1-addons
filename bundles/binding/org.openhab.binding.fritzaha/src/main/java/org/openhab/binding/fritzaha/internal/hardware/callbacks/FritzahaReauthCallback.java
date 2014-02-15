/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.callbacks;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;

/**
 * Callback implementation for reauthorization and retry
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaReauthCallback implements FritzahaCallback {
	/**
	 * Path to HTTP interface
	 */
	String path;
	/**
	 * Arguments to use
	 */
	String args;
	/**
	 * Web interface to use
	 */
	FritzahaWebInterface webIface;

	/**
	 * HTTP Method for callback retries
	 * 
	 * @author Projektgruppe Smart Home, Lehrstuhl KT, TU Dortmund
	 * @since 1.3.0
	 */
	public enum Method {
		POST, GET
	};

	/**
	 * Method used
	 */
	Method httpMethod;
	/**
	 * Number of remaining retries
	 */
	int retries;
	/**
	 * Whether the request returned a valid response
	 */
	boolean validRequest;
	/**
	 * Callback to execute on next retry
	 */
	FritzahaCallback retryCallback;

	/**
	 * Returns whether the request returned a valid response
	 * 
	 * @return Validity of response
	 */
	public boolean isValidRequest() {
		return validRequest;
	}

	/**
	 * Returns whether there will be another retry on an invalid response
	 * 
	 * @return true if there will be no more retries, false otherwise
	 */
	public boolean isFinalAttempt() {
		return retries <= 0;
	}

	/**
	 * Sets different Callback to use on retry (initial value: same callback
	 * after decremented retry counter)
	 * 
	 * @param newRetryCallback
	 *            Callback to retry with
	 */
	public void setRetryCallback(FritzahaCallback newRetryCallback) {
		retryCallback = newRetryCallback;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(int status, String response) {
		if (status != 200 || "".equals(response) || ".".equals(response)) {
			validRequest = false;
			if (retries >= 1) {
				webIface.authenticate();
				retries--;
				if (httpMethod == Method.GET) {
					webIface.asyncGet(path, args, retryCallback);
				} else if (httpMethod == Method.POST) {
					webIface.asyncPost(path, args, retryCallback);
				}
			}
		} else
			validRequest = true;
	}

	/**
	 * Constructor for retriable authentication
	 * 
	 * @param path
	 *            Path to HTTP interface
	 * @param args
	 *            Arguments to use
	 * @param webIface
	 *            Web interface to use
	 * @param httpMethod
	 *            Method used
	 * @param retries
	 *            Number of retries
	 */
	public FritzahaReauthCallback(String path, String args, FritzahaWebInterface webIface, Method httpMethod,
			int retries) {
		this.path = path;
		this.args = args;
		this.webIface = webIface;
		this.httpMethod = httpMethod;
		this.retries = retries;
		retryCallback = this;
	}
}
