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
package org.openhab.binding.powermax.internal.state;

/**
 * A class to store the state of a zone
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxZoneState {

    private Boolean tripped;
    private Long lastTripped;
    private Boolean lowBattery;
    private Boolean bypassed;
    private Boolean armed;

    public PowerMaxZoneState() {
        tripped = null;
        lastTripped = null;
        lowBattery = null;
        bypassed = null;
        armed = null;
    }

    public Boolean isTripped() {
        return tripped;
    }

    public void setTripped(Boolean tripped) {
        this.tripped = tripped;
    }

    public Long getLastTripped() {
        return lastTripped;
    }

    public void setLastTripped(Long lastTripped) {
        this.lastTripped = lastTripped;
    }

    public Boolean isLowBattery() {
        return lowBattery;
    }

    public void setLowBattery(Boolean lowBattery) {
        this.lowBattery = lowBattery;
    }

    public Boolean isBypassed() {
        return bypassed;
    }

    public void setBypassed(Boolean bypassed) {
        this.bypassed = bypassed;
    }

    public Boolean isArmed() {
        return armed;
    }

    public void setArmed(Boolean armed) {
        this.armed = armed;
    }

}
