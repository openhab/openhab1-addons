/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Color Illuminance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.ColorIlluminance#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.ColorIlluminance#getGain <em>Gain</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.ColorIlluminance#getIntegrationTime <em>Integration
 * Time</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorIlluminance()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.BrickletColorDevice
 *        org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.MDecimalValue>
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
 *        <org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration>
 *        org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface ColorIlluminance
        extends BrickletColorDevice, MSensor<DecimalValue>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"color_illuminance"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorIlluminance_DeviceType()
     * @model default="color_illuminance" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Gain</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Gain</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Gain</em>' attribute.
     * @see #setGain(Short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorIlluminance_Gain()
     * @model unique="false"
     * @generated
     */
    Short getGain();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.ColorIlluminance#getGain
     * <em>Gain</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Gain</em>' attribute.
     * @see #getGain()
     * @generated
     */
    void setGain(Short value);

    /**
     * Returns the value of the '<em><b>Integration Time</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Integration Time</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Integration Time</em>' attribute.
     * @see #setIntegrationTime(Short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorIlluminance_IntegrationTime()
     * @model unique="false"
     * @generated
     */
    Short getIntegrationTime();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.ColorIlluminance#getIntegrationTime
     * <em>Integration Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Integration Time</em>' attribute.
     * @see #getIntegrationTime()
     * @generated
     */
    void setIntegrationTime(Short value);

} // ColorIlluminance
