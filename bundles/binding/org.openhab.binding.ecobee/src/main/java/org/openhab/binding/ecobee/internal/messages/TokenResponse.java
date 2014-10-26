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
 * @see TokenRequest
 * @see RefreshTokenRequest
 * @see <a hred="https://www.ecobee.com/home/developer/api/documentation/v1/auth/pin-api-authorization.shtml">PIN Authorization Strategy</a>
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/token-refresh.shtml">Refreshing Your Tokens</a>
 * @author John Cocula
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse extends AbstractAuthResponse {

	@JsonProperty("access_token")	private String accessToken;
    @JsonProperty("token_type")		private String tokenType;
    @JsonProperty("expires_in")		private Integer expiresIn;
    @JsonProperty("refresh_token")	private String refreshToken;
    @JsonProperty("scope")			private String scope;

    /**
	 * @return the accessToken
	 */
	@JsonProperty("access_token")
	public String getAccessToken() {
		return this.accessToken;
	}

	/**
	 * @return the tokenType
	 */
	@JsonProperty("token_type")
	public String getTokenType() {
		return this.tokenType;
	}

	/**
	 * @return the expiresIn
	 */
	@JsonProperty("expires_in")
	public Integer getExpiresIn() {
		return this.expiresIn;
	}

	/**
	 * @return the refreshToken
	 */
	@JsonProperty("refresh_token")
	public String getRefreshToken() {
		return this.refreshToken;
	}

	/**
	 * @return the scope
	 */
	@JsonProperty("scope")
	public String getScope() {
		return this.scope;
	}
	
	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);
		builder.append("tokenType", this.tokenType);
		builder.append("expiresIn", this.expiresIn);
		builder.append("refreshToken", this.refreshToken);
		builder.append("scope", this.scope);

		return builder.toString();
	}
}
