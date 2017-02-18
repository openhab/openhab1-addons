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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TF Temperature Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFTemperatureConfiguration#isSlowI2C <em>Slow I2C</em>}
 * </li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFTemperatureConfiguration()
 * @model
 * @generated
 */
public interface TFTemperatureConfiguration extends TFBaseConfiguration {
    /**
     * Returns the value of the '<em><b>Slow I2C</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Slow I2C</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Slow I2C</em>' attribute.
     * @see #setSlowI2C(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFTemperatureConfiguration_SlowI2C()
     * @model unique="false"
     * @generated
     */
    boolean isSlowI2C();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFTemperatureConfiguration#isSlowI2C
     * <em>Slow I2C</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Slow I2C</em>' attribute.
     * @see #isSlowI2C()
     * @generated
     */
    void setSlowI2C(boolean value);

} // TFTemperatureConfiguration
