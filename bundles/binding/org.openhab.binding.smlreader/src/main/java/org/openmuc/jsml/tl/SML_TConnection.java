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
package org.openmuc.jsml.tl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;

import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_AttentionRes;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.openmuc.jsml.structures.Unsigned8;

public class SML_TConnection {

	private Socket socket;
	private SML_File smlFile = null;
	private OctetString serverID;
	private DataOutputStream os;
	private DataInputStream is;
	private int messageTimeout;
	private int messageFragmentTimeout;

	public SML_TConnection(Socket socket, int messageTimeout, int messageFragmentTimeout) throws IOException {
		this.socket = socket;
		os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

		this.messageTimeout = messageTimeout;
		this.messageFragmentTimeout = messageFragmentTimeout;

	}

	/*
	 * table to calculate crc16
	 */
	private static int[] table = { 0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf, 0x8c48, 0x9dc1,
			0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e, 0xf8f7, 0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c, 0x75b7,
			0x643e, 0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed, 0xcb64, 0xf9ff, 0xe876, 0x2102, 0x308b, 0x0210, 0x1399,
			0x6726, 0x76af, 0x4434, 0x55bd, 0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7, 0xc87c, 0xd9f5, 0x3183,
			0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5, 0x453c, 0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef, 0xea66,
			0xd8fd, 0xc974, 0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732, 0x36bb, 0xce4c, 0xdfc5, 0xed5e,
			0xfcd7, 0x8868, 0x99e1, 0xab7a, 0xbaf3, 0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3, 0x263a,
			0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb, 0xaa72, 0x6306, 0x728f, 0x4014, 0x519d, 0x2522,
			0x34ab, 0x0630, 0x17b9, 0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78, 0x9bf1, 0x7387, 0x620e,
			0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1, 0x0738, 0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862, 0x9af9,
			0x8b70, 0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c, 0xd3a5, 0xe13e, 0xf0b7, 0x0840, 0x19c9, 0x2b52, 0x3adb,
			0x4e64, 0x5fed, 0x6d76, 0x7cff, 0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad, 0xc324, 0xf1bf, 0xe036, 0x18c1,
			0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7, 0x6c7e, 0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e, 0xf2a7,
			0xc03c, 0xd1b5, 0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74, 0x5dfd, 0xb58b, 0xa402, 0x9699,
			0x8710, 0xf3af, 0xe226, 0xd0bd, 0xc134, 0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5, 0x4d7c,
			0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a, 0xb2b3, 0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60,
			0x1de9, 0x2f72, 0x3efb, 0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb, 0xa232, 0x5ac5, 0x4b4c,
			0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3, 0x2e7a, 0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3, 0x8238,
			0x93b1, 0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70, 0x1ff9, 0xf78f, 0xe606, 0xd49d, 0xc514,
			0xb1ab, 0xa022, 0x92b9, 0x8330, 0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1, 0x0f78 };

	/**
	 * calculates crc16 as specified by DIN EN 62056-46
	 * 
	 * @param bytes
	 * @return crc16 value
	 */
	public static int crc16(byte[] bytes) {
		return crc16(bytes, bytes.length);
	}

	/**
	 * calculates crc16 from element 0 to length-1 as specified by DIN EN 62056-46
	 * 
	 * @param bytes
	 * @param length
	 *            bytes in array to take for calculation
	 * @return crc16 value
	 */
	public static int crc16(byte[] bytes, int length) {
		int crc = 0xffff;
		for (int i = 0; i < length; i++) {
			crc = (crc >> 8) ^ table[(crc ^ bytes[i]) & 0xff];
		}
		crc ^= 0xffff;
		crc = ((crc & 0xff) << 8) | ((crc & 0xff00) >> 8);
		return crc;
	}

	private static void writeUnsignedInt(DataOutputStream os, long value) throws IOException {
		os.writeByte((int) ((value & 0xFF000000L) >> 24));
		os.writeByte((int) ((value & 0x00FF0000L) >> 16));
		os.writeByte((int) ((value & 0x0000FF00L) >> 8));
		os.writeByte((int) (value & 0x000000FFL));
	}

