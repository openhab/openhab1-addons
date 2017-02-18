/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MLCD2 0x4 Button</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.3.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getDeviceType <em>Device
 *        Type</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum <em>Button
 *        Num</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.SwitchState>
 *        org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
 *        <org.openhab.binding.tinkerforge.internal.model.ButtonConfiguration>"
 * @generated
 */
public interface MLCD20x4Button extends MSensor<OnOffValue>, MLCDSubDevice, MTFConfigConsumer<ButtonConfiguration> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"lcd_button"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button_DeviceType()
     * @model default="lcd_button" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Button Num</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Button Num</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Button Num</em>' attribute.
     * @see #setButtonNum(short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button_ButtonNum()
     * @model unique="false"
     * @generated
     */
    short getButtonNum();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum
     * <em>Button Num</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Button Num</em>' attribute.
     * @see #getButtonNum()
     * @generated
     */
    void setButtonNum(short value);

} // MLCD20x4Button
