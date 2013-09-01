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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TF Brick DC Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getPwmFrequency <em>Pwm Frequency</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getDriveMode <em>Drive Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getSwitchOnVelocity <em>Switch On Velocity</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration()
 * @model
 * @generated
 */
public interface TFBrickDCConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity</em>' attribute.
   * @see #setVelocity(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration_Velocity()
   * @model unique="false"
   * @generated
   */
  short getVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getVelocity <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity</em>' attribute.
   * @see #getVelocity()
   * @generated
   */
  void setVelocity(short value);

  /**
   * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Acceleration</em>' attribute.
   * @see #setAcceleration(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration_Acceleration()
   * @model unique="false"
   * @generated
   */
  int getAcceleration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getAcceleration <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceleration</em>' attribute.
   * @see #getAcceleration()
   * @generated
   */
  void setAcceleration(int value);

  /**
   * Returns the value of the '<em><b>Pwm Frequency</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pwm Frequency</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pwm Frequency</em>' attribute.
   * @see #setPwmFrequency(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration_PwmFrequency()
   * @model unique="false"
   * @generated
   */
  int getPwmFrequency();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getPwmFrequency <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pwm Frequency</em>' attribute.
   * @see #getPwmFrequency()
   * @generated
   */
  void setPwmFrequency(int value);

  /**
   * Returns the value of the '<em><b>Drive Mode</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Drive Mode</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Drive Mode</em>' attribute.
   * @see #setDriveMode(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration_DriveMode()
   * @model unique="false"
   * @generated
   */
  int getDriveMode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getDriveMode <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Drive Mode</em>' attribute.
   * @see #getDriveMode()
   * @generated
   */
  void setDriveMode(int value);

  /**
   * Returns the value of the '<em><b>Switch On Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Switch On Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Switch On Velocity</em>' attribute.
   * @see #setSwitchOnVelocity(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickDCConfiguration_SwitchOnVelocity()
   * @model unique="false"
   * @generated
   */
  short getSwitchOnVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getSwitchOnVelocity <em>Switch On Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Switch On Velocity</em>' attribute.
   * @see #getSwitchOnVelocity()
   * @generated
   */
  void setSwitchOnVelocity(short value);

} // TFBrickDCConfiguration
