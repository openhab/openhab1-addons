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
package org.openhab.binding.homematic.internal.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Object that represents a Homematic datapoint.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */

@XmlRootElement(name = "datapoint")
@XmlAccessorType(XmlAccessType.FIELD)
public class HmDatapoint extends HmValueItem {

    @XmlTransient
    private HmChannel channel;

    /**
     * Returns the channel of the datapoint.
     */
    public HmChannel getChannel() {
        return channel;
    }

    /**
     * Sets the channel of the datapoint.
     */
    protected void setChannel(HmChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("address", channel.getDevice().getAddress()).append("channel", channel.getNumber())
                .append("parameter", name).toString();
    }

    /**
     * Dumps all data to support better debugging.
     */
    public String toDumpString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("address", channel.getDevice().getAddress()).append("channel", channel.getNumber())
                .append("parameter", name).append("value", value).append("valueType", valueType)
                .append("subType", subType).append("minValue", minValue).append("maxValue", maxValue)
                .append("valueList", valueList == null ? null : StringUtils.join(valueList, ","))
                .append("writeable", writeable).toString();
    }
}
