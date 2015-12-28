/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsDimmable;
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsMove;
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsSwitchSpeed;
import org.openhab.binding.tinkerforge.internal.model.DCDriveMode;
import org.openhab.binding.tinkerforge.internal.model.DimmableActor;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MoveActor;
import org.openhab.binding.tinkerforge.internal.model.PercentTypeActor;
import org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.SetPointActor;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.DirectionValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickDC;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>MBrick DC</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0 <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getMaxValue <em>Max Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getPercentValue <em>Percent Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getCallbackPeriod <em>Callback Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getMaxVelocity <em>Max Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getMinVelocity <em>Min Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getTargetvelocity <em>Targetvelocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getCurrentVelocity <em>Current Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getPwmFrequency <em>Pwm Frequency</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl#getDriveMode <em>Drive Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickDCImpl extends MinimalEObjectImpl.Container implements MBrickDC
{
  /**
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected DecimalValue sensorValue;

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
   * The default value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected static final boolean POLL_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected boolean poll = POLL_EDEFAULT;

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
   * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected static final DirectionValue DIRECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected DirectionValue direction = DIRECTION_EDEFAULT;

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
   * The default value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinValue()
   * @generated
   * @ordered
   */
  protected static final BigDecimal MIN_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinValue()
   * @generated
   * @ordered
   */
  protected BigDecimal minValue = MIN_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxValue()
   * @generated
   * @ordered
   */
  protected static final BigDecimal MAX_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxValue()
   * @generated
   * @ordered
   */
  protected BigDecimal maxValue = MAX_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getPercentValue() <em>Percent Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPercentValue()
   * @generated
   * @ordered
   */
  protected static final PercentValue PERCENT_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPercentValue() <em>Percent Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPercentValue()
   * @generated
   * @ordered
   */
  protected PercentValue percentValue = PERCENT_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected static final long CALLBACK_PERIOD_EDEFAULT = 1000L;

  /**
   * The cached value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected long callbackPeriod = CALLBACK_PERIOD_EDEFAULT;

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
   * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThreshold()
   * @generated
   * @ordered
   */
  protected static final BigDecimal THRESHOLD_EDEFAULT = new BigDecimal("10");

  /**
   * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThreshold()
   * @generated
   * @ordered
   */
  protected BigDecimal threshold = THRESHOLD_EDEFAULT;

  /**
   * The default value of the '{@link #getMaxVelocity() <em>Max Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxVelocity()
   * @generated
   * @ordered
   */
  protected static final Short MAX_VELOCITY_EDEFAULT = new Short((short)32767);

  /**
   * The cached value of the '{@link #getMaxVelocity() <em>Max Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxVelocity()
   * @generated
   * @ordered
   */
  protected Short maxVelocity = MAX_VELOCITY_EDEFAULT;

  /**
   * The default value of the '{@link #getMinVelocity() <em>Min Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinVelocity()
   * @generated
   * @ordered
   */
  protected static final Short MIN_VELOCITY_EDEFAULT = new Short((short)-32767);

  /**
   * The cached value of the '{@link #getMinVelocity() <em>Min Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinVelocity()
   * @generated
   * @ordered
   */
  protected Short minVelocity = MIN_VELOCITY_EDEFAULT;

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
   * The default value of the '{@link #getTargetvelocity() <em>Targetvelocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetvelocity()
   * @generated
   * @ordered
   */
  protected static final short TARGETVELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getTargetvelocity() <em>Targetvelocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetvelocity()
   * @generated
   * @ordered
   */
  protected short targetvelocity = TARGETVELOCITY_EDEFAULT;

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

  private VelocityListener listener;

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
  public DecimalValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(DecimalValue newSensorValue)
  {
    DecimalValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__SENSOR_VALUE, oldSensorValue, sensorValue));
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
  public boolean isPoll()
  {
    return poll;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPoll(boolean newPoll)
  {
    boolean oldPoll = poll;
    poll = newPoll;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__POLL, oldPoll, poll));
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
  public BigDecimal getMinValue()
  {
    return minValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMinValue(BigDecimal newMinValue)
  {
    BigDecimal oldMinValue = minValue;
    minValue = newMinValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__MIN_VALUE, oldMinValue, minValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BigDecimal getMaxValue()
  {
    return maxValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMaxValue(BigDecimal newMaxValue)
  {
    BigDecimal oldMaxValue = maxValue;
    maxValue = newMaxValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__MAX_VALUE, oldMaxValue, maxValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PercentValue getPercentValue()
  {
    return percentValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPercentValue(PercentValue newPercentValue)
  {
    PercentValue oldPercentValue = percentValue;
    percentValue = newPercentValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__PERCENT_VALUE, oldPercentValue, percentValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DirectionValue getDirection()
  {
    return direction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDirection(DirectionValue newDirection)
  {
    DirectionValue oldDirection = direction;
    direction = newDirection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__DIRECTION, oldDirection, direction));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getCallbackPeriod()
  {
    return callbackPeriod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCallbackPeriod(long newCallbackPeriod)
  {
    long oldCallbackPeriod = callbackPeriod;
    callbackPeriod = newCallbackPeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BigDecimal getThreshold()
  {
    return threshold;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setThreshold(BigDecimal newThreshold)
  {
    BigDecimal oldThreshold = threshold;
    threshold = newThreshold;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__THRESHOLD, oldThreshold, threshold));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getMaxVelocity()
  {
    return maxVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMaxVelocity(Short newMaxVelocity)
  {
    Short oldMaxVelocity = maxVelocity;
    maxVelocity = newMaxVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__MAX_VELOCITY, oldMaxVelocity, maxVelocity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getMinVelocity()
  {
    return minVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMinVelocity(Short newMinVelocity)
  {
    Short oldMinVelocity = minVelocity;
    minVelocity = newMinVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__MIN_VELOCITY, oldMinVelocity, minVelocity));
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
  public short getTargetvelocity()
  {
    return targetvelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetvelocity(short newTargetvelocity)
  {
    short oldTargetvelocity = targetvelocity;
    targetvelocity = newTargetvelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICK_DC__TARGETVELOCITY, oldTargetvelocity, targetvelocity));
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
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void init()
  {
    logger = LoggerFactory.getLogger(MBrickDCImpl.class);
    poll = true; // don't use the setter to prevent notification
    setEnabledA(new AtomicBoolean());
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void setValue(BigDecimal newValue, DeviceOptions opts)
  {
    setPoint(newValue.shortValue(), opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void setValue(PercentType percentValue, DeviceOptions opts)
  {
    BigDecimal max = Tools.getBigDecimalOpt(ConfigOptsDimmable.MAX.toString(), opts);
    if (max == null) {
      logger.error("BrickDC dimmer option max is missing, items configuration has to be fixed!");
      return;
    } else {
      logger.debug("Brick DC max {}", max);
    }
    BigDecimal min = Tools.getBigDecimalOpt(ConfigOptsDimmable.MIN.toString(), opts);
    logger.debug("Brick DC min {}", min);
    if (min == null) {
      logger.error("BrickDC dimmer option min is missing, items configuration has to be fixed!");
      return;
    }
    setPercentValue(new PercentValue(percentValue.toBigDecimal()));
    BigDecimal abs = max.add(min.abs());
    Short newVelocity =
        abs.divide(new BigDecimal(100)).multiply(percentValue.toBigDecimal()).subtract(min.abs())
            .shortValue();
    setPoint(newVelocity, opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void dimm(IncreaseDecreaseType increaseDecrease, DeviceOptions opts) {
    logger.trace("dimmer increase increaseDecrease {} opts {}", increaseDecrease, opts);
    if (opts == null) {
      logger.error("Brick DC options are missing");
      return;
    }
    if (increaseDecrease == null) {
      logger.error("Brick DC increaseDecrease may not be null!");
      return;
    }
    Short step = Tools.getShortOpt(ConfigOptsDimmable.STEP.toString(), opts);
    if (step == null) {
      logger.error("BrickDC dimmer option step is missing, items configuration has to be fixed!");
      return;
    }
    Short newVelocity = null;
    if (increaseDecrease.equals(IncreaseDecreaseType.INCREASE)) {
      newVelocity = (short) (this.targetvelocity + step);
    } else if (increaseDecrease.equals(IncreaseDecreaseType.DECREASE)) {
      newVelocity = (short) (this.targetvelocity - step);
    }
    logger.debug("Brick DC newVelocity {}", newVelocity);
    setPoint(newVelocity, opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  private void setPoint(Short targetspeed, DeviceOptions opts) {
    Integer xacceleration = Tools.getIntOpt(ConfigOptsMove.ACCELERATION.toString(), opts);
    String drivemodestr = Tools.getStringOpt(ConfigOptsMove.DRIVEMODE.toString(), opts);
    Short speed = null;
    logger.trace("setPiont speed {} opts {}", targetspeed, opts);
    if (opts == null) {
      logger.error("Brick DC options are missing");
      return;
    }
    if (targetspeed == null) {
      logger.error("Brick DC setPoint targetspeed may not be null!");
      return;
    }
    Short max = Tools.getShortOpt(ConfigOptsDimmable.MAX.toString(), opts);
    if (max == null) {
      logger.error("BrickDC dimmer option max is missing, items configuration has to be fixed!");
      return;
    } else {
      logger.debug("Brick DC max {}", max);
    }
    Short min = Tools.getShortOpt(ConfigOptsDimmable.MIN.toString(), opts);
    logger.debug("Brick DC min {}", min);
    if (min == null) {
      logger.error("BrickDC dimmer option min is missing, items configuration has to be fixed!");
      return;
    }
    if (targetspeed > max) {
      if (this.velocity < targetspeed) {
        logger.debug("setting velocity to max speed {}, which is lower then target speed {}", max,
            targetspeed);
        speed = max;
      } else {
        logger.debug("max velocity already reached {}", max);
        return;
      }
    } else if (targetspeed < min) {
      if (this.velocity > targetspeed) {
        logger.debug("setting velocity to min speed {}, which is higher then target speed {}", min,
            targetspeed);
        speed = min;
      } else {
        logger.debug("min velocity already reached {}", min);
        return;
      }
    } else {
      speed = targetspeed;
    }
    setSpeed(speed, xacceleration, drivemodestr, this.pwmFrequency);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  @Override
  public void turnSwitch(OnOffValue state, DeviceOptions opts) {
    logger.trace("turnSwitch called");
    if (state == OnOffValue.OFF) {
      setSpeed((short) 0, null, null, null);
    } else if (state == OnOffValue.ON) {
      setSpeed(Tools.getShortOpt(ConfigOptsSwitchSpeed.SPEED.toString(), opts), null, null, null);
    } else {
      logger.error("{} unkown switchstate {}", LoggerConstants.TFMODELUPDATE, state);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSwitchState() {
    fetchSensorValue();
  }
  
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    try {
      handleVelocity(tinkerforgeDevice.getVelocity(), false);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  public void moveon(DeviceOptions opts) {
    if (direction != null && direction != DirectionValue.UNDEF) {
      UpDownType directiontmp =
          this.direction == DirectionValue.LEFT ? UpDownType.UP : UpDownType.DOWN;
      move(directiontmp, opts);
    } else {
      logger.warn("cannot moveon because direction is undefined");
    }
  }



  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void move(UpDownType direction, DeviceOptions opts) {
    if (opts == null) {
      logger.error("Brick DC options are missing");
      return;
    }
    if (direction == null) {
      logger.error("Brick DC direction may not be null, items configuration has to be fixed!");
      return;
    }
    Integer itemacceleration = Tools.getIntOpt(ConfigOptsMove.ACCELERATION.toString(), opts);
    String drivemodestr = Tools.getStringOpt(ConfigOptsMove.DRIVEMODE.toString(), opts);
    Short speed = null;

    if (direction.equals(UpDownType.DOWN)) {
      speed = Tools.getShortOpt(ConfigOptsMove.RIGHTSPEED.toString(), opts);
      if (speed == null) {
        logger
            .error("\"rightspeed\" option missing or empty, items configuration has to be fixed!");
        return;
      } else {
        setDirection(DirectionValue.RIGHT);
      }
    } else if (direction.equals(UpDownType.UP)) {
      speed = Tools.getShortOpt(ConfigOptsMove.LEFTSPEED.toString(), opts);
      if (speed == null) {
        logger.error("\"leftspeed\" option missing or empty, items configuration has to be fixed!");
        return;
      } else {
        setDirection(DirectionValue.LEFT);
      }
    }
    setSpeed(speed, itemacceleration, drivemodestr, this.pwmFrequency);
  }

  /**
   * 
   * @generated NOT
   */
  private Short driveModeFromString(String drivemodestr) {
    logger.debug("drivemodestr short is: {}", drivemodestr);
    Short drivemode = null;
    if (drivemodestr != null) {
      if (drivemodestr.equals(DCDriveMode.BRAKE.toString().toLowerCase())) {
        drivemode = BrickDC.DRIVE_MODE_DRIVE_BRAKE;
      } else if (drivemodestr.equals(DCDriveMode.COAST.toString().toLowerCase())) {
        drivemode = BrickDC.DRIVE_MODE_DRIVE_COAST;
      } else {
        logger.error("invalid drivemode {}", drivemodestr);
      }
    } else {
      // use defaults
      if (driveMode == DCDriveMode.BRAKE) {
        drivemode = BrickDC.DRIVE_MODE_DRIVE_BRAKE;
      } else if (driveMode == DCDriveMode.COAST) {
        drivemode = BrickDC.DRIVE_MODE_DRIVE_COAST;
      }

    }
    logger.debug("drivemode short is: {}", drivemode);
    return drivemode;
  }

  /**
   * 
   * @generated NOT
   */
  private boolean setSpeed(Short speed, Integer acceleration, String drivemode, Integer pwm) {
    // use defaults if acceleration, drivemode or pwm are null
    Short xdrivemode = driveModeFromString(drivemode);
    Integer xacceleration = acceleration != null ? acceleration : this.acceleration;
    Integer xpwm = pwm != null ? pwm : this.pwmFrequency;
    short xspeed = 0;
    if (speed == null) {
      logger.warn("Brick DC got speed null, setting speed to 0");
    } else {
      xspeed = speed;
    }
    try {
      if (xdrivemode != null) {
        // let Brick DC choose the drive mode
        tinkerforgeDevice.setDriveMode(xdrivemode);
      }
      if (xacceleration != null) {
        tinkerforgeDevice.setAcceleration(xacceleration);
      }
      if (xpwm != null) {
        tinkerforgeDevice.setPWMFrequency(xpwm);
      }
      setTargetvelocity(xspeed);
      tinkerforgeDevice.setVelocity(xspeed);
      return true;
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
    return false;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public boolean setSpeed(Short velocity, int acceleration, String drivemode) {
    return setSpeed(velocity, acceleration, drivemode, null);
  }


  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void stop() {
    try {
      tinkerforgeDevice.setVelocity((short) 0);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  public void enable() {
    tinkerforgeDevice = new BrickDC(uid, ipConnection);
    if (tfConfig != null) {
      logger.debug("found tfConfig");
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("acceleration"))) {
        setAcceleration(tfConfig.getAcceleration());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("pwmFrequency"))) {
        setPwmFrequency(tfConfig.getPwmFrequency());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("driveMode"))) {
        setDriveMode(DCDriveMode.get(tfConfig.getDriveMode()));
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("threshold"))) {
        setThreshold(tfConfig.getThreshold());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("callbackPeriod"))) {
        setCallbackPeriod(tfConfig.getCallbackPeriod());
      }
    }
    try {
      tinkerforgeDevice.setPWMFrequency(pwmFrequency);
      if (driveMode == DCDriveMode.BRAKE) {
        tinkerforgeDevice.setDriveMode(BrickDC.DRIVE_MODE_DRIVE_BRAKE);
      } else if (driveMode == DCDriveMode.COAST) {
        tinkerforgeDevice.setDriveMode(BrickDC.DRIVE_MODE_DRIVE_COAST);
      }
      tinkerforgeDevice.setCurrentVelocityPeriod((int) callbackPeriod);
      listener = new VelocityListener();
      tinkerforgeDevice.addCurrentVelocityListener(listener);
      tinkerforgeDevice.enable();
      handleVelocity(tinkerforgeDevice.getVelocity(), false);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  private class VelocityListener implements BrickDC.CurrentVelocityListener {

    @Override
    public void currentVelocity(short velocity) {
      handleVelocity(velocity);
    }

  }

  /**
   * 
   * @generated NOT
   */
  private void handleVelocity(short velocity, boolean usethreshold) {
    DecimalValue newValue = Tools.calculate(velocity);
    logger.trace("{} got new value {}", LoggerConstants.TFMODELUPDATE, newValue);
    if (usethreshold) {
      if (newValue.compareTo(getSensorValue(), getThreshold()) != 0) {
        logger.trace("{} setting new value {}", LoggerConstants.TFMODELUPDATE, newValue);
        setSensorValue(newValue);
      } else {
        logger.trace("{} omitting new value {}", LoggerConstants.TFMODELUPDATE, newValue);
      }
    } else {
      logger.trace("{} setting new value {}", LoggerConstants.TFMODELUPDATE, newValue);
      setSensorValue(newValue);
    }
    OnOffValue newSwitchState = newValue.onOffValue(0);
    logger.trace("new switchstate {} new value {}", newSwitchState, newValue);
    if (newSwitchState != switchState) {
      setSwitchState(newSwitchState);
    }
  }

  /**
   * 
   * @generated NOT
   */
  private void handleVelocity(short velocity) {
    handleVelocity(velocity, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disable() {
    if (listener != null) {
      tinkerforgeDevice.removeCurrentVelocityListener(listener);
    }

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
      case ModelPackage.MBRICK_DC__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MBRICK_DC__LOGGER:
        return getLogger();
      case ModelPackage.MBRICK_DC__UID:
        return getUid();
      case ModelPackage.MBRICK_DC__POLL:
        return isPoll();
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
      case ModelPackage.MBRICK_DC__DIRECTION:
        return getDirection();
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MBRICK_DC__MIN_VALUE:
        return getMinValue();
      case ModelPackage.MBRICK_DC__MAX_VALUE:
        return getMaxValue();
      case ModelPackage.MBRICK_DC__PERCENT_VALUE:
        return getPercentValue();
      case ModelPackage.MBRICK_DC__CALLBACK_PERIOD:
        return getCallbackPeriod();
      case ModelPackage.MBRICK_DC__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICK_DC__THRESHOLD:
        return getThreshold();
      case ModelPackage.MBRICK_DC__MAX_VELOCITY:
        return getMaxVelocity();
      case ModelPackage.MBRICK_DC__MIN_VELOCITY:
        return getMinVelocity();
      case ModelPackage.MBRICK_DC__VELOCITY:
        return getVelocity();
      case ModelPackage.MBRICK_DC__TARGETVELOCITY:
        return getTargetvelocity();
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        return getCurrentVelocity();
      case ModelPackage.MBRICK_DC__ACCELERATION:
        return getAcceleration();
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        return getPwmFrequency();
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        return getDriveMode();
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
      case ModelPackage.MBRICK_DC__SENSOR_VALUE:
        setSensorValue((DecimalValue)newValue);
        return;
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MBRICK_DC__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICK_DC__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICK_DC__POLL:
        setPoll((Boolean)newValue);
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
      case ModelPackage.MBRICK_DC__DIRECTION:
        setDirection((DirectionValue)newValue);
        return;
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        setTfConfig((TFBrickDCConfiguration)newValue);
        return;
      case ModelPackage.MBRICK_DC__MIN_VALUE:
        setMinValue((BigDecimal)newValue);
        return;
      case ModelPackage.MBRICK_DC__MAX_VALUE:
        setMaxValue((BigDecimal)newValue);
        return;
      case ModelPackage.MBRICK_DC__PERCENT_VALUE:
        setPercentValue((PercentValue)newValue);
        return;
      case ModelPackage.MBRICK_DC__CALLBACK_PERIOD:
        setCallbackPeriod((Long)newValue);
        return;
      case ModelPackage.MBRICK_DC__THRESHOLD:
        setThreshold((BigDecimal)newValue);
        return;
      case ModelPackage.MBRICK_DC__MAX_VELOCITY:
        setMaxVelocity((Short)newValue);
        return;
      case ModelPackage.MBRICK_DC__MIN_VELOCITY:
        setMinVelocity((Short)newValue);
        return;
      case ModelPackage.MBRICK_DC__VELOCITY:
        setVelocity((Short)newValue);
        return;
      case ModelPackage.MBRICK_DC__TARGETVELOCITY:
        setTargetvelocity((Short)newValue);
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
      case ModelPackage.MBRICK_DC__SENSOR_VALUE:
        setSensorValue((DecimalValue)null);
        return;
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__POLL:
        setPoll(POLL_EDEFAULT);
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
      case ModelPackage.MBRICK_DC__DIRECTION:
        setDirection(DIRECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        setTfConfig((TFBrickDCConfiguration)null);
        return;
      case ModelPackage.MBRICK_DC__MIN_VALUE:
        setMinValue(MIN_VALUE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__MAX_VALUE:
        setMaxValue(MAX_VALUE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__PERCENT_VALUE:
        setPercentValue(PERCENT_VALUE_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__CALLBACK_PERIOD:
        setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__THRESHOLD:
        setThreshold(THRESHOLD_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__MAX_VELOCITY:
        setMaxVelocity(MAX_VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__MIN_VELOCITY:
        setMinVelocity(MIN_VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MBRICK_DC__TARGETVELOCITY:
        setTargetvelocity(TARGETVELOCITY_EDEFAULT);
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
      case ModelPackage.MBRICK_DC__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.MBRICK_DC__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MBRICK_DC__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICK_DC__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICK_DC__POLL:
        return poll != POLL_EDEFAULT;
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
      case ModelPackage.MBRICK_DC__DIRECTION:
        return DIRECTION_EDEFAULT == null ? direction != null : !DIRECTION_EDEFAULT.equals(direction);
      case ModelPackage.MBRICK_DC__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MBRICK_DC__MIN_VALUE:
        return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
      case ModelPackage.MBRICK_DC__MAX_VALUE:
        return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
      case ModelPackage.MBRICK_DC__PERCENT_VALUE:
        return PERCENT_VALUE_EDEFAULT == null ? percentValue != null : !PERCENT_VALUE_EDEFAULT.equals(percentValue);
      case ModelPackage.MBRICK_DC__CALLBACK_PERIOD:
        return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
      case ModelPackage.MBRICK_DC__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICK_DC__THRESHOLD:
        return THRESHOLD_EDEFAULT == null ? threshold != null : !THRESHOLD_EDEFAULT.equals(threshold);
      case ModelPackage.MBRICK_DC__MAX_VELOCITY:
        return MAX_VELOCITY_EDEFAULT == null ? maxVelocity != null : !MAX_VELOCITY_EDEFAULT.equals(maxVelocity);
      case ModelPackage.MBRICK_DC__MIN_VELOCITY:
        return MIN_VELOCITY_EDEFAULT == null ? minVelocity != null : !MIN_VELOCITY_EDEFAULT.equals(minVelocity);
      case ModelPackage.MBRICK_DC__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.MBRICK_DC__TARGETVELOCITY:
        return targetvelocity != TARGETVELOCITY_EDEFAULT;
      case ModelPackage.MBRICK_DC__CURRENT_VELOCITY:
        return currentVelocity != CURRENT_VELOCITY_EDEFAULT;
      case ModelPackage.MBRICK_DC__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.MBRICK_DC__PWM_FREQUENCY:
        return pwmFrequency != PWM_FREQUENCY_EDEFAULT;
      case ModelPackage.MBRICK_DC__DRIVE_MODE:
        return driveMode != DRIVE_MODE_EDEFAULT;
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
    if (baseClass == SwitchSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__SWITCH_STATE: return ModelPackage.SWITCH_SENSOR__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MBaseDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MBRICK_DC__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.MBRICK_DC__POLL: return ModelPackage.MBASE_DEVICE__POLL;
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
    if (baseClass == MoveActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__DIRECTION: return ModelPackage.MOVE_ACTOR__DIRECTION;
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
    if (baseClass == DimmableActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__MIN_VALUE: return ModelPackage.DIMMABLE_ACTOR__MIN_VALUE;
        case ModelPackage.MBRICK_DC__MAX_VALUE: return ModelPackage.DIMMABLE_ACTOR__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__PERCENT_VALUE: return ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE;
        default: return -1;
      }
    }
    if (baseClass == SetPointActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICK_DC__CALLBACK_PERIOD: return ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD;
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
    if (baseClass == SwitchSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.SWITCH_SENSOR__SWITCH_STATE: return ModelPackage.MBRICK_DC__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MBaseDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MBRICK_DC__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MBRICK_DC__UID;
        case ModelPackage.MBASE_DEVICE__POLL: return ModelPackage.MBRICK_DC__POLL;
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
    if (baseClass == MoveActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MOVE_ACTOR__DIRECTION: return ModelPackage.MBRICK_DC__DIRECTION;
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
    if (baseClass == DimmableActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.DIMMABLE_ACTOR__MIN_VALUE: return ModelPackage.MBRICK_DC__MIN_VALUE;
        case ModelPackage.DIMMABLE_ACTOR__MAX_VALUE: return ModelPackage.MBRICK_DC__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE: return ModelPackage.MBRICK_DC__PERCENT_VALUE;
        default: return -1;
      }
    }
    if (baseClass == SetPointActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD: return ModelPackage.MBRICK_DC__CALLBACK_PERIOD;
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
    if (baseClass == SwitchSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SWITCH_SENSOR___FETCH_SWITCH_STATE: return ModelPackage.MBRICK_DC___FETCH_SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PROGRAMMABLE_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS: return ModelPackage.MBRICK_DC___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS;
        default: return -1;
      }
    }
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
    if (baseClass == MoveActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MOVE_ACTOR___MOVE__UPDOWNTYPE_DEVICEOPTIONS: return ModelPackage.MBRICK_DC___MOVE__UPDOWNTYPE_DEVICEOPTIONS;
        case ModelPackage.MOVE_ACTOR___STOP: return ModelPackage.MBRICK_DC___STOP;
        case ModelPackage.MOVE_ACTOR___MOVEON__DEVICEOPTIONS: return ModelPackage.MBRICK_DC___MOVEON__DEVICEOPTIONS;
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
    if (baseClass == DimmableActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.DIMMABLE_ACTOR___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS: return ModelPackage.MBRICK_DC___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS: return ModelPackage.MBRICK_DC___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == SetPointActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SET_POINT_ACTOR___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS: return ModelPackage.MBRICK_DC___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
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
      case ModelPackage.MBRICK_DC___SET_SPEED__SHORT_INT_STRING:
        return setSpeed((Short)arguments.get(0), (Integer)arguments.get(1), (String)arguments.get(2));
      case ModelPackage.MBRICK_DC___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS:
        setValue((BigDecimal)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICK_DC___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS:
        setValue((PercentType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICK_DC___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS:
        dimm((IncreaseDecreaseType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICK_DC___MOVE__UPDOWNTYPE_DEVICEOPTIONS:
        move((UpDownType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICK_DC___STOP:
        stop();
        return null;
      case ModelPackage.MBRICK_DC___MOVEON__DEVICEOPTIONS:
        moveon((DeviceOptions)arguments.get(0));
        return null;
      case ModelPackage.MBRICK_DC___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICK_DC___DISABLE:
        disable();
        return null;
      case ModelPackage.MBRICK_DC___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS:
        turnSwitch((OnOffValue)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MBRICK_DC___FETCH_SWITCH_STATE:
        fetchSwitchState();
        return null;
      case ModelPackage.MBRICK_DC___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
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
    result.append(" (sensorValue: ");
    result.append(sensorValue);
    result.append(", switchState: ");
    result.append(switchState);
    result.append(", logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", poll: ");
    result.append(poll);
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
    result.append(", direction: ");
    result.append(direction);
    result.append(", minValue: ");
    result.append(minValue);
    result.append(", maxValue: ");
    result.append(maxValue);
    result.append(", percentValue: ");
    result.append(percentValue);
    result.append(", callbackPeriod: ");
    result.append(callbackPeriod);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", threshold: ");
    result.append(threshold);
    result.append(", maxVelocity: ");
    result.append(maxVelocity);
    result.append(", minVelocity: ");
    result.append(minVelocity);
    result.append(", velocity: ");
    result.append(velocity);
    result.append(", targetvelocity: ");
    result.append(targetvelocity);
    result.append(", currentVelocity: ");
    result.append(currentVelocity);
    result.append(", acceleration: ");
    result.append(acceleration);
    result.append(", pwmFrequency: ");
    result.append(pwmFrequency);
    result.append(", driveMode: ");
    result.append(driveMode);
    result.append(')');
    return result.toString();
  }

} //MBrickDCImpl
