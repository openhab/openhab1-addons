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
import java.math.RoundingMode;
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
import org.openhab.binding.tinkerforge.internal.model.DimmableActor;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch;
import org.openhab.binding.tinkerforge.internal.model.MInSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.PercentTypeActor;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitch;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Switch B</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getMaxValue <em>Max Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getPercentValue <em>Percent Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getAddress <em>Address</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getRepeats <em>Repeats</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBImpl#getAbsDimmValue <em>Abs Dimm Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoteSwitchBImpl extends MinimalEObjectImpl.Container implements RemoteSwitchB
{
  /**
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected PercentValue sensorValue;

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
  protected RemoteSwitchBConfiguration tfConfig;

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
  protected static final String DEVICE_TYPE_EDEFAULT = "remote_switch_b";

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
   * The default value of the '{@link #getAddress() <em>Address</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAddress()
   * @generated
   * @ordered
   */
  protected static final Long ADDRESS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAddress() <em>Address</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAddress()
   * @generated
   * @ordered
   */
  protected Long address = ADDRESS_EDEFAULT;

  /**
   * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected static final Short UNIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected Short unit = UNIT_EDEFAULT;

  /**
   * The default value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected static final Short REPEATS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected Short repeats = REPEATS_EDEFAULT;

  /**
   * The default value of the '{@link #getAbsDimmValue() <em>Abs Dimm Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAbsDimmValue()
   * @generated
   * @ordered
   */
  protected static final Short ABS_DIMM_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAbsDimmValue() <em>Abs Dimm Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAbsDimmValue()
   * @generated
   * @ordered
   */
  protected Short absDimmValue = ABS_DIMM_VALUE_EDEFAULT;

  private BrickletRemoteSwitch tinkerforgeDevice;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RemoteSwitchBImpl()
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
    return ModelPackage.Literals.REMOTE_SWITCH_B;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PercentValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(PercentValue newSensorValue)
  {
    PercentValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__SENSOR_VALUE, oldSensorValue, sensorValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE, oldSwitchState, switchState));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletRemoteSwitch getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.REMOTE_SWITCH_B__MBRICK) return null;
    return (MBrickletRemoteSwitch)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletRemoteSwitch newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.REMOTE_SWITCH_B__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletRemoteSwitch newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.REMOTE_SWITCH_B__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__MBRICK, newMbrick, newMbrick));
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
  public Long getAddress()
  {
    return address;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAddress(Long newAddress)
  {
    Long oldAddress = address;
    address = newAddress;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__ADDRESS, oldAddress, address));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getUnit()
  {
    return unit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUnit(Short newUnit)
  {
    Short oldUnit = unit;
    unit = newUnit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__UNIT, oldUnit, unit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getRepeats()
  {
    return repeats;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRepeats(Short newRepeats)
  {
    Short oldRepeats = repeats;
    repeats = newRepeats;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__REPEATS, oldRepeats, repeats));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getAbsDimmValue()
  {
    return absDimmValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAbsDimmValue(Short newAbsDimmValue)
  {
    Short oldAbsDimmValue = absDimmValue;
    absDimmValue = newAbsDimmValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__ABS_DIMM_VALUE, oldAbsDimmValue, absDimmValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchBConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(RemoteSwitchBConfiguration newTfConfig, NotificationChain msgs)
  {
    RemoteSwitchBConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(RemoteSwitchBConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.REMOTE_SWITCH_B__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.REMOTE_SWITCH_B__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__MIN_VALUE, oldMinValue, minValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__MAX_VALUE, oldMaxValue, maxValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE, oldPercentValue, percentValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(RemoteSwitchBImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable() {
    logger.debug("{} enable called on RemoteSwitchB", LoggerConstants.TFINIT);
    minValue = BigDecimal.ZERO;
    maxValue = new BigDecimal("15");
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("address"))) {
        setAddress(tfConfig.getAddress());
      } else {
        logger.error("{} address not configured for subid {}", LoggerConstants.TFINITSUB,
            getSubId());
        throw new RuntimeException("address not configured for subid " + getSubId());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("unit"))) {
        setUnit(tfConfig.getUnit());
      } else {
        logger.error("{} unit not configured for subid {}", LoggerConstants.TFINITSUB, getSubId());
        throw new RuntimeException("unit not configured for subid " + getSubId());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("repeats"))) {
        setRepeats(tfConfig.getRepeats());
      }
    }
    tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
    if (getRepeats() != null) {
      try {
        tinkerforgeDevice.setRepeats(getRepeats());
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  public void disable()
  {
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void turnSwitch(OnOffValue state)
  {
    if (state == OnOffValue.UNDEF) {
      logger.warn("got undef state, nothing to be done");
      return;
    }
    logger.debug("turnSwitch called for {}/{}", getUid(), getSubId());
    if (state == OnOffValue.OFF){
      dimm(minValue.shortValue());
    } else {
      dimm(maxValue.shortValue());
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setValue(PercentType newValue, DeviceOptions opts) {
    logger.debug("setValue percentType called {}", newValue);
    short value =
        getMaxValue().multiply(newValue.toBigDecimal())
            .divide(new BigDecimal("100"), RoundingMode.HALF_UP).shortValue();
    dimm(value);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void dimm(IncreaseDecreaseType increaseDecrease, DeviceOptions opts) {
    logger.trace("dimmer increase increaseDecrease {} opts {}", increaseDecrease, opts);
    short defaultStep = 1;
    if (opts == null) {
      logger.debug("no step option, defaulting step to {}", defaultStep);
    }
    Short  step = Tools.getShortOpt(ConfigOptsDimmable.STEP.toString(), opts, defaultStep);
    if (increaseDecrease == null) {
      logger.error("increaseDecrease may not be null!");
      return;
    }
    Short newDimmValue = null;
    if (increaseDecrease.equals(IncreaseDecreaseType.INCREASE)) {
      if (absDimmValue == null){
        newDimmValue = step;
      } else {
        newDimmValue = (short) (getSensorValue().shortValue() + step);
      }
    } else if (increaseDecrease.equals(IncreaseDecreaseType.DECREASE)) {
      if (absDimmValue == null){
        newDimmValue = 0;
      }
      else {
        newDimmValue = (short) (absDimmValue - step);
      }
    }
    dimm(newDimmValue);
  }

  /**
   * <!-- begin-user-doc -->
   * wait for device ready
   * if dimm value is 0% call switch off
   * if dimm value is 100% call switch on
   * <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  private void dimm(Short newDimmValue) {
    if (newDimmValue == null) {
      logger.error("newDimmValue must not be null");
      return;
    }
    logger.trace("new dimm value {}", newDimmValue);
    if (newDimmValue > getMaxValue().shortValue()) {
      if (absDimmValue != null && absDimmValue < getMaxValue().shortValue()) {
        newDimmValue = getMaxValue().shortValue();
      } else {
        logger.trace("max dim value already reached");
        return;
      }
    } else if (newDimmValue < getMinValue().shortValue()) {
      if (absDimmValue != null && absDimmValue > getMinValue().shortValue()) {
        newDimmValue = getMinValue().shortValue();
      } else {
        logger.trace("min dim value already reached");
        return;
      }
    }
    logger.debug("newDimmValue {}", newDimmValue);
    int maxRetries = 20;
    int trial = 0;
    try {
      while (tinkerforgeDevice.getSwitchingState() == BrickletRemoteSwitch.SWITCHING_STATE_BUSY
          && trial < maxRetries) {
        trial++;
        logger.trace("waiting for ready state {}", trial);
        Thread.sleep(50);
      }
      if (trial == maxRetries) {
        logger.error("remote switch doesn't go to ready state in spite of {} retries.", trial);
        return;
      }
      PercentValue dimmState;
      if (newDimmValue == getMinValue().shortValue()) {
        // switch off
        dimmState = new PercentValue(BigDecimal.ZERO);
        logger.debug("switching socket B with address {}, unit {} to {}", getAddress(), getUnit(),
            BrickletRemoteSwitch.SWITCH_TO_OFF);
        tinkerforgeDevice
            .switchSocketB(getAddress(), getUnit(), BrickletRemoteSwitch.SWITCH_TO_OFF);
      } else if (newDimmValue == getMaxValue().shortValue()) {
        // switch on
        dimmState = new PercentValue(new BigDecimal(100));
        logger.debug("switching socket B with address {}, unit {} to {}", getAddress(), getUnit(),
            BrickletRemoteSwitch.SWITCH_TO_ON);
        tinkerforgeDevice.switchSocketB(getAddress(), getUnit(), BrickletRemoteSwitch.SWITCH_TO_ON);
      } else {
        logger.debug("*** newValue {}", (new BigDecimal(newDimmValue).divide(getMaxValue(), 2,
            RoundingMode.HALF_UP).multiply(new BigDecimal(100))));
        dimmState =
            new PercentValue(new BigDecimal(newDimmValue).divide(getMaxValue(), 2,
                RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        getMbrick().getTinkerforgeDevice().dimSocketB(getAddress(), getUnit(), newDimmValue);
      }
      logger.debug("new dimmState is {}", dimmState);
      absDimmValue = newDimmValue; // do not notify listeners
      setSensorValue(dimmState);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    } catch (InterruptedException e) {
      logger.warn("retry was interrupted");
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSwitchState() {
    setSensorValue(getSensorValue()); // trigger a value update to the eventbus
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    setSensorValue(getSensorValue()); // trigger a value update to the eventbus
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
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletRemoteSwitch)otherEnd, msgs);
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
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG:
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
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
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
      case ModelPackage.REMOTE_SWITCH_B__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.REMOTE_SWITCH_B__LOGGER:
        return getLogger();
      case ModelPackage.REMOTE_SWITCH_B__UID:
        return getUid();
      case ModelPackage.REMOTE_SWITCH_B__POLL:
        return isPoll();
      case ModelPackage.REMOTE_SWITCH_B__ENABLED_A:
        return getEnabledA();
      case ModelPackage.REMOTE_SWITCH_B__SUB_ID:
        return getSubId();
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        return getMbrick();
      case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.REMOTE_SWITCH_B__MIN_VALUE:
        return getMinValue();
      case ModelPackage.REMOTE_SWITCH_B__MAX_VALUE:
        return getMaxValue();
      case ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE:
        return getPercentValue();
      case ModelPackage.REMOTE_SWITCH_B__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.REMOTE_SWITCH_B__ADDRESS:
        return getAddress();
      case ModelPackage.REMOTE_SWITCH_B__UNIT:
        return getUnit();
      case ModelPackage.REMOTE_SWITCH_B__REPEATS:
        return getRepeats();
      case ModelPackage.REMOTE_SWITCH_B__ABS_DIMM_VALUE:
        return getAbsDimmValue();
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
      case ModelPackage.REMOTE_SWITCH_B__SENSOR_VALUE:
        setSensorValue((PercentValue)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        setMbrick((MBrickletRemoteSwitch)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG:
        setTfConfig((RemoteSwitchBConfiguration)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MIN_VALUE:
        setMinValue((BigDecimal)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MAX_VALUE:
        setMaxValue((BigDecimal)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE:
        setPercentValue((PercentValue)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ADDRESS:
        setAddress((Long)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__UNIT:
        setUnit((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__REPEATS:
        setRepeats((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ABS_DIMM_VALUE:
        setAbsDimmValue((Short)newValue);
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
      case ModelPackage.REMOTE_SWITCH_B__SENSOR_VALUE:
        setSensorValue((PercentValue)null);
        return;
      case ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        setMbrick((MBrickletRemoteSwitch)null);
        return;
      case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG:
        setTfConfig((RemoteSwitchBConfiguration)null);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MIN_VALUE:
        setMinValue(MIN_VALUE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__MAX_VALUE:
        setMaxValue(MAX_VALUE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE:
        setPercentValue(PERCENT_VALUE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ADDRESS:
        setAddress(ADDRESS_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__UNIT:
        setUnit(UNIT_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__REPEATS:
        setRepeats(REPEATS_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_B__ABS_DIMM_VALUE:
        setAbsDimmValue(ABS_DIMM_VALUE_EDEFAULT);
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
      case ModelPackage.REMOTE_SWITCH_B__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.REMOTE_SWITCH_B__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.REMOTE_SWITCH_B__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.REMOTE_SWITCH_B__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.REMOTE_SWITCH_B__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.REMOTE_SWITCH_B__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.REMOTE_SWITCH_B__MBRICK:
        return getMbrick() != null;
      case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.REMOTE_SWITCH_B__MIN_VALUE:
        return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
      case ModelPackage.REMOTE_SWITCH_B__MAX_VALUE:
        return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
      case ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE:
        return PERCENT_VALUE_EDEFAULT == null ? percentValue != null : !PERCENT_VALUE_EDEFAULT.equals(percentValue);
      case ModelPackage.REMOTE_SWITCH_B__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.REMOTE_SWITCH_B__ADDRESS:
        return ADDRESS_EDEFAULT == null ? address != null : !ADDRESS_EDEFAULT.equals(address);
      case ModelPackage.REMOTE_SWITCH_B__UNIT:
        return UNIT_EDEFAULT == null ? unit != null : !UNIT_EDEFAULT.equals(unit);
      case ModelPackage.REMOTE_SWITCH_B__REPEATS:
        return REPEATS_EDEFAULT == null ? repeats != null : !REPEATS_EDEFAULT.equals(repeats);
      case ModelPackage.REMOTE_SWITCH_B__ABS_DIMM_VALUE:
        return ABS_DIMM_VALUE_EDEFAULT == null ? absDimmValue != null : !ABS_DIMM_VALUE_EDEFAULT.equals(absDimmValue);
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
        case ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE: return ModelPackage.SWITCH_SENSOR__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MInSwitchActor.class)
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
        case ModelPackage.REMOTE_SWITCH_B__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.REMOTE_SWITCH_B__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.REMOTE_SWITCH_B__POLL: return ModelPackage.MBASE_DEVICE__POLL;
        case ModelPackage.REMOTE_SWITCH_B__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_B__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.REMOTE_SWITCH_B__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == RemoteSwitch.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_B__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == DimmableActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_B__MIN_VALUE: return ModelPackage.DIMMABLE_ACTOR__MIN_VALUE;
        case ModelPackage.REMOTE_SWITCH_B__MAX_VALUE: return ModelPackage.DIMMABLE_ACTOR__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE: return ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE;
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
        case ModelPackage.SWITCH_SENSOR__SWITCH_STATE: return ModelPackage.REMOTE_SWITCH_B__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MInSwitchActor.class)
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
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.REMOTE_SWITCH_B__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.REMOTE_SWITCH_B__UID;
        case ModelPackage.MBASE_DEVICE__POLL: return ModelPackage.REMOTE_SWITCH_B__POLL;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.REMOTE_SWITCH_B__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.REMOTE_SWITCH_B__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.REMOTE_SWITCH_B__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == RemoteSwitch.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.REMOTE_SWITCH_B__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == DimmableActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.DIMMABLE_ACTOR__MIN_VALUE: return ModelPackage.REMOTE_SWITCH_B__MIN_VALUE;
        case ModelPackage.DIMMABLE_ACTOR__MAX_VALUE: return ModelPackage.REMOTE_SWITCH_B__MAX_VALUE;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE: return ModelPackage.REMOTE_SWITCH_B__PERCENT_VALUE;
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
        case ModelPackage.SWITCH_SENSOR___FETCH_SWITCH_STATE: return ModelPackage.REMOTE_SWITCH_B___FETCH_SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE: return ModelPackage.REMOTE_SWITCH_B___TURN_SWITCH__ONOFFVALUE;
        default: return -1;
      }
    }
    if (baseClass == MInSwitchActor.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    if (baseClass == MBaseDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.REMOTE_SWITCH_B___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.REMOTE_SWITCH_B___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.REMOTE_SWITCH_B___DISABLE;
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
    if (baseClass == RemoteSwitch.class)
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
    if (baseClass == DimmableActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.DIMMABLE_ACTOR___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS: return ModelPackage.REMOTE_SWITCH_B___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS;
        default: return -1;
      }
    }
    if (baseClass == PercentTypeActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.PERCENT_TYPE_ACTOR___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS: return ModelPackage.REMOTE_SWITCH_B___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS;
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
      case ModelPackage.REMOTE_SWITCH_B___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS:
        setValue((PercentType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.REMOTE_SWITCH_B___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS:
        dimm((IncreaseDecreaseType)arguments.get(0), (DeviceOptions)arguments.get(1));
        return null;
      case ModelPackage.REMOTE_SWITCH_B___INIT:
        init();
        return null;
      case ModelPackage.REMOTE_SWITCH_B___ENABLE:
        enable();
        return null;
      case ModelPackage.REMOTE_SWITCH_B___DISABLE:
        disable();
        return null;
      case ModelPackage.REMOTE_SWITCH_B___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.REMOTE_SWITCH_B___FETCH_SWITCH_STATE:
        fetchSwitchState();
        return null;
      case ModelPackage.REMOTE_SWITCH_B___FETCH_SENSOR_VALUE:
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
    result.append(", minValue: ");
    result.append(minValue);
    result.append(", maxValue: ");
    result.append(maxValue);
    result.append(", percentValue: ");
    result.append(percentValue);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", address: ");
    result.append(address);
    result.append(", unit: ");
    result.append(unit);
    result.append(", repeats: ");
    result.append(repeats);
    result.append(", absDimmValue: ");
    result.append(absDimmValue);
    result.append(')');
    return result.toString();
  }

} //RemoteSwitchBImpl
