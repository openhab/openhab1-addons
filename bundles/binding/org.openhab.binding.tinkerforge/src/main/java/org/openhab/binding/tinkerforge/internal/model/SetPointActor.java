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

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Point Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSetPointActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface SetPointActor<C extends DimmableConfiguration> extends DimmableActor<C>, PercentTypeActor {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model newValueUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions"
     *        optsUnique="false"
     * @generated
     */
    void setValue(BigDecimal newValue, DeviceOptions opts);

} // SetPointActor
