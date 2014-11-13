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
import java.util.GregorianCalendar;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
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
	protected int getButtonInfo(Msg msg, DeviceFeature f) {
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
			if (msg.isAckOfDirect() && (f.getQueryStatus() == DeviceFeature.QueryStatus.QUERY_PENDING)
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
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends MessageHandler> T s_makeHandler(String name, DeviceFeature f) {
		String cname = MessageHandler.class.getName() + "$" + name;
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