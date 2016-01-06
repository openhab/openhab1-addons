/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.*;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.DirectionValue;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
import org.slf4j.Logger;

import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletAccelerometer;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.BrickletAnalogIn;
import com.tinkerforge.BrickletAnalogInV2;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletDustDetector;
import com.tinkerforge.BrickletHallEffect;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.BrickletIO4;
import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.BrickletIndustrialDigitalOut4;
import com.tinkerforge.BrickletIndustrialDual020mA;
import com.tinkerforge.BrickletIndustrialDualAnalogIn;
import com.tinkerforge.BrickletIndustrialQuadRelay;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletLEDStrip;
import com.tinkerforge.BrickletLaserRangeFinder;
import com.tinkerforge.BrickletLinearPoti;
import com.tinkerforge.BrickletLoadCell;
import com.tinkerforge.BrickletMoisture;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.BrickletPTC;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.BrickletRotaryEncoder;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.BrickletSolidStateRelay;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.BrickletVoltageCurrent;
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
      case ModelPackage.MBRICKLET_DUAL_BUTTON: return createMBrickletDualButton();
      case ModelPackage.MBRICKLET_PIEZO_SPEAKER: return createMBrickletPiezoSpeaker();
      case ModelPackage.DUAL_BUTTON_BUTTON: return createDualButtonButton();
      case ModelPackage.MBRICKLET_ACCELEROMETER: return createMBrickletAccelerometer();
      case ModelPackage.ACCELEROMETER_DIRECTION: return createAccelerometerDirection();
      case ModelPackage.ACCELEROMETER_TEMPERATURE: return createAccelerometerTemperature();
      case ModelPackage.ACCELEROMETER_LED: return createAccelerometerLed();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER: return createMBrickletLaserRangeFinder();
      case ModelPackage.LASER_RANGE_FINDER_LASER: return createLaserRangeFinderLaser();
      case ModelPackage.LASER_RANGE_FINDER_DISTANCE: return createLaserRangeFinderDistance();
      case ModelPackage.LASER_RANGE_FINDER_VELOCITY: return createLaserRangeFinderVelocity();
      case ModelPackage.MBRICKLET_LOAD_CELL: return createMBrickletLoadCell();
      case ModelPackage.LOAD_CELL_WEIGHT: return createLoadCellWeight();
      case ModelPackage.LOAD_CELL_LED: return createLoadCellLed();
      case ModelPackage.MBRICKLET_COLOR: return createMBrickletColor();
      case ModelPackage.COLOR_COLOR: return createColorColor();
      case ModelPackage.COLOR_ILLUMINANCE: return createColorIlluminance();
      case ModelPackage.COLOR_COLOR_TEMPERATURE: return createColorColorTemperature();
      case ModelPackage.COLOR_LED: return createColorLed();
      case ModelPackage.DUAL_BUTTON_LED: return createDualButtonLed();
      case ModelPackage.MBRICKLET_LINEAR_POTI: return createMBrickletLinearPoti();
      case ModelPackage.MBRICKLET_ROTARY_ENCODER: return createMBrickletRotaryEncoder();
      case ModelPackage.ROTARY_ENCODER: return createRotaryEncoder();
      case ModelPackage.ROTARY_ENCODER_BUTTON: return createRotaryEncoderButton();
      case ModelPackage.MBRICKLET_JOYSTICK: return createMBrickletJoystick();
      case ModelPackage.JOYSTICK_XPOSITION: return createJoystickXPosition();
      case ModelPackage.JOYSTICK_YPOSITION: return createJoystickYPosition();
      case ModelPackage.JOYSTICK_BUTTON: return createJoystickButton();
      case ModelPackage.MBRICK_SERVO: return createMBrickServo();
      case ModelPackage.MSERVO: return createMServo();
      case ModelPackage.MBRICK_DC: return createMBrickDC();
      case ModelPackage.MDUAL_RELAY_BRICKLET: return createMDualRelayBricklet();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY_BRICKLET: return createMIndustrialQuadRelayBricklet();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY: return createMIndustrialQuadRelay();
      case ModelPackage.MBRICKLET_INDUSTRIAL_DIGITAL_IN4: return createMBrickletIndustrialDigitalIn4();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN: return createMIndustrialDigitalIn();
      case ModelPackage.MBRICKLET_INDUSTRIAL_DIGITAL_OUT4: return createMBrickletIndustrialDigitalOut4();
      case ModelPackage.DIGITAL_ACTOR_DIGITAL_OUT4: return createDigitalActorDigitalOut4();
      case ModelPackage.MBRICKLET_SEGMENT_DISPLAY4X7: return createMBrickletSegmentDisplay4x7();
      case ModelPackage.MBRICKLET_LED_STRIP: return createMBrickletLEDStrip();
      case ModelPackage.LED_GROUP: return createLEDGroup();
      case ModelPackage.DIGITAL_ACTOR_IO16: return createDigitalActorIO16();
      case ModelPackage.MBRICKLET_IO16: return createMBrickletIO16();
      case ModelPackage.DIGITAL_SENSOR: return createDigitalSensor();
      case ModelPackage.MBRICKLET_IO4: return createMBrickletIO4();
      case ModelPackage.DIGITAL_SENSOR_IO4: return createDigitalSensorIO4();
      case ModelPackage.DIGITAL_ACTOR_IO4: return createDigitalActorIO4();
      case ModelPackage.MBRICKLET_MULTI_TOUCH: return createMBrickletMultiTouch();
      case ModelPackage.MULTI_TOUCH_DEVICE: return createMultiTouchDevice();
      case ModelPackage.ELECTRODE: return createElectrode();
      case ModelPackage.PROXIMITY: return createProximity();
      case ModelPackage.MBRICKLET_MOTION_DETECTOR: return createMBrickletMotionDetector();
      case ModelPackage.MBRICKLET_HALL_EFFECT: return createMBrickletHallEffect();
      case ModelPackage.MDUAL_RELAY: return createMDualRelay();
      case ModelPackage.MBRICKLET_REMOTE_SWITCH: return createMBrickletRemoteSwitch();
      case ModelPackage.REMOTE_SWITCH_A: return createRemoteSwitchA();
      case ModelPackage.REMOTE_SWITCH_B: return createRemoteSwitchB();
      case ModelPackage.REMOTE_SWITCH_C: return createRemoteSwitchC();
      case ModelPackage.MBRICKLET_HUMIDITY: return createMBrickletHumidity();
      case ModelPackage.MBRICKLET_DISTANCE_IR: return createMBrickletDistanceIR();
      case ModelPackage.MBRICKLET_SOLID_STATE_RELAY: return createMBrickletSolidStateRelay();
      case ModelPackage.MBRICKLET_INDUSTRIAL_DUAL020M_A: return createMBrickletIndustrialDual020mA();
      case ModelPackage.DUAL020M_ADEVICE: return createDual020mADevice();
      case ModelPackage.MBRICKLET_PTC: return createMBrickletPTC();
      case ModelPackage.PTC_TEMPERATURE: return createPTCTemperature();
      case ModelPackage.PTC_RESISTANCE: return createPTCResistance();
      case ModelPackage.PTC_CONNECTED: return createPTCConnected();
      case ModelPackage.MBRICKLET_TEMPERATURE: return createMBrickletTemperature();
      case ModelPackage.MBRICKLET_TEMPERATURE_IR: return createMBrickletTemperatureIR();
      case ModelPackage.OBJECT_TEMPERATURE: return createObjectTemperature();
      case ModelPackage.AMBIENT_TEMPERATURE: return createAmbientTemperature();
      case ModelPackage.MBRICKLET_TILT: return createMBrickletTilt();
      case ModelPackage.MBRICKLET_VOLTAGE_CURRENT: return createMBrickletVoltageCurrent();
      case ModelPackage.VC_DEVICE_VOLTAGE: return createVCDeviceVoltage();
      case ModelPackage.VC_DEVICE_CURRENT: return createVCDeviceCurrent();
      case ModelPackage.VC_DEVICE_POWER: return createVCDevicePower();
      case ModelPackage.MBRICKLET_BAROMETER: return createMBrickletBarometer();
      case ModelPackage.MBAROMETER_TEMPERATURE: return createMBarometerTemperature();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT: return createMBrickletAmbientLight();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2: return createMBrickletAmbientLightV2();
      case ModelPackage.MBRICKLET_INDUSTRIAL_DUAL_ANALOG_IN: return createMBrickletIndustrialDualAnalogIn();
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_CHANNEL: return createIndustrialDualAnalogInChannel();
      case ModelPackage.MBRICKLET_SOUND_INTENSITY: return createMBrickletSoundIntensity();
      case ModelPackage.MBRICKLET_DUST_DETECTOR: return createMBrickletDustDetector();
      case ModelPackage.MBRICKLET_MOISTURE: return createMBrickletMoisture();
      case ModelPackage.MBRICKLET_ANALOG_IN_V2: return createMBrickletAnalogInV2();
      case ModelPackage.MBRICKLET_ANALOG_IN: return createMBrickletAnalogIn();
      case ModelPackage.MBRICKLET_DISTANCE_US: return createMBrickletDistanceUS();
      case ModelPackage.MBRICKLET_LCD2_0X4: return createMBrickletLCD20x4();
      case ModelPackage.MLCD2_0X4_BACKLIGHT: return createMLCD20x4Backlight();
      case ModelPackage.MLCD2_0X4_BUTTON: return createMLCD20x4Button();
      case ModelPackage.OHTF_DEVICE: return createOHTFDevice();
      case ModelPackage.OHTF_SUB_DEVICE_ADMIN_DEVICE: return createOHTFSubDeviceAdminDevice();
      case ModelPackage.OH_CONFIG: return createOHConfig();
      case ModelPackage.TF_NULL_CONFIGURATION: return createTFNullConfiguration();
      case ModelPackage.TFPTC_BRICKLET_CONFIGURATION: return createTFPTCBrickletConfiguration();
      case ModelPackage.TF_INDUSTRIAL_DUAL020M_ACONFIGURATION: return createTFIndustrialDual020mAConfiguration();
      case ModelPackage.TF_BASE_CONFIGURATION: return createTFBaseConfiguration();
      case ModelPackage.LOAD_CELL_CONFIGURATION: return createLoadCellConfiguration();
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION: return createLaserRangeFinderConfiguration();
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION: return createAmbientLightV2Configuration();
      case ModelPackage.BRICKLET_INDUSTRIAL_DUAL_ANALOG_IN_CONFIGURATION: return createBrickletIndustrialDualAnalogInConfiguration();
      case ModelPackage.TF_TEMPERATURE_CONFIGURATION: return createTFTemperatureConfiguration();
      case ModelPackage.TF_OBJECT_TEMPERATURE_CONFIGURATION: return createTFObjectTemperatureConfiguration();
      case ModelPackage.TF_MOISTURE_BRICKLET_CONFIGURATION: return createTFMoistureBrickletConfiguration();
      case ModelPackage.TF_ANALOG_IN_CONFIGURATION: return createTFAnalogInConfiguration();
      case ModelPackage.TF_ANALOG_IN_V2_CONFIGURATION: return createTFAnalogInV2Configuration();
      case ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION: return createTFDistanceUSBrickletConfiguration();
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION: return createTFVoltageCurrentConfiguration();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION: return createTFBrickDCConfiguration();
      case ModelPackage.TFIO_ACTOR_CONFIGURATION: return createTFIOActorConfiguration();
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION: return createTFInterruptListenerConfiguration();
      case ModelPackage.TFIO_SENSOR_CONFIGURATION: return createTFIOSensorConfiguration();
      case ModelPackage.TF_SERVO_CONFIGURATION: return createTFServoConfiguration();
      case ModelPackage.BRICKLET_REMOTE_SWITCH_CONFIGURATION: return createBrickletRemoteSwitchConfiguration();
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION: return createRemoteSwitchAConfiguration();
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION: return createRemoteSwitchBConfiguration();
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION: return createRemoteSwitchCConfiguration();
      case ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION: return createMultiTouchDeviceConfiguration();
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION: return createBrickletMultiTouchConfiguration();
      case ModelPackage.DIMMABLE_CONFIGURATION: return createDimmableConfiguration();
      case ModelPackage.BUTTON_CONFIGURATION: return createButtonConfiguration();
      case ModelPackage.DUAL_BUTTON_LED_CONFIGURATION: return createDualButtonLEDConfiguration();
      case ModelPackage.LED_STRIP_CONFIGURATION: return createLEDStripConfiguration();
      case ModelPackage.LED_GROUP_CONFIGURATION: return createLEDGroupConfiguration();
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION: return createBrickletColorConfiguration();
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION: return createBrickletAccelerometerConfiguration();
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
      case ModelPackage.ACCELEROMETER_COORDINATE:
        return createAccelerometerCoordinateFromString(eDataType, initialValue);
      case ModelPackage.NO_SUB_IDS:
        return createNoSubIdsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_IN_SUB_IDS:
        return createIndustrialDigitalInSubIDsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_OUT_SUB_IDS:
        return createIndustrialDigitalOutSubIDsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_QUAD_RELAY_IDS:
        return createIndustrialQuadRelayIDsFromString(eDataType, initialValue);
      case ModelPackage.SERVO_SUB_IDS:
        return createServoSubIDsFromString(eDataType, initialValue);
      case ModelPackage.BAROMETER_SUB_IDS:
        return createBarometerSubIDsFromString(eDataType, initialValue);
      case ModelPackage.IO16_SUB_IDS:
        return createIO16SubIdsFromString(eDataType, initialValue);
      case ModelPackage.IO4_SUB_IDS:
        return createIO4SubIdsFromString(eDataType, initialValue);
      case ModelPackage.DUAL_RELAY_SUB_IDS:
        return createDualRelaySubIdsFromString(eDataType, initialValue);
      case ModelPackage.LCD_BUTTON_SUB_IDS:
        return createLCDButtonSubIdsFromString(eDataType, initialValue);
      case ModelPackage.LCD_BACKLIGHT_SUB_IDS:
        return createLCDBacklightSubIdsFromString(eDataType, initialValue);
      case ModelPackage.MULTI_TOUCH_SUB_IDS:
        return createMultiTouchSubIdsFromString(eDataType, initialValue);
      case ModelPackage.TEMPERATURE_IR_SUB_IDS:
        return createTemperatureIRSubIdsFromString(eDataType, initialValue);
      case ModelPackage.VOLTAGE_CURRENT_SUB_IDS:
        return createVoltageCurrentSubIdsFromString(eDataType, initialValue);
      case ModelPackage.CONFIG_OPTS_MOVE:
        return createConfigOptsMoveFromString(eDataType, initialValue);
      case ModelPackage.CONFIG_OPTS_DIMMABLE:
        return createConfigOptsDimmableFromString(eDataType, initialValue);
      case ModelPackage.CONFIG_OPTS_SET_POINT:
        return createConfigOptsSetPointFromString(eDataType, initialValue);
      case ModelPackage.CONFIG_OPTS_SWITCH_SPEED:
        return createConfigOptsSwitchSpeedFromString(eDataType, initialValue);
      case ModelPackage.DC_DRIVE_MODE:
        return createDCDriveModeFromString(eDataType, initialValue);
      case ModelPackage.CONFIG_OPTS_SERVO:
        return createConfigOptsServoFromString(eDataType, initialValue);
      case ModelPackage.DUAL_BUTTON_DEVICE_POSITION:
        return createDualButtonDevicePositionFromString(eDataType, initialValue);
      case ModelPackage.DUAL_BUTTON_LED_SUB_IDS:
        return createDualButtonLedSubIdsFromString(eDataType, initialValue);
      case ModelPackage.DUAL_BUTTON_BUTTON_SUB_IDS:
        return createDualButtonButtonSubIdsFromString(eDataType, initialValue);
      case ModelPackage.JOYSTICK_SUB_IDS:
        return createJoystickSubIdsFromString(eDataType, initialValue);
      case ModelPackage.PTC_SUB_IDS:
        return createPTCSubIdsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_DUAL020M_ASUB_IDS:
        return createIndustrialDual020mASubIdsFromString(eDataType, initialValue);
      case ModelPackage.ROTARY_ENCODER_SUB_IDS:
        return createRotaryEncoderSubIdsFromString(eDataType, initialValue);
      case ModelPackage.COLOR_BRICKLET_SUB_IDS:
        return createColorBrickletSubIdsFromString(eDataType, initialValue);
      case ModelPackage.LOAD_CELL_SUB_IDS:
        return createLoadCellSubIdsFromString(eDataType, initialValue);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_SUB_IDS:
        return createIndustrialDualAnalogInSubIdsFromString(eDataType, initialValue);
      case ModelPackage.LASER_RANGE_FINDER_SUB_IDS:
        return createLaserRangeFinderSubIdsFromString(eDataType, initialValue);
      case ModelPackage.ACCELEROMETER_SUB_IDS:
        return createAccelerometerSubIdsFromString(eDataType, initialValue);
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
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_OUT4:
        return createMTinkerBrickletIndustrialDigitalOut4FromString(eDataType, initialValue);
      case ModelPackage.SWITCH_STATE:
        return createSwitchStateFromString(eDataType, initialValue);
      case ModelPackage.DIGITAL_VALUE:
        return createDigitalValueFromString(eDataType, initialValue);
      case ModelPackage.HSB_VALUE:
        return createHSBValueFromString(eDataType, initialValue);
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
      case ModelPackage.TINKER_BRICKLET_MOTION_DETECTOR:
        return createTinkerBrickletMotionDetectorFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_MULTI_TOUCH:
        return createTinkerBrickletMultiTouchFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_TEMPERATURE_IR:
        return createTinkerBrickletTemperatureIRFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_SOUND_INTENSITY:
        return createTinkerBrickletSoundIntensityFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_MOISTURE:
        return createTinkerBrickletMoistureFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_DISTANCE_US:
        return createTinkerBrickletDistanceUSFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_VOLTAGE_CURRENT:
        return createTinkerBrickletVoltageCurrentFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_TILT:
        return createTinkerBrickletTiltFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_IO4:
        return createTinkerBrickletIO4FromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_HALL_EFFECT:
        return createTinkerBrickletHallEffectFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_SEGMENT_DISPLAY4X7:
        return createTinkerBrickletSegmentDisplay4x7FromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_LED_STRIP:
        return createTinkerBrickletLEDStripFromString(eDataType, initialValue);
      case ModelPackage.BRICKLET_JOYSTICK:
        return createBrickletJoystickFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_LINEAR_POTI:
        return createTinkerBrickletLinearPotiFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_DUAL_BUTTON:
        return createTinkerBrickletDualButtonFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_PTC:
        return createTinkerBrickletPTCFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_INDUSTRIAL_DUAL020M_A:
        return createTinkerBrickletIndustrialDual020mAFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_SOLID_STATE_RELAY:
        return createTinkerBrickletSolidStateRelayFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_PIEZO_SPEAKER:
        return createTinkerBrickletPiezoSpeakerFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_ROTARY_ENCODER:
        return createTinkerBrickletRotaryEncoderFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_AMBIENT_LIGHT_V2:
        return createTinkerBrickletAmbientLightV2FromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_DUST_DETECTOR:
        return createTinkerBrickletDustDetectorFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_LOAD_CELL:
        return createTinkerBrickletLoadCellFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_COLOR:
        return createTinkerBrickletColorFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_INDUSTRIAL_DUAL_ANALOG_IN:
        return createTinkerBrickletIndustrialDualAnalogInFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_ANALOG_IN_V2:
        return createTinkerBrickletAnalogInV2FromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_ANALOG_IN:
        return createTinkerBrickletAnalogInFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_LASER_RANGE_FINDER:
        return createTinkerBrickletLaserRangeFinderFromString(eDataType, initialValue);
      case ModelPackage.TINKER_BRICKLET_ACCELEROMETER:
        return createTinkerBrickletAccelerometerFromString(eDataType, initialValue);
      case ModelPackage.HSB_TYPE:
        return createHSBTypeFromString(eDataType, initialValue);
      case ModelPackage.UP_DOWN_TYPE:
        return createUpDownTypeFromString(eDataType, initialValue);
      case ModelPackage.PERCENT_VALUE:
        return createPercentValueFromString(eDataType, initialValue);
      case ModelPackage.DEVICE_OPTIONS:
        return createDeviceOptionsFromString(eDataType, initialValue);
      case ModelPackage.PERCENT_TYPE:
        return createPercentTypeFromString(eDataType, initialValue);
      case ModelPackage.INCREASE_DECREASE_TYPE:
        return createIncreaseDecreaseTypeFromString(eDataType, initialValue);
      case ModelPackage.DIRECTION_VALUE:
        return createDirectionValueFromString(eDataType, initialValue);
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
      case ModelPackage.ACCELEROMETER_COORDINATE:
        return convertAccelerometerCoordinateToString(eDataType, instanceValue);
      case ModelPackage.NO_SUB_IDS:
        return convertNoSubIdsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_IN_SUB_IDS:
        return convertIndustrialDigitalInSubIDsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_DIGITAL_OUT_SUB_IDS:
        return convertIndustrialDigitalOutSubIDsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_QUAD_RELAY_IDS:
        return convertIndustrialQuadRelayIDsToString(eDataType, instanceValue);
      case ModelPackage.SERVO_SUB_IDS:
        return convertServoSubIDsToString(eDataType, instanceValue);
      case ModelPackage.BAROMETER_SUB_IDS:
        return convertBarometerSubIDsToString(eDataType, instanceValue);
      case ModelPackage.IO16_SUB_IDS:
        return convertIO16SubIdsToString(eDataType, instanceValue);
      case ModelPackage.IO4_SUB_IDS:
        return convertIO4SubIdsToString(eDataType, instanceValue);
      case ModelPackage.DUAL_RELAY_SUB_IDS:
        return convertDualRelaySubIdsToString(eDataType, instanceValue);
      case ModelPackage.LCD_BUTTON_SUB_IDS:
        return convertLCDButtonSubIdsToString(eDataType, instanceValue);
      case ModelPackage.LCD_BACKLIGHT_SUB_IDS:
        return convertLCDBacklightSubIdsToString(eDataType, instanceValue);
      case ModelPackage.MULTI_TOUCH_SUB_IDS:
        return convertMultiTouchSubIdsToString(eDataType, instanceValue);
      case ModelPackage.TEMPERATURE_IR_SUB_IDS:
        return convertTemperatureIRSubIdsToString(eDataType, instanceValue);
      case ModelPackage.VOLTAGE_CURRENT_SUB_IDS:
        return convertVoltageCurrentSubIdsToString(eDataType, instanceValue);
      case ModelPackage.CONFIG_OPTS_MOVE:
        return convertConfigOptsMoveToString(eDataType, instanceValue);
      case ModelPackage.CONFIG_OPTS_DIMMABLE:
        return convertConfigOptsDimmableToString(eDataType, instanceValue);
      case ModelPackage.CONFIG_OPTS_SET_POINT:
        return convertConfigOptsSetPointToString(eDataType, instanceValue);
      case ModelPackage.CONFIG_OPTS_SWITCH_SPEED:
        return convertConfigOptsSwitchSpeedToString(eDataType, instanceValue);
      case ModelPackage.DC_DRIVE_MODE:
        return convertDCDriveModeToString(eDataType, instanceValue);
      case ModelPackage.CONFIG_OPTS_SERVO:
        return convertConfigOptsServoToString(eDataType, instanceValue);
      case ModelPackage.DUAL_BUTTON_DEVICE_POSITION:
        return convertDualButtonDevicePositionToString(eDataType, instanceValue);
      case ModelPackage.DUAL_BUTTON_LED_SUB_IDS:
        return convertDualButtonLedSubIdsToString(eDataType, instanceValue);
      case ModelPackage.DUAL_BUTTON_BUTTON_SUB_IDS:
        return convertDualButtonButtonSubIdsToString(eDataType, instanceValue);
      case ModelPackage.JOYSTICK_SUB_IDS:
        return convertJoystickSubIdsToString(eDataType, instanceValue);
      case ModelPackage.PTC_SUB_IDS:
        return convertPTCSubIdsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_DUAL020M_ASUB_IDS:
        return convertIndustrialDual020mASubIdsToString(eDataType, instanceValue);
      case ModelPackage.ROTARY_ENCODER_SUB_IDS:
        return convertRotaryEncoderSubIdsToString(eDataType, instanceValue);
      case ModelPackage.COLOR_BRICKLET_SUB_IDS:
        return convertColorBrickletSubIdsToString(eDataType, instanceValue);
      case ModelPackage.LOAD_CELL_SUB_IDS:
        return convertLoadCellSubIdsToString(eDataType, instanceValue);
      case ModelPackage.INDUSTRIAL_DUAL_ANALOG_IN_SUB_IDS:
        return convertIndustrialDualAnalogInSubIdsToString(eDataType, instanceValue);
      case ModelPackage.LASER_RANGE_FINDER_SUB_IDS:
        return convertLaserRangeFinderSubIdsToString(eDataType, instanceValue);
      case ModelPackage.ACCELEROMETER_SUB_IDS:
        return convertAccelerometerSubIdsToString(eDataType, instanceValue);
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
      case ModelPackage.MTINKER_BRICKLET_INDUSTRIAL_DIGITAL_OUT4:
        return convertMTinkerBrickletIndustrialDigitalOut4ToString(eDataType, instanceValue);
      case ModelPackage.SWITCH_STATE:
        return convertSwitchStateToString(eDataType, instanceValue);
      case ModelPackage.DIGITAL_VALUE:
        return convertDigitalValueToString(eDataType, instanceValue);
      case ModelPackage.HSB_VALUE:
        return convertHSBValueToString(eDataType, instanceValue);
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
      case ModelPackage.TINKER_BRICKLET_MOTION_DETECTOR:
        return convertTinkerBrickletMotionDetectorToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_MULTI_TOUCH:
        return convertTinkerBrickletMultiTouchToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_TEMPERATURE_IR:
        return convertTinkerBrickletTemperatureIRToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_SOUND_INTENSITY:
        return convertTinkerBrickletSoundIntensityToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_MOISTURE:
        return convertTinkerBrickletMoistureToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_DISTANCE_US:
        return convertTinkerBrickletDistanceUSToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_VOLTAGE_CURRENT:
        return convertTinkerBrickletVoltageCurrentToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_TILT:
        return convertTinkerBrickletTiltToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_IO4:
        return convertTinkerBrickletIO4ToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_HALL_EFFECT:
        return convertTinkerBrickletHallEffectToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_SEGMENT_DISPLAY4X7:
        return convertTinkerBrickletSegmentDisplay4x7ToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_LED_STRIP:
        return convertTinkerBrickletLEDStripToString(eDataType, instanceValue);
      case ModelPackage.BRICKLET_JOYSTICK:
        return convertBrickletJoystickToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_LINEAR_POTI:
        return convertTinkerBrickletLinearPotiToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_DUAL_BUTTON:
        return convertTinkerBrickletDualButtonToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_PTC:
        return convertTinkerBrickletPTCToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_INDUSTRIAL_DUAL020M_A:
        return convertTinkerBrickletIndustrialDual020mAToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_SOLID_STATE_RELAY:
        return convertTinkerBrickletSolidStateRelayToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_PIEZO_SPEAKER:
        return convertTinkerBrickletPiezoSpeakerToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_ROTARY_ENCODER:
        return convertTinkerBrickletRotaryEncoderToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_AMBIENT_LIGHT_V2:
        return convertTinkerBrickletAmbientLightV2ToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_DUST_DETECTOR:
        return convertTinkerBrickletDustDetectorToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_LOAD_CELL:
        return convertTinkerBrickletLoadCellToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_COLOR:
        return convertTinkerBrickletColorToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_INDUSTRIAL_DUAL_ANALOG_IN:
        return convertTinkerBrickletIndustrialDualAnalogInToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_ANALOG_IN_V2:
        return convertTinkerBrickletAnalogInV2ToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_ANALOG_IN:
        return convertTinkerBrickletAnalogInToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_LASER_RANGE_FINDER:
        return convertTinkerBrickletLaserRangeFinderToString(eDataType, instanceValue);
      case ModelPackage.TINKER_BRICKLET_ACCELEROMETER:
        return convertTinkerBrickletAccelerometerToString(eDataType, instanceValue);
      case ModelPackage.HSB_TYPE:
        return convertHSBTypeToString(eDataType, instanceValue);
      case ModelPackage.UP_DOWN_TYPE:
        return convertUpDownTypeToString(eDataType, instanceValue);
      case ModelPackage.PERCENT_VALUE:
        return convertPercentValueToString(eDataType, instanceValue);
      case ModelPackage.DEVICE_OPTIONS:
        return convertDeviceOptionsToString(eDataType, instanceValue);
      case ModelPackage.PERCENT_TYPE:
        return convertPercentTypeToString(eDataType, instanceValue);
      case ModelPackage.INCREASE_DECREASE_TYPE:
        return convertIncreaseDecreaseTypeToString(eDataType, instanceValue);
      case ModelPackage.DIRECTION_VALUE:
        return convertDirectionValueToString(eDataType, instanceValue);
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
  public MBrickletDualButton createMBrickletDualButton()
  {
    MBrickletDualButtonImpl mBrickletDualButton = new MBrickletDualButtonImpl();
    return mBrickletDualButton;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletPiezoSpeaker createMBrickletPiezoSpeaker()
  {
    MBrickletPiezoSpeakerImpl mBrickletPiezoSpeaker = new MBrickletPiezoSpeakerImpl();
    return mBrickletPiezoSpeaker;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonButton createDualButtonButton()
  {
    DualButtonButtonImpl dualButtonButton = new DualButtonButtonImpl();
    return dualButtonButton;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletAccelerometer createMBrickletAccelerometer()
  {
    MBrickletAccelerometerImpl mBrickletAccelerometer = new MBrickletAccelerometerImpl();
    return mBrickletAccelerometer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccelerometerDirection createAccelerometerDirection()
  {
    AccelerometerDirectionImpl accelerometerDirection = new AccelerometerDirectionImpl();
    return accelerometerDirection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccelerometerTemperature createAccelerometerTemperature()
  {
    AccelerometerTemperatureImpl accelerometerTemperature = new AccelerometerTemperatureImpl();
    return accelerometerTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccelerometerLed createAccelerometerLed()
  {
    AccelerometerLedImpl accelerometerLed = new AccelerometerLedImpl();
    return accelerometerLed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLaserRangeFinder createMBrickletLaserRangeFinder()
  {
    MBrickletLaserRangeFinderImpl mBrickletLaserRangeFinder = new MBrickletLaserRangeFinderImpl();
    return mBrickletLaserRangeFinder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderLaser createLaserRangeFinderLaser()
  {
    LaserRangeFinderLaserImpl laserRangeFinderLaser = new LaserRangeFinderLaserImpl();
    return laserRangeFinderLaser;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderDistance createLaserRangeFinderDistance()
  {
    LaserRangeFinderDistanceImpl laserRangeFinderDistance = new LaserRangeFinderDistanceImpl();
    return laserRangeFinderDistance;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderVelocity createLaserRangeFinderVelocity()
  {
    LaserRangeFinderVelocityImpl laserRangeFinderVelocity = new LaserRangeFinderVelocityImpl();
    return laserRangeFinderVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletColor createMBrickletColor()
  {
    MBrickletColorImpl mBrickletColor = new MBrickletColorImpl();
    return mBrickletColor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColorColor createColorColor()
  {
    ColorColorImpl colorColor = new ColorColorImpl();
    return colorColor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColorIlluminance createColorIlluminance()
  {
    ColorIlluminanceImpl colorIlluminance = new ColorIlluminanceImpl();
    return colorIlluminance;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColorColorTemperature createColorColorTemperature()
  {
    ColorColorTemperatureImpl colorColorTemperature = new ColorColorTemperatureImpl();
    return colorColorTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColorLed createColorLed()
  {
    ColorLedImpl colorLed = new ColorLedImpl();
    return colorLed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonLed createDualButtonLed()
  {
    DualButtonLedImpl dualButtonLed = new DualButtonLedImpl();
    return dualButtonLed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLinearPoti createMBrickletLinearPoti()
  {
    MBrickletLinearPotiImpl mBrickletLinearPoti = new MBrickletLinearPotiImpl();
    return mBrickletLinearPoti;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletRotaryEncoder createMBrickletRotaryEncoder()
  {
    MBrickletRotaryEncoderImpl mBrickletRotaryEncoder = new MBrickletRotaryEncoderImpl();
    return mBrickletRotaryEncoder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RotaryEncoder createRotaryEncoder()
  {
    RotaryEncoderImpl rotaryEncoder = new RotaryEncoderImpl();
    return rotaryEncoder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RotaryEncoderButton createRotaryEncoderButton()
  {
    RotaryEncoderButtonImpl rotaryEncoderButton = new RotaryEncoderButtonImpl();
    return rotaryEncoderButton;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletJoystick createMBrickletJoystick()
  {
    MBrickletJoystickImpl mBrickletJoystick = new MBrickletJoystickImpl();
    return mBrickletJoystick;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JoystickXPosition createJoystickXPosition()
  {
    JoystickXPositionImpl joystickXPosition = new JoystickXPositionImpl();
    return joystickXPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JoystickYPosition createJoystickYPosition()
  {
    JoystickYPositionImpl joystickYPosition = new JoystickYPositionImpl();
    return joystickYPosition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JoystickButton createJoystickButton()
  {
    JoystickButtonImpl joystickButton = new JoystickButtonImpl();
    return joystickButton;
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
  public MBrickletIndustrialDigitalOut4 createMBrickletIndustrialDigitalOut4()
  {
    MBrickletIndustrialDigitalOut4Impl mBrickletIndustrialDigitalOut4 = new MBrickletIndustrialDigitalOut4Impl();
    return mBrickletIndustrialDigitalOut4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalActorDigitalOut4 createDigitalActorDigitalOut4()
  {
    DigitalActorDigitalOut4Impl digitalActorDigitalOut4 = new DigitalActorDigitalOut4Impl();
    return digitalActorDigitalOut4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletSegmentDisplay4x7 createMBrickletSegmentDisplay4x7()
  {
    MBrickletSegmentDisplay4x7Impl mBrickletSegmentDisplay4x7 = new MBrickletSegmentDisplay4x7Impl();
    return mBrickletSegmentDisplay4x7;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLEDStrip createMBrickletLEDStrip()
  {
    MBrickletLEDStripImpl mBrickletLEDStrip = new MBrickletLEDStripImpl();
    return mBrickletLEDStrip;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LEDGroup createLEDGroup()
  {
    LEDGroupImpl ledGroup = new LEDGroupImpl();
    return ledGroup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalActorIO16 createDigitalActorIO16()
  {
    DigitalActorIO16Impl digitalActorIO16 = new DigitalActorIO16Impl();
    return digitalActorIO16;
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
  public MBrickletIO4 createMBrickletIO4()
  {
    MBrickletIO4Impl mBrickletIO4 = new MBrickletIO4Impl();
    return mBrickletIO4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalSensorIO4 createDigitalSensorIO4()
  {
    DigitalSensorIO4Impl digitalSensorIO4 = new DigitalSensorIO4Impl();
    return digitalSensorIO4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DigitalActorIO4 createDigitalActorIO4()
  {
    DigitalActorIO4Impl digitalActorIO4 = new DigitalActorIO4Impl();
    return digitalActorIO4;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletMultiTouch createMBrickletMultiTouch()
  {
    MBrickletMultiTouchImpl mBrickletMultiTouch = new MBrickletMultiTouchImpl();
    return mBrickletMultiTouch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MultiTouchDevice createMultiTouchDevice()
  {
    MultiTouchDeviceImpl multiTouchDevice = new MultiTouchDeviceImpl();
    return multiTouchDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Electrode createElectrode()
  {
    ElectrodeImpl electrode = new ElectrodeImpl();
    return electrode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Proximity createProximity()
  {
    ProximityImpl proximity = new ProximityImpl();
    return proximity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletMotionDetector createMBrickletMotionDetector()
  {
    MBrickletMotionDetectorImpl mBrickletMotionDetector = new MBrickletMotionDetectorImpl();
    return mBrickletMotionDetector;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletHallEffect createMBrickletHallEffect()
  {
    MBrickletHallEffectImpl mBrickletHallEffect = new MBrickletHallEffectImpl();
    return mBrickletHallEffect;
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
  public TFPTCBrickletConfiguration createTFPTCBrickletConfiguration()
  {
    TFPTCBrickletConfigurationImpl tfptcBrickletConfiguration = new TFPTCBrickletConfigurationImpl();
    return tfptcBrickletConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFIndustrialDual020mAConfiguration createTFIndustrialDual020mAConfiguration()
  {
    TFIndustrialDual020mAConfigurationImpl tfIndustrialDual020mAConfiguration = new TFIndustrialDual020mAConfigurationImpl();
    return tfIndustrialDual020mAConfiguration;
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
  public MultiTouchDeviceConfiguration createMultiTouchDeviceConfiguration()
  {
    MultiTouchDeviceConfigurationImpl multiTouchDeviceConfiguration = new MultiTouchDeviceConfigurationImpl();
    return multiTouchDeviceConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletMultiTouchConfiguration createBrickletMultiTouchConfiguration()
  {
    BrickletMultiTouchConfigurationImpl brickletMultiTouchConfiguration = new BrickletMultiTouchConfigurationImpl();
    return brickletMultiTouchConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DimmableConfiguration createDimmableConfiguration()
  {
    DimmableConfigurationImpl dimmableConfiguration = new DimmableConfigurationImpl();
    return dimmableConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ButtonConfiguration createButtonConfiguration()
  {
    ButtonConfigurationImpl buttonConfiguration = new ButtonConfigurationImpl();
    return buttonConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonLEDConfiguration createDualButtonLEDConfiguration()
  {
    DualButtonLEDConfigurationImpl dualButtonLEDConfiguration = new DualButtonLEDConfigurationImpl();
    return dualButtonLEDConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LEDStripConfiguration createLEDStripConfiguration()
  {
    LEDStripConfigurationImpl ledStripConfiguration = new LEDStripConfigurationImpl();
    return ledStripConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LEDGroupConfiguration createLEDGroupConfiguration()
  {
    LEDGroupConfigurationImpl ledGroupConfiguration = new LEDGroupConfigurationImpl();
    return ledGroupConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletColorConfiguration createBrickletColorConfiguration()
  {
    BrickletColorConfigurationImpl brickletColorConfiguration = new BrickletColorConfigurationImpl();
    return brickletColorConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAccelerometerConfiguration createBrickletAccelerometerConfiguration()
  {
    BrickletAccelerometerConfigurationImpl brickletAccelerometerConfiguration = new BrickletAccelerometerConfigurationImpl();
    return brickletAccelerometerConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccelerometerCoordinate createAccelerometerCoordinateFromString(EDataType eDataType, String initialValue)
  {
    AccelerometerCoordinate result = AccelerometerCoordinate.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertAccelerometerCoordinateToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
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
  public MBrickletSolidStateRelay createMBrickletSolidStateRelay()
  {
    MBrickletSolidStateRelayImpl mBrickletSolidStateRelay = new MBrickletSolidStateRelayImpl();
    return mBrickletSolidStateRelay;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDual020mA createMBrickletIndustrialDual020mA()
  {
    MBrickletIndustrialDual020mAImpl mBrickletIndustrialDual020mA = new MBrickletIndustrialDual020mAImpl();
    return mBrickletIndustrialDual020mA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Dual020mADevice createDual020mADevice()
  {
    Dual020mADeviceImpl dual020mADevice = new Dual020mADeviceImpl();
    return dual020mADevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletPTC createMBrickletPTC()
  {
    MBrickletPTCImpl mBrickletPTC = new MBrickletPTCImpl();
    return mBrickletPTC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PTCTemperature createPTCTemperature()
  {
    PTCTemperatureImpl ptcTemperature = new PTCTemperatureImpl();
    return ptcTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PTCResistance createPTCResistance()
  {
    PTCResistanceImpl ptcResistance = new PTCResistanceImpl();
    return ptcResistance;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PTCConnected createPTCConnected()
  {
    PTCConnectedImpl ptcConnected = new PTCConnectedImpl();
    return ptcConnected;
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
  public MBrickletTemperatureIR createMBrickletTemperatureIR()
  {
    MBrickletTemperatureIRImpl mBrickletTemperatureIR = new MBrickletTemperatureIRImpl();
    return mBrickletTemperatureIR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ObjectTemperature createObjectTemperature()
  {
    ObjectTemperatureImpl objectTemperature = new ObjectTemperatureImpl();
    return objectTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AmbientTemperature createAmbientTemperature()
  {
    AmbientTemperatureImpl ambientTemperature = new AmbientTemperatureImpl();
    return ambientTemperature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletTilt createMBrickletTilt()
  {
    MBrickletTiltImpl mBrickletTilt = new MBrickletTiltImpl();
    return mBrickletTilt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletVoltageCurrent createMBrickletVoltageCurrent()
  {
    MBrickletVoltageCurrentImpl mBrickletVoltageCurrent = new MBrickletVoltageCurrentImpl();
    return mBrickletVoltageCurrent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VCDeviceVoltage createVCDeviceVoltage()
  {
    VCDeviceVoltageImpl vcDeviceVoltage = new VCDeviceVoltageImpl();
    return vcDeviceVoltage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VCDeviceCurrent createVCDeviceCurrent()
  {
    VCDeviceCurrentImpl vcDeviceCurrent = new VCDeviceCurrentImpl();
    return vcDeviceCurrent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VCDevicePower createVCDevicePower()
  {
    VCDevicePowerImpl vcDevicePower = new VCDevicePowerImpl();
    return vcDevicePower;
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
  public LoadCellConfiguration createLoadCellConfiguration()
  {
    LoadCellConfigurationImpl loadCellConfiguration = new LoadCellConfigurationImpl();
    return loadCellConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderConfiguration createLaserRangeFinderConfiguration()
  {
    LaserRangeFinderConfigurationImpl laserRangeFinderConfiguration = new LaserRangeFinderConfigurationImpl();
    return laserRangeFinderConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AmbientLightV2Configuration createAmbientLightV2Configuration()
  {
    AmbientLightV2ConfigurationImpl ambientLightV2Configuration = new AmbientLightV2ConfigurationImpl();
    return ambientLightV2Configuration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIndustrialDualAnalogInConfiguration createBrickletIndustrialDualAnalogInConfiguration()
  {
    BrickletIndustrialDualAnalogInConfigurationImpl brickletIndustrialDualAnalogInConfiguration = new BrickletIndustrialDualAnalogInConfigurationImpl();
    return brickletIndustrialDualAnalogInConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFTemperatureConfiguration createTFTemperatureConfiguration()
  {
    TFTemperatureConfigurationImpl tfTemperatureConfiguration = new TFTemperatureConfigurationImpl();
    return tfTemperatureConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFObjectTemperatureConfiguration createTFObjectTemperatureConfiguration()
  {
    TFObjectTemperatureConfigurationImpl tfObjectTemperatureConfiguration = new TFObjectTemperatureConfigurationImpl();
    return tfObjectTemperatureConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFMoistureBrickletConfiguration createTFMoistureBrickletConfiguration()
  {
    TFMoistureBrickletConfigurationImpl tfMoistureBrickletConfiguration = new TFMoistureBrickletConfigurationImpl();
    return tfMoistureBrickletConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFAnalogInConfiguration createTFAnalogInConfiguration()
  {
    TFAnalogInConfigurationImpl tfAnalogInConfiguration = new TFAnalogInConfigurationImpl();
    return tfAnalogInConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFAnalogInV2Configuration createTFAnalogInV2Configuration()
  {
    TFAnalogInV2ConfigurationImpl tfAnalogInV2Configuration = new TFAnalogInV2ConfigurationImpl();
    return tfAnalogInV2Configuration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFDistanceUSBrickletConfiguration createTFDistanceUSBrickletConfiguration()
  {
    TFDistanceUSBrickletConfigurationImpl tfDistanceUSBrickletConfiguration = new TFDistanceUSBrickletConfigurationImpl();
    return tfDistanceUSBrickletConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFVoltageCurrentConfiguration createTFVoltageCurrentConfiguration()
  {
    TFVoltageCurrentConfigurationImpl tfVoltageCurrentConfiguration = new TFVoltageCurrentConfigurationImpl();
    return tfVoltageCurrentConfiguration;
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
  public MBrickletAmbientLightV2 createMBrickletAmbientLightV2()
  {
    MBrickletAmbientLightV2Impl mBrickletAmbientLightV2 = new MBrickletAmbientLightV2Impl();
    return mBrickletAmbientLightV2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDualAnalogIn createMBrickletIndustrialDualAnalogIn()
  {
    MBrickletIndustrialDualAnalogInImpl mBrickletIndustrialDualAnalogIn = new MBrickletIndustrialDualAnalogInImpl();
    return mBrickletIndustrialDualAnalogIn;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndustrialDualAnalogInChannel createIndustrialDualAnalogInChannel()
  {
    IndustrialDualAnalogInChannelImpl industrialDualAnalogInChannel = new IndustrialDualAnalogInChannelImpl();
    return industrialDualAnalogInChannel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletSoundIntensity createMBrickletSoundIntensity()
  {
    MBrickletSoundIntensityImpl mBrickletSoundIntensity = new MBrickletSoundIntensityImpl();
    return mBrickletSoundIntensity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletDustDetector createMBrickletDustDetector()
  {
    MBrickletDustDetectorImpl mBrickletDustDetector = new MBrickletDustDetectorImpl();
    return mBrickletDustDetector;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLoadCell createMBrickletLoadCell()
  {
    MBrickletLoadCellImpl mBrickletLoadCell = new MBrickletLoadCellImpl();
    return mBrickletLoadCell;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LoadCellWeight createLoadCellWeight()
  {
    LoadCellWeightImpl loadCellWeight = new LoadCellWeightImpl();
    return loadCellWeight;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LoadCellLed createLoadCellLed()
  {
    LoadCellLedImpl loadCellLed = new LoadCellLedImpl();
    return loadCellLed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletMoisture createMBrickletMoisture()
  {
    MBrickletMoistureImpl mBrickletMoisture = new MBrickletMoistureImpl();
    return mBrickletMoisture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletAnalogInV2 createMBrickletAnalogInV2()
  {
    MBrickletAnalogInV2Impl mBrickletAnalogInV2 = new MBrickletAnalogInV2Impl();
    return mBrickletAnalogInV2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletAnalogIn createMBrickletAnalogIn()
  {
    MBrickletAnalogInImpl mBrickletAnalogIn = new MBrickletAnalogInImpl();
    return mBrickletAnalogIn;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletDistanceUS createMBrickletDistanceUS()
  {
    MBrickletDistanceUSImpl mBrickletDistanceUS = new MBrickletDistanceUSImpl();
    return mBrickletDistanceUS;
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
  public HSBValue createHSBValueFromString(EDataType eDataType, String initialValue)
  {
    return (HSBValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertHSBValueToString(EDataType eDataType, Object instanceValue)
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
  public ConfigOptsServo createConfigOptsServoFromString(EDataType eDataType, String initialValue)
  {
    ConfigOptsServo result = ConfigOptsServo.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertConfigOptsServoToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonDevicePosition createDualButtonDevicePositionFromString(EDataType eDataType, String initialValue)
  {
    DualButtonDevicePosition result = DualButtonDevicePosition.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDualButtonDevicePositionToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonLedSubIds createDualButtonLedSubIdsFromString(EDataType eDataType, String initialValue)
  {
    DualButtonLedSubIds result = DualButtonLedSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDualButtonLedSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DualButtonButtonSubIds createDualButtonButtonSubIdsFromString(EDataType eDataType, String initialValue)
  {
    DualButtonButtonSubIds result = DualButtonButtonSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDualButtonButtonSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JoystickSubIds createJoystickSubIdsFromString(EDataType eDataType, String initialValue)
  {
    JoystickSubIds result = JoystickSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertJoystickSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PTCSubIds createPTCSubIdsFromString(EDataType eDataType, String initialValue)
  {
    PTCSubIds result = PTCSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPTCSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndustrialDual020mASubIds createIndustrialDual020mASubIdsFromString(EDataType eDataType, String initialValue)
  {
    IndustrialDual020mASubIds result = IndustrialDual020mASubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIndustrialDual020mASubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RotaryEncoderSubIds createRotaryEncoderSubIdsFromString(EDataType eDataType, String initialValue)
  {
    RotaryEncoderSubIds result = RotaryEncoderSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertRotaryEncoderSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColorBrickletSubIds createColorBrickletSubIdsFromString(EDataType eDataType, String initialValue)
  {
    ColorBrickletSubIds result = ColorBrickletSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertColorBrickletSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LoadCellSubIds createLoadCellSubIdsFromString(EDataType eDataType, String initialValue)
  {
    LoadCellSubIds result = LoadCellSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertLoadCellSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndustrialDualAnalogInSubIds createIndustrialDualAnalogInSubIdsFromString(EDataType eDataType, String initialValue)
  {
    IndustrialDualAnalogInSubIds result = IndustrialDualAnalogInSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIndustrialDualAnalogInSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderSubIds createLaserRangeFinderSubIdsFromString(EDataType eDataType, String initialValue)
  {
    LaserRangeFinderSubIds result = LaserRangeFinderSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertLaserRangeFinderSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccelerometerSubIds createAccelerometerSubIdsFromString(EDataType eDataType, String initialValue)
  {
    AccelerometerSubIds result = AccelerometerSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertAccelerometerSubIdsToString(EDataType eDataType, Object instanceValue)
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
  public IndustrialDigitalOutSubIDs createIndustrialDigitalOutSubIDsFromString(EDataType eDataType, String initialValue)
  {
    IndustrialDigitalOutSubIDs result = IndustrialDigitalOutSubIDs.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIndustrialDigitalOutSubIDsToString(EDataType eDataType, Object instanceValue)
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
  public IO4SubIds createIO4SubIdsFromString(EDataType eDataType, String initialValue)
  {
    IO4SubIds result = IO4SubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIO4SubIdsToString(EDataType eDataType, Object instanceValue)
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
  public MultiTouchSubIds createMultiTouchSubIdsFromString(EDataType eDataType, String initialValue)
  {
    MultiTouchSubIds result = MultiTouchSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMultiTouchSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TemperatureIRSubIds createTemperatureIRSubIdsFromString(EDataType eDataType, String initialValue)
  {
    TemperatureIRSubIds result = TemperatureIRSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTemperatureIRSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VoltageCurrentSubIds createVoltageCurrentSubIdsFromString(EDataType eDataType, String initialValue)
  {
    VoltageCurrentSubIds result = VoltageCurrentSubIds.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertVoltageCurrentSubIdsToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigOptsMove createConfigOptsMoveFromString(EDataType eDataType, String initialValue)
  {
    ConfigOptsMove result = ConfigOptsMove.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertConfigOptsMoveToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigOptsDimmable createConfigOptsDimmableFromString(EDataType eDataType, String initialValue)
  {
    ConfigOptsDimmable result = ConfigOptsDimmable.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertConfigOptsDimmableToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigOptsSetPoint createConfigOptsSetPointFromString(EDataType eDataType, String initialValue)
  {
    ConfigOptsSetPoint result = ConfigOptsSetPoint.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertConfigOptsSetPointToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigOptsSwitchSpeed createConfigOptsSwitchSpeedFromString(EDataType eDataType, String initialValue)
  {
    ConfigOptsSwitchSpeed result = ConfigOptsSwitchSpeed.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertConfigOptsSwitchSpeedToString(EDataType eDataType, Object instanceValue)
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
  public BrickletMotionDetector createTinkerBrickletMotionDetectorFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletMotionDetector)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletMotionDetectorToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletMultiTouch createTinkerBrickletMultiTouchFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletMultiTouch)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletMultiTouchToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletTemperatureIR createTinkerBrickletTemperatureIRFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletTemperatureIR)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletTemperatureIRToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletSoundIntensity createTinkerBrickletSoundIntensityFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletSoundIntensity)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletSoundIntensityToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletMoisture createTinkerBrickletMoistureFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletMoisture)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletMoistureToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletDistanceUS createTinkerBrickletDistanceUSFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletDistanceUS)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletDistanceUSToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletVoltageCurrent createTinkerBrickletVoltageCurrentFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletVoltageCurrent)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletVoltageCurrentToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletTilt createTinkerBrickletTiltFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletTilt)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletTiltToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIO4 createTinkerBrickletIO4FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIO4)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletIO4ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletHallEffect createTinkerBrickletHallEffectFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletHallEffect)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletHallEffectToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletSegmentDisplay4x7 createTinkerBrickletSegmentDisplay4x7FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletSegmentDisplay4x7)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletSegmentDisplay4x7ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLEDStrip createTinkerBrickletLEDStripFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletLEDStrip)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletLEDStripToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletJoystick createBrickletJoystickFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletJoystick)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertBrickletJoystickToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLinearPoti createTinkerBrickletLinearPotiFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletLinearPoti)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletLinearPotiToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletDualButton createTinkerBrickletDualButtonFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletDualButton)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletDualButtonToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletPTC createTinkerBrickletPTCFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletPTC)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletPTCToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIndustrialDual020mA createTinkerBrickletIndustrialDual020mAFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIndustrialDual020mA)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletIndustrialDual020mAToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletSolidStateRelay createTinkerBrickletSolidStateRelayFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletSolidStateRelay)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletSolidStateRelayToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletPiezoSpeaker createTinkerBrickletPiezoSpeakerFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletPiezoSpeaker)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletPiezoSpeakerToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletRotaryEncoder createTinkerBrickletRotaryEncoderFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletRotaryEncoder)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletRotaryEncoderToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAmbientLightV2 createTinkerBrickletAmbientLightV2FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletAmbientLightV2)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletAmbientLightV2ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletDustDetector createTinkerBrickletDustDetectorFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletDustDetector)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletDustDetectorToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLoadCell createTinkerBrickletLoadCellFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletLoadCell)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletLoadCellToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletColor createTinkerBrickletColorFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletColor)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletColorToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletIndustrialDualAnalogIn createTinkerBrickletIndustrialDualAnalogInFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIndustrialDualAnalogIn)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletIndustrialDualAnalogInToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAnalogInV2 createTinkerBrickletAnalogInV2FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletAnalogInV2)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletAnalogInV2ToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAnalogIn createTinkerBrickletAnalogInFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletAnalogIn)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletAnalogInToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLaserRangeFinder createTinkerBrickletLaserRangeFinderFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletLaserRangeFinder)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletLaserRangeFinderToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAccelerometer createTinkerBrickletAccelerometerFromString(EDataType eDataType, String initialValue)
  {
    return (BrickletAccelerometer)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTinkerBrickletAccelerometerToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HSBType createHSBTypeFromString(EDataType eDataType, String initialValue)
  {
    return (HSBType)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertHSBTypeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UpDownType createUpDownTypeFromString(EDataType eDataType, String initialValue)
  {
    return (UpDownType)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertUpDownTypeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PercentValue createPercentValueFromString(EDataType eDataType, String initialValue)
  {
    return (PercentValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPercentValueToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DeviceOptions createDeviceOptionsFromString(EDataType eDataType, String initialValue)
  {
    return (DeviceOptions)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDeviceOptionsToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PercentType createPercentTypeFromString(EDataType eDataType, String initialValue)
  {
    return (PercentType)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPercentTypeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IncreaseDecreaseType createIncreaseDecreaseTypeFromString(EDataType eDataType, String initialValue)
  {
    return (IncreaseDecreaseType)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIncreaseDecreaseTypeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DirectionValue createDirectionValueFromString(EDataType eDataType, String initialValue)
  {
    return (DirectionValue)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDirectionValueToString(EDataType eDataType, Object instanceValue)
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
  public BrickletIndustrialDigitalOut4 createMTinkerBrickletIndustrialDigitalOut4FromString(EDataType eDataType, String initialValue)
  {
    return (BrickletIndustrialDigitalOut4)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertMTinkerBrickletIndustrialDigitalOut4ToString(EDataType eDataType, Object instanceValue)
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
