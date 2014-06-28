package org.openhab.binding.maxcul.internal.messages;

/**
 * Interface for MaxCul binding message processors
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface MaxCulBindingMessageProcessor {

	/**
	 * Process filtered CUL message in MAX! mode
	 * @param data
	 */
	void MaxCulMsgReceived( String data, boolean broadcast );
}
