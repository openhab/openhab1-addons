/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Does preprocessing of messages to decide which handler should be called.
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public abstract class MessageDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

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
				if (m_feature.getQueryStatus() == DeviceFeature.QueryStatus.QUERY_PENDING
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
				MessageHandler h = m_feature.getMsgHandlers().get(key);
				if (h == null) h = m_feature.getDefaultMsgHandler();
				logger.trace("{}:{}->{} {}", m_feature.getDevice().getAddress(), m_feature.getName(),
						h.getClass().getSimpleName(), msg);
				h.handleMessage(cmd1, msg, m_feature, port);
			}
			if (isConsumed) {
				m_feature.setQueryStatus(DeviceFeature.QueryStatus.QUERY_ANSWERED);
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
			MessageHandler h = m_feature.getMsgHandlers().get(key);
			if (h == null) h = m_feature.getDefaultMsgHandler();
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
			MessageHandler h = m_feature.getDefaultMsgHandler();
			logger.trace("{}:{}->{} {}", m_feature.getDevice().getAddress(), m_feature.getName(),
					h.getClass().getSimpleName(), msg);
			h.handleMessage((byte)0x01, msg, m_feature, port);
			return false;
		}
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
}
