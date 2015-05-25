/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getValueActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ValueActor extends DimmableActor
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueUnique="false"
   * @generated
   */
  void setValue(BigDecimal newValue);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueDataType="org.openhab.binding.tinkerforge.internal.model.PercentType" newValueUnique="false"
   * @generated
   */
  void setValue(PercentType newValue);

} // ValueActor
