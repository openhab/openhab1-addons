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
 * A representation of the model object '<em><b>TF Object Temperature Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration#getEmissivity <em>Emissivity</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFObjectTemperatureConfiguration()
 * @model
 * @generated
 */
public interface TFObjectTemperatureConfiguration extends TFBaseConfiguration
{
  /**
   * Returns the value of the '<em><b>Emissivity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Emissivity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Emissivity</em>' attribute.
   * @see #setEmissivity(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFObjectTemperatureConfiguration_Emissivity()
   * @model unique="false"
   * @generated
   */
  int getEmissivity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration#getEmissivity <em>Emissivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Emissivity</em>' attribute.
   * @see #getEmissivity()
   * @generated
   */
  void setEmissivity(int value);

} // TFObjectTemperatureConfiguration
