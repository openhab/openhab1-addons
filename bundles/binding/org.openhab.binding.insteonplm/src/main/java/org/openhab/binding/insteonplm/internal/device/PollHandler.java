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
import org.openhab.binding.insteonplm.internal.utils.Utils;
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
	
	/**
	 * Returns parameter as integer
	 * @param key key of parameter
	 * @param def default
	 * @return value of parameter
	 */
	protected int getIntParameter(String key, int def) {
		String val = m_parameters.get(key);
		if (val == null) return (def); // param not found
		int ret = def;
		try {
			ret = Utils.strToInt(val);
		} catch (NumberFormatException e) {
			logger.error("malformed int parameter in command handler: {}", key);
		}
		return ret;
	}
	/**
	 * A flexible, parameterized poll handler that can generate
	 * most query messages. Provide the suitable parameters in
	 * the device features file.
	 */

	public static class FlexPollHandler extends PollHandler {
		FlexPollHandler(DeviceFeature f) { super(f); }
		@Override
		public Msg makeMsg(InsteonDevice d) {
			Msg m = null;
			int cmd1 = getIntParameter("cmd1", 0);
			int cmd2 = getIntParameter("cmd2", 0);
			int ext  = getIntParameter("ext", -1);
			try {
				if (ext == 1 || ext == 2) {
					int d1   = getIntParameter("d1", 0);
					int d2   = getIntParameter("d2", 0);
					int d3   = getIntParameter("d3", 0);
					m = d.makeExtendedMessage((byte) 0x0f, (byte)cmd1, (byte)cmd2,
							new byte [] {(byte)d1, (byte)d2, (byte)d3});
					if (ext == 1) m.setCRC();
					else if (ext == 2) m.setCRC2();
				} else {
					m = d.makeStandardMessage((byte) 0x0f, (byte)cmd1, (byte)cmd2);
				}
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
	 * @param ph the name of the handler to create
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
