/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Color Actor</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.ColorActor#getColor <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ColorActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Color</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Color</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Color</em>' attribute.
   * @see #setColor(HSBValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorActor_Color()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.HSBValue"
   * @generated
   */
  HSBValue getColor();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.ColorActor#getColor <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Color</em>' attribute.
   * @see #getColor()
   * @generated
   */
  void setColor(HSBValue value);

} // ColorActor
