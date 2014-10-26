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
 * Base class for all Ecobee authorization responses.  The members of this abstract
 * class will de-serialize to <code>null</code>s if there was no error, but the 
 * concrete subclasses will instead be filled in.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/auth-req-resp.shtml">Authorization Requests and Errors</a>
 * @author John Cocula
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractAuthResponse extends AbstractMessage implements Response {

	@JsonProperty("error")				private String error;
	@JsonProperty("error_description")	private String errorDescription;
	@JsonProperty("error_uri")			private String errorURI;

	/**
	 * @return the error type
	 */
	@JsonProperty("error")
	public String getError() {
		return this.error;
	}

	/**
	 * @return the error description
	 */
	@JsonProperty("error_description")
	public String getErrorDescription() {
		return this.errorDescription;
	}

	/**
	 * @return the error URI, explaining why this error may have occurred.
	 */
	@JsonProperty("error_uri")
	public String getErrorURI() {
		return this.errorURI;
	}

	@Override
	public String getResponseMessage() {
		return this.error;
	}

	@Override
	public boolean isError() {
		return this.error != null;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		if (this.error != null) {
			builder.append("error", this.error);
		}
		if (this.errorDescription != null) {
			builder.append("errorDescription", this.errorDescription);
		}
		if (this.errorURI != null) {
			builder.append("errorURI", this.errorURI);
		}

		return builder.toString();
	}
}
