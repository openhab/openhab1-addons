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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MTF Config Consumer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer#getTfConfig <em>Tf Config</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMTFConfigConsumer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MTFConfigConsumer<TFC> extends EObject
{
  /**
   * Returns the value of the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tf Config</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tf Config</em>' containment reference.
   * @see #setTfConfig(Object)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMTFConfigConsumer_TfConfig()
   * @model kind="reference" containment="true"
   * @generated
   */
  TFC getTfConfig();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer#getTfConfig <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tf Config</em>' containment reference.
   * @see #getTfConfig()
   * @generated
   */
  void setTfConfig(TFC value);

} // MTFConfigConsumer
