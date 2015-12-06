/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Switch B</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getAddress <em>Address</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getRepeats <em>Repeats</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getAbsDimmValue <em>Abs Dimm Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.PercentValue> org.openhab.binding.tinkerforge.internal.model.RemoteSwitch org.openhab.binding.tinkerforge.internal.model.DimmableActor<org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration> org.openhab.binding.tinkerforge.internal.model.PercentTypeActor"
 * @generated
 */
public interface RemoteSwitchB extends MSensor<PercentValue>, RemoteSwitch, DimmableActor<RemoteSwitchBConfiguration>, PercentTypeActor
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"remote_switch_b"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB_DeviceType()
   * @model default="remote_switch_b" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Address</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Address</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Address</em>' attribute.
   * @see #setAddress(Long)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB_Address()
   * @model unique="false"
   * @generated
   */
  Long getAddress();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getAddress <em>Address</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Address</em>' attribute.
   * @see #getAddress()
   * @generated
   */
  void setAddress(Long value);

  /**
   * Returns the value of the '<em><b>Unit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Unit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Unit</em>' attribute.
   * @see #setUnit(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB_Unit()
   * @model unique="false"
   * @generated
   */
  Short getUnit();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getUnit <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Unit</em>' attribute.
   * @see #getUnit()
   * @generated
   */
  void setUnit(Short value);

  /**
   * Returns the value of the '<em><b>Repeats</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repeats</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repeats</em>' attribute.
   * @see #setRepeats(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB_Repeats()
   * @model unique="false"
   * @generated
   */
  Short getRepeats();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getRepeats <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Repeats</em>' attribute.
   * @see #getRepeats()
   * @generated
   */
  void setRepeats(Short value);

  /**
   * Returns the value of the '<em><b>Abs Dimm Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Abs Dimm Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Abs Dimm Value</em>' attribute.
   * @see #setAbsDimmValue(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchB_AbsDimmValue()
   * @model unique="false"
   * @generated
   */
  Short getAbsDimmValue();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB#getAbsDimmValue <em>Abs Dimm Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Abs Dimm Value</em>' attribute.
   * @see #getAbsDimmValue()
   * @generated
   */
  void setAbsDimmValue(Short value);
} // RemoteSwitchB
