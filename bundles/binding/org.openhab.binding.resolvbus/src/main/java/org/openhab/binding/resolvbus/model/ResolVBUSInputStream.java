/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.model;

/**
 * @author Michael Heckmann
 * @since 1.7.0
 */

import java.util.List;

import org.openhab.binding.resolvbus.internal.ResolVBUSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResolVBUSInputStream {

	private static final Logger logger = 
			LoggerFactory.getLogger(ResolVBUSInputStream.class);
	
	private String sourceAddress;
	private String destinationAdress;
	private int protocolVersion;
	private boolean errorFree;
	private int command;
	private byte[] rawByteData;
	private byte[] payloadByte;

	public String getDestinationAdress() {
		return destinationAdress;
	}

	public void setDestinationAdress(String destinationAdress) {
		this.destinationAdress = destinationAdress;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
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

	public ResolVBUSInputStream(List<Byte> rawData) {
		int rawDataSize = rawData.size();
		this.rawByteData = new byte [rawDataSize];
		for (int i = 0; i< rawDataSize;i++) {
			this.rawByteData[i] = rawData.remove(0);
		}
		processPacket();
	}

	private void processPacket() {

		if (rawByteData.length < 9) {
			logger.debug("Stream to short");
			return;
		}
		
		if (checkMSB()) {
			logger.debug("MSB is set => Stream has errors");
			errorFree = false;
			return;
		}

		// SyncFlag?
		if (rawByteData[0] != (byte) 0xAA) {
			logger.debug("No Sync Flag");
			errorFree = false;
			return;
		}

		byte [] destAddress = {rawByteData[2],rawByteData[1]};
		destinationAdress = ResolVBUSUtility.bytesToHex(destAddress);
//		destinationAdress = rawByteData[1];
//		destinationAdress |= (rawByteData[2] << 8);

		byte [] srcAddress= {rawByteData[4],rawByteData[3]};
		sourceAddress = ResolVBUSUtility.bytesToHex(srcAddress);
//		sourceAddress = rawByteData[3];
//		sourceAddress |= (rawByteData[4] << 8);

		protocolVersion = rawByteData[5];

		command = rawByteData[6];
		command |= (rawByteData[7] << 8);

		// Number of PayloadFrames
		int payloadCount = rawByteData[8];

		// Verify checksum of Header
		if (ResolVBUSUtility.calcChecksum(rawByteData, 1, 8) != rawByteData[9]) {
			logger.debug("Header Checksum failure");
			errorFree = false;
			return;
		}

		payloadByte = new byte[4 * payloadCount];
		
		if (rawByteData.length < (15 + 6 * (payloadCount - 1))) {
			errorFree = false;
			return;
		}

		
		// Verify checksum of payLoad Frames
		for (int i = 0; i < payloadCount; i++) {
			int offset = 10 + i * 6;
			
			// Verify checksum
		
			if (ResolVBUSUtility.calcChecksum(rawByteData, offset, 5) != rawByteData[15 + i * 6]) {
				logger.debug("Payload Error in: "+i);
				logger.debug("Payload ChecksumC:"+(int) ResolVBUSUtility.calcChecksum(rawByteData, offset, 5));
				logger.debug("Payload Checksum:"+(int) rawByteData[15 + i * 6]);
//				logger.debug(VBusUtility.bytesToHex(rawData));
				errorFree = false;
			}

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
		for (int i = 1; i < rawByteData.length; i++) {
			if ((rawByteData[i] & 0x80) ==  0x80) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return ResolVBUSUtility.bytesToHexFormatted(rawByteData);
	}


}
