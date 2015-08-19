/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import static org.creek.mailcontrol.model.message.MessageType.ITEMS_STATE_REQUEST_MESSAGE;
import static org.creek.mailcontrol.model.message.MessageType.ITEM_COMMAND_REQUEST_MESSAGE;
import static org.creek.mailcontrol.model.message.MessageType.ITEM_STATE_REQUEST_MESSAGE;

import java.util.Dictionary;
import java.util.List;
import java.util.Set;

import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.data.ItemCommandData;
import org.creek.mailcontrol.model.message.GenericMessage;
import org.creek.mailcontrol.model.message.GenericRequestTransformer;
import org.creek.mailcontrol.model.message.ItemCommandRequestMessage;
import org.creek.mailcontrol.model.message.ItemStateRequestMessage;
import org.creek.mailcontrol.model.message.TransformException;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class MessagesProcessor<T extends Command> {
    private static final Logger logger = LoggerFactory.getLogger(MessagesProcessor.class);

    private final MessagesSender messagesSender;
    private final GenericRequestTransformer requestTransformer;
    private final ItemCommandProcessor<T> itemCommandProcessor;
    private final ItemStateRequestProcessor itemStateRequestProcessor;

    public MessagesProcessor(MailConnector mailConnector, EventPublisher eventPublisher, Dictionary<String, ?> config) {
        this.messagesSender = new MessagesSender(mailConnector, config);
        this.requestTransformer = new GenericRequestTransformer();
        this.itemCommandProcessor = new ItemCommandProcessor<T>(eventPublisher);
        this.itemStateRequestProcessor = new ItemStateRequestProcessor();
    }

    public void processReceivedMessages(Set<Object> messages) throws TransformException, ServiceException {
        logger.debug("Processing messages");
        for (Object msg : messages) {
            if (msg instanceof String) {
                logger.debug("Message: " + msg);
                GenericMessage message = requestTransformer.transform((String) msg);
                logger.debug("Processing message: " + message);

                if (message.getMessageType() == ITEM_COMMAND_REQUEST_MESSAGE) {
                    ItemCommandData itemCommand = (ItemCommandData) ((ItemCommandRequestMessage) message).getItemCommand();
                    itemCommandProcessor.processItemCommand(itemCommand);
                } else if (message.getMessageType() == ITEM_STATE_REQUEST_MESSAGE) {
                    ItemStateRequestMessage itemStateRequestMessage = (ItemStateRequestMessage) message;
                    ItemStateData itemState = itemStateRequestProcessor.getItemState(itemStateRequestMessage.getItemId());
                    messagesSender.sendMessage(message, itemState);
                } else if (message.getMessageType() == ITEMS_STATE_REQUEST_MESSAGE) {
                    List<ItemStateData> itemStates = itemStateRequestProcessor.getItemStates();
                    messagesSender.sendMessage(message, itemStates);
                }
            }
        }
    }
}