	public void send(byte[] smlPackage) throws IOException {

		DataOutputStream socketStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

		// use ByteArrayOutputStream so that later we can use the toByteArray
		// member function
		ByteArrayOutputStream bs = new ByteArrayOutputStream(smlPackage.length + 30);
		DataOutputStream os = new DataOutputStream(bs);

		// Write start bytes of SML-TL
		writeUnsignedInt(os, 0x1b1b1b1bL);
		writeUnsignedInt(os, 0x01010101L);

		// insert escape sequences inside the sml-package if necessary
		int numEscapeSeqFound = 0;
		for (int i = 0; i < smlPackage.length; i += 4) {

			// if there is less than 3 bytes left to write
			if (i >= smlPackage.length - 3) {
				for (int j = i; j < smlPackage.length; j++) {
					os.write(smlPackage[j]);
				}
				break;
			}
			if ((smlPackage[i] & 0xff) == 0x1b && (smlPackage[i + 1] & 0xff) == 0x1b
					&& (smlPackage[i + 2] & 0xff) == 0x1b && (smlPackage[i + 3] & 0xff) == 0x1b) {
				writeUnsignedInt(os, 0x1b1b1b1bL);
				numEscapeSeqFound++;
			}
			for (int k = 0; k < 4; k++) {
				os.writeByte(smlPackage[i + k]);
			}
		}
		int numStuffBits = 4 - ((smlPackage.length + numEscapeSeqFound * 4) % 4);
		if (numStuffBits == 4) {
			numStuffBits = 0;
		}

		for (int i = 0; i < numStuffBits; i++) {
			os.writeByte(0x00);
		}

		// write last bytes
		writeUnsignedInt(os, 0x1b1b1b1bL);
		os.writeByte(0x1a);
		os.writeByte(numStuffBits & 0x00ff);

		byte[] packet = bs.toByteArray();

		// calculate crc16 of the whole packet
		int crc16 = crc16(packet);

		socketStream.write(packet);
		socketStream.writeShort(crc16 & 0xffff);
		socketStream.flush();

		os.close();
		bs.close();
	}

	/**
	 * sends an SML_AttentionRes with sock
	 * 
	 * @param attentionType
	 *            see SML_AttentionRes
	 * @param message
	 *            OPTIONAL human readable message
	 * @param serverID
	 *            id of this machine
	 * @param faultyMessage
	 *            the SML_Message which caused this attentionResponse
	 * @param socket
	 *            socket from which the faulty message was received, to answer on the same
	 */
	public void sendAttentionResponse(OctetString attentionType, String message, OctetString serverID,
			SML_Message faultyMessage, Socket socket) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os = new DataOutputStream(bs);
		// continue on error
		Unsigned8 abortOnError = new Unsigned8(0x00);
		OctetString attentionMsg = null;
		if (message != null) {
			attentionMsg = new OctetString(message.getBytes());
		}
		// send an UNEXPECTED_SMLMESSAGE AttentionResponse back to the client
		SML_AttentionRes attentionres = new SML_AttentionRes(serverID, attentionType, attentionMsg, null);
		SML_MessageBody attentionresBody = new SML_MessageBody(SML_MessageBody.AttentionResponse, attentionres);
		// take the transactionId and group number from the faulty SML_Message
		SML_Message attentionmessage = new SML_Message(faultyMessage.getTransactionId(), faultyMessage.getGroupNo(),
				abortOnError, attentionresBody);

