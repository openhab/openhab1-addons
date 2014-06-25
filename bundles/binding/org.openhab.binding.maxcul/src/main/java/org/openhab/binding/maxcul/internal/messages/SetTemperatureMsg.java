package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetTemperatureMsg extends BaseMsg {

	final static private int SET_TEMPERATURE_PAYLOAD_LEN = 1; /* in bytes */

	private static final Logger logger =
			LoggerFactory.getLogger(SetTemperatureMsg.class);

	private double desiredTemperature;
	private ThermostatControlMode ctrlMode;

	public SetTemperatureMsg(String rawMsg) {
		super(rawMsg);
		logger.debug(this.msgType+" Payload Len -> "+this.payload.length);

		if (this.payload.length == SET_TEMPERATURE_PAYLOAD_LEN)
		{
			/* extract temperature information */
			desiredTemperature = (this.payload[0] & 0x3f) / 2.0;
			/* extract control mode */
			ctrlMode = ThermostatControlMode.values()[(this.payload[0]>>6)];
		}
		else logger.error("Got "+this.msgType+" message with incorrect length!");
	}

	public SetTemperatureMsg(byte msgCount, byte msgFlag,
			byte groupId, String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.SET_TEMPERATURE, groupId, srcAddr, dstAddr);


	}

	public void debugPrint()
	{
		logger.debug("Set Temperature Message:");
		logger.debug("\tDesired Temperature => "+desiredTemperature);
		logger.debug("\tControl Mode => "+ctrlMode);
	}

	public double getDesiredTemperature()
	{
		return desiredTemperature;
	}

	public ThermostatControlMode getControlMode()
	{
		return ctrlMode;
	}
}
