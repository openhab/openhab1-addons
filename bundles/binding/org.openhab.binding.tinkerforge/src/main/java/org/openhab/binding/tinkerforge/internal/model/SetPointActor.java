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

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.core.library.types.PercentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Point Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSetPointActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface SetPointActor<C extends DimmableConfiguration> extends DimmableActor<C>, PercentTypeActor
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void setValue(BigDecimal newValue, DeviceOptions opts);

} // SetPointActor
