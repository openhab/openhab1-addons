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
 * A representation of the model object '<em><b>Bricklet OLED Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration#getContrast <em>Contrast</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration#isInvert <em>Invert</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletOLEDConfiguration()
 * @model
 * @generated
 */
public interface BrickletOLEDConfiguration extends TFConfig {
    /**
     * Returns the value of the '<em><b>Contrast</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contrast</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Contrast</em>' attribute.
     * @see #setContrast(Short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletOLEDConfiguration_Contrast()
     * @model unique="false"
     * @generated
     */
    Short getContrast();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration#getContrast <em>Contrast</em>}'
     * attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Contrast</em>' attribute.
     * @see #getContrast()
     * @generated
     */
    void setContrast(Short value);

    /**
     * Returns the value of the '<em><b>Invert</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Invert</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Invert</em>' attribute.
     * @see #setInvert(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletOLEDConfiguration_Invert()
     * @model unique="false"
     * @generated
     */
    boolean isInvert();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration#isInvert
     * <em>Invert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Invert</em>' attribute.
     * @see #isInvert()
     * @generated
     */
    void setInvert(boolean value);

} // BrickletOLEDConfiguration
