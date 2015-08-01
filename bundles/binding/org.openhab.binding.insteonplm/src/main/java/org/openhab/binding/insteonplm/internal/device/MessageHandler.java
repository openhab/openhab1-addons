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
		try {
			if (m_parameters.get(key) != null) {
				return Integer.parseInt(m_parameters.get(key));
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
		logger.debug("updating group state for {} to {}", group, a);
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
			logger.debug("{} ignore msg {}: {}", nm(), Utils.getHexByte(cmd1), msg);
		}
	}

	public static class LightOnDimmerHandler extends MessageHandler {
		LightOnDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (isDuplicate(msg) || !isMybutton(msg, f)) {
				return;
			}
			InsteonAddress a = f.getDevice().getAddress();
			if (msg.isAckOfDirect()) {
				logger.error("{}: device {}: ignoring ack of direct.", nm(), a);
			} else {
				logger.info("{}: device {} was turned on. Sending poll request to get actual level", nm(), a);
				m_feature.publish(PercentType.HUNDRED, StateChangeType.ALWAYS);
				// need to poll to find out what level the dimmer is at now.
				// it may not be at 100% because dimmers can be configured
				// to switch to e.g. 75% when turned on.
				Msg m = f.makePollMsg();
				if (m != null)	f.getDevice().enqueueDelayedMessage(m, f, 1000);
			}
		}
	}

	public static class LightOnSwitchHandler extends MessageHandler {
		LightOnSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (!isDuplicate(msg) && isMybutton(msg, f)) {
				logger.info("{}: device {} was switched on.", nm(),
								f.getDevice().getAddress());
				f.publish(OnOffType.ON, StateChangeType.ALWAYS);
			} else {
				logger.debug("ignored message: {} or {}", isDuplicate(msg), isMybutton(msg,f));
			}
		}
	}

	public static class LightOffDimmerHandler extends MessageHandler {
		LightOffDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (!isDuplicate(msg) && isMybutton(msg, f)) {
				logger.info("{}: device {} was turned off.", nm(),
						f.getDevice().getAddress());
				f.publish(PercentType.ZERO, StateChangeType.ALWAYS);
			}
		}
	}

	public static class LightOffSwitchHandler extends MessageHandler {
		LightOffSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			if (!isDuplicate(msg) && isMybutton(msg, f)) {
				logger.info("{}: device {} was switched off.", nm(),
						f.getDevice().getAddress());
				f.publish(OnOffType.OFF, StateChangeType.ALWAYS);
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

	public static class StopManualChangeHandler extends MessageHandler {
		StopManualChangeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			Msg m = f.makePollMsg();
			if (m != null)	f.getDevice().enqueueMessage(m, f);
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
			if (cmd1 != 0x11) return;
			try {
				byte cmd2 = msg.getByte("command2");
				switch (cmd2) {
				case 0x02:
					m_feature.publish(OpenClosedType.CLOSED, StateChangeType.CHANGED);
					break;
				case 0x01:
					m_feature.publish(OpenClosedType.OPEN, StateChangeType.CHANGED);
					break;
				default: // do nothing
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
	 * Handles Thermostat replies to Set Cool SetPoint requests.
	 */
	public static class ThermostatSetPointMsgHandler extends  MessageHandler {
		ThermostatSetPointMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				if (msg.isExtended()) {
					logger.info("{}: received msg for feature {}", nm(), f.getName());
					int level = ((f.getName()).equals("ThermostatCoolSetPoint")) ? (int)(msg.getByte("userData7") & 0xff) : (int)(msg.getByte("userData8") & 0xff);
					logger.info("{}: got SetPoint from {} of value: {}", nm(), dev.getAddress(), level);
					f.publish(new DecimalType(level), StateChangeType.CHANGED);
				} else {
					logger.info("{}: received msg for feature {}", nm(), f.getName());
					int cmd2 = (int) (msg.getByte("command2") & 0xff);
					int level = cmd2/2;
					logger.info("{}: got SETPOINT from {} of value: {}", nm(), dev.getAddress(), level);
					f.publish(new DecimalType(level), StateChangeType.CHANGED);
				}
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}

	/**
	 * Handles Thermostat replies to Temperature requests.
	 */
	public static class ThermostatTemperatureRequestReplyHandler extends  MessageHandler {
		ThermostatTemperatureRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				int cmd1Msg = (int) (msg.getByte("command1") & 0xff);
				if (cmd1Msg != 0x6a) {
					logger.warn("{}: ignoring bad TEMPERATURE reply from {}", nm(), dev.getAddress());
					return;
				}
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				int level = cmd2/2;
				logger.info("{}: got TEMPERATURE from {} of value: {}", nm(), dev.getAddress(), level);
				logger.info("{}: set device {} to level {}", nm(), dev.getAddress(), level);
				f.publish(new DecimalType(level), StateChangeType.CHANGED);
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}	
		
	/**
	 * Handles Thermostat replies to Humidity requests.
	 */
	public static class ThermostatHumidityRequestReplyHandler extends  MessageHandler {
		ThermostatHumidityRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				int cmd1Msg = (int) (msg.getByte("command1") & 0xff);
				if (cmd1Msg != 0x6a) {
					logger.warn("{}: ignoring bad HUMIDITY reply from {}", nm(), dev.getAddress());
					return;
				}
				int cmd2 = (int) msg.getByte("command2");
				logger.info("{}: got HUMIDITY from {} of value: {}", nm(), dev.getAddress(), cmd2);
				logger.info("{}: set device {} to level {}", nm(), dev.getAddress(), cmd2);
				f.publish(new PercentType(cmd2), StateChangeType.CHANGED);
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}
	
	/**
	 * Handles Thermostat replies to Mode requests.
	 */
	public static class ThermostatModeControlReplyHandler extends  MessageHandler {
		ThermostatModeControlReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				/**
				* Cmd2 Description 										Thermostat Support 	Comments
				* 0x04 set mode to heat and returns 04 in ACK 			yes 				On Heat
				* 0x05 set mode to cool and returns 05 in ACK 			yes 				On Cool
				* 0x06 set mode to manual auto and returns 06 in ACK 	yes 				Manual Auto
				*/
				byte cmd2 = msg.getByte("command2");
				switch (cmd2) {
				case 0x04:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "HEAT");
					f.publish(new DecimalType(2), StateChangeType.CHANGED);
					break;
				case 0x05:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "COOL");
					f.publish(new DecimalType(1), StateChangeType.CHANGED);
					break;
				case 0x06:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "AUTO");
					f.publish(new DecimalType(3), StateChangeType.CHANGED);
					break;
				default: // do nothing
					break;
				}
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}

	/**
	 * Handles Thermostat replies to Fan requests.
	 */
	public static class ThermostatFanControlReplyHandler extends  MessageHandler {
		ThermostatFanControlReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				/**
				* Cmd2 Description 										Thermostat Support 	Comments
				* 0x07 Turn fan on and returns 07 in ACK 				yes 				On Fan
				* 0x08 Turn fan auto mode and returns 08 in ACK 		yes 				Auto Fan
				* 0x09 Turn all off and returns 09 in ACK 				yes 				Off All
				*/
				byte cmd2 = msg.getByte("command2");
				switch (cmd2) {
				case 0x07:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "ON");
					f.publish(new DecimalType(2), StateChangeType.CHANGED);
					break;
				case 0x08:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "AUTO");
					f.publish(new DecimalType(3), StateChangeType.CHANGED);
					break;	
				case 0x09:
					logger.info("{}: set device {} to {}", nm(),
							dev.getAddress(), "OFF");
					f.publish(new DecimalType(1), StateChangeType.CHANGED);
					break;	
				default: // do nothing
					break;
				}
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
			}
		}
	}

	/**
	 * Handles Thermostat replies to Master requests.
	 */
	public static class ThermostatMasterControlReplyHandler extends  MessageHandler {
		ThermostatMasterControlReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(int group, byte cmd1, Msg msg,
				DeviceFeature f, String fromPort) {
			try {
				/**
				* 
				*/
				byte cmd2 = msg.getByte("userData3");
				switch (cmd2) {
				case 0x00:
					logger.info("{}: set PRIMARY Thermostat to MASTER", nm());
					f.publish(new DecimalType(1), StateChangeType.CHANGED);
					break;
				case 0x01:
					logger.info("{}: set SECONDARY Thermostat to MASTER", nm());
					f.publish(new DecimalType(2), StateChangeType.CHANGED);
					break;	
				case 0x02:
					logger.info("{}: set TERTIARY Thermostat to MASTER", nm());
					f.publish(new DecimalType(3), StateChangeType.CHANGED);
					break;	
				default: // do nothing
					break;
				}
			} catch (FieldException e) {
				logger.debug("{} no cmd2 found, dropping msg {}", nm(), msg);
				return;
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
