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
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.AmbientTemperature;
import org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs;
import org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.ColorActor;
import org.openhab.binding.tinkerforge.internal.model.DCDriveMode;
import org.openhab.binding.tinkerforge.internal.model.DigitalActor;
import org.openhab.binding.tinkerforge.internal.model.DigitalActorDigitalOut4;
import org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16;
import org.openhab.binding.tinkerforge.internal.model.DigitalActorIO4;
import org.openhab.binding.tinkerforge.internal.model.DigitalSensor;
import org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4;
import org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.Electrode;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
import org.openhab.binding.tinkerforge.internal.model.IO16SubIds;
import org.openhab.binding.tinkerforge.internal.model.IO4Device;
import org.openhab.binding.tinkerforge.internal.model.IO4SubIds;
import org.openhab.binding.tinkerforge.internal.model.IODevice;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs;
import org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs;
import org.openhab.binding.tinkerforge.internal.model.InterruptListener;
import org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds;
import org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds;
import org.openhab.binding.tinkerforge.internal.model.MActor;
import org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight;
import org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceUS;
import org.openhab.binding.tinkerforge.internal.model.MBrickletHallEffect;
import org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO16;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalOut4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip;
import org.openhab.binding.tinkerforge.internal.model.MBrickletMoisture;
import org.openhab.binding.tinkerforge.internal.model.MBrickletMotionDetector;
import org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch;
import org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch;
import org.openhab.binding.tinkerforge.internal.model.MBrickletSegmentDisplay4x7;
import org.openhab.binding.tinkerforge.internal.model.MBrickletSoundIntensity;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTemperatureIR;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTilt;
import org.openhab.binding.tinkerforge.internal.model.MBrickletVoltageCurrent;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MDualRelay;
import org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MInSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.MTemperatureIRDevice;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchSubIds;
import org.openhab.binding.tinkerforge.internal.model.NoSubIds;
import org.openhab.binding.tinkerforge.internal.model.NumberActor;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.OHTFSubDeviceAdminDevice;
import org.openhab.binding.tinkerforge.internal.model.ObjectTemperature;
import org.openhab.binding.tinkerforge.internal.model.Proximity;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitch;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchA;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ServoSubIDs;
import org.openhab.binding.tinkerforge.internal.model.SubDeviceAdmin;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.model.TFDistanceUSBrickletConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFVoltageCurrentConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TemperatureIRSubIds;
import org.openhab.binding.tinkerforge.internal.model.VCDeviceCurrent;
import org.openhab.binding.tinkerforge.internal.model.VCDevicePower;
import org.openhab.binding.tinkerforge.internal.model.VCDeviceVoltage;
import org.openhab.binding.tinkerforge.internal.model.VoltageCurrentDevice;
import org.openhab.binding.tinkerforge.internal.model.VoltageCurrentSubIds;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;
import org.openhab.core.library.types.HSBType;
import org.slf4j.Logger;

