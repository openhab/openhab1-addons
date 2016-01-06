/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bricklet Dual Button Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletDualButtonConfiguration#getAutotoggleleft <em>Autotoggleleft</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletDualButtonConfiguration#getAutotoggleright <em>Autotoggleright</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletDualButtonConfiguration()
 * @model
 * @generated
 */
public interface BrickletDualButtonConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Autotoggleleft</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Autotoggleleft</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Autotoggleleft</em>' attribute.
   * @see #setAutotoggleleft(Boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletDualButtonConfiguration_Autotoggleleft()
   * @model default="false" unique="false"
   * @generated
   */
  Boolean getAutotoggleleft();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletDualButtonConfiguration#getAutotoggleleft <em>Autotoggleleft</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Autotoggleleft</em>' attribute.
   * @see #getAutotoggleleft()
   * @generated
   */
  void setAutotoggleleft(Boolean value);

  /**
   * Returns the value of the '<em><b>Autotoggleright</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Autotoggleright</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Autotoggleright</em>' attribute.
   * @see #setAutotoggleright(Boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletDualButtonConfiguration_Autotoggleright()
   * @model default="false" unique="false"
   * @generated
   */
  Boolean getAutotoggleright();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletDualButtonConfiguration#getAutotoggleright <em>Autotoggleright</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Autotoggleright</em>' attribute.
   * @see #getAutotoggleright()
   * @generated
   */
  void setAutotoggleright(Boolean value);

} // BrickletDualButtonConfiguration
