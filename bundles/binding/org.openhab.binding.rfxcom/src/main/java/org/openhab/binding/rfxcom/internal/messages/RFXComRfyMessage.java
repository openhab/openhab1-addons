/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for RFY (Somfy RTS) message.
 * 
 * @author Jürgen Richtsfeld
 * @since 1.6
 */
public class RFXComRfyMessage extends RFXComBaseMessage {

	public enum Commands {
		STOP(0x00), 
		OPEN(0x01), 
		CLOSE(0x03), 
		UP_2SEC(0x11), 
		DOWN_2SEC(0x12);

		private final int command;

		Commands(int command) {
			this.command = command;
		}

		Commands(byte command) {
			this.command = command;
		}

		public byte toByte() {
			return (byte) command;
		}
	}

	public enum SubType {
		RFY(0), RFY_EXT(1);

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

	private final static List<RFXComValueSelector> supportedValueSelectors = Arrays
			.asList(RFXComValueSelector.RAW_DATA,
					RFXComValueSelector.SIGNAL_LEVEL,
					RFXComValueSelector.COMMAND);
	
	public SubType subType = SubType.RFY;
	/**
	 * valid numbers 0-4; 0 == all units
	 */
	public byte unitCode = 0;
	public byte id3 = 0;
	public Commands command = Commands.STOP;
	public byte signalLevel = 0xF; // maximum

	public RFXComRfyMessage() {
		packetType = PacketType.RFY;

	}

	public RFXComRfyMessage(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		if (rawMessage != null) {
			str += super.toString();
		}
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + id1 + " " + id2 + " " + id3;
		str += "\n - Unit code = " + unitCode;
		str += "\n - Command = " + command;
		str += "\n - Signal level = " + signalLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		subType = SubType.values()[super.subType];

		id3 = data[6];
		unitCode = data[7];

		command = Commands.STOP;

		for (Commands loCmd : Commands.values()) {
			if (loCmd.toByte() == data[8]) {
				command = loCmd;
				break;
			}
		}
		signalLevel = (byte) ((data[12] & 0xF0) >> 4);

	}

	@Override
	public byte[] decodeMessage() {
		final byte[] data = new byte[13];

		data[0] = 12;
		data[1] = RFXComBaseMessage.PacketType.RFY.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = id1;
		data[5] = id2;
		data[6] = id3;
		data[7] = unitCode;
		data[8] = command.toByte();
		data[12] = (byte) ((signalLevel & 0x0F) << 4);

		return data;
	}

	@Override
	public String generateDeviceId() {
		return id1 + "." + id2 + "." + id3 + "." + unitCode;
	}

	/**
	 * this was copied from RFXComBlinds1Message.
	 */
	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {
			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {
				state = new DecimalType(signalLevel);
			} else {
				throw new RFXComException("Can't convert " + valueSelector
						+ " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == RollershutterItem.class) {
			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (command) {
				case CLOSE:
					state = OpenClosedType.CLOSED;
					break;

				case OPEN:
					state = OpenClosedType.OPEN;
					break;

				default:
					break;
				}

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to RollershutterItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {
				state = new StringType( DatatypeConverter.printHexBinary(rawMessage));
			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to StringItem");
			}
		} else {
			throw new NumberFormatException("Can't convert " + valueSelector
					+ " to " + valueSelector.getItemClass());
		}

		return state;
	}

	@Override
	public void convertFromState(RFXComValueSelector valueSelector, String id,
			Object subType, Type type, byte seqNumber) throws RFXComException {

		this.subType = (RFXComRfyMessage.SubType) subType;
		this.seqNbr = seqNumber;
		String[] ids = id.split("\\.");
		this.id1 = (byte) Short.parseShort(ids[0]);
		this.id2 = (byte) Short.parseShort(ids[1]);
		this.id3 = (byte) Short.parseShort(ids[2]);
		this.unitCode = Byte.parseByte(ids[3]);

		switch (valueSelector) {
		case SHUTTER:
			if (type instanceof OpenClosedType) {
				this.command = (type == OpenClosedType.CLOSED ? Commands.CLOSE : Commands.OPEN);
			} else if (type instanceof UpDownType) {
				this.command = (type == UpDownType.UP ? Commands.OPEN : Commands.CLOSE);
			} else if (type instanceof StopMoveType) {
				this.command = RFXComRfyMessage.Commands.STOP;

			} else {
				throw new NumberFormatException("Can't convert " + type + " to Command");
			}
			break;
		default:
			throw new RFXComException("Can't convert " + type + " to " + valueSelector);
		}
	}

	@Override
	public Object convertSubType(String subType) throws RFXComException {
		for (SubType s : SubType.values()) {
			if (s.toString().equals(subType)) {
				return s;
			}
		}
		
		throw new RFXComException("Unknown sub type " + subType);
	}

	@Override
	public List<RFXComValueSelector> getSupportedValueSelectors()
			throws RFXComException {
		return supportedValueSelectors;
	}
}
