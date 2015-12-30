/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.model.BarometerSubIDs;
import org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ButtonConfiguration;
import org.openhab.binding.tinkerforge.internal.model.DualButtonButtonSubIds;
import org.openhab.binding.tinkerforge.internal.model.DualButtonLEDConfiguration;
import org.openhab.binding.tinkerforge.internal.model.DualButtonLedSubIds;
import org.openhab.binding.tinkerforge.internal.model.DualRelaySubIds;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.IO16SubIds;
import org.openhab.binding.tinkerforge.internal.model.IO4SubIds;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalInSubIDs;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDigitalOutSubIDs;
import org.openhab.binding.tinkerforge.internal.model.IndustrialDual020mASubIds;
import org.openhab.binding.tinkerforge.internal.model.IndustrialQuadRelayIDs;
import org.openhab.binding.tinkerforge.internal.model.JoystickSubIds;
import org.openhab.binding.tinkerforge.internal.model.LCDButtonSubIds;
import org.openhab.binding.tinkerforge.internal.model.LEDGroupConfiguration;
import org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchSubIds;
import org.openhab.binding.tinkerforge.internal.model.NoSubIds;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.OHTFSubDeviceAdminDevice;
import org.openhab.binding.tinkerforge.internal.model.PTCSubIds;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ServoSubIDs;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFDistanceUSBrickletConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIndustrialDual020mAConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFPTCBrickletConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFTemperatureConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFVoltageCurrentConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TemperatureIRSubIds;
import org.openhab.binding.tinkerforge.internal.model.VoltageCurrentSubIds;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to parse the device configuration from openhab.cfg for the TinkerforgeBinding.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * 
 */
public class ConfigurationHandler {

  private ModelFactory modelFactory;
  private OHConfig ohConfig;
  private static final Logger logger = LoggerFactory.getLogger(ConfigurationHandler.class);

  private static final Pattern UID_PATTERN = Pattern.compile(String.format("^(.*?)\\.(%s)$",
      ConfigKey.uid.name()));

  private enum ConfigKey {
    subid, uid, type, hosts
  }

  private enum ConfigKeyAdmin {
    subid, uid, type, ohId;
  }

  private enum TypeKey {
    servo, bricklet_distance_ir, brick_dc, bricklet_humidity, bricklet_temperature, 
    bricklet_barometer, bricklet_ambient_light, io_actuator, iosensor, bricklet_io16, 
    bricklet_industrial_digital_4in, remote_switch_a, remote_switch_b, remote_switch_c, 
    bricklet_remote_switch, bricklet_multitouch, electrode, proximity, object_temperature,
    ambient_temperature, bricklet_temperatureIR, bricklet_soundintensity, 
    bricklet_moisture, bricklet_distanceUS, bricklet_voltageCurrent, 
    voltageCurrent_voltage, voltageCurrent_current, voltageCurrent_power, bricklet_tilt, 
    io4_actuator, io4sensor, bricklet_io4, bricklet_halleffect, bricklet_joystick, 
    joystick_button, joystick_xposition, joystick_yposition,
    bricklet_linear_poti, dualbutton_button, dualbutton_led, lcd_button, 
    bricklet_ledstrip, ledgroup, bricklet_ptc, ptc_temperature, ptc_resistance, 
    industrial020ma_sensor, bricklet_industrialdual020ma, dual_relay, quad_relay,
    digital_4in, digital_4out
  }


  public ConfigurationHandler() {
    modelFactory = ModelFactory.eINSTANCE;
    ohConfig = modelFactory.createOHConfig();
  }

  public OHConfig createConfig(Dictionary<String, ?> config) throws ConfigurationException {
    Map<String, Map<String, String>> configContainer = createConfigContainer(config);

    for (Map<String, String> deviceConfig : configContainer.values()) {
      createOHTFDeviceConfig(deviceConfig);
    }
    return ohConfig;

  }

