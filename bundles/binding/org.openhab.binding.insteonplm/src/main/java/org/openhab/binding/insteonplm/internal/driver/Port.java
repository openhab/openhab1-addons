/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.insteonplm.internal.device.DeviceDescriptor;
import org.openhab.binding.insteonplm.internal.device.DeviceListBuilder;
import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice.InitStatus;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgFactory;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The Port class represents a serial port and thereby its connected Insteon modem.
 * It does the initialization of the serial port, and (via its inner classes SerialReader and SerialWriter)
 * manages the reading/writing of messages on the Insteon network.
 * 
 * The SerialReader and SerialWriter class combined implement the somewhat tricky flow control protocol.
 * In combination with the MsgFactory class, the incoming data stream is turned into a Msg structure
 * for further processing by the upper layers (MsgListeners).
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class Port {
	/**
	 * A write queue is maintained to pace the flow of outgoing messages. Sending messages back-to-back
	 * can lead to dropped messages.
	 */
	enum ReplyType {
		GOT_ACK,
		WAITING_FOR_ACK,
		GOT_NACK
	}
	private static final Logger logger = LoggerFactory.getLogger(Port.class);

	private	String			m_devName	= "INVALID";
	private	String			m_logName	= "INVALID";
	private Modem			m_modem		= null;
	private SerialReader	m_reader	= null;
	private	SerialWriter	m_writer	= null;
	private	SerialPort		m_port		= null;
	private	InputStream		m_in		= null;
	private	OutputStream	m_out		= null;
	private	final String	m_appName	= "PLM";
	private final int		m_speed		= 19200; // baud rate
	private	final int		m_readSize	= 1024; // read buffer size
	private	Thread			m_readThread  = null;
	private	Thread			m_writeThread = null;
	private	boolean			m_running	  = false;
	private boolean		m_deviceListComplete = false;
	private MsgFactory		m_msgFactory = new MsgFactory();
	private Driver			m_driver	 = null;
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
		m_reader	= new SerialReader();
		m_writer	= new SerialWriter();
	}

	public boolean 			isRunning() 	{ return m_running; }
	public synchronized boolean isDeviceListComplete() { return (m_deviceListComplete); }
	public InsteonAddress	getAddress()	{ return m_modem.getAddress(); } 
	public String			getDeviceName()	{ return m_devName; }
	public Driver			getDriver()		{ return m_driver; }


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
	
	public void start() {
		if (m_running) {
			logger.debug("port {} already running, not started again", m_logName);
		}
		try {
			/* by default, RXTX searches only devices /dev/ttyS* and
			 * /dev/ttyUSB*, and will so not find symlinks. The
			 *  setProperty() call below helps 
			 */
			System.setProperty("gnu.io.rxtx.SerialPorts", m_devName);
			CommPortIdentifier ci =
					CommPortIdentifier.getPortIdentifier(m_devName);
			CommPort cp = ci.open(m_appName, 1000);
			if (cp instanceof SerialPort) {
				m_port = (SerialPort)cp;
			} else {
				throw new IllegalStateException("unknown port type");
			}
			m_port.setSerialPortParams(m_speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			m_port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			logger.debug("setting port speed to {}", m_speed);
			m_port.disableReceiveFraming();
			m_port.disableReceiveThreshold();
			m_in	= m_port.getInputStream();
			m_out	= m_port.getOutputStream();
			m_readThread	= new Thread(m_reader);
			m_writeThread	= new Thread(m_writer);
			m_readThread.start();
			m_writeThread.start();
			m_modem.initialize();
			DeviceListBuilder dlb = new DeviceListBuilder(this);
			dlb.start(); // start downloading the device list
			m_running = true;
		} catch (IOException e) {
			logger.error("cannot open port: {}, got IOException ", m_logName, e);
		} catch (PortInUseException e) {
			logger.error("cannot open port: {}, it is in use!", m_logName);
		} catch (UnsupportedCommOperationException e) {
			logger.error("got unsupported operation {} on port {}",
					e.getMessage(), m_logName);
		} catch (NoSuchPortException e) {
			logger.error("got no such port for {}", m_logName);
		} catch (IllegalStateException e) {
			logger.error("got unknown port type for {}", m_logName);
		} finally {
			if (!m_running) {
				logger.error("failed to open port {}", m_logName);
			} else {
				logger.info("successfully opened port {}", m_logName);
			}
		}
	}
	public void stop() {
		if (!m_running) {
			logger.debug("port {} not running, no need to stop it", m_logName);
			return;
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
		if (m_port != null) {
			m_port.close();
		}
		m_running = false;
		synchronized (m_listeners) {
			m_listeners.clear();
		}
	}
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

	public void deviceListComplete() {
		synchronized (this) {
			m_deviceListComplete = true;
		}
		m_driver.deviceListComplete(this);
	}
	
	public HashMap<InsteonAddress, InsteonDevice> getDeviceList() {
		return m_driver.getDeviceList();
	}
	/**
	 * The SerialReader uses the MsgFactory to turn the incoming bytes into
	 * Msgs for the listeners. It also communicates with the SerialWriter
	 * via a synchronized objects to implement flow control (tell the SerialWriter
	 * that it needs to retransmit, or the message has been received correctly).
	 * 
	 * @author pfrommer
	 */
	class SerialReader implements Runnable {
		private ReplyType m_reply = ReplyType.GOT_ACK;
		@Override
		public void run() {
			byte[] buffer = new byte[2 * m_readSize];
			int len = -1;
			try	{
				while ((len = m_in.read(buffer, 0, m_readSize)) > -1) {
					m_msgFactory.addData(buffer, len);
					// must call processData() until we get a null pointer back
					for (Msg m = m_msgFactory.processData(); m != null;
							m = m_msgFactory.processData()) {
							toAllListeners(m);
						notifyWaiters(m);
					}
				}
			} catch (IOException e)	{
				e.printStackTrace();
				logger.error("got io exception on port {}, port is no longer read!", m_logName);
			}            
		}
		private synchronized void notifyWaiters(Msg msg) {
			if (m_reply == ReplyType.WAITING_FOR_ACK) {
				if (!msg.isUnsolicited()) {
					m_reply = (msg.isPureNack() ? ReplyType.GOT_NACK : ReplyType.GOT_ACK);
					logger.trace("signaling receipt of ack: {}", (m_reply == ReplyType.GOT_ACK));
					notify();
				} else if (msg.isPureNack()){
					m_reply = ReplyType.GOT_NACK;
					logger.trace("signaling receipt of pure nack");
					notify();
				} else {
					logger.trace("got unsolicited message");
				}
			}
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
		 * Called by SerialWriter for flow control.
		 * @return true if retransmission is necessary
		 */
		public synchronized boolean waitForReply() {
			m_reply = ReplyType.WAITING_FOR_ACK;
			while (m_reply == ReplyType.WAITING_FOR_ACK) {
				try {
					logger.trace("writer waiting for ack.");
					wait();
					logger.trace("writer got ack: {}", (m_reply == ReplyType.GOT_ACK));
				} catch (InterruptedException e) {
					// do nothing
				}
			}
			return (m_reply == ReplyType.GOT_NACK);
		}
	}
	/**
	 * Writes messages to the serial port. Flow control is implemented following Insteon
	 * documents to avoid over running the modem.
	 */
	class SerialWriter implements Runnable {
		private static final int WAIT_TIME = 200; // milliseconds
		@Override
		public void run() {
			logger.debug("Starting writer...");
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
						//Thread.sleep(500);
						m_out.write(msg.getData());
						while (m_reader.waitForReply()) {
							Thread.sleep(WAIT_TIME);
							logger.trace("retransmitting msg: {}", msg);
							m_out.write(msg.getData());
						}
						// if rate limited, need to sleep now.
						if (msg.getQuietTime() > 0) {
							Thread.sleep(msg.getQuietTime());
						}
					}
				} catch (InterruptedException e) {
					logger.error("got interrupted exception in write thread:", e);
				} catch (IOException e) {
					logger.error("got i/o exception in write thread:", e);
					try { Thread.sleep(30000);} catch (InterruptedException ie) {	}
				} catch (Exception e) {
					logger.error("got exception in write thread:", e);
				}
			}
		}
	}
	/**
	 * Class to get info about the modem
	 */
	class Modem implements MsgListener {
		private InsteonAddress m_address = new InsteonAddress("00.00.00");
		InsteonAddress getAddress() { return m_address; }
		@Override
		public void msg(Msg msg, String fromPort) {
			try {
				if (msg.isPureNack()) return;
				if (msg.getByte("Cmd") == 0x60) {
					// add the modem to the device list
					InsteonAddress a = new InsteonAddress(msg.getAddress("IMAddress"));
					m_address = a;
					InsteonDevice dev = InsteonDevice.s_makeDevice(a, m_driver);
					HashMap<InsteonAddress, InsteonDevice> devices = m_driver.getDeviceList();
					int devCat	= msg.getByte("DeviceCategory");
					int subCat	= msg.getByte("DeviceSubCategory");
					int vers	= msg.getByte("FirmwareVersion");
					DeviceDescriptor d = DeviceDescriptor.s_getDeviceDescriptor(devCat, subCat, vers);
					dev.addDescriptor(d);
					dev.setInitStatus(InitStatus.INITIALIZED);
					dev.setIsModem(true);
					synchronized (devices) {
						devices.put(a, dev);
					}
					// can unsubscribe now
					removeListener(this);
					logger.debug("modem address: {} ident: {}", a, dev.toString());
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
