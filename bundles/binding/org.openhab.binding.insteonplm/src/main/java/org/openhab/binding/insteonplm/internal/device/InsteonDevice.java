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
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.openhab.binding.insteonplm.InsteonPLMBindingConfig;
import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * The InsteonDevice class holds known per-device state of a single Insteon device,
 * including the address, what port(modem) to reach it on etc.
 * Note that some Insteon devices de facto consist of two devices (let's say
 * a relay and a sensor), but operate under the same address. Such devices will
 * be represented just by a single InsteonDevice. Their different personalities
 * will then be represented by DeviceFeatures.
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class InsteonDevice {
	private static final Logger logger = LoggerFactory.getLogger(InsteonDevice.class);

	public static enum DeviceStatus {
		INITIALIZED,
		POLLING
	}

	private InsteonAddress				m_address		= new InsteonAddress();
	private ArrayList<String>			m_ports			= new ArrayList<String>();
	private long						m_pollInterval	= -1L; // in milliseconds
	private Driver						m_driver		= null;
	private HashMap<String, DeviceFeature> 	m_features	= new HashMap<String, DeviceFeature>();
	private String						m_productKey	= null;
	private Long						m_lastTimePolled = 0L;
	private Long						m_lastMsgReceived = 0L;
	private boolean						m_isModem		= false;
	private	 Deque<QEntry>				m_requestQueue  = new LinkedList<QEntry>();
	private	boolean						m_hasModemDBEntry = false;
	private DeviceStatus				m_status		= DeviceStatus.INITIALIZED;
	
	/**
	 * Constructor
	 */
	public InsteonDevice() {
		m_lastMsgReceived = System.currentTimeMillis();
	}

	// --------------------- simple getters -----------------------------

	public boolean			hasProductKey()		{ return m_productKey != null; }
	public String			getProductKey()		{ return m_productKey; }
	public boolean 			hasModemDBEntry()	{ return m_hasModemDBEntry; }
	public DeviceStatus 	getStatus()			{ return m_status; }
	public InsteonAddress	getAddress() 		{ return (m_address);	}
	public Driver			getDriver()			{ return m_driver; }
	public boolean 			hasValidPorts()		{ return (!m_ports.isEmpty());	}
	public long				getPollInterval()	{ return m_pollInterval; }
	public boolean			isModem()	 		{ return m_isModem; }
	public DeviceFeature	getFeature(String f) { 	return m_features.get(f);	}
	public HashMap<String, DeviceFeature> getFeatures() { return m_features; }
	public boolean			hasProductKey(String key) {
		return m_productKey != null && m_productKey.equals(key);
	}
	public boolean			hasValidPollingInterval() {
		return (m_pollInterval > 0);
	}
	public long 			getPollOverDueTime() {
		return (m_lastTimePolled - m_lastMsgReceived);
	}
	
	public String 			getPort() throws IOException {
		if (m_ports.isEmpty()) throw new IOException("no ports configured for instrument " + getAddress());
		return (m_ports.iterator().next());
	}
	public boolean 			hasAnyListeners() {
		synchronized (m_features) {
			for (DeviceFeature f: m_features.values()) {
				if (f.hasListeners()) return true;
			}
		}
		return false;
	}
	// --------------------- simple setters -----------------------------	

	public void setStatus(DeviceStatus aI)		{ m_status = aI; }
	public void setHasModemDBEntry(boolean b)	{ m_hasModemDBEntry = b; }
	public void setAddress(InsteonAddress ia)	{ m_address = ia; }
	public void setDriver(Driver d) 			{ m_driver = d; }
	public void setIsModem(boolean f)  			{ m_isModem = f; }
	public void setProductKey(String pk)		{ m_productKey = pk; }
	public void setPollInterval(long pi) {
		logger.trace("setting poll interval for {} to {} ", m_address, pi);
		if (pi > 0) m_pollInterval = pi;
	}

	/**
	 * Add a port. Currently only a single port is being used.
	 * @param p the port to add
	 */
	public void addPort(String p) {
		if (p == null) return;
		if (!m_ports.contains(p)) {
			m_ports.add(p);
		}
	}

	/**
	 * Removes feature listener from this device
	 * @param aItemName name of the feature listener to remove
	 * @return true if a feature listener was successfully removed
	 */
	public boolean removeFeatureListener(String aItemName) {
		boolean removedListener = false;
		synchronized (m_features) {
			for (Iterator<Entry<String, DeviceFeature>> it = m_features.entrySet().iterator(); it.hasNext();) {				
				DeviceFeature f = it.next().getValue();
				if (f.removeListener(aItemName)) {
					removedListener = true;
				}
			}
		}
		return removedListener;
	}
	/**
	 * Invoked to process an openHAB command
	 * @param driver The driver to use
	 * @param c The item configuration
	 * @param command The actual command to execute
	 */
	public void processCommand(Driver driver, InsteonPLMBindingConfig c, Command command) {
		logger.debug("processing command {} features: {}", command, m_features.size());
		synchronized(m_features) {
			for (DeviceFeature i : m_features.values()) {
				i.handleCommand(c, command);
			}
		}
	}

	/**
	 * Execute poll on this device: create an array of messages,
	 * add them to the request queue, and schedule the queue
	 * for processing.
	 */
	public void doPoll() {
		ArrayList<QEntry> l = new ArrayList<QEntry>();
		synchronized(m_features) {
			for (DeviceFeature i : m_features.values()) {
				if (i.hasListeners()) {
					Msg m = i.makePollMsg();
					if (m != null) l.add(new QEntry(i, m));
				}
			}
		}
		if (l.isEmpty()) return;
		synchronized (m_requestQueue) {
			for (QEntry e : l) {
				m_requestQueue.add(e);
			}
		}
		long now = System.currentTimeMillis();
		RequestQueueManager.s_instance().addQueue(this, now);
		
		if (!l.isEmpty()) {
			synchronized(m_lastTimePolled) {
				m_lastTimePolled = now;
			}
		}
	}

	/**
	 * Handle incoming message for this device by forwarding
	 * it to all features that this device supports 	
	 * @param fromPort port from which the message come in
	 * @param msg the incoming message
	 */
	public void handleMessage(String fromPort, Msg msg) {
		synchronized (m_lastMsgReceived) {
			m_lastMsgReceived = System.currentTimeMillis();
		}
		synchronized(m_features) {
			// first update all features that are
			// not status features
			for (DeviceFeature f : m_features.values()) {
				if (!f.isStatusFeature()) {
					if (f.handleMessage(msg, fromPort)) {
						break;
					}
				}
			}
			// then update all the status features,
			// e.g. when the device was last updated
			for (DeviceFeature f : m_features.values()) {
				if (f.isStatusFeature()) {
					f.handleMessage(msg, fromPort);
				}
			}
		}
	}

	/**
	 * Helper method to make standard message
	 * @param flags
	 * @param cmd1
	 * @param cmd2
	 * @return standard message
	 * @throws FieldException
	 * @throws IOException
	 */
	public Msg makeStandardMessage(byte flags, byte cmd1, byte cmd2)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendStandardMessage");
		m.setAddress("toAddress", getAddress());
		m.setByte("messageFlags", flags);
		m.setByte("command1", cmd1);
		m.setByte("command2", cmd2);
		return m;
	}

	/**
	 * Helper method to make extended message
	 * @param flags
	 * @param cmd1
	 * @param cmd2
	 * @return extended message
	 * @throws FieldException
	 * @throws IOException
	 */
	public Msg makeExtendedMessage(byte flags, byte cmd1, byte cmd2)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendExtendedMessage");
		m.setAddress("toAddress", getAddress());
		m.setByte("messageFlags", (byte) (((flags & 0xff) | 0x10) & 0xff));
		m.setByte("command1", cmd1);
		m.setByte("command2", cmd2);
		int checksum = (~(cmd1 + cmd2) + 1) &0xff;
		m.setByte("userData14", (byte)checksum);
		return m;
	}

	/**
	 * Called by the RequestQueueManager when the queue has expired
	 * @param timeNow
	 * @return time when to schedule the next message (timeNow + quietTime)
	 */
	public long processRequestQueue(long timeNow) {
		synchronized (m_requestQueue) {
			if (m_requestQueue.isEmpty()) {
				return 0L;
			}
			QEntry qe = m_requestQueue.poll();
			qe.getFeature().setQueryStatus(DeviceFeature.QueryStatus.QUERY_PENDING);
			long quietTime = qe.getMsg().getQuietTime();
			qe.getMsg().setQuietTime(500L); // rate limiting downstream:
			try {
				writeMessage(qe.getMsg());
			} catch (IOException e) {
				logger.error("message write failed for msg {}", qe.getMsg(), e);
			}
			return (timeNow + quietTime);
		}
	}
	/**
	 * Enqueues message to be sent at the next possible time
	 * @param m message to be sent
	 * @param f device feature that sent this message (so we can associate the response message with it)
	 */
	public void enqueueMessage(Msg m, DeviceFeature f) {
		synchronized (m_requestQueue) {
			m_requestQueue.add(new QEntry(f, m));
		}
		long now = System.currentTimeMillis();
		RequestQueueManager.s_instance().addQueue(this, now);
	}
	
	private void writeMessage(Msg m) throws IOException {
		m_driver.writeMessage(getPort(), m);
	}

	private void instantiateFeatures(DeviceType dt) {
		for (Entry<String,String> fe : dt.getFeatures().entrySet()) {
			DeviceFeature f = DeviceFeature.s_makeDeviceFeature(fe.getValue());
			if (f == null) {
				logger.error("device type {} references unknown feature: {}", dt, fe.getValue());
			} else {
				addFeature(fe.getKey(), f);
			}
		}
	}
	
	private void addFeature(String name, DeviceFeature f) {
		f.setDevice(this);
		synchronized (m_features) {
			m_features.put(name, f);
		}
	}


	@Override
	public String toString() {
		String s = m_address.toString();
		for (Entry<String, DeviceFeature> f : m_features.entrySet()) {
			s += "|" + f.getKey() + "->" + f.getValue().toString();
		}
		return s;
	}
	/**
	 * Factory method
	 * @param dt device type after which to model the device
	 * @return newly created device
	 */
	public static InsteonDevice s_makeDevice(DeviceType dt) {
		InsteonDevice dev = new InsteonDevice();
		dev.instantiateFeatures(dt);
		return dev;
	}
	
	/**
	 * Queue entry helper class
	 * @author Bernd Pfrommer
	 */
	public static class QEntry {
		private DeviceFeature	m_feature	= null;
		private Msg 			m_msg		= null;
		public DeviceFeature getFeature() { return m_feature; }
		public Msg getMsg() { return m_msg; }
		QEntry(DeviceFeature f, Msg m) {
			m_feature	= f;
			m_msg		= m;
		}
	}
}
