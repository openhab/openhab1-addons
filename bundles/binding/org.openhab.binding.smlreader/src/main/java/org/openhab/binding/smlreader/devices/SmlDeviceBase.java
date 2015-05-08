/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.devices;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openhab.binding.smlreader.conversion.IUnitConverter;
import org.openhab.binding.smlreader.internal.SmlReaderConstants;
import org.openhab.binding.smlreader.internal.SmlValue;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_GetListRes;
import org.openmuc.jsml.structures.SML_List;
import org.openmuc.jsml.structures.SML_ListEntry;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a basic SML capable device.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
abstract class SmlDeviceBase implements ISmlDevice {
	
	protected static final Logger logger = LoggerFactory.getLogger(SmlDeviceBase.class);

	/**
	* The id of the SML device from openHAB configuration.
	*/
	private String deviceId;
	
	/**
	* Controls wether the device info is logged to the osgi console.
	*/
	private boolean printMeterInfo;
	
	/**
	* Map of all values captured from the device during the read request.
	*/
	private HashMap<String, SmlValue> obisValues;
	
	/**
	* Constructor
	*/
	SmlDeviceBase(String id){
		this.printMeterInfo = true;
		this.deviceId = id;
		this.obisValues = new HashMap<String, SmlValue>();
	}	
	
	/**
	* Logs the object information with all given SML values. Is only called once except the config was updated.
	*/
	private void printInfo(){
		if(this.getPrintMeterInfo()){
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(this.toString());
			stringBuilder.append(System.lineSeparator());
			
			for(Entry<String, SmlValue> entry : obisValues.entrySet()){
				stringBuilder.append("Obis: '" + entry.getKey() + "': Value: '" + entry.getValue().toString() + "'");
				stringBuilder.append(System.lineSeparator());
			}
			
			logger.info(stringBuilder.toString());
			setPrintMeterInfo(false);		
		}		
	}	
	
	/**
	* Gets if the object information should be logged.
	* @return if the object information should be logged.
	*/
	private Boolean getPrintMeterInfo(){
		return this.printMeterInfo;
	}

	/**
	* Sets if the object information has to be logged.
	*/
	private void setPrintMeterInfo(Boolean printMeterInfo){
		this.printMeterInfo = printMeterInfo;	
	}

	/**
	 * Deletes all values.
	 */
	private void clearValues(){
		obisValues.clear();
	}

	/**
	* Open the connection against the given device.
	*/
	abstract protected boolean openConnection();
	
	/**
	* Close the connection against the given device.
	*/
	abstract protected void closeConnection();
	
	/**
	* Get the SML information from the given device.
	*/
	abstract protected SML_File getSmlFile();
	
	/**
	* Converts hex encoded OBIS to formatted string.
	* @return formatted OBIS code.
	*/
	protected String extractObisCodeFormatted(byte[] octetBytes) {
		String formattedObis = String.format(SmlReaderConstants.Configuration.CONFIGURATION_OBIS_FORMAT, 
			byteToInt(octetBytes[0]),
			byteToInt(octetBytes[1]),
			byteToInt(octetBytes[2]),
			byteToInt(octetBytes[3]),
			byteToInt(octetBytes[4]));
		
		return formattedObis;
	}
	
	/**
	* Byte to Integer conversion.
	* @param byte to convert to Integer.
	*/
	protected int byteToInt(byte b){
		return Integer.parseInt(String.format("%02x", b), 16);
	}

	/**
	 * Gets the configured id.
	 * @return the id of the SmlDevice from openHAB configuration.
	 */
	public String getDeviceId()	{
		return deviceId;
	}
	
	/**
	 * Returns the specified OBIS value if available.
	 * @param obis the OBIS code which value should be retrieved.
	 * @param unitConverterType to convert the value.
	 * @return the OBIS value as String if available - otherwise null.
	 */
	@SuppressWarnings("rawtypes")
	@ Override
	public String getObisValue(String obis, Class<? extends IUnitConverter> unitConverterType){
		String obisValue = null;
		
		if(obisValues.containsKey(obis)){
			String valueFromDevice = obisValues.get(obis).getValue();
			
			if(unitConverterType != null){
				IUnitConverter<?> converterObject;
				try {
					converterObject = unitConverterType.getConstructor().newInstance();
					obisValue = converterObject.convert(valueFromDevice).toString();
				} catch(NullPointerException | NumberFormatException e){
					logger.warn(e.getMessage() + " - the original value is returned!");
					obisValue = valueFromDevice;
				}catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e1) {
					logger.error("Not able to create converter instance " + unitConverterType.getClass());
					logger.error(e1.getMessage());
				} 				
			
			} else{
				obisValue = valueFromDevice;
			}
		}		
		
		return obisValue;
	}

	/**
	* Read values from this device an store them locally against their obis code. 
	*/
	public void readSmlValuesFromDevice() {
		clearValues();
		
		do{
			try{
				if(!openConnection())
					break;
				
				SML_File smlFile = getSmlFile();
				
				if(smlFile == null)
					break;

				List<SML_Message> smlMessages = smlFile.getMessages();

				for (int i = 0; i < smlMessages.size(); i++) {
					SML_Message sml_message = smlMessages.get(i);

					int tag = sml_message.getMessageBody().getTag().getVal();

					switch (tag) {
						case SML_MessageBody.GetListResponse:
							SML_GetListRes resp = (SML_GetListRes)sml_message.getMessageBody().getChoice();
							SML_List smlList = resp.getValList();
							SML_ListEntry[] list = smlList.getValListEntry();

							for (SML_ListEntry entry : list) {
								String obis = extractObisCodeFormatted(entry.getObjName().getOctetString());
								
								SmlValue smlValue = obisValues.get(obis);
								
								if(smlValue == null){
									smlValue = new SmlValue(entry);	
								}

								obisValues.put(obis, smlValue);
							}
							break;
					}
				}
				
				printInfo();
			} catch (Exception e) {
				logger.error("Unexpected exception occured: " + this.toString());
				logger.error(e.getMessage());
			} finally{
				closeConnection();
			}
		}while(false);
	}
}