  /**
   * Parses the configuration received from configManagement service and caches it in a map. This
   * map is added to another map with the openhab symbolic device name as key. The symbolic name is
   * the first part of configuration key of the openhab.cfg entry. The configuration entries look
   * like this: tinkerforge:<openhab symbolic device name>.<property>=<value> e.g.
   * "tinkerforge:dc_garage.uid=62Zduj"
   * 
   * @param config The configuration received from the configManagement service.
   * @return The parsed configuration for each device as Map with the configuration key as String
   *         and the value as String. These maps are hold in an outer Map with the symbolic device
   *         name as key.
   * @throws ConfigurationException
   */
  private Map<String, Map<String, String>> createConfigContainer(Dictionary<String, ?> config)
      throws ConfigurationException {
    Map<String, Map<String, String>> configContainer = new HashMap<String, Map<String, String>>();
    Enumeration<String> keys = config.keys();
    ArrayList<String> keys2ignore = new ArrayList<String>(3);
    keys2ignore.add(ConfigKey.uid.name());
    keys2ignore.add(ConfigKey.subid.name());

    while (keys.hasMoreElements()) {
      // first search all uids
      String key = keys.nextElement();
      logger.debug("{} key:value {} : {}", LoggerConstants.CONFIG, key, config.get(key));
      // the config-key enumeration contains additional keys that
      // we
      // don't want to process here ...
      if ("service.pid".equals(key)) {
        continue;
      }
      Matcher matcher = UID_PATTERN.matcher(key);
      if (matcher.matches()) {
        matcher.reset();
        matcher.find();
        HashMap<String, String> configMap = new HashMap<String, String>();
        String ohId = matcher.group(1);
        logger.trace("{} found symbolic name: {}", LoggerConstants.CONFIG, ohId);
        configMap.put(ConfigKeyAdmin.ohId.name(), ohId);
        configMap.put(ConfigKey.uid.name(), (String) config.get(key));
        configMap.put(ConfigKey.subid.name(),
            (String) config.get(ohId + "." + ConfigKey.subid.name()));
        String deviceType = (String) config.get(ohId + "." + ConfigKey.type.name());
        configMap.put(ConfigKey.type.name(), deviceType);
        if (deviceType == null) {
          throw new ConfigurationException(ohId, "type is missing");
        }
        checkTfType(ohId, deviceType);
        if (configContainer.containsKey(ohId)) {
          throw new ConfigurationException(ohId, String.format(
              "{} found duplicate entry for symbolic name {}", LoggerConstants.CONFIG, ohId));
        }
        // second iteration to get the remaining, not common,
        // configuration keys and their values
        Enumeration<String> keys2 = config.keys();
        Pattern ohIdPattern = Pattern.compile(String.format("^%s\\.(.*?)$", ohId));
        while (keys2.hasMoreElements()) {
          String key2 = keys2.nextElement();
          Matcher matcherOhId = ohIdPattern.matcher(key2);
          if (matcherOhId.matches()) {
            matcherOhId.reset();
            matcherOhId.find();
            String matchedKey = matcherOhId.group(1);
            if (keys2ignore.contains(matchedKey)) {
              continue;
            } else {
              String value2 = (String) config.get(key2);
              logger.trace("TFOPENHABCONFIG {} ohConfigKey {}", matchedKey, value2);
              configMap.put(matchedKey, value2);
            }
          }
        }
        configContainer.put(ohId, configMap);
      }
    }
    return configContainer;
  }

