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
 * A representation of the model object '<em><b>MBarometer Temperature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getTemperature <em>Temperature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBarometerTemperature()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.eclipse.emf.ecore.EDoubleObject> org.openhab.binding.tinkerforge.internal.model.MSubDevice<org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer>"
 * @generated
 */
public interface MBarometerTemperature extends MSensor<Double>, MSubDevice<MBrickletBarometer>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"barometer_temperature"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBarometerTemperature_DeviceType()
   * @model default="barometer_temperature" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Temperature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Temperature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Temperature</em>' attribute.
   * @see #setTemperature(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBarometerTemperature_Temperature()
   * @model unique="false"
   * @generated
   */
  short getTemperature();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getTemperature <em>Temperature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Temperature</em>' attribute.
   * @see #getTemperature()
   * @generated
   */
  void setTemperature(short value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MBarometerTemperature
