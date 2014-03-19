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
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletLCD20x4;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MLCD2 0x4 Button</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getButtonNum <em>Button Num</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl#getCallbackPeriod <em>Callback Period</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MLCD20x4ButtonImpl extends MinimalEObjectImpl.Container implements MLCD20x4Button
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
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "lcd_button";

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
   * The default value of the '{@link #getButtonNum() <em>Button Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getButtonNum()
   * @generated
   * @ordered
   */
  protected static final short BUTTON_NUM_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getButtonNum() <em>Button Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getButtonNum()
   * @generated
   * @ordered
   */
  protected short buttonNum = BUTTON_NUM_EDEFAULT;

  /**
   * The default value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected static final int CALLBACK_PERIOD_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected int callbackPeriod = CALLBACK_PERIOD_EDEFAULT;

private ButtonPressedListener buttonPressedListener;

private ButtonReleasedListener buttonReleasedListener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MLCD20x4ButtonImpl()
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
    return ModelPackage.Literals.MLCD2_0X4_BUTTON;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__SWITCH_STATE, oldSwitchState, switchState));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLCD20x4 getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.MLCD2_0X4_BUTTON__MBRICK) return null;
    return (MBrickletLCD20x4)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletLCD20x4 newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.MLCD2_0X4_BUTTON__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletLCD20x4 newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MLCD2_0X4_BUTTON__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__MBRICK, newMbrick, newMbrick));
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
  public short getButtonNum()
  {
    return buttonNum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setButtonNum(short newButtonNum)
  {
    short oldButtonNum = buttonNum;
    buttonNum = newButtonNum;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__BUTTON_NUM, oldButtonNum, buttonNum));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getCallbackPeriod()
  {
    return callbackPeriod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCallbackPeriod(int newCallbackPeriod)
  {
    int oldCallbackPeriod = callbackPeriod;
    callbackPeriod = newCallbackPeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MLCD2_0X4_BUTTON__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
	    setEnabledA(new AtomicBoolean());
		logger = LoggerFactory.getLogger(MLCD20x4ButtonImpl.class);
		buttonNum = Short.parseShort(String.valueOf(subId.charAt(subId.length() - 1)));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void enable() {
		setSwitchState(OnOffValue.UNDEF);
		MBrickletLCD20x4 masterBrick = getMbrick();
		if (masterBrick == null) {
			logger.error("{} No brick found for Button: {} ",
					LoggerConstants.TFINIT, subId);
		} else {
			BrickletLCD20x4 brickletLCD20x4 = masterBrick
					.getTinkerforgeDevice();
			buttonPressedListener = new ButtonPressedListener();
			brickletLCD20x4.addButtonPressedListener(buttonPressedListener);
			// buttonReleasedListener = new ButtonReleasedListener();
			// brickletLCD20x4.addButtonReleasedListener(buttonReleasedListener);
		}
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  private class ButtonPressedListener implements BrickletLCD20x4.ButtonPressedListener{

	@Override
	public void buttonPressed(short buttonChangedButtonNum) {
		if (buttonChangedButtonNum == buttonNum){
			if (switchState == OnOffValue.OFF){
				setSwitchState(OnOffValue.ON);
				logger.debug("set switch state on");
			}
			else if (switchState == OnOffValue.ON){
				setSwitchState(OnOffValue.OFF);
				logger.debug("set switch state on");
			}
			else {
				setSwitchState(OnOffValue.ON);
				logger.debug("set switch state on");				
			}
		}
	}
	  
  }
  
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
   private class ButtonReleasedListener implements  BrickletLCD20x4.ButtonReleasedListener{

	@Override
	public void buttonReleased(short arg0) {
		// TODO Auto-generated method stub
		
	}
	   
   }
   
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disable()
  {
	    MBrickletLCD20x4 masterBrick = getMbrick();
	    if (masterBrick == null){
	    	logger.error("{} disable: no brick found for Button: {} ", LoggerConstants.TFINIT, subId);
	    }
	    else {
	    	BrickletLCD20x4 brickletLCD20x4 = masterBrick.getTinkerforgeDevice();
	    	if (buttonPressedListener != null)
	    		brickletLCD20x4.removeButtonPressedListener(buttonPressedListener);
	    	if (buttonReleasedListener != null)
	    		brickletLCD20x4.removeButtonReleasedListener(buttonReleasedListener);
	    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void turnSwitch(OnOffValue state)
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue fetchSwitchState()
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
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
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletLCD20x4)otherEnd, msgs);
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
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
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
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
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
      case ModelPackage.MLCD2_0X4_BUTTON__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MLCD2_0X4_BUTTON__LOGGER:
        return getLogger();
      case ModelPackage.MLCD2_0X4_BUTTON__UID:
        return getUid();
      case ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MLCD2_0X4_BUTTON__SUB_ID:
        return getSubId();
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
        return getMbrick();
      case ModelPackage.MLCD2_0X4_BUTTON__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MLCD2_0X4_BUTTON__BUTTON_NUM:
        return getButtonNum();
      case ModelPackage.MLCD2_0X4_BUTTON__CALLBACK_PERIOD:
        return getCallbackPeriod();
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
      case ModelPackage.MLCD2_0X4_BUTTON__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
        setMbrick((MBrickletLCD20x4)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__BUTTON_NUM:
        setButtonNum((Short)newValue);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__CALLBACK_PERIOD:
        setCallbackPeriod((Integer)newValue);
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
      case ModelPackage.MLCD2_0X4_BUTTON__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
        setMbrick((MBrickletLCD20x4)null);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__BUTTON_NUM:
        setButtonNum(BUTTON_NUM_EDEFAULT);
        return;
      case ModelPackage.MLCD2_0X4_BUTTON__CALLBACK_PERIOD:
        setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
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
      case ModelPackage.MLCD2_0X4_BUTTON__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MLCD2_0X4_BUTTON__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MLCD2_0X4_BUTTON__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MLCD2_0X4_BUTTON__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MLCD2_0X4_BUTTON__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MLCD2_0X4_BUTTON__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MLCD2_0X4_BUTTON__BUTTON_NUM:
        return buttonNum != BUTTON_NUM_EDEFAULT;
      case ModelPackage.MLCD2_0X4_BUTTON__CALLBACK_PERIOD:
        return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
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
        case ModelPackage.MLCD2_0X4_BUTTON__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MLCD2_0X4_BUTTON__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MLCD2_0X4_BUTTON__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.MLCD2_0X4_BUTTON__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MLCDSubDevice.class)
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
    if (baseClass == MBaseDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MLCD2_0X4_BUTTON__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MLCD2_0X4_BUTTON__UID;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.MLCD2_0X4_BUTTON__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.MLCD2_0X4_BUTTON__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.MLCD2_0X4_BUTTON__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MLCDSubDevice.class)
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
    if (baseClass == MBaseDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.MLCD2_0X4_BUTTON___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.MLCD2_0X4_BUTTON___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.MLCD2_0X4_BUTTON___DISABLE;
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
    if (baseClass == MLCDSubDevice.class)
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
      case ModelPackage.MLCD2_0X4_BUTTON___INIT:
        init();
        return null;
      case ModelPackage.MLCD2_0X4_BUTTON___ENABLE:
        enable();
        return null;
      case ModelPackage.MLCD2_0X4_BUTTON___DISABLE:
        disable();
        return null;
      case ModelPackage.MLCD2_0X4_BUTTON___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.MLCD2_0X4_BUTTON___FETCH_SWITCH_STATE:
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
    result.append(", buttonNum: ");
    result.append(buttonNum);
    result.append(", callbackPeriod: ");
    result.append(callbackPeriod);
    result.append(')');
    return result.toString();
  }

} //MLCD20x4ButtonImpl
