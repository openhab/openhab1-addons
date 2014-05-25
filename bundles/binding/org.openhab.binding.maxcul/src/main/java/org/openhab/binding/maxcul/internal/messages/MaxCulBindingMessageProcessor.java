package org.openhab.binding.maxcul.internal.messages;

public interface MaxCulBindingMessageProcessor {

	/**
	 * Process filtered CUL message in MAX! mode
	 * @param data
	 */
	void MaxCulMsgReceived( String data );
}
