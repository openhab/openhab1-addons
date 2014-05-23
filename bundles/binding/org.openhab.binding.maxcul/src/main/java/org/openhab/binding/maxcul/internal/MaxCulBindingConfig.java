package org.openhab.binding.maxcul.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.5.0
 */
public class MaxCulBindingConfig implements BindingConfig {
	MaxCulDevice deviceType;
	MaxCulFeature feature;
	String serialNumber;
	String dstAddr = null;
	boolean paired = false;

	private static final Logger logger =
			LoggerFactory.getLogger(MaxCulBindingConfig.class);

	MaxCulBindingConfig(String bindingConfig)
			throws BindingConfigParseException {
		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length == 1) {
			if (configParts[0] == "PairMode") {
				this.deviceType = MaxCulDevice.PAIR_MODE;
				return;
			}
		}

		if (configParts.length < 2) {
			throw new BindingConfigParseException(
					"MaxCul configuration requires a configuration of at least the format <device_type>:<serial_num> for a MAX! device.");
		}

		/* handle device type */
		switch (configParts[0]) {
		case "RadiatorThermostat":
			this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT;
			break;
		case "RadiatorThermostatPlus":
			this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT_PLUS;
			break;
		case "WallThermostat":
			this.deviceType = MaxCulDevice.WALL_THERMOSTAT;
			break;
		case "PushButton":
			this.deviceType = MaxCulDevice.PUSH_BUTTON;
			break;
		case "ShutterContact":
			this.deviceType = MaxCulDevice.SHUTTER_CONTACT;
			break;
		default:
			throw new BindingConfigParseException(
					"Invalid device type. Use RadiatorThermostat / RadiatorThermostatPlus / WallThermostat / PushButton / ShutterContact");
		}

		/* handle serial number */
		this.serialNumber = configParts[1];

		/* handle feature if set */
		if (configParts.length > 2) {
			switch (configParts[2]) {
			case "thermostat":
				if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
						|| this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						|| this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'thermostat' on radiator or wall thermostats");
				this.feature = MaxCulFeature.THERMOSTAT;
				break;
			case "temperature":
				if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
						|| this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						|| this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats");
				this.feature = MaxCulFeature.TEMPERATURE;
				break;
			case "battery":
				this.feature = MaxCulFeature.BATTERY;
				break;
			case "mode":
				if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
						|| this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						|| this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats");
				this.feature = MaxCulFeature.MODE;
				break;
			case "switch":
				if (this.deviceType != MaxCulDevice.PUSH_BUTTON
						|| this.deviceType != MaxCulDevice.SHUTTER_CONTACT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'switch' on PushButton or ShutterContact");
				this.feature = MaxCulFeature.TEMPERATURE;
				break;
			}
		} else {
			/* use defaults - handle all device types */
			switch (this.deviceType) {
			case PUSH_BUTTON:
				this.feature = MaxCulFeature.SWITCH;
				break;
			case RADIATOR_THERMOSTAT:
				this.feature = MaxCulFeature.THERMOSTAT;
				break;
			case RADIATOR_THERMOSTAT_PLUS:
				this.feature = MaxCulFeature.THERMOSTAT;
				break;
			case SHUTTER_CONTACT:
				this.feature = MaxCulFeature.SWITCH;
				break;
			case WALL_THERMOSTAT:
				this.feature = MaxCulFeature.THERMOSTAT;
				break;
			case PAIR_MODE:
				break;
			}
		}
		/* load stored configuration from pairing (if present) except on pair mode switch binding */
		if (this.deviceType != MaxCulDevice.PAIR_MODE)
			this.loadStoredConfig();
	}

	private String generateConfigFilename()
	{
		String base = "etc/maxcul";
		String filename = String.format("%s/%s.properties", base, this.serialNumber);
		return filename;
	}

	/**
	 * Load the stored configuration information if it exists. This information
	 * is established during the pairing process.
	 */
	void loadStoredConfig()
	{
		File cfgFile = new File(generateConfigFilename());

		if (cfgFile.exists())
		{
			try {
				FileInputStream fiStream = new FileInputStream(cfgFile);
				Properties propertiesFile = new Properties();
				propertiesFile.load(fiStream);

				this.dstAddr = propertiesFile.getProperty("dstAddr");
				this.paired = true;

				fiStream.close();
			} catch (IOException e) {
				logger.warn("Unable to load information for "+this.deviceType+" "+this.serialNumber+" it may not yet be paired. Error was "+e.getMessage());
				this.paired = false;
				return;
			}
		} else {
			logger.warn("Unable to locate information for "+this.deviceType+" "+this.serialNumber+" it may not yet be paired");
			this.paired = false;
		}
	}

	/**
	 * Save the stored configuration information. Will update it if it already exists.
	 * The information is primarily established during the pairing process.
	 */
	void saveStoredConfig()
	{
		if (this.paired)
		{
			File cfgFile = new File(generateConfigFilename());

			if (!cfgFile.exists())
			{
				try {
					cfgFile.mkdirs();
					cfgFile.createNewFile();
				} catch (IOException e) {
					logger.warn("Unable to create new properties file for "+this.deviceType+" "+this.serialNumber+". Data won't be saved so pairing will be lost. Error was "+e.getMessage());
					return;
				}
			}

			Properties propertiesFile = new Properties();
			propertiesFile.setProperty("dstAddr", this.dstAddr);

			try {
				FileOutputStream foStream = new FileOutputStream(cfgFile);
				Date updateTime = new Date();
				propertiesFile.store(foStream, "Autogenerated by MaxCul binding on "+updateTime.toString());
				this.paired = true;
				foStream.close();
			} catch (IOException e) {
				logger.warn("Unable to load information for "+this.deviceType+" "+this.serialNumber+" it may not yet be paired. Error was "+e.getMessage());
				this.paired = false;
				return;
			}
		} else logger.error("Tried saving configuration for "+this.serialNumber+" which is not paired.");
	}
}
