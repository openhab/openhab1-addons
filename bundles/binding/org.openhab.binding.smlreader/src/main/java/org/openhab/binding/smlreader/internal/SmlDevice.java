/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openhab.binding.smlreader.connectors.ISmlConnector;
import org.openhab.binding.smlreader.connectors.SerialConnector;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_GetListRes;
import org.openmuc.jsml.structures.SML_List;
import org.openmuc.jsml.structures.SML_ListEntry;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a SML capable device.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public final class SmlDevice {
	/**
	* Static factory method to create a SmlDevice object with a serial connector member.
	*
	* @param deviceId the id of the device as defined in openHAB configuration.
	* @param pullRequestRequired identicates if SML values have to be actively requested.
	* @param serialPort the port where the device is connected as defined in openHAB configuration.
	*/
	public static SmlDevice createInstance(String deviceId, String serialPort) {
		SmlDevice device = new SmlDevice(deviceId, serialPort);
		logger.debug("Created SmlDevice instance {} with serial connector on port {}", deviceId, serialPort);

		return device;
	}

	protected static final Logger logger = LoggerFactory.getLogger(SmlDevice.class);

	/**
	* Used to establish the device connection
	*/
	private ISmlConnector connector;

	/**
	* The id of the SML device from openHAB configuration.
	*/
	private String deviceId;
	
	/**
	* Controls wether the device info is logged to the OSGi console.
	*/
	private boolean printMeterInfo;
	
	/**
	* Map of all values captured from the device during the read request.
	*/
	private HashMap<String, SmlValue> valueCache;
	
	/**
	* Basic constructor
	*
	* @param deviceId the id of the device as defined in openHAB configuration.
	*/
	private SmlDevice(String deviceId) {
		this.deviceId = deviceId;
		this.connector = null;
		this.printMeterInfo = true;
		this.valueCache = new HashMap<String, SmlValue>();
	}

	/**
	* Contructor to create a SmlDevice object with a serial connector member.
	*
	* @param deviceId the id of the device as defined in openHAB configuration.
	* @param serialPort the port where the device is connected as defined in openHAB configuration.
	*/
	private SmlDevice(String deviceId, String serialPort) {
		this(deviceId);
		this.connector = new SerialConnector(serialPort);
	}

	/**
	* Logs the object information with all given SML values to OSGi console. 
	*
	* It's only called once - except the config was updated.
	*/
	private void printInfo(){
		if(this.getPrintMeterInfo()){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(this.toString());
			stringBuilder.append(System.lineSeparator());
			
			for(Entry<String, SmlValue> entry : valueCache.entrySet()){
				stringBuilder.append("Obis: " + entry.getKey() + " " + entry.getValue().toString());
				stringBuilder.append(System.lineSeparator());
			}
			
			logger.info(stringBuilder.toString());
			setPrintMeterInfo(false);		
		}		
	}	
	
	/**
	* Gets if the object information has to be logged to OSGi console.
	*
	* @return true if the object information should be logged, otherwise false.
	*/
	private Boolean getPrintMeterInfo(){
		return this.printMeterInfo;
	}

	/**
	* Sets if the object information has to be logged to OSGi console.
	*/
	private void setPrintMeterInfo(Boolean printMeterInfo){
		this.printMeterInfo = printMeterInfo;	
	}

	/**
	 * Deletes all cached values.
	 *
	 * The method will always be called before new values are populated.
	 */
	private void clearValueCache(){
		valueCache.clear();
	}
	
	/**
	* Converts hex encoded OBIS to formatted string.
	*
	* @return the hex encoded OBIS code as readable string.
	*/
	private String getObisAsString(byte[] octetBytes) {
		String formattedObis = String.format(SmlReaderConstants.OBIS_FORMAT, 
			byteToInt(octetBytes[0]),
			byteToInt(octetBytes[1]),
			byteToInt(octetBytes[2]),
			byteToInt(octetBytes[3]),
			byteToInt(octetBytes[4]));
		
		return formattedObis;
	}
	
	/**
	* Byte to Integer conversion.
	*
	* @param byte to convert to Integer.
	*/
	private int byteToInt(byte b){
		return Integer.parseInt(String.format("%02x", b), 16);
	}

	/**
	* Decodes native SML informations from the device and stores them locally until the next read request.
	*
	* @param smlFile the native SML informations from the device
	*/
	private void populateValueCache(SML_File smlFile) {
		if(smlFile != null){
			List<SML_Message> smlMessages = smlFile.getMessages();
			
			if(smlMessages != null) {
				int messageCount = smlMessages.size();

				if(messageCount <= 0) {
					logger.warn("{}: no valid SML messages list retrieved.", this.toString());
				}	
					
				for (int i = 0; i < messageCount; i++) {
					SML_Message sml_message = smlMessages.get(i);
					
					if(sml_message == null) {
						logger.warn("{}: no valid SML message.", this.toString());
						continue;
					}				

					int tag = sml_message.getMessageBody().getTag().getVal();
					
					if(tag != SML_MessageBody.GetListResponse) {
						continue;
					}
					
					SML_GetListRes listResponse = (SML_GetListRes)sml_message.getMessageBody().getChoice();
					SML_List smlValueList = listResponse.getValList();
					SML_ListEntry[] smlListEntries = smlValueList.getValListEntry();

					for (SML_ListEntry entry : smlListEntries) {
						String obis = getObisAsString(entry.getObjName().getOctetString());
						
						SmlValue smlValue = valueCache.get(obis);
						
						if(smlValue == null){
							smlValue = new SmlValue(entry);	
						}

						valueCache.put(obis, smlValue);
					}
				}
				
				printInfo();
				
			} else {
				logger.warn("{}: no valid SML messages list retrieved.", this.toString());
			}
		} else {
			logger.warn("{}: no valid SML File.", this.toString());
		}
	}
	
	/**
	 * Gets the configured deviceId.
	 *
	 * @return the id of the SmlDevice from openHAB configuration.
	 */
	public String getDeviceId()	{
		return deviceId;
	}
	
	/**
	 * Returns the specified OBIS value if available.
	 *
	 * @param obis the OBIS code which value should be retrieved.
	 * @return the OBIS value as String if available - otherwise null.
	 */
	public String getValue(String obisId){
		String obisValue = null;
		
		if(valueCache.containsKey(obisId)){
			obisValue = valueCache.get(obisId).getValue();
		}
			
		return obisValue;
	}

	/**
	* Read values from this device an store them locally against their OBIS code. 
	*/
	public void readValues() {
		SML_File smlFile = null;
		
		do {

			clearValueCache();
			
			if(connector == null){
				logger.error("{}: connector is not instantiated", this.toString());
				break;
			}
			
			try {
				smlFile = connector.getMeterValues();
			} catch(Exception ex) {
				logger.error("{}: Error during receive values from device: {}", this.toString(), ex.getMessage());
				break;
			}
						
			populateValueCache(smlFile);
		
		}while (false);
	}

	@Override
	/**
	* Returns the name of the configured device. 
	*/
	public String toString() { 
		return this.getDeviceId();
	}
	
}
