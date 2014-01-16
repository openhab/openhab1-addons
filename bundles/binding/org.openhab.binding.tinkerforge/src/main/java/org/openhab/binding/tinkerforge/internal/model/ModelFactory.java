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
   * Returns a new object of class '<em>TFIO Actor Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TFIO Actor Configuration</em>'.
   * @generated
   */
  TFIOActorConfiguration createTFIOActorConfiguration();

  /**
   * Returns a new object of class '<em>Digital Actor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Digital Actor</em>'.
   * @generated
   */
  DigitalActor createDigitalActor();

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
   * Returns a new object of class '<em>MDual Relay</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MDual Relay</em>'.
   * @generated
   */
  MDualRelay createMDualRelay();

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
   * Returns a new object of class '<em>TF Base Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TF Base Configuration</em>'.
   * @generated
   */
  TFBaseConfiguration createTFBaseConfiguration();

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
