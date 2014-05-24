package org.openhab.binding.maxcul.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper functions to handle and generate commands
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.5.0
 */
public class MaxCulCommandHelper {

	private static final Logger logger =
			LoggerFactory.getLogger(MaxCulCommandHelper.class);

	static class MaxPacket {
		byte len;
		byte msgCount;
		byte msgFlag;
		byte msgTypeRaw;
		MaxCulMsgType msgType;
		byte[] srcAddr = new byte[3];
		byte[] dstAddr = new byte[3];
		byte groupid;
		byte[] payload;
	}

	/**
	 * Take the raw message from the CUL stick and convert parse
	 * out the values from the string. Does a length check.
	 * @param rawMsg Message from CUL stick
	 * @return MaxPacket class if successful with all fields completed. Null if length check fails
	 */
	static public MaxPacket parsePacket(String rawMsg)
	{
		MaxPacket pkt = new MaxPacket();

		pkt.len = Byte.parseByte(rawMsg.substring(1, 3),16); /* length of packet */
		if (pkt.len != ((rawMsg.length()-3)/2)) /* -3 => 'Z' and len byte (2 chars), div by two as it is a hex string  */
		{
			logger.error("Unable to process packet "+rawMsg+". Length is not correct.");
			return null;
		}
		int startIdx = 3;

		pkt.msgCount = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgFlag = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgTypeRaw = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

		pkt.msgType = MaxCulMsgType.fromByte(pkt.msgTypeRaw);

		pkt.srcAddr[0] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.srcAddr[1] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;
		pkt.srcAddr[2] = Byte.parseByte(rawMsg.substring(startIdx,startIdx+2),16);
		startIdx += 2;

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

		return pkt;
	}

}
