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

import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MSwitch Actor</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.4.0
 *        <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSwitchActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSwitchActor extends SwitchSensor {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model stateDataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" stateUnique="false"
     * @generated
     */
    void turnSwitch(OnOffValue state);

} // MSwitchActor
