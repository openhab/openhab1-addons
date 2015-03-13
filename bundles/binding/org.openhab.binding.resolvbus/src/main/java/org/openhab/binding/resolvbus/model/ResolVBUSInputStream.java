package org.openhab.binding.resolvbus.model;

import java.util.List;

import org.openhab.binding.resolvbus.internal.ResolVBUSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResolVBUSInputStream {

	private static final Logger logger = 
			LoggerFactory.getLogger(ResolVBUSInputStream.class);
	
	private int sourceAddress;
	private int destinationAdress;
	private int protocolVersion;
	private boolean errorFree;
	private int command;
	private char[] rawCharData;
	private byte[] rawByteData;
	private char[] payloadChar;
	private byte[] payloadByte;

	public int getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(int sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public int getDestinationAdress() {
		return destinationAdress;
	}

	public void setDestinationAdress(int destinationAdress) {
		this.destinationAdress = destinationAdress;
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public boolean isErrorFree() {
		return errorFree;
	}

	public void setErrorFree(boolean errorFree) {
		this.errorFree = errorFree;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}



	public char[] getPayloadChar() {
		return payloadChar;
	}

	public void setPayloadChar(char[] payload) {
		this.payloadChar = payload;
	}

	public ResolVBUSInputStream(List<Byte> rawData) {
		int rawDataSize = rawData.size();
		this.rawCharData = new char [rawDataSize];
		this.rawByteData = new byte [rawDataSize];
		for (int i = 0; i< rawDataSize;i++) {
			this.rawCharData[i] = (char) (rawData.get(0)&0xFFFF);
			this.rawByteData[i] = rawData.remove(0);
		}
		processPacket();
	}

	private void processPacket() {

		if (rawCharData.length < 9) {
			logger.debug("Stream to short, current length: "+rawCharData.length);
			
			return;
		}

		if (checkMSB()) {
			logger.debug("MSB is set => Stream has errors");
			errorFree = false;
			return;
		}

		// SyncFlag?
		if (rawCharData[0] != (char) ((byte) 0xAA&0xffff)) {
			logger.debug("No Sync Flag");
			errorFree = false;
			return;
		}

		destinationAdress = rawCharData[1];
		destinationAdress |= (rawCharData[2] << 8);

		sourceAddress = rawCharData[3];
		sourceAddress |= (rawCharData[4] << 8);

		protocolVersion = rawCharData[5];

		command = rawCharData[6];
		command |= (rawCharData[7] << 8);

		// Number of PayloadFrames
		int payloadCount = rawCharData[8];

		// Verify checksum of Header
		if (ResolVBUSUtility.calcChecksum(rawCharData, 1, 8) != rawCharData[9]) {
			errorFree = false;
			return;
		}

		payloadChar = new char[4 * payloadCount];
		payloadByte = new byte[4 * payloadCount];
		
		if (rawCharData.length < (15 + 6 * (payloadCount - 1))) {
			errorFree = false;
			return;
		}

		
		// Verify checksum of payLoad Frames
		for (int i = 0; i < payloadCount; i++) {
			int offset = 10 + i * 6;
			
			// Verify checksum
		
			if (ResolVBUSUtility.calcChecksum(rawCharData, offset, 5) != rawCharData[15 + i * 6]) {
				logger.debug("Error in: "+i);
				logger.debug("ChecksumC:"+(int) ResolVBUSUtility.calcChecksum(rawCharData, offset, 5));
				logger.debug("Checksum:"+(int) rawCharData[15 + i * 6]);
//				logger.debug(VBusUtility.bytesToHex(rawData));
				errorFree = false;
			}

			// Insert Septett and copy to payLoadData
			ResolVBUSUtility.VBus_InjectSeptett(rawCharData, offset, 4);
			payloadChar[i * 4] = rawCharData[offset];
			payloadChar[i * 4 + 1] = rawCharData[offset + 1];
			payloadChar[i * 4 + 2] = rawCharData[offset + 2];
			payloadChar[i * 4 + 3] = rawCharData[offset + 3];
			
			// Insert Septett and copy to payLoadData
			ResolVBUSUtility.VBus_InjectSeptett(rawByteData, offset, 4);
			payloadByte[i * 4] = rawByteData[offset];
			payloadByte[i * 4 + 1] = rawByteData[offset + 1];
			payloadByte[i * 4 + 2] = rawByteData[offset + 2];
			payloadByte[i * 4 + 3] = rawByteData[offset + 3];
			
		}
		errorFree = true;

	}

	public byte[] getPayloadByte() {
		return payloadByte;
	}

	public void setPayloadByte(byte[] payloadByte) {
		this.payloadByte = payloadByte;
	}

	private boolean checkMSB() {
		for (int i = 1; i < rawCharData.length; i++) {
			if ((rawCharData[i] & 0x80) ==  0x80) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return ResolVBUSUtility.bytesToHexFormatted(rawByteData);
	}


}
