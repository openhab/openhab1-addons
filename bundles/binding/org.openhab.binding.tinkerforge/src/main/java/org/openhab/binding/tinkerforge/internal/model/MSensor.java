/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MSensor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSensor#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSensor#getCallbackPeriod <em>Callback Period</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSensor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSensor extends EObject
{
  /**
   * Returns the value of the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sensor Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sensor Value</em>' attribute.
   * @see #setSensorValue(double)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSensor_SensorValue()
   * @model unique="false"
   * @generated
   */
  double getSensorValue();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MSensor#getSensorValue <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sensor Value</em>' attribute.
   * @see #getSensorValue()
   * @generated
   */
  void setSensorValue(double value);

  /**
   * Returns the value of the '<em><b>Callback Period</b></em>' attribute.
   * The default value is <code>"1000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Callback Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Callback Period</em>' attribute.
   * @see #setCallbackPeriod(long)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSensor_CallbackPeriod()
   * @model default="1000" unique="false"
   * @generated
   */
  long getCallbackPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MSensor#getCallbackPeriod <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Callback Period</em>' attribute.
   * @see #getCallbackPeriod()
   * @generated
   */
  void setCallbackPeriod(long value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false"
   * @generated
   */
  Double fetchSensorValue();

} // MSensor
