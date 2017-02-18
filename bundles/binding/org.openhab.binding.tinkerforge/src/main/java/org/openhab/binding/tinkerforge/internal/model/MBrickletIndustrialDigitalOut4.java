/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletIndustrialDigitalOut4;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Industrial Digital Out4</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.5.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalOut4#getDeviceType
 *        <em>Device Type</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDigitalOut4()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice
 *        <org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletIndustrialDigitalOut4>
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder
 *        <org.openhab.binding.tinkerforge.internal.model.DigitalActorDigitalOut4>"
 * @generated
 */
public interface MBrickletIndustrialDigitalOut4
        extends MDevice<BrickletIndustrialDigitalOut4>, MSubDeviceHolder<DigitalActorDigitalOut4> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_industrial_digital_4out"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDigitalOut4_DeviceType()
     * @model default="bricklet_industrial_digital_4out" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();
} // MBrickletIndustrialDigitalOut4
