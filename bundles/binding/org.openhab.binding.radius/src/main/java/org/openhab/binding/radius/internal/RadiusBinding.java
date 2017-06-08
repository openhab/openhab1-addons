/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.radius.internal;

import org.openhab.binding.radius.RadiusBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.UnDefType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.util.Dictionary;

/**
 * RADIUS server communication.
 * 
 * @author Jan N. Klug
 * @since 1.8
 */
public class RadiusBinding extends AbstractActiveBinding<RadiusBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(RadiusBinding.class);

	/**
	 * the refresh interval which is used to poll values from the Radius server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * the address of the Radius servers (optional, defaults to 127.0.0.1)
	 */
	InetAddress radiusServer;

	/**
	 * the shared secret for the Radius server (required)
	 */
	private String sharedSecret = "";

	public RadiusBinding() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ?> configuration) throws ConfigurationException {
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		try {
			String radiusServerAddress = (String) configuration.get("server");
			if (StringUtils.isNotBlank(radiusServerAddress)) {
				radiusServer = InetAddress.getByName(radiusServerAddress);
			} else {
				radiusServer = InetAddress.getByName("127.0.0.1");
			}
		} catch (UnknownHostException e) {
			logger.warn("Could not determine RADIUS server address");
		}

		sharedSecret = (String) configuration.get("sharedSecret");

		if (StringUtils.isNotBlank(sharedSecret)) {
			setProperlyConfigured(true);
		} else {
			setProperlyConfigured(false);
		}

		logger.debug("server={}, secret={}, refresh={}", radiusServer, sharedSecret, refreshInterval);
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
		return "Radius Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (RadiusBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				logger.debug("Item '{}' is about to be refreshed", itemName);
				RadiusPacket p = new RadiusPacket(provider.getRadiusType(itemName));
				RadiusPacket pRet = null;
				p.addAttribute(new RadiusAVP(RadiusAttribute.USER_NAME, provider.getUserName(itemName)));
				p.encodePapPassword(provider.getPassword(itemName), sharedSecret);
				try {
					DatagramSocket clientSocket = new DatagramSocket();

					byte[] receiveData = new byte[1024];
					DatagramPacket sendPacket = new DatagramPacket(p.getRawPacket(), p.getRawPacketLength(),
							radiusServer, 1812);
					logger.trace("Sent: {}", p);
					clientSocket.send(sendPacket);
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					clientSocket.close();
					pRet = new RadiusPacket(receivePacket.getData());
					logger.trace("Received: {}", pRet);
				} catch (IOException e) {
					logger.warn("Error while sending packet to server");
				}

				if (pRet == null) {
					logger.warn("Received empty or no packet");
					return;
				}

				Class<? extends Item> itemType = provider.getItemType(itemName);
				State state = null;
				byte attr = provider.getRadiusAttribute(itemName);

				if ((attr == 0) || pRet.hasAttribute(attr)) {
					if (itemType.isAssignableFrom(StringItem.class)) {
						if (attr == 0) {
							state = UnDefType.UNDEF;
						} else {
							state = new StringType(pRet.getAttribute(attr).getValueString());
						}
					} else if (itemType.isAssignableFrom(NumberItem.class)) {
						if (attr == 0) {
							state = UnDefType.UNDEF;
						} else {
							state = new DecimalType(pRet.getAttribute(attr).getValueLong());
						}
					} else if (itemType.isAssignableFrom(SwitchItem.class)) {
						if (p.getTypeCode() == RadiusTypeCode.ACCESS_REQUEST) {
							if (pRet.getTypeCode() == RadiusTypeCode.ACCESS_ACCEPT) {
								state = OnOffType.ON;
							} else if (pRet.getTypeCode() == RadiusTypeCode.ACCESS_REJECT) {
								state = OnOffType.OFF;
							} else {
								state = UnDefType.UNDEF;
							}
						}
					}
				} else {
					state = UnDefType.UNDEF;
				}

				eventPublisher.postUpdate(itemName, state);

			}

		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// TODO: add out-binding
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// TODO: add OUT binding
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

}
