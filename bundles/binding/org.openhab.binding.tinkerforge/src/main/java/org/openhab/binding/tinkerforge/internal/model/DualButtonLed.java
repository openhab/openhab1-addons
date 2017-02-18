/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dual Button Led</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLed#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLed#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonLed()
 * @model
 * @generated
 */
public interface DualButtonLed extends DualButtonDevice, DigitalActor, MTFConfigConsumer<DualButtonLEDConfiguration> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"dualbutton_led"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see #setDeviceType(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonLed_DeviceType()
     * @model default="dualbutton_led" unique="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLed#getDeviceType
     * <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Device Type</em>' attribute.
     * @see #getDeviceType()
     * @generated
     */
    void setDeviceType(String value);

    /**
     * Returns the value of the '<em><b>Position</b></em>' attribute.
     * The literals are from the enumeration
     * {@link org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Position</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
     * @see #setPosition(DualButtonDevicePosition)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonLed_Position()
     * @model unique="false"
     * @generated
     */
    DualButtonDevicePosition getPosition();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLed#getPosition
     * <em>Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Position</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
     * @see #getPosition()
     * @generated
     */
    void setPosition(DualButtonDevicePosition value);
} // DualButtonLed
