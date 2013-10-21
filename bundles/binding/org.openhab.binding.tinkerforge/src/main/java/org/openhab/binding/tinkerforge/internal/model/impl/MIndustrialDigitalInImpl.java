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
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.SwitchState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletIndustrialDigitalIn4;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MIndustrial Digital In</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getSwitchState <em>Switch State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIndustrialDigitalInImpl extends MinimalEObjectImpl.Container implements MIndustrialDigitalIn
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
   * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected static final SwitchState SWITCH_STATE_EDEFAULT = SwitchState.ON;

  /**
   * The cached value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected SwitchState switchState = SWITCH_STATE_EDEFAULT;

private short inNum;

private InterruptListener interruptListener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIndustrialDigitalInImpl()
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
    return ModelPackage.Literals.MINDUSTRIAL_DIGITAL_IN;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDigitalIn4 getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK) return null;
    return (MBrickletIndustrialDigitalIn4)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletIndustrialDigitalIn4 newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletIndustrialDigitalIn4 newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SwitchState getSwitchState()
  {
    return switchState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchState(SwitchState newSwitchState)
  {
    SwitchState oldSwitchState = switchState;
    switchState = newSwitchState == null ? SWITCH_STATE_EDEFAULT : newSwitchState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE, oldSwitchState, switchState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
	    setEnabledA(new AtomicBoolean());
		logger = LoggerFactory.getLogger(MIndustrialDigitalInImpl.class);
		inNum = Short.parseShort(String.valueOf(subId.charAt(subId.length() - 1)));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable()
  {
	  setSwitchState(SwitchState.OFF);
	  MBrickletIndustrialDigitalIn4 masterBrick = getMbrick();
	  if (masterBrick == null){
		  logger.error("{} No brick found for Digital4In: {} ", LoggerConstants.TFINIT, subId);
	  }
	  else {
		  BrickletIndustrialDigitalIn4 brickletIndustrialDigitalIn4 = masterBrick.getTinkerforgeDevice();
		  interruptListener = new InterruptListener();
		  brickletIndustrialDigitalIn4.addInterruptListener(interruptListener);
	  }
	  
  }

  /**
   *
   * @generated NOT
   */
  private class InterruptListener implements BrickletIndustrialDigitalIn4.InterruptListener {
	int mask = (0001 << (inNum - 1));
	@Override
	public void interrupt(int interruptMask, int valueMask) {
		if ((interruptMask & mask) == mask){
			if ((valueMask & mask) == mask){
				setSwitchState(SwitchState.ON);
			}
			else {
				setSwitchState(SwitchState.OFF);
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
	  BrickletIndustrialDigitalIn4 brickletIndustrialDigitalIn4 = getMbrick().getTinkerforgeDevice();
	  brickletIndustrialDigitalIn4.removeInterruptListener(interruptListener);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletIndustrialDigitalIn4)otherEnd, msgs);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        return getLogger();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        return getUid();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        return getSubId();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        return getMbrick();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE:
        return getSwitchState();
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        setMbrick((MBrickletIndustrialDigitalIn4)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE:
        setSwitchState((SwitchState)newValue);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        setMbrick((MBrickletIndustrialDigitalIn4)null);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE:
        return switchState != SWITCH_STATE_EDEFAULT;
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
    if (baseClass == MSwitchActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE: return ModelPackage.MSWITCH_ACTOR__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MOutSwitchActor.class)
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
    if (baseClass == MSwitchActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSWITCH_ACTOR__SWITCH_STATE: return ModelPackage.MINDUSTRIAL_DIGITAL_IN__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MOutSwitchActor.class)
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
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___INIT:
        init();
        return null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___ENABLE:
        enable();
        return null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___DISABLE:
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
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", subId: ");
    result.append(subId);
    result.append(", switchState: ");
    result.append(switchState);
    result.append(')');
    return result.toString();
  }

} //MIndustrialDigitalInImpl
