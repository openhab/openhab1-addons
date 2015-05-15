package org.openhab.binding.maxcube.internal.exceptions;

/**
 * Will be thrown when there is an attempt to put a new message line into the message processor,
 * the processor detects a known message indicator, but the message could not be parsed correctly.
 * 
 * @author Christian Rockrohr <christian@rockrohr.de>
 */
public class UnprocessableMessageException extends Exception {

}
