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

import com.tinkerforge.IPConnection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.slf4j.Logger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBrickd</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getHost <em>Host</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getPort <em>Port</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isIsConnected <em>Is Connected</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isAutoReconnect <em>Auto Reconnect</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isReconnected <em>Reconnected</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getTimeout <em>Timeout</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getMdevices <em>Mdevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getEcosystem <em>Ecosystem</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd()
 * @model
 * @generated
 */
public interface MBrickd extends EObject
{
  /**
   * Returns the value of the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Logger</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logger</em>' attribute.
   * @see #setLogger(Logger)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Logger()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.MLogger"
   * @generated
   */
  Logger getLogger();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getLogger <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Logger</em>' attribute.
   * @see #getLogger()
   * @generated
   */
  void setLogger(Logger value);

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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_IpConnection()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.MIPConnection"
   * @generated
   */
  IPConnection getIpConnection();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getIpConnection <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ip Connection</em>' attribute.
   * @see #getIpConnection()
   * @generated
   */
  void setIpConnection(IPConnection value);

  /**
   * Returns the value of the '<em><b>Host</b></em>' attribute.
   * The default value is <code>"localhost"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Host</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Host</em>' attribute.
   * @see #setHost(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Host()
   * @model default="localhost" unique="false"
   * @generated
   */
  String getHost();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getHost <em>Host</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Host</em>' attribute.
   * @see #getHost()
   * @generated
   */
  void setHost(String value);

  /**
   * Returns the value of the '<em><b>Port</b></em>' attribute.
   * The default value is <code>"4223"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Port</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Port</em>' attribute.
   * @see #setPort(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Port()
   * @model default="4223" unique="false"
   * @generated
   */
  int getPort();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getPort <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Port</em>' attribute.
   * @see #getPort()
   * @generated
   */
  void setPort(int value);

  /**
   * Returns the value of the '<em><b>Is Connected</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Is Connected</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Is Connected</em>' attribute.
   * @see #setIsConnected(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_IsConnected()
   * @model default="false" unique="false"
   * @generated
   */
  boolean isIsConnected();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isIsConnected <em>Is Connected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Connected</em>' attribute.
   * @see #isIsConnected()
   * @generated
   */
  void setIsConnected(boolean value);

  /**
   * Returns the value of the '<em><b>Auto Reconnect</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Auto Reconnect</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Auto Reconnect</em>' attribute.
   * @see #setAutoReconnect(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_AutoReconnect()
   * @model default="true" unique="false"
   * @generated
   */
  boolean isAutoReconnect();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isAutoReconnect <em>Auto Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Auto Reconnect</em>' attribute.
   * @see #isAutoReconnect()
   * @generated
   */
  void setAutoReconnect(boolean value);

  /**
   * Returns the value of the '<em><b>Reconnected</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Reconnected</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reconnected</em>' attribute.
   * @see #setReconnected(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Reconnected()
   * @model default="false" unique="false"
   * @generated
   */
  boolean isReconnected();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isReconnected <em>Reconnected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reconnected</em>' attribute.
   * @see #isReconnected()
   * @generated
   */
  void setReconnected(boolean value);

  /**
   * Returns the value of the '<em><b>Timeout</b></em>' attribute.
   * The default value is <code>"2500"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Timeout</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Timeout</em>' attribute.
   * @see #setTimeout(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Timeout()
   * @model default="2500" unique="false"
   * @generated
   */
  int getTimeout();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getTimeout <em>Timeout</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Timeout</em>' attribute.
   * @see #getTimeout()
   * @generated
   */
  void setTimeout(int value);

  /**
   * Returns the value of the '<em><b>Mdevices</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.binding.tinkerforge.internal.model.MDevice}&lt;?>.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd <em>Brickd</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mdevices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mdevices</em>' containment reference list.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Mdevices()
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd
   * @model opposite="brickd" containment="true"
   * @generated
   */
  EList<MDevice<?>> getMdevices();

  /**
   * Returns the value of the '<em><b>Ecosystem</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getMbrickds <em>Mbrickds</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ecosystem</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ecosystem</em>' container reference.
   * @see #setEcosystem(Ecosystem)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickd_Ecosystem()
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getMbrickds
   * @model opposite="mbrickds" transient="false"
   * @generated
   */
  Ecosystem getEcosystem();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getEcosystem <em>Ecosystem</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ecosystem</em>' container reference.
   * @see #getEcosystem()
   * @generated
   */
  void setEcosystem(Ecosystem value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void connect();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void disconnect();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void init();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" uidUnique="false"
   * @generated
   */
  MBaseDevice getDevice(String uid);

} // MBrickd
