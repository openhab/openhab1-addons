/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PTC Resistance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.PTCResistance#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.PTCResistance#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCResistance()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.PTCDevice org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface PTCResistance extends PTCDevice, MSensor<DecimalValue>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"ptc_resistance"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCResistance_DeviceType()
   * @model default="ptc_resistance" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"2"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(BigDecimal)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCResistance_Threshold()
   * @model default="2" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.PTCResistance#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

} // PTCResistance
