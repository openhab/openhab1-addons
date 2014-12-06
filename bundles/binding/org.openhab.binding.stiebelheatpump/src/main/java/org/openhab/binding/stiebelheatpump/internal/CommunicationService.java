/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.openhab.binding.stiebelheatpump.internal;

import java.util.*;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.openhab.binding.stiebelheatpump.protocol.DataParser;
import org.openhab.binding.stiebelheatpump.protocol.ProtocolConnector;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition.Type;
import org.openhab.binding.stiebelheatpump.protocol.Request;
import org.openhab.binding.stiebelheatpump.protocol.SerialConnector;

public class CommunicationService {

	private static ProtocolConnector connector;
	private String serialPortName;
	private static final int MAXRETRIES = 1000;
	private final int INPUT_BUFFER_LENGTH = 1024;
	private byte buffer[] = new byte[INPUT_BUFFER_LENGTH];

	private int WAITING_TIME_BETWEEN_REQUESTS = 2000;

	/** heat pump request definition */
	private List<Request> heatPumpConfiguration = new ArrayList<Request>();
	private List<Request> heatPumpSensorConfiguration = new ArrayList<Request>();
	private List<Request> heatPumpSettingConfiguration = new ArrayList<Request>();
	private List<Request> heatPumpStatusConfiguration = new ArrayList<Request>();
	Request versionRequest;
	DataParser parser = new DataParser();

	private static final Logger logger = LoggerFactory
			.getLogger(CommunicationService.class);

	public CommunicationService() {
	}

	public CommunicationService(String serialPortName, int baudRate)
			throws StiebelHeatPumpException {
		this.serialPortName = serialPortName;
		connector = getStiebelHeatPumpConnector();
		connector.connect(serialPortName, baudRate);
		return;
	}

	public CommunicationService(String serialPortName, int baudRate,
			List<Request> configuration) throws StiebelHeatPumpException {
		this.serialPortName = serialPortName;
		connector = getStiebelHeatPumpConnector();
		connector.connect(serialPortName, baudRate);
		heatPumpConfiguration = configuration;
		return;
	}

	public void finalizer() {
		logger.info("Disconnecting heat pump.");
		connector.disconnect();
		logger.info("Heat pump disconnected.");
	}

