/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Temperature</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.ObjectTemperature#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.ObjectTemperature#getEmissivity <em>Emissivity</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getObjectTemperature()
 * @model
 * @generated
 */
public interface ObjectTemperature extends MTemperatureIRDevice, MTFConfigConsumer<TFObjectTemperatureConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"object_temperature"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getObjectTemperature_DeviceType()
   * @model default="object_temperature" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Emissivity</b></em>' attribute.
   * The default value is <code>"65535"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Emissivity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Emissivity</em>' attribute.
   * @see #setEmissivity(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getObjectTemperature_Emissivity()
   * @model default="65535" unique="false"
   * @generated
   */
  int getEmissivity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.ObjectTemperature#getEmissivity <em>Emissivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Emissivity</em>' attribute.
   * @see #getEmissivity()
   * @generated
   */
  void setEmissivity(int value);

} // ObjectTemperature
