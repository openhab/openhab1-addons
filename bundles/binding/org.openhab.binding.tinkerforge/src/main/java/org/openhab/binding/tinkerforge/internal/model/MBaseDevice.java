/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.emf.ecore.EObject;

import org.slf4j.Logger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBase Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getEnabledA <em>Enabled A</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBaseDevice()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MBaseDevice extends EObject
{
  /**
   * Returns the value of the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Logger</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logger</em>' attribute.
   * @see #setLogger(Logger)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBaseDevice_Logger()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.MLogger"
   * @generated
   */
  Logger getLogger();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getLogger <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Logger</em>' attribute.
   * @see #getLogger()
   * @generated
   */
  void setLogger(Logger value);

  /**
   * Returns the value of the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Uid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Uid</em>' attribute.
   * @see #setUid(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBaseDevice_Uid()
   * @model unique="false"
   * @generated
   */
  String getUid();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getUid <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uid</em>' attribute.
   * @see #getUid()
   * @generated
   */
  void setUid(String value);

  /**
   * Returns the value of the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Enabled A</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Enabled A</em>' attribute.
   * @see #setEnabledA(AtomicBoolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBaseDevice_EnabledA()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.MAtomicBoolean"
   * @generated
   */
  AtomicBoolean getEnabledA();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getEnabledA <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Enabled A</em>' attribute.
   * @see #getEnabledA()
   * @generated
   */
  void setEnabledA(AtomicBoolean value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void init();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void enable();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void disable();

} // MBaseDevice
