package org.openhab.binding.maxcul.internal.messages;

import org.openhab.binding.maxcul.internal.MaxCulMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseMsg {
	public byte len;
	public byte msgCount;
	public byte msgFlag;
	public byte msgTypeRaw;
	public MaxCulMsgType msgType;
	public byte[] srcAddr = new byte[3];
	public String srcAddrStr;
	public byte[] dstAddr = new byte[3];
	public String dstAddrStr;
	public byte groupid;
	public byte[] payload;

	private static final Logger logger =
			LoggerFactory.getLogger(BaseMsg.class);

	public BaseMsg(String rawMsg)
	{
		BaseMsg pkt = this;

		pkt.len = Byte.parseByte(rawMsg.substring(1, 3),16); /* length of packet */
		if (pkt.len != ((rawMsg.length()-3)/2)) /* -3 => 'Z' and len byte (2 chars), div by two as it is a hex string  */
		{
			logger.error("Unable to process packet "+rawMsg+". Length is not correct.");
			pkt.len = 0; /* indicate not a valid packet */
			return;
		}
		int startIdx = 3;

		pkt.msgCount = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgFlag = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgTypeRaw = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgType = MaxCulMsgType.fromByte(pkt.msgTypeRaw);

		pkt.srcAddrStr = rawMsg.substring(startIdx,startIdx+6);
		pkt.srcAddr[0] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.srcAddr[1] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.srcAddr[2] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.dstAddrStr = rawMsg.substring(startIdx,startIdx+6);
		pkt.dstAddr[0] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.dstAddr[1] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.dstAddr[2] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.groupid = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		int payloadStrLen = ((pkt.len-1)*2)-startIdx;
		int payloadByteLen = payloadStrLen / 2;
		logger.debug("Payload length = "+payloadStrLen+" => "+(payloadByteLen));
		pkt.payload = new byte[payloadByteLen];
		for (int payIdx = 0; payIdx < payloadByteLen; payIdx++ )
		{
			pkt.payload[payIdx] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
			startIdx += 2;
		}
	}

	public static MaxCulMsgType getMsgType(String rawMsg)
	{
		int len = Byte.parseByte(rawMsg.substring(1, 3),16); /* length of packet */
		if (len != ((rawMsg.length()-3)/2)) /* -3 => 'Z' and len byte (2 chars), div by two as it is a hex string  */
		{
			logger.error("Unable to process packet "+rawMsg+". Length is not correct.");
			return MaxCulMsgType.UNKNOWN;
		}
		int startIdx = 7;
		return MaxCulMsgType.fromByte(Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16));
	}
}
