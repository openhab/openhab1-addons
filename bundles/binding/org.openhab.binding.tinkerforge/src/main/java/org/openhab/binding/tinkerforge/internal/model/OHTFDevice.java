/**
 * 
 *  Tinkerforge Binding Copyright (C) 2013 Theo Weiss <theo.weiss@gmail.com> contributed to: openHAB, the open Home Automation Bus.
 *  Copyright (C)  2013, openHAB.org <admin@openhab.org>
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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>OHTF Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubid <em>Subid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhid <em>Ohid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig <em>Oh Config</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice()
 * @model
 * @generated
 */
public interface OHTFDevice<TFC extends TFConfig> extends EObject
{
  /**
   * Returns the value of the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Uid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Uid</em>' attribute.
   * @see #setUid(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_Uid()
   * @model unique="false"
   * @generated
   */
  String getUid();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getUid <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uid</em>' attribute.
   * @see #getUid()
   * @generated
   */
  void setUid(String value);

  /**
   * Returns the value of the '<em><b>Subid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subid</em>' attribute.
   * @see #setSubid(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_Subid()
   * @model unique="false"
   * @generated
   */
  String getSubid();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubid <em>Subid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subid</em>' attribute.
   * @see #getSubid()
   * @generated
   */
  void setSubid(String value);

  /**
   * Returns the value of the '<em><b>Ohid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ohid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ohid</em>' attribute.
   * @see #setOhid(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_Ohid()
   * @model unique="false"
   * @generated
   */
  String getOhid();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhid <em>Ohid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ohid</em>' attribute.
   * @see #getOhid()
   * @generated
   */
  void setOhid(String value);

  /**
   * Returns the value of the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tf Config</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tf Config</em>' containment reference.
   * @see #setTfConfig(TFConfig)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_TfConfig()
   * @model containment="true"
   * @generated
   */
  TFC getTfConfig();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getTfConfig <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tf Config</em>' containment reference.
   * @see #getTfConfig()
   * @generated
   */
  void setTfConfig(TFC value);

  /**
   * Returns the value of the '<em><b>Oh Config</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig#getOhTfDevices <em>Oh Tf Devices</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Oh Config</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Oh Config</em>' container reference.
   * @see #setOhConfig(OHConfig)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_OhConfig()
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig#getOhTfDevices
   * @model opposite="ohTfDevices" transient="false"
   * @generated
   */
  OHConfig getOhConfig();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig <em>Oh Config</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Oh Config</em>' container reference.
   * @see #getOhConfig()
   * @generated
   */
  void setOhConfig(OHConfig value);

} // OHTFDevice
