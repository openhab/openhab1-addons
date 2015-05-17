package org.openhab.binding.maxcube.internal.exceptions;

/**
 * Will be thrown when there is an attempt to put a new message line into the message processor,
 * but the processor is currently processing an other message type.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 */
public class IncompleteMessageException extends Exception {

}
