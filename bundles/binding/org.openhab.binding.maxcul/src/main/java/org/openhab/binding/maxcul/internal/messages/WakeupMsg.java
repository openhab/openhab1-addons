package org.openhab.binding.maxcul.internal.messages;

/**
 * Wakeup message
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class WakeupMsg extends BaseMsg {

	final private int WAKEUP_MSG_PAYLOAD_LEN = 1;

	public WakeupMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.WAKEUP, groupId, srcAddr, dstAddr);

		byte[] payload = new byte[WAKEUP_MSG_PAYLOAD_LEN];

		payload[0] = 0x3f; // no idea what this means, just always 0x3f?

		super.appendPayload(payload);
	}

	public WakeupMsg(String rawmsg)
	{
		super(rawmsg);
	}

}
