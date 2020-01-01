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
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Switch Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableSwitchActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableSwitchActor extends SwitchSensor {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model stateDataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" stateUnique="false"
     *        optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
     * @generated
     */
    void turnSwitch(OnOffValue state, DeviceOptions opts);

} // ProgrammableSwitchActor
