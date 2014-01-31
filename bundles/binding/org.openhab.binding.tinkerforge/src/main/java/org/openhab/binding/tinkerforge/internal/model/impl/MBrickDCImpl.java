/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.DCDriveMode;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickDC;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBrick DC</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getCurrentVelocity <em>Current Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getPwmFrequency <em>Pwm Frequency</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDriveMode <em>Drive Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getSwitchOnVelocity <em>Switch On Velocity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickDCImpl extends MinimalEObjectImpl.Container implements MBrickDC
{
  /**
   * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected static final OnOffValue SWITCH_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected OnOffValue switchState = SWITCH_STATE_EDEFAULT;

  /**
   * The default value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected static final Logger LOGGER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected Logger logger = LOGGER_EDEFAULT;

  /**
   * The default value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected static final String UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected String uid = UID_EDEFAULT;

  /**
   * The default value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected static final AtomicBoolean ENABLED_A_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected AtomicBoolean enabledA = ENABLED_A_EDEFAULT;

  /**
   * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTinkerforgeDevice()
   * @generated
   * @ordered
   */
  protected BrickDC tinkerforgeDevice;

  /**
   * The default value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected static final IPConnection IP_CONNECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected IPConnection ipConnection = IP_CONNECTION_EDEFAULT;

  /**
   * The default value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected static final String CONNECTED_UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected String connectedUid = CONNECTED_UID_EDEFAULT;

  /**
   * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected static final char POSITION_EDEFAULT = '\u0000';

  /**
   * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected char position = POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected static final int DEVICE_IDENTIFIER_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected int deviceIdentifier = DEVICE_IDENTIFIER_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected TFBrickDCConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "brick_dc";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected static final short VELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected short velocity = VELOCITY_EDEFAULT;

  /**
   * The default value of the '{@link #getCurrentVelocity() <em>Current Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCurrentVelocity()
   * @generated
   * @ordered
   */
  protected static final short CURRENT_VELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getCurrentVelocity() <em>Current Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCurrentVelocity()
   * @generated
   * @ordered
   */
  protected short currentVelocity = CURRENT_VELOCITY_EDEFAULT;

  /**
   * The default value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceleration()
   * @generated
   * @ordered
   */
  protected static final int ACCELERATION_EDEFAULT = 10000;

  /**
   * The cached value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceleration()
   * @generated
   * @ordered
   */
  protected int acceleration = ACCELERATION_EDEFAULT;

  /**
   * The default value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPwmFrequency()
   * @generated
   * @ordered
   */
  protected static final int PWM_FREQUENCY_EDEFAULT = 15000;

  /**
   * The cached value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPwmFrequency()
   * @generated
   * @ordered
   */
  protected int pwmFrequency = PWM_FREQUENCY_EDEFAULT;

  /**
   * The default value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriveMode()
   * @generated
   * @ordered
   */
  protected static final DCDriveMode DRIVE_MODE_EDEFAULT = DCDriveMode.BRAKE;

  /**
   * The cached value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriveMode()
   * @generated
   * @ordered
   */
  protected DCDriveMode driveMode = DRIVE_MODE_EDEFAULT;

  /**
   * The default value of the '{@link #getSwitchOnVelocity() <em>Switch On Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchOnVelocity()
   * @generated
   * @ordered
   */
  protected static final short SWITCH_ON_VELOCITY_EDEFAULT = 10000;

  /**
   * The cached value of the '{@link #getSwitchOnVelocity() <em>Switch On Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchOnVelocity()
   * @generated
   * @ordered
   */
  protected short switchOnVelocity = SWITCH_ON_VELOCITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickDCImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ModelPackage.Literals.MBRICK_DC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue getSwitchState()
  {
    return switchState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchState(OnOffValue newSwitchState)
  {
    OnOffValue oldSwitchState = switchState;
    switchState = newSwitchState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__SWITCH_STATE, oldSwitchState, switchState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void turnSwitch(OnOffValue state) {
		logger.trace("turnSwitch called");
		try {
			if (state == OnOffValue.OFF) {
				tinkerforgeDevice.setVelocity((short) 0);
			} else if (state == OnOffValue.ON) {
				tinkerforgeDevice.setVelocity(switchOnVelocity);
			}
			else {
				logger.error("{} unkown switchstate {}", LoggerConstants.TFMODELUPDATE, state);
			}
			switchState = state == null ? OnOffValue.UNDEF
					: state;
			setSwitchState(switchState);
		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public OnOffValue fetchSwitchState() {
		OnOffValue value = OnOffValue.UNDEF;
		try {
			short currentVelocity = tinkerforgeDevice.getVelocity();
			value = currentVelocity == 0 ? OnOffValue.OFF : OnOffValue.ON;
		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
		}
		return value;
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Logger getLogger()
  {
    return logger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLogger(Logger newLogger)
  {
    Logger oldLogger = logger;
    logger = newLogger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__LOGGER, oldLogger, logger));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUid()
  {
    return uid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUid(String newUid)
  {
    String oldUid = uid;
    uid = newUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__UID, oldUid, uid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AtomicBoolean getEnabledA()
  {
    return enabledA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEnabledA(AtomicBoolean newEnabledA)
  {
    AtomicBoolean oldEnabledA = enabledA;
    enabledA = newEnabledA;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickDC getTinkerforgeDevice()
  {
    return tinkerforgeDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTinkerforgeDevice(BrickDC newTinkerforgeDevice)
  {
    BrickDC oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IPConnection getIpConnection()
  {
    return ipConnection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIpConnection(IPConnection newIpConnection)
  {
    IPConnection oldIpConnection = ipConnection;
    ipConnection = newIpConnection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__IP_CONNECTION, oldIpConnection, ipConnection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectedUid()
  {
    return connectedUid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectedUid(String newConnectedUid)
  {
    String oldConnectedUid = connectedUid;
    connectedUid = newConnectedUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__CONNECTED_UID, oldConnectedUid, connectedUid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public char getPosition()
  {
    return position;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPosition(char newPosition)
  {
    char oldPosition = position;
    position = newPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__POSITION, oldPosition, position));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getDeviceIdentifier()
  {
    return deviceIdentifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDeviceIdentifier(int newDeviceIdentifier)
  {
    int oldDeviceIdentifier = deviceIdentifier;
    deviceIdentifier = newDeviceIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd getBrickd()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICK_DC__BRICKD) return null;
    return (MBrickd)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICK_DC__BRICKD, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBrickd(MBrickd newBrickd)
  {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICK_DC__BRICKD && newBrickd != null))
    {
      if (EcoreUtil.isAncestor(this, newBrickd))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newBrickd != null)
        msgs = ((InternalEObject)newBrickd).eInverseAdd(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
      msgs = basicSetBrickd(newBrickd, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__BRICKD, newBrickd, newBrickd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFBrickDCConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(TFBrickDCConfiguration newTfConfig, NotificationChain msgs)
  {
    TFBrickDCConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(TFBrickDCConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICK_DC__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICK_DC__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__TF_CONFIG, newTfConfig, newTfConfig));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDeviceType()
  {
    return deviceType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getVelocity()
  {
    return velocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocity(short newVelocity)
  {
    short oldVelocity = velocity;
    velocity = newVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__VELOCITY, oldVelocity, velocity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getCurrentVelocity()
  {
    return currentVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCurrentVelocity(short newCurrentVelocity)
  {
    short oldCurrentVelocity = currentVelocity;
    currentVelocity = newCurrentVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__CURRENT_VELOCITY, oldCurrentVelocity, currentVelocity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getAcceleration()
  {
    return acceleration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAcceleration(int newAcceleration)
  {
    int oldAcceleration = acceleration;
    acceleration = newAcceleration;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__ACCELERATION, oldAcceleration, acceleration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPwmFrequency()
  {
    return pwmFrequency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPwmFrequency(int newPwmFrequency)
  {
    int oldPwmFrequency = pwmFrequency;
    pwmFrequency = newPwmFrequency;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__PWM_FREQUENCY, oldPwmFrequency, pwmFrequency));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DCDriveMode getDriveMode()
  {
    return driveMode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDriveMode(DCDriveMode newDriveMode)
  {
    DCDriveMode oldDriveMode = driveMode;
    driveMode = newDriveMode == null ? DRIVE_MODE_EDEFAULT : newDriveMode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__DRIVE_MODE, oldDriveMode, driveMode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getSwitchOnVelocity()
  {
    return switchOnVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchOnVelocity(short newSwitchOnVelocity)
  {
    short oldSwitchOnVelocity = switchOnVelocity;
    switchOnVelocity = newSwitchOnVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__SWITCH_ON_VELOCITY, oldSwitchOnVelocity, switchOnVelocity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    logger = LoggerFactory.getLogger(MBrickDCImpl.class);
    setEnabledA(new AtomicBoolean());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void enable() {
		tinkerforgeDevice = new BrickDC(uid, ipConnection);
		if (tfConfig != null) {
			logger.debug("found tfConfig");
			if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature(
					"acceleration"))) {
				setAcceleration(tfConfig.getAcceleration());
			}
			if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature(
					"pwmFrequency"))) {
				setPwmFrequency(tfConfig.getPwmFrequency());
			}
			if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature(
					"switchOnVelocity"))) {
				setSwitchOnVelocity(tfConfig.getSwitchOnVelocity());
			}
			if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature(
					"driveMode"))) {
				setDriveMode(DCDriveMode.get(tfConfig.getDriveMode()));
			}
		}
		try {
			tinkerforgeDevice.setAcceleration(acceleration);
			tinkerforgeDevice.setPWMFrequency(pwmFrequency);
			if (driveMode == DCDriveMode.BRAKE)
				tinkerforgeDevice.setDriveMode(BrickDC.DRIVE_MODE_DRIVE_BRAKE);
			else if (driveMode == DCDriveMode.COAST)
				tinkerforgeDevice.setDriveMode(BrickDC.DRIVE_MODE_DRIVE_COAST);
			tinkerforgeDevice.enable();
			setVelocity(getCurrentVelocity());
		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
		}
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disable()
  {
	  tinkerforgeDevice = null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__BRICKD:
        return basicSetBrickd(null, msgs);
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        return basicSetTfConfig(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case ModelPackage.MBRICK_DC__BRICKD:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MBRICK_DC__LOGGER:
        return getLogger();
      case ModelPackage.MBRICK_DC__UID:
        return getUid();
      case ModelPackage.MBRICK_DC__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICK_DC__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICK_DC__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICK_DC__POSITION:
        return getPosition();
      case ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICK_DC__NAME:
        return getName();
      case ModelPackage.MBRICK_DC__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MBRICK_DC__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICK_DC__VELOCITY:
        return getVelocity();
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        return getCurrentVelocity();
      case ModelPackage.MBRICK_DC__ACCELERATION:
        return getAcceleration();
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        return getPwmFrequency();
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        return getDriveMode();
      case ModelPackage.MBRICK_DC__SWITCH_ON_VELOCITY:
        return getSwitchOnVelocity();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MBRICK_DC__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICK_DC__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICK_DC__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickDC)newValue);
        return;
      case ModelPackage.MBRICK_DC__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICK_DC__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICK_DC__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICK_DC__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICK_DC__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        setTfConfig((TFBrickDCConfiguration)newValue);
        return;
      case ModelPackage.MBRICK_DC__VELOCITY:
        setVelocity((Short)newValue);
        return;
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        setCurrentVelocity((Short)newValue);
        return;
      case ModelPackage.MBRICK_DC__ACCELERATION:
        setAcceleration((Integer)newValue);
        return;
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        setPwmFrequency((Integer)newValue);
        return;
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        setDriveMode((DCDriveMode)newValue);
        return;
      case ModelPackage.MBRICK_DC__SWITCH_ON_VELOCITY:
        setSwitchOnVelocity((Short)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickDC)null);
        return;
      case ModelPackage.MBRICK_DC__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        setTfConfig((TFBrickDCConfiguration)null);
        return;
      case ModelPackage.MBRICK_DC__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        setCurrentVelocity(CURRENT_VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__ACCELERATION:
        setAcceleration(ACCELERATION_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        setPwmFrequency(PWM_FREQUENCY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        setDriveMode(DRIVE_MODE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__SWITCH_ON_VELOCITY:
        setSwitchOnVelocity(SWITCH_ON_VELOCITY_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MBRICK_DC__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICK_DC__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICK_DC__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICK_DC__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICK_DC__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICK_DC__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICK_DC__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICK_DC__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MBRICK_DC__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICK_DC__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        return currentVelocity != CURRENT_VELOCITY_EDEFAULT;
      case ModelPackage.MBRICK_DC__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        return pwmFrequency != PWM_FREQUENCY_EDEFAULT;
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        return driveMode != DRIVE_MODE_EDEFAULT;
      case ModelPackage.MBRICK_DC__SWITCH_ON_VELOCITY:
        return switchOnVelocity != SWITCH_ON_VELOCITY_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MBRICK_DC__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.MBRICK_DC__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE: return ModelPackage.MDEVICE__TINKERFORGE_DEVICE;
        case ModelPackage.MBRICK_DC__IP_CONNECTION: return ModelPackage.MDEVICE__IP_CONNECTION;
        case ModelPackage.MBRICK_DC__CONNECTED_UID: return ModelPackage.MDEVICE__CONNECTED_UID;
        case ModelPackage.MBRICK_DC__POSITION: return ModelPackage.MDEVICE__POSITION;
        case ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER: return ModelPackage.MDEVICE__DEVICE_IDENTIFIER;
        case ModelPackage.MBRICK_DC__NAME: return ModelPackage.MDEVICE__NAME;
        case ModelPackage.MBRICK_DC__BRICKD: return ModelPackage.MDEVICE__BRICKD;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MBRICK_DC__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MBRICK_DC__UID;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.MBRICK_DC__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MDEVICE__TINKERFORGE_DEVICE: return ModelPackage.MBRICK_DC__TINKERFORGE_DEVICE;
        case ModelPackage.MDEVICE__IP_CONNECTION: return ModelPackage.MBRICK_DC__IP_CONNECTION;
        case ModelPackage.MDEVICE__CONNECTED_UID: return ModelPackage.MBRICK_DC__CONNECTED_UID;
        case ModelPackage.MDEVICE__POSITION: return ModelPackage.MBRICK_DC__POSITION;
        case ModelPackage.MDEVICE__DEVICE_IDENTIFIER: return ModelPackage.MBRICK_DC__DEVICE_IDENTIFIER;
        case ModelPackage.MDEVICE__NAME: return ModelPackage.MBRICK_DC__NAME;
        case ModelPackage.MDEVICE__BRICKD: return ModelPackage.MBRICK_DC__BRICKD;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MBRICK_DC__TF_CONFIG;
        default: return -1;
      }
    }
    return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedOperationID(int baseOperationID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.MBRICK_DC___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.MBRICK_DC___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.MBRICK_DC___DISABLE;
        default: return -1;
      }
    }
    if (baseClass == MDevice.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    return super.eDerivedOperationID(baseOperationID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ModelPackage.MBRICK_DC___INIT:
        init();
        return null;
      case ModelPackage.MBRICK_DC___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICK_DC___DISABLE:
        disable();
        return null;
      case ModelPackage.MBRICK_DC___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.MBRICK_DC___FETCH_SWITCH_STATE:
        return fetchSwitchState();
    }
    return super.eInvoke(operationID, arguments);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (switchState: ");
    result.append(switchState);
    result.append(", logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", tinkerforgeDevice: ");
    result.append(tinkerforgeDevice);
    result.append(", ipConnection: ");
    result.append(ipConnection);
    result.append(", connectedUid: ");
    result.append(connectedUid);
    result.append(", position: ");
    result.append(position);
    result.append(", deviceIdentifier: ");
    result.append(deviceIdentifier);
    result.append(", name: ");
    result.append(name);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", velocity: ");
    result.append(velocity);
    result.append(", currentVelocity: ");
    result.append(currentVelocity);
    result.append(", acceleration: ");
    result.append(acceleration);
    result.append(", pwmFrequency: ");
    result.append(pwmFrequency);
    result.append(", driveMode: ");
    result.append(driveMode);
    result.append(", switchOnVelocity: ");
    result.append(switchOnVelocity);
    result.append(')');
    return result.toString();
  }

} //MBrickDCImpl
