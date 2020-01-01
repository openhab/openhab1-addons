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
package org.openhab.binding.fritzaha.internal.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * See {@link DevicelistModel}.
 *
 * @author Robert Bausdorf
 * @since 1.6
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement(name = "powermeter")
@XmlType(propOrder = { "power", "energy" })
public class PowerMeterModel {
    private BigDecimal power;
    private BigDecimal energy;

    public BigDecimal getPower() {
        return power;
    }

    public void setPower(BigDecimal power) {
        this.power = power;
    }

    public BigDecimal getEnergy() {
        return energy;
    }

    public void setEnergy(BigDecimal energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("powermeter");
        out.append("[power=").append(this.getPower()).append(',');
        out.append("energy=").append(this.getEnergy()).append(']');
        return out.toString();
    }
}
