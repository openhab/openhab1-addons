package org.openhab.binding.maxcube.internal.exceptions;

/**
 * Will be thrown when there is an attempt to put a new message line into the message processor,
 * but the line starts with an unknown message indicator.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 */
public class UnsupportedMessageTypeException extends Exception {

}
