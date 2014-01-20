/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MDevice</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd <em>Brickd</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice()
 * @model interface="true" abstract="true" TFBounds="org.openhab.binding.tinkerforge.internal.model.MTinkerforgeDevice"
 * @generated
 */
public interface MDevice<TF extends Device> extends MBaseDevice
{
  /**
   * Returns the value of the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tinkerforge Device</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tinkerforge Device</em>' attribute.
   * @see #setTinkerforgeDevice(Device)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_TinkerforgeDevice()
   * @model unique="false"
   * @generated
   */
  TF getTinkerforgeDevice();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getTinkerforgeDevice <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tinkerforge Device</em>' attribute.
   * @see #getTinkerforgeDevice()
   * @generated
   */
  void setTinkerforgeDevice(TF value);

  /**
   * Returns the value of the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ip Connection</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ip Connection</em>' attribute.
   * @see #setIpConnection(IPConnection)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_IpConnection()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.MIPConnection"
   * @generated
   */
  IPConnection getIpConnection();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getIpConnection <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ip Connection</em>' attribute.
   * @see #getIpConnection()
   * @generated
   */
  void setIpConnection(IPConnection value);

  /**
   * Returns the value of the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Connected Uid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Connected Uid</em>' attribute.
   * @see #setConnectedUid(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_ConnectedUid()
   * @model unique="false"
   * @generated
   */
  String getConnectedUid();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getConnectedUid <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Connected Uid</em>' attribute.
   * @see #getConnectedUid()
   * @generated
   */
  void setConnectedUid(String value);

  /**
   * Returns the value of the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Position</em>' attribute.
   * @see #setPosition(char)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_Position()
   * @model unique="false"
   * @generated
   */
  char getPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getPosition <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Position</em>' attribute.
   * @see #getPosition()
   * @generated
   */
  void setPosition(char value);

  /**
   * Returns the value of the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Identifier</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Identifier</em>' attribute.
   * @see #setDeviceIdentifier(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_DeviceIdentifier()
   * @model unique="false"
   * @generated
   */
  int getDeviceIdentifier();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getDeviceIdentifier <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Device Identifier</em>' attribute.
   * @see #getDeviceIdentifier()
   * @generated
   */
  void setDeviceIdentifier(int value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_Name()
   * @model unique="false"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Brickd</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getMdevices <em>Mdevices</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Brickd</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Brickd</em>' container reference.
   * @see #setBrickd(MBrickd)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMDevice_Brickd()
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getMdevices
   * @model opposite="mdevices" transient="false"
   * @generated
   */
  MBrickd getBrickd();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd <em>Brickd</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Brickd</em>' container reference.
   * @see #getBrickd()
   * @generated
   */
  void setBrickd(MBrickd value);

} // MDevice
