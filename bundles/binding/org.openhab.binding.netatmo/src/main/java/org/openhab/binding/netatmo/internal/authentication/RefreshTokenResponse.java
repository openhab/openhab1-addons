/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo.internal.authentication;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.binding.netatmo.internal.messages.AbstractResponse;

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
