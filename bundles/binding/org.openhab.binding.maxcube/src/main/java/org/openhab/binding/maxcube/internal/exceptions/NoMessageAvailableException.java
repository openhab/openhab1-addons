package org.openhab.binding.maxcube.internal.exceptions;

/**
 * Will be thrown when there is an attempt to pull a  message from the message processor,
 * but the processor does not yet have a complete message.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 */
public class NoMessageAvailableException extends Exception {

}
