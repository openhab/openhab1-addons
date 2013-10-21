/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model.util;

import com.tinkerforge.Device;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.openhab.binding.tinkerforge.internal.model.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage
 * @generated
 */
public class ModelSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ModelPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ModelPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case ModelPackage.TF_CONFIG:
      {
        TFConfig tfConfig = (TFConfig)theEObject;
        T result = caseTFConfig(tfConfig);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.OHTF_DEVICE:
      {
        OHTFDevice<?> ohtfDevice = (OHTFDevice<?>)theEObject;
        T result = caseOHTFDevice(ohtfDevice);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.OH_CONFIG:
      {
        OHConfig ohConfig = (OHConfig)theEObject;
        T result = caseOHConfig(ohConfig);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.ECOSYSTEM:
      {
        Ecosystem ecosystem = (Ecosystem)theEObject;
        T result = caseEcosystem(ecosystem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKD:
      {
        MBrickd mBrickd = (MBrickd)theEObject;
        T result = caseMBrickd(mBrickd);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MTF_CONFIG_CONSUMER:
      {
        MTFConfigConsumer<?> mtfConfigConsumer = (MTFConfigConsumer<?>)theEObject;
        T result = caseMTFConfigConsumer(mtfConfigConsumer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBASE_DEVICE:
      {
        MBaseDevice mBaseDevice = (MBaseDevice)theEObject;
        T result = caseMBaseDevice(mBaseDevice);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MDEVICE:
      {
        MDevice<?> mDevice = (MDevice<?>)theEObject;
        T result = caseMDevice(mDevice);
        if (result == null) result = caseMBaseDevice(mDevice);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MSUB_DEVICE_HOLDER:
      {
        MSubDeviceHolder<?> mSubDeviceHolder = (MSubDeviceHolder<?>)theEObject;
        T result = caseMSubDeviceHolder(mSubDeviceHolder);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICK_SERVO:
      {
        MBrickServo mBrickServo = (MBrickServo)theEObject;
        T result = caseMBrickServo(mBrickServo);
        if (result == null) result = caseMDevice(mBrickServo);
        if (result == null) result = caseMSubDeviceHolder(mBrickServo);
        if (result == null) result = caseMBaseDevice(mBrickServo);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.TF_BRICK_DC_CONFIGURATION:
      {
        TFBrickDCConfiguration tfBrickDCConfiguration = (TFBrickDCConfiguration)theEObject;
        T result = caseTFBrickDCConfiguration(tfBrickDCConfiguration);
        if (result == null) result = caseTFConfig(tfBrickDCConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICK_DC:
      {
        MBrickDC mBrickDC = (MBrickDC)theEObject;
        T result = caseMBrickDC(mBrickDC);
        if (result == null) result = caseMInSwitchActor(mBrickDC);
        if (result == null) result = caseMDevice(mBrickDC);
        if (result == null) result = caseMTFConfigConsumer(mBrickDC);
        if (result == null) result = caseMSwitchActor(mBrickDC);
        if (result == null) result = caseMBaseDevice(mBrickDC);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MDUAL_RELAY_BRICKLET:
      {
        MDualRelayBricklet mDualRelayBricklet = (MDualRelayBricklet)theEObject;
        T result = caseMDualRelayBricklet(mDualRelayBricklet);
        if (result == null) result = caseMDevice(mDualRelayBricklet);
        if (result == null) result = caseMSubDeviceHolder(mDualRelayBricklet);
        if (result == null) result = caseMBaseDevice(mDualRelayBricklet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY_BRICKLET:
      {
        MIndustrialQuadRelayBricklet mIndustrialQuadRelayBricklet = (MIndustrialQuadRelayBricklet)theEObject;
        T result = caseMIndustrialQuadRelayBricklet(mIndustrialQuadRelayBricklet);
        if (result == null) result = caseMDevice(mIndustrialQuadRelayBricklet);
        if (result == null) result = caseMSubDeviceHolder(mIndustrialQuadRelayBricklet);
        if (result == null) result = caseMBaseDevice(mIndustrialQuadRelayBricklet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY:
      {
        MIndustrialQuadRelay mIndustrialQuadRelay = (MIndustrialQuadRelay)theEObject;
        T result = caseMIndustrialQuadRelay(mIndustrialQuadRelay);
        if (result == null) result = caseMInSwitchActor(mIndustrialQuadRelay);
        if (result == null) result = caseMSubDevice(mIndustrialQuadRelay);
        if (result == null) result = caseMSwitchActor(mIndustrialQuadRelay);
        if (result == null) result = caseMBaseDevice(mIndustrialQuadRelay);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_INDUSTRIAL_DIGITAL_IN4:
      {
        MBrickletIndustrialDigitalIn4 mBrickletIndustrialDigitalIn4 = (MBrickletIndustrialDigitalIn4)theEObject;
        T result = caseMBrickletIndustrialDigitalIn4(mBrickletIndustrialDigitalIn4);
        if (result == null) result = caseMSubDeviceHolder(mBrickletIndustrialDigitalIn4);
        if (result == null) result = caseMDevice(mBrickletIndustrialDigitalIn4);
        if (result == null) result = caseMBaseDevice(mBrickletIndustrialDigitalIn4);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN:
      {
        MIndustrialDigitalIn mIndustrialDigitalIn = (MIndustrialDigitalIn)theEObject;
        T result = caseMIndustrialDigitalIn(mIndustrialDigitalIn);
        if (result == null) result = caseMSubDevice(mIndustrialDigitalIn);
        if (result == null) result = caseMOutSwitchActor(mIndustrialDigitalIn);
        if (result == null) result = caseMBaseDevice(mIndustrialDigitalIn);
        if (result == null) result = caseMSwitchActor(mIndustrialDigitalIn);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MACTOR:
      {
        MActor mActor = (MActor)theEObject;
        T result = caseMActor(mActor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MSWITCH_ACTOR:
      {
        MSwitchActor mSwitchActor = (MSwitchActor)theEObject;
        T result = caseMSwitchActor(mSwitchActor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MOUT_SWITCH_ACTOR:
      {
        MOutSwitchActor mOutSwitchActor = (MOutSwitchActor)theEObject;
        T result = caseMOutSwitchActor(mOutSwitchActor);
        if (result == null) result = caseMSwitchActor(mOutSwitchActor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MIN_SWITCH_ACTOR:
      {
        MInSwitchActor mInSwitchActor = (MInSwitchActor)theEObject;
        T result = caseMInSwitchActor(mInSwitchActor);
        if (result == null) result = caseMSwitchActor(mInSwitchActor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MSUB_DEVICE:
      {
        MSubDevice<?> mSubDevice = (MSubDevice<?>)theEObject;
        T result = caseMSubDevice(mSubDevice);
        if (result == null) result = caseMBaseDevice(mSubDevice);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MDUAL_RELAY:
      {
        MDualRelay mDualRelay = (MDualRelay)theEObject;
        T result = caseMDualRelay(mDualRelay);
        if (result == null) result = caseMInSwitchActor(mDualRelay);
        if (result == null) result = caseMSubDevice(mDualRelay);
        if (result == null) result = caseMSwitchActor(mDualRelay);
        if (result == null) result = caseMBaseDevice(mDualRelay);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.TF_NULL_CONFIGURATION:
      {
        TFNullConfiguration tfNullConfiguration = (TFNullConfiguration)theEObject;
        T result = caseTFNullConfiguration(tfNullConfiguration);
        if (result == null) result = caseTFConfig(tfNullConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.TF_SERVO_CONFIGURATION:
      {
        TFServoConfiguration tfServoConfiguration = (TFServoConfiguration)theEObject;
        T result = caseTFServoConfiguration(tfServoConfiguration);
        if (result == null) result = caseTFConfig(tfServoConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MSERVO:
      {
        MServo mServo = (MServo)theEObject;
        T result = caseMServo(mServo);
        if (result == null) result = caseMInSwitchActor(mServo);
        if (result == null) result = caseMSubDevice(mServo);
        if (result == null) result = caseMTFConfigConsumer(mServo);
        if (result == null) result = caseMSwitchActor(mServo);
        if (result == null) result = caseMBaseDevice(mServo);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.CALLBACK_LISTENER:
      {
        CallbackListener callbackListener = (CallbackListener)theEObject;
        T result = caseCallbackListener(callbackListener);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MSENSOR:
      {
        MSensor<?> mSensor = (MSensor<?>)theEObject;
        T result = caseMSensor(mSensor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_HUMIDITY:
      {
        MBrickletHumidity mBrickletHumidity = (MBrickletHumidity)theEObject;
        T result = caseMBrickletHumidity(mBrickletHumidity);
        if (result == null) result = caseMSensor(mBrickletHumidity);
        if (result == null) result = caseMDevice(mBrickletHumidity);
        if (result == null) result = caseMTFConfigConsumer(mBrickletHumidity);
        if (result == null) result = caseCallbackListener(mBrickletHumidity);
        if (result == null) result = caseMBaseDevice(mBrickletHumidity);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_DISTANCE_IR:
      {
        MBrickletDistanceIR mBrickletDistanceIR = (MBrickletDistanceIR)theEObject;
        T result = caseMBrickletDistanceIR(mBrickletDistanceIR);
        if (result == null) result = caseMDevice(mBrickletDistanceIR);
        if (result == null) result = caseMSensor(mBrickletDistanceIR);
        if (result == null) result = caseMTFConfigConsumer(mBrickletDistanceIR);
        if (result == null) result = caseCallbackListener(mBrickletDistanceIR);
        if (result == null) result = caseMBaseDevice(mBrickletDistanceIR);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_TEMPERATURE:
      {
        MBrickletTemperature mBrickletTemperature = (MBrickletTemperature)theEObject;
        T result = caseMBrickletTemperature(mBrickletTemperature);
        if (result == null) result = caseMDevice(mBrickletTemperature);
        if (result == null) result = caseMSensor(mBrickletTemperature);
        if (result == null) result = caseMTFConfigConsumer(mBrickletTemperature);
        if (result == null) result = caseCallbackListener(mBrickletTemperature);
        if (result == null) result = caseMBaseDevice(mBrickletTemperature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.TF_BASE_CONFIGURATION:
      {
        TFBaseConfiguration tfBaseConfiguration = (TFBaseConfiguration)theEObject;
        T result = caseTFBaseConfiguration(tfBaseConfiguration);
        if (result == null) result = caseTFConfig(tfBaseConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_BAROMETER:
      {
        MBrickletBarometer mBrickletBarometer = (MBrickletBarometer)theEObject;
        T result = caseMBrickletBarometer(mBrickletBarometer);
        if (result == null) result = caseMDevice(mBrickletBarometer);
        if (result == null) result = caseMSensor(mBrickletBarometer);
        if (result == null) result = caseMTFConfigConsumer(mBrickletBarometer);
        if (result == null) result = caseMSubDeviceHolder(mBrickletBarometer);
        if (result == null) result = caseCallbackListener(mBrickletBarometer);
        if (result == null) result = caseMBaseDevice(mBrickletBarometer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBAROMETER_TEMPERATURE:
      {
        MBarometerTemperature mBarometerTemperature = (MBarometerTemperature)theEObject;
        T result = caseMBarometerTemperature(mBarometerTemperature);
        if (result == null) result = caseMSensor(mBarometerTemperature);
        if (result == null) result = caseMSubDevice(mBarometerTemperature);
        if (result == null) result = caseMBaseDevice(mBarometerTemperature);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT:
      {
        MBrickletAmbientLight mBrickletAmbientLight = (MBrickletAmbientLight)theEObject;
        T result = caseMBrickletAmbientLight(mBrickletAmbientLight);
        if (result == null) result = caseMDevice(mBrickletAmbientLight);
        if (result == null) result = caseMSensor(mBrickletAmbientLight);
        if (result == null) result = caseMTFConfigConsumer(mBrickletAmbientLight);
        if (result == null) result = caseCallbackListener(mBrickletAmbientLight);
        if (result == null) result = caseMBaseDevice(mBrickletAmbientLight);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MBRICKLET_LCD2_0X4:
      {
        MBrickletLCD20x4 mBrickletLCD20x4 = (MBrickletLCD20x4)theEObject;
        T result = caseMBrickletLCD20x4(mBrickletLCD20x4);
        if (result == null) result = caseMDevice(mBrickletLCD20x4);
        if (result == null) result = caseMTextActor(mBrickletLCD20x4);
        if (result == null) result = caseMInSwitchActor(mBrickletLCD20x4);
        if (result == null) result = caseMSubDeviceHolder(mBrickletLCD20x4);
        if (result == null) result = caseMBaseDevice(mBrickletLCD20x4);
        if (result == null) result = caseMSwitchActor(mBrickletLCD20x4);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MTEXT_ACTOR:
      {
        MTextActor mTextActor = (MTextActor)theEObject;
        T result = caseMTextActor(mTextActor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ModelPackage.MLCD2_0X4_BUTTON:
      {
        MLCD20x4Button mlcd20x4Button = (MLCD20x4Button)theEObject;
        T result = caseMLCD20x4Button(mlcd20x4Button);
        if (result == null) result = caseMOutSwitchActor(mlcd20x4Button);
        if (result == null) result = caseMSubDevice(mlcd20x4Button);
        if (result == null) result = caseMSwitchActor(mlcd20x4Button);
        if (result == null) result = caseMBaseDevice(mlcd20x4Button);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TF Config</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TF Config</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTFConfig(TFConfig object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>OHTF Device</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>OHTF Device</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <TFC extends TFConfig> T caseOHTFDevice(OHTFDevice<TFC> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>OH Config</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>OH Config</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOHConfig(OHConfig object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Ecosystem</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Ecosystem</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEcosystem(Ecosystem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBrickd</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBrickd</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickd(MBrickd object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MTF Config Consumer</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MTF Config Consumer</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <TFC> T caseMTFConfigConsumer(MTFConfigConsumer<TFC> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBase Device</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBase Device</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBaseDevice(MBaseDevice object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MDevice</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MDevice</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <TF extends Device> T caseMDevice(MDevice<TF> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MSub Device Holder</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MSub Device Holder</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <S extends MSubDevice<?>> T caseMSubDeviceHolder(MSubDeviceHolder<S> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBrick Servo</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBrick Servo</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickServo(MBrickServo object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TF Brick DC Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TF Brick DC Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTFBrickDCConfiguration(TFBrickDCConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBrick DC</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBrick DC</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickDC(MBrickDC object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MDual Relay Bricklet</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MDual Relay Bricklet</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMDualRelayBricklet(MDualRelayBricklet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MIndustrial Quad Relay Bricklet</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MIndustrial Quad Relay Bricklet</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMIndustrialQuadRelayBricklet(MIndustrialQuadRelayBricklet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MIndustrial Quad Relay</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MIndustrial Quad Relay</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMIndustrialQuadRelay(MIndustrialQuadRelay object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Industrial Digital In4</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Industrial Digital In4</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletIndustrialDigitalIn4(MBrickletIndustrialDigitalIn4 object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MIndustrial Digital In</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MIndustrial Digital In</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMIndustrialDigitalIn(MIndustrialDigitalIn object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MActor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MActor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMActor(MActor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MSwitch Actor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MSwitch Actor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMSwitchActor(MSwitchActor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MOut Switch Actor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MOut Switch Actor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMOutSwitchActor(MOutSwitchActor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MIn Switch Actor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MIn Switch Actor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMInSwitchActor(MInSwitchActor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MSub Device</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MSub Device</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <B extends MSubDeviceHolder<?>> T caseMSubDevice(MSubDevice<B> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MDual Relay</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MDual Relay</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMDualRelay(MDualRelay object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TF Null Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TF Null Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTFNullConfiguration(TFNullConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TF Servo Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TF Servo Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTFServoConfiguration(TFServoConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MServo</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MServo</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMServo(MServo object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Callback Listener</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Callback Listener</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCallbackListener(CallbackListener object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MSensor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MSensor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public <ValueType> T caseMSensor(MSensor<ValueType> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Humidity</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Humidity</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletHumidity(MBrickletHumidity object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Distance IR</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Distance IR</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletDistanceIR(MBrickletDistanceIR object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Temperature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Temperature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletTemperature(MBrickletTemperature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TF Base Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TF Base Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTFBaseConfiguration(TFBaseConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Barometer</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Barometer</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletBarometer(MBrickletBarometer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBarometer Temperature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBarometer Temperature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBarometerTemperature(MBarometerTemperature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet Ambient Light</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet Ambient Light</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletAmbientLight(MBrickletAmbientLight object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MBricklet LCD2 0x4</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MBricklet LCD2 0x4</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMBrickletLCD20x4(MBrickletLCD20x4 object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MText Actor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MText Actor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMTextActor(MTextActor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MLCD2 0x4 Button</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MLCD2 0x4 Button</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMLCD20x4Button(MLCD20x4Button object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //ModelSwitch
