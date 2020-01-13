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
import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Color Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableColorActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableColorActor extends ColorActor {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model colorDataType="org.openhab.binding.tinkerforge.internal.model.HSBType" colorUnique="false"
     *        optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
     * @generated
     */
    void setSelectedColor(HSBType color, DeviceOptions opts);

} // ProgrammableColorActor
