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

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

import com.tinkerforge.BrickletAmbientLightV2;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Ambient Light V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getDeviceType <em>Device Type</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getThreshold <em>Threshold</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getIlluminanceRange <em>Illuminance
 * Range</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getIntegrationTime <em>Integration
 * Time</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLightV2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice
 *        <org.openhab.binding.tinkerforge.internal.model.TinkerBrickletAmbientLightV2>
 *        org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.MDecimalValue>
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
 *        <org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration>
 *        org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickletAmbientLightV2 extends MDevice<BrickletAmbientLightV2>, MSensor<DecimalValue>,
        MTFConfigConsumer<AmbientLightV2Configuration>, CallbackListener {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_ambient_lightv2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLightV2_DeviceType()
     * @model default="bricklet_ambient_lightv2" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Threshold</b></em>' attribute.
     * The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Threshold</em>' attribute.
     * @see #setThreshold(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLightV2_Threshold()
     * @model default="1" unique="false"
     * @generated
     */
    BigDecimal getThreshold();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getThreshold
     * <em>Threshold</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Threshold</em>' attribute.
     * @see #getThreshold()
     * @generated
     */
    void setThreshold(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Illuminance Range</b></em>' attribute.
     * The default value is <code>"3"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Illuminance Range</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Illuminance Range</em>' attribute.
     * @see #setIlluminanceRange(short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLightV2_IlluminanceRange()
     * @model default="3" unique="false"
     * @generated
     */
    short getIlluminanceRange();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getIlluminanceRange
     * <em>Illuminance Range</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Illuminance Range</em>' attribute.
     * @see #getIlluminanceRange()
     * @generated
     */
    void setIlluminanceRange(short value);

    /**
     * Returns the value of the '<em><b>Integration Time</b></em>' attribute.
     * The default value is <code>"3"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Integration Time</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Integration Time</em>' attribute.
     * @see #setIntegrationTime(short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLightV2_IntegrationTime()
     * @model default="3" unique="false"
     * @generated
     */
    short getIntegrationTime();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2#getIntegrationTime <em>Integration
     * Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Integration Time</em>' attribute.
     * @see #getIntegrationTime()
     * @generated
     */
    void setIntegrationTime(short value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
     * @generated
     */
    @Override
    void init();

} // MBrickletAmbientLightV2
