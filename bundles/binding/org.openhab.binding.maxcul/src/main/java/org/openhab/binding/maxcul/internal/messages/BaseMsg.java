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

		pkt.len = (byte) (Integer.parseInt(rawMsg.substring(1, 3),16) & 0xFF); /* length of packet */
		if (pkt.len != ((rawMsg.length()-5)/2)) /* -5 => 'Z' and len byte (2 chars) and checksum at end?, div by two as it is a hex string  */
		{
			logger.error("Unable to process packet "+rawMsg+". Length is not correct.");
			pkt.len = 0; /* indicate not a valid packet */
			return;
		}
		int startIdx = 3;

		pkt.msgCount = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		pkt.msgFlag = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		pkt.msgTypeRaw = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		pkt.msgType = MaxCulMsgType.fromByte(pkt.msgTypeRaw);

		pkt.srcAddrStr = rawMsg.substring(startIdx,startIdx+6);
		pkt.srcAddr[0] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;
		pkt.srcAddr[1] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;
		pkt.srcAddr[2] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		pkt.dstAddrStr = rawMsg.substring(startIdx,startIdx+6);
		pkt.dstAddr[0] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;
		pkt.dstAddr[1] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;
		pkt.dstAddr[2] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		pkt.groupid = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
		startIdx += 2;

		int payloadStrLen = ((pkt.len)*2)+3-startIdx; /* +3 -> Z and len byte (2 chars) */
		int payloadByteLen = payloadStrLen / 2;
		logger.debug("Payload length = "+payloadStrLen+" => "+(payloadByteLen));
		pkt.payload = new byte[payloadByteLen];
		for (int payIdx = 0; payIdx < payloadByteLen; payIdx++ )
		{
			pkt.payload[payIdx] = (byte) (Integer.parseInt(rawMsg.substring(startIdx,startIdx+2),16) & 0xFF);
			startIdx += 2;
		}
	}

	public static MaxCulMsgType getMsgType(String rawMsg)
	{
		int len = (byte) (Integer.parseInt(rawMsg.substring(1, 3),16) & 0xFF); /* length of packet */
		if (len != ((rawMsg.length()-5)/2)) /* -3 => 'Z' and len byte (2 chars) and check byte, div by two as it is a hex string  */
		{
			logger.error("Unable to process packet "+rawMsg+". Length is not correct.");
			return MaxCulMsgType.UNKNOWN;
		}
		int startIdx = 7;
		return MaxCulMsgType.fromByte(Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16));
	}
}
