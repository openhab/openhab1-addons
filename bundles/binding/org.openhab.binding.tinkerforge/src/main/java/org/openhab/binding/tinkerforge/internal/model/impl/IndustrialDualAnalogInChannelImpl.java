/**
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
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDualAnalogIn;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletIndustrialDualAnalogIn;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Industrial Dual Analog In Channel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getCallbackPeriod <em>Callback Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.IndustrialDualAnalogInChannelImpl#getChannelNum <em>Channel Num</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndustrialDualAnalogInChannelImpl extends MinimalEObjectImpl.Container implements IndustrialDualAnalogInChannel
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
  protected TFBaseConfiguration tfConfig;

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
  protected static final String DEVICE_TYPE_EDEFAULT = "industrial_dual_analogin_channel";

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
  protected static final BigDecimal THRESHOLD_EDEFAULT = new BigDecimal("0");

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
   * The default value of the '{@link #getChannelNum() <em>Channel Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChannelNum()
   * @generated
   * @ordered
   */
  protected static final Short CHANNEL_NUM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getChannelNum() <em>Channel Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChannelNum()
   * @generated
   * @ordered
   */
  protected Short channelNum = CHANNEL_NUM_EDEFAULT;

  private VoltageListener listener;

  private BrickletIndustrialDualAnalogIn tinkerforgeDevice;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IndustrialDualAnalogInChannelImpl()
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
    return ModelPackage.Literals.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SENSOR_VALUE, oldSensorValue, sensorValue));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDualAnalogIn getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK) return null;
    return (MBrickletIndustrialDualAnalogIn)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletIndustrialDualAnalogIn newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletIndustrialDualAnalogIn newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFBaseConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(TFBaseConfiguration newTfConfig, NotificationChain msgs)
  {
    TFBaseConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(TFBaseConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__THRESHOLD, oldThreshold, threshold));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getChannelNum()
  {
    return channelNum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setChannelNum(Short newChannelNum)
  {
    Short oldChannelNum = channelNum;
    channelNum = newChannelNum;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CHANNEL_NUM, oldChannelNum, channelNum));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(IndustrialDualAnalogInChannelImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable() {
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("threshold"))) {
        logger.debug("threshold {}", tfConfig.getThreshold());
        setThreshold(tfConfig.getThreshold());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("callbackPeriod"))) {
        logger.debug("callbackPeriod {}", tfConfig.getCallbackPeriod());
        setCallbackPeriod(tfConfig.getCallbackPeriod());
      }
    }
    try {
      tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
      tinkerforgeDevice.setVoltageCallbackPeriod(getChannelNum(), getCallbackPeriod());
      listener = new VoltageListener();
      tinkerforgeDevice.addVoltageListener(listener);
      fetchSensorValue();
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  private class VoltageListener implements BrickletIndustrialDualAnalogIn.VoltageListener {

    @Override
    public void voltage(short channel, int voltage) {
      if (getChannelNum() == channel){
        DecimalValue value = Tools.calculate(voltage);
        logger.trace("{} got new value {}", LoggerConstants.TFMODELUPDATE, value);
        if (value.compareTo(getSensorValue(), getThreshold()) != 0) {
          logger.trace("{} setting new value {}", LoggerConstants.TFMODELUPDATE, value);
          setSensorValue(value);
        } else {
          logger.trace("{} omitting new value {}", LoggerConstants.TFMODELUPDATE, value);
        }
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
    if (listener != null) {
      tinkerforgeDevice.removeVoltageListener(listener);
    }
    tinkerforgeDevice = null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    try {
      int voltage = tinkerforgeDevice.getVoltage(getChannelNum());
      DecimalValue value = Tools.calculate(voltage);
      setSensorValue(value);
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
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletIndustrialDualAnalogIn)otherEnd, msgs);
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG:
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER:
        return getLogger();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID:
        return getUid();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL:
        return isPoll();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A:
        return getEnabledA();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID:
        return getSubId();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        return getMbrick();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD:
        return getCallbackPeriod();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__THRESHOLD:
        return getThreshold();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CHANNEL_NUM:
        return getChannelNum();
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SENSOR_VALUE:
        setSensorValue((DecimalValue)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        setMbrick((MBrickletIndustrialDualAnalogIn)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG:
        setTfConfig((TFBaseConfiguration)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD:
        setCallbackPeriod((Long)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__THRESHOLD:
        setThreshold((BigDecimal)newValue);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CHANNEL_NUM:
        setChannelNum((Short)newValue);
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SENSOR_VALUE:
        setSensorValue((DecimalValue)null);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        setMbrick((MBrickletIndustrialDualAnalogIn)null);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG:
        setTfConfig((TFBaseConfiguration)null);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD:
        setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__THRESHOLD:
        setThreshold(THRESHOLD_EDEFAULT);
        return;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CHANNEL_NUM:
        setChannelNum(CHANNEL_NUM_EDEFAULT);
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK:
        return getMbrick() != null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD:
        return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__THRESHOLD:
        return THRESHOLD_EDEFAULT == null ? threshold != null : !THRESHOLD_EDEFAULT.equals(threshold);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CHANNEL_NUM:
        return CHANNEL_NUM_EDEFAULT == null ? channelNum != null : !CHANNEL_NUM_EDEFAULT.equals(channelNum);
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
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL: return ModelPackage.MBASE_DEVICE__POLL;
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD: return ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD;
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
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__UID;
        case ModelPackage.MBASE_DEVICE__POLL: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__POLL;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL__CALLBACK_PERIOD;
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
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___DISABLE;
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
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___INIT:
        init();
        return null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___ENABLE:
        enable();
        return null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___DISABLE:
        disable();
        return null;
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL___FETCH_SENSOR_VALUE:
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
    result.append(", callbackPeriod: ");
    result.append(callbackPeriod);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", threshold: ");
    result.append(threshold);
    result.append(", channelNum: ");
    result.append(channelNum);
    result.append(')');
    return result.toString();
  }

} //IndustrialDualAnalogInChannelImpl
