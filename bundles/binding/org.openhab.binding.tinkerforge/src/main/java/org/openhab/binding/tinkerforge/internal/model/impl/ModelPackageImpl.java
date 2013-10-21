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
package org.openhab.binding.tinkerforge.internal.model.impl;

import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.BrickletIndustrialQuadRelay;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletTemperature;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.DCDriveMode;
import org.openhab.binding.tinkerforge.internal.model.DigitalState;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.MActor;
import org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight;
import org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR;
import org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MDualRelay;
import org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MInSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.SwitchState;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.slf4j.Logger;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelPackageImpl extends EPackageImpl implements ModelPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfConfigEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ohtfDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ohConfigEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ecosystemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickdEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mtfConfigConsumerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBaseDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mSubDeviceHolderEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickServoEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfBrickDCConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickDCEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mDualRelayBrickletEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mIndustrialQuadRelayBrickletEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mIndustrialQuadRelayEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletIndustrialDigitalIn4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mIndustrialDigitalInEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mSwitchActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mOutSwitchActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mInSwitchActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mSubDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mDualRelayEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfNullConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfServoConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mServoEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass callbackListenerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mSensorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletHumidityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletDistanceIREClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletTemperatureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfBaseConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletBarometerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBarometerTemperatureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletAmbientLightEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletLCD20x4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mTextActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mlcd20x4ButtonEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum switchStateEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum digitalStateEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum dcDriveModeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mipConnectionEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerDeviceEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mLoggerEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mAtomicBooleanEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerforgeDeviceEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickDCEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickServoEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletHumidityEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletDistanceIREDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletTemperatureEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletBarometerEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletAmbientLightEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletLCD20x4EDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletDualRelayEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletIndustrialQuadRelayEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletIndustrialDigitalIn4EDataType = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ModelPackageImpl()
  {
    super(eNS_URI, ModelFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link ModelPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ModelPackage init()
  {
    if (isInited) return (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

    // Obtain or create and register package
    ModelPackageImpl theModelPackage = (ModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ModelPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    EcorePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theModelPackage.createPackageContents();

    // Initialize created meta-data
    theModelPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theModelPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, theModelPackage);
    return theModelPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFConfig()
  {
    return tfConfigEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOHTFDevice()
  {
    return ohtfDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOHTFDevice_Uid()
  {
    return (EAttribute)ohtfDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOHTFDevice_Subid()
  {
    return (EAttribute)ohtfDeviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOHTFDevice_Ohid()
  {
    return (EAttribute)ohtfDeviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOHTFDevice_TfConfig()
  {
    return (EReference)ohtfDeviceEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOHTFDevice_OhConfig()
  {
    return (EReference)ohtfDeviceEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOHConfig()
  {
    return ohConfigEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOHConfig_OhTfDevices()
  {
    return (EReference)ohConfigEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getOHConfig__GetConfigByTFId__String_String()
  {
    return ohConfigEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getOHConfig__GetConfigByOHId__String()
  {
    return ohConfigEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getEcosystem()
  {
    return ecosystemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEcosystem_Logger()
  {
    return (EAttribute)ecosystemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getEcosystem_Mbrickds()
  {
    return (EReference)ecosystemEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getEcosystem__GetBrickd__String_int()
  {
    return ecosystemEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getEcosystem__GetDevice__String_String()
  {
    return ecosystemEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getEcosystem__Disconnect()
  {
    return ecosystemEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickd()
  {
    return mBrickdEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_Logger()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_IpConnection()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_Host()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_Port()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_IsConnected()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_AutoReconnect()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_Timeout()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMBrickd_Mdevices()
  {
    return (EReference)mBrickdEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMBrickd_Ecosystem()
  {
    return (EReference)mBrickdEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickd__Connect()
  {
    return mBrickdEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickd__Disconnect()
  {
    return mBrickdEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickd__Init()
  {
    return mBrickdEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickd__GetDevice__String()
  {
    return mBrickdEClass.getEOperations().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMTFConfigConsumer()
  {
    return mtfConfigConsumerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMTFConfigConsumer_TfConfig()
  {
    return (EReference)mtfConfigConsumerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBaseDevice()
  {
    return mBaseDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBaseDevice_Logger()
  {
    return (EAttribute)mBaseDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBaseDevice_Uid()
  {
    return (EAttribute)mBaseDeviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBaseDevice_EnabledA()
  {
    return (EAttribute)mBaseDeviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBaseDevice__Init()
  {
    return mBaseDeviceEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBaseDevice__Enable()
  {
    return mBaseDeviceEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBaseDevice__Disable()
  {
    return mBaseDeviceEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMDevice()
  {
    return mDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_TinkerforgeDevice()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_IpConnection()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_ConnectedUid()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_Position()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_DeviceIdentifier()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDevice_Name()
  {
    return (EAttribute)mDeviceEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMDevice_Brickd()
  {
    return (EReference)mDeviceEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMSubDeviceHolder()
  {
    return mSubDeviceHolderEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMSubDeviceHolder_Msubdevices()
  {
    return (EReference)mSubDeviceHolderEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMSubDeviceHolder__InitSubDevices()
  {
    return mSubDeviceHolderEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickServo()
  {
    return mBrickServoEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickServo_DeviceType()
  {
    return (EAttribute)mBrickServoEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickServo__Init()
  {
    return mBrickServoEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFBrickDCConfiguration()
  {
    return tfBrickDCConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBrickDCConfiguration_Velocity()
  {
    return (EAttribute)tfBrickDCConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBrickDCConfiguration_Acceleration()
  {
    return (EAttribute)tfBrickDCConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBrickDCConfiguration_PwmFrequency()
  {
    return (EAttribute)tfBrickDCConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBrickDCConfiguration_DriveMode()
  {
    return (EAttribute)tfBrickDCConfigurationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBrickDCConfiguration_SwitchOnVelocity()
  {
    return (EAttribute)tfBrickDCConfigurationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickDC()
  {
    return mBrickDCEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_DeviceType()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_Velocity()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_CurrentVelocity()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_Acceleration()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_PwmFrequency()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_DriveMode()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickDC_SwitchOnVelocity()
  {
    return (EAttribute)mBrickDCEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickDC__Init()
  {
    return mBrickDCEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMDualRelayBricklet()
  {
    return mDualRelayBrickletEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDualRelayBricklet_DeviceType()
  {
    return (EAttribute)mDualRelayBrickletEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMIndustrialQuadRelayBricklet()
  {
    return mIndustrialQuadRelayBrickletEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMIndustrialQuadRelay()
  {
    return mIndustrialQuadRelayEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMIndustrialQuadRelay_DeviceType()
  {
    return (EAttribute)mIndustrialQuadRelayEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletIndustrialDigitalIn4()
  {
    return mBrickletIndustrialDigitalIn4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMIndustrialDigitalIn()
  {
    return mIndustrialDigitalInEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMActor()
  {
    return mActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMSwitchActor()
  {
    return mSwitchActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMSwitchActor_SwitchState()
  {
    return (EAttribute)mSwitchActorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMOutSwitchActor()
  {
    return mOutSwitchActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMInSwitchActor()
  {
    return mInSwitchActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMSubDevice()
  {
    return mSubDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMSubDevice_SubId()
  {
    return (EAttribute)mSubDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMSubDevice_Mbrick()
  {
    return (EReference)mSubDeviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMDualRelay()
  {
    return mDualRelayEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMDualRelay_DeviceType()
  {
    return (EAttribute)mDualRelayEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFNullConfiguration()
  {
    return tfNullConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFServoConfiguration()
  {
    return tfServoConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_Velocity()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_Acceleration()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_ServoVoltage()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_PulseWidthMin()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_PulseWidthMax()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_Period()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFServoConfiguration_OutputVoltage()
  {
    return (EAttribute)tfServoConfigurationEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMServo()
  {
    return mServoEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_DeviceType()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_Velocity()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_Acceleration()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_PulseWidthMin()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_PulseWidthMax()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_Period()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_OutputVoltage()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_ServoCurrentPosition()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMServo_ServoDestinationPosition()
  {
    return (EAttribute)mServoEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMServo__Init()
  {
    return mServoEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCallbackListener()
  {
    return callbackListenerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getCallbackListener_CallbackPeriod()
  {
    return (EAttribute)callbackListenerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMSensor()
  {
    return mSensorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMSensor_SensorValue()
  {
    return (EAttribute)mSensorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMSensor__FetchSensorValue()
  {
    return mSensorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletHumidity()
  {
    return mBrickletHumidityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletHumidity_DeviceType()
  {
    return (EAttribute)mBrickletHumidityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletHumidity_Humiditiy()
  {
    return (EAttribute)mBrickletHumidityEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletHumidity_Threshold()
  {
    return (EAttribute)mBrickletHumidityEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletHumidity__Init()
  {
    return mBrickletHumidityEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletDistanceIR()
  {
    return mBrickletDistanceIREClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceIR_DeviceType()
  {
    return (EAttribute)mBrickletDistanceIREClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceIR_Distance()
  {
    return (EAttribute)mBrickletDistanceIREClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceIR_Threshold()
  {
    return (EAttribute)mBrickletDistanceIREClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletDistanceIR__Init()
  {
    return mBrickletDistanceIREClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletTemperature()
  {
    return mBrickletTemperatureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletTemperature_DeviceType()
  {
    return (EAttribute)mBrickletTemperatureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletTemperature_Temperature()
  {
    return (EAttribute)mBrickletTemperatureEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletTemperature_Threshold()
  {
    return (EAttribute)mBrickletTemperatureEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletTemperature__Init()
  {
    return mBrickletTemperatureEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFBaseConfiguration()
  {
    return tfBaseConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBaseConfiguration_Threshold()
  {
    return (EAttribute)tfBaseConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFBaseConfiguration_CallbackPeriod()
  {
    return (EAttribute)tfBaseConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletBarometer()
  {
    return mBrickletBarometerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletBarometer_DeviceType()
  {
    return (EAttribute)mBrickletBarometerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletBarometer_AirPressure()
  {
    return (EAttribute)mBrickletBarometerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletBarometer_Threshold()
  {
    return (EAttribute)mBrickletBarometerEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletBarometer__Init()
  {
    return mBrickletBarometerEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBarometerTemperature()
  {
    return mBarometerTemperatureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBarometerTemperature_DeviceType()
  {
    return (EAttribute)mBarometerTemperatureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBarometerTemperature_Temperature()
  {
    return (EAttribute)mBarometerTemperatureEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBarometerTemperature__Init()
  {
    return mBarometerTemperatureEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletAmbientLight()
  {
    return mBrickletAmbientLightEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletAmbientLight_DeviceType()
  {
    return (EAttribute)mBrickletAmbientLightEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletAmbientLight_Illuminance()
  {
    return (EAttribute)mBrickletAmbientLightEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletAmbientLight_Threshold()
  {
    return (EAttribute)mBrickletAmbientLightEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletAmbientLight__Init()
  {
    return mBrickletAmbientLightEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletLCD20x4()
  {
    return mBrickletLCD20x4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletLCD20x4_DeviceType()
  {
    return (EAttribute)mBrickletLCD20x4EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletLCD20x4_PositionPrefix()
  {
    return (EAttribute)mBrickletLCD20x4EClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletLCD20x4_PositonSuffix()
  {
    return (EAttribute)mBrickletLCD20x4EClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletLCD20x4_DisplayErrors()
  {
    return (EAttribute)mBrickletLCD20x4EClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletLCD20x4_ErrorPrefix()
  {
    return (EAttribute)mBrickletLCD20x4EClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletLCD20x4__Init()
  {
    return mBrickletLCD20x4EClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMTextActor()
  {
    return mTextActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMTextActor_Text()
  {
    return (EAttribute)mTextActorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMLCD20x4Button()
  {
    return mlcd20x4ButtonEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMLCD20x4Button_DeviceType()
  {
    return (EAttribute)mlcd20x4ButtonEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMLCD20x4Button_ButtonNum()
  {
    return (EAttribute)mlcd20x4ButtonEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMLCD20x4Button_CallbackPeriod()
  {
    return (EAttribute)mlcd20x4ButtonEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getSwitchState()
  {
    return switchStateEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getDigitalState()
  {
    return digitalStateEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getDCDriveMode()
  {
    return dcDriveModeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMIPConnection()
  {
    return mipConnectionEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerDevice()
  {
    return mTinkerDeviceEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMLogger()
  {
    return mLoggerEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMAtomicBoolean()
  {
    return mAtomicBooleanEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerforgeDevice()
  {
    return mTinkerforgeDeviceEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickDC()
  {
    return mTinkerBrickDCEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickServo()
  {
    return mTinkerBrickServoEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletHumidity()
  {
    return mTinkerBrickletHumidityEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletDistanceIR()
  {
    return mTinkerBrickletDistanceIREDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletTemperature()
  {
    return mTinkerBrickletTemperatureEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletBarometer()
  {
    return mTinkerBrickletBarometerEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletAmbientLight()
  {
    return mTinkerBrickletAmbientLightEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletLCD20x4()
  {
    return mTinkerBrickletLCD20x4EDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletDualRelay()
  {
    return mTinkerBrickletDualRelayEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletIndustrialQuadRelay()
  {
    return mTinkerBrickletIndustrialQuadRelayEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMTinkerBrickletIndustrialDigitalIn4()
  {
    return mTinkerBrickletIndustrialDigitalIn4EDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelFactory getModelFactory()
  {
    return (ModelFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    tfConfigEClass = createEClass(TF_CONFIG);

    ohtfDeviceEClass = createEClass(OHTF_DEVICE);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__UID);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__SUBID);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__OHID);
    createEReference(ohtfDeviceEClass, OHTF_DEVICE__TF_CONFIG);
    createEReference(ohtfDeviceEClass, OHTF_DEVICE__OH_CONFIG);

    ohConfigEClass = createEClass(OH_CONFIG);
    createEReference(ohConfigEClass, OH_CONFIG__OH_TF_DEVICES);
    createEOperation(ohConfigEClass, OH_CONFIG___GET_CONFIG_BY_TF_ID__STRING_STRING);
    createEOperation(ohConfigEClass, OH_CONFIG___GET_CONFIG_BY_OH_ID__STRING);

    ecosystemEClass = createEClass(ECOSYSTEM);
    createEAttribute(ecosystemEClass, ECOSYSTEM__LOGGER);
    createEReference(ecosystemEClass, ECOSYSTEM__MBRICKDS);
    createEOperation(ecosystemEClass, ECOSYSTEM___GET_BRICKD__STRING_INT);
    createEOperation(ecosystemEClass, ECOSYSTEM___GET_DEVICE__STRING_STRING);
    createEOperation(ecosystemEClass, ECOSYSTEM___DISCONNECT);

    mBrickdEClass = createEClass(MBRICKD);
    createEAttribute(mBrickdEClass, MBRICKD__LOGGER);
    createEAttribute(mBrickdEClass, MBRICKD__IP_CONNECTION);
    createEAttribute(mBrickdEClass, MBRICKD__HOST);
    createEAttribute(mBrickdEClass, MBRICKD__PORT);
    createEAttribute(mBrickdEClass, MBRICKD__IS_CONNECTED);
    createEAttribute(mBrickdEClass, MBRICKD__AUTO_RECONNECT);
    createEAttribute(mBrickdEClass, MBRICKD__TIMEOUT);
    createEReference(mBrickdEClass, MBRICKD__MDEVICES);
    createEReference(mBrickdEClass, MBRICKD__ECOSYSTEM);
    createEOperation(mBrickdEClass, MBRICKD___CONNECT);
    createEOperation(mBrickdEClass, MBRICKD___DISCONNECT);
    createEOperation(mBrickdEClass, MBRICKD___INIT);
    createEOperation(mBrickdEClass, MBRICKD___GET_DEVICE__STRING);

    mtfConfigConsumerEClass = createEClass(MTF_CONFIG_CONSUMER);
    createEReference(mtfConfigConsumerEClass, MTF_CONFIG_CONSUMER__TF_CONFIG);

    mBaseDeviceEClass = createEClass(MBASE_DEVICE);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__LOGGER);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__UID);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__ENABLED_A);
    createEOperation(mBaseDeviceEClass, MBASE_DEVICE___INIT);
    createEOperation(mBaseDeviceEClass, MBASE_DEVICE___ENABLE);
    createEOperation(mBaseDeviceEClass, MBASE_DEVICE___DISABLE);

    mDeviceEClass = createEClass(MDEVICE);
    createEAttribute(mDeviceEClass, MDEVICE__TINKERFORGE_DEVICE);
    createEAttribute(mDeviceEClass, MDEVICE__IP_CONNECTION);
    createEAttribute(mDeviceEClass, MDEVICE__CONNECTED_UID);
    createEAttribute(mDeviceEClass, MDEVICE__POSITION);
    createEAttribute(mDeviceEClass, MDEVICE__DEVICE_IDENTIFIER);
    createEAttribute(mDeviceEClass, MDEVICE__NAME);
    createEReference(mDeviceEClass, MDEVICE__BRICKD);

    mSubDeviceHolderEClass = createEClass(MSUB_DEVICE_HOLDER);
    createEReference(mSubDeviceHolderEClass, MSUB_DEVICE_HOLDER__MSUBDEVICES);
    createEOperation(mSubDeviceHolderEClass, MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES);

    mBrickServoEClass = createEClass(MBRICK_SERVO);
    createEAttribute(mBrickServoEClass, MBRICK_SERVO__DEVICE_TYPE);
    createEOperation(mBrickServoEClass, MBRICK_SERVO___INIT);

    tfBrickDCConfigurationEClass = createEClass(TF_BRICK_DC_CONFIGURATION);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__VELOCITY);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__ACCELERATION);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__DRIVE_MODE);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY);

    mBrickDCEClass = createEClass(MBRICK_DC);
    createEAttribute(mBrickDCEClass, MBRICK_DC__DEVICE_TYPE);
    createEAttribute(mBrickDCEClass, MBRICK_DC__VELOCITY);
    createEAttribute(mBrickDCEClass, MBRICK_DC__CURRENT_VELOCITY);
    createEAttribute(mBrickDCEClass, MBRICK_DC__ACCELERATION);
    createEAttribute(mBrickDCEClass, MBRICK_DC__PWM_FREQUENCY);
    createEAttribute(mBrickDCEClass, MBRICK_DC__DRIVE_MODE);
    createEAttribute(mBrickDCEClass, MBRICK_DC__SWITCH_ON_VELOCITY);
    createEOperation(mBrickDCEClass, MBRICK_DC___INIT);

    mDualRelayBrickletEClass = createEClass(MDUAL_RELAY_BRICKLET);
    createEAttribute(mDualRelayBrickletEClass, MDUAL_RELAY_BRICKLET__DEVICE_TYPE);

    mIndustrialQuadRelayBrickletEClass = createEClass(MINDUSTRIAL_QUAD_RELAY_BRICKLET);

    mIndustrialQuadRelayEClass = createEClass(MINDUSTRIAL_QUAD_RELAY);
    createEAttribute(mIndustrialQuadRelayEClass, MINDUSTRIAL_QUAD_RELAY__DEVICE_TYPE);

    mBrickletIndustrialDigitalIn4EClass = createEClass(MBRICKLET_INDUSTRIAL_DIGITAL_IN4);

    mIndustrialDigitalInEClass = createEClass(MINDUSTRIAL_DIGITAL_IN);

    mActorEClass = createEClass(MACTOR);

    mSwitchActorEClass = createEClass(MSWITCH_ACTOR);
    createEAttribute(mSwitchActorEClass, MSWITCH_ACTOR__SWITCH_STATE);

    mOutSwitchActorEClass = createEClass(MOUT_SWITCH_ACTOR);

    mInSwitchActorEClass = createEClass(MIN_SWITCH_ACTOR);

    mSubDeviceEClass = createEClass(MSUB_DEVICE);
    createEAttribute(mSubDeviceEClass, MSUB_DEVICE__SUB_ID);
    createEReference(mSubDeviceEClass, MSUB_DEVICE__MBRICK);

    mDualRelayEClass = createEClass(MDUAL_RELAY);
    createEAttribute(mDualRelayEClass, MDUAL_RELAY__DEVICE_TYPE);

    tfNullConfigurationEClass = createEClass(TF_NULL_CONFIGURATION);

    tfServoConfigurationEClass = createEClass(TF_SERVO_CONFIGURATION);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__VELOCITY);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__ACCELERATION);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__SERVO_VOLTAGE);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PERIOD);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE);

    mServoEClass = createEClass(MSERVO);
    createEAttribute(mServoEClass, MSERVO__DEVICE_TYPE);
    createEAttribute(mServoEClass, MSERVO__VELOCITY);
    createEAttribute(mServoEClass, MSERVO__ACCELERATION);
    createEAttribute(mServoEClass, MSERVO__PULSE_WIDTH_MIN);
    createEAttribute(mServoEClass, MSERVO__PULSE_WIDTH_MAX);
    createEAttribute(mServoEClass, MSERVO__PERIOD);
    createEAttribute(mServoEClass, MSERVO__OUTPUT_VOLTAGE);
    createEAttribute(mServoEClass, MSERVO__SERVO_CURRENT_POSITION);
    createEAttribute(mServoEClass, MSERVO__SERVO_DESTINATION_POSITION);
    createEOperation(mServoEClass, MSERVO___INIT);

    callbackListenerEClass = createEClass(CALLBACK_LISTENER);
    createEAttribute(callbackListenerEClass, CALLBACK_LISTENER__CALLBACK_PERIOD);

    mSensorEClass = createEClass(MSENSOR);
    createEAttribute(mSensorEClass, MSENSOR__SENSOR_VALUE);
    createEOperation(mSensorEClass, MSENSOR___FETCH_SENSOR_VALUE);

    mBrickletHumidityEClass = createEClass(MBRICKLET_HUMIDITY);
    createEAttribute(mBrickletHumidityEClass, MBRICKLET_HUMIDITY__DEVICE_TYPE);
    createEAttribute(mBrickletHumidityEClass, MBRICKLET_HUMIDITY__HUMIDITIY);
    createEAttribute(mBrickletHumidityEClass, MBRICKLET_HUMIDITY__THRESHOLD);
    createEOperation(mBrickletHumidityEClass, MBRICKLET_HUMIDITY___INIT);

    mBrickletDistanceIREClass = createEClass(MBRICKLET_DISTANCE_IR);
    createEAttribute(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR__DEVICE_TYPE);
    createEAttribute(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR__DISTANCE);
    createEAttribute(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR__THRESHOLD);
    createEOperation(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR___INIT);

    mBrickletTemperatureEClass = createEClass(MBRICKLET_TEMPERATURE);
    createEAttribute(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE__DEVICE_TYPE);
    createEAttribute(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE__TEMPERATURE);
    createEAttribute(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE__THRESHOLD);
    createEOperation(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE___INIT);

    tfBaseConfigurationEClass = createEClass(TF_BASE_CONFIGURATION);
    createEAttribute(tfBaseConfigurationEClass, TF_BASE_CONFIGURATION__THRESHOLD);
    createEAttribute(tfBaseConfigurationEClass, TF_BASE_CONFIGURATION__CALLBACK_PERIOD);

    mBrickletBarometerEClass = createEClass(MBRICKLET_BAROMETER);
    createEAttribute(mBrickletBarometerEClass, MBRICKLET_BAROMETER__DEVICE_TYPE);
    createEAttribute(mBrickletBarometerEClass, MBRICKLET_BAROMETER__AIR_PRESSURE);
    createEAttribute(mBrickletBarometerEClass, MBRICKLET_BAROMETER__THRESHOLD);
    createEOperation(mBrickletBarometerEClass, MBRICKLET_BAROMETER___INIT);

    mBarometerTemperatureEClass = createEClass(MBAROMETER_TEMPERATURE);
    createEAttribute(mBarometerTemperatureEClass, MBAROMETER_TEMPERATURE__DEVICE_TYPE);
    createEAttribute(mBarometerTemperatureEClass, MBAROMETER_TEMPERATURE__TEMPERATURE);
    createEOperation(mBarometerTemperatureEClass, MBAROMETER_TEMPERATURE___INIT);

    mBrickletAmbientLightEClass = createEClass(MBRICKLET_AMBIENT_LIGHT);
    createEAttribute(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT__DEVICE_TYPE);
    createEAttribute(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT__ILLUMINANCE);
    createEAttribute(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT__THRESHOLD);
    createEOperation(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT___INIT);

    mBrickletLCD20x4EClass = createEClass(MBRICKLET_LCD2_0X4);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__DEVICE_TYPE);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__POSITION_PREFIX);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__POSITON_SUFFIX);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__DISPLAY_ERRORS);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__ERROR_PREFIX);
    createEOperation(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4___INIT);

    mTextActorEClass = createEClass(MTEXT_ACTOR);
    createEAttribute(mTextActorEClass, MTEXT_ACTOR__TEXT);

    mlcd20x4ButtonEClass = createEClass(MLCD2_0X4_BUTTON);
    createEAttribute(mlcd20x4ButtonEClass, MLCD2_0X4_BUTTON__DEVICE_TYPE);
    createEAttribute(mlcd20x4ButtonEClass, MLCD2_0X4_BUTTON__BUTTON_NUM);
    createEAttribute(mlcd20x4ButtonEClass, MLCD2_0X4_BUTTON__CALLBACK_PERIOD);

    // Create enums
    switchStateEEnum = createEEnum(SWITCH_STATE);
    digitalStateEEnum = createEEnum(DIGITAL_STATE);
    dcDriveModeEEnum = createEEnum(DC_DRIVE_MODE);

    // Create data types
    mipConnectionEDataType = createEDataType(MIP_CONNECTION);
    mTinkerDeviceEDataType = createEDataType(MTINKER_DEVICE);
    mLoggerEDataType = createEDataType(MLOGGER);
    mAtomicBooleanEDataType = createEDataType(MATOMIC_BOOLEAN);
    mTinkerforgeDeviceEDataType = createEDataType(MTINKERFORGE_DEVICE);
    mTinkerBrickDCEDataType = createEDataType(MTINKER_BRICK_DC);
    mTinkerBrickletDualRelayEDataType = createEDataType(MTINKER_BRICKLET_DUAL_RELAY);
    mTinkerBrickletIndustrialQuadRelayEDataType = createEDataType(MTINKER_BRICKLET_INDUSTRIAL_QUAD_RELAY);
    mTinkerBrickletIndustrialDigitalIn4EDataType = createEDataType(MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_IN4);
    mTinkerBrickServoEDataType = createEDataType(MTINKER_BRICK_SERVO);
    mTinkerBrickletHumidityEDataType = createEDataType(MTINKER_BRICKLET_HUMIDITY);
    mTinkerBrickletDistanceIREDataType = createEDataType(MTINKER_BRICKLET_DISTANCE_IR);
    mTinkerBrickletTemperatureEDataType = createEDataType(MTINKER_BRICKLET_TEMPERATURE);
    mTinkerBrickletBarometerEDataType = createEDataType(MTINKER_BRICKLET_BAROMETER);
    mTinkerBrickletAmbientLightEDataType = createEDataType(MTINKER_BRICKLET_AMBIENT_LIGHT);
    mTinkerBrickletLCD20x4EDataType = createEDataType(MTINKER_BRICKLET_LCD2_0X4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

    // Create type parameters
    ETypeParameter ohtfDeviceEClass_TFC = addETypeParameter(ohtfDeviceEClass, "TFC");
    ETypeParameter mtfConfigConsumerEClass_TFC = addETypeParameter(mtfConfigConsumerEClass, "TFC");
    ETypeParameter mDeviceEClass_TF = addETypeParameter(mDeviceEClass, "TF");
    ETypeParameter mSubDeviceHolderEClass_S = addETypeParameter(mSubDeviceHolderEClass, "S");
    ETypeParameter mSubDeviceEClass_B = addETypeParameter(mSubDeviceEClass, "B");
    ETypeParameter mSensorEClass_ValueType = addETypeParameter(mSensorEClass, "ValueType");

    // Set bounds for type parameters
    EGenericType g1 = createEGenericType(this.getTFConfig());
    ohtfDeviceEClass_TFC.getEBounds().add(g1);
    g1 = createEGenericType(this.getMTinkerforgeDevice());
    mDeviceEClass_TF.getEBounds().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    EGenericType g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    mSubDeviceHolderEClass_S.getEBounds().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    mSubDeviceEClass_B.getEBounds().add(g1);

    // Add supertypes to classes
    mDeviceEClass.getESuperTypes().add(this.getMBaseDevice());
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickServo());
    g1.getETypeArguments().add(g2);
    mBrickServoEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMServo());
    g1.getETypeArguments().add(g2);
    mBrickServoEClass.getEGenericSuperTypes().add(g1);
    tfBrickDCConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    g1 = createEGenericType(this.getMInSwitchActor());
    mBrickDCEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickDC());
    g1.getETypeArguments().add(g2);
    mBrickDCEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBrickDCConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickDCEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletDualRelay());
    g1.getETypeArguments().add(g2);
    mDualRelayBrickletEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMDualRelay());
    g1.getETypeArguments().add(g2);
    mDualRelayBrickletEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletIndustrialQuadRelay());
    g1.getETypeArguments().add(g2);
    mIndustrialQuadRelayBrickletEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMIndustrialQuadRelay());
    g1.getETypeArguments().add(g2);
    mIndustrialQuadRelayBrickletEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMInSwitchActor());
    mIndustrialQuadRelayEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMIndustrialQuadRelayBricklet());
    g1.getETypeArguments().add(g2);
    mIndustrialQuadRelayEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMIndustrialDigitalIn());
    g1.getETypeArguments().add(g2);
    mBrickletIndustrialDigitalIn4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletIndustrialDigitalIn4());
    g1.getETypeArguments().add(g2);
    mBrickletIndustrialDigitalIn4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletIndustrialDigitalIn4());
    g1.getETypeArguments().add(g2);
    mIndustrialDigitalInEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMOutSwitchActor());
    mIndustrialDigitalInEClass.getEGenericSuperTypes().add(g1);
    mOutSwitchActorEClass.getESuperTypes().add(this.getMSwitchActor());
    mInSwitchActorEClass.getESuperTypes().add(this.getMSwitchActor());
    mSubDeviceEClass.getESuperTypes().add(this.getMBaseDevice());
    g1 = createEGenericType(this.getMInSwitchActor());
    mDualRelayEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMDualRelayBricklet());
    g1.getETypeArguments().add(g2);
    mDualRelayEClass.getEGenericSuperTypes().add(g1);
    tfNullConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfServoConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    g1 = createEGenericType(this.getMInSwitchActor());
    mServoEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickServo());
    g1.getETypeArguments().add(g2);
    mServoEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFServoConfiguration());
    g1.getETypeArguments().add(g2);
    mServoEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBrickletHumidityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletHumidity());
    g1.getETypeArguments().add(g2);
    mBrickletHumidityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletHumidityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletHumidityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletDistanceIR());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletDistanceIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletTemperature());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    tfBaseConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletBarometer());
    g1.getETypeArguments().add(g2);
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMBarometerTemperature());
    g1.getETypeArguments().add(g2);
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBarometerTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletBarometer());
    g1.getETypeArguments().add(g2);
    mBarometerTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletAmbientLight());
    g1.getETypeArguments().add(g2);
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(theEcorePackage.getEDoubleObject());
    g1.getETypeArguments().add(g2);
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletLCD20x4());
    g1.getETypeArguments().add(g2);
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTextActor());
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMInSwitchActor());
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMLCD20x4Button());
    g1.getETypeArguments().add(g2);
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMOutSwitchActor());
    mlcd20x4ButtonEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletLCD20x4());
    g1.getETypeArguments().add(g2);
    mlcd20x4ButtonEClass.getEGenericSuperTypes().add(g1);

    // Initialize classes, features, and operations; add parameters
    initEClass(tfConfigEClass, TFConfig.class, "TFConfig", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(ohtfDeviceEClass, OHTFDevice.class, "OHTFDevice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getOHTFDevice_Uid(), theEcorePackage.getEString(), "uid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOHTFDevice_Subid(), theEcorePackage.getEString(), "subid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOHTFDevice_Ohid(), theEcorePackage.getEString(), "ohid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(ohtfDeviceEClass_TFC);
    initEReference(getOHTFDevice_TfConfig(), g1, null, "tfConfig", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOHTFDevice_OhConfig(), this.getOHConfig(), this.getOHConfig_OhTfDevices(), "ohConfig", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ohConfigEClass, OHConfig.class, "OHConfig", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEReference(getOHConfig_OhTfDevices(), g1, this.getOHTFDevice_OhConfig(), "ohTfDevices", null, 0, -1, OHConfig.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getOHConfig__GetConfigByTFId__String_String(), null, "getConfigByTFId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEOperation(op, g1);

    op = initEOperation(getOHConfig__GetConfigByOHId__String(), null, "getConfigByOHId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "ohid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEOperation(op, g1);

    initEClass(ecosystemEClass, Ecosystem.class, "Ecosystem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEcosystem_Logger(), this.getMLogger(), "logger", null, 0, 1, Ecosystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getEcosystem_Mbrickds(), this.getMBrickd(), this.getMBrickd_Ecosystem(), "mbrickds", null, 0, -1, Ecosystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getEcosystem__GetBrickd__String_int(), this.getMBrickd(), "getBrickd", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "host", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEInt(), "port", 0, 1, !IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getEcosystem__GetDevice__String_String(), this.getMBaseDevice(), "getDevice", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subId", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getEcosystem__Disconnect(), null, "disconnect", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickdEClass, MBrickd.class, "MBrickd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickd_Logger(), this.getMLogger(), "logger", null, 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_IpConnection(), this.getMIPConnection(), "ipConnection", null, 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Host(), theEcorePackage.getEString(), "host", "localhost", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Port(), theEcorePackage.getEInt(), "port", "4223", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_IsConnected(), theEcorePackage.getEBoolean(), "isConnected", "false", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_AutoReconnect(), theEcorePackage.getEBoolean(), "autoReconnect", "true", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Timeout(), theEcorePackage.getEInt(), "timeout", "2500", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEReference(getMBrickd_Mdevices(), g1, this.getMDevice_Brickd(), "mdevices", null, 0, -1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMBrickd_Ecosystem(), this.getEcosystem(), this.getEcosystem_Mbrickds(), "ecosystem", null, 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickd__Connect(), null, "connect", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getMBrickd__Disconnect(), null, "disconnect", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getMBrickd__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getMBrickd__GetDevice__String(), this.getMBaseDevice(), "getDevice", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mtfConfigConsumerEClass, MTFConfigConsumer.class, "MTFConfigConsumer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mtfConfigConsumerEClass_TFC);
    initEReference(getMTFConfigConsumer_TfConfig(), g1, null, "tfConfig", null, 0, 1, MTFConfigConsumer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBaseDeviceEClass, MBaseDevice.class, "MBaseDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBaseDevice_Logger(), this.getMLogger(), "logger", null, 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBaseDevice_Uid(), theEcorePackage.getEString(), "uid", null, 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBaseDevice_EnabledA(), this.getMAtomicBoolean(), "enabledA", null, 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBaseDevice__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getMBaseDevice__Enable(), null, "enable", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getMBaseDevice__Disable(), null, "disable", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mDeviceEClass, MDevice.class, "MDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mDeviceEClass_TF);
    initEAttribute(getMDevice_TinkerforgeDevice(), g1, "tinkerforgeDevice", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMDevice_IpConnection(), this.getMIPConnection(), "ipConnection", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMDevice_ConnectedUid(), theEcorePackage.getEString(), "connectedUid", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMDevice_Position(), theEcorePackage.getEChar(), "position", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMDevice_DeviceIdentifier(), theEcorePackage.getEInt(), "deviceIdentifier", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMDevice_Name(), theEcorePackage.getEString(), "name", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMDevice_Brickd(), this.getMBrickd(), this.getMBrickd_Mdevices(), "brickd", null, 0, 1, MDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mSubDeviceHolderEClass, MSubDeviceHolder.class, "MSubDeviceHolder", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mSubDeviceHolderEClass_S);
    initEReference(getMSubDeviceHolder_Msubdevices(), g1, this.getMSubDevice_Mbrick(), "msubdevices", null, 0, -1, MSubDeviceHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMSubDeviceHolder__InitSubDevices(), null, "initSubDevices", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickServoEClass, MBrickServo.class, "MBrickServo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickServo_DeviceType(), theEcorePackage.getEString(), "deviceType", "brick_servo", 0, 1, MBrickServo.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickServo__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(tfBrickDCConfigurationEClass, TFBrickDCConfiguration.class, "TFBrickDCConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFBrickDCConfiguration_Velocity(), theEcorePackage.getEShort(), "velocity", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_Acceleration(), theEcorePackage.getEInt(), "acceleration", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_PwmFrequency(), theEcorePackage.getEInt(), "pwmFrequency", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_DriveMode(), theEcorePackage.getEInt(), "driveMode", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_SwitchOnVelocity(), theEcorePackage.getEShort(), "switchOnVelocity", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickDCEClass, MBrickDC.class, "MBrickDC", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickDC_DeviceType(), theEcorePackage.getEString(), "deviceType", "brick_dc", 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_Velocity(), theEcorePackage.getEShort(), "velocity", null, 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_CurrentVelocity(), theEcorePackage.getEShort(), "currentVelocity", null, 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_Acceleration(), theEcorePackage.getEInt(), "acceleration", "10000", 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_PwmFrequency(), theEcorePackage.getEInt(), "pwmFrequency", "15000", 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_DriveMode(), this.getDCDriveMode(), "driveMode", "Break", 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickDC_SwitchOnVelocity(), theEcorePackage.getEShort(), "switchOnVelocity", "10000", 0, 1, MBrickDC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickDC__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mDualRelayBrickletEClass, MDualRelayBricklet.class, "MDualRelayBricklet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMDualRelayBricklet_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_dual_relay", 0, 1, MDualRelayBricklet.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mIndustrialQuadRelayBrickletEClass, MIndustrialQuadRelayBricklet.class, "MIndustrialQuadRelayBricklet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mIndustrialQuadRelayEClass, MIndustrialQuadRelay.class, "MIndustrialQuadRelay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMIndustrialQuadRelay_DeviceType(), theEcorePackage.getEString(), "deviceType", "industrial_quad_relay", 0, 1, MIndustrialQuadRelay.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletIndustrialDigitalIn4EClass, MBrickletIndustrialDigitalIn4.class, "MBrickletIndustrialDigitalIn4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mIndustrialDigitalInEClass, MIndustrialDigitalIn.class, "MIndustrialDigitalIn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mActorEClass, MActor.class, "MActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mSwitchActorEClass, MSwitchActor.class, "MSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMSwitchActor_SwitchState(), this.getSwitchState(), "switchState", null, 0, 1, MSwitchActor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mOutSwitchActorEClass, MOutSwitchActor.class, "MOutSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mInSwitchActorEClass, MInSwitchActor.class, "MInSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mSubDeviceEClass, MSubDevice.class, "MSubDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMSubDevice_SubId(), theEcorePackage.getEString(), "subId", null, 0, 1, MSubDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(mSubDeviceEClass_B);
    initEReference(getMSubDevice_Mbrick(), g1, this.getMSubDeviceHolder_Msubdevices(), "mbrick", null, 0, 1, MSubDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mDualRelayEClass, MDualRelay.class, "MDualRelay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMDualRelay_DeviceType(), theEcorePackage.getEString(), "deviceType", "dual_relay", 0, 1, MDualRelay.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfNullConfigurationEClass, TFNullConfiguration.class, "TFNullConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(tfServoConfigurationEClass, TFServoConfiguration.class, "TFServoConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFServoConfiguration_Velocity(), theEcorePackage.getEInt(), "velocity", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_Acceleration(), theEcorePackage.getEInt(), "acceleration", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_ServoVoltage(), theEcorePackage.getEInt(), "servoVoltage", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_PulseWidthMin(), theEcorePackage.getEInt(), "pulseWidthMin", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_PulseWidthMax(), theEcorePackage.getEInt(), "pulseWidthMax", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_Period(), theEcorePackage.getEInt(), "period", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_OutputVoltage(), theEcorePackage.getEInt(), "outputVoltage", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mServoEClass, MServo.class, "MServo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMServo_DeviceType(), theEcorePackage.getEString(), "deviceType", "servo", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_Velocity(), theEcorePackage.getEInt(), "velocity", "30000", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_Acceleration(), theEcorePackage.getEInt(), "acceleration", "30000", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_PulseWidthMin(), theEcorePackage.getEInt(), "pulseWidthMin", "1000", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_PulseWidthMax(), theEcorePackage.getEInt(), "pulseWidthMax", "2000", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_Period(), theEcorePackage.getEInt(), "period", "19500", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_OutputVoltage(), theEcorePackage.getEInt(), "outputVoltage", "5000", 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_ServoCurrentPosition(), theEcorePackage.getEShort(), "servoCurrentPosition", null, 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMServo_ServoDestinationPosition(), theEcorePackage.getEShort(), "servoDestinationPosition", null, 0, 1, MServo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMServo__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(callbackListenerEClass, CallbackListener.class, "CallbackListener", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCallbackListener_CallbackPeriod(), theEcorePackage.getELong(), "callbackPeriod", "1000", 0, 1, CallbackListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mSensorEClass, MSensor.class, "MSensor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mSensorEClass_ValueType);
    initEAttribute(getMSensor_SensorValue(), g1, "sensorValue", null, 0, 1, MSensor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getMSensor__FetchSensorValue(), null, "fetchSensorValue", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(mSensorEClass_ValueType);
    initEOperation(op, g1);

    initEClass(mBrickletHumidityEClass, MBrickletHumidity.class, "MBrickletHumidity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletHumidity_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_humidity", 0, 1, MBrickletHumidity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletHumidity_Humiditiy(), theEcorePackage.getEInt(), "humiditiy", null, 0, 1, MBrickletHumidity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletHumidity_Threshold(), theEcorePackage.getEInt(), "threshold", "5", 0, 1, MBrickletHumidity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletHumidity__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletDistanceIREClass, MBrickletDistanceIR.class, "MBrickletDistanceIR", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletDistanceIR_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_distance_ir", 0, 1, MBrickletDistanceIR.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletDistanceIR_Distance(), theEcorePackage.getEInt(), "distance", null, 0, 1, MBrickletDistanceIR.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletDistanceIR_Threshold(), theEcorePackage.getEInt(), "threshold", "5", 0, 1, MBrickletDistanceIR.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletDistanceIR__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletTemperatureEClass, MBrickletTemperature.class, "MBrickletTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_temperature", 0, 1, MBrickletTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletTemperature_Temperature(), theEcorePackage.getEShort(), "temperature", null, 0, 1, MBrickletTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletTemperature_Threshold(), theEcorePackage.getEInt(), "threshold", "10", 0, 1, MBrickletTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletTemperature__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(tfBaseConfigurationEClass, TFBaseConfiguration.class, "TFBaseConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFBaseConfiguration_Threshold(), theEcorePackage.getEInt(), "threshold", null, 0, 1, TFBaseConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBaseConfiguration_CallbackPeriod(), theEcorePackage.getEInt(), "callbackPeriod", null, 0, 1, TFBaseConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletBarometerEClass, MBrickletBarometer.class, "MBrickletBarometer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletBarometer_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_barometer", 0, 1, MBrickletBarometer.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletBarometer_AirPressure(), theEcorePackage.getEInt(), "airPressure", null, 0, 1, MBrickletBarometer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletBarometer_Threshold(), theEcorePackage.getEInt(), "threshold", "1000", 0, 1, MBrickletBarometer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletBarometer__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBarometerTemperatureEClass, MBarometerTemperature.class, "MBarometerTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBarometerTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "barometer_temperature", 0, 1, MBarometerTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBarometerTemperature_Temperature(), theEcorePackage.getEShort(), "temperature", null, 0, 1, MBarometerTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBarometerTemperature__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletAmbientLightEClass, MBrickletAmbientLight.class, "MBrickletAmbientLight", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletAmbientLight_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_ambient_light", 0, 1, MBrickletAmbientLight.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletAmbientLight_Illuminance(), theEcorePackage.getEInt(), "illuminance", null, 0, 1, MBrickletAmbientLight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletAmbientLight_Threshold(), theEcorePackage.getEInt(), "threshold", "10", 0, 1, MBrickletAmbientLight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletAmbientLight__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletLCD20x4EClass, MBrickletLCD20x4.class, "MBrickletLCD20x4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletLCD20x4_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_LCD20x4", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_PositionPrefix(), theEcorePackage.getEString(), "positionPrefix", "TFNUM<", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_PositonSuffix(), theEcorePackage.getEString(), "positonSuffix", ">", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_DisplayErrors(), theEcorePackage.getEBoolean(), "displayErrors", "true", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_ErrorPrefix(), theEcorePackage.getEString(), "errorPrefix", "openhab Error:", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletLCD20x4__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mTextActorEClass, MTextActor.class, "MTextActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMTextActor_Text(), theEcorePackage.getEString(), "text", null, 0, 1, MTextActor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mlcd20x4ButtonEClass, MLCD20x4Button.class, "MLCD20x4Button", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMLCD20x4Button_DeviceType(), theEcorePackage.getEString(), "deviceType", "lcd_button", 0, 1, MLCD20x4Button.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMLCD20x4Button_ButtonNum(), theEcorePackage.getEShort(), "buttonNum", null, 0, 1, MLCD20x4Button.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMLCD20x4Button_CallbackPeriod(), theEcorePackage.getEInt(), "callbackPeriod", null, 0, 1, MLCD20x4Button.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(switchStateEEnum, SwitchState.class, "SwitchState");
    addEEnumLiteral(switchStateEEnum, SwitchState.ON);
    addEEnumLiteral(switchStateEEnum, SwitchState.OFF);
    addEEnumLiteral(switchStateEEnum, SwitchState.UNDEF);

    initEEnum(digitalStateEEnum, DigitalState.class, "DigitalState");
    addEEnumLiteral(digitalStateEEnum, DigitalState.HIGH);
    addEEnumLiteral(digitalStateEEnum, DigitalState.LOW);
    addEEnumLiteral(digitalStateEEnum, DigitalState.UNDEF);

    initEEnum(dcDriveModeEEnum, DCDriveMode.class, "DCDriveMode");
    addEEnumLiteral(dcDriveModeEEnum, DCDriveMode.BRAKE);
    addEEnumLiteral(dcDriveModeEEnum, DCDriveMode.COAST);

    // Initialize data types
    initEDataType(mipConnectionEDataType, IPConnection.class, "MIPConnection", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerDeviceEDataType, Device.class, "MTinkerDevice", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mLoggerEDataType, Logger.class, "MLogger", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mAtomicBooleanEDataType, AtomicBoolean.class, "MAtomicBoolean", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerforgeDeviceEDataType, Device.class, "MTinkerforgeDevice", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickDCEDataType, BrickDC.class, "MTinkerBrickDC", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletDualRelayEDataType, BrickletDualRelay.class, "MTinkerBrickletDualRelay", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletIndustrialQuadRelayEDataType, BrickletIndustrialQuadRelay.class, "MTinkerBrickletIndustrialQuadRelay", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletIndustrialDigitalIn4EDataType, BrickletIndustrialDigitalIn4.class, "MTinkerBrickletIndustrialDigitalIn4", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickServoEDataType, BrickServo.class, "MTinkerBrickServo", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletHumidityEDataType, BrickletHumidity.class, "MTinkerBrickletHumidity", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletDistanceIREDataType, BrickletDistanceIR.class, "MTinkerBrickletDistanceIR", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletTemperatureEDataType, BrickletTemperature.class, "MTinkerBrickletTemperature", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletBarometerEDataType, BrickletBarometer.class, "MTinkerBrickletBarometer", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletAmbientLightEDataType, BrickletAmbientLight.class, "MTinkerBrickletAmbientLight", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletLCD20x4EDataType, BrickletLCD20x4.class, "MTinkerBrickletLCD20x4", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);
  }

} //ModelPackageImpl
