package org.openhab.binding.mailcontrol.service;

import java.util.Set;

import org.creek.mailcontrol.model.message.AbstractMessage;
import org.creek.mailcontrol.model.message.TransformException;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class MessagesService {
    private static final Logger logger = LoggerFactory.getLogger(MessagesService.class);
    
    private final MailConnector mailConnector;
    private final MessagesProcessor messagesProcessor;
    
    static final String MAIL_SUBJECT_PREFIX = "OpenHAB";

    public MessagesService(MailConnector mailConnector, EventPublisher eventPublisher) {
        this.mailConnector = mailConnector;
        this.messagesProcessor = new MessagesProcessor(eventPublisher);
    }

    public MessagesService(MailConnector mailConnector, MessagesProcessor messagesProcessor) {
        this.mailConnector = mailConnector;
        this.messagesProcessor = messagesProcessor;
    }

    public <T extends AbstractMessage>void sendMessage(T message, String... emails) throws ServiceException {
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
