/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This class represents the response to the <code>/authorize</code> endpoint. Upon success, the PIN, authorization
 * token, scope, the PIN expiration and minimum polling interval for PINs is returned.
 * 
 * <p>
 * At this point, the application should display the PIN to the user and request that they log into their web portal and
 * register the application using the PIN in their My Apps widget.
 * 
 * @see AuthorizeRequest
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/pin-api-authorization.shtml">PIN
 *      Authorization Strategy</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizeResponse extends AbstractAuthResponse {

	@JsonProperty("ecobeePin")
	private String ecobeePin;
	@JsonProperty("code")
	private String authToken;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("expires_in")
	private Integer expiresIn;
	@JsonProperty("interval")
	private Integer interval;

	/**
	 * @return the PIN a user enters in the web portal
	 */
	@JsonProperty("ecobeePin")
	public String getEcobeePin() {
		return this.ecobeePin;
	}

	/**
	 * @return the authorization token needed to request the access and refresh tokens
	 */
	@JsonProperty("code")
	public String getAuthToken() {
		return this.authToken;
	}

	/**
	 * @return the requested Scope from the original request. This must match the original request.
	 */
	@JsonProperty("scope")
	public String getScope() {
		return this.scope;
	}

	/**
	 * @return the number of seconds until the PIN expires. Ensure you inform the user how much time they have.
	 */
	@JsonProperty("expires_in")
	public Integer getExpiresIn() {
		return this.expiresIn;
	}

	/**
	 * @return the minimum amount of seconds which must pass between polling attempts for a token.
	 */
	@JsonProperty("interval")
	public Integer getInterval() {
		return this.interval;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("ecobeePin", this.ecobeePin);
		builder.append("authToken", this.authToken);
		builder.append("scope", this.scope);
		builder.append("expiresIn", this.expiresIn);
		builder.append("interval", this.interval);

		return builder.toString();
	}
}
