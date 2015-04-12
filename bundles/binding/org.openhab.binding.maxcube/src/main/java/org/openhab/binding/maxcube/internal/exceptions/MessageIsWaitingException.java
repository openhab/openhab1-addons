package org.openhab.binding.maxcube.internal.exceptions;

/**
 * Will be thrown when there is an attempt to put a new message line into the message processor,
 * but the processor is not yet ready to handle new lines because there is already a message that
 * has be pulled before.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 */
public class MessageIsWaitingException extends Exception {

}
