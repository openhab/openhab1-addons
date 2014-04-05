/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBrick</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices <em>Msubdevices</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDeviceHolder()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSubDeviceHolder<S extends MSubDevice<?>> extends EObject
{
  /**
   * Returns the value of the '<em><b>Msubdevices</b></em>' containment reference list.
   * The list contents are of type {@link S}.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick <em>Mbrick</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Msubdevices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Msubdevices</em>' containment reference list.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDeviceHolder_Msubdevices()
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick
   * @model opposite="mbrick" containment="true"
   * @generated
   */
  EList<S> getMsubdevices();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void initSubDevices();

} // MBrick
