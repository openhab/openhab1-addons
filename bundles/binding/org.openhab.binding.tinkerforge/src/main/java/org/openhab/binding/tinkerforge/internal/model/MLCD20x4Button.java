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
 * A representation of the model object '<em><b>MLCD2 0x4 Button</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum <em>Button Num</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getCallbackPeriod <em>Callback Period</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button()
 * @model
 * @generated
 */
public interface MLCD20x4Button extends MOutSwitchActor, MLCDSubDevice
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"lcd_button"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button_DeviceType()
   * @model default="lcd_button" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Button Num</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Button Num</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Button Num</em>' attribute.
   * @see #setButtonNum(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button_ButtonNum()
   * @model unique="false"
   * @generated
   */
  short getButtonNum();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum <em>Button Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Button Num</em>' attribute.
   * @see #getButtonNum()
   * @generated
   */
  void setButtonNum(short value);

  /**
   * Returns the value of the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Callback Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Callback Period</em>' attribute.
   * @see #setCallbackPeriod(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD20x4Button_CallbackPeriod()
   * @model unique="false"
   * @generated
   */
  int getCallbackPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getCallbackPeriod <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Callback Period</em>' attribute.
   * @see #getCallbackPeriod()
   * @generated
   */
  void setCallbackPeriod(int value);

} // MLCD20x4Button
