/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.insteonplm.InsteonPLMBindingConfig;
import org.openhab.binding.insteonplm.internal.device.DeviceFeatureListener.StateChangeType;
import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.driver.ModemDBEntry;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command handler translates an openHAB command into a insteon message
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public abstract class CommandHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
	DeviceFeature			m_feature = null; // related DeviceFeature
	HashMap<String, String> m_parameters  = new HashMap<String, String>();
	/**
	 * Constructor
	 * @param feature The DeviceFeature for which this command was intended.
	 * The openHAB commands are issued on an openhab item. The .items files bind
	 * an openHAB item to a DeviceFeature.
	 */
	CommandHandler(DeviceFeature feature) {
		m_feature = feature;
	}	
	/**
	 * Implements what to do when an openHAB command is received
	 * @param config the configuration for the item that generated the command
	 * @param cmd the openhab command issued
	 * @param device the Insteon device to which this command applies
	 */
	public abstract void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice device);

	/**
	 * Returns parameter as integer
	 * @param key key of parameter
	 * @param def default
	 * @return integer value of parameter
	 */
	protected int getIntParameter(String key, int def) {
		try {
			if (m_parameters.get(key) != null) {
				return Integer.parseInt(m_parameters.get(key));
			}
		} catch (NumberFormatException e) {
			logger.error("malformed int parameter in command handler: {}", key);
		}
		return def;
	}
	/**
	 * Shorthand to return class name for logging purposes
	 * @return name of the class
	 */
	protected String nm() {
		return (this.getClass().getSimpleName());
	}
	
	protected int getMaxLightLevel(InsteonPLMBindingConfig conf, int defaultLevel) {
		HashMap<String, String> params = conf.getParameters();
		if (conf.getFeature().contains("dimmer") && params.containsKey("dimmermax")) {
			String item = conf.getItemName();
			String dimmerMax = params.get("dimmermax");
			try {
				int i = Integer.parseInt(dimmerMax);
				if (i > 1 && i <= 99) {
					int level = (int) Math.ceil((i * 255.0) / 100); // round up
					if (level < defaultLevel) {
						logger.info("item {}: using dimmermax value of {}", item, dimmerMax);
						return level;
					}
				} else {
					logger.error("item {}: dimmermax must be between 1-99 inclusive: {}", item, dimmerMax);
				}
			} catch (NumberFormatException e) {
				logger.error("item {}: invalid int value for dimmermax: {}", item, dimmerMax);
			}
		}

		return defaultLevel;
	}
	
	void setParameters(HashMap<String, String> hm) { m_parameters = hm; }
	
	/**
	 * Helper function to extract the group parameter from the binding config,
	 * @param c the binding configuration to test
	 * @return the value of the "group" parameter, or -1 if none
	 */
	protected static int s_getGroup(InsteonPLMBindingConfig c) {
		String v = c.getParameters().get("group");
		int iv = -1;
		try {
			iv = (v == null) ? -1 : Integer.parseInt(v);
		} catch (NumberFormatException e) {
			logger.error("malformed int parameter in for item {}", c.getItemName());
		}
		return iv;
	}
	
	public static class WarnCommandHandler extends CommandHandler {
		WarnCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			logger.warn("{}: command {} is not implemented yet!", nm(), cmd);
		}
	}

	public static class NoOpCommandHandler extends CommandHandler {
		NoOpCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			// do nothing, not even log
		}
	}

	public static class LightOnOffCommandHandler extends CommandHandler {
		LightOnOffCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == OnOffType.ON) {
					int level = getMaxLightLevel(conf, 0xff);
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x11, (byte) level,
								s_getGroup(conf));
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} to {}", nm(), dev.getAddress(),
							level == 0xff ? "on" : level);
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00,
									s_getGroup(conf));
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} off", nm(), dev.getAddress());
				}
				// expect to get a direct ack after this!
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	/**
	 * This Handler was supposed to set the LEDs of the 2487S, but it doesn't work.
	 * The parameters were modeled after the 2486D, it may work for that one,
	 * leaving it in for now.
	 * 
	 * From the HouseLinc PLM traffic log, the following commands (in the D2 data field)
	 * of the 2486D are supported:
	 * 
	 * 0x02: LED follow mask may work or not
	 * 0x03: LED OFF mask
	 * 0x04: X10 addr setting
	 * 0x05: ramp rate
	 * 0x06: on Level for button
	 * 0x07: global LED brightness (could not see any effect during testing)
	 * 0x0B: set nontoggle on/off command
	 *
	 * crucially, the 0x09 command does not work (NACK from device)
	 * @author Bernd Pfrommer
	 */
	public static class LEDOnOffCommandHandler extends CommandHandler {
		LEDOnOffCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				int button = this.getIntParameter("button", -1);
				if (cmd == OnOffType.ON) {
					Msg m = dev.makeExtendedMessage((byte) 0x1f, (byte)0x2e,  (byte)0x00);
					m.setByte("userData1", (byte)button);
					  // set command for 2486D, does not work for 2487S. Putting a 0x00 here will send query
					m.setByte("userData2", (byte) 0x09);
					m.setByte("userData3", (byte) 0x01);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} on", nm(), dev.getAddress());
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeExtendedMessage((byte) 0x1f, (byte)0x2e,  (byte)0x00);
					m.setByte("userData1", (byte)button);
					m.setByte("userData2", (byte) 0x09);
					m.setByte("userData3", (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} off", nm(), dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	public static class X10OnOffCommandHandler extends CommandHandler {
		X10OnOffCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				byte houseCode = dev.getX10HouseCode();
				byte houseUnitCode = (byte) (houseCode << 4 | dev.getX10UnitCode());
				if (cmd == OnOffType.ON || cmd == OnOffType.OFF) {
					byte houseCommandCode = (byte) (houseCode << 4 | (cmd == OnOffType.ON ?
							X10.Command.ON.code() : X10.Command.OFF.code()));
				Msg munit = dev.makeX10Message(houseUnitCode,(byte)0x00); // send unit code
				dev.enqueueMessage(munit, m_feature);
				Msg mcmd = dev.makeX10Message(houseCommandCode,(byte)0x80); // send command code
				dev.enqueueMessage(mcmd, m_feature);
					String onOff = cmd == OnOffType.ON ? "ON" : "OFF";
					logger.info("{}: sent msg to switch {} {}", nm(), dev.getAddress(), onOff);
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}

	public static class X10PercentCommandHandler extends CommandHandler {
		X10PercentCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				//
				// I did not have hardware that would respond to the PRESET_DIM codes.
				// This code path needs testing.
				//
				byte houseCode = dev.getX10HouseCode();
				byte houseUnitCode = (byte) (houseCode << 4 | dev.getX10UnitCode());
				Msg munit = dev.makeX10Message(houseUnitCode, (byte)0x00); // send unit code
				dev.enqueueMessage(munit, m_feature);
				PercentType pc = (PercentType)cmd;
				logger.debug("{}: changing level of {} to {}", nm(), dev.getAddress(), pc.intValue());
				int level = (pc.intValue() * 32) / 100;
				byte cmdCode = (level >= 16) ?
							X10.Command.PRESET_DIM_2.code() : X10.Command.PRESET_DIM_1.code();
				level = level % 16;
				if (level <= 0) level = 0;
				houseCode = (byte) s_X10CodeForLevel[level];
				cmdCode |= (houseCode << 4);
				Msg mcmd = dev.makeX10Message(cmdCode,(byte)0x80); // send command code
				dev.enqueueMessage(mcmd, m_feature);
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
		static private final int [] s_X10CodeForLevel = {0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15};
	}

	public static class X10IncreaseDecreaseCommandHandler extends CommandHandler {
		X10IncreaseDecreaseCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				byte houseCode = dev.getX10HouseCode();
				byte houseUnitCode = (byte) (houseCode << 4 | dev.getX10UnitCode());
				if (cmd == IncreaseDecreaseType.INCREASE || cmd == IncreaseDecreaseType.DECREASE) {
					byte houseCommandCode = (byte) (houseCode << 4 |
								(cmd == IncreaseDecreaseType.INCREASE ?
										X10.Command.BRIGHT.code() : X10.Command.DIM.code()));
					Msg munit = dev.makeX10Message(houseUnitCode,(byte)0x00); // send unit code
					dev.enqueueMessage(munit, m_feature);
					Msg mcmd = dev.makeX10Message(houseCommandCode,(byte)0x80); // send command code
					dev.enqueueMessage(mcmd, m_feature);
					String bd = cmd == IncreaseDecreaseType.INCREASE ? "BRIGHTEN" : "DIM";
					logger.info("{}: sent msg to switch {} {}", nm(), dev.getAddress(), bd);
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}

	public static class IOLincOnOffCommandHandler extends CommandHandler {
		IOLincOnOffCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == OnOffType.ON) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x11, (byte) 0xff);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} on", nm(), dev.getAddress());
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} off", nm(), dev.getAddress());
				}
				// This used to be configurable, but was made static to make
				// the architecture of the binding cleaner.
				int delay = 2000;
				delay = Math.max(1000, delay);
				delay = Math.min(10000, delay);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Msg m = m_feature.makePollMsg();
						InsteonDevice dev = m_feature.getDevice();
						if (m != null) dev.enqueueMessage(m, m_feature);
					}
				}, delay);
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error: ", nm(), e);
			}
		}
	}

	public static class IncreaseDecreaseCommandHandler extends CommandHandler {
		IncreaseDecreaseCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == IncreaseDecreaseType.INCREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x15, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to brighten {}", nm(), dev.getAddress());
				} else if (cmd == IncreaseDecreaseType.DECREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x16, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to dimm {}", nm(), dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	public static class PercentHandler extends CommandHandler {
		PercentHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				PercentType pc = (PercentType)cmd;
				logger.debug("changing level of {} to {}", dev.getAddress(), pc.intValue());
				int level = (int) Math.ceil((pc.intValue() * 255.0) / 100); // round up
				if (level > 0) { // make light on message with given level
					level = getMaxLightLevel(conf, level);
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x11, (byte) level);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to set {} to {}", nm(), dev.getAddress(), level);
				} else { // switch off
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to set {} to zero by switching off", nm(), dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}

	public static class PowerMeterCommandHandler extends CommandHandler {
		PowerMeterCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			String cmdParam = conf.getParameter("cmd");
			if (cmdParam == null) {
				logger.error("{} ignoring cmd {} because no cmd= is configured!", nm(), cmd);
				return;
			}
			try {
				if (cmd == OnOffType.ON) {
					if (cmdParam.equals("reset")) {
						Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x80, (byte) 0x00);
						dev.enqueueMessage(m, m_feature);
						logger.info("{}: sent reset msg to power meter {}", nm(), dev.getAddress());
						m_feature.publish(OnOffType.OFF, StateChangeType.ALWAYS, "cmd", "reset");
					} else if (cmdParam.equals("update")) {
						Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x82, (byte) 0x00);
						dev.enqueueMessage(m, m_feature);
						logger.info("{}: sent update msg to power meter {}", nm(), dev.getAddress());
						m_feature.publish(OnOffType.OFF, StateChangeType.ALWAYS, "cmd", "update");
					} else {
						logger.error("{}: ignoring unknown cmd {} for power meter {}", nm(), cmdParam, dev.getAddress());
					}
				} else if (cmd == OnOffType.OFF) {
					logger.info("{}: ignoring off request for power meter {}", nm(), dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}

	public static class ModemCommandHandler extends CommandHandler {
		ModemCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice modem) {
			if (!(cmd instanceof OnOffType) || ((OnOffType) cmd) != OnOffType.ON) return;
			String removeAddr = conf.getParameters().get("remove_address");
			if (!InsteonAddress.s_isValid(removeAddr)) {
				logger.debug("{}: invalid remove address: {}", nm(), removeAddr);
				return;
			}
			InsteonAddress addr = new InsteonAddress(removeAddr);
			if (removeFromModem(addr)) {
				logger.debug("{} successfully removed device {} from modem db", nm(), addr);
			}
		}

		boolean removeFromModem(InsteonAddress aAddr) {
			boolean removed = false;
			Driver driver = m_feature.getDevice().getDriver();
			try {
				HashMap<InsteonAddress, ModemDBEntry> dbes = driver.lockModemDBEntries();
				ModemDBEntry dbe = dbes.get(aAddr);
				if (dbe != null) {
					for (Msg lr : dbe.getLinkRecords()) {
						Msg m = Msg.s_makeMessage("ManageALLLinkRecord");
						m.setByte("controlCode", (byte)0x80);
						m.setByte("recordFlags", (byte)0x00);
						m.setByte("ALLLinkGroup", lr.getByte("ALLLinkGroup"));
						m.setAddress("linkAddress", aAddr);
						m.setByte("linkData1", (byte)0x00);
						m.setByte("linkData2", (byte)0x00);
						m.setByte("linkData3", (byte)0x00);
						dbe.getPort().writeMessage(m);
						removed = true;
						logger.info("{}: wrote erase message: {}", nm(), m);
					}
				} else {
					logger.warn("{}: address {} not found in modem database!", nm(), aAddr);
				}
			} catch (FieldException e) {
				logger.error("{}: field exception: ", nm(), e);
			} catch (IOException e) {
				logger.error("{}: i/o exception: ", nm(), e);
			} finally {
				driver.unlockModemDBEntries();
			}
			return removed;
		}
	}

	/**
	 * Method for setting Thermostat mode to Heat (Off) or Cool (On)
	 * @param name the name of the handler to create
	 * @param params 
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static class HeatCoolCommandHandler extends CommandHandler {
		HeatCoolCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == OnOffType.ON) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x05,
								s_getGroup(conf));
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} to COOL", nm(), dev.getAddress());
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x04,
								s_getGroup(conf));
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch {} to HEAT", nm(), dev.getAddress());
				} 
				// expect to get a direct ack after this!
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}	
	
	/**
	 * Method for setting Thermostat mode to Heat, Cool or Auto
	 */
	public static class ThermostatModeControlCommandHandler extends CommandHandler {
		ThermostatModeControlCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				//String test = ((StringType)cmd).toString();
				int dc = ((DecimalType)cmd).intValue();
				Msg m = null;
				switch (dc) {
				case 1:            //Set AC mode to COOL
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x05);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C mode to COOL", nm());
					m = null;
					break;
				case 2:            //Set AC mode to HEAT
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x04);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C mode to HEAT", nm());
					m = null;
					break;
				case 3:            //Set AC mode to AUTO MANUAL
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x06);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C mode to AUTO MANUAL", nm());
					m = null;
					break;
				default:
					break;
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	/**
	 * Method for setting Thermostat fan to ON, AUTO or OFF
	 */
	public static class ThermostatFanControlCommandHandler extends CommandHandler {
		ThermostatFanControlCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				int dc = ((DecimalType)cmd).intValue();
				Msg m = null;
				switch (dc) {
				case 1:            //Turn ALL Off
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x09);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C OFF", nm());
					m = null;
					break;
				case 2:            //Turn FAN ON
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x07);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C fan ON", nm());
					m = null;
					break;
				case 3:            //Turn fan AUTO
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6b, (byte) 0x08);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to turn A/C fan AUTO", nm());
					m = null;
					break;
				default:
					break;
				}
				// expect to get a direct ack after this!
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}

	/**
	 * Method for setting Thermostat Master controller  -  NOT TESTED
	 */
	public static class ThermostatMasterControlCommandHandler extends CommandHandler {
		ThermostatMasterControlCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				int dc = ((DecimalType)cmd).intValue();
				Msg m = null;
				int checksum = 0;
				switch (dc) {
				case 1:            //Set primary thermostat to MASTER
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x2e, (byte) 0x00);
					m.setByte("userData1", (byte) 0x00);
					m.setByte("userData2", (byte) 0x09);
					m.setByte("userData3", (byte) 0x00);
					
					checksum = (~(0x2e + 0x09) + 1) &0xff;
					m.setByte("userData14", (byte)checksum);

					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch PRIMARY Thermostat to MASTER", nm());
					m = null;
					break;
				case 2:            //Set 2nd Thermostat to MASTER
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x2e, (byte) 0x00);
					m.setByte("userData1", (byte) 0x00);
					m.setByte("userData2", (byte) 0x09);
					m.setByte("userData3", (byte) 0x01);
					
					checksum = (~(0x2e + 0x09 + 0x01) + 1) &0xff;
					m.setByte("userData14", (byte)checksum);

					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch SECONDARY Thermostat to MASTER", nm());
					m = null;
					break;
				case 3:            //Set 3rd Thermostat to MASTER
					m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x2e, (byte) 0x00);
					m.setByte("userData1", (byte) 0x00);
					m.setByte("userData2", (byte) 0x09);
					m.setByte("userData3", (byte) 0x02);
					
					checksum = (~(0x2e + 0x09 + 0x02) + 1) &0xff;
					m.setByte("userData14", (byte)checksum);

					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sent msg to switch TERTIARY Thermostat to MASTER", nm());
					m = null;
					break;
				default:
					break;
				}
				// expect to get a direct ack after this!
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	/* This Command Handler has been tested but is not implemented.  
	 * Increments or decrements cool setpoint by 1 
	 **/
	public static class ThermostatSetPointCommandHandler extends CommandHandler {
		ThermostatSetPointCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == IncreaseDecreaseType.INCREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x15, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sending command for {} to INCREASE Setpoint by 1.", nm(), m_feature.getName());
				} else if (cmd == IncreaseDecreaseType.DECREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x16, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("{}: sending command for {} to DECREASE Setpoint by 1.", nm(), m_feature.getName());
				} else {
					// DO NOTHING
				}
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	public static class ThermostatCoolSetPointCommandHandler extends CommandHandler {
		ThermostatCoolSetPointCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				byte level = (byte) (((DecimalType)cmd).intValue() * 2);
				Msg m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6c, level);
				dev.enqueueMessage(m, m_feature);
				logger.info("{}: sent msg to change Cool SetPoint to {}", nm(), ((DecimalType)cmd).intValue());
				m = null;
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	public static class ThermostatHeatSetPointCommandHandler extends CommandHandler {
		ThermostatHeatSetPointCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				byte level = (byte) (((DecimalType)cmd).intValue() * 2);
				Msg m = dev.makeExtendedMessage((byte) 0x0f, (byte) 0x6d, level);
				dev.enqueueMessage(m, m_feature);
				logger.info("{}: sent msg to change Heat SetPoint to {}", nm(), ((DecimalType)cmd).intValue());
				m = null;
			} catch (IOException e) {
				logger.error("{}: command send i/o error: ", nm(), e);
			} catch (FieldException e) {
				logger.error("{}: command send message creation error ", nm(), e);
			}
		}
	}
	
	/**
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param params 
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends CommandHandler> T s_makeHandler(String name, HashMap<String, String> params, DeviceFeature f) {
		String cname = CommandHandler.class.getName() + "$" + name;
		try {
			Class<?> c = Class.forName(cname);
			@SuppressWarnings("unchecked")
			Class<? extends T> dc = (Class <? extends T>) c;
			T ch = dc.getDeclaredConstructor(DeviceFeature.class).newInstance(f);
			ch.setParameters(params);
			return ch;
		} catch (Exception e) {
			logger.error("error trying to create message handler: {}", name, e);
		}
		return null;
	}
}



