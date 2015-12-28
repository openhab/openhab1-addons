/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
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
 * @see <a href="https://www.mojio.com/home/developer/api/documentation/v1/auth/pin-api-authorization.shtml">PIN
 *      Authorization Strategy</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizeResponse extends GenericMojioResponse {

	@JsonProperty("AppId")
	private String AppId;
	@JsonProperty("UserId")
	private String UserId;
	@JsonProperty("Scopes")
	private String Scopes;
	@JsonProperty("ValidUntil")
	private String ValidUntil;
	@JsonProperty("Sandboxed")
	private boolean Sandboxed;
	@JsonProperty("_id")
	private String _id;

	/**
	 * @return the Sandboxed
	 */
	@JsonProperty("Sandboxed")
	public boolean getSandboxed() {
		return this.Sandboxed;
	}

	/**
	 * @return the Scopes
	 */
	@JsonProperty("Scopes")
	public String getScopes() {
		return this.Scopes;
	}

	/**
	 * @return the authorization token needed to request the access and refresh tokens
	 */
	@JsonProperty("_id")
	public String getAuthToken() {
		return this._id;
	}

	/**
	 * @return the requested Scope from the original request. This must match the original request.
	 */
	@JsonProperty("AppId")
	public String getApplication() {
		return this.AppId;
	}

	/**
	 * @return the number of minutes until the PIN expires. Ensure you inform the user how much time they have.
	 */
	@JsonProperty("ValidUntil")
	public MojioTimestamp getValidUntil() {
		return new MojioTimestamp(this.ValidUntil);
	}

	@JsonProperty("UserId")
	public String getUserId() {
		return this.UserId;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("AppId", this.AppId);
		builder.append("UserId", this.UserId);
		builder.append("_id", this._id);
		builder.append("ValidUntil", this.ValidUntil);
		builder.append("Scopes", this.Scopes);
		builder.append("Sandboxed", this.Sandboxed);

		return builder.toString();
	}
}
