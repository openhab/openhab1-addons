package org.openhab.binding.maxcul.internal.messages;

import java.util.Arrays;

public class PairPingMsg extends BaseMsg{

	public int firmwareMajor;
	public int firmwareMinor;
	public int type;
	public int testResult;
	public String serial = null;

	public PairPingMsg(String rawMsg)
	{
		super(rawMsg);

		/* process payload */
		if (this.payload.length > 3)
		{
			this.firmwareMajor = this.payload[0]/16;
			this.firmwareMinor = this.payload[0]%16;
			this.type = this.payload[1];
			this.testResult = this.payload[2];
			this.serial = new String(Arrays.copyOfRange(this.payload, 3, this.payload.length));
		}
	}
}
