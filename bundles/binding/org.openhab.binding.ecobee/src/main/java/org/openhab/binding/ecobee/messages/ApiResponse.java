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
package org.openhab.binding.ecobee.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class for all Ecobee API responses.
 *
 * @author John Cocula
 * @since 1.7.0
 */
public class ApiResponse extends AbstractMessage implements Response {

    private Status status;

    @JsonProperty("status")
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String getResponseMessage() {
        return status.toString();
    }

    @Override
    public boolean isError() {
        return status.getCode() != 0;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = createToStringBuilder();
        builder.appendSuper(super.toString());
        if (this.status != null) {
            builder.append("status", this.status);
        }

        return builder.toString();
    }
}
