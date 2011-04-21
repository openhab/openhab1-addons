/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.model.items;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Group Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.items.ModelGroupItem#getFunction <em>Function</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelGroupItem#getArgs <em>Args</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.items.ItemsPackage#getModelGroupItem()
 * @model
 * @generated
 */
public interface ModelGroupItem extends ModelItem
{
  /**
   * Returns the value of the '<em><b>Function</b></em>' attribute.
   * The literals are from the enumeration {@link org.openhab.model.items.ModelGroupFunction}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Function</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Function</em>' attribute.
   * @see org.openhab.model.items.ModelGroupFunction
   * @see #setFunction(ModelGroupFunction)
   * @see org.openhab.model.items.ItemsPackage#getModelGroupItem_Function()
   * @model
   * @generated
   */
  ModelGroupFunction getFunction();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelGroupItem#getFunction <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Function</em>' attribute.
   * @see org.openhab.model.items.ModelGroupFunction
   * @see #getFunction()
   * @generated
   */
  void setFunction(ModelGroupFunction value);

  /**
   * Returns the value of the '<em><b>Args</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Args</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Args</em>' attribute list.
   * @see org.openhab.model.items.ItemsPackage#getModelGroupItem_Args()
   * @model unique="false"
   * @generated
   */
  EList<String> getArgs();

} // ModelGroupItem
