/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Sample error response:
 * 
 * <pre>
 * {"error": {"code": 9, "message": "Device not found" }}
 * </pre>
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class NetatmoError extends AbstractMessagePart {

	private int code;

	private String message;

	/**
	 * <ul>
	 * <li>1 : No access token given to the API
	 * <li>2 : The access token is not valid
	 * <li>3 : The access token has expired
	 * <li>4 : Internal error
	 * <li>5 : The application has been deactivated
	 * <li>9 : The device has not been found
	 * <li>10 : A mandatory API parameter is missing
	 * <li>11 : An unexpected error occured
	 * <li>13 : Operation not allowed
	 * <li>15 : Installation of the device has not been finalized
	 * <li>21 : Invalid argument
	 * <li>25 : Invalid date given
	 * <li>26 : Maximum usage of the API has been reached by application
	 * </ul>
	 * 
	 * @see #isAccessTokenExpired()
	 */
	@JsonProperty("code")
	public int getCode() {
		return this.code;
	}

	@JsonProperty("message")
	public String getMessage() {
		return this.message;
	}

	public boolean isAccessTokenExpired() {
		return this.code == 3;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("code", this.code);
		builder.append("message", this.message);

		return builder.toString();
	}
}