		// encode the attentionResponse message and write the encoded data to
		// the stream os
		try {
			attentionmessage.code(os);
		} catch (IOException e) {
			// TODO remove all prints in this file
			e.printStackTrace();
		}
		// send it to the client
		try {
			send(bs.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SML_File receive() throws IOException {

		socket.setSoTimeout(messageTimeout);

		smlFile = null;

		DataInputStream is;

		is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		int fourBytes;
		int fourBytes2;

		try {
			// for CRC calculation
			ByteArrayOutputStream origPacketByteStream = new ByteArrayOutputStream();
			DataOutputStream origPacketStream = new DataOutputStream(origPacketByteStream);
			// check for start sequence
			fourBytes = is.readInt();
			socket.setSoTimeout(messageFragmentTimeout);

			origPacketStream.writeInt(fourBytes & 0xffffffff);
			fourBytes2 = is.readInt();
			origPacketStream.writeInt(fourBytes2 & 0xffffffff);
			if (!(((fourBytes & 0xffffffff) == 0x1b1b1b1b) && ((fourBytes2 & 0xffffffff) == 0x01010101))) {
				is.close();
				socket.close();
				throw new IOException("start sequence not found");
			}

			// use ByteArrayOutputStream so that later we can use the
			// toByteArray member function
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);

			int bytesToWriteLater = 0;
			boolean firstRead = true;

			while (true) {

				fourBytes = is.readInt();
				origPacketStream.writeInt(fourBytes & 0xffffffff);

				// check for endsequence
				if ((fourBytes & 0xffffffff) == 0x1b1b1b1b) {
					fourBytes = is.readInt();
					if ((fourBytes & 0xffffffff) != 0x1b1b1b1b) {
						byte firstByte = (byte) ((fourBytes & 0xff000000) >> 24);
						int numStuffBits = ((fourBytes & 0x00ff0000) >> 16);
						int receivedCRC = (fourBytes & 0x0000ffff);

						if (((firstByte & 0xff) != 0x1a) || (numStuffBits > 3) || firstRead) {
							os.close();
							bs.close();
							is.close();
							socket.close();
							throw new IOException("SMLTransLayerHandler: Termination sequence is wrong");
						}
						origPacketStream.writeByte(0x1a);
						origPacketStream.writeByte(numStuffBits & 0xff);
						int calculatedCRC = SML_TConnection.crc16(origPacketByteStream.toByteArray());
						if (calculatedCRC != receivedCRC) {

							os.close();
							bs.close();
							is.close();
							socket.close();
							throw new IOException("wrong CRC");
						}

						int numBytesToWrite = 4 - numStuffBits;
						byte[] bytesToWrite = { (byte) ((bytesToWriteLater & 0xff000000) >> 24),
								(byte) ((bytesToWriteLater & 0x00ff0000) >> 16),
								(byte) ((bytesToWriteLater & 0x0000ff00) >> 8), (byte) ((bytesToWriteLater & 0xff)) };
						for (int i = 0; i < numBytesToWrite; i++) {

							os.writeByte(bytesToWrite[i] & 0xff);
						}

						byte[] smlPacket = bs.toByteArray();

						os.close();
						bs.close();
						return handleSMLStream(smlPacket, socket);

					}
					else {
						origPacketStream.writeInt(fourBytes & 0xffffffff);
					}

				}
				// if (!endOfFile) {
				if (!firstRead) {
					os.writeInt(bytesToWriteLater & 0xffffffff);
				}

				firstRead = false;
				bytesToWriteLater = fourBytes;
				// }
			}

		} catch (SocketException e) {
			throw e;
		} catch (InterruptedIOException iioe) {
			// receiveTimeout occurred
			is.close();
			throw iioe;
		} catch (IOException e) {
			throw e;
		}

	}

	/**
	 * tries to decode all SML_Messages in smlPacket sent over socket gives the message to handleSMLMessage
	 * 
	 * @param smlPacket
	 *            byteArray of SML_Messages (could be one message, one file or several messages but no complete file)
	 * @param socket
	 *            receive socket
	 * @throws IOException
	 */
	private SML_File handleSMLStream(byte[] smlPacket, Socket socket) throws IOException {
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(smlPacket));

		try {
			while (is.available() > 0) {
				SML_Message smlMessage = new SML_Message();

				if (!smlMessage.decode(is)) {
				}
				else {
					SML_File sml_file = handleSMLMessage(smlMessage, socket);
					if (sml_file != null) {
						return sml_file;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		throw new IOException("Unable to decode package");
	}

	/**
	 * Interprets the type of message and handle them. Packages the publicOpen, publicClose and the messages between to
	 * one SML_File and redirects the file to the ReceiveMessageListener if publicClose was received. Sends an
	 * attentionResponse, if a file doesn't begin with a publicOpen. If a attentionResponse was received, it is
	 * redirected to the ReceiveMessageListener immediately.
	 * 
	 * @param message
	 *            received SML_Message
	 * @param socket
	 *            receive socket
	 * @throws IOException
	 */
	private SML_File handleSMLMessage(SML_Message message, Socket socket) throws IOException {

		int tag = message.getMessageBody().getTag().getVal();

		if (tag == SML_MessageBody.OpenResponse || tag == SML_MessageBody.OpenRequest) {
			// TODO check if previous file was closed
			smlFile = new SML_File();
		}

		// if AttentionReponse was received, open SML_File, save the response,
		// give it to the listener and close the file
		else if (tag == SML_MessageBody.AttentionResponse) {
			smlFile = new SML_File();
			smlFile.add(message);
			return smlFile;
		}

		// if smlFile is null, no Open-Message was received before
		if (smlFile == null) {

			// response with an AttentionResponse of type UNEXPECTED_SMLMESSAGE
			sendAttentionResponse(SML_AttentionRes.UNEXPECTED_SMLMESSAGE, null, serverID, message, socket);

			throw new IOException("SML_Message without PublicOpen* before received! send AttentionResponse");
		}

		// add message to the file
		smlFile.add(message);

		if (tag == SML_MessageBody.CloseRequest || tag == SML_MessageBody.CloseResponse) {
			return smlFile;
		}
		return null;
	}

	public void disconnect() {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Set the TConnection timeout for waiting for the first byte of a new message. Default is 0 (unlimited)
	 * 
	 * @param messageTimeout
	 *            in milliseconds
	 * @throws SocketException
	 */
	public void setMessageTimeout(int messageTimeout) throws SocketException {
		this.messageTimeout = messageTimeout;
	}

	/**
	 * Set the TConnection timeout for receiving data once the beginning of a message has been received. Default is 2000
	 * (2seconds)
	 * 
	 * @param messageFragmentTimeout
	 *            in milliseconds
	 * @throws SocketException
	 */
	public void setMessageFragmentTimeout(int messageFragmentTimeout) throws SocketException {
		this.messageFragmentTimeout = messageFragmentTimeout;
	}

	public void close() {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
			}
			os = null;
		}

		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
			is = null;
		}

		if (socket != null && socket.isBound()) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			socket = null;
		}
		// connected = false;
	}

}
