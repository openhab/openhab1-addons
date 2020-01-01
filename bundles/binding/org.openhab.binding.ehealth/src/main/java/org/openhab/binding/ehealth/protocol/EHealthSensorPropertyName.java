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
package org.openhab.binding.ehealth.protocol;

/**
 * Represents the valid configuration property names. The lowercase version
 * of the parameter should be configured in the binding configuration in the
 * *.items file.
 *
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public enum EHealthSensorPropertyName {

    AIR_FLOW("airFlow"),
    TEMPERATURE("temperature"),
    SKIN_CONDUCTANCE("skinConductance"),
    SKIN_RESISTANCE("skinResistance"),
    SKIN_CONDUCTANCE_VOLTAGE("skinConductanceVoltage"),
    BPM("bpm"),
    OXYGEN_SATURATION("oxygenSaturation"),
    BODY_POSITION("bodyPosition"),
    ECG("ecg"),
    EMG("emg"),
    GLUCOSE("glucose");

    private String propertyName;

    private EHealthSensorPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public static EHealthSensorPropertyName getPropertyName(String propertyName) throws IllegalArgumentException {
        for (EHealthSensorPropertyName propName : EHealthSensorPropertyName.values()) {
            if (propName.propertyName.equals(propertyName)) {
                return propName;
            }
        }
        throw new IllegalArgumentException("Invalid PropertyName '" + propertyName + "'");
    }

}
