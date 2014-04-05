package org.openhab.binding.freeswitch.internal;

/*
 * Message headers used in a ESL connection
 */
public enum FreeswitchMessageHeader {
	UUID("Unique-ID"),
	CHANNEl_CREATE("CHANNEL_CREATE"),
	CHANNEL_DESTROY("CHANNEL_DESTROY"),
	CALL_DIRECTION("Call-Direction"),
	CID_NAME("Caller-Caller-ID-Name"),
	CID_NUMBER("Caller-Caller-ID-Number"),
	DEST_NUMBER("Caller-Destination-Number"),
	ORIG_NUMBER("Caller-ANI"),
	MESSAGE_WAITING("MESSAGE_WAITING"),
	MWI_WAITING("MWI-Messages-Waiting"),
	MWI_ACCOUNT("MWI-Message-Account"),
	MWI_MESSAGE("MWI-Voice-Message");
	
	private final String text;
	
	private FreeswitchMessageHeader(String text){
		this.text = text;
	}
	
	@Override
	public String toString(){
		return text;
	}
	
	public boolean matches(String string){
		return this.toString().equals(string);
	}
}
