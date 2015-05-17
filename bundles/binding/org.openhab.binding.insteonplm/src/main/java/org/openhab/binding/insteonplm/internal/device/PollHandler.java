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
	private static final Logger logger = LoggerFactory.getLogger(PollHandler.class);		
	DeviceFeature m_feature = null;
	HashMap<String, String> m_parameters = new HashMap<String, String>();
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

	public void setParameters(HashMap<String, String> hm) { m_parameters = hm; }
	
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
	/**
	 * Handler for e.g. the KeypadLinc devices which have multiple
	 * LED buttons. By sending cmd1 = 0x19, cmd2 = 0x01, the status
	 * of the individual LED buttons will be returned in the response 
	 * @author Bernd Pfrommer
	 *
	 */
	public static class LEDBitPollHandler extends PollHandler {
		LEDBitPollHandler(DeviceFeature f) { super(f); }
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

	public static class PowerMeterPollHandler extends PollHandler {
		PowerMeterPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				m = d.makeStandardMessage((byte)0x0f, (byte)0x82, (byte)0x00);
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
	
	public static class ThermostatTemperaturePollHandler extends PollHandler {
		ThermostatTemperaturePollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				m = d.makeStandardMessage((byte)0x0f, (byte)0x6a, (byte)0x00);
				m.setQuietTime(500L);
			} catch (FieldException e) {
				logger.warn("error setting field in msg: ", e);
			} catch (IOException e) {
				logger.error("poll failed with exception ", e);
			}
			return m;
		}
	}
	
	public static class ThermostatHeatCoolSetPointPollHandler extends PollHandler {
		ThermostatHeatCoolSetPointPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				m = d.makeExtendedMessage((byte) 0x1f, (byte) 0x2e, (byte) 0x00);
				m.setByte("userData1", (byte) 0x01);
				m.setByte("userData3", (byte) 0x01);
				m.setByte("userData14", (byte)0x00);
				m.setQuietTime(500L);
			} catch (FieldException e) {
				logger.warn("error setting field in msg: ", e);
			} catch (IOException e) {
				logger.error("poll failed with exception ", e);
			}
			return m;
		}
	}
	
	public static class ThermostatHumidityPollHandler extends PollHandler {
		ThermostatHumidityPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			try {
				m = d.makeStandardMessage((byte)0x0f, (byte)0x6a, (byte)0x60);
				m.setQuietTime(500L);
			} catch (FieldException e) {
				logger.warn("error setting field in msg: ", e);
			} catch (IOException e) {
				logger.error("poll failed with exception ", e);
			}
			return m;
		}
	}
	
	/**
	 * Factory method for creating handlers of a given name using java reflection
	 * @param m_pollHandler the name of the handler to create
	 * @param f the feature for which to create the handler
	 * @return the handler which was created
	 */
	public static <T extends PollHandler> T s_makeHandler(HandlerEntry ph, DeviceFeature f) {
		String cname = PollHandler.class.getName() + "$" + ph.getName();
		try {
			Class<?> c = Class.forName(cname);
			@SuppressWarnings("unchecked")
			Class<? extends T> dc = (Class <? extends T>) c;
			T phc = dc.getDeclaredConstructor(DeviceFeature.class).newInstance(f);
			phc.setParameters(ph.getParams());
			return phc;
		} catch (Exception e) {
			logger.error("error trying to create message handler: {}", ph.getName(), e);
		}
		return null;
	}
}
