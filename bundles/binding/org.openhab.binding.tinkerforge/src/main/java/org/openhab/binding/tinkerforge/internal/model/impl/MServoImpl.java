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
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
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
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getOutputVoltage <em>Output Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getServoCurrentPosition <em>Servo Current Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl#getServoDestinationPosition <em>Servo Destination Position</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MServoImpl extends MinimalEObjectImpl.Container implements MServo
{
	private static final short OFF_POSITION = -9000;
	private static final short ON_POSITION = 9000;

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
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected TFServoConfiguration tfConfig;

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
  protected static final int VELOCITY_EDEFAULT = 30000;

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
  protected static final int ACCELERATION_EDEFAULT = 30000;

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
   * The default value of the '{@link #getServoCurrentPosition() <em>Servo Current Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoCurrentPosition()
   * @generated
   * @ordered
   */
  protected static final short SERVO_CURRENT_POSITION_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getServoCurrentPosition() <em>Servo Current Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoCurrentPosition()
   * @generated
   * @ordered
   */
  protected short servoCurrentPosition = SERVO_CURRENT_POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getServoDestinationPosition() <em>Servo Destination Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoDestinationPosition()
   * @generated
   * @ordered
   */
  protected static final short SERVO_DESTINATION_POSITION_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getServoDestinationPosition() <em>Servo Destination Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoDestinationPosition()
   * @generated
   * @ordered
   */
  protected short servoDestinationPosition = SERVO_DESTINATION_POSITION_EDEFAULT;

  private Short servoNum;

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
  public short getServoCurrentPosition()
  {
    return servoCurrentPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServoCurrentPosition(short newServoCurrentPosition)
  {
    short oldServoCurrentPosition = servoCurrentPosition;
    servoCurrentPosition = newServoCurrentPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__SERVO_CURRENT_POSITION, oldServoCurrentPosition, servoCurrentPosition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getServoDestinationPosition()
  {
    return servoDestinationPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServoDestinationPosition(short newServoDestinationPosition)
  {
    short oldServoDestinationPosition = servoDestinationPosition;
    servoDestinationPosition = newServoDestinationPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MSERVO__SERVO_DESTINATION_POSITION, oldServoDestinationPosition, servoDestinationPosition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
	setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MServoImpl.class);
    logger.debug("init called on MServo: " + uid);
  }
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
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
				if (tfConfig.getVelocity() != 0)
					setVelocity(tfConfig.getVelocity()); // TODO check for unset
															// state
				if (tfConfig.getAcceleration() != 0)
					setAcceleration(tfConfig.getAcceleration());
				if (tfConfig.getPeriod() != 0)
					setPeriod(tfConfig.getPeriod());
				if (tfConfig.getPulseWidthMax() != 0
						&& tfConfig.getPulseWidthMin() != 0) {
					setPulseWidthMax(tfConfig.getPulseWidthMax());
					setPulseWidthMin(tfConfig.getPulseWidthMin());
				}
				if (tfConfig.getOutputVoltage() != 0)
					setOutputVoltage(tfConfig.getOutputVoltage());
			}
			BrickServo tinkerBrickServo = brick.getTinkerforgeDevice();
			try {

				servoNum = Short.parseShort(String.valueOf(subId.charAt(subId
						.length() - 1)));
				tinkerBrickServo.setVelocity(servoNum, velocity);
				tinkerBrickServo.setAcceleration(servoNum, acceleration);
				tinkerBrickServo.setPulseWidth(servoNum, pulseWidthMin,
						pulseWidthMax);
				tinkerBrickServo.setPeriod(servoNum, period);
				tinkerBrickServo.setOutputVoltage(outputVoltage);
				tinkerBrickServo
						.addPositionReachedListener(new PositionReachedListener());
				tinkerBrickServo.enable(servoNum);
				setSwitchState(fetchSwitchState());
			} catch (NumberFormatException e) {
				TinkerforgeErrorHandler.handleError(this,
						"can not determine servoNum", e);
			} catch (TimeoutException e) {
				TinkerforgeErrorHandler.handleError(this,
						TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
			} catch (NotConnectedException e) {
				TinkerforgeErrorHandler.handleError(this,
						TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
			}
		}
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	@Override
	public void disable() {
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
  public OnOffValue fetchSwitchState()
  {
		OnOffValue value = OnOffValue.UNDEF;
		try {
			short position = getMbrick().getTinkerforgeDevice().getPosition(servoNum);
			if (position == OFF_POSITION){
				value = OnOffValue.OFF;
			} else if (position == ON_POSITION) {
				value = OnOffValue.ON;
			}
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
   * @generated NOT
   */
  private class PositionReachedListener implements BrickServo.PositionReachedListener {

	@Override
	public void positionReached(short servoNumPar, short position) {
		if (servoNumPar == servoNum) setServoCurrentPosition(position);
	}
	  
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
    @Override
	public void turnSwitch(OnOffValue state) {
		logger.debug("setSwitchState called");
		try {
			
			if (state == OnOffValue.OFF) {
				logger.debug("setSwitchState off");
				setServoDestinationPosition(OFF_POSITION);
				getMbrick().getTinkerforgeDevice().setPosition(servoNum,
						OFF_POSITION);
			}
			else if (state == OnOffValue.ON){
				logger.debug("setSwitchState off");

				setServoDestinationPosition(ON_POSITION);
				MBrickServo mbrick = getMbrick();
				mbrick.getTinkerforgeDevice()
						.setPosition(servoNum, ON_POSITION);
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
      case ModelPackage.MSERVO__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MSERVO__LOGGER:
        return getLogger();
      case ModelPackage.MSERVO__UID:
        return getUid();
      case ModelPackage.MSERVO__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MSERVO__SUB_ID:
        return getSubId();
      case ModelPackage.MSERVO__MBRICK:
        return getMbrick();
      case ModelPackage.MSERVO__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MSERVO__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MSERVO__VELOCITY:
        return getVelocity();
      case ModelPackage.MSERVO__ACCELERATION:
        return getAcceleration();
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        return getPulseWidthMin();
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        return getPulseWidthMax();
      case ModelPackage.MSERVO__PERIOD:
        return getPeriod();
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        return getOutputVoltage();
      case ModelPackage.MSERVO__SERVO_CURRENT_POSITION:
        return getServoCurrentPosition();
      case ModelPackage.MSERVO__SERVO_DESTINATION_POSITION:
        return getServoDestinationPosition();
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
      case ModelPackage.MSERVO__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MSERVO__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MSERVO__UID:
        setUid((String)newValue);
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
      case ModelPackage.MSERVO__TF_CONFIG:
        setTfConfig((TFServoConfiguration)newValue);
        return;
      case ModelPackage.MSERVO__VELOCITY:
        setVelocity((Integer)newValue);
        return;
      case ModelPackage.MSERVO__ACCELERATION:
        setAcceleration((Integer)newValue);
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
      case ModelPackage.MSERVO__SERVO_CURRENT_POSITION:
        setServoCurrentPosition((Short)newValue);
        return;
      case ModelPackage.MSERVO__SERVO_DESTINATION_POSITION:
        setServoDestinationPosition((Short)newValue);
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
      case ModelPackage.MSERVO__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MSERVO__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MSERVO__UID:
        setUid(UID_EDEFAULT);
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
      case ModelPackage.MSERVO__TF_CONFIG:
        setTfConfig((TFServoConfiguration)null);
        return;
      case ModelPackage.MSERVO__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.MSERVO__ACCELERATION:
        setAcceleration(ACCELERATION_EDEFAULT);
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
      case ModelPackage.MSERVO__SERVO_CURRENT_POSITION:
        setServoCurrentPosition(SERVO_CURRENT_POSITION_EDEFAULT);
        return;
      case ModelPackage.MSERVO__SERVO_DESTINATION_POSITION:
        setServoDestinationPosition(SERVO_DESTINATION_POSITION_EDEFAULT);
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
      case ModelPackage.MSERVO__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MSERVO__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MSERVO__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MSERVO__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MSERVO__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MSERVO__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MSERVO__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MSERVO__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MSERVO__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.MSERVO__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.MSERVO__PULSE_WIDTH_MIN:
        return pulseWidthMin != PULSE_WIDTH_MIN_EDEFAULT;
      case ModelPackage.MSERVO__PULSE_WIDTH_MAX:
        return pulseWidthMax != PULSE_WIDTH_MAX_EDEFAULT;
      case ModelPackage.MSERVO__PERIOD:
        return period != PERIOD_EDEFAULT;
      case ModelPackage.MSERVO__OUTPUT_VOLTAGE:
        return outputVoltage != OUTPUT_VOLTAGE_EDEFAULT;
      case ModelPackage.MSERVO__SERVO_CURRENT_POSITION:
        return servoCurrentPosition != SERVO_CURRENT_POSITION_EDEFAULT;
      case ModelPackage.MSERVO__SERVO_DESTINATION_POSITION:
        return servoDestinationPosition != SERVO_DESTINATION_POSITION_EDEFAULT;
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
        case ModelPackage.MSERVO__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MSERVO__UID: return ModelPackage.MBASE_DEVICE__UID;
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
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MSERVO__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MSERVO__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MSERVO__UID;
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
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MSERVO__TF_CONFIG;
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
      case ModelPackage.MSERVO___INIT:
        init();
        return null;
      case ModelPackage.MSERVO___ENABLE:
        enable();
        return null;
      case ModelPackage.MSERVO___DISABLE:
        disable();
        return null;
      case ModelPackage.MSERVO___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.MSERVO___FETCH_SWITCH_STATE:
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
    result.append(", subId: ");
    result.append(subId);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", velocity: ");
    result.append(velocity);
    result.append(", acceleration: ");
    result.append(acceleration);
    result.append(", pulseWidthMin: ");
    result.append(pulseWidthMin);
    result.append(", pulseWidthMax: ");
    result.append(pulseWidthMax);
    result.append(", period: ");
    result.append(period);
    result.append(", outputVoltage: ");
    result.append(outputVoltage);
    result.append(", servoCurrentPosition: ");
    result.append(servoCurrentPosition);
    result.append(", servoDestinationPosition: ");
    result.append(servoDestinationPosition);
    result.append(')');
    return result.toString();
  }

} //MServoImpl
