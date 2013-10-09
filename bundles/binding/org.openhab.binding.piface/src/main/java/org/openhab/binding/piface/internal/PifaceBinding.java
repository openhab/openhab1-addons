/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.piface.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.piface.PifaceBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding which communicates with (one or many) Raspberry Pis with PiFace boards. 
 * It registers as listener of the Pis to accomplish real bidirectional communication.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public class PifaceBinding extends AbstractBinding<PifaceBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(PifaceBinding.class);
	
	private static final String CONFIG_KEY_HOST = "host";
	private static final String CONFIG_KEY_LISTENER_PORT = "listenerport";
	private static final String CONFIG_KEY_MONITOR_PORT = "monitorport";
	private static final String CONFIG_KEY_SOCKET_TIMEOUT = "sockettimeout";
	private static final String CONFIG_KEY_MAX_RETRIES = "maxretries";
	
	private static final int DEFAULT_LISTENER_PORT = 15432;
	private static final int DEFAULT_MONITOR_PORT = 15433;
<<<<<<< HEAD
	
	// socket timeout (1 sec) - should probably be a configurable parameter 
	private static final int SOCKET_TIMEOUT_MS = 1000;
	private static final byte ERROR_RESPONSE = -1;	
=======
	private static final int DEFAULT_SOCKET_TIMEOUT_MS = 1000;
	private static final int DEFAULT_MAX_RETRIES = 3;
>>>>>>> upstream/master
	
	// list of Piface nodes loaded from the binding configuration
	private final Map<String, PifaceNode> pifaceNodes = new HashMap<String, PifaceNode>();
	
	// keeps track of all Piface pins which require initial read requests
	private final List<PifacePin> pifacePinsToInitialise =
			Collections.synchronizedList(new ArrayList<PifacePin>());
	
	// the Piface pin initialiser, which runs in a separate thread
	private final PifaceInitialiser initialiser = new PifaceInitialiser();
	
	
