/**
 * 
 *  openHAB, the open Home Automation Bus.
 *  Copyright (C)  2013, Thomas Weiss <theo.weiss@gmail.com>
 * 
 *  See the contributors.txt file in the distribution for a
 *  full listing of individual contributors.
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 3 of the
 *  License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 * 
 *  Additional permission under GNU GPL version 3 section 7
 * 
 *  If you modify this Program, or any covered work, by linking or
 *  combining it with Eclipse (or a modified version of that library),
 *  containing parts covered by the terms of the Eclipse Public License
 *  (EPL), the licensors of this Program grant you additional permission
 *  to convey the resulting work.
 * 
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLCD20x4;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet LCD2 0x4</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositionPrefix <em>Position Prefix</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositonSuffix <em>Positon Suffix</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#isDisplayErrors <em>Display Errors</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getErrorPrefix <em>Error Prefix</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletLCD20x4> org.openhab.binding.tinkerforge.internal.model.MTextActor org.openhab.binding.tinkerforge.internal.model.MInSwitchActor org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button>"
 * @generated
 */
public interface MBrickletLCD20x4 extends MDevice<BrickletLCD20x4>, MTextActor, MInSwitchActor, MSubDeviceHolder<MLCD20x4Button>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_LCD20x4"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4_DeviceType()
   * @model default="bricklet_LCD20x4" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Position Prefix</b></em>' attribute.
   * The default value is <code>"TFNUM<"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Position Prefix</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Position Prefix</em>' attribute.
   * @see #setPositionPrefix(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4_PositionPrefix()
   * @model default="TFNUM<" unique="false"
   * @generated
   */
  String getPositionPrefix();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositionPrefix <em>Position Prefix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Position Prefix</em>' attribute.
   * @see #getPositionPrefix()
   * @generated
   */
  void setPositionPrefix(String value);

  /**
   * Returns the value of the '<em><b>Positon Suffix</b></em>' attribute.
   * The default value is <code>">"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Positon Suffix</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Positon Suffix</em>' attribute.
   * @see #setPositonSuffix(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4_PositonSuffix()
   * @model default=">" unique="false"
   * @generated
   */
  String getPositonSuffix();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositonSuffix <em>Positon Suffix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Positon Suffix</em>' attribute.
   * @see #getPositonSuffix()
   * @generated
   */
  void setPositonSuffix(String value);

  /**
   * Returns the value of the '<em><b>Display Errors</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Display Errors</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Display Errors</em>' attribute.
   * @see #setDisplayErrors(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4_DisplayErrors()
   * @model default="true" unique="false"
   * @generated
   */
  boolean isDisplayErrors();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#isDisplayErrors <em>Display Errors</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Display Errors</em>' attribute.
   * @see #isDisplayErrors()
   * @generated
   */
  void setDisplayErrors(boolean value);

  /**
   * Returns the value of the '<em><b>Error Prefix</b></em>' attribute.
   * The default value is <code>"openhab Error:"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Error Prefix</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Error Prefix</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD20x4_ErrorPrefix()
   * @model default="openhab Error:" unique="false" changeable="false"
   * @generated
   */
  String getErrorPrefix();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MBrickletLCD20x4
