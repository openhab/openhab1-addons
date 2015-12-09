/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import org.openhab.binding.tacmi.TACmiBindingProvider;
import org.openhab.binding.tacmi.internal.message.AnalogMessage;
import org.openhab.binding.tacmi.internal.message.AnalogValue;
import org.openhab.binding.tacmi.internal.message.DigitalMessage;
import org.openhab.binding.tacmi.internal.message.Message;
import org.openhab.binding.tacmi.internal.message.MessageType;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Timo Wendt
 * @since 1.8.0
 */
public class TACmiBinding extends AbstractActiveBinding<TACmiBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(TACmiBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the TACmi server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 1000;

	/**
	 * IP or hostname of the CMI This is set in the activate method
	 */
	//private String cmiAddress;
	private InetAddress cmiAddress;

	/**
	 * Port the cmi is listening on and also sending to
	 */
	private static int cmiPort = 5441;

	/**
	 * Connection socket
	 */
	private DatagramSocket clientSocket = null;

	public TACmiBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// set cmiAddress from configuration
		//cmiAddress = (String) configuration.get("cmiAddress");
		try {
			cmiAddress = InetAddress
					.getByName((String) configuration.get("cmiAddress"));
		} catch (UnknownHostException e1) {
			logger.error("Failed to get IP of CMI from configuration");
            setProperlyConfigured(false);
            return;
		}

		try {
			clientSocket = new DatagramSocket(cmiPort);
		} catch (SocketException e) {
			logger.error("Failed to create Socket for receiving UDP packts from CMI");
            setProperlyConfigured(true);
            return;
		}

		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		clientSocket.close();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "TACmi Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.trace("execute() method is called!");
		try {
			clientSocket.setBroadcast(true);
			clientSocket.setSoTimeout(10000);
			byte[] receiveData = new byte[14];
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			byte[] data = receivePacket.getData();
			Message message;
			if (data[1] > 0) {
				logger.debug("Processing analog message");
				message = new AnalogMessage(data);
			} else if (data[1] == 0) {
				logger.debug("Processing digital message");
				message = new DigitalMessage(data);
			} else {
				logger.debug("Invalid message received");
				return;
			}
			logger.debug(message.toString());
			for (TACmiBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					logger.debug("Processing item: " + itemName);
					int portNumber = provider.getPortNumber(itemName);
					if (provider.getCanNode(itemName) == message.canNode
							&& provider.getPortType(itemName)
							.equals(message.getType().toString().toLowerCase())) {
						if (message.hasPortnumber(portNumber)) {
							if (message.getType() == MessageType.A) {
								AnalogValue value = ((AnalogMessage) message)
										.getAnalogValue(portNumber);
								if (value.value != null) {
									logger.debug("Updating item: " + itemName
											+ " with value: " + value.value);
									eventPublisher.postUpdate(itemName,
											new DecimalType(value.value));
								}
							} else {
								OnOffType state = ((DigitalMessage) message).getPortStateAsOnOffType(portNumber);
								logger.debug("Updating item {} with state {}",
										itemName, state);
								eventPublisher.postUpdate(itemName,
										state);
							}
						} else {
							logger.debug(
									"Portnumber {} not included in message",
									portNumber);
						}
					} else {
						logger.debug("CAN Node does not match");
					}
				}
			}

		} catch (Exception e) {
			logger.warn("Error in execute: {}, Message: {}", e.getClass()
					.getName(), e.getMessage());
		}
		logger.trace("TACmi execute() finished");

	}

	/**
	 * @throws UnknownHostException
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName,
				command);
		for (TACmiBindingProvider provider : providers) {
			int canNode = provider.getCanNode(itemName);
			String portType = provider.getPortType(itemName);
			int portNumber = provider.getPortNumber(itemName);
			logger.trace("Type: {}, portNumber: {}, command: {}", portType,
					portNumber, command.toString());
			byte[] messageBytes;
			if (portType.equals("d") && portNumber == 1
					&& command instanceof OnOffType) {
				boolean state = OnOffType.ON.equals(command) ? true : false;
				DigitalMessage message = new DigitalMessage((byte) canNode,
							state);
				messageBytes = message.getRaw();
			} else if (portType.equals("a") && (portNumber-1) % 4 == 0
					&& command instanceof DecimalType) {
					TACmiMeasureType measureType = provider.getMeasureType(itemName);
					AnalogMessage message = new AnalogMessage((byte) canNode, 1, (DecimalType) command, measureType);
					messageBytes = message.getRaw();
			} else {
				logger.info(
						"Not sending command: portType: {}, portNumber: {}, command: {}",
						portType, portNumber, command.toString());
				return;
			}
			DatagramPacket packet = new DatagramPacket(messageBytes,
					messageBytes.length, cmiAddress,
					TACmiBinding.cmiPort);
			try {
				clientSocket.send(packet);
			} catch (IOException e) {
				logger.warn("Error sending message: {}, {}", e.getClass()
						.getName(), e.getMessage());
			}
		}
	}
}
