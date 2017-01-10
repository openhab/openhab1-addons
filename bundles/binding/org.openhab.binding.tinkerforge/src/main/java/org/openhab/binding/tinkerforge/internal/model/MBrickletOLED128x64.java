/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletOLED128x64;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet OLED12 8x64</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getDeviceType <em>Device Type</em>}
 * </li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.OLEDBricklet
 *        org.openhab.binding.tinkerforge.internal.model.MDevice
 *        <org.openhab.binding.tinkerforge.internal.model.TinkerBrickletOLED128x64>
 *        org.openhab.binding.tinkerforge.internal.model.MTextActor
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
 *        <org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration>"
 * @generated
 */
public interface MBrickletOLED128x64
        extends OLEDBricklet, MDevice<BrickletOLED128x64>, MTextActor, MTFConfigConsumer<BrickletOLEDConfiguration> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_oled128x64"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_DeviceType()
     * @model default="bricklet_oled128x64" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

} // MBrickletOLED128x64
