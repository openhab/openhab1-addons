package org.openhab.binding.maxcul.internal.messages;

public class PairPongMsg extends BaseMsg {

	final static private int PAIR_PONG_PAYLOAD_LEN = 5; /* in bytes */

	public PairPongMsg(byte msgCount, byte msgFlag, MaxCulMsgType msgType,
			byte groupId, String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, msgType, groupId, srcAddr, dstAddr);

		byte[] payload = new byte[PAIR_PONG_PAYLOAD_LEN];



		super.appendPayload(payload);
	}

}
