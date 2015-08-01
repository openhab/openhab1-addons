/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Switch Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableSwitchActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableSwitchActor extends SwitchSensor
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model stateDataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" stateUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void turnSwitch(OnOffValue state, DeviceOptions opts);

} // ProgrammableSwitchActor
