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
 * A representation of the model object '<em><b>Digital Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState <em>Digital State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPort <em>Port</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPin <em>Pin</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#isDefaultState <em>Default State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor()
 * @model
 * @generated
 */
public interface DigitalActor extends IODevice, MTFConfigConsumer<TFIOActorConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"io_actuator"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_DeviceType()
   * @model default="io_actuator" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Digital State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Digital State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Digital State</em>' attribute.
   * @see #setDigitalState(HighLowValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_DigitalState()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue"
   * @generated
   */
  HighLowValue getDigitalState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState <em>Digital State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Digital State</em>' attribute.
   * @see #getDigitalState()
   * @generated
   */
  void setDigitalState(HighLowValue value);

  /**
   * Returns the value of the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Port</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Port</em>' attribute.
   * @see #setPort(char)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_Port()
   * @model unique="false"
   * @generated
   */
  char getPort();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPort <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Port</em>' attribute.
   * @see #getPort()
   * @generated
   */
  void setPort(char value);

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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_Pin()
   * @model unique="false"
   * @generated
   */
  int getPin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPin <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pin</em>' attribute.
   * @see #getPin()
   * @generated
   */
  void setPin(int value);

  /**
   * Returns the value of the '<em><b>Default State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default State</em>' attribute.
   * @see #setDefaultState(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_DefaultState()
   * @model unique="false"
   * @generated
   */
  boolean isDefaultState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#isDefaultState <em>Default State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default State</em>' attribute.
   * @see #isDefaultState()
   * @generated
   */
  void setDefaultState(boolean value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model digitalStateDataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue" digitalStateUnique="false"
   * @generated
   */
  void turnDigital(HighLowValue digitalState);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue" unique="false"
   * @generated
   */
  HighLowValue fetchDigitalValue();

} // DigitalActor
