/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MSub Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick <em>Mbrick</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDevice()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MSubDevice<B extends MSubDeviceHolder<?>> extends MBaseDevice
{
  /**
   * Returns the value of the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sub Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub Id</em>' attribute.
   * @see #setSubId(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDevice_SubId()
   * @model unique="false"
   * @generated
   */
  String getSubId();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getSubId <em>Sub Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sub Id</em>' attribute.
   * @see #getSubId()
   * @generated
   */
  void setSubId(String value);

  /**
   * Returns the value of the '<em><b>Mbrick</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices <em>Msubdevices</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mbrick</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mbrick</em>' container reference.
   * @see #setMbrick(MSubDeviceHolder)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMSubDevice_Mbrick()
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices
   * @model opposite="msubdevices" transient="false"
   * @generated
   */
  B getMbrick();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick <em>Mbrick</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mbrick</em>' container reference.
   * @see #getMbrick()
   * @generated
   */
  void setMbrick(B value);

} // MSubDevice
