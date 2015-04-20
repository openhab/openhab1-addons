/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLEDStrip;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet LED Strip</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip#getColorMapping <em>Color Mapping</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLEDStrip()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.ProgrammableColorActor org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletLEDStrip> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.LEDGroup>"
 * @generated
 */
public interface MBrickletLEDStrip extends ProgrammableColorActor, MDevice<BrickletLEDStrip>, MTFConfigConsumer<LEDStripConfiguration>, MSubDeviceHolder<LEDGroup>
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_ledstrip"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLEDStrip_DeviceType()
   * @model default="bricklet_ledstrip" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Color Mapping</b></em>' attribute.
   * The default value is <code>"rgb"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Color Mapping</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Color Mapping</em>' attribute.
   * @see #setColorMapping(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLEDStrip_ColorMapping()
   * @model default="rgb" unique="false"
   * @generated
   */
  String getColorMapping();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip#getColorMapping <em>Color Mapping</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Color Mapping</em>' attribute.
   * @see #getColorMapping()
   * @generated
   */
  void setColorMapping(String value);
} // MBrickletLEDStrip
