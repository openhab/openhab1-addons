/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.core.library.types.IncreaseDecreaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimmable Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DimmableActor#getMinValue <em>Min Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DimmableActor#getMaxValue <em>Max Value</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDimmableActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DimmableActor<TC extends DimmableConfiguration> extends MTFConfigConsumer<TC> {
    /**
     * Returns the value of the '<em><b>Min Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Min Value</em>' attribute.
     * @see #setMinValue(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDimmableActor_MinValue()
     * @model unique="false"
     * @generated
     */
    BigDecimal getMinValue();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DimmableActor#getMinValue <em>Min
     * Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Min Value</em>' attribute.
     * @see #getMinValue()
     * @generated
     */
    void setMinValue(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Max Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max Value</em>' attribute.
     * @see #setMaxValue(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDimmableActor_MaxValue()
     * @model unique="false"
     * @generated
     */
    BigDecimal getMaxValue();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DimmableActor#getMaxValue <em>Max
     * Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Max Value</em>' attribute.
     * @see #getMaxValue()
     * @generated
     */
    void setMaxValue(BigDecimal value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model increaseDecreaseDataType="org.openhab.binding.tinkerforge.internal.model.IncreaseDecreaseType"
     *        increaseDecreaseUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions"
     *        optsUnique="false"
     * @generated
     */
    void dimm(IncreaseDecreaseType increaseDecrease, DeviceOptions opts);

} // DimmableActor
