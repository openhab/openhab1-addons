/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.driver.ModemDBEntry;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
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
	DeviceFeature m_feature = null; // related DeviceFeature
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

	
	public static class WarnCommandHandler extends CommandHandler {
		WarnCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			logger.warn("command {} is not implemented yet!", cmd);
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
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x11, (byte) 0xff);
					dev.enqueueMessage(m, m_feature);
					logger.info("LightOnOffCommandHandler: sent msg to switch {} on", dev.getAddress());
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("LightOnOffCommandHandler: sent msg to switch {} off", dev.getAddress());
				}
				// expect to get a direct ack after this!
			} catch (IOException e) {
				logger.error("command send i/o error: ", e);
			} catch (FieldException e) {
				logger.error("command send message creation error ", e);
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
					logger.info("IOLincOnOffCommandHandler: sent msg to switch {} on", dev.getAddress());
				} else if (cmd == OnOffType.OFF) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("IOLincOnOffCommandHandler: sent msg to switch {} off", dev.getAddress());
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
				logger.error("command send i/o error: ", e);
			} catch (FieldException e) {
				logger.error("command send message creation error: ", e);
			}
		}
	}

	public static class IncreaseDecreaseHandler extends CommandHandler {
		IncreaseDecreaseHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(InsteonPLMBindingConfig conf, Command cmd, InsteonDevice dev) {
			try {
				if (cmd == IncreaseDecreaseType.INCREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x15, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("IncreaseDecreaseHandler: sent msg to brighten {}", dev.getAddress());
				} else if (cmd == IncreaseDecreaseType.DECREASE) {
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x16, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("IncreaseDecreaseHandler: sent msg to dimm {}", dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("command send i/o error: ", e);
			} catch (FieldException e) {
				logger.error("command send message creation error ", e);
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
				int level = (pc.intValue()*255)/100;
				if (level > 0) { // make light on message with given level
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x11, (byte) level);
					dev.enqueueMessage(m, m_feature);
					logger.info("PercentHandler: sent msg to set {} to {}", dev.getAddress(), level);
				} else { // switch off
					Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x13, (byte) 0x00);
					dev.enqueueMessage(m, m_feature);
					logger.info("PercentHandler: sent msg to set {} to zero by switching off", dev.getAddress());
				}
			} catch (IOException e) {
				logger.error("command send i/o error: ", e);
			} catch (FieldException e) {
				logger.error("command send message creation error ", e);
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
				logger.debug("invalid remove address: {}", removeAddr);
				return;
			}
			InsteonAddress addr = new InsteonAddress(removeAddr);
			if (removeFromModem(addr)) {
				logger.debug("successfully removed device {} from modem db", addr);
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
						logger.info("wrote erase message: {}", m);
					}
				} else {
					logger.warn("address {} not found in modem database!", aAddr);
				}
			} catch (FieldException e) {
				logger.error("field exception: ", e);
			} catch (IOException e) {
				logger.error("i/o exception: ", e);
			} finally {
				driver.unlockModemDBEntries();
			}
			return removed;
		}
	}

	/**
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends CommandHandler> T s_makeHandler(String name, DeviceFeature f) {
		String cname = CommandHandler.class.getName() + "$" + name;
		try {
			Class<?> c = Class.forName(cname);
			@SuppressWarnings("unchecked")
			Class<? extends T> dc = (Class <? extends T>) c;
			return dc.getDeclaredConstructor(DeviceFeature.class).newInstance(f);
		} catch (Exception e) {
			logger.error("error trying to create message handler: {}", name, e);
		}
		return null;
	}
}



