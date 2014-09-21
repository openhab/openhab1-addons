/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;


import org.openhab.binding.alarmdecoder.AlarmDecoderBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The actual AlarmDecoderBinding
 * 
 * Implementing it as an ActiveBinding and using the refresh to reestablish connections that
 * might have broken.
 * 
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public class AlarmDecoderBinding extends AbstractActiveBinding<AlarmDecoderBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(AlarmDecoderBinding.class);

	/** straight copy of the connection string */
	private String m_connectString = null;
	/** hostname for the alarmdecoder process */
	private String	m_tcpHostName = null;
	/** port for the alarmdecoder process */
	private int	m_tcpPort = -1;
	/** name of serial device */
	private String m_serialDeviceName = null;
	/** Interval between attempts to reestablish the connection	 */
	private long refreshInterval = 10000;

	private BufferedReader m_reader = null;
	private Socket	m_socket = null;
	private SerialPort m_port = null;
	private Thread m_thread = null;
	private MsgReader m_msgReader = null;
	private static HashMap<String, ADMsgType> s_startToMsgType = new HashMap<String, ADMsgType>();
	// pretty disgusting to have a separate hash map to keep track of which
	// items have been updated. But where else to put per-item state?
	private HashMap<String, AlarmDecoderBindingConfig> m_unupdatedItems =
			new HashMap<String, AlarmDecoderBindingConfig>();
	
	@Override
	protected String getName() {
		return "AlarmDecoder binding";
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	public void execute() {
		// The framework calls this function once the binding has been configured,
		// so we use it as a hook to start the binding.
		// At the same time, should the socket get disconnected, it will
		// be reconnected when this method is called.
		synchronized (this) {
			if (m_socket == null && m_port == null) {
				connect();
			}
		}
	}
	/**
	 * Parses and stores the tcp configuration string of the binding configuration
	 * @param parts
	 * @throws ConfigurationException
	 */
	private void parseTcpConfig(String [] parts) throws ConfigurationException {
		if (parts.length != 3) {
			throw new ConfigurationException("alarmdecoder:connect", "need hostname and port separated by :");
		}
		m_tcpHostName = parts[1];
		try {
			m_tcpPort = Integer.parseInt(parts[2]);
		} catch (NumberFormatException e) {
			throw new ConfigurationException("alarmdecoder:connect", "tcp port not numeric!");
		}
		logger.debug("got tcp configuration: {}:{}", m_tcpHostName, m_tcpPort);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if (config == null) {
			throw new ConfigurationException("alarmdecoder:connect", "no config!");
		}
		try {
			m_connectString = (String) config.get("connect");
			if (m_connectString == null) {
				throw new ConfigurationException("alarmdecoder:connect", "no connect config in openhab.cfg!");
			}
			String [] parts = m_connectString.split(":");
			if (parts.length < 2) {
				throw new ConfigurationException("alarmdecoder:connect", "missing :, check openhab.cfg!");
			}
			if (parts[0].equals("tcp")) {
				parseTcpConfig(parts);
			} else if (parts[0].equals("serial")) {
				if (parts.length != 2) {
					throw new ConfigurationException("alarmdecoder:connect", "serial device name cannot have :");
				}
				m_serialDeviceName = parts[1];
			} else {
				throw new ConfigurationException("alarmdecoder:connect", "invalid parameter " + parts[0]);
			}

			String reconn = (String) config.get("reconnect");
			if (reconn != null && reconn.trim().length() > 0) {
				refreshInterval = Long.parseLong(reconn);
			}
			setProperlyConfigured(true);
		} catch (ConfigurationException e) {
			logger.error("configuration error: {} ", e.getMessage(), e);
			throw e;
		}

	}
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.warn("binding does not support sending commands yet!");
	}
	
	private synchronized void connect() {
		try {
			disconnect(); // make sure we have disconnected
			markAllItemsUnupdated();
			if (m_tcpHostName != null && m_tcpPort > 0) {
				m_socket = new Socket(m_tcpHostName, m_tcpPort);
				m_reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
				logger.info("connected to {}:{}", m_tcpHostName, m_tcpPort);
				startMsgReader();
			} else if (this.m_serialDeviceName != null) {
				/* by default, RXTX searches only devices /dev/ttyS* and
				 * /dev/ttyUSB*, and will so not find symlinks. The
				 *  setProperty() call below helps 
				 */
				System.setProperty("gnu.io.rxtx.SerialPorts", m_serialDeviceName);
				CommPortIdentifier ci =	CommPortIdentifier.getPortIdentifier(m_serialDeviceName);
				CommPort cp = ci.open("openhabalarmdecoder", 10000);
				if (cp == null) {
					throw new IllegalStateException("cannot open serial port!");
				}
				if (cp instanceof SerialPort) {
					m_port = (SerialPort)cp;
				} else {
					throw new IllegalStateException("unknown port type");
				}
				m_port.setSerialPortParams(19200, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				//m_port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				m_port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
				m_port.disableReceiveFraming();
				m_port.disableReceiveThreshold();
				m_reader = new BufferedReader(new InputStreamReader(m_port.getInputStream()));
				logger.info("connected to serial port: {}", m_serialDeviceName);
				startMsgReader();
			} else {
				logger.warn("alarmdecoder hostname or port not configured!");
			}
		} catch (PortInUseException e) {
			logger.error("cannot open serial port: {}, it is in use!", m_serialDeviceName);
		} catch (UnsupportedCommOperationException e) {
			logger.error("got unsupported operation {} on port {}",	e.getMessage(), m_serialDeviceName);
				} catch (NoSuchPortException e) {
					logger.error("got no such port for {}", m_serialDeviceName);
				} catch (IllegalStateException e) {
					logger.error("got unknown port type for {}", m_serialDeviceName);
		} catch (UnknownHostException e) {
			logger.error("unknown host name :{}: ", m_tcpHostName, e);
		} catch (IOException e) {
			logger.error("cannot open connection to {}", m_connectString);
		}
	}
	
	private void startMsgReader() {
		m_msgReader = new MsgReader();
		m_thread = new Thread(m_msgReader);
		m_thread.start();
	}
	
	private synchronized void disconnect() {
		stopThread();
		if (m_socket != null) {
			try {
				m_socket.close();
			} catch (IOException e) {
				logger.error("error when closing socket ", e);
			}
			m_socket = null;
		}
		if (m_port != null) {
			m_port.close();
			m_port = null;
		}
	}
	private void markAllItemsUnupdated() {
		logger.debug("marking all items as unknown");
		for (AlarmDecoderBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			logger.debug("unupdated items found: {}", items.size());
			for (Iterator<String> item = items.iterator(); item.hasNext();) {
				String s = item.next();
				AlarmDecoderBindingConfig c =	provider.getBindingConfig(s);
				m_unupdatedItems.put(s, c);
			}
		}
	}
	
	private void stopThread() {
		if (m_msgReader != null) {
			m_msgReader.stopRunning();
			m_msgReader = null;
		}
		if (m_thread != null) {
			try {
				// wait for thread to stop
				m_thread.interrupt();
				m_thread.join();
			} catch (InterruptedException e) {
				// do nothing
			}
			m_thread = null;
		}
	}
	
	class MsgReader implements Runnable {
		private boolean m_keepRunning = true;
		@Override
		public void run() {
			logger.debug("msg reader thread started");
			String msg;
			try {
				while ((msg = m_reader.readLine()) != null && m_keepRunning) {
					logger.debug("got msg: {}", msg);
					ADMsgType mt = s_getMsgType(msg);
					try {
						switch (mt) {
						case KPM:
							parseKeypadMessage(msg);
							break;
						case REL:
						case EXP:
							parseRelayOrExpanderMessage(mt, msg);
							break;
						case RFX:
							parseRFMessage(msg);
							break;
						case INVALID:
						default:
							break;
						}
					} catch  (MessageParseException e) {
						logger.error("{} while parsing message {}", e.getMessage(), msg);
					}
				}
				if (msg == null) {
					logger.error("null read from input stream!");
				}
			} catch (IOException e) {
				logger.error("I/O error while reading from stream: {}", e.getMessage());
				// mark connections as down so they get reestablished
				m_socket	= null;
				m_port		= null;
			}
			logger.debug("msg reader thread exited");
		}
		
		public void stopRunning() {
			m_keepRunning = false;
		}
	}
	private void parseKeypadMessage(String msg) throws MessageParseException {
		String parts[] = msg.split(",");
		if (parts.length != 4) {
			throw new MessageParseException("got invalid keypad msg");
		}
		if (parts[0].length() != 22) {
			throw new MessageParseException("bad keypad status length : " + parts[0].length());
		}
		try {
			int numeric = Integer.parseInt(parts[1]);
			int upper = Integer.parseInt(parts[0].substring(1,6), 2);
			int nbeeps = Integer.parseInt(parts[0].substring(6,7));
			int lower = Integer.parseInt(parts[0].substring(7,17), 2);
			int status = ((upper & 0x1F) << 13)  | ((nbeeps & 0x3) << 10) | lower;
			ArrayList<AlarmDecoderBindingConfig> bcl = getItems(ADMsgType.KPM, null, null);
			for (AlarmDecoderBindingConfig c : bcl) {
				if (c.hasFeature("zone")) {
					updateItem(c, new DecimalType(numeric));
				} else if (c.hasFeature("text")) {
					updateItem(c, new StringType(parts[3]));
				}  else if (c.hasFeature("beeps")) {
					updateItem(c, new DecimalType(nbeeps));
				}  else if (c.hasFeature("status")) {
					int bit = c.getIntParameter("bit", 0, 17, -1);
					if (bit >= 0) { // only pick a single bit
						int v = (status >> bit) & 0x1;
						updateItem(c, new DecimalType(v));
					} else { // pick all bits
						updateItem(c, new DecimalType(status));
					}
				} else if (c.hasFeature("contact")) {
					int bit = c.getIntParameter("bit", 0, 17, -1);
					if (bit >= 0) { // only pick a single bit
						int v = (status >> bit) & 0x1;
						updateItem(c, (v == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
					} else { // pick all bits
						logger.warn("ignoring item {}: it has contact without bit field",
									c.getItemName());
					}
				}
			}
			if ((status & (1 << 17)) != 0) {
				// the panel is clear, so we can assume that all contacts that we
				// have not heard from are open
				setUnupdatedItemsToDefault();
			}
		} catch (NumberFormatException e) {
			throw new MessageParseException("keypad msg contains invalid number: " + e.getMessage());
		}
	}

	
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		logger.trace("binding changed for {}", itemName);
		AlarmDecoderBindingConfig c = ((AlarmDecoderBindingProvider)provider).getBindingConfig(itemName);
		// careful, the config reference c is a null pointer!
		m_unupdatedItems.put(itemName, c);
	}
	

	/**
	 * Since there is no way to poll, all items of unknown status are
	 * simply assumed to be in the default (neutral) state. This method
	 * is called when there are reasons to assume that there are no faults,
	 * for instance because the alarm panel is in state READY.
	 */
	private void setUnupdatedItemsToDefault() {
		logger.trace("setting {} unupdated items to default", m_unupdatedItems.size());
		while (!m_unupdatedItems.isEmpty()) {
			// cannot use the config in the hash map, since it is null
			String itemName = m_unupdatedItems.keySet().iterator().next();
			ArrayList<AlarmDecoderBindingConfig> al = getItems(itemName);
			for (AlarmDecoderBindingConfig bc : al) {
				switch (bc.getMsgType()) {
				case RFX:
					if (bc.hasFeature("data")) {
						updateItem(bc, new DecimalType(0));
					} else if (bc.hasFeature("contact")) {
						updateItem(bc, OpenClosedType.CLOSED);
					}
					break;
				case EXP:
				case REL:
					updateItem(bc, OpenClosedType.CLOSED);
					break;
				default:
					break;
				}
			}
		}
		m_unupdatedItems.clear();
	}

	/**
	 * The relay and expander messages have identical format
	 * @param mt message type of incoming message
	 * @param msg string containing incoming message
	 * @throws MessageParseException
	 */
	private void parseRelayOrExpanderMessage(ADMsgType mt, String msg) throws MessageParseException {
		String parts[] = splitMessage(msg);
		if (parts.length != 3) {
			throw new MessageParseException("need 3 comma separated fields in msg");
		}
		String addr = parts[0] + "," + parts[1];
		try {
			int numeric = Integer.parseInt(parts[2]);
			if ((numeric & ~0x1) != 0) {
				throw new MessageParseException("zone status should only be 0 or 1");
			}
			ArrayList<AlarmDecoderBindingConfig> bcl = getItems(mt, addr, "contact");
			for (AlarmDecoderBindingConfig c : bcl) {
				updateItem(c, numeric == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
			}
		} catch (NumberFormatException e) {
			throw new MessageParseException("msg contains invalid state number" + e.getMessage());
		}
	}

	private void parseRFMessage(String msg) throws MessageParseException {
		String parts[] = splitMessage(msg);
		if (parts.length != 2) {
			throw new MessageParseException("need 2 comma separated fields in msg");
		}
		try {
			int numeric = Integer.parseInt(parts[1], 16);
			ArrayList<AlarmDecoderBindingConfig> bcl = getItems(ADMsgType.RFX, parts[0], null);
			for (AlarmDecoderBindingConfig c : bcl) {
				if (c.hasFeature("data")) {
					int bit = c.getIntParameter("bit", 0, 7, -1);
					// apply bitmask if requested, else publish raw number
					int v = (bit == -1) ? numeric : ((numeric >> bit) & 0x00000001);
					updateItem(c, new DecimalType(v));
				} else if (c.hasFeature("contact")) {
					// if no loop indicator bitmask is set, default to 0x80
					int bit = c.getIntParameter("bitmask", 0, 255, 0x80);
					int v = numeric & bit;
					updateItem(c, v == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
				}
			}
		} catch (NumberFormatException e) {
			throw new MessageParseException("msg contains invalid state number: " + e.getMessage());
		}
	}
	
	private String [] splitMessage(String msg) throws MessageParseException {
		String parts[] = msg.split(":");
		if (parts.length != 2) {
			throw new MessageParseException("msg must have exactly one colon");
		}
		return (parts[1].split(","));
	}

	/**
	 * Updates item on the openhab bus
	 * @param bc binding config
	 * @param state new state of item
	 */
	private void updateItem(AlarmDecoderBindingConfig bc, State state) {
		if (bc.getMsgType() != ADMsgType.KPM) {
			logger.debug("updating item: {} to state {}", bc.getItemName(), state);
		} else {
			logger.trace("updating item: {} to state {}", bc.getItemName(), state);
		}
		m_unupdatedItems.remove(bc.getItemName());
		eventPublisher.postUpdate(bc.getItemName(), state);
	}
	/**
	 * Finds all items that refer to a given message type, address, and feature
	 * @param mt  message type (or null for all messages)
	 * @param addr address to match (or all addresses if null)
	 * @param feature feature to match (or all features if null)
	 * @return array list of all messages
	 */
	private ArrayList<AlarmDecoderBindingConfig> getItems(ADMsgType mt, String addr,
			String feature) {
		ArrayList<AlarmDecoderBindingConfig> al = new ArrayList<AlarmDecoderBindingConfig>();
		for (AlarmDecoderBindingProvider bp : providers) {
			al.addAll(bp.getConfigurations(mt, addr, feature));
		}
		return al;
	}
	/**
	 * Find binding configurations for a given item
	 * @param itemName name of item to look for
	 * @return array with binding configurations
	 */

	private ArrayList<AlarmDecoderBindingConfig> getItems(String itemName) {
		ArrayList<AlarmDecoderBindingConfig> al = new ArrayList<AlarmDecoderBindingConfig>();
		for (AlarmDecoderBindingProvider bp : providers) {
			al.add(bp.getBindingConfig(itemName));
		}
		return al;
	}
	/**
	 * Extract message type from message
	 * @param s message string
	 * @return message type
	 */
	private static ADMsgType s_getMsgType(String s) {
		if (s == null || s.length() < 4) {
			return ADMsgType.INVALID;
		}
		if (s.startsWith("[")) {
			return ADMsgType.KPM;
		}
		ADMsgType mt = s_startToMsgType.get(s.substring(0,4));
		if (mt == null) {
			mt = ADMsgType.INVALID;
		}
		return mt;
	}
	static {
		s_startToMsgType.put("!REL", ADMsgType.REL);
		s_startToMsgType.put("!SER", ADMsgType.INVALID);
		s_startToMsgType.put("!RFX", ADMsgType.RFX);
		s_startToMsgType.put("!EXP", ADMsgType.EXP);
	}
	/**
	 * custom exception for cleaner error handling
	 */
	private static class MessageParseException extends Exception {
		private static final long serialVersionUID = 1L;

		public MessageParseException(String msg) {
			super(msg);
		}
	}
}
