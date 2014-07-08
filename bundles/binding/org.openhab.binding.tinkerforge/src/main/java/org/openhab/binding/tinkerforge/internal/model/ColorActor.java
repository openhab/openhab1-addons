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

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Color Actor</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ColorActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model colorDataType="org.openhab.binding.tinkerforge.internal.model.HSBType" colorUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void setColor(HSBType color, DeviceOptions opts);

} // ColorActor
