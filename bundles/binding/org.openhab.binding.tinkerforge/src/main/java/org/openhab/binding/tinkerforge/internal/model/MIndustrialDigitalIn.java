/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MIndustrial Digital In</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.5.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn#getDeviceType <em>Device
 *        Type</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMIndustrialDigitalIn()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSubDevice
 *        <org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4>
 *        org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.DigitalValue>"
 * @generated
 */
public interface MIndustrialDigitalIn extends MSubDevice<MBrickletIndustrialDigitalIn4>, MSensor<HighLowValue> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"digital_4in"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMIndustrialDigitalIn_DeviceType()
     * @model default="digital_4in" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();
} // MIndustrialDigitalIn
