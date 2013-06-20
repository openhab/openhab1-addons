package org.openhab.binding.rfxcom.internal.messages;

public class RFXComEnergyMessage  extends RFXComBaseMessage {

	public enum SubType {
		ELEC1(0),
		ELEC2(1),

		UNKNOWN(255);

		private final int subType;

		SubType(int subType) {
			this.subType = subType;
		}

		SubType(byte subType) {
			this.subType = subType;
		}

		public byte toByte() {
			return (byte) subType;
		}
	}

	public SubType subType = SubType.UNKNOWN;
	public int sensorId = 0;
	public int count = 0;
	public double instantAmps = 0;
	public double totalAmpHours = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComEnergyMessage() {
		packetType = PacketType.ENERGY;
	}

	public RFXComEnergyMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Count = " + count;
		str += "\n - Instant Amps = " + instantAmps;
		str += "\n - Total Amp Hours = " + totalAmpHours;
		str += "\n - Signal level = " + signalLevel;
		str += "\n - Battery level = " + batteryLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

/*
        enum ENERGY : byte
        {
            packetlength = 0,
            packettype = 1,
            subtype = 2,
            seqnbr = 3,
            id1 = 4,
            id2 = 5,
            count = 6,
            instant1 = 7,
            instant2 = 8,
            instant3 = 9,
            instant4 = 10,
            total1 = 11,
            total2 = 12,
            total3 = 13,
            total4 = 14,
            total5 = 15,
            total6 = 16,
            battery_level = 17,
            //bits 3-0
            rssi = 17,
            //bits 7-4
            size = 17,

            //energy
            pType = 0x5a,		// ENERGY
            ELEC2 = 0x1         // CM119/160
        }
 */
		for (SubType st : SubType.values()) {
			if (st.toByte() == super.subType) {
				subType = st;
				break;
			}
		}
		
		sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);
		count = data[6];
		
		// all usage is reported in Watts based on 230V
		double instantUsage = ((data[7] & 0xFF) << 24 | (data[8] & 0xFF) << 16 | (data[9] & 0xFF) << 8 | (data[10] & 0xFF));
		double totalUsage = ((data[11] & 0xFF) << 40 | (data[12] & 0xFF) << 32 | (data[13] & 0xFF) << 24 | (data[14] & 0xFF) << 16 | (data[15] & 0xFF) << 8 | (data[16] & 0xFF)) / 223.666;
		
		// convert to amps so external code can determine the watts based on local voltage
		instantAmps = instantUsage / 230F;
		totalAmpHours = totalUsage / 230F;
		
		signalLevel = (byte) ((data[17] & 0xF0) >> 4);
		batteryLevel = (byte) (data[17] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		throw new RuntimeException("RFXCOM does not support transmitting ENERGY messages!");
	}
	
	@Override
	public String generateDeviceId() {
		 return String.valueOf(sensorId);
	}

}
