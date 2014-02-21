/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.openhab.binding.tinkerforge.internal.model.*;
import org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs;
import org.openhab.binding.tinkerforge.internal.model.DCDriveMode;
import org.openhab.binding.tinkerforge.internal.model.DigitalActor;
import org.openhab.binding.tinkerforge.internal.model.DigitalSensor;
import org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.IO16SubIds;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs;
import org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs;
import org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds;
import org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds;
import org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight;
import org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR;
import org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO16;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature;
import org.openhab.binding.tinkerforge.internal.model.MDualRelay;
import org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.NoSubIds;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.ServoSubIDs;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;
import org.slf4j.Logger;

import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.BrickletIndustrialQuadRelay;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ModelFactory init()
  {
    try
    {
      ModelFactory theModelFactory = (ModelFactory)EPackage.Registry.INSTANCE.getEFactory("org.openhab.binding.tinkerforge.internal.model"); 
      if (theModelFactory != null)
      {
        return theModelFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ModelFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case ModelPackage.ECOSYSTEM: return createEcosystem();
      case ModelPackage.MBRICKD: return createMBrickd();
      case ModelPackage.MBRICK_SERVO: return createMBrickServo();
      case ModelPackage.MSERVO: return createMServo();
      case ModelPackage.MBRICK_DC: return createMBrickDC();
      case ModelPackage.MDUAL_RELAY_BRICKLET: return createMDualRelayBricklet();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY_BRICKLET: return createMIndustrialQuadRelayBricklet();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY: return createMIndustrialQuadRelay();
      case ModelPackage.MBRICKLET_INDUSTRIAL_DIGITAL_IN4: return createMBrickletIndustrialDigitalIn4();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN: return createMIndustrialDigitalIn();
      case ModelPackage.DIGITAL_ACTOR: return createDigitalActor();
      case ModelPackage.MBRICKLET_IO16: return createMBrickletIO16();
      case ModelPackage.DIGITAL_SENSOR: return createDigitalSensor();
      case ModelPackage.MDUAL_RELAY: return createMDualRelay();
      case ModelPackage.MBRICKLET_REMOTE_SWITCH: return createMBrickletRemoteSwitch();
      case ModelPackage.REMOTE_SWITCH_A: return createRemoteSwitchA();
      case ModelPackage.REMOTE_SWITCH_B: return createRemoteSwitchB();
      case ModelPackage.REMOTE_SWITCH_C: return createRemoteSwitchC();
      case ModelPackage.MBRICKLET_HUMIDITY: return createMBrickletHumidity();
      case ModelPackage.MBRICKLET_DISTANCE_IR: return createMBrickletDistanceIR();
      case ModelPackage.MBRICKLET_TEMPERATURE: return createMBrickletTemperature();
      case ModelPackage.MBRICKLET_BAROMETER: return createMBrickletBarometer();
      case ModelPackage.MBAROMETER_TEMPERATURE: return createMBarometerTemperature();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT: return createMBrickletAmbientLight();
      case ModelPackage.MBRICKLET_LCD2_0X4: return createMBrickletLCD20x4();
      case ModelPackage.MLCD2_0X4_BACKLIGHT: return createMLCD20x4Backlight();
      case ModelPackage.MLCD2_0X4_BUTTON: return createMLCD20x4Button();
      case ModelPackage.OHTF_DEVICE: return createOHTFDevice();
      case ModelPackage.OHTF_SUB_DEVICE_ADMIN_DEVICE: return createOHTFSubDeviceAdminDevice();
      case ModelPackage.OH_CONFIG: return createOHConfig();
      case ModelPackage.TF_NULL_CONFIGURATION: return createTFNullConfiguration();
      case ModelPackage.TF_BASE_CONFIGURATION: return createTFBaseConfiguration();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION: return createTFBrickDCConfiguration();
      case ModelPackage.TFIO_ACTOR_CONFIGURATION: return createTFIOActorConfiguration();
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION: return createTFInterruptListenerConfiguration();
      case ModelPackage.TFIO_SENSOR_CONFIGURATION: return createTFIOSensorConfiguration();
      case ModelPackage.TF_SERVO_CONFIGURATION: return createTFServoConfiguration();
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION: return createBrickletRemoteSwitchConfiguration();
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION: return createRemoteSwitchAConfiguration();
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION: return createRemoteSwitchBConfiguration();
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION: return createRemoteSwitchCConfiguration();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case ModelPackage.DC_DRIVE_MODE:
        return createDCDriveModeFromString(eDataType, initialValue);
      case ModelPackage.NO_SUB_IDS:
        return createNoSubIdsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_IN_SUB_IDS:
        return createIndustrialDigitalInSubIDsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_QUAD_RELAY_IDS:
        return createIndustrialQuadRelayIDsFromString(eDataType, initialValue);
      case ModelPackage.SERVO_SUB_IDS:
        return createServoSubIDsFromString(eDataType, initialValue);
      case ModelPackage.BAROMETER_SUB_IDS:
        return createBarometerSubIDsFromString(eDataType, initialValue);
      case ModelPackage.IO16_SUB_IDS:
        return createIO16SubIdsFromString(eDataType, initialValue);
      case ModelPackage.DUAL_RELAY_SUB_IDS:
        return createDualRelaySubIdsFromString(eDataType, initialValue);
      case ModelPackage.LCD_BUTTON_SUB_IDS:
        return createLCDButtonSubIdsFromString(eDataType, initialValue);
      case ModelPackage.LCD_BACKLIGHT_SUB_IDS:
        return createLCDBacklightSubIdsFromString(eDataType, initialValue);
      case ModelPackage.MIP_CONNECTION:
        return createMIPConnectionFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_DEVICE:
        return createMTinkerDeviceFromString(eDataType, initialValue);
      case ModelPackage.MLOGGER:
        return createMLoggerFromString(eDataType, initialValue);
      case ModelPackage.MATOMIC_BOOLEAN:
        return createMAtomicBooleanFromString(eDataType, initialValue);
      case ModelPackage.MTINKERFORGE_DEVICE:
        return createMTinkerforgeDeviceFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICK_DC:
        return createMTinkerBrickDCFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_DUAL_RELAY:
        return createMTinkerBrickletDualRelayFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_QUAD_RELAY:
        return createMTinkerBrickletIndustrialQuadRelayFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_IN4:
        return createMTinkerBrickletIndustrialDigitalIn4FromString(eDataType, initialValue);
      case ModelPackage.SWITCH_STATE:
        return createSwitchStateFromString(eDataType, initialValue);
      case ModelPackage.DIGITAL_VALUE:
        return createDigitalValueFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_IO16:
        return createTinkerBrickletIO16FromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICK_SERVO:
        return createMTinkerBrickServoFromString(eDataType, initialValue);
      case ModelPackage.MTINKERFORGE_VALUE:
        return createMTinkerforgeValueFromString(eDataType, initialValue);
      case ModelPackage.MDECIMAL_VALUE:
        return createMDecimalValueFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_HUMIDITY:
        return createMTinkerBrickletHumidityFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_DISTANCE_IR:
        return createMTinkerBrickletDistanceIRFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_TEMPERATURE:
        return createMTinkerBrickletTemperatureFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_BAROMETER:
        return createMTinkerBrickletBarometerFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_AMBIENT_LIGHT:
        return createMTinkerBrickletAmbientLightFromString(eDataType, initialValue);
      case ModelPackage.MTINKER_BRICKLET_LCD2_0X4:
        return createMTinkerBrickletLCD20x4FromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_REMOTE_SWITCH:
        return createTinkerBrickletRemoteSwitchFromString(eDataType, initialValue);
      case ModelPackage.ENUM:
        return createEnumFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case ModelPackage.DC_DRIVE_MODE:
        return convertDCDriveModeToString(eDataType, instanceValue);
      case ModelPackage.NO_SUB_IDS:
        return convertNoSubIdsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_IN_SUB_IDS:
        return convertIndustrialDigitalInSubIDsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_QUAD_RELAY_IDS:
        return convertIndustrialQuadRelayIDsToString(eDataType, instanceValue);
      case ModelPackage.SERVO_SUB_IDS:
        return convertServoSubIDsToString(eDataType, instanceValue);
      case ModelPackage.BAROMETER_SUB_IDS:
        return convertBarometerSubIDsToString(eDataType, instanceValue);
      case ModelPackage.IO16_SUB_IDS:
        return convertIO16SubIdsToString(eDataType, instanceValue);
      case ModelPackage.DUAL_RELAY_SUB_IDS:
        return convertDualRelaySubIdsToString(eDataType, instanceValue);
      case ModelPackage.LCD_BUTTON_SUB_IDS:
        return convertLCDButtonSubIdsToString(eDataType, instanceValue);
      case ModelPackage.LCD_BACKLIGHT_SUB_IDS:
        return convertLCDBacklightSubIdsToString(eDataType, instanceValue);
      case ModelPackage.MIP_CONNECTION:
        return convertMIPConnectionToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_DEVICE:
        return convertMTinkerDeviceToString(eDataType, instanceValue);
      case ModelPackage.MLOGGER:
        return convertMLoggerToString(eDataType, instanceValue);
      case ModelPackage.MATOMIC_BOOLEAN:
        return convertMAtomicBooleanToString(eDataType, instanceValue);
      case ModelPackage.MTINKERFORGE_DEVICE:
        return convertMTinkerforgeDeviceToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICK_DC:
        return convertMTinkerBrickDCToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_DUAL_RELAY:
        return convertMTinkerBrickletDualRelayToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_QUAD_RELAY:
        return convertMTinkerBrickletIndustrialQuadRelayToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_IN4:
        return convertMTinkerBrickletIndustrialDigitalIn4ToString(eDataType, instanceValue);
      case ModelPackage.SWITCH_STATE:
        return convertSwitchStateToString(eDataType, instanceValue);
      case ModelPackage.DIGITAL_VALUE:
        return convertDigitalValueToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_IO16:
        return convertTinkerBrickletIO16ToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICK_SERVO:
        return convertMTinkerBrickServoToString(eDataType, instanceValue);
      case ModelPackage.MTINKERFORGE_VALUE:
        return convertMTinkerforgeValueToString(eDataType, instanceValue);
      case ModelPackage.MDECIMAL_VALUE:
        return convertMDecimalValueToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_HUMIDITY:
        return convertMTinkerBrickletHumidityToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_DISTANCE_IR:
        return convertMTinkerBrickletDistanceIRToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_TEMPERATURE:
        return convertMTinkerBrickletTemperatureToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_BAROMETER:
        return convertMTinkerBrickletBarometerToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_AMBIENT_LIGHT:
        return convertMTinkerBrickletAmbientLightToString(eDataType, instanceValue);
      case ModelPackage.MTINKER_BRICKLET_LCD2_0X4:
        return convertMTinkerBrickletLCD20x4ToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_REMOTE_SWITCH:
        return convertTinkerBrickletRemoteSwitchToString(eDataType, instanceValue);
      case ModelPackage.ENUM:
        return convertEnumToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
	@SuppressWarnings("rawtypes")
	public <TFC extends TFConfig, IDS extends Enum> OHTFDevice<TFC, IDS> createOHTFDevice()
  {
    OHTFDeviceImpl<TFC, IDS> ohtfDevice = new OHTFDeviceImpl<TFC, IDS>();
    return ohtfDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public <TFC extends TFConfig, IDS extends Enum> OHTFSubDeviceAdminDevice<TFC, IDS> createOHTFSubDeviceAdminDevice()
  {
    OHTFSubDeviceAdminDeviceImpl<TFC, IDS> ohtfSubDeviceAdminDevice = new OHTFSubDeviceAdminDeviceImpl<TFC, IDS>();
    return ohtfSubDeviceAdminDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OHConfig createOHConfig()
  {
    OHConfigImpl ohConfig = new OHConfigImpl();
    return ohConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Ecosystem createEcosystem()
  {
    EcosystemImpl ecosystem = new EcosystemImpl();
    return ecosystem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd createMBrickd()
  {
    MBrickdImpl mBrickd = new MBrickdImpl();
    return mBrickd;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickServo createMBrickServo()
  {
    MBrickServoImpl mBrickServo = new MBrickServoImpl();
    return mBrickServo;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFBrickDCConfiguration createTFBrickDCConfiguration()
  {
    TFBrickDCConfigurationImpl tfBrickDCConfiguration = new TFBrickDCConfigurationImpl();
    return tfBrickDCConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickDC createMBrickDC()
  {
    MBrickDCImpl mBrickDC = new MBrickDCImpl();
    return mBrickDC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MDualRelayBricklet createMDualRelayBricklet()
  {
    MDualRelayBrickletImpl mDualRelayBricklet = new MDualRelayBrickletImpl();
    return mDualRelayBricklet;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIndustrialQuadRelayBricklet createMIndustrialQuadRelayBricklet()
  {
    MIndustrialQuadRelayBrickletImpl mIndustrialQuadRelayBricklet = new MIndustrialQuadRelayBrickletImpl();
    return mIndustrialQuadRelayBricklet;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIndustrialQuadRelay createMIndustrialQuadRelay()
  {
    MIndustrialQuadRelayImpl mIndustrialQuadRelay = new MIndustrialQuadRelayImpl();
    return mIndustrialQuadRelay;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDigitalIn4 createMBrickletIndustrialDigitalIn4()
  {
    MBrickletIndustrialDigitalIn4Impl mBrickletIndustrialDigitalIn4 = new MBrickletIndustrialDigitalIn4Impl();
    return mBrickletIndustrialDigitalIn4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIndustrialDigitalIn createMIndustrialDigitalIn()
  {
    MIndustrialDigitalInImpl mIndustrialDigitalIn = new MIndustrialDigitalInImpl();
    return mIndustrialDigitalIn;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFIOActorConfiguration createTFIOActorConfiguration()
  {
    TFIOActorConfigurationImpl tfioActorConfiguration = new TFIOActorConfigurationImpl();
    return tfioActorConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalActor createDigitalActor()
  {
    DigitalActorImpl digitalActor = new DigitalActorImpl();
    return digitalActor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFInterruptListenerConfiguration createTFInterruptListenerConfiguration()
  {
    TFInterruptListenerConfigurationImpl tfInterruptListenerConfiguration = new TFInterruptListenerConfigurationImpl();
    return tfInterruptListenerConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIO16 createMBrickletIO16()
  {
    MBrickletIO16Impl mBrickletIO16 = new MBrickletIO16Impl();
    return mBrickletIO16;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFIOSensorConfiguration createTFIOSensorConfiguration()
  {
    TFIOSensorConfigurationImpl tfioSensorConfiguration = new TFIOSensorConfigurationImpl();
    return tfioSensorConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalSensor createDigitalSensor()
  {
    DigitalSensorImpl digitalSensor = new DigitalSensorImpl();
    return digitalSensor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MDualRelay createMDualRelay()
  {
    MDualRelayImpl mDualRelay = new MDualRelayImpl();
    return mDualRelay;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletRemoteSwitch createMBrickletRemoteSwitch()
  {
    MBrickletRemoteSwitchImpl mBrickletRemoteSwitch = new MBrickletRemoteSwitchImpl();
    return mBrickletRemoteSwitch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchA createRemoteSwitchA()
  {
    RemoteSwitchAImpl remoteSwitchA = new RemoteSwitchAImpl();
    return remoteSwitchA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchB createRemoteSwitchB()
  {
    RemoteSwitchBImpl remoteSwitchB = new RemoteSwitchBImpl();
    return remoteSwitchB;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchC createRemoteSwitchC()
  {
    RemoteSwitchCImpl remoteSwitchC = new RemoteSwitchCImpl();
    return remoteSwitchC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFNullConfiguration createTFNullConfiguration()
  {
    TFNullConfigurationImpl tfNullConfiguration = new TFNullConfigurationImpl();
    return tfNullConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFServoConfiguration createTFServoConfiguration()
  {
    TFServoConfigurationImpl tfServoConfiguration = new TFServoConfigurationImpl();
    return tfServoConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletRemoteSwitchConfiguration createBrickletRemoteSwitchConfiguration()
  {
    BrickletRemoteSwitchConfigurationImpl brickletRemoteSwitchConfiguration = new BrickletRemoteSwitchConfigurationImpl();
    return brickletRemoteSwitchConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchAConfiguration createRemoteSwitchAConfiguration()
  {
    RemoteSwitchAConfigurationImpl remoteSwitchAConfiguration = new RemoteSwitchAConfigurationImpl();
    return remoteSwitchAConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchBConfiguration createRemoteSwitchBConfiguration()
  {
    RemoteSwitchBConfigurationImpl remoteSwitchBConfiguration = new RemoteSwitchBConfigurationImpl();
    return remoteSwitchBConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchCConfiguration createRemoteSwitchCConfiguration()
  {
    RemoteSwitchCConfigurationImpl remoteSwitchCConfiguration = new RemoteSwitchCConfigurationImpl();
    return remoteSwitchCConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MServo createMServo()
  {
    MServoImpl mServo = new MServoImpl();
    return mServo;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletHumidity createMBrickletHumidity()
  {
    MBrickletHumidityImpl mBrickletHumidity = new MBrickletHumidityImpl();
    return mBrickletHumidity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletDistanceIR createMBrickletDistanceIR()
  {
    MBrickletDistanceIRImpl mBrickletDistanceIR = new MBrickletDistanceIRImpl();
    return mBrickletDistanceIR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletTemperature createMBrickletTemperature()
  {
    MBrickletTemperatureImpl mBrickletTemperature = new MBrickletTemperatureImpl();
    return mBrickletTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFBaseConfiguration createTFBaseConfiguration()
  {
    TFBaseConfigurationImpl tfBaseConfiguration = new TFBaseConfigurationImpl();
    return tfBaseConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletBarometer createMBrickletBarometer()
  {
    MBrickletBarometerImpl mBrickletBarometer = new MBrickletBarometerImpl();
    return mBrickletBarometer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBarometerTemperature createMBarometerTemperature()
  {
    MBarometerTemperatureImpl mBarometerTemperature = new MBarometerTemperatureImpl();
    return mBarometerTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletAmbientLight createMBrickletAmbientLight()
  {
    MBrickletAmbientLightImpl mBrickletAmbientLight = new MBrickletAmbientLightImpl();
    return mBrickletAmbientLight;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLCD20x4 createMBrickletLCD20x4()
  {
    MBrickletLCD20x4Impl mBrickletLCD20x4 = new MBrickletLCD20x4Impl();
    return mBrickletLCD20x4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MLCD20x4Backlight createMLCD20x4Backlight()
  {
    MLCD20x4BacklightImpl mlcd20x4Backlight = new MLCD20x4BacklightImpl();
    return mlcd20x4Backlight;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MLCD20x4Button createMLCD20x4Button()
  {
    MLCD20x4ButtonImpl mlcd20x4Button = new MLCD20x4ButtonImpl();
    return mlcd20x4Button;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue createSwitchStateFromString(EDataType eDataType, String initialValue)
  {
    return (OnOffValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertSwitchStateToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HighLowValue createDigitalValueFromString(EDataType eDataType, String initialValue)
  {
    return (HighLowValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDigitalValueToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIO16 createTinkerBrickletIO16FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIO16)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletIO16ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DCDriveMode createDCDriveModeFromString(EDataType eDataType, String initialValue)
  {
    DCDriveMode result = DCDriveMode.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDCDriveModeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NoSubIds createNoSubIdsFromString(EDataType eDataType, String initialValue)
  {
    NoSubIds result = NoSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertNoSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndustrialDigitalInSubIDs createIndustrialDigitalInSubIDsFromString(EDataType eDataType, String initialValue)
  {
    IndustrialDigitalInSubIDs result = IndustrialDigitalInSubIDs.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIndustrialDigitalInSubIDsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndustrialQuadRelayIDs createIndustrialQuadRelayIDsFromString(EDataType eDataType, String initialValue)
  {
    IndustrialQuadRelayIDs result = IndustrialQuadRelayIDs.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIndustrialQuadRelayIDsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ServoSubIDs createServoSubIDsFromString(EDataType eDataType, String initialValue)
  {
    ServoSubIDs result = ServoSubIDs.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertServoSubIDsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BarometerSubIDs createBarometerSubIDsFromString(EDataType eDataType, String initialValue)
  {
    BarometerSubIDs result = BarometerSubIDs.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertBarometerSubIDsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IO16SubIds createIO16SubIdsFromString(EDataType eDataType, String initialValue)
  {
    IO16SubIds result = IO16SubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIO16SubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualRelaySubIds createDualRelaySubIdsFromString(EDataType eDataType, String initialValue)
  {
    DualRelaySubIds result = DualRelaySubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDualRelaySubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LCDButtonSubIds createLCDButtonSubIdsFromString(EDataType eDataType, String initialValue)
  {
    LCDButtonSubIds result = LCDButtonSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertLCDButtonSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LCDBacklightSubIds createLCDBacklightSubIdsFromString(EDataType eDataType, String initialValue)
  {
    LCDBacklightSubIds result = LCDBacklightSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertLCDBacklightSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IPConnection createMIPConnectionFromString(EDataType eDataType, String initialValue)
  {
    return (IPConnection)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMIPConnectionToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Device createMTinkerDeviceFromString(EDataType eDataType, String initialValue)
  {
    return (Device)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerDeviceToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Logger createMLoggerFromString(EDataType eDataType, String initialValue)
  {
    return (Logger)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMLoggerToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AtomicBoolean createMAtomicBooleanFromString(EDataType eDataType, String initialValue)
  {
    return (AtomicBoolean)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMAtomicBooleanToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Device createMTinkerforgeDeviceFromString(EDataType eDataType, String initialValue)
  {
    return (Device)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerforgeDeviceToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickDC createMTinkerBrickDCFromString(EDataType eDataType, String initialValue)
  {
    return (BrickDC)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickDCToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickServo createMTinkerBrickServoFromString(EDataType eDataType, String initialValue)
  {
    return (BrickServo)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickServoToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TinkerforgeValue createMTinkerforgeValueFromString(EDataType eDataType, String initialValue)
  {
    return (TinkerforgeValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerforgeValueToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DecimalValue createMDecimalValueFromString(EDataType eDataType, String initialValue)
  {
    return (DecimalValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMDecimalValueToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletHumidity createMTinkerBrickletHumidityFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletHumidity)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletHumidityToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletDistanceIR createMTinkerBrickletDistanceIRFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletDistanceIR)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletDistanceIRToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletTemperature createMTinkerBrickletTemperatureFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletTemperature)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletTemperatureToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletBarometer createMTinkerBrickletBarometerFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletBarometer)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletBarometerToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAmbientLight createMTinkerBrickletAmbientLightFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletAmbientLight)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletAmbientLightToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLCD20x4 createMTinkerBrickletLCD20x4FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletLCD20x4)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletLCD20x4ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletRemoteSwitch createTinkerBrickletRemoteSwitchFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletRemoteSwitch)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletRemoteSwitchToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
	@SuppressWarnings("rawtypes")
	public Enum createEnumFromString(EDataType eDataType, String initialValue)
  {
    return (Enum)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertEnumToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletDualRelay createMTinkerBrickletDualRelayFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletDualRelay)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletDualRelayToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIndustrialQuadRelay createMTinkerBrickletIndustrialQuadRelayFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIndustrialQuadRelay)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletIndustrialQuadRelayToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIndustrialDigitalIn4 createMTinkerBrickletIndustrialDigitalIn4FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIndustrialDigitalIn4)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletIndustrialDigitalIn4ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelPackage getModelPackage()
  {
    return (ModelPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ModelPackage getPackage()
  {
    return ModelPackage.eINSTANCE;
  }

} //ModelFactoryImpl
