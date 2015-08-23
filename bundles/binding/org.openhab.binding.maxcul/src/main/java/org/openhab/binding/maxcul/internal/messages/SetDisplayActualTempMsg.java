package org.openhab.binding.maxcul.internal.messages;

public class SetDisplayActualTempMsg extends BaseMsg {

	final private int SET_DISPLAY_ACTUAL_TEMP_PAYLOAD_LEN = 1;
	
	public SetDisplayActualTempMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr,
			String dstAddr, boolean displayActualTemp) {
		super(msgCount, msgFlag, MaxCulMsgType.SET_DISPLAY_ACTUAL_TEMP, groupId, srcAddr, dstAddr);
		byte[] payload = new byte[SET_DISPLAY_ACTUAL_TEMP_PAYLOAD_LEN];
		payload[0] =  displayActualTemp?( byte )0x04:( byte )0x00;
		super.appendPayload(payload);
	}

}
