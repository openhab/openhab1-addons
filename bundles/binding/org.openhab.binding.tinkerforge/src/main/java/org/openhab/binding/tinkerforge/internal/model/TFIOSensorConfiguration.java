/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TFIO Sensor Configuration</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.4.0
 *        <!-- end-user-doc -->
 *
 *        <p>
 *        The following features are supported:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration#isPullUpResistorEnabled
 *        <em>Pull Up Resistor Enabled</em>}</li>
 *        </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFIOSensorConfiguration()
 * @model
 * @generated
 */
public interface TFIOSensorConfiguration extends TFConfig {
    /**
     * Returns the value of the '<em><b>Pull Up Resistor Enabled</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pull Up Resistor Enabled</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Pull Up Resistor Enabled</em>' attribute.
     * @see #setPullUpResistorEnabled(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFIOSensorConfiguration_PullUpResistorEnabled()
     * @model unique="false"
     * @generated
     */
    boolean isPullUpResistorEnabled();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration#isPullUpResistorEnabled <em>Pull
     * Up Resistor Enabled</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Pull Up Resistor Enabled</em>' attribute.
     * @see #isPullUpResistorEnabled()
     * @generated
     */
    void setPullUpResistorEnabled(boolean value);

} // TFIOSensorConfiguration
