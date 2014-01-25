/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MSwitch Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor#getSwitchState <em>Switch State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSwitchActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSwitchActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Switch State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Switch State</em>' attribute.
   * @see #setSwitchState(OnOffValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSwitchActor_SwitchState()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.SwitchState"
   * @generated
   */
  OnOffValue getSwitchState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor#getSwitchState <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Switch State</em>' attribute.
   * @see #getSwitchState()
   * @generated
   */
  void setSwitchState(OnOffValue value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model stateDataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" stateUnique="false"
   * @generated
   */
  void turnSwitch(OnOffValue state);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" unique="false"
   * @generated
   */
  OnOffValue fetchSwitchState();

} // MSwitchActor
