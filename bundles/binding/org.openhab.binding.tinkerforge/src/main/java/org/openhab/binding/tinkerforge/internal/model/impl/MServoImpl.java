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
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsDimmable;
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsServo;
import org.openhab.binding.tinkerforge.internal.model.DimmableActor;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MoveActor;
import org.openhab.binding.tinkerforge.internal.model.PercentTypeActor;
import org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.SetPointActor;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
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

import com.tinkerforge.BrickServo;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MServo</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMaxValue <em>Max Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPercentValue <em>Percent Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMaxPosition <em>Max Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMinPosition <em>Min Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getOutputVoltage <em>Output Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getTargetPosition <em>Target Position</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MServoImpl extends MinimalEObjectImpl.Container implements MServo
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
   * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
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
   * The default value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubId()
   * @generated
   * @ordered
   */
  protected static final String SUB_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubId()
   * @generated
   * @ordered
   */
  protected String subId = SUB_ID_EDEFAULT;

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
  protected TFServoConfiguration tfConfig;

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
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "servo";

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
  protected static final int VELOCITY_EDEFAULT = 65535;

  /**
   * The cached value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected int velocity = VELOCITY_EDEFAULT;

  /**
   * The default value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceleration()
   * @generated
   * @ordered
   */
  protected static final int ACCELERATION_EDEFAULT = 65535;

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
   * The default value of the '{@link #getMaxPosition() <em>Max Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxPosition()
   * @generated
   * @ordered
   */
  protected static final Short MAX_POSITION_EDEFAULT = new Short((short)9000);

  /**
   * The cached value of the '{@link #getMaxPosition() <em>Max Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxPosition()
   * @generated
   * @ordered
   */
  protected Short maxPosition = MAX_POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getMinPosition() <em>Min Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinPosition()
   * @generated
   * @ordered
   */
  protected static final Short MIN_POSITION_EDEFAULT = new Short((short)-9000);

  /**
   * The cached value of the '{@link #getMinPosition() <em>Min Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMinPosition()
   * @generated
   * @ordered
   */
  protected Short minPosition = MIN_POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getPulseWidthMin() <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMin()
   * @generated
   * @ordered
   */
  protected static final int PULSE_WIDTH_MIN_EDEFAULT = 1000;

  /**
   * The cached value of the '{@link #getPulseWidthMin() <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMin()
   * @generated
   * @ordered
   */
  protected int pulseWidthMin = PULSE_WIDTH_MIN_EDEFAULT;

  /**
   * The default value of the '{@link #getPulseWidthMax() <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMax()
   * @generated
   * @ordered
   */
  protected static final int PULSE_WIDTH_MAX_EDEFAULT = 2000;

  /**
   * The cached value of the '{@link #getPulseWidthMax() <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMax()
   * @generated
   * @ordered
   */
  protected int pulseWidthMax = PULSE_WIDTH_MAX_EDEFAULT;

  /**
   * The default value of the '{@link #getPeriod() <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPeriod()
   * @generated
   * @ordered
   */
  protected static final int PERIOD_EDEFAULT = 19500;

  /**
   * The cached value of the '{@link #getPeriod() <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPeriod()
   * @generated
   * @ordered
   */
  protected int period = PERIOD_EDEFAULT;

  /**
   * The default value of the '{@link #getOutputVoltage() <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputVoltage()
   * @generated
   * @ordered
   */
  protected static final int OUTPUT_VOLTAGE_EDEFAULT = 5000;

  /**
   * The cached value of the '{@link #getOutputVoltage() <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputVoltage()
   * @generated
   * @ordered
   */
  protected int outputVoltage = OUTPUT_VOLTAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetPosition() <em>Target Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetPosition()
   * @generated
   * @ordered
   */
  protected static final short TARGET_POSITION_EDEFAULT = 0;
  /**
   * The cached value of the '{@link #getTargetPosition() <em>Target Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetPosition()
   * @generated
   * @ordered
   */
  protected short targetPosition = TARGET_POSITION_EDEFAULT;
  private Short servoNum;
  private PositionReachedListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MServoImpl()
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
    return ModelPackage.Literals.MSERVO;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__SENSOR_VALUE, oldSensorValue, sensorValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__SWITCH_STATE, oldSwitchState, switchState));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getVelocity()
  {
    return velocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocity(int newVelocity)
  {
    int oldVelocity = velocity;
    velocity = newVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__VELOCITY, oldVelocity, velocity));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__ACCELERATION, oldAcceleration, acceleration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getMaxPosition()
  {
    return maxPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMaxPosition(Short newMaxPosition)
  {
    Short oldMaxPosition = maxPosition;
    maxPosition = newMaxPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__MAX_POSITION, oldMaxPosition, maxPosition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getMinPosition()
  {
    return minPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMinPosition(Short newMinPosition)
  {
    Short oldMinPosition = minPosition;
    minPosition = newMinPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__MIN_POSITION, oldMinPosition, minPosition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPulseWidthMin()
  {
    return pulseWidthMin;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPulseWidthMin(int newPulseWidthMin)
  {
    int oldPulseWidthMin = pulseWidthMin;
    pulseWidthMin = newPulseWidthMin;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__PULSE_WIDTH_MIN, oldPulseWidthMin, pulseWidthMin));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPulseWidthMax()
  {
    return pulseWidthMax;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPulseWidthMax(int newPulseWidthMax)
  {
    int oldPulseWidthMax = pulseWidthMax;
    pulseWidthMax = newPulseWidthMax;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__PULSE_WIDTH_MAX, oldPulseWidthMax, pulseWidthMax));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPeriod()
  {
    return period;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPeriod(int newPeriod)
  {
    int oldPeriod = period;
    period = newPeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__PERIOD, oldPeriod, period));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getOutputVoltage()
  {
    return outputVoltage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOutputVoltage(int newOutputVoltage)
  {
    int oldOutputVoltage = outputVoltage;
    outputVoltage = newOutputVoltage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__OUTPUT_VOLTAGE, oldOutputVoltage, outputVoltage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getTargetPosition()
  {
    return targetPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetPosition(short newTargetPosition)
  {
    short oldTargetPosition = targetPosition;
    targetPosition = newTargetPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__TARGET_POSITION, oldTargetPosition, targetPosition));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void init() {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MServoImpl.class);
    logger.debug("init called on MServo: " + uid);
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
  public void setValue(PercentType newPercentValue, DeviceOptions opts)
  {
    BigDecimal max = Tools.getBigDecimalOpt(ConfigOptsDimmable.MAX.toString(), opts);
    if (max == null) {
      logger.error("dimmer option max is missing, items configuration has to be fixed!");
      return;
    } else {
      logger.debug("max {}", max);
    }
    BigDecimal min = Tools.getBigDecimalOpt(ConfigOptsDimmable.MIN.toString(), opts);
    logger.debug("min {}", min);
    if (min == null) {
      logger.error("dimmer option min is missing, items configuration has to be fixed!");
      return;
    }
    setPercentValue(new PercentValue(newPercentValue.toBigDecimal()));
    BigDecimal abs = max.add(min.abs());
    Short newVelocity =
        abs.divide(new BigDecimal(100)).multiply(newPercentValue.toBigDecimal())
            .subtract(min.abs())
            .shortValue();
    setPoint(newVelocity, opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void dimm(IncreaseDecreaseType increaseDecrease, DeviceOptions opts)
  {
    logger.trace("dimmer increase increaseDecrease {} opts {}", increaseDecrease, opts);
    if (opts == null) {
      logger.error("options are missing");
      return;
    }
    if (increaseDecrease == null) {
      logger.error("increaseDecrease may not be null!");
      return;
    }
    Short step = Tools.getShortOpt(ConfigOptsDimmable.STEP.toString(), opts);
    if (step == null) {
      logger.error("dimmer option step is missing, items configuration has to be fixed!");
      return;
    }
    Short newPosition = null;
    if (increaseDecrease.equals(IncreaseDecreaseType.INCREASE)) {
      newPosition = (short) (this.targetPosition + step);
    } else if (increaseDecrease.equals(IncreaseDecreaseType.DECREASE)) {
      newPosition = (short) (this.targetPosition - step);
    }
    logger.debug("new position {}", newPosition);
    setPoint(newPosition, opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void move(UpDownType direction, DeviceOptions opts)
  {
    if (opts == null) {
      logger.error("options are missing");
      return;
    }
    if (direction == null) {
      logger.error("direction may not be null, items configuration has to be fixed!");
      return;
    }
    Short xposition = null;

    if (direction.equals(UpDownType.DOWN)) {
      xposition = Tools.getShortOpt(ConfigOptsServo.RIGHTPOSITION.toString(), opts);
      if (xposition == null) {
        logger
.error("\"{}\" option missing or empty, items configuration has to be fixed!",
            ConfigOptsServo.RIGHTPOSITION.toString());
        return;
      } else {
        setDirection(DirectionValue.RIGHT);
      }
    } else if (direction.equals(UpDownType.UP)) {
      xposition = Tools.getShortOpt(ConfigOptsServo.LEFTPOSITION.toString(), opts);
      if (xposition == null) {
        logger.error("\"{}\" option missing or empty, items configuration has to be fixed!",
            ConfigOptsServo.LEFTPOSITION.toString());
        return;
      } else {
        setDirection(DirectionValue.LEFT);
      }
    }
    setPoint(xposition, opts);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void stop()
  {
    try {
      // stop by setting current position value with setPosition on the tf servo device
      getMbrick().getTinkerforgeDevice().setPosition(servoNum, getSensorValue().shortValue());
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
  public void moveon(DeviceOptions opts)
  {
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
  private void setPoint(Short newPosition, DeviceOptions opts) {
    Integer xacceleration =
        Tools.getIntOpt(ConfigOptsServo.ACCELERATION.toString(), opts, this.acceleration);
    if (xacceleration == null) {
      logger.error("acceleration may not be null");
      return;
    }
    Integer xvelocity = Tools.getIntOpt(ConfigOptsServo.VELOCITY.toString(), opts, this.velocity);
    if (xvelocity == null) {
      logger.error("xvelocity may not be null");
      return;
    }
    if (newPosition == null) {
      logger.error("position may not be null");
      return;
    }
    Short max = Tools.getShortOpt(ConfigOptsDimmable.MAX.toString(), opts, getMaxPosition());
    if (max == null) {
      logger.error("option max is missing, items configuration has to be fixed!");
      return;
    } else {
      logger.debug("max {}", max);
    }
    Short min = Tools.getShortOpt(ConfigOptsDimmable.MIN.toString(), opts, getMinPosition());
    logger.debug("min {}", min);
    if (min == null) {
      logger.error("dimmer option min is missing, items configuration has to be fixed!");
      return;
    }
    if (newPosition > max) {
      if (this.targetPosition < newPosition) {
        logger.debug("setting value to max speed {}, which is lower then target speed {}", max,
            newPosition);
        newPosition = max;
      } else {
        logger.debug("max value already reached {}", max);
        return;
      }
    } else if (newPosition < min) {
      if (this.targetPosition > newPosition) {
        logger.debug("setting velocity to min speed {}, which is higher then target speed {}", min,
            newPosition);
        newPosition = min;
      } else {
        logger.debug("min value already reached {}", min);
        return;
      }
    }
    setPoint(newPosition, xvelocity, xacceleration);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public boolean setPoint(Short newPosition, int newVelocity, int newAccelleration) {
    Short max = getMaxPosition();
    Short min = getMinPosition();
    if (newPosition > max) {
      if (this.targetPosition < newPosition) {
        logger.debug("setting value to max speed {}, which is lower then target speed {}", max,
            newPosition);
        newPosition = max;
      } else {
        logger.debug("max value already reached {}", max);
        return true;
      }
    } else if (newPosition < min) {
      if (this.targetPosition > newPosition) {
        logger.debug("setting velocity to min speed {}, which is higher then target speed {}", min,
            newPosition);
        newPosition = min;
      } else {
        logger.debug("min value already reached {}", min);
        return true;
      }
    }
    try {
      BrickServo tinkerBrickServo = getMbrick().getTinkerforgeDevice();
      tinkerBrickServo.setVelocity(servoNum, newVelocity);
      tinkerBrickServo.setAcceleration(servoNum, newAccelleration);
      tinkerBrickServo.setPosition(servoNum, newPosition);
      setTargetPosition(newPosition);
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
  @Override
  public void enable() {
    MBrickServo brick = getMbrick();
    if (brick == null) {
      logger.error("No servo brick configured for servo: " + uid);
    } else {
      if (tfConfig != null) {
        logger.debug("found tfConfig");
        if (tfConfig.getVelocity() != 0) setVelocity(tfConfig.getVelocity()); // TODO check for
                                                                              // unset
        // state
        if (tfConfig.getAcceleration() != 0) setAcceleration(tfConfig.getAcceleration());
        if (tfConfig.getPeriod() != 0) setPeriod(tfConfig.getPeriod());
        if (tfConfig.getPulseWidthMax() != 0 && tfConfig.getPulseWidthMin() != 0) {
          setPulseWidthMax(tfConfig.getPulseWidthMax());
          setPulseWidthMin(tfConfig.getPulseWidthMin());
        }
        if (tfConfig.getOutputVoltage() != 0) setOutputVoltage(tfConfig.getOutputVoltage());
      }
      BrickServo tinkerBrickServo = brick.getTinkerforgeDevice();
      try {
        servoNum = Short.parseShort(String.valueOf(subId.charAt(subId.length() - 1)));
        // tinkerBrickServo.setVelocity(servoNum, velocity);
        // tinkerBrickServo.setAcceleration(servoNum, acceleration);
        tinkerBrickServo.setPulseWidth(servoNum, pulseWidthMin, pulseWidthMax);
        tinkerBrickServo.setPeriod(servoNum, period);
        tinkerBrickServo.setOutputVoltage(outputVoltage);
        setTargetPosition(tinkerBrickServo.getPosition(servoNum)); // initialize target postion with
                                                                   // current position value
        listener = new PositionReachedListener();
        tinkerBrickServo.addPositionReachedListener(listener);
        tinkerBrickServo.enablePositionReachedCallback();
        tinkerBrickServo.enable(servoNum);
        fetchSwitchState();
      } catch (NumberFormatException e) {
        TinkerforgeErrorHandler.handleError(this, "can not determine servoNum", e);
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(this,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  @Override
  public void disable() {
    if (listener != null) {
      getMbrick().getTinkerforgeDevice().removePositionReachedListener(listener);
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
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    try {
      short position = getMbrick().getTinkerforgeDevice().getPosition(servoNum);
      DecimalValue newValue = Tools.calculate(position);
      setSensorValue(newValue);

      OnOffValue newSwitchState = newValue.onOffValue(0);
      logger.trace("new switchstate {} new value {}", newSwitchState, newValue);
      setSwitchState(newSwitchState);

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
  private class PositionReachedListener implements BrickServo.PositionReachedListener {

    @Override
    public void positionReached(short servoNumPar, short newposition) {
      DecimalValue newValue = Tools.calculate(newposition);
      logger.trace("positionreachedlistener called servonum {} servonumpar {}, newpostion {}",
          servoNum, servoNumPar,
          newposition);
      if (servoNumPar == servoNum) {
        logger.trace("setting new value {}", newValue);
        setSensorValue(newValue);
      }
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  @Override
  public void turnSwitch(OnOffValue state, DeviceOptions opts) {
    logger.debug("setSwitchState called");
    Short position = null;
    if (state == OnOffValue.OFF) {
      position = 0;
    } else if (state == OnOffValue.ON) {
      position = Tools.getShortOpt(ConfigOptsServo.POSITION.toString(), opts);
      if (position == null) {
        logger.error("{} option is missing", ConfigOptsServo.POSITION.toString().toLowerCase());
        return;
      }
    } else {
      logger.error("{} unkown switchstate {}", LoggerConstants.TFMODELUPDATE, state);
    }
    setPoint(position, opts);
  }

/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubId()
  {
    return subId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubId(String newSubId)
  {
    String oldSubId = subId;
    subId = newSubId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__SUB_ID, oldSubId, subId));
  }

/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickServo getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.MSERVO__MBRICK) return null;
    return (MBrickServo)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickServo newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.MSERVO__MBRICK, msgs);
    return msgs;
  }

/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickServo newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MSERVO__MBRICK && newMbrick != null))
    {
      if (EcoreUtil.isAncestor(this, newMbrick))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newMbrick != null)
        msgs = ((InternalEObject)newMbrick).eInverseAdd(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES, MSubDeviceHolder.class, msgs);
      msgs = basicSetMbrick(newMbrick, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__MBRICK, newMbrick, newMbrick));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__DIRECTION, oldDirection, direction));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFServoConfiguration getTfConfig()
  {
    return tfConfig;
  }

/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(TFServoConfiguration newTfConfig, NotificationChain msgs)
  {
    TFServoConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(TFServoConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MSERVO__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MSERVO__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__MIN_VALUE, oldMinValue, minValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__MAX_VALUE, oldMaxValue, maxValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__PERCENT_VALUE, oldPercentValue, percentValue));
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
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.MSERVO__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickServo)otherEnd, msgs);
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
      case ModelPackage.MSERVO__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.MSERVO__TF_CONFIG:
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
      case ModelPackage.MSERVO__MBRICK:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES, MSubDeviceHolder.class, msgs);
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
      case ModelPackage.MSERVO__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.MSERVO__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MSERVO__LOGGER:
        return getLogger();
      case ModelPackage.MSERVO__UID:
        return getUid();
      case ModelPackage.MSERVO__POLL:
        return isPoll();
      case ModelPackage.MSERVO__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MSERVO__SUB_ID:
        return getSubId();
      case ModelPackage.MSERVO__MBRICK:
        return getMbrick();
      case ModelPackage.MSERVO__DIRECTION:
        return getDirection();
      case ModelPackage.MSERVO__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MSERVO__MIN_VALUE:
        return getMinValue();
      case ModelPackage.MSERVO__MAX_VALUE:
        return getMaxValue();
      case ModelPackage.MSERVO__PERCENT_VALUE:
        return getPercentValue();
      case ModelPackage.MSERVO__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MSERVO__VELOCITY:
        return getVelocity();
      case ModelPackage.MSERVO__ACCELERATION:
        return getAcceleration();
      case ModelPackage.MSERVO__MAX_POSITION:
        return getMaxPosition();
      case ModelPackage.MSERVO__MIN_POSITION:
        return getMinPosition();
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        return getPulseWidthMin();
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        return getPulseWidthMax();
      case ModelPackage.MSERVO__PERIOD:
        return getPeriod();
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        return getOutputVoltage();
      case ModelPackage.MSERVO__TARGET_POSITION:
        return getTargetPosition();
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
      case ModelPackage.MSERVO__SENSOR_VALUE:
        setSensorValue((DecimalValue)newValue);
        return;
      case ModelPackage.MSERVO__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MSERVO__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MSERVO__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MSERVO__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.MSERVO__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MSERVO__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.MSERVO__MBRICK:
        setMbrick((MBrickServo)newValue);
        return;
      case ModelPackage.MSERVO__DIRECTION:
        setDirection((DirectionValue)newValue);
        return;
      case ModelPackage.MSERVO__TF_CONFIG:
        setTfConfig((TFServoConfiguration)newValue);
        return;
      case ModelPackage.MSERVO__MIN_VALUE:
        setMinValue((BigDecimal)newValue);
        return;
      case ModelPackage.MSERVO__MAX_VALUE:
        setMaxValue((BigDecimal)newValue);
        return;
      case ModelPackage.MSERVO__PERCENT_VALUE:
        setPercentValue((PercentValue)newValue);
        return;
      case ModelPackage.MSERVO__VELOCITY:
        setVelocity((Integer)newValue);
        return;
      case ModelPackage.MSERVO__ACCELERATION:
        setAcceleration((Integer)newValue);
        return;
      case ModelPackage.MSERVO__MAX_POSITION:
        setMaxPosition((Short)newValue);
        return;
      case ModelPackage.MSERVO__MIN_POSITION:
        setMinPosition((Short)newValue);
        return;
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        setPulseWidthMin((Integer)newValue);
        return;
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        setPulseWidthMax((Integer)newValue);
        return;
      case ModelPackage.MSERVO__PERIOD:
        setPeriod((Integer)newValue);
        return;
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        setOutputVoltage((Integer)newValue);
        return;
      case ModelPackage.MSERVO__TARGET_POSITION:
        setTargetPosition((Short)newValue);
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
      case ModelPackage.MSERVO__SENSOR_VALUE:
        setSensorValue((DecimalValue)null);
        return;
      case ModelPackage.MSERVO__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MSERVO__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MSERVO__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.MSERVO__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MSERVO__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.MSERVO__MBRICK:
        setMbrick((MBrickServo)null);
        return;
      case ModelPackage.MSERVO__DIRECTION:
        setDirection(DIRECTION_EDEFAULT);
        return;
      case ModelPackage.MSERVO__TF_CONFIG:
        setTfConfig((TFServoConfiguration)null);
        return;
      case ModelPackage.MSERVO__MIN_VALUE:
        setMinValue(MIN_VALUE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__MAX_VALUE:
        setMaxValue(MAX_VALUE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__PERCENT_VALUE:
        setPercentValue(PERCENT_VALUE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MSERVO__ACCELERATION:
        setAcceleration(ACCELERATION_EDEFAULT);
        return;
      case ModelPackage.MSERVO__MAX_POSITION:
        setMaxPosition(MAX_POSITION_EDEFAULT);
        return;
      case ModelPackage.MSERVO__MIN_POSITION:
        setMinPosition(MIN_POSITION_EDEFAULT);
        return;
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        setPulseWidthMin(PULSE_WIDTH_MIN_EDEFAULT);
        return;
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        setPulseWidthMax(PULSE_WIDTH_MAX_EDEFAULT);
        return;
      case ModelPackage.MSERVO__PERIOD:
        setPeriod(PERIOD_EDEFAULT);
        return;
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        setOutputVoltage(OUTPUT_VOLTAGE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__TARGET_POSITION:
        setTargetPosition(TARGET_POSITION_EDEFAULT);
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
      case ModelPackage.MSERVO__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.MSERVO__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MSERVO__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MSERVO__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MSERVO__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.MSERVO__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MSERVO__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MSERVO__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MSERVO__DIRECTION:
        return DIRECTION_EDEFAULT == null ? direction != null : !DIRECTION_EDEFAULT.equals(direction);
      case ModelPackage.MSERVO__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MSERVO__MIN_VALUE:
        return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
      case ModelPackage.MSERVO__MAX_VALUE:
        return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
      case ModelPackage.MSERVO__PERCENT_VALUE:
        return PERCENT_VALUE_EDEFAULT == null ? percentValue != null : !PERCENT_VALUE_EDEFAULT.equals(percentValue);
      case ModelPackage.MSERVO__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MSERVO__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.MSERVO__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.MSERVO__MAX_POSITION:
        return MAX_POSITION_EDEFAULT == null ? maxPosition != null : !MAX_POSITION_EDEFAULT.equals(maxPosition);
      case ModelPackage.MSERVO__MIN_POSITION:
        return MIN_POSITION_EDEFAULT == null ? minPosition != null : !MIN_POSITION_EDEFAULT.equals(minPosition);
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        return pulseWidthMin != PULSE_WIDTH_MIN_EDEFAULT;
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        return pulseWidthMax != PULSE_WIDTH_MAX_EDEFAULT;
      case ModelPackage.MSERVO__PERIOD:
        return period != PERIOD_EDEFAULT;
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        return outputVoltage != OUTPUT_VOLTAGE_EDEFAULT;
      case ModelPackage.MSERVO__TARGET_POSITION:
        return targetPosition != TARGET_POSITION_EDEFAULT;
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
        case ModelPackage.MSERVO__SWITCH_STATE: return ModelPackage.SWITCH_SENSOR__SWITCH_STATE;
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
        case ModelPackage.MSERVO__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MSERVO__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.MSERVO__POLL: return ModelPackage.MBASE_DEVICE__POLL;
        case ModelPackage.MSERVO__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.MSERVO__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MoveActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__DIRECTION: return ModelPackage.MOVE_ACTOR__DIRECTION;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == DimmableActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__MIN_VALUE: return ModelPackage.DIMMABLE_ACTOR__MIN_VALUE;
        case ModelPackage.MSERVO__MAX_VALUE: return ModelPackage.DIMMABLE_ACTOR__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__PERCENT_VALUE: return ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE;
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
        case ModelPackage.SWITCH_SENSOR__SWITCH_STATE: return ModelPackage.MSERVO__SWITCH_STATE;
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
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MSERVO__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MSERVO__UID;
        case ModelPackage.MBASE_DEVICE__POLL: return ModelPackage.MSERVO__POLL;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.MSERVO__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.MSERVO__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.MSERVO__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MoveActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MOVE_ACTOR__DIRECTION: return ModelPackage.MSERVO__DIRECTION;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MSERVO__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == DimmableActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.DIMMABLE_ACTOR__MIN_VALUE: return ModelPackage.MSERVO__MIN_VALUE;
        case ModelPackage.DIMMABLE_ACTOR__MAX_VALUE: return ModelPackage.MSERVO__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE: return ModelPackage.MSERVO__PERCENT_VALUE;
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
        case ModelPackage.SWITCH_SENSOR___FETCH_SWITCH_STATE: return ModelPackage.MSERVO___FETCH_SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == ProgrammableSwitchActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PROGRAMMABLE_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS: return ModelPackage.MSERVO___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == MBaseDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.MSERVO___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.MSERVO___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.MSERVO___DISABLE;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
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
        case ModelPackage.MOVE_ACTOR___MOVE__UPDOWNTYPE_DEVICEOPTIONS: return ModelPackage.MSERVO___MOVE__UPDOWNTYPE_DEVICEOPTIONS;
        case ModelPackage.MOVE_ACTOR___STOP: return ModelPackage.MSERVO___STOP;
        case ModelPackage.MOVE_ACTOR___MOVEON__DEVICEOPTIONS: return ModelPackage.MSERVO___MOVEON__DEVICEOPTIONS;
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
        case ModelPackage.DIMMABLE_ACTOR___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS: return ModelPackage.MSERVO___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS: return ModelPackage.MSERVO___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == SetPointActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SET_POINT_ACTOR___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS: return ModelPackage.MSERVO___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS;
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
      case ModelPackage.MSERVO___INIT:
        init();
        return null;
      case ModelPackage.MSERVO___SET_POINT__SHORT_INT_INT:
        return setPoint((Short)arguments.get(0), (Integer)arguments.get(1), (Integer)arguments.get(2));
      case ModelPackage.MSERVO___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS:
        setValue((BigDecimal)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MSERVO___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS:
        setValue((PercentType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MSERVO___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS:
        dimm((IncreaseDecreaseType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MSERVO___MOVE__UPDOWNTYPE_DEVICEOPTIONS:
        move((UpDownType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MSERVO___STOP:
        stop();
        return null;
      case ModelPackage.MSERVO___MOVEON__DEVICEOPTIONS:
        moveon((DeviceOptions)arguments.get(0));
        return null;
      case ModelPackage.MSERVO___ENABLE:
        enable();
        return null;
      case ModelPackage.MSERVO___DISABLE:
        disable();
        return null;
      case ModelPackage.MSERVO___TURN_SWITCH__ONOFFVALUE_DEVICEOPTIONS:
        turnSwitch((OnOffValue)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.MSERVO___FETCH_SWITCH_STATE:
        fetchSwitchState();
        return null;
      case ModelPackage.MSERVO___FETCH_SENSOR_VALUE:
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
    result.append(", subId: ");
    result.append(subId);
    result.append(", direction: ");
    result.append(direction);
    result.append(", minValue: ");
    result.append(minValue);
    result.append(", maxValue: ");
    result.append(maxValue);
    result.append(", percentValue: ");
    result.append(percentValue);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", velocity: ");
    result.append(velocity);
    result.append(", acceleration: ");
    result.append(acceleration);
    result.append(", maxPosition: ");
    result.append(maxPosition);
    result.append(", minPosition: ");
    result.append(minPosition);
    result.append(", pulseWidthMin: ");
    result.append(pulseWidthMin);
    result.append(", pulseWidthMax: ");
    result.append(pulseWidthMax);
    result.append(", period: ");
    result.append(period);
    result.append(", outputVoltage: ");
    result.append(outputVoltage);
    result.append(", targetPosition: ");
    result.append(targetPosition);
    result.append(')');
    return result.toString();
  }

} //MServoImpl
