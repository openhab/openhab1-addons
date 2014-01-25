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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * *
 * Copyright (c) 2010-2014, openHAB.org and others.
 *  * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <!-- end-model-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/GenModel basePackage='org.openhab.binding.tinkerforge.internal'"
 * @generated
 */
public interface ModelPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "model";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "org.openhab.binding.tinkerforge.internal.model";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "model";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ModelPackage eINSTANCE = org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl.init();

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.TFConfig <em>TF Config</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.TFConfig
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFConfig()
   * @generated
   */
  int TF_CONFIG = 39;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl <em>OHTF Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getOHTFDevice()
   * @generated
   */
  int OHTF_DEVICE = 40;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.OHConfigImpl <em>OH Config</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.OHConfigImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getOHConfig()
   * @generated
   */
  int OH_CONFIG = 41;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl <em>Ecosystem</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getEcosystem()
   * @generated
   */
  int ECOSYSTEM = 0;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl <em>MBrickd</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickd()
   * @generated
   */
  int MBRICKD = 1;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer <em>MTF Config Consumer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTFConfigConsumer()
   * @generated
   */
  int MTF_CONFIG_CONSUMER = 2;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice <em>MBase Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBaseDevice()
   * @generated
   */
  int MBASE_DEVICE = 3;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice <em>MDevice</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDevice()
   * @generated
   */
  int MDEVICE = 4;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder <em>MSub Device Holder</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSubDeviceHolder()
   * @generated
   */
  int MSUB_DEVICE_HOLDER = 5;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickServoImpl <em>MBrick Servo</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickServoImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickServo()
   * @generated
   */
  int MBRICK_SERVO = 18;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl <em>TF Brick DC Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFBrickDCConfiguration()
   * @generated
   */
  int TF_BRICK_DC_CONFIGURATION = 44;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayBrickletImpl <em>MDual Relay Bricklet</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayBrickletImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDualRelayBricklet()
   * @generated
   */
  int MDUAL_RELAY_BRICKLET = 21;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MActor <em>MActor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MActor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMActor()
   * @generated
   */
  int MACTOR = 6;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor <em>MSwitch Actor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSwitchActor()
   * @generated
   */
  int MSWITCH_ACTOR = 7;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MInSwitchActor <em>MIn Switch Actor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MInSwitchActor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMInSwitchActor()
   * @generated
   */
  int MIN_SWITCH_ACTOR = 9;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl <em>MBrick DC</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickDC()
   * @generated
   */
  int MBRICK_DC = 20;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayBrickletImpl <em>MIndustrial Quad Relay Bricklet</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayBrickletImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialQuadRelayBricklet()
   * @generated
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET = 22;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl <em>MIndustrial Quad Relay</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialQuadRelay()
   * @generated
   */
  int MINDUSTRIAL_QUAD_RELAY = 23;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIndustrialDigitalIn4Impl <em>MBricklet Industrial Digital In4</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIndustrialDigitalIn4Impl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletIndustrialDigitalIn4()
   * @generated
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4 = 24;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor <em>MOut Switch Actor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMOutSwitchActor()
   * @generated
   */
  int MOUT_SWITCH_ACTOR = 8;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice <em>MSub Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSubDevice()
   * @generated
   */
  int MSUB_DEVICE = 12;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl <em>MIndustrial Digital In</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialDigitalIn()
   * @generated
   */
  int MINDUSTRIAL_DIGITAL_IN = 25;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl <em>TFIO Actor Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFIOActorConfiguration()
   * @generated
   */
  int TFIO_ACTOR_CONFIGURATION = 45;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalActorImpl <em>Digital Actor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.DigitalActorImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalActor()
   * @generated
   */
  int DIGITAL_ACTOR = 26;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIO16Impl <em>MBricklet IO16</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIO16Impl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletIO16()
   * @generated
   */
  int MBRICKLET_IO16 = 27;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl <em>Digital Sensor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalSensor()
   * @generated
   */
  int DIGITAL_SENSOR = 28;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.IODevice <em>IO Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.IODevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIODevice()
   * @generated
   */
  int IO_DEVICE = 11;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFInterruptListenerConfigurationImpl <em>TF Interrupt Listener Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFInterruptListenerConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFInterruptListenerConfiguration()
   * @generated
   */
  int TF_INTERRUPT_LISTENER_CONFIGURATION = 46;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOSensorConfigurationImpl <em>TFIO Sensor Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFIOSensorConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFIOSensorConfiguration()
   * @generated
   */
  int TFIO_SENSOR_CONFIGURATION = 47;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayImpl <em>MDual Relay</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDualRelay()
   * @generated
   */
  int MDUAL_RELAY = 29;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFNullConfigurationImpl <em>TF Null Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFNullConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFNullConfiguration()
   * @generated
   */
  int TF_NULL_CONFIGURATION = 42;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl <em>TF Servo Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFServoConfiguration()
   * @generated
   */
  int TF_SERVO_CONFIGURATION = 48;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl <em>MServo</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMServo()
   * @generated
   */
  int MSERVO = 19;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener <em>Callback Listener</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.CallbackListener
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getCallbackListener()
   * @generated
   */
  int CALLBACK_LISTENER = 13;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.InterruptListener <em>Interrupt Listener</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.InterruptListener
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getInterruptListener()
   * @generated
   */
  int INTERRUPT_LISTENER = 14;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MSensor <em>MSensor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MSensor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSensor()
   * @generated
   */
  int MSENSOR = 15;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletHumidityImpl <em>MBricklet Humidity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletHumidityImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletHumidity()
   * @generated
   */
  int MBRICKLET_HUMIDITY = 30;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletDistanceIRImpl <em>MBricklet Distance IR</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletDistanceIRImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletDistanceIR()
   * @generated
   */
  int MBRICKLET_DISTANCE_IR = 31;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletTemperatureImpl <em>MBricklet Temperature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletTemperatureImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletTemperature()
   * @generated
   */
  int MBRICKLET_TEMPERATURE = 32;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBaseConfigurationImpl <em>TF Base Configuration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.TFBaseConfigurationImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFBaseConfiguration()
   * @generated
   */
  int TF_BASE_CONFIGURATION = 43;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM__LOGGER = 0;

  /**
   * The feature id for the '<em><b>Mbrickds</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM__MBRICKDS = 1;

  /**
   * The number of structural features of the '<em>Ecosystem</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM_FEATURE_COUNT = 2;

  /**
   * The operation id for the '<em>Get Brickd</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM___GET_BRICKD__STRING_INT = 0;

  /**
   * The operation id for the '<em>Get Device</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM___GET_DEVICE__STRING_STRING = 1;

  /**
   * The operation id for the '<em>Get Devices4 Generic Id</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM___GET_DEVICES4_GENERIC_ID__STRING_STRING = 2;

  /**
   * The operation id for the '<em>Disconnect</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM___DISCONNECT = 3;

  /**
   * The number of operations of the '<em>Ecosystem</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECOSYSTEM_OPERATION_COUNT = 4;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__LOGGER = 0;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__IP_CONNECTION = 1;

  /**
   * The feature id for the '<em><b>Host</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__HOST = 2;

  /**
   * The feature id for the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__PORT = 3;

  /**
   * The feature id for the '<em><b>Is Connected</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__IS_CONNECTED = 4;

  /**
   * The feature id for the '<em><b>Auto Reconnect</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__AUTO_RECONNECT = 5;

  /**
   * The feature id for the '<em><b>Timeout</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__TIMEOUT = 6;

  /**
   * The feature id for the '<em><b>Mdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__MDEVICES = 7;

  /**
   * The feature id for the '<em><b>Ecosystem</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD__ECOSYSTEM = 8;

  /**
   * The number of structural features of the '<em>MBrickd</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD_FEATURE_COUNT = 9;

  /**
   * The operation id for the '<em>Connect</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD___CONNECT = 0;

  /**
   * The operation id for the '<em>Disconnect</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD___DISCONNECT = 1;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD___INIT = 2;

  /**
   * The operation id for the '<em>Get Device</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD___GET_DEVICE__STRING = 3;

  /**
   * The number of operations of the '<em>MBrickd</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKD_OPERATION_COUNT = 4;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTF_CONFIG_CONSUMER__TF_CONFIG = 0;

  /**
   * The number of structural features of the '<em>MTF Config Consumer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTF_CONFIG_CONSUMER_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>MTF Config Consumer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTF_CONFIG_CONSUMER_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE__LOGGER = 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE__UID = 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE__ENABLED_A = 2;

  /**
   * The number of structural features of the '<em>MBase Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE_FEATURE_COUNT = 3;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE___INIT = 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE___ENABLE = 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE___DISABLE = 2;

  /**
   * The number of operations of the '<em>MBase Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBASE_DEVICE_OPERATION_COUNT = 3;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__LOGGER = MBASE_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__UID = MBASE_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__ENABLED_A = MBASE_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__TINKERFORGE_DEVICE = MBASE_DEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__IP_CONNECTION = MBASE_DEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__CONNECTED_UID = MBASE_DEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__POSITION = MBASE_DEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__DEVICE_IDENTIFIER = MBASE_DEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__NAME = MBASE_DEVICE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE__BRICKD = MBASE_DEVICE_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>MDevice</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE_FEATURE_COUNT = MBASE_DEVICE_FEATURE_COUNT + 7;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE___INIT = MBASE_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE___ENABLE = MBASE_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE___DISABLE = MBASE_DEVICE___DISABLE;

  /**
   * The number of operations of the '<em>MDevice</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEVICE_OPERATION_COUNT = MBASE_DEVICE_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_HOLDER__MSUBDEVICES = 0;

  /**
   * The number of structural features of the '<em>MSub Device Holder</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_HOLDER_FEATURE_COUNT = 1;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES = 0;

  /**
   * The number of operations of the '<em>MSub Device Holder</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_HOLDER_OPERATION_COUNT = 1;

  /**
   * The number of structural features of the '<em>MActor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MACTOR_FEATURE_COUNT = 0;

  /**
   * The number of operations of the '<em>MActor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MACTOR_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSWITCH_ACTOR__SWITCH_STATE = 0;

  /**
   * The number of structural features of the '<em>MSwitch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSWITCH_ACTOR_FEATURE_COUNT = 1;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE = 0;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSWITCH_ACTOR___FETCH_SWITCH_STATE = 1;

  /**
   * The number of operations of the '<em>MSwitch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSWITCH_ACTOR_OPERATION_COUNT = 2;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOUT_SWITCH_ACTOR__SWITCH_STATE = MSWITCH_ACTOR__SWITCH_STATE;

  /**
   * The number of structural features of the '<em>MOut Switch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOUT_SWITCH_ACTOR_FEATURE_COUNT = MSWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOUT_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE = MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOUT_SWITCH_ACTOR___FETCH_SWITCH_STATE = MSWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The number of operations of the '<em>MOut Switch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOUT_SWITCH_ACTOR_OPERATION_COUNT = MSWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIN_SWITCH_ACTOR__SWITCH_STATE = MSWITCH_ACTOR__SWITCH_STATE;

  /**
   * The number of structural features of the '<em>MIn Switch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIN_SWITCH_ACTOR_FEATURE_COUNT = MSWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE = MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE = MSWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The number of operations of the '<em>MIn Switch Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIN_SWITCH_ACTOR_OPERATION_COUNT = MSWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice <em>Generic Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.GenericDevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getGenericDevice()
   * @generated
   */
  int GENERIC_DEVICE = 10;

  /**
   * The feature id for the '<em><b>Generic Device Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GENERIC_DEVICE__GENERIC_DEVICE_ID = 0;

  /**
   * The number of structural features of the '<em>Generic Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GENERIC_DEVICE_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>Generic Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GENERIC_DEVICE_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE__LOGGER = MBASE_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE__UID = MBASE_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE__ENABLED_A = MBASE_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE__SUB_ID = MBASE_DEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE__MBRICK = MBASE_DEVICE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>MSub Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_FEATURE_COUNT = MBASE_DEVICE_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE___INIT = MBASE_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE___ENABLE = MBASE_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE___DISABLE = MBASE_DEVICE___DISABLE;

  /**
   * The number of operations of the '<em>MSub Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSUB_DEVICE_OPERATION_COUNT = MBASE_DEVICE_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__LOGGER = MSUB_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__UID = MSUB_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__ENABLED_A = MSUB_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__SUB_ID = MSUB_DEVICE__SUB_ID;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__MBRICK = MSUB_DEVICE__MBRICK;

  /**
   * The feature id for the '<em><b>Generic Device Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE__GENERIC_DEVICE_ID = MSUB_DEVICE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IO Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE_FEATURE_COUNT = MSUB_DEVICE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE___INIT = MSUB_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE___ENABLE = MSUB_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE___DISABLE = MSUB_DEVICE___DISABLE;

  /**
   * The number of operations of the '<em>IO Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IO_DEVICE_OPERATION_COUNT = MSUB_DEVICE_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CALLBACK_LISTENER__CALLBACK_PERIOD = 0;

  /**
   * The number of structural features of the '<em>Callback Listener</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CALLBACK_LISTENER_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>Callback Listener</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CALLBACK_LISTENER_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Debounce Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERRUPT_LISTENER__DEBOUNCE_PERIOD = 0;

  /**
   * The number of structural features of the '<em>Interrupt Listener</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERRUPT_LISTENER_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>Interrupt Listener</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERRUPT_LISTENER_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSENSOR__SENSOR_VALUE = 0;

  /**
   * The number of structural features of the '<em>MSensor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSENSOR_FEATURE_COUNT = 1;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSENSOR___FETCH_SENSOR_VALUE = 0;

  /**
   * The number of operations of the '<em>MSensor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSENSOR_OPERATION_COUNT = 1;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletBarometerImpl <em>MBricklet Barometer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletBarometerImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletBarometer()
   * @generated
   */
  int MBRICKLET_BAROMETER = 33;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBarometerTemperatureImpl <em>MBarometer Temperature</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBarometerTemperatureImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBarometerTemperature()
   * @generated
   */
  int MBAROMETER_TEMPERATURE = 34;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightImpl <em>MBricklet Ambient Light</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletAmbientLight()
   * @generated
   */
  int MBRICKLET_AMBIENT_LIGHT = 35;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl <em>MBricklet LCD2 0x4</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletLCD20x4()
   * @generated
   */
  int MBRICKLET_LCD2_0X4 = 36;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MTextActor <em>MText Actor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MTextActor
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTextActor()
   * @generated
   */
  int MTEXT_ACTOR = 16;

  /**
   * The feature id for the '<em><b>Text</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTEXT_ACTOR__TEXT = 0;

  /**
   * The number of structural features of the '<em>MText Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTEXT_ACTOR_FEATURE_COUNT = 1;

  /**
   * The number of operations of the '<em>MText Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MTEXT_ACTOR_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice <em>MLCD Sub Device</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCDSubDevice()
   * @generated
   */
  int MLCD_SUB_DEVICE = 17;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE__LOGGER = MSUB_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE__UID = MSUB_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE__ENABLED_A = MSUB_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE__SUB_ID = MSUB_DEVICE__SUB_ID;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE__MBRICK = MSUB_DEVICE__MBRICK;

  /**
   * The number of structural features of the '<em>MLCD Sub Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE_FEATURE_COUNT = MSUB_DEVICE_FEATURE_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE___INIT = MSUB_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE___ENABLE = MSUB_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE___DISABLE = MSUB_DEVICE___DISABLE;

  /**
   * The number of operations of the '<em>MLCD Sub Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD_SUB_DEVICE_OPERATION_COUNT = MSUB_DEVICE_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>MBrick Servo</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO___INIT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>MBrick Servo</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_SERVO_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__SWITCH_STATE = MIN_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__LOGGER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__ENABLED_A = MIN_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__SUB_ID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__MBRICK = MIN_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__TF_CONFIG = MIN_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__DEVICE_TYPE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__VELOCITY = MIN_SWITCH_ACTOR_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__ACCELERATION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Pulse Width Min</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__PULSE_WIDTH_MIN = MIN_SWITCH_ACTOR_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Pulse Width Max</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__PULSE_WIDTH_MAX = MIN_SWITCH_ACTOR_FEATURE_COUNT + 10;

  /**
   * The feature id for the '<em><b>Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__PERIOD = MIN_SWITCH_ACTOR_FEATURE_COUNT + 11;

  /**
   * The feature id for the '<em><b>Output Voltage</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__OUTPUT_VOLTAGE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 12;

  /**
   * The feature id for the '<em><b>Servo Current Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__SERVO_CURRENT_POSITION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 13;

  /**
   * The feature id for the '<em><b>Servo Destination Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO__SERVO_DESTINATION_POSITION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 14;

  /**
   * The number of structural features of the '<em>MServo</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO_FEATURE_COUNT = MIN_SWITCH_ACTOR_FEATURE_COUNT + 15;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO___TURN_SWITCH__ONOFFVALUE = MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO___FETCH_SWITCH_STATE = MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO___ENABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO___DISABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO___INIT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The number of operations of the '<em>MServo</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MSERVO_OPERATION_COUNT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 4;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__SWITCH_STATE = MIN_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__LOGGER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__ENABLED_A = MIN_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__TINKERFORGE_DEVICE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__IP_CONNECTION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__CONNECTED_UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__POSITION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__DEVICE_IDENTIFIER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__NAME = MIN_SWITCH_ACTOR_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__BRICKD = MIN_SWITCH_ACTOR_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__TF_CONFIG = MIN_SWITCH_ACTOR_FEATURE_COUNT + 10;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__DEVICE_TYPE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 11;

  /**
   * The feature id for the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__VELOCITY = MIN_SWITCH_ACTOR_FEATURE_COUNT + 12;

  /**
   * The feature id for the '<em><b>Current Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__CURRENT_VELOCITY = MIN_SWITCH_ACTOR_FEATURE_COUNT + 13;

  /**
   * The feature id for the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__ACCELERATION = MIN_SWITCH_ACTOR_FEATURE_COUNT + 14;

  /**
   * The feature id for the '<em><b>Pwm Frequency</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__PWM_FREQUENCY = MIN_SWITCH_ACTOR_FEATURE_COUNT + 15;

  /**
   * The feature id for the '<em><b>Drive Mode</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__DRIVE_MODE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 16;

  /**
   * The feature id for the '<em><b>Switch On Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC__SWITCH_ON_VELOCITY = MIN_SWITCH_ACTOR_FEATURE_COUNT + 17;

  /**
   * The number of structural features of the '<em>MBrick DC</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC_FEATURE_COUNT = MIN_SWITCH_ACTOR_FEATURE_COUNT + 18;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC___TURN_SWITCH__ONOFFVALUE = MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC___FETCH_SWITCH_STATE = MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC___ENABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC___DISABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC___INIT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The number of operations of the '<em>MBrick DC</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICK_DC_OPERATION_COUNT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 4;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>MDual Relay Bricklet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET___INIT = MDEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>MDual Relay Bricklet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_BRICKLET_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>MIndustrial Quad Relay Bricklet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET___INIT = MDEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>MIndustrial Quad Relay Bricklet</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_BRICKLET_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE = MIN_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__LOGGER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__ENABLED_A = MIN_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__SUB_ID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__MBRICK = MIN_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY__DEVICE_TYPE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MIndustrial Quad Relay</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_FEATURE_COUNT = MIN_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY___TURN_SWITCH__ONOFFVALUE = MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY___FETCH_SWITCH_STATE = MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY___INIT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY___ENABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY___DISABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MIndustrial Quad Relay</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_QUAD_RELAY_OPERATION_COUNT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__MSUBDEVICES = MSUB_DEVICE_HOLDER__MSUBDEVICES;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__LOGGER = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__UID = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__ENABLED_A = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__TINKERFORGE_DEVICE = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__IP_CONNECTION = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__CONNECTED_UID = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__POSITION = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__DEVICE_IDENTIFIER = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__NAME = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__BRICKD = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Debounce Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__DEBOUNCE_PERIOD = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 10;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__TF_CONFIG = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 11;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4__DEVICE_TYPE = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 12;

  /**
   * The number of structural features of the '<em>MBricklet Industrial Digital In4</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4_FEATURE_COUNT = MSUB_DEVICE_HOLDER_FEATURE_COUNT + 13;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4___INIT_SUB_DEVICES = MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4___INIT = MSUB_DEVICE_HOLDER_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4___ENABLE = MSUB_DEVICE_HOLDER_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4___DISABLE = MSUB_DEVICE_HOLDER_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MBricklet Industrial Digital In4</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_INDUSTRIAL_DIGITAL_IN4_OPERATION_COUNT = MSUB_DEVICE_HOLDER_OPERATION_COUNT + 3;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__LOGGER = MSUB_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__UID = MSUB_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__ENABLED_A = MSUB_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__SUB_ID = MSUB_DEVICE__SUB_ID;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__MBRICK = MSUB_DEVICE__MBRICK;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE = MSUB_DEVICE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>MIndustrial Digital In</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN_FEATURE_COUNT = MSUB_DEVICE_FEATURE_COUNT + 1;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN___INIT = MSUB_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN___ENABLE = MSUB_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN___DISABLE = MSUB_DEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN___FETCH_SENSOR_VALUE = MSUB_DEVICE_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>MIndustrial Digital In</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MINDUSTRIAL_DIGITAL_IN_OPERATION_COUNT = MSUB_DEVICE_OPERATION_COUNT + 1;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__LOGGER = IO_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__UID = IO_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__ENABLED_A = IO_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__SUB_ID = IO_DEVICE__SUB_ID;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__MBRICK = IO_DEVICE__MBRICK;

  /**
   * The feature id for the '<em><b>Generic Device Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__GENERIC_DEVICE_ID = IO_DEVICE__GENERIC_DEVICE_ID;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__TF_CONFIG = IO_DEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__DEVICE_TYPE = IO_DEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Digital State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__DIGITAL_STATE = IO_DEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__PORT = IO_DEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Pin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__PIN = IO_DEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Default State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR__DEFAULT_STATE = IO_DEVICE_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Digital Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR_FEATURE_COUNT = IO_DEVICE_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR___INIT = IO_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR___ENABLE = IO_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR___DISABLE = IO_DEVICE___DISABLE;

  /**
   * The operation id for the '<em>Turn Digital</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR___TURN_DIGITAL__HIGHLOWVALUE = IO_DEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Fetch Digital Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR___FETCH_DIGITAL_VALUE = IO_DEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>Digital Actor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_ACTOR_OPERATION_COUNT = IO_DEVICE_OPERATION_COUNT + 2;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Debounce Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__DEBOUNCE_PERIOD = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__TF_CONFIG = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>MBricklet IO16</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16___INIT = MDEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>MBricklet IO16</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_IO16_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__LOGGER = IO_DEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__UID = IO_DEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__ENABLED_A = IO_DEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__SUB_ID = IO_DEVICE__SUB_ID;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__MBRICK = IO_DEVICE__MBRICK;

  /**
   * The feature id for the '<em><b>Generic Device Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__GENERIC_DEVICE_ID = IO_DEVICE__GENERIC_DEVICE_ID;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__SENSOR_VALUE = IO_DEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__TF_CONFIG = IO_DEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__DEVICE_TYPE = IO_DEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Pull Up Resistor Enabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED = IO_DEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__PORT = IO_DEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Pin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR__PIN = IO_DEVICE_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Digital Sensor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR_FEATURE_COUNT = IO_DEVICE_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR___INIT = IO_DEVICE___INIT;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR___ENABLE = IO_DEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR___DISABLE = IO_DEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR___FETCH_SENSOR_VALUE = IO_DEVICE_OPERATION_COUNT + 0;

  /**
   * The number of operations of the '<em>Digital Sensor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DIGITAL_SENSOR_OPERATION_COUNT = IO_DEVICE_OPERATION_COUNT + 1;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__SWITCH_STATE = MIN_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__LOGGER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__ENABLED_A = MIN_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__SUB_ID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__MBRICK = MIN_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY__DEVICE_TYPE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MDual Relay</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_FEATURE_COUNT = MIN_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY___TURN_SWITCH__ONOFFVALUE = MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY___FETCH_SWITCH_STATE = MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY___INIT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY___ENABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY___DISABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MDual Relay</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDUAL_RELAY_OPERATION_COUNT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__SENSOR_VALUE = MSENSOR__SENSOR_VALUE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__LOGGER = MSENSOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__UID = MSENSOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__ENABLED_A = MSENSOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__TINKERFORGE_DEVICE = MSENSOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__IP_CONNECTION = MSENSOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__CONNECTED_UID = MSENSOR_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__POSITION = MSENSOR_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__DEVICE_IDENTIFIER = MSENSOR_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__NAME = MSENSOR_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__BRICKD = MSENSOR_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__TF_CONFIG = MSENSOR_FEATURE_COUNT + 10;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__CALLBACK_PERIOD = MSENSOR_FEATURE_COUNT + 11;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__DEVICE_TYPE = MSENSOR_FEATURE_COUNT + 12;

  /**
   * The feature id for the '<em><b>Humiditiy</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__HUMIDITIY = MSENSOR_FEATURE_COUNT + 13;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY__THRESHOLD = MSENSOR_FEATURE_COUNT + 14;

  /**
   * The number of structural features of the '<em>MBricklet Humidity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY_FEATURE_COUNT = MSENSOR_FEATURE_COUNT + 15;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY___FETCH_SENSOR_VALUE = MSENSOR___FETCH_SENSOR_VALUE;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY___ENABLE = MSENSOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY___DISABLE = MSENSOR_OPERATION_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY___INIT = MSENSOR_OPERATION_COUNT + 3;

  /**
   * The number of operations of the '<em>MBricklet Humidity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_HUMIDITY_OPERATION_COUNT = MSENSOR_OPERATION_COUNT + 4;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__SENSOR_VALUE = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__TF_CONFIG = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__CALLBACK_PERIOD = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Distance</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__DISTANCE = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR__THRESHOLD = MDEVICE_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MBricklet Distance IR</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR___FETCH_SENSOR_VALUE = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR___INIT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>MBricklet Distance IR</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_DISTANCE_IR_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__SENSOR_VALUE = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__TF_CONFIG = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__CALLBACK_PERIOD = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Temperature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__TEMPERATURE = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE__THRESHOLD = MDEVICE_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MBricklet Temperature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE___FETCH_SENSOR_VALUE = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE___INIT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>MBricklet Temperature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_TEMPERATURE_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__SENSOR_VALUE = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__TF_CONFIG = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__CALLBACK_PERIOD = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Air Pressure</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__AIR_PRESSURE = MDEVICE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER__THRESHOLD = MDEVICE_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>MBricklet Barometer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 7;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER___FETCH_SENSOR_VALUE = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER___INIT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MBricklet Barometer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_BAROMETER_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 3;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__SENSOR_VALUE = MSENSOR__SENSOR_VALUE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__LOGGER = MSENSOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__UID = MSENSOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__ENABLED_A = MSENSOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__SUB_ID = MSENSOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__MBRICK = MSENSOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__DEVICE_TYPE = MSENSOR_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Temperature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE__TEMPERATURE = MSENSOR_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>MBarometer Temperature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE_FEATURE_COUNT = MSENSOR_FEATURE_COUNT + 7;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE___FETCH_SENSOR_VALUE = MSENSOR___FETCH_SENSOR_VALUE;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE___ENABLE = MSENSOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE___DISABLE = MSENSOR_OPERATION_COUNT + 2;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE___INIT = MSENSOR_OPERATION_COUNT + 3;

  /**
   * The number of operations of the '<em>MBarometer Temperature</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBAROMETER_TEMPERATURE_OPERATION_COUNT = MSENSOR_OPERATION_COUNT + 4;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Sensor Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__SENSOR_VALUE = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__TF_CONFIG = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__CALLBACK_PERIOD = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Illuminance</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__ILLUMINANCE = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT__THRESHOLD = MDEVICE_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MBricklet Ambient Light</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Fetch Sensor Value</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT___FETCH_SENSOR_VALUE = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT___INIT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>MBricklet Ambient Light</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_AMBIENT_LIGHT_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__LOGGER = MDEVICE__LOGGER;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__UID = MDEVICE__UID;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__ENABLED_A = MDEVICE__ENABLED_A;

  /**
   * The feature id for the '<em><b>Tinkerforge Device</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE = MDEVICE__TINKERFORGE_DEVICE;

  /**
   * The feature id for the '<em><b>Ip Connection</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__IP_CONNECTION = MDEVICE__IP_CONNECTION;

  /**
   * The feature id for the '<em><b>Connected Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__CONNECTED_UID = MDEVICE__CONNECTED_UID;

  /**
   * The feature id for the '<em><b>Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__POSITION = MDEVICE__POSITION;

  /**
   * The feature id for the '<em><b>Device Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER = MDEVICE__DEVICE_IDENTIFIER;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__NAME = MDEVICE__NAME;

  /**
   * The feature id for the '<em><b>Brickd</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__BRICKD = MDEVICE__BRICKD;

  /**
   * The feature id for the '<em><b>Text</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__TEXT = MDEVICE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Msubdevices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__MSUBDEVICES = MDEVICE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__DEVICE_TYPE = MDEVICE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Position Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__POSITION_PREFIX = MDEVICE_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Positon Suffix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__POSITON_SUFFIX = MDEVICE_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Display Errors</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__DISPLAY_ERRORS = MDEVICE_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Error Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4__ERROR_PREFIX = MDEVICE_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>MBricklet LCD2 0x4</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4_FEATURE_COUNT = MDEVICE_FEATURE_COUNT + 7;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4___ENABLE = MDEVICE___ENABLE;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4___DISABLE = MDEVICE___DISABLE;

  /**
   * The operation id for the '<em>Init Sub Devices</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4___INIT_SUB_DEVICES = MDEVICE_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4___INIT = MDEVICE_OPERATION_COUNT + 1;

  /**
   * The number of operations of the '<em>MBricklet LCD2 0x4</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MBRICKLET_LCD2_0X4_OPERATION_COUNT = MDEVICE_OPERATION_COUNT + 2;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4BacklightImpl <em>MLCD2 0x4 Backlight</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4BacklightImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCD20x4Backlight()
   * @generated
   */
  int MLCD2_0X4_BACKLIGHT = 37;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__SWITCH_STATE = MIN_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__LOGGER = MIN_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__UID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__ENABLED_A = MIN_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__SUB_ID = MIN_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__MBRICK = MIN_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT__DEVICE_TYPE = MIN_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>MLCD2 0x4 Backlight</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT_FEATURE_COUNT = MIN_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT___TURN_SWITCH__ONOFFVALUE = MIN_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT___FETCH_SWITCH_STATE = MIN_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT___INIT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT___ENABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT___DISABLE = MIN_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MLCD2 0x4 Backlight</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BACKLIGHT_OPERATION_COUNT = MIN_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl <em>MLCD2 0x4 Button</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCD20x4Button()
   * @generated
   */
  int MLCD2_0X4_BUTTON = 38;

  /**
   * The feature id for the '<em><b>Switch State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__SWITCH_STATE = MOUT_SWITCH_ACTOR__SWITCH_STATE;

  /**
   * The feature id for the '<em><b>Logger</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__LOGGER = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__UID = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Enabled A</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__ENABLED_A = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Sub Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__SUB_ID = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Mbrick</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__MBRICK = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Device Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__DEVICE_TYPE = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Button Num</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__BUTTON_NUM = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON__CALLBACK_PERIOD = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 7;

  /**
   * The number of structural features of the '<em>MLCD2 0x4 Button</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON_FEATURE_COUNT = MOUT_SWITCH_ACTOR_FEATURE_COUNT + 8;

  /**
   * The operation id for the '<em>Turn Switch</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON___TURN_SWITCH__ONOFFVALUE = MOUT_SWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE;

  /**
   * The operation id for the '<em>Fetch Switch State</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON___FETCH_SWITCH_STATE = MOUT_SWITCH_ACTOR___FETCH_SWITCH_STATE;

  /**
   * The operation id for the '<em>Init</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON___INIT = MOUT_SWITCH_ACTOR_OPERATION_COUNT + 0;

  /**
   * The operation id for the '<em>Enable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON___ENABLE = MOUT_SWITCH_ACTOR_OPERATION_COUNT + 1;

  /**
   * The operation id for the '<em>Disable</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON___DISABLE = MOUT_SWITCH_ACTOR_OPERATION_COUNT + 2;

  /**
   * The number of operations of the '<em>MLCD2 0x4 Button</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MLCD2_0X4_BUTTON_OPERATION_COUNT = MOUT_SWITCH_ACTOR_OPERATION_COUNT + 3;

  /**
   * The number of structural features of the '<em>TF Config</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_CONFIG_FEATURE_COUNT = 0;

  /**
   * The number of operations of the '<em>TF Config</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_CONFIG_OPERATION_COUNT = 0;

  /**
   * The feature id for the '<em><b>Uid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__UID = 0;

  /**
   * The feature id for the '<em><b>Subid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__SUBID = 1;

  /**
   * The feature id for the '<em><b>Ohid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__OHID = 2;

  /**
   * The feature id for the '<em><b>Sub Device Ids</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__SUB_DEVICE_IDS = 3;

  /**
   * The feature id for the '<em><b>Tf Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__TF_CONFIG = 4;

  /**
   * The feature id for the '<em><b>Oh Config</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE__OH_CONFIG = 5;

  /**
   * The number of structural features of the '<em>OHTF Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE_FEATURE_COUNT = 6;

  /**
   * The operation id for the '<em>Is Valid Sub Id</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE___IS_VALID_SUB_ID__STRING = 0;

  /**
   * The number of operations of the '<em>OHTF Device</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OHTF_DEVICE_OPERATION_COUNT = 1;

  /**
   * The feature id for the '<em><b>Oh Tf Devices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OH_CONFIG__OH_TF_DEVICES = 0;

  /**
   * The number of structural features of the '<em>OH Config</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OH_CONFIG_FEATURE_COUNT = 1;

  /**
   * The operation id for the '<em>Get Config By TF Id</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OH_CONFIG___GET_CONFIG_BY_TF_ID__STRING_STRING = 0;

  /**
   * The operation id for the '<em>Get Config By OH Id</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OH_CONFIG___GET_CONFIG_BY_OH_ID__STRING = 1;

  /**
   * The number of operations of the '<em>OH Config</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OH_CONFIG_OPERATION_COUNT = 2;

  /**
   * The number of structural features of the '<em>TF Null Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_NULL_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The number of operations of the '<em>TF Null Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_NULL_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BASE_CONFIGURATION__THRESHOLD = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Callback Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BASE_CONFIGURATION__CALLBACK_PERIOD = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TF Base Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BASE_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 2;

  /**
   * The number of operations of the '<em>TF Base Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BASE_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION__VELOCITY = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION__ACCELERATION = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Pwm Frequency</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY = TF_CONFIG_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Drive Mode</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION__DRIVE_MODE = TF_CONFIG_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Switch On Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY = TF_CONFIG_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>TF Brick DC Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 5;

  /**
   * The number of operations of the '<em>TF Brick DC Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_BRICK_DC_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Default State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TFIO Actor Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_ACTOR_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The number of operations of the '<em>TFIO Actor Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_ACTOR_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Debounce Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TF Interrupt Listener Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_INTERRUPT_LISTENER_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The number of operations of the '<em>TF Interrupt Listener Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_INTERRUPT_LISTENER_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Pull Up Resistor Enabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TFIO Sensor Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_SENSOR_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The number of operations of the '<em>TFIO Sensor Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TFIO_SENSOR_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The feature id for the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__VELOCITY = TF_CONFIG_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__ACCELERATION = TF_CONFIG_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Servo Voltage</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__SERVO_VOLTAGE = TF_CONFIG_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Pulse Width Min</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN = TF_CONFIG_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Pulse Width Max</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX = TF_CONFIG_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__PERIOD = TF_CONFIG_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Output Voltage</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE = TF_CONFIG_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>TF Servo Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION_FEATURE_COUNT = TF_CONFIG_FEATURE_COUNT + 7;

  /**
   * The number of operations of the '<em>TF Servo Configuration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TF_SERVO_CONFIGURATION_OPERATION_COUNT = TF_CONFIG_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '<em>Switch State</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.types.OnOffValue
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getSwitchState()
   * @generated
   */
  int SWITCH_STATE = 68;

  /**
   * The meta object id for the '<em>Digital Value</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.types.HighLowValue
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalValue()
   * @generated
   */
  int DIGITAL_VALUE = 69;

  /**
   * The meta object id for the '<em>Tinker Bricklet IO16</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletIO16
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTinkerBrickletIO16()
   * @generated
   */
  int TINKER_BRICKLET_IO16 = 70;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.DCDriveMode <em>DC Drive Mode</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.DCDriveMode
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDCDriveMode()
   * @generated
   */
  int DC_DRIVE_MODE = 49;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.NoSubIds <em>No Sub Ids</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.NoSubIds
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getNoSubIds()
   * @generated
   */
  int NO_SUB_IDS = 50;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs <em>Industrial Digital In Sub IDs</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIndustrialDigitalInSubIDs()
   * @generated
   */
  int INDUSTRIAL_DIGITAL_IN_SUB_IDS = 51;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs <em>Industrial Quad Relay IDs</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIndustrialQuadRelayIDs()
   * @generated
   */
  int INDUSTRIAL_QUAD_RELAY_IDS = 52;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.ServoSubIDs <em>Servo Sub IDs</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.ServoSubIDs
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getServoSubIDs()
   * @generated
   */
  int SERVO_SUB_IDS = 53;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs <em>Barometer Sub IDs</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getBarometerSubIDs()
   * @generated
   */
  int BAROMETER_SUB_IDS = 54;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.IO16SubIds <em>IO16 Sub Ids</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.IO16SubIds
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIO16SubIds()
   * @generated
   */
  int IO16_SUB_IDS = 55;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds <em>Dual Relay Sub Ids</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDualRelaySubIds()
   * @generated
   */
  int DUAL_RELAY_SUB_IDS = 56;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds <em>LCD Button Sub Ids</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getLCDButtonSubIds()
   * @generated
   */
  int LCD_BUTTON_SUB_IDS = 57;

  /**
   * The meta object id for the '{@link org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds <em>LCD Backlight Sub Ids</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getLCDBacklightSubIds()
   * @generated
   */
  int LCD_BACKLIGHT_SUB_IDS = 58;

  /**
   * The meta object id for the '<em>MIP Connection</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.IPConnection
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIPConnection()
   * @generated
   */
  int MIP_CONNECTION = 59;

  /**
   * The meta object id for the '<em>MTinker Device</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.Device
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerDevice()
   * @generated
   */
  int MTINKER_DEVICE = 60;

  /**
   * The meta object id for the '<em>MLogger</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.slf4j.Logger
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLogger()
   * @generated
   */
  int MLOGGER = 61;


  /**
   * The meta object id for the '<em>MAtomic Boolean</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.concurrent.atomic.AtomicBoolean
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMAtomicBoolean()
   * @generated
   */
  int MATOMIC_BOOLEAN = 62;

  /**
   * The meta object id for the '<em>MTinkerforge Device</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.Device
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerforgeDevice()
   * @generated
   */
  int MTINKERFORGE_DEVICE = 63;

  /**
   * The meta object id for the '<em>MTinker Brick DC</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickDC
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickDC()
   * @generated
   */
  int MTINKER_BRICK_DC = 64;

  /**
   * The meta object id for the '<em>MTinker Brick Servo</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickServo
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickServo()
   * @generated
   */
  int MTINKER_BRICK_SERVO = 71;


  /**
   * The meta object id for the '<em>MTinkerforge Value</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerforgeValue()
   * @generated
   */
  int MTINKERFORGE_VALUE = 72;

  /**
   * The meta object id for the '<em>MDecimal Value</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.binding.tinkerforge.internal.types.DecimalValue
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDecimalValue()
   * @generated
   */
  int MDECIMAL_VALUE = 73;

  /**
   * The meta object id for the '<em>MTinker Bricklet Humidity</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletHumidity
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletHumidity()
   * @generated
   */
  int MTINKER_BRICKLET_HUMIDITY = 74;

  /**
   * The meta object id for the '<em>MTinker Bricklet Distance IR</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletDistanceIR
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletDistanceIR()
   * @generated
   */
  int MTINKER_BRICKLET_DISTANCE_IR = 75;

  /**
   * The meta object id for the '<em>MTinker Bricklet Temperature</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletTemperature
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletTemperature()
   * @generated
   */
  int MTINKER_BRICKLET_TEMPERATURE = 76;

  /**
   * The meta object id for the '<em>MTinker Bricklet Barometer</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletBarometer
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletBarometer()
   * @generated
   */
  int MTINKER_BRICKLET_BAROMETER = 77;

  /**
   * The meta object id for the '<em>MTinker Bricklet Ambient Light</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletAmbientLight
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletAmbientLight()
   * @generated
   */
  int MTINKER_BRICKLET_AMBIENT_LIGHT = 78;

  /**
   * The meta object id for the '<em>MTinker Bricklet LCD2 0x4</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletLCD20x4
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletLCD20x4()
   * @generated
   */
  int MTINKER_BRICKLET_LCD2_0X4 = 79;

  /**
   * The meta object id for the '<em>Enum</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.Enum
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getEnum()
   * @generated
   */
  int ENUM = 80;

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFConfig <em>TF Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Config</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFConfig
   * @generated
   */
  EClass getTFConfig();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice <em>OHTF Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>OHTF Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice
   * @generated
   */
  EClass getOHTFDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getUid <em>Uid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Uid</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getUid()
   * @see #getOHTFDevice()
   * @generated
   */
  EAttribute getOHTFDevice_Uid();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubid <em>Subid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Subid</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubid()
   * @see #getOHTFDevice()
   * @generated
   */
  EAttribute getOHTFDevice_Subid();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhid <em>Ohid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Ohid</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhid()
   * @see #getOHTFDevice()
   * @generated
   */
  EAttribute getOHTFDevice_Ohid();

  /**
   * Returns the meta object for the attribute list '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubDeviceIds <em>Sub Device Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Sub Device Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getSubDeviceIds()
   * @see #getOHTFDevice()
   * @generated
   */
  EAttribute getOHTFDevice_SubDeviceIds();

  /**
   * Returns the meta object for the containment reference '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getTfConfig <em>Tf Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Tf Config</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getTfConfig()
   * @see #getOHTFDevice()
   * @generated
   */
  EReference getOHTFDevice_TfConfig();

  /**
   * Returns the meta object for the container reference '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig <em>Oh Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Oh Config</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#getOhConfig()
   * @see #getOHTFDevice()
   * @generated
   */
  EReference getOHTFDevice_OhConfig();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice#isValidSubId(java.lang.String) <em>Is Valid Sub Id</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Is Valid Sub Id</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice#isValidSubId(java.lang.String)
   * @generated
   */
  EOperation getOHTFDevice__IsValidSubId__String();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig <em>OH Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>OH Config</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig
   * @generated
   */
  EClass getOHConfig();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig#getOhTfDevices <em>Oh Tf Devices</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Oh Tf Devices</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig#getOhTfDevices()
   * @see #getOHConfig()
   * @generated
   */
  EReference getOHConfig_OhTfDevices();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig#getConfigByTFId(java.lang.String, java.lang.String) <em>Get Config By TF Id</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Config By TF Id</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig#getConfigByTFId(java.lang.String, java.lang.String)
   * @generated
   */
  EOperation getOHConfig__GetConfigByTFId__String_String();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig#getConfigByOHId(java.lang.String) <em>Get Config By OH Id</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Config By OH Id</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig#getConfigByOHId(java.lang.String)
   * @generated
   */
  EOperation getOHConfig__GetConfigByOHId__String();

  /**
   * The meta object id for the '<em>MTinker Bricklet Dual Relay</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletDualRelay
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletDualRelay()
   * @generated
   */
  int MTINKER_BRICKLET_DUAL_RELAY = 65;


  /**
   * The meta object id for the '<em>MTinker Bricklet Industrial Quad Relay</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletIndustrialQuadRelay
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletIndustrialQuadRelay()
   * @generated
   */
  int MTINKER_BRICKLET_INDUSTRIAL_QUAD_RELAY = 66;

  /**
   * The meta object id for the '<em>MTinker Bricklet Industrial Digital In4</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.tinkerforge.BrickletIndustrialDigitalIn4
   * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletIndustrialDigitalIn4()
   * @generated
   */
  int MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_IN4 = 67;

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem <em>Ecosystem</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Ecosystem</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem
   * @generated
   */
  EClass getEcosystem();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getLogger <em>Logger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Logger</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getLogger()
   * @see #getEcosystem()
   * @generated
   */
  EAttribute getEcosystem_Logger();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getMbrickds <em>Mbrickds</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Mbrickds</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getMbrickds()
   * @see #getEcosystem()
   * @generated
   */
  EReference getEcosystem_Mbrickds();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getBrickd(java.lang.String, int) <em>Get Brickd</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Brickd</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getBrickd(java.lang.String, int)
   * @generated
   */
  EOperation getEcosystem__GetBrickd__String_int();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getDevice(java.lang.String, java.lang.String) <em>Get Device</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Device</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getDevice(java.lang.String, java.lang.String)
   * @generated
   */
  EOperation getEcosystem__GetDevice__String_String();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#getDevices4GenericId(java.lang.String, java.lang.String) <em>Get Devices4 Generic Id</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Devices4 Generic Id</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#getDevices4GenericId(java.lang.String, java.lang.String)
   * @generated
   */
  EOperation getEcosystem__GetDevices4GenericId__String_String();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem#disconnect() <em>Disconnect</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Disconnect</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem#disconnect()
   * @generated
   */
  EOperation getEcosystem__Disconnect();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd <em>MBrickd</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBrickd</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd
   * @generated
   */
  EClass getMBrickd();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getLogger <em>Logger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Logger</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getLogger()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_Logger();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getIpConnection <em>Ip Connection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Ip Connection</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getIpConnection()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_IpConnection();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getHost <em>Host</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Host</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getHost()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_Host();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getPort <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Port</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getPort()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_Port();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isIsConnected <em>Is Connected</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Is Connected</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#isIsConnected()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_IsConnected();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#isAutoReconnect <em>Auto Reconnect</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Auto Reconnect</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#isAutoReconnect()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_AutoReconnect();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getTimeout <em>Timeout</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Timeout</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getTimeout()
   * @see #getMBrickd()
   * @generated
   */
  EAttribute getMBrickd_Timeout();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getMdevices <em>Mdevices</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Mdevices</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getMdevices()
   * @see #getMBrickd()
   * @generated
   */
  EReference getMBrickd_Mdevices();

  /**
   * Returns the meta object for the container reference '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getEcosystem <em>Ecosystem</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Ecosystem</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getEcosystem()
   * @see #getMBrickd()
   * @generated
   */
  EReference getMBrickd_Ecosystem();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#connect() <em>Connect</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Connect</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#connect()
   * @generated
   */
  EOperation getMBrickd__Connect();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#disconnect() <em>Disconnect</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Disconnect</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#disconnect()
   * @generated
   */
  EOperation getMBrickd__Disconnect();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#init()
   * @generated
   */
  EOperation getMBrickd__Init();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd#getDevice(java.lang.String) <em>Get Device</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Device</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd#getDevice(java.lang.String)
   * @generated
   */
  EOperation getMBrickd__GetDevice__String();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer <em>MTF Config Consumer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MTF Config Consumer</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
   * @generated
   */
  EClass getMTFConfigConsumer();

  /**
   * Returns the meta object for the containment reference '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer#getTfConfig <em>Tf Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Tf Config</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer#getTfConfig()
   * @see #getMTFConfigConsumer()
   * @generated
   */
  EReference getMTFConfigConsumer_TfConfig();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice <em>MBase Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBase Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice
   * @generated
   */
  EClass getMBaseDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getLogger <em>Logger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Logger</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getLogger()
   * @see #getMBaseDevice()
   * @generated
   */
  EAttribute getMBaseDevice_Logger();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getUid <em>Uid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Uid</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getUid()
   * @see #getMBaseDevice()
   * @generated
   */
  EAttribute getMBaseDevice_Uid();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getEnabledA <em>Enabled A</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Enabled A</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#getEnabledA()
   * @see #getMBaseDevice()
   * @generated
   */
  EAttribute getMBaseDevice_EnabledA();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#init()
   * @generated
   */
  EOperation getMBaseDevice__Init();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#enable() <em>Enable</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Enable</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#enable()
   * @generated
   */
  EOperation getMBaseDevice__Enable();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice#disable() <em>Disable</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Disable</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice#disable()
   * @generated
   */
  EOperation getMBaseDevice__Disable();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MDevice <em>MDevice</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MDevice</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice
   * @generated
   */
  EClass getMDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getTinkerforgeDevice <em>Tinkerforge Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Tinkerforge Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getTinkerforgeDevice()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_TinkerforgeDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getIpConnection <em>Ip Connection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Ip Connection</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getIpConnection()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_IpConnection();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getConnectedUid <em>Connected Uid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Connected Uid</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getConnectedUid()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_ConnectedUid();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getPosition <em>Position</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Position</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getPosition()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_Position();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getDeviceIdentifier <em>Device Identifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Identifier</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getDeviceIdentifier()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_DeviceIdentifier();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getName()
   * @see #getMDevice()
   * @generated
   */
  EAttribute getMDevice_Name();

  /**
   * Returns the meta object for the container reference '{@link org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd <em>Brickd</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Brickd</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice#getBrickd()
   * @see #getMDevice()
   * @generated
   */
  EReference getMDevice_Brickd();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder <em>MSub Device Holder</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MSub Device Holder</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder
   * @generated
   */
  EClass getMSubDeviceHolder();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices <em>Msubdevices</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Msubdevices</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#getMsubdevices()
   * @see #getMSubDeviceHolder()
   * @generated
   */
  EReference getMSubDeviceHolder_Msubdevices();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#initSubDevices() <em>Init Sub Devices</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init Sub Devices</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder#initSubDevices()
   * @generated
   */
  EOperation getMSubDeviceHolder__InitSubDevices();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickServo <em>MBrick Servo</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBrick Servo</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickServo
   * @generated
   */
  EClass getMBrickServo();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickServo#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickServo#getDeviceType()
   * @see #getMBrickServo()
   * @generated
   */
  EAttribute getMBrickServo_DeviceType();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickServo#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickServo#init()
   * @generated
   */
  EOperation getMBrickServo__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration <em>TF Brick DC Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Brick DC Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration
   * @generated
   */
  EClass getTFBrickDCConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getVelocity <em>Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getVelocity()
   * @see #getTFBrickDCConfiguration()
   * @generated
   */
  EAttribute getTFBrickDCConfiguration_Velocity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getAcceleration <em>Acceleration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Acceleration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getAcceleration()
   * @see #getTFBrickDCConfiguration()
   * @generated
   */
  EAttribute getTFBrickDCConfiguration_Acceleration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getPwmFrequency <em>Pwm Frequency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pwm Frequency</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getPwmFrequency()
   * @see #getTFBrickDCConfiguration()
   * @generated
   */
  EAttribute getTFBrickDCConfiguration_PwmFrequency();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getDriveMode <em>Drive Mode</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Drive Mode</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getDriveMode()
   * @see #getTFBrickDCConfiguration()
   * @generated
   */
  EAttribute getTFBrickDCConfiguration_DriveMode();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getSwitchOnVelocity <em>Switch On Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Switch On Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration#getSwitchOnVelocity()
   * @see #getTFBrickDCConfiguration()
   * @generated
   */
  EAttribute getTFBrickDCConfiguration_SwitchOnVelocity();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC <em>MBrick DC</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBrick DC</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC
   * @generated
   */
  EClass getMBrickDC();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDeviceType()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getVelocity <em>Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getVelocity()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_Velocity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getCurrentVelocity <em>Current Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Current Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getCurrentVelocity()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_CurrentVelocity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getAcceleration <em>Acceleration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Acceleration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getAcceleration()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_Acceleration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getPwmFrequency <em>Pwm Frequency</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pwm Frequency</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getPwmFrequency()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_PwmFrequency();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDriveMode <em>Drive Mode</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Drive Mode</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDriveMode()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_DriveMode();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getSwitchOnVelocity <em>Switch On Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Switch On Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#getSwitchOnVelocity()
   * @see #getMBrickDC()
   * @generated
   */
  EAttribute getMBrickDC_SwitchOnVelocity();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC#init()
   * @generated
   */
  EOperation getMBrickDC__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet <em>MDual Relay Bricklet</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MDual Relay Bricklet</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet
   * @generated
   */
  EClass getMDualRelayBricklet();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet#getDeviceType()
   * @see #getMDualRelayBricklet()
   * @generated
   */
  EAttribute getMDualRelayBricklet_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet <em>MIndustrial Quad Relay Bricklet</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MIndustrial Quad Relay Bricklet</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet
   * @generated
   */
  EClass getMIndustrialQuadRelayBricklet();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay <em>MIndustrial Quad Relay</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MIndustrial Quad Relay</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay
   * @generated
   */
  EClass getMIndustrialQuadRelay();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay#getDeviceType()
   * @see #getMIndustrialQuadRelay()
   * @generated
   */
  EAttribute getMIndustrialQuadRelay_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4 <em>MBricklet Industrial Digital In4</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Industrial Digital In4</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4
   * @generated
   */
  EClass getMBrickletIndustrialDigitalIn4();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4#getDeviceType()
   * @see #getMBrickletIndustrialDigitalIn4()
   * @generated
   */
  EAttribute getMBrickletIndustrialDigitalIn4_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn <em>MIndustrial Digital In</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MIndustrial Digital In</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn
   * @generated
   */
  EClass getMIndustrialDigitalIn();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MActor <em>MActor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MActor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MActor
   * @generated
   */
  EClass getMActor();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor <em>MSwitch Actor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MSwitch Actor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor
   * @generated
   */
  EClass getMSwitchActor();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor#getSwitchState <em>Switch State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Switch State</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor#getSwitchState()
   * @see #getMSwitchActor()
   * @generated
   */
  EAttribute getMSwitchActor_SwitchState();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor#turnSwitch(org.openhab.binding.tinkerforge.internal.types.OnOffValue) <em>Turn Switch</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Turn Switch</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor#turnSwitch(org.openhab.binding.tinkerforge.internal.types.OnOffValue)
   * @generated
   */
  EOperation getMSwitchActor__TurnSwitch__OnOffValue();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor#fetchSwitchState() <em>Fetch Switch State</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Fetch Switch State</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor#fetchSwitchState()
   * @generated
   */
  EOperation getMSwitchActor__FetchSwitchState();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor <em>MOut Switch Actor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MOut Switch Actor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor
   * @generated
   */
  EClass getMOutSwitchActor();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MInSwitchActor <em>MIn Switch Actor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MIn Switch Actor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MInSwitchActor
   * @generated
   */
  EClass getMInSwitchActor();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice <em>Generic Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Generic Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.GenericDevice
   * @generated
   */
  EClass getGenericDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice#getGenericDeviceId <em>Generic Device Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Generic Device Id</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.GenericDevice#getGenericDeviceId()
   * @see #getGenericDevice()
   * @generated
   */
  EAttribute getGenericDevice_GenericDeviceId();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration <em>TFIO Actor Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TFIO Actor Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration
   * @generated
   */
  EClass getTFIOActorConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration#isDefaultState <em>Default State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default State</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration#isDefaultState()
   * @see #getTFIOActorConfiguration()
   * @generated
   */
  EAttribute getTFIOActorConfiguration_DefaultState();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor <em>Digital Actor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Digital Actor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor
   * @generated
   */
  EClass getDigitalActor();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDeviceType()
   * @see #getDigitalActor()
   * @generated
   */
  EAttribute getDigitalActor_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState <em>Digital State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Digital State</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState()
   * @see #getDigitalActor()
   * @generated
   */
  EAttribute getDigitalActor_DigitalState();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPort <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Port</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPort()
   * @see #getDigitalActor()
   * @generated
   */
  EAttribute getDigitalActor_Port();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPin <em>Pin</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pin</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#getPin()
   * @see #getDigitalActor()
   * @generated
   */
  EAttribute getDigitalActor_Pin();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#isDefaultState <em>Default State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default State</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#isDefaultState()
   * @see #getDigitalActor()
   * @generated
   */
  EAttribute getDigitalActor_DefaultState();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#turnDigital(org.openhab.binding.tinkerforge.internal.types.HighLowValue) <em>Turn Digital</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Turn Digital</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#turnDigital(org.openhab.binding.tinkerforge.internal.types.HighLowValue)
   * @generated
   */
  EOperation getDigitalActor__TurnDigital__HighLowValue();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#fetchDigitalValue() <em>Fetch Digital Value</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Fetch Digital Value</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor#fetchDigitalValue()
   * @generated
   */
  EOperation getDigitalActor__FetchDigitalValue();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration <em>TF Interrupt Listener Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Interrupt Listener Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration
   * @generated
   */
  EClass getTFInterruptListenerConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration#getDebouncePeriod <em>Debounce Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Debounce Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration#getDebouncePeriod()
   * @see #getTFInterruptListenerConfiguration()
   * @generated
   */
  EAttribute getTFInterruptListenerConfiguration_DebouncePeriod();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIO16 <em>MBricklet IO16</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet IO16</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIO16
   * @generated
   */
  EClass getMBrickletIO16();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIO16#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIO16#getDeviceType()
   * @see #getMBrickletIO16()
   * @generated
   */
  EAttribute getMBrickletIO16_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.IODevice <em>IO Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IO Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.IODevice
   * @generated
   */
  EClass getIODevice();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration <em>TFIO Sensor Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TFIO Sensor Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration
   * @generated
   */
  EClass getTFIOSensorConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pull Up Resistor Enabled</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration#isPullUpResistorEnabled()
   * @see #getTFIOSensorConfiguration()
   * @generated
   */
  EAttribute getTFIOSensorConfiguration_PullUpResistorEnabled();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor <em>Digital Sensor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Digital Sensor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor
   * @generated
   */
  EClass getDigitalSensor();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getDeviceType()
   * @see #getDigitalSensor()
   * @generated
   */
  EAttribute getDigitalSensor_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pull Up Resistor Enabled</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor#isPullUpResistorEnabled()
   * @see #getDigitalSensor()
   * @generated
   */
  EAttribute getDigitalSensor_PullUpResistorEnabled();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getPort <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Port</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getPort()
   * @see #getDigitalSensor()
   * @generated
   */
  EAttribute getDigitalSensor_Port();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getPin <em>Pin</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pin</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor#getPin()
   * @see #getDigitalSensor()
   * @generated
   */
  EAttribute getDigitalSensor_Pin();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice <em>MSub Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MSub Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice
   * @generated
   */
  EClass getMSubDevice();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getSubId <em>Sub Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sub Id</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice#getSubId()
   * @see #getMSubDevice()
   * @generated
   */
  EAttribute getMSubDevice_SubId();

  /**
   * Returns the meta object for the container reference '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick <em>Mbrick</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Mbrick</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice#getMbrick()
   * @see #getMSubDevice()
   * @generated
   */
  EReference getMSubDevice_Mbrick();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelay <em>MDual Relay</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MDual Relay</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelay
   * @generated
   */
  EClass getMDualRelay();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelay#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelay#getDeviceType()
   * @see #getMDualRelay()
   * @generated
   */
  EAttribute getMDualRelay_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration <em>TF Null Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Null Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration
   * @generated
   */
  EClass getTFNullConfiguration();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration <em>TF Servo Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Servo Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration
   * @generated
   */
  EClass getTFServoConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getVelocity <em>Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getVelocity()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_Velocity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getAcceleration <em>Acceleration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Acceleration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getAcceleration()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_Acceleration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getServoVoltage <em>Servo Voltage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Servo Voltage</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getServoVoltage()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_ServoVoltage();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMin <em>Pulse Width Min</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pulse Width Min</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMin()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_PulseWidthMin();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMax <em>Pulse Width Max</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pulse Width Max</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMax()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_PulseWidthMax();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPeriod <em>Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPeriod()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_Period();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getOutputVoltage <em>Output Voltage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Output Voltage</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getOutputVoltage()
   * @see #getTFServoConfiguration()
   * @generated
   */
  EAttribute getTFServoConfiguration_OutputVoltage();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MServo <em>MServo</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MServo</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo
   * @generated
   */
  EClass getMServo();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getDeviceType()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity <em>Velocity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Velocity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_Velocity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration <em>Acceleration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Acceleration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_Acceleration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin <em>Pulse Width Min</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pulse Width Min</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_PulseWidthMin();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax <em>Pulse Width Max</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pulse Width Max</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_PulseWidthMax();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod <em>Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_Period();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage <em>Output Voltage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Output Voltage</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_OutputVoltage();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoCurrentPosition <em>Servo Current Position</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Servo Current Position</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getServoCurrentPosition()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_ServoCurrentPosition();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoDestinationPosition <em>Servo Destination Position</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Servo Destination Position</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#getServoDestinationPosition()
   * @see #getMServo()
   * @generated
   */
  EAttribute getMServo_ServoDestinationPosition();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo#init()
   * @generated
   */
  EOperation getMServo__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener <em>Callback Listener</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Callback Listener</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.CallbackListener
   * @generated
   */
  EClass getCallbackListener();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener#getCallbackPeriod <em>Callback Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Callback Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.CallbackListener#getCallbackPeriod()
   * @see #getCallbackListener()
   * @generated
   */
  EAttribute getCallbackListener_CallbackPeriod();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.InterruptListener <em>Interrupt Listener</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Interrupt Listener</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.InterruptListener
   * @generated
   */
  EClass getInterruptListener();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.InterruptListener#getDebouncePeriod <em>Debounce Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Debounce Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.InterruptListener#getDebouncePeriod()
   * @see #getInterruptListener()
   * @generated
   */
  EAttribute getInterruptListener_DebouncePeriod();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MSensor <em>MSensor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MSensor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSensor
   * @generated
   */
  EClass getMSensor();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MSensor#getSensorValue <em>Sensor Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sensor Value</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MSensor#getSensorValue()
   * @see #getMSensor()
   * @generated
   */
  EAttribute getMSensor_SensorValue();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MSensor#fetchSensorValue() <em>Fetch Sensor Value</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Fetch Sensor Value</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MSensor#fetchSensorValue()
   * @generated
   */
  EOperation getMSensor__FetchSensorValue();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity <em>MBricklet Humidity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Humidity</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity
   * @generated
   */
  EClass getMBrickletHumidity();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getDeviceType()
   * @see #getMBrickletHumidity()
   * @generated
   */
  EAttribute getMBrickletHumidity_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getHumiditiy <em>Humiditiy</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Humiditiy</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getHumiditiy()
   * @see #getMBrickletHumidity()
   * @generated
   */
  EAttribute getMBrickletHumidity_Humiditiy();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#getThreshold()
   * @see #getMBrickletHumidity()
   * @generated
   */
  EAttribute getMBrickletHumidity_Threshold();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity#init()
   * @generated
   */
  EOperation getMBrickletHumidity__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR <em>MBricklet Distance IR</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Distance IR</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR
   * @generated
   */
  EClass getMBrickletDistanceIR();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getDeviceType()
   * @see #getMBrickletDistanceIR()
   * @generated
   */
  EAttribute getMBrickletDistanceIR_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getDistance <em>Distance</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Distance</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getDistance()
   * @see #getMBrickletDistanceIR()
   * @generated
   */
  EAttribute getMBrickletDistanceIR_Distance();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#getThreshold()
   * @see #getMBrickletDistanceIR()
   * @generated
   */
  EAttribute getMBrickletDistanceIR_Threshold();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR#init()
   * @generated
   */
  EOperation getMBrickletDistanceIR__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature <em>MBricklet Temperature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Temperature</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature
   * @generated
   */
  EClass getMBrickletTemperature();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getDeviceType()
   * @see #getMBrickletTemperature()
   * @generated
   */
  EAttribute getMBrickletTemperature_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getTemperature <em>Temperature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Temperature</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getTemperature()
   * @see #getMBrickletTemperature()
   * @generated
   */
  EAttribute getMBrickletTemperature_Temperature();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#getThreshold()
   * @see #getMBrickletTemperature()
   * @generated
   */
  EAttribute getMBrickletTemperature_Threshold();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature#init()
   * @generated
   */
  EOperation getMBrickletTemperature__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration <em>TF Base Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TF Base Configuration</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration
   * @generated
   */
  EClass getTFBaseConfiguration();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration#getThreshold()
   * @see #getTFBaseConfiguration()
   * @generated
   */
  EAttribute getTFBaseConfiguration_Threshold();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration#getCallbackPeriod <em>Callback Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Callback Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration#getCallbackPeriod()
   * @see #getTFBaseConfiguration()
   * @generated
   */
  EAttribute getTFBaseConfiguration_CallbackPeriod();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer <em>MBricklet Barometer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Barometer</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer
   * @generated
   */
  EClass getMBrickletBarometer();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getDeviceType()
   * @see #getMBrickletBarometer()
   * @generated
   */
  EAttribute getMBrickletBarometer_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getAirPressure <em>Air Pressure</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Air Pressure</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getAirPressure()
   * @see #getMBrickletBarometer()
   * @generated
   */
  EAttribute getMBrickletBarometer_AirPressure();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#getThreshold()
   * @see #getMBrickletBarometer()
   * @generated
   */
  EAttribute getMBrickletBarometer_Threshold();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer#init()
   * @generated
   */
  EOperation getMBrickletBarometer__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature <em>MBarometer Temperature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBarometer Temperature</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature
   * @generated
   */
  EClass getMBarometerTemperature();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getDeviceType()
   * @see #getMBarometerTemperature()
   * @generated
   */
  EAttribute getMBarometerTemperature_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getTemperature <em>Temperature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Temperature</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#getTemperature()
   * @see #getMBarometerTemperature()
   * @generated
   */
  EAttribute getMBarometerTemperature_Temperature();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature#init()
   * @generated
   */
  EOperation getMBarometerTemperature__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight <em>MBricklet Ambient Light</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet Ambient Light</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight
   * @generated
   */
  EClass getMBrickletAmbientLight();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getDeviceType()
   * @see #getMBrickletAmbientLight()
   * @generated
   */
  EAttribute getMBrickletAmbientLight_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getIlluminance <em>Illuminance</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Illuminance</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getIlluminance()
   * @see #getMBrickletAmbientLight()
   * @generated
   */
  EAttribute getMBrickletAmbientLight_Illuminance();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getThreshold <em>Threshold</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Threshold</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#getThreshold()
   * @see #getMBrickletAmbientLight()
   * @generated
   */
  EAttribute getMBrickletAmbientLight_Threshold();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight#init()
   * @generated
   */
  EOperation getMBrickletAmbientLight__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4 <em>MBricklet LCD2 0x4</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MBricklet LCD2 0x4</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4
   * @generated
   */
  EClass getMBrickletLCD20x4();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getDeviceType()
   * @see #getMBrickletLCD20x4()
   * @generated
   */
  EAttribute getMBrickletLCD20x4_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositionPrefix <em>Position Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Position Prefix</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositionPrefix()
   * @see #getMBrickletLCD20x4()
   * @generated
   */
  EAttribute getMBrickletLCD20x4_PositionPrefix();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositonSuffix <em>Positon Suffix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Positon Suffix</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getPositonSuffix()
   * @see #getMBrickletLCD20x4()
   * @generated
   */
  EAttribute getMBrickletLCD20x4_PositonSuffix();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#isDisplayErrors <em>Display Errors</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Display Errors</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#isDisplayErrors()
   * @see #getMBrickletLCD20x4()
   * @generated
   */
  EAttribute getMBrickletLCD20x4_DisplayErrors();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getErrorPrefix <em>Error Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Error Prefix</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#getErrorPrefix()
   * @see #getMBrickletLCD20x4()
   * @generated
   */
  EAttribute getMBrickletLCD20x4_ErrorPrefix();

  /**
   * Returns the meta object for the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#init() <em>Init</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Init</em>' operation.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4#init()
   * @generated
   */
  EOperation getMBrickletLCD20x4__Init();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MTextActor <em>MText Actor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MText Actor</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MTextActor
   * @generated
   */
  EClass getMTextActor();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MTextActor#getText <em>Text</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Text</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MTextActor#getText()
   * @see #getMTextActor()
   * @generated
   */
  EAttribute getMTextActor_Text();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice <em>MLCD Sub Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MLCD Sub Device</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice
   * @generated
   */
  EClass getMLCDSubDevice();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight <em>MLCD2 0x4 Backlight</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MLCD2 0x4 Backlight</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight
   * @generated
   */
  EClass getMLCD20x4Backlight();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight#getDeviceType()
   * @see #getMLCD20x4Backlight()
   * @generated
   */
  EAttribute getMLCD20x4Backlight_DeviceType();

  /**
   * Returns the meta object for class '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button <em>MLCD2 0x4 Button</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MLCD2 0x4 Button</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button
   * @generated
   */
  EClass getMLCD20x4Button();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getDeviceType <em>Device Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Device Type</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getDeviceType()
   * @see #getMLCD20x4Button()
   * @generated
   */
  EAttribute getMLCD20x4Button_DeviceType();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum <em>Button Num</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Button Num</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getButtonNum()
   * @see #getMLCD20x4Button()
   * @generated
   */
  EAttribute getMLCD20x4Button_ButtonNum();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getCallbackPeriod <em>Callback Period</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Callback Period</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button#getCallbackPeriod()
   * @see #getMLCD20x4Button()
   * @generated
   */
  EAttribute getMLCD20x4Button_CallbackPeriod();

  /**
   * Returns the meta object for data type '{@link org.openhab.binding.tinkerforge.internal.types.OnOffValue <em>Switch State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Switch State</em>'.
   * @see org.openhab.binding.tinkerforge.internal.types.OnOffValue
   * @model instanceClass="org.openhab.binding.tinkerforge.internal.types.OnOffValue"
   * @generated
   */
  EDataType getSwitchState();

  /**
   * Returns the meta object for data type '{@link org.openhab.binding.tinkerforge.internal.types.HighLowValue <em>Digital Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Digital Value</em>'.
   * @see org.openhab.binding.tinkerforge.internal.types.HighLowValue
   * @model instanceClass="org.openhab.binding.tinkerforge.internal.types.HighLowValue"
   * @generated
   */
  EDataType getDigitalValue();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletIO16 <em>Tinker Bricklet IO16</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Tinker Bricklet IO16</em>'.
   * @see com.tinkerforge.BrickletIO16
   * @model instanceClass="com.tinkerforge.BrickletIO16"
   * @generated
   */
  EDataType getTinkerBrickletIO16();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.DCDriveMode <em>DC Drive Mode</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>DC Drive Mode</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DCDriveMode
   * @generated
   */
  EEnum getDCDriveMode();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.NoSubIds <em>No Sub Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>No Sub Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.NoSubIds
   * @generated
   */
  EEnum getNoSubIds();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs <em>Industrial Digital In Sub IDs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Industrial Digital In Sub IDs</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs
   * @generated
   */
  EEnum getIndustrialDigitalInSubIDs();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs <em>Industrial Quad Relay IDs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Industrial Quad Relay IDs</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs
   * @generated
   */
  EEnum getIndustrialQuadRelayIDs();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.ServoSubIDs <em>Servo Sub IDs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Servo Sub IDs</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.ServoSubIDs
   * @generated
   */
  EEnum getServoSubIDs();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs <em>Barometer Sub IDs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Barometer Sub IDs</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs
   * @generated
   */
  EEnum getBarometerSubIDs();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.IO16SubIds <em>IO16 Sub Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>IO16 Sub Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.IO16SubIds
   * @generated
   */
  EEnum getIO16SubIds();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds <em>Dual Relay Sub Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Dual Relay Sub Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds
   * @generated
   */
  EEnum getDualRelaySubIds();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds <em>LCD Button Sub Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>LCD Button Sub Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds
   * @generated
   */
  EEnum getLCDButtonSubIds();

  /**
   * Returns the meta object for enum '{@link org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds <em>LCD Backlight Sub Ids</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>LCD Backlight Sub Ids</em>'.
   * @see org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds
   * @generated
   */
  EEnum getLCDBacklightSubIds();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.IPConnection <em>MIP Connection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MIP Connection</em>'.
   * @see com.tinkerforge.IPConnection
   * @model instanceClass="com.tinkerforge.IPConnection"
   * @generated
   */
  EDataType getMIPConnection();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.Device <em>MTinker Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Device</em>'.
   * @see com.tinkerforge.Device
   * @model instanceClass="com.tinkerforge.Device"
   * @generated
   */
  EDataType getMTinkerDevice();

  /**
   * Returns the meta object for data type '{@link org.slf4j.Logger <em>MLogger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MLogger</em>'.
   * @see org.slf4j.Logger
   * @model instanceClass="org.slf4j.Logger"
   * @generated
   */
  EDataType getMLogger();

  /**
   * Returns the meta object for data type '{@link java.util.concurrent.atomic.AtomicBoolean <em>MAtomic Boolean</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MAtomic Boolean</em>'.
   * @see java.util.concurrent.atomic.AtomicBoolean
   * @model instanceClass="java.util.concurrent.atomic.AtomicBoolean"
   * @generated
   */
  EDataType getMAtomicBoolean();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.Device <em>MTinkerforge Device</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinkerforge Device</em>'.
   * @see com.tinkerforge.Device
   * @model instanceClass="com.tinkerforge.Device"
   * @generated
   */
  EDataType getMTinkerforgeDevice();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickDC <em>MTinker Brick DC</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Brick DC</em>'.
   * @see com.tinkerforge.BrickDC
   * @model instanceClass="com.tinkerforge.BrickDC"
   * @generated
   */
  EDataType getMTinkerBrickDC();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickServo <em>MTinker Brick Servo</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Brick Servo</em>'.
   * @see com.tinkerforge.BrickServo
   * @model instanceClass="com.tinkerforge.BrickServo"
   * @generated
   */
  EDataType getMTinkerBrickServo();

  /**
   * Returns the meta object for data type '{@link org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue <em>MTinkerforge Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinkerforge Value</em>'.
   * @see org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue
   * @model instanceClass="org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue"
   * @generated
   */
  EDataType getMTinkerforgeValue();

  /**
   * Returns the meta object for data type '{@link org.openhab.binding.tinkerforge.internal.types.DecimalValue <em>MDecimal Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MDecimal Value</em>'.
   * @see org.openhab.binding.tinkerforge.internal.types.DecimalValue
   * @model instanceClass="org.openhab.binding.tinkerforge.internal.types.DecimalValue"
   * @generated
   */
  EDataType getMDecimalValue();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletHumidity <em>MTinker Bricklet Humidity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Humidity</em>'.
   * @see com.tinkerforge.BrickletHumidity
   * @model instanceClass="com.tinkerforge.BrickletHumidity"
   * @generated
   */
  EDataType getMTinkerBrickletHumidity();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletDistanceIR <em>MTinker Bricklet Distance IR</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Distance IR</em>'.
   * @see com.tinkerforge.BrickletDistanceIR
   * @model instanceClass="com.tinkerforge.BrickletDistanceIR"
   * @generated
   */
  EDataType getMTinkerBrickletDistanceIR();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletTemperature <em>MTinker Bricklet Temperature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Temperature</em>'.
   * @see com.tinkerforge.BrickletTemperature
   * @model instanceClass="com.tinkerforge.BrickletTemperature"
   * @generated
   */
  EDataType getMTinkerBrickletTemperature();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletBarometer <em>MTinker Bricklet Barometer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Barometer</em>'.
   * @see com.tinkerforge.BrickletBarometer
   * @model instanceClass="com.tinkerforge.BrickletBarometer"
   * @generated
   */
  EDataType getMTinkerBrickletBarometer();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletAmbientLight <em>MTinker Bricklet Ambient Light</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Ambient Light</em>'.
   * @see com.tinkerforge.BrickletAmbientLight
   * @model instanceClass="com.tinkerforge.BrickletAmbientLight"
   * @generated
   */
  EDataType getMTinkerBrickletAmbientLight();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletLCD20x4 <em>MTinker Bricklet LCD2 0x4</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet LCD2 0x4</em>'.
   * @see com.tinkerforge.BrickletLCD20x4
   * @model instanceClass="com.tinkerforge.BrickletLCD20x4"
   * @generated
   */
  EDataType getMTinkerBrickletLCD20x4();

  /**
   * Returns the meta object for data type '{@link java.lang.Enum <em>Enum</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Enum</em>'.
   * @see java.lang.Enum
   * @model instanceClass="java.lang.Enum"
   * @generated
   */
  EDataType getEnum();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletDualRelay <em>MTinker Bricklet Dual Relay</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Dual Relay</em>'.
   * @see com.tinkerforge.BrickletDualRelay
   * @model instanceClass="com.tinkerforge.BrickletDualRelay"
   * @generated
   */
  EDataType getMTinkerBrickletDualRelay();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletIndustrialQuadRelay <em>MTinker Bricklet Industrial Quad Relay</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Industrial Quad Relay</em>'.
   * @see com.tinkerforge.BrickletIndustrialQuadRelay
   * @model instanceClass="com.tinkerforge.BrickletIndustrialQuadRelay"
   * @generated
   */
  EDataType getMTinkerBrickletIndustrialQuadRelay();

  /**
   * Returns the meta object for data type '{@link com.tinkerforge.BrickletIndustrialDigitalIn4 <em>MTinker Bricklet Industrial Digital In4</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>MTinker Bricklet Industrial Digital In4</em>'.
   * @see com.tinkerforge.BrickletIndustrialDigitalIn4
   * @model instanceClass="com.tinkerforge.BrickletIndustrialDigitalIn4"
   * @generated
   */
  EDataType getMTinkerBrickletIndustrialDigitalIn4();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ModelFactory getModelFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.TFConfig <em>TF Config</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.TFConfig
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFConfig()
     * @generated
     */
    EClass TF_CONFIG = eINSTANCE.getTFConfig();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl <em>OHTF Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getOHTFDevice()
     * @generated
     */
    EClass OHTF_DEVICE = eINSTANCE.getOHTFDevice();

    /**
     * The meta object literal for the '<em><b>Uid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OHTF_DEVICE__UID = eINSTANCE.getOHTFDevice_Uid();

    /**
     * The meta object literal for the '<em><b>Subid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OHTF_DEVICE__SUBID = eINSTANCE.getOHTFDevice_Subid();

    /**
     * The meta object literal for the '<em><b>Ohid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OHTF_DEVICE__OHID = eINSTANCE.getOHTFDevice_Ohid();

    /**
     * The meta object literal for the '<em><b>Sub Device Ids</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OHTF_DEVICE__SUB_DEVICE_IDS = eINSTANCE.getOHTFDevice_SubDeviceIds();

    /**
     * The meta object literal for the '<em><b>Tf Config</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OHTF_DEVICE__TF_CONFIG = eINSTANCE.getOHTFDevice_TfConfig();

    /**
     * The meta object literal for the '<em><b>Oh Config</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OHTF_DEVICE__OH_CONFIG = eINSTANCE.getOHTFDevice_OhConfig();

    /**
     * The meta object literal for the '<em><b>Is Valid Sub Id</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation OHTF_DEVICE___IS_VALID_SUB_ID__STRING = eINSTANCE.getOHTFDevice__IsValidSubId__String();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.OHConfigImpl <em>OH Config</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.OHConfigImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getOHConfig()
     * @generated
     */
    EClass OH_CONFIG = eINSTANCE.getOHConfig();

    /**
     * The meta object literal for the '<em><b>Oh Tf Devices</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OH_CONFIG__OH_TF_DEVICES = eINSTANCE.getOHConfig_OhTfDevices();

    /**
     * The meta object literal for the '<em><b>Get Config By TF Id</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation OH_CONFIG___GET_CONFIG_BY_TF_ID__STRING_STRING = eINSTANCE.getOHConfig__GetConfigByTFId__String_String();

    /**
     * The meta object literal for the '<em><b>Get Config By OH Id</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation OH_CONFIG___GET_CONFIG_BY_OH_ID__STRING = eINSTANCE.getOHConfig__GetConfigByOHId__String();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl <em>Ecosystem</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.EcosystemImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getEcosystem()
     * @generated
     */
    EClass ECOSYSTEM = eINSTANCE.getEcosystem();

    /**
     * The meta object literal for the '<em><b>Logger</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECOSYSTEM__LOGGER = eINSTANCE.getEcosystem_Logger();

    /**
     * The meta object literal for the '<em><b>Mbrickds</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ECOSYSTEM__MBRICKDS = eINSTANCE.getEcosystem_Mbrickds();

    /**
     * The meta object literal for the '<em><b>Get Brickd</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation ECOSYSTEM___GET_BRICKD__STRING_INT = eINSTANCE.getEcosystem__GetBrickd__String_int();

    /**
     * The meta object literal for the '<em><b>Get Device</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation ECOSYSTEM___GET_DEVICE__STRING_STRING = eINSTANCE.getEcosystem__GetDevice__String_String();

    /**
     * The meta object literal for the '<em><b>Get Devices4 Generic Id</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation ECOSYSTEM___GET_DEVICES4_GENERIC_ID__STRING_STRING = eINSTANCE.getEcosystem__GetDevices4GenericId__String_String();

    /**
     * The meta object literal for the '<em><b>Disconnect</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation ECOSYSTEM___DISCONNECT = eINSTANCE.getEcosystem__Disconnect();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl <em>MBrickd</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickd()
     * @generated
     */
    EClass MBRICKD = eINSTANCE.getMBrickd();

    /**
     * The meta object literal for the '<em><b>Logger</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__LOGGER = eINSTANCE.getMBrickd_Logger();

    /**
     * The meta object literal for the '<em><b>Ip Connection</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__IP_CONNECTION = eINSTANCE.getMBrickd_IpConnection();

    /**
     * The meta object literal for the '<em><b>Host</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__HOST = eINSTANCE.getMBrickd_Host();

    /**
     * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__PORT = eINSTANCE.getMBrickd_Port();

    /**
     * The meta object literal for the '<em><b>Is Connected</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__IS_CONNECTED = eINSTANCE.getMBrickd_IsConnected();

    /**
     * The meta object literal for the '<em><b>Auto Reconnect</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__AUTO_RECONNECT = eINSTANCE.getMBrickd_AutoReconnect();

    /**
     * The meta object literal for the '<em><b>Timeout</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKD__TIMEOUT = eINSTANCE.getMBrickd_Timeout();

    /**
     * The meta object literal for the '<em><b>Mdevices</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MBRICKD__MDEVICES = eINSTANCE.getMBrickd_Mdevices();

    /**
     * The meta object literal for the '<em><b>Ecosystem</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MBRICKD__ECOSYSTEM = eINSTANCE.getMBrickd_Ecosystem();

    /**
     * The meta object literal for the '<em><b>Connect</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKD___CONNECT = eINSTANCE.getMBrickd__Connect();

    /**
     * The meta object literal for the '<em><b>Disconnect</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKD___DISCONNECT = eINSTANCE.getMBrickd__Disconnect();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKD___INIT = eINSTANCE.getMBrickd__Init();

    /**
     * The meta object literal for the '<em><b>Get Device</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKD___GET_DEVICE__STRING = eINSTANCE.getMBrickd__GetDevice__String();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer <em>MTF Config Consumer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTFConfigConsumer()
     * @generated
     */
    EClass MTF_CONFIG_CONSUMER = eINSTANCE.getMTFConfigConsumer();

    /**
     * The meta object literal for the '<em><b>Tf Config</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MTF_CONFIG_CONSUMER__TF_CONFIG = eINSTANCE.getMTFConfigConsumer_TfConfig();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice <em>MBase Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBaseDevice()
     * @generated
     */
    EClass MBASE_DEVICE = eINSTANCE.getMBaseDevice();

    /**
     * The meta object literal for the '<em><b>Logger</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBASE_DEVICE__LOGGER = eINSTANCE.getMBaseDevice_Logger();

    /**
     * The meta object literal for the '<em><b>Uid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBASE_DEVICE__UID = eINSTANCE.getMBaseDevice_Uid();

    /**
     * The meta object literal for the '<em><b>Enabled A</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBASE_DEVICE__ENABLED_A = eINSTANCE.getMBaseDevice_EnabledA();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBASE_DEVICE___INIT = eINSTANCE.getMBaseDevice__Init();

    /**
     * The meta object literal for the '<em><b>Enable</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBASE_DEVICE___ENABLE = eINSTANCE.getMBaseDevice__Enable();

    /**
     * The meta object literal for the '<em><b>Disable</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBASE_DEVICE___DISABLE = eINSTANCE.getMBaseDevice__Disable();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MDevice <em>MDevice</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MDevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDevice()
     * @generated
     */
    EClass MDEVICE = eINSTANCE.getMDevice();

    /**
     * The meta object literal for the '<em><b>Tinkerforge Device</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__TINKERFORGE_DEVICE = eINSTANCE.getMDevice_TinkerforgeDevice();

    /**
     * The meta object literal for the '<em><b>Ip Connection</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__IP_CONNECTION = eINSTANCE.getMDevice_IpConnection();

    /**
     * The meta object literal for the '<em><b>Connected Uid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__CONNECTED_UID = eINSTANCE.getMDevice_ConnectedUid();

    /**
     * The meta object literal for the '<em><b>Position</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__POSITION = eINSTANCE.getMDevice_Position();

    /**
     * The meta object literal for the '<em><b>Device Identifier</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__DEVICE_IDENTIFIER = eINSTANCE.getMDevice_DeviceIdentifier();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEVICE__NAME = eINSTANCE.getMDevice_Name();

    /**
     * The meta object literal for the '<em><b>Brickd</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MDEVICE__BRICKD = eINSTANCE.getMDevice_Brickd();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder <em>MSub Device Holder</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSubDeviceHolder()
     * @generated
     */
    EClass MSUB_DEVICE_HOLDER = eINSTANCE.getMSubDeviceHolder();

    /**
     * The meta object literal for the '<em><b>Msubdevices</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MSUB_DEVICE_HOLDER__MSUBDEVICES = eINSTANCE.getMSubDeviceHolder_Msubdevices();

    /**
     * The meta object literal for the '<em><b>Init Sub Devices</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES = eINSTANCE.getMSubDeviceHolder__InitSubDevices();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickServoImpl <em>MBrick Servo</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickServoImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickServo()
     * @generated
     */
    EClass MBRICK_SERVO = eINSTANCE.getMBrickServo();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_SERVO__DEVICE_TYPE = eINSTANCE.getMBrickServo_DeviceType();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICK_SERVO___INIT = eINSTANCE.getMBrickServo__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl <em>TF Brick DC Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFBrickDCConfiguration()
     * @generated
     */
    EClass TF_BRICK_DC_CONFIGURATION = eINSTANCE.getTFBrickDCConfiguration();

    /**
     * The meta object literal for the '<em><b>Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BRICK_DC_CONFIGURATION__VELOCITY = eINSTANCE.getTFBrickDCConfiguration_Velocity();

    /**
     * The meta object literal for the '<em><b>Acceleration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BRICK_DC_CONFIGURATION__ACCELERATION = eINSTANCE.getTFBrickDCConfiguration_Acceleration();

    /**
     * The meta object literal for the '<em><b>Pwm Frequency</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY = eINSTANCE.getTFBrickDCConfiguration_PwmFrequency();

    /**
     * The meta object literal for the '<em><b>Drive Mode</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BRICK_DC_CONFIGURATION__DRIVE_MODE = eINSTANCE.getTFBrickDCConfiguration_DriveMode();

    /**
     * The meta object literal for the '<em><b>Switch On Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY = eINSTANCE.getTFBrickDCConfiguration_SwitchOnVelocity();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl <em>MBrick DC</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickDCImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickDC()
     * @generated
     */
    EClass MBRICK_DC = eINSTANCE.getMBrickDC();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__DEVICE_TYPE = eINSTANCE.getMBrickDC_DeviceType();

    /**
     * The meta object literal for the '<em><b>Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__VELOCITY = eINSTANCE.getMBrickDC_Velocity();

    /**
     * The meta object literal for the '<em><b>Current Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__CURRENT_VELOCITY = eINSTANCE.getMBrickDC_CurrentVelocity();

    /**
     * The meta object literal for the '<em><b>Acceleration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__ACCELERATION = eINSTANCE.getMBrickDC_Acceleration();

    /**
     * The meta object literal for the '<em><b>Pwm Frequency</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__PWM_FREQUENCY = eINSTANCE.getMBrickDC_PwmFrequency();

    /**
     * The meta object literal for the '<em><b>Drive Mode</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__DRIVE_MODE = eINSTANCE.getMBrickDC_DriveMode();

    /**
     * The meta object literal for the '<em><b>Switch On Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICK_DC__SWITCH_ON_VELOCITY = eINSTANCE.getMBrickDC_SwitchOnVelocity();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICK_DC___INIT = eINSTANCE.getMBrickDC__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayBrickletImpl <em>MDual Relay Bricklet</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayBrickletImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDualRelayBricklet()
     * @generated
     */
    EClass MDUAL_RELAY_BRICKLET = eINSTANCE.getMDualRelayBricklet();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDUAL_RELAY_BRICKLET__DEVICE_TYPE = eINSTANCE.getMDualRelayBricklet_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayBrickletImpl <em>MIndustrial Quad Relay Bricklet</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayBrickletImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialQuadRelayBricklet()
     * @generated
     */
    EClass MINDUSTRIAL_QUAD_RELAY_BRICKLET = eINSTANCE.getMIndustrialQuadRelayBricklet();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl <em>MIndustrial Quad Relay</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialQuadRelay()
     * @generated
     */
    EClass MINDUSTRIAL_QUAD_RELAY = eINSTANCE.getMIndustrialQuadRelay();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MINDUSTRIAL_QUAD_RELAY__DEVICE_TYPE = eINSTANCE.getMIndustrialQuadRelay_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIndustrialDigitalIn4Impl <em>MBricklet Industrial Digital In4</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIndustrialDigitalIn4Impl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletIndustrialDigitalIn4()
     * @generated
     */
    EClass MBRICKLET_INDUSTRIAL_DIGITAL_IN4 = eINSTANCE.getMBrickletIndustrialDigitalIn4();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_INDUSTRIAL_DIGITAL_IN4__DEVICE_TYPE = eINSTANCE.getMBrickletIndustrialDigitalIn4_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl <em>MIndustrial Digital In</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIndustrialDigitalIn()
     * @generated
     */
    EClass MINDUSTRIAL_DIGITAL_IN = eINSTANCE.getMIndustrialDigitalIn();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MActor <em>MActor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MActor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMActor()
     * @generated
     */
    EClass MACTOR = eINSTANCE.getMActor();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor <em>MSwitch Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSwitchActor()
     * @generated
     */
    EClass MSWITCH_ACTOR = eINSTANCE.getMSwitchActor();

    /**
     * The meta object literal for the '<em><b>Switch State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSWITCH_ACTOR__SWITCH_STATE = eINSTANCE.getMSwitchActor_SwitchState();

    /**
     * The meta object literal for the '<em><b>Turn Switch</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE = eINSTANCE.getMSwitchActor__TurnSwitch__OnOffValue();

    /**
     * The meta object literal for the '<em><b>Fetch Switch State</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MSWITCH_ACTOR___FETCH_SWITCH_STATE = eINSTANCE.getMSwitchActor__FetchSwitchState();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor <em>MOut Switch Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMOutSwitchActor()
     * @generated
     */
    EClass MOUT_SWITCH_ACTOR = eINSTANCE.getMOutSwitchActor();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MInSwitchActor <em>MIn Switch Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MInSwitchActor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMInSwitchActor()
     * @generated
     */
    EClass MIN_SWITCH_ACTOR = eINSTANCE.getMInSwitchActor();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice <em>Generic Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.GenericDevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getGenericDevice()
     * @generated
     */
    EClass GENERIC_DEVICE = eINSTANCE.getGenericDevice();

    /**
     * The meta object literal for the '<em><b>Generic Device Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GENERIC_DEVICE__GENERIC_DEVICE_ID = eINSTANCE.getGenericDevice_GenericDeviceId();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl <em>TFIO Actor Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFIOActorConfiguration()
     * @generated
     */
    EClass TFIO_ACTOR_CONFIGURATION = eINSTANCE.getTFIOActorConfiguration();

    /**
     * The meta object literal for the '<em><b>Default State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE = eINSTANCE.getTFIOActorConfiguration_DefaultState();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalActorImpl <em>Digital Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.DigitalActorImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalActor()
     * @generated
     */
    EClass DIGITAL_ACTOR = eINSTANCE.getDigitalActor();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_ACTOR__DEVICE_TYPE = eINSTANCE.getDigitalActor_DeviceType();

    /**
     * The meta object literal for the '<em><b>Digital State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_ACTOR__DIGITAL_STATE = eINSTANCE.getDigitalActor_DigitalState();

    /**
     * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_ACTOR__PORT = eINSTANCE.getDigitalActor_Port();

    /**
     * The meta object literal for the '<em><b>Pin</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_ACTOR__PIN = eINSTANCE.getDigitalActor_Pin();

    /**
     * The meta object literal for the '<em><b>Default State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_ACTOR__DEFAULT_STATE = eINSTANCE.getDigitalActor_DefaultState();

    /**
     * The meta object literal for the '<em><b>Turn Digital</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation DIGITAL_ACTOR___TURN_DIGITAL__HIGHLOWVALUE = eINSTANCE.getDigitalActor__TurnDigital__HighLowValue();

    /**
     * The meta object literal for the '<em><b>Fetch Digital Value</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation DIGITAL_ACTOR___FETCH_DIGITAL_VALUE = eINSTANCE.getDigitalActor__FetchDigitalValue();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFInterruptListenerConfigurationImpl <em>TF Interrupt Listener Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFInterruptListenerConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFInterruptListenerConfiguration()
     * @generated
     */
    EClass TF_INTERRUPT_LISTENER_CONFIGURATION = eINSTANCE.getTFInterruptListenerConfiguration();

    /**
     * The meta object literal for the '<em><b>Debounce Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD = eINSTANCE.getTFInterruptListenerConfiguration_DebouncePeriod();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIO16Impl <em>MBricklet IO16</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletIO16Impl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletIO16()
     * @generated
     */
    EClass MBRICKLET_IO16 = eINSTANCE.getMBrickletIO16();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_IO16__DEVICE_TYPE = eINSTANCE.getMBrickletIO16_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.IODevice <em>IO Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.IODevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIODevice()
     * @generated
     */
    EClass IO_DEVICE = eINSTANCE.getIODevice();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOSensorConfigurationImpl <em>TFIO Sensor Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFIOSensorConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFIOSensorConfiguration()
     * @generated
     */
    EClass TFIO_SENSOR_CONFIGURATION = eINSTANCE.getTFIOSensorConfiguration();

    /**
     * The meta object literal for the '<em><b>Pull Up Resistor Enabled</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED = eINSTANCE.getTFIOSensorConfiguration_PullUpResistorEnabled();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl <em>Digital Sensor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalSensor()
     * @generated
     */
    EClass DIGITAL_SENSOR = eINSTANCE.getDigitalSensor();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_SENSOR__DEVICE_TYPE = eINSTANCE.getDigitalSensor_DeviceType();

    /**
     * The meta object literal for the '<em><b>Pull Up Resistor Enabled</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED = eINSTANCE.getDigitalSensor_PullUpResistorEnabled();

    /**
     * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_SENSOR__PORT = eINSTANCE.getDigitalSensor_Port();

    /**
     * The meta object literal for the '<em><b>Pin</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DIGITAL_SENSOR__PIN = eINSTANCE.getDigitalSensor_Pin();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice <em>MSub Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSubDevice()
     * @generated
     */
    EClass MSUB_DEVICE = eINSTANCE.getMSubDevice();

    /**
     * The meta object literal for the '<em><b>Sub Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSUB_DEVICE__SUB_ID = eINSTANCE.getMSubDevice_SubId();

    /**
     * The meta object literal for the '<em><b>Mbrick</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MSUB_DEVICE__MBRICK = eINSTANCE.getMSubDevice_Mbrick();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayImpl <em>MDual Relay</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MDualRelayImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDualRelay()
     * @generated
     */
    EClass MDUAL_RELAY = eINSTANCE.getMDualRelay();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDUAL_RELAY__DEVICE_TYPE = eINSTANCE.getMDualRelay_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFNullConfigurationImpl <em>TF Null Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFNullConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFNullConfiguration()
     * @generated
     */
    EClass TF_NULL_CONFIGURATION = eINSTANCE.getTFNullConfiguration();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl <em>TF Servo Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFServoConfiguration()
     * @generated
     */
    EClass TF_SERVO_CONFIGURATION = eINSTANCE.getTFServoConfiguration();

    /**
     * The meta object literal for the '<em><b>Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__VELOCITY = eINSTANCE.getTFServoConfiguration_Velocity();

    /**
     * The meta object literal for the '<em><b>Acceleration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__ACCELERATION = eINSTANCE.getTFServoConfiguration_Acceleration();

    /**
     * The meta object literal for the '<em><b>Servo Voltage</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__SERVO_VOLTAGE = eINSTANCE.getTFServoConfiguration_ServoVoltage();

    /**
     * The meta object literal for the '<em><b>Pulse Width Min</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN = eINSTANCE.getTFServoConfiguration_PulseWidthMin();

    /**
     * The meta object literal for the '<em><b>Pulse Width Max</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX = eINSTANCE.getTFServoConfiguration_PulseWidthMax();

    /**
     * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__PERIOD = eINSTANCE.getTFServoConfiguration_Period();

    /**
     * The meta object literal for the '<em><b>Output Voltage</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE = eINSTANCE.getTFServoConfiguration_OutputVoltage();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl <em>MServo</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MServoImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMServo()
     * @generated
     */
    EClass MSERVO = eINSTANCE.getMServo();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__DEVICE_TYPE = eINSTANCE.getMServo_DeviceType();

    /**
     * The meta object literal for the '<em><b>Velocity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__VELOCITY = eINSTANCE.getMServo_Velocity();

    /**
     * The meta object literal for the '<em><b>Acceleration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__ACCELERATION = eINSTANCE.getMServo_Acceleration();

    /**
     * The meta object literal for the '<em><b>Pulse Width Min</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__PULSE_WIDTH_MIN = eINSTANCE.getMServo_PulseWidthMin();

    /**
     * The meta object literal for the '<em><b>Pulse Width Max</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__PULSE_WIDTH_MAX = eINSTANCE.getMServo_PulseWidthMax();

    /**
     * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__PERIOD = eINSTANCE.getMServo_Period();

    /**
     * The meta object literal for the '<em><b>Output Voltage</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__OUTPUT_VOLTAGE = eINSTANCE.getMServo_OutputVoltage();

    /**
     * The meta object literal for the '<em><b>Servo Current Position</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__SERVO_CURRENT_POSITION = eINSTANCE.getMServo_ServoCurrentPosition();

    /**
     * The meta object literal for the '<em><b>Servo Destination Position</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSERVO__SERVO_DESTINATION_POSITION = eINSTANCE.getMServo_ServoDestinationPosition();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MSERVO___INIT = eINSTANCE.getMServo__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener <em>Callback Listener</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.CallbackListener
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getCallbackListener()
     * @generated
     */
    EClass CALLBACK_LISTENER = eINSTANCE.getCallbackListener();

    /**
     * The meta object literal for the '<em><b>Callback Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CALLBACK_LISTENER__CALLBACK_PERIOD = eINSTANCE.getCallbackListener_CallbackPeriod();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.InterruptListener <em>Interrupt Listener</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.InterruptListener
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getInterruptListener()
     * @generated
     */
    EClass INTERRUPT_LISTENER = eINSTANCE.getInterruptListener();

    /**
     * The meta object literal for the '<em><b>Debounce Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INTERRUPT_LISTENER__DEBOUNCE_PERIOD = eINSTANCE.getInterruptListener_DebouncePeriod();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MSensor <em>MSensor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MSensor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMSensor()
     * @generated
     */
    EClass MSENSOR = eINSTANCE.getMSensor();

    /**
     * The meta object literal for the '<em><b>Sensor Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MSENSOR__SENSOR_VALUE = eINSTANCE.getMSensor_SensorValue();

    /**
     * The meta object literal for the '<em><b>Fetch Sensor Value</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MSENSOR___FETCH_SENSOR_VALUE = eINSTANCE.getMSensor__FetchSensorValue();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletHumidityImpl <em>MBricklet Humidity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletHumidityImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletHumidity()
     * @generated
     */
    EClass MBRICKLET_HUMIDITY = eINSTANCE.getMBrickletHumidity();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_HUMIDITY__DEVICE_TYPE = eINSTANCE.getMBrickletHumidity_DeviceType();

    /**
     * The meta object literal for the '<em><b>Humiditiy</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_HUMIDITY__HUMIDITIY = eINSTANCE.getMBrickletHumidity_Humiditiy();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_HUMIDITY__THRESHOLD = eINSTANCE.getMBrickletHumidity_Threshold();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_HUMIDITY___INIT = eINSTANCE.getMBrickletHumidity__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletDistanceIRImpl <em>MBricklet Distance IR</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletDistanceIRImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletDistanceIR()
     * @generated
     */
    EClass MBRICKLET_DISTANCE_IR = eINSTANCE.getMBrickletDistanceIR();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_DISTANCE_IR__DEVICE_TYPE = eINSTANCE.getMBrickletDistanceIR_DeviceType();

    /**
     * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_DISTANCE_IR__DISTANCE = eINSTANCE.getMBrickletDistanceIR_Distance();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_DISTANCE_IR__THRESHOLD = eINSTANCE.getMBrickletDistanceIR_Threshold();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_DISTANCE_IR___INIT = eINSTANCE.getMBrickletDistanceIR__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletTemperatureImpl <em>MBricklet Temperature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletTemperatureImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletTemperature()
     * @generated
     */
    EClass MBRICKLET_TEMPERATURE = eINSTANCE.getMBrickletTemperature();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_TEMPERATURE__DEVICE_TYPE = eINSTANCE.getMBrickletTemperature_DeviceType();

    /**
     * The meta object literal for the '<em><b>Temperature</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_TEMPERATURE__TEMPERATURE = eINSTANCE.getMBrickletTemperature_Temperature();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_TEMPERATURE__THRESHOLD = eINSTANCE.getMBrickletTemperature_Threshold();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_TEMPERATURE___INIT = eINSTANCE.getMBrickletTemperature__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBaseConfigurationImpl <em>TF Base Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.TFBaseConfigurationImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTFBaseConfiguration()
     * @generated
     */
    EClass TF_BASE_CONFIGURATION = eINSTANCE.getTFBaseConfiguration();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BASE_CONFIGURATION__THRESHOLD = eINSTANCE.getTFBaseConfiguration_Threshold();

    /**
     * The meta object literal for the '<em><b>Callback Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TF_BASE_CONFIGURATION__CALLBACK_PERIOD = eINSTANCE.getTFBaseConfiguration_CallbackPeriod();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletBarometerImpl <em>MBricklet Barometer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletBarometerImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletBarometer()
     * @generated
     */
    EClass MBRICKLET_BAROMETER = eINSTANCE.getMBrickletBarometer();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_BAROMETER__DEVICE_TYPE = eINSTANCE.getMBrickletBarometer_DeviceType();

    /**
     * The meta object literal for the '<em><b>Air Pressure</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_BAROMETER__AIR_PRESSURE = eINSTANCE.getMBrickletBarometer_AirPressure();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_BAROMETER__THRESHOLD = eINSTANCE.getMBrickletBarometer_Threshold();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_BAROMETER___INIT = eINSTANCE.getMBrickletBarometer__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBarometerTemperatureImpl <em>MBarometer Temperature</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBarometerTemperatureImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBarometerTemperature()
     * @generated
     */
    EClass MBAROMETER_TEMPERATURE = eINSTANCE.getMBarometerTemperature();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBAROMETER_TEMPERATURE__DEVICE_TYPE = eINSTANCE.getMBarometerTemperature_DeviceType();

    /**
     * The meta object literal for the '<em><b>Temperature</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBAROMETER_TEMPERATURE__TEMPERATURE = eINSTANCE.getMBarometerTemperature_Temperature();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBAROMETER_TEMPERATURE___INIT = eINSTANCE.getMBarometerTemperature__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightImpl <em>MBricklet Ambient Light</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletAmbientLight()
     * @generated
     */
    EClass MBRICKLET_AMBIENT_LIGHT = eINSTANCE.getMBrickletAmbientLight();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_AMBIENT_LIGHT__DEVICE_TYPE = eINSTANCE.getMBrickletAmbientLight_DeviceType();

    /**
     * The meta object literal for the '<em><b>Illuminance</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_AMBIENT_LIGHT__ILLUMINANCE = eINSTANCE.getMBrickletAmbientLight_Illuminance();

    /**
     * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_AMBIENT_LIGHT__THRESHOLD = eINSTANCE.getMBrickletAmbientLight_Threshold();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_AMBIENT_LIGHT___INIT = eINSTANCE.getMBrickletAmbientLight__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl <em>MBricklet LCD2 0x4</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMBrickletLCD20x4()
     * @generated
     */
    EClass MBRICKLET_LCD2_0X4 = eINSTANCE.getMBrickletLCD20x4();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_LCD2_0X4__DEVICE_TYPE = eINSTANCE.getMBrickletLCD20x4_DeviceType();

    /**
     * The meta object literal for the '<em><b>Position Prefix</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_LCD2_0X4__POSITION_PREFIX = eINSTANCE.getMBrickletLCD20x4_PositionPrefix();

    /**
     * The meta object literal for the '<em><b>Positon Suffix</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_LCD2_0X4__POSITON_SUFFIX = eINSTANCE.getMBrickletLCD20x4_PositonSuffix();

    /**
     * The meta object literal for the '<em><b>Display Errors</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_LCD2_0X4__DISPLAY_ERRORS = eINSTANCE.getMBrickletLCD20x4_DisplayErrors();

    /**
     * The meta object literal for the '<em><b>Error Prefix</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MBRICKLET_LCD2_0X4__ERROR_PREFIX = eINSTANCE.getMBrickletLCD20x4_ErrorPrefix();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MBRICKLET_LCD2_0X4___INIT = eINSTANCE.getMBrickletLCD20x4__Init();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MTextActor <em>MText Actor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MTextActor
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTextActor()
     * @generated
     */
    EClass MTEXT_ACTOR = eINSTANCE.getMTextActor();

    /**
     * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MTEXT_ACTOR__TEXT = eINSTANCE.getMTextActor_Text();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice <em>MLCD Sub Device</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCDSubDevice()
     * @generated
     */
    EClass MLCD_SUB_DEVICE = eINSTANCE.getMLCDSubDevice();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4BacklightImpl <em>MLCD2 0x4 Backlight</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4BacklightImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCD20x4Backlight()
     * @generated
     */
    EClass MLCD2_0X4_BACKLIGHT = eINSTANCE.getMLCD20x4Backlight();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MLCD2_0X4_BACKLIGHT__DEVICE_TYPE = eINSTANCE.getMLCD20x4Backlight_DeviceType();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl <em>MLCD2 0x4 Button</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.impl.MLCD20x4ButtonImpl
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLCD20x4Button()
     * @generated
     */
    EClass MLCD2_0X4_BUTTON = eINSTANCE.getMLCD20x4Button();

    /**
     * The meta object literal for the '<em><b>Device Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MLCD2_0X4_BUTTON__DEVICE_TYPE = eINSTANCE.getMLCD20x4Button_DeviceType();

    /**
     * The meta object literal for the '<em><b>Button Num</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MLCD2_0X4_BUTTON__BUTTON_NUM = eINSTANCE.getMLCD20x4Button_ButtonNum();

    /**
     * The meta object literal for the '<em><b>Callback Period</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MLCD2_0X4_BUTTON__CALLBACK_PERIOD = eINSTANCE.getMLCD20x4Button_CallbackPeriod();

    /**
     * The meta object literal for the '<em>Switch State</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.types.OnOffValue
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getSwitchState()
     * @generated
     */
    EDataType SWITCH_STATE = eINSTANCE.getSwitchState();

    /**
     * The meta object literal for the '<em>Digital Value</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.types.HighLowValue
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDigitalValue()
     * @generated
     */
    EDataType DIGITAL_VALUE = eINSTANCE.getDigitalValue();

    /**
     * The meta object literal for the '<em>Tinker Bricklet IO16</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletIO16
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getTinkerBrickletIO16()
     * @generated
     */
    EDataType TINKER_BRICKLET_IO16 = eINSTANCE.getTinkerBrickletIO16();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.DCDriveMode <em>DC Drive Mode</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.DCDriveMode
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDCDriveMode()
     * @generated
     */
    EEnum DC_DRIVE_MODE = eINSTANCE.getDCDriveMode();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.NoSubIds <em>No Sub Ids</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.NoSubIds
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getNoSubIds()
     * @generated
     */
    EEnum NO_SUB_IDS = eINSTANCE.getNoSubIds();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs <em>Industrial Digital In Sub IDs</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIndustrialDigitalInSubIDs()
     * @generated
     */
    EEnum INDUSTRIAL_DIGITAL_IN_SUB_IDS = eINSTANCE.getIndustrialDigitalInSubIDs();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs <em>Industrial Quad Relay IDs</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIndustrialQuadRelayIDs()
     * @generated
     */
    EEnum INDUSTRIAL_QUAD_RELAY_IDS = eINSTANCE.getIndustrialQuadRelayIDs();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.ServoSubIDs <em>Servo Sub IDs</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.ServoSubIDs
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getServoSubIDs()
     * @generated
     */
    EEnum SERVO_SUB_IDS = eINSTANCE.getServoSubIDs();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs <em>Barometer Sub IDs</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getBarometerSubIDs()
     * @generated
     */
    EEnum BAROMETER_SUB_IDS = eINSTANCE.getBarometerSubIDs();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.IO16SubIds <em>IO16 Sub Ids</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.IO16SubIds
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getIO16SubIds()
     * @generated
     */
    EEnum IO16_SUB_IDS = eINSTANCE.getIO16SubIds();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds <em>Dual Relay Sub Ids</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getDualRelaySubIds()
     * @generated
     */
    EEnum DUAL_RELAY_SUB_IDS = eINSTANCE.getDualRelaySubIds();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds <em>LCD Button Sub Ids</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getLCDButtonSubIds()
     * @generated
     */
    EEnum LCD_BUTTON_SUB_IDS = eINSTANCE.getLCDButtonSubIds();

    /**
     * The meta object literal for the '{@link org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds <em>LCD Backlight Sub Ids</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.model.LCDBacklightSubIds
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getLCDBacklightSubIds()
     * @generated
     */
    EEnum LCD_BACKLIGHT_SUB_IDS = eINSTANCE.getLCDBacklightSubIds();

    /**
     * The meta object literal for the '<em>MIP Connection</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.IPConnection
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMIPConnection()
     * @generated
     */
    EDataType MIP_CONNECTION = eINSTANCE.getMIPConnection();

    /**
     * The meta object literal for the '<em>MTinker Device</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.Device
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerDevice()
     * @generated
     */
    EDataType MTINKER_DEVICE = eINSTANCE.getMTinkerDevice();

    /**
     * The meta object literal for the '<em>MLogger</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.slf4j.Logger
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMLogger()
     * @generated
     */
    EDataType MLOGGER = eINSTANCE.getMLogger();

    /**
     * The meta object literal for the '<em>MAtomic Boolean</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.concurrent.atomic.AtomicBoolean
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMAtomicBoolean()
     * @generated
     */
    EDataType MATOMIC_BOOLEAN = eINSTANCE.getMAtomicBoolean();

    /**
     * The meta object literal for the '<em>MTinkerforge Device</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.Device
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerforgeDevice()
     * @generated
     */
    EDataType MTINKERFORGE_DEVICE = eINSTANCE.getMTinkerforgeDevice();

    /**
     * The meta object literal for the '<em>MTinker Brick DC</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickDC
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickDC()
     * @generated
     */
    EDataType MTINKER_BRICK_DC = eINSTANCE.getMTinkerBrickDC();

    /**
     * The meta object literal for the '<em>MTinker Brick Servo</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickServo
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickServo()
     * @generated
     */
    EDataType MTINKER_BRICK_SERVO = eINSTANCE.getMTinkerBrickServo();

    /**
     * The meta object literal for the '<em>MTinkerforge Value</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerforgeValue()
     * @generated
     */
    EDataType MTINKERFORGE_VALUE = eINSTANCE.getMTinkerforgeValue();

    /**
     * The meta object literal for the '<em>MDecimal Value</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.binding.tinkerforge.internal.types.DecimalValue
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMDecimalValue()
     * @generated
     */
    EDataType MDECIMAL_VALUE = eINSTANCE.getMDecimalValue();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Humidity</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletHumidity
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletHumidity()
     * @generated
     */
    EDataType MTINKER_BRICKLET_HUMIDITY = eINSTANCE.getMTinkerBrickletHumidity();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Distance IR</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletDistanceIR
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletDistanceIR()
     * @generated
     */
    EDataType MTINKER_BRICKLET_DISTANCE_IR = eINSTANCE.getMTinkerBrickletDistanceIR();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Temperature</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletTemperature
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletTemperature()
     * @generated
     */
    EDataType MTINKER_BRICKLET_TEMPERATURE = eINSTANCE.getMTinkerBrickletTemperature();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Barometer</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletBarometer
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletBarometer()
     * @generated
     */
    EDataType MTINKER_BRICKLET_BAROMETER = eINSTANCE.getMTinkerBrickletBarometer();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Ambient Light</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletAmbientLight
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletAmbientLight()
     * @generated
     */
    EDataType MTINKER_BRICKLET_AMBIENT_LIGHT = eINSTANCE.getMTinkerBrickletAmbientLight();

    /**
     * The meta object literal for the '<em>MTinker Bricklet LCD2 0x4</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletLCD20x4
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletLCD20x4()
     * @generated
     */
    EDataType MTINKER_BRICKLET_LCD2_0X4 = eINSTANCE.getMTinkerBrickletLCD20x4();

    /**
     * The meta object literal for the '<em>Enum</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Enum
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getEnum()
     * @generated
     */
    EDataType ENUM = eINSTANCE.getEnum();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Dual Relay</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletDualRelay
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletDualRelay()
     * @generated
     */
    EDataType MTINKER_BRICKLET_DUAL_RELAY = eINSTANCE.getMTinkerBrickletDualRelay();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Industrial Quad Relay</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletIndustrialQuadRelay
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletIndustrialQuadRelay()
     * @generated
     */
    EDataType MTINKER_BRICKLET_INDUSTRIAL_QUAD_RELAY = eINSTANCE.getMTinkerBrickletIndustrialQuadRelay();

    /**
     * The meta object literal for the '<em>MTinker Bricklet Industrial Digital In4</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.tinkerforge.BrickletIndustrialDigitalIn4
     * @see org.openhab.binding.tinkerforge.internal.model.impl.ModelPackageImpl#getMTinkerBrickletIndustrialDigitalIn4()
     * @generated
     */
    EDataType MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_IN4 = eINSTANCE.getMTinkerBrickletIndustrialDigitalIn4();

  }

} //ModelPackage
