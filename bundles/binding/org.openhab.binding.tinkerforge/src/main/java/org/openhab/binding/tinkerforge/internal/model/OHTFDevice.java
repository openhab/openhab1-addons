/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.common.util.EList;
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
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubDeviceIds <em>Sub Device Ids</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig <em>Oh Config</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice()
 * @model IDSBounds="org.openhab.binding.tinkerforge.internal.model.Enum"
 * @generated
 */
@SuppressWarnings("rawtypes")
public interface OHTFDevice<TFC extends TFConfig, IDS extends Enum> extends EObject
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
   * Returns the value of the '<em><b>Sub Device Ids</b></em>' attribute list.
   * The list contents are of type {@link IDS}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sub Device Ids</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub Device Ids</em>' attribute list.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFDevice_SubDeviceIds()
   * @model unique="false"
   * @generated
   */
  EList<IDS> getSubDeviceIds();

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

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" subIdUnique="false"
   *        annotation="http://www.eclipse.org/emf/2002/GenModel body='<%org.openhab.binding.tinkerforge.internal.model.OHTFDevice%><TFC,IDS> _this = this;\n<%org.eclipse.emf.common.util.EList%><IDS> _subDeviceIds = _this.getSubDeviceIds();\nfor (final IDS sid : _subDeviceIds)\n{\n\t<%java.lang.String%> _string = sid.toString();\n\tboolean _equalsIgnoreCase = _string.equalsIgnoreCase(subId);\n\tif (_equalsIgnoreCase)\n\t{\n\t\treturn true;\n\t}\n}\nreturn false;'"
   * @generated
   */
  boolean isValidSubId(String subId);

} // OHTFDevice
