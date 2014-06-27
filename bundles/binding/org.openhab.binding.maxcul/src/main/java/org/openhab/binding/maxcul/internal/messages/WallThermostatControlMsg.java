package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle Wall Thermostat Control messages
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class WallThermostatControlMsg extends BaseMsg {

	final static private int WALL_THERMOSTAT_CONTROL_PAYLOAD_LEN = 2; /* in bytes */

	private static final Logger logger =
			LoggerFactory.getLogger(WallThermostatControlMsg.class);

	private double desiredTemperature;
	private double measuredTemperature;

	public WallThermostatControlMsg(String rawMsg) {
		super(rawMsg);
		logger.debug(this.msgType+" Payload Len -> "+this.payload.length);

		if (this.payload.length == WALL_THERMOSTAT_CONTROL_PAYLOAD_LEN)
		{
			desiredTemperature = (this.payload[0] & 0x7F)/2.0;
			measuredTemperature = (((this.payload[0] & 0x80)<<1) + this.payload[1])/10.0; // temperature over 25.5 uses extra bit in desiredTemperature byte
		}
		else logger.error("Got "+this.msgType+" message with incorrect length!");
	}

	public WallThermostatControlMsg(byte msgCount, byte msgFlag,
			byte groupId, String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.WALL_THERMOSTAT_STATE, groupId, srcAddr, dstAddr);


	}

	@Override
	protected void printFormattedPayload()
	{
		logger.debug("\tDesired Temperature  => "+desiredTemperature);
		logger.debug("\tMeasured Temperature => "+measuredTemperature);
	}

	public double getMeasuredTemperature() {
		return measuredTemperature;
	}

	public double getDesiredTemperature() {
		return desiredTemperature;
	}
}
