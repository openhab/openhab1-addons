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
package org.openhab.binding.nest.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @see AccessTokenRequest
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponse extends AbstractMessage implements Response {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;

    /**
     * @return the access_token returned from the AuthorizeResponse
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * @return the number of minutes until the PIN expires. Ensure you inform the user how much time they have.
     */
    public Integer getExpiresIn() {
        return this.expiresIn;
    }

    @Override
    public String getError() {
        return this.error;
    }

    @Override
    public boolean isError() {
        return this.error != null;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = createToStringBuilder();
        builder.appendSuper(super.toString());
        builder.append("accessToken", this.accessToken);
        builder.append("expiresIn", this.expiresIn);
        builder.append("error", this.error);
        builder.append("errorDescription", this.errorDescription);

        return builder.toString();
    }
}
