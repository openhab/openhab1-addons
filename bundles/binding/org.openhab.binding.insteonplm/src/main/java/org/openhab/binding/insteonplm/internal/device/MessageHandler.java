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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.openhab.binding.insteonplm.internal.device.DeviceFeatureListener.StateChangeType;
import org.openhab.binding.insteonplm.internal.device.GroupMessageStateMachine.GroupMessage;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgType;
import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A message handler processes incoming Insteon messages and reacts by publishing
 * corresponding messages on the openhab bus, updating device state etc.
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public abstract class MessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	
	DeviceFeature				m_feature	 	= null;
	HashMap<String, String> 	m_parameters	= new HashMap<String, String>();
	HashMap<Integer, GroupMessageStateMachine>	m_groupState = new HashMap<Integer, GroupMessageStateMachine>();
	/**
	 * Constructor
	 * @param p state publishing object for dissemination of state changes
	 */
	MessageHandler(DeviceFeature p) {
		m_feature = p;
	}
	/**
	 * Method that processes incoming message. The cmd1 parameter
	 * has been extracted earlier already (to make a decision which message handler to call),
	 * and is passed in as an argument so cmd1 does not have to be extracted from the message again.
	 * @param group all-link group or -1 if not specified
	 * @param cmd1 the insteon cmd1 field
	 * @param msg the received insteon message
	 * @param feature the DeviceFeature to which this message handler is attached
	 * @param fromPort the device (/dev/ttyUSB0) from which the message has been received
	 */
	public abstract void handleMessage(int group, byte cmd1, Msg msg,
			DeviceFeature feature, String fromPort);
	
	/**
	 * Method to send an extended insteon message for querying a device
	 * @param f		DeviceFeature that is being currently handled
	 * @param aCmd1	cmd1 for message to be sent
	 * @param aCmd2	cmd2 for message to be sent
	 */
	public void sendExtendedQuery(DeviceFeature f, byte aCmd1, byte aCmd2) {
		InsteonDevice d = f.getDevice();
		try {
			Msg m = d.makeExtendedMessage((byte)0x1f, aCmd1, aCmd2);
			m.setQuietTime(500L);
			d.enqueueMessage(m, f);
		} catch (IOException e) {
			logger.warn("i/o problem sending query message to device {}", d.getAddress());
		} catch (FieldException e) {
			logger.warn("field exception sending query message to device {}", d.getAddress());
		}
	}
	/**
	 * Check if group matches
	 * @param group group to test for
	 * @return true if group matches or no group is specified
	 */
	public boolean  matchesGroup(int group) {
		int g = getIntParameter("group", -1);
		return (g == -1 || g == group);
	}
	/**
	 * Retrieve group parameter or -1 if no group is specified
	 * @return group parameter
	 */
	public int getGroup() {
		return (getIntParameter("group", -1));
	}
	/**
	 * Helper function to get an integer parameter for the handler
	 * @param key name of the int parameter (as specified in device features!)
	 * @param def default to return if parameter not found
	 * @return value of int parameter (or default if not found)
	 */
	protected int getIntParameter(String key, int def) {
		String val = m_parameters.get(key);
		if (val == null) return (def); // param not found
		int ret = def;
		try {
			ret = Utils.strToInt(val);
		} catch (NumberFormatException e) {
			logger.error("malformed int parameter in message handler: {}", key);
		}
		return ret;
	}
	/**
	 * Helper function to get a String parameter for the handler
	 * @param key name of the String parameter (as specified in device features!)
	 * @param def default to return if parameter not found
	 * @return value of parameter (or default if not found)
	 */
	protected String getStringParameter(String key, String def) {
		return (m_parameters.get(key) == null ? def : m_parameters.get(key));
	}
	/**
	 * Helper function to get a double parameter for the handler
	 * @param key name of the parameter (as specified in device features!)
	 * @param def default to return if parameter not found
	 * @return value of parameter (or default if not found)
	 */
	protected double getDoubleParameter(String key, double def) {
		try {
			if (m_parameters.get(key) != null) {
				return Double.parseDouble(m_parameters.get(key));
			}
		} catch (NumberFormatException e) {
			logger.error("malformed int parameter in message handler: {}", key);
		}
		return def;
	}
	
	/**
	 * Test if message refers to the button configured for given feature
	 * @param msg received message
	 * @param f device feature to test
	 * @return true if we have no button configured or the message is for this button
	 */
	protected boolean isMybutton(Msg msg, DeviceFeature f) {
		int myButton = getIntParameter("button", -1);
		// if there is no button configured for this handler
		// the message is assumed to refer to this feature
		// no matter what button is addressed in the message
		if (myButton == -1) return true;
		
		int button = getButtonInfo(msg, f);
		return button != -1 && myButton == button;
	}
			
	/**
	 * Test if parameter matches value
	 * @param param name of parameter to match
	 * @param msg message to search
	 * @param field field name to match
	 * @return true if parameter matches
	 * @throws FieldException if field not there
	 */
	protected boolean testMatch(String param, Msg msg, String field) throws FieldException {
		int mp = getIntParameter(param, -1);
		// parameter not filtered for, declare this a match!
		if (mp == -1) return (true);
		byte value = msg.getByte(field);
		return ((int)value == mp);
	}

	/**
	 * Test if message matches the filter parameters
	 * @param msg message to be tested against
	 * @return true if message matches
	 */
	public boolean matches(Msg msg) {
		try {
			int ext = getIntParameter("ext", -1);
			if (ext != -1) {
				if ((msg.isExtended() && ext != 1) ||
						(!msg.isExtended() && ext != 0)) {
					return (false);
				}
				if (!testMatch("match_cmd1", msg, "command1")) {
					return (false);
				}
			}
			if (!testMatch("match_cmd2", msg, "command2")) return (false);
			if (!testMatch("match_d1", msg, "userData1")) return (false);
			if (!testMatch("match_d2", msg, "userData2")) return (false);
			if (!testMatch("match_d3", msg, "userData3")) return (false);
		} catch (FieldException e) {
			logger.error("error matching message: {}", msg, e);
			return (false);
		}
		return (true);
	}

	/**
	 * Determines is an incoming ALL LINK message is a duplicate
	 * @param msg the received ALL LINK message
	 * @return true if this message is a duplicate
	 */
	protected boolean isDuplicate(Msg msg) {
		boolean isDuplicate = false;
		try {
			MsgType t = MsgType.s_fromValue(msg.getByte("messageFlags"));
			int hops = msg.getHopsLeft();
			if (t == MsgType.ALL_LINK_BROADCAST) {
				int group = (int) (msg.getAddress("toAddress").getLowByte() & 0xff);
				byte cmd1 = msg.getByte("command1");
				// if the command is 0x06, then it's success message
				// from the original broadcaster, with which the device
				// confirms that it got all cleanup replies successfully.
				GroupMessage gm = (cmd1 == 0x06) ? GroupMessage.SUCCESS :
										GroupMessage.BCAST;
				isDuplicate = !updateGroupState(group, hops, gm);
			} else if (t == MsgType.ALL_LINK_CLEANUP) {
				// the cleanup messages are direct messages, so the
				// group # is not in the toAddress, but in cmd2 
				int group = (int)(msg.getByte("command2") & 0xff);
				isDuplicate = !updateGroupState(group, hops,
								GroupMessage.CLEAN);
			}
		} catch (IllegalArgumentException e) {
			logger.error("cannot parse msg: {}", msg, e);			
		} catch (FieldException e) {
			logger.error("cannot parse msg: {}", msg, e);
		}
		return (isDuplicate);
	}
	/**
	 * Advance the state of the state machine that suppresses duplicates
	 * 
	 * @param group the insteon group of the broadcast message
	 * @param hops number of hops left
	 * @param a what type of group message came in (action etc)
	 * @return true if this is message is NOT a duplicate
	 */
	private boolean updateGroupState(int group, int hops, GroupMessage a) {
		GroupMessageStateMachine m = m_groupState.get(new Integer(group));
		if (m == null) {
			m = new GroupMessageStateMachine();
			m_groupState.put(new Integer(group), m);
		}
		logger.trace("updating group state for {} to {}", group, a);
		return (m.action(a, hops));
	}
	
	/**
	 * Extract button information from message
	 * @param msg the message to extract from
	 * @param the device feature (needed for debug printing)
	 * @return the button number or -1 if no button found
	 */
	static protected int getButtonInfo(Msg msg, DeviceFeature f) {
		// the cleanup messages have the button number in the command2 field
		// the broadcast messages have it as the lsb of the toAddress
		try {
			int bclean = (int) (msg.getByte("command2") & 0xff);
			int bbcast = (int) (msg.getAddress("toAddress").getLowByte() & 0xff);
			int button = msg.isCleanup() ? bclean : bbcast;
			logger.trace("{} button: {} bclean: {} bbcast: {}",
					f.getDevice().getAddress(), button, bclean, bbcast);
			return button;
		}  catch (FieldException e) {
			logger.error("field exception while parsing msg {}: ", msg, e);
		}
		return -1;
	}
	
	/**
	 * Shorthand to return class name for logging purposes
	 * @return name of the class
	 */
	protected String nm() {
		return (this.getClass().getSimpleName());
	}
	
	/**
	 * Set parameter map
	 * @param hm the parameter map for this message handler
	 */
	public void setParameters(HashMap<String, String> hm) { m_parameters = hm; }
	
	
	//
	//
	// ---------------- the various command handler start here -------------------
	//
	//
	
	public static class DefaultMsgHandler extends MessageHandler {
		DefaultMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
					DeviceFeature f, String fromPort) {
			logger.debug("{} drop unimpl message {}: {}", nm(), Utils.getHexByte(cmd1), msg);
		}
	}

	public static class NoOpMsgHandler extends MessageHandler {
		NoOpMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			logger.trace("{} ignore msg {}: {}", nm(), Utils.getHexByte(cmd1), msg);
		}
	}

	public static class LightOnDimmerHandler extends MessageHandler {
		LightOnDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (!isMybutton(msg, f)) {
				return;
			}
			InsteonAddress a = f.getDevice().getAddress();
			if (msg.isAckOfDirect()) {
				logger.error("{}: device {}: ignoring ack of direct.", nm(), a);
			} else {
				String mode = getStringParameter("mode", "REGULAR");
				logger.info("{}: device {} was turned on {}. " +
						"Sending poll request to get actual level", nm(), a, mode);
				m_feature.publish(PercentType.HUNDRED, StateChangeType.ALWAYS);
				// need to poll to find out what level the dimmer is at now.
				// it may not be at 100% because dimmers can be configured
				// to switch to e.g. 75% when turned on.
				Msg m = f.makePollMsg();
				if (m != null)	f.getDevice().enqueueDelayedMessage(m, f, 1000);
			}
		}
	}

	public static class LightOffDimmerHandler extends MessageHandler {
		LightOffDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (isMybutton(msg, f)) {
				String mode = getStringParameter("mode", "REGULAR");
				logger.info("{}: device {} was turned off {}.", nm(),
						f.getDevice().getAddress(), mode);
				f.publish(PercentType.ZERO, StateChangeType.ALWAYS);
			}
		}
	}

	public static class LightOnSwitchHandler extends MessageHandler {
		LightOnSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (isMybutton(msg, f)) {
				String mode = getStringParameter("mode", "REGULAR");
				logger.info("{}: device {} was switched on {}.", nm(),
								f.getDevice().getAddress(), mode);
				f.publish(OnOffType.ON, StateChangeType.ALWAYS);
			} else {
				logger.debug("ignored message: {}", isMybutton(msg,f));
			}
		}
	}

	public static class LightOffSwitchHandler extends MessageHandler {
		LightOffSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (isMybutton(msg, f)) {
				String mode = getStringParameter("mode", "REGULAR");
				logger.info("{}: device {} was switched off {}.", nm(),
						f.getDevice().getAddress(), mode);
				f.publish(OnOffType.OFF, StateChangeType.ALWAYS);
			}
		}
	}

	/**
	 * This message handler processes replies to Ramp ON/OFF commands.
	 * Currently, it's been tested for the 2672-222 LED Bulb. Other
	 * devices may use a different pair of commands (0x2E, 0x2F). This
	 * handler and the command handler will need to be extended to support
	 * those devices.
	 */
	public static class RampDimmerHandler extends MessageHandler {
		private int onCmd;
		private int offCmd;
		
		RampDimmerHandler(DeviceFeature p) { 
			super(p);
			// Can't process parameters here because they are set after constructor is invoked.
			// Unfortunately, this means we can't declare the onCmd, offCmd to be final.
		}
		
		@Override
		public void setParameters(HashMap<String, String> params)
		{
			super.setParameters(params);
			onCmd = getIntParameter("on", 0x2E);
			offCmd = getIntParameter("off", 0x2F);
		}
		
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (cmd1 == onCmd) {
				int level = getLevel(msg);
				logger.info(
						"{}: device {} was switched on using ramp to level {}.",
						nm(), f.getDevice().getAddress(), level);
				if (level == 100) {
					f.publish(OnOffType.ON, StateChangeType.ALWAYS);
				} else {
					// The publisher will convert an ON at level==0 to an OFF.
					// However, this is not completely accurate since a ramp
					// off at level == 0 may not turn off the dimmer completely
					// (if I understand the Insteon docs correctly). In any
					// case,
					// it would be an odd scenario to turn ON a light at level
					// == 0
					// rather than turn if OFF.
					f.publish(new PercentType(level), StateChangeType.ALWAYS);
				}
			} else if (cmd1 == offCmd) {
				logger.info("{}: device {} was switched off using ramp.", nm(),
						f.getDevice().getAddress());
				f.publish(new PercentType(0), StateChangeType.ALWAYS);
			}
		}

		private int getLevel(Msg msg) {
			try {
				byte cmd2 = msg.getByte("command2");
				return (int) Math.round(((cmd2 >> 4) & 0x0f) * (100/15d));
			}
			catch (FieldException e) {
				logger.error("Can't access command2 byte", e);
				return 0;
			}
		}
	}

	/**
	 * A message handler that processes replies to queries.
	 * If command2 == 0xFF then the light has been turned on
	 * else if command2 == 0x00 then the light has been turned off
	 */

	public static class SwitchRequestReplyHandler extends  MessageHandler {
		SwitchRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			try {
				InsteonAddress a = f.getDevice().getAddress();
				int cmd2	= (int) (msg.getByte("command2") & 0xff);
				int button	= this.getIntParameter("button", -1);
				if (button < 0) {
					handleNoButtons(cmd2, a, msg);
				} else {
					boolean isOn = isLEDLit(cmd2, button);
					logger.info("{}: dev {} button {} switched to {}", nm(),
													a, button, isOn ? "ON" : "OFF");
					m_feature.publish(isOn ? OnOffType.ON : OnOffType.OFF, StateChangeType.CHANGED);
				}
			} catch (FieldException e) {
				logger.error("{} error parsing {}: ", nm(), msg, e);
			}
		}
		/**
		 * Handle the case where no buttons have been configured.
		 * In this situation, the only return values should be 0 (light off)
		 * or 0xff (light on)
		 * @param cmd2
		 */
		void handleNoButtons(int cmd2, InsteonAddress a, Msg msg) {
			if (cmd2 == 0) {
				logger.info("{}: set device {} to OFF", nm(), a);
				m_feature.publish(OnOffType.OFF, StateChangeType.CHANGED);
			} else if (cmd2 == 0xff) {
				logger.info("{}: set device {} to ON", nm(), a);
				m_feature.publish(OnOffType.ON, StateChangeType.CHANGED);
			} else {
				logger.warn("{}: {} ignoring unexpected cmd2 in msg: {}",
							nm(), a, msg);
			}	
		}
		/**
		 * Test if cmd byte indicates that button is lit.
		 * The cmd byte has the LED status bitwise from the left:
		 *       87654321
		 * Note that the 2487S has buttons assigned like this:
		 *      22|6543|11
		 * They used the basis of the 8-button remote, and assigned
		 * the ON button to 1+2, the OFF button to 7+8
		 * 
		 * @param cmd    cmd byte as received in message
		 * @param button button to test (number in range 1..8)
		 * @return true if button is lit, false otherwise
		 */
		private boolean isLEDLit(int cmd, int button) {
			boolean isSet = (cmd & (0x1 << (button-1))) != 0;
			logger.trace("cmd: {} button {}", Integer.toBinaryString(cmd), button);
			logger.trace("msk: {} isSet: {}", Integer.toBinaryString(0x1 << (button-1)), isSet);
			return (isSet);
		}
	}

	/**
	 * Handles Dimmer replies to status requests.
	 * In the dimmers case the command2 byte represents the light level from 0-255
	 */
	public static class DimmerRequestReplyHandler extends  MessageHandler {
		DimmerRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				if (cmd2 == 0xfe) {
					// sometimes dimmer devices are returning 0xfe when on instead of 0xff
					cmd2 = 0xff;
				}

				if (cmd2 == 0) {
					logger.info("{}: set device {} to level 0", nm(),
							dev.getAddress());
					m_feature.publish(PercentType.ZERO, StateChangeType.CHANGED);
				} else if (cmd2 == 0xff) {
					logger.info("{}: set device {} to level 100", nm(),
							dev.getAddress());
					m_feature.publish(PercentType.HUNDRED, StateChangeType.CHANGED);
				} else {
					int level = cmd2*100/255;
					if (level == 0) level = 1;
					logger.info("{}: set device {} to level {}", nm(),
							dev.getAddress(), level);
					m_feature.publish(new PercentType(level), StateChangeType.CHANGED);
				}
			} catch (FieldException e) {
				logger.error("{}: error parsing {}: ", nm(), msg, e);
			}
		}
	}

	public static class DimmerStopManualChangeHandler extends MessageHandler {
		DimmerStopManualChangeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			Msg m = f.makePollMsg();
			if (m != null)	f.getDevice().enqueueMessage(m, f);
		}
	}

	public static class StartManualChangeHandler extends MessageHandler {
		StartManualChangeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			try {
				int cmd2	= (int) (msg.getByte("command2") & 0xff);
				int upDown	= (cmd2 == 0) ? 0 : 2;
				logger.info("{}: dev {} manual state change: {}", nm(),
						f.getDevice().getAddress(), (upDown  == 0) ? "DOWN" : "UP");
				m_feature.publish(new DecimalType(upDown), StateChangeType.ALWAYS);
			} catch (FieldException e) {
				logger.error("{} error parsing {}: ", nm(), msg, e);
			}
		}
	}
	public static class StopManualChangeHandler extends MessageHandler {
		StopManualChangeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			logger.info("{}: dev {} manual state change: {}", nm(),
					f.getDevice().getAddress(), 0);
			m_feature.publish(new DecimalType(1), StateChangeType.ALWAYS);

		}
	}

	public static class InfoRequestReplyHandler extends MessageHandler {
		InfoRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.warn("{} device {} expected extended msg as info reply, got {}",
						nm(), dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int prodKey = msg.getInt24("userData2", "userData3", "userData4");
					int devCat  = msg.getByte("userData5");
					int subCat  = msg.getByte("userData6");
					logger.info("{} {} got product data: cat: {} subcat: {} key: {} ",
							nm(), dev.getAddress(), devCat, subCat,	Utils.getHexString(prodKey));
					break;
				case 0x02: // this is a device text string response message
					logger.info("{} {} got text str {} ", nm(), dev.getAddress(), msg);
					break;
				default:
					logger.warn("{} unknown cmd2 = {} in info reply message {}", nm(), cmd2, msg);
					break;
				}
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}

	public static class MotionSensorDataReplyHandler extends MessageHandler {
		MotionSensorDataReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.trace("{} device {} ignoring non-extended msg {}", nm(), dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int batteryLevel = msg.getByte("userData12") & 0xff;
					int lightLevel = msg.getByte("userData11") & 0xff;
					logger.debug("{}: {} got light level: {}, battery level: {}",
								nm(), dev.getAddress(), lightLevel, batteryLevel);
					m_feature.publish(new DecimalType(lightLevel), StateChangeType.CHANGED, "field", "light_level");
					m_feature.publish(new DecimalType(batteryLevel), StateChangeType.CHANGED, "field", "battery_level");
					break;
				default:
					logger.warn("unknown cmd2 = {} in info reply message {}", cmd2, msg);
					break;
				}
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}
	
	public static class HiddenDoorSensorDataReplyHandler extends MessageHandler {
		HiddenDoorSensorDataReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.trace("{} device {} ignoring non-extended msg {}", nm(), dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int batteryLevel = msg.getByte("userData4") & 0xff;
					int batteryWatermark = msg.getByte("userData7") & 0xff;
					logger.debug("{}: {} got light level: {}, battery level: {}",
								nm(), dev.getAddress(), batteryWatermark, batteryLevel);
					m_feature.publish(new DecimalType(batteryWatermark), StateChangeType.CHANGED, "field", "battery_watermark_level");
					m_feature.publish(new DecimalType(batteryLevel), StateChangeType.CHANGED, "field", "battery_level");
					break;
				default:
					logger.warn("unknown cmd2 = {} in info reply message {}", cmd2, msg);
					break;
				}
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}

	public static class PowerMeterUpdateHandler extends MessageHandler {
		PowerMeterUpdateHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (msg.isExtended()) {
				try {
					// see iMeter developer notes 2423A1dev-072013-en.pdf
					int b7	= msg.getByte("userData7")	& 0xff;
					int b8	= msg.getByte("userData8")	& 0xff;
					int watts = (b7 << 8) | b8;
					if (watts > 32767) {
						watts -= 65535;
					}

					int b9	= msg.getByte("userData9")	& 0xff;
					int b10	= msg.getByte("userData10")	& 0xff;
					int b11	= msg.getByte("userData11")	& 0xff;
					int b12	= msg.getByte("userData12")	& 0xff;
					BigDecimal kwh = BigDecimal.ZERO;
					if (b9 < 254) {
						int e = (b9 << 24) | (b10 << 16) | (b11 << 8) | b12;
						kwh = new BigDecimal(e * 65535.0 / (1000 * 60 * 60 * 60)).setScale(4, RoundingMode.HALF_UP);
					}

					logger.debug("{}:{} watts: {} kwh: {} ", nm(), f.getDevice().getAddress(), watts, kwh);
					m_feature.publish(new DecimalType(kwh), StateChangeType.CHANGED, "field", "kwh");
					m_feature.publish(new DecimalType(watts), StateChangeType.CHANGED, "field", "watts");
				} catch (FieldException e) {
					logger.error("error parsing {}: ", msg, e);
				}
			}
		}
	}
	
	public static class PowerMeterResetHandler extends MessageHandler {
		PowerMeterResetHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			logger.info("{}: power meter {} was reset", nm(), dev.getAddress());

			// poll device to get updated kilowatt hours and watts
			Msg m = f.makePollMsg();
			if (m != null)	f.getDevice().enqueueMessage(m, f);
		}
	}
	
	public static class LastTimeHandler extends MessageHandler {
		LastTimeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1a, Msg msg,
				DeviceFeature f, String fromPort) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(System.currentTimeMillis());
			DateTimeType t = new DateTimeType(calendar);
			m_feature.publish(t, StateChangeType.ALWAYS);
		}
	}

	public static class ContactRequestReplyHandler extends MessageHandler {
		ContactRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1a, Msg msg,
				DeviceFeature f, String fromPort) {
			byte cmd  = 0x00;
			byte cmd2 = 0x00;
			try {
				cmd = msg.getByte("Cmd");
				cmd2 = msg.getByte("command2");
			} catch (FieldException e) {
				logger.debug("{} no cmd found, dropping msg {}", nm(), msg);
				return;
			}
			if (msg.isAckOfDirect() && (f.getQueryStatus() == DeviceFeature.QueryStatus.QUERY_PENDING)
					&& cmd == 0x50) {
				OpenClosedType oc = (cmd2 == 0) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
				logger.info("{}: set contact {} to: {}", nm(), f.getDevice().getAddress(), oc);
				m_feature.publish(oc, StateChangeType.CHANGED);
			}
		}
	}

	public static class ClosedContactHandler extends MessageHandler {
		ClosedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			m_feature.publish(OpenClosedType.CLOSED, StateChangeType.ALWAYS);
		}
	}

	public static class OpenedContactHandler extends MessageHandler {
		OpenedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			m_feature.publish(OpenClosedType.OPEN, StateChangeType.ALWAYS);
		}
	}

	public static class OpenedOrClosedContactHandler extends MessageHandler {
		OpenedOrClosedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			try {
				byte cmd2 = msg.getByte("command2");
				switch (cmd1) {
				case 0x11:
					switch (cmd2) {
					case 0x02:
						m_feature.publish(OpenClosedType.CLOSED, StateChangeType.CHANGED);
						break;
					case 0x01:
					case 0x04:
						m_feature.publish(OpenClosedType.OPEN, StateChangeType.CHANGED);
						break;
					default: // do nothing
						break;
					}
					break;
				case 0x13:
					switch (cmd2) {
					case 0x04:
						m_feature.publish(OpenClosedType.CLOSED, StateChangeType.CHANGED);
						break;
					default: // do nothing
						break;
					}
					break;
				}
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}

	public static class ClosedSleepingContactHandler extends MessageHandler {
		ClosedSleepingContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			m_feature.publish(OpenClosedType.CLOSED, StateChangeType.ALWAYS);
			sendExtendedQuery(f, (byte)0x2e, (byte) 00);
		}
	}


	public static class OpenedSleepingContactHandler extends MessageHandler {
		OpenedSleepingContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			m_feature.publish(OpenClosedType.OPEN, StateChangeType.ALWAYS);
			sendExtendedQuery(f, (byte)0x2e, (byte) 00);
		}
	}

	/**
	 * Triggers a poll when a message comes in. Use this handler to react
	 * to messages that notify of a status update, but don't carry the information
	 * that you are interested in. Example: you send a command to change a setting,
	 * get a DIRECT ack back, but the ack does not have the value of the updated setting.
	 * Then connect this handler to the ACK, such that the device will be polled, and
	 * the settings updated.
	 */
	public static class TriggerPollMsgHandler extends MessageHandler {
		TriggerPollMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			m_feature.getDevice().doPoll(2000); // 2000 ms delay
		}
	}

	/**
	 * Flexible handler to extract numerical data from messages.
	 */
	public static class NumberMsgHandler extends MessageHandler {
		NumberMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			try {
				// first do the bit manipulations to focus on the right area
				int mask = getIntParameter("mask", 0xFFFF);
				int rawValue = extractValue(msg);
				int cooked = (rawValue & mask) >> getIntParameter("rshift", 0);
				// now do an arbitrary transform on the data
				double value = (double)transform(cooked);
				// last, multiply with factor and add an offset
				double dvalue = getDoubleParameter("offset", 0) +  value * getDoubleParameter("factor", 1.0);
				m_feature.publish(new DecimalType(dvalue), StateChangeType.CHANGED);
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
		public int transform(int raw) {
			return (raw);
		}
		private int extractValue(Msg msg) throws FieldException {
			String lowByte = getStringParameter("low_byte", "");
			if (lowByte == "") {
				logger.error("{} handler misconfigured, missing low_byte!", nm());
				return 0;
			}
			
			int value = (int) (msg.getByte(lowByte) & 0xFF);
			String highByte = getStringParameter("high_byte", "");
			if (highByte != "") {
				value |= ((int) (msg.getByte(highByte) & 0xFF)) << 8;
			}
			return (value);
		}
	}

	/**
	 * Convert system mode field to number 0...4. Insteon has two different
	 * conventions for numbering, we use the one of the status update messages
	 */
	public static class ThermostatSystemModeMsgHandler extends NumberMsgHandler {
		ThermostatSystemModeMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public int transform(int raw) {
			switch (raw) {
			case 0: return (0);	// off
			case 1: return (3);	// auto
			case 2:	return (1);	// heat
			case 3:	return (2);	// cool
			case 4:	return (4);	// program
			default: break;
			}
			return (4); // when in doubt assume to be in "program" mode 
		}
	}

	/**
	 * Handle reply to system mode change command
	 */
	public static class ThermostatSystemModeReplyHandler extends NumberMsgHandler {
		ThermostatSystemModeReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public int transform(int raw) {
			switch (raw) {
			case 0x09:	return (0);	// off
			case 0x04:	return (1);	// heat
			case 0x05:	return (2);	// cool
			case 0x06:	return (3);	// auto
			case 0x0A:	return (4);	// program
			default: break;
			}
			return (4); // when in doubt assume to be in "program" mode 
		}
	}
	/**
	 * Handle reply to fan mode change command
	 */
	public static class ThermostatFanModeReplyHandler extends NumberMsgHandler {
		ThermostatFanModeReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public int transform(int raw) {
			switch (raw) {
			case 0x08:	return (0);	// auto
			case 0x07:	return (1);	// always on
			default: break;
			}
			return (0); // when in doubt assume to be auto mode 
		}
	}

	/**
	 * Handle reply to fanlinc fan speed change command
	 */
	public static class FanLincFanReplyHandler extends NumberMsgHandler {
		FanLincFanReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public int transform(int raw) {
			switch (raw) {
			case 0x00:	return (0);	// off
			case 0x55:	return (1);	// low
			case 0xAA:	return (2);	// medium
			case 0xFF:	return (3);	// high
			default:
				logger.warn("fanlinc got unexpected level: {}", raw);
			}
			return (0); // when in doubt assume to be off 
		}
	}
	
	/**
	 * Process X10 messages that are generated when another controller
	 * changes the state of an X10 device.
	 */
	public static class X10OnHandler extends  MessageHandler {
		X10OnHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.info("{}: set X10 device {} to ON", nm(), a);
			m_feature.publish(OnOffType.ON, StateChangeType.ALWAYS);
		}
	}
	public static class X10OffHandler extends  MessageHandler {
		X10OffHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.info("{}: set X10 device {} to OFF", nm(), a);
			m_feature.publish(OnOffType.OFF, StateChangeType.ALWAYS);
		}
	}
	public static class X10BrightHandler extends  MessageHandler {
		X10BrightHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.debug("{}: ignoring brighten message for device {}", nm(), a);
		}
	}
	public static class X10DimHandler extends  MessageHandler {
		X10DimHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.debug("{}: ignoring dim message for device {}", nm(), a);
		}
	}
	public static class X10OpenHandler extends  MessageHandler {
		X10OpenHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.info("{}: set X10 device {} to OPEN", nm(), a);
			m_feature.publish(OpenClosedType.OPEN, StateChangeType.ALWAYS);
		}
	}
	public static class X10ClosedHandler extends  MessageHandler {
		X10ClosedHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonAddress a = f.getDevice().getAddress();
			logger.info("{}: set X10 device {} to CLOSED", nm(), a);
			m_feature.publish(OpenClosedType.CLOSED, StateChangeType.ALWAYS);
		}
	}
	
	/**
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param params 
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends MessageHandler> T s_makeHandler(String name, HashMap<String, String> params, DeviceFeature f) {
		String cname = MessageHandler.class.getName() + "$" + name;
		try {
			Class<?> c = Class.forName(cname);
			@SuppressWarnings("unchecked")
			Class<? extends T> dc = (Class <? extends T>) c;
			T mh = dc.getDeclaredConstructor(DeviceFeature.class).newInstance(f);
			mh.setParameters(params);
			return mh;
		} catch (Exception e) {
			logger.error("error trying to create message handler: {}", name, e);
		}
		return null;
	}
}
