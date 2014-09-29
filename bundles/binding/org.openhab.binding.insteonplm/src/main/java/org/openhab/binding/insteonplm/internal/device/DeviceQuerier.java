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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.driver.Port;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Queries the devices for product information such as category, subcategory, version
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class DeviceQuerier implements MsgListener {
	private static final Logger logger = LoggerFactory.getLogger(DeviceQuerier.class);
	private TreeSet<String> m_ports	= new TreeSet<String>();
	private HashMap<InsteonAddress, InsteonDevice> m_nonResponders = new HashMap<InsteonAddress, InsteonDevice>();
	private HashMap<InsteonAddress, InsteonDevice>	m_devices = null;
	private Driver m_driver = null;
	private Thread m_queryThread = null;
	private final long MSEC_BETWEEN_QUERIES = 5000L;
	private DeviceQueryListener m_listener = null;
	
	
	public DeviceQuerier(HashMap<InsteonAddress, InsteonDevice> devices,
			Driver d, DeviceQueryListener l) {
		m_devices = devices;
		m_driver = d;
		m_listener = l;
	}
	
	public void start() {
		m_queryThread = new Thread(new Querier());
		m_queryThread.start();
	}

	public void addDevice(InsteonDevice d) {
		if (d.isModem()) return;
		synchronized (m_nonResponders) {
			if (!m_nonResponders.containsKey(d)) {
				logger.debug("put dev {} on query list", d);
				m_nonResponders.put(d.getAddress(), d);
				try {
					if (!m_ports.contains(d.getPort())) {
						m_driver.addMsgListener(this, d.getPort());
					}
				} catch (IOException e) {
					logger.error("failed to set up querying:", e);
				}
				m_nonResponders.notify();
			}
		}
	}
	
	/**
	 * processes product information messages from the modem.
	 * @see org.openhab.binding.insteonplm.internal.message.MsgListener#msg(org.openhab.binding.insteonplm.internal.message.Msg, java.lang.String)
	 */
	@Override
	public void msg(Msg msg, String fromPort) {
		if (msg.isEcho() || msg.isPureNack()) return;
		if (!msg.isExtended()) {
			// 
			// switchlinc devices first send an ACK_OF_DIRECT standard message with
			// cmd1 = 0x03, cmd2 =0x00
			// we simply ignore this message
			return;
		}
		try {
			InsteonAddress toAddr = msg.getAddr("toAddress");
			if (!msg.isBroadcast() && !m_driver.isMsgForUs(toAddr)) {
				return;
			}
			InsteonAddress fromAddr = msg.getAddr("fromAddress");
			if (fromAddr == null) {
				logger.debug("invalid fromAddress, ignoring msg {}", msg);
				return;
			}
			int cmd2 = (int) (msg.getByte("command2") & 0xff);
			switch (cmd2) {
			case 0x00: // this is a product data response message
				int prodKey = msg.getInt24("userData2", "userData3", "userData4");
				int devCat  = msg.getByte("userData5");
				int subCat  = msg.getByte("userData6");
				int vers    = msg.getByte("userData7");
				synchronized (m_nonResponders) {
					InsteonDevice dev = m_nonResponders.get(fromAddr);
					if (dev != null) {
						synchronized (m_devices) {
							dev.setProductInfo(prodKey, devCat, subCat, vers);
							m_nonResponders.notify();
						}
					}
				}
				if (m_listener != null) {
					m_listener.deviceQueryComplete(fromAddr);
				}
				break;
			}
		} catch (FieldException e) {
			logger.error("error parsing product info msg {}: ", msg, e);
		}
	}
	
	private class Querier implements Runnable {
		@Override
		public void run() {
			logger.debug("starting query thread...");
			synchronized (m_nonResponders) {
				while (true) {
					while (!m_nonResponders.isEmpty()) {
						// query all devices that have not responded
						// need to make a deep copy because m_nonResponders
						// may change during wait in sendQueryMsg()
						InsteonDevice[] nonresp = m_nonResponders.values().toArray(new InsteonDevice[0]);
						logger.debug("query array is size {}", nonresp.length);
						for (InsteonDevice d : nonresp) {
							sendQueryMsg(d);
						}
						removeResponders();
					}
					try {
						logger.debug("waiting for query to be added to list");
						m_nonResponders.wait();
					} catch (InterruptedException e) {
						logger.debug("ignoring interruption of query thread");
					}
				}
			}
		}
		private void removeResponders() {
			Iterator<Map.Entry<InsteonAddress, InsteonDevice>> i;
			for (i = m_nonResponders.entrySet().iterator(); i.hasNext();) {
				Map.Entry<InsteonAddress, InsteonDevice> me = i.next();
				if (me.getValue().hasRespondedToQuery()) {
					logger.info("device {} queried successfully", me.getValue());
					i.remove();
				}
			}
		}
		public void sendQueryMsg(InsteonDevice d) {
			try {
				Msg m = d.makeStandardMessage((byte) 0x0f, (byte) 0x03, (byte) 0x00);
				
				Port p = m_driver.getPort(d.getPort());
				if (p != null) {
					logger.debug("sending query msg {}", m);
					p.writeMessage(m);
					m_nonResponders.wait(MSEC_BETWEEN_QUERIES);
					logger.debug("query msg wait complete");
				} else {
					logger.error("cannot query {} on unknown port {}", d, d.getPort());
				}
				
			} catch (IOException e) {
				logger.error("got io exception during query:", e);
			} catch (FieldException e) {
				logger.error("got field exception during query:", e);
			} catch (InterruptedException e) {
				logger.error("got interrupted while querying:", e);
			}
			
		}
	}
}
