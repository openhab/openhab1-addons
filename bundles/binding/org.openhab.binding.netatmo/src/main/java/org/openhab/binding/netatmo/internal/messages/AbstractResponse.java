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
package org.openhab.binding.netatmo.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class for all Netatmo API responses.
 *
 * @author Andreas Brenk
 * @since 1.4.0
 */
public abstract class AbstractResponse extends AbstractMessage implements Response {

    private NetatmoError error;

    @Override
    @JsonProperty("error")
    public NetatmoError getError() {
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

        return builder.toString();
    }

}
