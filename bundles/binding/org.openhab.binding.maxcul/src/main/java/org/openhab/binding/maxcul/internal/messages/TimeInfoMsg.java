package org.openhab.binding.maxcul.internal.messages;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle time information
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class TimeInfoMsg extends BaseMsg {

	final static private int TIME_INFO_PAYLOAD_LEN = 5; /* in bytes */

	private static final Logger logger =
			LoggerFactory.getLogger(TimeInfoMsg.class);

	private Calendar messageTimeInfo;
	private TimeZone tz = TimeZone.getTimeZone("Europe/London");

	public TimeInfoMsg(String rawMsg) {
		super(rawMsg);

		logger.debug("Year  => "+(this.payload[0]+2000));
		logger.debug("Month => "+(((this.payload[3]&0xC0)>>4)|((this.payload[4]&0xC0)>>6)));
		logger.debug("DoM   => "+this.payload[1]);
		logger.debug("Hour  => "+(this.payload[2] & 0x3F));
		logger.debug("Min   => "+(this.payload[3] & 0x3F));
		logger.debug("Sec   => "+(this.payload[4] & 0x3F));

		messageTimeInfo = new GregorianCalendar(this.payload[0]+2000, // year
											    (((this.payload[3]&0xC0)>>4)|((this.payload[4]&0xC0)>>6))-1, // month
												this.payload[1], // day of month
												(this.payload[2] & 0x3F), // hour of day
												(this.payload[3] & 0x3F), // minute
												(this.payload[4] & 0x3F)); // seconds
	}

	public TimeInfoMsg(byte msgCount, byte msgFlag,
			byte groupId, String srcAddr, String dstAddr, String TimeZoneStr) {
		super(msgCount, msgFlag, MaxCulMsgType.TIME_INFO, groupId, srcAddr, dstAddr);

		tz = TimeZone.getTimeZone(TimeZoneStr);
		updateTime();
	}

	public void updateTime()
	{
		byte[] payload = new byte[TIME_INFO_PAYLOAD_LEN];

		Calendar now = new GregorianCalendar(tz);

		payload[0] = (byte) (now.get(Calendar.YEAR) - 2000);
		payload[1] = (byte) now.get(Calendar.DAY_OF_MONTH);
		payload[2] = (byte) now.get(Calendar.HOUR_OF_DAY); // TODO ?? can set DST flag in bit[6] to advance time by 1hour, but java handles this
		payload[3] = (byte) ((now.get(Calendar.MINUTE)&0x3f) | ((now.get(Calendar.MONTH+1) & 0x0C)<<4));
		payload[4] = (byte) ((now.get(Calendar.SECOND)&0x3f) | ((now.get(Calendar.MONTH+1) & 0x03)<<6));

		super.appendPayload(payload);
		super.printDebugPayload();
	}

	@Override
	protected void printFormattedPayload()
	{
		super.printDebugPayload();
		logger.debug("\tDecoded Time: "+this.messageTimeInfo.get(Calendar.YEAR)+"-"+this.messageTimeInfo.get(Calendar.MONTH+1)+"-"+this.messageTimeInfo.get(Calendar.DAY_OF_MONTH)
									   +" "+this.messageTimeInfo.get(Calendar.HOUR_OF_DAY)+":"+this.messageTimeInfo.get(Calendar.MINUTE)+":"+this.messageTimeInfo.get(Calendar.SECOND));
	}
}
