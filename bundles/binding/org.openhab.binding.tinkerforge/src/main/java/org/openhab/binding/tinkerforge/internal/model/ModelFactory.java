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
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ModelFactory eINSTANCE = org.openhab.binding.tinkerforge.internal.model.impl.ModelFactoryImpl.init();

  /**
   * Returns a new object of class '<em>OHTF Device</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>OHTF Device</em>'.
   * @generated
   */
	@SuppressWarnings("rawtypes")
	<TFC extends TFConfig, IDS extends Enum> OHTFDevice<TFC, IDS> createOHTFDevice();

  /**
   * Returns a new object of class '<em>OHTF Sub Device Admin Device</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>OHTF Sub Device Admin Device</em>'.
   * @generated
   */
  <TFC extends TFConfig, IDS extends Enum> OHTFSubDeviceAdminDevice<TFC, IDS> createOHTFSubDeviceAdminDevice();

  /**
   * Returns a new object of class '<em>OH Config</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>OH Config</em>'.
   * @generated
   */
  OHConfig createOHConfig();

  /**
   * Returns a new object of class '<em>Ecosystem</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Ecosystem</em>'.
   * @generated
   */
  Ecosystem createEcosystem();

  /**
   * Returns a new object of class '<em>MBrickd</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBrickd</em>'.
   * @generated
   */
  MBrickd createMBrickd();

  /**
   * Returns a new object of class '<em>MBrick Servo</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBrick Servo</em>'.
   * @generated
   */
  MBrickServo createMBrickServo();

  /**
   * Returns a new object of class '<em>TF Brick DC Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Brick DC Configuration</em>'.
   * @generated
   */
  TFBrickDCConfiguration createTFBrickDCConfiguration();

  /**
   * Returns a new object of class '<em>MBrick DC</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBrick DC</em>'.
   * @generated
   */
  MBrickDC createMBrickDC();

  /**
   * Returns a new object of class '<em>MDual Relay Bricklet</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MDual Relay Bricklet</em>'.
   * @generated
   */
  MDualRelayBricklet createMDualRelayBricklet();

  /**
   * Returns a new object of class '<em>MIndustrial Quad Relay Bricklet</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MIndustrial Quad Relay Bricklet</em>'.
   * @generated
   */
  MIndustrialQuadRelayBricklet createMIndustrialQuadRelayBricklet();

  /**
   * Returns a new object of class '<em>MIndustrial Quad Relay</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MIndustrial Quad Relay</em>'.
   * @generated
   */
  MIndustrialQuadRelay createMIndustrialQuadRelay();

  /**
   * Returns a new object of class '<em>MBricklet Industrial Digital In4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Industrial Digital In4</em>'.
   * @generated
   */
  MBrickletIndustrialDigitalIn4 createMBrickletIndustrialDigitalIn4();

  /**
   * Returns a new object of class '<em>MIndustrial Digital In</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MIndustrial Digital In</em>'.
   * @generated
   */
  MIndustrialDigitalIn createMIndustrialDigitalIn();

  /**
   * Returns a new object of class '<em>MBricklet Industrial Digital Out4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Industrial Digital Out4</em>'.
   * @generated
   */
  MBrickletIndustrialDigitalOut4 createMBrickletIndustrialDigitalOut4();

  /**
   * Returns a new object of class '<em>Digital Actor Digital Out4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Actor Digital Out4</em>'.
   * @generated
   */
  DigitalActorDigitalOut4 createDigitalActorDigitalOut4();

  /**
   * Returns a new object of class '<em>MBricklet Segment Display4x7</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Segment Display4x7</em>'.
   * @generated
   */
  MBrickletSegmentDisplay4x7 createMBrickletSegmentDisplay4x7();

  /**
   * Returns a new object of class '<em>MBricklet LED Strip</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet LED Strip</em>'.
   * @generated
   */
  MBrickletLEDStrip createMBrickletLEDStrip();

  /**
   * Returns a new object of class '<em>Digital Actor IO16</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Actor IO16</em>'.
   * @generated
   */
  DigitalActorIO16 createDigitalActorIO16();

  /**
   * Returns a new object of class '<em>TFIO Actor Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TFIO Actor Configuration</em>'.
   * @generated
   */
  TFIOActorConfiguration createTFIOActorConfiguration();

  /**
   * Returns a new object of class '<em>TF Interrupt Listener Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Interrupt Listener Configuration</em>'.
   * @generated
   */
  TFInterruptListenerConfiguration createTFInterruptListenerConfiguration();

  /**
   * Returns a new object of class '<em>MBricklet IO16</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet IO16</em>'.
   * @generated
   */
  MBrickletIO16 createMBrickletIO16();

  /**
   * Returns a new object of class '<em>TFIO Sensor Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TFIO Sensor Configuration</em>'.
   * @generated
   */
  TFIOSensorConfiguration createTFIOSensorConfiguration();

  /**
   * Returns a new object of class '<em>Digital Sensor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Sensor</em>'.
   * @generated
   */
  DigitalSensor createDigitalSensor();

  /**
   * Returns a new object of class '<em>MBricklet IO4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet IO4</em>'.
   * @generated
   */
  MBrickletIO4 createMBrickletIO4();

  /**
   * Returns a new object of class '<em>Digital Sensor IO4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Sensor IO4</em>'.
   * @generated
   */
  DigitalSensorIO4 createDigitalSensorIO4();

  /**
   * Returns a new object of class '<em>Digital Actor IO4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Actor IO4</em>'.
   * @generated
   */
  DigitalActorIO4 createDigitalActorIO4();

  /**
   * Returns a new object of class '<em>MBricklet Multi Touch</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Multi Touch</em>'.
   * @generated
   */
  MBrickletMultiTouch createMBrickletMultiTouch();

  /**
   * Returns a new object of class '<em>Multi Touch Device</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Multi Touch Device</em>'.
   * @generated
   */
  MultiTouchDevice createMultiTouchDevice();

  /**
   * Returns a new object of class '<em>Electrode</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Electrode</em>'.
   * @generated
   */
  Electrode createElectrode();

  /**
   * Returns a new object of class '<em>Proximity</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Proximity</em>'.
   * @generated
   */
  Proximity createProximity();

  /**
   * Returns a new object of class '<em>MBricklet Motion Detector</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Motion Detector</em>'.
   * @generated
   */
  MBrickletMotionDetector createMBrickletMotionDetector();

  /**
   * Returns a new object of class '<em>MBricklet Hall Effect</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Hall Effect</em>'.
   * @generated
   */
  MBrickletHallEffect createMBrickletHallEffect();

  /**
   * Returns a new object of class '<em>MDual Relay</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MDual Relay</em>'.
   * @generated
   */
  MDualRelay createMDualRelay();

  /**
   * Returns a new object of class '<em>MBricklet Remote Switch</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Remote Switch</em>'.
   * @generated
   */
  MBrickletRemoteSwitch createMBrickletRemoteSwitch();

  /**
   * Returns a new object of class '<em>Remote Switch A</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch A</em>'.
   * @generated
   */
  RemoteSwitchA createRemoteSwitchA();

  /**
   * Returns a new object of class '<em>Remote Switch B</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch B</em>'.
   * @generated
   */
  RemoteSwitchB createRemoteSwitchB();

  /**
   * Returns a new object of class '<em>Remote Switch C</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch C</em>'.
   * @generated
   */
  RemoteSwitchC createRemoteSwitchC();

  /**
   * Returns a new object of class '<em>TF Null Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Null Configuration</em>'.
   * @generated
   */
  TFNullConfiguration createTFNullConfiguration();

  /**
   * Returns a new object of class '<em>TF Servo Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Servo Configuration</em>'.
   * @generated
   */
  TFServoConfiguration createTFServoConfiguration();

  /**
   * Returns a new object of class '<em>Bricklet Remote Switch Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Bricklet Remote Switch Configuration</em>'.
   * @generated
   */
  BrickletRemoteSwitchConfiguration createBrickletRemoteSwitchConfiguration();

  /**
   * Returns a new object of class '<em>Remote Switch AConfiguration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch AConfiguration</em>'.
   * @generated
   */
  RemoteSwitchAConfiguration createRemoteSwitchAConfiguration();

  /**
   * Returns a new object of class '<em>Remote Switch BConfiguration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch BConfiguration</em>'.
   * @generated
   */
  RemoteSwitchBConfiguration createRemoteSwitchBConfiguration();

  /**
   * Returns a new object of class '<em>Remote Switch CConfiguration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Switch CConfiguration</em>'.
   * @generated
   */
  RemoteSwitchCConfiguration createRemoteSwitchCConfiguration();

  /**
   * Returns a new object of class '<em>Multi Touch Device Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Multi Touch Device Configuration</em>'.
   * @generated
   */
  MultiTouchDeviceConfiguration createMultiTouchDeviceConfiguration();

  /**
   * Returns a new object of class '<em>Bricklet Multi Touch Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Bricklet Multi Touch Configuration</em>'.
   * @generated
   */
  BrickletMultiTouchConfiguration createBrickletMultiTouchConfiguration();

  /**
   * Returns a new object of class '<em>MServo</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MServo</em>'.
   * @generated
   */
  MServo createMServo();

  /**
   * Returns a new object of class '<em>MBricklet Humidity</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Humidity</em>'.
   * @generated
   */
  MBrickletHumidity createMBrickletHumidity();

  /**
   * Returns a new object of class '<em>MBricklet Distance IR</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Distance IR</em>'.
   * @generated
   */
  MBrickletDistanceIR createMBrickletDistanceIR();

  /**
   * Returns a new object of class '<em>MBricklet Temperature</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Temperature</em>'.
   * @generated
   */
  MBrickletTemperature createMBrickletTemperature();

  /**
   * Returns a new object of class '<em>MBricklet Temperature IR</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Temperature IR</em>'.
   * @generated
   */
  MBrickletTemperatureIR createMBrickletTemperatureIR();

  /**
   * Returns a new object of class '<em>Object Temperature</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Object Temperature</em>'.
   * @generated
   */
  ObjectTemperature createObjectTemperature();

  /**
   * Returns a new object of class '<em>Ambient Temperature</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Ambient Temperature</em>'.
   * @generated
   */
  AmbientTemperature createAmbientTemperature();

  /**
   * Returns a new object of class '<em>MBricklet Tilt</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Tilt</em>'.
   * @generated
   */
  MBrickletTilt createMBrickletTilt();

  /**
   * Returns a new object of class '<em>MBricklet Voltage Current</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Voltage Current</em>'.
   * @generated
   */
  MBrickletVoltageCurrent createMBrickletVoltageCurrent();

  /**
   * Returns a new object of class '<em>VC Device Voltage</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>VC Device Voltage</em>'.
   * @generated
   */
  VCDeviceVoltage createVCDeviceVoltage();

  /**
   * Returns a new object of class '<em>VC Device Current</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>VC Device Current</em>'.
   * @generated
   */
  VCDeviceCurrent createVCDeviceCurrent();

  /**
   * Returns a new object of class '<em>VC Device Power</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>VC Device Power</em>'.
   * @generated
   */
  VCDevicePower createVCDevicePower();

  /**
   * Returns a new object of class '<em>TF Base Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Base Configuration</em>'.
   * @generated
   */
  TFBaseConfiguration createTFBaseConfiguration();

  /**
   * Returns a new object of class '<em>TF Object Temperature Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Object Temperature Configuration</em>'.
   * @generated
   */
  TFObjectTemperatureConfiguration createTFObjectTemperatureConfiguration();

  /**
   * Returns a new object of class '<em>TF Moisture Bricklet Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Moisture Bricklet Configuration</em>'.
   * @generated
   */
  TFMoistureBrickletConfiguration createTFMoistureBrickletConfiguration();

  /**
   * Returns a new object of class '<em>TF Distance US Bricklet Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Distance US Bricklet Configuration</em>'.
   * @generated
   */
  TFDistanceUSBrickletConfiguration createTFDistanceUSBrickletConfiguration();

  /**
   * Returns a new object of class '<em>TF Voltage Current Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Voltage Current Configuration</em>'.
   * @generated
   */
  TFVoltageCurrentConfiguration createTFVoltageCurrentConfiguration();

  /**
   * Returns a new object of class '<em>MBricklet Barometer</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Barometer</em>'.
   * @generated
   */
  MBrickletBarometer createMBrickletBarometer();

  /**
   * Returns a new object of class '<em>MBarometer Temperature</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBarometer Temperature</em>'.
   * @generated
   */
  MBarometerTemperature createMBarometerTemperature();

  /**
   * Returns a new object of class '<em>MBricklet Ambient Light</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Ambient Light</em>'.
   * @generated
   */
  MBrickletAmbientLight createMBrickletAmbientLight();

  /**
   * Returns a new object of class '<em>MBricklet Sound Intensity</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Sound Intensity</em>'.
   * @generated
   */
  MBrickletSoundIntensity createMBrickletSoundIntensity();

  /**
   * Returns a new object of class '<em>MBricklet Moisture</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Moisture</em>'.
   * @generated
   */
  MBrickletMoisture createMBrickletMoisture();

  /**
   * Returns a new object of class '<em>MBricklet Distance US</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet Distance US</em>'.
   * @generated
   */
  MBrickletDistanceUS createMBrickletDistanceUS();

  /**
   * Returns a new object of class '<em>MBricklet LCD2 0x4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MBricklet LCD2 0x4</em>'.
   * @generated
   */
  MBrickletLCD20x4 createMBrickletLCD20x4();

  /**
   * Returns a new object of class '<em>MLCD2 0x4 Backlight</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MLCD2 0x4 Backlight</em>'.
   * @generated
   */
  MLCD20x4Backlight createMLCD20x4Backlight();

  /**
   * Returns a new object of class '<em>MLCD2 0x4 Button</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MLCD2 0x4 Button</em>'.
   * @generated
   */
  MLCD20x4Button createMLCD20x4Button();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ModelPackage getModelPackage();

} //ModelFactory