  /**
   * Generates the {@link OHConfig} EMF model configuration store for the device configuration from
   * openhab.cfg.
   * 
   * Creates the device specific configuration object {@link OHTFDevice} and calls
   * {@link #fillupConfig(OHTFDevice, Map) fillupConfig} to fill in the configuration into the
   * configuration object.
   * 
   * @param deviceConfig The device configuration as {@code Map} of {@code Strings}.
   * @throws ConfigurationException
   */
  private void createOHTFDeviceConfig(Map<String, String> deviceConfig)
      throws ConfigurationException {
    String deviceType = deviceConfig.get(ConfigKey.type.name());
    if (deviceType.equals(TypeKey.servo.name())) {
      logger.debug("{} setting servo config", LoggerConstants.CONFIG);
      TFServoConfiguration servoConfiguration = modelFactory.createTFServoConfiguration();
      OHTFDevice<TFServoConfiguration, ServoSubIDs> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(ServoSubIDs.values()));
      ohtfDevice.setTfConfig(servoConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_distance_ir.name())
        || deviceType.equals(TypeKey.bricklet_humidity.name())
        || deviceType.equals(TypeKey.bricklet_barometer.name())
        || deviceType.equals(TypeKey.bricklet_ambient_light.name())
        || deviceType.equals(TypeKey.ambient_temperature.name())
        || deviceType.equals(TypeKey.bricklet_soundintensity.name())
        || deviceType.equals(TypeKey.voltageCurrent_voltage.name())
        || deviceType.equals(TypeKey.voltageCurrent_current.name())
        || deviceType.equals(TypeKey.voltageCurrent_power.name())
        || deviceType.equals(TypeKey.bricklet_joystick.name())
        || deviceType.equals(TypeKey.bricklet_halleffect.name())
        || deviceType.equals(TypeKey.bricklet_linear_poti.name())
        || deviceType.equals(TypeKey.ptc_resistance.name())
        || deviceType.equals(TypeKey.ptc_temperature.name())
        || deviceType.equals(TypeKey.industrial020ma_sensor.name())) {
      logger.debug("{} setting base config", LoggerConstants.CONFIG);
      TFBaseConfiguration tfBaseConfiguration = modelFactory.createTFBaseConfiguration();
      if (deviceType.equals(TypeKey.bricklet_barometer)) {
        OHTFDevice<TFBaseConfiguration, BarometerSubIDs> ohtfDevice =
            modelFactory.createOHTFDevice();
        ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(BarometerSubIDs.values()));
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      } else if (deviceType.equals(TypeKey.ambient_temperature.name())) {
        OHTFDevice<TFBaseConfiguration, TemperatureIRSubIds> ohtfDevice =
            modelFactory.createOHTFDevice();
        ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(TemperatureIRSubIds.values()));
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      } else if (deviceType.equals(TypeKey.voltageCurrent_current.name())
          || deviceType.equals(TypeKey.voltageCurrent_voltage.name())
          || deviceType.equals(TypeKey.voltageCurrent_power.name())) {
        OHTFDevice<TFBaseConfiguration, VoltageCurrentSubIds> ohtfDevice =
            modelFactory.createOHTFDevice();
        ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(VoltageCurrentSubIds.values()));
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      } else if (deviceType.equals(TypeKey.ptc_resistance.name())
          || deviceType.equals(TypeKey.ptc_temperature.name())) {
        OHTFDevice<TFBaseConfiguration, PTCSubIds> ohtfDevice =
            modelFactory.createOHTFDevice();
        ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(PTCSubIds.values()));
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      } else if (deviceType.equals(TypeKey.industrial020ma_sensor.name())) {
        OHTFDevice<TFBaseConfiguration, IndustrialDual020mASubIds> ohtfDevice =
            modelFactory.createOHTFDevice();
        ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IndustrialDual020mASubIds.values()));
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      } else {
        OHTFDevice<TFBaseConfiguration, NoSubIds> ohtfDevice = modelFactory.createOHTFDevice();
        ohtfDevice.setTfConfig(tfBaseConfiguration);
        fillupConfig(ohtfDevice, deviceConfig);
      }

    } else if (deviceType.equals(TypeKey.brick_dc.name())) {
      logger.debug("{} setting dc config", LoggerConstants.CONFIG);
      TFBrickDCConfiguration tfBrickDCConfiguration = modelFactory.createTFBrickDCConfiguration();
      OHTFDevice<TFBrickDCConfiguration, NoSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(tfBrickDCConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.io_actuator.name())) {
      logger.debug("{} setting io_actuator config", LoggerConstants.CONFIG);
      TFIOActorConfiguration tfioActorConfiguration = modelFactory.createTFIOActorConfiguration();
      OHTFDevice<TFIOActorConfiguration, IO16SubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IO16SubIds.values()));
      ohtfDevice.setTfConfig(tfioActorConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.io4_actuator.name())) {
      logger.debug("{} setting io4_actuator config", LoggerConstants.CONFIG);
      TFIOActorConfiguration tfioActorConfiguration = modelFactory.createTFIOActorConfiguration();
      OHTFDevice<TFIOActorConfiguration, IO4SubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IO4SubIds.values()));
      ohtfDevice.setTfConfig(tfioActorConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.iosensor.name())) {
      logger.debug("{} setting iosensor config", LoggerConstants.CONFIG);
      TFIOSensorConfiguration tfioSensorConfiguration =
          modelFactory.createTFIOSensorConfiguration();
      OHTFDevice<TFIOSensorConfiguration, IO16SubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IO16SubIds.values()));
      ohtfDevice.setTfConfig(tfioSensorConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.io4sensor.name())) {
      logger.debug("{} setting io4sensor config", LoggerConstants.CONFIG);
      TFIOSensorConfiguration tfioSensorConfiguration =
          modelFactory.createTFIOSensorConfiguration();
      OHTFDevice<TFIOSensorConfiguration, IO4SubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IO4SubIds.values()));
      ohtfDevice.setTfConfig(tfioSensorConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_industrial_digital_4in.name())
        || deviceType.equals(TypeKey.bricklet_io16.name())
        || deviceType.equals(TypeKey.bricklet_io4.name())) {
      logger.debug("{} setting TFInterruptListenerConfiguration device_type {}",
          LoggerConstants.CONFIG, deviceType);
      TFInterruptListenerConfiguration tfInterruptListenerConfiguration =
          modelFactory.createTFInterruptListenerConfiguration();
      OHTFDevice<TFInterruptListenerConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(tfInterruptListenerConfiguration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_remote_switch.name())) {
      logger.debug("{} setting BrickletRemoteSwitchConfiguration device_type {}",
          LoggerConstants.CONFIG, deviceType);
      BrickletRemoteSwitchConfiguration configuration =
          modelFactory.createBrickletRemoteSwitchConfiguration();
      OHTFDevice<BrickletRemoteSwitchConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);

    } else if (deviceType.equals(TypeKey.remote_switch_a.name())) {
      logger.debug("{} setting RemoteSwitchAConfiguration device_type {}", LoggerConstants.CONFIG,
          deviceType);
      RemoteSwitchAConfiguration configuration = modelFactory.createRemoteSwitchAConfiguration();
      OHTFSubDeviceAdminDevice<RemoteSwitchAConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFSubDeviceAdminDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.remote_switch_b.name())) {
      logger.debug("{} setting RemoteSwitchBConfiguration device_type {}", LoggerConstants.CONFIG,
          deviceType);
      RemoteSwitchBConfiguration configuration = modelFactory.createRemoteSwitchBConfiguration();
      OHTFSubDeviceAdminDevice<RemoteSwitchBConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFSubDeviceAdminDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.remote_switch_c.name())) {
      logger.debug("{} setting RemoteSwitchCConfiguration device_type {}", LoggerConstants.CONFIG,
          deviceType);
      RemoteSwitchCConfiguration configuration = modelFactory.createRemoteSwitchCConfiguration();
      OHTFSubDeviceAdminDevice<RemoteSwitchCConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFSubDeviceAdminDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_multitouch.name())) {
      logger.debug("{} setting BrickletMultiTouchConfiguration device_type {}",
          LoggerConstants.CONFIG, deviceType);
      BrickletMultiTouchConfiguration configuration =
          modelFactory.createBrickletMultiTouchConfiguration();
      OHTFDevice<BrickletMultiTouchConfiguration, MultiTouchSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(MultiTouchSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.electrode.name())
        || deviceType.equals(TypeKey.proximity.name())) {
      logger.debug("{} setting MultiTouchDeviceConfiguration device_type {}",
          LoggerConstants.CONFIG, deviceType);
      MultiTouchDeviceConfiguration configuration =
          modelFactory.createMultiTouchDeviceConfiguration();
      OHTFDevice<MultiTouchDeviceConfiguration, MultiTouchSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(MultiTouchSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.object_temperature.name())) {
      logger.debug("{} setting TFObjectTemperatureConfiguration device_type {}",
          LoggerConstants.CONFIG, deviceType);
      TFObjectTemperatureConfiguration configuration =
          modelFactory.createTFObjectTemperatureConfiguration();
      OHTFDevice<TFObjectTemperatureConfiguration, TemperatureIRSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(TemperatureIRSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_moisture.name())) {
      TFMoistureBrickletConfiguration configuration =
          modelFactory.createTFMoistureBrickletConfiguration();
      OHTFDevice<TFMoistureBrickletConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_distanceUS.name())) {
      TFDistanceUSBrickletConfiguration configuration =
          modelFactory.createTFDistanceUSBrickletConfiguration();
      OHTFDevice<TFDistanceUSBrickletConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_voltageCurrent.name())) {
      TFVoltageCurrentConfiguration configuration =
          modelFactory.createTFVoltageCurrentConfiguration();
      OHTFDevice<TFVoltageCurrentConfiguration, VoltageCurrentSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(VoltageCurrentSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.dualbutton_button.name())) {
      ButtonConfiguration configuration = modelFactory.createButtonConfiguration();
      OHTFDevice<ButtonConfiguration, DualButtonButtonSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(DualButtonButtonSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.dualbutton_led.name())) {
      logger.debug("setting DualButtonLEDConfiguration device_type {}", deviceType);
      DualButtonLEDConfiguration configuration = modelFactory.createDualButtonLEDConfiguration();
      OHTFDevice<DualButtonLEDConfiguration, DualButtonLedSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(DualButtonLedSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.joystick_button.name())) {
      ButtonConfiguration configuration = modelFactory.createButtonConfiguration();
      OHTFDevice<ButtonConfiguration, JoystickSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(JoystickSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.lcd_button.name())) {
      ButtonConfiguration configuration = modelFactory.createButtonConfiguration();
      OHTFDevice<ButtonConfiguration, LCDButtonSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(LCDButtonSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_ledstrip.name())) {
      LEDStripConfiguration configuration = modelFactory.createLEDStripConfiguration();
      OHTFDevice<LEDStripConfiguration, NoSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.ledgroup.name())) {
      LEDGroupConfiguration configuration = modelFactory.createLEDGroupConfiguration();
      OHTFSubDeviceAdminDevice<LEDGroupConfiguration, NoSubIds> ohtfDevice =
          modelFactory.createOHTFSubDeviceAdminDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_ptc.name())) {
      TFPTCBrickletConfiguration configuration = modelFactory.createTFPTCBrickletConfiguration();
      OHTFDevice<TFPTCBrickletConfiguration, PTCSubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(PTCSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_industrialdual020ma.name())) {
      TFIndustrialDual020mAConfiguration configuration =
          modelFactory.createTFIndustrialDual020mAConfiguration();
      OHTFDevice<TFIndustrialDual020mAConfiguration, IndustrialDual020mASubIds> ohtfDevice =
          modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IndustrialDual020mASubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.dual_relay.name())) {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.debug("{} setting subdevice ids to {}", LoggerConstants.CONFIG,
          DualRelaySubIds.values());
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, DualRelaySubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(DualRelaySubIds.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.quad_relay.name())) {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.debug("{} setting subdevice ids to {}", LoggerConstants.CONFIG,
          IndustrialQuadRelayIDs.values());
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, IndustrialQuadRelayIDs> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IndustrialQuadRelayIDs.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.joystick_xposition.name())
        || deviceType.equals(TypeKey.joystick_yposition.name())) {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.debug("{} setting subdevice ids to {}", LoggerConstants.CONFIG,
          JoystickSubIds.values());
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, JoystickSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(JoystickSubIds.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.digital_4in.name())) {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.debug("{} setting subdevice ids to {}", LoggerConstants.CONFIG,
          IndustrialDigitalInSubIDs.values());
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, IndustrialDigitalInSubIDs> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IndustrialDigitalInSubIDs.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.digital_4out.name())) {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.debug("{} setting subdevice ids to {}", LoggerConstants.CONFIG,
          IndustrialDigitalOutSubIDs.values());
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, IndustrialDigitalOutSubIDs> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(IndustrialDigitalOutSubIDs.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    } else if (deviceType.equals(TypeKey.bricklet_temperature.name())) {
      TFTemperatureConfiguration configuration = modelFactory.createTFTemperatureConfiguration();
      OHTFDevice<TFTemperatureConfiguration, NoSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      ohtfDevice.setTfConfig(configuration);
      fillupConfig(ohtfDevice, deviceConfig);
    } else {
      logger.debug("{} setting no tfConfig device_type {}", LoggerConstants.CONFIG, deviceType);
      logger.trace("{} deviceType {}", LoggerConstants.CONFIG, deviceType);
      OHTFDevice<?, NoSubIds> ohtfDevice = modelFactory.createOHTFDevice();
      ohtfDevice.getSubDeviceIds().addAll(Arrays.asList(NoSubIds.values()));
      fillupConfig(ohtfDevice, deviceConfig);
    }
  }

  /**
   * Fills in the configuration into the configuration object and adds it to the {@link OHConfig}.
   * 
   * @param ohtfDevice The device specific configuration object {@link OHTFDevice}.
   * @param deviceConfig The device configuration as {@code Map} of {@code Strings}.
   * @throws ConfigurationException
   */
  private void fillupConfig(OHTFDevice<?, ?> ohtfDevice, Map<String, String> deviceConfig)
      throws ConfigurationException {
    String uid = deviceConfig.get(ConfigKey.uid.name());
    if (uid == null || uid.equals("")) {
      // das kommt hier gar nie an
      logger.error("===== uid missing");
      throw new ConfigurationException(deviceConfig.toString(),
          "config is an invalid missing uid: openhab.cfg has to be fixed!");
    } else {
      logger.debug("*** uid is \"{}\"", uid);
    }
    ohtfDevice.setUid(uid);
    String subid = deviceConfig.get(ConfigKey.subid.name());
    if (subid != null) {
      if (!ohtfDevice.isValidSubId(subid)) {
        throw new ConfigurationException(subid, String.format(
            "\"%s\" is an invalid subId: openhab.cfg has to be fixed!", subid));
      }
      logger.trace("fillupConfig ohtfDevice subid {}", subid);
      ohtfDevice.setSubid(subid);
    }
    if (ohConfig.getConfigByTFId(uid, subid) != null) {
      throw new ConfigurationException(String.format("uid: %s subId: %s", uid, subid),
          String.format(
              "%s: duplicate device config for uid \"%s\" and subId \"%s\": fix openhab.cfg",
              LoggerConstants.CONFIG, uid, subid));
    }
    String symbolicName = deviceConfig.get(ConfigKeyAdmin.ohId.name());
    if (ohConfig.getConfigByOHId(symbolicName) != null) {
      throw new ConfigurationException(String.format("symbolic name: %s", symbolicName),
          String.format("%s: duplicate device config for symbolic name \"%s\": fix openhab.cfg",
              LoggerConstants.CONFIG, symbolicName));
    }
    ohtfDevice.setOhid(symbolicName);

    EObject tfConfig = ohtfDevice.getTfConfig();
    EList<EStructuralFeature> features = null;
    if (tfConfig != null) {
      features = tfConfig.eClass().getEAllStructuralFeatures();
    }
    ArrayList<String> configKeyList = new ArrayList<String>();
    for (ConfigKeyAdmin configKey : ConfigKeyAdmin.values()) {
      configKeyList.add(configKey.toString());
    }
    for (String property : deviceConfig.keySet()) {
      if (configKeyList.contains(property)) {
        continue;
      } else {
        logger.trace("{} found  property {}", LoggerConstants.CONFIG, property);
      }

      if (features != null) {
        for (EStructuralFeature feature : features) {
          logger.trace("found feature: {}", feature.getName());
          if (feature.getName().equals(property)) {
            logger.trace("{} feature type {}", LoggerConstants.CONFIG, feature.getEType()
                .getInstanceClassName());
            logger.debug("configuring feature: {} for uid {} subid {}", feature.getName(), uid,
                subid);
            String className = feature.getEType().getInstanceClassName();
            if (className.equals("int") || className.equals("java.lang.Integer")) {
              tfConfig.eSet(feature, Integer.parseInt(deviceConfig.get(property)));
            } else if (className.equals("short") || className.equals("java.lang.Short")) {
              tfConfig.eSet(feature, Short.parseShort(deviceConfig.get(property)));
            } else if (className.equals("long") || className.equals("java.lang.Long")) {
              tfConfig.eSet(feature, Long.parseLong(deviceConfig.get(property)));
            } else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
              logger.debug("{} found boolean value", LoggerConstants.CONFIG);
              tfConfig.eSet(feature, Boolean.parseBoolean(deviceConfig.get(property)));
            } else if (className.equals("java.lang.String")) {
              logger.debug("{} found String value", LoggerConstants.CONFIG);
              tfConfig.eSet(feature, deviceConfig.get(property));
            } else if (className.equals("java.math.BigDecimal")) {
              logger.debug("{} found BigDecimal value", LoggerConstants.CONFIG);
              tfConfig.eSet(feature, new BigDecimal(deviceConfig.get(property)));
              // } else if (feature.getEType().getInstanceClassName().equals("EList")){
              // logger.debug("{} found EList value", LoggerConstants.CONFIG);
              // List<String> strings = new
              // ArrayList<String>(Arrays.asList(deviceConfig.get(property).trim().split("\\s+")));
              // tfConfig.eSet(feature, strings);
            } else {
              throw new ConfigurationException(feature.getName(),
                  "unsupported configuration type needed: " + className);
            }
            break;
          }
        }
      }
    }

    ohConfig.getOhTfDevices().add(ohtfDevice);
  }

  /**
   * Checks if the {@code deviceType} is known by the {@link Ecosystem}.
   * 
   * @param ohId The name of the device found in openhab.cfg as {@code String}.
   * @param deviceType The device type found in openhab.cfg as {@code String}.
   * @throws ConfigurationException
   */
  private void checkTfType(String ohId, String deviceType) throws ConfigurationException {
    ModelPackage modelPackage = ModelPackage.eINSTANCE;
    boolean deviceFound = false;
    for (EClassifier eClassifier : modelPackage.getEClassifiers()) {
      if (eClassifier instanceof EClass) {
        EList<EAttribute> attributes = ((EClass) eClassifier).getEAllAttributes();
        for (EAttribute attribute : attributes) {
          if (attribute.getName().equals("deviceType")) {
            if (attribute.getDefaultValueLiteral().equals(deviceType)) {
              deviceFound = true;
              break;
            }
          }
        }
      }
    }
    if (!deviceFound) {
      throw new ConfigurationException(ohId, "unknown device type: " + deviceType);
    }
  }

}
