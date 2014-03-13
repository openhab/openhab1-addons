package org.openhab.binding.freeswitch.internal;

/*
 * Message headers used in a ESL connection
 */
public enum FreeswitchMessageHeader {
	CHANNEl_CREATE("CHANNEL_CREATE"),
	CHANNEL_DESTROY("CHANNEL_DESTROY"),
	MESSAGE_WAITING("MESSAGE_WAITING"),
	UUID("Unique-ID"),
	CID_NAME("Caller-Caller-ID-Name"),
	CID_NUMBER("Caller-Caller-ID-Number"),
	DEST_NUMBER("Caller-Destination-Number"),
	ORIG_NUMBER("Caller-ANI"),
	MWI_WAITING("MWI-Messages-Waiting"),
	MWI_ACCOUNT("MWI-Message-Account"),
	MWI_MESSAGE("MWI-Voice-Message");
	
	private final String text;
	
	private FreeswitchMessageHeader(String text){
		this.text = text;
	}
	
	public String toString(){
		return text;
	}
}
