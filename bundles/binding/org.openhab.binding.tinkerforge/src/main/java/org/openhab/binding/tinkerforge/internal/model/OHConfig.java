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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>OH Config</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHConfig#getOhTfDevices <em>Oh Tf Devices</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHConfig()
 * @model
 * @generated
 */
public interface OHConfig extends EObject
{
  /**
   * Returns the value of the '<em><b>Oh Tf Devices</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice}&lt;?>.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig <em>Oh Config</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Oh Tf Devices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Oh Tf Devices</em>' containment reference list.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHConfig_OhTfDevices()
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig
   * @model opposite="ohConfig" containment="true"
   * @generated
   */
  EList<OHTFDevice<?>> getOhTfDevices();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" uidUnique="false" subidUnique="false"
   * @generated
   */
  OHTFDevice<?> getConfigByTFId(String uid, String subid);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" ohidUnique="false"
   * @generated
   */
  OHTFDevice<?> getConfigByOHId(String ohid);

} // OHConfig
