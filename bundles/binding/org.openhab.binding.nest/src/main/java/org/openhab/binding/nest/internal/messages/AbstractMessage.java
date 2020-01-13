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

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base class for all messages.
 *
 * @author John Cocula
 * @since 1.7.0
 */
public class AbstractMessage {

    @Override
    public String toString() {
        final ToStringBuilder builder = createToStringBuilder();

        return builder.toString();
    }

    protected final ToStringBuilder createToStringBuilder() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE);
    }
}
