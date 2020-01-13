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
package org.openhab.binding.zwave.internal.protocol.event;

import java.util.Date;

/**
 * This event signals a node being included or excluded into the network.
 *
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ZWaveInclusionEvent extends ZWaveEvent {
    private final Type type;
    private final Date includedAt;

    /**
     * Constructor. Creates a new instance of the ZWaveInclusionEvent
     * class.
     *
     * @param nodeId the nodeId of the event.
     */
    public ZWaveInclusionEvent(Type type, int nodeId) {
        super(nodeId);

        this.type = type;
        this.includedAt = new Date();
    }

    public ZWaveInclusionEvent(Type type) {
        super(255);

        this.type = type;
        this.includedAt = new Date();
    }

    public Type getEvent() {
        return type;
    }

    public Date getIncludedAt() {
        return includedAt;
    }

    @Override
    public String toString() {
        return new StringBuilder("ZWaveInclusionEvent [type=").append(type).append(", occurred ")
                .append(System.currentTimeMillis() - includedAt.getTime()).append("ms ago, getNodeId()=")
                .append(getNodeId()).append(", getEndpoint()=").append(getEndpoint()).append("]").toString();
    }

    public enum Type {
        IncludeStart,
        IncludeSlaveFound,
        IncludeControllerFound,
        IncludeFail,
        IncludeDone,
        ExcludeStart,
        ExcludeSlaveFound,
        ExcludeControllerFound,
        ExcludeFail,
        ExcludeDone,
    }
}
