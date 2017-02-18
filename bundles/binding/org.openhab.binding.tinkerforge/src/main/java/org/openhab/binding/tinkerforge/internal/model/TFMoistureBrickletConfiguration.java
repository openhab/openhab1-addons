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
 * A representation of the model object '<em><b>TF Moisture Bricklet Configuration</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.5.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration#getMovingAverage
 *        <em>Moving Average</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFMoistureBrickletConfiguration()
 * @model
 * @generated
 */
public interface TFMoistureBrickletConfiguration extends TFBaseConfiguration {
    /**
     * Returns the value of the '<em><b>Moving Average</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Moving Average</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Moving Average</em>' attribute.
     * @see #setMovingAverage(Short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFMoistureBrickletConfiguration_MovingAverage()
     * @model unique="false"
     * @generated
     */
    Short getMovingAverage();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration#getMovingAverage
     * <em>Moving Average</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Moving Average</em>' attribute.
     * @see #getMovingAverage()
     * @generated
     */
    void setMovingAverage(Short value);

} // TFMoistureBrickletConfiguration