<<<<<<< HEAD
	public void activate(ComponentContext componentContext) {
=======
	@Override
	protected long getRefreshInterval() {
		return watchdogInterval;
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing PiFace binding configuration => watchdog cycle aborted!");
			return;
		}
		
		// send a watchdog ping to each node
		for (Map.Entry<String, PifaceNode> entry : pifaceNodes.entrySet()) {
			String pifaceId = entry.getKey();
			PifaceNode node = entry.getValue();
			
			int value = sendWatchdog(node);
			for (String itemName : getItemNamesForPin(pifaceId, BindingType.WATCHDOG, 0)) {
				updateItemState(itemName, value);
			}
		}		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void activate() {
>>>>>>> upstream/master
		initialiser.start();
	}

	public void deactivate(ComponentContext componentContext) {
		stopMonitors();
		pifaceNodes.clear();
		providers.clear();
		initialiser.setInterrupted(true);
	}
	
	private void startMonitors() {
		// start the monitor threads for each configured PiFace device
		for (PifaceNode node : pifaceNodes.values()) {
			node.startMonitor();
		}		
	}
	
	private void stopMonitors() {
		// stop the monitor threads for each configured PiFace device
		for (PifaceNode node : pifaceNodes.values()) {
			node.stopMonitor();
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof PifaceBindingProvider) {
			PifaceBindingProvider pifaceProvider = (PifaceBindingProvider) provider;
			PifacePin pin = pifaceProvider.getPifacePin(itemName);
			if (pin != null && !pifacePinsToInitialise.contains(pin)) {
				pifacePinsToInitialise.add(pin);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		if (provider instanceof PifaceBindingProvider) {
			PifaceBindingProvider pifaceProvider = (PifaceBindingProvider) provider;
			for (String itemName : pifaceProvider.getItemNames()) {
				PifacePin pin = pifaceProvider.getPifacePin(itemName);
				if (pin != null && !pifacePinsToInitialise.contains(pin)) {
					pifacePinsToInitialise.add(pin);
				}
			}
		}
	}
	
	private void updateItemState(String itemName, int value) {
    	Class<? extends Item> itemType = getItemType(itemName);
    	if (itemType.equals(SwitchItem.class)) {
	    	if (value == 0) {
	    		eventPublisher.postUpdate(itemName, OnOffType.OFF);
	    	} else if (value == 1) {
	    		eventPublisher.postUpdate(itemName, OnOffType.ON);
	    	}
    	} 
    	
    	if (itemType.equals(ContactItem.class)) {
	    	if (value == 0) {
	    		eventPublisher.postUpdate(itemName, OpenClosedType.OPEN);
	    	} else if (value == 1) {
	    		eventPublisher.postUpdate(itemName, OpenClosedType.CLOSED);
	    	}
    	}		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		for (PifaceBindingProvider provider : getProvidersForItemName(itemName)) {
			PifacePin pin = provider.getPifacePin(itemName);
			if (pin == null) {
				logger.warn("No Piface pin configuration exists for '" + itemName + "'");
				continue;
			}
			if (pin.getPinType() == PifacePin.PinType.IN) {
				logger.warn("Unable to send a command to a Piface 'IN' pin - these pin types are read-only");
				continue;
			}
			String pifaceId = pin.getPifaceId();
			int pinNumber = pin.getPinNumber();

			PifaceNode node = pifaceNodes.get(pifaceId);	
			if (node == null) {
				logger.warn("No Piface node for id " + pifaceId);
				continue;
			}
			
			if (command.equals(OnOffType.ON) || command.equals(OpenClosedType.CLOSED)) {
				sendDigitalWrite(node, pinNumber, 1);
			} else {
				sendDigitalWrite(node, pinNumber, 0);
			}
		}
	}

<<<<<<< HEAD
	private void sendDigitalWrite(String host, int port, int pinNumber, int pinValue) {
	    sendCommand(host, port, PifaceCommand.DIGITAL_WRITE_CMD.toByte(), PifaceCommand.DIGITAL_WRITE_ACK.toByte(), pinNumber, pinValue);
	}
	
	private int sendDigitalRead(String host, int port, int pinNumber) {
	    byte response = sendCommand(host, port, 
	    	PifaceCommand.DIGITAL_READ_CMD.toByte(), PifaceCommand.DIGITAL_READ_ACK.toByte(), pinNumber, 0);
	    if (response == ERROR_RESPONSE) {
	    	return -1;
	    }
	    
		return (int) response;
	}

	private byte sendCommand(String host, int port, byte command, byte commandAck, int pinNumber, int pinValue) {
	    logger.debug("Sending command (" + command + ") to " + host + ":" 
	    		+ port + " for pin " + pinNumber + " (value=" + pinValue + ")");
=======
	private int sendWatchdog(PifaceNode node) {
		byte response = sendCommand(node, PifaceCommand.WATCHDOG_CMD.toByte(), PifaceCommand.WATCHDOG_ACK.toByte(), 0, 0);
		return response == ERROR_RESPONSE ? 0 : 1;
	}
	
	private void sendDigitalWrite(PifaceNode node, int pinNumber, int pinValue) {
	    sendCommand(node, PifaceCommand.DIGITAL_WRITE_CMD.toByte(), PifaceCommand.DIGITAL_WRITE_ACK.toByte(), pinNumber, pinValue);
	}
	
	private int sendDigitalRead(PifaceNode node, int pinNumber) {
	    byte response = sendCommand(node, PifaceCommand.DIGITAL_READ_CMD.toByte(), PifaceCommand.DIGITAL_READ_ACK.toByte(), pinNumber, 0);
	    return response == ERROR_RESPONSE ? -1 : (int)response;
	}

	private byte sendCommand(PifaceNode node, byte command, byte commandAck, int pinNumber, int pinValue) {
		int attempt = 1;
		while (attempt <= node.maxRetries) {
			byte response = sendCommand(node, command, commandAck, pinNumber, pinValue, attempt);
			if (response != ERROR_RESPONSE)
				return response;
			attempt++;
		}
		logger.warn("Command failed " + node.maxRetries + " times. Stopping.");
		return ERROR_RESPONSE;
	}
	
	private byte sendCommand(PifaceNode node, byte command, byte commandAck, int pinNumber, int pinValue, int attempt) {
	    logger.debug("Sending command (" + command + ") to " + node.host + ":" 
	    		+ node.listenerPort + " for pin " + pinNumber + " (value=" + pinValue + ")");
	    logger.debug("Attempt " + attempt + "...");
>>>>>>> upstream/master
	    
	    DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(node.socketTimeout);

			InetAddress inetAddress = InetAddress.getByName(node.host);
			
		    // send the packet
			byte[] sendData = new byte[] { command, (byte)pinNumber, (byte)pinValue };
		    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, node.listenerPort);
		    socket.send(sendPacket);
		    
		    // read the response
			byte[] receiveData = new byte[16];
		    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    socket.receive(receivePacket);
		    
		    // check the response is valid
		    if (receiveData[0] != commandAck) {
			    logger.error("Unexpected 'ack' code received - expecting " + commandAck + " but got " + receiveData[0]);
		    	return ERROR_RESPONSE;
		    }
		    if (receiveData[1] != pinNumber) {
			    logger.error("Invalid pin received - expecting " + pinNumber + " but got " + receiveData[1]);
		    	return ERROR_RESPONSE;
		    }
		    
		    // return the data value
		    logger.debug("Command successfully sent and acknowledged (returned " + receiveData[2]+ ")");
		    return receiveData[2];
		} catch (IOException e) {
<<<<<<< HEAD
			logger.error("Failed to send UDP packet", e);
=======
			logger.error("Failed to send command (" + command + ") to " + node.host + ":" + node.listenerPort + " (attempt " + attempt + ")", e);
>>>>>>> upstream/master
			return ERROR_RESPONSE;
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
	
	private List<PifaceBindingProvider> getProvidersForItemName(String itemName) {
		List<PifaceBindingProvider> providers = new ArrayList<PifaceBindingProvider>();
		for (PifaceBindingProvider provider : this.providers) {
			if (provider.getItemNames().contains(itemName)) {
				providers.add(provider);
			}
		}
		return providers;
	}	

	private List<String> getItemNamesForPin(String pifaceId, PifacePin.PinType pinType, int pinNumber) {
		List<String> itemNames = new ArrayList<String>();
		for (PifaceBindingProvider provider : this.providers) {
			itemNames.addAll(provider.getItemNames(pifaceId, pinType, pinNumber));
		}
		return itemNames;
	}	
	
	private Class<? extends Item> getItemType(String itemName) {
		for (PifaceBindingProvider provider : getProvidersForItemName(itemName)) {
			return provider.getItemType(itemName);	
		}
		throw new RuntimeException("Could not determine item type for " + itemName);
	}	
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		// stop any existing monitor threads
		stopMonitors();
		pifaceNodes.clear();
		
		if (config != null) {
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) config.get(key);
			
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				String[] keyParts = key.split("\\.");
				String pifaceId = keyParts[0];
				String configKey = keyParts[1];
				
				if (!pifaceNodes.containsKey(pifaceId)) {
					pifaceNodes.put(pifaceId, new PifaceNode(pifaceId));
				}
				
				PifaceNode pifaceNode = pifaceNodes.get(pifaceId);
				
				if (configKey.equals(CONFIG_KEY_HOST)) {
					pifaceNode.host = value;
				} else if (configKey.equals(CONFIG_KEY_LISTENER_PORT)) {
					pifaceNode.listenerPort = Integer.parseInt(value);
				} else if (configKey.equals(CONFIG_KEY_MONITOR_PORT)) {
					pifaceNode.monitorPort = Integer.parseInt(value);
				} else if (configKey.equals(CONFIG_KEY_SOCKET_TIMEOUT)) {
					pifaceNode.socketTimeout = Integer.parseInt(value);
				} else if (configKey.equals(CONFIG_KEY_MAX_RETRIES)) {
					pifaceNode.maxRetries = Integer.parseInt(value);
				} else {
					throw new ConfigurationException(key, "Unrecognised configuration parameter: " + configKey);
				}
			}
			
			// start the monitor threads for each PiFace node
			startMonitors();
		}
	}
	
	private class PifaceNode {
		final PifaceNodeMonitor monitor;	
		
		String host;
		int listenerPort = DEFAULT_LISTENER_PORT;
		int monitorPort = DEFAULT_MONITOR_PORT;
		int socketTimeout = DEFAULT_SOCKET_TIMEOUT_MS;
		int maxRetries = DEFAULT_MAX_RETRIES;
		
		PifaceNode(String pifaceId) {
			this.monitor = new PifaceNodeMonitor(pifaceId);
		}
		
		void startMonitor() {
			monitor.setMonitorPort(monitorPort);
			if (monitor.isAlive())
				return;
			monitor.start();
		}

		void stopMonitor() {
			monitor.setInterrupted(true);
		}
	}
	
	private class PifaceNodeMonitor extends Thread {
		private final String pifaceId;
		
		private int monitorPort = -1;
		private boolean interrupted = false;

		public PifaceNodeMonitor(String piFaceId) {
			super("Piface Monitor Thread [" + piFaceId + "]");
			this.pifaceId = piFaceId;
		}
		
		public void setMonitorPort(int monitorPort) {
			this.monitorPort = monitorPort;
		}
		
		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}
		
		@Override
		public void run() {
			logger.debug(getName() + " started monitoring port " + monitorPort);

			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(monitorPort);
				
				byte[] receiveData = new byte[100];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				while (!interrupted) {
					    // block on this port waiting for a message				    
					    socket.receive(receivePacket);
					    
					    int pinNumber = receiveData[0];
					    int pinValue = receiveData[1];					    
					    logger.debug(getName() + " received message for pin " + pinNumber + " (value=" + pinValue + ")");
					    
					    for (String itemName : getItemNamesForPin(pifaceId, PifacePin.PinType.IN, pinNumber)) {
					    	updateItemState(itemName, pinValue);
					    }
				}
			} catch (IOException e) {
				logger.error(getName() + " failed", e);
			} finally {
				if (socket != null) {
					socket.close();
				}
			}
		}		
	}
	
	/**
	 * The PifaceInitialiser runs as a separate thread. Whenever new Piface pin bindings are added, it takes care that read
	 * requests are sent to initialise the pin state.
	 * 
	 * @author Ben Jones
	 * @since 1.3.0
	 * 
	 */
	private class PifaceInitialiser extends Thread {
		
		private boolean interrupted = false;
		
		public PifaceInitialiser() {
			super("Piface Initialiser");
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {
			// as long as no interrupt is requested, continue running
			while (!interrupted) {
				if (pifacePinsToInitialise.size() > 0) {
					// we first clone the list, so that it stays unmodified
					ArrayList<PifacePin> clonedList =
						new ArrayList<PifacePin>(pifacePinsToInitialise);
					initialisePifacePins(clonedList);
				}
				// just wait before looping again
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}

		private void initialisePifacePins(ArrayList<PifacePin> clonedList) {
			for (PifacePin pin : clonedList) {
				try {
					// the Piface node might not have been read from the binding config yet so just skip
					// and we will try again on the next loop
					PifaceNode node = pifaceNodes.get(pin.getPifaceId());	
					if (node == null)
						continue;

<<<<<<< HEAD
					int pinValue = sendDigitalRead(node.host, node.listenerPort, pin.getPinNumber());
					for (String itemName : getItemNamesForPin(pin.getPifaceId(), pin.getPinType(), pin.getPinNumber()))
						updateItemState(itemName, pinValue);
					pifacePinsToInitialise.remove(pin);
=======
					int value = sendDigitalRead(node, bindingConfig.getPinNumber());
					for (String itemName : getItemNamesForPin(bindingConfig.getPifaceId(), bindingConfig.getBindingType(), bindingConfig.getPinNumber()))
						updateItemState(itemName, value);
					bindingConfigsToInitialise.remove(bindingConfig);
>>>>>>> upstream/master
				} catch (Exception e) {
					logger.warn("Failed to initialise value for Piface pin {} ({}): {}", new Object[] { pin.getPinNumber(), pin.getPifaceId(), e.getMessage() });
				}
			}
		}
	}
	
}
