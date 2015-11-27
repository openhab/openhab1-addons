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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.openhab.binding.insteonplm.InsteonPLMBindingConfig;
import org.openhab.binding.insteonplm.internal.device.DeviceType.FeatureGroup;
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
	private	PriorityQueue<QEntry>		m_requestQueue  = new PriorityQueue<QEntry>();
	private	DeviceFeature				m_featureQueried = null;
	/** need to wait after query to avoid misinterpretation of duplicate replies */
	private static final int			QUIET_TIME_DIRECT_MESSAGE = 2000;
	/** how far to space out poll messages */
	private static final int			TIME_BETWEEN_POLL_MESSAGES = 1500;
	private long						m_lastQueryTime	= 0L;					
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
	public InsteonAddress	getAddress() 		{ return (m_address); }
	public Driver			getDriver()			{ return m_driver; }
	public boolean 			hasValidPorts()		{ return (!m_ports.isEmpty());	}
	public long				getPollInterval()	{ return m_pollInterval; }
	public boolean			isModem()	 		{ return m_isModem; }
	public DeviceFeature	getFeature(String f) { 	return m_features.get(f);	}
	public HashMap<String, DeviceFeature> getFeatures() { return m_features; }
	public byte				getX10HouseCode()	{ return (m_address.getX10HouseCode()); }
	public byte				getX10UnitCode()	{ return (m_address.getX10UnitCode()); }

	
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
	public void setFeatureQueried(DeviceFeature f) {
		synchronized (m_requestQueue) {
			m_featureQueried = f;
		}
	};
	public DeviceFeature getFeatureQueried() {
		synchronized (m_requestQueue) {
			return (m_featureQueried);
		}
	};

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
				if (i.isReferencedByItem(c.getItemName())) {
					i.handleCommand(c, command);
				}
			}
		}
	}

	/**
	 * Execute poll on this device: create an array of messages,
	 * add them to the request queue, and schedule the queue
	 * for processing.
	 * @param delay scheduling delay (in milliseconds)
	 */
	public void doPoll(long delay) {
		long now = System.currentTimeMillis();
		ArrayList<QEntry> l = new ArrayList<QEntry>();
		synchronized(m_features) {
			int spacing = 0;
			for (DeviceFeature i : m_features.values()) {
				if (i.hasListeners()) {
					Msg m = i.makePollMsg();
					if (m != null) {
						l.add(new QEntry(i, m, now + delay + spacing));
						spacing += TIME_BETWEEN_POLL_MESSAGES;
					}
				}
			}
		}
		if (l.isEmpty()) return;
		synchronized (m_requestQueue) {
			for (QEntry e : l) {
				m_requestQueue.add(e);
			}
		}
		RequestQueueManager.s_instance().addQueue(this, now + delay);
		
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
						// handled a reply to a query,
						// mark it as processed
						logger.trace("handled reply of direct: {}", f);
						setFeatureQueried(null);
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
		return (makeStandardMessage(flags, cmd1, cmd2, -1));
	}
	/**
	 * Helper method to make standard message, possibly with group
	 * @param flags
	 * @param cmd1
	 * @param cmd2
	 * @param group (-1 if not a group message)
	 * @return standard message
	 * @throws FieldException
	 * @throws IOException
	 */
	public Msg makeStandardMessage(byte flags, byte cmd1, byte cmd2, int group)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendStandardMessage");
		InsteonAddress addr = null;
		if (group != -1) {
			flags |= 0xc0; // mark message as group message
			// and stash the group number into the address
			addr = new InsteonAddress((byte) 0, (byte) 0, (byte) (group & 0xff));
		} else {
			addr = getAddress();
		}
		m.setAddress("toAddress", addr);
		m.setByte("messageFlags", flags);
		m.setByte("command1", cmd1);
		m.setByte("command2", cmd2);
		return m;
	}
	
	public Msg makeX10Message(byte rawX10, byte X10Flag)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendX10Message");
		m.setByte("rawX10", rawX10);
		m.setByte("X10Flag", X10Flag);
		m.setQuietTime(300L);
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
		return makeExtendedMessage(flags, cmd1, cmd2, new byte[] {});
	}
	/**
	 * Helper method to make extended message
	 * @param flags
	 * @param cmd1
	 * @param cmd2
	 * @param data array with userdata
	 * @return extended message
	 * @throws FieldException
	 * @throws IOException
	 */
	public Msg makeExtendedMessage(byte flags, byte cmd1, byte cmd2, byte[] data)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendExtendedMessage");
		m.setAddress("toAddress", getAddress());
		m.setByte("messageFlags", (byte) (((flags & 0xff) | 0x10) & 0xff));
		m.setByte("command1", cmd1);
		m.setByte("command2", cmd2);
		m.setUserData(data);
		m.setCRC();
		return m;
	}
	/**
	 * Helper method to make extended message, but with different CRC calculation
	 * @param flags
	 * @param cmd1
	 * @param cmd2
	 * @param data array with user data
	 * @return extended message
	 * @throws FieldException
	 * @throws IOException
	 */
	public Msg makeExtendedMessageCRC2(byte flags, byte cmd1, byte cmd2, byte[] data)
			throws FieldException, IOException {
		Msg m = Msg.s_makeMessage("SendExtendedMessage");
		m.setAddress("toAddress", getAddress());
		m.setByte("messageFlags", (byte) (((flags & 0xff) | 0x10) & 0xff));
		m.setByte("command1", cmd1);
		m.setByte("command2", cmd2);
		m.setUserData(data);
		m.setCRC2();
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
			if (m_featureQueried != null) {
				// A feature has been queried, but
				// the response has not been digested yet.
				// Must wait for the query to be processed.
				long dt = timeNow - (m_lastQueryTime + m_featureQueried.getDirectAckTimeout());
				if (dt < 0) {
					logger.debug("still waiting for query reply from {} for another {} usec",
							m_address, -dt);
					return (timeNow + 2000L); // retry soon
				} else {
					logger.debug("gave up waiting for query reply from device {}", m_address);
				}
			}
			QEntry qe = m_requestQueue.poll(); // take it off the queue!
			if (!qe.getMsg().isBroadcast()) {
				logger.debug("qe taken off direct: {} {}", qe.getFeature(), qe.getMsg());
				m_lastQueryTime = timeNow;
				// mark feature as pending
				qe.getFeature().setQueryStatus(DeviceFeature.QueryStatus.QUERY_PENDING);
				// also mark this queue as pending so there is no doubt
				m_featureQueried = qe.getFeature();
			} else {
				logger.debug("qe taken off bcast: {} {}", qe.getFeature(), qe.getMsg());
			}
			long quietTime = qe.getMsg().getQuietTime();
			qe.getMsg().setQuietTime(500L); // rate limiting downstream!
			try {
				writeMessage(qe.getMsg());
			} catch (IOException e) {
				logger.error("message write failed for msg {}", qe.getMsg(), e);
			}
			// figure out when the request queue should be checked next
			QEntry qnext = m_requestQueue.peek();
			long nextExpTime = (qnext == null ? 0L : qnext.getExpirationTime());
			long nextTime = Math.max(timeNow + quietTime, nextExpTime);
			logger.debug("next request queue processed in {} msec, quiettime = {}",
						 nextTime - timeNow, quietTime);
			return (nextTime);
		}
	}
	/**
	 * Enqueues message to be sent at the next possible time
	 * @param m message to be sent
	 * @param f device feature that sent this message (so we can associate the response message with it)
	 */
	public void enqueueMessage(Msg m, DeviceFeature f) {
		enqueueDelayedMessage(m, f, 0);
	}

	/**
	 * Enqueues message to be sent after a delay
	 * @param m message to be sent
	 * @param f device feature that sent this message (so we can associate the response message with it)
	 * @param d time (in milliseconds)to delay before enqueuing message
	 */
	public void enqueueDelayedMessage(Msg m, DeviceFeature f, long delay) {
		long now = System.currentTimeMillis();
		synchronized (m_requestQueue) {
			m_requestQueue.add(new QEntry(f, m, now + delay));
		}
		if (!m.isBroadcast()) {
			m.setQuietTime(QUIET_TIME_DIRECT_MESSAGE);
		}
		logger.trace("enqueing direct message with delay {}", delay);
		RequestQueueManager.s_instance().addQueue(this, now + delay);
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
		for (Entry<String, FeatureGroup> fe : dt.getFeatureGroups().entrySet()) {
			FeatureGroup fg = fe.getValue();
			DeviceFeature f = DeviceFeature.s_makeDeviceFeature(fg.getType());
			if (f == null) {
				logger.error("device type {} references unknown feature group: {}",
						dt, fg.getType());
			} else {
				addFeature(fe.getKey(), f);
			}
			connectFeatures(fe.getKey(), f, fg.getFeatures());
		}
	}

	private void connectFeatures(String gn, DeviceFeature fg, ArrayList<String> features) {
		for (String fs : features) {
			DeviceFeature f = m_features.get(fs);
			if (f == null) {
				logger.error("feature group {} references unknown feature {}", gn, fs);
			} else {
				logger.debug("{} connected feature: {}", gn, f);
				fg.addConnectedFeature(f);
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
	public static class QEntry implements Comparable<QEntry> {
		private DeviceFeature	m_feature	= null;
		private Msg 			m_msg		= null;
		private long			m_expirationTime = 0L;
		public DeviceFeature getFeature() { return m_feature; }
		public Msg getMsg() { return m_msg; }
		public long getExpirationTime() { return m_expirationTime; }
		QEntry(DeviceFeature f, Msg m, long t) {
			m_feature			= f;
			m_msg				= m;
			m_expirationTime	= t;
		}
		@Override
		public int compareTo(QEntry a) {
			return (int)(m_expirationTime - a.m_expirationTime);
		}
	}
}
