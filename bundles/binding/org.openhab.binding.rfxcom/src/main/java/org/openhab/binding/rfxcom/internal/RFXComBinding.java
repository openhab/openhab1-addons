/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal;

import java.io.IOException;
import java.util.EventObject;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComBindingProvider;
import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.connector.RFXComEventListener;
import org.openhab.binding.rfxcom.internal.connector.RFXComSerialConnector;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;
import org.openhab.binding.rfxcom.internal.messages.RFXComMessageFactory;
import org.openhab.binding.rfxcom.internal.messages.RFXComMessageInterface;
import org.openhab.binding.rfxcom.internal.messages.RFXComTransmitterMessage;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFXComBinding listens to RFXCOM controller notifications and post values to
 * the openHAB event bus when data is received and post item updates
 * from openHAB internal bus to RFXCOM controller.
 * 
 * @author Pauli Anttila, Evert van Es
 * @since 1.2.0
 */
public class RFXComBinding extends AbstractBinding<RFXComBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(RFXComBinding.class);

	private EventPublisher eventPublisher;
	
	private static final int timeout = 5000;

	private static byte seqNbr = 0;
	private static RFXComTransmitterMessage responseMessage = null;
	private Object notifierObject = new Object();

	private MessageLister eventLister = new MessageLister();

	public RFXComBinding() {
	}

	public void activate() {
		logger.debug("Activate");
		RFXComSerialConnector connector = RFXComConnection.getCommunicator();
		if (connector != null) {
			connector.addEventListener(eventLister);
		}
	}

	public void deactivate() {
		logger.debug("Deactivate");
		RFXComSerialConnector connector = RFXComConnection.getCommunicator();
		if (connector != null) {
			connector.removeEventListener(eventLister);
		}
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		executeCommand(itemName, command);
	}

	/**
	 * Find the first matching {@link RFXComBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private RFXComBindingProvider findFirstMatchingBindingProvider(
			String itemName) {

		RFXComBindingProvider firstMatchingProvider = null;

		for (RFXComBindingProvider provider : this.providers) {

			String Id = provider.getId(itemName);

			if (Id != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	public static synchronized byte getSeqNumber() {
		return seqNbr;
	}

	public synchronized byte getNextSeqNumber() {
		if (++seqNbr == 0)
			seqNbr = 1;

		return seqNbr;
	}

	private void executeCommand(String itemName, Type command) {
		if (itemName != null) {
			RFXComBindingProvider provider = findFirstMatchingBindingProvider(itemName);
			if (provider == null) {
				logger.warn(
						"Cannot execute command because no binding provider was found for itemname '{}'",
						itemName);
				return;
			}

			if (provider.isInBinding(itemName) == false) {
				logger.debug(
						"Received command (item='{}', state='{}', class='{}')",
						new Object[] { itemName, command.toString(),
								command.getClass().toString() });
				RFXComSerialConnector connector = RFXComConnection
						.getCommunicator();

				if (connector == null) {
					logger.warn("RFXCom controller is not initialized!");
					return;
				}

				String id = provider.getId(itemName);
				PacketType packetType = provider.getPacketType(itemName);
				Object subType = provider.getSubType(itemName);
				RFXComValueSelector valueSelector = provider
						.getValueSelector(itemName);

				try {
					RFXComMessageInterface obj = RFXComMessageFactory.getMessageInterface(packetType);
					obj.convertFromState(valueSelector, id, subType, command, getNextSeqNumber());
					byte[] data = obj.decodeMessage();
					
					logger.debug("Transmitting data: {}",
							DatatypeConverter.printHexBinary(data));

					setResponseMessage(null);
					connector.sendMessage(data);
					
				} catch (RFXComException e) {
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("Message sending to RFXCOM controller failed.", e);	
				}
				
				try {

					synchronized (notifierObject) {
						notifierObject.wait(timeout);
					}

					RFXComTransmitterMessage resp = getResponseMessage();

					switch (resp.response) {
					case ACK:
					case ACK_DELAYED:
						logger.debug(
								"Command succesfully transmitted, '{}' received",
								resp.response);
						break;

					case NAK:
					case NAK_INVALID_AC_ADDRESS:
					case UNKNOWN:
						logger.error("Command transmit failed, '{}' received",
								resp.response);
						break;
					}

				} catch (InterruptedException ie) {
					logger.error(
							"No acknowledge received from RFXCOM controller, timeout {}ms ",
							timeout);
				}
			}
			else
			{
				logger.warn(
						"Provider is not in binding '{}'",
						provider.toString());
			}

		}

	}

	public static synchronized RFXComTransmitterMessage getResponseMessage() {
		return responseMessage;
	}

	public synchronized void setResponseMessage(
			RFXComTransmitterMessage responseMessage) {
		RFXComBinding.responseMessage = responseMessage;
	}

	private class MessageLister implements RFXComEventListener {

		@Override
		public void packetReceived(EventObject event, byte[] packet) {

			try {
				RFXComMessageInterface obj = RFXComMessageFactory.getMessageInterface(packet);
				
				if (obj instanceof RFXComTransmitterMessage) {
					RFXComTransmitterMessage resp = (RFXComTransmitterMessage) obj;

					if (resp.seqNbr == getSeqNumber()) {
						logger.debug("Transmitter response received:\n{}",
								obj.toString());
						setResponseMessage(resp);
						synchronized (notifierObject) {
							notifierObject.notify();
						}
					}

				} else {
					String id2 = obj.generateDeviceId();

					List<RFXComValueSelector> supportedValueSelectors = obj
							.getSupportedValueSelectors();

					if (supportedValueSelectors != null) {

						for (RFXComBindingProvider provider : providers) {
							for (String itemName : provider.getItemNames()) {

								String id1 = provider.getId(itemName);
								boolean inBinding = provider.isInBinding(itemName);

								if (id1.equals(id2) && inBinding) {

									RFXComValueSelector valueSelector = provider
											.getValueSelector(itemName);

									if (supportedValueSelectors.contains(valueSelector)) {
										try {
											State value = obj.convertToState(valueSelector);
											eventPublisher.postUpdate(itemName, value);
										} catch (RFXComException e) {
											logger.warn( "Data conversion error", e);
										}
									}
								}

							}
						}
					}
				}
			} catch (RFXComException e) {
				logger.error("Error occured during packet receiving, data: {}",
						DatatypeConverter.printHexBinary(packet), e);
			}
		}
	}

}
