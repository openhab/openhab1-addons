/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Digital Sensor IO4</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4#getPin <em>Pin</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalSensorIO4()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.IO4Device org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.DigitalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration>"
 * @generated
 */
public interface DigitalSensorIO4 extends IO4Device, MSensor<HighLowValue>, MTFConfigConsumer<TFIOSensorConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"io4sensor"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalSensorIO4_DeviceType()
   * @model default="io4sensor" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Pull Up Resistor Enabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pull Up Resistor Enabled</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pull Up Resistor Enabled</em>' attribute.
   * @see #setPullUpResistorEnabled(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalSensorIO4_PullUpResistorEnabled()
   * @model unique="false"
   * @generated
   */
  boolean isPullUpResistorEnabled();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pull Up Resistor Enabled</em>' attribute.
   * @see #isPullUpResistorEnabled()
   * @generated
   */
  void setPullUpResistorEnabled(boolean value);

  /**
   * Returns the value of the '<em><b>Pin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pin</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pin</em>' attribute.
   * @see #setPin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalSensorIO4_Pin()
   * @model unique="false"
   * @generated
   */
  int getPin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4#getPin <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pin</em>' attribute.
   * @see #getPin()
   * @generated
   */
  void setPin(int value);

} // DigitalSensorIO4
