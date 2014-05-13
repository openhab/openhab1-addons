/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.MaxCubeBinding;
import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The C message contains configuration about a MAX! device.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public final class C_Message extends Message {

	private static final Logger logger = LoggerFactory.getLogger(MaxCubeBinding.class);

	private String rfAddress = null;
	private int length = 0;
	private DeviceType deviceType = null;
	private String serialNumber = null;

	private String TempComfort= null;
	private String TempEco = null;
	private String TempSetpointMax= null;
	private String TempSetpointMin= null;
	private String TempOffset = null;
	private String TempOpenWindow = null;
	private String DurationOpenWindow = null;
	private String Decalcification = null;
	private String ValveMaximum = null;
	private String ValveOffset = null;
	private String ProgramData = null;
	private String BoostDuration = null;
	private String BoostValve = null;

	
	public C_Message(String raw) {
		super(raw);
		logger.debug(" *** C-Message ***");
		String[] tokens = this.getPayload().split(Message.DELIMETER);

		rfAddress = tokens[0];

		byte[] bytes = Base64.decodeBase64(tokens[1].getBytes());


		int[] data = new int[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			data[i] = bytes[i] & 0xFF;
		}

		length = data[0];
		if (length != data.length - 1) {
			logger.debug("C_Message malformed: wrong data length. Expected bytes {}, actual bytes {}", length, data.length - 1);
		}

		String rfAddress2 = Utils.toHex(data[1], data[2], data[3]);
		if (!rfAddress.toUpperCase().equals(rfAddress2.toUpperCase())) {
			logger.debug("C_Message malformed: wrong RF address. Expected address {}, actual address {}", rfAddress.toUpperCase(), rfAddress2.toUpperCase());
		}

		deviceType = DeviceType.create(data[4]);

		serialNumber = getSerialNumber(bytes);
		//logger.trace("Data:" + ParseData (bytes));
		if (deviceType == DeviceType.HeatingThermostatPlus || deviceType == DeviceType.HeatingThermostat)  ParseHeatingThermostatPlusData (bytes);
		//if (deviceType.getValue() != 2) logger.debug("Data:" + ParseData (bytes));
		logger.debug("");
	}

	private String getSerialNumber(byte[] bytes) {
		byte[] sn = new byte[10];

		for (int i = 0; i < 10; i++) {
			sn[i] = (byte) bytes[i + 8];
		}

		try {
			return new String(sn, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("Cannot encode serial number from C message due to encoding issues.");
		}

		return "";
	}

	private String ParseData(byte[] bytes) {
		int DataStart = 18;
		byte[] sn = new byte[bytes.length - DataStart];

		for (int i = 0; i < sn.length; i++) {
			sn[i] = (byte) bytes[i + DataStart];
		}
		logger.debug("DataBytes: " + Utils.getHex(sn));
		try {
			return new String(sn, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("Cannot encode device string from C message due to encoding issues.");
		}

		return "";
	}

	private void ParseHeatingThermostatPlusData(byte[] bytes) {
		try{
			int PlusDataStart = 18;
			TempComfort= Float.toString( bytes[PlusDataStart ]/2);
			TempEco = Float.toString( bytes[PlusDataStart + 1]/2);
			TempSetpointMax=  Float.toString( bytes[PlusDataStart + 2]/2);
			TempSetpointMin=  Float.toString( bytes[PlusDataStart + 3]/2);
			TempOffset =  Double.toString( (bytes[PlusDataStart +4 ]/2) - 3.5);
			TempOpenWindow =  Float.toString( bytes[PlusDataStart + 5]/2);
			DurationOpenWindow =  Float.toString( bytes[PlusDataStart + 6]);
			BoostDuration =  Float.toString( bytes[PlusDataStart + 7]&0xFF >> 5 );
			BoostValve =  Float.toString( (bytes[PlusDataStart + 7]&0x1F)*5);
			Decalcification =  Float.toString( bytes[PlusDataStart + 8]);
			ValveMaximum = Float.toString( bytes[PlusDataStart + 9]&0xFF * 100 / 255);
			ValveOffset = Float.toString( bytes[PlusDataStart+ 10]&0xFF * 100 / 255 );

			ProgramData = "";
			int ln = 13 * 6; //first day = Sat 
			String StartTime = "00:00h";
			for (int char_idx = PlusDataStart + 11; char_idx < bytes.length; char_idx++) {
				if (ln % 13 == 0 ) { ProgramData += "\r\n Day " + Integer.toString((ln / 13) % 7 ) + ": "; StartTime = "00:00h"; }
				//ProgramData += Double.toString(bytes[char_idx] /4) + "C till ";
				int ptime =  (bytes[char_idx+1]&0xFF ) * 5 + (bytes[char_idx]&0x01 ) * 1280 ;
				int pm = ptime % 60;
				int ph = (ptime - pm ) / 60;
				String EndTime = Integer.toString(ph) + ":" + String.format("%02d", pm) + "h";
				ProgramData += StartTime + "-" + EndTime + " " + Double.toString(bytes[char_idx] /4) + "C  ";
				StartTime = EndTime;
				char_idx++;
				ln++;
			}

			logger.debug("Temp Comfort:         " + TempComfort);
			logger.debug("TempEco:              " + TempEco);
			logger.debug("Temp Setpoint Max:    " + TempSetpointMax);
			logger.debug("Temp Setpoint Min:    " + TempSetpointMin);
			logger.debug("Temp Offset:          " + TempOffset);
			logger.debug("Temp Open Window:     " + TempOpenWindow );
			logger.debug("Duration Open Window: " + DurationOpenWindow  );
			logger.debug("Duration Boost:       " + BoostDuration );
			logger.debug("Boost Valve Pos:      " + BoostValve);
			logger.debug("Decalcification:      " + Decalcification);
			logger.debug("ValveMaximum:         " + ValveMaximum );
			logger.debug("ValveOffset:          " + ValveOffset );
			logger.debug("ProgramData:          " + ProgramData);
		}  catch (Exception e) {
			logger.debug(e.getMessage());
			logger.debug(Utils.getStackTrace(e));
		}
		return ;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	@Override
	public MessageType getType() {
		return MessageType.C;
	}

	public String getRFAddress() {
		return rfAddress;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	@Override
	public void debug(Logger logger) {
		logger.debug("=== C_Message === ");
		logger.trace("\tRAW:        " + this.getPayload());
		logger.debug("DeviceType:   " + deviceType.toString());
		logger.debug("SerialNumber: " + serialNumber);
		logger.debug("RFAddress:    " + rfAddress);
	}
}