import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletHallEffect;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.BrickletIO4;
import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.BrickletIndustrialDigitalOut4;
import com.tinkerforge.BrickletIndustrialQuadRelay;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletLEDStrip;
import com.tinkerforge.BrickletMoisture;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.BrickletVoltageCurrent;
import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import java.math.BigDecimal;

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
  private EClass ohtfSubDeviceAdminDeviceEClass = null;

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
  private EClass subDeviceAdminEClass = null;

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
  private EClass mBrickletIndustrialDigitalOut4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalActorDigitalOut4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass numberActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass colorActorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletLEDStripEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletSegmentDisplay4x7EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalActorIO16EClass = null;

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
  private EClass genericDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfioActorConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfInterruptListenerConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletIO16EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ioDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfioSensorConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalSensorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletIO4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass io4DeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalSensorIO4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass digitalActorIO4EClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletMultiTouchEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass multiTouchDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass electrodeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass proximityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletMotionDetectorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletHallEffectEClass = null;

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
  private EClass mBrickletRemoteSwitchEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchAEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchBEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchCEClass = null;

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
  private EClass brickletRemoteSwitchConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchAConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchBConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass remoteSwitchCConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass multiTouchDeviceConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass brickletMultiTouchConfigurationEClass = null;

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
  private EClass interruptListenerEClass = null;

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
  private EClass mBrickletTemperatureIREClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mTemperatureIRDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass objectTemperatureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ambientTemperatureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletTiltEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletVoltageCurrentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass voltageCurrentDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass vcDeviceVoltageEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass vcDeviceCurrentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass vcDevicePowerEClass = null;

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
  private EClass tfObjectTemperatureConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfMoistureBrickletConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfDistanceUSBrickletConfigurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tfVoltageCurrentConfigurationEClass = null;

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
  private EClass mBrickletSoundIntensityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletMoistureEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mBrickletDistanceUSEClass = null;

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
  private EClass mlcdSubDeviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mlcd20x4BacklightEClass = null;

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
  private EEnum dcDriveModeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum noSubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum industrialDigitalInSubIDsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum industrialQuadRelayIDsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum servoSubIDsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum barometerSubIDsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum io16SubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum io4SubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum dualRelaySubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum lcdButtonSubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum lcdBacklightSubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum multiTouchSubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum temperatureIRSubIdsEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum voltageCurrentSubIdsEEnum = null;

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
  private EDataType mTinkerforgeValueEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mDecimalValueEDataType = null;

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
  private EDataType tinkerBrickletRemoteSwitchEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletMotionDetectorEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletMultiTouchEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletTemperatureIREDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletSoundIntensityEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletMoistureEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletDistanceUSEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletVoltageCurrentEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletTiltEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletIO4EDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletHallEffectEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletSegmentDisplay4x7EDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletLEDStripEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType hsbTypeEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType deviceOptionsEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType enumEDataType = null;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType mTinkerBrickletIndustrialDigitalOut4EDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType switchStateEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType digitalValueEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType tinkerBrickletIO16EDataType = null;

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
  public EAttribute getOHTFDevice_SubDeviceIds()
  {
    return (EAttribute)ohtfDeviceEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOHTFDevice_TfConfig()
  {
    return (EReference)ohtfDeviceEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOHTFDevice_OhConfig()
  {
    return (EReference)ohtfDeviceEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getOHTFDevice__IsValidSubId__String()
  {
    return ohtfDeviceEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOHTFSubDeviceAdminDevice()
  {
    return ohtfSubDeviceAdminDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getOHTFSubDeviceAdminDevice__IsValidSubId__String()
  {
    return ohtfSubDeviceAdminDeviceEClass.getEOperations().get(0);
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
  public EOperation getEcosystem__GetDevices4GenericId__String_String()
  {
    return ecosystemEClass.getEOperations().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getEcosystem__Disconnect()
  {
    return ecosystemEClass.getEOperations().get(3);
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
  public EAttribute getMBrickd_Reconnected()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickd_Timeout()
  {
    return (EAttribute)mBrickdEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMBrickd_Mdevices()
  {
    return (EReference)mBrickdEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMBrickd_Ecosystem()
  {
    return (EReference)mBrickdEClass.getEStructuralFeatures().get(9);
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
  public EClass getSubDeviceAdmin()
  {
    return subDeviceAdminEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getSubDeviceAdmin__AddSubDevice__String_String()
  {
    return subDeviceAdminEClass.getEOperations().get(0);
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
  public EAttribute getMBaseDevice_Poll()
  {
    return (EAttribute)mBaseDeviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBaseDevice_EnabledA()
  {
    return (EAttribute)mBaseDeviceEClass.getEStructuralFeatures().get(3);
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
  public EAttribute getMBrickletIndustrialDigitalIn4_DeviceType()
  {
    return (EAttribute)mBrickletIndustrialDigitalIn4EClass.getEStructuralFeatures().get(0);
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
  public EClass getMBrickletIndustrialDigitalOut4()
  {
    return mBrickletIndustrialDigitalOut4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalActorDigitalOut4()
  {
    return digitalActorDigitalOut4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorDigitalOut4_Pin()
  {
    return (EAttribute)digitalActorDigitalOut4EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalActor()
  {
    return digitalActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActor_DigitalState()
  {
    return (EAttribute)digitalActorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActor__TurnDigital__HighLowValue()
  {
    return digitalActorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActor__FetchDigitalValue()
  {
    return digitalActorEClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNumberActor()
  {
    return numberActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getNumberActor__SetNumber__BigDecimal()
  {
    return numberActorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getColorActor()
  {
    return colorActorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getColorActor__SetColor__HSBType_DeviceOptions()
  {
    return colorActorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletLEDStrip()
  {
    return mBrickletLEDStripEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletSegmentDisplay4x7()
  {
    return mBrickletSegmentDisplay4x7EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalActorIO16()
  {
    return digitalActorIO16EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO16_DeviceType()
  {
    return (EAttribute)digitalActorIO16EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO16_Port()
  {
    return (EAttribute)digitalActorIO16EClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO16_Pin()
  {
    return (EAttribute)digitalActorIO16EClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO16_DefaultState()
  {
    return (EAttribute)digitalActorIO16EClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO16_KeepOnReconnect()
  {
    return (EAttribute)digitalActorIO16EClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActorIO16__TurnDigital__HighLowValue()
  {
    return digitalActorIO16EClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActorIO16__FetchDigitalValue()
  {
    return digitalActorIO16EClass.getEOperations().get(1);
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
  public EOperation getMSwitchActor__TurnSwitch__OnOffValue()
  {
    return mSwitchActorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMSwitchActor__FetchSwitchState()
  {
    return mSwitchActorEClass.getEOperations().get(1);
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
  public EClass getGenericDevice()
  {
    return genericDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGenericDevice_GenericDeviceId()
  {
    return (EAttribute)genericDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFIOActorConfiguration()
  {
    return tfioActorConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFIOActorConfiguration_DefaultState()
  {
    return (EAttribute)tfioActorConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFIOActorConfiguration_KeepOnReconnect()
  {
    return (EAttribute)tfioActorConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFInterruptListenerConfiguration()
  {
    return tfInterruptListenerConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFInterruptListenerConfiguration_DebouncePeriod()
  {
    return (EAttribute)tfInterruptListenerConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletIO16()
  {
    return mBrickletIO16EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletIO16_DeviceType()
  {
    return (EAttribute)mBrickletIO16EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIODevice()
  {
    return ioDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFIOSensorConfiguration()
  {
    return tfioSensorConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFIOSensorConfiguration_PullUpResistorEnabled()
  {
    return (EAttribute)tfioSensorConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalSensor()
  {
    return digitalSensorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensor_DeviceType()
  {
    return (EAttribute)digitalSensorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensor_PullUpResistorEnabled()
  {
    return (EAttribute)digitalSensorEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensor_Port()
  {
    return (EAttribute)digitalSensorEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensor_Pin()
  {
    return (EAttribute)digitalSensorEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletIO4()
  {
    return mBrickletIO4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletIO4_DeviceType()
  {
    return (EAttribute)mBrickletIO4EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIO4Device()
  {
    return io4DeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalSensorIO4()
  {
    return digitalSensorIO4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensorIO4_DeviceType()
  {
    return (EAttribute)digitalSensorIO4EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensorIO4_PullUpResistorEnabled()
  {
    return (EAttribute)digitalSensorIO4EClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalSensorIO4_Pin()
  {
    return (EAttribute)digitalSensorIO4EClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDigitalActorIO4()
  {
    return digitalActorIO4EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO4_DeviceType()
  {
    return (EAttribute)digitalActorIO4EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO4_Pin()
  {
    return (EAttribute)digitalActorIO4EClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO4_DefaultState()
  {
    return (EAttribute)digitalActorIO4EClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDigitalActorIO4_KeepOnReconnect()
  {
    return (EAttribute)digitalActorIO4EClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActorIO4__TurnDigital__HighLowValue()
  {
    return digitalActorIO4EClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getDigitalActorIO4__FetchDigitalValue()
  {
    return digitalActorIO4EClass.getEOperations().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletMultiTouch()
  {
    return mBrickletMultiTouchEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMultiTouch_DeviceType()
  {
    return (EAttribute)mBrickletMultiTouchEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMultiTouch_Recalibrate()
  {
    return (EAttribute)mBrickletMultiTouchEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMultiTouch_Sensitivity()
  {
    return (EAttribute)mBrickletMultiTouchEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMultiTouchDevice()
  {
    return multiTouchDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMultiTouchDevice_Pin()
  {
    return (EAttribute)multiTouchDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMultiTouchDevice_DisableElectrode()
  {
    return (EAttribute)multiTouchDeviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getElectrode()
  {
    return electrodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getElectrode_DeviceType()
  {
    return (EAttribute)electrodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProximity()
  {
    return proximityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProximity_DeviceType()
  {
    return (EAttribute)proximityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletMotionDetector()
  {
    return mBrickletMotionDetectorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMotionDetector_DeviceType()
  {
    return (EAttribute)mBrickletMotionDetectorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletMotionDetector__Init()
  {
    return mBrickletMotionDetectorEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletHallEffect()
  {
    return mBrickletHallEffectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletHallEffect_DeviceType()
  {
    return (EAttribute)mBrickletHallEffectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletHallEffect__Init()
  {
    return mBrickletHallEffectEClass.getEOperations().get(0);
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
  public EClass getMBrickletRemoteSwitch()
  {
    return mBrickletRemoteSwitchEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletRemoteSwitch_DeviceType()
  {
    return (EAttribute)mBrickletRemoteSwitchEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletRemoteSwitch_TypeADevices()
  {
    return (EAttribute)mBrickletRemoteSwitchEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletRemoteSwitch_TypeBDevices()
  {
    return (EAttribute)mBrickletRemoteSwitchEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletRemoteSwitch_TypeCDevices()
  {
    return (EAttribute)mBrickletRemoteSwitchEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitch()
  {
    return remoteSwitchEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchA()
  {
    return remoteSwitchAEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchA_DeviceType()
  {
    return (EAttribute)remoteSwitchAEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchA_HouseCode()
  {
    return (EAttribute)remoteSwitchAEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchA_ReceiverCode()
  {
    return (EAttribute)remoteSwitchAEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchA_Repeats()
  {
    return (EAttribute)remoteSwitchAEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchB()
  {
    return remoteSwitchBEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchB_DeviceType()
  {
    return (EAttribute)remoteSwitchBEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchB_Address()
  {
    return (EAttribute)remoteSwitchBEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchB_Unit()
  {
    return (EAttribute)remoteSwitchBEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchB_Repeats()
  {
    return (EAttribute)remoteSwitchBEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchC()
  {
    return remoteSwitchCEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchC_DeviceType()
  {
    return (EAttribute)remoteSwitchCEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchC_SystemCode()
  {
    return (EAttribute)remoteSwitchCEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchC_DeviceCode()
  {
    return (EAttribute)remoteSwitchCEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchC_Repeats()
  {
    return (EAttribute)remoteSwitchCEClass.getEStructuralFeatures().get(3);
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
  public EClass getBrickletRemoteSwitchConfiguration()
  {
    return brickletRemoteSwitchConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBrickletRemoteSwitchConfiguration_TypeADevices()
  {
    return (EAttribute)brickletRemoteSwitchConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBrickletRemoteSwitchConfiguration_TypeBDevices()
  {
    return (EAttribute)brickletRemoteSwitchConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBrickletRemoteSwitchConfiguration_TypeCDevices()
  {
    return (EAttribute)brickletRemoteSwitchConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchAConfiguration()
  {
    return remoteSwitchAConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchAConfiguration_HouseCode()
  {
    return (EAttribute)remoteSwitchAConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchAConfiguration_ReceiverCode()
  {
    return (EAttribute)remoteSwitchAConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchAConfiguration_Repeats()
  {
    return (EAttribute)remoteSwitchAConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchBConfiguration()
  {
    return remoteSwitchBConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchBConfiguration_Address()
  {
    return (EAttribute)remoteSwitchBConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchBConfiguration_Unit()
  {
    return (EAttribute)remoteSwitchBConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchBConfiguration_Repeats()
  {
    return (EAttribute)remoteSwitchBConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRemoteSwitchCConfiguration()
  {
    return remoteSwitchCConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchCConfiguration_SystemCode()
  {
    return (EAttribute)remoteSwitchCConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchCConfiguration_DeviceCode()
  {
    return (EAttribute)remoteSwitchCConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRemoteSwitchCConfiguration_Repeats()
  {
    return (EAttribute)remoteSwitchCConfigurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMultiTouchDeviceConfiguration()
  {
    return multiTouchDeviceConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMultiTouchDeviceConfiguration_DisableElectrode()
  {
    return (EAttribute)multiTouchDeviceConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBrickletMultiTouchConfiguration()
  {
    return brickletMultiTouchConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBrickletMultiTouchConfiguration_Recalibrate()
  {
    return (EAttribute)brickletMultiTouchConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBrickletMultiTouchConfiguration_Sensitivity()
  {
    return (EAttribute)brickletMultiTouchConfigurationEClass.getEStructuralFeatures().get(1);
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
  public EClass getInterruptListener()
  {
    return interruptListenerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getInterruptListener_DebouncePeriod()
  {
    return (EAttribute)interruptListenerEClass.getEStructuralFeatures().get(0);
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
  public EAttribute getMBrickletHumidity_Threshold()
  {
    return (EAttribute)mBrickletHumidityEClass.getEStructuralFeatures().get(1);
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
  public EAttribute getMBrickletDistanceIR_Threshold()
  {
    return (EAttribute)mBrickletDistanceIREClass.getEStructuralFeatures().get(1);
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
  public EAttribute getMBrickletTemperature_Threshold()
  {
    return (EAttribute)mBrickletTemperatureEClass.getEStructuralFeatures().get(1);
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
  public EClass getMBrickletTemperatureIR()
  {
    return mBrickletTemperatureIREClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletTemperatureIR_DeviceType()
  {
    return (EAttribute)mBrickletTemperatureIREClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMTemperatureIRDevice()
  {
    return mTemperatureIRDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMTemperatureIRDevice_Threshold()
  {
    return (EAttribute)mTemperatureIRDeviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getObjectTemperature()
  {
    return objectTemperatureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getObjectTemperature_DeviceType()
  {
    return (EAttribute)objectTemperatureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getObjectTemperature_Emissivity()
  {
    return (EAttribute)objectTemperatureEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAmbientTemperature()
  {
    return ambientTemperatureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAmbientTemperature_DeviceType()
  {
    return (EAttribute)ambientTemperatureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletTilt()
  {
    return mBrickletTiltEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletTilt_DeviceType()
  {
    return (EAttribute)mBrickletTiltEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletVoltageCurrent()
  {
    return mBrickletVoltageCurrentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletVoltageCurrent_DeviceType()
  {
    return (EAttribute)mBrickletVoltageCurrentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletVoltageCurrent_Averaging()
  {
    return (EAttribute)mBrickletVoltageCurrentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletVoltageCurrent_VoltageConversionTime()
  {
    return (EAttribute)mBrickletVoltageCurrentEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletVoltageCurrent_CurrentConversionTime()
  {
    return (EAttribute)mBrickletVoltageCurrentEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVoltageCurrentDevice()
  {
    return voltageCurrentDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVCDeviceVoltage()
  {
    return vcDeviceVoltageEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDeviceVoltage_DeviceType()
  {
    return (EAttribute)vcDeviceVoltageEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDeviceVoltage_Threshold()
  {
    return (EAttribute)vcDeviceVoltageEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVCDeviceCurrent()
  {
    return vcDeviceCurrentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDeviceCurrent_DeviceType()
  {
    return (EAttribute)vcDeviceCurrentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDeviceCurrent_Threshold()
  {
    return (EAttribute)vcDeviceCurrentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVCDevicePower()
  {
    return vcDevicePowerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDevicePower_DeviceType()
  {
    return (EAttribute)vcDevicePowerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVCDevicePower_Threshold()
  {
    return (EAttribute)vcDevicePowerEClass.getEStructuralFeatures().get(1);
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
  public EClass getTFObjectTemperatureConfiguration()
  {
    return tfObjectTemperatureConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFObjectTemperatureConfiguration_Emissivity()
  {
    return (EAttribute)tfObjectTemperatureConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFMoistureBrickletConfiguration()
  {
    return tfMoistureBrickletConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFMoistureBrickletConfiguration_MovingAverage()
  {
    return (EAttribute)tfMoistureBrickletConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFDistanceUSBrickletConfiguration()
  {
    return tfDistanceUSBrickletConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFDistanceUSBrickletConfiguration_MovingAverage()
  {
    return (EAttribute)tfDistanceUSBrickletConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTFVoltageCurrentConfiguration()
  {
    return tfVoltageCurrentConfigurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFVoltageCurrentConfiguration_Averaging()
  {
    return (EAttribute)tfVoltageCurrentConfigurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFVoltageCurrentConfiguration_VoltageConversionTime()
  {
    return (EAttribute)tfVoltageCurrentConfigurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTFVoltageCurrentConfiguration_CurrentConversionTime()
  {
    return (EAttribute)tfVoltageCurrentConfigurationEClass.getEStructuralFeatures().get(2);
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
  public EAttribute getMBrickletBarometer_Threshold()
  {
    return (EAttribute)mBrickletBarometerEClass.getEStructuralFeatures().get(1);
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
  public EAttribute getMBrickletAmbientLight_Threshold()
  {
    return (EAttribute)mBrickletAmbientLightEClass.getEStructuralFeatures().get(1);
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
  public EClass getMBrickletSoundIntensity()
  {
    return mBrickletSoundIntensityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletSoundIntensity_DeviceType()
  {
    return (EAttribute)mBrickletSoundIntensityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletSoundIntensity_Threshold()
  {
    return (EAttribute)mBrickletSoundIntensityEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletSoundIntensity__Init()
  {
    return mBrickletSoundIntensityEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletMoisture()
  {
    return mBrickletMoistureEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMoisture_DeviceType()
  {
    return (EAttribute)mBrickletMoistureEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMoisture_Threshold()
  {
    return (EAttribute)mBrickletMoistureEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletMoisture_MovingAverage()
  {
    return (EAttribute)mBrickletMoistureEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletMoisture__Init()
  {
    return mBrickletMoistureEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMBrickletDistanceUS()
  {
    return mBrickletDistanceUSEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceUS_DeviceType()
  {
    return (EAttribute)mBrickletDistanceUSEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceUS_Threshold()
  {
    return (EAttribute)mBrickletDistanceUSEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMBrickletDistanceUS_MovingAverage()
  {
    return (EAttribute)mBrickletDistanceUSEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EOperation getMBrickletDistanceUS__Init()
  {
    return mBrickletDistanceUSEClass.getEOperations().get(0);
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
  public EClass getMLCDSubDevice()
  {
    return mlcdSubDeviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMLCD20x4Backlight()
  {
    return mlcd20x4BacklightEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMLCD20x4Backlight_DeviceType()
  {
    return (EAttribute)mlcd20x4BacklightEClass.getEStructuralFeatures().get(0);
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
  public EDataType getSwitchState()
  {
    return switchStateEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getDigitalValue()
  {
    return digitalValueEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletIO16()
  {
    return tinkerBrickletIO16EDataType;
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
  public EEnum getNoSubIds()
  {
    return noSubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getIndustrialDigitalInSubIDs()
  {
    return industrialDigitalInSubIDsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getIndustrialQuadRelayIDs()
  {
    return industrialQuadRelayIDsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getServoSubIDs()
  {
    return servoSubIDsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getBarometerSubIDs()
  {
    return barometerSubIDsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getIO16SubIds()
  {
    return io16SubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getIO4SubIds()
  {
    return io4SubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getDualRelaySubIds()
  {
    return dualRelaySubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getLCDButtonSubIds()
  {
    return lcdButtonSubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getLCDBacklightSubIds()
  {
    return lcdBacklightSubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getMultiTouchSubIds()
  {
    return multiTouchSubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getTemperatureIRSubIds()
  {
    return temperatureIRSubIdsEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getVoltageCurrentSubIds()
  {
    return voltageCurrentSubIdsEEnum;
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
  public EDataType getMTinkerforgeValue()
  {
    return mTinkerforgeValueEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getMDecimalValue()
  {
    return mDecimalValueEDataType;
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
  public EDataType getTinkerBrickletRemoteSwitch()
  {
    return tinkerBrickletRemoteSwitchEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletMotionDetector()
  {
    return tinkerBrickletMotionDetectorEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletMultiTouch()
  {
    return tinkerBrickletMultiTouchEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletTemperatureIR()
  {
    return tinkerBrickletTemperatureIREDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletSoundIntensity()
  {
    return tinkerBrickletSoundIntensityEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletMoisture()
  {
    return tinkerBrickletMoistureEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletDistanceUS()
  {
    return tinkerBrickletDistanceUSEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletVoltageCurrent()
  {
    return tinkerBrickletVoltageCurrentEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletTilt()
  {
    return tinkerBrickletTiltEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletIO4()
  {
    return tinkerBrickletIO4EDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletHallEffect()
  {
    return tinkerBrickletHallEffectEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletSegmentDisplay4x7()
  {
    return tinkerBrickletSegmentDisplay4x7EDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTinkerBrickletLEDStrip()
  {
    return tinkerBrickletLEDStripEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getHSBType()
  {
    return hsbTypeEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getDeviceOptions()
  {
    return deviceOptionsEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getEnum()
  {
    return enumEDataType;
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
  public EDataType getMTinkerBrickletIndustrialDigitalOut4()
  {
    return mTinkerBrickletIndustrialDigitalOut4EDataType;
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
    ecosystemEClass = createEClass(ECOSYSTEM);
    createEAttribute(ecosystemEClass, ECOSYSTEM__LOGGER);
    createEReference(ecosystemEClass, ECOSYSTEM__MBRICKDS);
    createEOperation(ecosystemEClass, ECOSYSTEM___GET_BRICKD__STRING_INT);
    createEOperation(ecosystemEClass, ECOSYSTEM___GET_DEVICE__STRING_STRING);
    createEOperation(ecosystemEClass, ECOSYSTEM___GET_DEVICES4_GENERIC_ID__STRING_STRING);
    createEOperation(ecosystemEClass, ECOSYSTEM___DISCONNECT);

    mBrickdEClass = createEClass(MBRICKD);
    createEAttribute(mBrickdEClass, MBRICKD__LOGGER);
    createEAttribute(mBrickdEClass, MBRICKD__IP_CONNECTION);
    createEAttribute(mBrickdEClass, MBRICKD__HOST);
    createEAttribute(mBrickdEClass, MBRICKD__PORT);
    createEAttribute(mBrickdEClass, MBRICKD__IS_CONNECTED);
    createEAttribute(mBrickdEClass, MBRICKD__AUTO_RECONNECT);
    createEAttribute(mBrickdEClass, MBRICKD__RECONNECTED);
    createEAttribute(mBrickdEClass, MBRICKD__TIMEOUT);
    createEReference(mBrickdEClass, MBRICKD__MDEVICES);
    createEReference(mBrickdEClass, MBRICKD__ECOSYSTEM);
    createEOperation(mBrickdEClass, MBRICKD___CONNECT);
    createEOperation(mBrickdEClass, MBRICKD___DISCONNECT);
    createEOperation(mBrickdEClass, MBRICKD___INIT);
    createEOperation(mBrickdEClass, MBRICKD___GET_DEVICE__STRING);

    subDeviceAdminEClass = createEClass(SUB_DEVICE_ADMIN);
    createEOperation(subDeviceAdminEClass, SUB_DEVICE_ADMIN___ADD_SUB_DEVICE__STRING_STRING);

    mtfConfigConsumerEClass = createEClass(MTF_CONFIG_CONSUMER);
    createEReference(mtfConfigConsumerEClass, MTF_CONFIG_CONSUMER__TF_CONFIG);

    mBaseDeviceEClass = createEClass(MBASE_DEVICE);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__LOGGER);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__UID);
    createEAttribute(mBaseDeviceEClass, MBASE_DEVICE__POLL);
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

    mActorEClass = createEClass(MACTOR);

    mSwitchActorEClass = createEClass(MSWITCH_ACTOR);
    createEAttribute(mSwitchActorEClass, MSWITCH_ACTOR__SWITCH_STATE);
    createEOperation(mSwitchActorEClass, MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE);
    createEOperation(mSwitchActorEClass, MSWITCH_ACTOR___FETCH_SWITCH_STATE);

    mOutSwitchActorEClass = createEClass(MOUT_SWITCH_ACTOR);

    mInSwitchActorEClass = createEClass(MIN_SWITCH_ACTOR);

    genericDeviceEClass = createEClass(GENERIC_DEVICE);
    createEAttribute(genericDeviceEClass, GENERIC_DEVICE__GENERIC_DEVICE_ID);

    ioDeviceEClass = createEClass(IO_DEVICE);

    mSubDeviceEClass = createEClass(MSUB_DEVICE);
    createEAttribute(mSubDeviceEClass, MSUB_DEVICE__SUB_ID);
    createEReference(mSubDeviceEClass, MSUB_DEVICE__MBRICK);

    callbackListenerEClass = createEClass(CALLBACK_LISTENER);
    createEAttribute(callbackListenerEClass, CALLBACK_LISTENER__CALLBACK_PERIOD);

    interruptListenerEClass = createEClass(INTERRUPT_LISTENER);
    createEAttribute(interruptListenerEClass, INTERRUPT_LISTENER__DEBOUNCE_PERIOD);

    mSensorEClass = createEClass(MSENSOR);
    createEAttribute(mSensorEClass, MSENSOR__SENSOR_VALUE);
    createEOperation(mSensorEClass, MSENSOR___FETCH_SENSOR_VALUE);

    mTextActorEClass = createEClass(MTEXT_ACTOR);
    createEAttribute(mTextActorEClass, MTEXT_ACTOR__TEXT);

    mlcdSubDeviceEClass = createEClass(MLCD_SUB_DEVICE);

    mBrickServoEClass = createEClass(MBRICK_SERVO);
    createEAttribute(mBrickServoEClass, MBRICK_SERVO__DEVICE_TYPE);
    createEOperation(mBrickServoEClass, MBRICK_SERVO___INIT);

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
    createEAttribute(mBrickletIndustrialDigitalIn4EClass, MBRICKLET_INDUSTRIAL_DIGITAL_IN4__DEVICE_TYPE);

    mIndustrialDigitalInEClass = createEClass(MINDUSTRIAL_DIGITAL_IN);

    mBrickletIndustrialDigitalOut4EClass = createEClass(MBRICKLET_INDUSTRIAL_DIGITAL_OUT4);

    digitalActorDigitalOut4EClass = createEClass(DIGITAL_ACTOR_DIGITAL_OUT4);
    createEAttribute(digitalActorDigitalOut4EClass, DIGITAL_ACTOR_DIGITAL_OUT4__PIN);

    digitalActorEClass = createEClass(DIGITAL_ACTOR);
    createEAttribute(digitalActorEClass, DIGITAL_ACTOR__DIGITAL_STATE);
    createEOperation(digitalActorEClass, DIGITAL_ACTOR___TURN_DIGITAL__HIGHLOWVALUE);
    createEOperation(digitalActorEClass, DIGITAL_ACTOR___FETCH_DIGITAL_VALUE);

    numberActorEClass = createEClass(NUMBER_ACTOR);
    createEOperation(numberActorEClass, NUMBER_ACTOR___SET_NUMBER__BIGDECIMAL);

    mBrickletSegmentDisplay4x7EClass = createEClass(MBRICKLET_SEGMENT_DISPLAY4X7);

    colorActorEClass = createEClass(COLOR_ACTOR);
    createEOperation(colorActorEClass, COLOR_ACTOR___SET_COLOR__HSBTYPE_DEVICEOPTIONS);

    mBrickletLEDStripEClass = createEClass(MBRICKLET_LED_STRIP);

    digitalActorIO16EClass = createEClass(DIGITAL_ACTOR_IO16);
    createEAttribute(digitalActorIO16EClass, DIGITAL_ACTOR_IO16__DEVICE_TYPE);
    createEAttribute(digitalActorIO16EClass, DIGITAL_ACTOR_IO16__PORT);
    createEAttribute(digitalActorIO16EClass, DIGITAL_ACTOR_IO16__PIN);
    createEAttribute(digitalActorIO16EClass, DIGITAL_ACTOR_IO16__DEFAULT_STATE);
    createEAttribute(digitalActorIO16EClass, DIGITAL_ACTOR_IO16__KEEP_ON_RECONNECT);
    createEOperation(digitalActorIO16EClass, DIGITAL_ACTOR_IO16___TURN_DIGITAL__HIGHLOWVALUE);
    createEOperation(digitalActorIO16EClass, DIGITAL_ACTOR_IO16___FETCH_DIGITAL_VALUE);

    mBrickletIO16EClass = createEClass(MBRICKLET_IO16);
    createEAttribute(mBrickletIO16EClass, MBRICKLET_IO16__DEVICE_TYPE);

    digitalSensorEClass = createEClass(DIGITAL_SENSOR);
    createEAttribute(digitalSensorEClass, DIGITAL_SENSOR__DEVICE_TYPE);
    createEAttribute(digitalSensorEClass, DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED);
    createEAttribute(digitalSensorEClass, DIGITAL_SENSOR__PORT);
    createEAttribute(digitalSensorEClass, DIGITAL_SENSOR__PIN);

    mBrickletIO4EClass = createEClass(MBRICKLET_IO4);
    createEAttribute(mBrickletIO4EClass, MBRICKLET_IO4__DEVICE_TYPE);

    io4DeviceEClass = createEClass(IO4_DEVICE);

    digitalSensorIO4EClass = createEClass(DIGITAL_SENSOR_IO4);
    createEAttribute(digitalSensorIO4EClass, DIGITAL_SENSOR_IO4__DEVICE_TYPE);
    createEAttribute(digitalSensorIO4EClass, DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED);
    createEAttribute(digitalSensorIO4EClass, DIGITAL_SENSOR_IO4__PIN);

    digitalActorIO4EClass = createEClass(DIGITAL_ACTOR_IO4);
    createEAttribute(digitalActorIO4EClass, DIGITAL_ACTOR_IO4__DEVICE_TYPE);
    createEAttribute(digitalActorIO4EClass, DIGITAL_ACTOR_IO4__PIN);
    createEAttribute(digitalActorIO4EClass, DIGITAL_ACTOR_IO4__DEFAULT_STATE);
    createEAttribute(digitalActorIO4EClass, DIGITAL_ACTOR_IO4__KEEP_ON_RECONNECT);
    createEOperation(digitalActorIO4EClass, DIGITAL_ACTOR_IO4___TURN_DIGITAL__HIGHLOWVALUE);
    createEOperation(digitalActorIO4EClass, DIGITAL_ACTOR_IO4___FETCH_DIGITAL_VALUE);

    mBrickletMultiTouchEClass = createEClass(MBRICKLET_MULTI_TOUCH);
    createEAttribute(mBrickletMultiTouchEClass, MBRICKLET_MULTI_TOUCH__DEVICE_TYPE);
    createEAttribute(mBrickletMultiTouchEClass, MBRICKLET_MULTI_TOUCH__RECALIBRATE);
    createEAttribute(mBrickletMultiTouchEClass, MBRICKLET_MULTI_TOUCH__SENSITIVITY);

    multiTouchDeviceEClass = createEClass(MULTI_TOUCH_DEVICE);
    createEAttribute(multiTouchDeviceEClass, MULTI_TOUCH_DEVICE__PIN);
    createEAttribute(multiTouchDeviceEClass, MULTI_TOUCH_DEVICE__DISABLE_ELECTRODE);

    electrodeEClass = createEClass(ELECTRODE);
    createEAttribute(electrodeEClass, ELECTRODE__DEVICE_TYPE);

    proximityEClass = createEClass(PROXIMITY);
    createEAttribute(proximityEClass, PROXIMITY__DEVICE_TYPE);

    mBrickletMotionDetectorEClass = createEClass(MBRICKLET_MOTION_DETECTOR);
    createEAttribute(mBrickletMotionDetectorEClass, MBRICKLET_MOTION_DETECTOR__DEVICE_TYPE);
    createEOperation(mBrickletMotionDetectorEClass, MBRICKLET_MOTION_DETECTOR___INIT);

    mBrickletHallEffectEClass = createEClass(MBRICKLET_HALL_EFFECT);
    createEAttribute(mBrickletHallEffectEClass, MBRICKLET_HALL_EFFECT__DEVICE_TYPE);
    createEOperation(mBrickletHallEffectEClass, MBRICKLET_HALL_EFFECT___INIT);

    mDualRelayEClass = createEClass(MDUAL_RELAY);
    createEAttribute(mDualRelayEClass, MDUAL_RELAY__DEVICE_TYPE);

    mBrickletRemoteSwitchEClass = createEClass(MBRICKLET_REMOTE_SWITCH);
    createEAttribute(mBrickletRemoteSwitchEClass, MBRICKLET_REMOTE_SWITCH__DEVICE_TYPE);
    createEAttribute(mBrickletRemoteSwitchEClass, MBRICKLET_REMOTE_SWITCH__TYPE_ADEVICES);
    createEAttribute(mBrickletRemoteSwitchEClass, MBRICKLET_REMOTE_SWITCH__TYPE_BDEVICES);
    createEAttribute(mBrickletRemoteSwitchEClass, MBRICKLET_REMOTE_SWITCH__TYPE_CDEVICES);

    remoteSwitchEClass = createEClass(REMOTE_SWITCH);

    remoteSwitchAEClass = createEClass(REMOTE_SWITCH_A);
    createEAttribute(remoteSwitchAEClass, REMOTE_SWITCH_A__DEVICE_TYPE);
    createEAttribute(remoteSwitchAEClass, REMOTE_SWITCH_A__HOUSE_CODE);
    createEAttribute(remoteSwitchAEClass, REMOTE_SWITCH_A__RECEIVER_CODE);
    createEAttribute(remoteSwitchAEClass, REMOTE_SWITCH_A__REPEATS);

    remoteSwitchBEClass = createEClass(REMOTE_SWITCH_B);
    createEAttribute(remoteSwitchBEClass, REMOTE_SWITCH_B__DEVICE_TYPE);
    createEAttribute(remoteSwitchBEClass, REMOTE_SWITCH_B__ADDRESS);
    createEAttribute(remoteSwitchBEClass, REMOTE_SWITCH_B__UNIT);
    createEAttribute(remoteSwitchBEClass, REMOTE_SWITCH_B__REPEATS);

    remoteSwitchCEClass = createEClass(REMOTE_SWITCH_C);
    createEAttribute(remoteSwitchCEClass, REMOTE_SWITCH_C__DEVICE_TYPE);
    createEAttribute(remoteSwitchCEClass, REMOTE_SWITCH_C__SYSTEM_CODE);
    createEAttribute(remoteSwitchCEClass, REMOTE_SWITCH_C__DEVICE_CODE);
    createEAttribute(remoteSwitchCEClass, REMOTE_SWITCH_C__REPEATS);

    mBrickletHumidityEClass = createEClass(MBRICKLET_HUMIDITY);
    createEAttribute(mBrickletHumidityEClass, MBRICKLET_HUMIDITY__DEVICE_TYPE);
    createEAttribute(mBrickletHumidityEClass, MBRICKLET_HUMIDITY__THRESHOLD);
    createEOperation(mBrickletHumidityEClass, MBRICKLET_HUMIDITY___INIT);

    mBrickletDistanceIREClass = createEClass(MBRICKLET_DISTANCE_IR);
    createEAttribute(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR__DEVICE_TYPE);
    createEAttribute(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR__THRESHOLD);
    createEOperation(mBrickletDistanceIREClass, MBRICKLET_DISTANCE_IR___INIT);

    mBrickletTemperatureEClass = createEClass(MBRICKLET_TEMPERATURE);
    createEAttribute(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE__DEVICE_TYPE);
    createEAttribute(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE__THRESHOLD);
    createEOperation(mBrickletTemperatureEClass, MBRICKLET_TEMPERATURE___INIT);

    mBrickletTemperatureIREClass = createEClass(MBRICKLET_TEMPERATURE_IR);
    createEAttribute(mBrickletTemperatureIREClass, MBRICKLET_TEMPERATURE_IR__DEVICE_TYPE);

    mTemperatureIRDeviceEClass = createEClass(MTEMPERATURE_IR_DEVICE);
    createEAttribute(mTemperatureIRDeviceEClass, MTEMPERATURE_IR_DEVICE__THRESHOLD);

    objectTemperatureEClass = createEClass(OBJECT_TEMPERATURE);
    createEAttribute(objectTemperatureEClass, OBJECT_TEMPERATURE__DEVICE_TYPE);
    createEAttribute(objectTemperatureEClass, OBJECT_TEMPERATURE__EMISSIVITY);

    ambientTemperatureEClass = createEClass(AMBIENT_TEMPERATURE);
    createEAttribute(ambientTemperatureEClass, AMBIENT_TEMPERATURE__DEVICE_TYPE);

    mBrickletTiltEClass = createEClass(MBRICKLET_TILT);
    createEAttribute(mBrickletTiltEClass, MBRICKLET_TILT__DEVICE_TYPE);

    mBrickletVoltageCurrentEClass = createEClass(MBRICKLET_VOLTAGE_CURRENT);
    createEAttribute(mBrickletVoltageCurrentEClass, MBRICKLET_VOLTAGE_CURRENT__DEVICE_TYPE);
    createEAttribute(mBrickletVoltageCurrentEClass, MBRICKLET_VOLTAGE_CURRENT__AVERAGING);
    createEAttribute(mBrickletVoltageCurrentEClass, MBRICKLET_VOLTAGE_CURRENT__VOLTAGE_CONVERSION_TIME);
    createEAttribute(mBrickletVoltageCurrentEClass, MBRICKLET_VOLTAGE_CURRENT__CURRENT_CONVERSION_TIME);

    voltageCurrentDeviceEClass = createEClass(VOLTAGE_CURRENT_DEVICE);

    vcDeviceVoltageEClass = createEClass(VC_DEVICE_VOLTAGE);
    createEAttribute(vcDeviceVoltageEClass, VC_DEVICE_VOLTAGE__DEVICE_TYPE);
    createEAttribute(vcDeviceVoltageEClass, VC_DEVICE_VOLTAGE__THRESHOLD);

    vcDeviceCurrentEClass = createEClass(VC_DEVICE_CURRENT);
    createEAttribute(vcDeviceCurrentEClass, VC_DEVICE_CURRENT__DEVICE_TYPE);
    createEAttribute(vcDeviceCurrentEClass, VC_DEVICE_CURRENT__THRESHOLD);

    vcDevicePowerEClass = createEClass(VC_DEVICE_POWER);
    createEAttribute(vcDevicePowerEClass, VC_DEVICE_POWER__DEVICE_TYPE);
    createEAttribute(vcDevicePowerEClass, VC_DEVICE_POWER__THRESHOLD);

    mBrickletBarometerEClass = createEClass(MBRICKLET_BAROMETER);
    createEAttribute(mBrickletBarometerEClass, MBRICKLET_BAROMETER__DEVICE_TYPE);
    createEAttribute(mBrickletBarometerEClass, MBRICKLET_BAROMETER__THRESHOLD);
    createEOperation(mBrickletBarometerEClass, MBRICKLET_BAROMETER___INIT);

    mBarometerTemperatureEClass = createEClass(MBAROMETER_TEMPERATURE);
    createEAttribute(mBarometerTemperatureEClass, MBAROMETER_TEMPERATURE__DEVICE_TYPE);
    createEOperation(mBarometerTemperatureEClass, MBAROMETER_TEMPERATURE___INIT);

    mBrickletAmbientLightEClass = createEClass(MBRICKLET_AMBIENT_LIGHT);
    createEAttribute(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT__DEVICE_TYPE);
    createEAttribute(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT__THRESHOLD);
    createEOperation(mBrickletAmbientLightEClass, MBRICKLET_AMBIENT_LIGHT___INIT);

    mBrickletSoundIntensityEClass = createEClass(MBRICKLET_SOUND_INTENSITY);
    createEAttribute(mBrickletSoundIntensityEClass, MBRICKLET_SOUND_INTENSITY__DEVICE_TYPE);
    createEAttribute(mBrickletSoundIntensityEClass, MBRICKLET_SOUND_INTENSITY__THRESHOLD);
    createEOperation(mBrickletSoundIntensityEClass, MBRICKLET_SOUND_INTENSITY___INIT);

    mBrickletMoistureEClass = createEClass(MBRICKLET_MOISTURE);
    createEAttribute(mBrickletMoistureEClass, MBRICKLET_MOISTURE__DEVICE_TYPE);
    createEAttribute(mBrickletMoistureEClass, MBRICKLET_MOISTURE__THRESHOLD);
    createEAttribute(mBrickletMoistureEClass, MBRICKLET_MOISTURE__MOVING_AVERAGE);
    createEOperation(mBrickletMoistureEClass, MBRICKLET_MOISTURE___INIT);

    mBrickletDistanceUSEClass = createEClass(MBRICKLET_DISTANCE_US);
    createEAttribute(mBrickletDistanceUSEClass, MBRICKLET_DISTANCE_US__DEVICE_TYPE);
    createEAttribute(mBrickletDistanceUSEClass, MBRICKLET_DISTANCE_US__THRESHOLD);
    createEAttribute(mBrickletDistanceUSEClass, MBRICKLET_DISTANCE_US__MOVING_AVERAGE);
    createEOperation(mBrickletDistanceUSEClass, MBRICKLET_DISTANCE_US___INIT);

    mBrickletLCD20x4EClass = createEClass(MBRICKLET_LCD2_0X4);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__DEVICE_TYPE);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__POSITION_PREFIX);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__POSITON_SUFFIX);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__DISPLAY_ERRORS);
    createEAttribute(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4__ERROR_PREFIX);
    createEOperation(mBrickletLCD20x4EClass, MBRICKLET_LCD2_0X4___INIT);

    mlcd20x4BacklightEClass = createEClass(MLCD2_0X4_BACKLIGHT);
    createEAttribute(mlcd20x4BacklightEClass, MLCD2_0X4_BACKLIGHT__DEVICE_TYPE);

    mlcd20x4ButtonEClass = createEClass(MLCD2_0X4_BUTTON);
    createEAttribute(mlcd20x4ButtonEClass, MLCD2_0X4_BUTTON__DEVICE_TYPE);
    createEAttribute(mlcd20x4ButtonEClass, MLCD2_0X4_BUTTON__BUTTON_NUM);

    tfConfigEClass = createEClass(TF_CONFIG);

    ohtfDeviceEClass = createEClass(OHTF_DEVICE);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__UID);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__SUBID);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__OHID);
    createEAttribute(ohtfDeviceEClass, OHTF_DEVICE__SUB_DEVICE_IDS);
    createEReference(ohtfDeviceEClass, OHTF_DEVICE__TF_CONFIG);
    createEReference(ohtfDeviceEClass, OHTF_DEVICE__OH_CONFIG);
    createEOperation(ohtfDeviceEClass, OHTF_DEVICE___IS_VALID_SUB_ID__STRING);

    ohtfSubDeviceAdminDeviceEClass = createEClass(OHTF_SUB_DEVICE_ADMIN_DEVICE);
    createEOperation(ohtfSubDeviceAdminDeviceEClass, OHTF_SUB_DEVICE_ADMIN_DEVICE___IS_VALID_SUB_ID__STRING);

    ohConfigEClass = createEClass(OH_CONFIG);
    createEReference(ohConfigEClass, OH_CONFIG__OH_TF_DEVICES);
    createEOperation(ohConfigEClass, OH_CONFIG___GET_CONFIG_BY_TF_ID__STRING_STRING);
    createEOperation(ohConfigEClass, OH_CONFIG___GET_CONFIG_BY_OH_ID__STRING);

    tfNullConfigurationEClass = createEClass(TF_NULL_CONFIGURATION);

    tfBaseConfigurationEClass = createEClass(TF_BASE_CONFIGURATION);
    createEAttribute(tfBaseConfigurationEClass, TF_BASE_CONFIGURATION__THRESHOLD);
    createEAttribute(tfBaseConfigurationEClass, TF_BASE_CONFIGURATION__CALLBACK_PERIOD);

    tfObjectTemperatureConfigurationEClass = createEClass(TF_OBJECT_TEMPERATURE_CONFIGURATION);
    createEAttribute(tfObjectTemperatureConfigurationEClass, TF_OBJECT_TEMPERATURE_CONFIGURATION__EMISSIVITY);

    tfMoistureBrickletConfigurationEClass = createEClass(TF_MOISTURE_BRICKLET_CONFIGURATION);
    createEAttribute(tfMoistureBrickletConfigurationEClass, TF_MOISTURE_BRICKLET_CONFIGURATION__MOVING_AVERAGE);

    tfDistanceUSBrickletConfigurationEClass = createEClass(TF_DISTANCE_US_BRICKLET_CONFIGURATION);
    createEAttribute(tfDistanceUSBrickletConfigurationEClass, TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE);

    tfVoltageCurrentConfigurationEClass = createEClass(TF_VOLTAGE_CURRENT_CONFIGURATION);
    createEAttribute(tfVoltageCurrentConfigurationEClass, TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING);
    createEAttribute(tfVoltageCurrentConfigurationEClass, TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME);
    createEAttribute(tfVoltageCurrentConfigurationEClass, TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME);

    tfBrickDCConfigurationEClass = createEClass(TF_BRICK_DC_CONFIGURATION);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__VELOCITY);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__ACCELERATION);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__DRIVE_MODE);
    createEAttribute(tfBrickDCConfigurationEClass, TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY);

    tfioActorConfigurationEClass = createEClass(TFIO_ACTOR_CONFIGURATION);
    createEAttribute(tfioActorConfigurationEClass, TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE);
    createEAttribute(tfioActorConfigurationEClass, TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT);

    tfInterruptListenerConfigurationEClass = createEClass(TF_INTERRUPT_LISTENER_CONFIGURATION);
    createEAttribute(tfInterruptListenerConfigurationEClass, TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD);

    tfioSensorConfigurationEClass = createEClass(TFIO_SENSOR_CONFIGURATION);
    createEAttribute(tfioSensorConfigurationEClass, TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED);

    tfServoConfigurationEClass = createEClass(TF_SERVO_CONFIGURATION);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__VELOCITY);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__ACCELERATION);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__SERVO_VOLTAGE);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__PERIOD);
    createEAttribute(tfServoConfigurationEClass, TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE);

    brickletRemoteSwitchConfigurationEClass = createEClass(BRICKLET_REMOTE_SWITCH_CONFIGURATION);
    createEAttribute(brickletRemoteSwitchConfigurationEClass, BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_ADEVICES);
    createEAttribute(brickletRemoteSwitchConfigurationEClass, BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_BDEVICES);
    createEAttribute(brickletRemoteSwitchConfigurationEClass, BRICKLET_REMOTE_SWITCH_CONFIGURATION__TYPE_CDEVICES);

    remoteSwitchAConfigurationEClass = createEClass(REMOTE_SWITCH_ACONFIGURATION);
    createEAttribute(remoteSwitchAConfigurationEClass, REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE);
    createEAttribute(remoteSwitchAConfigurationEClass, REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE);
    createEAttribute(remoteSwitchAConfigurationEClass, REMOTE_SWITCH_ACONFIGURATION__REPEATS);

    remoteSwitchBConfigurationEClass = createEClass(REMOTE_SWITCH_BCONFIGURATION);
    createEAttribute(remoteSwitchBConfigurationEClass, REMOTE_SWITCH_BCONFIGURATION__ADDRESS);
    createEAttribute(remoteSwitchBConfigurationEClass, REMOTE_SWITCH_BCONFIGURATION__UNIT);
    createEAttribute(remoteSwitchBConfigurationEClass, REMOTE_SWITCH_BCONFIGURATION__REPEATS);

    remoteSwitchCConfigurationEClass = createEClass(REMOTE_SWITCH_CCONFIGURATION);
    createEAttribute(remoteSwitchCConfigurationEClass, REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE);
    createEAttribute(remoteSwitchCConfigurationEClass, REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE);
    createEAttribute(remoteSwitchCConfigurationEClass, REMOTE_SWITCH_CCONFIGURATION__REPEATS);

    multiTouchDeviceConfigurationEClass = createEClass(MULTI_TOUCH_DEVICE_CONFIGURATION);
    createEAttribute(multiTouchDeviceConfigurationEClass, MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE);

    brickletMultiTouchConfigurationEClass = createEClass(BRICKLET_MULTI_TOUCH_CONFIGURATION);
    createEAttribute(brickletMultiTouchConfigurationEClass, BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE);
    createEAttribute(brickletMultiTouchConfigurationEClass, BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY);

    // Create enums
    dcDriveModeEEnum = createEEnum(DC_DRIVE_MODE);
    noSubIdsEEnum = createEEnum(NO_SUB_IDS);
    industrialDigitalInSubIDsEEnum = createEEnum(INDUSTRIAL_DIGITAL_IN_SUB_IDS);
    industrialQuadRelayIDsEEnum = createEEnum(INDUSTRIAL_QUAD_RELAY_IDS);
    servoSubIDsEEnum = createEEnum(SERVO_SUB_IDS);
    barometerSubIDsEEnum = createEEnum(BAROMETER_SUB_IDS);
    io16SubIdsEEnum = createEEnum(IO16_SUB_IDS);
    io4SubIdsEEnum = createEEnum(IO4_SUB_IDS);
    dualRelaySubIdsEEnum = createEEnum(DUAL_RELAY_SUB_IDS);
    lcdButtonSubIdsEEnum = createEEnum(LCD_BUTTON_SUB_IDS);
    lcdBacklightSubIdsEEnum = createEEnum(LCD_BACKLIGHT_SUB_IDS);
    multiTouchSubIdsEEnum = createEEnum(MULTI_TOUCH_SUB_IDS);
    temperatureIRSubIdsEEnum = createEEnum(TEMPERATURE_IR_SUB_IDS);
    voltageCurrentSubIdsEEnum = createEEnum(VOLTAGE_CURRENT_SUB_IDS);

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
    mTinkerBrickletIndustrialDigitalOut4EDataType = createEDataType(MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_OUT4);
    switchStateEDataType = createEDataType(SWITCH_STATE);
    digitalValueEDataType = createEDataType(DIGITAL_VALUE);
    tinkerBrickletIO16EDataType = createEDataType(TINKER_BRICKLET_IO16);
    mTinkerBrickServoEDataType = createEDataType(MTINKER_BRICK_SERVO);
    mTinkerforgeValueEDataType = createEDataType(MTINKERFORGE_VALUE);
    mDecimalValueEDataType = createEDataType(MDECIMAL_VALUE);
    mTinkerBrickletHumidityEDataType = createEDataType(MTINKER_BRICKLET_HUMIDITY);
    mTinkerBrickletDistanceIREDataType = createEDataType(MTINKER_BRICKLET_DISTANCE_IR);
    mTinkerBrickletTemperatureEDataType = createEDataType(MTINKER_BRICKLET_TEMPERATURE);
    mTinkerBrickletBarometerEDataType = createEDataType(MTINKER_BRICKLET_BAROMETER);
    mTinkerBrickletAmbientLightEDataType = createEDataType(MTINKER_BRICKLET_AMBIENT_LIGHT);
    mTinkerBrickletLCD20x4EDataType = createEDataType(MTINKER_BRICKLET_LCD2_0X4);
    tinkerBrickletRemoteSwitchEDataType = createEDataType(TINKER_BRICKLET_REMOTE_SWITCH);
    tinkerBrickletMotionDetectorEDataType = createEDataType(TINKER_BRICKLET_MOTION_DETECTOR);
    tinkerBrickletMultiTouchEDataType = createEDataType(TINKER_BRICKLET_MULTI_TOUCH);
    tinkerBrickletTemperatureIREDataType = createEDataType(TINKER_BRICKLET_TEMPERATURE_IR);
    tinkerBrickletSoundIntensityEDataType = createEDataType(TINKER_BRICKLET_SOUND_INTENSITY);
    tinkerBrickletMoistureEDataType = createEDataType(TINKER_BRICKLET_MOISTURE);
    tinkerBrickletDistanceUSEDataType = createEDataType(TINKER_BRICKLET_DISTANCE_US);
    tinkerBrickletVoltageCurrentEDataType = createEDataType(TINKER_BRICKLET_VOLTAGE_CURRENT);
    tinkerBrickletTiltEDataType = createEDataType(TINKER_BRICKLET_TILT);
    tinkerBrickletIO4EDataType = createEDataType(TINKER_BRICKLET_IO4);
    tinkerBrickletHallEffectEDataType = createEDataType(TINKER_BRICKLET_HALL_EFFECT);
    tinkerBrickletSegmentDisplay4x7EDataType = createEDataType(TINKER_BRICKLET_SEGMENT_DISPLAY4X7);
    tinkerBrickletLEDStripEDataType = createEDataType(TINKER_BRICKLET_LED_STRIP);
    hsbTypeEDataType = createEDataType(HSB_TYPE);
    deviceOptionsEDataType = createEDataType(DEVICE_OPTIONS);
    enumEDataType = createEDataType(ENUM);
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
    ETypeParameter mtfConfigConsumerEClass_TFC = addETypeParameter(mtfConfigConsumerEClass, "TFC");
    ETypeParameter mDeviceEClass_TF = addETypeParameter(mDeviceEClass, "TF");
    ETypeParameter mSubDeviceHolderEClass_S = addETypeParameter(mSubDeviceHolderEClass, "S");
    ETypeParameter mSubDeviceEClass_B = addETypeParameter(mSubDeviceEClass, "B");
    ETypeParameter mSensorEClass_DeviceValue = addETypeParameter(mSensorEClass, "DeviceValue");
    ETypeParameter ohtfDeviceEClass_TFC = addETypeParameter(ohtfDeviceEClass, "TFC");
    ETypeParameter ohtfDeviceEClass_IDS = addETypeParameter(ohtfDeviceEClass, "IDS");
    ETypeParameter ohtfSubDeviceAdminDeviceEClass_TFC = addETypeParameter(ohtfSubDeviceAdminDeviceEClass, "TFC");
    ETypeParameter ohtfSubDeviceAdminDeviceEClass_IDS = addETypeParameter(ohtfSubDeviceAdminDeviceEClass, "IDS");

    // Set bounds for type parameters
    EGenericType g1 = createEGenericType(this.getMTinkerforgeDevice());
    mDeviceEClass_TF.getEBounds().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    EGenericType g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    mSubDeviceHolderEClass_S.getEBounds().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    mSubDeviceEClass_B.getEBounds().add(g1);
    g1 = createEGenericType(this.getMTinkerforgeValue());
    mSensorEClass_DeviceValue.getEBounds().add(g1);
    g1 = createEGenericType(this.getTFConfig());
    ohtfDeviceEClass_TFC.getEBounds().add(g1);
    g1 = createEGenericType(this.getEnum());
    ohtfDeviceEClass_IDS.getEBounds().add(g1);
    g1 = createEGenericType(this.getTFConfig());
    ohtfSubDeviceAdminDeviceEClass_TFC.getEBounds().add(g1);
    g1 = createEGenericType(this.getEnum());
    ohtfSubDeviceAdminDeviceEClass_IDS.getEBounds().add(g1);

    // Add supertypes to classes
    mDeviceEClass.getESuperTypes().add(this.getMBaseDevice());
    mOutSwitchActorEClass.getESuperTypes().add(this.getMSwitchActor());
    mInSwitchActorEClass.getESuperTypes().add(this.getMSwitchActor());
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletIO16());
    g1.getETypeArguments().add(g2);
    ioDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getGenericDevice());
    ioDeviceEClass.getEGenericSuperTypes().add(g1);
    mSubDeviceEClass.getESuperTypes().add(this.getMBaseDevice());
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletLCD20x4());
    g1.getETypeArguments().add(g2);
    mlcdSubDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickServo());
    g1.getETypeArguments().add(g2);
    mBrickServoEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMServo());
    g1.getETypeArguments().add(g2);
    mBrickServoEClass.getEGenericSuperTypes().add(g1);
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
    g1 = createEGenericType(this.getInterruptListener());
    mBrickletIndustrialDigitalIn4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFInterruptListenerConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletIndustrialDigitalIn4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletIndustrialDigitalIn4());
    g1.getETypeArguments().add(g2);
    mIndustrialDigitalInEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    mIndustrialDigitalInEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletIndustrialDigitalOut4());
    g1.getETypeArguments().add(g2);
    mBrickletIndustrialDigitalOut4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getDigitalActorDigitalOut4());
    g1.getETypeArguments().add(g2);
    mBrickletIndustrialDigitalOut4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getDigitalActor());
    digitalActorDigitalOut4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletIndustrialDigitalOut4());
    g1.getETypeArguments().add(g2);
    digitalActorDigitalOut4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getNumberActor());
    mBrickletSegmentDisplay4x7EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletSegmentDisplay4x7());
    g1.getETypeArguments().add(g2);
    mBrickletSegmentDisplay4x7EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getColorActor());
    mBrickletLEDStripEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletLEDStrip());
    g1.getETypeArguments().add(g2);
    mBrickletLEDStripEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getDigitalActor());
    digitalActorIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getIODevice());
    digitalActorIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFIOActorConfiguration());
    g1.getETypeArguments().add(g2);
    digitalActorIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletIO16());
    g1.getETypeArguments().add(g2);
    mBrickletIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getIODevice());
    g1.getETypeArguments().add(g2);
    mBrickletIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getInterruptListener());
    mBrickletIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFInterruptListenerConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletIO16EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getIODevice());
    digitalSensorEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    digitalSensorEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFIOSensorConfiguration());
    g1.getETypeArguments().add(g2);
    digitalSensorEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletIO4());
    g1.getETypeArguments().add(g2);
    mBrickletIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getIO4Device());
    g1.getETypeArguments().add(g2);
    mBrickletIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getInterruptListener());
    mBrickletIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFInterruptListenerConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletIO4());
    g1.getETypeArguments().add(g2);
    io4DeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getGenericDevice());
    io4DeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getIO4Device());
    digitalSensorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    digitalSensorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFIOSensorConfiguration());
    g1.getETypeArguments().add(g2);
    digitalSensorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getDigitalActor());
    digitalActorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getIO4Device());
    digitalActorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFIOActorConfiguration());
    g1.getETypeArguments().add(g2);
    digitalActorIO4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletMultiTouch());
    g1.getETypeArguments().add(g2);
    mBrickletMultiTouchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMultiTouchDevice());
    g1.getETypeArguments().add(g2);
    mBrickletMultiTouchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getBrickletMultiTouchConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletMultiTouchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletMultiTouch());
    g1.getETypeArguments().add(g2);
    multiTouchDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    multiTouchDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getMultiTouchDeviceConfiguration());
    g1.getETypeArguments().add(g2);
    multiTouchDeviceEClass.getEGenericSuperTypes().add(g1);
    electrodeEClass.getESuperTypes().add(this.getMultiTouchDevice());
    proximityEClass.getESuperTypes().add(this.getMultiTouchDevice());
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletMotionDetector());
    g1.getETypeArguments().add(g2);
    mBrickletMotionDetectorEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    mBrickletMotionDetectorEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletHallEffect());
    g1.getETypeArguments().add(g2);
    mBrickletHallEffectEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getDigitalValue());
    g1.getETypeArguments().add(g2);
    mBrickletHallEffectEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletHallEffectEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletHallEffectEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMInSwitchActor());
    mDualRelayEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMDualRelayBricklet());
    g1.getETypeArguments().add(g2);
    mDualRelayEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletRemoteSwitch());
    g1.getETypeArguments().add(g2);
    mBrickletRemoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getRemoteSwitch());
    g1.getETypeArguments().add(g2);
    mBrickletRemoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getSubDeviceAdmin());
    mBrickletRemoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getBrickletRemoteSwitchConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletRemoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMInSwitchActor());
    remoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletRemoteSwitch());
    g1.getETypeArguments().add(g2);
    remoteSwitchEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getRemoteSwitch());
    remoteSwitchAEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getRemoteSwitchAConfiguration());
    g1.getETypeArguments().add(g2);
    remoteSwitchAEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getRemoteSwitch());
    remoteSwitchBEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getRemoteSwitchBConfiguration());
    g1.getETypeArguments().add(g2);
    remoteSwitchBEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getRemoteSwitch());
    remoteSwitchCEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getRemoteSwitchCConfiguration());
    g1.getETypeArguments().add(g2);
    remoteSwitchCEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
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
    g2 = createEGenericType(this.getMDecimalValue());
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
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletTemperatureIR());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMTemperatureIRDevice());
    g1.getETypeArguments().add(g2);
    mBrickletTemperatureIREClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mTemperatureIRDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletTemperatureIR());
    g1.getETypeArguments().add(g2);
    mTemperatureIRDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mTemperatureIRDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTemperatureIRDevice());
    objectTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFObjectTemperatureConfiguration());
    g1.getETypeArguments().add(g2);
    objectTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTemperatureIRDevice());
    ambientTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    ambientTemperatureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletTilt());
    g1.getETypeArguments().add(g2);
    mBrickletTiltEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletTiltEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletVoltageCurrent());
    g1.getETypeArguments().add(g2);
    mBrickletVoltageCurrentEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getVoltageCurrentDevice());
    g1.getETypeArguments().add(g2);
    mBrickletVoltageCurrentEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFVoltageCurrentConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletVoltageCurrentEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    voltageCurrentDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType(this.getMBrickletVoltageCurrent());
    g1.getETypeArguments().add(g2);
    voltageCurrentDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    voltageCurrentDeviceEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    voltageCurrentDeviceEClass.getEGenericSuperTypes().add(g1);
    vcDeviceVoltageEClass.getESuperTypes().add(this.getVoltageCurrentDevice());
    vcDeviceCurrentEClass.getESuperTypes().add(this.getVoltageCurrentDevice());
    vcDevicePowerEClass.getESuperTypes().add(this.getVoltageCurrentDevice());
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletBarometer());
    g1.getETypeArguments().add(g2);
    mBrickletBarometerEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
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
    g2 = createEGenericType(this.getMDecimalValue());
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
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletAmbientLightEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletSoundIntensity());
    g1.getETypeArguments().add(g2);
    mBrickletSoundIntensityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletSoundIntensityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFBaseConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletSoundIntensityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletSoundIntensityEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletMoisture());
    g1.getETypeArguments().add(g2);
    mBrickletMoistureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletMoistureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFMoistureBrickletConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletMoistureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletMoistureEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getTinkerBrickletDistanceUS());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceUSEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSensor());
    g2 = createEGenericType(this.getMDecimalValue());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceUSEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTFConfigConsumer());
    g2 = createEGenericType(this.getTFDistanceUSBrickletConfiguration());
    g1.getETypeArguments().add(g2);
    mBrickletDistanceUSEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getCallbackListener());
    mBrickletDistanceUSEClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMDevice());
    g2 = createEGenericType(this.getMTinkerBrickletLCD20x4());
    g1.getETypeArguments().add(g2);
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMTextActor());
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    g1 = createEGenericType(this.getMSubDeviceHolder());
    g2 = createEGenericType(this.getMLCDSubDevice());
    g1.getETypeArguments().add(g2);
    mBrickletLCD20x4EClass.getEGenericSuperTypes().add(g1);
    mlcd20x4BacklightEClass.getESuperTypes().add(this.getMInSwitchActor());
    mlcd20x4BacklightEClass.getESuperTypes().add(this.getMLCDSubDevice());
    mlcd20x4ButtonEClass.getESuperTypes().add(this.getMOutSwitchActor());
    mlcd20x4ButtonEClass.getESuperTypes().add(this.getMLCDSubDevice());
    mlcd20x4ButtonEClass.getESuperTypes().add(this.getCallbackListener());
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType(ohtfSubDeviceAdminDeviceEClass_TFC);
    g1.getETypeArguments().add(g2);
    g2 = createEGenericType(ohtfSubDeviceAdminDeviceEClass_IDS);
    g1.getETypeArguments().add(g2);
    ohtfSubDeviceAdminDeviceEClass.getEGenericSuperTypes().add(g1);
    tfNullConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfBaseConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfObjectTemperatureConfigurationEClass.getESuperTypes().add(this.getTFBaseConfiguration());
    tfMoistureBrickletConfigurationEClass.getESuperTypes().add(this.getTFBaseConfiguration());
    tfDistanceUSBrickletConfigurationEClass.getESuperTypes().add(this.getTFBaseConfiguration());
    tfVoltageCurrentConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfBrickDCConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfioActorConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfInterruptListenerConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfioSensorConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    tfServoConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    brickletRemoteSwitchConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    remoteSwitchAConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    remoteSwitchBConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    remoteSwitchCConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    multiTouchDeviceConfigurationEClass.getESuperTypes().add(this.getTFConfig());
    brickletMultiTouchConfigurationEClass.getESuperTypes().add(this.getTFConfig());

    // Initialize classes, features, and operations; add parameters
    initEClass(ecosystemEClass, Ecosystem.class, "Ecosystem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEcosystem_Logger(), this.getMLogger(), "logger", null, 0, 1, Ecosystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getEcosystem_Mbrickds(), this.getMBrickd(), this.getMBrickd_Ecosystem(), "mbrickds", null, 0, -1, Ecosystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getEcosystem__GetBrickd__String_int(), this.getMBrickd(), "getBrickd", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "host", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEInt(), "port", 0, 1, !IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getEcosystem__GetDevice__String_String(), this.getMBaseDevice(), "getDevice", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subId", 0, 1, !IS_UNIQUE, IS_ORDERED);

    op = initEOperation(getEcosystem__GetDevices4GenericId__String_String(), null, "getDevices4GenericId", 0, -1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "genericId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(this.getMSubDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEOperation(op, g1);

    initEOperation(getEcosystem__Disconnect(), null, "disconnect", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickdEClass, MBrickd.class, "MBrickd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickd_Logger(), this.getMLogger(), "logger", null, 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_IpConnection(), this.getMIPConnection(), "ipConnection", null, 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Host(), theEcorePackage.getEString(), "host", "localhost", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Port(), theEcorePackage.getEInt(), "port", "4223", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_IsConnected(), theEcorePackage.getEBoolean(), "isConnected", "false", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_AutoReconnect(), theEcorePackage.getEBoolean(), "autoReconnect", "true", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickd_Reconnected(), theEcorePackage.getEBoolean(), "reconnected", "false", 0, 1, MBrickd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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

    initEClass(subDeviceAdminEClass, SubDeviceAdmin.class, "SubDeviceAdmin", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    op = initEOperation(getSubDeviceAdmin__AddSubDevice__String_String(), null, "addSubDevice", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subDeviceType", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mtfConfigConsumerEClass, MTFConfigConsumer.class, "MTFConfigConsumer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mtfConfigConsumerEClass_TFC);
    initEReference(getMTFConfigConsumer_TfConfig(), g1, null, "tfConfig", null, 0, 1, MTFConfigConsumer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBaseDeviceEClass, MBaseDevice.class, "MBaseDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBaseDevice_Logger(), this.getMLogger(), "logger", null, 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBaseDevice_Uid(), theEcorePackage.getEString(), "uid", null, 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBaseDevice_Poll(), theEcorePackage.getEBoolean(), "poll", "true", 0, 1, MBaseDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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

    initEClass(mActorEClass, MActor.class, "MActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mSwitchActorEClass, MSwitchActor.class, "MSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMSwitchActor_SwitchState(), this.getSwitchState(), "switchState", null, 0, 1, MSwitchActor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getMSwitchActor__TurnSwitch__OnOffValue(), null, "turnSwitch", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getSwitchState(), "state", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getMSwitchActor__FetchSwitchState(), null, "fetchSwitchState", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mOutSwitchActorEClass, MOutSwitchActor.class, "MOutSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mInSwitchActorEClass, MInSwitchActor.class, "MInSwitchActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(genericDeviceEClass, GenericDevice.class, "GenericDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getGenericDevice_GenericDeviceId(), theEcorePackage.getEString(), "genericDeviceId", null, 0, 1, GenericDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ioDeviceEClass, IODevice.class, "IODevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mSubDeviceEClass, MSubDevice.class, "MSubDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMSubDevice_SubId(), theEcorePackage.getEString(), "subId", null, 0, 1, MSubDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(mSubDeviceEClass_B);
    initEReference(getMSubDevice_Mbrick(), g1, this.getMSubDeviceHolder_Msubdevices(), "mbrick", null, 0, 1, MSubDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(callbackListenerEClass, CallbackListener.class, "CallbackListener", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCallbackListener_CallbackPeriod(), theEcorePackage.getELong(), "callbackPeriod", "1000", 0, 1, CallbackListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(interruptListenerEClass, InterruptListener.class, "InterruptListener", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getInterruptListener_DebouncePeriod(), theEcorePackage.getELong(), "debouncePeriod", "100", 0, 1, InterruptListener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mSensorEClass, MSensor.class, "MSensor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(mSensorEClass_DeviceValue);
    initEAttribute(getMSensor_SensorValue(), g1, "sensorValue", null, 0, 1, MSensor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMSensor__FetchSensorValue(), null, "fetchSensorValue", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mTextActorEClass, MTextActor.class, "MTextActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMTextActor_Text(), theEcorePackage.getEString(), "text", null, 0, 1, MTextActor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mlcdSubDeviceEClass, MLCDSubDevice.class, "MLCDSubDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mBrickServoEClass, MBrickServo.class, "MBrickServo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickServo_DeviceType(), theEcorePackage.getEString(), "deviceType", "brick_servo", 0, 1, MBrickServo.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickServo__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

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
    initEAttribute(getMBrickletIndustrialDigitalIn4_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_industrial_digital_4in", 0, 1, MBrickletIndustrialDigitalIn4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mIndustrialDigitalInEClass, MIndustrialDigitalIn.class, "MIndustrialDigitalIn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(mBrickletIndustrialDigitalOut4EClass, MBrickletIndustrialDigitalOut4.class, "MBrickletIndustrialDigitalOut4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(digitalActorDigitalOut4EClass, DigitalActorDigitalOut4.class, "DigitalActorDigitalOut4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalActorDigitalOut4_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, DigitalActorDigitalOut4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(digitalActorEClass, DigitalActor.class, "DigitalActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalActor_DigitalState(), this.getDigitalValue(), "digitalState", null, 0, 1, DigitalActor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getDigitalActor__TurnDigital__HighLowValue(), null, "turnDigital", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getDigitalValue(), "digitalState", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getDigitalActor__FetchDigitalValue(), null, "fetchDigitalValue", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(numberActorEClass, NumberActor.class, "NumberActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    op = initEOperation(getNumberActor__SetNumber__BigDecimal(), null, "setNumber", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEBigDecimal(), "value", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletSegmentDisplay4x7EClass, MBrickletSegmentDisplay4x7.class, "MBrickletSegmentDisplay4x7", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(colorActorEClass, ColorActor.class, "ColorActor", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    op = initEOperation(getColorActor__SetColor__HSBType_DeviceOptions(), null, "setColor", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getHSBType(), "color", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getDeviceOptions(), "opts", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletLEDStripEClass, MBrickletLEDStrip.class, "MBrickletLEDStrip", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(digitalActorIO16EClass, DigitalActorIO16.class, "DigitalActorIO16", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalActorIO16_DeviceType(), theEcorePackage.getEString(), "deviceType", "io_actuator", 0, 1, DigitalActorIO16.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO16_Port(), theEcorePackage.getEChar(), "port", null, 0, 1, DigitalActorIO16.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO16_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, DigitalActorIO16.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO16_DefaultState(), theEcorePackage.getEString(), "defaultState", null, 0, 1, DigitalActorIO16.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO16_KeepOnReconnect(), theEcorePackage.getEBoolean(), "keepOnReconnect", "false", 0, 1, DigitalActorIO16.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getDigitalActorIO16__TurnDigital__HighLowValue(), null, "turnDigital", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getDigitalValue(), "digitalState", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getDigitalActorIO16__FetchDigitalValue(), null, "fetchDigitalValue", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletIO16EClass, MBrickletIO16.class, "MBrickletIO16", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletIO16_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_io16", 0, 1, MBrickletIO16.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(digitalSensorEClass, DigitalSensor.class, "DigitalSensor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalSensor_DeviceType(), theEcorePackage.getEString(), "deviceType", "iosensor", 0, 1, DigitalSensor.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalSensor_PullUpResistorEnabled(), theEcorePackage.getEBoolean(), "pullUpResistorEnabled", null, 0, 1, DigitalSensor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalSensor_Port(), theEcorePackage.getEChar(), "port", null, 0, 1, DigitalSensor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalSensor_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, DigitalSensor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletIO4EClass, MBrickletIO4.class, "MBrickletIO4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletIO4_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_io4", 0, 1, MBrickletIO4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(io4DeviceEClass, IO4Device.class, "IO4Device", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(digitalSensorIO4EClass, DigitalSensorIO4.class, "DigitalSensorIO4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalSensorIO4_DeviceType(), theEcorePackage.getEString(), "deviceType", "io4sensor", 0, 1, DigitalSensorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalSensorIO4_PullUpResistorEnabled(), theEcorePackage.getEBoolean(), "pullUpResistorEnabled", null, 0, 1, DigitalSensorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalSensorIO4_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, DigitalSensorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(digitalActorIO4EClass, DigitalActorIO4.class, "DigitalActorIO4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDigitalActorIO4_DeviceType(), theEcorePackage.getEString(), "deviceType", "io4_actuator", 0, 1, DigitalActorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO4_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, DigitalActorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO4_DefaultState(), theEcorePackage.getEString(), "defaultState", null, 0, 1, DigitalActorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDigitalActorIO4_KeepOnReconnect(), theEcorePackage.getEBoolean(), "keepOnReconnect", "false", 0, 1, DigitalActorIO4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getDigitalActorIO4__TurnDigital__HighLowValue(), null, "turnDigital", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, this.getDigitalValue(), "digitalState", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEOperation(getDigitalActorIO4__FetchDigitalValue(), null, "fetchDigitalValue", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletMultiTouchEClass, MBrickletMultiTouch.class, "MBrickletMultiTouch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletMultiTouch_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_multitouch", 0, 1, MBrickletMultiTouch.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletMultiTouch_Recalibrate(), theEcorePackage.getEBooleanObject(), "recalibrate", null, 0, 1, MBrickletMultiTouch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletMultiTouch_Sensitivity(), theEcorePackage.getEShortObject(), "sensitivity", null, 0, 1, MBrickletMultiTouch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(multiTouchDeviceEClass, MultiTouchDevice.class, "MultiTouchDevice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMultiTouchDevice_Pin(), theEcorePackage.getEInt(), "pin", null, 0, 1, MultiTouchDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMultiTouchDevice_DisableElectrode(), theEcorePackage.getEBooleanObject(), "disableElectrode", null, 0, 1, MultiTouchDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(electrodeEClass, Electrode.class, "Electrode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getElectrode_DeviceType(), theEcorePackage.getEString(), "deviceType", "electrode", 0, 1, Electrode.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(proximityEClass, Proximity.class, "Proximity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getProximity_DeviceType(), theEcorePackage.getEString(), "deviceType", "proximity", 0, 1, Proximity.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletMotionDetectorEClass, MBrickletMotionDetector.class, "MBrickletMotionDetector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletMotionDetector_DeviceType(), theEcorePackage.getEString(), "deviceType", "motion_detector", 0, 1, MBrickletMotionDetector.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletMotionDetector__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletHallEffectEClass, MBrickletHallEffect.class, "MBrickletHallEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletHallEffect_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_halleffect", 0, 1, MBrickletHallEffect.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletHallEffect__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mDualRelayEClass, MDualRelay.class, "MDualRelay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMDualRelay_DeviceType(), theEcorePackage.getEString(), "deviceType", "dual_relay", 0, 1, MDualRelay.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletRemoteSwitchEClass, MBrickletRemoteSwitch.class, "MBrickletRemoteSwitch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletRemoteSwitch_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_remote_switch", 0, 1, MBrickletRemoteSwitch.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletRemoteSwitch_TypeADevices(), theEcorePackage.getEString(), "typeADevices", null, 0, 1, MBrickletRemoteSwitch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletRemoteSwitch_TypeBDevices(), theEcorePackage.getEString(), "typeBDevices", null, 0, 1, MBrickletRemoteSwitch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletRemoteSwitch_TypeCDevices(), theEcorePackage.getEString(), "typeCDevices", null, 0, 1, MBrickletRemoteSwitch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchEClass, RemoteSwitch.class, "RemoteSwitch", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(remoteSwitchAEClass, RemoteSwitchA.class, "RemoteSwitchA", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchA_DeviceType(), theEcorePackage.getEString(), "deviceType", "remote_switch_a", 0, 1, RemoteSwitchA.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchA_HouseCode(), theEcorePackage.getEShortObject(), "houseCode", null, 0, 1, RemoteSwitchA.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchA_ReceiverCode(), theEcorePackage.getEShortObject(), "receiverCode", null, 0, 1, RemoteSwitchA.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchA_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchA.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchBEClass, RemoteSwitchB.class, "RemoteSwitchB", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchB_DeviceType(), theEcorePackage.getEString(), "deviceType", "remote_switch_b", 0, 1, RemoteSwitchB.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchB_Address(), theEcorePackage.getELongObject(), "address", null, 0, 1, RemoteSwitchB.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchB_Unit(), theEcorePackage.getEShortObject(), "unit", null, 0, 1, RemoteSwitchB.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchB_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchB.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchCEClass, RemoteSwitchC.class, "RemoteSwitchC", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchC_DeviceType(), theEcorePackage.getEString(), "deviceType", "remote_switch_c", 0, 1, RemoteSwitchC.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchC_SystemCode(), theEcorePackage.getEString(), "systemCode", null, 0, 1, RemoteSwitchC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchC_DeviceCode(), theEcorePackage.getEShortObject(), "deviceCode", null, 0, 1, RemoteSwitchC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchC_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchC.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletHumidityEClass, MBrickletHumidity.class, "MBrickletHumidity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletHumidity_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_humidity", 0, 1, MBrickletHumidity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletHumidity_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0.5", 0, 1, MBrickletHumidity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletHumidity__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletDistanceIREClass, MBrickletDistanceIR.class, "MBrickletDistanceIR", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletDistanceIR_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_distance_ir", 0, 1, MBrickletDistanceIR.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletDistanceIR_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "5", 0, 1, MBrickletDistanceIR.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletDistanceIR__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletTemperatureEClass, MBrickletTemperature.class, "MBrickletTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_temperature", 0, 1, MBrickletTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletTemperature_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0.1", 0, 1, MBrickletTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletTemperature__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletTemperatureIREClass, MBrickletTemperatureIR.class, "MBrickletTemperatureIR", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletTemperatureIR_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_temperatureIR", 0, 1, MBrickletTemperatureIR.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mTemperatureIRDeviceEClass, MTemperatureIRDevice.class, "MTemperatureIRDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMTemperatureIRDevice_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0.1", 0, 1, MTemperatureIRDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(objectTemperatureEClass, ObjectTemperature.class, "ObjectTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getObjectTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "object_temperature", 0, 1, ObjectTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getObjectTemperature_Emissivity(), theEcorePackage.getEInt(), "emissivity", "65535", 0, 1, ObjectTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ambientTemperatureEClass, AmbientTemperature.class, "AmbientTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAmbientTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "ambient_temperature", 0, 1, AmbientTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletTiltEClass, MBrickletTilt.class, "MBrickletTilt", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletTilt_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_tilt", 0, 1, MBrickletTilt.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletVoltageCurrentEClass, MBrickletVoltageCurrent.class, "MBrickletVoltageCurrent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletVoltageCurrent_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_voltageCurrent", 0, 1, MBrickletVoltageCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletVoltageCurrent_Averaging(), theEcorePackage.getEShortObject(), "averaging", "3", 0, 1, MBrickletVoltageCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletVoltageCurrent_VoltageConversionTime(), theEcorePackage.getEShortObject(), "voltageConversionTime", "4", 0, 1, MBrickletVoltageCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletVoltageCurrent_CurrentConversionTime(), theEcorePackage.getEShortObject(), "currentConversionTime", "4", 0, 1, MBrickletVoltageCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(voltageCurrentDeviceEClass, VoltageCurrentDevice.class, "VoltageCurrentDevice", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(vcDeviceVoltageEClass, VCDeviceVoltage.class, "VCDeviceVoltage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVCDeviceVoltage_DeviceType(), theEcorePackage.getEString(), "deviceType", "voltageCurrent_voltage", 0, 1, VCDeviceVoltage.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVCDeviceVoltage_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "20", 0, 1, VCDeviceVoltage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(vcDeviceCurrentEClass, VCDeviceCurrent.class, "VCDeviceCurrent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVCDeviceCurrent_DeviceType(), theEcorePackage.getEString(), "deviceType", "voltageCurrent_current", 0, 1, VCDeviceCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVCDeviceCurrent_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "10", 0, 1, VCDeviceCurrent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(vcDevicePowerEClass, VCDevicePower.class, "VCDevicePower", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVCDevicePower_DeviceType(), theEcorePackage.getEString(), "deviceType", "voltageCurrent_power", 0, 1, VCDevicePower.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVCDevicePower_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "50", 0, 1, VCDevicePower.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mBrickletBarometerEClass, MBrickletBarometer.class, "MBrickletBarometer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletBarometer_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_barometer", 0, 1, MBrickletBarometer.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletBarometer_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "1", 0, 1, MBrickletBarometer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletBarometer__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBarometerTemperatureEClass, MBarometerTemperature.class, "MBarometerTemperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBarometerTemperature_DeviceType(), theEcorePackage.getEString(), "deviceType", "barometer_temperature", 0, 1, MBarometerTemperature.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBarometerTemperature__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletAmbientLightEClass, MBrickletAmbientLight.class, "MBrickletAmbientLight", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletAmbientLight_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_ambient_light", 0, 1, MBrickletAmbientLight.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletAmbientLight_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "1", 0, 1, MBrickletAmbientLight.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletAmbientLight__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletSoundIntensityEClass, MBrickletSoundIntensity.class, "MBrickletSoundIntensity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletSoundIntensity_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_soundintensity", 0, 1, MBrickletSoundIntensity.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletSoundIntensity_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0", 0, 1, MBrickletSoundIntensity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletSoundIntensity__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletMoistureEClass, MBrickletMoisture.class, "MBrickletMoisture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletMoisture_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_moisture", 0, 1, MBrickletMoisture.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletMoisture_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0", 0, 1, MBrickletMoisture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletMoisture_MovingAverage(), theEcorePackage.getEShortObject(), "movingAverage", "100", 0, 1, MBrickletMoisture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletMoisture__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletDistanceUSEClass, MBrickletDistanceUS.class, "MBrickletDistanceUS", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletDistanceUS_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_distanceUS", 0, 1, MBrickletDistanceUS.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletDistanceUS_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", "0", 0, 1, MBrickletDistanceUS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletDistanceUS_MovingAverage(), theEcorePackage.getEShortObject(), "movingAverage", "100", 0, 1, MBrickletDistanceUS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletDistanceUS__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mBrickletLCD20x4EClass, MBrickletLCD20x4.class, "MBrickletLCD20x4", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMBrickletLCD20x4_DeviceType(), theEcorePackage.getEString(), "deviceType", "bricklet_LCD20x4", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_PositionPrefix(), theEcorePackage.getEString(), "positionPrefix", "TFNUM<", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_PositonSuffix(), theEcorePackage.getEString(), "positonSuffix", ">", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_DisplayErrors(), theEcorePackage.getEBoolean(), "displayErrors", "true", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMBrickletLCD20x4_ErrorPrefix(), theEcorePackage.getEString(), "errorPrefix", "openhab Error:", 0, 1, MBrickletLCD20x4.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEOperation(getMBrickletLCD20x4__Init(), null, "init", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(mlcd20x4BacklightEClass, MLCD20x4Backlight.class, "MLCD20x4Backlight", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMLCD20x4Backlight_DeviceType(), theEcorePackage.getEString(), "deviceType", "backlight", 0, 1, MLCD20x4Backlight.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mlcd20x4ButtonEClass, MLCD20x4Button.class, "MLCD20x4Button", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMLCD20x4Button_DeviceType(), theEcorePackage.getEString(), "deviceType", "lcd_button", 0, 1, MLCD20x4Button.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMLCD20x4Button_ButtonNum(), theEcorePackage.getEShort(), "buttonNum", null, 0, 1, MLCD20x4Button.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfConfigEClass, TFConfig.class, "TFConfig", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(ohtfDeviceEClass, OHTFDevice.class, "OHTFDevice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getOHTFDevice_Uid(), theEcorePackage.getEString(), "uid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOHTFDevice_Subid(), theEcorePackage.getEString(), "subid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOHTFDevice_Ohid(), theEcorePackage.getEString(), "ohid", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(ohtfDeviceEClass_IDS);
    initEAttribute(getOHTFDevice_SubDeviceIds(), g1, "subDeviceIds", null, 0, -1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    g1 = createEGenericType(ohtfDeviceEClass_TFC);
    initEReference(getOHTFDevice_TfConfig(), g1, null, "tfConfig", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOHTFDevice_OhConfig(), this.getOHConfig(), this.getOHConfig_OhTfDevices(), "ohConfig", null, 0, 1, OHTFDevice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getOHTFDevice__IsValidSubId__String(), theEcorePackage.getEBoolean(), "isValidSubId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subId", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(ohtfSubDeviceAdminDeviceEClass, OHTFSubDeviceAdminDevice.class, "OHTFSubDeviceAdminDevice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    op = initEOperation(getOHTFSubDeviceAdminDevice__IsValidSubId__String(), theEcorePackage.getEBoolean(), "isValidSubId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subId", 0, 1, !IS_UNIQUE, IS_ORDERED);

    initEClass(ohConfigEClass, OHConfig.class, "OHConfig", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEReference(getOHConfig_OhTfDevices(), g1, this.getOHTFDevice_OhConfig(), "ohTfDevices", null, 0, -1, OHConfig.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    op = initEOperation(getOHConfig__GetConfigByTFId__String_String(), null, "getConfigByTFId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "uid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "subid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEOperation(op, g1);

    op = initEOperation(getOHConfig__GetConfigByOHId__String(), null, "getConfigByOHId", 0, 1, !IS_UNIQUE, IS_ORDERED);
    addEParameter(op, theEcorePackage.getEString(), "ohid", 0, 1, !IS_UNIQUE, IS_ORDERED);
    g1 = createEGenericType(this.getOHTFDevice());
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    g2 = createEGenericType();
    g1.getETypeArguments().add(g2);
    initEOperation(op, g1);

    initEClass(tfNullConfigurationEClass, TFNullConfiguration.class, "TFNullConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(tfBaseConfigurationEClass, TFBaseConfiguration.class, "TFBaseConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFBaseConfiguration_Threshold(), theEcorePackage.getEBigDecimal(), "threshold", null, 0, 1, TFBaseConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBaseConfiguration_CallbackPeriod(), theEcorePackage.getEInt(), "callbackPeriod", null, 0, 1, TFBaseConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfObjectTemperatureConfigurationEClass, TFObjectTemperatureConfiguration.class, "TFObjectTemperatureConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFObjectTemperatureConfiguration_Emissivity(), theEcorePackage.getEInt(), "emissivity", null, 0, 1, TFObjectTemperatureConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfMoistureBrickletConfigurationEClass, TFMoistureBrickletConfiguration.class, "TFMoistureBrickletConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFMoistureBrickletConfiguration_MovingAverage(), theEcorePackage.getEShortObject(), "movingAverage", null, 0, 1, TFMoistureBrickletConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfDistanceUSBrickletConfigurationEClass, TFDistanceUSBrickletConfiguration.class, "TFDistanceUSBrickletConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFDistanceUSBrickletConfiguration_MovingAverage(), theEcorePackage.getEShortObject(), "movingAverage", null, 0, 1, TFDistanceUSBrickletConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfVoltageCurrentConfigurationEClass, TFVoltageCurrentConfiguration.class, "TFVoltageCurrentConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFVoltageCurrentConfiguration_Averaging(), theEcorePackage.getEShortObject(), "averaging", null, 0, 1, TFVoltageCurrentConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFVoltageCurrentConfiguration_VoltageConversionTime(), theEcorePackage.getEShortObject(), "voltageConversionTime", null, 0, 1, TFVoltageCurrentConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFVoltageCurrentConfiguration_CurrentConversionTime(), theEcorePackage.getEShortObject(), "currentConversionTime", null, 0, 1, TFVoltageCurrentConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfBrickDCConfigurationEClass, TFBrickDCConfiguration.class, "TFBrickDCConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFBrickDCConfiguration_Velocity(), theEcorePackage.getEShort(), "velocity", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_Acceleration(), theEcorePackage.getEInt(), "acceleration", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_PwmFrequency(), theEcorePackage.getEInt(), "pwmFrequency", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_DriveMode(), theEcorePackage.getEInt(), "driveMode", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFBrickDCConfiguration_SwitchOnVelocity(), theEcorePackage.getEShort(), "switchOnVelocity", null, 0, 1, TFBrickDCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfioActorConfigurationEClass, TFIOActorConfiguration.class, "TFIOActorConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFIOActorConfiguration_DefaultState(), theEcorePackage.getEString(), "defaultState", null, 0, 1, TFIOActorConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFIOActorConfiguration_KeepOnReconnect(), theEcorePackage.getEBoolean(), "keepOnReconnect", null, 0, 1, TFIOActorConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfInterruptListenerConfigurationEClass, TFInterruptListenerConfiguration.class, "TFInterruptListenerConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFInterruptListenerConfiguration_DebouncePeriod(), theEcorePackage.getELong(), "debouncePeriod", null, 0, 1, TFInterruptListenerConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfioSensorConfigurationEClass, TFIOSensorConfiguration.class, "TFIOSensorConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFIOSensorConfiguration_PullUpResistorEnabled(), theEcorePackage.getEBoolean(), "pullUpResistorEnabled", null, 0, 1, TFIOSensorConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tfServoConfigurationEClass, TFServoConfiguration.class, "TFServoConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTFServoConfiguration_Velocity(), theEcorePackage.getEInt(), "velocity", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_Acceleration(), theEcorePackage.getEInt(), "acceleration", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_ServoVoltage(), theEcorePackage.getEInt(), "servoVoltage", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_PulseWidthMin(), theEcorePackage.getEInt(), "pulseWidthMin", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_PulseWidthMax(), theEcorePackage.getEInt(), "pulseWidthMax", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_Period(), theEcorePackage.getEInt(), "period", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTFServoConfiguration_OutputVoltage(), theEcorePackage.getEInt(), "outputVoltage", null, 0, 1, TFServoConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(brickletRemoteSwitchConfigurationEClass, BrickletRemoteSwitchConfiguration.class, "BrickletRemoteSwitchConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBrickletRemoteSwitchConfiguration_TypeADevices(), theEcorePackage.getEString(), "typeADevices", null, 0, 1, BrickletRemoteSwitchConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBrickletRemoteSwitchConfiguration_TypeBDevices(), theEcorePackage.getEString(), "typeBDevices", null, 0, 1, BrickletRemoteSwitchConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBrickletRemoteSwitchConfiguration_TypeCDevices(), theEcorePackage.getEString(), "typeCDevices", null, 0, 1, BrickletRemoteSwitchConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchAConfigurationEClass, RemoteSwitchAConfiguration.class, "RemoteSwitchAConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchAConfiguration_HouseCode(), theEcorePackage.getEShortObject(), "houseCode", null, 0, 1, RemoteSwitchAConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchAConfiguration_ReceiverCode(), theEcorePackage.getEShortObject(), "receiverCode", null, 0, 1, RemoteSwitchAConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchAConfiguration_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchAConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchBConfigurationEClass, RemoteSwitchBConfiguration.class, "RemoteSwitchBConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchBConfiguration_Address(), theEcorePackage.getELongObject(), "address", null, 0, 1, RemoteSwitchBConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchBConfiguration_Unit(), theEcorePackage.getEShortObject(), "unit", null, 0, 1, RemoteSwitchBConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchBConfiguration_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchBConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(remoteSwitchCConfigurationEClass, RemoteSwitchCConfiguration.class, "RemoteSwitchCConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRemoteSwitchCConfiguration_SystemCode(), theEcorePackage.getEString(), "systemCode", null, 0, 1, RemoteSwitchCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchCConfiguration_DeviceCode(), theEcorePackage.getEShortObject(), "deviceCode", null, 0, 1, RemoteSwitchCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRemoteSwitchCConfiguration_Repeats(), theEcorePackage.getEShortObject(), "repeats", null, 0, 1, RemoteSwitchCConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(multiTouchDeviceConfigurationEClass, MultiTouchDeviceConfiguration.class, "MultiTouchDeviceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMultiTouchDeviceConfiguration_DisableElectrode(), theEcorePackage.getEBooleanObject(), "disableElectrode", null, 0, 1, MultiTouchDeviceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(brickletMultiTouchConfigurationEClass, BrickletMultiTouchConfiguration.class, "BrickletMultiTouchConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBrickletMultiTouchConfiguration_Recalibrate(), theEcorePackage.getEBooleanObject(), "recalibrate", null, 0, 1, BrickletMultiTouchConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBrickletMultiTouchConfiguration_Sensitivity(), theEcorePackage.getEShortObject(), "sensitivity", null, 0, 1, BrickletMultiTouchConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(dcDriveModeEEnum, DCDriveMode.class, "DCDriveMode");
    addEEnumLiteral(dcDriveModeEEnum, DCDriveMode.BRAKE);
    addEEnumLiteral(dcDriveModeEEnum, DCDriveMode.COAST);

    initEEnum(noSubIdsEEnum, NoSubIds.class, "NoSubIds");

    initEEnum(industrialDigitalInSubIDsEEnum, IndustrialDigitalInSubIDs.class, "IndustrialDigitalInSubIDs");
    addEEnumLiteral(industrialDigitalInSubIDsEEnum, IndustrialDigitalInSubIDs.IN0);
    addEEnumLiteral(industrialDigitalInSubIDsEEnum, IndustrialDigitalInSubIDs.IN1);
    addEEnumLiteral(industrialDigitalInSubIDsEEnum, IndustrialDigitalInSubIDs.IN2);
    addEEnumLiteral(industrialDigitalInSubIDsEEnum, IndustrialDigitalInSubIDs.IN3);

    initEEnum(industrialQuadRelayIDsEEnum, IndustrialQuadRelayIDs.class, "IndustrialQuadRelayIDs");
    addEEnumLiteral(industrialQuadRelayIDsEEnum, IndustrialQuadRelayIDs.RELAY0);
    addEEnumLiteral(industrialQuadRelayIDsEEnum, IndustrialQuadRelayIDs.RELAY1);
    addEEnumLiteral(industrialQuadRelayIDsEEnum, IndustrialQuadRelayIDs.RELAY2);
    addEEnumLiteral(industrialQuadRelayIDsEEnum, IndustrialQuadRelayIDs.RELAY3);

    initEEnum(servoSubIDsEEnum, ServoSubIDs.class, "ServoSubIDs");
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO0);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO1);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO2);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO3);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO4);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO5);
    addEEnumLiteral(servoSubIDsEEnum, ServoSubIDs.SERVO6);

    initEEnum(barometerSubIDsEEnum, BarometerSubIDs.class, "BarometerSubIDs");
    addEEnumLiteral(barometerSubIDsEEnum, BarometerSubIDs.TEMPERATURE);

    initEEnum(io16SubIdsEEnum, IO16SubIds.class, "IO16SubIds");
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA0);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA1);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA2);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA3);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA4);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA5);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA6);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INA7);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB0);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB1);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB2);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB3);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB4);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB5);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB6);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.INB7);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA0);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA1);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA2);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA3);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA4);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA5);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA6);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTA7);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB0);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB1);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB2);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB3);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB4);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB5);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB6);
    addEEnumLiteral(io16SubIdsEEnum, IO16SubIds.OUTB7);

    initEEnum(io4SubIdsEEnum, IO4SubIds.class, "IO4SubIds");
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.IN0);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.IN1);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.IN2);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.IN3);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.OUT0);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.OUT1);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.OUT2);
    addEEnumLiteral(io4SubIdsEEnum, IO4SubIds.OUT3);

    initEEnum(dualRelaySubIdsEEnum, DualRelaySubIds.class, "DualRelaySubIds");
    addEEnumLiteral(dualRelaySubIdsEEnum, DualRelaySubIds.RELAY1);
    addEEnumLiteral(dualRelaySubIdsEEnum, DualRelaySubIds.RELAY2);

    initEEnum(lcdButtonSubIdsEEnum, LCDButtonSubIds.class, "LCDButtonSubIds");
    addEEnumLiteral(lcdButtonSubIdsEEnum, LCDButtonSubIds.BUTTON0);
    addEEnumLiteral(lcdButtonSubIdsEEnum, LCDButtonSubIds.BUTTON1);
    addEEnumLiteral(lcdButtonSubIdsEEnum, LCDButtonSubIds.BUTTON2);
    addEEnumLiteral(lcdButtonSubIdsEEnum, LCDButtonSubIds.BUTTON3);

    initEEnum(lcdBacklightSubIdsEEnum, LCDBacklightSubIds.class, "LCDBacklightSubIds");
    addEEnumLiteral(lcdBacklightSubIdsEEnum, LCDBacklightSubIds.BACKLIGHT);

    initEEnum(multiTouchSubIdsEEnum, MultiTouchSubIds.class, "MultiTouchSubIds");
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE0);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE1);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE2);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE3);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE4);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE5);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE6);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE7);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE8);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE9);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE10);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.ELECTRODE11);
    addEEnumLiteral(multiTouchSubIdsEEnum, MultiTouchSubIds.PROXIMITY);

    initEEnum(temperatureIRSubIdsEEnum, TemperatureIRSubIds.class, "TemperatureIRSubIds");
    addEEnumLiteral(temperatureIRSubIdsEEnum, TemperatureIRSubIds.OBJECT_TEMPERATURE);
    addEEnumLiteral(temperatureIRSubIdsEEnum, TemperatureIRSubIds.AMBIENT_TEMPERATURE);

    initEEnum(voltageCurrentSubIdsEEnum, VoltageCurrentSubIds.class, "VoltageCurrentSubIds");
    addEEnumLiteral(voltageCurrentSubIdsEEnum, VoltageCurrentSubIds.VOLTAGECURRENT_VOLTAGE);
    addEEnumLiteral(voltageCurrentSubIdsEEnum, VoltageCurrentSubIds.VOLTAGECURRENT_CURRENT);
    addEEnumLiteral(voltageCurrentSubIdsEEnum, VoltageCurrentSubIds.VOLTAGECURRENT_POWER);

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
    initEDataType(mTinkerBrickletIndustrialDigitalOut4EDataType, BrickletIndustrialDigitalOut4.class, "MTinkerBrickletIndustrialDigitalOut4", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(switchStateEDataType, OnOffValue.class, "SwitchState", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(digitalValueEDataType, HighLowValue.class, "DigitalValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletIO16EDataType, BrickletIO16.class, "TinkerBrickletIO16", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickServoEDataType, BrickServo.class, "MTinkerBrickServo", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerforgeValueEDataType, TinkerforgeValue.class, "MTinkerforgeValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mDecimalValueEDataType, DecimalValue.class, "MDecimalValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletHumidityEDataType, BrickletHumidity.class, "MTinkerBrickletHumidity", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletDistanceIREDataType, BrickletDistanceIR.class, "MTinkerBrickletDistanceIR", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletTemperatureEDataType, BrickletTemperature.class, "MTinkerBrickletTemperature", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletBarometerEDataType, BrickletBarometer.class, "MTinkerBrickletBarometer", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletAmbientLightEDataType, BrickletAmbientLight.class, "MTinkerBrickletAmbientLight", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(mTinkerBrickletLCD20x4EDataType, BrickletLCD20x4.class, "MTinkerBrickletLCD20x4", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletRemoteSwitchEDataType, BrickletRemoteSwitch.class, "TinkerBrickletRemoteSwitch", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletMotionDetectorEDataType, BrickletMotionDetector.class, "TinkerBrickletMotionDetector", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletMultiTouchEDataType, BrickletMultiTouch.class, "TinkerBrickletMultiTouch", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletTemperatureIREDataType, BrickletTemperatureIR.class, "TinkerBrickletTemperatureIR", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletSoundIntensityEDataType, BrickletSoundIntensity.class, "TinkerBrickletSoundIntensity", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletMoistureEDataType, BrickletMoisture.class, "TinkerBrickletMoisture", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletDistanceUSEDataType, BrickletDistanceUS.class, "TinkerBrickletDistanceUS", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletVoltageCurrentEDataType, BrickletVoltageCurrent.class, "TinkerBrickletVoltageCurrent", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletTiltEDataType, BrickletTilt.class, "TinkerBrickletTilt", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletIO4EDataType, BrickletIO4.class, "TinkerBrickletIO4", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletHallEffectEDataType, BrickletHallEffect.class, "TinkerBrickletHallEffect", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletSegmentDisplay4x7EDataType, BrickletSegmentDisplay4x7.class, "TinkerBrickletSegmentDisplay4x7", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(tinkerBrickletLEDStripEDataType, BrickletLEDStrip.class, "TinkerBrickletLEDStrip", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(hsbTypeEDataType, HSBType.class, "HSBType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(deviceOptionsEDataType, DeviceOptions.class, "DeviceOptions", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
    initEDataType(enumEDataType, Enum.class, "Enum", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);
  }

} //ModelPackageImpl
