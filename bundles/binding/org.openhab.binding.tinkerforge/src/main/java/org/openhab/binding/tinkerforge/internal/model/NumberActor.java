/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Number Actor</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNumberActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NumberActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model valueUnique="false"
   * @generated
   */
  void setNumber(BigDecimal value);

} // NumberActor
