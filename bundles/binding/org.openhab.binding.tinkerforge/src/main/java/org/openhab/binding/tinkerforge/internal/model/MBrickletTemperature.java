/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletTemperature;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Temperature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getTemperature <em>Temperature</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperature()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletTemperature> org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickletTemperature extends MDevice<BrickletTemperature>, MSensor<DecimalValue>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_temperature"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperature_DeviceType()
   * @model default="bricklet_temperature" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Temperature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Temperature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Temperature</em>' attribute.
   * @see #setTemperature(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperature_Temperature()
   * @model unique="false"
   * @generated
   */
  short getTemperature();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getTemperature <em>Temperature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Temperature</em>' attribute.
   * @see #getTemperature()
   * @generated
   */
  void setTemperature(short value);

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"10"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperature_Threshold()
   * @model default="10" unique="false"
   * @generated
   */
  int getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(int value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MBrickletTemperature
