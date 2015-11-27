/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.insteonplm.internal.device.ModemDBBuilder;
import org.openhab.binding.insteonplm.internal.device.DeviceType;
import org.openhab.binding.insteonplm.internal.device.DeviceTypeLoader;
import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgFactory;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The Port class represents a port, that is a connection to either an Insteon modem either through
 * a serial or USB port, or via an Insteon Hub.
 * It does the initialization of the port, and (via its inner classes IOStreamReader and IOStreamWriter)
 * manages the reading/writing of messages on the Insteon network.
 * 
 * The IOStreamReader and IOStreamWriter class combined implement the somewhat tricky flow control protocol.
 * In combination with the MsgFactory class, the incoming data stream is turned into a Msg structure
 * for further processing by the upper layers (MsgListeners).
 *
 * A write queue is maintained to pace the flow of outgoing messages. Sending messages back-to-back
 * can lead to dropped messages.
 *
 *
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class Port {
	private static final Logger logger = LoggerFactory.getLogger(Port.class);

	/**
	 * The ReplyType is used to keep track of the state of the serial port receiver
	 */
	enum ReplyType {
		GOT_ACK,
		WAITING_FOR_ACK,
		GOT_NACK
	}

	private IOStream		m_ioStream	= null;
	private	String			m_devName	= "INVALID";
	private	String			m_logName	= "INVALID";
	private Modem			m_modem		= null;
	private IOStreamReader	m_reader	= null;
	private	IOStreamWriter	m_writer	= null;
	private	final int		m_readSize	= 1024; // read buffer size
	private	Thread			m_readThread  = null;
	private	Thread			m_writeThread = null;
	private	boolean			m_running	  = false;
	private boolean			m_modemDBComplete = false;
	private MsgFactory		m_msgFactory = new MsgFactory();
	private Driver			m_driver	 = null;
	private ModemDBBuilder	m_mdbb 		 = null;
	private ArrayList<MsgListener>	 m_listeners = new ArrayList<MsgListener>();
	private LinkedBlockingQueue<Msg> m_writeQueue = new LinkedBlockingQueue<Msg>();

	/**
	 * Constructor
	 * @param devName the name of the port, i.e. '/dev/insteon'
	 * @param d The Driver object that manages this port
	 */
	public Port(String devName, Driver d) {
		m_devName	= devName;
		m_driver	= d;
		m_logName	= devName;
		m_modem		= new Modem();
		addListener(m_modem);
		m_ioStream 	= IOStream.s_create(devName);
		m_reader	= new IOStreamReader();
		m_writer	= new IOStreamWriter();
		m_mdbb 		= new ModemDBBuilder(this);
	}

	public synchronized boolean isModemDBComplete() { return (m_modemDBComplete); }
	public boolean 			isRunning() 	{ return m_running; }
	public InsteonAddress	getAddress()	{ return m_modem.getAddress(); }
	public String			getDeviceName()	{ return m_devName; }
	public Driver			getDriver()		{ return m_driver; }


	public void setModemDBRetryTimeout(int timeout) {
		m_mdbb.setRetryTimeout(timeout);
	}

	public void addListener (MsgListener l) {
		synchronized(m_listeners) {
			if (!m_listeners.contains(l)) m_listeners.add(l);
		}
	}
	
	public void removeListener(MsgListener l) {
		synchronized(m_listeners) {
			if (m_listeners.remove(l)) {
				// logger.debug("removed listener from port");
			}
		}
	}

	/**
	 * Clear modem database that has been queried so far.
	 */
	public void clearModemDB() {
		logger.debug("clearing modem db!");
		HashMap<InsteonAddress, ModemDBEntry> dbes = getDriver().lockModemDBEntries();
		dbes.clear();
		getDriver().unlockModemDBEntries();
	}
	
	/**
	 * Starts threads necessary for reading and writing
	 */
	public void start() {
		logger.debug("starting port {}", m_logName);
		if (m_running) {
			logger.debug("port {} already running, not started again", m_logName);
		}
		if (!m_ioStream.open()) {
			logger.debug("failed to open port {}", m_logName);
			return;
		}
		m_readThread	= new Thread(m_reader);
		m_writeThread	= new Thread(m_writer);
		m_readThread.setName(m_logName + " Reader");
		m_writeThread.setName(m_logName + " Writer");
		m_readThread.start();
		m_writeThread.start();
		m_modem.initialize();
		m_mdbb.start(); // start downloading the device list
		m_running = true;
	}

	/**
	 * Stops all threads
	 */
	public void stop() {
		if (!m_running) {
			logger.debug("port {} not running, no need to stop it", m_logName);
			return;
		}
		if (m_mdbb != null) {
			m_mdbb = null;
		}
		if (m_readThread != null) m_readThread.interrupt();
		if (m_writeThread != null) m_writeThread.interrupt();
		logger.debug("waiting for read thread to exit for port {}",
				m_logName);
		try {
			if (m_readThread != null) m_readThread.join();
		} catch (InterruptedException e) {
			logger.debug("got interrupted waiting for read thread to exit.");
		}
		logger.debug("waiting for write thread to exit for port {}",
				m_logName);
		try {
			if (m_writeThread != null) m_writeThread.join();
		} catch (InterruptedException e) {
			logger.debug("got interrupted waiting for write thread to exit.");
		}
		logger.debug("all threads for port {} stopped.", m_logName);
		m_ioStream.close();
		m_running = false;
		synchronized (m_listeners) {
			m_listeners.clear();
		}
	}
	/**
	 * Adds message to the write queue
	 * @param m message to be added to the write queue
	 * @throws IOException
	 */
	public void writeMessage(Msg m) throws IOException {
		if (m == null) {
			logger.error("trying to write null message!");
			throw new IOException("trying to write null message!");
		}
		if (m.getData() == null) {
			logger.error("trying to write message without data!");
			throw new IOException("trying to write message without data!");
		}
		try {
			m_writeQueue.add(m);
			logger.trace("enqueued msg: {}", m);
		} catch (IllegalStateException e) {
			logger.error("cannot write message {}, write queue is full!", m);
		}
		
	}

	/**
	 * Gets called by the modem database builder when the modem database is complete
	 */
	public void modemDBComplete() {
		synchronized (this) {
			m_modemDBComplete = true;
		}
		m_driver.modemDBComplete(this);
	}

	/**
	 * The IOStreamReader uses the MsgFactory to turn the incoming bytes into
	 * Msgs for the listeners. It also communicates with the IOStreamWriter
	 * to implement flow control (tell the IOStreamWriter that it needs to retransmit,
	 * or the reply message has been received correctly).
	 * 
	 * @author Bernd Pfrommer
	 */
	class IOStreamReader implements Runnable {
		
		private ReplyType	m_reply = ReplyType.GOT_ACK;
		private	Object		m_replyLock = new Object();
		private	boolean		m_dropRandomBytes = false; // set to true for fault injection
		/**
		 * Helper function for implementing synchronization between reader and writer
		 * @return reference to the RequesReplyLock
		 */
		public	Object getRequestReplyLock() { return m_replyLock; }

		@Override
		public void run() {
			logger.debug("starting reader...");
			byte[] buffer = new byte[2 * m_readSize];
			Random rng	  = new Random();
			try {
				for (int len = -1; (len = m_ioStream.read(buffer, 0, m_readSize)) > 0;) {
					if (m_dropRandomBytes && rng.nextInt(100) < 20) {
						len = dropBytes(buffer, len);
					}
					m_msgFactory.addData(buffer, len);
					processMessages();
				}
			} catch (InterruptedException e) {
				logger.debug("reader thread got interrupted!");
			}
			logger.error("reader thread exiting!");
		}
		private void processMessages() {
			try {
				// must call processData() until we get a null pointer back
				for (Msg m = m_msgFactory.processData(); m != null;
						m = m_msgFactory.processData()) {
						toAllListeners(m);
						notifyWriter(m);
				}
			} catch (IOException e) {
				// got bad data from modem,
				// unblock those waiting for ack
				logger.warn("bad data received: {}", e.getMessage());
				synchronized (getRequestReplyLock()) {
					if (m_reply == ReplyType.WAITING_FOR_ACK) {
						logger.warn("got bad data back, must assume message was acked.");
						m_reply = ReplyType.GOT_ACK;
						getRequestReplyLock().notify();
					}
				}
			}
		}

		private void notifyWriter(Msg msg) {
			synchronized (getRequestReplyLock()) {
				if (m_reply == ReplyType.WAITING_FOR_ACK) {
					if (!msg.isUnsolicited()) {
						m_reply = (msg.isPureNack() ? ReplyType.GOT_NACK : ReplyType.GOT_ACK);
						logger.trace("signaling receipt of ack: {}", (m_reply == ReplyType.GOT_ACK));
						getRequestReplyLock().notify();
					} else if (msg.isPureNack()){
						m_reply = ReplyType.GOT_NACK;
						logger.trace("signaling receipt of pure nack");
						getRequestReplyLock().notify();
					} else {
						logger.trace("got unsolicited message");
					}
				}
			}
		}

		/**
		 * Drops bytes randomly from buffer to simulate errors seen
		 * from the InsteonHub using the raw interface
		 * @param buffer byte buffer from which to drop bytes
		 * @param len original number of valid bytes in buffer
		 * @return length of byte buffer after dropping from it
		 */
		private int dropBytes(byte [] buffer, int len) {
			final int DROP_RATE = 2; // in percent
			Random rng	  = new Random();
			ArrayList<Byte> l = new ArrayList<Byte>();
			for (int i = 0; i < len; i++) {
				if (rng.nextInt(100) >= DROP_RATE) {
					l.add(new Byte(buffer[i]));
				}
			}
			for (int i = 0; i < l.size(); i++) {
				buffer[i] = l.get(i);
			}
			return (l.size());
		}

		@SuppressWarnings("unchecked")
		private void toAllListeners(Msg msg) {
			// When we deliver the message, the recipient
			// may in turn call removeListener() or addListener(),
			// thereby corrupting the very same list we are iterating
			// through. That's why we make a copy of it, and
			// iterate through the copy.
			ArrayList<MsgListener> tempList = null;
			synchronized(m_listeners) {
				tempList= (ArrayList<MsgListener>) m_listeners.clone();
			}
			for (MsgListener l : tempList) {
				l.msg(msg, m_devName); // deliver msg to listener
			}
		}
		
		/**
		 * Blocking wait for ack or nack from modem.
		 * Called by IOStreamWriter for flow control.
		 * @return true if retransmission is necessary
		 */
		public boolean waitForReply() {
			m_reply = ReplyType.WAITING_FOR_ACK;
			while (m_reply == ReplyType.WAITING_FOR_ACK) {
				try {
					logger.trace("writer waiting for ack.");
					// There have been cases observed, in particular for
					// the Hub, where we get no ack or nack back, causing the binding
					// to hang in the wait() below, because unsolicited messages
					// do not trigger a notify(). For this reason we request retransmission
					// if the wait() times out.
					getRequestReplyLock().wait(30000); // be patient for 30 msec
					if (m_reply == ReplyType.WAITING_FOR_ACK) { // timeout expired without getting ACK or NACK
						logger.trace("writer timeout expired, asking for retransmit!");
						m_reply = ReplyType.GOT_NACK;
						break;
					} else {
						logger.trace("writer got ack: {}", (m_reply == ReplyType.GOT_ACK));
					}
				} catch (InterruptedException e) {
					break; // done for the day...
				}
			}
			return (m_reply == ReplyType.GOT_NACK);
		}
	}
	/**
	 * Writes messages to the port. Flow control is implemented following Insteon
	 * documents to avoid over running the modem.
	 * 
	 * @author Bernd Pfrommer
	 */
	class IOStreamWriter implements Runnable {
		private static final int WAIT_TIME = 200; // milliseconds
		@Override
		public void run() {
			logger.debug("starting writer...");
			while(true) {
				try {
					// this call blocks until the lock on the queue is released
					logger.trace("writer checking message queue");
					Msg msg = m_writeQueue.take();
					if (msg.getData() == null) {
						logger.error("found null message in write queue!");
					} else {
						logger.debug("writing ({}): {}", msg.getQuietTime(), msg);
						// To debug race conditions during startup (i.e. make the .items
						// file definitions be available *before* the modem link records,
						// slow down the modem traffic with the following statement:
						// Thread.sleep(500);
						synchronized (m_reader.getRequestReplyLock()) {
							m_ioStream.write(msg.getData());
							while (m_reader.waitForReply()) {
								Thread.sleep(WAIT_TIME);
								logger.trace("retransmitting msg: {}", msg);
								m_ioStream.write(msg.getData());
							}
							
						}
						// if rate limited, need to sleep now.
						if (msg.getQuietTime() > 0) {
							Thread.sleep(msg.getQuietTime());
						}
					}
				} catch (InterruptedException e) {
					logger.error("got interrupted exception in write thread");
					break;
				} catch (Exception e) {
					logger.error("got exception in write thread:", e);
				}
			}
			logger.debug("exiting writer thread!");
		}
	}
	/**
	 * Class to get info about the modem
	 */
	class Modem implements MsgListener {
		private InsteonDevice m_device = null;
		InsteonAddress getAddress() { return (m_device == null) ? new InsteonAddress() : (m_device.getAddress()); }
		InsteonDevice getDevice() { return m_device; }
		@Override
		public void msg(Msg msg, String fromPort) {
			try {
				if (msg.isPureNack()) return;
				if (msg.getByte("Cmd") == 0x60) {
					// add the modem to the device list
					InsteonAddress a = new InsteonAddress(msg.getAddress("IMAddress"));
					String prodKey = "0x000045";
					DeviceType dt = DeviceTypeLoader.s_instance().getDeviceType(prodKey);
					if (dt == null) {
						logger.error("unknown modem product key: {} for modem: {}.", prodKey, a);
					} else {
						m_device =	InsteonDevice.s_makeDevice(dt);
						m_device.setAddress(a);
						m_device.setProductKey(prodKey);
						m_device.setDriver(m_driver);
						m_device.setIsModem(true);
						m_device.addPort(fromPort);
						logger.debug("found modem {} in device_types: {}", a, m_device.toString());
						m_mdbb.updateModemDB(a, Port.this, null);
					}
					// can unsubscribe now
					removeListener(this);
				}
			} catch (FieldException e) {
				logger.error("error parsing im info reply field: ", e);
			}
		}
		
		
		public void initialize() {
			try {
				Msg m = Msg.s_makeMessage("GetIMInfo");
				writeMessage(m);
			} catch (IOException e) {
				logger.error("modem init failed!", e);
			}
		}
	}
}
