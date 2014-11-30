package org.openhab.binding.mailcontrol.service;

import static org.creek.mailcontrol.model.message.MessageType.ITEM_COMMAND_REQUEST_MESSAGE;

import java.util.Set;

import org.creek.mailcontrol.model.command.ItemCommand;
import org.creek.mailcontrol.model.message.GenericMessage;
import org.creek.mailcontrol.model.message.GenericMessageTransformer;
import org.creek.mailcontrol.model.message.ItemCommandRequestMessage;
import org.creek.mailcontrol.model.message.TransformException;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class MessagesProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessagesProcessor.class);
    
    private final GenericMessageTransformer messageTransformer;
    private final ItemCommandProcessor itemCommandProcessor;
    
    public MessagesProcessor(EventPublisher eventPublisher) {
        this.itemCommandProcessor = new ItemCommandProcessor(eventPublisher);
        this.messageTransformer = new GenericMessageTransformer();
    }
    
    public MessagesProcessor(ItemCommandProcessor itemCommandProcessor, GenericMessageTransformer messageTransformer) {
        this.itemCommandProcessor = itemCommandProcessor;
        this.messageTransformer = messageTransformer;
    }
    
    public <T extends Command> void processReceivedMessages(Set<Object> messages) throws TransformException {
        logger.debug("Processing messages");
        for (Object msg: messages) {
            if (msg instanceof String) {
                logger.debug("Message: " + msg);
                GenericMessage message = messageTransformer.transform((String)msg);
                logger.debug("Processing message: " + message);
                if (message.getMessageType() == ITEM_COMMAND_REQUEST_MESSAGE) {
                    ItemCommand itemCommand = (ItemCommand)((ItemCommandRequestMessage)message).getItemCommand();
                    itemCommandProcessor.processItemCommand(itemCommand);
                }
            }
        }
    }
}
