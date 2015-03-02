/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.openmuc.jsml.tl.SML_TConnection;

public class SML_Message extends Sequence {
	protected OctetString transactionId;
	protected Unsigned8 groupNo;
	protected Unsigned8 abortOnError;
	protected SML_MessageBody messageBody;
	protected Unsigned16 crc16;
	protected EndOfSmlMessage endOfSmlMsg;

	public void setTransactionId(OctetString transactionId) {
		this.transactionId = transactionId;
	}

	public void setGroupNo(Unsigned8 groupNo) {
		this.groupNo = groupNo;
	}

	public void setAbortOnError(Unsigned8 abortOnError) {
		this.abortOnError = abortOnError;
	}

	public OctetString getTransactionId() {
		return transactionId;
	}

	public Unsigned8 getGroupNo() {
		return groupNo;
	}

	public Unsigned8 getAbortOnError() {
		return abortOnError;
	}

	public SML_MessageBody getMessageBody() {
		return messageBody;
	}

	public SML_Message(OctetString transactionId, Unsigned8 groupNo, Unsigned8 abortOnError, SML_MessageBody messageBody) {
		this.transactionId = transactionId;
		this.groupNo = groupNo;
		this.abortOnError = abortOnError;
		this.messageBody = messageBody;
		crc16 = new Unsigned16();
		endOfSmlMsg = new EndOfSmlMessage();
		seqArray = new ASNObject[] { transactionId, groupNo, abortOnError, messageBody, crc16, endOfSmlMsg };
		isSelected = true;
	}

	public SML_Message() {
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os2 = new DataOutputStream(bs);

		super.code(os2);

		byte[] bytes = bs.toByteArray();

		// -4 = 3 Bytes for Unsigned16 (crc) and 1 byte for EndOfSMLMessage
		int crc = SML_TConnection.crc16(bytes, bytes.length - 4);
		crc16 = new Unsigned16(crc);

		// write crc16 to byte-array (bytes.lenght-1 is the
		// EndOfSMLMessage-byte)
		bytes[bytes.length - 3] = (byte) ((crc & 0xFF00) >> 8);
		bytes[bytes.length - 2] = (byte) ((crc & 0x00FF));

		os.write(bytes);
	}

	public boolean decodeAndCheck(DataInputStream is) throws IOException {
		int size = is.available();
		is.mark(size + 1);
		if (super.decode(is)) {
			int rest = is.available();

			int crc = crc16.getVal();

			// messageEndingSize: 3 or 2 bytes (TL-Field + Data) of crc16 and 1
			// byte of
			// endOfSmlMessage
			int messageEndingSize = 4;
			if (crc <= 0xff) {
				messageEndingSize = 3;
			}

			byte[] message = new byte[size - rest - messageEndingSize];
			is.reset();
			is.read(message, 0, size - rest - messageEndingSize);
			is.skip(messageEndingSize);

			int crcmessage = SML_TConnection.crc16(message);

			if (crc != crcmessage) {
				// crc might have been encoded with 3 instead of 2 bytes even
				// though it is < 0xff
				if (messageEndingSize == 3 && crc == SML_TConnection.crc16(message, message.length - 1)) {
					return true;
				}
				return false;
			}

			return true;
		}
		return false;
	}

	@Override
	protected void createElements() {
		transactionId = new OctetString();
		groupNo = new Unsigned8();
		abortOnError = new Unsigned8();
		messageBody = new SML_MessageBody();
		crc16 = new Unsigned16();
		endOfSmlMsg = new EndOfSmlMessage();
		seqArray = new ASNObject[] { transactionId, groupNo, abortOnError, messageBody, crc16, endOfSmlMsg };
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("--- SML_Message begin: ---");
			super.print();
			System.out.println("--- SML_Message end ---");
		}
	}
}
