package org.openhab.binding.maxcul.internal.messages;

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
	public String rawMsg;

	private boolean flgReadyToSend = false;

	private static final Logger logger =
			LoggerFactory.getLogger(BaseMsg.class);

	/**
	 * Constructor based on received message
	 * @param rawMsg
	 */
	public BaseMsg(String rawMsg)
	{
		BaseMsg pkt = this;

		this.rawMsg = rawMsg;

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

	/**
	 * This constructor creates a raw message ready for sending.
	 * @param msgCount Message Counter
	 * @param msgFlag Message flag
	 * @param msgType the message type
	 * @param groupId Group ID
	 * @param srcAddr Source address of controller
	 * @param dstAddr Dest addr of device
	 * @param payload payload of message
	 */
	public BaseMsg(byte msgCount, byte msgFlag, MaxCulMsgType msgType, byte groupId, String srcAddr, String dstAddr, byte[] payload)
	{
		StringBuilder sb = new StringBuilder();

		this.msgCount = msgCount;
		sb.append(String.format("%02x", msgCount));

		this.msgFlag = msgFlag;
		sb.append(String.format("%02x", msgFlag));

		this.msgType = msgType;
		this.msgTypeRaw = this.msgType.toByte();
		sb.append(String.format("%02x", this.msgTypeRaw));

		this.srcAddrStr = srcAddr;
		this.srcAddr[0] = (byte) (Integer.parseInt(srcAddr.substring(0,2),16) & 0xFF);
		this.srcAddr[1] = (byte) (Integer.parseInt(srcAddr.substring(2,4),16) & 0xFF);
		this.srcAddr[2] = (byte) (Integer.parseInt(srcAddr.substring(4,6),16) & 0xFF);
		sb.append(srcAddr);

		this.dstAddrStr = dstAddr;
		this.dstAddr[0] = (byte) (Integer.parseInt(dstAddr.substring(0,2),16) & 0xFF);
		this.dstAddr[1] = (byte) (Integer.parseInt(dstAddr.substring(2,4),16) & 0xFF);
		this.dstAddr[2] = (byte) (Integer.parseInt(dstAddr.substring(4,6),16) & 0xFF);
		sb.append(dstAddr);

		this.payload = payload;
		for (int byteIdx = 0; byteIdx < payload.length; byteIdx++) {
			sb.append(String.format("%02x", payload[byteIdx]));
		}

		/* prepend length & Z command */
		byte len = (byte) ((sb.length() / 2) & 0xFF);
		if ((int)len * 2 != sb.length())
			logger.error("Unable to build raw message. Length is not correct");
		sb.insert(0, String.format("Z%02x", len));

		this.rawMsg = sb.toString();
		this.flgReadyToSend = true;
	}

	/**
	 * This constructor creates an incomplete raw message ready for sending. Payload must be set before sending.
	 * @param msgCount Message Counter
	 * @param msgFlag Message flag
	 * @param msgType the message type
	 * @param groupId Group ID
	 * @param srcAddr Source address of controller
	 * @param dstAddr Dest addr of device
	 */
	public BaseMsg(byte msgCount, byte msgFlag, MaxCulMsgType msgType, byte groupId, String srcAddr, String dstAddr)
	{
		StringBuilder sb = new StringBuilder();

		this.msgCount = msgCount;
		sb.append(String.format("%02x", msgCount));

		this.msgFlag = msgFlag;
		sb.append(String.format("%02x", msgFlag));

		this.msgType = msgType;
		this.msgTypeRaw = this.msgType.toByte();
		sb.append(String.format("%02x", this.msgTypeRaw));

		this.srcAddrStr = srcAddr;
		this.srcAddr[0] = (byte) (Integer.parseInt(srcAddr.substring(0,2),16) & 0xFF);
		this.srcAddr[1] = (byte) (Integer.parseInt(srcAddr.substring(2,4),16) & 0xFF);
		this.srcAddr[2] = (byte) (Integer.parseInt(srcAddr.substring(4,6),16) & 0xFF);
		sb.append(srcAddr);

		this.dstAddrStr = dstAddr;
		this.dstAddr[0] = (byte) (Integer.parseInt(dstAddr.substring(0,2),16) & 0xFF);
		this.dstAddr[1] = (byte) (Integer.parseInt(dstAddr.substring(2,4),16) & 0xFF);
		this.dstAddr[2] = (byte) (Integer.parseInt(dstAddr.substring(4,6),16) & 0xFF);
		sb.append(dstAddr);
		this.rawMsg = sb.toString();
	}

	protected void appendPayload(byte[] payload)
	{
		StringBuilder sb = new StringBuilder(this.rawMsg);
		this.payload = payload;
		for (int byteIdx = 0; byteIdx < payload.length; byteIdx++) {
			sb.append(String.format("%02x", payload[byteIdx]));
		}

		/* prepend length & Z command */
		byte len = (byte) ((sb.length() / 2) & 0xFF);
		if ((int)len * 2 != sb.length())
			logger.error("Unable to build raw message. Length is not correct");
		sb.insert(0, String.format("Z%02x", len));

		this.rawMsg = sb.toString();
		this.flgReadyToSend = true;
	}

	/**
	 * Return the type of message that has been received given the message string
	 * @param rawMsg Message string from CUL device
	 * @return MaxCulMsgType extracted from the message
	 */
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

	public int requiredCredit()
	{
		/* length in bits = amount of credit needed
		 * length in bits = num chars * 4
		 * This is because each char represents 4 bits of a hex number. RawMsg length
		 * is decremented by one to account for the 'Z'
		 */
		int credit = (this.rawMsg.length()-1)*4;

		/* credit is in 10ms units, round up */
		credit = (int)Math.ceil(credit/10.0);

		return credit;
	}

	/**
	 * Indicate if this packet is complete
	 * @return true if packet is ready to send
	 */
	public boolean readyToSend()
	{
		return this.flgReadyToSend;
	}

	public boolean requireCallback;

	/**
	 * Function called when requireCallback is set and a response is received associated with
	 * this message
	 */
	public void callBack()
	{
		/* Do nothing */
	}
}
