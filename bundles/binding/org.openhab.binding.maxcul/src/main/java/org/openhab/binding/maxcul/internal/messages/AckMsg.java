package org.openhab.binding.maxcul.internal.messages;

public class AckMsg extends BaseMsg {

	private boolean isNack;

	public AckMsg(String rawMsg) {
		super(rawMsg);
		isNack = (this.payload[0] == 0x80);
	}

	public boolean getIsNack()
	{
		return isNack;
	}

}
