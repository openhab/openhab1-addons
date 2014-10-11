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
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PollHandler creates an Insteon message to query a particular
 * DeviceFeature of an Insteon device.
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public abstract class PollHandler {
	private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);		
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
	 * Factory method for creating handlers of a given name using java reflection
	 * @param name the name of the handler to create
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends PollHandler> T s_makeHandler(String name, DeviceFeature f) {
		String cname = PollHandler.class.getName() + "$" + name;
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
