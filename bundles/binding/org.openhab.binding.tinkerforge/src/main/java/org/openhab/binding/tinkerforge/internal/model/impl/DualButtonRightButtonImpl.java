/**
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
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.DualButtonRightButton;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDualButton;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualButton.ButtonState;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dual Button Right Button</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonRightButtonImpl#getSensorValue <em>Sensor Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DualButtonRightButtonImpl extends MinimalEObjectImpl.Container implements DualButtonRightButton
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
  protected HighLowValue sensorValue;
  private BrickletDualButton tinkerforgeDevice;
  private StateListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DualButtonRightButtonImpl()
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
    return ModelPackage.Literals.DUAL_BUTTON_RIGHT_BUTTON;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletDualButton getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK) return null;
    return (MBrickletDualButton)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletDualButton newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletDualButton newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HighLowValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(HighLowValue newSensorValue)
  {
    HighLowValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    try {
      ButtonState buttonState = tinkerforgeDevice.getButtonState();
      setSensorValue(getValue4State(buttonState.buttonR));
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
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(DualButtonRightButtonImpl.class);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void enable()
  {
    tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
    listener = new StateListener();
    tinkerforgeDevice.addStateChangedListener(listener);
    fetchSensorValue();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  private class StateListener implements BrickletDualButton.StateChangedListener {

    @Override
    public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
      HighLowValue value = getValue4State(buttonR);
      setSensorValue(value);
    }

  }

  private HighLowValue getValue4State(short state) {
    return state == BrickletDualButton.BUTTON_STATE_PRESSED ? HighLowValue.LOW : HighLowValue.HIGH;
  }


  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void disable()
  {
    if (listener != null) {
      tinkerforgeDevice.removeStateChangedListener(listener);
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletDualButton)otherEnd, msgs);
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        return basicSetMbrick(null, msgs);
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__LOGGER:
        return getLogger();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__UID:
        return getUid();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__POLL:
        return isPoll();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__ENABLED_A:
        return getEnabledA();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SUB_ID:
        return getSubId();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        return getMbrick();
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE:
        return getSensorValue();
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        setMbrick((MBrickletDualButton)newValue);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE:
        setSensorValue((HighLowValue)newValue);
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        setMbrick((MBrickletDualButton)null);
        return;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE:
        setSensorValue((HighLowValue)null);
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__MBRICK:
        return getMbrick() != null;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE:
        return sensorValue != null;
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
        case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
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
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.DUAL_BUTTON_RIGHT_BUTTON__SENSOR_VALUE;
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
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.DUAL_BUTTON_RIGHT_BUTTON___FETCH_SENSOR_VALUE;
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
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON___INIT:
        init();
        return null;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON___ENABLE:
        enable();
        return null;
      case ModelPackage.DUAL_BUTTON_RIGHT_BUTTON___DISABLE:
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
    result.append(')');
    return result.toString();
  }

} //DualButtonRightButtonImpl
