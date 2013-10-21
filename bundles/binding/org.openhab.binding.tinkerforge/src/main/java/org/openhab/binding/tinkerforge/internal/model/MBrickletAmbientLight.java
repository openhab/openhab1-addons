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

import com.tinkerforge.BrickletAmbientLight;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Ambient Light</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getIlluminance <em>Illuminance</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLight()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletAmbientLight> org.openhab.binding.tinkerforge.internal.model.MSensor<org.eclipse.emf.ecore.EDoubleObject> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickletAmbientLight extends MDevice<BrickletAmbientLight>, MSensor<Double>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_ambient_light"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLight_DeviceType()
   * @model default="bricklet_ambient_light" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Illuminance</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Illuminance</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Illuminance</em>' attribute.
   * @see #setIlluminance(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLight_Illuminance()
   * @model unique="false"
   * @generated
   */
  int getIlluminance();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getIlluminance <em>Illuminance</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Illuminance</em>' attribute.
   * @see #getIlluminance()
   * @generated
   */
  void setIlluminance(int value);

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"10"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAmbientLight_Threshold()
   * @model default="10" unique="false"
   * @generated
   */
  int getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(int value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MBrickletAmbientLight
