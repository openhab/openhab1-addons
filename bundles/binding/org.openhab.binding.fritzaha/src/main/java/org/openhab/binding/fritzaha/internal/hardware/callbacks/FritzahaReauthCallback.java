/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
