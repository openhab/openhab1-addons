package org.openhab.binding.maxcul.internal.messages;

/**
 * Interface for MaxCul binding message processors
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface MaxCulBindingMessageProcessor {

	/**
	 * Process filtered CUL message in MAX! mode
	 * @param data Raw data of packet
	 * @param broadcast True if a broadcast packet or is snooped and has a valid dest addr, false if is addressed to us
	 */
	void MaxCulMsgReceived( String data, boolean broadcast);
}
