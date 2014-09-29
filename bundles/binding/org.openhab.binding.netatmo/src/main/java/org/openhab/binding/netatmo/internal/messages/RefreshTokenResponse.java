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
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Java Bean to represent a JSON response to a <code>refresh_token</code> API
 * method call.
 * <p>
 * Sample response:
 * 
 * <pre>
 * {"access_token":"5185698919775928fd000015|bc29b1ef5c6c18b8da8a3071638674a2",
 *  "refresh_token":"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDE",
 *  "expires_in":10800,
 *  "expire_in":10800,
 *  "scope":null}
 * </pre>
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenResponse extends AbstractResponse {

	private String accessToken;

	@JsonProperty("access_token")
	public String getAccessToken() {
		return this.accessToken;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);

		return builder.toString();
	}
}
