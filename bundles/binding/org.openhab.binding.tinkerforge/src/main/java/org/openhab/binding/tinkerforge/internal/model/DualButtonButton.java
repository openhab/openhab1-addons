/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dual Button Button</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButton#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButton#getPosition <em>Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButton()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.DualButtonDevice org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.SwitchState> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.ButtonConfiguration>"
 * @generated
 */
public interface DualButtonButton extends DualButtonDevice, MSensor<OnOffValue>, MTFConfigConsumer<ButtonConfiguration>
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"dualbutton_button"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see #setDeviceType(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButton_DeviceType()
   * @model default="dualbutton_button" unique="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButton#getDeviceType <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Device Type</em>' attribute.
   * @see #getDeviceType()
   * @generated
   */
  void setDeviceType(String value);

  /**
   * Returns the value of the '<em><b>Position</b></em>' attribute.
   * The literals are from the enumeration {@link org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Position</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
   * @see #setPosition(DualButtonDevicePosition)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonButton_Position()
   * @model unique="false"
   * @generated
   */
  DualButtonDevicePosition getPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonButton#getPosition <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Position</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.DualButtonDevicePosition
   * @see #getPosition()
   * @generated
   */
  void setPosition(DualButtonDevicePosition value);
} // DualButtonButton
