/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletIndustrialDigitalIn4;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Industrial Digital In4</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDigitalIn4()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn> org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletIndustrialDigitalIn4> org.openhab.binding.tinkerforge.internal.model.InterruptListener org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration>"
 * @generated
 */
public interface MBrickletIndustrialDigitalIn4 extends MSubDeviceHolder<MIndustrialDigitalIn>, MDevice<BrickletIndustrialDigitalIn4>, InterruptListener, MTFConfigConsumer<TFInterruptListenerConfiguration>
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_industrial_digital_4in"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDigitalIn4_DeviceType()
   * @model default="bricklet_industrial_digital_4in" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();
} // MBrickletIndustrialDigitalIn4
