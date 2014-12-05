/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import java.util.Set;

import org.creek.mailcontrol.model.message.AbstractMessage;
import org.creek.mailcontrol.model.message.TransformException;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;

/**
 * Receives messages in JSON format and submits them for further processing.
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class MessagesService <T extends Command> {
    private static final Logger logger = LoggerFactory.getLogger(MessagesService.class);
    
    private final MailConnector mailConnector;
    private final MessagesProcessor<T> messagesProcessor;
    
    static final String MAIL_SUBJECT_PREFIX = "OpenHAB";

    public MessagesService(MailConnector mailConnector, EventPublisher eventPublisher) {
        this.mailConnector = mailConnector;
        this.messagesProcessor = new MessagesProcessor<T>(eventPublisher);
    }

    public MessagesService(MailConnector mailConnector, MessagesProcessor<T> messagesProcessor) {
        this.mailConnector = mailConnector;
        this.messagesProcessor = messagesProcessor;
    }

    public <U extends AbstractMessage>void sendMessage(U message, String... emails) throws ServiceException {
        try {
            mailConnector.sendMessage(MAIL_SUBJECT_PREFIX, message.getSenderEmail(), message.toJSON().toString(), emails);
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
    
    public void receiveMessages() throws ServiceException {
        logger.debug("Receiving messages: " + MAIL_SUBJECT_PREFIX);
        try {
            Set<Object> messages = mailConnector.receiveMessages(MAIL_SUBJECT_PREFIX);
            logger.debug("Messages received: " + messages.size());
            
            if (messages.size() > 0) { 
                messagesProcessor.processReceivedMessages(messages);
            }
        } catch(TransformException ex) {
            throw new ServiceException(ex);
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
}
