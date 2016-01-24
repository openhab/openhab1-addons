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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TF Analog In V2 Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInV2Configuration#getMovingAverage
 * <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFAnalogInV2Configuration()
 * @model
 * @generated
 */
public interface TFAnalogInV2Configuration extends TFBaseConfiguration {
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
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFAnalogInV2Configuration_MovingAverage()
     * @model unique="false"
     * @generated
     */
    Short getMovingAverage();

    /**
     * Sets the value of the '
     * {@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInV2Configuration#getMovingAverage
     * <em>Moving Average</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Moving Average</em>' attribute.
     * @see #getMovingAverage()
     * @generated
     */
    void setMovingAverage(Short value);

} // TFAnalogInV2Configuration
