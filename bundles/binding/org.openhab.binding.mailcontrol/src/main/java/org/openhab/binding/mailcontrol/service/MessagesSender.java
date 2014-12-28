/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import java.util.List;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.message.AbstractMessage;
import org.creek.mailcontrol.model.message.GenericMessage;
import org.creek.mailcontrol.model.message.ItemStateResponseMessage;
import org.creek.mailcontrol.model.message.ItemsStateResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class MessagesSender {
    private static final String SENDER_EMAIL = "andrey.pereverzin_sweethome@yahoo.com";

    private static final Logger logger = LoggerFactory.getLogger(MessagesSender.class);

    static final String RESPONSE_SUBJECT = "OpenHABResponse";

    private final MailConnector mailConnector;

    public MessagesSender(MailConnector mailConnector) {
        this.mailConnector = mailConnector;
    }

    public void sendMessage(GenericMessage message, ItemStateData itemState) throws ServiceException {
        ItemStateResponseMessage msg = new ItemStateResponseMessage(itemState, message.getMessageId(), SENDER_EMAIL);
        sendMessage(msg, message.getMessageId().getSenderEmail());
    }

    public void sendMessage(GenericMessage message, List<ItemStateData> itemStates) throws ServiceException {
        ItemsStateResponseMessage msg = new ItemsStateResponseMessage(itemStates, message.getMessageId(), SENDER_EMAIL);
        sendMessage(msg, message.getMessageId().getSenderEmail());
    }
    
    public <U extends AbstractMessage>void sendMessage(U message, String... emails) throws ServiceException {
        try {
            logger.debug("Sending message: " + message);
            mailConnector.sendMessage(RESPONSE_SUBJECT, message.getMessageId().getSenderEmail(), message.toJSON().toString(), emails);
            logger.debug("Message sent");
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
}