	/**
	 * This method reads the version information from the heat pump
	 * 
	 * @return version string, e.g: 2.06
	 */
	public String getversion() throws StiebelHeatPumpException {
		String version = "";
		try {
			Map<String, String> data = readData(versionRequest);
			version = (String) data.get("Version");
			Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);
		} catch (InterruptedException e) {
			throw new StiebelHeatPumpException(e.toString());
		}
		return version;
	}

	/**
	 * This method reads all settings defined in the heat pump configuration
	 * from the heat pump
	 * 
	 * @return map of heat pump setting values
	 */
	public Map<String, String> getSettings() throws StiebelHeatPumpException {
		logger.info("Loading Settings");
		Map<String, String> data = new HashMap<String, String>();
		for (Request request : heatPumpSettingConfiguration) {
			logger.info("Loading data for request {} ...", request.getName());
			try {
				Map<String, String> newData = readData(request);
				data.putAll(newData);
				Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);
			} catch (InterruptedException e) {
				throw new StiebelHeatPumpException(e.toString());
			}
		}
		return data;
	}

	/**
	 * This method reads all sensor values defined in the heat pump
	 * configuration from the heat pump
	 * 
	 * @return map of heat pump sensor values
	 */
	public Map<String, String> getSensors() throws StiebelHeatPumpException {
		logger.info("Loading Sensors");
		Map<String, String> data = new HashMap<String, String>();
		for (Request request : heatPumpSensorConfiguration) {
			logger.info("Loading data for request {} ...", request.getName());
			try {
				Map<String, String> newData = readData(request);
				data.putAll(newData);
				Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);
			} catch (InterruptedException e) {
				throw new StiebelHeatPumpException(e.toString());
			}
		}
		return data;
	}

	/**
	 * This method reads all status values defined in the heat pump
	 * configuration from the heat pump
	 * 
	 * @return map of heat pump status values
	 */
	public Map<String, String> getStatus() throws StiebelHeatPumpException {
		logger.info("Loading Status");
		Map<String, String> data = new HashMap<String, String>();
		for (Request request : heatPumpStatusConfiguration) {
			logger.info("Loading data for request {} ...", request.getName());
			try {
				Map<String, String> newData = readData(request);
				data.putAll(newData);
				Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);
			} catch (InterruptedException e) {
				throw new StiebelHeatPumpException(e.toString());
			}
		}
		return data;
	}

	/**
	 * This method set the time of the heat pump to the current time
	 * 
	 * @return true if time has been updated
	 */
	public Map<String, String> setTime() throws StiebelHeatPumpException {

		startCommunication();
		Map<String, String> data = new HashMap<String, String>();

		for (Request request : heatPumpSettingConfiguration) {
			if (request.getName().equalsIgnoreCase("Time")) {
				logger.debug("Loading current time data ...");
				try {
					DateTime dt = DateTime.now();
					logger.debug("Current time is : {}", dt.toString());
					String weekday = Integer.toString(dt.getDayOfWeek() - 1);
					String day = Integer.toString(dt.getDayOfMonth());
					String month = Integer.toString(dt.getMonthOfYear());
					String year = Integer.toString(dt.getYearOfCentury());
					String seconds = Integer.toString(dt.getSecondOfMinute());
					String hours = Integer.toString(dt.getHourOfDay());
					String minutes = Integer.toString(dt.getMinuteOfHour());

					byte[] requestMessage = createRequestMessage(request);
					byte[] response = getData(requestMessage);
					data = parser.parseRecords(response, request);
					Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);

					boolean updateRequired = false;
					for (Map.Entry<String, String> entry : data.entrySet()) {
						String entryName = entry.getKey();
						String entryValue = entry.getValue();
						RecordDefinition currentRecord = null;

						for (RecordDefinition record : request
								.getRecordDefinitions()) {
							if (record.getName().equalsIgnoreCase(entryName)) {
								currentRecord = record;
								break;
							}
						}
						if (entryName.equals("WeekDay")
								&& !entryValue.equalsIgnoreCase(weekday)) {
							updateRequired = true;
							response = parser.composeRecord(weekday, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Hours")
								&& !entryValue.equalsIgnoreCase(hours)) {
							updateRequired = true;
							response = parser.composeRecord(hours, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Minutes")
								&& !entryValue.equalsIgnoreCase(minutes)) {
							updateRequired = true;
							response = parser.composeRecord(minutes, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Seconds")
								&& !entryValue.equalsIgnoreCase(seconds)) {
							updateRequired = true;
							response = parser.composeRecord(seconds, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Year")
								&& !entryValue.equalsIgnoreCase(year)) {
							updateRequired = true;
							response = parser.composeRecord(year, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Month")
								&& !entryValue.equalsIgnoreCase(month)) {
							updateRequired = true;
							response = parser.composeRecord(month, response,
									currentRecord);
							continue;
						}
						if (entryName.equals("Day")
								&& !entryValue.equalsIgnoreCase(day)) {
							updateRequired = true;
							response = parser.composeRecord(day, response,
									currentRecord);
							continue;
						}
					}

					if (updateRequired) {
						logger.info("Time need update");
						setData(response);
					}
					return data;

				} catch (InterruptedException e) {
					throw new StiebelHeatPumpException(e.toString());
				}
			}
		}
		return data;
	}

	/**
	 * This method looks up all files in resource and List of Request objects
	 * into xml file
	 * 
	 * @return true if heat pump configuration for version could be found and
	 *         loaded
	 */
	public List<Request> getHeatPumpConfiguration(String configFile) {
		ConfigLocator configLocator = new ConfigLocator(configFile);
		heatPumpConfiguration = configLocator.getConfig();

		if (heatPumpConfiguration != null && !heatPumpConfiguration.isEmpty()) {
			logger.info("Loaded heat pump configuration {} .", configFile);
			logger.info("Configuration file contains {} requests.",
					heatPumpConfiguration.size());

			logger.debug("Loading heat pump configuration ...");

			for (Request request : heatPumpConfiguration) {
				logger.debug(
						"Request : Name -> {}, Description -> {} , RequestByte -> {}",
						request.getName(), request.getDescription(),
						DatatypeConverter.printHexBinary(new byte[] { request
								.getRequestByte() }));
				if (request.getName().equalsIgnoreCase("Version")) {
					versionRequest = request;
					logger.debug("Loaded Request : "
							+ versionRequest.getDescription());
					continue;
				}

				for (RecordDefinition record : request.getRecordDefinitions()) {
					if (record.getDataType() == Type.Settings
							&& !heatPumpSettingConfiguration.contains(request)) {
						heatPumpSettingConfiguration.add(request);
					}
					if (record.getDataType() == Type.Status
							&& !heatPumpStatusConfiguration.contains(request)) {
						heatPumpStatusConfiguration.add(request);
					}
					if (record.getDataType() == Type.Sensor
							&& !heatPumpSensorConfiguration.contains(request)) {
						heatPumpSensorConfiguration.add(request);
					}
				}
			}

			if (versionRequest == null) {
				logger.debug("version request could not be found in configuration");
				return heatPumpConfiguration;
			}
			return heatPumpConfiguration;
		}
		logger.warn("Could not load heat pump configuration file for {}!",
				configFile);
		return null;
	}

	/**
	 * This method reads all values defined in the request from the heat pump
	 * 
	 * @param request
	 *            definition to load the values from
	 * @return map of heat pump values according request definition
	 */
	public Map<String, String> readData(Request request)
			throws StiebelHeatPumpException {
		Map<String, String> data = new HashMap<String, String>();
		logger.debug(
				"Request : Name -> {}, Description -> {} , RequestByte -> {}",
				request.getName(), request.getDescription(),
				DatatypeConverter.printHexBinary(new byte[] { request
						.getRequestByte() }));
		startCommunication();
		byte responseAvailable[] = new byte[0];
		byte requestMessage[] = createRequestMessage(request);
		boolean validData = false;
		try {
			while (!validData) {
				responseAvailable = getData(requestMessage);
				responseAvailable = parser
						.fixDuplicatedBytes(responseAvailable);
				validData = parser.headerCheck(responseAvailable);
				if (validData) {
					data = parser.parseRecords(responseAvailable, request);
					continue;
				}
				Thread.sleep(WAITING_TIME_BETWEEN_REQUESTS);
				startCommunication();
			}
		} catch (StiebelHeatPumpException e) {
			logger.error("Error reading data : {}", e.toString());
		} catch (InterruptedException e) {
		}
		return data;
	}

	/**
	 * This method updates the parameter item of a heat pump request
	 * 
	 * @param value
	 *            the new value of the item
	 * @param parameter
	 *            to be update in the heat pump
	 */
	public Map<String, String> setData(String value, String parameter)
			throws StiebelHeatPumpException {
		Request updateRequest = null;
		RecordDefinition updateRecord = null;
		Map<String, String> data = new HashMap<String, String>();

		// we lookup the right request definition that contains the parameter to
		// be updated
		if (parameter != null) {
			for (Request request : heatPumpSettingConfiguration) {
				for (RecordDefinition record : request.getRecordDefinitions()) {
					if (record.getName().equalsIgnoreCase(parameter)) {
						updateRecord = record;
						updateRequest = request;

						logger.debug(
								"Found valid record definition {} in request {}:{}",
								record.getName(), request.getName(),
								request.getDescription());
						break;
					}
				}
			}
		}

		if (updateRecord == null || updateRequest == null) {
			// did not find any valid record, do nothing
			logger.warn("Could not find valid record definition for {}",
					parameter);
			return data;
		}

		logger.debug("Setting new value [{}] for parameter [{}]", value,
				parameter);

		try {
			// get actual value for the corresponding request
			// as we do no have individual requests for each settings we need to
			// decode the new value
			// into a current response , the response is available in the
			// connector object
			byte[] requestMessage = createRequestMessage(updateRequest);
			byte[] response = getData(requestMessage);
			data = parser.parseRecords(response, updateRequest);

			// lookup parameter value in the data
			String currentState = data.get(updateRecord.getName());
			if (currentState.equals(value)) {
				// current State is already same as new values!
				return data;
			}

			// create new set request out from the existing read response
			byte[] requestUpdateMessage = parser.composeRecord(value, response,
					updateRecord);
			response = setData(requestUpdateMessage);

			if (parser.setDataCheck(response)) {
				logger.debug("Updated parmeter {} sucessfully.", parameter);
			}

		} catch (StiebelHeatPumpException e) {
			logger.error("Stiebel heat pump communication error during update of value! "
					+ e.toString());
		} finally {
		}
		return data;
	}

	/**
	 * Gets data from connected heat pump
	 * 
	 * @param request
	 *            request bytes to send to heat pump
	 * @return response bytes from heat pump
	 * 
	 *         General overview of handshake between application and serial
	 *         interface of heat pump 1. Sending request bytes , e.g.: 01 00 FD
	 *         FC 10 03 for version request 01 -> header start 00 -> get request
	 *         FD -> checksum of request FC -> request byte 10 03 -> Footer
	 *         ending the communication 2. Receive a data available 10 -> ok 02
	 *         -> it does have data,which wants to send now 3. acknowledge
	 *         sending data 10 -> ok 4. receive data until footer 01 -> header
	 *         start 00 -> get request CC -> checksum of send data FD -> request
	 *         byte 00 CE -> data , e.g. short value as 2 bytes -> 206 -> 2.06
	 *         version 10 03 -> Footer ending the communication
	 */
	private byte[] getData(byte request[]) {
		if (!establishRequest(request)) {
			return new byte[0];
		}
		try {
			connector.write(DataParser.ESCAPE);
			byte[] response = receiveData();
			return response;
		} catch (Exception e) {
			logger.error("Could not get data from heat pump! {}", e.toString());
			return buffer;
		}
	}

	/**
	 * Sets data to connected heat pump
	 * 
	 * @param request
	 *            request bytes to send to heat pump
	 * @return response bytes from heat pump
	 * 
	 *         General overview of handshake between application and serial
	 *         interface of heat pump 1. Sending request bytes, e.g update time
	 *         in heat pump 01 -> header start 80 -> set request F1 -> checksum
	 *         of request FC -> request byte 00 02 0a 22 1b 0e 00 03 1a -> new
	 *         values according record definition for time 10 03 -> Footer
	 *         ending the communication 2. Receive response message the
	 *         confirmation message is ready foe sending 10 -> ok 02 -> it does
	 *         have data ,which wants to send now 3. acknowledge sending data 10
	 *         -> ok 4. receive confirmation message until footer 01 -> header
	 *         start 80 -> set request 7D -> checksum of send data FC -> request
	 *         byte 10 03 -> Footer ending the communication
	 */
	private byte[] setData(byte[] request) throws StiebelHeatPumpException {
		try {
			startCommunication();
			establishRequest(request);
			// Acknowledge sending data
			connector.write(DataParser.ESCAPE);

		} catch (Exception e) {
			logger.error("Could not set data to heat pump! {}", e.toString());
			return new byte[0];
		}

		// finally receive data
		return receiveData();
	}

	/**
	 * This method start the communication for the request It send the initial
	 * handshake and expects a response
	 */
	private void startCommunication() throws StiebelHeatPumpException {
		logger.debug("Sending start communication");
		byte response;
		try {
			connector.write(DataParser.STARTCOMMUNICATION);
			response = connector.get();
		} catch (Exception e) {
			logger.error("heat pump communication could not be established !");
			throw new StiebelHeatPumpException(
					"heat pump communication could not be established !");
		}
		if (response != DataParser.ESCAPE) {
			logger.warn("heat pump is communicating, but did not received Escape message in inital handshake!");
			throw new StiebelHeatPumpException(
					"heat pump is communicating, but did not received Escape message in inital handshake!");
		}
	}

	/**
	 * This method establish the connection for the request It send the request
	 * and expects a data available response
	 * 
	 * @param request
	 *            to be send to heat pump
	 * @return true if data are available from heatpump
	 */
	private boolean establishRequest(byte[] request) {
		int numBytesReadTotal = 0;
		boolean dataAvailable = false;
		int requestRetry = 0;
		int retry = 0;
		try {
			while (requestRetry < MAXRETRIES) {
				connector.write(request);
				retry = 0;
				byte singleByte;
				while ((!dataAvailable) & (retry < MAXRETRIES)) {
					try {
						singleByte = connector.get();
					} catch (Exception e) {
						retry++;
						continue;
					}
					buffer[numBytesReadTotal] = singleByte;
					numBytesReadTotal++;
					if (buffer[0] != DataParser.DATAAVAILABLE[0]
							|| buffer[1] != DataParser.DATAAVAILABLE[1]) {
						continue;
					}
					dataAvailable = true;
					return true;
				}
				logger.debug("retry request!");

			}
			if (!dataAvailable) {
				logger.warn("heat pump has no data available for request!");
				return false;
			}
		} catch (Exception e1) {
			logger.error("Could not get data from heat pump! {}", e1.toString());
			return false;
		}
		return true;
	}

	/**
	 * This method receive the response from the heat pump It receive single
	 * bytes until the end of message s detected
	 * 
	 * @return bytes representing the data send from heat pump
	 */
	private byte[] receiveData() {
		byte singleByte;
		int numBytesReadTotal;
		int retry;
		buffer = new byte[INPUT_BUFFER_LENGTH];
		retry = 0;
		numBytesReadTotal = 0;
		boolean endOfMessage = false;

		while (!endOfMessage & retry < MAXRETRIES) {
			try {
				singleByte = connector.get();
			} catch (Exception e) {
				// reconnect and try again to send request
				retry++;
				continue;
			}

			buffer[numBytesReadTotal] = singleByte;
			numBytesReadTotal++;

			if (numBytesReadTotal > 4
					&& buffer[numBytesReadTotal - 2] == DataParser.ESCAPE
					&& buffer[numBytesReadTotal - 1] == DataParser.END) {
				// we have reached the end of the response
				endOfMessage = true;
				logger.debug("reached end of response message.");
				break;
			}
		}

		byte[] responseBuffer = new byte[numBytesReadTotal];
		System.arraycopy(buffer, 0, responseBuffer, 0, numBytesReadTotal);
		return responseBuffer;
	}

	/**
	 * This creates the request message ready to be send to heat pump
	 * 
	 * @param request
	 *            object containing necessary information to build request
	 *            message
	 * @return request message byte[]
	 */
	private byte[] createRequestMessage(Request request) {
		short checkSum;
		byte[] requestMessage = new byte[] { DataParser.HEADERSTART,
				DataParser.GET, (byte) 0x00, request.getRequestByte(),
				DataParser.ESCAPE, DataParser.END };
		try {
			// prepare request message
			checkSum = parser.calculateChecksum(requestMessage);
			requestMessage[2] = parser.shortToByte(checkSum)[0];
			requestMessage = parser.addDuplicatedBytes(requestMessage);
		} catch (StiebelHeatPumpException e) {
		}
		return requestMessage;
	}

	private ProtocolConnector getStiebelHeatPumpConnector() {
		if (connector != null)
			return connector;
		if (serialPortName != null)
			connector = new SerialConnector();
		return connector;
	}
}
