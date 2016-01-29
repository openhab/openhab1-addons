/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableActor extends EObject {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
     * @generated
     */
    void action(DeviceOptions opts);

} // ProgrammableActor
