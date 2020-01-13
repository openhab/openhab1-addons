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
 * A representation of the model object '<em><b>TF Thermocouple Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getAveraging
 * <em>Averaging</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getThermocoupleType
 * <em>Thermocouple Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFThermocoupleConfiguration()
 * @model
 * @generated
 */
public interface TFThermocoupleConfiguration extends TFBaseConfiguration {
    /**
     * Returns the value of the '<em><b>Averaging</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Averaging</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Averaging</em>' attribute.
     * @see #setAveraging(Short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFThermocoupleConfiguration_Averaging()
     * @model unique="false"
     * @generated
     */
    Short getAveraging();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getAveraging
     * <em>Averaging</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Averaging</em>' attribute.
     * @see #getAveraging()
     * @generated
     */
    void setAveraging(Short value);

    /**
     * Returns the value of the '<em><b>Thermocouple Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Thermocouple Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Thermocouple Type</em>' attribute.
     * @see #setThermocoupleType(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFThermocoupleConfiguration_ThermocoupleType()
     * @model unique="false"
     * @generated
     */
    String getThermocoupleType();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getThermocoupleType
     * <em>Thermocouple Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Thermocouple Type</em>' attribute.
     * @see #getThermocoupleType()
     * @generated
     */
    void setThermocoupleType(String value);

    /**
     * Returns the value of the '<em><b>Filter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Filter</em>' attribute.
     * @see #setFilter(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFThermocoupleConfiguration_Filter()
     * @model unique="false"
     * @generated
     */
    String getFilter();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFThermocoupleConfiguration#getFilter <em>Filter</em>}'
     * attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(String value);

} // TFThermocoupleConfiguration
