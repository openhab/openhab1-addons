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
import org.openhab.binding.tinkerforge.internal.model.LoadCellConfiguration;
import org.openhab.binding.tinkerforge.internal.model.LoadCellWeight;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLoadCell;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletLoadCell;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Cell Weight</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getCallbackPeriod <em>Callback Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellWeightImpl#getMovingAverage <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadCellWeightImpl extends MinimalEObjectImpl.Container implements LoadCellWeight
{
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
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected DecimalValue sensorValue;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected LoadCellConfiguration tfConfig;

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
  protected static final String DEVICE_TYPE_EDEFAULT = "loadcell_weight";

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
   * The default value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected static final short MOVING_AVERAGE_EDEFAULT = 4;

  /**
   * The cached value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected short movingAverage = MOVING_AVERAGE_EDEFAULT;

  private BrickletLoadCell tinkerforgeDevice;

  private WeightListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LoadCellWeightImpl()
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
    return ModelPackage.Literals.LOAD_CELL_WEIGHT;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLoadCell getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.LOAD_CELL_WEIGHT__MBRICK) return null;
    return (MBrickletLoadCell)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletLoadCell newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.LOAD_CELL_WEIGHT__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletLoadCell newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.LOAD_CELL_WEIGHT__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__MBRICK, newMbrick, newMbrick));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LoadCellConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(LoadCellConfiguration newTfConfig, NotificationChain msgs)
  {
    LoadCellConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(LoadCellConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__THRESHOLD, oldThreshold, threshold));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getMovingAverage()
  {
    return movingAverage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMovingAverage(short newMovingAverage)
  {
    short oldMovingAverage = movingAverage;
    movingAverage = newMovingAverage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_WEIGHT__MOVING_AVERAGE, oldMovingAverage, movingAverage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(LoadCellWeightImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void tare()
  {
    try {
      logger.debug("calling tare");
      tinkerforgeDevice.tare();
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
  public void fetchSensorValue()
  {
    try {
      int currentValue = tinkerforgeDevice.getWeight();
      DecimalValue value = Tools.calculate(currentValue);
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
   * @generated NOT
   */
  public void enable()
  {
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("threshold"))) {
        setThreshold(tfConfig.getThreshold());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("callbackPeriod"))) {
        setCallbackPeriod(tfConfig.getCallbackPeriod());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("movingAverage"))) {
        logger.debug("movingAverage configured as {}", tfConfig.getMovingAverage());
        setMovingAverage(tfConfig.getMovingAverage());
      }
    }
    tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
    try {
      tinkerforgeDevice.setWeightCallbackPeriod(getCallbackPeriod());
      tinkerforgeDevice.setMovingAverage(getMovingAverage());
      listener = new WeightListener();
      tinkerforgeDevice.addWeightListener(listener);
      fetchSensorValue();
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  private class WeightListener implements BrickletLoadCell.WeightListener {

    @Override
    public void weight(int newValue) {
      DecimalValue value = Tools.calculate(newValue);
      logger.trace("{} got new value {}", LoggerConstants.TFMODELUPDATE, value);
      if (value.compareTo(getSensorValue(), getThreshold()) != 0) {
        logger.trace("{} setting new value {}", LoggerConstants.TFMODELUPDATE, value);
        setSensorValue(value);
      } else {
        logger.trace("{} omitting new value {}", LoggerConstants.TFMODELUPDATE, value);
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
    if (listener != null){
      tinkerforgeDevice.removeWeightListener(listener);
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
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletLoadCell)otherEnd, msgs);
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
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG:
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
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
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
      case ModelPackage.LOAD_CELL_WEIGHT__LOGGER:
        return getLogger();
      case ModelPackage.LOAD_CELL_WEIGHT__UID:
        return getUid();
      case ModelPackage.LOAD_CELL_WEIGHT__POLL:
        return isPoll();
      case ModelPackage.LOAD_CELL_WEIGHT__ENABLED_A:
        return getEnabledA();
      case ModelPackage.LOAD_CELL_WEIGHT__SUB_ID:
        return getSubId();
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        return getMbrick();
      case ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD:
        return getCallbackPeriod();
      case ModelPackage.LOAD_CELL_WEIGHT__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.LOAD_CELL_WEIGHT__THRESHOLD:
        return getThreshold();
      case ModelPackage.LOAD_CELL_WEIGHT__MOVING_AVERAGE:
        return getMovingAverage();
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
      case ModelPackage.LOAD_CELL_WEIGHT__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        setMbrick((MBrickletLoadCell)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE:
        setSensorValue((DecimalValue)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG:
        setTfConfig((LoadCellConfiguration)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD:
        setCallbackPeriod((Long)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__THRESHOLD:
        setThreshold((BigDecimal)newValue);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__MOVING_AVERAGE:
        setMovingAverage((Short)newValue);
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
      case ModelPackage.LOAD_CELL_WEIGHT__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        setMbrick((MBrickletLoadCell)null);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE:
        setSensorValue((DecimalValue)null);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG:
        setTfConfig((LoadCellConfiguration)null);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD:
        setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__THRESHOLD:
        setThreshold(THRESHOLD_EDEFAULT);
        return;
      case ModelPackage.LOAD_CELL_WEIGHT__MOVING_AVERAGE:
        setMovingAverage(MOVING_AVERAGE_EDEFAULT);
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
      case ModelPackage.LOAD_CELL_WEIGHT__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.LOAD_CELL_WEIGHT__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.LOAD_CELL_WEIGHT__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.LOAD_CELL_WEIGHT__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.LOAD_CELL_WEIGHT__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.LOAD_CELL_WEIGHT__MBRICK:
        return getMbrick() != null;
      case ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD:
        return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
      case ModelPackage.LOAD_CELL_WEIGHT__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.LOAD_CELL_WEIGHT__THRESHOLD:
        return THRESHOLD_EDEFAULT == null ? threshold != null : !THRESHOLD_EDEFAULT.equals(threshold);
      case ModelPackage.LOAD_CELL_WEIGHT__MOVING_AVERAGE:
        return movingAverage != MOVING_AVERAGE_EDEFAULT;
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
    if (baseClass == MSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD: return ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD;
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
    if (baseClass == MSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.LOAD_CELL_WEIGHT__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.LOAD_CELL_WEIGHT__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD: return ModelPackage.LOAD_CELL_WEIGHT__CALLBACK_PERIOD;
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
    if (baseClass == MSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.LOAD_CELL_WEIGHT___FETCH_SENSOR_VALUE;
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
      case ModelPackage.LOAD_CELL_WEIGHT___INIT:
        init();
        return null;
      case ModelPackage.LOAD_CELL_WEIGHT___TARE:
        tare();
        return null;
      case ModelPackage.LOAD_CELL_WEIGHT___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
      case ModelPackage.LOAD_CELL_WEIGHT___ENABLE:
        enable();
        return null;
      case ModelPackage.LOAD_CELL_WEIGHT___DISABLE:
        disable();
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
    result.append(" (logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", poll: ");
    result.append(poll);
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", subId: ");
    result.append(subId);
    result.append(", sensorValue: ");
    result.append(sensorValue);
    result.append(", callbackPeriod: ");
    result.append(callbackPeriod);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", threshold: ");
    result.append(threshold);
    result.append(", movingAverage: ");
    result.append(movingAverage);
    result.append(')');
    return result.toString();
  }

} //LoadCellWeightImpl
