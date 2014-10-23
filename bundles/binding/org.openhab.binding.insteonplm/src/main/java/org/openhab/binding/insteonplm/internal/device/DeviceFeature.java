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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.openhab.binding.insteonplm.internal.utils.Utils.ParsingException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A DeviceFeature represents a certain feature (trait) of a given Insteon device, e.g. something
 * operating under a given InsteonAddress that can be manipulated (relay) or read (sensor).
 * 
 * The DeviceFeature does the processing of incoming messages, and handles commands for the
 * particular feature it represents.
 * 
 * It uses four mechanisms for that:
 * 
 * 1) MessageDispatcher: makes high level decisions about an incoming message and then runs the
 * 2) MessageHandler: further processes the message, updates state etc
 * 3) CommandHandler: translates commands from the openhab bus into an Insteon message.
 * 4) PollHandler: creates an Insteon message to query the DeviceFeature
 * 
 * Lastly, DeviceFeatureListeners can register with the DeviceFeature to get notifications when
 * the state of a feature has changed. In practice, a DeviceFeatureListener corresponds to an
 * OpenHAB item.
 * 
 * The character of a DeviceFeature is thus given by a set of message and command handlers.
 * A FeatureTemplate captures exactly that: it says what set of handlers make up a DeviceFeature.
 * 
 * DeviceFeatures are added to a new device by referencing a FeatureTemplate (defined in device_features.xml)
 * from the Device definition file (categories.xml).
 * 
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class DeviceFeature {
	public static enum QueryStatus {
		NEVER_QUERIED,
		QUERY_PENDING,
		QUERY_ANSWERED
	}

	private static final Logger logger = LoggerFactory.getLogger(DeviceFeature.class);
	
	private static HashMap<String, FeatureTemplate> s_features = new HashMap<String, FeatureTemplate>();
	
	private InsteonDevice				m_device = null;
	private String						m_name	 = "INVALID_FEATURE_NAME";
	private boolean					m_isStatus = false;
	private QueryStatus	                m_queryStatus = QueryStatus.NEVER_QUERIED;
	
	private MessageHandler				m_defaultMsgHandler = new DefaultMsgHandler(this);
	private CommandHandler				m_defaultCommandHandler = new WarnCommandHandler(this);
	private PollHandler					m_pollHandler = null;
	private	 MessageDispatcher			m_dispatcher = null;

	private HashMap<Integer, MessageHandler> m_msgHandlers =
				new HashMap<Integer, MessageHandler>();
	private HashMap<Class<? extends Command>, CommandHandler> m_commandHandlers =
				new HashMap<Class<? extends Command>, CommandHandler>();
	private ArrayList<DeviceFeatureListener> m_listeners = new ArrayList<DeviceFeatureListener>();
	
	static {
		try {
			InputStream input = DeviceFeature.class.getResourceAsStream("/device_features.xml");
			ArrayList<FeatureTemplate> features = XMLDeviceFeatureReader.s_readTemplates(input);
			for (FeatureTemplate f : features) {
				s_features.put(f.getName(), f);
			}
		} catch (IOException e) {
			logger.error("IOException while reading device features", e);
		} catch (ParsingException e) {
			logger.error("Parsing exception while reading device features", e);
		}
	}
	
	/**
	 * Constructor
	 * @param device Insteon device to which this feature belongs
	 * @param name descriptive name for that feature
	 */
	public DeviceFeature(InsteonDevice device, String name) {
		m_name = name;
		setDevice(device);
	}

	/**
	 * Constructor
	 * @param name descriptive name of the feature
	 */
	public DeviceFeature(String name) {
		m_name = name;
	}
	
	public String	 	getName()			{ return m_name; }
	public synchronized QueryStatus	getQueryStatus()	{ return m_queryStatus; }
	public InsteonDevice getDevice() 		{ return m_device; }
	public boolean 		hasListeners() 		{ return !m_listeners.isEmpty(); }
	public boolean		isStatusFeature()	{ return m_isStatus; }
	
	
	public void setStatusFeature(boolean f)	{ m_isStatus = f; }
	public void setPollHandler(PollHandler h)	{ m_pollHandler = h; }
	public void setDevice(InsteonDevice d)		{ m_device = d; }
	public void setMessageDispatcher(MessageDispatcher md) { m_dispatcher = md; }
	public void setDefaultCommandHandler(CommandHandler ch) { m_defaultCommandHandler = ch; }
	public void setDefaultMsgHandler(MessageHandler mh) { m_defaultMsgHandler = mh; }
	
	public synchronized void setQueryStatus(QueryStatus status)	{
		logger.trace("{} set query status to: {}", m_name, status);
		m_queryStatus = status;
	}

	public void addListener(DeviceFeatureListener l) {
		synchronized(m_listeners) {
			for (DeviceFeatureListener m : m_listeners) {
				if (m.getItemName().equals(l.getItemName())) {
					return;
				}
			}
			m_listeners.add(l);
		}
	}
	
	public void addMessageHandler(int cm1, MessageHandler handler) {
		synchronized(m_msgHandlers) {
			m_msgHandlers.put(cm1, handler);
		}
	}
	public void addCommandHandler(Class<? extends Command> c, CommandHandler handler) {
		synchronized(m_commandHandlers) {
			m_commandHandlers.put(c, handler);
		}
	}
	
	public boolean handleMessage(Msg msg, String port) {
		if (m_dispatcher == null) {
			logger.error("{} no dispatcher for msg {}", m_name, msg);
			return false;
		}
		return (m_dispatcher.dispatch(msg, port));
	}
	
	public void handleCommand(Command cmd) {
		Class<? extends Command> key = cmd.getClass();
		CommandHandler h = m_commandHandlers.containsKey(key) ? m_commandHandlers.get(key) : m_defaultCommandHandler;
		logger.trace("{} uses {} to handle command {} for {}", getName(), h.getClass().getSimpleName(),
				key.getSimpleName(), getDevice().getAddress());
		h.handleCommand(cmd, getDevice());
	}
	
	public Msg makePollMsg() {
		if (m_pollHandler == null) return null;
		logger.trace("{} making poll msg for {} using handler {}", getName(), getDevice().getAddress(),
				m_pollHandler.getClass().getSimpleName());
		Msg m = m_pollHandler.makeMsg(m_device);
		return m;
	}
	
	
	public void publishAll(State newState) {
		logger.debug("{} publishing: {}", getName(), newState);
		synchronized(m_listeners) {
			for (DeviceFeatureListener listener : m_listeners) {
				listener.stateChanged(newState);
			}
		}
	}
	public void publishFiltered(State newState, String key, int val) {
		logger.debug("{} publishing filtered: {} param {} == {}",
				getName(), newState, key, val);
		synchronized(m_listeners) {
			for (DeviceFeatureListener listener : m_listeners) {
				if (listener.hasParameter(key) && listener.getIntParameter(key) == val) {
					logger.debug("{} publishing to: {}", getName(), listener.getItemName());
					listener.stateChanged(newState);
				}
			}
		}
	}
	@Override
	public String toString() {
		return m_name + "(" + m_listeners.size()  +":" +m_commandHandlers.size() + ":" + m_msgHandlers.size() + ")";
	}
	
	/**
	 * Base class for all message handlers. A message handler processes incoming
	 * Insteon messages and reacts by publishing corresponding messages on the openhab
	 * bus, updates device state etc.
	 * @author Daniel Pfrommer
	 */
	public static abstract class MessageHandler {
		DeviceFeature m_feature = null;
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
		 * @param cmd1 the insteon cmd1 field
		 * @param msg the received insteon message
		 * @param feature the DeviceFeature to which this message handler is attached
		 * @param fromPort the device (/dev/ttyUSB0) from which the message has been received
		 */
		public abstract void handleMessage(byte cmd1, Msg msg, DeviceFeature feature,
									String fromPort);
		
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
		 * Extract button information from message
		 * @param msg the message to extract from
		 * @param the device feature (needed for debug printing)
		 * @return the button number or -1 if no button found
		 */
		public int getButtonInfo(Msg msg, DeviceFeature f) {
			// the cleanup messages have the button number in the command2 field
			// the broadcast messages have it as the lsb of the toAddress
			try {
				int bclean = (int) (msg.getByte("command2") & 0xff);
				int bbcast = (int) (msg.getAddress("toAddress").getLowByte() & 0xff);
				int button = msg.isCleanup() ? bclean : bbcast;
				logger.debug("{} button: {} bclean: {} bbcast: {}",
						f.getDevice().getAddress(), button, bclean, bbcast);
				return button;
			}  catch (FieldException e) {
				logger.error("field exception while parsing msg {}: ", msg, e);
			}
			return -1;
		}

	}
	
	public static class DefaultMsgHandler extends MessageHandler {
		DefaultMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			logger.debug("drop unimpl message {}: {}", Utils.getHexByte(cmd1), msg);
		}
	}

	public static class NoOpMsgHandler extends MessageHandler {
		NoOpMsgHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			logger.debug("ignore msg {}: {}", Utils.getHexByte(cmd1), msg);
		}
	}

	public static class LightOnDimmerHandler extends MessageHandler {
		LightOnDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			// There are two ways we can get cmd1 == 0x11 messages:
			// 1) When we query (poll). In this case, we get a DIRECT_ACK back,
			//    and the cmd2 code has the new light level
			// 2) When the switch/dimmer is switched completely on manually,
			//    i.e. by physically tapping the button. 
			try {
				InsteonAddress a = f.getDevice().getAddress();
				if (msg.isAckOfDirect()) {
					 // got this in response to query, check cmd2 for light level
					int cmd2 = (int) (msg.getByte("command2") & 0xff);
					if (cmd2 > 0) {
						if (cmd2 == 0xff) {
							// only if it's fully on should we send
							// an ON status message
							logger.info("LightOnDimmerHandler: device {} was turned fully on", a);
							m_feature.publishAll(OnOffType.ON);
						} else {
							int level = Math.max(1, (cmd2*100)/255);
							logger.info("LightOnDimmerHandler: device {} was set to level {}", a, level);
							m_feature.publishAll(new PercentType(level));
						}
					} else {
						logger.info("LightOnDimmerHandler: device {} was turned fully off", a);
						m_feature.publishAll(OnOffType.OFF);
					}
				} else {
					// if we get this via broadcast, ignore the light level and just switch on
					m_feature.publishAll(OnOffType.ON);
				}
			}  catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}
	public static class LightOnSwitchHandler extends MessageHandler {
		LightOnSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			f.publishAll(OnOffType.ON);
		}
	}
	public static class LightOffHandler extends MessageHandler {
		LightOffHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			f.publishAll(OnOffType.OFF);
		}
	}
	public static class LightOnMultiHandler extends MessageHandler {
		LightOnMultiHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			int button = this.getButtonInfo(msg, f);
			if (button != -1) {
				f.publishFiltered(OnOffType.ON, "button", button);
			}
		}
	}
	public static class LightOffMultiHandler extends MessageHandler {
		LightOffMultiHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			int button = this.getButtonInfo(msg, f);
			if (button != -1) {
				f.publishFiltered(OnOffType.OFF, "button", button);
			}
		}
	}
	
	/**
	 * A message handler which reads the command2 field
	 * if command2 == 0xFF then the light has been turned on
	 * else if command2 == 0x00 then the light has been turned off
	 * it should ideally be mapped to the 0x19 command byte so that it reads
	 * the status request acks sent back by the switch
	 */
	public static class LightStateSwitchHandler extends  MessageHandler {
		LightStateSwitchHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			try {
				InsteonAddress a = f.getDevice().getAddress();
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				if (cmd2 == 0) {
					logger.info("LightStateSwitchHandler: set device {} to OFF", a);
					m_feature.publishAll(OnOffType.OFF);
				} else if (cmd2 == 0xff) {
					logger.info("LightStateSwitchHandler: set device {} to ON", a);
					m_feature.publishAll(OnOffType.ON);
				} else {
					logger.warn("LightStateSwitchHandler: {} ignoring unexpected" +
							" cmd2 in msg: {}", a, msg);
				}
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}
	/**
	 * Similar to the LightStateSwitchHandler, but for a dimmer
	 * In the dimmers case the command2 byte represents the light level from 0-255
	 * @see LightStateSwitchHandler
	 */
	public static class LightStateDimmerHandler extends  MessageHandler {
		LightStateDimmerHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			InsteonDevice dev = f.getDevice();
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);

				int level = cmd2*100/255;
				if (level == 0 && cmd2 > 0) level = 1;
				if (cmd2 == 0) {
					logger.info("LightStateDimmerHandler: set device {} to OFF",
							dev.getAddress());
					m_feature.publishAll(OnOffType.OFF);
				} else if (cmd2 == 0xff) {
					logger.info("LightStateDimmerHandler: set device {} to ON",
							dev.getAddress());
					m_feature.publishAll(OnOffType.ON);
				} else {
					logger.info("LightStateDimmerHandler: set device {} to level {}",
							dev.getAddress(), level);
				}
				m_feature.publishAll(new PercentType(level));
			} catch (FieldException e) {
				logger.error("error parsing {}: ", msg, e);
			}
		}
	}
	
	public static class StopManualChangeHandler extends MessageHandler {
		StopManualChangeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			Msg m = f.makePollMsg();
			if (m != null)	f.getDevice().enqueueMessage(m, f);
		}
	}
	
	public static class InfoRequestReplyHandler extends MessageHandler {
		InfoRequestReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.warn("device {} expected extended msg as info reply, got {}", dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int prodKey = msg.getInt24("userData2", "userData3", "userData4");
					int devCat  = msg.getByte("userData5");
					int subCat  = msg.getByte("userData6");
					logger.info("{} got product data: cat: {} subcat: {} key: {} ", dev.getAddress(), devCat, subCat,
							Utils.getHexString(prodKey));
					break;
				case 0x02: // this is a device text string response message
					logger.info("{} got text str {} ", dev.getAddress(), msg);
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

	public static class MotionSensorLightReplyHandler extends MessageHandler {
		MotionSensorLightReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.trace("device {} ignoring non-extended msg {}", dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int lightLevel = msg.getByte("userData11") & 0xff;
					logger.debug("{} got light level {}", dev.getAddress(), lightLevel);
					m_feature.publishAll(new DecimalType(lightLevel));
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
	public static class MotionSensorBatteryReplyHandler extends MessageHandler {
		MotionSensorBatteryReplyHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			InsteonDevice dev = f.getDevice();
			if (!msg.isExtended()) {
				logger.warn("device {} expected extended msg as info reply, got {}", dev.getAddress(), msg);
				return;
			}
			try {
				int cmd2 = (int) (msg.getByte("command2") & 0xff);
				switch (cmd2) {
				case 0x00: // this is a product data response message
					int batteryLevel = msg.getByte("userData12") & 0xff;
					logger.debug("{} got battery level {}", dev.getAddress(), batteryLevel);
					m_feature.publishAll(new DecimalType(batteryLevel));
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

	public static class LastTimeHandler extends MessageHandler {
		LastTimeHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1a, Msg msg, DeviceFeature f,
					String fromPort) {
			GregorianCalendar calendar = new GregorianCalendar();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        DateTimeType t = new DateTimeType(calendar);
			m_feature.publishAll(t);
		}
	}

	
	public static class StateContactHandler extends MessageHandler {
		StateContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1a, Msg msg, DeviceFeature f,
					String fromPort) {
			byte cmd  = 0x00;
			byte cmd2 = 0x00;
			try {
				cmd = msg.getByte("Cmd");
				cmd2 = msg.getByte("command2");
			} catch (FieldException e) {
				logger.debug("no cmd found, dropping msg {}", msg);
				return;
			}
			if (msg.isAckOfDirect() && (f.getQueryStatus() == QueryStatus.QUERY_PENDING)
					&& cmd == 0x50) {
				OpenClosedType oc = (cmd2 == 0) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
				logger.info("StateContactHandler: set contact {} to: {}", f.getDevice().getAddress(), oc);
				m_feature.publishAll(oc);
			}
		}
	}

	public static class ClosedContactHandler extends MessageHandler {
		ClosedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			m_feature.publishAll(OpenClosedType.CLOSED);
		}
	}

	public static class OpenedContactHandler extends MessageHandler {
		OpenedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			m_feature.publishAll(OpenClosedType.OPEN);
		}
	}

	public static class OpenedOrClosedContactHandler extends MessageHandler {
		OpenedOrClosedContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			if (cmd1 != 0x11) return;
			try {
				byte cmd2 = msg.getByte("command2");
				switch (cmd2) {
				case 0x02:
					m_feature.publishAll(OpenClosedType.CLOSED);
					break;
				case 0x01:
					m_feature.publishAll(OpenClosedType.OPEN);
					break;
				default: // do nothing
					break;
				}
			} catch (FieldException e) {
				logger.debug("no cmd2 found, dropping msg {}", msg);
				return;
			}

		}
	}

	public static class ClosedSleepingContactHandler extends MessageHandler {
		ClosedSleepingContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			m_feature.publishAll(OpenClosedType.CLOSED);
			sendExtendedQuery(f, (byte)0x2e, (byte) 00);
		}
	}

	public static class OpenedSleepingContactHandler extends MessageHandler {
		OpenedSleepingContactHandler(DeviceFeature p) { super(p); }
		@Override
		public void handleMessage(byte cmd1, Msg msg, DeviceFeature f,
				String fromPort) {
			m_feature.publishAll(OpenClosedType.OPEN);
			sendExtendedQuery(f, (byte)0x2e, (byte) 00);
		}
	}

	/**
	 * A PollHandler creates an Insteon message to query a particular
	 * DeviceFeature of an Insteon device.  
	 */
	public static abstract class PollHandler {
		DeviceFeature m_feature = null;
		/**
		 * Constructor
		 * @param feature The device feature being polled
		 */
		PollHandler(DeviceFeature feature) {
			m_feature = feature;
		}		
		/**
		 * Creates Insteon message that can be used to poll a feature
		 * via the Insteon network.
		 * @param device reference to the insteon device to be polled
		 * @return Insteon query message or null if creation failed
		 */
		public abstract Msg makeMsg(InsteonDevice device);
	}
	
	public static class DefaultPollHandler extends PollHandler {
		DefaultPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				m = d.makeStandardMessage((byte)0x0f, (byte)0x19, (byte)0x00);
				m.setQuietTime(500L);
			} catch (FieldException e) {
				logger.warn("error setting field in msg: ", e);
			} catch (IOException e) {
				logger.error("poll failed with exception ", e);
			}
			return m;
		}
	}
	
	public static class ContactPollHandler extends PollHandler {
		ContactPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				// setting cmd2 = 0x01 will return the LED bit flags
				// rather than the relay status!
				m = d.makeStandardMessage((byte)0x0f, (byte)0x19, (byte)0x01);
				m.setQuietTime(500L);
			} catch (FieldException e) {
				logger.warn("error setting field in msg: ", e);
			} catch (IOException e) {
				logger.error("poll failed with exception ", e);
			}
			return m;
		}
	}

	public static class NoPollHandler extends PollHandler {
		NoPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			return null;
		}
	}
	/**
	 * A command handler translates an openHAB command into a insteon message
	 *
	 */
	public static abstract class CommandHandler {
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
		 * @param cmd the openhab command issued
		 * @param device the Insteon device to which this command applies
		 */
		public abstract void handleCommand(Command cmd, InsteonDevice device);
	}
	

	public static class WarnCommandHandler extends CommandHandler {
		WarnCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(Command cmd, InsteonDevice dev) {
			logger.warn("command {} is not implemented yet!", cmd);
		}
	}
	
	public static class NoOpCommandHandler extends CommandHandler {
		NoOpCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(Command cmd, InsteonDevice dev) {
			// do nothing, not even log
		}
	}
	
	public static class LightOnOffCommandHandler extends CommandHandler {
		LightOnOffCommandHandler(DeviceFeature f) { super(f); }
		@Override
		public void handleCommand(Command cmd, InsteonDevice dev) {
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
		public void handleCommand(Command cmd, InsteonDevice dev) {
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
		public void handleCommand(Command cmd, InsteonDevice dev) {
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
		public void handleCommand(Command cmd, InsteonDevice dev) {
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
		public void handleCommand(Command cmd, InsteonDevice modem) {
			if (!(cmd instanceof DecimalType)) return;
			DecimalType d = (DecimalType)cmd;
			int num = d.intValue();
			if (num != 1) return;
			Driver dr = m_feature.getDevice().getDriver();
			HashMap<InsteonAddress, InsteonDevice> devs = dr.getDeviceList();
			String erasedDevice = null;
			for (InsteonDevice dev : devs.values()) {
				if (!dev.isModem() && !dev.isInItemsFile()) {
					logger.info("ModemCommandHandler: erasing {} from modem link database", dev.getAddress());
					dev.eraseFromModem();
					erasedDevice = dev.getAddress().toString();
					break; // only one at a time
				}
			}
			if (erasedDevice == null) {
				logger.info("no devices erased from link database");
			}
		}
	}
	/**
	 * Does preprocessing of messages to decide which handler should be called
	 *
	 */
	public static abstract class MessageDispatcher {
		DeviceFeature m_feature = null;
		/**
		 * Constructor
		 * @param f DeviceFeature to which this MessageDispatcher belongs
		 */
		MessageDispatcher(DeviceFeature f) {
			m_feature = f;
		}
		/**
		 * Dispatches message
		 * @param msg Message to dispatch
		 * @param port Insteon device ('/dev/usb') from which the message came
		 * @return true if dispatch was successful, false otherwise
		 */
		public abstract boolean dispatch(Msg msg, String port);
	}

	private static class DefaultDispatcher extends MessageDispatcher {
		DefaultDispatcher(DeviceFeature f) { super(f); }
		@Override
		public boolean dispatch(Msg msg, String port) {
			byte cmd  = 0x00;
			byte cmd1 = 0x00;
			boolean isConsumed = false;
			int key = -1;
			try {
				cmd  = msg.getByte("Cmd");
				cmd1 = msg.getByte("command1");
			} catch (FieldException e) {
				logger.debug("no command found, dropping msg {}", msg);
				return isConsumed;
			}
			if (msg.isAckOfDirect()) {
				// in the case of direct ack, the cmd1 code is useless.
				// you have to know what message was sent before to
				// interpret the reply message
				if (m_feature.getQueryStatus() == QueryStatus.QUERY_PENDING
						&& cmd == 0x50) {
					// must be a reply to our message, tweak the cmd1 code!
					logger.trace("changing key to 0x19 for msg {}", msg);
					key = 0x19; // we have installed a handler under that command number
					isConsumed = true;
				} else {
					key = -1;
				}
			} else {
				key = (cmd1 & 0xFF);
			}
			if (key != -1 || m_feature.isStatusFeature()) {
				MessageHandler h = m_feature.m_msgHandlers.get(key);
				if (h == null) h = m_feature.m_defaultMsgHandler;
				logger.trace("{}:{}->{} {}", m_feature.getDevice().getAddress(), m_feature.getName(),
						h.getClass().getSimpleName(), msg);
				h.handleMessage(cmd1, msg, m_feature, port);
			}
			if (isConsumed) {
				m_feature.setQueryStatus(QueryStatus.QUERY_ANSWERED);
			}

			return isConsumed;
		}
	}
	private static class GreedyDispatcher extends MessageDispatcher {
		GreedyDispatcher(DeviceFeature f) { super(f); }
		@Override
		public boolean dispatch(Msg msg, String port) {
			byte cmd1 = 0x00;
			boolean isConsumed = false;
			try {
				cmd1 = msg.getByte("command1");
			} catch (FieldException e) {
				logger.debug("no cmd1 found, dropping msg {}", msg);
				return isConsumed;
			}
			int key = (cmd1 & 0xFF);
			MessageHandler h = m_feature.m_msgHandlers.get(key);
			if (h == null) h = m_feature.m_defaultMsgHandler;
			logger.trace("{}:{}->{} {}", m_feature.getDevice().getAddress(), m_feature.getName(),
						h.getClass().getSimpleName(), msg);
			h.handleMessage(cmd1, msg, m_feature, port);
			return isConsumed;
		}
	}
	
	
	private static class PassThroughDispatcher extends MessageDispatcher {
		PassThroughDispatcher(DeviceFeature f) { super(f); }
		@Override
		public boolean dispatch(Msg msg, String port) {
			MessageHandler h = m_feature.m_defaultMsgHandler;
			logger.trace("{}:{}->{} {}", m_feature.getDevice().getAddress(), m_feature.getName(),
					h.getClass().getSimpleName(), msg);
			h.handleMessage((byte)0x01, msg, m_feature, port);
			return false;
		}
	}
	
	/**
	 * Factory method for creating DeviceFeatures.
	 * @param s The name of the device feature to create.
	 * @return The newly created DeviceFeature, or null if requested DeviceFeature does not exist.
	 */
	
	public static DeviceFeature s_makeDeviceFeature(String s) {
		DeviceFeature f = null;
		if (s.equals("PLACEHOLDER")) {
			f = new DeviceFeature("PLACEHOLDER");
		} else if (s_features.containsKey(s)) {
			f = s_features.get(s).build();
		} else {
			logger.error("unimplemented feature requested: {}", s);
		}
		return f;
	}

	/**
	 * Factory method for creating device dispatchers given a name.
	 * The name is usually the class name.
	 * @param name the name of the dispatcher to create
	 * @param f the feature for which to create the dispatcher
	 * @return the dispatcher which was created
	 */
	public static MessageDispatcher s_makeMessageDispatcher(String name, DeviceFeature f) {
		if (name.equals("PassThroughDispatcher")) return new PassThroughDispatcher(f);
		else if (name.equals("DefaultDispatcher")) return new DefaultDispatcher(f);
		else if (name.equals("GreedyDispatcher")) return new GreedyDispatcher(f);
		else {
			logger.error("unimplemented message dispatcher requested: {}", name);
			return null;
		}
	}
	/**
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T> T s_makeHandler(String name, DeviceFeature f) {
		String cname = DeviceFeature.class.getName() + "$" + name;
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
