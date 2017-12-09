/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    private static final Logger logger = LoggerFactory.getLogger(RFXComBinding.class);

    private EventPublisher eventPublisher;

    private static final int timeout = 5000;

    private static byte seqNbr = 0;
    private final ResultRegistry resultRegistry = new ResultRegistry();

    private final MessageLister eventLister = new MessageLister();

    public RFXComBinding() {
    }

    @Override
    public void activate() {
        logger.debug("Activate");
        RFXComSerialConnector connector = RFXComConnection.getCommunicator();
        if (connector != null) {
            connector.addEventListener(eventLister);
        }
    }

    @Override
    public void deactivate() {
        logger.debug("Deactivate");
        RFXComSerialConnector connector = RFXComConnection.getCommunicator();
        if (connector != null) {
            connector.removeEventListener(eventLister);
        }
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("Received command: {} {}", itemName, command);
        if (itemName != null) {
            if (executeCommand(itemName, command) && command instanceof State) {
                eventPublisher.postUpdate(itemName, (State) command);
            }
        }
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
    private RFXComBindingProvider findFirstMatchingBindingProvider(String itemName) {

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
        if (++seqNbr == 0) {
            seqNbr = 1;
        }

        return seqNbr;
    }

    /**
     * 
     * @return true if the command was successfully sent, false otherwise
     */
    private boolean executeCommand(String itemName, Type command) {
        final RFXComBindingProvider provider = findFirstMatchingBindingProvider(itemName);
        if (provider == null) {
            logger.warn("Cannot execute command because no binding provider was found for itemname '{}'", itemName);
            return false;
        }

        if (!provider.isInBinding(itemName)) {
            logger.debug("Received command (item='{}', state='{}', class='{}')",
                    new Object[] { itemName, command.toString(), command.getClass().toString() });
            RFXComSerialConnector connector = RFXComConnection.getCommunicator();

            if (connector == null) {
                logger.warn("RFXCom controller is not initialized!");
                return false;
            }

            if (!connector.isConnected()) {
                logger.warn("RFXCom controller is not connected");
                return false;
            }

            return executeCommand0(itemName, command, provider, connector);
        } else {
            logger.warn("Provider is not in binding '{}'", provider.toString());

            return false;
        }
    }

    private boolean executeCommand0(String itemName, Type command, final RFXComBindingProvider provider,
            RFXComSerialConnector connector) {
        String id = provider.getId(itemName);
        PacketType packetType = provider.getPacketType(itemName);
        Object subType = provider.getSubType(itemName);
        RFXComValueSelector valueSelector = provider.getValueSelector(itemName);

        final Future<RFXComTransmitterMessage> result;
        try {
            RFXComMessageInterface obj = RFXComMessageFactory.getMessageInterface(packetType);
            final byte seqNumber = getNextSeqNumber();
            obj.convertFromState(valueSelector, id, subType, command, seqNumber);
            byte[] data = obj.decodeMessage();

            logger.debug("Transmitting data: {}", DatatypeConverter.printHexBinary(data));

            result = resultRegistry.registerCommand(seqNumber);
            connector.sendMessage(data);

        } catch (IOException e) {
            logger.error("Message sending to RFXCOM controller failed.", e);
            return false;
        } catch (RFXComException e) {
            logger.error("Message sending to RFXCOM controller failed.", e);
            return false;
        }

        boolean success = false;
        try {
            final RFXComTransmitterMessage resp = result.get(timeout, TimeUnit.MILLISECONDS);

            switch (resp.response) {
                case ACK:
                case ACK_DELAYED:
                    logger.debug("Command succesfully transmitted, '{}' received", resp.response);
                    success = true;
                    break;

                case NAK:
                case NAK_INVALID_AC_ADDRESS:
                case UNKNOWN:
                    logger.error("Command transmit failed, '{}' received", resp.response);
                    break;
            }

        } catch (InterruptedException e) {
            logger.error("No acknowledge received from RFXCOM controller, timeout {}ms due to", timeout, e);
        } catch (ExecutionException e) {
            logger.error("No acknowledge received from RFXCOM controller, timeout {}ms due to {}", timeout, e);
        } catch (TimeoutException e) {
            logger.error("No acknowledge received from RFXCOM controller, timeout {}ms due to {}", timeout, e);
        }

        return success;
    }

    private class MessageLister implements RFXComEventListener {

        @Override
        public void packetReceived(EventObject event, byte[] packet) {

            try {
                RFXComMessageInterface obj = RFXComMessageFactory.getMessageInterface(packet);

                if (obj instanceof RFXComTransmitterMessage) {
                    RFXComTransmitterMessage resp = (RFXComTransmitterMessage) obj;
                    resultRegistry.responseReceived(resp);
                } else {
                    final String deviceId = obj.generateDeviceId();

                    final List<RFXComValueSelector> supportedValueSelectors = obj.getSupportedValueSelectors();

                    if (supportedValueSelectors != null) {

                        for (RFXComBindingProvider provider : providers) {
                            for (String itemName : provider.getItemNames()) {

                                String id1 = provider.getId(itemName);
                                boolean inBinding = provider.isInBinding(itemName);

                                if (id1.equals(deviceId) && inBinding) {

                                    RFXComValueSelector valueSelector = provider.getValueSelector(itemName);

                                    if (supportedValueSelectors.contains(valueSelector)) {
                                        try {
                                            State value = obj.convertToState(valueSelector);
                                            eventPublisher.postUpdate(itemName, value);
                                        } catch (RFXComException e) {
                                            logger.warn("Data conversion error", e);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (RFXComException e) {
                logger.error("Error occurred during packet receiving, data: {}",
                        DatatypeConverter.printHexBinary(packet), e);
            }
        }
    }
}
