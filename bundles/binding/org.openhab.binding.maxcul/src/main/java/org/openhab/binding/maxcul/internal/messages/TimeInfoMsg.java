package org.openhab.binding.maxcul.internal.messages;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeInfoMsg extends BaseMsg {

	final static private int TIME_INFO_PAYLOAD_LEN = 5; /* in bytes */

	public TimeInfoMsg(String rawMsg) {
		super(rawMsg);
		// TODO Process message to extract time - not used at the moment
	}

	public TimeInfoMsg(byte msgCount, byte msgFlag,
			byte groupId, String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.TIME_INFO, groupId, srcAddr, dstAddr);

		updateTime();
	}

	public void updateTime()
	{
		byte[] payload = new byte[TIME_INFO_PAYLOAD_LEN];

		Calendar now = new GregorianCalendar();

		payload[0] = (byte) (now.get(Calendar.YEAR) - 2000);
		payload[1] = (byte) now.get(Calendar.DAY_OF_MONTH);
		payload[2] = (byte) now.get(Calendar.HOUR);
		payload[3] = (byte) (now.get(Calendar.MINUTE) | ((now.get(Calendar.MONTH) & 0x0C)<<4));
		payload[4] = (byte) (now.get(Calendar.SECOND) | ((now.get(Calendar.MONTH) & 0x03)<<6));

		super.appendPayload(payload);
	}
}
