/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The response status object contains the processing status of the request. 
 * It will contain any relevant error information should an error occur. 
 * The status object is returned with every response regardless of success or failure status. 
 * It is suitable for logging request failures.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Status.shtml">Status</a>
 * @author John Cocula
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status extends AbstractMessagePart {
	private Integer code;
	private String message;

	/**
	 * @return the status code for this status
	 */
	@JsonProperty("code")
	public Integer getCode() {
		return this.code;
	}

	/**
	 * @return the detailed message for this status
	 */
	@JsonProperty("message")
	public String getMessage() {
		return this.message;
	}

	/**
	 * @return <code>true</code> if we know this error indicates that
	 * the access token has expired.
	 */
	public boolean isAccessTokenExpired() {
		return this.code != null && this.code == 14;
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
