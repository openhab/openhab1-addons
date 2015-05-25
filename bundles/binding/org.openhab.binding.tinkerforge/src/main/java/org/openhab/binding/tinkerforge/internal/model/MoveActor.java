/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

import org.openhab.binding.tinkerforge.internal.types.DirectionValue;
import org.openhab.core.library.types.UpDownType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Move Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MoveActor#getDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMoveActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MoveActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Direction</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Direction</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Direction</em>' attribute.
   * @see #setDirection(DirectionValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMoveActor_Direction()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.DirectionValue"
   * @generated
   */
  DirectionValue getDirection();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MoveActor#getDirection <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Direction</em>' attribute.
   * @see #getDirection()
   * @generated
   */
  void setDirection(DirectionValue value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model directionDataType="org.openhab.binding.tinkerforge.internal.model.UpDownType" directionUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void move(UpDownType direction, DeviceOptions opts);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void stop();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void moveon(DeviceOptions opts);

} // MoveActor
