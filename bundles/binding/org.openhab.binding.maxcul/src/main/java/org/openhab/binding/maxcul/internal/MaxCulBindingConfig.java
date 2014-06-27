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
 * Class to hold configuration for binding
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class MaxCulBindingConfig implements BindingConfig {
	public MaxCulDevice deviceType;
	public MaxCulFeature feature;
	public String serialNumber;
	public String devAddr = null;
	public boolean paired = false;

	private final String CONFIG_PROPERTIES_BASE = "etc/maxcul";

	private static final Logger logger =
			LoggerFactory.getLogger(MaxCulBindingConfig.class);

	MaxCulBindingConfig(String bindingConfig)
			throws BindingConfigParseException {
		String[] configParts = bindingConfig.trim().split(":");

		if (bindingConfig.startsWith("PairMode"))
		{
			logger.debug("Pair Mode switch found");
			this.deviceType = MaxCulDevice.PAIR_MODE;
			return;
		}
		else if (bindingConfig.startsWith("ListenMode"))
		{
			logger.debug("Listen Mode switch found");
			this.deviceType = MaxCulDevice.LISTEN_MODE;
			return;
		}
		else if (configParts.length < 2) {
			throw new BindingConfigParseException(
					"MaxCul configuration requires a configuration of at least the format <device_type>:<serial_num> for a MAX! device.");
		}
		else
		{

			logger.debug("Found real device");
			/* handle device type */
			logger.debug("Part 0/"+(configParts.length-1)+" -> "+configParts[0]);
			if (configParts[0].compareTo("RadiatorThermostat") == 0){
				this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT;
			} else if (configParts[0].compareTo("RadiatorThermostatPlus") == 0) {
				this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT_PLUS;
			} else if (configParts[0].compareTo("WallThermostat") == 0) {
				this.deviceType = MaxCulDevice.WALL_THERMOSTAT;
			} else if (configParts[0].compareTo("PushButton") == 0) {
				this.deviceType = MaxCulDevice.PUSH_BUTTON;
			} else if (configParts[0].compareTo("ShutterContact") == 0) {
				this.deviceType = MaxCulDevice.SHUTTER_CONTACT;
			} else {
				throw new BindingConfigParseException(
						"Invalid device type. Use RadiatorThermostat / RadiatorThermostatPlus / WallThermostat / PushButton / ShutterContact");
			}

			/* handle serial number */
			logger.debug("Part 1/"+(configParts.length-1)+" -> "+configParts[1]);
			this.serialNumber = configParts[1];

			/* handle feature if set */
			if (configParts.length > 2) {
				logger.debug("Part 2/"+(configParts.length-1)+" -> "+configParts[2]);
				if (configParts[2].compareTo("thermostat") == 0) {
					if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
							&& this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
							&& this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
						throw new BindingConfigParseException(
								"Invalid device feature. Can only use 'thermostat' on radiator or wall thermostats. This is a "+this.deviceType);
					this.feature = MaxCulFeature.THERMOSTAT;
				} else if (configParts[2].compareTo("temperature") == 0) {
					if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
							&& this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
							&& this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
						throw new BindingConfigParseException(
								"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats. This is a "+this.deviceType);
					this.feature = MaxCulFeature.TEMPERATURE;
				} else if (configParts[2].compareTo("battery") == 0) {
					this.feature = MaxCulFeature.BATTERY;
				} else if (configParts[2].compareTo("mode") == 0) {
					if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT
							&& this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
							&& this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
						throw new BindingConfigParseException(
								"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats. This is a "+this.deviceType);
					this.feature = MaxCulFeature.MODE;
				} else if (configParts[2].compareTo("switch") == 0) {
					if (this.deviceType != MaxCulDevice.PUSH_BUTTON
							&& this.deviceType != MaxCulDevice.SHUTTER_CONTACT)
						throw new BindingConfigParseException(
								"Invalid device feature. Can only use 'switch' on PushButton or ShutterContact. This is a "+this.deviceType);
					this.feature = MaxCulFeature.TEMPERATURE;
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
				case LISTEN_MODE:
					break;
				}
			}
			/* load stored configuration from pairing (if present) except on pair mode switch binding */
			if (this.deviceType != MaxCulDevice.PAIR_MODE)
				this.loadStoredConfig();
		}
	}

	void setPairedInfo(String dstAddr)
	{
		this.devAddr = dstAddr;
		this.paired = true;
		saveStoredConfig();
	}

	private String generateConfigFilename()
	{
		String base = CONFIG_PROPERTIES_BASE;
		String filename = String.format("%s/%s.properties", base, this.serialNumber);
		return filename;
	}

	/**
	 * Load the stored configuration information if it exists. This information
	 * is established during the pairing process.
	 */
	public void loadStoredConfig()
	{
		File cfgFile = new File(generateConfigFilename());

		if (cfgFile.exists())
		{
			try {
				FileInputStream fiStream = new FileInputStream(cfgFile);
				Properties propertiesFile = new Properties();
				propertiesFile.load(fiStream);

				this.devAddr = propertiesFile.getProperty("devAddr");
				this.paired = true;

				fiStream.close();
			} catch (IOException e) {
				logger.warn("Unable to load information for "+this.deviceType+" "+this.serialNumber+" it may not yet be paired. Error was "+e.getMessage());
				this.paired = false;
				return;
			}
			logger.debug("Successfully loaded pairing info for "+this.serialNumber);
		} else {
			logger.warn("Unable to locate information for "+this.deviceType+" "+this.serialNumber+" it may not yet be paired");
			this.paired = false;
		}
	}

	/**
	 * Save the stored configuration information. Will update it if it already exists.
	 * The information is primarily established during the pairing process.
	 */
	private void saveStoredConfig()
	{
		if (this.paired)
		{
			File cfgFile = new File(generateConfigFilename());
			File cfgDir = new File( CONFIG_PROPERTIES_BASE );

			if (!cfgFile.exists())
			{
				try {
					if (!cfgDir.exists()) cfgDir.mkdirs();
					cfgFile.createNewFile();
				} catch (IOException e) {
					logger.warn("Unable to create new properties file for "+this.deviceType+" "+this.serialNumber+". Data won't be saved so pairing will be lost. Error was "+e.getMessage());
					return;
				}
			}

			Properties propertiesFile = new Properties();
			propertiesFile.setProperty("devAddr", this.devAddr);

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
			logger.debug("Successfully wrote pairing info for "+this.serialNumber);
		} else logger.error("Tried saving configuration for "+this.serialNumber+" which is not paired.");
	}
}
