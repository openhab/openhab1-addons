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
 * A representation of the model object '<em><b>LED Strip Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getChiptype <em>Chiptype</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getFrameduration
 * <em>Frameduration</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getClockfrequency
 * <em>Clockfrequency</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getColorMapping <em>Color
 * Mapping</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getSubDevices <em>Sub Devices</em>}
 * </li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration()
 * @model
 * @generated
 */
public interface LEDStripConfiguration extends TFConfig {
    /**
     * Returns the value of the '<em><b>Chiptype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Chiptype</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Chiptype</em>' attribute.
     * @see #setChiptype(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Chiptype()
     * @model unique="false"
     * @generated
     */
    String getChiptype();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getChiptype
     * <em>Chiptype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Chiptype</em>' attribute.
     * @see #getChiptype()
     * @generated
     */
    void setChiptype(String value);

    /**
     * Returns the value of the '<em><b>Frameduration</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Frameduration</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Frameduration</em>' attribute.
     * @see #setFrameduration(Integer)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Frameduration()
     * @model unique="false"
     * @generated
     */
    Integer getFrameduration();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getFrameduration
     * <em>Frameduration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Frameduration</em>' attribute.
     * @see #getFrameduration()
     * @generated
     */
    void setFrameduration(Integer value);

    /**
     * Returns the value of the '<em><b>Clockfrequency</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Clockfrequency</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Clockfrequency</em>' attribute.
     * @see #setClockfrequency(Long)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Clockfrequency()
     * @model unique="false"
     * @generated
     */
    Long getClockfrequency();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getClockfrequency
     * <em>Clockfrequency</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Clockfrequency</em>' attribute.
     * @see #getClockfrequency()
     * @generated
     */
    void setClockfrequency(Long value);

    /**
     * Returns the value of the '<em><b>Color Mapping</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Color Mapping</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Color Mapping</em>' attribute.
     * @see #setColorMapping(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_ColorMapping()
     * @model unique="false"
     * @generated
     */
    String getColorMapping();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getColorMapping <em>Color
     * Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Color Mapping</em>' attribute.
     * @see #getColorMapping()
     * @generated
     */
    void setColorMapping(String value);

    /**
     * Returns the value of the '<em><b>Sub Devices</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sub Devices</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Sub Devices</em>' attribute.
     * @see #setSubDevices(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_SubDevices()
     * @model unique="false"
     * @generated
     */
    String getSubDevices();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getSubDevices
     * <em>Sub Devices</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Sub Devices</em>' attribute.
     * @see #getSubDevices()
     * @generated
     */
    void setSubDevices(String value);

} // LEDStripConfiguration
