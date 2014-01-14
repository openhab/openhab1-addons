package org.openhab.io.transport.cul;

/**
 * Listen to received events from the CUL. These events can be either received
 * data or an exception thrown while trying to read data.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULListener {

	public void dataReceived(String data);

	public void error(Exception e);

}
